package org.rainy.commerce.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 打印请求处理时长
 * </p>
 *
 * @author zhangyu
 */
@Slf4j
@Component
public class ElapsedLogFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        StopWatch sw = StopWatch.createStarted();
        ServerHttpRequest request = exchange.getRequest();
        String uri = request.getURI().getPath();
        MultiValueMap<String, String> params = request.getQueryParams();

        return chain.filter(exchange).then(
                Mono.fromRunnable(() -> log.info("[{}] [{}] cost: [{}ms]", uri, params, sw.getTime(TimeUnit.MILLISECONDS)))
        );
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
