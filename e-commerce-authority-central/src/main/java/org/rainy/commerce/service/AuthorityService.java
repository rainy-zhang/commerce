package org.rainy.commerce.service;

import lombok.extern.slf4j.Slf4j;
import org.rainy.commerce.entity.User;
import org.rainy.commerce.param.LoginParam;
import org.rainy.commerce.param.RegisterParam;
import org.rainy.commerce.repository.UserRepository;
import org.rainy.commerce.util.PasswordUtils;
import org.rainy.commerce.vo.JwtToken;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangyu
 */
@Slf4j
@Service
public class AuthorityService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthorityService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public JwtToken login(@Validated LoginParam loginParam) {
        String password = PasswordUtils.encrypt(loginParam.getPassword());
        User user = userRepository.findByUsernameAndPassword(loginParam.getUsername(), password).orElseThrow(() -> new RuntimeException(
                String.format("user can not find: [%s], [%s]", loginParam.getUsername(), password)
        ));
        log.info("user login success: [{}]", loginParam.getUsername());
        return jwtService.generateToken(user);
    }

    /**
     * 用户注册并返回token
     * @param registerParam 用户注册参数
     * @return token
     */
    public JwtToken register(@Validated RegisterParam registerParam) {
        userRepository.findByUsername(registerParam.getUsername()).ifPresent(user -> {
            throw new RuntimeException(
                    String.format("user already exist: [%s]", registerParam.getUsername())
            );
        });

        User user = userRepository.save(User.convert(registerParam));
        return jwtService.generateToken(user);
    }


}
