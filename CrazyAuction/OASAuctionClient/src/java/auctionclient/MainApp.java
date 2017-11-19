/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auctionclient;

import ejb.session.stateless.AddressControllerRemote;
import ejb.session.stateless.BidControllerRemote;
import ejb.session.stateless.CreditPackageControllerRemote;
import ejb.session.stateless.CreditTransactionControllerRemote;
import entity.Address;
import entity.AuctionListing;
import entity.Bid;
import entity.CreditPackage;
import entity.CreditTransaction;
import entity.Customer;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import util.enumeration.AuctionStatus;
import util.exception.AddressNotFoundException;
import util.exception.AuctionListingNotFoundException;
import util.exception.CustomerInsufficientCreditBalance;
import ejb.session.stateless.AuctionListingControllerRemote;
import ejb.session.stateless.CustomerControllerRemote;
import util.exception.AuctionListingNotFound;

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

    private Customer currentCustomer;

    public MainApp() {
    }

    public MainApp(CustomerControllerRemote customerController, CreditTransactionControllerRemote creditTransactionController, BidControllerRemote bidController, AddressControllerRemote addressController, CreditPackageControllerRemote creditPackageController, AuctionListingControllerRemote auctionListingController) {
        this.customerController = customerController;
        this.creditTransactionController = creditTransactionController;
        this.bidController = bidController;
        this.addressController = addressController;
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
                case 1:
                    try {
                        doLogin();
                        menuMain();
                    } catch (InvalidLoginCredentialException ex) {
                    }
                    break;
                case 2:
                    doRegisterNewUser();
                    break;
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
        System.out.printf("You have registered successfully, %s %s.\nPlease use %s to login",
                customer.getFirstName(), customer.getLastName(), customer.getUsername());

    }

    private Address doReadAddress(int curr, int numberOfAddress, Customer newCustomer) {
        Scanner sc = new Scanner(System.in);
        System.out.println("enter the 1st line of your address (" + (curr) + " of " + numberOfAddress + "):");
        System.out.print("> ");
        String lineOne = sc.nextLine().trim();
        while (lineOne.length() > 32) {
            System.err.println("enter the 1st line of your address (" + (curr) + " of " + numberOfAddress + ", maximum 32 characters):");
            System.out.print("> ");
            lineOne = sc.nextLine().trim();
        }
        System.out.println("enter the 2nd line of your address (" + (curr) + " of " + numberOfAddress + "):");
        System.out.print("> ");
        String lineTwo = sc.nextLine().trim();
        while (lineTwo.length() > 32) {
            System.out.print("> ");
            System.err.println("enter the 2nd line of your address (" + (curr) + " of " + numberOfAddress + "): ");
            lineOne = sc.nextLine().trim();
        }
        System.out.println("enter zipcode (" + (curr) + " of " + numberOfAddress + "):");
        System.out.print("> ");
        String zipcode = sc.nextLine().trim();
        while (zipcode.length() != 6) {
            System.out.print("> ");
            System.err.println("enter zipcode (" + (curr) + " of " + numberOfAddress + ", 6 digits):");
            zipcode = sc.nextLine().trim();
        }
        return (new Address(lineOne, lineTwo, zipcode, newCustomer));
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
            Address address = doReadAddress(i + 1, numberOfAddress, null);
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

    private void viewAllAddresses() {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n*** Auction Client :: View All Addresses ***\n");

        //display
        System.out.printf("%8s%15s\n", "ID", "Zipcode");
        List<Address> addresses = currentCustomer.getAddresses();

//        System.out.println("test");
//        System.out.println(addresses.size());
        for (Address address : addresses) {
            System.out.printf("%8d%15s\n", address.getAddressId(), address.getZipCode());
        }
        System.out.println("(end of list)");
        //sub menu
        Integer response = 0;
        while (true) {
            System.out.println("1: view address detail");
            System.out.println("2: back");
            response = 0;

            while (response < 1 || response > 2) {
                System.out.print("> ");
                response = sc.nextInt();
                if (response == 1) { //view details
                    Address address = null;
                    Long addressId = Long.valueOf(doReadToken("address ID"));
                    try {
                        address = addressController.retrieveAddressById(addressId);
                    } catch (AddressNotFoundException ex) {
//                        System.out.println("test1");
                        System.err.println("invalid address ID");
                    }
                    //verify if the address belongs to the current user
                    if (address.getCustomer().getCustomerId().equals(currentCustomer.getCustomerId())) {
                        viewAddressDetails(addressId);
                    } else {
//                        System.out.println("test2");
                        System.err.println("Invalid address ID");
                    }
                    return;
                } else if (response == 2) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 2) {
                break;
            }
//            sc.nextLine();
//            Address toDelete = null;
//            System.out.println("Please enter the address ID that you would like to delete:");
//            Long addressIdToDelete = null;
//            System.out.print("> ");
//            addressIdToDelete = Long.valueOf(sc.nextLine().trim()); //adjust for 0-based indice
//            try {
//                toDelete = addressController.retrieveAddressById(addressIdToDelete);
//            } catch (AddressNotFoundException ex) {
//                System.out.println("Invalid address ID.");
//                return; //assuming the customer no longer want to delete address
//            }
//            if (toDelete.getIsAssociatedWithWinningBid()) {
//                toDelete.setEnabled((false)); //one wining bid is associated with this address. peudo-delete
//            } else {
//                try {
//                    addressController.deleteAddress(addressIdToDelete); //no winning bid is associated with this address. delete
//                } catch (AddressNotFoundException ex) { //pre-checked. will not reach this statement
//                }
//            }
//            customerController.updateCustomer(currentCustomer);
//            System.out.println("The address has been successfully deleted");
//            System.out.print("Press any key to continue...> ");
//            sc.nextLine();
        }
    }

    private void doLogin() throws InvalidLoginCredentialException {
        Scanner sc = new Scanner(System.in);
        String username;
        String password = "";

        System.out.println("\n*** Auction Client :: Login***\n");
        username = doReadToken("username");
        password = doReadToken("password");

        if (username.length() > 0 && password.length() > 0) {
            try {
                currentCustomer = customerController.doLogin(username, password);
                System.out.println();
                System.out.println("Login successful!\n");
            } catch (InvalidLoginCredentialException ex) {
                System.out.println("");
                System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                throw new InvalidLoginCredentialException();
            }
        } else {
            throw new InvalidLoginCredentialException("Login credential was not provided!");
        }
    }

    private String doReadToken(String tokenName) {
        Scanner sc = new Scanner(System.in);
        System.out.printf("Enter your %s\n", tokenName);
        System.out.print("> ");
        String token = sc.next();
        sc.nextLine();
        return token;
    }

    private void menuMain() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        while (true) {
            System.out.println("\n*** Auction Client :: Menu Main***\n");
            System.out.printf("You are logged in as %s\n", currentCustomer.getUsername());
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
                        System.out.println(ex.getMessage());
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
            System.out.printf("You are logged in as %s\n", currentCustomer.getUsername());
            System.out.println("1: view my profile");
            System.out.println("2: update profile");
            System.out.println("3: exit to main menu\n");
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");

                response = sc.nextInt();

                if (response == 1) {
                    doViewProfile();
                } else if (response == 2) {
                    doUpdateProfile();
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

    private void doViewProfile() {
        Scanner scanner = new Scanner(System.in);
        if (Thread.currentThread().getStackTrace()[1].getMethodName().equals("doUpdateProfile")) {
            System.out.println("\n*** Auction Client :: Profile Menu:: View Profile***\n");
        }
        System.out.printf("first name: %s\n", currentCustomer.getFirstName());
        System.out.printf("last name: %s\n", currentCustomer.getLastName());
        System.out.printf("identification number: %s\n", currentCustomer.getIdentificationNumber());
        System.out.printf("username: %s\n", currentCustomer.getUsername());
        System.out.printf("password: %s\n", currentCustomer.getPassword());
        if (Thread.currentThread().getStackTrace()[2].getMethodName().equals("doUpdateProfile")) {
//            System.out.println("called by doupdateprofile");
        } else {
            System.out.printf("enter any key to continue...\n");
            System.out.print("> ");
            scanner.nextLine();
        }

    }

    private void doUpdateProfile() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n*** Auction Client :: Update Profile ***\n");
        while (true) {
            try {
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
                        String firstName = doReadFirstName();
                        String lastName = doReadLastName();
//                        customerController.changeCustomerName(currentCustomer.getCustomerId(),firstName, lastName);
                        currentCustomer.setFirstName(firstName);
                        currentCustomer.setLastName(lastName);
//                        customerController.updateCustomer(currentCustomer);
                        customerController.updateCustomer(currentCustomer);
                    } else if (response == 2) {
                        String password = doReadPassword();
                        currentCustomer.setPassword(password);
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void menuAddress() {
        Integer response = 0;
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n*** Auction Client :: Address Menu***\n");
            System.out.printf("You are logged in as %s\n", currentCustomer.getUsername());
            //Summer: sequence of use cases. add update Address.
            System.out.println("1. view all addresses");
            System.out.println("2. add address");
            System.out.println("3: back\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");
                response = sc.nextInt();
                if (response == 1) {
                    viewAllAddresses();
                } else if (response == 2) {
                    Address address = doReadAddress(1, 1, currentCustomer);
                    currentCustomer.getAddresses().add(addressController.createNewAddress(address));
                    customerController.updateCustomer(currentCustomer);
                    System.out.println("");
                    System.out.println("New address successfully added!");
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

    private void menuCredit() {
        Integer response = 0;
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n*** Auction Client :: Credit Menu***\n");
            System.out.printf("You are logged in as %s\n", currentCustomer.getUsername());
            System.out.println("1. view credit balance");
            System.out.println("2. purchase new credit package");
            System.out.println("3. view credit transaction history");
            System.out.println("4: back\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");
                response = sc.nextInt();
                sc.nextLine();
                if (response == 1) {
                    System.out.println();
                    System.out.printf("Credit Balance: %s\n", customerController.retrieveCustomerCreditBalance(currentCustomer.getCustomerId()).setScale(2).toPlainString());
                    System.out.println("***************************************");
                    System.out.print("Press any key to continue...> ");
                    sc.nextLine();
                } else if (response == 2) {
                    doPurchaseCreditPackage();
                } else if (response == 3) {
                    viewAllCreditTransations();
                } else if (response == 4) {
                    return;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 4) {
                break;
            }
        }
    }

    private void viewAllCreditTransations() {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n*** Auction Client :: Credit Menu :: View All Credit Transactions ***\n");
        System.out.printf("%8s%15s%30s\n", "ID", "Type", "Date");
        List<CreditTransaction> creditTransactions = currentCustomer.getCreditTransactionHistory();

//        System.out.println("test");
//        System.out.println(creditTransactions.size());
        for (CreditTransaction creditTransaction : creditTransactions) {
            System.out.printf("%8d%15s\n", creditTransaction.getCreditTransactionId(),
                    creditTransaction.getType().toString(), creditTransaction.getTransactionDateTime().toString());
        }
        System.out.println("(end of list)");
        System.out.print("Press any key to continue...> ");
        sc.nextLine();

    }

    private void doPurchaseCreditPackage() {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n*** Auction Client :: Credit Menu :: Purchase ***\n");
        List<CreditPackage> allCreditPackages = creditPackageController.retrieveAllCreditPackages();
        System.out.println("test 1: " + allCreditPackages.size());
        System.out.printf("%8s%20s%15s\n", "ID", "Price", "Available Credit");
        for (CreditPackage cp : allCreditPackages) {
            //Summer:customer can only purchase the credit package if no one has bought it befoure 
            if (cp.getEnabled()) {
                System.out.printf("%8s%20s%15s\n", cp.getCreditPackageId(), cp.getPrice(), cp.getCredit());
            }
        }
        System.out.println("***************************************");
        System.out.println("Enter the CreditPackage ID to purchase (enter an unlisted id to quit purchase)");
        System.out.print("> ");
        Long creditPackageId = sc.nextLong();
        sc.nextLine();
        CreditPackage creditPackage;
        try {
            creditPackage = creditPackageController.retrieveCreditPackageByCreditPackageId(creditPackageId);
        } catch (CreditPackageNotFoundException ex) {
            return;
        }
        System.out.println("Enter the number of units");
        System.out.print("> ");
        int unit = Integer.valueOf(sc.nextLine().trim());
        //make transaction
        CreditTransaction creditTransaction = new CreditTransaction(new Date(), currentCustomer, creditPackage, unit, TransactionTypeEnum.CREDIT, null); //add transaction to customer and credit package 
        //add to credit package
        creditTransactionController.createNewCreditTransaction(creditTransaction);
        creditPackage.getCreditTransactions().add(creditTransaction);
        creditPackageController.updateCreditPackage(creditPackage);
        //add to customer
        currentCustomer.getCreditTransactionHistory().add(creditTransaction);
        currentCustomer.setCreditBalance(currentCustomer.getCreditBalance().add(creditPackage.getCredit().multiply(new BigDecimal(unit))));
        customerController.updateCustomer(currentCustomer);
        System.out.println("Thank you for purchasing the credit package(s)");
    }

    //view or place bid
    private void menusAuctionAndBid() throws CustomerInsufficientCreditBalance {
        Integer response = 0;
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n*** Auction Client :: Auction&Bid Menu***\n");
            System.out.printf("You are logged in as %s\n", currentCustomer.getUsername());
            System.out.println("1. view all auction listings");//browse -> for details
            System.out.println("2: back\n");
            response = 0;
            //Summer: add "view acution listing" and only show place new bid and refresh auction listing in the view method instead of here
            while (response < 1 || response > 4) {
                System.out.print("> ");
                response = sc.nextInt();
                if (response == 1) {
                    viewAllAuctionListings();
                } else if (response == 2) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 2) {
                break;
            }
        }
    }

//display a glossary for all active listings. prompt to view details or refresh
    private void viewAllAuctionListings() throws CustomerInsufficientCreditBalance {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n*** Auction Client :: Auction&Bid Menu :: View Auction Listing ***\n");

        List<AuctionListing> allAuctionListings = auctionListingController.retrieveOpenedAuctions();
        System.out.printf("%8s%30s%20s\n", "ID", "End Date Time", "Current Bid");
        for (AuctionListing auctionListing : allAuctionListings) {
            BigDecimal highestBid = null;
            //assign non-null current highest bid 
            if (auctionListing.getBidList() != null && auctionListing.getBidList().size() != 0) {
                highestBid = new BigDecimal(0);
                List<Bid> bids = auctionListing.getBidList();
                for (Bid b : bids) {
                    if (b.getCreditValue().compareTo(highestBid) == 1) {
                        highestBid = b.getCreditValue();
                    }
                }
            }
            String highestBidString;
            if (highestBid != null) {
                highestBidString = highestBid.setScale(2).toString();
            } else {
                highestBidString = "N.A.";
            }
            System.out.printf("%8s%30s%20s\n",
                    auctionListing.getAuctionListingId().toString(), auctionListing.getEndDateTime().toString(), highestBidString);
        }
        //sub meu
        Integer response = 0;
        while (true) {
            System.out.println("1: view detail");
            System.out.println("2: refresh");
            System.out.println("3: place new bid");
            System.out.println("4: back");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");
                response = sc.nextInt();
                if (response == 1) { //view details
                    Long auctionListingId = null;
                    AuctionListing auctionListing = null;
                    while (auctionListingId == null) {

                        try {
                            auctionListingId = Long.valueOf(doReadToken("auction listing ID"));
                            try {
                                auctionListing = auctionListingController.retrieveAuctionListingByAuctionListingId(auctionListingId);
                            } catch (AuctionListingNotFoundException ex) {
                                System.err.println("invalid auction listing ID");
                            }
                        } catch (Exception ex) {
                            System.err.println("invalid input. Please try again");
                        }
                        //verify if the address belongs to the current user
                        if (auctionListing.getStatus() == AuctionStatus.OPENED) {
                            viewAnAuctionListing(auctionListingId);
                        } else {
                            System.err.println("Invalid address ID");
                        }
                        return;
                    }
                } else if (response == 2) {
                    viewAllAuctionListings();
                } else if (response == 3) {
                    AuctionListing auctionListing = null;
                    Long auctionListingId = Long.valueOf(doReadToken("auction listing ID"));
                    try {
                        auctionListing = auctionListingController.retrieveAuctionListingByAuctionListingId(auctionListingId);
                    } catch (AuctionListingNotFoundException ex) {
                        System.err.println("invalid auction listing ID");
                    }
                    //verify if the address belongs to the current user
                    if (auctionListing.getStatus() == AuctionStatus.OPENED) {
                        placeNewBid(auctionListingId);
                    } else {
                        System.err.println("Invalid address ID");
                    }
                    return;
                } else if (response == 4) {
                    return;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 2) {
                break;
            }
        }
    }

    private void viewAnAuctionListing(Long auctionListingId) {
        AuctionListing al;
        try {
            al = auctionListingController.retrieveAuctionListingByAuctionListingId(auctionListingId);
            Scanner scanner = new Scanner(System.in);
            System.out.println();
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
            System.out.print("Press any key to continue...> ");
            scanner.nextLine();
        } catch (AuctionListingNotFoundException ex) {
            //won't reach here
        }

    }

    private void placeNewBid(Long auctionListingId) throws CustomerInsufficientCreditBalance {
        AuctionListing al = null;
        try {
            al = auctionListingController.retrieveAuctionListingByAuctionListingId(auctionListingId);
            System.out.println();
            doPlaceBid(al);
            //may display current smallest increment here
        } catch (AuctionListingNotFoundException ex) {
            //won't reach here
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
        System.out.println("You need to bid as least " + df.format(minNewBid) + " credit(s).");
        //to enter new bid amount
        System.out.println("enter your bid with maximum 2 decimal place: ");

        BigDecimal bidAmount = new BigDecimal(0);
        while (bidAmount.compareTo(new BigDecimal(.05)) < 0) {
            System.out.print("> ");
            bidAmount = new BigDecimal(Double.valueOf(scanner.nextLine().trim()));
            //validate for min new bid . promote for reenter 
            if (bidAmount.compareTo(minNewBid) < 0) {
                bidAmount = new BigDecimal(0);
                System.out.println("Your bid must exceed" + df.format(minNewBid));
//                continue;

            }
            //validate for enough bal. throw exception
            if (bidAmount.compareTo(currentCustomer.getCreditBalance()) > 0) {

                throw new CustomerInsufficientCreditBalance("Please ensure you have enough credit balance");
            }
        }

        System.out.println("Please enter an address ID for shipping purposes");
        Address address = null;
        Long addressId = Long.valueOf(doReadToken("address ID"));
        try {
            address = addressController.retrieveAddressById(addressId);
        } catch (AddressNotFoundException ex) {
            //System.out.println("test1");
            System.err.println("invalid address ID");
            return;
        }
        //verify if the address belongs to the current user
        if (address.getCustomer().getCustomerId().equals(currentCustomer.getCustomerId())) {
            Bid newBid = new Bid(bidAmount, address);
            newBid.setAuctionListing(al);
            al.getBidList().add(newBid);
            auctionListingController.updateAuctionListing(al);
            //record credit transaction
            CreditTransaction ct = new CreditTransaction();
            ct.setTransactionDateTime(new Date());
            ct.setCustomer(currentCustomer);
            ct.setType(TransactionTypeEnum.DEBIT);
            ct.setBid(newBid);
            //associate transaction to bid
            newBid.setCreditTransaction(ct);
            bidController.createNewBid(newBid);
//            creditTransactionController.createNewCreditTransaction(ct);

            //associate transaction to customer
            currentCustomer.getCreditTransactionHistory().add(ct);
            //subtract credit balance 
            currentCustomer.setCreditBalance(currentCustomer.getCreditBalance().subtract(newBid.getCreditValue()));
            customerController.updateCustomer(currentCustomer);
            System.out.println("you have successfully bid fot the item!");
        } else {
            //System.out.println("test2");
            System.err.println("Invalid address ID");
        }
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
        System.out.printf("%8s%10s%40s\n", "ID", "End Date", "Description");
        for (Bid b : wonBids) {
            AuctionListing auctionListing = b.getAuctionListing();
            System.out.printf("%8s%10s%40s\n", auctionListing.getAuctionListingId().toString(),
                    auctionListing.getEndDateTime().toString(), auctionListing.getDescription());
        }
        Integer response = 0;
        while (true) {
            System.out.println("1: assign address to a won auction listings");
            System.out.println("2: back");
            response = 0;

            while (response < 1 || response > 2) {
                System.out.print("> ");
                response = sc.nextInt();
                if (response == 1) { //assign address to WON BID
                    AuctionListing auctionListing = null;
                    Long auctionListingId = Long.valueOf(doReadToken("auction listing ID"));
                    Bid wonBid = auctionListing.getWinningBid();
                    try {
                        auctionListing = auctionListingController.retrieveAuctionListingByAuctionListingId(auctionListingId);

                    } catch (AuctionListingNotFoundException ex) {
//                        System.out.println("test1");
                        System.err.println("invalid auction listing ID");
                        return;
                    }
                    //verify if the action listing's winning bid's transaction belongs to the current user
                    //if so, ask for input of an valid address id; link
                    if (auctionListing.getWinningBid().getCreditTransaction().getCustomer().getCustomerId().equals(currentCustomer.getCustomerId())) {
                        Long addressId = Long.valueOf(doReadToken("address ID"));
                        Address address = null;
                        try {
                            address = addressController.retrieveAddressById(addressId);
                        } catch (AddressNotFoundException ex) {
                            System.err.println("invalid address ID");
                            return;
                        }
                        //verify if the address belongs to the current user
                        if (address.getCustomer().getCustomerId().equals(currentCustomer.getCustomerId())) {
                            viewAddressDetails(addressId);
                        } else {
                            System.err.println("Invalid address ID");
                            return;
                        }

                        //link
                        address.getBids().add(wonBid);
                        address.setIsAssociatedWithWinningBid(true);//set address associated with won bid
                        addressController.updateAddress(address);
                        wonBid.setAddress(address);
                        bidController.updateBid(wonBid);
                    } else {
                        //System.out.println("test2");
                        System.err.println("Invalid address ID");
                    }
                } else if (response == 2) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 2) {
                break;
            }
        }
    }

    //Summer: include select delivery address in the browse won auction listing method
    private void viewAddressDetails(Long addressId) {

        Scanner sc = new Scanner(System.in);
        Address address = null;
        try {
            address = addressController.retrieveAddressById(addressId);

            //display
            System.out.printf("Address ID: %s\n", address.getAddressId());
            System.out.printf("Address Line 1: %s\n", address.getLineOne());
            System.out.printf("Address Line 2: %s\n", address.getLineTwo());
            System.out.printf("Zipcode: %s\n", address.getZipCode());
            //sub meu
            Integer response = 0;
            while (true) {
                System.out.println("1: update");
                System.out.println("2: delete");
                System.out.println("3: back");
                response = 0;

                while (response < 1 || response > 3) {
                    System.out.print("> ");
                    response = sc.nextInt();
                    if (response == 1) { //view details
                        Address newAddress = doReadAddress(1, 1, currentCustomer);
                        address.setLineOne(newAddress.getLineOne());
                        address.setLineTwo(newAddress.getLineTwo());
                        address.setZipCode(newAddress.getZipCode());
                        addressController.updateAddress(address);
                        System.out.println("");
                        System.out.println("Address successfully updated!");
                        return;
                    } else if (response == 2) {
                        addressController.deleteAddress(addressId);
                        System.out.println("");
                        System.out.println("Address deleted");
                        return;
                    } else if (response == 3) {
                        return;
                    } else {
                        System.out.println("Invalid option, please try again!\n");
                    }
                }
                if (response == 3) {
                    break;
                }
            }
        } catch (AddressNotFoundException ex) {
            //will not reach here
        }
    }
}
