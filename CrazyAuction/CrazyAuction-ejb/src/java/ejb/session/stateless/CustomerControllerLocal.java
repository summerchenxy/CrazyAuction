/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import util.exception.CustomerNotFoundException;
import util.exception.CustomerPasswordChangeException;


/**
 *
 * @author alex_zy
 */
public interface CustomerControllerLocal {

    public Customer createNewCustomer(Customer customer);
    public void updateCustomer(Customer customer);

    public Customer retrieveCustomerById(Long customerId, Boolean fetchAddresses, Boolean fetchCreditBalance, Boolean fetchCreditTransactionHistory) throws CustomerNotFoundException;

    public void changePassword(Long customerId, String currentPassword, String newPassword) throws CustomerNotFoundException, CustomerPasswordChangeException;
    
}
