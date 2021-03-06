package com.heqichao.springBootDemo.base.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.StatementType;

import com.heqichao.springBootDemo.base.entity.Equipment;
import com.heqichao.springBootDemo.base.entity.UploadResultEntity;
import com.heqichao.springBootDemo.base.vo.EquipmentVO;
import com.heqichao.springBootDemo.module.entity.DataDetail;
import com.heqichao.springBootDemo.module.vo.CreateEquimentVO;

/**
 * @author Muzzy Xu.
 * 
 * 
 */
public interface EquipmentMapper {

	/**
	 * 查找某人下所拥有某个设备类型的设备dev_id
	 * @param uid
	 * @param type
	 * @return
	 */
	@Select("SELECT dev_id FROM equipments where uid = #{uid}  and valid = 'N' and type_cd  = #{type}")
	List<String> queryDevIdByUidAndType(@Param("uid") Integer uid,@Param("type")String type);
	
	@Select("<script>select equipments.dev_id,equipments.name from equipments where valid = 'N'  " +
			"and uid like (" +
			"select case u.competence " +
			"when 2 then '%'" +
			" when 3 then u.id " +
			" when 4 then u.parent_uid end as u_id " +
			" from users u where u.id = #{uid} limit 1 ) "
			+ "</script>")
	public List<Map<String,String>> getUserEquipmentIdList(@Param("uid") Integer uid);


	@Select("<script>select equipments.dev_id,equipments.name from equipments where valid = 'N' and pro_id = #{prodId} </script>")
	List<Map<String,String>> getUserEquipmentIdListByProdId(@Param("prodId") Integer prodId);

	@Select("SELECT u.parent_uid FROM users u where u.id=#{uid} and u.valid = 'N' ")
	public Integer getUserParent(@Param("uid") Integer uid);

	@Select("SELECT dev_id FROM equipments where online = #{status}  and valid = 'N' ")
	public List<String> getEquipmentByStatus(@Param("status") String  status);

	@Select("SELECT * FROM equipments where valid = 'N' and dev_id = #{devId} ")
	public Equipment getEquipmentInfo(@Param("devId") String  devId);

	
	/**
	 * 缓存获取设备信息
	 * @param devId
	 * @return
	 */

	@Select("SELECT e.id,e.name,e.dev_id,e.type_cd,e.group_id,e.group_adm_id,p.model_id,p.app_id,e.pro_id,"+
			" e.verification,e.support_code,e.supporter,e.site,e.address,e.remark,e.online,e.uid,e.udp_date," +
			" u.company uName,m.model_name,a.app_name, p.name proName," +
			" case e.type_cd when 'L' then 'Lora' when 'N' then 'Nbiot' when 'G' then '2G' else null end as typeName, " +
			" (select u2.open_id from users u2 where u2.valid = 'N' and u2.open_id is not null and  (u2.id=e.id or u2.parent_uid=e.id) limit 1) as validWechat "+
			"  FROM equipments e" +
			"  left join users u on e.uid=u.id" +
			"  left join products p on e.pro_id=p.id and p.valid = 'N' " + 
			"  left join model m on p.model_id=m.id" +
			"  left join applications a on p.app_id=a.id" +
			"  where e.valid = 'N' and e.dev_id = #{devId} ")
	public EquipmentVO getEquById(@Param("devId") String  devId);

	/**
	 * 设备编辑获取设备信息
	 * @param devId
	 * @param id
	 * @return
	 */

	@Select("SELECT e.id,e.name,e.dev_id,e.type_cd,e.group_id,e.group_adm_id,e.pro_id,p.model_id,p.app_id,"+
			" e.verification,e.support_code,e.supporter,e.site,e.address,e.remark,e.online,e.uid,e.udp_date," +
			" u.company uName,m.model_name,a.app_name," +
			" case e.type_cd when 'L' then 'Lora' when 'N' then 'Nbiot' when 'G' then '2G' else null end as typeName " +
			"  FROM equipments e" +
			"  left join users u on e.uid=u.id" +
			"  left join products p on e.pro_id=p.id and p.valid = 'N'" +
			"  left join model m on p.model_id=m.id" +
			"  left join applications a on p.app_id=a.id" +
			"  where e.valid = 'N' and e.dev_id = #{devId} and e.id=#{id}")
	public EquipmentVO getEquEditById(@Param("devId") String  devId,@Param("id") Integer  id);

