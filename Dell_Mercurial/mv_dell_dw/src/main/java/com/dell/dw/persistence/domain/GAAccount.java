package com.dell.dw.persistence.domain;


import com.sourcen.core.persistence.domain.IdentifiableEntity;
import com.sourcen.core.persistence.domain.impl.jpa.EntityModel;

import javax.persistence.*;
import java.util.Collection;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy$
 * @version $Revision$, $Date::                     $
 */
@Entity
@Table(name = "ga_account")
public class GAAccount  extends EntityModel implements IdentifiableEntity<Long> {

    @Id
    @Column(insertable = true, nullable = false, unique = true, updatable = true)
    private Long id;

    @ManyToOne
    private Retailer retailer;

    @Column
    private String name;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "gaAccount")
    private Collection<GAWebProperty> gaWebProperties;

    @Column
    private String email;

    public GAAccount() {
    }

    public GAAccount(Long id,  String accountName, Retailer retailer) {
        this.setId(id);
        this.name = accountName;
        this.retailer = retailer;
    }

    public Retailer getRetailer() {
        return retailer;
    }

    public void setRetailer(Retailer retailer) {
        this.retailer = retailer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<GAWebProperty> getGaWebProperties() {
        return gaWebProperties;
    }

    public void setGaWebProperties(Collection<GAWebProperty> gaWebProperties) {
        this.gaWebProperties = gaWebProperties;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
