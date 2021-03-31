package net.wenzuo.base.filter;

import cn.hutool.core.util.IdUtil;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zhanghao13
 * @since 2021/3/31 16:48
 */
@Component
public class HttpLoggingFilter extends OncePerRequestFilter {

    private static final String X_REQUEST_ID = "X-Request-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String requestId = request.getHeader(X_REQUEST_ID);
        if (!StringUtils.hasLength(requestId)) {
            requestId = IdUtil.fastUUID();
        }

        MDC.put(X_REQUEST_ID, requestId);

        boolean isFirstRequest = !isAsyncDispatch(request);
        if (isFirstRequest) {
            if (!(request instanceof ContentCachingRequestWrapper)) {
                request = new ContentCachingRequestWrapper(request);
            }
            if (!(response instanceof ContentCachingResponseWrapper)) {
                response = new ContentCachingResponseWrapper(response);
                response.addHeader(X_REQUEST_ID, requestId);
            }
        }

        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove(X_REQUEST_ID);
        }
    }

}
