/*
 * Copyright (c) Dell Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws.v1.beans;

import java.util.Collection;

import com.sourcen.core.util.beans.Scope;
import com.sourcen.core.util.beans.ScopeAware;
import com.sourcen.core.util.beans.Scopes;
import com.sourcen.core.web.ws.beans.base.WSBeanModel;

/**
 * @author Shawn R Fisk
 * @author $LastChangedBy
 * @version $Revision
 */
@Scopes({
        @Scope(name = "minimal", fields = {"header", "products"})
})
public class WSProductSearchResult extends WSBeanModel implements ScopeAware {
	/**
	 * Generated serial id required by java serialization. 
	 */
	private static final long serialVersionUID = -5403296723456428718L;
	
	WSSearchHeader header;
	Collection<WSProduct> products;

    public WSSearchHeader getHeader() {
        return header;
    }

    public void setHeader(WSSearchHeader pHeader) {
        this.header = pHeader;
    }

    public Collection<WSProduct> getProducts() {
        return products;
    }

    public void setProducts(Collection<WSProduct> pProducts) {
        products = pProducts;
    }

    @Override
    public String toString() {
        return "WSProductSearchResult{" +
                "header=" + header +
                ", products=" + products +
                '}';
    }
}
