
package com.dmdelivery.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * This datatype represents a campaign.
 * 
 * <p>CampaignType complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="CampaignType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="brand_id" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="name">
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
 *               &lt;minLength value="2"/>
 *               &lt;maxLength value="80"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="is_overall" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="is_active" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="has_soap_api" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="create_date" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="remarks" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="65536"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CampaignType", propOrder = {
    "id",
    "brandId",
    "name",
    "lang",
    "isOverall",
    "isActive",
    "hasSoapApi",
    "createDate",
    "remarks"
})
public class CampaignType {

    protected int id;
    @XmlElement(name = "brand_id")
    protected Integer brandId;
    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected String lang;
    @XmlElement(name = "is_overall")
    protected boolean isOverall;
    @XmlElement(name = "is_active")
    protected boolean isActive;
    @XmlElement(name = "has_soap_api")
    protected boolean hasSoapApi;
    @XmlElement(name = "create_date", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar createDate;
    protected String remarks;

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
     * 获取brandId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBrandId() {
        return brandId;
    }

    /**
     * 设置brandId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBrandId(Integer value) {
        this.brandId = value;
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
     * 获取isOverall属性的值。
     * 
     */
    public boolean isIsOverall() {
        return isOverall;
    }

    /**
     * 设置isOverall属性的值。
     * 
     */
    public void setIsOverall(boolean value) {
        this.isOverall = value;
    }

    /**
     * 获取isActive属性的值。
     * 
     */
    public boolean isIsActive() {
        return isActive;
    }

    /**
     * 设置isActive属性的值。
     * 
     */
    public void setIsActive(boolean value) {
        this.isActive = value;
    }

    /**
     * 获取hasSoapApi属性的值。
     * 
     */
    public boolean isHasSoapApi() {
        return hasSoapApi;
    }

    /**
     * 设置hasSoapApi属性的值。
     * 
     */
    public void setHasSoapApi(boolean value) {
        this.hasSoapApi = value;
    }

    /**
     * 获取createDate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreateDate() {
        return createDate;
    }

    /**
     * 设置createDate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public void setCreateDate(XMLGregorianCalendar value) {
        this.createDate = value;
    }

    /**
     * 获取remarks属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * 设置remarks属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemarks(String value) {
        this.remarks = value;
    }

}
