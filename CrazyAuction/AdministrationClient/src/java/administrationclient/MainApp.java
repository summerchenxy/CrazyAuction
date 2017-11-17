/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package administrationclient;

import ejb.session.stateless.AuctionListingControllerRemote;
import ejb.session.stateless.CreditPackageControllerRemote;
import ejb.session.stateless.EmployeeControllerRemote;
import entity.Employee;
import java.text.ParseException;
import java.util.Scanner;
import util.enumeration.AccessRightEnum;
import util.exception.InvalidAccessRightException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author Summer
 */
public class MainApp {
    private EmployeeControllerRemote employeeControllerRemote;
    private CreditPackageControllerRemote creditPackageControllerRemote;
    private AuctionListingControllerRemote auctionListingControllerRemote;
    
    private Employee currentEmployee;
    
    private SystemAdministrationModule systemAdministrationModule;
    private FinanceOperationModule financeOperationModule;
    private SalesOperationModule salesOperationModule;

    public MainApp() {
    }

    public MainApp(EmployeeControllerRemote employeeControllerRemote) {
        this.employeeControllerRemote = employeeControllerRemote;
    }
    public void runApp() throws InvalidAccessRightException, ParseException
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to OAS Administration Panel ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;
            
            while(response < 1 || response > 2)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    
                    try
                    {
                        doLogin();
                        menuMain();
                    }
                    catch(InvalidLoginCredentialException ex) 
                    {
                    }
                }
                else if (response == 2)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 2)
            {
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
        password = scanner.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0)
        {
            try
            {
                currentEmployee = employeeControllerRemote.employeeLogin(username, password);
                System.out.println("Login successful!\n");
            }        
            catch (InvalidLoginCredentialException ex)
            {
                System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                
                throw new InvalidLoginCredentialException();
            }   
        }
        else
        {
            throw new InvalidLoginCredentialException("Login credential was not provided!");
        }
    }
    
    
    
    private void menuMain() throws InvalidLoginCredentialException, InvalidAccessRightException, ParseException
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** RCBS - Teller Terminal ***\n");
            System.out.println("You are login. \n");
            System.out.println("1: Change password");
            System.out.println("2: Specify role to carry out more tasks");
            System.out.println("3: Logout\n");
            response = 0;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doChangePassword();
                }
                else if(response == 2)
                {
                    doSpecifyRole();
                }
                else if (response == 3)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 3)
            {
                break;
            }
        }
    }
    private void doChangePassword() throws InvalidLoginCredentialException{//runnable
        Scanner scanner = new Scanner(System.in); 
        String currentPassword = "";
        String newPassword = "";
        String confirmNewPassword = "";
        System.out.print("Enter Current Password> ");
        currentPassword = scanner.nextLine().trim();
        if (currentPassword.equals(currentEmployee.getPassword())){
            System.out.print("Enter new Password> ");
            newPassword = scanner.nextLine().trim();
            if(newPassword.length() > 0)
            {
                System.out.print("Reenter new Password for Confirmation> ");
                confirmNewPassword = scanner.nextLine().trim();
                if (newPassword.equals(confirmNewPassword)){
                    currentEmployee.setPassword(newPassword);
                    System.out.println("New password changed successfully");
                }
                else {
                    System.out.println("New password does not match");
                }
            }
        }
        else{
            throw new InvalidLoginCredentialException("Password entered was incorrect!");
        }
    }
    
    private void doSpecifyRole() throws InvalidAccessRightException, ParseException{
        Scanner scanner = new Scanner(System.in);        
        String input;
        while(true)
        {
            System.out.println("Specify your role (0: No Change, 1: System Administrator, 2: Finance employee, 3: Sales employee)> ");
            Integer accessRightInt = scanner.nextInt();
            
            if(accessRightInt >= 1 && accessRightInt <= 3)
            {
                if (!AccessRightEnum.values()[accessRightInt-1].equals(currentEmployee.getAccessRightEnum())){
                    System.out.println("You have chosen an incorrect role.");
                    break;
                }
                else {
                    if (accessRightInt == 1){
                        systemAdministrationModule = new SystemAdministrationModule(employeeControllerRemote, currentEmployee);
                        systemAdministrationModule.menuSystemAdministration();
                    }
                    else if (accessRightInt == 2){
                        financeOperationModule = new FinanceOperationModule(employeeControllerRemote, creditPackageControllerRemote, currentEmployee);
                        financeOperationModule.menuFinanceOperation();
                    }
                    else if (accessRightInt == 3){
                        salesOperationModule = new SalesOperationModule(employeeControllerRemote, auctionListingControllerRemote, currentEmployee);
                        salesOperationModule.menuSalesOperation();
                    }
                }
                break;
            }
            else if (accessRightInt == 0)
            {
                break;
            }
            else
            {
                System.out.println("Invalid option, please try again!\n");
            }
        }
    }
}
