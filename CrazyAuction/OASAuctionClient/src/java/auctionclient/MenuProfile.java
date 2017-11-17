/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auctionclient;

import entity.Customer;
import java.util.Scanner;

/**
 *
 * @author alex_zy
 */
public class MenuProfile {

    private Customer customer;

    public MenuProfile(Customer customer) {
        this.customer = customer;
    }

    public void run() {
        Integer response = 0;
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n*** Auction Client :: Profile Menu***\n");
            System.out.printf("You are login as %s\n", customer.getUsername());
            System.out.println("1: view my profile");
            System.out.println("2: update profile");
            System.out.println("3: Logout\n");
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");

                response = sc.nextInt();

                if (response == 1) {
                    doViewProfile();
                } else if (response == 2) {
                    doUpdateProfile();
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 3) {
                break;
            }
        }
    }

    private void doViewProfile() {
        System.out.println("\n*** Auction Client :: Profile Menu:: View Profile***\n");
        System.out.printf("first name: %s%n", customer.getFirstName());
        System.out.printf("last name: %s%n", customer.getLastName());
        System.out.printf("identification number: %s%n", customer.getIdentificationNumber());
        System.out.printf("username: %s%n", customer.getUsername());
        System.out.printf("credit balance: %s%n", customer.getCreditBalance());
        System.out.printf("password: %s%n", customer.getPassword());

    }

    private void doUpdateProfile() {
        Integer response = 0;
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n*** Auction Client :: Profile Menu :: Update Profile***\n");
            System.out.printf("You are login as %s\n", customer.getUsername());
            System.out.println("1: first name");
            System.out.println("2: last name");
            System.out.println("3: identification number");
            System.out.println("4: username");
            System.out.println("5: password");

            System.out.println("6: Logout\n");
            response = 0;

            while (response < 1 || response > 6) {
                System.out.print("> ");

                response = sc.nextInt();

                if (response == 1) {
                    doViewProfile();
                } else if (response == 2) {

                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 3) {
                break;
            }

        }
    }

}
