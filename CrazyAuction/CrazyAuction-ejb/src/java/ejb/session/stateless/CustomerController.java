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
import javax.xml.ws.WebServiceRef;
import util.exception.CustomerNotFoundException;
import util.exception.CustomerPasswordChangeException;
import ws.client.CrazyAuctionWebService_Service;
import ws.client.CustomerNotFoundException_Exception;
import ws.client.InvalidLoginCredentialException_Exception;


/**
 *
 * @author alex_zy
 */
@Stateless
@Local(CustomerControllerLocal.class)
@Remote(CustomerControllerRemote.class)
public class CustomerController implements CustomerControllerRemote, CustomerControllerLocal {

    @WebServiceRef(wsdlLocation = "META-INF/wsdl/localhost_8080/CrazyAuctionWebService/CrazyAuctionWebService.wsdl")
    private CrazyAuctionWebService_Service service;

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
    public Customer retrieveCustomerByUsername(String username) throws CustomerNotFoundException
    {
        Query query = em.createQuery("SELECT s FROM Customer s WHERE s.username = :inUsername");
        query.setParameter("inUsername", username);
        
        try
        {
            return (Customer)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new CustomerNotFoundException("Customer Username " + username + " does not exist!");
        }
    }

    private void remoteLogin(java.lang.String username, java.lang.String password) throws InvalidLoginCredentialException_Exception, CustomerNotFoundException_Exception {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.CrazyAuctionWebService port = service.getCrazyAuctionWebServicePort();
        port.remoteLogin(username, password);
    }
    
    

}
