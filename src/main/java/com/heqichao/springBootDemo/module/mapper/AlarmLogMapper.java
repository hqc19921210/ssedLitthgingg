
package com.heqichao.springBootDemo.module.mapper;

import com.heqichao.springBootDemo.module.entity.AlarmLog;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by heqichao on 2018-12-16.
 */
public interface AlarmLogMapper {

	@Update(
			"<script>"
					+ "update alarm_log set  data_status = #{status},udp_date=#{date} where dev_id in "
					+ "<foreach  collection=\"list\" open=\"(\" close=\")\" separator=\",\" item=\"uid\" >"
					+ "#{uid}"
					+ "</foreach>"
					+ "</script>")
	void updateDeleteStatus(@Param("status")String status , @Param("list") List<String> list, @Param("date")Date date);

    @Insert(
            "<script>"
                    +" insert into alarm_log (add_date,udp_date,add_uid,udp_uid,model_id,dev_id,attr_id,setting_id,data_value,unit,data_status,dev_type,alram_type,log_id) values"
                    + "<foreach  collection=\"list\"  separator=\",\" item=\"o\" >"
                    +" (#{o.addDate},#{o.udpDate},#{o.addUid},#{o.udpUid},#{o.modelId},#{o.devId},#{o.attrId},#{o.settingId},#{o.dataValue},#{o.unit},#{o.dataStatus},#{o.devType},#{o.alramType},#{o.logId}) "
                    + "</foreach>"
                    +"</script>"
    )
    int save(@Param("list") List<AlarmLog> list);

    @Update(
            "<script>"
            + "update alarm_log set  data_status = #{status},udp_date=#{date},new_value= #{newValue} where dev_id = #{devId} and  data_status =#{srcStatus} and attr_id = #{attrId} "
            + "</script>")
    void updateStatus(@Param("status")String status ,@Param("date")Date date , @Param("devId")String devId, @Param("attrId")Integer attrId,@Param("srcStatus")String srcStatus,@Param("newValue")String newValue);

    @Update(
            "<script>"
                    + "update alarm_log set  data_status = #{status},udp_date=#{date},record=#{record} where id = #{id} "
                    + "</script>")
    void updateAlarm(@Param("status")String status ,@Param("date")Date date , @Param("record")String record,@Param("id")Integer id);


    @Select("<script>"
            +" select a.*,e.name,m.attr_name,m.data_type from ( alarm_log a inner join equipments e on a.dev_id =e.dev_id and e.valid ='N' ) inner join model_attr m on a.attr_id =m.id and model_type='R' where 1=1  and a.dev_id in "
            + "<foreach  collection=\"list\" open=\"(\" close=\")\" separator=\",\" item=\"uid\" >"
            + "#{uid}"
            + "</foreach>"
            + "<if test =\"devId !=null and devId !='' \"> and  a.dev_id = #{devId}  </if>"
            + "<if test =\"attrId !=null  \"> and  a.attr_id =#{attrId}  </if>"
            + "<if test =\"status !=null and status !=''\"> and  a.data_status= #{status}  </if>"
            + "<if test =\"start !=null  and start!=''\"> and a.udp_date &gt;= #{start} </if>" //大于等于
            + "<if test =\"end !=null  and end!='' \"> and a.udp_date &lt;= #{end} </if>"  // 小于等于
            +" and  a.data_status != 'D' order by udp_date desc"
            +"</script>")
    List<Map> queryAlarmLogByDevIdAttrId(@Param("list") List<String> list ,@Param("devId") String devId, @Param("attrId")Integer attrId, @Param("status")String status, @Param("start") String start, @Param("end") String end);

    @Select("<script>"
            +"select a.id,a.dev_id,a.attr_id,a.data_status,a.udp_date,a.alram_type,a.data_value,a.new_value,a.unit,e.name,m.attr_name,m.data_type from (alarm_log a LEFT JOIN equipments e on a.dev_id = e.dev_id and e.valid ='N') LEFT JOIN model_attr m on a.attr_id = m.id and model_type='R' where a.id in ("
            +"select MAX(id) from alarm_log  where 1=1 and  data_status != 'D' and dev_id in "
            + "<foreach  collection=\"list\" open=\"(\" close=\")\" separator=\",\" item=\"uid\" >"
            + "#{uid}"
            + "</foreach>"
             + "<if test =\" status !=null and status != ''\">  and  data_status = #{status} </if>"
            + "<if test =\"udpDate !=null \"> and  udp_date &gt; #{udpDate} </if>"
            + "<if test =\"recordIsNull \"> and  record is null </if>"
            +" GROUP BY dev_id,attr_id "
            +") order by a.udp_date desc"
            +"</script>")
    List<Map> queryAlarm(@Param("list") List<String> list ,@Param("status")String status,@Param("udpDate")Date udpDate,@Param("recordIsNull")boolean recordIsNull);

