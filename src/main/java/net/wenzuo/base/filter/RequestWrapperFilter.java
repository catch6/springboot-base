package net.wenzuo.base.filter;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import net.wenzuo.base.util.ShortUuidUtil;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 包装 HttpServletRequest 对象为 RequestWrapper
 * RequestWrapper 实现了可重复读取 getReader() 和 getInputStream()
 *
 * @author Catch
 */
@Slf4j
@Order(10)
@Component
public class RequestWrapperFilter extends OncePerRequestFilter {

    private static final String TRACE_ID = "X-ID";

    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String CONTENT_TYPE_XML = "application/xml";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String traceId = IdUtil.fastSimpleUUID();
        traceId = ShortUuidUtil.encode(traceId);
        MDC.put(TRACE_ID, traceId);

        String contentType = request.getContentType();
        if (contentType != null
                && (contentType.contains(CONTENT_TYPE_JSON) || contentType.contains(CONTENT_TYPE_XML))) {
            request = new RequestWrapper(request);
        }
        ContentCachingResponseWrapper wrapper = new ContentCachingResponseWrapper(response);
        try {
            chain.doFilter(request, wrapper);
        } finally {
            wrapper.copyBodyToResponse();
            MDC.clear();
        }
    }

}
