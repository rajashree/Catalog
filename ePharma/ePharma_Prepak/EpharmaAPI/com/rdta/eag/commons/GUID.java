//GUID.java
//
// Generates globally unique identifiers and confimation codes.
// The confirmation code is a random string of 6 upper case letters

package com.rdta.eag.commons;

import java.rmi.server.UID;
import java.net.InetAddress;
import java.math.BigInteger;

/**
 * This class is used to generate globally unique identifers and confirmation
 * codes.
 * 
 * @see java.rmi.server.UID
 */
public class GUID extends Object {
    /**
     * Gets a globally unique identifier by creating a UID and appending the IP
     * address of the local machine.
     * 
     * @return GUID string
     * @see java.rmi.server.UID#toString()
     */
    public String getGUID() {
        // get the uid as a string buffer
        UID uid = new UID();
        StringBuffer strUID = new StringBuffer(uid.toString());

        boolean removedFirst = false;

        for (int i = 0; i < strUID.length(); i++) {
            if (strUID.charAt(i) == ':') {
                if (removedFirst == false) {
                    strUID.deleteCharAt(i);
                    i--;
                    removedFirst = true;
                } else {
                    String counter = strUID.substring(i + 1, strUID.length());
                    long number = 0x8000 + Long.parseLong(counter, 16);

                    strUID.delete(i, strUID.length());
                    strUID.append(Long.toHexString(number));
                    break;
                }
            }
        }

        // append the ip address to the string buffer
        try {
            InetAddress address = InetAddress.getLocalHost();
            BigInteger bi = new BigInteger(address.getAddress());
            bi = bi.abs();
            strUID.append(bi.toString(16));
        }
        // if it fails, just make a random ip address
        catch (Exception e) {
            strUID.append(Long.toString((long) (Math.random() * (256 ^ 4)),
                            16));
        }

        BigInteger decStr = new BigInteger(strUID.toString(), 16);
        decStr = decStr.abs();
        return decStr.toString(10);
    }

    /**
     * Gets a payment confirmation code.
     * 
     * @return Confirmation code, 6 characters, all upper case.
     */
    public String getConfirmation() {
        StringBuffer confirmCode = new StringBuffer("123456");

        for (int i = 0; i < 6; i++) {
            // get random char in the range A-Z
            char randChar = (char) ((int) (Math.random() * 26) + 65);

            // do not allow vowels in the confirmation code
            // ! A, E, I, O, U

            boolean vowel = true;

            while (vowel) {

                if (randChar == (0 + 65) || randChar == (4 + 65)
                        || randChar == (8 + 65) || randChar == (14 + 65)
                        || randChar == (20 + 65)) {

                    randChar = (char) ((int) (Math.random() * 26) + 65);
                }

                else {
                    vowel = false;
                }
            }

            // put it into the string
            confirmCode.setCharAt(i, randChar);
        }

        return confirmCode.toString();
    }

}
