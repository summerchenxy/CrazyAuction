/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Address;
import java.util.List;
import util.exception.AddressNotFoundException;

public interface AddressControllerLocal {

    public Address createNewAddress(Address address);

    public Address retrieveAddressById(Long addressId) throws AddressNotFoundException;

    public void deleteAddress(Long addressId) throws AddressNotFoundException;

    public void updateAddress(Address address);

    public List<Address> retrieveAddressesByCustomerId(Long customerId);

}