	//查找今年的按年按月按日统计
	@Select("<script>"
			+"select DATE_FORMAT(add_date,#{timeType}) times,count(id) count from alarm_log where  add_date &gt; year(SYSDATE()) "
			+" and  data_status != 'D' and dev_id in "
			+ "<foreach  collection=\"list\" open=\"(\" close=\")\" separator=\",\" item=\"uid\" >"
			+ "#{uid}"
			+ "</foreach>"
			+" group by times order by times asc"
			+"</script>"
		)
	List<Map> queryCountByTimeType(@Param("list") List<String> list,@Param("timeType")String timeType);


	//查找某个时间段内的统计
	@Select("<script>"
			+"select DATE_FORMAT(add_date,'%j') times,count(id) count from alarm_log where 1=1  "
			+" and  data_status != 'D' and dev_id in "
			+ "<foreach  collection=\"list\" open=\"(\" close=\")\" separator=\",\" item=\"uid\" >"
			+ "#{uid}"
			+ "</foreach>"
			+ "<if test =\"start !=null  and start!=''\"> and add_date &gt;= #{start} </if>" //大于等于
			+ "<if test =\"end !=null  and end!='' \"> and add_date &lt;= #{end} </if>"  // 小于等于
			+" group by times order by times asc"
			+"</script>"
	)
	List<Map> queryCountByDay(@Param("list") List<String> list,@Param("start")String start,@Param("end")String end);


	// Muzzy
   /* @Select("<script>"
    		+"select * from ( select a.model_id,a.dev_id,a.attr_id,a.setting_id,a.alram_type,ot.param_value,a.add_date," +
    		" case a.data_status when 'A' then '报警' when 'N' then '已处理' end as status_name, " + 
    		" m.model_name,ma.attr_name,al.data_a,al.data_b,a.data_status " + 
    		" from alarm_log a" + 
    		" left join model m on a.model_id=m.id " + 
    		" left join model_attr ma on a.attr_id=ma.id " + 
    		" left join alarm_setting al on a.setting_id=al.id and al.data_status='N'"+
    		" left join option_tree ot on a.alram_type=ot.param_key and ot.code='AlarmSettingCode'" + 
    		" where 1=1 " + 
    		" and a.dev_id in (select e.dev_id from equipments e where e.valid = 'N'"
    		+ "<if test=\"competence == 3 \"> and e.uid= #{id} </if>"
			+ "<if test=\"competence == 4 \"> and e.uid= #{parentId} </if>"
    		+ ")" + 
    		" order by a.add_date desc" + 
    		" limit 5 ) alog  "
    		+"</script>")*/
	@Select(
			"<script>"
			+"select * from ("
			+"select a.id,a.dev_id,a.attr_id,a.data_status, " +
					" case a.data_status when 'A' then '报警' when 'N' then '已处理' end as status_name, "+
					"a.udp_date,a.alram_type,a.data_value,a.new_value,a.unit,e.name,ma.attr_name,ma.data_type,m.model_name from (alarm_log a LEFT JOIN equipments e on a.dev_id = e.dev_id and e.valid ='N') LEFT JOIN model_attr ma on a.attr_id = ma.id and model_type='R' left join model m on m.id = ma.model_id where "
			+"   data_status != 'D' and  a.dev_id in "
			+"  (select e.dev_id from equipments e where e.valid = 'N'"
			+ "<if test=\"competence == 3 \"> and e.uid= #{id} </if>"
			+ "<if test=\"competence == 4 \"> and e.uid= #{parentId} </if>"
			+ ") order by a.add_date desc limit 5"
			+") alog"
			+"</script>"
	)
    List<Map> queryAlarmNewestFive(
    		@Param("id")Integer id,
			@Param("parentId")Integer pid,
			@Param("competence")Integer cmp
    		);
    // End Muzzy
    
    

}
