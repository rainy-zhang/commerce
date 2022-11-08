package org.rainy.commerce.controller;

import org.rainy.commerce.service.SleuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 打印Sleuth中traceId和spanId <br>
 * trace: 可以看作是一个逻辑执行过程的整个链条 <br>
 * span: 是trace跟踪的基本单位
 * </p>
 *
 * @author zhangyu
 */
@RestController
@RequestMapping(value = "/sleuth")
public class SleuthController {
    
    private final SleuthService sleuthService;

    public SleuthController(SleuthService sleuthService) {
        this.sleuthService = sleuthService;
    }
    
    @GetMapping(value = "/trace-info")
    public void traceInfo() {
        sleuthService.traceInfo();
    }
    
}
