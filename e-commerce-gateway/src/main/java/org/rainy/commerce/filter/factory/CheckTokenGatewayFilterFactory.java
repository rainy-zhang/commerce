package org.rainy.commerce.filter.factory;

import org.rainy.commerce.filter.CheckTokenFilter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 定义{@link CheckTokenFilter}过滤器工厂
 * </p>
 *
 * @author zhangyu
 */
@Component
public class CheckTokenGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
    @Override
    public GatewayFilter apply(Object config) {
        return new CheckTokenFilter();
    }
}
