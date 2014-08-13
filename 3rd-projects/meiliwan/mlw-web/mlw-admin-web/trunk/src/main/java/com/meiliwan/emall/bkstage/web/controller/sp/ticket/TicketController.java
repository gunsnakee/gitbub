package com.meiliwan.emall.bkstage.web.controller.sp.ticket;

import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.sp2.bean.SpTicket;
import com.meiliwan.emall.sp2.client.SpTicketClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * User: wuzixin
 * Date: 14-6-6
 * Time: 上午11:24
 */
@Controller("ticketController")
@RequestMapping(value = "/ticket/ticket")
public class TicketController {
    private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());

    @RequestMapping("/list")
    public String list(HttpServletRequest request, Model model) {
        SpTicket ticket = getModelBatch(request);
        PageInfo pageInfo = StageHelper.getPageInfo(request);
        PagerControl<SpTicket> pc = SpTicketClient.getPagerByObj(ticket, pageInfo, pageInfo.getOrderField(), pageInfo.getOrderDirection());
        if (ticket.getState() == null) {
            ticket.setState((short) 99);
        }
        model.addAttribute("ticket", ticket);
        model.addAttribute("pc", pc);
        model.addAttribute("currentTime", new Date());
        return "/sp/ticket/ticket/list";
    }

    private SpTicket getModelBatch(HttpServletRequest request) {
        SpTicket ticket = new SpTicket();

        String ticketId = ServletRequestUtils.getStringParameter(request, "ticketId", null);
        int batchId = ServletRequestUtils.getIntParameter(request, "batchId", 0);
        String startTimeMin = ServletRequestUtils.getStringParameter(request, "startTimeMin", null);
        String startTimeMax = ServletRequestUtils.getStringParameter(request, "startTimeMax", null);


        String ticketName = ServletRequestUtils.getStringParameter(request, "ticketName", null);
        int batchState = ServletRequestUtils.getIntParameter(request, "batchState", 99);
        String endTimeMin = ServletRequestUtils.getStringParameter(request, "endTimeMin", null);
        String endTimeMax = ServletRequestUtils.getStringParameter(request, "endTimeMax", null);

        int state = ServletRequestUtils.getIntParameter(request, "state", 99);
        String userName = ServletRequestUtils.getStringParameter(request, "userName", null);
        String sellTimeMin = ServletRequestUtils.getStringParameter(request, "sellTimeMin", null);
        String sellTimeMax = ServletRequestUtils.getStringParameter(request, "sellTimeMax", null);

        String buyerPhone = ServletRequestUtils.getStringParameter(request, "buyerPhone", null);
        String buyerEmail = ServletRequestUtils.getStringParameter(request, "buyerEmail", null);

        if (batchId > 0) {
            ticket.setBatchId(batchId);
        }
        if (StringUtils.isNotBlank(ticketId)) {
            ticket.setTicketId(ticketId);
        }
        if (StringUtils.isNotEmpty(startTimeMin) && StringUtils.isNotEmpty(startTimeMax)) {
            ticket.setStartTimeMin(DateUtil.parseTimestamp(startTimeMin));
            ticket.setStartTimeMax(DateUtil.parseTimestamp(startTimeMax));
        }

        if (StringUtils.isNotBlank(ticketName)) {
            ticket.setTicketName(ticketName);
        }
        if (StringUtils.isNotEmpty(endTimeMin) && StringUtils.isNotEmpty(endTimeMax)) {
            ticket.setEndTimeMin(DateUtil.parseTimestamp(endTimeMin));
            ticket.setEndTimeMax(DateUtil.parseTimestamp(endTimeMax));
        }

        if (StringUtils.isNotBlank(userName)) {
            ticket.setUserName(userName);
        }
        if (StringUtils.isNotEmpty(sellTimeMin) && StringUtils.isNotEmpty(sellTimeMax)) {
            ticket.setSellTimeMin(DateUtil.parseTimestamp(sellTimeMin));
            ticket.setSellTimeMax(DateUtil.parseTimestamp(sellTimeMax));
        }

        if (StringUtils.isNotBlank(buyerPhone)) {
            ticket.setBuyerPhone(buyerPhone);
        }
        if (StringUtils.isNotBlank(buyerEmail)) {
            ticket.setBuyerEmail(buyerEmail);
        }
        if (state != 99) {
            ticket.setState((short) state);
        }
        ticket.setBatchState(batchState);

        return ticket;
    }

}
