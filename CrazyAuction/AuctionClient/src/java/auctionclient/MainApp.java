/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auctionclient;

import ejb.session.stateless.AddressControllerRemote;
import ejb.session.stateless.BidControllerRemote;
import ejb.session.stateless.CreditTransactionControllerRemote;
import ejb.session.stateless.CustomerControllerRemote;
import entity.Address;
import entity.Customer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author alex_zy
 */
class MainApp {
    
    private CustomerControllerRemote customerController;

    private CreditTransactionControllerRemote creditTransactionController;
 
    private BidControllerRemote bidController;
    
    private AddressControllerRemote addressController;
    private Customer currentCustomer;

    public MainApp() {
    }

    public MainApp(CustomerControllerRemote customerController, CreditTransactionControllerRemote creditTransactionController, BidControllerRemote bidController, AddressControllerRemote addressController) {
        this.customerController=customerController;
        this.creditTransactionController = creditTransactionController;
        this.bidController = bidController;
        this.addressController =addressController;
    }
    
    public void runApp(){
        Scanner sc = new Scanner(System.in);

        
        while(true)
        {
            Integer response = 0;            
            System.out.println("\n*** Auction Client :: Welcome ***\n");
            System.out.println("1: Returning User");
            System.out.println("2: New User");           
            System.out.println("3: Exit\n"); 
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");
                response = sc.nextInt();
                
                if(response < 1 || response>3)
                    System.out.println("Invalid option, please try again!\n"); 
            }
            
            switch(response)
            {
                case 1: {
                try {
                    DoLogin(sc);
                } catch (InvalidLoginCredentialException ex) {
                }
            }
                case 2: doRegisterNewUser(sc);
                case 3: break;
            }
        }
    }

    private void doRegisterNewUser(Scanner sc) {
        System.out.println("\n*** Auction Client :: Register New User***\n");
        String firstName = doReadFirstName(sc);
        String lastName = doReadLastName(sc);
        String nric = doReadNric(sc);
        List<Address> addresses = doReadAddresses(sc);
        String password = readPassword(sc);
        String username = setUsername(sc);
        
        Customer newCustomer = new Customer (firstName, lastName, nric, password, (new BigDecimal(0.0)), username);
        newCustomer.setAddresses(addresses);
        for (Address address:addresses) address.setCustomer(newCustomer);
        customerController.createNewCustomer(newCustomer);
        System.out.printf("You have registered successfully, %s%s.%nPlease use %s to login", 
                newCustomer.getFirstName(), newCustomer.getLastName(), newCustomer.getUsername());

    }
   
    private void doUpdateProfile(Scanner sc, Customer customer)
    {
        System.out.println("\n*** Auction Client :: Update Profile ***\n");
        System.out.println("1. change name ");
        System.out.println("2. add address(es)");
        System.out.println("3. disable an address");
        System.out.println("4. change password");
        int response = sc.nextInt();
        sc.nextLine();
        switch(response)
        {
            case 1: 
                String firtname = doReadFirstName(sc);
                String lastName = doReadLastName(sc);
                customer.setFirstName(firtname);
                customer.setLastName(lastName);
                customerController.updateCustomer(customer);
            case 2:
                Boolean update = true;
                viewAllAddresses(sc, update);
                
        }
    }
    private Address doReadAddress(Scanner sc, int i, int numberOfAddress, Customer newCustomer) {
        System.out.println("enter the 1st line of your address ("+i+" of "+numberOfAddress+"):");
        String lineOne = sc.nextLine().trim();
        while (lineOne.length()>32)
        {
            System.err.println("enter the 1st line of your address ("+i+" of "+numberOfAddress+", maximum 32 characters):");
            lineOne = sc.nextLine().trim();
        }
        System.out.println("enter the 2nd line of your address ("+i+" of "+numberOfAddress+"):");
        String lineTwo= sc.nextLine().trim();
        while (lineTwo.length()>32)
        {
            System.err.println("enter the 2nd line of your address ("+i+" of "+numberOfAddress+", maximum 32 characters):");
            lineOne = sc.nextLine().trim();
        }
        return (new Address(lineOne, lineOne, newCustomer));
    }

    private String doReadFirstName(Scanner sc) {
        System.out.println("please enter your first name: \n> ");
        System.out.print("> ");
        String firstName = sc.next();
        sc.nextLine();
        while (firstName.length()>32)
        {
            System.err.println("please enter your first name(maximum 32 characters): ");
            System.out.print("> ");
            firstName = sc.next();  
            sc.nextLine();
        }
        return firstName;
    }

    private String doReadLastName(Scanner sc) {
        System.out.print("please enter your last name: \n> ");
        sc.nextLine();
        String lastName = sc.nextLine().trim();
        while (lastName.length()>32)
        {
            System.err.println("please enter your last name(maximum 32 characters): ");
            System.out.print("> ");
            lastName = sc.next();
            sc.nextLine();
        }
        return lastName;
    }

    private String doReadNric(Scanner sc) {
        System.out.println("please enter your NRIC or passport number. This cannot be changed after you create your profile");
        System.out.print("> ");

        String identificationNumber = sc.next();
        sc.nextLine();
        while (identificationNumber.length()!=9)
        {
            System.err.println("please enter your NRIC or passport Number(9 characters): ");
            System.err.print("> ");
            identificationNumber = sc.next();
            sc.nextLine();
        }
        return identificationNumber;
    }

    private List<Address> doReadAddresses(Scanner sc) {
        System.out.println("You are going to provide your address for shipping purposes. Do you wish to enter more than 1 address? (Y/N)");
        System.out.print("> ");
        String response = sc.next();
        sc.nextLine();
        int numberOfAddress;
        List<Address> addresses = new ArrayList<Address>();
        while (true)
        {
            if (response.equalsIgnoreCase("y"))
            {
                System.out.println("enter the number of addresses you wish to provide:");
                System.out.print("> ");
                numberOfAddress=sc.nextInt();
                sc.nextLine();
                break;
            }
            else if (response.equalsIgnoreCase("n"))
            {
                numberOfAddress = 1;
                break;
            }
            else 
            {
                System.err.println("Invalid input. Please try again");
                System.err.print("> ");
                response = sc.next();
                sc.nextLine();
            }
        }
        for (int i = 0; i < numberOfAddress; i++)
        {
            Address address = doReadAddress(sc,i,numberOfAddress,null);
        }
        
        return addresses;
    }

    private String readPassword(Scanner sc) {
        String password1="password1";
        String password2="password2";
        boolean firstAttempt=true;
        while (!password1.equals(password2))
        {
            if (!firstAttempt) System.err.println("please make sure you have entered the same password");
            System.out.println("Please enter your password:");
            System.out.print("> ");
            password1 = sc.next();
            sc.nextLine();
            if (password1.indexOf(' ')!=-1) 
            {
                System.err.println("password cannot contain blankspace");
                continue;
            }
            System.out.println("Please enter your password again:");
            System.out.print("> ");
            password2 = sc.next();
            sc.nextLine();
            firstAttempt=false;
        }
        return password1;
    }

    private String setUsername(Scanner sc) {
        System.out.print("Please enter your email or phone number. "
                + "This cannot be changed and will be used as your username for login.\n");

        String username1="login1";
        String username2="login2";
        boolean firstAttempt=true;
        while (!username1.equals(username2))
        {
            if (!firstAttempt) System.err.println("please make sure you have entered the same email or phone number");
            System.out.println("Please enter your email or phone number:");
            System.out.print("> ");
            username1 = sc.next();
            sc.nextLine();
            if (username1.indexOf(' ')!=-1) 
            {
                System.err.println("email or phone number cannot contain blankspace");
                continue;
            }
            System.out.println("Please enter your email or phone number again:");
            System.out.print("> ");
            username2 = sc.next();
            sc.nextLine();
            firstAttempt=false;
        }
        return username1;
    }

    private void viewAllAddresses(Scanner sc, Boolean delete) {
        System.out.println("\n*** Auction Client :: View All Addresses ***\n");
        
        List<Address> addresses = currentCustomer.getAddresses();
        int AddressIndex = 0;
        for (Address address : addresses)
        {
            System.out.printf("%4d%35s%n%4s%35s%n", ++AddressIndex, address.getLineOne(), "", address.getLineTwo());
        }
        if (!delete)
        {
            System.out.print("Press any key to continue...> ");
            sc.nextLine();
            return;
        }
        System.out.println("Please enter the address index number that you would like to delete:");
        int indexOfAddressToDelete = 0;
        boolean firstAttempt = true;
        while (indexOfAddressToDelete < 0 || indexOfAddressToDelete >= currentCustomer.getAddresses().size())
        {        
            if (!firstAttempt)
            {
                System.err.print("Invalid address index number. Please try again");
            }
            System.out.print("> ");
            indexOfAddressToDelete = sc.nextInt()-1; //adjust for 0-based indice
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
        try
        {
            customer = customerController.retrieveCustomerByUsername(username);
        } catch (CustomerNotFoundException ex) {
            System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
            throw new InvalidLoginCredentialException();
        }
        String password = doReadToken(sc, "password");
        if (customer.getPassword().equals(password))
        {
            currentCustomer = customer;
            menuMain(sc);
        }
        else 
        {
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
        
        while(true)
        {
            System.out.println("\n*** Auction C lient :: Menu Main***\n");
            System.out.printf("You are login as %s\n", currentCustomer.getUsername());
            System.out.println("1: manage my profile");
            System.out.println("2: manage addresses");
            System.out.println("3: auction & bid");
            System.out.println("4: manage my credits");
            System.out.println("5: Logout\n");
            response = 0;
            
            while(response < 1 || response > 5)
            {
                System.out.print("> ");

                response = sc.nextInt();

                if(response == 1)
                {
                    menuProfile();
                }
                else if(response == 2)
                {
                    menuAddress();
                }
                else if(response == 3)
                {
                    menusAuctionAndBid();
                }
                else if(response == 4)
                {
                    menusCredit();
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
            if(response == 5)
            {
                break;
            }
        }
    }

    private void menuProfile() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void menuAddress() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void menusAuctionAndBid() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void menusCredit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}