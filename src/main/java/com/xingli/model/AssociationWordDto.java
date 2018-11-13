package com.xingli.model;

import java.util.Date;

/**
 * @ProjectName: assist_search
 * @Package: com.iflytek.model
 * @Author: xingli12
 * @Description: 词汇联想
 * @Date: Created in 2018-06-25 11:09
 * @Modified By:
 * @UpdateDate:
 * @Version:
 */
public class AssociationWordDto {

    private Integer id;
    //联想词条
    private String word;

    //调用次数
    private Integer num;

    //创建时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    //分类
    private String classify;

    //拼音
    private String wordPinyin;

    //简拼
    private String wordShoup;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getWordPinyin() {
        return wordPinyin;
    }

    public void setWordPinyin(String wordPinyin) {
        this.wordPinyin = wordPinyin;
    }

    public String getWordShoup() {
        return wordShoup;
    }

    public void setWordShoup(String wordShoup) {
        this.wordShoup = wordShoup;
    }
}
