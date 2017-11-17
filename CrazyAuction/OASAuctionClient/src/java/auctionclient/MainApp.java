/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auctionclient;

import ejb.session.stateless.AddressControllerRemote;
import ejb.session.stateless.AuctionListingControllerRemote;
import ejb.session.stateless.BidControllerRemote;
import ejb.session.stateless.CreditPackageControllerRemote;
import ejb.session.stateless.CreditTransactionControllerRemote;
import ejb.session.stateless.CustomerControllerRemote;
import entity.Address;
import entity.AuctionListing;
import entity.Bid;
import entity.CreditPackage;
import entity.CreditTransaction;
import entity.Customer;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import util.enumeration.TransactionTypeEnum;
import util.exception.CreditPackageNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialException;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.enumeration.AuctionStatus;
import util.exception.AuctionListingNotFoundException;
import util.exception.CustomerInsufficientCreditBalance;

/**
 *
 * @author alex_zy
 */
class MainApp {

    private CustomerControllerRemote customerController;
    private CreditTransactionControllerRemote creditTransactionController;
    private BidControllerRemote bidController;
    private AddressControllerRemote addressController;
    private CreditPackageControllerRemote creditPackageController;
    private AuctionListingControllerRemote auctionListingController;

    private static DateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
    private Customer currentCustomer;

    public MainApp() {
    }

    public MainApp(CustomerControllerRemote customerController, CreditTransactionControllerRemote creditTransactionController, BidControllerRemote bidController, AddressControllerRemote addressController, CreditPackageControllerRemote creditPackageController, AuctionListingControllerRemote auctionListingController) {
        this.customerController = customerController;
        this.creditTransactionController = creditTransactionController;
        this.bidController = bidController;
        this.addressController = addressController;
        this.currentCustomer = currentCustomer;
        this.creditPackageController = creditPackageController;
        this.auctionListingController = auctionListingController;
    }

