package org.rainy.commerce.service;

import brave.Tracer;
import brave.propagation.TraceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 打印Sleuth中的TraceId和SpanId
 * </p>
 *
 * @author zhangyu
 */
@Slf4j
@Service
public class SleuthService {
    
    private final Tracer tracer;

    public SleuthService(Tracer tracer) {
        this.tracer = tracer;
    }
    
    public void traceInfo() {
        TraceContext context = tracer.currentSpan().context();
        log.info("Sleuth Trace Info traceId: [{}], spanId: [{}]", context.traceId(), context.spanId());
    }
    
}
