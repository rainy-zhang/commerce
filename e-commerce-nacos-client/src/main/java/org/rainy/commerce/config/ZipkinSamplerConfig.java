package org.rainy.commerce.config;

import brave.sampler.RateLimitingSampler;
import brave.sampler.Sampler;
import org.springframework.cloud.sleuth.sampler.ProbabilityBasedSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 以代码的方式调整Zipkin的抽样策略
 * </p>
 *
 * @author zhangyu
 */
@Configuration
public class ZipkinSamplerConfig {
    
    @Bean
    public Sampler rateSampler() {
        return RateLimitingSampler.create(100);
    }
    
    @Bean
    public Sampler probabilitySampler() {
        return ProbabilityBasedSampler.create(0.5f);
    }
    
}
