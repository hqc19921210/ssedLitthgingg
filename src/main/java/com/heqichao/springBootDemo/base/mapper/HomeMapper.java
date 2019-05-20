package com.heqichao.springBootDemo.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author Muzzy Xu.
 * 
 * 
 */
public interface HomeMapper {
	
	
	@Select("<script>select 0 from dual " 
			+" union all select count(1) from equipment where status = 'Y'" 
			+ "<if test=\"competence == 3 \"> and own_id = #{id}  </if>"
			+ "<if test=\"competence == 4 \"> and own_id = #{parentId}  </if>"
			+" union all select count(1) from lite_equipment where status = 'N' "  
			+ "<if test=\"competence == 3 \"> and own_id = #{id}  </if>"
			+ "<if test=\"competence == 4 \"> and own_id = #{parentId}  </if>"
			+ " </script>")
	public List<Integer> queryPieData(
			@Param("competence")Integer competence,
			@Param("id")Integer id,
			@Param("parentId")Integer parentId);
	
	@Select("<script>select count(1) from equipments e where e.valid='N' and e.type_cd='G' " 
			+ "<if test=\"competence == 3 \"> and uid = #{id}  </if>"
			+ "<if test=\"competence == 4 \"> and uid = #{parentId}  </if>"+
			" union all" + 
			" select count(1) from equipments e where e.valid='N' and e.type_cd='L' "
			+ "<if test=\"competence == 3 \"> and uid = #{id}  </if>"
			+ "<if test=\"competence == 4 \"> and uid = #{parentId}  </if>"+
			" union all" + 
			" select count(1) from equipments e where e.valid='N' and e.type_cd='N' "
			+ "<if test=\"competence == 3 \"> and uid = #{id}  </if>"
			+ "<if test=\"competence == 4 \"> and uid = #{parentId}  </if>"+
			" union all" + 
			" select count(1) from equipments e2 where e2.valid='N' and e2.type_cd='G' and e2.online=1"  
			+ "<if test=\"competence == 3 \"> and uid = #{id}  </if>"
			+ "<if test=\"competence == 4 \"> and uid = #{parentId}  </if>"+
			" union all" + 
			" select count(1) from equipments e2 where e2.valid='N' and e2.type_cd='L' and e2.online=1"
			+ "<if test=\"competence == 3 \"> and uid = #{id}  </if>"
			+ "<if test=\"competence == 4 \"> and uid = #{parentId}  </if>"
			+" union all" + 
			" select count(1) from equipments e2 where e2.valid='N' and e2.type_cd='N' and e2.online=1"
			+ "<if test=\"competence == 3 \"> and uid = #{id}  </if>"
			+ "<if test=\"competence == 4 \"> and uid = #{parentId}  </if>"
			+ " </script>")
	public List<Integer> queryHomeData(
			@Param("competence")Integer competence,
			@Param("id")Integer id,
			@Param("parentId")Integer parentId);
	
	@Select("<script>" 
			+"select count(1) from user where status = 'Y' and competence= 3" 
			+" union all select count(1) from user where status = 'Y' and competence= 4"  
			+ " </script>")
	public List<Integer> queryUserData();
	
	@Select("<script>select sum(num) from (select 0 as num from dual " + 
			" union all select count(1) from lite_equipment where status = 'N' "  
			+ "<if test=\"competence == 3 \"> and own_id = #{id}  </if>"
			+ "<if test=\"competence == 4 \"> and own_id = #{parentId}  </if>"
			+" union all select count(1) from equipment where status = 'Y'" 
			+ "<if test=\"competence == 3 \"> and own_id = #{id}  </if>"
			+ "<if test=\"competence == 4 \"> and own_id = #{parentId}  </if>"
			+ ") as t_all </script>")
	public Integer queryAllEqu(
			@Param("competence")Integer competence,
			@Param("id")Integer id,
			@Param("parentId")Integer parentId);
	
	@Select("<script>select count(1) from equipment where status = 'Y' and e_status = 'N' "  
			+ "<if test=\"competence == 3 \"> and own_id = #{id}  </if>"
			+ "<if test=\"competence == 4 \"> and own_id = #{parentId}  </if>"
			+ " </script>")
	public Integer queryOnlineEqu(
			@Param("competence")Integer competence,
			@Param("id")Integer id,
			@Param("parentId")Integer parentId);
	@Select("<script>"
			+ "select max(l.ligntningTime) from lightning_log l,equipment e " + 
			" where l.devEUI = e.eid and l.STATUS = '1111' and e.`status`='Y'"  
			+ "<if test=\"competence == 3 \"> and own_id = #{id}  </if>"
			+ "<if test=\"competence == 4 \"> and own_id = #{parentId}  </if>"
			+" union all " + 
			" select max(l.peakValue) from lightning_log l,equipment e " + 
			" where l.devEUI = e.eid and l.STATUS = '1111' and e.`status`='Y'"  
			+ "<if test=\"competence == 3 \"> and own_id = #{id}  </if>"
			+ "<if test=\"competence == 4 \"> and own_id = #{parentId}  </if>"
			+ " </script>")
	public List<String> queryUserMax(
			@Param("competence")Integer competence,
			@Param("id")Integer id,
			@Param("parentId")Integer parentId);
	
}
