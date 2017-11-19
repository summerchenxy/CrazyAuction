/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.CustomerControllerRemote;
import entity.AuctionListing;
import entity.Bid;
import entity.Customer;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author Summer
 */
@WebService(serviceName = "CrazyAuctionWebService")
@Stateless()
public class CrazyAuctionWebService {

    @EJB
    private CustomerControllerRemote customerControllerRemote;

    /**
     * This is a sample web service operation
     */
    
    @WebMethod(operationName = "remoteLogin")
    public Customer remoteLogin(@WebParam(name = "username") String username,
                              @WebParam(name = "password") String password) 
                                throws InvalidLoginCredentialException, CustomerNotFoundException
    {
        Customer remoteCustomer = customerControllerRemote.doLogin(username, password);
        System.out.println("********** AuctionListingWebService.remoteLogin(): Customer " 
                            + remoteCustomer.getUsername() 
                            + " login remotely via web service");
        
        return remoteCustomer;
    }
    
    //debug
    @WebMethod(operationName = "remoteLogout")
    public void remoteLogout(@WebParam(name = "customer") Customer currentCustomer) 
    {
        currentCustomer = null;
        System.out.println("********** AuctionListingWebService.remoteLogout(): Customer " 
                            + currentCustomer.getUsername() 
                            + " logout remotely via web service");
        System.out.println("You have successfully logged out\n");
    }
    
    @WebMethod(operationName = "remoteViewCreditBalance")
    public void remoteViewCreditBalance(@WebParam(name = "customer") Customer currentCustomer) 
    {
        BigDecimal creditBalance = null;
        creditBalance = currentCustomer.getCreditBalance();
        System.out.println("********** AuctionListingWebService.remoteiewCreditBalance(): Customer " 
                            + currentCustomer.getUsername() 
                            + " view credit balance remotely via web service");
        System.out.println("Your Credit balance is "+creditBalance.toString()+"\n");
    }
    
    @WebMethod(operationName = "remoteViewAuctionListingDetail")
    public void remoteViewAuctionListingDetail(@WebParam(name = "auctionListing") AuctionListing al) 
    {
        System.out.println("********** AuctionListingWebService.remoteViewAuctionListingDetail(): Auction Listing ID " 
                            + al.getAuctionListingId());
        System.out.println("Start Date: " + al.getStartDateTime());
        System.out.println("End Date: " + al.getEndDateTime());
        System.out.println("Reserve Price: " + al.getReservePrice());
        List<Bid> bids = al.getBidList();
        BigDecimal highestBid = new BigDecimal(0);
        for (Bid b : bids) {
            if (b.getCreditValue().compareTo(highestBid) == 1) {
                highestBid = b.getCreditValue();
            }
        }
        DecimalFormat df = new DecimalFormat("0.00");
        System.out.println("Current Highest Bid: " + df.format(highestBid.floatValue()));
//        may display current smallest increment here
        System.out.println();
    }
}
