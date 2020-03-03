package com.chenli.health.controllerApp;

import com.alibaba.dubbo.config.annotation.Reference;
import com.chenli.health.app.RedisMessageConstant;
import com.chenli.health.entity.reuntmessageresult.Result;
import com.chenli.health.entity.reuntmessgafinal.MessageConstant;
import com.chenli.health.pojo.Member;
import com.chenli.health.serviceinterface.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginMobileController {

    @Reference
    private MemberService memberService;
    @Autowired
    private JedisPool jedisPool;
    public Result check(HttpServletResponse response, @RequestBody Map map){
        String telephone = (String)map.get("telephone");
        String validateCode = (String)map.get("validateCode");
        //1:从redis中获取缓存的验证码,判断验证码输入是否正确
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        if(codeInRedis == null || !codeInRedis.equals(validateCode)){
            //验证码输入错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }else{
            //验证码输入正确
            Member member = memberService.find_memberByTelephone(telephone);
            if(member ==null){
                Member member1 = new Member();
                member1.setPhoneNumber(telephone);
                member1.setRegTime(new Date());

                memberService.add_member(member1);
            }

            //登录成功
            //写入cookie,跟踪用户,用于分布式系统单点登录
            Cookie cookie = new Cookie("login_member_telephone",telephone);
            cookie.setPath("/");
            cookie.setMaxAge(30*60*60*24);
            response.addCookie(cookie);
            return new Result(true,MessageConstant.LOGIN_SUCCESS);
        }
    }
}
