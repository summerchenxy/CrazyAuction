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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author alex_zy
 */
@Entity
public class CreditTransaction implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long creditTransactionId;
    @Column(nullable = false)
    private Date transactionDateTime;
    @ManyToOne(optional = false)
    private Customer purchasingCustomer;
    @ManyToOne
    private CreditPackage purchasedCreditPackage;
    @Column(nullable = false)
    private Integer unitPurchased;

    public CreditTransaction() {
    }

    public CreditTransaction(Date transactionDateTime, Customer purchasingCustomer, CreditPackage purchasedCreditPackage, Integer unitPurchased) {
        this.transactionDateTime = transactionDateTime;
        this.purchasingCustomer = purchasingCustomer;
        this.purchasedCreditPackage = purchasedCreditPackage;
        this.unitPurchased = unitPurchased;
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

    public CreditPackage getPurchasedCreditPackage() {
        return purchasedCreditPackage;
    }

    public void setPurchasedCreditPackage(CreditPackage purchasedCreditPackage) {
        this.purchasedCreditPackage = purchasedCreditPackage;
    }

    public Integer getUnitPurchased() {
        return unitPurchased;
    }

    public void setUnitPurchased(Integer unitPurchased) {
        this.unitPurchased = unitPurchased;
    }



}