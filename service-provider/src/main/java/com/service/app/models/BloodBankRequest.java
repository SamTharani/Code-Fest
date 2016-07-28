package com.service.app.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class BloodBankRequest implements Serializable {

    @Id
    // @GeneratedValue(strategy = GenerationType.AUTO)
    private int requestId;
    private int pinCode;
    private String bloodGroup;
    private String neededType;

    public BloodBankRequest() {
    }

    public BloodBankRequest(int requestId, int pinCode, String bloodGroup, String neededType) {
        this.requestId = requestId;
        this.pinCode = pinCode;
        this.bloodGroup = bloodGroup;
        this.neededType = neededType;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getNeededType() {
        return neededType;
    }

    public void setNeededType(String neededType) {
        this.neededType = neededType;
    }

    public int getPinCode() {
        return pinCode;
    }

    public void setPinCode(int pinCode) {
        this.pinCode = pinCode;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

}
