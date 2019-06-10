package com.heqichao.springBootDemo.base.mapper;

import com.heqichao.springBootDemo.base.entity.SendRequestLogEntity;
import org.apache.ibatis.annotations.Insert;

/**
 * Created by heqichao on 2019-6-9.
 */
public interface SendRequestLogMapper {
    @Insert(
            "<script>"+
                    " insert into send_request_log (add_date,udp_date,add_uid,udp_uid,url,param,status,respone,memo) " +
                    " values(#{addDate},#{udpDate},#{addUid},#{udpUid},#{url},#{param},#{status},#{respone},#{memo}) "
            +"</script>"
    )
    int save(SendRequestLogEntity log);
}
