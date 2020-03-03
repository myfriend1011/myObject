package com.chenli.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.chenli.health.entity.reuntmessageresult.Result;
import com.chenli.health.entity.reuntmessgafinal.MessageConstant;
import com.chenli.health.pojo.OrderSetting;
import com.chenli.health.serviceinterface.OrderSettingService;
import com.chenli.health.uintl.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    public OrderSettingService orderSettingService;

    /**
     * 批量导入数据信息
     * @param excelFile
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile){
        try {
            //读取文件
            List<String[]> lists = POIUtils.readExcel(excelFile);

            if(lists != null && lists.size() >0 ){
                //创建预约表
                List<OrderSetting> orderSettings = new ArrayList<>();
                //遍历数据信息
                for (String[] list : lists) {
                    OrderSetting orderSetting = new OrderSetting(new Date(list[0]),Integer.parseInt(list[1]));
                    orderSettings.add(orderSetting);
                }
                //调用业务添加预约信息
                orderSettingService.add(orderSettings);
            }
        } catch (Exception e){
            e.printStackTrace();
            //响应失败
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        //响应成功
        return new Result(true,MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }

    //显示预约信息
    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date){

        try {
            List<Map> mapList =  orderSettingService.getOrderSettingByMonth(date);
            return  new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,mapList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }

    //预约设置
    @RequestMapping("/edit_orderSettingNumberByDate")
    public Result edit_orderSettingNumberByDate(@RequestBody OrderSetting orderSetting){
        try {
            //设置预约人数和已经预约人数的比较
            int num =  orderSettingService.find_orderSettingByDate(orderSetting.getOrderDate());
            if(orderSetting.getNumber() < num){
                return new Result(false,MessageConstant.ORDERSETTING_NUMBER_MIN);
            }
            //更新操作
            orderSettingService.edit_orderSettingNumberByDate(orderSetting);
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }
}
