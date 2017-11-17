/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AuctionListingControllerLocal;
import java.util.Date;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.SessionContext;
import javax.ejb.Startup;

/**
 *
 * @author Summer
 */
@Singleton
@LocalBean
@Startup
public class AuctionTimerSessionBean {

    @EJB
    private AuctionListingControllerLocal auctionListingControllerLocal;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Resource
    private SessionContext sessionContext;
    
    @Schedule(hour = "*", minute = "*/5", info = "auctionTimerCheckOpenAuctionEvery5s")
    public void automaticActivateAuctionTimer()
    {
        System.out.println("Activate Auction Timer event "+new Date());
        //auctionListingControllerLocal.openAuction(auctionListing);
    }
    @Schedule(hour = "*", minute = "*/5", info = "auctionTimerCheckCloseAuctionEvery5s")
    public void automaticCloseAuctionTimer()
    {
        System.out.println("Activate Auction Timer event "+new Date());
        //auctionListingControllerLocal.closeAuction(auctionListing);
    }
    
}
