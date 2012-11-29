package com.sourcen.space.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sourcen.space.dao.ProductDAO;
import com.sourcen.space.model.Product;
import com.sourcen.space.service.ProductManager;

public class DefaultProductManager implements ProductManager {

	private ProductDAO productDAO = null;

	public List<Product> getProductList(int pid) {
		return productDAO.getProductList(pid);
	}

	public void init() {

	}

	public boolean isEnabled() {
		return true;
	}

	public void restart() {

	}

	public void start() {
		// TODO Auto-generated method stub

	}

	public void stop() {

	}

	public ProductDAO getProductDAO() {
		return productDAO;
	}

	public void setProductDAO(ProductDAO productDAO) {
		this.productDAO = productDAO;
	}

	public String getProductListAsXML() {

		String productListXml = "";

		List products = getProductList(0);
		while (products.size() > 0)
			return genProductListXML(productListXml);

		return productListXml;

	}

	private String genProductListXML(String xml) {
		return xml;

	}

	public void saveProduct(Product product) {
		this.productDAO.saveProduct(product);
	}

	public String getFeatureListAsXML() {
		// TODO Auto-generated method stub
		return null;
	}

	public void saveFeatureXML(File featureXML) {
		try {
			Document doc = parserXML(featureXML);

			visit(doc, 0, false);
		} catch (Exception error) {
			error.printStackTrace();
		}
	}

	public void saveProductXML(File productXML) {
		try {
			Document doc = parserXML(productXML);

			visit(doc, 0, true);
		} catch (Exception error) {
			error.printStackTrace();
		}

	}

	private Document parserXML(File file) throws SAXException, IOException,
			ParserConfigurationException {
		return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
				file);
	}

	private void visit(Node node, int level, boolean isProduct) {
		NodeList nl = node.getChildNodes();
		NamedNodeMap temp2 = node.getAttributes();

		for (int i = 0, cnt = nl.getLength(); i < cnt; i++) {
			NamedNodeMap temp1 = nl.item(i).getAttributes();
			if (temp1 != null && temp2 != null) {
				Product product = new Product();
				product.setId(Integer.parseInt(temp1.getNamedItem("id").getNodeValue()));
				product.setName(temp1.getNamedItem("label").getNodeValue());
				product.setPid(Integer.parseInt(temp2.getNamedItem("id").getNodeValue()));

				if (isProduct)
					this.saveProduct(product);
				else
					this.saveFeature(product);
			}

			visit(nl.item(i), level + 1, isProduct);
		}
	}

	public void saveFeature(Product product) {
		this.productDAO.saveFeature(product);

	}

}
