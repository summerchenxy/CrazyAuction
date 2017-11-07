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
public interface CustomerControllerRemote {
    
    public Customer createNewCustomer(Customer customer);
    public void updateCustomer(Customer customer);
    public Customer retrieveCustomerByUsername(String username) throws CustomerNotFoundException;
}
