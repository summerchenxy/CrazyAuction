/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proxyclient;

import java.util.Scanner;

/**
 *
 * @author Summer
 */
public class MainApp {

    public MainApp() {
    }
    public void runApp()
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
                    remoteLogin();
                    menuMain();
                    break;
                case 2:
                    break;
            }
            if (response == 2) {
                break;
            }
        }
    }
    
    public void remoteLogin(){
        
    }
    
    public void menuMain(){
        Scanner sc = new Scanner(System.in);

        while (true) {
            Integer response = 0;
            System.out.println("\n*** Proxy Bidding cum Sniping Agent:: Main Menu ***\n");
//            System.out.printf("You are logged in as %s\n", currentCustomer.getUsername());
            System.out.println("1: Premium Registration");
            System.out.println("2: Remote View Credit Balance");
            System.out.println("3: Remote View An Auction Listing Details");
            System.out.println("4: Remote Browse All Auction Listings");
            System.out.println("5: Remote View Won Auction Listings");
            System.out.println("6: Remote Logout\n");

            while (response < 1 || response > 6) {
                System.out.print("> ");
                try {
                    response = Integer.valueOf(sc.nextLine().trim());
                } catch (Exception ex) {
                    response = 0;
                }

                if (response < 1 || response > 6) {
                    System.err.println("\nInvalid option, please try again!");
                }
            }

            switch (response) {
                case 1:
                    premiumRegister();
                    break;
                case 2:
                    remoteViewCreditBalance();
                    break;
                case 3:
                    remoteViewAuctionListingDetails();
                    break;
                case 4:
                    remoteBrowseAllAuctionListing();
                    break;
                case 5:
                    remoteViewWonAuctionListing();
                    break;
                case 6:
                    remoteLogout();
            }
            if (response == 6) {
                remoteLogout();
            }
        }
    }
    
    public void premiumRegister(){
        
    }
    
    public void remoteLogout(){
        
    }
    
    public void remoteViewCreditBalance(){
        
    }
    
    public void remoteViewAuctionListingDetails(){
        Scanner sc = new Scanner(System.in);

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
                    configureProxyBidding();
                    break;
                case 2:
                    configureSniping();
                    break;
                case 3:
                    break;
            }
            if (response == 3) {
                break;
            }
        }
    }
    
    public void configureProxyBidding(){
        
    }
    
    public void configureSniping(){
        
    }
    
    public void remoteBrowseAllAuctionListing(){
        
    }
    
    public void remoteViewWonAuctionListing(){
        
    }
}
