/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long customerId;
    
    @Column (length = 32, nullable = false)
    private String firstName;
    @Column (length = 32, nullable = false)
    private String lastName;
    @Column (length = 9, nullable = false, unique = true)
    private String identificationNumber;
    @OneToMany(mappedBy = "customer")
    private Collection<Address> addresses= new ArrayList<Address>();
    @OneToMany(mappedBy = "purchasingCustomer")
    private Collection<CreditTransaction> creditBalance = new ArrayList<CreditTransaction>();
    @Column(nullable=false)
    private String password;

    public Customer() {
    }

    public Customer(String firstName, String lastName, String identificationNumber, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.identificationNumber = identificationNumber;
        this.password = password;
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

    public Collection<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Collection<Address> addresses) {
        this.addresses = addresses;
    }

    public Collection<CreditTransaction> getCreditBalance() {
        return creditBalance;
    }

    public void setCreditBalance(Collection<CreditTransaction> creditBalance) {
        this.creditBalance = creditBalance;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
}