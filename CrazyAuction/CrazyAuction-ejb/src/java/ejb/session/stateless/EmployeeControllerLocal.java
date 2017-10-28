/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import util.exception.EmployeeNotFoundException;
import util.exception.EmployeePwChangeException;
import util.exception.InvalidLoginCredentialException;


/**
 *
 * @author Summer
 */
public interface EmployeeControllerLocal {

    Employee employeeLogin(String username, String password) throws InvalidLoginCredentialException;

    void changePw(String username, String currentPw, String newPw) throws EmployeeNotFoundException, EmployeePwChangeException;

    Employee createNewEmployee(Employee newEmployee);

    void updateEmployee(Employee employee);

    void deleteEmployee(Long employeeId) throws EmployeeNotFoundException;

    List<Employee> retrieveAllStaffs();

    Employee retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException;

    Employee retrieveEmployeeByEmployeeId(Long employeeId) throws EmployeeNotFoundException;
    
}
