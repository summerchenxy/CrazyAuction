/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proxyclient;

import ws.client.AuctionListingNotFoundException_Exception;
import ws.client.CustomerNotFoundException_Exception;
import ws.client.InvalidLoginCredentialException_Exception;

/**
 *
 * @author Summer
 */
public class ProxyClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InvalidLoginCredentialException_Exception, CustomerNotFoundException_Exception, AuctionListingNotFoundException_Exception {
        // TODO code application logic here
        MainApp mainApp = new MainApp();
        mainApp.runApp();
    }
    
}
