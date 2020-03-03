package com.chenli.health.serviceinterface;

import com.chenli.health.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    void add(List<OrderSetting> orderSettings);

    List<Map> getOrderSettingByMonth(String date);

    void edit_orderSettingNumberByDate(OrderSetting orderSetting);

    int find_orderSettingByDate(Date orderDate);
}
