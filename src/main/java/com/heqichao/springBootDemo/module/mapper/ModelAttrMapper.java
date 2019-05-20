package com.heqichao.springBootDemo.module.mapper;

import com.heqichao.springBootDemo.module.entity.ModelAttr;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by heqichao on 2018-7-15.
 */
public interface ModelAttrMapper {

    @Delete("<script>"
            +"delete from model_attr where model_id = #{modelID} "
            +"</script>")
    int deleteByModelId( @Param("modelID") Integer modelID);

    @Delete("<script>"
            +"delete from model_attr where id in "
            + "<foreach  collection=\"list\" open=\"(\" close=\")\" separator=\",\" item=\"uid\" >"
            + "#{uid}"
            + "</foreach>"
            +"</script>")
    int deleteById(@Param("list") List<Integer> ids);

    @Select("<script>"
            +"select * from model_attr  where model_id = #{modelID} and model_type='R' order by order_no"
            +"</script>")
    List<ModelAttr> queryByModelId(@Param("modelID") Integer modelID);
    
    //查询命令模板列表
    @Select("<script>"
    		+"select * from model_attr  where model_id = #{modelID} and model_type='W' order by order_no"
    		+"</script>")
    List<ModelAttr> queryCMDAttrByModelId(@Param("modelID") Integer modelID);
    
    //查询命令模板列表带格式
    @Select("<script>"
    		+"select  ma.id, ma.attr_name,ma.num_max,ma.num_min,ma.expression,ma.unit,ot1.param_value dataName,ot2.param_value valueName,ma.memo,ma.data_type,ma.value_type "
    		+ " from model_attr ma   "
    		+ " left join option_tree ot1 on ma.data_type=ot1.param_key and ot1.code='ModelAttrDataType' "
    		+ " left join option_tree ot2 on ma.value_type=ot2.param_key and ot2.code='ModelAttrValueType' "
    		+ " where ma.model_id = #{modelID} and ma.model_type='W' order by ma.order_no"
    		+"</script>")
    List<ModelAttr> queryCMDAttrByModelIdFMT(@Param("modelID") Integer modelID);
    
    // Muzzy
    @Select("<script>"
    		+"select * from model_attr  where model_id = #{modelID} and data_type in ('INT_TYPE','SWITCH_TYPE','ALARM_TYPE','ENUMERATION_TYPE') and model_type = 'R' order by order_no"
    		+"</script>")
    List<ModelAttr> queryAttrByModelId(@Param("modelID") Integer modelID);
    
    @Select("<script>"
    		+"select id,model_id,attr_name,data_type,expression from model_attr  where id = #{attrID} "
    		+"</script>")
    ModelAttr getUserAttrByAttrId(@Param("attrID") Integer attrID);
    // End Muzzy

    @Insert("<script>"
            +"insert into model_attr (add_date,udp_date,add_uid,udp_uid,model_id,attr_name,data_type,value_type,number_format,unit,expression,memo,order_no,model_type) values "
            + "<foreach  collection=\"list\"  separator=\",\" item=\"o\" >"
            + "(#{o.addDate},#{o.udpDate},#{o.addUid},#{o.udpUid},#{o.modelId},#{o.attrName},#{o.dataType},#{o.valueType},#{o.numberFormat},#{o.unit},#{o.expression},#{o.memo},#{o.orderNo},'R')"
            + "</foreach>"
            +"</script>")
    int saveModelAttr(@Param("list") List<ModelAttr> list);
    

    @Update("update model_attr set  add_date = #{addDate},udp_date=#{udpDate},add_uid =#{addUid},udp_uid =#{udpUid},"
            + "model_id =#{modelId},attr_name =#{attrName},data_type =#{dataType},value_type =#{valueType}, "
            +"number_format =#{numberFormat},unit =#{unit},expression =#{expression},memo =#{memo},order_no=#{orderNo}"
            +" where id=#{id}")
    void updateModelById(ModelAttr attr);
    
    //插入命令模板
    @Insert("<script>"
    		+"insert into model_attr (add_date,udp_date,add_uid,udp_uid,model_id,attr_name,data_type,value_type,expression,num_max,unit,num_min,memo,order_no,model_type) values "
    		+ "<foreach  collection=\"list\"  separator=\",\" item=\"o\" >"
    		+ "(#{o.addDate},#{o.udpDate},#{o.addUid},#{o.udpUid},#{o.modelId},#{o.attrName},#{o.dataType},#{o.valueType},#{o.expression},#{o.numMax},#{o.unit},#{o.numMin},#{o.memo},#{o.orderNo},'W')"
    		+ "</foreach>"
    		+"</script>")
    int saveCMDModelAttr(@Param("list") List<ModelAttr> list);
    
    //更新命令模板
    @Update("update model_attr set  udp_date=#{udpDate},udp_uid =#{udpUid},"
    		+ "model_id =#{modelId},attr_name =#{attrName},data_type =#{dataType},value_type =#{valueType}, expression =#{expression},"
    		+"num_max =#{numMax},unit =#{unit},num_min =#{numMin},memo =#{memo},order_no=#{orderNo}"
    		+" where id=#{id}")
    void updateCMDModelById(ModelAttr attr);


}