	@Select("SELECT dev_id FROM equipments where valid = 'N' ")
	public List<String> getEquipmentIdListAll();

	/**
	 * 设备列表查询SQL
	 * @param competence
	 * @param id
	 * @param parentId
	 * @param gid
	 * @param sEid
	 * @param sType
	 * @param sStatus
	 * @return
	 */

	@Select("<script>SELECT e.id,e.name,e.dev_id,e.type_cd,e.group_id,e.group_adm_id,e.pro_id,p.model_id,p.app_id," +
			" e.verification,e.support_code,e.supporter,e.site,e.address,e.remark,e.sec_remark,e.online,e.uid,e.udp_date," +
			" u.company uName,g.name groupName,p.name proName," +
			" case e.type_cd when 'L' then 'Lora' when 'N' then 'Nbiot' when 'G' then '2G' else null end as typeName, " +
			" (select count(1)>0 from model_attr ma where ma.model_id=p.model_id and ma.model_type='W') as validCMD " +
			"  FROM group_equ g,equipments e" +
			"  left join users u on e.uid=u.id" +
			"  left join products p on e.pro_id=p.id and p.valid = 'N'" + 
			"  where e.valid = 'N'  "
			+ "<if test=\"competence == 2 \"> and e.group_adm_id=g.id"
			+ "<if test=\"gid !=null \"> and e.group_adm_id = #{gid}  </if> </if>"
			+ "<if test=\"competence == 3 \"> and e.group_id=g.id and e.uid = #{id}"
			+ " <if test=\"gid !=null \"> and e.group_id = #{gid}  </if>  </if>"
			+ "<if test=\"competence == 4 \"> and e.group_id=g.id and e.uid = #{parentId} "
			+ "<if test=\"gid !=null \"> and e.group_id = #{gid}  </if>  </if>"
			+ "<if test=\"pid !=null \"> and e.pro_id = #{pid}   </if>"
			+ "<if test =\"sEid !=null  and sEid!='' \"> and (e.dev_id like CONCAT('%',#{sEid},'%') or e.name like CONCAT('%',#{sEid},'%'))  </if>"
			+ "<if test =\"sType !=null  and sType!='' \"> and e.type_cd like CONCAT(CONCAT('%',#{sType}),'%')  </if>"
			+ "<if test =\"sStatus !=null  and sStatus!='' \"> and e.online = #{sStatus}  </if>"
			+ " order by e.add_date desc </script>")
	public List<EquipmentVO> getEquipments(
			@Param("competence")Integer competence,
			@Param("id")Integer id,
			@Param("parentId")Integer parentId,
			@Param("gid")Integer gid,
			@Param("sEid")String sEid,
			@Param("sType")String sType,
			@Param("sStatus")String sStatus,@Param("pid")Integer pid);
	
	/**
	 * 列表展示主查询
	 * @param competence
	 * @param id
	 * @param parentId
	 * @param gid
	 * @param sEid
	 * @param sType
	 * @param sStatus
	 * @return
	 */
	@Select("<script>SELECT e.id,e.name,e.dev_id,e.type_cd,e.pro_id,"+
//			 "e.model_id,e.group_id,e.group_adm_id,e.app_id," + 
//			"e.support_code,e.supporter,e.site,e.address,e.remark,"+
			" e.verification,e.online,e.uid,e.udp_date," + 
//			" u.company uName,m.model_name,a.app_name,g.name groupName," + 
//			" e.data_point_date mx_date,"+
			" case e.type_cd when 'L' then 'Lora' when 'N' then 'Nbiot' when 'G' then '2G' else null end as typeName " +
			"  FROM group_equ g,equipments e" +
//			"  left join users u on e.uid=u.id" +
//			"  left join model m on e.model_id=m.id" +
//			"  left join applications a on e.app_id=a.id" +
			"  where e.valid = 'N'  "
			+ "<if test=\"competence == 2 \"> and e.group_adm_id=g.id"
			+ "<if test=\"gid !=null \"> and e.group_adm_id = #{gid}  </if> </if>"
			+ "<if test=\"competence == 3 \"> and e.group_id=g.id and e.uid = #{id}"
			+ " <if test=\"gid !=null \"> and e.group_id = #{gid}  </if>  </if>"
			+ "<if test=\"competence == 4 \"> and e.group_id=g.id and e.uid = #{parentId} "
			+ "<if test=\"gid !=null \"> and e.group_id = #{gid}  </if>  </if>"
			+ "<if test =\"sEid !=null  and sEid!='' \"> and (e.dev_id like CONCAT('%',#{sEid},'%') or e.name like CONCAT('%',#{sEid},'%'))  </if>"
			+ "<if test =\"sType !=null  and sType!='' \"> and e.type_cd like CONCAT(CONCAT('%',#{sType}),'%')  </if>"
			+ "<if test =\"sStatus !=null  and sStatus!='' \"> and e.online = #{sStatus}  </if>"
			+ " order by e.data_point_date desc,e.online desc </script>")
	public List<EquipmentVO> getEquipmentsForDevLstOrderBy(
			@Param("competence")Integer competence,
			@Param("id")Integer id,
			@Param("parentId")Integer parentId,
			@Param("gid")Integer gid,
			@Param("sEid")String sEid,
			@Param("sType")String sType,
			@Param("sStatus")String sStatus);

