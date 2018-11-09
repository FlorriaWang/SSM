package com.rice.enums;

/**
 * @author wangfan
 * @Date 2018-08-03 13:53
 */
public enum  SeckillStatEnum {
    //如果在枚举类中添加新方法，则必须先定义枚举实例，以分号结尾，才能定义新方法
    SUCCESS(1,"秒杀成功"),
    END(0,"秒杀结束"),
    REPEAT_KILL(-1,"重复秒杀"),
    INNER_ERROR(-2,"系统异常"),
    DATE_REWRITE(-3,"数据篡改");

    private int state;
    private String stateInfo;

    SeckillStatEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

}
