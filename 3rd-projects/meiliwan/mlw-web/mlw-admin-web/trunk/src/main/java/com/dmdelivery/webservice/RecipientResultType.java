
package com.dmdelivery.webservice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * This generic datatype is used as a return-value when adding multiple recipients.
 * 
 * <p>RecipientResultType complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="RecipientResultType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="fields" type="{http://dmdelivery.com/webservice/}RecipientNameValuePairType" maxOccurs="unbounded"/>
 *         &lt;element name="DMDmessage">
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
@XmlType(name = "RecipientResultType", propOrder = {
    "fields",
    "dmDmessage"
})
public class RecipientResultType {

    @XmlElement(required = true)
    protected List<RecipientNameValuePairType> fields;
    @XmlElement(name = "DMDmessage", required = true)
    protected String dmDmessage;

    /**
     * Gets the value of the fields property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fields property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFields().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RecipientNameValuePairType }
     * 
     * 
     */
    public List<RecipientNameValuePairType> getFields() {
        if (fields == null) {
            fields = new ArrayList<RecipientNameValuePairType>();
        }
        return this.fields;
    }

    /**
     * 获取dmDmessage属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDMDmessage() {
        return dmDmessage;
    }

    /**
     * 设置dmDmessage属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDMDmessage(String value) {
        this.dmDmessage = value;
    }

}
