package com.rice.service;

import com.rice.dto.SeckillExecution;
import com.rice.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author wangfan
 * @Date 2018-08-03 14:05
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})
public class SeckillServiceTest {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() {
        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list = {}",list);
    }

    @Test
    public void getById() {
        long seckillId = 1002;
        Seckill seckill = seckillService.getById(seckillId);
        logger.info("seckill = {}",seckill);
    }

    @Test
    public void executeSeckill() {
        long seckillId = 1000;
        long userPhone = 1881208171l;
        SeckillExecution seckillExecution = null;
        try {
            seckillExecution = seckillService.executeSeckill(seckillId,userPhone);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        logger.info("seckillExecution = {}",seckillExecution);
    }
}