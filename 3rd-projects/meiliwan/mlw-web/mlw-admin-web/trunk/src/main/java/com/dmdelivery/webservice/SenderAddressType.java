
package com.dmdelivery.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * This datatype represents a sender address.
 * 
 * <p>SenderAddressType complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="SenderAddressType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="dmdelivery_address">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="255"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="forward_address">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="255"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="alias_address" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="255"/>
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
@XmlType(name = "SenderAddressType", propOrder = {
    "id",
    "dmdeliveryAddress",
    "forwardAddress",
    "aliasAddress"
})
public class SenderAddressType {

    protected int id;
    @XmlElement(name = "dmdelivery_address", required = true)
    protected String dmdeliveryAddress;
    @XmlElement(name = "forward_address", required = true)
    protected String forwardAddress;
    @XmlElement(name = "alias_address")
    protected String aliasAddress;

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
     * 获取dmdeliveryAddress属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDmdeliveryAddress() {
        return dmdeliveryAddress;
    }

    /**
     * 设置dmdeliveryAddress属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDmdeliveryAddress(String value) {
        this.dmdeliveryAddress = value;
    }

    /**
     * 获取forwardAddress属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getForwardAddress() {
        return forwardAddress;
    }

    /**
     * 设置forwardAddress属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setForwardAddress(String value) {
        this.forwardAddress = value;
    }

    /**
     * 获取aliasAddress属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAliasAddress() {
        return aliasAddress;
    }

    /**
     * 设置aliasAddress属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAliasAddress(String value) {
        this.aliasAddress = value;
    }

}
