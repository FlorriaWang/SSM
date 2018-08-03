package com.rice.dao;

import com.rice.entity.Seckill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author wangfan
 * @Date 2018-07-25 17:15
 */
public interface SeckillDao {
    /**
     * 减少库存
     * @param seckillId
     * @param date
     * @return
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime")Date killTime) ;

    /**
     * 根据id查询商品信息
     * @param seckillId
     * @return
     */
    Seckill queryById(long seckillId);

    /**
     * 根据偏移量查询秒杀商品列表
     * @param offset
     * @param limit
     * @return
     */
    List<Seckill> queryAll(@Param("offset") int offset,@Param("limit") int limit);
}
