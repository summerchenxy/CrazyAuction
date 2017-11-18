/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package administrationclient;

import ejb.session.stateless.EmployeeControllerRemote;
import entity.Employee;
import java.util.List;
import java.util.Scanner;
import util.enumeration.AccessRightEnum;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidAccessRightException;

/**
 *
 * @author Summer
 */
public class SystemAdministrationModule {
    private EmployeeControllerRemote employeeControllerRemote;
    
    private Employee currentEmployee;

    public SystemAdministrationModule() {
    }

    public SystemAdministrationModule(EmployeeControllerRemote employeeControllerRemote, Employee currentEmployee) {
        this.employeeControllerRemote = employeeControllerRemote;
        this.currentEmployee = currentEmployee;
        System.out.println(currentEmployee.toString());
    }
    
    public void menuSystemAdministration() throws InvalidAccessRightException{
        System.out.println(currentEmployee.getUsername());
        if(currentEmployee.getAccessRightEnum() != AccessRightEnum.ADMIN)
        {
            throw new InvalidAccessRightException("You don't have ADMIN rights to access the system administration module.");
        }
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** OAS Administration Panel :: System Administration ***\n");
            System.out.println("1: Create New Employee");
            System.out.println("2: View Employee Details");
            System.out.println("3: View All Employee");
            System.out.println("4: Back\n");
            response = 0;
            
            while(response < 1 || response > 4)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doCreateNewEmployee();
                }
                else if(response == 2)
                {
                    doViewEmployeeDetails();
                }
                else if(response == 3)
                {
                    doViewAllEmployees();
                }
                else if (response == 4)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 4)
            {
                break;
            }
        }
    }
    private void doCreateNewEmployee()
    {
        Scanner scanner = new Scanner(System.in);
        Employee newEmployee = new Employee();
        
        System.out.println("*** OAS Administration Panel :: System Administration :: Create New Employee ***\n");
        System.out.print("Enter First Name> ");
        newEmployee.setFirstName(scanner.nextLine().trim());
        System.out.print("Enter Last Name> ");
        newEmployee.setLastName(scanner.nextLine().trim());
        
        while(true)
        {
            System.out.print("Select Access Right (1: System Administrator, 2: Finance employee, 3: Sales employee)> ");
            Integer accessRightInt = scanner.nextInt();
            
            if(accessRightInt >= 1 && accessRightInt <= 3)
            {
                newEmployee.setAccessRightEnum(AccessRightEnum.values()[accessRightInt-1]);
                break;
            }
            else
            {
                System.out.println("Invalid option, please try again!\n");
            }
        }
        
        scanner.nextLine();
        System.out.print("Enter Username> ");
        newEmployee.setUsername(scanner.nextLine().trim());
        System.out.print("Enter Password> ");
        newEmployee.setPassword(scanner.nextLine().trim());
        
        newEmployee = employeeControllerRemote.createNewEmployee(newEmployee);
        System.out.println("New employeecreated successfully! ID: " + newEmployee.getEmployeeId()+ "\n");
    }
    
    private void doViewEmployeeDetails()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** OAS Administration Panel :: System Administration :: View Employee Details ***\n");
        System.out.print("Enter Employee ID> ");
        Long employeeId = scanner.nextLong();
        
        try
        {
            Employee employee = employeeControllerRemote.retrieveEmployeeByEmployeeId(employeeId);
            System.out.printf("%8s%20s%20s%15s%20s%20s\n", "Employee ID", "First Name", "Last Name", "Access Right", "Username", "Password");
            System.out.printf("%8s%20s%20s%15s%20s%20s\n", employee.getEmployeeId().toString(), employee.getFirstName(), employee.getLastName(), employee.getAccessRightEnum().toString(), employee.getUsername(), employee.getPassword());         
            System.out.println("------------------------");
            System.out.println("1: Update Employee");
            System.out.println("2: Delete Employee");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = scanner.nextInt();

            if(response == 1)
            {
                doUpdateEmployee(employee);
            }
            else if(response == 2)
            {
                doDeleteEmployee(employee);
            }
        }
        catch(EmployeeNotFoundException ex)
        {
            System.out.println("An error has occurred while retrieving employee: " + ex.getMessage() + "\n");
        }
    }
    private void doUpdateEmployee(Employee employee)
    {
        Scanner scanner = new Scanner(System.in);        
        String input;
        
        System.out.println("*** OAS Administration Panel :: System Administration :: View Employee Details :: Update Employee ***\n");
        System.out.print("Enter First Name (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            employee.setFirstName(input);
        }
                
        System.out.print("Enter Last Name (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            employee.setLastName(input);
        }
        
        while(true)
        {
            System.out.print("Select Access Right (0: No Change, 1: System Administrator, 2: Finance employee, 3: Sales employee)> ");
            Integer accessRightInt = scanner.nextInt();
            
            if(accessRightInt >= 1 && accessRightInt <= 3)
            {
                employee.setAccessRightEnum(AccessRightEnum.values()[accessRightInt-1]);
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
        
        scanner.nextLine();
        System.out.print("Enter Username (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            employee.setUsername(input);
        }
        
        System.out.print("Enter Password (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            employee.setPassword(input);
        }
        
        employeeControllerRemote.updateEmployee(employee);
        System.out.println("employee updated successfully!\n");
    }
    
    private void doDeleteEmployee(Employee employee)
    {
        Scanner scanner = new Scanner(System.in);        
        String input;
        
        System.out.println("*** OAS Administration Panel :: System Administration :: View Employee Details :: Delete Employee ***\n");
        System.out.printf("Confirm Delete Employee %s %s (Employee ID: %d) (Enter 'Y' to Delete)> ", employee.getFirstName(), employee.getLastName(), employee.getEmployeeId());
        input = scanner.nextLine().trim();
        
        if(input.equals("Y"))
        {
            try 
            {
                employeeControllerRemote.deleteEmployee(employee.getEmployeeId());
                System.out.println("Employee deleted successfully!\n");
            } 
            catch (EmployeeNotFoundException ex) 
            {
                System.out.println("An error has occurred while deleting employee: " + ex.getMessage() + "\n");
            }            
        }
        else
        {
            System.out.println("Employee NOT deleted!\n");
        }
    }
    private void doViewAllEmployees()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** OAS Administration Panel :: System Administration :: View All Employees ***\n");
        
        List<Employee> allEmployees = employeeControllerRemote.retrieveAllEmployees();
        System.out.printf("%8s%20s%20s%15s%20s%20s\n", "Employee ID", "First Name", "Last Name", "Access Right", "Username", "Password");

        for(Employee employee:allEmployees)
        {
            System.out.printf("%8s%20s%20s%15s%20s%20s\n", employee.getEmployeeId().toString(), employee.getFirstName(), employee.getLastName(), employee.getAccessRightEnum().toString(), employee.getUsername(), employee.getPassword());
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
}
