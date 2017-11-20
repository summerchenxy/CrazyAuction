/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
    @Column(nullable = true)
    private int creditPacketUnit;
    @Column(nullable = true)
    private BigDecimal biddedAmount;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Customer customer;
    @ManyToOne(optional = true)
    private CreditPackage creditPackage;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionTypeEnum type;
    @OneToOne(mappedBy = "creditTransaction", optional = true)
    private Bid bid;

    public CreditTransaction() {
        this.transactionDateTime = new Date();
    }

    public CreditTransaction(Customer purchasingCustomer, CreditPackage creditPackages, int unit) {
        this.customer = purchasingCustomer;
        this.creditPackage = creditPackages;
        this.creditPacketUnit = unit;
        this.type = TransactionTypeEnum.DEBIT;
    }

    public CreditTransaction(Customer purchasingCustomer, Bid bid, BigDecimal biddedAmount) {
        this.customer = purchasingCustomer;
        this.type = TransactionTypeEnum.CREDIT;
        this.bid = bid;
        this.biddedAmount = biddedAmount;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public CreditPackage getCreditPackage() {
        return creditPackage;
    }

    public void setCreditPackage(CreditPackage creditPackage) {
        this.creditPackage = creditPackage;
    }

    public Bid getBid() {
        return bid;
    }

    public void setBid(Bid bid) {
        this.bid = bid;
    }

    public BigDecimal getBiddedAmount() {
        return biddedAmount;
    }

    public void setBiddedAmount(BigDecimal biddedAmount) {
        this.biddedAmount = biddedAmount;
    }

}
