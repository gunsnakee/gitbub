package com.meiliwan.emall.sp2.dto;

/**
 * User: wuzixin
 * Date: 14-6-3
 * Time: 下午3:56
 */
public class TicketParms {
    private String ticketId;

    private String acountNum;

    private Integer type;

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getAcountNum() {
        return acountNum;
    }

    public void setAcountNum(String acountNum) {
        this.acountNum = acountNum;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
