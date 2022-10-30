package org.rainy.commerce.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rainy.commerce.entity.User;
import org.rainy.commerce.repository.UserRepository;
import org.rainy.commerce.util.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangyu
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class UserTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    public void createUser() {
        User user = User.builder()
                .username("rainy")
                .password(PasswordUtils.encrypt("12345678"))
                .build();
        userRepository.deleteByUsername(user.getUsername());
        userRepository.flush();
        log.info("create user success: [{}]", JSON.toJSONString(userRepository.save(user)));

    }

}
