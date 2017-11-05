/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Address;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    public Address createNewAddress(Address address)
    {
        em.persist(address);
        em.flush();
        em.refresh(address);
        return address;
    }
}
