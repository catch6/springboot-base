package net.wenzuo.base.interceptor;

import cn.hutool.core.io.IoUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.wenzuo.base.util.JsonUtil;
import net.wenzuo.base.util.RetEnum;
import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

/**
 * 日志拦截器
 * 用于记录请求参数, 响应参数, 请求耗时等
 *
 * @author Catch
 */
@Slf4j
@Component
@Order(-1)
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 非接口请求直接跳过日志
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        long start = System.currentTimeMillis();
        request.setAttribute("start", start);
        StringBuilder logAppender = new StringBuilder(LoggingConsts.START);

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        logAppender.append(LoggingConsts.EXECUTION_METHOD)
                .append(handlerMethod.toString());

        processRequest(request, logAppender);

        request.setAttribute("logAppender", logAppender);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) throws Exception {
        // 非接口请求直接跳过日志
        if (!(handler instanceof HandlerMethod)) {
            return;
        }

        StringBuilder logAppender = (StringBuilder) request.getAttribute("logAppender");
        boolean isFail = processResponse(response, logAppender);

        long start = (long) request.getAttribute("start");
        long end = System.currentTimeMillis();
        long cost = end - start;

        logAppender.append(LoggingConsts.EXECUTION_COST)
                .append(cost)
                .append(LoggingConsts.COST_UNIT)
                .append(LoggingConsts.END);

        // 最终输出
        if (ex != null || isFail || cost > 3000L) {
            log.error(logAppender.toString());
            return;
        }
        if (cost > 2000L) {
            log.warn(logAppender.toString());
            return;
        }
        log.info(logAppender.toString());
    }


    /**
     * 处理请求
     *
     * @param request     HttpServletRequest
     * @param logAppender 日志StringBuilder
     * @throws Exception 异常
     */
    private void processRequest(HttpServletRequest request, StringBuilder logAppender) throws Exception {
        logAppender.append(LoggingConsts.REQUEST_URL)
                .append(request.getRequestURL());

        logAppender.append(LoggingConsts.REQUEST_PARAM);
        String contentType = request.getContentType();

        // 浏览器地址栏访问
        if (contentType == null) {
            processRequestParam(request, logAppender);
            return;
        }

        // json 请求
        if (contentType.contains(LoggingConsts.CONTENT_TYPE_JSON)) {
            String line;
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                logAppender.append(line.trim());
            }
            return;
        }

        // form 表单
        if (contentType.contains(LoggingConsts.CONTENT_TYPE_FORM)) {
            processRequestParam(request, logAppender);
            return;
        }

        // form-data 文件上传
        if (contentType.contains(LoggingConsts.CONTENT_TYPE_FORM_DATA)) {
            processRequestParam(request, logAppender);
            return;
        }

        // xml 请求
        if (contentType.contains(LoggingConsts.CONTENT_TYPE_XML)) {
            String line;
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                logAppender.append(line.trim());
            }
        }
    }

    /**
     * 追加请求参数
     *
     * @param request     HttpServletRequest
     * @param logAppender 日志StringBuilder
     * @throws Exception 异常
     */
    private void processRequestParam(HttpServletRequest request, StringBuilder logAppender) throws Exception {
        Enumeration<String> names = request.getParameterNames();
        String name;
        while (names.hasMoreElements()) {
            name = names.nextElement();
            logAppender.append(name)
                    .append(LoggingConsts.EQUALS)
                    .append(request.getParameter(name).replace(LoggingConsts.NEW_LINE, LoggingConsts.NEW_LINE_REPLACEMENT))
                    .append(LoggingConsts.DELIMITER);
        }
    }

    /**
     * 处理响应
     *
     * @param response    HttpServletResponse
     * @param logAppender 日志 StringBuilder
     * @return boolean 是否异常返回 true=发生了异常  false=正常
     * @throws Exception 异常
     */
    public boolean processResponse(HttpServletResponse response, StringBuilder logAppender) throws Exception {
        ContentCachingResponseWrapper wrapper = (ContentCachingResponseWrapper) response;
        InputStream inputStream = wrapper.getContentInputStream();
        String body = IoUtil.read(inputStream, StandardCharsets.UTF_8);
        logAppender.append(LoggingConsts.RESPONSE_BODY).append(body);
        IoUtil.close(inputStream);
        // 转换失败, 说明不是返回 Ret, 按正确返回处理
        CodeWrapper codeWrapper = JsonUtil.parseObject(body, CodeWrapper.class);
        return codeWrapper != null && codeWrapper.getCode() != RetEnum.OK.code;
    }

    @Data
    private static class CodeWrapper {

        private int code;

    }

}
