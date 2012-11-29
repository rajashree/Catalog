/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */


package com.sourcen.core.util.collections;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public final class Tree<T> {

    public static <T> TreeNode<T> getLinearTreeNode(final List<T> list) {
        final int size = list.size();
        final TreeNode<T> root = new TreeNode<T>();
        if (size < 1) {
            return root;
        }
        root.setData(list.get(0));
        TreeNode<T> parent = root;
        for (int i = 1; i < size; i++) {
            final TreeNode<T> node = new TreeNode<T>(parent, list.get(i));
            parent.addChild(node);
            // refresh the parent to this current Node, hence the next element will be a child of this node.
            parent = node;
        }
        return root;
    }

    private TreeNode<T> rootElement;

    public Tree() {
        super();
    }

    public TreeNode<T> getRootElement() {
        return this.rootElement;
    }

    public void setRootElement(final TreeNode<T> rootElement) {
        this.rootElement = rootElement;
    }

    public List<TreeNode<T>> toList() {
        final List<TreeNode<T>> list = new ArrayList<TreeNode<T>>();
        walkTree(this.rootElement, list);
        return list;
    }

    @Override
    public String toString() {
        return "Tree{" + "rootElement=" + this.rootElement + '}';
    }

    /**
     * walk the tree to return a list of elements in the order inserted.
     *
     * @param element
     * @param list
     */
    private void walkTree(final TreeNode<T> element, final List<TreeNode<T>> list) {
        list.add(element);
        for (final TreeNode<T> data : element.getChildren()) {
            walkTree(data, list);
        }
    }
}
