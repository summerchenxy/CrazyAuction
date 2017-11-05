/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package administrationclient;

import ejb.session.stateless.EmployeeControllerRemote;
import javax.ejb.EJB;
import util.exception.InvalidAccessRightException;

/**
 *
 * @author Summer
 */
public class Main {
    @EJB
    private static EmployeeControllerRemote employeeControllerRemote;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InvalidAccessRightException {
        // TODO code application logic here

        MainApp mainApp = new MainApp(employeeControllerRemote);
        mainApp.runApp();

    }
    
}
