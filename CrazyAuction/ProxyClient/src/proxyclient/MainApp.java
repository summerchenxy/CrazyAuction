/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proxyclient;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import ws.client.AuctionListing;
//import ws.client.AuctionListingNotFoundException_Exception;
import ws.client.CustomerNotFoundException_Exception;
import ws.client.InvalidLoginCredentialException;
import ws.client.InvalidLoginCredentialException_Exception;

/**
 *
 * @author Summer
 */
public class MainApp {
    private ws.client.Customer currentCustomer;

    public MainApp() {
    }
    public void runApp() throws InvalidLoginCredentialException_Exception, CustomerNotFoundException_Exception/*, AuctionListingNotFoundException_Exception*/
    {
        Scanner sc = new Scanner(System.in);
        while (true) {
            Integer response = 0;
            System.out.println("\n*** Proxy Bidding cum Sniping Agent:: Welcome ***\n");
            System.out.println("1: Remote Login");
            System.out.println("2: Exit\n");

            while (response < 1 || response > 2) {
                System.out.print("> ");
                try {
                    response = Integer.valueOf(sc.nextLine().trim());
                } catch (Exception ex) {
                    response = 0;
                }

                if (response < 1 || response > 2) {
                    System.err.println("\nInvalid option, please try again!");
                }
            }

            switch (response) {
                case 1:
                    doRemoteLogin();
                    //doCheckIfPremium();
                    break;
                case 2:
                    break;
            }
            if (response == 2) {
                break;
            }
        }
    }
    
    public void doRemoteLogin() throws InvalidLoginCredentialException_Exception, CustomerNotFoundException_Exception{
        Scanner sc = new Scanner(System.in);
        String username;
        String password = "";

        System.out.println("\n*** Proxy Bidding cum Sniping Agent:: Login***\n");
        username = doReadToken("username");
        password = doReadToken("password");
        if (username.length() > 0 && password.length() > 0) {
            currentCustomer = remoteLogin(username, password);
            System.out.println();
            System.out.println("Login successful!\n");
        }
    }
    
    private String doReadToken(String tokenName) {
        Scanner sc = new Scanner(System.in);
        System.out.printf("Enter your %s%n", tokenName);
        System.out.print("> ");
        String token = sc.next();
        sc.nextLine();
        return token;
    }
    
    /*public void doCheckIfPremium() throws AuctionListingNotFoundException_Exception{
        if(checkIfPremium(currentCustomer)){
            menuMain();
        }
        else{
            Scanner sc = new Scanner(System.in);
            while (true) {
                Integer response = 0;
                System.out.println("1: Register as a premium customer");
                System.out.println("2: Exit\n");

                while (response < 1 || response > 2) {
                    System.out.print("> ");
                    try {
                        response = Integer.valueOf(sc.nextLine().trim());
                    } catch (Exception ex) {
                        response = 0;
                    }

                    if (response < 1 || response > 2) {
                        System.err.println("\nInvalid option, please try again!");
                    }
                }

                switch (response) {
                    case 1:
                        doPremiumRegister();
                        break;
                    case 2:
                        break;
                }
                if (response == 2) {
                    break;
                }
            }
        }
    }
    
    public void menuMain() throws AuctionListingNotFoundException_Exception{
        Scanner sc = new Scanner(System.in);

        while (true) {
            Integer response = 0;
            System.out.println("\n*** Proxy Bidding cum Sniping Agent:: Main Menu ***\n");
            System.out.printf("You are logged in as %s\n", currentCustomer.getUsername());
            System.out.println("1: Remote View Credit Balance");
            System.out.println("2: Remote View Auction Listing Details");
            System.out.println("3: Remote Browse All Auction Listings");
            System.out.println("4: Remote View Won Auction Listings");
            System.out.println("5: Remote Logout\n");

            while (response < 1 || response > 5) {
                System.out.print("> ");
                try {
                    response = Integer.valueOf(sc.nextLine().trim());
                } catch (Exception ex) {
                    response = 0;
                }

                if (response < 1 || response > 5) {
                    System.err.println("\nInvalid option, please try again!");
                }
            }

            switch (response) {
                case 1:
                    doRemoteViewCreditBalance();
                    break;
                case 2:
                    doRemoteViewAuctionListingDetails();
                    break;
                case 3:
                    doRemoteBrowseAllAuctionListings();
                    break;
                case 4:
                    doRemoteViewWonAuctionListings();
                    break;
                case 5:
                    doRemoteLogout();
            }
            if (response == 5) {
                doRemoteLogout();
            }
        }
    }
    
    public void doPremiumRegister() throws AuctionListingNotFoundException_Exception{
        premiumRegistration(currentCustomer);
        System.out.println("You have successfully registered as premium customer!");
        menuMain();
    }
    
    public void doRemoteLogout(){
        remoteLogout(currentCustomer);
        System.out.println("You have successfully logged out!");
    }
    
    public void doRemoteViewCreditBalance(){
        BigDecimal creditBalance = remoteViewCreditBalance(currentCustomer);
        DecimalFormat df = new DecimalFormat("0.00");
        System.out.println("Your Credit balance is "+df.format(creditBalance.floatValue())+"\n");
    }
    
    public void doRemoteViewAuctionListingDetails() throws AuctionListingNotFoundException_Exception{
        Scanner sc = new Scanner(System.in);
        System.out.println();
        System.out.print("Enter AuctionListing ID> ");
        Long auctionListingId = sc.nextLong();
        AuctionListing al = remoteViewAuctionListingDetail(auctionListingId);
        while (true) {
            Integer response = 0;
            System.out.println("\n*** Proxy Bidding cum Sniping Agent:: View Auction Listing Detail ***\n");
            System.out.println("1: Configure Proxy Bidding for Auction Listing");
            System.out.println("2: Configure Sniping for Auction Listing");
            System.out.println("3: Exit\n");

            while (response < 1 || response > 3) {
                System.out.print("> ");
                try {
                    response = Integer.valueOf(sc.nextLine().trim());
                } catch (Exception ex) {
                    response = 0;
                }

                if (response < 1 || response > 3) {
                    System.err.println("\nInvalid option, please try again!");
                }
            }

            switch (response) {
                case 1:
                    doConfigureProxyBidding(al);
                    break;
                case 2:
                    doConfigureSniping(al);
                    break;
                case 3:
                    break;
            }
            if (response == 3) {
                break;
            }
        }
    }
    
    public void doConfigureProxyBidding(AuctionListing al){
        Scanner sc = new Scanner(System.in);
        BigDecimal maxAmt = null;
        while (maxAmt == null) {
            try {
                System.out.print("> ");
                maxAmt = BigDecimal.valueOf(sc.nextDouble());
            } catch (Exception ex) {
            }
        }
        System.out.println("You have successfully configured proxy bidding for auction listing!");
    }
    
    public void doConfigureSniping(AuctionListing al){
        Scanner sc = new Scanner(System.in);
        DateFormat format = new SimpleDateFormat("yyyy.MM.dd.HH.mm");
        System.out.println("Auction Listing ID "+al.getAuctionListingId().toString()+"expires on " + format.format(al.getEndDateTime()));
        System.out.println("Enter End Date in yyyy.MM.dd.HH format ");
        Date date = null;
        while (date == null) {
            System.out.print("> ");
            String line = sc.nextLine();
            try {
                date = format.parse(line);
            } catch (ParseException e) {
                System.out.println("Sorry, that's not valid. Please try again.");
            }
        }
        System.out.println("You have successfully configured sniping for auction listing!");
    }
    
    public void doRemoteBrowseAllAuctionListings(){
        remoteBrowseAllAuctionListings();
    }
    
    public void doRemoteViewWonAuctionListings(){
        remoteViewWonAuctionListings(currentCustomer);
    }*/

