/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package administrationclient;

import ejb.session.stateless.AuctionListingControllerRemote;
import ejb.session.stateless.EmployeeControllerRemote;
import entity.AuctionListing;
import entity.Employee;
import java.util.Scanner;
import util.enumeration.AccessRightEnum;
import util.exception.InvalidAccessRightException;

/**
 *
 * @author Summer
 */
public class SalesOperationModule {
    private EmployeeControllerRemote employeeControllerRemote;
    private AuctionListingControllerRemote auctionListingControllerRemote;
    private Employee currentEmployee;

    public SalesOperationModule() {
    }

    public SalesOperationModule(EmployeeControllerRemote employeeControllerRemote, AuctionListingControllerRemote auctionListingControllerRemote, Employee currenteEmployee) {
        this.employeeControllerRemote = employeeControllerRemote;
        this.auctionListingControllerRemote = auctionListingControllerRemote;
        this.currentEmployee = currentEmployee;
    }
    
    public void menuSalesOperation() throws InvalidAccessRightException{
        /*if(currentEmployee.getAccessRightEnum() != AccessRightEnum.SALES)
        {
            throw new InvalidAccessRightException("You don't have SALES Employee rights to access the sales operation module.");
        }
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        while(true)
        {
            System.out.println("*** OAS Administration Panel :: Sales Operation ***\n");
            System.out.println("1: Create New Auction Listing");
            System.out.println("2: View Auction Listing Details");
            System.out.println("3: View All Auction Listings");
            System.out.println("4: View All Auction Listings with Bids but Below Reserve Price");
            System.out.println("5: Back\n");
            response = 0;
            
            while(response < 1 || response > 5)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doCreateNewAuctionListing();
                }
                else if(response == 2)
                {
                    doViewAuctionListingDetails();
                }
                else if(response == 3)
                {
                    doViewAllAuctionListings();
                }
                else if(response == 4)
                {
                    doViewAllAuctionListingsRequiringManualIntervention();
                }
                else if (response == 5)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 7)
            {
                break;
            }
        }
    }
    private void doCreateNewAuctionListing()
    {
        Scanner scanner = new Scanner(System.in);
        AuctionListing newAuctionListing = new AuctionListing();
        
        System.out.println("*** OAS Administration Panel :: Sales Operation :: Create New Auction Listing ***\n");
        System.out.print("Enter Initial Credit> ");
        newAuctionListing.setPrice(scanner.nextBigDecimal());
        newAuctionListing.setInitialCredit(scanner.nextBigDecimal());
        newAuctionListing.setAvailableCredit(newAuctionListing.getInitialCredit());
        newAuctionListing.setEnabled(Boolean.TRUE);//assume that credit package is by default enabled when it is created
        
        newAuctionListing = auctionListingControllerRemote.createAuctionListing(newAuctionListing);
        System.out.println("New auctionListingcreated successfully!: " + newAuctionListing.getAuctionListingId()+ "\n");
    }
    
    private void doViewAuctionListingDetails()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** OAS Administration Panel :: Sales Operation :: View Auction Listing Details ***\n");
        System.out.print("Enter AuctionListing ID> ");
        Long auctionListingId = scanner.nextLong();
        
        try
        {
            AuctionListing auctionListing = auctionListingControllerRemote.retrieveAuctionListingByAuctionListingId(auctionListingId);
            System.out.printf("%8s%20s%20s%15s%20s%20s\n", "AuctionListing ID", "Price", "Initial Credit", "Available Credit", "Enabled Status", "Credit Transactions");
            System.out.printf("%8s%20s%20s%15s%20s%20s\n", 
                    auctionListing.getAuctionListingId().toString(), auctionListing.getPrice().toString(),auctionListing.getInitialCredit().toString(), auctionListing.getAvailableCredit().toString(), auctionListing.getEnabled().toString(), auctionListing.getTransactions().toArray().toString());         
            System.out.println("------------------------");
            System.out.println("1: Update Auction Listing");
            System.out.println("2: Delete Auction Listing");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = scanner.nextInt();

            if(response == 1)
            {
                doUpdateAuctionListing(auctionListing);
            }
            else if(response == 2)
            {
                doDeleteAuctionListing(auctionListing);
            }
        }
        catch(AuctionListingNotFoundException ex)
        {
            System.out.println("An error has occurred while retrieving auctionListing: " + ex.getMessage() + "\n");
        }
    }
    private void doUpdateAuctionListing(AuctionListing auctionListing)
    {
        Scanner scanner = new Scanner(System.in);        
        BigDecimal newPrice;
        String input;
        
        System.out.println("*** OAS Administration Panel :: Sales Operation :: View Auction Listing Details :: Update Auction Listing ***\n");
        
        System.out.print("Enter Price (blank if no change)> ");
        newPrice = scanner.nextBigDecimal();
        if(auctionListing.getTransactions().isEmpty())
        {
            auctionListing.setPrice(newPrice);
        }
        else{
            System.out.println("Price of the Auction Listing cannot be updated as it has been purchased");
        }
        
        System.out.print("Enter 'Enabled' or 'Disabled'> ");
        input = scanner.nextLine().trim();
        if (input.equals("Enabled")){
            auctionListing.setEnabled(Boolean.TRUE);
        }
        else if (input.equals("Disabled")){
            auctionListing.setEnabled(Boolean.FALSE);
        }
        else{
            System.out.println("Invalid option!\n");
        }
        auctionListingControllerRemote.updateAuctionListing(auctionListing);
        System.out.println("auctionListing updated successfully!\n");
    }
    
    private void doDeleteAuctionListing(AuctionListing auctionListing) throws AuctionListingNotFoundException
    {
        Scanner scanner = new Scanner(System.in);        
        String input;
        
        System.out.println("*** OAS Administration Panel :: Sales Operation :: View Auction Listing Details :: Delete Auction Listing ***\n");
        System.out.printf("Confirm Delete AuctionListing of price %s and initial credit of %s (Auction Listing ID: %d) (Enter 'Y' to Delete)> ", auctionListing.getPrice().toString(), auctionListing.getInitialCredit().toString(), auctionListing.getAuctionListingId());
        input = scanner.nextLine().trim();
        
        if(input.equals("Y"))
        {
            if(auctionListing.getInitialCredit().equals(auctionListing.getAvailableCredit())){
                auctionListingControllerRemote.deleteAuctionListing(auctionListing.getAuctionListingId());
                System.out.println("Auction Listing deleted successfully!\n");
            }
            else{//credit package is used and is marked as disabled
                auctionListing.setEnabled(Boolean.FALSE);
                auctionListingControllerRemote.updateAuctionListing(auctionListing);
                System.out.println("Auction Listing has been used hence it is diabled but not deleted!\n");
            }
        }
        else
        {
            System.out.println("AuctionListing NOT deleted!\n");
        }
    }
    private void doViewAllAuctionListings()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** OAS Administration Panel :: System Administration :: View All AuctionListings ***\n");
        
        List<AuctionListing> allAuctionListings = auctionListingControllerRemote.retrieveAllAuctionListings();
        System.out.printf("%8s%20s%20s%15s%20s%20s\n", "AuctionListing ID", "Price", "Initial Credit", "Available Credit", "Enabled Status", "Credit Transactions");

        for(AuctionListing auctionListing:allAuctionListings)
        {
            System.out.printf("%8s%20s%20s%15s%20s%20s\n", auctionListing.getAuctionListingId().toString(), auctionListing.getPrice().toString(),auctionListing.getInitialCredit().toString(), auctionListing.getAvailableCredit().toString(), auctionListing.getEnabled().toString(), auctionListing.getTransactions().toArray().toString());
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
    private void doViewAllAuctionListingsRequiringManualIntervention()
    {*/
        
    }
}