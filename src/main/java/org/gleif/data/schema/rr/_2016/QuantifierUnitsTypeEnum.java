//
// This file was generated by the Eclipse Implementation of JAXB, v3.0.0 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.07.28 at 04:03:08 PM CEST 
//


package org.gleif.data.schema.rr._2016;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for QuantifierUnitsTypeEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="QuantifierUnitsTypeEnum"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="PERCENTAGE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "QuantifierUnitsTypeEnum")
@XmlEnum
public enum QuantifierUnitsTypeEnum {

    PERCENTAGE;

    public String value() {
        return name();
    }

    public static QuantifierUnitsTypeEnum fromValue(String v) {
        return valueOf(v);
    }

}