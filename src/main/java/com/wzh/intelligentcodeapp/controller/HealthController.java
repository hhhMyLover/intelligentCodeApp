package com.wzh.intelligentcodeapp.controller;


import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping("/check")
    @Operation(summary = "检查服务是否正常")
    public String check(){
        return "success";
    }
}
