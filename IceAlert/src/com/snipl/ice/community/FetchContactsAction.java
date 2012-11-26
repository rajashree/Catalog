package com.snipl.ice.community;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.snipl.ice.utility.contacts.RediffContacts;
import com.snipl.ice.utility.contacts.YahooContacts;
import com.snipl.ice.utility.contacts.gmail.Contacts_gmail;
import com.snipl.ice.utility.contacts.gmail.GMContact;

public class FetchContactsAction extends Action
{
	public ActionForward execute(
									ActionMapping mapping,
									ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response) 
									throws Exception
	{
		if(request.getSession().getAttribute("security_id")!=null)
		{
			if(form!=null)
			{
				List list_al=new ArrayList();
				FetchContactsForm fetchForm=(FetchContactsForm) form;
				if(fetchForm.getMail_providers().equalsIgnoreCase("01"))
				{
					Iterable<GMContact> contacts=new Contacts_gmail().getGmailContacts(fetchForm.getId_uname(),fetchForm.getId_passwd());
					if(contacts!=null)
					{
						for (Iterator iter = contacts.iterator(); iter.hasNext();) {
							GMContact contact = (GMContact) iter.next();
							ContactsBean cbean=new ContactsBean();
							cbean.setContact_email((String)contact.getEmail());
							cbean.setContact_name(contact.getName());
							list_al.add(cbean);
						}
						if(list_al.size()!=0)
						{
							request.setAttribute("Contacts", list_al);
							request.setAttribute("count", list_al.size());
						}
						if(!fetchForm.getPrev_contact().equalsIgnoreCase(""))
							request.setAttribute("previousContacts", fetchForm.getPrev_contact());
						request.setAttribute("Mail", "Gmail");
						return mapping.findForward("success");
					}
					else
					{
						if(!fetchForm.getPrev_contact().equalsIgnoreCase(""))
							request.setAttribute("previousContacts", fetchForm.getPrev_contact());
						request.setAttribute("loginFailed", "true");
						return mapping.findForward("failure");
					}
				}
				else if(fetchForm.getMail_providers().equalsIgnoreCase("02"))
				{
					YahooContacts yahooContacts=new YahooContacts();
					ArrayList al= yahooContacts.getYahooContacts(fetchForm.getId_uname(),fetchForm.getId_passwd());
					if(al!=null)
					{
						ArrayList name=(ArrayList) al.get(0);
						ArrayList email=(ArrayList) al.get(1);
						
						for(int i=0;i<name.size();i++)
						{
							ContactsBean cbean=new ContactsBean();
							cbean.setContact_email((String)email.get(i));
							cbean.setContact_name((String)name.get(i));
							list_al.add(cbean);
						}
						if(list_al.size()!=0)
						{
							request.setAttribute("Contacts", list_al);
							request.setAttribute("count", name.size());
						}
						if(!fetchForm.getPrev_contact().equalsIgnoreCase(""))
							request.setAttribute("previousContacts", fetchForm.getPrev_contact());
						request.setAttribute("Mail", "Yahoo");
						return mapping.findForward("success");
					}
					else
					{
						if(!fetchForm.getPrev_contact().equalsIgnoreCase(""))
							request.setAttribute("previousContacts", fetchForm.getPrev_contact());
						request.setAttribute("loginFailed", "true");
						return mapping.findForward("failure");
					}
				}
				else if(fetchForm.getMail_providers().equalsIgnoreCase("03"))
				{
					RediffContacts rediffContacts=new RediffContacts();
					ArrayList al= rediffContacts.getRediffContacts(fetchForm.getId_uname(),fetchForm.getId_passwd());
					if(al!=null)
					{
						String status=(String) al.get(0);
						if(status.equalsIgnoreCase("0"))
						{
							ArrayList name=(ArrayList) al.get(1);
							ArrayList email=(ArrayList) al.get(2);
							
							for(int i=0;i<name.size();i++)
							{
								ContactsBean cbean=new ContactsBean();
								cbean.setContact_email((String)email.get(i));
								cbean.setContact_name((String)name.get(i));
								list_al.add(cbean);
							}
							if(list_al.size()!=0)
							{
								request.setAttribute("Contacts", list_al);
								request.setAttribute("count", name.size());
							}
							if(!fetchForm.getPrev_contact().equalsIgnoreCase(""))
								request.setAttribute("previousContacts", fetchForm.getPrev_contact());
							request.setAttribute("Mail", "Rediff");
							return mapping.findForward("success");
						}
						else if(status.equalsIgnoreCase("1"))
						{
							if(!fetchForm.getPrev_contact().equalsIgnoreCase(""))
								request.setAttribute("previousContacts", fetchForm.getPrev_contact());
							request.setAttribute("loginFailed", "true");
							return mapping.findForward("failure");
						}
						else if(status.equalsIgnoreCase("2"))
						{
							if(!fetchForm.getPrev_contact().equalsIgnoreCase(""))
								request.setAttribute("previousContacts", fetchForm.getPrev_contact());
							request.setAttribute("Mail", "Rediff");
							return mapping.findForward("success");
						}
					}
					
				}
				
			}
		}
		return mapping.findForward("sessionExpaired_Frame");
		
	}
}
