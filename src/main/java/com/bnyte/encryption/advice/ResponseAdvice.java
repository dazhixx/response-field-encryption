package com.bnyte.encryption.advice;

import com.bnyte.encryption.bind.EnableFieldEncryption;
import com.bnyte.encryption.bind.EncryptionField;
import com.bnyte.forge.http.reactive.web.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 响应处理器
 *
 * @author bnyte
 * @date 2022/4/14 23:57
 * @mail bnytezz@gmail.com
 */
@RestControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    private static final Logger log = LoggerFactory.getLogger(ResponseAdvice.class);

    /**
     *  过滤需要被beforeBodyWrite处理的方法
     * @param returnType 返回类型对象
     * @param converterType 响应结果转换器
     * @return true则会调用beforeBodyWrite(),false则不会
     */
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 获取当前被执行的控制器方法，并获取该方法是否有
        return Objects.nonNull(Objects.requireNonNull(returnType.getMethod()).getAnnotation(EnableFieldEncryption.class));
    }

    /**
     * 重写响应结果(返回值就是响应结果)
     *  通过supports()方法的校验 可以确定走了这个方法就是需要加密的方法
     * @param body 当前响应结果值
     * @param returnType 响应结果类型
     * @param selectedContentType   选择的内容类型
     * @param selectedConverterType 选择的转换器类型
     * @param request 请求对象
     * @param response 响应对象
     * @return 返回响应结果
     */
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        /*
         校验当前响应结果是否为统一返回结果值
            R这个返回结果值换成你们项目的统一返回结果集对象
         */
        if (Objects.nonNull(body) && body instanceof R) {

            // 获取响应结果中的data值,如果你们不叫data的话可能叫result 具体可以看实体类
            Object data = ((R<?>) body).getData();
            // 响应结果中的data不为空
            if (Objects.nonNull(data)) {
                // 获取值得所有属性并且返回处理好之后的数据
                try {
                    findEncryptedPropertyRequired(data);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }

        }

        return body;
    }

    /**
     * 查找需要加密的属性
     * @param data 统一响应结果集的data对象
     *             比如以下响应结果的话就是data对象，程序是不会处理code he message的
     *              {
     *                  "code": 0,
     *                  "message": "succeeded",
     *                  "data": {
     *                      "username": "ggboy_username",
     *                      "password": "ggboy_password"
     *              }
     * @throws IllegalAccessException 可能抛出权限越级异常
     */
    private void findEncryptedPropertyRequired(Object data) throws IllegalAccessException {
        // 校验data值是否为数组
        if (data instanceof Collection) {
            List<Object> dataValue = new ArrayList<>((Collection<?>) data);
            for (Object o : dataValue) {
                findEncryptedPropertyRequired(o);
            }
        }
        // 已经不是集合了，现在是对象，获取对象中的属性，并且判断是否有注解
        else {
            // 响应对象为空不继续处理
            if (Objects.isNull(data)) return;
            // 获取所有属性
            Field[] declaredFields = data.getClass().getDeclaredFields();
            // 如果响应对象是基本数据类型，不处理
            if (CollectionUtils.isEmpty(CollectionUtils.arrayToList(declaredFields))) return;
            // 循环遍历所有属性, 判断是否有指定注解
            hasChildrenFieldHandler(List.of(declaredFields), data);
        }
    }

    /**
     * 有子字段需要继续处理
     *  条件：
     *      1. 在当前对象不是基础数据类型(通过notBaseDataType()判断)
     *      2. 在对象不是一个集合
     * @param fields 当前对象属性集合比如现在有个User类，这个对象的值就是user里面的所有属性
     * @param obj 当前对象，和上面一样的比喻的话这个参数就是user实例对象
     * @throws IllegalAccessException 可能会反射权限异常
     */
    private void hasChildrenFieldHandler(List<Field> fields, Object obj) throws IllegalAccessException {
        for (Field field : fields) {
            // 给最高操作等级(非常重要)
            field.setAccessible(true);
            Object o = field.get(obj);
            if (Objects.isNull(o)) return;
            // 获取字段的所有属性值
            List<Field> innerFields = List.of(o.getClass().getDeclaredFields());
            // 如果是集合还得继续往下找
            if (o instanceof Collection) {
                findEncryptedPropertyRequired(o);
            }
            else if (!CollectionUtils.isEmpty(innerFields) && notBaseDataType(o)) {
                hasChildrenFieldHandler(innerFields, o);
            }
            // 这里才是真正的需要处理的对象
            else if (Objects.nonNull(field.getAnnotation(EncryptionField.class))) {
                // 判断该属性值是否包含了需要加密注解
                field.set(obj, fieldEncryption(o));
            }
        }
    }

    /**
     * 不是基础数据类型(这个方法很重要一定要补全)
     * 过滤掉不需要继续循环递归找的类型(一般是基础数据类型，包括int long Integer Long)这些 继续&&就可以了一定要全，不然
     * 如果使用下面的代码的话只有String类型和Integer类型会被加密如果是其他类型则直接报错比如char long这些
     * @param o 对象值
     * @return 是否需要过滤掉当前对象
     */
    private boolean notBaseDataType(Object o) {
        return !(o instanceof String) && !(o instanceof Integer) && !(o instanceof Long);
    }

    /**
     * 拿着没加密的值来进行加密，然后将加密的值返回
     * @param obj 没加密的属性值
     * @return 加密后的值
     */
    // TODO: 2022/4/15 加密代码
    private Object fieldEncryption(Object obj) throws IllegalAccessException {
        return "加密了: " + obj;
    }
}
