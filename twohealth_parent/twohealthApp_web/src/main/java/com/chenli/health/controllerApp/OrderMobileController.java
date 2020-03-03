package com.chenli.health.controllerApp;

import com.alibaba.dubbo.config.annotation.Reference;
import com.chenli.health.app.RedisMessageConstant;
import com.chenli.health.entity.reuntmessageresult.Result;
import com.chenli.health.entity.reuntmessgafinal.MessageConstant;
import com.chenli.health.pojo.CheckGroup;
import com.chenli.health.pojo.CheckItem;
import com.chenli.health.pojo.Order;
import com.chenli.health.pojo.Setmeal;
import com.chenli.health.serviceinterface.OrderService;
import com.chenli.health.serviceinterface.SetMealService;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderMobileController {
    @Reference
    private OrderService orderService;
    @Autowired
    private JedisPool  jedisPool;

    @Reference
    private SetMealService setMealService;

    @RequestMapping("/submit")
    public Result submitOrder(@RequestBody Map map){
        String telephone = (String) map.get("telephone");
        //从redis中获取缓存的验证码 key为手机号+RedisConstand.SENDTYPE_order
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        //获取客户端输入时的验证码
        String validateCode = (String)map.get("validateCode");
        //比较redis中和输入中的验证码
        if(codeInRedis == null || !codeInRedis.equals(validateCode)){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        Result result = null;

        //添加微信预约
        try {
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            result = orderService.order(map);
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }

        return result;
    }


    @RequestMapping("/find_orderById")
    public Result find_orderById(Integer id){
        Map map = null;

        try {
            map = orderService.find_orderById4detail(id);
            //查询预约信息成功
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            //查询预约信息失败
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    // 使用订单id，导出套餐信息
    @RequestMapping(value = "/exportSetmealInfo")
    public Result exportSetmealInfo(Integer id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 查询出 满足当前条件 结果数据
        // 使用订单ID，查询订单信息（需要包括套餐ID）
        Map map = orderService.find_orderById4detail(id);
        // 获取套餐ID
        Integer setmealId = (Integer)map.get("setmealId");
        // 使用套餐ID，查询套餐信息
        Setmeal setmeal = setMealService.find_setmealById(setmealId);
        // 下载导出
        // 设置头信息
        response.setContentType("application/pdf");
        String filename = "exportPDF.pdf";

        // 设置以附件的形式导出
        response.setHeader("Content-Disposition",
                "attachment;filename=" + filename);

        // 生成PDF文件
        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // 设置表格字体
        BaseFont cn = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H",false);
        Font font = new Font(cn, 10, Font.NORMAL, Color.BLUE);

        // 写PDF数据
        // 输出订单和套餐信息
        // 体检人
        document.add(new Paragraph("体检人："+(String)map.get("member"), font));
        // 体检套餐
        document.add(new Paragraph("体检套餐："+(String)map.get("setmeal"), font));
        // 体检日期
        document.add(new Paragraph("体检日期："+(String)map.get("orderDate"), font));
        // 预约类型
        document.add(new Paragraph("预约类型："+(String)map.get("orderType"), font));

        // 向document 生成pdf表格
        Table table = new Table(3);//创建3列的表格

        // 写表头
        table.addCell(buildCell("项目名称", font));
        table.addCell(buildCell("项目内容", font));
        table.addCell(buildCell("项目解读", font));
        // 写数据
        for (CheckGroup checkGroup : setmeal.getCheckGroups()) {
            table.addCell(buildCell(checkGroup.getName(), font));
            // 组织检查项集合
            StringBuffer checkItems = new StringBuffer();
            for (CheckItem checkItem : checkGroup.getCheckItems()) {
                checkItems.append(checkItem.getName()+"  ");
            }
            table.addCell(buildCell(checkItems.toString(), font));
            table.addCell(buildCell(checkGroup.getRemark(), font));
        }
        // 将表格加入文档
        document.add(table);
        document.close();
        return null;
    }
    // 传递内容和字体样式，生成单元格
    private Cell buildCell(String content, Font font)
            throws BadElementException {
        Phrase phrase = new Phrase(content, font);
        return new Cell(phrase);
    }
}
