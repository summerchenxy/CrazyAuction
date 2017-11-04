/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auctionclient;

import ejb.session.stateless.AddressControllerRemote;
import ejb.session.stateless.BidControllerRemote;
import ejb.session.stateless.CreditTransactionControllerRemote;
import ejb.session.stateless.CustomerControllerRemote;
import javax.ejb.EJB;

/**
 *
 * @author alex_zy
 */
public class Main {

    @EJB
    private static CustomerControllerRemote customerController;

    @EJB
    private static CreditTransactionControllerRemote creditTransactionController;

    @EJB
    private static BidControllerRemote bidController;

    @EJB
    private static AddressControllerRemote addressController;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
        {
            MainApp mainApp = new MainApp(customerController,creditTransactionController,bidController,addressController );
            mainApp.runApp();
        }
    
}
