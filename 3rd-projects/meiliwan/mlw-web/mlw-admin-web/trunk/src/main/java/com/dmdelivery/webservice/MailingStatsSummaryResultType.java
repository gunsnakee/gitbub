
package com.dmdelivery.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * This datatype represents a single Summary Statistics record.
 * 
 * <p>MailingStatsSummaryResultType complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="MailingStatsSummaryResultType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="weblink">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="255"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="total_sent" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="total_accepted" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="hardbounces" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="softbounces" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="unsubscribers" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="suspends" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="spamcomplaints" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="unique_opens" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="total_renders" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="unique_renders" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="total_clickthroughs" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="unique_clickthroughs" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="unique_link_clickthroughs" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="total_conversion" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="unique_conversion" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="click2open_rate" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MailingStatsSummaryResultType", propOrder = {
    "weblink",
    "totalSent",
    "totalAccepted",
    "hardbounces",
    "softbounces",
    "unsubscribers",
    "suspends",
    "spamcomplaints",
    "uniqueOpens",
    "totalRenders",
    "uniqueRenders",
    "totalClickthroughs",
    "uniqueClickthroughs",
    "uniqueLinkClickthroughs",
    "totalConversion",
    "uniqueConversion",
    "click2OpenRate"
})
public class MailingStatsSummaryResultType {

    @XmlElement(required = true)
    protected String weblink;
    @XmlElement(name = "total_sent")
    protected int totalSent;
    @XmlElement(name = "total_accepted")
    protected int totalAccepted;
    protected int hardbounces;
    protected int softbounces;
    protected int unsubscribers;
    protected int suspends;
    protected int spamcomplaints;
    @XmlElement(name = "unique_opens")
    protected int uniqueOpens;
    @XmlElement(name = "total_renders")
    protected int totalRenders;
    @XmlElement(name = "unique_renders")
    protected int uniqueRenders;
    @XmlElement(name = "total_clickthroughs")
    protected int totalClickthroughs;
    @XmlElement(name = "unique_clickthroughs")
    protected int uniqueClickthroughs;
    @XmlElement(name = "unique_link_clickthroughs")
    protected int uniqueLinkClickthroughs;
    @XmlElement(name = "total_conversion")
    protected int totalConversion;
    @XmlElement(name = "unique_conversion")
    protected int uniqueConversion;
    @XmlElement(name = "click2open_rate")
    protected float click2OpenRate;

    /**
     * 获取weblink属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWeblink() {
        return weblink;
    }

    /**
     * 设置weblink属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWeblink(String value) {
        this.weblink = value;
    }

    /**
     * 获取totalSent属性的值。
     * 
     */
    public int getTotalSent() {
        return totalSent;
    }

    /**
     * 设置totalSent属性的值。
     * 
     */
    public void setTotalSent(int value) {
        this.totalSent = value;
    }

    /**
     * 获取totalAccepted属性的值。
     * 
     */
    public int getTotalAccepted() {
        return totalAccepted;
    }

    /**
     * 设置totalAccepted属性的值。
     * 
     */
    public void setTotalAccepted(int value) {
        this.totalAccepted = value;
    }

    /**
     * 获取hardbounces属性的值。
     * 
     */
    public int getHardbounces() {
        return hardbounces;
    }

    /**
     * 设置hardbounces属性的值。
     * 
     */
    public void setHardbounces(int value) {
        this.hardbounces = value;
    }

    /**
     * 获取softbounces属性的值。
     * 
     */
    public int getSoftbounces() {
        return softbounces;
    }

    /**
     * 设置softbounces属性的值。
     * 
     */
    public void setSoftbounces(int value) {
        this.softbounces = value;
    }

    /**
     * 获取unsubscribers属性的值。
     * 
     */
    public int getUnsubscribers() {
        return unsubscribers;
    }

    /**
     * 设置unsubscribers属性的值。
     * 
     */
    public void setUnsubscribers(int value) {
        this.unsubscribers = value;
    }

    /**
     * 获取suspends属性的值。
     * 
     */
    public int getSuspends() {
        return suspends;
    }

    /**
     * 设置suspends属性的值。
     * 
     */
    public void setSuspends(int value) {
        this.suspends = value;
    }

    /**
     * 获取spamcomplaints属性的值。
     * 
     */
    public int getSpamcomplaints() {
        return spamcomplaints;
    }

    /**
     * 设置spamcomplaints属性的值。
     * 
     */
    public void setSpamcomplaints(int value) {
        this.spamcomplaints = value;
    }

    /**
     * 获取uniqueOpens属性的值。
     * 
     */
    public int getUniqueOpens() {
        return uniqueOpens;
    }

    /**
     * 设置uniqueOpens属性的值。
     * 
     */
    public void setUniqueOpens(int value) {
        this.uniqueOpens = value;
    }

    /**
     * 获取totalRenders属性的值。
     * 
     */
    public int getTotalRenders() {
        return totalRenders;
    }

    /**
     * 设置totalRenders属性的值。
     * 
     */
    public void setTotalRenders(int value) {
        this.totalRenders = value;
    }

    /**
     * 获取uniqueRenders属性的值。
     * 
     */
    public int getUniqueRenders() {
        return uniqueRenders;
    }

    /**
     * 设置uniqueRenders属性的值。
     * 
     */
    public void setUniqueRenders(int value) {
        this.uniqueRenders = value;
    }

    /**
     * 获取totalClickthroughs属性的值。
     * 
     */
    public int getTotalClickthroughs() {
        return totalClickthroughs;
    }

    /**
     * 设置totalClickthroughs属性的值。
     * 
     */
    public void setTotalClickthroughs(int value) {
        this.totalClickthroughs = value;
    }

    /**
     * 获取uniqueClickthroughs属性的值。
     * 
     */
    public int getUniqueClickthroughs() {
        return uniqueClickthroughs;
    }

    /**
     * 设置uniqueClickthroughs属性的值。
     * 
     */
    public void setUniqueClickthroughs(int value) {
        this.uniqueClickthroughs = value;
    }

    /**
     * 获取uniqueLinkClickthroughs属性的值。
     * 
     */
    public int getUniqueLinkClickthroughs() {
        return uniqueLinkClickthroughs;
    }

    /**
     * 设置uniqueLinkClickthroughs属性的值。
     * 
     */
    public void setUniqueLinkClickthroughs(int value) {
        this.uniqueLinkClickthroughs = value;
    }

    /**
     * 获取totalConversion属性的值。
     * 
     */
    public int getTotalConversion() {
        return totalConversion;
    }

    /**
     * 设置totalConversion属性的值。
     * 
     */
    public void setTotalConversion(int value) {
        this.totalConversion = value;
    }

    /**
     * 获取uniqueConversion属性的值。
     * 
     */
    public int getUniqueConversion() {
        return uniqueConversion;
    }

    /**
     * 设置uniqueConversion属性的值。
     * 
     */
    public void setUniqueConversion(int value) {
        this.uniqueConversion = value;
    }

    /**
     * 获取click2OpenRate属性的值。
     * 
     */
    public float getClick2OpenRate() {
        return click2OpenRate;
    }

    /**
     * 设置click2OpenRate属性的值。
     * 
     */
    public void setClick2OpenRate(float value) {
        this.click2OpenRate = value;
    }

}
