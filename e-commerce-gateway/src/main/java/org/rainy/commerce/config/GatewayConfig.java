package org.rainy.commerce.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangyu
 */
@Configuration
public class GatewayConfig {

    // 读取Nacos中配置信息的超时时间 
    public static final long DEFAULT_TIMEOUT = 30000;

    public static String NACOS_SERVER_ADDRESS;

    public static String NACOS_SERVER_NAMESPACE;

    public static String NACOS_ROUTER_DATA_ID;

    public static String NACOS_ROUTER_GROUP;

    @Value(value = "${spring.cloud.nacos.discovery.server-addr}")
    public void setNacosServerAddress(String nacosAddress) {
        NACOS_SERVER_ADDRESS = nacosAddress;
    }

    @Value(value = "${spring.cloud.nacos.discovery.namespace}")
    public void setNacosServerNamespace(String nacosNamespace) {
        NACOS_SERVER_NAMESPACE = nacosNamespace;
    }

    @Value(value = "${nacos.gateway.config.router.data-id}")
    public void setNacosRouterDataId(String routerDataId) {
        NACOS_ROUTER_DATA_ID = routerDataId;
    }

    @Value(value = "${nacos.gateway.config.router.group}")
    public void setNacosRouterGroup(String routerGroup) {
        NACOS_ROUTER_GROUP = routerGroup;
    }

}
