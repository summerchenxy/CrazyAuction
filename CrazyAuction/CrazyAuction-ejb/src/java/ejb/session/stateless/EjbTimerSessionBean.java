/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AuctionListing;
import entity.Bid;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.AuctionStatus;
import util.exception.BidNotFoundException;

/**
 *
 * @author Summer
 */
@Stateless
@Local(EjbTimerSessionBeanLocal.class)
@Remote(EjbTimerSessionBeanRemote.class)
public class EjbTimerSessionBean implements EjbTimerSessionBeanRemote, EjbTimerSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @EJB
    private AuctionListingControllerLocal auctionListingControllerLocal;
    @PersistenceContext(unitName = "CrazyAuction-ejbPU")
    private EntityManager em;
    @EJB
    private BidControllerLocal bidControllerLocal;
    
    @Schedule(hour = "*/1", info = "auctionListingStatusCheckTimer")
    @Override
    public void auctionListingStatusCheckTimer()        
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH");
        Date currentDate = new Date();
        String currentDateString = sdf.format(currentDate);
        //System.out.println("********** EjbTimerSession.auctionListingStatusCheckTimer(): Timeout at " + currentDate);
        
        List<AuctionListing> auctionListings = auctionListingControllerLocal.retrieveAllAuctionListings();
        
        for(AuctionListing auctionListing:auctionListings)
        {
            if(auctionListing.getEndDateTime().after(currentDate))
            {
                //close auction listing
                auctionListing.setStatus(AuctionStatus.CLOSED);
                BigDecimal highestBidValue = BigDecimal.valueOf(0);
                List<Bid> bidList = auctionListing.getBidList();
                if (bidList.isEmpty()){
                    //1. the auction listing has no bid and thus no winner
                    auctionListing.setWinningBid(null);
                    auctionListing.setWinningBidValue(null);
                }
                else{
                    
                    for (Bid bid: bidList){
                        highestBidValue = highestBidValue.max(bid.getCreditValue());
                    }
                    //assign winning bid regardless of reserve price
                    //subject to further manual adjustment for winning bid below reserve price
                    try {
                        auctionListing.setWinningBidValue(highestBidValue);
                        Bid winningBid;
                        winningBid = bidControllerLocal.retrieveBidByCreditValue(highestBidValue);
                        auctionListing.setWinningBid(winningBid);
                    } catch (BidNotFoundException ex) {
                        Logger.getLogger(EjbTimerSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                //System.out.println("********** EjbTimerSession.auctionListingStatusCheckTimer(): Auction Listing with ID " + auctionListing.getAuctionListingId()+" Timeout at " + currentDate);
            }
            else if(auctionListing.getStartDateTime().after(currentDate))
            {
                //start auction listing
                auctionListing.setStatus(AuctionStatus.OPENED);
            } 
            em.merge(auctionListing);
        }
        
    }

    public void persist(Object object) {
        em.persist(object);
    }
}
