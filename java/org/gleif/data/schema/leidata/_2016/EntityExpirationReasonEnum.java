//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.07.15 at 11:47:31 PM CEST 
//


package org.gleif.data.schema.leidata._2016;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EntityExpirationReasonEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EntityExpirationReasonEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="DISSOLVED"/>
 *     &lt;enumeration value="CORPORATE_ACTION"/>
 *     &lt;enumeration value="OTHER"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "EntityExpirationReasonEnum")
@XmlEnum
public enum EntityExpirationReasonEnum {


    /**
     * The entity ceased to operate. 
     * 
     */
    DISSOLVED,

    /**
     * The entity was acquired or merged with another entity.
     *           
     * 
     */
    CORPORATE_ACTION,

    /**
     * The reason for expiry is neither of 
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;code xmlns:lei="http://www.gleif.org/data/schema/leidata/2016" xmlns:xs="http://www.w3.org/2001/XMLSchema"&gt;DISSOLVED&lt;/code&gt;
     * </pre>
     *  nor
     *               
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;code xmlns:lei="http://www.gleif.org/data/schema/leidata/2016" xmlns:xs="http://www.w3.org/2001/XMLSchema"&gt;CORPORATE_ACTION&lt;/code&gt;
     * </pre>
     * 
     *           
     * 
     */
    OTHER;

    public String value() {
        return name();
    }

    public static EntityExpirationReasonEnum fromValue(String v) {
        return valueOf(v);
    }

}