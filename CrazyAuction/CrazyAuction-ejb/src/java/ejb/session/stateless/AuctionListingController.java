/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AuctionListing;
import entity.Bid;
import entity.CreditTransaction;
import entity.Customer;
import static java.lang.Boolean.FALSE;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.AuctionStatus;
import static util.enumeration.AuctionStatus.CLOSED;
import static util.enumeration.AuctionStatus.MANUAL;
import static util.enumeration.AuctionStatus.OPENED;
import util.enumeration.TransactionTypeEnum;
import util.exception.AuctionListingNotFoundException;

/**
 *
 * @author alex_zy =======
 */
/**
 *
 * @author Summer
 */
@Stateless
@Local(AuctionListingControllerLocal.class)
@Remote(AuctionListingControllerRemote.class)
public class AuctionListingController implements AuctionListingControllerLocal, AuctionListingControllerRemote {

    @PersistenceContext(unitName = "CrazyAuction-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public AuctionListingController() {
    }

    @Override
    public Long createAuctionListing(AuctionListing auctionListing) {
        em.persist(auctionListing);
        em.flush();
        em.refresh(auctionListing);

        return auctionListing.getAuctionListingId();
    }

    @Override
    public void updateAuctionListing(AuctionListing auctionListing) {
        em.merge(auctionListing);
    }

    @Override
    public void deleteAuctionListing(Long auctionListingId) throws AuctionListingNotFoundException {
        AuctionListing auctionListing = retrieveAuctionListingByAuctionListingId(auctionListingId);
        if (auctionListing.getBidList().size() == 0) {
            em.remove(auctionListing);
            em.flush();
        } else {//listing used. mark as disabled
            auctionListing.setEnabled(Boolean.FALSE);
        };
    }

    @Override
    public List<AuctionListing> retrieveAllAuctionListings() {
        Query query = em.createQuery("SELECT s FROM AuctionListing s");
        List<AuctionListing> auctionListings = query.getResultList();
        for (AuctionListing al : auctionListings) {
            al.getBidList().size();
        }
        return auctionListings;
    }

    @Override
    public void assignWinningBid(Long auctionListingId) {
        AuctionListing auctionListing = null;
        try {
            auctionListing = retrieveAuctionListingByAuctionListingId(auctionListingId);
        } catch (AuctionListingNotFoundException ex) {
        }
        auctionListing.setStatus(CLOSED);
        BigDecimal highestBidValue = BigDecimal.ZERO;
        Bid highestBid = new Bid();
        int size = auctionListing.getBidList().size();
        //1. has no bid hence no winner
        if (size == 0) {
            auctionListing.setWinningBid(null);
            auctionListing.setWinningBidValue(BigDecimal.ZERO);
        } else {
            highestBid = getHighestBid(auctionListing);
            highestBidValue = highestBid.getCreditValue();
            auctionListing.setWinningBid(highestBid);
            auctionListing.setWinningBidValue(highestBidValue);
            //add bid to wonbids of customer
            Customer customer = highestBid.getCreditTransaction().getCustomer();
            customer.getWonBids().add(highestBid);
            em.merge(customer);
            //refund the non-winning bids
            for (Bid bid : auctionListing.getBidList()) {
                if (highestBid.getBidId().compareTo(bid.getBidId())!=0) {
                    refundBid(bid);
                }
            }
            //done for 2. no reserve price but has bids or 3a. highest bid above reserve price
            //set manual for 3b. highest bid same or below reserve price. 
            if (auctionListing.getReservePrice().compareTo(BigDecimal.ZERO) > 0
                    && auctionListing.getReservePrice().compareTo(highestBidValue) > 0) {
                auctionListing.setStatus(MANUAL);
            }
        }
        updateAuctionListing(auctionListing);
    }
    
    @Override
    public void refundBid(Bid bid){
        //create a new transaction to add back the credit to customer
        Customer customer = bid.getCreditTransaction().getCustomer();
        CreditTransaction ct = new CreditTransaction();
        ct.setType(TransactionTypeEnum.REFUND);
        ct.setCustomer(customer);
        customer.addCreditBalance(bid.getCreditValue());
        em.merge(customer);
        em.persist(ct);
        em.flush();
        em.refresh(ct);
    }
    
    @Override
    public Bid getHighestBid(AuctionListing auctionListing){
        BigDecimal highestBidValue = BigDecimal.ZERO;
        Bid highestBid = new Bid();
        for (Bid bid : auctionListing.getBidList()) {
                highestBidValue = highestBidValue.max(bid.getCreditValue());
                if (highestBidValue.compareTo(bid.getCreditValue()) == 0) {
                    highestBid = bid;
                }
            }
        return highestBid;
    }

    @Override
    public AuctionListing retrieveAuctionListingByAuctionListingId(Long auctionListingId) throws AuctionListingNotFoundException {
        AuctionListing auctionListing = em.find(AuctionListing.class, auctionListingId);

        if (auctionListingId != null) {
            auctionListing.getBidList().size();
            System.out.print(auctionListing.getBidList().size());
            return auctionListing;
        } else {
            throw new AuctionListingNotFoundException("Auction Listing ID " + auctionListingId + " does not exist!");
        }
    }

    @Override
    public List<AuctionListing> retrieveClosedAuctions() {
        AuctionStatus status = AuctionStatus.CLOSED;
        Query query = em.createQuery("SELECT s FROM AuctionListing s WHERE s.status = :inStatus");
        query.setParameter("inStatus", status);

        List<AuctionListing> als = query.getResultList();
        for (AuctionListing al : als) {
            al.getBidList().size();
        }
        return als;
    }

    @Override
    public List<AuctionListing> retrieveOpenedAuctions() {
        AuctionStatus status = AuctionStatus.OPENED;
        Query query = em.createQuery("SELECT s FROM AuctionListing s WHERE s.status = :inStatus");
        query.setParameter("inStatus", status);
        List<AuctionListing> als = query.getResultList();
        for (AuctionListing al : als) {
            al.getBidList().size();
        }
        return als;
    }

    @Override
    public List<AuctionListing> retrieveAllAuctionListingsRequiringManualIntervention() {
        AuctionStatus status = AuctionStatus.MANUAL;
        Boolean checkFinal = false;
        Query query = em.createQuery("SELECT s FROM AuctionListing s WHERE s.status = :inStatus AND s.isFinal = :inFinal");
        query.setParameter("inStatus", status);
        query.setParameter("inFinal", checkFinal);

        List<AuctionListing> als = query.getResultList();
        for (AuctionListing al : als) {
            al.getBidList().size();
        }
        return als;
    }

    @Override
    public void openAuction() {
        List<AuctionListing> auctionListings = retrieveClosedAuctions();
        for (AuctionListing auctionListing : auctionListings) {
            if (auctionListing.getStartDateTime().compareTo(new Date()) <= 0) {
                auctionListing.setStatus(AuctionStatus.OPENED);
                updateAuctionListing(auctionListing);
            }
        }
    }

    @Override
    public void closeAuction() {
        List<AuctionListing> auctionListings = retrieveOpenedAuctions();
        for (AuctionListing auctionListing : auctionListings) {
            if (auctionListing.getEndDateTime().compareTo(new Date()) >= 0) {

                auctionListing.setStatus(CLOSED);
                assignWinningBid(auctionListing.getAuctionListingId());
            }
        }
    }

    public void persist(Object object) {
        em.persist(object);
    }
}
