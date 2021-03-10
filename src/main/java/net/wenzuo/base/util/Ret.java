package net.wenzuo.base.util;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 统一响应, 用于返回 json 响应体
 *
 * @author Catch
 */
public class Ret<T> {

    private int code;
    private String msg;
    private T data;

    public static <T> Ret<T> ok() {
        Ret<T> ret = new Ret<>();
        ret.setCode(RetEnum.OK.code);
        ret.setMsg(RetEnum.OK.msg);
        return ret;
    }

    public static <T> Ret<T> ok(T data) {
        Ret<T> ret = Ret.ok();
        ret.setData(data);
        return ret;
    }

    public static Ret<Void> fail(int code, String msg) {
        Ret<Void> ret = new Ret<>();
        ret.setCode(code);
        ret.setMsg(msg);
        return ret;
    }

    public static Ret<Void> fail(String msg) {
        return Ret.fail(RetEnum.BAD_REQUEST.code, msg);
    }

    public static Ret<Void> fail(String template, Object... values) {
        return Ret.fail(RetEnum.BAD_REQUEST.code, StrUtil.format(template, values));
    }

    public static Ret<Void> fail(Retable ret) {
        return Ret.fail(ret.getCode(), ret.getMsg());
    }

    @JsonIgnore
    public boolean isOk() {
        return code == RetEnum.OK.code;
    }

    @JsonIgnore
    public boolean isFail() {
        return code != RetEnum.OK.code;
    }

    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓以下为常用业务失败封装↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static Ret<Void> badRequest() {
        return Ret.fail(RetEnum.BAD_REQUEST);
    }

    public static Ret<Void> unauthorized() {
        return Ret.fail(RetEnum.UNAUTHORIZED);
    }

    public static Ret<Void> forbidden() {
        return Ret.fail(RetEnum.FORBIDDEN);
    }

    public static Ret<Void> notFound() {
        return Ret.fail(RetEnum.NOT_FOUND);
    }

    public static Ret<Void> serverError() {
        return Ret.fail(RetEnum.SERVER_ERROR);
    }

    // ========== Getter And Setter ==========
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JsonUtil.toJsonString(this);
    }

}
