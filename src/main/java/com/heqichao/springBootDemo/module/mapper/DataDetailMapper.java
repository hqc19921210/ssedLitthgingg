package com.heqichao.springBootDemo.module.mapper;

import com.heqichao.springBootDemo.module.entity.DataDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

/**
 * Created by heqichao on 2018-11-28.
 */
public interface DataDetailMapper {

    @Insert(
            "<script>"
            +" insert into data_detail (add_date,udp_date,add_uid,udp_uid,log_id,dev_id,data_name,attr_id,data_type,data_value,data_src,unit,data_status,order_no) values "
            + "<foreach  collection=\"list\"  separator=\",\" item=\"o\" >"
            +"(#{o.addDate},#{o.udpDate},#{o.addUid},#{o.udpUid},#{o.logId},#{o.devId},#{o.dataName},#{o.attrId},#{o.dataType},#{o.dataValue},#{o.dataSrc},#{o.unit},#{o.dataStatus},#{o.orderNo}) "
            + "</foreach>"
            +"</script>"
    )
    int save(@Param("list") List<DataDetail> list);


    @Update(
            "<script>"
                    + "update data_detail set  data_status = #{status},udp_date=#{date} where dev_id in "
                    + "<foreach  collection=\"list\" open=\"(\" close=\")\" separator=\",\" item=\"uid\" >"
                    + "#{uid}"
                    + "</foreach>"
                    + "</script>")
    void updateStatus(@Param("status")String status , @Param("list") List<String> list, @Param("date")Date date);

    @Select("<script>"
            +"select data_name,add_date,unit,data_value,data_type,attr_id from data_detail  where data_value != '' and dev_id = #{devId} and attr_id = #{attrId} and data_status = #{status} and data_status!='D' "
            + "<if test =\"start !=null  and start!=''\"> and add_date &gt;= #{start} </if>" //大于等于
            + "<if test =\"end !=null  and end!='' \"> and add_date &lt;= #{end} </if>"  // 小于等于
            +" order by add_date desc "
            +"</script>")
    List<DataDetail> queryDetail( @Param("devId") String devId, @Param("attrId") Integer attrId,@Param("status") String status, @Param("start") String start, @Param("end") String end);



    @Select("<script>" +
            "select DISTINCT dev_id from equipments where  online =#{onLine} and type_cd= #{type} and dev_id not in (select DISTINCT dev_id from data_log where data_status != 'D' and add_date > #{date} and dev_type=#{type} )" +
            "</script>")
    List<String> checkOffLineDev(@Param("type") String type,@Param("onLine") String onLine,@Param("date")Date date);

    @Select("<script>" +
            "select DISTINCT dev_id from data_log where data_status != 'D' and dev_type=#{type} and add_date > #{date} " +
            "</script>")
    List<String> checkOnLineDev(@Param("type") String type,@Param("date")Date date);

}

