package net.wenzuo.base.util;

/**
 * 错误码枚举类需继承此接口
 * {@link Ret} 的 fail(Retable ret) 方法可传递此接口的实现类
 *
 * @author Catch
 */
public interface Retable {

    int getCode();

    String getMsg();

}
