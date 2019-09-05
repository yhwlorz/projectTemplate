package com.yhw.dao.demo;

import com.yhw.domain.demo.DemoEntity;
import com.yhw.domain.demo.DemoEntity2;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * The interface Demo dao.
 *
 * @author 杨怀伟
 * @date 2018 -11-13 23:40:55
 */
public interface DemoDao {

    /**
     * 获取取消原因
     *
     * @param vcode 取消原因编码
     *
     * @return 一条取消原因记录 reason by vcode
     */
    public DemoEntity getReasonByVcode(int vcode);


    public void updateReasonByVcode(int vcode);

    /**
     * 根据id获取用户名称.
     *
     * @param id 主键id
     *
     * @return 用户名称
     */
    public String getUserNameById(String id);

    public String getName(int vcode);

    public String getName1(DemoEntity demoEntity);

    public String getName2(@Param("demoEntity") DemoEntity demoEntity, @Param("demoEntity2") DemoEntity2 demoEntity2);

    public String getName3(Map<Object, Object> params);

    public String getName4(List<Object> params);


}
