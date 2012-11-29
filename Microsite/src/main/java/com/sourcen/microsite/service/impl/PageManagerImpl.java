package com.sourcen.microsite.service.impl;

import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.sourcen.microsite.dao.PageDAO;
import com.sourcen.microsite.model.Block;
import com.sourcen.microsite.model.Page;
import com.sourcen.microsite.service.PageManager;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.StringTemplateLoader;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class PageManagerImpl implements PageManager {

	private PageDAO pageDAO = null;
	private Configuration configuration;

	
	private String macro="<#macro label lid default=''><div class=editable id=\"${lid}\"> ${lid} </div></#macro>";

	public void init() {

		configuration = new Configuration();
		configuration.setLocalizedLookup(false);
	
	}


	public void createPage(Page page) {

		pageDAO.createPage(page);
	}

	public void removePage(String name) {
		pageDAO.removePage(name);

	}

	public void updatePage(Page page) {

		pageDAO.updatePage(page);
	}

	public PageDAO getPageDAO() {
		return pageDAO;
	}

	public void setPageDAO(PageDAO pageDAO) {
		this.pageDAO = pageDAO;
	}

	public Page getPage(String name) {
		return pageDAO.getPage(name);
	}

	public List<Page> listAllPages() {
		
		return this.pageDAO.listAllPages();
	}

	public List<Page> getThemePages(int tid) {
		
		return pageDAO.getThemePages(tid);
	}

	public String joinBlocks(int pid,Object properties ) {
		
		
		String pageHtml="";
			
		List<Block> blocks= pageDAO.getPageBlocks(pid);
		Iterator<Block> blocksIt=blocks.iterator();
		while(blocksIt.hasNext()){
			  Block block=(Block) blocksIt.next();	
			  if(block.isDynamic())
				  pageHtml=pageHtml+this.processBlock(block, properties).getContent();
			  else
				  pageHtml=pageHtml+block.getContent();
			}
		
		 return pageHtml;
	}
	
	

	public Block processBlock(Block block, Object hash) {
		Template template= null;
		StringWriter out = null;
		try {
			StringTemplateLoader stringLoader = new StringTemplateLoader();			
			stringLoader.putTemplate(block.getName(), block.getContent()+macro);
			
			configuration.setTemplateLoader(stringLoader);
			
			template = configuration.getTemplate(block.getName());
			
			
			out = new StringWriter();
			template.process(hash, out);

		} catch (Exception e) {

			e.printStackTrace();
		}

		String content = out.toString();
		block.setContent(content);

		return block;
	}
	
	public String decoratePage( Object hash) {
		Template template= null;
		StringWriter out = null;
		try {
			String sitePath = ServletActionContext.getServletContext().getRealPath("theme")+ System.getProperty("file.separator")+"default" ;
			File file = new File(sitePath);
			FileTemplateLoader stringLoader = new FileTemplateLoader(file);			
			
			configuration.setTemplateLoader(stringLoader);
			template = configuration.getTemplate("decorate.ftl");
			
			
			out = new StringWriter();
			template.process(hash, out);

		} catch (Exception e) {

			e.printStackTrace();
		}

		String content = out.toString();
		

		return content;
	}
	public void addPageblock(int pid, int bid,int pos) {
		
		if( pageDAO.updatePageblock(pid,bid,pos) <= 0)
		 pageDAO.addPageblock(pid,bid,pos);
		
	}

	public void removePageBlocks(int pid) {
		
		 pageDAO.removePageBlocks(pid);
	}


	public Page getPageById(int pid) {
		// TODO Auto-generated method stub
		return pageDAO.getPageById(pid);
	}


	
	public void updateUserSitePage(int sid, int pid, String content,String created,String modified) {
		
	  	
		if(pageDAO.updateUserSitePage(sid,pid, content,modified) <=0)
			pageDAO.saveUserSitePage(sid,pid, content,created,modified);
	}
    public void cleanUserSitePages(String sid) {
			  	
		pageDAO.cleanUserSitePages(sid);
			
	}


	public Page getUserSavedPage(int sid, int pid,
			HashMap<String, Object> properties) {
      String content="";

		Page temp= (Page) pageDAO.getUserSavedPage(sid,pid);
		 
		if(temp != null)
			content = temp.getContent();
		
		else	{		
	
			temp= new Page();
			temp.setId(pid);
			content = joinBlocks(pid, properties);
			
		}
		 ((HashMap)properties).put("htmlBody", content);
			
		 content=decoratePage(properties);
		 
		 temp.setContent(content);
		return temp;
	}


	
}
