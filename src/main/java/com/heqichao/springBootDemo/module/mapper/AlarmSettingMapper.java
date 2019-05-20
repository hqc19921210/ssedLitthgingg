package com.heqichao.springBootDemo.module.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.heqichao.springBootDemo.module.entity.AlarmSetting;

/**
 * @author Muzzy Xu.
 * 
 * 
 */
public interface AlarmSettingMapper {
	
	
	@Select("<script>SELECT e.id,e.name,e.model_id,e.attr_id,e.alram_type,a.data_type,a.expression,e.data_eum,e.data_a,e.data_b,e.data_status,"
			+ " (select param_value from option_tree where code='AlarmSettingCode' and param_key=e.alram_type) alramTypeName, "
			+ " m.model_name modelName,a.attr_name attrName " + 
			"  FROM alarm_setting e" + 
			"  left join model m on e.model_id=m.id" + 
			"  left join model_attr a on e.attr_id=a.id and model_type='R' " + 
			"  where e.data_status = 'N'   "
			+ "<if test =\"name !=null  and name!='' \"> and e.name like CONCAT('%',#{name},'%')  </if>"
//			+ "<if test=\"competence == 2 \"> and e.group_adm_id=g.id </if>"
			+ "<if test=\"competence == 3 \"> and e.add_uid= #{id} </if>"
			+ "<if test=\"competence == 4 \"> and e.add_uid= #{parentId} </if>"
			+ " order by e.udp_date desc </script>")
	public List<AlarmSetting> queryAlarmSettingAll(
			@Param("name")String name,
			@Param("id")Integer id,
			@Param("parentId")Integer pid,
			@Param("competence")Integer cmp);
	
	@Select("SELECT count(1)>0 from alarm_setting where add_uid=#{uid} and model_id=#{mid} and attr_id=#{aid} and data_status='N' ")
	public boolean checkModelAndAttr(@Param("uid")Integer uid,@Param("mid")Integer mid,@Param("aid")Integer aid);
	
	@Select("<script>SELECT count(1)>0 from alarm_setting"
			+ " where add_uid=#{udpUid} and model_id=#{modelId} and attr_id=#{attrId} "
			+ "<if test=\"actFlag == 'EDIT' \"> and id not in (#{id}) </if>"
			+ "<if test=\"dataType == 'INT_TYPE' \"> and data_a = #{dataA} and data_b = #{dataB} </if>"
			+ "<if test=\"dataType == 'ENUMERATION_TYPE' \"> and data_eum = #{dataEum} </if>"
			+ "and data_status='N'</script> ")
	public boolean checkModelAndAttrWithout(AlarmSetting as);
	
	@Select("<script>SELECT count(1)>0 from alarm_setting"
			+ " where add_uid=#{udpUid} and name=#{name}  "
			+ "<if test=\"actFlag == 'EDIT' \"> and id not in (#{id}) </if>"
			+ "and data_status='N'</script> ")
	public boolean checkAlarmNameDuplication(AlarmSetting as);
	
	
	@Insert("insert into alarm_setting (name,model_id,attr_id,alram_type,data_a,data_b,data_eum,add_uid,udp_uid)"
			+ " values(#{name},#{modelId},#{attrId},#{alramType},#{dataA},#{dataB},#{dataEum},#{udpUid},#{udpUid}) ")
	public int addAlarmSetting(AlarmSetting as);
	
	
	@Update("update alarm_setting set  name = #{name},model_id=#{modelId},attr_id =#{attrId},alram_type =#{alramType},"
			+ "data_a =#{dataA},data_b =#{dataB},data_eum =#{dataEum},udp_uid =#{udpUid},udp_date =sysdate()  where id=#{id} and data_status = 'N' ")
	public int editAlarmSetting(AlarmSetting as);
	
	@Update("update alarm_setting set  data_status = 'D', udp_date=sysdate(),udp_uid =#{uid}  where id=#{aid} and data_status = 'N' ")
	public int delAlarmSetting(@Param("aid")Integer aid,@Param("uid")Integer uid);

	@Select("<script>"
			+" select * from alarm_setting  where model_id = #{modelId}  and data_status= #{status}"
			+"</script>")
	List<AlarmSetting> queryByModelIdAndStatus(@Param("modelId")Integer modelId,@Param("status")String status);

	@Update("update alarm_setting set  data_status = #{status} , udp_date=sysdate()  where model_id=#{modelId} and data_status = 'N' ")
	public void deleteByModelId(@Param("modelId")Integer modelId,@Param("status")String status);
	
}
