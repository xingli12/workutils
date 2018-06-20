package com.xingli.utils;

/**
 * @program: springboot-first-application
 * @Author: xingli12
 * @Description:
 * @Date: Created in 2018-05-17 09:04
 * @Modified By:
 * @UpdateDate:
 * @Version:
 */
import org.springframework.util.StringUtils;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5摘要工具
 *  @author xkfeng@iflytek.com
 *  @lastModified
 *  @history
 *  @date 2018-01-29
 */
public class MD5Util {

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static final String encodingAlgorithm="MD5";

    private static String characterEncoding="utf-8";

    public static String encode(final String password) {
        if (password == null) {
            return null;
        }

        try {
            MessageDigest messageDigest = MessageDigest
                    .getInstance(encodingAlgorithm);

            if (StringUtils.hasText(characterEncoding)) {
                messageDigest.update(password.getBytes(characterEncoding));
            } else {
                messageDigest.update(password.getBytes());
            }


            final byte[] digest = messageDigest.digest();

            return getFormattedText(digest);
        } catch (final NoSuchAlgorithmException e) {
            throw new SecurityException(e);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Takes the raw bytes from the digest and formats them correct.
     *
     * @param bytes the raw bytes from the digest.
     * @return the formatted bytes.
     */
    private static String getFormattedText(byte[] bytes) {
        final StringBuilder buf = new StringBuilder(bytes.length * 2);

        for (int j = 0; j < bytes.length; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }
}