    public void runApp() {

        Scanner sc = new Scanner(System.in);

        while (true) {
            Integer response = 0;
            System.out.println("\n*** Auction Client :: Welcome ***\n");
            System.out.println("1: Returning User");
            System.out.println("2: New User");
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
                case 1: {
                    try {
                        DoLogin(sc);
                    } catch (InvalidLoginCredentialException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
                case 2:
                    doRegisterNewUser();
                case 3:
                    break;
            }
            if (response == 3) {
                break;
            }
        }
    }

    private void doRegisterNewUser() {
        Scanner sc = new Scanner(System.in);
        Customer customer = new Customer();
        System.out.println("\n*** Auction Client :: Register New User***\n");
        customer.setFirstName(doReadFirstName());
        System.out.println();
        customer.setLastName(doReadLastName());
        System.out.println();
        customer.setIdentificationNumber(doReadNric());
        System.out.println();
        customer.setAddresses(doReadAddresses());
        System.out.println();
        customer.setPassword(doReadPassword());
        System.out.println();
        customer.setUsername(doReadUsername());
        System.out.println();
        customerController.createNewCustomer(customer);
        System.out.printf("You have registered successfully, %s %s.%nPlease use %s to login",
                customer.getFirstName(), customer.getLastName(), customer.getUsername());

    }

    private Address doReadAddress(int i, int numberOfAddress, Customer newCustomer) {
        Scanner sc = new Scanner(System.in);
        System.out.println("enter the 1st line of your address (" + (++i) + " of " + numberOfAddress + "):");
        System.out.print("> ");
        String lineOne = sc.nextLine().trim();
        while (lineOne.length() > 32) {
            System.err.println("\nenter the 1st line of your address (" + (++i) + " of " + numberOfAddress + ", maximum 32 characters):");
            System.out.print("> ");
            lineOne = sc.nextLine().trim();
        }
        System.out.println("enter the 2nd line of your address (" + (++i) + " of " + numberOfAddress + "):");
        System.out.print("> ");
        String lineTwo = sc.nextLine().trim();
        while (lineTwo.length() > 32) {
            System.out.print("> ");
            System.err.println("\nenter the 2nd line of your address (" + (++i) + " of " + numberOfAddress + ", maximum 32 characters):");
            lineOne = sc.nextLine().trim();
        }
        return (new Address(lineOne, lineOne, newCustomer));
    }

    private String doReadFirstName() {
        Scanner sc = new Scanner(System.in);
        System.out.print("please enter your first name: \n> ");
        String firstName = sc.next();
        sc.nextLine();
        while (firstName.length() > 32) {
            System.err.println("\nplease enter your first name(maximum 32 characters): ");
            System.out.print("> ");
            firstName = sc.next();
            sc.nextLine();
        }
        return firstName;
    }

    private String doReadLastName() {
        Scanner sc = new Scanner(System.in);
        System.out.print("please enter your last name: \n> ");
        String lastName = sc.next();
        sc.nextLine();
        while (lastName.length() > 32) {
            System.err.println("\nplease enter your last name(maximum 32 characters): ");
            System.out.print("> ");
            lastName = sc.next();
            sc.nextLine();
        }
        return lastName;
    }

    private String doReadNric() {
        Scanner sc = new Scanner(System.in);
        System.out.println("please enter your NRIC or passport number. This cannot be changed after you create your profile");
        System.out.print("> ");

        String identificationNumber = sc.next();
        sc.nextLine();
        while (identificationNumber.length() != 9) {
            System.err.println("\nplease enter your NRIC or passport Number(9 characters, no blackspace allowed): ");
            System.out.print("> ");
            identificationNumber = sc.next();
            sc.nextLine();
        }
        return identificationNumber;
    }

    private List<Address> doReadAddresses() {
        Scanner sc = new Scanner(System.in);
        System.out.println("You are going to provide your address for shipping purposes. Do you wish to enter more than 1 address? (Y/N)");
        System.out.print("> ");
        String response = sc.nextLine().trim();
        int numberOfAddress;
        List<Address> addresses = new ArrayList<Address>();
        while (true) {
            if (response.equalsIgnoreCase("y")) {
                numberOfAddress = 0;
                try {
                    System.out.println("Enter number of addresses");
                    System.out.print("> ");
                    try {
                        numberOfAddress = Integer.valueOf(sc.nextLine().trim());
                    } catch (Exception ex) {
//                        ex.printStackTrace();
                    }
                    while (numberOfAddress <= 0) {
                        System.err.println("\nInvalid input. Please try again");
                        System.out.print("> ");
                        try {
                            numberOfAddress = Integer.valueOf(sc.nextLine().trim());
                        } catch (Exception ex) {
//                            ex.printStackTrace();
                        }
                    }
                } catch (Exception ex) {
                }
                if (numberOfAddress > 0) {
                    break;
                }
            } else if (response.equalsIgnoreCase("n")) {
                numberOfAddress = 1;
                break;
            } else {
                System.err.println("\nInvalid input. Please try again");
                System.out.print("> ");
                response = sc.next();
                sc.nextLine();
            }
        }
        for (int i = 0; i < numberOfAddress; i++) {
            Address address = doReadAddress(i, numberOfAddress, null);
        }

        return addresses;
    }

    private String doReadPassword() {
        Scanner sc = new Scanner(System.in);
        String password1 = "password1";
        String password2 = "password2";
        boolean firstAttempt = true;
        while (!password1.equals(password2)) {
            if (!firstAttempt) {
                System.err.println("Make sure you have entered the same password twice./n");
            }
            System.out.println("Please enter your password");
            System.out.print("> ");
            password1 = sc.nextLine().trim();
            if (password1.indexOf(' ') != -1) {
                System.err.println("password cannot contain blankspace/n");
                continue;
            }
            System.out.println("Please enter your password again");
            System.out.print("> ");
            password2 = sc.nextLine().trim();
            firstAttempt = false;
        }
        return password1;
    }

    private String doReadUsername() {
        Scanner sc = new Scanner(System.in);

        System.out.print("You can use your email or phone nunber as your username. "
                + "It cannot be changed.\n");

        String username1 = "login1";
        String username2 = "login2";
        boolean firstAttempt = true;
        while (!username1.equals(username2)) {
            if (!firstAttempt) {
                System.err.println("Make sure you have entered the same email or phone number/n");
            }
            System.out.println("Please enter your email or phone number:");
            System.out.print("> ");
            username1 = sc.next();
            sc.nextLine();
            if (username1.indexOf(' ') != -1) {
                System.err.println("email or phone number cannot contain blankspace/n");
                continue;
            }
            System.out.println("Please enter your email or phone number again:");
            System.out.print("> ");
            username2 = sc.next();
            sc.nextLine();
            firstAttempt = false;
        }
        return username1;
    }

    private void viewAllAddresses(Boolean delete) {
        Scanner sc = new Scanner(System.in);
        if (Thread.currentThread().getStackTrace()[1].getMethodName().equals("menuAddress")) {
            System.out.println("\n*** Auction Client :: View All Addresses ***\n");
        }

        List<Address> addresses = currentCustomer.getAddresses();
        int AddressIndex = 0;
        for (Address address : addresses) {
            System.out.printf("%4d%35s%n%4s%35s%n", ++AddressIndex, address.getLineOne(), "", address.getLineTwo());
        }
        if (!Thread.currentThread().getStackTrace()[1].getMethodName().equals("menuAddress")) {
            return;
        }
        if (!delete) {
            System.out.print("Press any key to continue...> ");
            sc.nextLine();
            return;
        }
        System.out.println("Please enter the address index number that you would like to delete:");
        int indexOfAddressToDelete = 0;
        boolean firstAttempt = true;
        while (indexOfAddressToDelete < 0 || indexOfAddressToDelete >= currentCustomer.getAddresses().size()) {
            if (!firstAttempt) {
                System.err.print("Invalid address index number. Please try again");
            }
            System.out.print("> ");
            indexOfAddressToDelete = sc.nextInt() - 1; //adjust for 0-based indice
            sc.nextLine();
        }
        currentCustomer.getAddresses().remove(indexOfAddressToDelete);
        customerController.updateCustomer(currentCustomer);
        System.out.println("The address has been successfully deleted");
        System.out.print("Press any key to continue...> ");
        sc.nextLine();
    }

    private void DoLogin(Scanner sc) throws InvalidLoginCredentialException {
        Customer customer = null;
        System.out.println("\n*** Auction Client :: Login***\n");
        String username = doReadToken(sc, "username");
        try {
            customer = customerController.retrieveCustomerByUsername(username);
        } catch (CustomerNotFoundException ex) {
            System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
            throw new InvalidLoginCredentialException();
        }
        String password = doReadToken(sc, "password");
        if (customer.getPassword().equals(password)) {
            currentCustomer = customer;
            menuMain(sc);
        } else {
            throw new InvalidLoginCredentialException("Invalid login credential!");
        }
    }

    private String doReadToken(Scanner sc, String tokenName) {
        System.out.printf("Please enter your %s%n", tokenName);
        System.out.print("> ");
        String token = sc.next();
        sc.nextLine();
        return token;
    }

    private void menuMain(Scanner sc) {
        Integer response = 0;

        while (true) {
            System.out.println("\n*** Auction C lient :: Menu Main***\n");
            System.out.printf("You are login as %s\n", currentCustomer.getUsername());
            System.out.println("1: manage my profile");
            System.out.println("2: manage addresses");
            System.out.println("3: auction & bid");
            System.out.println("4: manage my credits");
            System.out.println("5: Logout\n");
            response = 0;

            while (response < 1 || response > 5) {
                System.out.print("> ");

                response = sc.nextInt();

                if (response == 1) {
                    menuProfile();
                } else if (response == 2) {
                    menuAddress();
                } else if (response == 3) {
                    try {
                        menusAuctionAndBid();
                    } catch (CustomerInsufficientCreditBalance ex) {

                    }
                } else if (response == 4) {
                    menuCredit();
                } else if (response == 5) {
                    doLogout();
                    return;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 5) {
                break;
            }
        }
    }

    public void menuProfile() {
        Integer response = 0;
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n*** Auction Client :: Profile Menu***\n");
            System.out.printf("You are login as %s\n", currentCustomer.getUsername());
            System.out.println("1: view my profile");
            System.out.println("2: update profile");
            System.out.println("3: Logout\n");
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");

                response = sc.nextInt();

                if (response == 1) {
                    doViewProfile();
                } else if (response == 2) {
                    doUpdateProfile();
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 3) {
                break;
            }
        }
    }

    private void doViewProfile() {
        Scanner scanner = new Scanner(System.in);
        if (Thread.currentThread().getStackTrace()[1].getMethodName().equals("doUpdateProfile")) {
            System.out.println("\n*** Auction Client :: Profile Menu:: View Profile***\n");
        }
        System.out.printf("first name: %s%n", currentCustomer.getFirstName());
        System.out.printf("last name: %s%n", currentCustomer.getLastName());
        System.out.printf("identification number: %s%n", currentCustomer.getIdentificationNumber());
        System.out.printf("username: %s%n", currentCustomer.getUsername());
        System.out.printf("credit balance: %s%n", currentCustomer.getCreditBalance());
        System.out.printf("password: %s%n", currentCustomer.getPassword());
        if (Thread.currentThread().getStackTrace()[1].getMethodName().equals("doUpdateProfile")) {
            System.out.printf("enter any key to continue...%n");
            System.out.print("> ");
            scanner.nextLine();
        }

    }

    private void doUpdateProfile() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n*** Auction Client :: Update Profile ***\n");
        while (true) {
            doViewProfile();
            System.out.println("1. change name");
            System.out.println("2. change password");
            System.out.println("3. exit");
            int response = 0;
            while (response < 1 || response > 3) {
                System.out.print("> ");
                response = sc.nextInt();
                sc.nextLine();
                if (response == 1) {
                    String firtname = doReadFirstName();
                    String lastName = doReadLastName();
                    currentCustomer.setFirstName(firtname);
                    currentCustomer.setLastName(lastName);
                    customerController.updateCustomer(currentCustomer);
                } else if (response == 2) {
                    List<Address> addresses = doReadAddresses();
                    currentCustomer.setAddresses(addresses);
                    customerController.updateCustomer(currentCustomer);
                } else if (response == 3) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 3) {
                break;
            }
        }
    }

