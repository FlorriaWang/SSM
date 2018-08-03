package com.rice.dao;

import com.rice.entity.SuccessKilled;
import org.apache.ibatis.annotations.Param;

/**
 * @author wangfan
 * @Date 2018-07-25 17:19
 */
public interface SuccessKilledDao {
    /**
     * 插入秒杀成功的信息
     * @param seckillId
     * @param userPhone
     * @return
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone")long userPhone);

    /**
     * 根据秒杀商品的id查询明细SuccessKilled对象(该对象携带了Seckill秒杀产品对象)
     * @param seckillId
     * @param userPhone
     * @return
     */
    SuccessKilled queryByIdAndUser(@Param("seckillId") long seckillId, @Param("userPhone")long userPhone);
}
