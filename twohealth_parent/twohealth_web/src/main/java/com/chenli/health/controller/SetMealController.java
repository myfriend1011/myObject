package com.chenli.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.chenli.health.entity.querycondition.QueryPageBean;
import com.chenli.health.entity.redisfinalData.RedisContont;
import com.chenli.health.entity.reuntmessageresult.PageResult;
import com.chenli.health.entity.reuntmessageresult.Result;
import com.chenli.health.entity.reuntmessgafinal.MessageConstant;
import com.chenli.health.pojo.Setmeal;
import com.chenli.health.serviceinterface.SetMealService;
import com.chenli.health.uintl.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;


import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/setMeal")
public class SetMealController {

    @Reference
    private SetMealService setMealService;

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/upload")
    public Result  upload(@RequestParam("imgFile") MultipartFile imgFile){
        try {
            //获取原始文件
            String originalFilename = imgFile.getOriginalFilename();
            int lastIndexOf = originalFilename.lastIndexOf(".");

            //获取文件后缀
            String suffix = originalFilename.substring(lastIndexOf);

            //使用uuid随机产生文件名称,防止同名文件覆盖
            String filename = UUID.randomUUID().toString() + suffix;
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),filename);
            //图片上传成功
            Result result = new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,filename);

            //将上传图片名称存入Redis，基于Redis的Set集合存储
            jedisPool.getResource().sadd(RedisContont.SETMEAL_PIC_RESOURCE,filename);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }

    }

    //添加数据
    @RequestMapping("/add_setMeal")
    public Result add_setMeal(@RequestBody Setmeal setmeal,Integer [] checkgroupIds){
        try {
            setMealService.add_setMeal(setmeal,checkgroupIds);
            return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    //分页查询
    @RequestMapping("/find_setMealPage")
    public PageResult find_setMealPage(@RequestBody QueryPageBean queryPageBean){

            PageResult result = setMealService.find_setMealPage(queryPageBean.getCurrentPage(),
                                        queryPageBean.getPageSize(),
                                        queryPageBean.getQueryString());
            return result;
    }

    //+++++++++++++++++++编辑方法++++++++++++++++++++++++++==
    //根据id查询数据信息
    @RequestMapping("/find_setMealById")
    public Result find_setMealById(Integer id){
        try {
            Setmeal setmeal =  setMealService.find_setMealById(id);
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
    @RequestMapping("/find_setMealAndCheckGroupByIds")
    public List<Integer> find_setMealAndCheckGroupByIds(Integer ids){
       return setMealService.find_setMealAndCheckGroupByIds(ids);
    }

    //编辑数据信息
    @RequestMapping("/edit_setMeal")
    public Result edit_setMeal(@RequestBody Setmeal setmeal,Integer [] checkgroupIds){
        try {
            setMealService.edit_setMeal(setmeal,checkgroupIds);
            return new Result(true,MessageConstant.EDIT_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_SETMEAL_FAIL);
        }

    }
    //根据id 删除
    @RequestMapping("/delete_setMealById")
    public Result delete_setMealById(Integer id){
        try {
            setMealService.delete_setMealById(id);
            return new Result(true,MessageConstant.DELETE_SETMEAL_SUCCESS);
        }catch (RuntimeException ex){
            return new Result(false,ex.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_SETMEAL_FAIL);
        }
    }

}
