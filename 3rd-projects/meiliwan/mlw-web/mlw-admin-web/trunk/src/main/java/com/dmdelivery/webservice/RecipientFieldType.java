
package com.dmdelivery.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * This datatype represents a single recipient field.
 * 
 * <p>RecipientFieldType complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="RecipientFieldType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="200"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="type">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="maxlength" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="required" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="default">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="80"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="unq_single" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="unq_combi" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="desc">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="65536"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="explanation">
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
@XmlType(name = "RecipientFieldType", propOrder = {
    "name",
    "type",
    "maxlength",
    "required",
    "_default",
    "unqSingle",
    "unqCombi",
    "desc",
    "explanation"
})
public class RecipientFieldType {

    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected String type;
    protected int maxlength;
    protected boolean required;
    @XmlElement(name = "default", required = true)
    protected String _default;
    @XmlElement(name = "unq_single")
    protected boolean unqSingle;
    @XmlElement(name = "unq_combi")
    protected boolean unqCombi;
    @XmlElement(required = true)
    protected String desc;
    @XmlElement(required = true)
    protected String explanation;

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
     * 获取type属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * 设置type属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * 获取maxlength属性的值。
     * 
     */
    public int getMaxlength() {
        return maxlength;
    }

    /**
     * 设置maxlength属性的值。
     * 
     */
    public void setMaxlength(int value) {
        this.maxlength = value;
    }

    /**
     * 获取required属性的值。
     * 
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * 设置required属性的值。
     * 
     */
    public void setRequired(boolean value) {
        this.required = value;
    }

    /**
     * 获取default属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDefault() {
        return _default;
    }

    /**
     * 设置default属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDefault(String value) {
        this._default = value;
    }

    /**
     * 获取unqSingle属性的值。
     * 
     */
    public boolean isUnqSingle() {
        return unqSingle;
    }

    /**
     * 设置unqSingle属性的值。
     * 
     */
    public void setUnqSingle(boolean value) {
        this.unqSingle = value;
    }

    /**
     * 获取unqCombi属性的值。
     * 
     */
    public boolean isUnqCombi() {
        return unqCombi;
    }

    /**
     * 设置unqCombi属性的值。
     * 
     */
    public void setUnqCombi(boolean value) {
        this.unqCombi = value;
    }

    /**
     * 获取desc属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDesc() {
        return desc;
    }

    /**
     * 设置desc属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDesc(String value) {
        this.desc = value;
    }

    /**
     * 获取explanation属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExplanation() {
        return explanation;
    }

    /**
     * 设置explanation属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExplanation(String value) {
        this.explanation = value;
    }

}