    private void menuAddress() {
        Integer response = 0;
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n*** Auction Client :: Address Menu***\n");
            System.out.printf("You are login as %s\n", currentCustomer.getUsername());
            System.out.println("1. view all address(es)");
            System.out.println("2. add address(es)");
            System.out.println("3. disable an address");
            System.out.println("4: Logout\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");
                response = sc.nextInt();
                if (response == 1) {
                    Boolean delete = false;
                    viewAllAddresses((delete));
                } else if (response == 2) {
                    List<Address> addresses = doReadAddresses();
                    currentCustomer.setAddresses(addresses);
                    customerController.updateCustomer(currentCustomer);
                } else if (response == 3) {
                    Boolean delete = true;
                    viewAllAddresses(delete);
                } else if (response == 4) {
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

    private void menuCredit() {
        Integer response = 0;
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n*** Auction Client :: Credit Menu***\n");
            System.out.printf("You are login as %s\n", currentCustomer.getUsername());
            System.out.println("1. purchase new credit package");
            System.out.println("2. add address(es)");
            System.out.println("3. disable an address");
            System.out.println("4: Logout\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");
                response = sc.nextInt();
                if (response == 1) {
                    doPurchaseCreditPackage();
                } else if (response == 2) {
                    List<Address> addresses = doReadAddresses();
                    currentCustomer.setAddresses(addresses);
                    customerController.updateCustomer(currentCustomer);
                } else if (response == 3) {
                    Boolean delete = true;
                    viewAllAddresses(delete);
                } else if (response == 4) {
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

    private void doPurchaseCreditPackage() {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n*** Auction Client :: Credit Menu :: Purchase ***\n");
        List<CreditPackage> allCreditPackages = creditPackageController.retrieveAllCreditPackages();
        System.out.printf("%8s%20s%15s\n", "CreditPackage ID", "Price", "Available Credit");
        for (CreditPackage cp : allCreditPackages) {
            //customer can only purchase the credit package if no one has bought it befoure 
            if (cp.getTransactions() == null && cp.getEnabled()) {
                System.out.printf("%8s%20s%15s\n", cp.getCreditPackageId(), cp.getPrice(), cp.getCredit());
            }
        }
        System.out.println("***************************************");
        System.out.println("Enter the CreditPackage ID to purchase (enter an unlisted id to quit purchase)");
        System.out.print("> ");
        Long creditPackageId = sc.nextLong();
        CreditPackage creditPackage;
        try {
            creditPackage = creditPackageController.retrieveCreditPackageByCreditPackageId(creditPackageId);
        } catch (CreditPackageNotFoundException ex) {
            return;
        }
        System.out.println("Enter the number of units");
        System.out.print("> ");
        int unit = sc.nextInt();
        //make transaction
        CreditTransaction creditTransaction = new CreditTransaction(new Date(), currentCustomer, creditPackage, unit, TransactionTypeEnum.CREDIT, null); //add transaction to customer and credit package 
        //add to credit package
        creditPackage.getTransactions().add(creditTransaction);
        creditPackageController.updateCreditPackage(creditPackage);
        //add to customer
        currentCustomer.getCreditTransactionHistory().add(creditTransaction);
        currentCustomer.setCreditBalance(currentCustomer.getCreditBalance().add(creditPackage.getCredit()));
        customerController.updateCustomer(currentCustomer);
        System.out.println("Thank you for purchasing the credit package(s)");
    }

    //view or place bid
    private void menusAuctionAndBid() throws CustomerInsufficientCreditBalance {
        Integer response = 0;
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n*** Auction Client :: Auction&Bid Menu***\n");
            System.out.printf("You are login as %s\n", currentCustomer.getUsername());
            System.out.println("1. view all auction listings");//browse -> for details
            System.out.println("2. place new bid");//browser -> for place bidding
            System.out.println("4. Browse won auction listing(s)");//browser -> for place bidding
            System.out.println("4: exit\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");
                response = sc.nextInt();
                if (response == 1) {
                    viewAllAuctionListings();
                } else if (response == 2) {
                    placeNewBid();
                } else if (response == 3) {
                    Boolean delete = true;
                    viewAllAddresses(delete);
                } else if (response == 4) {
                    viewCustomerWonAuctionListing();
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

//display a grossry for all active listings. prompt to view details or refresh
    private void viewAllAuctionListings() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n*** Auction Client :: Auction&Bid Menu :: View Auction Listing ***\n");

        viewActiveAuctionListingSimple(); //only display active ones
        AuctionListing al = null;
        System.out.println("enter the auction listing ID to display details, or R to refresh.");
        while (al == null) {
            String token = sc.nextLine().trim();
            if (token.equalsIgnoreCase("r")) {
                viewAllAuctionListings();
            } else {
                try {
                    Long alId = Long.valueOf(token);
                    al = auctionListingController.retrieveAuctionListingByAuctionListingId(alId);
                    viewAnAuctionListing(al);
                } catch (AuctionListingNotFoundException ex) {
                    System.out.println("An error has occurred while retrieving auctionListing: " + ex.getMessage() + "\n");
                }
            }
        }

    }

    private void viewAnAuctionListing(AuctionListing al) {

        System.out.println("Auction Listing ID: " + al.getAuctionListingId());
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

    //display all active listing as a grossry
    private void viewActiveAuctionListingSimple() {
        List<AuctionListing> allAuctionListings = auctionListingController.retrieveAllAuctionListings();
        System.out.printf("%8s%20s%20s%35s%20s%20s\n", "AuctionListing ID", "Start Date Time", "End Date Time", "Description", "Reserve Price", "Current Bid");

        for (AuctionListing auctionListing : allAuctionListings) {
            if (auctionListing.getStatus() == AuctionStatus.OPENED) {
                List<Bid> bids = auctionListing.getBidList();
                BigDecimal highestBid = new BigDecimal(0);
                for (Bid b : bids) {
                    if (b.getCreditValue().compareTo(highestBid) == 1) {
                        highestBid = b.getCreditValue();
                    }
                }
                DecimalFormat df = new DecimalFormat("0.00");
                System.out.printf("%8s%20s%20s%35s%20s%20s\n",
                        auctionListing.getAuctionListingId().toString(), auctionListing.getStartingBidAmount().toString(), auctionListing.getStartDateTime().toString(), auctionListing.getEndDateTime().toString(),
                        auctionListing.getDescription(), auctionListing.getReservePrice().toString(), df.format(highestBid.floatValue()));
            }
        }
        System.out.println();
    }

    //display a grossry for all active listings. prompt the user to refresh or bid on one listing
    private void placeNewBid() throws CustomerInsufficientCreditBalance {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n*** Auction Client :: Auction&Bid Menu :: View Auction Listing ***\n");

        viewActiveAuctionListingSimple(); //only display active ones
        AuctionListing al = null;
        System.out.println("enter the auction listing ID to display details, or R to refresh.");
        while (al == null) {
            String token = sc.nextLine().trim();
            if (token.equalsIgnoreCase("r")) {
                viewAllAuctionListings();
            } else {
                try {
                    Long alId = Long.valueOf(token);
                    al = auctionListingController.retrieveAuctionListingByAuctionListingId(alId);
                    doPlaceBid(al);
                } catch (AuctionListingNotFoundException ex) {
                    System.out.println("An error has occurred while retrieving auctionListing: " + ex.getMessage() + "\n");
                }
            }
        }
    }

    private void doPlaceBid(AuctionListing al) throws CustomerInsufficientCreditBalance {
        Scanner scanner = new Scanner(System.in);
        //display reserved price & current highest bidding
        System.out.println("Auction Listing ID: " + al.getAuctionListingId());
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
        double minIncrement = getSmallestIncrementWithCurrentBid(highestBid);
        BigDecimal minNewBid = highestBid.add(new BigDecimal(minIncrement));
        System.out.println("You need to bid as least" + df.format(minNewBid) + " credit(s).");
        //to enter new bid amount
        System.out.println("enter your bid with maximum 2 decimal place: ");

        BigDecimal bidAmount = new BigDecimal(0);
        while (bidAmount.compareTo(new BigDecimal(.05)) < 0) {
            System.out.print("> ");
            bidAmount = new BigDecimal(scanner.nextDouble());
            //validate for min new bid . promote for reenter 
            if (bidAmount.compareTo(minNewBid) < 0) {
                bidAmount = new BigDecimal(0);
                System.out.println("Your bid must exceed" + df.format(minNewBid));
                continue;
            }
            //validate for enough bal. throw exception
            if (bidAmount.compareTo(currentCustomer.getCreditBalance()) < 0) {
                throw new CustomerInsufficientCreditBalance("Please ensure you have enough credit balance");
            }
        }
        Boolean delete = false;
        viewAllAddresses(delete);

        System.out.println("Please enter the address index number for shipping purposes");
        int indexOfAddressToShip = 0;
        boolean firstAttempt = true;
        while (indexOfAddressToShip < 0 || indexOfAddressToShip >= currentCustomer.getAddresses().size()) {
            if (!firstAttempt) {
                System.err.print("Invalid address index number. Please try again");
            }
            System.out.print("> ");
            indexOfAddressToShip = scanner.nextInt() - 1; //adjust for 0-based indice
            scanner.nextLine();
        }
        Address address = currentCustomer.getAddresses().get(indexOfAddressToShip);
        Bid newBid = new Bid(bidAmount, address);
        //record credit transaction
        CreditTransaction ct = new CreditTransaction();
        ct.setTransactionDateTime(new Date());
        ct.setPurchasingCustomer(currentCustomer);
        ct.setType(TransactionTypeEnum.DEBIT);
        ct.setBid(newBid);
        creditTransactionController.createNewCreditTransaction(ct);

        //associate transaction to bid
        newBid.setCreditTransaction(ct);
        bidController.createNewBid(newBid);
        //associate transaction to customer
        currentCustomer.getCreditTransactionHistory().add(ct);
        customerController.updateCustomer(currentCustomer);

        System.out.println("you have successfully bid fot the item!");
    }

    //validate input (smallest amount placeable, smallest increment, must be higher than current highest bid)
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

    private void doLogout() {
        currentCustomer = null;
        System.out.println("You have successfully logged out\n");
    }

    private void viewCustomerWonAuctionListing() {
        Scanner sc = new Scanner(System.in);
        List<Bid> wonBids = currentCustomer.getWonBids();
        System.out.println("\n*** Auction Client :: Auction&Bid Menu :: View Won Auction Listing ***\n");
        System.out.printf("%8s%20s%20s%15s%20s%20s\n", "AuctionListing ID", "Start Date Time", "End Date Time", "Status", "Description", "Reserve Price", "Bid List", "Winning Bid");
        for (Bid b : wonBids) {
            AuctionListing auctionListing = b.getAuctionListing();
            System.out.printf("%8s%20s%20s%15s%20s%20s\n",
                    auctionListing.getAuctionListingId().toString(), auctionListing.getStartingBidAmount().toString(), auctionListing.getStartDateTime().toString(), auctionListing.getEndDateTime().toString(),
                    auctionListing.getStatus().toString(), auctionListing.getDescription(), auctionListing.getReservePrice().toString(), auctionListing.getBidList().toArray().toString(), auctionListing.getWinningBid().toString());
        }
        System.out.print("Press any key to continue...> ");
        sc.nextLine();
        return;
    }

}
