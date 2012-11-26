package com.snipl.ice.utility.contacts.gmail;

import java.util.Iterator;

public class Contacts_gmail {

	/**
	 * @param args
	 */
	public Iterable<GMContact> getGmailContacts(String uname,String passwd)
	{
		Iterable<GMContact> contacts=null;
		GMLoginInfo loginInfo = new GMLoginInfo();
		loginInfo.setUsername(uname);
		loginInfo.setPassword(passwd);
		IClient client=(IClient) new GMClient(loginInfo);
		client = (IClient) new GMClient(loginInfo);
		try {
			boolean status = client.connect();
			if (status)
			{
				contacts= client.getContacts(ContactType.CONTACT_ALL);
				return contacts;
		}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return contacts;
	}

}
