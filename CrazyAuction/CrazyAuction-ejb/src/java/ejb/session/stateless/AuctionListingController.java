/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AuctionListing;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.AuctionStatus;
import static util.enumeration.AuctionStatus.CLOSED;
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
    public AuctionListing createAuctionListing(AuctionListing auctionListing)
    {
        em.persist(auctionListing);
        em.flush();
        em.refresh(auctionListing);
        
        
        return auctionListing;
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
            auctionListing.setStatus(CLOSED);
        };
    }
    
 
    @Override
    public List<AuctionListing> retrieveAllAuctionListings()
    {
        Query query = em.createQuery("SELECT s FROM AuctionListing s");
        return query.getResultList();
    }
    
    @Override
    public List<AuctionListing> retrieveAllAuctionListingsRequiringManualIntervention()
    {
        /*must fullfil all 3 conditions:
            1. auction listing is closed
            2. auction listing has bids / winning bid is not 0
            3. acution listing has winning bid lower than the reserve price
        */
        AuctionStatus status = CLOSED;
        Query query = em.createQuery("SELECT s FROM AuctionListing s WHERE s.status = :inStatus AND s.winningBidValue > 0 AND s.winningBidValue < s.reservePrice");
        query.setParameter("inStatus", status);
        return query.getResultList();
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
    public void openAuction(AuctionListing auctionListing){
        auctionListing.setStatus(CLOSED);
        updateAuctionListing(auctionListing);
    }
    
    @Override
    public void closeAuction(AuctionListing auctionListing){
        auctionListing.setStatus(OPENED);
        updateAuctionListing(auctionListing);
    }


    public void persist(Object object) {
        em.persist(object);
    }
}
