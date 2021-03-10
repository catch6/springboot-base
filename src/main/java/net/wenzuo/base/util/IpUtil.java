package net.wenzuo.base.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取 IP 工具类
 *
 * @author Catch
 */
public class IpUtil {

    /**
     * 获取 ip 地址
     * 格式 xxx.xxx.xxx.xxx
     *
     * @param request HttpServletRequest
     * @return ip 地址
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (isValidIp(ip)) {
            return getFirstIp(ip);
        }
        ip = request.getHeader("Proxy-Client-IP");
        if (isValidIp(ip)) {
            return getFirstIp(ip);
        }
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (isValidIp(ip)) {
            return getFirstIp(ip);
        }
        ip = request.getHeader("HTTP_CLIENT_IP");
        if (isValidIp(ip)) {
            return getFirstIp(ip);
        }
        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (isValidIp(ip)) {
            return getFirstIp(ip);
        }
        ip = request.getRemoteAddr();
        if (isValidIp(ip)) {
            return getFirstIp(ip);
        }
        return null;
    }

    /**
     * 校验是否是有效 ip
     *
     * @param ip ip 地址
     * @return 是否有效, true=有效, false=无效
     */
    private static boolean isValidIp(String ip) {
        return ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip);

    }

    /**
     * 获取第一个 ip
     * 因为一些电脑有多个网卡, 比如在同时使用了有线网卡和无线网卡时会检测出多个 ip, 这里取第一个
     *
     * @param ip 原始 ip 地址
     * @return 第一个 ip
     */
    private static String getFirstIp(String ip) {
        if (ip.contains(",")) {
            ip = ip.substring(0, ip.indexOf(",")).trim();
        }
        return ip;
    }

}
