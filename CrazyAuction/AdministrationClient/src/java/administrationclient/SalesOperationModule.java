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
public class SalesOperationModule {
    private EmployeeControllerRemote employeeControllerRemote;
    
    private Employee currentEmployee;

    public SalesOperationModule() {
    }

    public SalesOperationModule(EmployeeControllerRemote employeeControllerRemote, Employee currentEmployee) {
        this.employeeControllerRemote = employeeControllerRemote;
        this.currentEmployee = currentEmployee;
    }
    
    public void menuSalesOperation() throws InvalidAccessRightException{
        if(currentEmployee.getAccessRightEnum() != AccessRightEnum.SALES)
            {
                throw new InvalidAccessRightException("You don't have SALES employee rights to access the sales operation module.");
            }

            Scanner scanner = new Scanner(System.in);
            Integer response = 0;
    }
}
