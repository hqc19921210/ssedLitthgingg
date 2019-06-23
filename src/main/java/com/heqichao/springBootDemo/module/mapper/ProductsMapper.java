package com.heqichao.springBootDemo.module.mapper;

import com.heqichao.springBootDemo.module.entity.Products;
import com.heqichao.springBootDemo.module.vo.ProductsVo;

import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Muzzy Xu.
 * @date 2019/06/12
 */
public interface ProductsMapper {


	/**
	 * 查询产品表
	 * @param name
	 * @param modelId
	 * @param appId
	 * @return
	 */
	@Select("<script>"
            +" SELECT p.id, p.udp_date,p.name,p.model_id,p.app_id,p.type_cd,p.vu_id,p.remark,p.valid,m.model_name,a.app_name,u.company, " + 
			" p.device_type, p.app_model, p.manufacturer_id,"+
			" case p.type_cd when 'L' then 'Lora' when 'N' then 'Nbiot' when 'G' then '2G' else null end as typeName"+
            " FROM  model m, products p" + 
            " left join applications a on p.app_id = a.id" + 
            " left join users u on p.vu_id = u.id" + 
            " where p.model_id=m.id  "
            + "<if test =\"modelId !=null  \"> and m.id =#{modelId}   </if>"
            + "<if test =\"appId !=null \"> and a.id =#{appId}  </if>"
            + "<if test =\"name !=null  and name!='' \"> and p.name like CONCAT(CONCAT('%',#{name}),'%')  </if>"
            +" and p.valid = 'N' order by p.udp_date desc "
            +"</script>")
     List<ProductsVo> queryProducts(
    		 @Param("name") String name,
    		 @Param("modelId") Integer modelId,
    		 @Param("appId") Integer appId);
	

	
	/**
	 * 添加产品
	 * @param pro
	 * @return
	 */
	@Insert("insert into products (add_uid,udp_uid,name,type_cd,model_id,app_id,vu_id,device_type, app_model, manufacturer_id,remark,valid)"
			+ " values(#{udpUid},#{udpUid},#{name},#{typeCd},#{modelId},#{appId},#{vuId},#{deviceType},#{appModel},#{manufacturerId},#{remark},'N') ")
	public int insertProducts(Products pro);
	
	/**
	 * 修改产品
	 * @param pro
	 * @return
	 */
	@Update("update  products set name=#{name},model_id=#{modelId},type_cd=#{typeCd},app_id=#{appId},vu_id=#{vuId},remark=#{remark},udp_uid=#{udpUid},udp_date=sysdate(),"
			+ " device_type=#{deviceType},app_model=#{appModel},manufacturer_id=#{manufacturerId} where id=#{id} and valid = 'N' ")
	public int updateProducts(Products pro);
	
	/**
	 * 检查产品名称是否重复
	 * @param name
	 * @return
	 */
	@Select("select count(1)>0 from products where name = #{name} and valid = 'N' ")
	public boolean duplicatedProductsName(@Param("name")String name);
	
	/**
	 * 删除产品
	 * @param id
	 * @param uid
	 * @return
	 */
    @Delete("update products set valid = 'D',udp_date=sysdate(),udp_uid=#{uid} where valid = 'N' and id= #{id}   ")
	public int deleteProductsById(@Param("id")Integer id,@Param("uid")Integer uid);
    
    
    /**
     * 产品下拉kv
     * @return
     */
    @Select("<script>"
			+"select id ,name from products a where 1=1 "
			 + "<if test =\"cmp == 3 \"> and  (vu_id is null or vu_id= #{uid})  </if>"
	         + "<if test =\"cmp == 4 \"> and  (vu_id is null or vu_id= #{pId})  </if>"
			+ "and valid = 'N' "
			+"</script>")
	public List<Products> getProductsList(
		 @Param("uid") Integer uid,
   		 @Param("pId") Integer pId,
   		 @Param("cmp") Integer cmp);
    /**
     * 根据设备类型查找产品下拉kv
     * @return
     */
    @Select("<script>"
    		+"select id ,name from products a where 1=1 "
    		+ "<if test =\"cmp == 3 \"> and  (vu_id is null or vu_id= #{uid})  </if>"
    		+ "<if test =\"cmp == 4 \"> and  (vu_id is null or vu_id= #{pId})  </if>"
    		+ "and valid = 'N' and type_cd = #{type}"
    		+"</script>")
    public List<Products> getProductsListByType(
    		@Param("uid") Integer uid,
    		@Param("pId") Integer pId,
    		@Param("cmp") Integer cmp,
    		@Param("type") String type);
}
