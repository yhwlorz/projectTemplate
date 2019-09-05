package com.yhw.domain.demo;

import lombok.Data;


/**
 * The type Demo entity.
 *
 * @author 杨怀伟
 * @date 2018 -11-13 23:41:28
 */
@Data
public class DemoEntity {

    public DemoEntity() {

    }


    /**
     * @date 2018 -11-13 23:41:28
     * @author 杨怀伟  The Vcode.
     */
    public int vcode;

    /**
     * @date 2018 -11-13 23:41:28
     * @author 杨怀伟  The Vname.
     */
//必须与sql字段名一致，否则get不到
    public String vname;

    public void init(){
        System.out.println ("初始化bean时执行了此方法");
    }

    public void initNo(){
        System.out.println ("不应该执行这个bean");
    }
    public void destory() {
        System.out.println ("销毁bean时执行了此方法");
    }
}
