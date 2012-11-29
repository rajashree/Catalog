package com.sourcen.space.service;

import com.sourcen.space.model.EmailMessage;

public interface EmailManager extends ServiceManager{

	
	 void send(EmailMessage message);
	
}
