package com.chenli.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.chenli.health.entity.querycondition.QueryPageBean;
import com.chenli.health.entity.reuntmessageresult.PageResult;
import com.chenli.health.entity.reuntmessageresult.Result;
import com.chenli.health.entity.reuntmessgafinal.MessageConstant;
import com.chenli.health.pojo.CheckItem;
import com.chenli.health.serviceinterface.CheckItemServiceInterface;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/checkItem")
public class CheckItemController {

    @Reference
    private CheckItemServiceInterface checkItemServiceImp;

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/find_checkItem")
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    public PageResult find_checkItem(@RequestBody QueryPageBean queryPageBean){

         PageResult checkItem = checkItemServiceImp.find_CheckItem(queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
        return checkItem;
    }

    /**
     * 添加页面
     * @param checkItem
     * @return
     */
    @RequestMapping("/add_checkItem")
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")
    public Result add_checkItem(@RequestBody CheckItem checkItem){
        try {
            checkItemServiceImp.add_checkItem(checkItem);
            return  new Result(true,MessageConstant.ADD_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_CHECKITEM_FAIL);
        }
    }

    /**
     * 根据id 删除数据
     * @param id
     * @return
     */
    @RequestMapping("/delete_checkItem")
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")
    public Result delete_checkItem(Integer id){
        try {
            checkItemServiceImp.delete_checkItem(id);
            return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
        }catch (RuntimeException ex){
            return new Result(false,ex.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }
    }

    /**
     * 根据id查询数据
     * @param id
     * @return
     */
    @RequestMapping("/find_checkItemById")
    public Result find_checkItemById(Integer id){
        try {
           CheckItem checkItem =  checkItemServiceImp.find_checkItemById(id);
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    @RequestMapping("/edit_checkItem")
    @PreAuthorize("hasAuthority('CHECKITEM_EDIT')")
    public Result edit_checkItem(@RequestBody CheckItem checkItem){
        try {
            checkItemServiceImp.edit_checkItem(checkItem);
            return  new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }
    }

    /**
     * 查询所有检查项
     * @return
     */
    @RequestMapping("/find_checkItems")
    public Result find_checkItems(){
        try {
            List<CheckItem> checkItems =  checkItemServiceImp.find_checkItems();
            if(checkItems != null && checkItems.size() > 0){
                return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItems);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
        return  new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
    }
}
