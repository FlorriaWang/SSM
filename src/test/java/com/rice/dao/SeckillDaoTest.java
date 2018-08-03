package com.rice.dao;

import com.rice.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

/**
 * @author wangfan
 * @Date 2018-07-26 14:37
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
    @Autowired
    private SeckillDao seckillDao;
    @Test
    public void queryById(){
        long seckillId = 1000L;
        Seckill seckill = seckillDao.queryById(seckillId);
        System.out.println(seckill.getName());
        System.out.println(seckill);
    }

    @Test
    public void queryAll(){
        List<Seckill> seckills = seckillDao.queryAll(0,100);
        for (Seckill seckill : seckills){
            System.out.println(seckill);
        }
    }

    @Test
    public void reduceNumber(){
        long seckillId = 1000L;
        Date killTime = new Date();
        int resuleCount = seckillDao.reduceNumber(seckillId,killTime);
        System.out.println("resuleCount:" + resuleCount);
    }

}
