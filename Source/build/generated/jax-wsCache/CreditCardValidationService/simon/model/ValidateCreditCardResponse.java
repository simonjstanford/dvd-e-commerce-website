
package simon.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ValidateCreditCardResult" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "validateCreditCardResult"
})
@XmlRootElement(name = "ValidateCreditCardResponse")
public class ValidateCreditCardResponse {

    @XmlElement(name = "ValidateCreditCardResult")
    protected boolean validateCreditCardResult;

    /**
     * Gets the value of the validateCreditCardResult property.
     * 
     */
    public boolean isValidateCreditCardResult() {
        return validateCreditCardResult;
    }

    /**
     * Sets the value of the validateCreditCardResult property.
     * 
     */
    public void setValidateCreditCardResult(boolean value) {
        this.validateCreditCardResult = value;
    }

}
