/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author alex_zy
 */
@Entity
public class Bid implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bidId;
    private BigDecimal creditValue;
    @ManyToOne
    @JoinColumn(nullable = true)
    private Address address; 
    @ManyToOne
    @JoinColumn(nullable = false)
    private AuctionListing auctionListing; 
    @OneToOne
    private CreditTransaction creditTransaction;
    
    public Bid() {
    }

    public Bid(BigDecimal creditValue, AuctionListing auctionListing) {
        this.creditValue = creditValue;
        this.auctionListing = auctionListing;
    }

    public Long getBidId() {
        return bidId;
    }

    public void setBidId(Long bidId) {
        this.bidId = bidId;
    }

    public BigDecimal getCreditValue() {
        return creditValue;
    }

    public void setCreditValue(BigDecimal creditValue) {
        this.creditValue = creditValue;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public AuctionListing getAuctionListing() {
        return auctionListing;
    }

    public void setAuctionListing(AuctionListing auctionListing) {
        this.auctionListing = auctionListing;
    }

    public CreditTransaction getCreditTransaction() {
        return creditTransaction;
    }

    public void setCreditTransaction(CreditTransaction creditTransaction) {
        this.creditTransaction = creditTransaction;
    }
    
}