
package com.dmdelivery.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * This datatype represents a recipient group. Used when retrieving groups.
 * 
 * <p>GroupType complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="GroupType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="name">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="80"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="is_test" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="is_active" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="is_system" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="remarks">
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
@XmlType(name = "GroupType", propOrder = {
    "id",
    "name",
    "isTest",
    "isActive",
    "isSystem",
    "remarks"
})
public class GroupType {

    protected int id;
    @XmlElement(required = true)
    protected String name;
    @XmlElement(name = "is_test")
    protected boolean isTest;
    @XmlElement(name = "is_active")
    protected boolean isActive;
    @XmlElement(name = "is_system")
    protected boolean isSystem;
    @XmlElement(required = true)
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
     * 获取isTest属性的值。
     * 
     */
    public boolean isIsTest() {
        return isTest;
    }

    /**
     * 设置isTest属性的值。
     * 
     */
    public void setIsTest(boolean value) {
        this.isTest = value;
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
     * 获取isSystem属性的值。
     * 
     */
    public boolean isIsSystem() {
        return isSystem;
    }

    /**
     * 设置isSystem属性的值。
     * 
     */
    public void setIsSystem(boolean value) {
        this.isSystem = value;
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
