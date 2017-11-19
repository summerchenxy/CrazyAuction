/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author alex_zy
 */
@Entity
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;
    @Column(length = 32, nullable = false)
    private String lineOne;
    @Column(length = 32, nullable = true)
    private String lineTwo;
    @Column(length = 6)
    private String zipCode;
    private Boolean isAssociatedWithWinningBid;//shipping
    private Boolean enabled; //psuedo-delete if associated with a winning bit

    @ManyToOne
    @JoinColumn(nullable = true)
    private Customer customer;
    @OneToMany(mappedBy = "address")
    private List<Bid> bids = new ArrayList();

    public Address() {
        this.bids = new ArrayList<>();
        enabled = true;
        isAssociatedWithWinningBid = false;
    }

    public Boolean getIsAssociatedWithWinningBid() {
        return isAssociatedWithWinningBid;
    }

    public void setIsAssociatedWithWinningBid(Boolean isAssociatedWithWinningBid) {
        this.isAssociatedWithWinningBid = isAssociatedWithWinningBid;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Address(String lineOne, String lineTwo, String zipCode, Customer customer) {
        this();
        this.customer = customer;
        this.lineOne = lineOne;
        this.lineTwo = lineTwo;
        this.zipCode = zipCode;
    }

    private boolean IsEnabled() {
        return this.enabled;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getLineOne() {
        return lineOne;
    }

    public void setLineOne(String lineOne) {
        this.lineOne = lineOne;
    }

    public String getLineTwo() {
        return lineTwo;
    }

    public void setLineTwo(String lineTwo) {
        this.lineTwo = lineTwo;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

}
