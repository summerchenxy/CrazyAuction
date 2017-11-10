/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
    private BigDecimal initialCredit;
    @Column(nullable = false, precision = 18, scale = 4)
    private BigDecimal availableCredit;
    @Column(nullable = false)
    private Boolean enabled;
    @OneToOne(mappedBy = "purchasedCreditPackage")
    private List<CreditTransaction> transactions;
            
    public CreditPackage() {
    }

    public CreditPackage(Long creditPackageId, BigDecimal price, BigDecimal initialCredit, Boolean enabled) {
        this.price = price;
        this.creditPackageId = creditPackageId;
        this.initialCredit = initialCredit;
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

    public BigDecimal getInitialCredit() {
        return initialCredit;
    }

    public final void setInitialCredit(BigDecimal initialCredit) {
        this.initialCredit = initialCredit;
    }

    public BigDecimal getAvailableCredit() {
        return availableCredit;
    }

    public void setAvailableCredit(BigDecimal availableCredit) {
        this.availableCredit = availableCredit;
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
