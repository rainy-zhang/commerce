package org.rainy.commerce.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.rainy.commerce.util.JsonMapper;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * <p>
 * 监听Nacos中路由配置变更，并下发动态路由配置
 * </p>
 *
 * @author zhangyu
 */
@Slf4j
@Component
@DependsOn({"gatewayConfig"})
public class DynamicRouteListenerByNacos {

    private final DynamicRouteEventPublisher routeEventPublisher;

    public DynamicRouteListenerByNacos(DynamicRouteEventPublisher routeEventPublisher) {
        this.routeEventPublisher = routeEventPublisher;
    }

    @PostConstruct
    public void init() {
        try {
            // 初始化Nacos配置客户端
            // Nacos 配置服务
            ConfigService configService = initConfigServer();
            if (configService == null) {
                log.error("nacos config init failed");
                return;
            }

            // 通过NacosConfig获取路由配置
            String configInfo = configService.getConfig(
                    GatewayConfig.NACOS_ROUTER_DATA_ID,
                    GatewayConfig.NACOS_ROUTER_GROUP,
                    GatewayConfig.DEFAULT_TIMEOUT
            );

            log.info("get current gateway config info: [{}]", configInfo);

            List<RouteDefinition> definitions = JsonMapper.string2Object(configInfo, new TypeReference<>() {
            });
            if (!CollectionUtils.isEmpty(definitions)) {
                definitions.forEach(routeEventPublisher::addRouteDefinition);
            }

            configService.addListener(
                    GatewayConfig.NACOS_ROUTER_DATA_ID,
                    GatewayConfig.NACOS_ROUTER_GROUP,
                    new NacosConfigListener()
            );
        } catch (NacosException e) {
            log.error("gateway route init error: [{}]", e.getMessage(), e);
        }
    }

    /**
     * 初始化ConfigServer
     */
    private ConfigService initConfigServer() {
        try {
            Properties properties = new Properties();
            properties.setProperty("serverAddr", GatewayConfig.NACOS_SERVER_ADDRESS);
            properties.setProperty("namespace", GatewayConfig.NACOS_SERVER_NAMESPACE);
            return NacosFactory.createConfigService(properties);
        } catch (NacosException e) {
            log.error("init nacos config error: [{}]", e.getMessage(), e);
            return null;
        }
    }

    private class NacosConfigListener implements Listener {

        // 这里可以自定义线程池，不指定的话默认使用内部的线程池
        @Override
        public Executor getExecutor() {
            return null;
        }

        /**
         * 接收到的配置信息
         * @param configInfo Nacos中最新的配置信息
         */
        @Override
        public void receiveConfigInfo(String configInfo) {
            log.info("receive nacos route config: [{}]", configInfo);
            List<RouteDefinition> definitions = JsonMapper.string2Object(configInfo, new TypeReference<>() {
            });
            routeEventPublisher.batchUpdateRouteDefinition(definitions);
        }
    }



}
