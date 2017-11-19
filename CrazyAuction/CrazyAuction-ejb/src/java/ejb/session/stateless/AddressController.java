/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Address;
import entity.Customer;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.AddressNotFoundException;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author alex_zy
 */
@Stateless
@Local(AddressControllerLocal.class)
@Remote(AddressControllerRemote.class)
public class AddressController implements AddressControllerRemote, AddressControllerLocal {

    @PersistenceContext(unitName = "CrazyAuction-ejbPU")
    private EntityManager em;

    @Override
    public Address createNewAddress(Address address) {
        em.persist(address);
        em.flush();
        em.refresh(address);
        return address;
    }

    @Override
    public Address retrieveAddressById(Long addressId) throws AddressNotFoundException {
        Address address = em.find(Address.class, addressId);
        if (address == null) {
            System.out.println("test3");
            throw new AddressNotFoundException();
        } else {
            return address;
        }
    }

    @Override
    public void deleteAddress(Long addressId) throws AddressNotFoundException {
        Address addressEntityToRemove = retrieveAddressById(addressId);
        em.remove(addressEntityToRemove);
    }

    @Override
    public void updateAddress(Address address) {
        em.merge(address);
    }

    @Override
    public List<Address> retrieveAddressesByCustomerId(Long customerId) {
        Query query = em.createQuery("SELECT ad FROM Address ad WHERE ad.Customer_CustomerId = :inCustomerId");
        query.setParameter("inCustomerId", customerId);
        return query.getResultList();

    }
}
