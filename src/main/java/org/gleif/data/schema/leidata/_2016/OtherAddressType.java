//
// This file was generated by the Eclipse Implementation of JAXB, v3.0.0 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.07.28 at 04:03:08 PM CEST 
//


package org.gleif.data.schema.leidata._2016;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OtherAddressType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OtherAddressType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.gleif.org/data/schema/leidata/2016}AddressType"&gt;
 *       &lt;attribute name="type" use="required" type="{http://www.gleif.org/data/schema/leidata/2016}AddressTypeEnum" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OtherAddressType")
public class OtherAddressType
    extends AddressType
{

    @XmlAttribute(name = "type", required = true)
    protected AddressTypeEnum type;

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link AddressTypeEnum }
     *     
     */
    public AddressTypeEnum getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressTypeEnum }
     *     
     */
    public void setType(AddressTypeEnum value) {
        this.type = value;
    }

}
