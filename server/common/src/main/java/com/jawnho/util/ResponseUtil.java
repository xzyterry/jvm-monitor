package com.jawnho.util;

import com.google.common.collect.Maps;
import com.jawnho.constant.WebResultCode;
import java.util.Map;

/**
 * 返回给前端的结果
 *
 * @author gyj
 * @date 2019/5/21
 */
public class ResponseUtil {

    public static final String RESULT_CODE = "resultCode";

    public static final String DATA = "data";

    public static final String MESSAGE = "message";

    /**
     * 成功
     */
    public static Map<String, Object> success(String message, Object data) {
        return response(WebResultCode.OK, message, data);
    }

    /**
     * 失败
     */
    public static Map<String, Object> fail(String message, Object data) {
        return response(WebResultCode.FAILED, message, data);
    }

    /**
     * 自定义返回值
     */
    public static Map<String, Object> response(int resultCode, String message, Object data) {
        Map<String, Object> map = Maps.newHashMapWithExpectedSize(3);
        map.put(RESULT_CODE, resultCode);
        map.put(DATA, data);
        map.put(MESSAGE, message);
        return map;
    }
}
