/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package administrationclient;

import ejb.session.stateless.AuctionListingControllerRemote;
import ejb.session.stateless.EmployeeControllerRemote;
import entity.AuctionListing;
import entity.Bid;
import entity.Employee;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.enumeration.AccessRightEnum;
import util.enumeration.AuctionStatus;
import util.exception.AuctionListingNotFoundException;
import util.exception.InvalidAccessRightException;

/**
 *
 * @author Summer
 */
public class SalesOperationModule {
    private EmployeeControllerRemote employeeControllerRemote;
    private AuctionListingControllerRemote auctionListingControllerRemote;
    private Employee currentEmployee;
    private static DateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");

    public SalesOperationModule() {
    }

    public SalesOperationModule(EmployeeControllerRemote employeeControllerRemote, AuctionListingControllerRemote auctionListingControllerRemote, Employee currenteEmployee) {
        this.employeeControllerRemote = employeeControllerRemote;
        this.auctionListingControllerRemote = auctionListingControllerRemote;
        this.currentEmployee = currentEmployee;
    }
    
    public void menuSalesOperation() throws InvalidAccessRightException, ParseException{
        if(currentEmployee.getAccessRightEnum() != AccessRightEnum.SALES)
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
                    //doViewAllAuctionListings();
                }
                else if(response == 4)
                {
                    //doViewAllAuctionListingsRequiringManualIntervention();
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
    private void doCreateNewAuctionListing() throws ParseException
    {
        Scanner scanner = new Scanner(System.in);
        AuctionListing newAuctionListing = new AuctionListing();
        
        System.out.println("*** OAS Administration Panel :: Sales Operation :: Create New Auction Listing ***\n");
        System.out.print("Enter Starting Bid Amount> ");
        newAuctionListing.setStartingBidAmount(scanner.nextBigDecimal());
        System.out.print("Enter Start Date in yyyy.MM.dd.HH format> ");
        String startDateString = scanner.nextLine().trim();
        Date startDate = formatter.parse(startDateString);
        newAuctionListing.setStartDateTime(startDate);
        System.out.print("Enter End Date in yyyy.MM.dd.HH format> ");
        String endDateString = scanner.nextLine().trim();
        Date endDate = formatter.parse(endDateString);
        newAuctionListing.setEndDateTime(endDate);
        //by default set it as disabled upon creation
        newAuctionListing.setStatus(AuctionStatus.CLOSED);
        System.out.print("Enter Description> ");
        newAuctionListing.setDescription(scanner.nextLine().trim());
        System.out.print("Enter Reserve Price (blank if no reserve price)> ");
        newAuctionListing.setReservePrice(scanner.nextBigDecimal());
        scanner.next();
        
        newAuctionListing = auctionListingControllerRemote.createAuctionListing(newAuctionListing);
        System.out.println("New auctionListingcreated successfully!: " + newAuctionListing.getAuctionListingId()+ "\n");
    }
    
    private void doViewAuctionListingDetails() throws ParseException
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** OAS Administration Panel :: Sales Operation :: View Auction Listing Details ***\n");
        System.out.print("Enter AuctionListing ID> ");
        Long auctionListingId = scanner.nextLong();
        
        try
        {
            AuctionListing auctionListing = auctionListingControllerRemote.retrieveAuctionListingByAuctionListingId(auctionListingId);
            System.out.printf("%8s%20s%20s%15s%20s%20s\n", "AuctionListing ID", "Starting Bid Amount", "Start Date Time", "End Date Time", "Status", "Description", "Reserve Price", "Bid List", "Winning Bid");
            System.out.printf("%8s%20s%20s%15s%20s%20s\n", 
                    auctionListing.getAuctionListingId().toString(), auctionListing.getStartingBidAmount().toString(), auctionListing.getStartDateTime().toString(),auctionListing.getEndDateTime().toString(), 
                    auctionListing.getStatus().toString(), auctionListing.getDescription(), auctionListing.getReservePrice().toString(), auctionListing.getBidList().toArray().toString(),auctionListing.getWinningBid().toString());         
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
    private void doUpdateAuctionListing(AuctionListing auctionListing) throws ParseException
    {
        Scanner scanner = new Scanner(System.in);        
        
        System.out.println("*** OAS Administration Panel :: Sales Operation :: View Auction Listing Details :: Update Auction Listing ***\n");
        
        System.out.print("Enter Starting Bid Amount (blank if not change)> ");
        auctionListing.setStartingBidAmount(scanner.nextBigDecimal());
        scanner.next();
        
        System.out.print("Enter Start Date in yyyy.MM.dd.HH format (blank if no change)> ");
        String startDateString = scanner.nextLine().trim();
        if (startDateString.length() >0){
            Date startDate = formatter.parse(startDateString);
            auctionListing.setStartDateTime(startDate);
        }
        
        System.out.print("Enter End Date in yyyy.MM.dd.HH format (blank if no change)> ");
        String endDateString = scanner.nextLine().trim();
        if (endDateString.length() >0){
            Date endDate = formatter.parse(endDateString);
            auctionListing.setEndDateTime(endDate);
        }
        
        System.out.print("Enter Description (blank if no change)> ");
        String input = scanner.nextLine().trim();
        if (startDateString.length() >0){
            auctionListing.setDescription(input);
        }
        
        System.out.print("Enter Reserve Price (blank if no change)> ");
        auctionListing.setReservePrice(scanner.nextBigDecimal());
        scanner.next();

        auctionListingControllerRemote.updateAuctionListing(auctionListing);
        System.out.println("auctionListing updated successfully!\n");
    }
    
    private void doDeleteAuctionListing(AuctionListing auctionListing) throws AuctionListingNotFoundException
    {
        Scanner scanner = new Scanner(System.in);        
        String input;
        
        System.out.println("*** OAS Administration Panel :: Sales Operation :: View Auction Listing Details :: Delete Auction Listing ***\n");
        System.out.printf("Confirm Delete AuctionListing of start date time %s and end date time of %s (Auction Listing ID: %d) (Enter 'Y' to Delete)> ", auctionListing.getStartDateTime().toString(), auctionListing.getEndDateTime().toString(), auctionListing.getAuctionListingId());
        input = scanner.nextLine().trim();
        
        if(input.equals("Y"))
        {
            List<Bid> bidList = auctionListing.getBidList();
            if(bidList.isEmpty()){
                auctionListingControllerRemote.deleteAuctionListing(auctionListing.getAuctionListingId());
                System.out.println("Auction Listing deleted successfully!\n");
            }
            else{//auction listing is used and is marked as disabled
                auctionListing.setStatus(AuctionStatus.DISABLED);
                //refund the credits to the customer
                for (Bid bid: bidList){
                    bid.refundToCustomer();
                }
                auctionListingControllerRemote.updateAuctionListing(auctionListing);
                System.out.println("Auction Listing has been used hence it is closed but not deleted!\n");
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
        System.out.printf("%8s%20s%20s%15s%20s%20s\n", "AuctionListing ID", "Start Date Time", "End Date Time", "Status", "Description", "Reserve Price", "Bid List", "Winning Bid");

        for(AuctionListing auctionListing:allAuctionListings)
        {
            System.out.printf("%8s%20s%20s%15s%20s%20s\n", 
                    auctionListing.getAuctionListingId().toString(), auctionListing.getStartingBidAmount().toString(), auctionListing.getStartDateTime().toString(),auctionListing.getEndDateTime().toString(), 
                    auctionListing.getStatus().toString(), auctionListing.getDescription(), auctionListing.getReservePrice().toString(), auctionListing.getBidList().toArray().toString(),auctionListing.getWinningBid().toString());
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
    private void doViewAllAuctionListingsRequiringManualIntervention()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** OAS Administration Panel :: System Administration :: View All AuctionListings with Bids but Below Reserve Price ***\n");
        
        List<AuctionListing> allAuctionListings = auctionListingControllerRemote.retrieveAllAuctionListingsRequiringManualIntervention();
        System.out.printf("%8s%20s%20s%15s%20s%20s\n", "AuctionListing ID", "Start Date Time", "End Date Time", "Status", "Description", "Reserve Price", "Bid List", "Winning Bid");

        for(AuctionListing auctionListing:allAuctionListings)
        {
            System.out.printf("%8s%20s%20s%15s%20s%20s\n", 
                    auctionListing.getAuctionListingId().toString(), auctionListing.getStartDateTime().toString(),auctionListing.getEndDateTime().toString(), auctionListing.getStatus().toString(), auctionListing.getDescription(), auctionListing.getDescription().toString(), auctionListing.getBidList().toArray().toString(),auctionListing.getWinningBid().toString());
        }
        
        System.out.println("------------------------");
            System.out.println("1: Assign Winning Bid for Listing with Bids but Below Reserve Price");
            System.out.println("2: Back\n");
            System.out.print("> ");
            int response = scanner.nextInt();
            
            if(response == 1)
            {
                doManuallyAssignWinningBids(allAuctionListings);
            }
            
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
    
    public void doManuallyAssignWinningBids(List<AuctionListing> allAuctionListings){
        Scanner scanner = new Scanner(System.in);
        for(AuctionListing auctionListing:allAuctionListings)
        {
            BigDecimal winningBidValue = auctionListing.getWinningBidValue();
            BigDecimal reservePrice = auctionListing.getReservePrice();
            //require manual intervention for case where highest bid is same or below reserve price
            if (winningBidValue.compareTo(reservePrice)<=0)
            {
                System.out.print("Reserve Price of Auction Listing ID "+ auctionListing.getAuctionListingId().toString());
                System.out.println(" has a reserve price of "+reservePrice.toString());
                System.out.print(". Do you want to assign Bid with the credit value of "+reservePrice.toString()+" ?");
                System.out.println("Enter 'Y' to mark it as winning bid and 'N' to demark it");
                String input = scanner.nextLine().trim();
        
                if(input.equals("Y")){
                    
                }
                else if(input.equals("N")){
                    //decide not to mark the highest bid as winning bid hence no winner
                    //final and cannot be changed
                    auctionListing.setWinningBidManually(null);
                    auctionListing.setWinningBidValueManually(null);
                }
                
                auctionListingControllerRemote.updateAuctionListing(auctionListing);
                System.out.println("auctionListing winning bid assigned successfully!\n");
            }
        }
    }
}