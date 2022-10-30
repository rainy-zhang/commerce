package org.rainy.commerce.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rainy.commerce.param.LoginParam;
import org.rainy.commerce.util.TokenUtils;
import org.rainy.commerce.vo.JwtToken;
import org.rainy.commerce.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangyu
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class AuthorityServiceTest {

    @Autowired
    private AuthorityService authorityService;

    @Test
    public void authorityTest() {
        JwtToken jwtToken = authorityService.login(
                LoginParam.builder()
                        .username("rainy")
                        .password("12345678")
                        .build()
        );
        log.info("login success, token: {}", jwtToken.getToken());

        UserInfo userInfo = TokenUtils.parseToken(jwtToken.getToken());
        log.info("token parse success, userInfo: {}", userInfo);
    }

}
