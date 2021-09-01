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
 * <p>Java class for MeasurementMethodTypeEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="MeasurementMethodTypeEnum"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="ACCOUNTING_CONSOLIDATION"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "MeasurementMethodTypeEnum")
@XmlEnum
public enum MeasurementMethodTypeEnum {


    /**
     * Accounting consolidation holds when "[in the] financial
     *                         statements of a group [...] the assets, liabilities, equity, income,
     *                         expenses and cash flows of the parent and its subsidiaries are presented as
     *                         those of a single economic entity (please see
     *                         http://www.iasplus.com/en/standards/ias/ias27-2011).
     * 
     */
    ACCOUNTING_CONSOLIDATION;

    public String value() {
        return name();
    }

    public static MeasurementMethodTypeEnum fromValue(String v) {
        return valueOf(v);
    }

}