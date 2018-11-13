package com.xingli.service;


import com.xingli.dao.AssociationWordDao;
import com.xingli.model.AssociationWordDto;
import com.xingli.utils.ChangeToPinYinJP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ProjectName: assist_search
 * @Package: com.iflytek.service
 * @Author: xingli12
 * @Description:
 * @Date: Created in 2018-06-26 09:21
 * @Modified By:
 * @UpdateDate:
 * @Version:
 */
@Service
public class AssociationWordService {
    private static Logger log = LoggerFactory.getLogger(AssociationWordService.class);
    @Autowired
    private AssociationWordDao associationWordDao;
    /** *
     * @Description:  联想词汇查询
     * @param associationWordDto
     * @return: java.util.List<com.iflytek.model.AssociationWordDto>
     * @Date: Created in 14:28 2018/6/25/0025
     * @Modified By: xingli12
     */
    public List<AssociationWordDto> selectAssociationWordList(AssociationWordDto associationWordDto){
        List<AssociationWordDto> associationWordDtoList =  associationWordDao.selectAssociationWordList(associationWordDto);
        Integer length = associationWordDtoList.size();
        if(length < 10){
            associationWordDto.setNum(10 - length);
            List<AssociationWordDto> associationWordDtoList1 = associationWordDao.selectHomonymWordList(associationWordDto);
            associationWordDtoList.addAll(associationWordDtoList1);
        }
        return associationWordDtoList;
    }

    /** *
     * @Description: 更新联想词汇数据库
     * @param associationWordDto
     * @return: void
     * @Date: Created in 14:40 2018/6/25/0025
     * @Modified By: xingli12
     */
    public void updateAssociationWord(AssociationWordDto associationWordDto){

        AssociationWordDto associationWordDto1 = associationWordDao.selectAssociationWord(associationWordDto);

        java.util.Date date = new java.util.Date();
        if(associationWordDto1 != null){
            associationWordDto1.setNum(associationWordDto1.getNum()+ 1);
            associationWordDto1.setUpdateTime(date);
            associationWordDao.updateAssociationWord(associationWordDto1);
        }else {
            AssociationWordDto associationWordDto2 = new AssociationWordDto();
            associationWordDto2.setWord(associationWordDto.getWord());
            associationWordDto2.setCreateTime(date);
            associationWordDto2.setNum(1);
            associationWordDto2.setClassify("1");
            associationWordDto2.setWordPinyin(ChangeToPinYinJP.changeToTonePinYin(associationWordDto.getWord()));
            associationWordDto2.setWordShoup(ChangeToPinYinJP.changeToGetShortPinYin(associationWordDto.getWord()));
            associationWordDao.addAssociationWord(associationWordDto2);
        }
    }

    /** *
     * @Description: 更新数据库词条的拼音
     * @param 
     * @return: void
     * @Date: Created in 9:59 2018/6/26/0026
     * @Modified By: xingli12
     */
    public void batchUpdatePinyin(){
        List<AssociationWordDto> associationWordDtoList = associationWordDao.getAllWords();
        for (AssociationWordDto record: associationWordDtoList
             ) {
            String word = record.getWord();
            String wordPinyin = ChangeToPinYinJP.changeToTonePinYin(word);
            String wordShoup = ChangeToPinYinJP.changeToGetShortPinYin(word);
            record.setWordPinyin(wordPinyin);
            record.setWordShoup(wordShoup);
        }
        associationWordDao.batchUpdatePinyin(associationWordDtoList);
    }
}
