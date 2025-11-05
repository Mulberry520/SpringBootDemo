package com.mulberry.common;


import lombok.Data;

@Data
public class R<T> {
    private Integer code;
    private String msg;
    private T data;

    private static <DATA> R<DATA> of(int code, String msg, DATA data) {
        R<DATA> r = new R<>();
        r.code = code;
        r.msg = msg;
        r.data = data;
        return r;
    }

    public static <DATA> R<DATA> success(int code, DATA data) {
        return R.of(code, CommonConst.SUCCESS_RESULT, data);
    }

    public static <DATA> R<DATA> success(DATA data) {
        return R.of(CommonConst.SUCCESS_CODE, CommonConst.SUCCESS_RESULT, data);
    }

    public static <DATA> R<DATA> success(String msg) {
        return R.of(CommonConst.SUCCESS_CODE, msg, null);
    }

    public static <DATA> R<DATA> success() {
        return R.of(CommonConst.SUCCESS_CODE, CommonConst.SUCCESS_RESULT, null);
    }

    public static <DATA> R<DATA> error(int code, String msg) {
        return R.of(code, msg, null);
    }

    public static <DATA> R<DATA> error(String msg) {
        return R.of(CommonConst.ERROR_CODE, msg, null);
    }

    public static <DATA> R<DATA> error() {
        return R.of(CommonConst.ERROR_CODE, CommonConst.ERROR_RESULT, null);
    }

    public static <T> R<T> handlerResult(T result, String msg) {
        if (result != null) {
            return R.success(result);
        }
        return R.error(msg);
    }
}