	@Select("<script>"
    		+" select d.id,d.data_type,d.udp_date,d.dev_id,d.unit,d.data_name,d.data_value,d.attr_id from data_detail d " +
    		" where d.log_id = (select d2.log_id from data_detail d2  where d2.dev_id=#{devId} and d2.data_status='N' order by d2.add_date desc limit 1) " +
    		" and d.data_status='N'"
    		+"</script>")
    List<DataDetail> queryDetailByDevId( @Param("devId") String devId);

	//获取最新数据点集合
	@Select("<script>"
			+" SELECT d.id,d.data_type,d.udp_date,d.dev_id,d.unit,d.data_name,d.data_value,d.attr_id " +
			" FROM data_detail d,equipments e "
			+ "<if test=\"competence == 2 \"> where 1=1 </if>"
			+ "<if test=\"competence == 3 \">  where 1=1  and e.uid = #{id} </if>"
			+ "<if test=\"competence == 4 \">  where 1=1  and e.uid = #{parentId} </if>"
			+" and d.add_date=e.data_point_date AND d.dev_id=e.dev_id and d.data_status='N' and e.valid='N' and d.dev_id in"
			+ "<foreach  collection=\"list\" open=\"(\" close=\")\" separator=\",\" item=\"equ\" >"
			+ "#{equ.devId}"
			+ "</foreach>"
			+"</script>")
	List<DataDetail> queryDetailPointList(
			@Param("list") List<Equipment> list,
			@Param("competence")Integer competence,
			@Param("id")Integer id,
			@Param("parentId")Integer parentId);


	@Select("<script>SELECT e.id,e.name,e.dev_id,e.type_cd,e.model_id,e.group_id,e.group_adm_id,e.app_id," +
			" e.verification,e.support_code,e.supporter,e.site,e.address,e.remark,e.online,e.uid,e.udp_date," +
			" u.company uName,m.model_name,a.app_name,g.name groupName," +
			" case e.type_cd when 'L' then 'Lora' when 'N' then 'Nbiot' when 'G' then '2G' else null end as typeName " +
			"  FROM group_equ g,equipments e" +
			"  left join users u on e.uid=u.id" +
			"  left join model m on e.model_id=m.id" +
			"  left join applications a on e.app_id=a.id" +
			"  where e.valid = 'N' and e.type_cd= #{type} "
			+ "<if test=\"competence == 2 \"> and e.group_adm_id=g.id </if>"
			+ "<if test=\"competence == 3 \"> and e.group_id=g.id and e.uid = #{uid} </if>"
			+ "<if test=\"competence == 4 \"> and e.group_id=g.id and e.uid = #{parentId} </if>"
			+ " </script>")
	public List<Map<String,Object>> getEquipmentsForExport(
			@Param("competence")Integer competence,
			@Param("uid")Integer uid,
			@Param("parentId")Integer parentId,
			@Param("type")String type);

