package org.rainy.commerce.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.rainy.commerce.constant.CommonConstant;
import org.rainy.commerce.constant.GatewayConstant;
import org.rainy.commerce.param.LoginParam;
import org.rainy.commerce.param.RegisterParam;
import org.rainy.commerce.util.JsonMapper;
import org.rainy.commerce.util.TokenUtils;
import org.rainy.commerce.vo.JwtToken;
import org.rainy.commerce.vo.UserInfo;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 * 鉴权过滤器
 * </p>
 *
 * @author zhangyu
 */
@Slf4j
@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private final RestTemplate restTemplate;
    private final LoadBalancerClient loadBalancerClient;

    public AuthenticationFilter(RestTemplate restTemplate, LoadBalancerClient loadBalancerClient) {
        this.restTemplate = restTemplate;
        this.loadBalancerClient = loadBalancerClient;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String uri = request.getURI().getPath();
        // 如果是登陆请求，则调用授权中心服务的登陆接口获取token
        if (uri.contains(GatewayConstant.LOGIN_URI)) {
            LoginParam loginRequestBody = JsonMapper.string2Object(getBodyFromRequest(request), new TypeReference<>() {
            });
            String token = getTokenFromAuthorityCentral(GatewayConstant.AUTHORITY_CENTRAL_LOGIN_URL_FORMAT, loginRequestBody);
            response.getHeaders().add(CommonConstant.JWT_USER_KEY, token == null ? "null" : token);
            response.setStatusCode(HttpStatus.OK);
            return response.setComplete();
        }

        // 如果是注册请求，则调用授权中心服务的注册接口获取token
        if (uri.contains(GatewayConstant.REGISTER_URI)) {
            RegisterParam registerRequestBody = JsonMapper.string2Object(getBodyFromRequest(request), new TypeReference<>() {
            });
            String token = getTokenFromAuthorityCentral(GatewayConstant.AUTHORITY_CENTRAL_REGISTER_URL_FORMAT, registerRequestBody);
            response.getHeaders().add(CommonConstant.JWT_USER_KEY, token == null ? "null" : token);
            response.setStatusCode(HttpStatus.OK);
            return response.setComplete();
        }

        // 其他请求，则获取request header中的token并解析
        HttpHeaders headers = request.getHeaders();
        String token = headers.getFirst(CommonConstant.JWT_USER_KEY);
        UserInfo userInfo = TokenUtils.parseToken(token);

        // 无法从token中解析出用户信息，返回401
        if (userInfo == null) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 2;
    }

    private String getTokenFromAuthorityCentral(String authorityCentralUrlFormat, Object requestBody) {
        ServiceInstance serviceInstance = loadBalancerClient.choose(CommonConstant.AUTHORITY_SERVICE_ID);
        log.debug("authority central service info: [{}]", JsonMapper.object2String(serviceInstance));
        String url = String.format(
                authorityCentralUrlFormat,
                serviceInstance.getHost(),
                serviceInstance.getPort()
        );

        log.info("request url and body: [{}], [{}]", url, requestBody);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JwtToken jwtToken = restTemplate.postForObject(url, new HttpEntity<>(requestBody, headers), JwtToken.class);
        if (jwtToken != null) {
            return jwtToken.getToken();
        }
        return null;
    }

    private String getBodyFromRequest(ServerHttpRequest request) {
        Flux<DataBuffer> body = request.getBody();
        AtomicReference<String> bodyReference = new AtomicReference<>();
        // 订阅缓冲区并消费request body
        body.subscribe(dataBuffer -> {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(dataBuffer.asByteBuffer());
            // 一定要使用 DataBufferUtils.release 方法释放已缓存的request body数据，否则会出现内存泄漏
            DataBufferUtils.release(dataBuffer);
            bodyReference.set(charBuffer.toString());
        });
        return bodyReference.get();
    }

}
