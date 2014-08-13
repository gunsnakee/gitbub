
package com.dmdelivery.webservice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * This (return) datatype is used as a return-value when returning Event data.
 * 
 * <p>EventType complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="EventType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="campaign_id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="template_id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="name">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="80"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="title">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="80"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="lang">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="close_date" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="nr_seats" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="show_fields">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="255"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="extras" type="{http://dmdelivery.com/webservice/}EventExtraType" maxOccurs="unbounded"/>
 *         &lt;element name="show_decline" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="max_attendees" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="accept_mailing_id" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="accept_sms_mailing_id" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="text_open">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="16777216"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="text_full">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="16777216"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="text_after">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="16777216"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="text_accept">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="16777216"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="text_decline">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="16777216"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="DMDgid_accept" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="DMDgid_decline" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EventType", propOrder = {
    "id",
    "campaignId",
    "templateId",
    "name",
    "title",
    "lang",
    "closeDate",
    "nrSeats",
    "showFields",
    "extras",
    "showDecline",
    "maxAttendees",
    "acceptMailingId",
    "acceptSmsMailingId",
    "textOpen",
    "textFull",
    "textAfter",
    "textAccept",
    "textDecline",
    "dmDgidAccept",
    "dmDgidDecline"
})
public class EventType {

    protected int id;
    @XmlElement(name = "campaign_id")
    protected int campaignId;
    @XmlElement(name = "template_id")
    protected int templateId;
    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected String title;
    @XmlElement(required = true)
    protected String lang;
    @XmlElement(name = "close_date", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar closeDate;
    @XmlElement(name = "nr_seats")
    protected int nrSeats;
    @XmlElement(name = "show_fields", required = true)
    protected String showFields;
    @XmlElement(required = true)
    protected List<EventExtraType> extras;
    @XmlElement(name = "show_decline")
    protected int showDecline;
    @XmlElement(name = "max_attendees")
    protected int maxAttendees;
    @XmlElement(name = "accept_mailing_id")
    protected Integer acceptMailingId;
    @XmlElement(name = "accept_sms_mailing_id")
    protected Integer acceptSmsMailingId;
    @XmlElement(name = "text_open", required = true)
    protected String textOpen;
    @XmlElement(name = "text_full", required = true)
    protected String textFull;
    @XmlElement(name = "text_after", required = true)
    protected String textAfter;
    @XmlElement(name = "text_accept", required = true)
    protected String textAccept;
    @XmlElement(name = "text_decline", required = true)
    protected String textDecline;
    @XmlElement(name = "DMDgid_accept")
    protected Integer dmDgidAccept;
    @XmlElement(name = "DMDgid_decline")
    protected Integer dmDgidDecline;

    /**
     * 获取id属性的值。
     * 
     */
    public int getId() {
        return id;
    }

    /**
     * 设置id属性的值。
     * 
     */
    public void setId(int value) {
        this.id = value;
    }

    /**
     * 获取campaignId属性的值。
     * 
     */
    public int getCampaignId() {
        return campaignId;
    }

    /**
     * 设置campaignId属性的值。
     * 
     */
    public void setCampaignId(int value) {
        this.campaignId = value;
    }

    /**
     * 获取templateId属性的值。
     * 
     */
    public int getTemplateId() {
        return templateId;
    }

    /**
     * 设置templateId属性的值。
     * 
     */
    public void setTemplateId(int value) {
        this.templateId = value;
    }

    /**
     * 获取name属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * 设置name属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * 获取title属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置title属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * 获取lang属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLang() {
        return lang;
    }

    /**
     * 设置lang属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLang(String value) {
        this.lang = value;
    }

    /**
     * 获取closeDate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCloseDate() {
        return closeDate;
    }

    /**
     * 设置closeDate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public void setCloseDate(XMLGregorianCalendar value) {
        this.closeDate = value;
    }

    /**
     * 获取nrSeats属性的值。
     * 
     */
    public int getNrSeats() {
        return nrSeats;
    }

    /**
     * 设置nrSeats属性的值。
     * 
     */
    public void setNrSeats(int value) {
        this.nrSeats = value;
    }

    /**
     * 获取showFields属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShowFields() {
        return showFields;
    }

    /**
     * 设置showFields属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShowFields(String value) {
        this.showFields = value;
    }

    /**
     * Gets the value of the extras property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the extras property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExtras().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EventExtraType }
     * 
     * 
     */
    public List<EventExtraType> getExtras() {
        if (extras == null) {
            extras = new ArrayList<EventExtraType>();
        }
        return this.extras;
    }

    /**
     * 获取showDecline属性的值。
     * 
     */
    public int getShowDecline() {
        return showDecline;
    }

    /**
     * 设置showDecline属性的值。
     * 
     */
    public void setShowDecline(int value) {
        this.showDecline = value;
    }

    /**
     * 获取maxAttendees属性的值。
     * 
     */
    public int getMaxAttendees() {
        return maxAttendees;
    }

    /**
     * 设置maxAttendees属性的值。
     * 
     */
    public void setMaxAttendees(int value) {
        this.maxAttendees = value;
    }

    /**
     * 获取acceptMailingId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAcceptMailingId() {
        return acceptMailingId;
    }

    /**
     * 设置acceptMailingId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAcceptMailingId(Integer value) {
        this.acceptMailingId = value;
    }

    /**
     * 获取acceptSmsMailingId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAcceptSmsMailingId() {
        return acceptSmsMailingId;
    }

    /**
     * 设置acceptSmsMailingId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAcceptSmsMailingId(Integer value) {
        this.acceptSmsMailingId = value;
    }

    /**
     * 获取textOpen属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTextOpen() {
        return textOpen;
    }

    /**
     * 设置textOpen属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTextOpen(String value) {
        this.textOpen = value;
    }

    /**
     * 获取textFull属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTextFull() {
        return textFull;
    }

    /**
     * 设置textFull属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTextFull(String value) {
        this.textFull = value;
    }

    /**
     * 获取textAfter属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTextAfter() {
        return textAfter;
    }

    /**
     * 设置textAfter属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTextAfter(String value) {
        this.textAfter = value;
    }

    /**
     * 获取textAccept属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTextAccept() {
        return textAccept;
    }

    /**
     * 设置textAccept属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTextAccept(String value) {
        this.textAccept = value;
    }

    /**
     * 获取textDecline属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTextDecline() {
        return textDecline;
    }

    /**
     * 设置textDecline属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTextDecline(String value) {
        this.textDecline = value;
    }

    /**
     * 获取dmDgidAccept属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDMDgidAccept() {
        return dmDgidAccept;
    }

    /**
     * 设置dmDgidAccept属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDMDgidAccept(Integer value) {
        this.dmDgidAccept = value;
    }

    /**
     * 获取dmDgidDecline属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDMDgidDecline() {
        return dmDgidDecline;
    }

    /**
     * 设置dmDgidDecline属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDMDgidDecline(Integer value) {
        this.dmDgidDecline = value;
    }

}
