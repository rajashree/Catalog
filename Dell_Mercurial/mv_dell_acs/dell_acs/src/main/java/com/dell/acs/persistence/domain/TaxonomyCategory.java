/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */
package com.dell.acs.persistence.domain;

import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;
import com.sourcen.core.util.beans.Scope;
import com.sourcen.core.util.beans.Scopes;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Collection;

/**
 * @author Adarsh.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2929 $, $Date:: 2012-06-05 23:25:24#$
 */

//sfisk - CS-380
@Table(name = "t_taxonomy_category")
@Entity
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Scopes({
        @Scope(name = "default", fields = {"id", "name", "position", "parent.id"}),
        @Scope(name = "minimal", fields = {"id", "name", "position"})
})
public class TaxonomyCategory extends IdentifiableEntityModel<Long> {


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH, optional = true)
    @Fetch(FetchMode.SELECT)
    @Scope(name = "id")
    private Taxonomy taxonomy;

    @Column(length = 255, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = TaxonomyCategory.class, optional = true, cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @Scope(name = "id")
    private TaxonomyCategory parent;

    @Column(length = 255, nullable = true)
    private Integer depth;

    @Column(length = 255, nullable = false)
    private Integer leftNode;

    @Column(length = 255, nullable = false)
    private Integer rightNode;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "parent")
    Collection<TaxonomyCategory> children;


    @Column(length = 255, nullable = false)
    private Integer position;

    public TaxonomyCategory() {
    }

    public TaxonomyCategory(String name) {
        this.name = name;
        this.leftNode = 1;
        this.rightNode = 2;
        depth = 0;
        position = 0;
    }


    public TaxonomyCategory(final Taxonomy taxonomy, final String name, final TaxonomyCategory parent, final int leftNode, final int rightNode) {
        this.taxonomy = taxonomy;
        this.name = name;
        this.parent = parent;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
        depth = 0;
        position = 0;
    }

    public Taxonomy getTaxonomy() {
        return taxonomy;
    }

    public void setTaxonomy(Taxonomy taxonomy) {
        this.taxonomy = taxonomy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaxonomyCategory getParent() {
        return parent;
    }

    public void setParent(TaxonomyCategory parent) {
        this.parent = parent;
        this.depth = parent.getDepth() + 1;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public Integer getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(Integer leftNode) {
        this.leftNode = leftNode;
    }

    public Integer getRightNode() {
        return rightNode;
    }

    public void setRightNode(Integer rightNode) {
        this.rightNode = rightNode;
    }

    public Collection<TaxonomyCategory> getChildren() {
        return children;
    }

    public void setChildren(final Collection<TaxonomyCategory> children) {
        this.children = children;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}
