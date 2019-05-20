package com.heqichao.springBootDemo.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.StatementType;

import com.heqichao.springBootDemo.base.entity.GroupsEntity;

/**
 * @author Muzzy Xu.
 * 
 * 
 */
public interface EquGroupsMapper {
	
	@Select("<script>SELECT c.id,c.name,c.pid,c.grp_sort FROM group_equ AS c " + 
			"    INNER JOIN group_tree t on c.id = t.descendant " + 
			"    WHERE t.ancestor = 1 and c.valid = 'N' and (uid = #{uid} or uid = 0 )"
			+ " order by grp_sort </script>")
	public List<GroupsEntity> getGroups(@Param("uid")Integer uid);
	
	@Select({ "call p_insert_groups("
			+ "#{name,mode=IN,jdbcType=VARCHAR},"
			+ "#{treeRoot,mode=IN,jdbcType=INTEGER},"
			+ "#{uid,mode=IN,jdbcType=INTEGER},"
			+ "#{aid,mode=IN,jdbcType=INTEGER},"
			+ "#{gSort,mode=IN,jdbcType=INTEGER})" })
	@Options(statementType=StatementType.CALLABLE)
	public void insertGroups(
			@Param("name") String name,
			@Param("treeRoot")Integer treeRoot,
			@Param("uid")Integer uid,
			@Param("aid")Integer aid,
			@Param("gSort")Integer gSort);
	
	@Select({ "call p_update_groups("
			+ "#{name,mode=IN,jdbcType=VARCHAR},"
			+ "#{gid,mode=IN,jdbcType=INTEGER},"
			+ "#{treeRoot,mode=IN,jdbcType=INTEGER},"
			+ "#{uid,mode=IN,jdbcType=INTEGER},"
			+ "#{gSort,mode=IN,jdbcType=INTEGER})" })
	@Options(statementType=StatementType.CALLABLE)
	public void updateGroups(
			@Param("name") String name,
			@Param("gid")Integer gid,
			@Param("treeRoot")Integer treeRoot,
			@Param("uid")Integer uid,
			@Param("gSort")Integer gSort);

	@Select("SELECT count(e.id)>0 FROM group_equ AS c " + 
			"  INNER JOIN group_tree t on c.id = t.descendant " + 
			"  left join equipments e on e.group_id = c.id and e.valid = 'N'" + 
			"   WHERE t.ancestor = #{gid} and c.valid = 'N' and (c.uid = #{uid} or c.uid = 0 )")
	public boolean checkIfEquOnNode(@Param("uid")Integer uid,@Param("gid")Integer gid);
	
	@Update(" update group_equ c " + 
			" INNER JOIN group_tree t on c.id = t.descendant " + 
			" set c.valid = 'D' " + 
			" where  t.ancestor = #{gid} and c.valid = 'N' and c.uid = #{uid}  ")
	public int deleteGroups(@Param("uid")Integer uid,@Param("gid")Integer gid);
}
