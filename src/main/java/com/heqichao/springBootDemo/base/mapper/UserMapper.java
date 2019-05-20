package com.heqichao.springBootDemo.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.heqichao.springBootDemo.base.entity.User;

/**
 * @author Muzzy Xu.
 * 
 * 
 */
public interface UserMapper {
	
	@Select("SELECT u.id,u.parent_uid parentId,u.account,u.company,u.contact,u.phone,u.fax,u.email,u.site,u.remark,"
			+ "case competence when 4 then (select u2.title_name from users u2 where u2.id = u.parent_uid) else u.title_name end as title_name,"
			+ " u.competence"
			+ " FROM users u "
			+ " where u.account = #{account}  "
			+ " and u.password = #{password} "
			+ " and u.valid = 'N' ")
	public User getUserInfo(@Param("account") String account,@Param("password") String password);

	@Select("SELECT id,parent_uid parentId,account,company,contact,phone,fax,email,site,remark,title_name,competence"
			+ " FROM users "
			+ "where id = #{uid}  "
			+ "and valid = 'N' ")
	public User getUserInfoById(@Param("uid")Integer uid);
	
	@Select("<script>SELECT id,parent_uid parentId,account,company,contact,phone,fax,email,site,remark,title_name,competence"
			+ " FROM users "
			+ "where valid = 'N'  "
			+ "<if test=\"competence == 3 \"> and ( id = #{id} or parent_uid = #{id} ) </if>"
			+ "<if test =\"sAccount !=null  and sAccount!='' \"> and account like CONCAT(CONCAT('%',#{sAccount}),'%')  </if>"
			+ "<if test =\"sCompany !=null  and sCompany!='' \"> and company like CONCAT(CONCAT('%',#{sCompany}),'%')  </if>"
			+ "<if test =\"sCompetence !=null  and sCompetence!=0 \"> and competence = #{sCompetence}  </if>"
			+ " </script>")
	public List<User> getUsers(@Param("competence")Integer competence,
							@Param("id")Integer id,
							@Param("sAccount")String sAccount,
							@Param("sCompany")String sCompany,
							@Param("sCompetence")Integer sCompetence);
	
	@Select("<script> select id,company from users where valid = 'N' "
			+ "<if test=\"competence == 2 \"> and (id = #{uid} or parent_uid = #{uid}) </if>"
			+ "<if test=\"competence == 3 \"> and id = #{uid}  </if>"
			+ " order by id</script> ")
	public List<User> getCompanySelectList(@Param("uid")Integer uid,@Param("competence")Integer competence);
	
	@Select("select count(1)>0 from users where (ACCOUNT = #{account} or company = #{company}) and valid = 'N' ")
	public boolean duplicatedAccount(@Param("account")String account,@Param("company")String company);
	
	@Select("select id from users where ACCOUNT = #{account} and password = #{password} and valid = 'N' limit 1")
	public Integer checkPassword(@Param("account") String account,@Param("password") String password);
	
	@Insert("insert into users (parent_uid,account,password,company,contact,phone,fax,email,site,remark,title_name,competence,valid,add_date,udp_uid)"
			+ " values(#{parentId},#{account},#{password},#{company},#{contact},#{phone},#{fax},#{email},#{site},#{remark},#{titleName},#{competence}, 'N', sysdate(), #{upadteUID}) ")
	public int insertUser(User user);
	
	@Update("update users set password=#{password}, udp_date = sysdate(), udp_uid = #{udid} where id=#{id} and valid = 'N' ")
	public int updateUserPassword(@Param("id")Integer uid,@Param("udid")Integer udid,@Param("password") String password);
	
	@Update("update users set company=#{company}, contact=#{contact}, phone=#{phone}, fax=#{fax}, email=#{email}, site=#{site}, remark=#{remark},title_name=#{titleName}, udp_date = sysdate(), udp_uid = #{id} where id=#{id} and valid = 'N' ")
	public int updateUserInfo(User user);
	
	@Update("update users set company=#{company},parent_uid=#{parentId},competence=#{competence}, contact=#{contact}, phone=#{phone}, fax=#{fax}, email=#{email}, site=#{site}, remark=#{remark},title_name=#{titleName}, udp_date = sysdate(), udp_uid = #{upadteUID} where id=#{id} and valid = 'N' ")
	public int updateUserById(User user);
	
	@Update("update users set  udp_date = sysdate(), udp_uid = #{udid}, valid = 'D' where id=#{id} and valid = 'N' ")
	public int delUserById(@Param("id")Integer uid,@Param("udid")Integer udid);

}
