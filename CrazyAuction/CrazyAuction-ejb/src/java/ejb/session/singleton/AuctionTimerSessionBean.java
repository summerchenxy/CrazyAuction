/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AuctionListingControllerLocal;
import ejb.session.ws.CrazyAuctionWebService;
import entity.AuctionListing;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.SessionContext;
import javax.ejb.Startup;
import java.lang.Long;

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
    @EJB 
    private CrazyAuctionWebService crazyAuctionWebService;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Resource
    private SessionContext sessionContext;
    
    
    @Schedule(hour = "*", minute = "*/1", info = "auctionTimerCheckOpenAuctionEvery5s")
    public void automaticActivateAuctionTimer()
    {
        System.out.println("Activate Auction Timer event "+new Date());
        auctionListingControllerLocal.openAuction();
    }
    
    @Schedule(hour = "*", minute = "*/1", info = "auctionTimerCheckCloseAuctionEvery5s")
    public void automaticCloseAuctionTimer()
    {
        System.out.println("Close Auction Timer event "+new Date());
        auctionListingControllerLocal.closeAuction();
    }
    
    @Schedule(hour = "*", minute = "*/1", info = "auctionTimerCheckCloseAuctionEvery5s")
    public void automaticProxyBidding()
    {
        System.out.println("Proxy Bidding Place Bid event "+new Date());
        //crazyAuctionWebService.configureProxyBidding(auctionListingId, maxAmount, custId);
    }
    @Schedule(hour = "*", minute = "*/1", info = "auctionTimerCheckCloseAuctionEvery5s")
    public void automaticSniping()
    {
        System.out.println("Sniping Place Bid event "+new Date());
        //crazyAuctionWebService.configureSniping(Long.MIN_VALUE, BigDecimal.ONE, username, password);
    }
}
