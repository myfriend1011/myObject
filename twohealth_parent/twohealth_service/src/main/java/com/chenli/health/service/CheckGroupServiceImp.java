package com.chenli.health.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.chenli.health.dao.CheckGroupDao;
import com.chenli.health.entity.reuntmessageresult.PageResult;
import com.chenli.health.entity.reuntmessgafinal.MessageConstant;
import com.chenli.health.pojo.CheckGroup;
import com.chenli.health.serviceinterface.CheckGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class CheckGroupServiceImp implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;
    @Override
    public void add_checkGroup(CheckGroup checkGroup, Integer[] checkItemIds) {
        //添加检查组
       checkGroupDao.add_checkGroup(checkGroup);
       //添加第三表关联
       update_checkGroupAndCheckItem(checkGroup.getId(),checkItemIds);
    }
    /**
     * 设置关联第三章表
     */
    public void update_checkGroupAndCheckItem( Integer id,Integer [] checkItemIds){
        if(checkItemIds != null && checkItemIds.length > 0){
            for (Integer checkItemId : checkItemIds) {
                checkGroupDao.update_checkGroupAndCheckItem(id,checkItemId);
            }
        }

    }

    /**
     * 分页查询数据
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult find_checkGroups(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckGroup> page = checkGroupDao.find_checkGroups(queryString);

        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public CheckGroup find_checkGroupById(Integer id) {
        return checkGroupDao.find_checkGroupId(id);
    }

    @Override
    public List<Integer> find_checkGroup_checkItem_ByIds(Integer id) {
        return checkGroupDao.find_checkGroup_checkItem_ByIds(id);
    }

    @Override
    public void edit_checkGroup(CheckGroup checkGroup, Integer[] ids) {
        //更新之前先解除关联关系
        checkGroupDao.delete_checkGroup_checkItem_guannian(checkGroup.getId());
        //设置新的关联关系
        setCheckGroup_checkItem_guannian(checkGroup.getId(),ids);
        //修改后保存数据信息
        checkGroupDao.edit_to_save_checkGroup(checkGroup);
    }


    @Override
    public void delete_checkGroupById(Integer id)throws RuntimeException {
        //查看该数据是否有和其他数据信息关联
        Long count = checkGroupDao.find_checkGroup_checkItem_count(id);
        if(count > 0 ){
            throw new RuntimeException(MessageConstant.CHECKGROUP_GUANNIAN_CHECKITEM);
        }
        checkGroupDao.delete_checkGroupById(id);
    }

    @Override
    public List<CheckGroup> find_checkGroupAll() {
        return checkGroupDao.find_checkGroupAll();
    }

    private void setCheckGroup_checkItem_guannian(Integer idCg, Integer[] ids) {
        if(ids.length > 0){
            for (Integer id : ids) {
                checkGroupDao.setCheckGroup_checkItem_guannain(idCg,id);
            }
        }

    }

}
