//
// This file was generated by the Eclipse Implementation of JAXB, v3.0.0 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.07.28 at 04:03:08 PM CEST 
//


package org.gleif.data.schema.rr._2016;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RelationshipQuantifiersType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RelationshipQuantifiersType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="RelationshipQuantifier" type="{http://www.gleif.org/data/schema/rr/2016}RelationshipQuantifierType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RelationshipQuantifiersType", propOrder = {
    "relationshipQuantifier"
})
public class RelationshipQuantifiersType {

    @XmlElement(name = "RelationshipQuantifier")
    protected List<RelationshipQuantifierType> relationshipQuantifier;

    /**
     * Gets the value of the relationshipQuantifier property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the relationshipQuantifier property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRelationshipQuantifier().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RelationshipQuantifierType }
     * 
     * 
     */
    public List<RelationshipQuantifierType> getRelationshipQuantifier() {
        if (relationshipQuantifier == null) {
            relationshipQuantifier = new ArrayList<RelationshipQuantifierType>();
        }
        return this.relationshipQuantifier;
    }

}
