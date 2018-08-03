package com.rice.service;

import com.rice.dto.SeckillExecution;
import com.rice.entity.Seckill;

import java.util.List;

/**
 * @author wangfan
 * @Date 2018-07-26 16:22
 */
public interface SeckillService {
    /**
     * 查询全部的秒杀记录
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     *查询单个秒杀记录
     * @param seckillId
     * @return
     */
    Seckill getById(long seckillId);

    /**
     * 秒杀执行操作
     * @param seckillId
     * @param userPhone
     * @return
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone) throws Exception;
}
