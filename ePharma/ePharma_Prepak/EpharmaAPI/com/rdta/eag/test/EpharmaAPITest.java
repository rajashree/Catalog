/*
 * Copyright (c) by RainingData Corporation.
 * 560 S.Winchester Blvd, San Jose,CA 95128 - U.S.A
 * -----------------------------------------------------------
 * 
 * Created on Apr 21, 2006
 * Date: Apr 21, 2006
 *
 */

package com.rdta.eag.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.rdta.eag.commons.xml.XMLUtil;
import com.rdta.eag.api.EpharmaAPI;

import junit.framework.TestCase;

/**
 * @author sanumolu
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EpharmaAPITest extends TestCase {
    EpharmaAPI ep=null;
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        try {
        ep = new EpharmaAPI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetSession() {
        String result = ep.getSession("Sub123","1234","bob111","bob1111");
        System.out.println("The result of getSession is: " + result);
    }

    public void testSubscribeTradingPartner() {
        System.out.println("In testSubscribeTradingPartner");
        String outMsg = ep.subscribeTradingPartner(
                "Sub1234","12345", "", "Distributor", "John Doe",
                "Manager", "123 Main St. San Jose, CA 95123", "408-123-1234",
                "jdoe@test.com", "comments comments comments");
        System.out.println("Result---------------" + outMsg);
    }
    

    public void testSubmitPublicKey() {
        try {
        System.out.println("In testSubscribeTradingPartner");
        String outMsg = ep.submitPublicKey("Sub1234","12345","key", "Distributor");
        System.out.println("Result---------------" + outMsg);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void testSubmitPedigreeEnvelope() {
        try {
            File str = new File("C:/signatureFile1.xml");
            Node nd = XMLUtil.parse(str);
            String xml = XMLUtil.convertToString(nd,true);
            String result = ep.submitPedigreeEnvelope(
                    "11d1def534ea1be0:1977b9b:10adc477c99:-80001146174957970",xml);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

   /* public void testPedIn() {
    }

    public void testPedRcv() {
    }

    public void testPedBadRcv() {
    }

    public void testShipGoods() {
    }

    public void testCreateSignature() {
        try {
            String str = new String("<PedigreeEnvelope><version>1</version><serialNumber>PE0004</serialNumber><date>2006-04-07T10:27:35.406Z</date><pedigree><shippedPedigree id='ID0009'><documentInfo><serialNumber>ID0009</serialNumber><version>1</version></documentInfo><repackagedPedigree><previousProducts><previousProductInfo><manufacturer>Watson Pharmaceuticals</manufacturer><productCode>00591-5818-01</productCode></previousProductInfo><itemInfo><lot>WAT1001</lot><quantity>100</quantity></itemInfo><contactInfo><name>watson user1</name></contactInfo></previousProducts><productInfo><drugName>Naproxen</drugName><manufacturer>Watson</manufacturer><productCode>58016-289-00</productCode><dosageForm>Oral</dosageForm><strength>500mg</strength><containerSize>bottle</containerSize></productInfo><itemInfo><lot>SWD001</lot><quantity>50</quantity></itemInfo></repackagedPedigree><transactionInfo><senderInfo><businessAddress><businessName>Southwood Pharmaceuticals</businessName><street1>60 Empire Drive</street1><street2/><city>Lake Forest</city><stateOrRegion>CA</stateOrRegion><postalCode>92630</postalCode><country>USA</country></businessAddress><shippingAddress><businessName>Southwood Pharmaceuticals</businessName><street1>60 Empire Drive</street1><street2/><city>Lake Forest</city><stateOrRegion>CA</stateOrRegion><postalCode>92630</postalCode><country>USA</country></shippingAddress><licenseNumber state='FL' agency=''>SWD-198453</licenseNumber><contactInfo><name>swd user1</name></contactInfo></senderInfo><recipientInfo><businessAddress><businessName>St. John's Hospital</businessName><street1>5001 St. Johns Avenue</street1><street2/><city>Palatka</city><stateOrRegion>FL</stateOrRegion><postalCode>32177</postalCode><country>USA</country></businessAddress><shippingAddress><businessName>St. John's Hospital</businessName><street1>5001 St. Johns Avenue</street1><street2/><city>Palatka</city><stateOrRegion>FL</stateOrRegion><postalCode>32177</postalCode><country>USA</country></shippingAddress><licenseNumber state='FL' agency=''>SWD-198453</licenseNumber><contactInfo><name>swd user1</name></contactInfo></recipientInfo><transactionIdentifier><identifier>C28785100776</identifier><identifierType>DespatchAdvice</identifierType></transactionIdentifier><transactionType>Sale</transactionType></transactionInfo><signatureInfo><signerInfo><name>Mike</name></signerInfo><signatureDate>2006-04-06T10:27:35.406Z</signatureDate><signatureMeaning>Certified</signatureMeaning></signatureInfo></shippedPedigree></pedigree></PedigreeEnvelope>");
            Node nd = XMLUtil.parse(str);
            String result = ep.createSignature(nd,"C:/security/keys/RDTA_keystore","jasmine23","RDTAClient");
            System.out.println("Signaturee:----" + result);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void testVerifySignature() {
        try {
            //String str = new String("C:/signatureFile1.xml");
            File str = new File("C:/signatureFile1.xml");
            Node nd = XMLUtil.parse(str);
            //String xml = XMLUtil.convertToString(nd,true);
            String result = ep.verifySignature(nd);
            System.out.println("Resultttttt " + result);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void testLogOut() {
        try {
            String str = ep.logOut("11d1def534ea1be0:1977b9b:10adc477c99:-80001146257814901");
            System.out.println("STRRRR " + str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

}
