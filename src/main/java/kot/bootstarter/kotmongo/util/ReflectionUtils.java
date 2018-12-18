package kot.bootstarter.kotmongo.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author YangYu
 */
public class ReflectionUtils {

    public static Map<String, Object> beanToMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        final Class<?> clazz = obj.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(obj));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
