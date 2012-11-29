/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */


package com.sourcen.core.util.collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class TreeNode<T> {

    private T data;

    private List<TreeNode<T>> children;

    private TreeNode<T> parent = null;

    private Map<String, Object> properties;

    public TreeNode() {
    }

    public TreeNode(final T data) {
        this.data = data;
    }

    public TreeNode(final TreeNode<T> parent, final T data) {
        this.parent = parent;
        this.data = data;
    }

    public List<TreeNode<T>> getChildren() {
        if (this.children == null) {
            this.children = new ArrayList<TreeNode<T>>();
        }
        return this.children;
    }

    public void setChildren(final List<TreeNode<T>> children) {
        this.children = children;
    }

    public int getNumChildren() {
        if (this.children == null) {
            return 0;
        }
        return this.children.size();
    }

    public void addChild(final TreeNode<T> child) {
        getChildren().add(child);
    }

    public void insertChildAt(final int index, final TreeNode<T> child) throws IndexOutOfBoundsException {
        if (getNumChildren() == index) {// can happen only when its the last element.
            addChild(child);
            return;
        }
        // try to get the index first, if we don't have any element then throw exception there.
        this.children.get(index);
        this.children.add(index, child);
    }

    // this will throw an IndexOutOfBoundsException
    public TreeNode<T> getChildAt(final int index) {
        return getChildren().get(index);
    }

    public void removeChildAt(final int index) throws IndexOutOfBoundsException {
        this.children.remove(index);
    }

    public T getData() {
        return this.data;
    }

    public void setData(final T data) {
        this.data = data;
    }

    public TreeNode<T> getParent() {
        return this.parent;
    }

    public void setParent(final TreeNode<T> parent) {
        this.parent = parent;
    }

    public Map<String, Object> getProperties() {
        if (this.properties == null) {
            this.properties = new HashMap<String, Object>(5);
        }
        return this.properties;
    }

    @Override
    public String toString() {
        return "TreeNode{" + "data=" + this.data + ", children=" + this.children + ", parent=" + this.parent + ", properties=" + this.properties + ", numChildren="
                + getNumChildren() + '}';
    }
}
