package org.rainy.commerce.constant;

/**
 * <p>
 * 网关服务常量类
 * </p>
 *
 * @author zhangyu
 */
public class GatewayConstant {

    public static final String LOGIN_URI = "e-commerce/login";
    public static final String REGISTER_URI = "e-commerce/register";

    /** 授权中心服务登陆接口的格式化字符串 */
    public static final String AUTHORITY_CENTRAL_LOGIN_URL_FORMAT = "http://%s:%s/e-commerce-authority/authority/login";
    /** 授权中心服务注册接口的格式化字符串 */
    public static final String AUTHORITY_CENTRAL_REGISTER_URL_FORMAT = "http://%s:%s/e-commerce-authority/authority/register";

}
