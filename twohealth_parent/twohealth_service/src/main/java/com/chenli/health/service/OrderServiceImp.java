package com.chenli.health.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.chenli.health.dao.MemberDao;
import com.chenli.health.dao.OrderDao;
import com.chenli.health.dao.OrderSettingDao;
import com.chenli.health.entity.reuntmessageresult.Result;
import com.chenli.health.entity.reuntmessgafinal.MessageConstant;
import com.chenli.health.pojo.Member;
import com.chenli.health.pojo.Order;
import com.chenli.health.pojo.OrderSetting;
import com.chenli.health.serviceinterface.OrderService;
import com.chenli.health.uintl.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class OrderServiceImp implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;//预约表
    @Autowired
    private MemberDao memberDao;//会员表
    @Autowired
    private OrderDao orderDao;//预约订单表
    @Override
    public Result order(Map map) throws Exception {
        //检查当前日期是否进行了预约设置
        String orderDate = (String)map.get("orderDate");
        Date date = DateUtils.parseString2Date(orderDate);
        //根据date查询预约表是否有预约
        OrderSetting orderSetting =  orderSettingDao.find_orderSettingByDateReturnObject(date);

        //判断
        if(orderSetting ==null){
            return  new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //检查预约日期是否预约已满
        int number = orderSetting.getNumber();//可预约人数
        int reservations = orderSetting.getReservations();//已预约人数
        if(reservations >= number){
            return new Result(false,MessageConstant.ORDER_FULL);
        }

        //检查当前用户是否为会员,根据手机号判断
        String telephone = (String)map.get("telephone");
        Member memeber = memberDao.find_memeberByTelephone(telephone);
        //如果是会员,防止重复预约(一个会员,一个时间,一个套餐不能重复,否则是重复预约)
        if(memeber != null){
            Integer memeberId = memeber.getId();
            int setmealId = Integer.parseInt((String) map.get("setmealId"));
            Order order = new Order(memeberId,date,null,null,setmealId);
            List<Order> list =  orderDao.find_orderByCondition(order);
        }


        if (memeber == null){
            memeber = new Member();
            memeber.setName((String) map.get("name"));
            memeber.setPhoneNumber((String) map.get("telephone"));
            memeber.setIdCard((String) map.get("idCard"));
            memeber.setSex((String) map.get("sex"));
            memeber.setRegTime(new Date());
            //设置会员
            memberDao.add_member(memeber);
        }

        //可以预约,设置预约人数加一
        orderSettingDao.update_orderSettingByOrderDate(date);

        //保存预约信息到预约表
        Order order = new Order(memeber.getId(),date,(String)map.get("orderType"),Order.ORDERSTATUS_NO,Integer.parseInt((String) map.get("setmealId")));

        orderDao.add_order(order);

        return new Result(true,MessageConstant.ORDER_SUCCESS,order);
    }


    @Override
    public Map find_orderById4detail(Integer id)throws Exception {
        Map map= orderDao.find_orderById4Detail(id);
        if(map !=null){
            //处理日期格式
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate",DateUtils.parseDate2String(orderDate));
            return map;
        }
        return map;

    }
}
