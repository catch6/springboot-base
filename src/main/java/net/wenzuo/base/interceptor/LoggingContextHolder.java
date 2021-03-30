package net.wenzuo.base.interceptor;

/**
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

    public static void append(String content) {
        LoggingContext context = LOGGING_CONTEXT_THREAD_LOCAL.get();
        if (context == null) {
            throw new IllegalStateException("必须先调用start()方法初始化");
        }
        context.loggingBuilder.append(content);
    }

    public LoggingContext end() {
        LoggingContext context = LOGGING_CONTEXT_THREAD_LOCAL.get();
        if (context == null) {
            throw new IllegalStateException("必须先调用start()方法初始化");
        }
        context.endTime = System.currentTimeMillis();
        return context;
    }


    public static class LoggingContext {

        private long startTime;
        private long endTime;

        private StringBuilder loggingBuilder;

    }

}
