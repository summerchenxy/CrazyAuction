/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package administrationclient;

import ejb.session.stateless.CreditPackageControllerRemote;
import ejb.session.stateless.EmployeeControllerRemote;
import java.text.ParseException;
import javax.ejb.EJB;
import util.exception.InvalidAccessRightException;
import ejb.session.stateless.AuctionListingControllerRemote;
import ejb.session.stateless.BidControllerRemote;
import ejb.session.stateless.CustomerControllerRemote;

/**
 *
 * @author Summer
 */
public class Main {
    @EJB
    private static EmployeeControllerRemote employeeControllerRemote;
    @EJB
    private static CustomerControllerRemote customergeControllerRemote;
    @EJB
    private static CreditPackageControllerRemote creditPackageControllerRemote;
    @EJB
    private static AuctionListingControllerRemote auctionListingControllerRemote;
    @EJB
    private static BidControllerRemote bidControllerRemote;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InvalidAccessRightException, ParseException {
        // TODO code application logic here

        MainApp mainApp = new MainApp(employeeControllerRemote, customergeControllerRemote, creditPackageControllerRemote, auctionListingControllerRemote, bidControllerRemote);
        mainApp.runApp();

    }
    
}
