package com.chenli.health.dao;

import com.chenli.health.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {

    Long find_orderSettingById(Integer id);

    void update_orderSetting(OrderSetting orderSetting);

    void add_orderSetting(OrderSetting orderSetting);

    List<OrderSetting> find_orderSetting(Map<String, String> map);


    int find_orderSettingByDate(Date orderDate);

    OrderSetting find_orderSettingByDateReturnObject(Date date);


    void update_orderSettingByOrderDate(Date date);
}
