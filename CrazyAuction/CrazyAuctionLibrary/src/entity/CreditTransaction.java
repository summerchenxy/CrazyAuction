/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import util.enumeration.AccessRightEnum;
import util.enumeration.TransactionTypeEnum;

/**
 *
 * @author alex_zy
 */
@Entity
public class CreditTransaction implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long creditTransactionId;
    @Column(nullable = false)
    private Date transactionDateTime;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Customer purchasingCustomer;
    @OneToOne(optional=true)
    private CreditPackage creditPackages; 
    private int creditPacketUnit;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionTypeEnum type;
    @OneToOne(mappedBy = "creditTransaction", optional=true)
    private Bid bid;
    


    public CreditTransaction() {
    }

    public CreditTransaction(Date transactionDateTime, Customer purchasingCustomer, CreditPackage creditPackages, int creditPacketUnit, TransactionTypeEnum type, Bid bid) {
        this.transactionDateTime = transactionDateTime;
        this.purchasingCustomer = purchasingCustomer;
        this.creditPackages = creditPackages;
        this.creditPacketUnit = creditPacketUnit;
        this.type = type;
        this.bid = bid;
    }

    public int getCreditPacketUnit() {
        return creditPacketUnit;
    }

    public void setCreditPacketUnit(int creditPacketUnit) {
        this.creditPacketUnit = creditPacketUnit;
    }

    public TransactionTypeEnum getType() {
        return type;
    }

    public void setType(TransactionTypeEnum type) {
        this.type = type;
    }

    public Long getCreditTransactionId() {
        return creditTransactionId;
    }

    public void setCreditTransactionId(Long creditTransactionId) {
        this.creditTransactionId = creditTransactionId;
    }

    public Date getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(Date transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public Customer getPurchasingCustomer() {
        return purchasingCustomer;
    }

    public void setPurchasingCustomer(Customer purchasingCustomer) {
        this.purchasingCustomer = purchasingCustomer;
    }

    public CreditPackage getCreditPackages() {
        return creditPackages;
    }

    public void setCreditPackages(CreditPackage creditPackages) {
        this.creditPackages = creditPackages;
    }

    public Bid getBid() {
        return bid;
    }

    public void setBid(Bid bid) {
        this.bid = bid;
    }

}