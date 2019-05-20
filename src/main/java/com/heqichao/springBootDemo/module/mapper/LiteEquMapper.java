package com.heqichao.springBootDemo.module.mapper;

import com.heqichao.springBootDemo.module.entity.LiteCommand;
import com.heqichao.springBootDemo.module.entity.LiteEquipment;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Muzzy Xu.
 */
public interface LiteEquMapper {


	@Select("<script>"
            +"SELECT e.*,a.app_name  FROM equipments e,applications a  where 1=1 "
            + "<if test =\"cmp == 3 \"> and  e.uid= #{uid}  </if>"
            + "<if test =\"cmp == 4 \"> and  e.uid= #{pId}  </if>"
            + "<if test =\"deviceId !=null  and deviceId!='' \"> and e.deviceId like CONCAT(CONCAT('%',#{deviceId}),'%')  </if>"
            + "<if test =\"name !=null  and name!='' \"> and e.name like CONCAT(CONCAT('%',#{name}),'%')  </if>"
            + "<if test =\"agreement !=null  and agreement!='' \"> and e.agreement like CONCAT(CONCAT('%',#{agreement}),'%')  </if>"
            +" and e.valid = 'N' and e.app_id = a.id order by e.create_time desc "
            +"</script>")
     List<LiteEquipment> queryLiteEqus(
    		 @Param("uid") Integer uid,
    		 @Param("pId") Integer pId,
    		 @Param("cmp") Integer cmp,
    		 @Param("deviceId") String deviceId,
    		 @Param("name") String name,
    		 @Param("agreement") String agreement);
	
	@Select("SELECT e.*,a.app_id as app_id_detial,a.secret,a.app_auth,a.callback_url,a.post_asyn_cmd "
			+"  FROM equipments e,applications a  where e.valid = 'N' and e.app_id = a.id and e.id=#{eid} "
			)
	LiteCommand queryLiteEquForCmd(@Param("eid") Integer eid);

	@Insert("insert into equipments (app_id,deviceId,name,verification,type,support_id,support_name,agreement,online_time,remark,uid,valid,update_uid)"
			+ " values(#{appId},#{deviceId},#{name},#{verification},#{type},#{supportId},#{supportName},#{agreement},sysdate(),#{remark},#{ownId},'N',#{updateUid}) ")
	public int insertLiteEquipment(LiteEquipment equ);
	
	@Update("update  equipments set app_id=#{appId},deviceId=#{deviceId},name=#{name},verification=#{verification},"
			+ "type=#{type},support_id=#{supportId},support_name=#{supportName},agreement=#{agreement},"
			+ "remark=#{remark},uid=#{ownId},update_time=sysdate(),update_uid=#{updateUid}"
			+ " where id=#{id} ")
	public int updateLiteEquipment(LiteEquipment equ);
	
	@Update("update  equipments set service_id=#{serviceId},method=#{method},param_field=#{paramField},param_value=#{paramValue}"
			+ " where id=#{id} ")
	public int updateLiteEquipmentForCmd(LiteCommand equ);

	/**
	 * 检查设备与应用拥有者是否一致，true为不一致
	 * @param oid
	 * @return
	 */
	@Select("select count(1)=0 from applications where uid = #{oId} and valid = 'N' ")
	public boolean checkAppOwnId(@Param("oId")Integer oid);
	
	@Select("select count(1)>0 from equipments where deviceId = #{deviceId} and valid = 'N' ")
	public boolean duplicatedEid(@Param("deviceId")String deviceId);
	
	@Select("select count(1)>0 from equipments where deviceId = #{deviceId} and valid = 'N' and id not in (#{id}) ")
	public boolean duplicatedEidByUpd(@Param("deviceId")String deviceId,@Param("id")Integer id);

	@Select("<script>"
			+"select id as appId,app_name as appName from applications a where 1=1 "
			+ "<if test =\"cmp == 3 \"> and  a.uid= #{uid}  </if>"
            + "<if test =\"cmp == 4 \"> and  a.uid= #{pId}  </if>"
			+ "and valid = 'N' "
			+"</script>")
	public List<LiteEquipment> getAppSelectList(
		@Param("uid") Integer uid,
   		 @Param("pId") Integer pId,
   		 @Param("cmp") Integer cmp);
	
    @Delete("update equipments set valid = 'C',update_time=sysdate(),update_uid=#{uid} where valid = 'N' and id= #{id}   ")
    int deleteById(@Param("id")Integer id,@Param("uid")Integer uid);
    
    @Select("SELECT deviceId FROM equipments where uid = #{uid}  and valid = 'N' ")
	public List<String> getUserNBEquipmentIdList(@Param("uid") Integer uid);
	
	@Select("SELECT e.deviceId FROM equipments e,user u where e.uid = u.parent_uid  and u.id=#{uid} and e.valid = 'N' ")
	public List<String> getUserNBEquipmentIdListByParent(@Param("uid") Integer uid);
	
	@Select("SELECT deviceId FROM equipments where e_valid = #{status}  and valid = 'N' ")
	public List<String> getNBEquipmentByStatus(@Param("status") String  status);
	
	@Select("SELECT deviceId FROM equipments where valid = 'N' ")
	public List<String> getNBEquipmentIdListAll();


}
