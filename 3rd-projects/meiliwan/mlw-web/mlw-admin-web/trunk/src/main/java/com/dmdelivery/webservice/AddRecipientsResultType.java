
package com.dmdelivery.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * This datatype is used as a return value for adding multiple new recipients to DMdelivery.
 * 
 * <p>addRecipientsResultType complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="addRecipientsResultType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="status">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="OK"/>
 *               &lt;enumeration value="DUPLICATE"/>
 *               &lt;enumeration value="ERROR"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="statusMsg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="errors" type="{http://dmdelivery.com/webservice/}RecipientResultArrayType" minOccurs="0"/>
 *         &lt;element name="duplicates" type="{http://dmdelivery.com/webservice/}RecipientResultArrayType" minOccurs="0"/>
 *         &lt;element name="successful" type="{http://dmdelivery.com/webservice/}RecipientArrayType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "addRecipientsResultType", propOrder = {
    "status",
    "statusMsg",
    "errors",
    "duplicates",
    "successful"
})
public class AddRecipientsResultType {

    @XmlElement(required = true)
    protected String status;
    protected String statusMsg;
    protected RecipientResultArrayType errors;
    protected RecipientResultArrayType duplicates;
    protected RecipientArrayType successful;

    /**
     * 获取status属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置status属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * 获取statusMsg属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatusMsg() {
        return statusMsg;
    }

    /**
     * 设置statusMsg属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatusMsg(String value) {
        this.statusMsg = value;
    }

    /**
     * 获取errors属性的值。
     * 
     * @return
     *     possible object is
     *     {@link RecipientResultArrayType }
     *     
     */
    public RecipientResultArrayType getErrors() {
        return errors;
    }

    /**
     * 设置errors属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link RecipientResultArrayType }
     *     
     */
    public void setErrors(RecipientResultArrayType value) {
        this.errors = value;
    }

    /**
     * 获取duplicates属性的值。
     * 
     * @return
     *     possible object is
     *     {@link RecipientResultArrayType }
     *     
     */
    public RecipientResultArrayType getDuplicates() {
        return duplicates;
    }

    /**
     * 设置duplicates属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link RecipientResultArrayType }
     *     
     */
    public void setDuplicates(RecipientResultArrayType value) {
        this.duplicates = value;
    }

    /**
     * 获取successful属性的值。
     * 
     * @return
     *     possible object is
     *     {@link RecipientArrayType }
     *     
     */
    public RecipientArrayType getSuccessful() {
        return successful;
    }

    /**
     * 设置successful属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link RecipientArrayType }
     *     
     */
    public void setSuccessful(RecipientArrayType value) {
        this.successful = value;
    }

}
