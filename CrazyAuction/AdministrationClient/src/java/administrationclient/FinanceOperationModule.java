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

/**
 *
 * @author Summer
 */
public class FinanceOperationModule {

    private EmployeeControllerRemote employeeControllerRemote;
    private CreditPackageControllerRemote creditPackageControllerRemote;
    private Employee currentEmployee;

    public FinanceOperationModule() {
    }

    public FinanceOperationModule(EmployeeControllerRemote employeeControllerRemote, CreditPackageControllerRemote creditPackageControllerRemote, Employee currenteEmployee) {
        this.employeeControllerRemote = employeeControllerRemote;
        this.creditPackageControllerRemote = creditPackageControllerRemote;
        this.currentEmployee = currentEmployee;
    }

    public void menuFinanceOperation() throws InvalidAccessRightException {
        if (currentEmployee.getAccessRightEnum() != AccessRightEnum.FINANCE) {
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

            if (response == 7) {
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
                newCreditPackage.setPrice(scanner.nextBigDecimal());
            } catch (Exception ex) {
            }
        }
        System.out.print("Enter Initial Credit> ");

        BigDecimal credit = null;
        while (credit == null) {
            try {
                System.out.print("> ");
                newCreditPackage.setCredit(scanner.nextBigDecimal());
            } catch (Exception ex) {
            }
        }
        newCreditPackage.setEnabled(Boolean.TRUE);//assume that credit package is by default enabled when it is created

        newCreditPackage = creditPackageControllerRemote.createCreditPackage(newCreditPackage);
        System.out.println("New creditPackagecreated successfully!: " + newCreditPackage.getCreditPackageId() + "\n");
    }

    private void doViewCreditPackageDetails() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        System.out.println("*** OAS Administration Panel :: Finance Operation :: View Credit Package Details ***\n");
        System.out.print("Enter CreditPackage ID> ");
        Long creditPackageId = scanner.nextLong();

        try {
            CreditPackage creditPackage = creditPackageControllerRemote.retrieveCreditPackageByCreditPackageId(creditPackageId);
            System.out.printf("%8s%20s%20s%15s%20s%20s\n", "CreditPackage ID", "Price", "Credit", "Enabled Status", "Credit Transactions");
            System.out.printf("%8s%20s%20s%15s%20s%20s\n",
                    creditPackage.getCreditPackageId().toString(), creditPackage.getPrice().toString(), creditPackage.getCredit().toString(), creditPackage.getEnabled().toString(), creditPackage.getTransactions().toString());
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

    private void doUpdateCreditPackage(CreditPackage creditPackage) {
        Scanner scanner = new Scanner(System.in);
        BigDecimal newPrice;
        String input;

        System.out.println("*** OAS Administration Panel :: Finance Operation :: View Credit Package Details :: Update Credit Package ***\n");

        System.out.print("Enter Price (blank if no change)> ");
        newPrice = scanner.nextBigDecimal();
        if (creditPackage.getTransactions() == null) {
            creditPackage.setPrice(newPrice);
        } else {
            System.out.println("Price of the Credit Package cannot be updated as it has been purchased");
        }

        System.out.print("Enter 'Enabled' or 'Disabled'> ");
        input = scanner.nextLine().trim();
        if (input.equals("Enabled")) {
            creditPackage.setEnabled(Boolean.TRUE);
        } else if (input.equals("Disabled")) {
            creditPackage.setEnabled(Boolean.FALSE);
        } else {
            System.out.println("Invalid option!\n");
        }
        creditPackageControllerRemote.updateCreditPackage(creditPackage);
        System.out.println("creditPackage updated successfully!\n");
    }

    private void doDeleteCreditPackage(CreditPackage creditPackage) throws CreditPackageNotFoundException {
        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("*** OAS Administration Panel :: Finance Operation :: View Credit Package Details :: Delete Credit Package ***\n");
        System.out.printf("Confirm Delete CreditPackage of price %s and initial credit of %s (Credit Package ID: %d) (Enter 'Y' to Delete)> ", creditPackage.getPrice().toString(), creditPackage.getCredit().toString(), creditPackage.getCreditPackageId());
        input = scanner.nextLine().trim();

        if (input.equals("Y")) {
            /*if(creditPackage.getInitialCredit().equals(creditPackage.getAvailableCredit())){
                creditPackageControllerRemote.deleteCreditPackage(creditPackage.getCreditPackageId());
                System.out.println("Credit Package deleted successfully!\n");
            }
            else{//credit package is used and is marked as disabled
                creditPackage.setEnabled(Boolean.FALSE);
                creditPackageControllerRemote.updateCreditPackage(creditPackage);
                System.out.println("Credit Package has been used hence it is disabled but not deleted!\n");
            }*/
        } else {
            System.out.println("CreditPackage NOT deleted!\n");
        }
    }

    private void doViewAllCreditPackages() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** OAS Administration Panel :: Finance Operation :: View All CreditPackages ***\n");

        List<CreditPackage> allCreditPackages = creditPackageControllerRemote.retrieveAllCreditPackages();
        System.out.printf("%8s%20s%20s%15s%20s%20s\n", "CreditPackage ID", "Price", "Credit", "Enabled Status", "Credit Transactions");

        for (CreditPackage creditPackage : allCreditPackages) {
            System.out.printf("%8s%20s%20s%15s%20s%20s\n", creditPackage.getCreditPackageId().toString(), creditPackage.getPrice().toString(), creditPackage.getCredit().toString(), creditPackage.getEnabled().toString(), creditPackage.getTransactions().toString());
        }

        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
}
