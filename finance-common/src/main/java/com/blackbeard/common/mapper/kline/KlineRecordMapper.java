package com.blackbeard.common.mapper.kline;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.blackbeard.common.pojo.kline.KlineRecord;
import com.blackbeard.common.pojo.kline.KlineRecordExample;

public interface KlineRecordMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_k_line_record
     *
     * @mbggenerated Sun Jun 12 15:21:36 CST 2016
     */
    int countByExample(KlineRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_k_line_record
     *
     * @mbggenerated Sun Jun 12 15:21:36 CST 2016
     */
    int deleteByExample(KlineRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_k_line_record
     *
     * @mbggenerated Sun Jun 12 15:21:36 CST 2016
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_k_line_record
     *
     * @mbggenerated Sun Jun 12 15:21:37 CST 2016
     */
    int insert(KlineRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_k_line_record
     *
     * @mbggenerated Sun Jun 12 15:21:37 CST 2016
     */
    int insertSelective(KlineRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_k_line_record
     *
     * @mbggenerated Sun Jun 12 15:21:37 CST 2016
     */
    List<KlineRecord> selectByExample(KlineRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_k_line_record
     *
     * @mbggenerated Sun Jun 12 15:21:37 CST 2016
     */
    KlineRecord selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_k_line_record
     *
     * @mbggenerated Sun Jun 12 15:21:37 CST 2016
     */
    int updateByExampleSelective(@Param("record") KlineRecord record, @Param("example") KlineRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_k_line_record
     *
     * @mbggenerated Sun Jun 12 15:21:37 CST 2016
     */
    int updateByExample(@Param("record") KlineRecord record, @Param("example") KlineRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_k_line_record
     *
     * @mbggenerated Sun Jun 12 15:21:37 CST 2016
     */
    int updateByPrimaryKeySelective(KlineRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_k_line_record
     *
     * @mbggenerated Sun Jun 12 15:21:37 CST 2016
     */
    int updateByPrimaryKey(KlineRecord record);
}