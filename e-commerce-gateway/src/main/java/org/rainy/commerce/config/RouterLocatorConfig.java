package org.rainy.commerce.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 通过代码定义路由规则
 * </p>
 *
 * @author zhangyu
 */
@Configuration
public class RouterLocatorConfig {

    @Bean
    public RouteLocator loginRouteLocator(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route(
                        "e-commerce-authority",
                        r -> r.path("/gateway/e-commerce/login", "/gateway/e-commerce/register").uri("http://localhost:9001")
                )
                .build();
    }

}
