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

/**
 *
 * @author alex_zy
 */
class MainApp {
    
    private CustomerControllerRemote customerController;

    private CreditTransactionControllerRemote creditTransactionController;
 
    private BidControllerRemote bidController;
    
    private AddressControllerRemote addressController;

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
            System.out.println("*** Welcome to Acution Client ***\n");
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
//                case 1: DoLogin(sc);
                case 2: doRegisterNewUser(sc);
                case 3: break;
            }
        }
    }

    private void doRegisterNewUser(Scanner sc) {
        System.out.println("*** You Are Registering s a New User***\n");
        String firstName = doReadFirstName(sc);
        String lastName = doReadLastName(sc);
        String nric = doReadNric(sc);
        List<Address> addresses = doReadAddresses(sc);
        String password = readPassword(sc);
        String username = readUsername(sc);
        
        Customer newCustomer = new Customer (firstName, lastName, nric, password, (new BigDecimal(0.0)), username);
        newCustomer.setAddresses(addresses);
        for (Address address:addresses) address.setCustomer(newCustomer);
        customerController.createNewCustomer(newCustomer);
        System.out.printf("You have registered successfully, %s%s.%nPlease use %s to login", 
                newCustomer.getFirstName(), newCustomer.getLastName(), newCustomer.getLoginCredential());

    }
   
    private void doUpdateProfile(Scanner sc, Customer customer)
    {
        System.out.println("You are updating your profile");
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
                viewAllAddresses();
                
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
        System.out.print("please enter your first name: \n> ");
        String firstName = sc.nextLine().trim();
        while (firstName.length()>32)
        {
            System.err.print("please enter your first name(maximum 32 characters): \n> ");
            firstName = sc.nextLine().trim();
        }
        return firstName;
    }

    private String doReadLastName(Scanner sc) {
        System.out.print("please enter your last name: \n> ");
        String lastName = sc.nextLine().trim();
        while (lastName.length()>32)
        {
            System.err.print("please enter your last name(maximum 32 characters): \n> ");
            lastName = sc.nextLine().trim();
        }
        return lastName;
    }

    private String doReadNric(Scanner sc) {
        System.out.print("please enter your NRIC or passport number: \n> ");
        System.out.print("This cannot be changed after you create your profile.\n");

        String identificationNumber = sc.nextLine().trim();
        while (identificationNumber.length()!=9)
        {
            System.err.print("please enter your NRIC or passport Number(9 characters): \n> ");
            identificationNumber = sc.nextLine().trim();
        }
        return identificationNumber;
    }

    private List<Address> doReadAddresses(Scanner sc) {
        System.out.print("You are going to provide your address for shipping purposes. Do you wish to enter more than 1 address? (Y/N)\n");
        String response = sc.nextLine().trim();
        int numberOfAddress;
        List<Address> addresses = new ArrayList<Address>();
        if (response.equalsIgnoreCase("y"))
        {
            System.out.println("enter the number of addresses you wish to provide:");
            numberOfAddress=sc.nextInt();
            sc.nextLine();
        }
        else  numberOfAddress = 1;
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
            password1 = sc.nextLine();
            if (password1.indexOf(' ')!=-1) 
            {
                System.err.println("password cannot contain blankspace");
                continue;
            }
            System.out.println("Please enter your password again:");
            password2 = sc.nextLine();
            firstAttempt=false;
        }
        return password1;
    }

    private String readUsername(Scanner sc) {
        System.out.print("Please enter your email or phone number. "
                + "This cannot be changed and will be used as your username for login.\n");

        String login1="login1";
        String login2="login2";
        boolean firstAttempt=true;
        while (!login1.equals(login2))
        {
            if (!firstAttempt) System.err.println("please make sure you have entered the same credential");
            System.out.println("Please enter your login credential:");
            login1 = sc.nextLine();
            if (login1.indexOf(' ')!=-1) 
            {
                System.err.println("credential cannot contain blankspace");
                continue;
            }
            System.out.println("Please enter your login credential again:");
            login2 = sc.nextLine();
            firstAttempt=false;
        }
        return login1;
    }

    private void viewAllAddresses() {
        
    }

}
