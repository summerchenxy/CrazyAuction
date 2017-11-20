/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Address;
import entity.CreditPackage;
import entity.CreditTransaction;
import entity.Customer;
import java.math.BigDecimal;
import java.util.Date;
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
import util.enumeration.TransactionTypeEnum;
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
public class CustomerController implements CustomerControllerLocal, CustomerControllerRemote {

    @EJB
    private AddressControllerLocal addressController;

    @PersistenceContext(unitName = "CrazyAuction-ejbPU")
    private EntityManager em;

    @Override
    public Customer createNewCustomer(Customer customer) {
        em.persist(customer);
        System.out.println("test1" + customer.getAddresses().size());
        for (Address address : customer.getAddresses()) {
            address.setCustomer(customer);
            em.persist(address);
        }
        System.out.println("test2" + customer.getAddresses().size());
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
    public Customer retrieveCustomerById(String id) throws CustomerNotFoundException {
        return em.find(Customer.class, id);
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

    @Override
    public Customer retrieveCustomerById(Long id) throws CustomerNotFoundException {
        return em.find(Customer.class, id);
    }

    @Override
    public void doPurchaseCreditPackage(Long creditPackageId, Long customerId, int unit) { // - need to handle exceptions here
        CreditPackage cp = em.find(CreditPackage.class, creditPackageId);
        Customer customer = em.find(Customer.class, customerId);
        CreditTransaction ct = new CreditTransaction(customer, cp, unit);
        em.persist(ct);
        customer.getCreditTransactionHistory().add(ct);
        customer.setCreditBalance(customer.getCreditBalance().add(cp.getCredit().multiply(new BigDecimal(unit))));
//        em.merge(customer);
        cp.getCreditTransactions().add(ct);
//        em.merge(cp);
        em.flush();

    }

}
