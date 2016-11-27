package com.blackbeard.common.mapper.order;

import com.blackbeard.common.pojo.order.NextOrder;
import com.blackbeard.common.pojo.order.NextOrderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface NextOrderMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_next_order
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    int countByExample(NextOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_next_order
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    int deleteByExample(NextOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_next_order
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_next_order
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    int insert(NextOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_next_order
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    int insertSelective(NextOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_next_order
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    List<NextOrder> selectByExample(NextOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_next_order
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    NextOrder selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_next_order
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    int updateByExampleSelective(@Param("record") NextOrder record, @Param("example") NextOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_next_order
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    int updateByExample(@Param("record") NextOrder record, @Param("example") NextOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_next_order
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    int updateByPrimaryKeySelective(NextOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_next_order
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    int updateByPrimaryKey(NextOrder record);
}