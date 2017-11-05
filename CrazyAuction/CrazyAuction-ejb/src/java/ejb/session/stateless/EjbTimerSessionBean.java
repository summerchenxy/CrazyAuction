/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AuctionListing;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import util.enumeration.AuctionStatus;

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
    
    @Schedule(hour = "*/1", info = "auctionListingStatusCheckTimer")
    @Override
    public void auctionListingStatusCheckTimer()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
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
                System.out.println("********** EjbTimerSession.auctionListingStatusCheckTimer(): Auction Listing with ID " + auctionListing.getAuctionListingId()+" Timeout at " + currentDate);
            }
            else if(auctionListing.getStartDateTime().after(currentDate))
            {
                //start auction listing
                auctionListing.setStatus(AuctionStatus.OPENED);
            }
        }
    }
}
