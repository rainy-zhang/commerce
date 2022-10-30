package org.rainy.commerce.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * JWT Token 中保存的用户信息
 * </p>
 *
 * @author zhangyu
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserInfo {

    private long id;

    private String username;

}
