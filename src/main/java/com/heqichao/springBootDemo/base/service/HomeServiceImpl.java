package com.heqichao.springBootDemo.base.service;

import com.heqichao.springBootDemo.base.mapper.HomeMapper;
import com.heqichao.springBootDemo.base.entity.HomeEntity;
import com.heqichao.springBootDemo.base.entity.User;
import com.heqichao.springBootDemo.base.util.ServletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Muzzy Xu.
 * 
 */


@Service
@Transactional
public class HomeServiceImpl implements HomeService {
    @Autowired
    private HomeMapper hMapper ;
    

    @Override
    public HomeEntity queryPieData() {
    	User user = ServletUtil.getSessionUser();
    	Integer competence = user.getCompetence();
    	Integer id = user.getId();
    	Integer parentId = user.getParentId();
    	HomeEntity hentity = new HomeEntity();
    	List<Integer> res = hMapper.queryHomeData(competence,id,parentId);
    	hentity.setGprsNom(res.get(0));
    	hentity.setGprsBrD(res.get(3));
    	hentity.setLorNom(res.get(1));
    	hentity.setLorBrD(res.get(4));
    	hentity.setNbNom(res.get(2));
    	hentity.setNbBrD(res.get(5));
    	hentity.setEquAll(res.get(0)+res.get(1)+res.get(2));
    	List<Integer> pieLst=new ArrayList<Integer>();
    	for(int i=0;i<3;i++) {
    		pieLst.add(res.get(i));
    	}
    	hentity.setPieMap(pieLst);
    	return hentity;
    }
    
    int getLstValue(List<Integer> lst,int key) {
    	
    	try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
    	return 0;
    }


}
