package net.wenzuo.base.util;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * 缩短 UUID 至 22 位
 * 参考自: https://github.com/hsingh/java-shortuuid
 *
 * @author Catch
 */
public class ShortUuidUtil {

    private static final char[] CHAR_BASE = {
            '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
            'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
            'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    };

    private static final BigInteger ALPHA_SIZE = BigInteger.valueOf(CHAR_BASE.length);

    /**
     * 将 uuid 缩短至 22 位
     *
     * @param uuid simpleUUID, 即去除'-'后的 UUID
     * @return 缩短后为 22 位的 uuid
     */
    public static String encode(String uuid) {
        // 注意! 此处为提高性能直接赋值计算好的 padToLen, 实际更改 CHAR_BASE 后应重新计算赋值
        // double factor = Math.log(25d) / Math.log(CHAR_BASE.length);
        // double length = Math.ceil(factor * 16);
        // int padToLen = (int) length;
        int padToLen = 13;

        BigInteger value = new BigInteger(uuid, 16);

        StringBuilder shortUuid = new StringBuilder();
        while (value.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] fracAndRemainder = value.divideAndRemainder(ALPHA_SIZE);
            shortUuid.append(CHAR_BASE[fracAndRemainder[1].intValue()]);
            value = fracAndRemainder[0];
        }

        int padding = Math.max(padToLen - shortUuid.length(), 0);
        for (int i = 0; i < padding; i++) {
            shortUuid.append(CHAR_BASE[0]);
        }

        return shortUuid.toString();
    }

    /**
     * 将缩短为 22 位的 UUID 反编码为 32 位的 UUID
     *
     * @param encoded 缩短为 22 位的 UUID
     * @return 32 位的 UUID
     */
    public static String decode(String encoded) {
        char[] chars = encoded.toCharArray();

        BigInteger sum = BigInteger.ZERO;
        int charLen = chars.length;

        for (int i = 0; i < charLen; i++) {
            sum = sum.add(ALPHA_SIZE.pow(i).multiply(BigInteger.valueOf(
                    Arrays.binarySearch(CHAR_BASE, chars[i]))));
        }

        String str = sum.toString(16);

        // Pad the most significant bit (MSG) with 0 (zero) if the string is too short.
        if (str.length() < 32) {
            str = String.format("%32s", str).replace(' ', '0');
        }

        return str;
    }

}
