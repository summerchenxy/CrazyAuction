/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.CustomerControllerRemote;
import entity.Customer;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author Summer
 */
@WebService(serviceName = "CrazyAuctionWebService")
@Stateless()
public class CrazyAuctionWebService {

    @EJB
    private CustomerControllerRemote customerController;

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "remoteLogin")
    public void remoteLogin(@WebParam(name = "username") String username,
                              @WebParam(name = "password") String password) 
                                throws InvalidLoginCredentialException, CustomerNotFoundException
    {
        Customer customer = customerController.retrieveCustomerByUsername(username);
        System.out.println("********** AuctionListingWebService.remoteLogin(): Customer " 
                            + customer.getUsername() 
                            + " login remotely via web service");
        
        //return CustomerControllerRemote.login();
    }
}
