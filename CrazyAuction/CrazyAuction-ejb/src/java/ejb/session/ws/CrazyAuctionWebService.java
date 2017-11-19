/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import entity.AuctionListing;
import entity.Bid;
import entity.Customer;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Scanner;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.exception.AuctionListingNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialException;
import ejb.session.stateless.CustomerControllerLocal;
import ejb.session.stateless.AuctionListingControllerLocal;

/**
 *
 * @author Summer
 */
@WebService(serviceName = "CrazyAuctionWebService")
@Stateless()
public class CrazyAuctionWebService {

    @EJB
    private CustomerControllerLocal customerControllerLocal;
    @EJB
    private AuctionListingControllerLocal auctionListingControllerLocal;

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "checkIfPremium")
    public boolean checkIfPremium(@WebParam(name = "customer") Customer currentCustomer) 
    {
        Boolean isPremium = currentCustomer.getIfPremium();
        return isPremium;
    }
    
    @WebMethod(operationName = "premiumRegistration")
    public void premiumRegistration(@WebParam(name = "customer") Customer currentCustomer) 
    {
        currentCustomer.setPremium(Boolean.TRUE);
        customerControllerLocal.updateCustomer(currentCustomer);
    }
    
    @WebMethod(operationName = "remoteLogin")
    public Customer remoteLogin(@WebParam(name = "username") String username,
                              @WebParam(name = "password") String password) 
                                throws InvalidLoginCredentialException, CustomerNotFoundException
    {
        Customer remoteCustomer = customerControllerLocal.doLogin(username, password);
        System.out.println("********** AuctionListingWebService.remoteLogin(): Customer " 
                            + remoteCustomer.getUsername() 
                            + " login remotely via web service");
        
        return remoteCustomer;
    }
    
    //debug
    @WebMethod(operationName = "remoteLogout")
    public Customer remoteLogout(@WebParam(name = "customer") Customer currentCustomer) 
    {
        currentCustomer = null;
        System.out.println("********** AuctionListingWebService.remoteLogout(): Customer " 
                            + currentCustomer.getUsername() 
                            + " logout remotely via web service");
        return currentCustomer;
    }
    
    @WebMethod(operationName = "remoteViewCreditBalance")
    public BigDecimal remoteViewCreditBalance(@WebParam(name = "customer") Customer currentCustomer) 
    {
        BigDecimal creditBalance = new BigDecimal(0);
        creditBalance = currentCustomer.getCreditBalance();
        System.out.println("********** AuctionListingWebService.remoteiewCreditBalance(): Customer " 
                            + currentCustomer.getUsername() 
                            + " view credit balance remotely via web service");
        return creditBalance;
    }
    
    @WebMethod(operationName = "remoteViewAuctionListingDetail")
    public AuctionListing remoteViewAuctionListingDetail(@WebParam(name = "auctionListingID") Long auctionListingId) throws AuctionListingNotFoundException 
    {
        System.out.println("********** AuctionListingWebService.remoteViewAuctionListingDetail(): Auction Listing ID " 
                            + auctionListingId);
        AuctionListing al = auctionListingControllerLocal.retrieveAuctionListingByAuctionListingId(auctionListingId);
        DecimalFormat df = new DecimalFormat("0.00");
        System.out.printf("%8s%20s%20s%15s%20s\n", "Start Date Time", "End Date Time", "Reserve Price", "Winning Bid");
        System.out.printf("%8s%20s%20s%15s%20s\n",
                al.getStartDateTime().toString(), al.getEndDateTime().toString(),
                al.getReservePrice().toString(), df.format(al.getWinningBidValue().floatValue()));
        System.out.println("------------------------");
        return al;
    }
    
    @WebMethod(operationName = "remoteBrowseAllAuctionListings")
    public void remoteBrowseAllAuctionListings() 
    {
        System.out.println("********** AuctionListingWebService.remoteBrowseAllAuctionListings(): " );
        List<AuctionListing> alList = auctionListingControllerLocal.retrieveAllAuctionListings();
        DecimalFormat df = new DecimalFormat("0.00");
        System.out.printf("%8s%20s%20s%15s%20s\n", "Start Date Time", "End Date Time", "Reserve Price", "Winning Bid");    
        for (AuctionListing al: alList){
            System.out.printf("%8s%20s%20s%15s%20s\n",al.getAuctionListingId().toString(),
                al.getStartDateTime().toString(), al.getEndDateTime().toString(),
                al.getReservePrice().toString(), df.format(al.getWinningBidValue().floatValue()));
        }
    }
    
    @WebMethod(operationName = "remoteViewWonAuctionListings")
    public void remoteViewWonAuctionListings(@WebParam(name = "customer") Customer currentCustomer) 
    {
        System.out.println("********** AuctionListingWebService.remoteViewWonAuctionListings(): " );
        List<Bid> wonBids = currentCustomer.getWonBids();
        DecimalFormat df = new DecimalFormat("0.00");
        System.out.printf("%8s%20s%20s%15s%20s\n", "ID","Start Date Time", "End Date Time", "Reserve Price", "Winning Bid");    
        for (Bid b : wonBids) {
            AuctionListing al = b.getAuctionListing();
            System.out.printf("%8s%20s%20s%15s%20s\n", al.getAuctionListingId().toString(),
                al.getStartDateTime().toString(), al.getEndDateTime().toString(),
                al.getReservePrice().toString(), df.format(al.getWinningBidValue().floatValue()));
        }
    }
    
    @WebMethod(operationName = "configureProxyBidding")
    public void configureProxyBidding(@WebParam(name = "auctionListing") AuctionListing al,
                                        @WebParam(name = "maxAmount") BigDecimal maxAmount) 
    {
        System.out.println("********** AuctionListingWebService.configureProxyBidding(): ID " +al.getAuctionListingId().toString());
        
    }
    
    private double getSmallestIncrementWithCurrentBid(BigDecimal highestBid) {

        double smallestIncrement = 0;
        if (highestBid.compareTo(new BigDecimal(1)) < 0) {
            smallestIncrement = .05;
        } else if (highestBid.compareTo(new BigDecimal(5)) < 0) {
            smallestIncrement = .25;
        } else if (highestBid.compareTo(new BigDecimal(25)) < 0) {
            smallestIncrement = .50;
        } else if (highestBid.compareTo(new BigDecimal(100)) < 0) {
            smallestIncrement = 1.00;
        } else if (highestBid.compareTo(new BigDecimal(250)) < 0) {
            smallestIncrement = 2.50;
        } else if (highestBid.compareTo(new BigDecimal(500)) < 0) {
            smallestIncrement = 5.00;
        } else if (highestBid.compareTo(new BigDecimal(1000)) < 0) {
            smallestIncrement = 10.00;
        } else if (highestBid.compareTo(new BigDecimal(2500)) < 0) {
            smallestIncrement = 25.00;
        } else if (highestBid.compareTo(new BigDecimal(5000)) < 0) {
            smallestIncrement = 50.00;
        } else {
            smallestIncrement = 100.00;
        }
        return smallestIncrement;
    }
}
