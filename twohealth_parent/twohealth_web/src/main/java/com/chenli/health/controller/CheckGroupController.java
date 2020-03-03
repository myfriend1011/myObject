package com.chenli.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.chenli.health.entity.querycondition.QueryPageBean;
import com.chenli.health.entity.reuntmessageresult.PageResult;
import com.chenli.health.entity.reuntmessageresult.Result;
import com.chenli.health.entity.reuntmessgafinal.MessageConstant;
import com.chenli.health.pojo.CheckGroup;
import com.chenli.health.serviceinterface.CheckGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/checkGroup")
public class CheckGroupController {

    //订阅
    @Reference
    private CheckGroupService checkGroupService;
    /**
     * 添加数据信息
     */
    @RequestMapping("/add_checkGroup")
    public Result add_checkGroup(@RequestBody CheckGroup checkGroup,Integer[] checkItemIds){
        try {
            checkGroupService.add_checkGroup(checkGroup,checkItemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    //分页查询
    @RequestMapping("/find_checkGroups")
    public PageResult find_checkGroups(@RequestBody QueryPageBean queryPageBean){
        try {
            PageResult pageResult = checkGroupService.find_checkGroups(queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());
            if(pageResult != null){
                return pageResult;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //根据id查询对象
    @RequestMapping("/find_checkGroupById")
    public Result find_checkGroupById(Integer id){
        try {
            CheckGroup checkGroups =  checkGroupService.find_checkGroupById(id);
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroups);
        }catch (RuntimeException e){
            return new Result(false,e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            return  new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }

    }

    //检查组与检查项关联
    @RequestMapping("/find_checkGroup_checkItem_ByIds")
    public List<Integer> find_checkGroup_checkItem_ByIds(Integer id){
        try {
            List<Integer> checkItemIds = checkGroupService.find_checkGroup_checkItem_ByIds(id);
            return checkItemIds;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //编辑
    @RequestMapping("/edit_checkGroup")
    public Result edit_checkGroup(@RequestBody CheckGroup checkGroup,Integer [] ids){
        try {
            checkGroupService.edit_checkGroup(checkGroup,ids);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
        return  new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS);
    }

    //根据id删除
    @RequestMapping("/delete_checkGroupById")
    public Result delete_checkGroupById(Integer id){
        try {
            checkGroupService.delete_checkGroupById(id);
        }catch (RuntimeException ex){
            return new Result(false,ex.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }

    //查询所有数据信息
    @RequestMapping("/find_checkGroupAll")
    public Result find_checkGroupAll(){
        try {
         List<CheckGroup> checkGroups=   checkGroupService.find_checkGroupAll();
         return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkGroups);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }
}
