package com.chenli.health.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.chenli.health.dao.OrderSettingDao;
import com.chenli.health.pojo.OrderSetting;
import com.chenli.health.serviceinterface.OrderSettingService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@Service
public class OrderSettingServiceImp implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;
    /**
     * 业务层实现
     * 批量导入预约信息
     * @param orderSettings
     */
    @Override
    public void add(List<OrderSetting> orderSettings) {
        if(orderSettings != null && orderSettings.size() > 0 ){
            for (OrderSetting orderSetting : orderSettings) {
               Long count =  orderSettingDao.find_orderSettingById(orderSetting.getId());
               //数据已成在
               if(count > 0){
                   //更新数据
                   orderSettingDao.update_orderSetting(orderSetting);
               }else{
                   orderSettingDao.add_orderSetting(orderSetting);
               }
            }
        }
    }


    @Override
    public List<Map> getOrderSettingByMonth(String date) {

        String dateBegin = date+"-01";//19-3-1
        String dateEnd = date+"-31";//19-3-31

        Map<String,String> map = new HashMap<>();
        map.put("dateBegin",dateBegin);
        map.put("dateEnd",dateEnd);

        //查询当前月信息的数据信息
        List<OrderSetting> settingList = orderSettingDao.find_orderSetting(map);

        List<Map> orderdate = new ArrayList<>();
        //遍历当前月信息
        for (OrderSetting orderSetting : settingList) {
            Map map1 = new HashMap();
            map1.put("orderDate",orderSetting.getOrderDate().getDate());
            map1.put("number",orderSetting.getNumber());
            map1.put("reservations",orderSetting.getReservations());
            orderdate.add(map1);
        }
        return orderdate;
    }

    @Override
    public void edit_orderSettingNumberByDate(OrderSetting orderSetting) {
        orderSettingDao.update_orderSetting(orderSetting);
    }

    @Override
    public int find_orderSettingByDate(Date orderDate) {
        return orderSettingDao.find_orderSettingByDate(orderDate);
    }
}
