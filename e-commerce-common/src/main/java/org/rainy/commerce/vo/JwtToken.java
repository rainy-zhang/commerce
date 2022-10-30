package org.rainy.commerce.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 授权中心返回的token信息
 * </p>
 *
 * @author zhangyu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtToken {

    private String token;

}
