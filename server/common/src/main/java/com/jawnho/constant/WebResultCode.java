package com.jawnho.constant;

/**
 * 返回给前端的resultCode
 *
 * @author gyj
 * @date 2019/5/21
 */
public class WebResultCode {

    /**
     * 成功
     */
    public static final int OK = 0;

    /**
     * 失败
     */
    public static final int FAILED = 400;

    /**
     * 需要登录
     */
    public static final int NEED_LOGIN = 401;

    /**
     * 无权访问
     */
    public static final int NO_ACCESS = 403;
}
