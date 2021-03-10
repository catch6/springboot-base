package net.wenzuo.base.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 响应工具类
 * 提供一些响应封装
 *
 * @author Catch
 */
@Slf4j
public class ResponseUtil {

    public static void renderJson(HttpServletResponse response, Object object) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            String json = JsonUtil.toJsonString(object);
            writer.print(json);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

}
