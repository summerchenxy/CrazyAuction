/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author alex_zy
 */
@Entity
public class CreditPackage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long creditPackageId;
    @Column(nullable = false, precision = 18, scale = 4)
    private BigDecimal price;
    @Column(nullable = false, precision = 18, scale = 4)
    private BigDecimal credit;
    @Column(nullable = false)
    private Boolean enabled;
    @OneToMany(mappedBy = "creditPackage")
    private List<CreditTransaction> creditTransactions = new ArrayList<CreditTransaction>();
            
    public CreditPackage() {
        this.creditTransactions = new ArrayList<>();
        enabled = true;
    }

    public CreditPackage(BigDecimal price, BigDecimal credit) {
        this();
        this.price = price;
        this.credit = credit;
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

    public List<CreditTransaction> getCreditTransactions() {
        return creditTransactions;
    }

    public void setCreditTransactions(List<CreditTransaction> creditTransactions) {
        this.creditTransactions = creditTransactions;
    }   
}