	/**
	 * 下拉框kv-设备
	 * @param competence
	 * @param uid
	 * @param parentId
	 * @return
	 */
	@Select("<script>select dev_id,name from equipments where valid = 'N' "
			+ "<if test=\"competence == 3 \">  and e.uid = #{uid} </if>"
			+ "<if test=\"competence == 4 \">  and e.uid = #{parentId} </if>"
			+ " order by add_date desc </script>")
	public List<Equipment> getEquSelectList(
			@Param("competence")Integer competence,
			@Param("uid")Integer uid,
			@Param("parentId")Integer parentId);
	/**
	 * 下拉框kv-根据应用获取设备
	 * @param competence
	 * @param uid
	 * @param parentId
	 * @return
	 */
	@Select("<script>select dev_id,name from equipments where valid = 'N' and app_id=#{aid} "
			+ "<if test=\"competence == 3 \">  and e.uid = #{uid} </if>"
			+ "<if test=\"competence == 4 \">  and e.uid = #{parentId} </if>"
			+ " order by add_date desc </script>")
	public List<Equipment> getEquSelectListByAppId(
			@Param("aid")Integer aid,
			@Param("competence")Integer competence,
			@Param("uid")Integer uid,
			@Param("parentId")Integer parentId);

	/**
	 * 日志导出
	 * @param eid
	 * @return List
	 */
	@Select("<script>SELECT e.add_date,e.dev_id,e.src_data,e.dev_type,e.data_status"+
			" FROM data_log e  where e.dev_id= #{eid} "
			+ " order by add_date desc </script>")
	public List<Map<String,Object>> getDataLogForExport(@Param("eid")String eid);

	/**
	 * 获取数据点导出列名
	 * @param eid
	 * @return String
	 */
	@Select("<script>select concat ('add_date,dev_id,',  " +
			"(SELECT GROUP_CONCAT(DISTINCT d.data_name)  " +
			"	FROM data_detail d  " +
			"	where d.dev_id = #{eid})) colname " +
			"from dual </script>")
	public String getDataDownloadColumn(@Param("eid")String eid);

	/**
	 * 数据点导出
	 * @param eid
	 * @return List
	 */
	@Select({ "call p_enq_data_column(#{dev_id,mode=IN,jdbcType=VARCHAR},"
			+ "#{start,mode=IN,jdbcType=VARCHAR},"
			+ "#{end,mode=IN,jdbcType=VARCHAR})" })
	@Options(statementType=StatementType.CALLABLE)
	public List<Map<String,Object>> getDataDownloadForExport(
			@Param("dev_id")String eid,
			@Param("start") String start,
			@Param("end") String end);


	@Insert("insert into upload_result (add_uid,res_index,res_status,err_reason,res_key)"
			+ " values(#{addUid},#{resIndex},#{resStatus},#{errReason},#{resKey}) ")
	public int insertUploadResult(UploadResultEntity res);

	@Select("select res_index,res_status,err_reason "
			+ " from upload_result where res_key=#{key} ")
	public List<UploadResultEntity> getUploadResult(@Param("key")String key);

	@Insert("insert into equipments (name,dev_id,type_cd,group_id,group_adm_id,pro_id,verification,support_code,supporter,site,address,remark,sec_remark,uid,valid,add_uid,udp_uid,online)"
			+ " values(#{name},#{devId},#{typeCd},#{groupId},#{groupAdmId},#{proId},#{verification},#{supportCode},#{supporter},#{site},#{address},#{remark},#{secRemark},#{uid},#{valid},#{addUid},#{addUid},0) ")
	public int insertEquipment(Equipment equ);

	@Update("update equipments set name=#{name},dev_id=#{devId},type_cd=#{typeCd},pro_id=#{proId},"
			+ "group_id=#{groupId},group_adm_id=#{groupAdmId},verification=#{verification},support_code=#{supportCode},"
			+ "supporter=#{supporter},site=#{site},address=#{address},remark=#{remark},sec_remark=#{secRemark},uid=#{uid},valid=#{valid},udp_uid=#{udpUid},udp_date=sysdate()"
			+ " where id=#{id}")
	public int editEquipment(Equipment equ);

	@Update("<script>"
			+"update equipments set data_point_date=#{addDate}"
			+ " where valid = 'N' and dev_id in "
			+ "<foreach  collection=\"list\" open=\"(\" close=\")\" separator=\",\" item=\"equ\" >"
			+ "#{equ.devId}"
			+ "</foreach>"
			+"</script>")
	public int updateEquDataPointDate(@Param("list")List<DataDetail> list,@Param("addDate")Date addDate);


