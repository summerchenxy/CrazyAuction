/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author Summer
 */
public class CreditPackage {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long creditPackageId;
    @Column(nullable = false, precision = 18, scale = 4)
    private BigDecimal price;
    @Column(nullable = false, precision = 18, scale = 4)
    private BigDecimal credit;
    @Column(nullable = false)
    private Boolean enabled;
    @OneToOne(mappedBy = "purchasedCreditPackage", optional = true)
    private List<CreditTransaction> transactions;
            
    public CreditPackage() {
    }

    public CreditPackage(Long creditPackageId, BigDecimal price, BigDecimal initialCredit, Boolean enabled) {
        this.price = price;
        this.creditPackageId = creditPackageId;
        this.credit = initialCredit;
        this.enabled = enabled;
    }

    public Long getCreditPackageId() {
        return creditPackageId;
    }

    public void setCreditPackageId(Long creditPackageId) {
        this.creditPackageId = creditPackageId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public final void setCredit(BigDecimal credit) {
        this.credit = credit;
    }


    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<CreditTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<CreditTransaction> transactions) {
        this.transactions = transactions;
    }   
}
