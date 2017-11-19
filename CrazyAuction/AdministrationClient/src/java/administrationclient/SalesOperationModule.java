/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package administrationclient;

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
import java.util.Locale;
import java.util.Scanner;
import util.enumeration.AccessRightEnum;
import util.enumeration.AuctionStatus;
import util.exception.AuctionListingNotFoundException;
import util.exception.InvalidAccessRightException;
import ejb.session.stateless.AuctionListingControllerRemote;
import java.text.DecimalFormat;

/**
 *
 * @author Summer
 */
public class SalesOperationModule {

    private EmployeeControllerRemote employeeControllerRemote;
    private AuctionListingControllerRemote auctionListingControllerRemote;
    private Employee currEmployee;
    private static DateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");

    public SalesOperationModule() {
    }

    public SalesOperationModule(EmployeeControllerRemote employeeControllerRemote, AuctionListingControllerRemote auctionListingControllerRemote, Employee currentEmployee) {
        this.employeeControllerRemote = employeeControllerRemote;
        this.auctionListingControllerRemote = auctionListingControllerRemote;
        this.currEmployee = currentEmployee;
    }

    public void menuSalesOperation() throws InvalidAccessRightException, ParseException {
        if (currEmployee.getAccessRightEnum() != AccessRightEnum.SALES) {
            throw new InvalidAccessRightException("You don't have SALES Employee rights to access the sales operation module.");
        }

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        while (true) {
            System.out.println("*** OAS Administration Panel :: Sales Operation ***\n");
            System.out.println("1: Create New Auction Listing");
            System.out.println("2: View Auction Listing Details");
            System.out.println("3: View All Auction Listings");
            System.out.println("4: View All Auction Listings with Bids but Below Reserve Price");
            System.out.println("5: Back\n");
            response = 0;

            while (response < 1 || response > 5) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doCreateNewAuctionListing();
                } else if (response == 2) {
                    doViewAuctionListingDetails();
                } else if (response == 3) {
                    doViewAllAuctionListings();
                } else if (response == 4) {
                    doViewAllAuctionListingsRequiringManualIntervention();
                } else if (response == 5) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 4) {
                break;
            }
        }
    }

    private void doCreateNewAuctionListing() throws ParseException {
        Scanner scanner = new Scanner(System.in);
        AuctionListing newAuctionListing = new AuctionListing();

        System.out.println("*** OAS Administration Panel :: Sales Operation :: Create New Auction Listing ***\n");
        System.out.print("Enter Starting Bid Amount ");
        BigDecimal bidAmount = null;
        while (bidAmount == null) {
            try {
                System.out.print("> ");
                newAuctionListing.setStartingBidAmount(BigDecimal.valueOf(scanner.nextDouble()));
                bidAmount = newAuctionListing.getStartingBidAmount();
            } catch (Exception ex) {
            }
        }
        DateFormat format = new SimpleDateFormat("yyyy.MM.dd.HH.mm");
        System.out.println("It is now " + format.format(new Date()));
        System.out.println("Enter Start Date in yyyy.MM.dd.HH.mm format ");
        Date date = null;
        while (date == null) {
            System.out.print("> ");
            String line = scanner.nextLine();
            try {
                date = format.parse(line);
            } catch (ParseException e) {
                System.out.println("Sorry, that's not valid. Please try again.");
            }
        }
        Date startDate = date;
        newAuctionListing.setStartDateTime(startDate);
        System.out.println("Enter End Date in yyyy.MM.dd.HH format ");
        date = null;
        while (date == null) {
            System.out.print("> ");
            String line = scanner.nextLine();
            try {
                date = format.parse(line);
            } catch (ParseException e) {
                System.out.println("Sorry, that's not valid. Please try again.");
            }
        }
        Date endDate = date;
        newAuctionListing.setEndDateTime(endDate);
        //by default set it as closed upon creation
        newAuctionListing.setStatus(AuctionStatus.CLOSED);
        System.out.print("Enter Description> ");
        newAuctionListing.setDescription(scanner.nextLine().trim());
        System.out.print("Enter Reserve Price (0 if no reserve price) ");
        BigDecimal reservePrice = null;
        while (reservePrice == null) {
            try {
                System.out.print("> ");
                newAuctionListing.setReservePrice(BigDecimal.valueOf(scanner.nextDouble()));
                reservePrice = newAuctionListing.getReservePrice();
            } catch (Exception ex) {
            }
        }
        //System.out.println("debug");
        
        Long newAuctionListingId = auctionListingControllerRemote.createAuctionListing(newAuctionListing);
        System.out.println("New auctionListingcreated successfully! ID: " + newAuctionListingId + "\n");
    }

    private void doViewAuctionListingDetails() throws ParseException {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        System.out.println("*** OAS Administration Panel :: Sales Operation :: View Auction Listing Details ***\n");
        System.out.print("Enter AuctionListing ID> ");
        Long auctionListingId = scanner.nextLong();
        
        try {
            AuctionListing al = auctionListingControllerRemote.retrieveAuctionListingByAuctionListingId(auctionListingId);
            DateFormat format = new SimpleDateFormat("yyyy.MM.dd.HH.mm");
            System.out.println("Auction Listing ID: " + al.getAuctionListingId());
            System.out.println("Starting Bid Amount: "+al.getStartingBidAmount().toString());
            System.out.println("Start Date: " + format.format(al.getStartDateTime()).toString());
            System.out.println("End Date: " + format.format(al.getEndDateTime()).toString());
            System.out.println("Status: " + al.getStatus().toString());
            System.out.println("Description: " + al.getDescription());
            System.out.println("Reserve Price: " + al.getReservePrice().toString());
            String winningBid = "NA";
            if (al.getWinningBidValue()!=null){
                winningBid = al.getWinningBidValue().toString();
            }
            System.out.println("Current Highest Bid: " + winningBid);
    
            System.out.println("------------------------");
            System.out.println("1: Update Auction Listing");
            System.out.println("2: Delete Auction Listing");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = scanner.nextInt();

            if (response == 1) {
                doUpdateAuctionListing(al);
            } else if (response == 2) {
                doDeleteAuctionListing(al);
            }
        } catch (AuctionListingNotFoundException ex) {
            System.out.println("An error has occurred while retrieving auctionListing: " + ex.getMessage() + "\n");
        }
    }

    private void doUpdateAuctionListing(AuctionListing auctionListing) throws ParseException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** OAS Administration Panel :: Sales Operation :: View Auction Listing Details :: Update Auction Listing ***\n");

        System.out.print("Enter Starting Bid Amount (blank if not change)> ");
        auctionListing.setStartingBidAmount(BigDecimal.valueOf(scanner.nextDouble()));
        scanner.next();

        System.out.print("Enter Start Date in yyyy.MM.dd.HH format (blank if no change)> ");
        String startDateString = scanner.nextLine().trim();
        if (startDateString.length() > 0) {
            Date startDate = formatter.parse(startDateString);
            auctionListing.setStartDateTime(startDate);
        }

        System.out.print("Enter End Date in yyyy.MM.dd.HH format (blank if no change)> ");
        String endDateString = scanner.nextLine().trim();
        if (endDateString.length() > 0) {
            Date endDate = formatter.parse(endDateString);
            auctionListing.setEndDateTime(endDate);
        }

        System.out.print("Enter Description (blank if no change)> ");
        String input = scanner.nextLine().trim();
        if (startDateString.length() > 0) {
            auctionListing.setDescription(input);
        }

        System.out.print("Enter Reserve Price (blank if no change)> ");
        auctionListing.setReservePrice(BigDecimal.valueOf(scanner.nextDouble()));
        scanner.next();

        auctionListingControllerRemote.updateAuctionListing(auctionListing);
        System.out.println("auctionListing updated successfully!\n");
    }

    private void doDeleteAuctionListing(AuctionListing auctionListing) throws AuctionListingNotFoundException {
        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("*** OAS Administration Panel :: Sales Operation :: View Auction Listing Details :: Delete Auction Listing ***\n");
        System.out.printf("Confirm Delete AuctionListing of ID: %d) (Enter 'Y' to Delete)> ", auctionListing.getAuctionListingId());
        input = scanner.nextLine().trim();

        if (input.equals("Y")) {
            List<Bid> bidList = auctionListing.getBidList();
            if (bidList.isEmpty()) {
                auctionListingControllerRemote.deleteAuctionListing(auctionListing.getAuctionListingId());
                System.out.println("Auction Listing deleted successfully!\n");
            } else {//auction listing is used and is marked as disabled
                auctionListing.setEnabled(Boolean.FALSE);
                //refund the credits to the customer
                for (Bid bid : bidList) {
                    bid.refundToCustomer();
                }
                auctionListingControllerRemote.updateAuctionListing(auctionListing);
                System.out.println("Auction Listing has been used hence it is closed but not deleted!\n");
            }
        } else {
            System.out.println("AuctionListing NOT deleted!\n");
        }
    }

    private void doViewAllAuctionListings() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** OAS Administration Panel :: System Administration :: View All AuctionListings ***\n");

        List<AuctionListing> allAuctionListings = auctionListingControllerRemote.retrieveAllAuctionListings();
        System.out.printf("%8s%20s%20s%10s%15s%15s\n", "ID", "Start Time", "End Time", "Status", "Reserve Price", "Winning Bid");
        String winningBid;
        DateFormat format = new SimpleDateFormat("yyyy.MM.dd.HH.mm");
        for (AuctionListing auctionListing : allAuctionListings) {
            winningBid = "NA";
            if (auctionListing.getWinningBidValue()!=null){
                winningBid = auctionListing.getWinningBidValue().toString();
            }
            System.out.printf("%8s%20s%20s%10s%15s%15s\n",
                    auctionListing.getAuctionListingId().toString(), format.format(auctionListing.getStartDateTime()).toString(), format.format(auctionListing.getEndDateTime()).toString(),
                    auctionListing.getStatus().toString(), auctionListing.getReservePrice().toString(), winningBid);
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doViewAllAuctionListingsRequiringManualIntervention() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** OAS Administration Panel :: System Administration :: View All AuctionListings with Bids but Below Reserve Price ***\n");

        List<AuctionListing> allAuctionListings = auctionListingControllerRemote.retrieveAllAuctionListingsRequiringManualIntervention();
        System.out.printf("%8s%20s%20s%10s%15s%15s\n", "ID", "Start Time", "End Time", "Status", "Reserve Price", "Winning Bid");
        String winningBid;
        DateFormat format = new SimpleDateFormat("yyyy.MM.dd.HH.mm");
        for (AuctionListing auctionListing : allAuctionListings) {
            winningBid = "NA";
            if (auctionListing.getWinningBidValue()!=null){
                winningBid = auctionListing.getWinningBidValue().toString();
            }
            System.out.printf("%8s%20s%20s%10s%15s%15s\n",
                    auctionListing.getAuctionListingId().toString(), format.format(auctionListing.getStartDateTime()).toString(), format.format(auctionListing.getEndDateTime()).toString(),
                    auctionListing.getStatus().toString(), auctionListing.getReservePrice().toString(), winningBid);
        }

        System.out.println("------------------------");
        System.out.println("1: Assign Winning Bid for Listing with Bids but Below Reserve Price");
        System.out.println("2: Back\n");
        System.out.print("> ");
        int response = scanner.nextInt();

        if (response == 1) {
            doManuallyAssignWinningBids(allAuctionListings);
        }

        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    public void doManuallyAssignWinningBids(List<AuctionListing> auctionListings) {
        Scanner scanner = new Scanner(System.in);
        for (AuctionListing auctionListing : auctionListings) {
            BigDecimal winningBidValue = auctionListing.getWinningBidValue();
            BigDecimal reservePrice = auctionListing.getReservePrice();
            //require manual intervention for case where highest bid is same or below reserve price
            System.out.print("Reserve Price of Auction Listing ID " + auctionListing.getAuctionListingId().toString());
            System.out.println(" has a reserve price of " + reservePrice.toString()+" .");
            System.out.print("Do you want to assign Bid with the credit value of " + winningBidValue.toString() + " ?");
            System.out.println("Enter 'Y' to mark it as winning bid and 'N' to demark it");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("Y")) {
                auctionListing.setWinningBidManually(auctionListing.getWinningBid());
                auctionListing.setWinningBidValueManually(auctionListing.getWinningBidValue());
            } else if (input.equalsIgnoreCase("N")) {
                //decide not to mark the highest bid as winning bid hence no winner
                //final and cannot be changed
                auctionListing.setWinningBidManually(null);
                auctionListing.setWinningBidValueManually(null);
            }//ALEX: DO INPUT VALIDATION WITH A LOOP HERE
            auctionListingControllerRemote.updateAuctionListing(auctionListing);
            System.out.println("auctionListing winning bid assigned successfully!\n");
        }
    }
}
