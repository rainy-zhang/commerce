package org.rainy.commerce.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * <p>
 * 检查Header中是否包含Token
 * </p>
 *
 * @author zhangyu
 */
@Slf4j
@Component
public class CheckTokenFilter implements GatewayFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String name = request.getHeaders().getFirst("token");
        if (!"e-commerce".equals(name)) {
            return chain.filter(exchange);
        }
        String uri = request.getURI().getPath();
        log.warn("[{}] header not found [token], request end!", uri);
        ServerHttpResponse response = exchange.getResponse();
        // 将本次请求标记为没有权限
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        // 结束请求
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
