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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author alex_zy
 */
@Entity
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    
    @Column (length = 32, nullable = false)
    private String firstName;
    @Column (length = 32, nullable = false)
    private String lastName;
    @Column (length = 9, nullable = false, unique = true)
    private String identificationNumber;
    @Column(nullable=false)
    private String password;
    private BigDecimal creditBalance;
    @OneToMany(mappedBy = "customer")
    private List<Address> addresses= new ArrayList<Address>();
    @OneToMany(mappedBy = "purchasingCustomer")
    private List<CreditTransaction> creditTransactionHistory = new ArrayList<CreditTransaction>();
    @Column(length=32, nullable = false, unique = true)
    private String loginCredential;




    public Customer() {
    }

    
    public Customer(String firstName, String lastName, String identificationNumber, String password, BigDecimal creditBalance, String loginCredential) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.identificationNumber = identificationNumber;
        this.password = password;
        this.creditBalance = creditBalance;
        this.loginCredential = loginCredential;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public List<CreditTransaction> getreditTransactionHistory() {
        return creditTransactionHistory;
    }

    public void setCreditBalance(List<CreditTransaction> creditTransactionHistory) {
        this.creditTransactionHistory = creditTransactionHistory;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public BigDecimal getCreditBalance() {
        return creditBalance;
    }

    public void setCreditBalance(BigDecimal creditBalance) {
        this.creditBalance = creditBalance;
    }

    public List<CreditTransaction> getCreditTransactionHistory() {
        return creditTransactionHistory;
    }

    public void setCreditTransactionHistory(List<CreditTransaction> creditTransactionHistory) {
        this.creditTransactionHistory = creditTransactionHistory;
    }
    
    public String getLoginCredential() {
        return loginCredential;
    }

    public void setLoginCredential(String loginCredential) {
        this.loginCredential = loginCredential;
    }
}