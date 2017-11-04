/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package administrationclient;

import ejb.session.stateless.EmployeeControllerRemote;
import entity.Employee;
import java.util.Scanner;
import util.enumeration.AccessRightEnum;
import util.exception.InvalidAccessRightException;

/**
 *
 * @author Summer
 */
public class FinanceOperationModule {
    private EmployeeControllerRemote employeeControllerRemote;
    
    private Employee currentEmployee;

    public FinanceOperationModule() {
    }

    public FinanceOperationModule(EmployeeControllerRemote employeeControllerRemote, Employee currentEmployee) {
        this.employeeControllerRemote = employeeControllerRemote;
        this.currentEmployee = currentEmployee;
    }
    
    public void menuFinanceOperation() throws InvalidAccessRightException{
        if(currentEmployee.getAccessRightEnum() != AccessRightEnum.FINANCE)
        {
            throw new InvalidAccessRightException("You don't have FINANCE employee rights to access the finance operation module.");
        }
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
    }
}
