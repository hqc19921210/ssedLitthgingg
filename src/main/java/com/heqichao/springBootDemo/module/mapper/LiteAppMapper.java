package com.heqichao.springBootDemo.module.mapper;

import com.heqichao.springBootDemo.module.entity.CommandLog;
import com.heqichao.springBootDemo.module.entity.LiteApplication;
import com.heqichao.springBootDemo.module.entity.LiteEquipment;

import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Muzzy Xu.
 */
public interface LiteAppMapper {


	@Select("<script>"
            +"SELECT *  FROM applications  where 1=1 "
            + "<if test =\"cmp == 3 \"> and  uid= #{uid}  </if>"
            + "<if test =\"cmp == 4 \"> and  uid= #{pId}  </if>"
            + "<if test =\"appId !=null  and appId!='' \"> and app like CONCAT(CONCAT('%',#{appId}),'%')  </if>"
            + "<if test =\"appName !=null  and appName!='' \"> and app_name like CONCAT(CONCAT('%',#{appName}),'%')  </if>"
            +" and valid = 'N' order by add_date desc "
            +"</script>")
     List<LiteApplication> queryLiteApps(
    		 @Param("uid") Integer uid,
    		 @Param("pId") Integer pId,
    		 @Param("cmp") Integer cmp,
    		 @Param("appId") String appId,
    		 @Param("appName") String appName);
	
	@Select("<script>"
			+"SELECT id,dev_id,cmd_id,cmd_status,context,add_date,callback  FROM command_log  where dev_id=#{devId} "
			+" and valid = 'N' order by add_date desc "
			+"</script>")
	List<CommandLog> getCommandLogByDevId(@Param("devId") String devId);

	/**
	 * 添加订阅回调记录
	 * @param context,callType
	 * @return
	 */
	@Insert("insert into subscription_log (context,notify_type) values(#{context},#{callType}) ")
	public int insertSubscriptionLog(@Param("context") String context,@Param("callType") String callType);
	
	/**
	 * 添加下发历史记录
	 * @param cmd
	 * @return
	 */
	@Insert("insert into command_log (add_uid,dev_id,cmd_id,cmd_status,callback,context,valid)"
			+ " values(#{addUid},#{devId},#{cmdId},#{cmdStatus},#{callback},#{context},'N') ")
	public int insertCommandLog(CommandLog cmd);
	
	/**
	 * 通过回调 更新下发历史状态
	 * @param cmd
	 * @return
	 */
	@Update("update  command_log set cmd_status=#{cmdStatus},request=#{request},udp_date=sysdate()"
			+ " where cmd_id=#{cmdId} and valid = 'N' ")
	public int updateCommandLog(CommandLog cmd);
	
	@Insert("insert into applications (app,app_name,secret,platform_ip,app_auth,callback_url,subscribe_notifycation,remark,uid,valid,udp_uid)"
			+ " values(#{appId},#{appName},#{secret},#{platformIp},#{appAuth},#{callbackUrl},#{subscribeNotifycation},#{remark},#{ownId},'N',#{updateUid}) ")
	public int insertLiteApplication(LiteApplication app);
	
	@Update("update  applications set app=#{appId},app_name=#{appName},"
			+ "platform_ip=#{platformIp},app_auth=#{appAuth},callback_url=#{callbackUrl},post_asyn_cmd=#{postAsynCmd},subscribe_notifycation=#{subscribeNotifycation},"
			+ "remark=#{remark},uid=#{ownId},udp_date=sysdate(),udp_uid=#{updateUid}"
			+ " where id=#{id} and valid = 'N' ")
	public int updateLiteEquipment(LiteApplication app);
	
	@Update("update  applications set secret=#{secret},udp_date=sysdate(),udp_uid=#{updateUid}"
			+ " where id=#{id} and valid = 'N' ")
	public int resetAppSecret(LiteApplication app);

	@Select("select count(1)>0 from applications where app = #{appId} and valid = 'N' ")
	public boolean duplicatedAid(@Param("appId")String appId);
	
	@Select("select count(1)>0 from applications where app = #{appId} and valid = 'N' and id not in (#{id}) ")
	public boolean duplicatedEidByUpd(@Param("appId")String appId,@Param("id")Integer id);


    @Delete("update applications set valid = 'D',udp_date=sysdate(),udp_uid=#{uid} where valid = 'N' and id= #{id}   ")
	public int deleteById(@Param("id")Integer id,@Param("uid")Integer uid);
    
    //根据ID获取应用信息
    @Select("select * from applications where valid = 'N' and id = #{id} limit 1 ")
	public LiteApplication getAppInfo(@Param("id")Integer id);
    
    @Select("<script>"
			+"select id ,app_name from applications a where 1=1 "
			+ "and valid = 'N' "
			+"</script>")
	public List<LiteApplication> getAppSelectList();

}
