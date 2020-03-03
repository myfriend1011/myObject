package com.chenli.health.controllerApp;

import com.aliyuncs.exceptions.ClientException;
import com.chenli.health.app.RedisMessageConstant;
import com.chenli.health.app.SMSUtils;
import com.chenli.health.app.ValidateCodeUtils;
import com.chenli.health.entity.reuntmessageresult.Result;
import com.chenli.health.entity.reuntmessgafinal.MessageConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    JedisPool jedisPool;
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){
        //生成6位数字验证码
        String code = ValidateCodeUtils.generateValidateCode4String(6);
        try {
            //发送短信
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code);
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        System.out.println("发送的手机验证码为:"+code);
        //将生成的验证码缓存到redis中
        jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_ORDER,5*60,code);
        //验证码发送成功
        return  new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
        //生成6位数字验证码
        String code = ValidateCodeUtils.generateValidateCode4String(6);
        try {
            //发送短信
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code);
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        System.out.println("发送的手机验证码为:"+code);
        //将生成的验证码缓存到redis中
        jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_LOGIN,5*60,code);
        //验证码发送成功
        return  new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
