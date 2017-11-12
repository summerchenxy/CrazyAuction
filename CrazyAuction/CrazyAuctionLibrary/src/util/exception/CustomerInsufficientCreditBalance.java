/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author alex_zy
 */
public class CustomerInsufficientCreditBalance extends Exception {

    /**
     * Creates a new instance of <code>CustomerInsufficientCreditBalance</code>
     * without detail message.
     */
    public CustomerInsufficientCreditBalance() {
    }

    /**
     * Constructs an instance of <code>CustomerInsufficientCreditBalance</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public CustomerInsufficientCreditBalance(String msg) {
        super(msg);
    }
}
