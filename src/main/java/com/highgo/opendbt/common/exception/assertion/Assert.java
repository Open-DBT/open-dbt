package com.highgo.opendbt.common.exception.assertion;

import com.highgo.opendbt.common.exception.BaseException;

import java.util.List;

public interface Assert {
    /**
     * 创建异常
     */
    BaseException newException(Object... args);
    /**
     * 创建异常
     */
    BaseException newException(Throwable t, Object... args);
    /**
     * <p>断言对象<code>obj</code>非空。如果对象<code>obj</code>为空，则抛出异常
     *
     * @param obj 待判断对象
     */
    default void assertNotNull(Object obj) {
        if (obj == null) {
            throw newException(obj);
        }
    }

    /**
     * <p>断言对象<code>obj</code>非空。如果对象<code>obj</code>为空，则抛出异常
     * <p>异常信息<code>message</code>支持传递参数方式，避免在判断之前进行字符串拼接操作
     *
     * @param obj  待判断对象
     * @param args message占位符对应的参数列表
     */
    default void assertNotNull(Object obj, Object... args) {
        if (obj == null) {
            throw newException(args);
        }
    }

    /**
     * 如果为false抛出异常
     **/
    default void assertIsTrue(boolean res, Object... args) {
        if (!res) {
            throw newException(args);
        }
    }

    default void assertIsTrue(boolean res) {
        if (!res) {
            throw newException(null);
        }
    }

    /**
     * 如果为true抛出异常
     **/
    default void assertIsFalse(boolean res, Object... args) {
        if (res) {
            throw newException(args);
        }
    }

    default void assertIsFalse(boolean res) {
        if (res) {
            throw newException(null);
        }
    }

    /**
     * 如果不为空抛出异常
     **/
    default void assertIsEmpty(Object obj, Object... args) {

        if (obj instanceof List) {
            if (obj != null && ((List) obj).size() > 0) {
                throw newException(args);
            }
        } else {
            if (obj != null) {
                throw newException(args);
            }
        }
    }

    /**
     * 如果为空抛出异常
     **/
    default void assertIsNotEmpty(Object obj, Object... args) {
        if (obj instanceof List) {
            if (obj == null || ((List) obj).size() == 0) {
                throw newException(args);
            }
        } else {
            if (obj == null) {
                throw newException(args);
            }
        }
    }
}
