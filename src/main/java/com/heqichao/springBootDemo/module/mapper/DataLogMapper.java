package com.heqichao.springBootDemo.module.mapper;

import com.heqichao.springBootDemo.module.entity.DataLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

/**
 * Created by heqichao on 2018-11-28.
 */
public interface DataLogMapper {

    @Insert(
            "<script>"
            +" insert into data_log (add_date,udp_date,add_uid,udp_uid,dev_id,src_data,data,main_data,data_status,dev_type) " +
                    " values(#{addDate},#{udpDate},#{addUid},#{udpUid},#{devId},#{srcData},#{data},#{mainData},#{dataStatus},#{devType}) "
            +"</script>"
    )
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    int save(DataLog log);

    @Update(
            "<script>"
            + "update data_log set  data_status = #{status},udp_date=#{date} where dev_id in "
            + "<foreach  collection=\"list\" open=\"(\" close=\")\" separator=\",\" item=\"uid\" >"
            + "#{uid}"
            + "</foreach>"
            + "</script>")
    void updateStatus( @Param("status")String status ,@Param("list") List<String> list, @Param("date")Date date);
    
    // Muzzy 
    @Select("select * from data_log d where data_status = 'N' order by add_date desc limit 100")
    public List<DataLog> queryDataLog();
    // End Muaay

    @Select(
            "<script>"
                + "select * from data_log  where data_status ='N' and dev_id=#{devId}  and id ="
                +" ( select log_id from data_detail where data_status ='N' and dev_id=#{devId}  and data_name =#{dataName} and data_value =#{dataValue} order by add_date desc limit 1 )"
            + "</script>"
    )
    DataLog queryLatestDataLog( @Param("devId")String devId,@Param("dataName")String dataName,@Param("dataValue")String dataValue);
}
