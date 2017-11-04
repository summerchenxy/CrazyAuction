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
public class CustomerPasswordChangeException extends Exception {

    /**
     * Creates a new instance of <code>CustomerPasswordChangeException</code>
     * without detail message.
     */
    public CustomerPasswordChangeException() {
    }

    /**
     * Constructs an instance of <code>CustomerPasswordChangeException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public CustomerPasswordChangeException(String msg) {
        super(msg);
    }
}
