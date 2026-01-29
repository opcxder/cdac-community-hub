package com.cdac.hostel.dto;

import java.math.BigDecimal;

public class HostelRequest {

    private String hostelName;
    private String description;
    private String address;
    private String city;
    private String locality;

    private BigDecimal monthlyRentMin;
    private BigDecimal monthlyRentMax;

    private Boolean hasWifi;
    private Boolean hasAc;
    private Boolean hasMess;
    private Boolean hasLaundry;

    private String contactPersonName;
    private String contactPersonPhone;

    // Getters and Setters
    public String getHostelName() {
        return hostelName;
    }

    public void setHostelName(String hostelName) {
        this.hostelName = hostelName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public BigDecimal getMonthlyRentMin() {
        return monthlyRentMin;
    }

    public void setMonthlyRentMin(BigDecimal monthlyRentMin) {
        this.monthlyRentMin = monthlyRentMin;
    }

    public BigDecimal getMonthlyRentMax() {
        return monthlyRentMax;
    }

    public void setMonthlyRentMax(BigDecimal monthlyRentMax) {
        this.monthlyRentMax = monthlyRentMax;
    }

    public Boolean getHasWifi() {
        return hasWifi;
    }

    public void setHasWifi(Boolean hasWifi) {
        this.hasWifi = hasWifi;
    }

    public Boolean getHasAc() {
        return hasAc;
    }

    public void setHasAc(Boolean hasAc) {
        this.hasAc = hasAc;
    }

    public Boolean getHasMess() {
        return hasMess;
    }

    public void setHasMess(Boolean hasMess) {
        this.hasMess = hasMess;
    }

    public Boolean getHasLaundry() {
        return hasLaundry;
    }

    public void setHasLaundry(Boolean hasLaundry) {
        this.hasLaundry = hasLaundry;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getContactPersonPhone() {
        return contactPersonPhone;
    }

    public void setContactPersonPhone(String contactPersonPhone) {
        this.contactPersonPhone = contactPersonPhone;
    }
}
