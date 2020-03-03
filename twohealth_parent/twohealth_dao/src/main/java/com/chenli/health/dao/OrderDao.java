package com.chenli.health.dao;

import com.chenli.health.pojo.Order;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    List<Order> find_orderByCondition(Order order);

    void add_order(Order order);

    Map find_orderById4Detail(Integer id);

    Integer findOrderCountByDate(String today);

    Integer findOrderCountBetweenDate(Map<String, Object> weekMap);

    Integer findVisitsCountByDate(String today);

    Integer findVisitsCountAfterDate(Map<String, Object> weekMap2);

    List<Map> findHotSetmeal();
}
