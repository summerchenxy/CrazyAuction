/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import java.math.BigDecimal;
import java.util.Scanner;
import util.exception.CustomerNotFoundException;
import util.exception.CustomerPasswordChangeException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author alex_zy
 */
public interface CustomerControllerRemote {

    public Customer createNewCustomer(Customer customer);

    public void updateCustomer(Customer customer);

    public Customer retrieveCustomerByUsername(String username) throws CustomerNotFoundException;

    public Customer doLogin(String username, String password) throws InvalidLoginCredentialException;
//    public Customer changeCustomerName(Long customerId, String firstName, String lastName);

    public BigDecimal retrieveCustomerCreditBalance(Long customerId);


    public Customer retrieveCustomerById(Long id) throws CustomerNotFoundException;

    public Customer retrieveCustomerById(String id) throws CustomerNotFoundException;

    public void doPurchaseCreditPackage(Long creditPackageId, Long customerId, int unit);

}
