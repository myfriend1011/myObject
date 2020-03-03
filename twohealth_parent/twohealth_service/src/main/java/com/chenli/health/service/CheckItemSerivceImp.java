package com.chenli.health.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.chenli.health.dao.CheckItemDao;
import com.chenli.health.entity.reuntmessageresult.PageResult;
import com.chenli.health.pojo.CheckItem;
import com.chenli.health.serviceinterface.CheckItemServiceInterface;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class CheckItemSerivceImp implements CheckItemServiceInterface {

    @Autowired
    private CheckItemDao checkItemDao;


    /**
     * 添加数据
     * @param checkItem
     */
    @Override
    public void add_checkItem(CheckItem checkItem) {
        checkItemDao.add_checkItem(checkItem);
    }

    /**
     * 分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult find_CheckItem(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckItem> page = checkItemDao.find_CheckItem(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete_checkItem(Integer id)throws RuntimeException {
        //这个数据与其他数据是由有关联
        long count = checkItemDao.find_checkItem_checkgroud_there(id);
        if(count > 0){//如果true数据有关联
            throw  new RuntimeException("当前检查项被检查组引用，不能删除");
        }
        //执行删除
        checkItemDao.delete_checkItem(id);
    }

    @Override
    public CheckItem find_checkItemById(Integer id) {
       CheckItem checkItem =  checkItemDao.find_checkItemByid(id);
       return checkItem;
    }

    @Override
    public void edit_checkItem(CheckItem checkItem) {
        checkItemDao.edit_checkItem(checkItem);
    }

    @Override
    public List<CheckItem> find_checkItems() {
        return checkItemDao.find_checkItems();
    }
}
