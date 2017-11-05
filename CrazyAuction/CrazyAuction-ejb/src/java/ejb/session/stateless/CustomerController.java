/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Address;
import entity.CreditTransaction;
import entity.Customer;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CustomerNotFoundException;
import util.exception.CustomerPasswordChangeException;


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
    public Customer createNewCustomer(Customer customer)
    {
        em.persist(customer);
        for (Address address:customer.getAddresses())
            addressController.createNewAddress(address);
        em.flush();
        em.refresh(customer);
        return customer;
    }
    
    
    @Override
    public void updateCustomer(Customer customer)
    {
        em.merge(customer);
        for (Address address:customer.getAddresses())
            em.merge(address);
        for (CreditTransaction ct: customer.getCreditTransactionHistory())
            em.merge(ct);
    }
    
    @Override
    public Customer retrieveCustomerById(Long customerId, Boolean fetchAddresses, Boolean fetchCreditBalance, Boolean fetchCreditTransactionHistory) throws CustomerNotFoundException
    {
        Customer customer = em.find(Customer.class, customerId);
        if (customer!=null)
        {
            if (fetchAddresses)
                customer.getAddresses();
            if(fetchCreditBalance)
                customer.getCreditBalance();
            if (fetchCreditTransactionHistory)
                customer.getCreditTransactionHistory();
            return customer;
        }
        else
        {
            throw new CustomerNotFoundException("Customer ID " + customerId + " does not exist");
        }
    }
    
    @Override
    public void changePassword(Long customerId, String currentPassword, String newPassword) throws CustomerNotFoundException, CustomerPasswordChangeException
    {
        Customer customer = retrieveCustomerById(customerId, false, false, false);
        
        if(customer.getPassword().equals(currentPassword))
        {
            customer.setPassword(newPassword);
        }
        else
        {
            throw new CustomerPasswordChangeException("Current password is invalid");
        }
    }
}
