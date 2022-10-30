package org.rainy.commerce.util;

import org.apache.commons.codec.digest.DigestUtils;


/**
 * <p>
 *
 * </p>
 *
 * @author zhangyu
 */
public class PasswordUtils {

    /**
     * 使用MD5对密码加密
     * @param password 密码明文
     * @return 加密后的密码
     */
    public static String encrypt(String password) {
        return DigestUtils.md5Hex(password);
    }


}
