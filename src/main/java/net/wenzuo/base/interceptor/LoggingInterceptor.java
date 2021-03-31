package net.wenzuo.base.interceptor;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.wenzuo.base.util.JsonUtil;
import net.wenzuo.base.util.RetEnum;
import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;

/**
 * 日志拦截器
 * 用于记录请求参数, 响应参数, 请求耗时等
 *
 * @author Catch
 */
@Slf4j
@Order(-1)
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 非接口请求直接跳过日志
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        LoggingContextHolder.start();
        LoggingContextHolder.append("\n╔")
                .append("============================================================");

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        LoggingContextHolder.append("\n║ 执行方法: ")
                .append(handlerMethod.toString());

        loggingRequest(request);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) throws Exception {
        // 非接口请求直接跳过日志
        if (!(handler instanceof HandlerMethod)) {
            return;
        }

        boolean isSuccess = loggingResponse(response);

        long took = LoggingContextHolder.stop();

        StringBuilder loggingBuilder = LoggingContextHolder.get();

        loggingBuilder.append("\n║ 执行耗时: ")
                .append(took)
                .append("ms")
                .append("\n╚")
                .append("============================================================");

        // 最终输出
        if (!isSuccess || took > 3000L || ex != null) {
            log.error(loggingBuilder.toString());
            return;
        }
        if (took > 2000L) {
            log.warn(loggingBuilder.toString());
            return;
        }
        log.info(loggingBuilder.toString());
    }

    /**
     * 记录 request 参数
     *
     * @param request HttpServletRequest
     * @throws Exception Exception
     */
    private void loggingRequest(HttpServletRequest request) throws Exception {
        LoggingContextHolder.append("\n║ 请求地址: ")
                .append(request.getRequestURL())
                .append("\n║ 请求参数: ");

        String queryString = request.getQueryString();
        if (StringUtils.hasLength(queryString)) {
            LoggingContextHolder.append(queryString).append(" ");
        }

        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            LoggingContextHolder.append(line.trim());
        }
    }

    /**
     * 记录 response 参数
     *
     * @param response HttpServletResponse
     * @return 是否正常响应: true=正常响应; false=发生异常
     * @throws Exception Exception
     */
    private boolean loggingResponse(HttpServletResponse response) throws Exception {
        ContentCachingResponseWrapper wrapper = (ContentCachingResponseWrapper) response;
        byte[] byteArray = wrapper.getContentAsByteArray();
        String body = new String(byteArray, StandardCharsets.UTF_8);
        LoggingContextHolder.append("\n║ 响应参数: ")
                .append(body);
        wrapper.copyBodyToResponse();
        // 转换失败, 返回 null, 说明不是返回 Ret, 按正确返回处理
        CodeWrapper codeWrapper = JsonUtil.parseObject(body, CodeWrapper.class);
        return codeWrapper == null || codeWrapper.getCode() == RetEnum.OK.code;
    }

    @Data
    private static class CodeWrapper {
        private int code;
    }

}
