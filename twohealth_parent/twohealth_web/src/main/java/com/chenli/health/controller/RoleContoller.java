package com.chenli.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.chenli.health.serviceinterface.RoleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName RoleContoller
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2020/2/13 16:04
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/role")
public class RoleContoller {

    @Reference// 订阅 dubbo注解
    RoleService roleService;
    
}
