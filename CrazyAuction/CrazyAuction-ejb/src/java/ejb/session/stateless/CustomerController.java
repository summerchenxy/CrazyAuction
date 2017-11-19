/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Address;
import entity.CreditTransaction;
import entity.Customer;
import java.math.BigDecimal;
import java.util.Scanner;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.ws.WebServiceRef;
import util.exception.CustomerNotFoundException;
import util.exception.CustomerPasswordChangeException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author alex_zy
 */
@Stateless
@Local(CustomerControllerLocal.class)
@Remote(CustomerControllerRemote.class)
public class CustomerController implements CustomerControllerRemote, CustomerControllerLocal {

    @EJB
    private AddressControllerLocal addressController;

    @PersistenceContext(unitName = "CrazyAuction-ejbPU")
    private EntityManager em;

    @Override
    public Customer createNewCustomer(Customer customer) {
        em.persist(customer);
        for (Address address : customer.getAddresses()) {
            addressController.createNewAddress(address);
        }
        em.flush();
        em.refresh(customer);
        return customer;
    }

    @Override
    public void updateCustomer(Customer customer) {
        em.merge(customer);
//        for (Address address : customer.getAddresses()) {
//            em.merge(address);
//        }
//        for (CreditTransaction ct : customer.getCreditTransactionHistory()) {
//            em.merge(ct);
//        }
    }

    @Override
    public Customer retrieveCustomerByUsername(String username) throws CustomerNotFoundException {
        Query query = em.createQuery("SELECT s FROM Customer s WHERE s.username = :inUsername");
        query.setParameter("inUsername", username);
        try {
            Customer customer = (Customer) query.getSingleResult();
            customer.getAddresses().size();
            customer.getCreditTransactionHistory().size();
            customer.getWonBids().size();
            return customer;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new CustomerNotFoundException("Customer Username " + username + " does not exist!");
        }
    }

    @Override
    public Customer doLogin(String username, String password) throws InvalidLoginCredentialException {
        try {
            Customer customer = retrieveCustomerByUsername(username);
            if (customer.getPassword().equals(password)) {
                return customer;
            } else {
                throw new InvalidLoginCredentialException("Invalid password!");
            }
        } catch (CustomerNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist!");
        }
    }
//        public Customer changeCustomerName(Long customerId, String firstName, String lastName){
//            Customer customer = em.find(Customer.class, customerId);
//            customer.setFirstName(firstName);
//            customer.setLastName(lastName);
//            updateCustomer(customer);
//            return customer;
//        }

    @Override
    public BigDecimal retrieveCustomerCreditBalance(Long customerId) {
        Customer customer = em.find(Customer.class, customerId);
        return customer.getCreditBalance();
    }

}
