/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package administrationclient;

import ejb.session.stateless.EmployeeControllerRemote;
import ejb.session.stateless.CreditPackageControllerRemote;
import entity.CreditPackage;
import entity.Employee;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import util.enumeration.AccessRightEnum;
import util.exception.CreditPackageNotFoundException;
import util.exception.InvalidAccessRightException;
import entity.CreditTransaction;
import java.util.ArrayList;

/**
 *
 * @author Summer
 */
public class FinanceOperationModule {

    private EmployeeControllerRemote employeeControllerRemote;
    private CreditPackageControllerRemote creditPackageControllerRemote;
    private Employee currEmployee;

    public FinanceOperationModule() {
    }

    public FinanceOperationModule(Employee currenteEmployee, EmployeeControllerRemote employeeControllerRemote, CreditPackageControllerRemote creditPackageControllerRemote) {

        this.currEmployee = currenteEmployee;
        this.employeeControllerRemote = employeeControllerRemote;
        this.creditPackageControllerRemote = creditPackageControllerRemote;
    }

    public void menuFinanceOperation() throws InvalidAccessRightException {
        //System.out.println(creditPackageControllerRemote.toString());
        //System.out.println(currEmployee.getEmployeeId());
        //System.out.println(currEmployee.getAccessRightEnum().toString());
        if (currEmployee.getAccessRightEnum() != AccessRightEnum.FINANCE) {
            throw new InvalidAccessRightException("You don't have FINANCE Employee rights to access the finance operation module.");
        }

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        while (true) {
            System.out.println("*** OAS Administration Panel :: Finance Operation ***\n");
            System.out.println("1: Create New Credit Package");
            System.out.println("2: View Credit Package Details");
            System.out.println("3: View All Credit Packages");
            System.out.println("4: Back\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doCreateNewCreditPackage();
                } else if (response == 2) {
                    doViewCreditPackageDetails();
                } else if (response == 3) {
                    doViewAllCreditPackages();
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

    private void doCreateNewCreditPackage() {
        Scanner scanner = new Scanner(System.in);
        CreditPackage newCreditPackage = new CreditPackage();

        System.out.println("*** OAS Administration Panel :: Finance Operation :: Create New Credit Package ***\n");
        System.out.println("Enter Price");

        BigDecimal price = null; //ALEX: YOUR SAMPLE IS HEREEEE
        while (price == null) {
            try {
                System.out.print("> ");
                newCreditPackage.setPrice(BigDecimal.valueOf(scanner.nextDouble()));
                price = newCreditPackage.getPrice();
            } catch (Exception ex) {
            }
        }
        System.out.println("Enter Credit Value");

        BigDecimal credit = null;
        while (credit == null) {
            try {
                System.out.print("> ");
                newCreditPackage.setCredit(BigDecimal.valueOf(scanner.nextDouble()));
                credit = newCreditPackage.getCredit();
            } catch (Exception ex) {
            }
        }
        //newCreditPackage.setCreditTransactions(new ArrayList<CreditTransaction>());
        //System.out.println(newCreditPackage.getCreditTransactions().size());
        newCreditPackage.setEnabled(Boolean.TRUE);//assume that credit package is by default enabled when it is created
        creditPackageControllerRemote.getTransactionsNum(newCreditPackage);
        newCreditPackage = creditPackageControllerRemote.createCreditPackage(newCreditPackage);
        System.out.println("New creditPackagecreated successfully!: ID " + newCreditPackage.getCreditPackageId() + "\n");
    }

    private void doViewCreditPackageDetails() {//runnable
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        System.out.println("*** OAS Administration Panel :: Finance Operation :: View Credit Package Details ***\n");
        System.out.println("Enter Credit Package ID");
        System.out.print("> ");

        Long creditPackageId = null;
        while (creditPackageId == null) {
            try {
                System.out.print("> ");
                creditPackageId = Long.valueOf(scanner.nextLine().trim());
            } catch (NumberFormatException ex) {
                System.out.println("Invalid input. Please try again");
            }
        }
        try {
            CreditPackage creditPackage = creditPackageControllerRemote.retrieveCreditPackageByCreditPackageId(creditPackageId);
//            List<CreditTransaction> creditTransactions = creditPackage.getCreditTransactions();
//            System.out.println(creditTransactions.toString());
//            System.out.println(creditTransactions==null);
//            System.out.println(creditTransactions.size());
//            if (!creditTransactions.isEmpty()){
//                System.out.printf("%8s%20s%20s%15s%20s%20s\n", "CreditPackage ID", "Price", "Credit Value", "Enabled Status", 
//                    "Credit Transaction Date Time", "Credit Transaction Purchasing Customer ID", "Credit Transaction Type");
//            
//                for (CreditTransaction ct : creditTransactions) {
//                    System.out.printf("%8s%20s%20s%15s%20s%20s\n",
//                            creditPackage.getCreditPackageId().toString(), creditPackage.getPrice().toString(), creditPackage.getCredit().toString(), creditPackage.getEnabled().toString(), 
//                            ct.getTransactionDateTime().toString(), ct.getPurchasingCustomer().getCustomerId(),ct.getType().toString());
//                }          
//            }
//            else{
//no need to print transactions
            System.out.printf("%8s%20s%20s%15s\n", "ID", "Price", "Credit Value", "Enabled Status");
            if (creditPackage.getEnabled() == true) {
                System.out.printf("%8s%20s%20s%15s\n",
                        creditPackage.getCreditPackageId().toString(), creditPackage.getPrice().toString(), creditPackage.getCredit().toString(), "Enabled");
            } else {
                System.out.printf("%8s%20s%20s%15s\n",
                        creditPackage.getCreditPackageId().toString(), creditPackage.getPrice().toString(), creditPackage.getCredit().toString(), "Disabled");
            }
            System.out.println("------------------------");
            System.out.println("1: Update Credit Package");
            System.out.println("2: Delete Credit Package");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = scanner.nextInt();

            if (response == 1) {
                doUpdateCreditPackage(creditPackage);
            } else if (response == 2) {
                doDeleteCreditPackage(creditPackage);
            }
        } catch (CreditPackageNotFoundException ex) {
            System.out.println("An error has occurred while retrieving creditPackage: " + ex.getMessage() + "\n");
        }
    }

    private void doUpdateCreditPackage(CreditPackage creditPackage) {//runnable
        Scanner scanner = new Scanner(System.in);
        BigDecimal newPrice;
        String input;

        System.out.println("*** OAS Administration Panel :: Finance Operation :: View Credit Package Details :: Update Credit Package ***\n");

        System.out.println("Enter Price");
        System.out.print("> ");
        BigDecimal price = null; //ALEX: YOUR SAMPLE IS HEREEEE
        while (price == null) {
            try {
                System.out.print("> ");
                creditPackage.setPrice(BigDecimal.valueOf(scanner.nextDouble()));
                price = creditPackage.getPrice();
            } catch (Exception ex) {
            }
        }

        creditPackage.setPrice(price);
        System.out.println("price updated " + price.toString());

        System.out.print("Enter 'Enabled' or 'Disabled'");
        input = scanner.nextLine().trim();
        while (!input.equalsIgnoreCase("Enabled") && !input.equalsIgnoreCase("Disabled")) {
            try {
                System.out.print("> ");
                input = scanner.nextLine().trim();
                if (input.equals("Enabled")) {
                    creditPackage.setEnabled(Boolean.TRUE);
                    creditPackageControllerRemote.updateCreditPackage(creditPackage);
                } else if (input.equals("Disabled")) {
                    creditPackageControllerRemote.disableCreditPackage(creditPackage);
                }
            } catch (Exception ex) {
            }
        }

        creditPackageControllerRemote.updateCreditPackage(creditPackage);
        System.out.println("Credit Package with ID " + creditPackage.getCreditPackageId() + " updated successfully!\n");
    }

    private void doDeleteCreditPackage(CreditPackage creditPackage) throws CreditPackageNotFoundException {
        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("*** OAS Administration Panel :: Finance Operation :: View Credit Package Details :: Delete Credit Package ***\n");
        System.out.print("Confirm Delete Credit Package of ID:" + creditPackage.getCreditPackageId() + "(Enter 'Y' to Delete)> ");
        input = scanner.nextLine().trim();

        if (input.equals("Y")) {
            if (creditPackageControllerRemote.getTransactionsNum(creditPackage) == 0) {
                //System.out.println(creditPackage.getCreditPackageId());
                //creditPackageControllerRemote.retrieveCreditPackageByCreditPackageId(creditPackage.getCreditPackageId());
                creditPackageControllerRemote.deleteCreditPackage(creditPackage);
                System.out.println("Credit Package deleted successfully!\n");
            } else {//credit package is used and is marked as disabled
                creditPackageControllerRemote.disableCreditPackage(creditPackage);
                System.out.println("Credit Package has been used hence it is disabled but not deleted!\n");
            }
        } else {
            System.out.println("Credit Package NOT deleted!\n");
        }
    }

    private void doViewAllCreditPackages() {//runnable. format
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** OAS Administration Panel :: Finance Operation :: View All CreditPackages ***\n");

        List<CreditPackage> allCreditPackages = creditPackageControllerRemote.retrieveAllCreditPackages();
        System.out.printf("%8s%20s%20s%15s\n", "ID", "Price", "Credit Value", "Enabled Status");

        for (CreditPackage creditPackage : allCreditPackages) {
            if (creditPackage.getEnabled() == true) {
                System.out.printf("%8s%20s%20s%15s\n",
                        creditPackage.getCreditPackageId().toString(), creditPackage.getPrice().toString(), creditPackage.getCredit().toString(), "Enabled");
            } else {
                System.out.printf("%8s%20s%20s%15s\n",
                        creditPackage.getCreditPackageId().toString(), creditPackage.getPrice().toString(), creditPackage.getCredit().toString(), "Disabled");
            }
        }

        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
}
