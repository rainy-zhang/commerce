package org.rainy.commerce.config;

import io.jsonwebtoken.lang.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * <p>
 * 将动态路由事件推送给Gateway
 * </p>
 *
 * @author zhangyu
 */
@Slf4j
@Component
public class DynamicRouteEventPublisher implements ApplicationEventPublisherAware {

    // 写路由信息
    private final RouteDefinitionWriter definitionWriter;

    // 读路由信息
    private final RouteDefinitionLocator definitionLocator;

    // 事件发布
    private ApplicationEventPublisher eventPublisher;

    public DynamicRouteEventPublisher(RouteDefinitionWriter definitionWriter, RouteDefinitionLocator definitionLocator) {
        this.definitionWriter = definitionWriter;
        this.definitionLocator = definitionLocator;
    }

    @Override
    public void setApplicationEventPublisher(@NonNull ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    /**
     * 新增路由信息
     * @param definition 路由信息
     */
    public void addRouteDefinition(RouteDefinition definition) {
        // 保存路由配置并发布
        this.definitionWriter.save(Mono.just(definition)).subscribe();
        // 发布事件通知给Gateway，同步新增的路由定义
        this.eventPublisher.publishEvent(new RefreshRoutesEvent(this));
        log.info("gateway add route: [{}]", definition);
    }

    /**
     * 批量更新路由信息
     * @param definitions 全量的路由信息
     */
    public void batchUpdateRouteDefinition(List<RouteDefinition> definitions) {
        List<RouteDefinition> existDefinitions = this.definitionLocator.getRouteDefinitions().buffer().blockFirst();
        if (!Collections.isEmpty(existDefinitions)) {
            existDefinitions.forEach(definition -> this.definitionWriter.delete(Mono.just(definition.getId())).subscribe());
        }
        definitions.forEach(definition -> this.definitionWriter.save(Mono.just(definition)).subscribe());
        this.eventPublisher.publishEvent(new RefreshRoutesEvent(this));
        log.info("gateway batch update route finish!");
    }


}
