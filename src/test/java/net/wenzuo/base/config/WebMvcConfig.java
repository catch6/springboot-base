package net.wenzuo.base.config;

import net.wenzuo.base.interceptor.LoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域及拦截器配置
 *
 * @author Catch
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 公共不拦截的路由
     */
    public static final String[] COMMON_EXCLUDES = {
            "/actuator/**", "/error/**", "/swagger-resources/**", "/webjars/**", "/doc.html"
    };

    @Autowired
    private LoggingInterceptor loggingInterceptor;

    // ========= 走 nginx proxy 反向代理, 不做跨域处理 ========
    // /**
    //  * 跨域配置
    //  *
    //  * @param registry CorsRegistry
    //  */
    // @Override
    // public void addCorsMappings(CorsRegistry registry) {
    //     registry.addMapping("/**")
    //             .allowCredentials(true)
    //             .allowedOrigins("https://auth.wenzuo.net")
    //             .allowedHeaders("X-Token")
    //             .allowedMethods("*")
    //             .exposedHeaders("*");
    // }

    /**
     * 拦截器配置
     *
     * @param registry InterceptorRegistry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(COMMON_EXCLUDES)
        ;
    }

}
