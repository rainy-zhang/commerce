package org.rainy.commerce.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.rainy.commerce.constant.CommonConstant;
import org.rainy.commerce.vo.UserInfo;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Calendar;

/**
 * <p>
 * JWT Token解析工具类
 * </p>
 *
 * @author zhangyu
 */
public class TokenUtils {

    public static UserInfo parseToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }

        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();

        if (claims.getExpiration().before(Calendar.getInstance().getTime())) {
            return null;
        }

        return JsonMapper.string2Object(claims.get(CommonConstant.JWT_USER_KEY).toString(), new TypeReference<UserInfo>() {
        });
    }

    private static PublicKey getPublicKey() {
        try {

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(
                    Base64.getDecoder().decode(CommonConstant.PUBLIC_KEY)
            );
            return KeyFactory.getInstance("RSA").generatePublic(keySpec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
