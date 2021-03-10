package net.wenzuo.base.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 基础错误码枚举类
 *
 * @author Catch
 */
@Getter
@AllArgsConstructor
public enum RetEnum implements Retable {

    OK(200, "成功"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "您未登录或您的登录信息已过期, 请重新登录"),
    FORBIDDEN(403, "您没有该权限"),
    NOT_FOUND(404, "您请求的资源未找到"),
    SERVER_ERROR(500, "服务器繁忙, 请稍后再试"),
    ;

    public final int code;
    public final String msg;

}
