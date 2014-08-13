package com.meiliwan.emall.monitor.dao;

import java.util.List;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.monitor.bean.Player;
import com.meiliwan.emall.monitor.bean.PlayerRequest;
import com.meiliwan.emall.monitor.bean.PlayerRequestVO;

public interface PlayerRequestDao extends IDao<Integer, PlayerRequest> {

	PagerControl<PlayerRequestVO> getPagePlayerRequest(PlayerRequest entity, PageInfo pageInfo, String whereSql);
	
	List<Player> getAllPlayer();
}