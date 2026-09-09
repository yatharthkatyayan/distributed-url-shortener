package com.yatharth.distributedurlshortener.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InstanceController {

    @Value("${app.instance-id}")
    private String instanceId;

    @GetMapping("/instance")
    public String instance() {
        return instanceId;
    }
}