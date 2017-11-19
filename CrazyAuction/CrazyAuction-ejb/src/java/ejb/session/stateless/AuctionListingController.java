/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AuctionListing;
import entity.Bid;
import static java.lang.Boolean.FALSE;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
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
import util.exception.AuctionListingNotFoundException;

/**
 *
 * @author alex_zy
=======*/

/**
 *
 * @author Summer
 */
@Stateless
@Local(AuctionListingControllerLocal.class)
@Remote(AuctionListingControllerRemote.class)
public class AuctionListingController implements AuctionListingControllerRemote, AuctionListingControllerLocal {

    @PersistenceContext(unitName = "CrazyAuction-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public AuctionListingController() {
    }
    
    
    @Override
    public Long createAuctionListing(AuctionListing auctionListing)
    {
        em.persist(auctionListing);
        em.flush();
        //em.refresh(auctionListing);
        
        return auctionListing.getAuctionListingId();
    }
    
    @Override
    public void updateAuctionListing(AuctionListing auctionListing)
    {
        em.merge(auctionListing);
    }
    
    @Override
    public void deleteAuctionListing(Long auctionListingId) throws AuctionListingNotFoundException
    {
        AuctionListing auctionListing = retrieveAuctionListingByAuctionListingId(auctionListingId);
        if (auctionListing.getBidList().isEmpty()){
            em.remove(auctionListing);
            em.flush();
        }
        else{//listing used. mark as disabled
            auctionListing.setEnabled(FALSE);
        };
    }
    
 
    @Override
    public List<AuctionListing> retrieveAllAuctionListings()
    {
        Query query = em.createQuery("SELECT s FROM AuctionListing s");
        return query.getResultList();
    }
    
    @Override
    public void assignWinningBid(AuctionListing auctionListing){
        auctionListing.setStatus(CLOSED);
        BigDecimal highestBidValue = BigDecimal.ZERO;
        Bid highestBid = new Bid();
        int size = auctionListing.getBidList().size();
        //1. has no bid hence no winner
        if (size == 0){
            auctionListing.setWinningBid(null);
            auctionListing.setWinningBidValue(BigDecimal.ZERO);
        }
        else{
            for (Bid bid: auctionListing.getBidList()){
                highestBidValue = highestBidValue.max(bid.getCreditValue());
                if (highestBidValue.compareTo(bid.getCreditValue())==0){
                    highestBid = bid;
                }
            }
            auctionListing.setWinningBid(highestBid);
            auctionListing.setWinningBidValue(highestBidValue);
            //done for 2. no reserve price but has bids or 3a. highest bid above reserve price
            //set manual for 3b. highest bid same or below reserve price. 
            if (auctionListing.getReservePrice().compareTo(BigDecimal.ZERO) > 0 
                    && auctionListing.getReservePrice().compareTo(highestBidValue) > 0){
                auctionListing.setStatus(MANUAL);
            }
        }
        updateAuctionListing(auctionListing);
    }
    
    @Override
    public AuctionListing retrieveAuctionListingByAuctionListingId(Long auctionListingId) throws AuctionListingNotFoundException
    {
        AuctionListing auctionListing = em.find(AuctionListing.class, auctionListingId);
        
        if(auctionListingId != null)
        {
            return auctionListing;
        }
        else
        {
            throw new AuctionListingNotFoundException("Auction Listing ID " + auctionListingId + " does not exist!");
        }
    }
    
    @Override
    public List<AuctionListing> retrieveClosedAuctions(){
        AuctionStatus status = CLOSED;
        Query query = em.createQuery("SELECT s FROM AuctionListing s WHERE s.status = :inStatus");
        query.setParameter("inStatus", status);
        return query.getResultList();
    }
    
    @Override
    public List<AuctionListing> retrieveOpenedAuctions(){
        AuctionStatus status = OPENED;
        Query query = em.createQuery("SELECT s FROM AuctionListing s WHERE s.status = :inStatus");
        query.setParameter("inStatus", status);
        return query.getResultList();
    }
    
    @Override
    public List<AuctionListing> retrieveAllAuctionListingsRequiringManualIntervention()
    {
        AuctionStatus status = MANUAL;
        Query query = em.createQuery("SELECT s FROM AuctionListing s WHERE s.status = :inStatus");
        query.setParameter("inStatus", status);
        return query.getResultList();
    }
    @Override
    public void openAuction(){
        List<AuctionListing> auctionListings = retrieveClosedAuctions();
        for (AuctionListing auctionListing: auctionListings){
            if (auctionListing.getStartDateTime().compareTo(new Date())<=0){
                auctionListing.setStatus(OPENED);
                updateAuctionListing(auctionListing);
            }
        }
    }
    
    @Override
    public void closeAuction(){
        List<AuctionListing> auctionListings = retrieveOpenedAuctions();
        for (AuctionListing auctionListing: auctionListings){
            if (auctionListing.getEndDateTime().compareTo(new Date())>=0){
                assignWinningBid(auctionListing);
            }
        }
    }


    public void persist(Object object) {
        em.persist(object);
    }
}
