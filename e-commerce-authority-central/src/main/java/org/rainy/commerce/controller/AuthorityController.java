package org.rainy.commerce.controller;

import org.rainy.commerce.annotation.IgnoreResponseAdvice;
import org.rainy.commerce.param.LoginParam;
import org.rainy.commerce.param.RegisterParam;
import org.rainy.commerce.service.AuthorityService;
import org.rainy.commerce.vo.JwtToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangyu
 */
@RestController
@RequestMapping(value = "/authority")
public class AuthorityController {

    private final AuthorityService authorityService;

    public AuthorityController(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @IgnoreResponseAdvice
    @PostMapping(value = "/login")
    public JwtToken login(@RequestBody LoginParam loginParam) {
        return authorityService.login(loginParam);
    }

    @IgnoreResponseAdvice
    @PostMapping(value = "/register")
    public JwtToken register(@RequestBody RegisterParam registerParam) {
        return authorityService.register(registerParam);
    }

}
