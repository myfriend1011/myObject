package com.chenli.health.job;




import com.chenli.health.entity.redisfinalData.RedisContont;
import com.chenli.health.uintl.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class ImgClear {

    @Autowired
    private JedisPool jedisPool;
    //清理图片
    public void clearImg(){
        //计算redis中两个集合的差值，获取垃圾图片名称
        Set<String> set = jedisPool.getResource().sdiff(
                RedisContont.SETMEAL_PIC_RESOURCE,
                RedisContont.SETMEAL_PIC_DB_RESOURCE);
        Iterator<String> iterator = set.iterator();
        while(iterator.hasNext()){
            String pic = iterator.next();
            System.out.println("删除图片的名称是："+pic);
            //删除图片服务器中的图片文件
            QiniuUtils.deleteFileFromQiniu(pic);
            //删除redis中的数据
            jedisPool.getResource().srem(RedisContont.SETMEAL_PIC_RESOURCE,pic);
        }
    }
}
