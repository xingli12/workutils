package com.xingli.web;


import com.alibaba.fastjson.JSON;
import com.xingli.model.AssociationWordDto;
import com.xingli.model.Result;
import com.xingli.model.ResultDto;
import com.xingli.service.AssociationWordService;
import com.xingli.utils.ChangeToPinYinJP;
import com.xingli.utils.StringUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by Administrator on 2017/12/18.
 *
 * @update by lws 2018-02-28 更新最新的分词工具，直接调用内核接口
 */
@RestController
@RequestMapping(value = "/search")
public class SearchCotroller {
//    private static Logger log = LoggerFactory.getLogger(SearchCotroller.class);



    @Autowired
    private AssociationWordService associationWordService;


    /**a
     *  医疗词汇联想功能
     * @param queryString
     * @param isEnd
     * @return
     */
    @RequestMapping(value = "/AssociationWord", method = RequestMethod.GET)
    @ApiOperation(value = "通过输入的查询医疗词汇获取联想词汇列表", notes = "通过输入的查询医疗词汇获取联想词汇列表，后台更新词库")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "queryString", value = "查询内容", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "isEnd", value = "是否结束", dataType = "Boolean",defaultValue = "false")
    })
    public String AssociationWord(String queryString,Boolean isEnd) {
//        log.error("----------------------------AssociationWords调用：" + queryString + "------------------");
        ResultDto resultDto = new ResultDto();
        Result result = new Result();
        try {
            if (StringUtil.isBlank(queryString)) {
                result.setCode(402);
                result.setResult("查询字段异常");
            } else{
                AssociationWordDto associationWordDto = new AssociationWordDto();
                associationWordDto.setWord(queryString);
                associationWordDto.setWordPinyin(ChangeToPinYinJP.changeToTonePinYin(queryString));
//                associationWordDto.setWordShoup(ChangeToPinYinJP.changeToGetShortPinYin(queryString));
                //isEnd默认为false
                if (Boolean.TRUE != isEnd){
                    List<AssociationWordDto> associationWordDtoList =associationWordService.selectAssociationWordList(associationWordDto);
                    resultDto.setData(associationWordDtoList);
                    result.setCode(0);
                    result.setMessage("success");
                }else {
                    associationWordService.updateAssociationWord(associationWordDto);
                    result.setCode(0);
                    result.setMessage("success");
                }

            }
        } catch (Exception e){
//            log.error("调用接口异常", e);
            result.setCode(401);
            result.setMessage("调用接口异常");
        }
        result.setResult(resultDto);
        result.setTimestamp();
        return JSON.toJSONString(result);
    }


    @RequestMapping(value = "/batchUpdatePinyin", method = RequestMethod.PUT)
    @ApiOperation(value = "更新数据库中词条的拼音")
    public void batchUpdatePinyin(){
//        log.error("----------------------------batchUpdatePinyin调用：" + "------------------");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("开始更新数据库词条拼音" +  df.format(new Date()));
        associationWordService.batchUpdatePinyin();
        System.out.println("结束更新数据库词条拼音" +  df.format(new Date()));
    }




}
