/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Bid;
import entity.CreditPackage;
import entity.Customer;
import java.math.BigDecimal;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.BidNotFoundException;

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
    
        
    @Override
    public void refundToCustomer(Bid bid){
        BigDecimal creditValue = bid.getCreditValue();
        Customer customer = bid.getCreditTransaction().getPurchasingCustomer();
        customer.addCreditBalance(creditValue);
        bid.setCreditValue(BigDecimal.ZERO);
        em.merge(bid);
        em.merge(customer);
    }
    
    @Override
     public Bid retrieveBidByBidId(Long bidId) throws BidNotFoundException
    {
        Bid bid = em.find(Bid.class, bidId);
        
        if(bidId != null)
        {
            return bid;
        }
        else
        {
            throw new BidNotFoundException("Bid ID " + bidId + " does not exist!");
        }
    }
     
    @Override
    public Bid retrieveBidByCreditValue(BigDecimal creditValue) throws BidNotFoundException
    {
        Query query = em.createQuery("SELECT s FROM Bid s WHERE s.creditValue = :inCreditValue");
        query.setParameter("inCreditValue", creditValue);
        
        try
        {
            return (Bid)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new BidNotFoundException("Bid of credit value " + creditValue.toString() + " does not exist!");
        }
    }
}
