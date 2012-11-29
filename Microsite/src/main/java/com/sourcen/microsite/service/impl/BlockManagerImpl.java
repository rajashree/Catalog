package com.sourcen.microsite.service.impl;

import java.util.List;
import java.util.regex.Pattern;

import com.sourcen.microsite.dao.BlockDAO;
import com.sourcen.microsite.model.Block;
import com.sourcen.microsite.service.ApplicationManager;
import com.sourcen.microsite.service.BlockManager;

import org.htmlparser.Attribute;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;

public class BlockManagerImpl implements BlockManager {

	private BlockDAO blockDAO = null;

	private Parser parser = null;
	private ApplicationManager applicationManager = null;

	public void init() {

		parser = new Parser();

	}

	public void createBlock(Block block) {
	
		blockDAO.createBlock(block);
	}

	public void removeBlock(String name) {
		blockDAO.removeBlock(name);

	}

	public void updateBlock(Block block) {
		
		blockDAO.updateBlock(block);
	}

	public BlockDAO getBlockDAO() {
		return blockDAO;
	}

	public void setBlockDAO(BlockDAO blockDAO) {
		this.blockDAO = blockDAO;
	}

	public Block getBlock(String name) {
		Block block = blockDAO.getBlock(name);

		return block;
	}

	public List<Block> listAllBlocks() {
		// TODO Auto-generated method stub
		return this.blockDAO.listAllBlocks();
	}

	protected String parseContent(String beforeParse) {
		NodeList list = null;
		try {
			this.parser.setInputHTML(beforeParse);
			list = parser.parse(null);

		} catch (ParserException e) {

			e.printStackTrace();
		}
		list = traverse(list);
		beforeParse = list.toHtml();

		return beforeParse;

	}

	public NodeList traverse(NodeList lis) {

		NodeList newNodeList = new NodeList();
		SimpleNodeIterator it = lis.elements();
		while (it.hasMoreNodes()) {

			Node node = it.nextNode();

			if(Pattern.matches("^div[\\sa-zA-Z]*class=[\"]*editable[\"\\s]*",node.getText().toLowerCase()))
			    
			{
				TagNode tagNode = (TagNode) node;
				tagNode.setAttribute(new Attribute("id", "\""
						+ applicationManager.getStringToken() + "\""));
			
				newNodeList.add(tagNode);

			} else {

				if (node.getChildren() != null) {

					node.setChildren(traverse(node.getChildren()));

				}
				newNodeList.add(node);
			}

		}
		return newNodeList;
	}

	public ApplicationManager getApplicationManager() {
		return applicationManager;
	}

	public void setApplicationManager(ApplicationManager applicationManager) {
		this.applicationManager = applicationManager;
	}

}
