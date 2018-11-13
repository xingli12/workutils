package com.xingli.dao;

import com.xingli.model.AssociationWordDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ProjectName: assist_search
 * @Package: com.iflytek.dao
 * @Author: xingli12
 * @Description:
 * @Date: Created in 2018-06-25 13:41
 * @Modified By:
 * @UpdateDate:
 * @Version:
 */

@Mapper
public interface AssociationWordDao {

    //查找联想词
    List<AssociationWordDto> selectAssociationWordList(AssociationWordDto associationWordDto);

    //查找同音词
    List<AssociationWordDto> selectHomonymWordList(AssociationWordDto associationWordDto);

    AssociationWordDto selectAssociationWord(AssociationWordDto associationWordDto);

    void addAssociationWord(AssociationWordDto associationWordDto);

    void updateAssociationWord(AssociationWordDto associationWordDto);

    //获取全部记录
    List<AssociationWordDto> getAllWords();

    //批量更新
    void batchUpdatePinyin(List<AssociationWordDto> associationWordDtoList);

}
