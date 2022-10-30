package org.rainy.commerce.service;

import com.alibaba.nacos.common.codec.Base64;
import com.google.common.base.Preconditions;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.rainy.commerce.constant.AuthorityConstant;
import org.rainy.commerce.constant.CommonConstant;
import org.rainy.commerce.entity.User;
import org.rainy.commerce.util.JsonMapper;
import org.rainy.commerce.vo.JwtToken;
import org.rainy.commerce.vo.UserInfo;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangyu
 */
@Slf4j
@Service
public class JwtService {

    /**
     * 根据用户登录信息生成token
     *
     * @param user 登录参数
     * @return token
     */
    public JwtToken generateToken(User user) {
        return generateToken(user, AuthorityConstant.DEFAULT_EXPIRE_DAY);
    }

    /**
     * 根据用户登录信息生成token
     *
     * @param user 用户信息
     * @param expire 过期时间，单位是天
     * @return token
     */
    public JwtToken generateToken(User user, int expire) {
        Preconditions.checkNotNull(user, "user can not be null");
        if (expire < AuthorityConstant.DEFAULT_EXPIRE_DAY) {
            expire = AuthorityConstant.DEFAULT_EXPIRE_DAY;
        }

        ZonedDateTime zdt = LocalDateTime.now().plus(expire, ChronoUnit.DAYS).atZone(ZoneId.systemDefault());
        Date expireDate = Date.from(zdt.toInstant());

        return new JwtToken(
                Jwts.builder()
                        .claim(CommonConstant.JWT_USER_KEY, JsonMapper.object2String(
                                new UserInfo(user.getId(), user.getUsername())
                        ))
                        .setId(UUID.randomUUID().toString())
                        .setExpiration(expireDate)
                        .signWith(getPrivateKey(), SignatureAlgorithm.RS256)
                        .compact()
        );

    }

    private PrivateKey getPrivateKey() {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(
                    Base64.decodeBase64(AuthorityConstant.PRIVATE_KEY.getBytes())
            );
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
