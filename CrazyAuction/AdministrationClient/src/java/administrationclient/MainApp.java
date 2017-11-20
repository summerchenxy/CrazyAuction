/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package administrationclient;

import ejb.session.stateless.CreditPackageControllerRemote;
import ejb.session.stateless.EmployeeControllerRemote;
import entity.Employee;
import java.text.ParseException;
import java.util.Scanner;
import util.enumeration.AccessRightEnum;
import static util.enumeration.AccessRightEnum.ADMIN;
import static util.enumeration.AccessRightEnum.FINANCE;
import static util.enumeration.AccessRightEnum.SALES;
import util.exception.InvalidAccessRightException;
import util.exception.InvalidLoginCredentialException;
import ejb.session.stateless.AuctionListingControllerRemote;
import ejb.session.stateless.BidControllerRemote;
import ejb.session.stateless.CustomerControllerRemote;
import util.exception.BidNotFoundException;

/**
 *
 * @author Summer
 */
public class MainApp {

    private EmployeeControllerRemote employeeControllerRemote;
    private CreditPackageControllerRemote creditPackageControllerRemote;
    private AuctionListingControllerRemote auctionListingControllerRemote;
    private CustomerControllerRemote customerControllerRemote;
    private BidControllerRemote bidControllerRemote;
    private Employee currentEmployee;

    private SystemAdministrationModule systemAdministrationModule;
//    private FinanceOperationModule financeOperationModule;
    private SalesOperationModule salesOperationModule;

    public MainApp() {
    }

    public MainApp(EmployeeControllerRemote employeeControllerRemote, CustomerControllerRemote customerControllerRemote, CreditPackageControllerRemote creditPackageControllerRemote, AuctionListingControllerRemote auctionListingControllerRemote, BidControllerRemote bidControllerRemote) {
        this.employeeControllerRemote = employeeControllerRemote;
        this.customerControllerRemote = customerControllerRemote;
        this.creditPackageControllerRemote = creditPackageControllerRemote;
        this.auctionListingControllerRemote = auctionListingControllerRemote;
        this.bidControllerRemote = bidControllerRemote;
    }

    public void runApp() throws InvalidAccessRightException, ParseException, BidNotFoundException {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to OAS Administration Panel ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;

            while (response < 1 || response > 2) {
                System.out.print("> ");
                response = scanner.nextInt();
                if (response == 1) {
                    try {
                        doLogin();
                        menuMain();
                    } catch (InvalidLoginCredentialException ex) {
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

    private void doLogin() throws InvalidLoginCredentialException//runnable for all cases
    {
        Scanner scanner = new Scanner(System.in);
        String username;
        String password = "";
        System.out.println("*** OAS Administration Panel :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = doReadPassword();
        //System.out.println(username+" "+password);
        if (username.length() > 0 && password.length() > 0) {
            try {
                currentEmployee = employeeControllerRemote.employeeLogin(username, password);
                System.out.println("Login successful!\n");
            } catch (InvalidLoginCredentialException ex) {
                System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                throw new InvalidLoginCredentialException();
            }
        } else {
            throw new InvalidLoginCredentialException("Login credential was not provided!");
        }
    }

    private void menuMain() throws InvalidLoginCredentialException, InvalidAccessRightException, ParseException, BidNotFoundException {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** OAS Administration Panel ***\n");
            System.out.println("You are login. \n");
            System.out.println("1: Change password");
            System.out.println("2: Perform more tasks");
            System.out.println("3: Logout\n");
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doChangePassword();
                } else if (response == 2) {
                    doRoleSpecificTasks();
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

    private void doChangePassword() throws InvalidLoginCredentialException {//runnable
        String password = doReadPassword();
        currentEmployee.setPassword(password);
        employeeControllerRemote.updateEmployee(currentEmployee);
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

    private void doRoleSpecificTasks() throws InvalidAccessRightException, ParseException, BidNotFoundException {

        AccessRightEnum accessRightEnum;
        accessRightEnum = currentEmployee.getAccessRightEnum();
        if (accessRightEnum.equals(ADMIN)) {
            systemAdministrationModule = new SystemAdministrationModule(employeeControllerRemote, currentEmployee);
            systemAdministrationModule.menuSystemAdministration();
        } else if (accessRightEnum.equals(FINANCE)) {
            //Employee employee = new Employee("test", "sian", "plswork", "password", AccessRightEnum.FINANCE);
            FinanceOperationModule financeOperationModule = new FinanceOperationModule(currentEmployee, employeeControllerRemote, creditPackageControllerRemote);
            //System.out.println(currentEmployee.getAccessRightEnum().toString());
            //System.out.println(creditPackageControllerRemote.toString());
            financeOperationModule.menuFinanceOperation();
        }
        else if (accessRightEnum.equals(SALES)){
            salesOperationModule = new SalesOperationModule(employeeControllerRemote, customerControllerRemote, auctionListingControllerRemote,bidControllerRemote, currentEmployee);
            salesOperationModule.menuSalesOperation();
        } else {
            throw new InvalidAccessRightException();
        }

    }
}