	@Select("<script>"
			+" select dev_id from equipments  "
			+" where online = #{online} and type_cd =#{type_cd} "
			+"</script>")
	List<String> queryByTypeAndOnline( @Param("type_cd") String type_cd,@Param("online") String online);

	@Update("<script>"
			+ "update equipments set  online = #{online},udp_date=#{date} where dev_id in "
			+ "<foreach  collection=\"list\" open=\"(\" close=\")\" separator=\",\" item=\"uid\" >"
			+ "#{uid}"
			+ "</foreach>"
			+ "</script>")
	void updateOnlineStatus(@Param("online")String online , @Param("list") List<String> list, @Param("date")Date date);


	@Update("update equipments set  udp_date = sysdate(), udp_uid = #{udid}, valid = 'D' where id=#{id} and valid = 'N' ")
	public int delEquById(@Param("id")Integer eid,@Param("udid")Integer udid);
	
	@Update("update equipments set  udp_date = sysdate(), udp_uid = #{udid}, valid = 'D' where id in (${equs}) and valid = 'N' ")
	public int delEquAll(@Param("equs")String equs,@Param("udid")Integer udid);
	
	@Select("select count(1)>0 from equipments where verification = #{verification} and valid = 'N' ")
	public boolean duplicatedEid(@Param("verification")String verification);
	

	@Update("update equipments set  valid = #{status} where dev_id=#{devId} and valid = 'N' ")
	public int setEquStatus(@Param("devId")String eidevIdd,@Param("status")String status);


	@Select("select dev_id from equipments where id = #{id} and valid = 'N'  ")
	public String getEquIdOld(@Param("id")Integer id);
	
	/**
	 * 根据prod_id获取注册设备信息
	 * @param id
	 * @return
	 */
	@Select(" SELECT p.device_type,p.app_model,p.manufacturer_id,a.app,a.app_name,a.secret,a.platform_ip" + 
			" FROM products p, applications a" + 
			" WHERE p.id = #{pid} AND p.valid = 'N' AND p.app_id=a.id")
	public CreateEquimentVO getAddDevVOByProId(@Param("pid")Integer pid);

	@Update("update equipments set  e_range = #{range} where eid=#{eid} and valid = 'N'")
	 int updateRange(@Param("eid")String eid,@Param("range")Integer range);

	@Select("select e_range from equipments where eid = #{eid} and valid = 'N'")
	Integer queryRange(@Param("eid")String eid);

	@Select("select dev_id from equipments where eid = #{eid} and valid = 'N'")
	String queryDevId(@Param("eid")String eid);

	@Select("select u.id from users u where u.company=#{uName} and valid = 'N' ")
	Integer getUserIdByName(@Param("uName")String uName);

	@Select("select u.id from model u where u.model_name=#{modelName} and add_uid=#{uid} limit 1")
	Integer getModelIdByName(@Param("modelName")String modelName,@Param("uid")Integer uid);

	@Select("select u.id from group_equ u where u.name=#{groupName} and uid=#{uid} and u.valid = 'N' limit 1")
	Integer getGroupIdByName(@Param("groupName")String groupName,@Param("uid")Integer uid);

	@Select("select u.id from applications u where u.app_name=#{appName} and uid=#{uid} and u.valid = 'N' limit 1")
	Integer getAppIdByName(@Param("appName")String appName,@Param("uid")Integer uid);

	@Select(
			"<script>"
			+" select  DATEDIFF(add_date,#{startDay})  times ,COUNT(1) count from equipments  where "
			+" add_date &gt;= #{startDay} and add_date &lt; #{endDay} and valid ='N' and dev_id in "
			+ "<foreach  collection=\"list\" open=\"(\" close=\")\" separator=\",\" item=\"id\">"
			+ "#{id}"
			+ "</foreach>"
			+" group by times "
			+ "</script>")
	List<Map> queryAddEquCount(@Param("list")List<String> list,@Param("startDay")Date startDay,@Param("endDay")Date endDay);

	@Select(
			"<script>"
					+" select  COUNT(1) count from equipments  where "
					+" add_date &lt; #{startDay} and valid ='N' and dev_id in "
					+ "<foreach  collection=\"list\" open=\"(\" close=\")\" separator=\",\" item=\"id\">"
					+ "#{id}"
					+ "</foreach>"
					+ "</script>")
	int querySumEquCount(@Param("list")List<String> list,@Param("startDay")Date startDay);
}
