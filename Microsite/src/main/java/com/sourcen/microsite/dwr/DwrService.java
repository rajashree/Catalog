package com.sourcen.microsite.dwr;


public interface DwrService {

	
	public void addPageBlock(int pid, int bid, int pos);
	
	public void updateSiteData(int sid, int pid, String lid, String data);
	
}
