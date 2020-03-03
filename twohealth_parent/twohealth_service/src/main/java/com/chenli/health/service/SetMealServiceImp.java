package com.chenli.health.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.chenli.health.dao.SetMealDao;
import com.chenli.health.entity.redisfinalData.RedisContont;
import com.chenli.health.entity.reuntmessageresult.PageResult;
import com.chenli.health.entity.reuntmessgafinal.MessageConstant;
import com.chenli.health.pojo.Setmeal;
import com.chenli.health.serviceinterface.SetMealService;
import com.chenli.health.uintl.QiniuUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class SetMealServiceImp implements SetMealService {

    @Autowired
    private SetMealDao setMealDao;
    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Value("${out_put_path}")
    private String outPutPath;
    /**
     * 添加套餐
     * @param setmeal
     * @param checkgroupIds
     */
    @Override
    public void add_setMeal(Setmeal setmeal, Integer[] checkgroupIds) {
        setMealDao.add_setMeal(setmeal);
        set_setMeal_checkgroup_guannian(setmeal.getId(),checkgroupIds);
        //将图片名称保存到Redis
        jedisPool.getResource().sadd(RedisContont.SETMEAL_PIC_DB_RESOURCE,setmeal.getImg());

        //新增套餐后需要重新生成静态页面
        generateMobileStaticHtml();
    }

    //生成静态页面
    public void generateMobileStaticHtml() {
        //准备模板文件中所需的数据
        List<Setmeal> setmealList = this.find_setMealAll();
        //生成套餐列表静态页面
        generateMobileSetmealListHtml(setmealList);
        //生成套餐详情静态页面（多个）
        generateMobileSetmealDetailHtml(setmealList);
    }

    //生成套餐列表静态页面
    public void generateMobileSetmealListHtml(List<Setmeal> setmealList) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("setmealList", setmealList);
        this.generateHtml("mobile_setmeal.ftl","m_setmeal.html",dataMap);
    }

    //生成套餐详情静态页面（多个）
    public void generateMobileSetmealDetailHtml(List<Setmeal> setmealList) {
        for (Setmeal setmeal : setmealList) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("setmeal", this.find_setmealById(setmeal.getId()));
            this.generateHtml("mobile_setmeal_detail.ftl",
                    "setmeal_detail_"+setmeal.getId()+".html",
                    dataMap);
        }
    }

    public void generateHtml(String templateName,String htmlPageName,Map<String, Object> dataMap){
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Writer out = null;
        try {
            // 加载模版文件
            Template template = configuration.getTemplate(templateName);
            // 生成数据
            File docFile = new File(outPutPath + "\\" + htmlPageName);
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile)));
            // 输出文件
            template.process(dataMap, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.flush();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
    @Override
    public PageResult find_setMealPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> page = setMealDao.find_setMealPage(queryString);
        long total = page.getTotal();
        List<Setmeal> result = page.getResult();

        return new PageResult(total, result);
    }

    /**
     * 添加时设置关联 在第三张表中
     * @param id
     * @param checkgroupIds
     */
    private void set_setMeal_checkgroup_guannian(Integer id, Integer[] checkgroupIds) {
        if(checkgroupIds != null && checkgroupIds.length > 0 ){
            for (Integer checkgroupId : checkgroupIds) {
                setMealDao.set_setMeal_checkgroup_guannian(id,checkgroupId);
            }
        }
    }
    //========================修改数据信息====================
    @Override
    public Setmeal find_setMealById(Integer id) {
        Setmeal setmeal = setMealDao.find_setMealById(id);
        return setmeal;
    }

    @Override
    public List<Integer> find_setMealAndCheckGroupByIds(Integer id) {
        return setMealDao.find_setMealAndCheckGroupByIds(id);
    }

    @Override
    public void edit_setMeal(Setmeal setmeal, Integer[] checkgroupIds) {

        //使用套餐id,查询数据库对应的套餐,获取数据库存放的img
        Setmeal img_db = setMealDao.find_setMealById(setmeal.getId());
        String imgDbImg = img_db.getImg();
        //如果页面传递的图片名称和数据库存放的图片名称不一致,说明图片更新,需要删除七牛云之前数据库的图片
        if(setmeal.getImg() !=null && !setmeal.getImg().equalsIgnoreCase(imgDbImg)){
            QiniuUtils.deleteFileFromQiniu(imgDbImg);//删除七牛云的图片
        }
        //解除之前的关联关系
        setMealDao.deleteAssociation(setmeal.getId());
        //重新建立关联关系
        setmealAndCheckGroup(setmeal.getId(),checkgroupIds);

        //更新套餐基本信息
        setMealDao.edit_setMeal(setmeal);

        // 重新使用Freemarker生成静态页面
        generateMobileStaticHtml();
    }

    private void setmealAndCheckGroup(Integer id, Integer[] checkgroupIds) {
        if(checkgroupIds != null && checkgroupIds.length >0 ){
            for (Integer checkgroupId : checkgroupIds) {
                setMealDao.set_setMeal_checkgroup_guannian(id,checkgroupId);
            }
        }
    }

    //=======================删除数据信息========================
    @Override
    public void delete_setMealById(Integer id) throws RuntimeException{
        //当前数据是否关联其他数据信息
       Long count =  setMealDao.find_setMealAndCheckGroup_count(id);
       if(count > 0 ){
           throw  new RuntimeException(MessageConstant.SETMEAL_CHECKGROUP_GUANNINA);
       }

       //查询数据库
        Setmeal setMealById = setMealDao.find_setMealById(id);
        String img = setMealById.getImg();

        //删除数据信息
        setMealDao.delete_setMealById(id);
        //删除七牛云图片
        if(img != null && !"".equals(img)) {
            QiniuUtils.deleteFileFromQiniu(img);
        }

        // 重新使用Freemarker生成静态页面
        generateMobileStaticHtml();
    }

    /**
     * 查询所有套餐
      * @return
     */
    @Override
    public List<Setmeal> find_setMealAll() {
        return setMealDao.fine_setMealAll();
    }

    @Override
    public Setmeal find_setmealById(Integer id) {
        Setmeal setmeal = setMealDao.find_setmealById(id);
        return setmeal;
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {
        List<Map<String,Object>> map = setMealDao.findSetmealCount();
        return  map;

    }
}
