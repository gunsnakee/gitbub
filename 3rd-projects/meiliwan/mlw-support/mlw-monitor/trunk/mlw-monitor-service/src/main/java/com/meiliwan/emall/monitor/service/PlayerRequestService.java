package com.meiliwan.emall.monitor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.monitor.bean.Player;
import com.meiliwan.emall.monitor.bean.PlayerRequest;
import com.meiliwan.emall.monitor.bean.PlayerRequestVO;
import com.meiliwan.emall.monitor.dao.PlayerRequestDao;
import com.mlcs.core.ice.service.impl.DefaultBaseServiceImpl;

@Repository
public class PlayerRequestService extends DefaultBaseServiceImpl {

	@Autowired
	private PlayerRequestDao playerRequestdao;

	public PagerControl<PlayerRequestVO> getPagePlayerRequest( PageInfo pageInfo) {

		PagerControl<PlayerRequestVO> pc = playerRequestdao
				.getPagePlayerRequest(null, pageInfo, null);
		return pc;
	}

	public void add( int pid) {

		if (pid <= 0) {
			throw new ServiceException("the pid can not less than 0");
		}
		PlayerRequest playerRequest = new PlayerRequest();
		playerRequest.setPid(pid);
		playerRequestdao.insert(playerRequest);
	}

	public void delete( int pid) {

		if (pid <= 0) {
			throw new ServiceException("the pid can not less than 0");
		}
		playerRequestdao.delete(pid);
	}

	public List<Player> getAllPlayer(){
		
		List<Player> all = playerRequestdao.getAllPlayer();
		return all;
	}
}
