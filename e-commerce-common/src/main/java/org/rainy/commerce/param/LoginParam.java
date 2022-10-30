package org.rainy.commerce.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * <p>
 * 用户登录时提交的参数信息
 * </p>
 *
 * @author zhangyu
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginParam {

    @NotEmpty(message = "用户名不能为空")
    @Length(min = 2, max = 32, message = "用户名长度必须在2-32个字符之间")
    private String username;

    @NotEmpty(message = "密码不能为空")
    @Length(min = 6, max = 32, message = "密码长度必须在6-32个字符之间")
    private String password;

}
