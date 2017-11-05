/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author Summer
 */
public class BidNotFoundException extends Exception{

    public BidNotFoundException() {
    }

    public BidNotFoundException(String message) {
        super(message);
    }
    
}
