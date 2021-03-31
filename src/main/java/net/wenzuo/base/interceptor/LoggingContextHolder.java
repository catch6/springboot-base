package net.wenzuo.base.interceptor;

/**
 * 请求响应日志记录
 *
 * @author zhanghao13
 * @since 2021/3/30 11:27
 */
public abstract class LoggingContextHolder {

    private static final ThreadLocal<LoggingContext> LOGGING_CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

    public static void start() {
        LoggingContext context = new LoggingContext();
        context.startTime = System.currentTimeMillis();
        context.loggingBuilder = new StringBuilder();
        LOGGING_CONTEXT_THREAD_LOCAL.set(context);
    }

    public static StringBuilder append(String content) {
        check();
        LoggingContext context = LOGGING_CONTEXT_THREAD_LOCAL.get();
        return context.loggingBuilder.append(content);
    }

    public static long stop() {
        check();
        LoggingContext context = LOGGING_CONTEXT_THREAD_LOCAL.get();
        context.endTime = System.currentTimeMillis();
        return context.endTime - context.startTime;
    }

    public static StringBuilder get() {
        check();
        LoggingContext context = LOGGING_CONTEXT_THREAD_LOCAL.get();
        return context.loggingBuilder;
    }

    private static void check() {
        LoggingContext context = LOGGING_CONTEXT_THREAD_LOCAL.get();
        if (context == null) {
            throw new IllegalStateException("必须先调用start()方法初始化");
        }
    }


    private static class LoggingContext {

        private long startTime;
        private long endTime;

        private StringBuilder loggingBuilder;

    }

}
