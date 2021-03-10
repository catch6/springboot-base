package net.wenzuo.base.interceptor;

/**
 * 日志拦截器常量
 *
 * @author Catch
 */
public class LoggingConsts {

    public static final String START = "\n╔════════════════════════════════════════════════════════════";
    public static final String END = "\n╚════════════════════════════════════════════════════════════";

    public static final String EXECUTION_METHOD = "\n║ 执行方法: ";
    public static final String REQUEST_URL = "\n║ 请求地址: ";
    public static final String REQUEST_PARAM = "\n║ 请求参数: ";
    public static final String RESPONSE_BODY = "\n║ 响应参数: ";
    public static final String EXECUTION_COST = "\n║ 执行耗时: ";

    public static final String NEW_LINE = "\n";
    public static final String NEW_LINE_REPLACEMENT = "\n║          ";

    public static final String EQUALS = "=";
    public static final String DELIMITER = "; ";

    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
    public static final String CONTENT_TYPE_XML = "application/xml";
    public static final String CONTENT_TYPE_FORM_DATA = "multipart/form-data";

    public static final String COST_UNIT = "ms";

}
