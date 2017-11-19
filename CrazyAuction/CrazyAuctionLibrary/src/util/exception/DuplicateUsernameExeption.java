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
public class DuplicateUsernameExeption extends Exception {

    /**
     * Creates a new instance of <code>DuplicateUsernameExeption</code> without
     * detail message.
     */
    public DuplicateUsernameExeption() {
    }

    /**
     * Constructs an instance of <code>DuplicateUsernameExeption</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DuplicateUsernameExeption(String msg) {
        super(msg);
    }
}