    private static ws.client.Customer remoteLogin(java.lang.String username, java.lang.String password) throws InvalidLoginCredentialException_Exception, CustomerNotFoundException_Exception {
        ws.client.CrazyAuctionWebService_Service service = new ws.client.CrazyAuctionWebService_Service();
        ws.client.CrazyAuctionWebService port = service.getCrazyAuctionWebServicePort();
        return port.remoteLogin(username, password);
    }

    /*private static ws.client.Customer remoteLogout(ws.client.Customer customer) {
        ws.client.CrazyAuctionWebService_Service service = new ws.client.CrazyAuctionWebService_Service();
        ws.client.CrazyAuctionWebService port = service.getCrazyAuctionWebServicePort();
        return port.remoteLogout(customer);
    }

    private static BigDecimal remoteViewCreditBalance(ws.client.Customer customer) {
        ws.client.CrazyAuctionWebService_Service service = new ws.client.CrazyAuctionWebService_Service();
        ws.client.CrazyAuctionWebService port = service.getCrazyAuctionWebServicePort();
        return port.remoteViewCreditBalance(customer);
    }

    private static ws.client.AuctionListing remoteViewAuctionListingDetail(Long auctionListingId) throws AuctionListingNotFoundException_Exception {
        ws.client.CrazyAuctionWebService_Service service = new ws.client.CrazyAuctionWebService_Service();
        ws.client.CrazyAuctionWebService port = service.getCrazyAuctionWebServicePort();
        return port.remoteViewAuctionListingDetail(auctionListingId);
    }

    private static boolean checkIfPremium(ws.client.Customer customer) {
        ws.client.CrazyAuctionWebService_Service service = new ws.client.CrazyAuctionWebService_Service();
        ws.client.CrazyAuctionWebService port = service.getCrazyAuctionWebServicePort();
        return port.checkIfPremium(customer);
    }

    private static void premiumRegistration(ws.client.Customer customer) {
        ws.client.CrazyAuctionWebService_Service service = new ws.client.CrazyAuctionWebService_Service();
        ws.client.CrazyAuctionWebService port = service.getCrazyAuctionWebServicePort();
        port.premiumRegistration(customer);
    }

    private static void remoteBrowseAllAuctionListings() {
        ws.client.CrazyAuctionWebService_Service service = new ws.client.CrazyAuctionWebService_Service();
        ws.client.CrazyAuctionWebService port = service.getCrazyAuctionWebServicePort();
        port.remoteBrowseAllAuctionListings();
    }

    private static void remoteViewWonAuctionListings(ws.client.Customer customer) {
        ws.client.CrazyAuctionWebService_Service service = new ws.client.CrazyAuctionWebService_Service();
        ws.client.CrazyAuctionWebService port = service.getCrazyAuctionWebServicePort();
        port.remoteViewWonAuctionListings(customer);
    }*/
    
}
