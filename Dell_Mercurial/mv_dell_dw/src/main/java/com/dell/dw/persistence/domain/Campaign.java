package com.dell.dw.persistence.domain;

import com.sourcen.core.persistence.domain.IdentifiableEntity;
import com.sourcen.core.persistence.domain.impl.jpa.EntityModel;

import javax.persistence.*;
import java.util.Collection;


@Entity
@Table(name = "campaign")
public class Campaign  extends EntityModel implements IdentifiableEntity<Long> {

    @Id
    @Column(insertable = true, nullable = false, unique = true, updatable = true)
    private Long id;

    @Column
    private String name;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "campaign")
    Collection<D3Link> d3Links;

    public Campaign() {
    }

    public Campaign(Long id, String name) {
        this.setId(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<D3Link> getD3Links() {
        return d3Links;
    }

    public void setD3Links(Collection<D3Link> d3Links) {
        this.d3Links = d3Links;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }


}
