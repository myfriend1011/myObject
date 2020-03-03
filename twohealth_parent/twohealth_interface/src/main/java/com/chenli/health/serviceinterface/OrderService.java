package com.chenli.health.serviceinterface;

import com.chenli.health.entity.reuntmessageresult.Result;

import java.util.Map;

public interface OrderService {
    Result order(Map map) throws Exception;

    Map find_orderById4detail(Integer id) throws Exception;

}
