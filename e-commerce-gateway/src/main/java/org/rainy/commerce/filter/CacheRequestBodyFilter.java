package org.rainy.commerce.filter;

import lombok.extern.slf4j.Slf4j;
import org.rainy.commerce.constant.GatewayConstant;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <p>
 * 缓存request body的过滤器
 * </p>
 *
 * @author zhangyu
 */
@Slf4j
@Component
public class CacheRequestBodyFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String uri = exchange.getRequest().getURI().getPath();
        boolean isLoginOrRegister = uri.contains(GatewayConstant.LOGIN_URI) || uri.contains(GatewayConstant.REGISTER_URI);

        // 登陆请求和注册请求没必要去缓存，直接放行
        if (isLoginOrRegister) {
            chain.filter(exchange);
        }

        return DataBufferUtils.join(exchange.getRequest().getBody()).flatMap(dataBuffer -> {
            // 确保数据缓冲区不被释放，必须要调用 DataBufferUtils.retain()方法
            DataBufferUtils.retain(dataBuffer);
            // 创建当前request body的副本
            Flux<DataBuffer> duplicateBody = Flux.defer(() -> Flux.just(dataBuffer.slice(0, dataBuffer.readableByteCount())));
            // 重新包装ServerHttpRequest，并替换request body
            ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                // 重写getBody方法，并返回request body副本
                @Override
                @NonNull
                public Flux<DataBuffer> getBody() {
                    return duplicateBody;
                }
            };
            // 将包装之后的ServerHttpRequest继续向下传递
            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        });
    }

    @Override
    public int getOrder() {
        // 其他过滤器如果想要获取缓存中的body，其getOrder的值需要大于 HIGHEST_PRECEDENCE + 1
        return HIGHEST_PRECEDENCE + 1;
    }
}
