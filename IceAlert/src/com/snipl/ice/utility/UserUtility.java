package com.snipl.ice.utility;
/**
* @Author Kamalakar Challa & Sankara Rao & Chethan Jayraj
*   
*/
import java.sql.SQLException;
import com.mysql.jdbc.ResultSet;

import java.util.HashMap;
import java.util.Random;

import com.snipl.ice.security.Dao;

public class UserUtility {

	private static final long serialVersionUID = 1L;

	Dao d;
	public HashMap getUserDetails(int id) {
		ResultSet rs = null;
		HashMap<String, String> hm = new HashMap<String, String>();

		try {
			d = new Dao();
			rs = d.executeQuery("select * from user_details where id=" + id);
			if (rs.next()) {
				hm.put("fname", rs.getString("F_Name"));
				hm.put("lname", rs.getString("L_Name"));
				hm.put("email", rs.getString("Email"));
				hm.put("extn", rs.getString("MobileExt"));
				hm.put("mobile", rs.getString("Mobile"));
				hm.put("provider", rs.getString("S_Provider"));
				hm.put("country", rs.getString("Country"));
				hm.put("zip", rs.getString("Zip_Code"));
				hm.put("code", rs.getString("Code"));
				hm.put("flag", rs.getString("Flag"));
				hm.put("Occupation", rs.getString("Occupation"));
				hm.put("street", rs.getString("Street"));
				hm.put("city", rs.getString("City"));
				hm.put("state", rs.getString("State"));
				hm.put("area", rs.getString("Area"));
				hm.put("phextn",rs.getString("PhoneExt"));
				String date = rs.getString("Dob");
				String year = date.substring(0, 4);
				String month =date.substring(5, 7);
				String day = date.substring(8, 10);
				
				hm.put("year", year);
				hm.put("month", month);
				hm.put("day", day);
				hm.put("phone", rs.getString("Phone"));
				hm.put("eoccupation", rs.getString("E_Occupation"));
				hm.put("bloodgroup", rs.getString("BloodGroup"));
				hm.put("allergies", rs.getString("Allergies"));
				hm.put("meds", rs.getString("Meds"));
				hm.put("disease", rs.getString("Disease"));
				hm.put("condition", rs.getString("Conditions"));
				
			}
		} catch (SQLException e) {
			System.out.println("SqlException caught" + e.getMessage());
		} finally {
			d.close();
		}
		return hm;

	}
	public String encrypt(String str) {
		byte k;
		do {
			Random r = new Random();
			k = (byte) (r.nextGaussian() * 10);
			k = (byte) (k % 10);
			if (k < 0)
				k = (byte) -k;
		} while (k == 0 || k == 1);
		byte[] b1 = str.getBytes();
		byte[] b2 = new byte[b1.length];
		byte[] b3 = new byte[b1.length];
		for (int i = 0; i < b1.length; i++) {
			b2[i] = (byte) (b1[i] / k);
			b3[i] = (byte) (b1[i] % k);
		}
		String s2 = new String(b2);
		String s3 = new String(b3);
		String s4 = (char) k + s2 + s3;
		return s4;
	}

	public String decrypt(String s4) {
		byte[] temp = s4.getBytes();
		byte[] original = new byte[(temp.length - 1) / 2];
		for (int l = 0; l < (temp.length - 1) / 2; l++) {
			original[l] = (byte) (temp[l + 1] * temp[0] + temp[1 + l
					+ (temp.length - 1) / 2]);
		}
		String str = new String(original);
		return str;

	}

	

	public String getUserEmail(int id) {
		ResultSet rs = null;
		String usermail = null;
		try {
			d = new Dao();
			rs = d.executeQuery("select * from user_details where id=" + id);
			if (rs.next()) {
				usermail = rs.getString("Email");
			}
		} catch (SQLException e) {
			System.out.println("SqlException caught" + e.getMessage());
		} finally {
			d.close();
		}
		return usermail;

	}
	
	public int getIDbyEmailID(String email){
        ResultSet rs=null;
        int ret=0;
        try
		{
        	d=new Dao();
            rs=d.executeQuery("select * from user_details where Email='"+email+"'");
            if(rs.next())
            {
            	ret=rs.getInt("id");
            }
		}
        catch(SQLException e)
        {
        	System.out.println("SqlException caught"+e.getMessage());
        }
        finally
        {
	        d.close();
        } 
        return ret;
        
	}
	
	public String getUserName(int id) {
		ResultSet rs = null;
		String user = null;
		try {
			d = new Dao();
			rs = d.executeQuery("select * from user_details where id=" + id);
			if (rs.next()) {
				user = rs.getString("F_Name")+" "+rs.getString("L_Name");
			}
		} catch (SQLException e) {
			System.out.println("SqlException caught" + e.getMessage());
		} finally {
			d.close();
		}
		return user;

	}
	
	
	
	
}
