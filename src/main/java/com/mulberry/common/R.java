package com.mulberry.common;


import lombok.Data;

@Data
public class R<T> {
    private Integer code;
    private String msg;
    private T data;

    public static <T> R<T> success(T obj) {
        R<T> r = new R<>();
        r.data = obj;
        r.code = 0;
        r.msg = CommonConst.SUCCESS_RESULT;
        return r;
    }

    public static <T> R<T> error(String msg) {
        R<T> r = new R<>();
        r.data = null;
        r.code = 1;
        r.msg = msg;
        return r;
    }

    public static <T> R<T> handlerResult(T result, String msg) {
        if (result != null) {
            return R.success(result);
        }
        return R.error(msg);
    }
}
