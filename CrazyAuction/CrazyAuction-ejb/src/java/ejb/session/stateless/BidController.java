/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Bid;
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
@Local(BidControllerLocal.class)
@Remote(BidControllerRemote.class)
public class BidController implements BidControllerRemote, BidControllerLocal {

    @PersistenceContext(unitName = "CrazyAuction-ejbPU")
    private EntityManager em;


    @Override
    public Bid createNewBid(Bid bid)
    {
        em.persist(bid);
        em.flush();
        em.refresh(bid);
        return bid;
    }
}
