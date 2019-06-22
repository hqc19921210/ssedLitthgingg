package com.heqichao.springBootDemo.module.mapper;
import com.heqichao.springBootDemo.module.entity.Model;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by heqichao on 2018-7-15.
 */
public interface ModelMapper {

    @Delete("<script>"
            +"delete from model where id = #{modelID} "
            +"</script>")
    int deleteByModelId(@Param("modelID") Integer modelID);

    @Select("<script>"
            +"select * from model  where id = #{modelID}   "
            +"</script>")
     List<Model> queryByModelId(@Param("modelID") Integer modelID);

    @Select("<script>"
            +"select count(1) from model  where model_name = #{modelName}   "
            + "<if test =\"modelID !=null  \"> and id != #{modelID} </if>"
            +"</script>")
    Integer queryCountByModelName(@Param("modelID") Integer modelID,@Param("modelName") String modelName);

    @Insert("<script>"
            +"insert into model (id,add_date,udp_date,add_uid,udp_uid,model_name) values (#{id},#{addDate},#{udpDate},#{addUid},#{udpUid},#{modelName})"
            +"</script>")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    int saveModel(Model model);

    @Update("<script>"
            +"update model set model_name =#{modelName},udp_uid=#{udpId},udp_date=#{udpDate} where id=#{modelID}"
            +"</script>")
    void updateModelName(@Param("modelID") Integer modelID, @Param("modelName") String modelName, @Param("udpId") Integer udpId, @Param("udpDate") Date udpDate);

    @Select("<script>"
            +"select * from model  where 1=1  "
            + "<if test =\"modelName !=null  and modelName!='' \"> and model_name like CONCAT(CONCAT('%',#{modelName}),'%')  </if>"
            +" order by udp_date desc"
            +"</script>")
    List<Model> queryAll(@Param("modelName")String modelName);


    @Select("<script>"
            +"select * from model  where 1=1  and add_uid in "
            + "<foreach  collection=\"list\" open=\"(\" close=\")\" separator=\",\" item=\"uid\" >"
            + "#{uid}"
            + "</foreach>"
            + "<if test =\"modelName !=null  and modelName!='' \"> and model_name like CONCAT(CONCAT('%',#{modelName}),'%')  </if>"
            +" order by udp_date desc"
            +"</script>")
    List<Model> queryByUserIds(@Param("list") List<Integer> list,@Param("modelName")String modelName);
    
    @Select("<script>"
    		+"select id,model_name from model  where 1=1   "
    		+" order by udp_date desc"
    		+"</script>")
    List<Model> queryByUserId(@Param("uid")String uid);

    @Select("<script>"
            +" select m.model_name , a.*  from model m INNER JOIN model_attr a ON m.id = a.model_id where 1=1  "
            + "<if test =\"list !=null \">  and m.add_uid in "
            + "<foreach  collection=\"list\" open=\"(\" close=\")\" separator=\",\" item=\"uid\" >"
            + "#{uid}"
            + "</foreach>"
            +" </if> "
            + "<if test =\"modelID !=null  \"> and model_id = #{modelID} </if>"
            +" order by model_id, order_no "
            +"</script>"
    )
    List<Map> queryExportInfo(@Param("list") List<Integer> list,@Param("modelID") Integer modelID);
}
