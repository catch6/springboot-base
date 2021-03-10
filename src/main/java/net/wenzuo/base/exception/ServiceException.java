package net.wenzuo.base.exception;


import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import net.wenzuo.base.util.RetEnum;
import net.wenzuo.base.util.Retable;

/**
 * 业务异常类
 * 此异常保留了异常栈追踪信息
 *
 * @author Catch
 */
@Getter
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 5763453391399323579L;

    // 错误码
    private final int code;
    // 错误信息
    private final String msg;

    /**
     * 业务异常
     *
     * @param msg 错误信息
     */
    public ServiceException(String msg) {
        this(RetEnum.BAD_REQUEST.code, msg);
    }

    /**
     * 业务异常
     *
     * @param template 错误信息模板, 实际调用了 String.format
     * @param values   字符串参数
     */
    public ServiceException(String template, Object... values) {
        this(RetEnum.BAD_REQUEST.code, StrUtil.format(template, values));
    }

    /**
     * 业务异常
     *
     * @param ret Retable 子类
     */
    public ServiceException(Retable ret) {
        this(ret.getCode(), ret.getMsg());
    }

    /**
     * 业务异常
     *
     * @param code 错误码
     * @param msg  错误信息
     */
    public ServiceException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

}
