/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AuctionListing;
import entity.Bid;
import entity.CreditPackage;
import entity.CreditTransaction;
import entity.Customer;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    public Bid createNewBid(Bid bid) {
        
        em.persist(bid.getCreditTransaction());
        em.persist(bid);
        em.flush();
        em.refresh(bid);
        return bid;
    }
    
    @Override
    public void refundToCustomer(Long bidId) {
        Bid bid;
        try {
            bid = retrieveBidByBidId(bidId);
            
            BigDecimal creditValue = bid.getCreditValue();
            System.out.println(bid.getCreditTransaction().toString());
            CreditTransaction ct = bid.getCreditTransaction();
            System.out.println(ct.getCustomer().toString());
            Customer customer = ct.getCustomer();
            customer.addCreditBalance(creditValue);
            bid.setCreditValue(BigDecimal.ZERO);
            em.merge(bid);
            em.merge(customer);
        } catch (BidNotFoundException ex) {
            Logger.getLogger(BidController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    @Override
    public Bid retrieveBidByBidId(Long bidId) throws BidNotFoundException {
        Bid bid = em.find(Bid.class, bidId);
        
        if (bidId != null) {
            return bid;
        } else {
            throw new BidNotFoundException("Bid ID " + bidId + " does not exist!");
        }
    }
    
    @Override
    public Bid retrieveBidByCreditValue(BigDecimal creditValue) throws BidNotFoundException {
        Query query = em.createQuery("SELECT s FROM Bid s WHERE s.creditValue = :inCreditValue");
        query.setParameter("inCreditValue", creditValue);
        
        try {
            return (Bid) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new BidNotFoundException("Bid of credit value " + creditValue.toString() + " does not exist!");
        }
    }

    @Override
    public void updateBid(Bid bid) {
        em.merge(bid);
    }

    //a customer spent an amount of credit to bid for an auction listing
    @Override
    public void placeBid(Long customerId, Long auctionListingId, BigDecimal amount) {
        AuctionListing al = em.find(AuctionListing.class, auctionListingId);
        Customer customer = em.find(Customer.class, customerId);
        Bid bid = new Bid(amount, al);
        CreditTransaction ct = new CreditTransaction(customer, bid, amount);
        em.persist(ct);
        bid.setCreditTransaction(ct);
        em.persist(bid);
        customer.getCreditTransactionHistory().add(ct);
        em.merge(customer);
        al.getBidList().add(bid);
        em.merge(al);
        
        em.flush();
        
    }
}
