package org.rainy.commerce.controller;

import org.rainy.commerce.service.NacosClientService;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangyu
 */
@RestController
@RequestMapping(value = "/nacos-client")
public class NacosClientController {

    private final NacosClientService nacosClientService;

    public NacosClientController(NacosClientService nacosClientService) {
        this.nacosClientService = nacosClientService;
    }

    @GetMapping(value = "/clients/{serviceId}")
    public List<ServiceInstance> clients(@PathVariable("serviceId") String serviceId) {
        return nacosClientService.getNacosClientInfo(serviceId);
    }

}
