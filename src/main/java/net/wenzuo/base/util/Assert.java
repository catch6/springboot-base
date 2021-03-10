package net.wenzuo.base.util;

import cn.hutool.core.util.StrUtil;
import net.wenzuo.base.exception.ServiceException;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Collection;

/**
 * @author Catch
 */
public abstract class Assert {

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new ServiceException(message);
        }
    }

    public static void notNull(Object object, String template, Object... values) {
        notNull(object, StrUtil.format(template, values));
    }

    public static void notEmpty(Object[] array, String message) {
        if (ObjectUtils.isEmpty(array)) {
            throw new ServiceException(message);
        }
    }

    public static void notEmpty(Object[] array, String template, Object... values) {
        notEmpty(array, StrUtil.format(template, values));
    }


    public static void notEmpty(@Nullable Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new ServiceException(message);
        }
    }

    public static void notEmpty(@Nullable Collection<?> collection, String template, Object... values) {
        notEmpty(collection, StrUtil.format(template, values));
    }


    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new ServiceException(message);
        }
    }

    public static void isTrue(boolean expression, String template, Object... values) {
        isTrue(expression, StrUtil.format(template, values));
    }

    public static void equals(String expected, String actual, String message) {
        if (objectsNotEqual(expected, actual)) {
            throw new ServiceException(message);
        }
    }

    public static void equals(String expected, String actual, String template, Object... values) {
        equals(expected, actual, StrUtil.format(template, values));
    }


    public static void equalsIgnoreCase(String expected, String actual, String message) {
        if (stringsNotEqual(expected, actual)) {
            throw new ServiceException(message);
        }
    }

    public static void equalsIgnoreCase(String expected, String actual, String template, Object... values) {
        equalsIgnoreCase(expected, actual, StrUtil.format(template, values));
    }


    private static boolean stringsNotEqual(String str1, String str2) {
        if (str1 == null) {
            return str2 != null;
        } else {
            return !str1.equalsIgnoreCase(str2);
        }
    }

    private static boolean objectsNotEqual(Object obj1, Object obj2) {
        if (obj1 == null) {
            return obj2 != null;
        } else {
            return !obj1.equals(obj2);
        }
    }

}
