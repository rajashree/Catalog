/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.java.io;

import java.io.Console;
import java.util.Arrays;

/**
 * @author : Rajashree Meganathan
 * @date : 11/12/12
 */
public class ConsoleEx {
    public static void main(String[] args) {

        //Run from console.. by executing java com.java.io.ConsoleEx
        Console console = System.console();
        if (console == null) {
            System.err.println("No console");
            System.exit(1);
        }
        String login = console.readLine("Enter your login : ");
        char[] oldPassword = console.readPassword("Enter your old password : ");
        if (verify(login, oldPassword)) {
            boolean noMatch;
            do {
                char[] newPassword1 = console.readPassword("Enter your new password: ");
                char[] newPassword2 = console.readPassword("Enter new password again: ");
                noMatch = !Arrays.equals(newPassword1, newPassword2);
                if (noMatch) {
                    console.format("Passwords don't match. Try again.\n");
                } else {
                    change(login, newPassword1);
                    console.format("Password for %s changed.\n", login);
                }
                Arrays.fill(newPassword1, ' ');
                Arrays.fill(newPassword2, ' ');
            } while (noMatch);
        }

        Arrays.fill(oldPassword, ' ');

    }

    // Dummy verify method.
    static boolean verify(String login, char[] password) {
        return true;
    }

    // Dummy change method.
    static void change(String login, char[] password) {
    }

}
