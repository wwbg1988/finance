package com.blackbeard.common.pojo.order;

import java.util.Date;

public class NextOrder {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_next_order.id
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_next_order.login_id
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    private Integer loginId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_next_order.currency_type
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    private String currencyType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_next_order.direction
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    private Integer direction;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_next_order.price
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    private String price;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_next_order.tick
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    private Integer tick;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_next_order.user_id
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    private Integer userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_next_order.open_time
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    private String openTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_next_order.close_time
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    private String closeTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_next_order.total
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    private Integer total;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_next_order.comment
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    private String comment;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_next_order.stat
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    private Integer stat;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_next_order.create_time
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_next_order.update_time
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_next_order.id
     *
     * @return the value of t_next_order.id
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_next_order.id
     *
     * @param id the value for t_next_order.id
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_next_order.login_id
     *
     * @return the value of t_next_order.login_id
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    public Integer getLoginId() {
        return loginId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_next_order.login_id
     *
     * @param loginId the value for t_next_order.login_id
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    public void setLoginId(Integer loginId) {
        this.loginId = loginId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_next_order.currency_type
     *
     * @return the value of t_next_order.currency_type
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    public String getCurrencyType() {
        return currencyType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_next_order.currency_type
     *
     * @param currencyType the value for t_next_order.currency_type
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType == null ? null : currencyType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_next_order.direction
     *
     * @return the value of t_next_order.direction
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    public Integer getDirection() {
        return direction;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_next_order.direction
     *
     * @param direction the value for t_next_order.direction
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_next_order.price
     *
     * @return the value of t_next_order.price
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    public String getPrice() {
        return price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_next_order.price
     *
     * @param price the value for t_next_order.price
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    public void setPrice(String price) {
        this.price = price == null ? null : price.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_next_order.tick
     *
     * @return the value of t_next_order.tick
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    public Integer getTick() {
        return tick;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_next_order.tick
     *
     * @param tick the value for t_next_order.tick
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    public void setTick(Integer tick) {
        this.tick = tick;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_next_order.user_id
     *
     * @return the value of t_next_order.user_id
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_next_order.user_id
     *
     * @param userId the value for t_next_order.user_id
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_next_order.open_time
     *
     * @return the value of t_next_order.open_time
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    public String getOpenTime() {
        return openTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_next_order.open_time
     *
     * @param openTime the value for t_next_order.open_time
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    public void setOpenTime(String openTime) {
        this.openTime = openTime == null ? null : openTime.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_next_order.close_time
     *
     * @return the value of t_next_order.close_time
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    public String getCloseTime() {
        return closeTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_next_order.close_time
     *
     * @param closeTime the value for t_next_order.close_time
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime == null ? null : closeTime.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_next_order.total
     *
     * @return the value of t_next_order.total
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    public Integer getTotal() {
        return total;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_next_order.total
     *
     * @param total the value for t_next_order.total
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    public void setTotal(Integer total) {
        this.total = total;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_next_order.comment
     *
     * @return the value of t_next_order.comment
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    public String getComment() {
        return comment;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_next_order.comment
     *
     * @param comment the value for t_next_order.comment
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_next_order.stat
     *
     * @return the value of t_next_order.stat
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    public Integer getStat() {
        return stat;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_next_order.stat
     *
     * @param stat the value for t_next_order.stat
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    public void setStat(Integer stat) {
        this.stat = stat;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_next_order.create_time
     *
     * @return the value of t_next_order.create_time
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_next_order.create_time
     *
     * @param createTime the value for t_next_order.create_time
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_next_order.update_time
     *
     * @return the value of t_next_order.update_time
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_next_order.update_time
     *
     * @param updateTime the value for t_next_order.update_time
     *
     * @mbggenerated Thu Jul 21 10:32:31 CST 2016
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}