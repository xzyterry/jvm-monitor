package com.jawnho.util;

/**
 * 用户controller层的判断工具类，自定义exception中的msg
 *
 * @author jawnho
 * @date 2019/12/23
 */
public class CheckUtil {

    /**
     * 检查是否为null
     */
    public static void checkNotNull(Object object, String... errMsgs) {
        if (object != null) {
            return;
        }

        if (errMsgs == null) {
            throw new NullPointerException();
        }

        StringBuilder allErrMsg = new StringBuilder();
        for (String errMsg : errMsgs) {
            allErrMsg.append(errMsg).append("\t");
        }
        throw new NullPointerException(allErrMsg.toString());
    }

    /**
     * 检查表达式是否为false
     */
    public static void checkArgument(boolean expression, String... errMsgs) {
        if (expression) {
            return;
        }

        if (errMsgs == null) {
            throw new IllegalArgumentException();
        }

        StringBuilder allErrMsg = new StringBuilder();
        for (String errMsg : errMsgs) {
            allErrMsg.append(errMsg).append("\t");
        }
        throw new IllegalArgumentException(allErrMsg.toString());
    }
}
