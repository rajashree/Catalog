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
@Table(name = "ga_webproperty")
public class GAWebProperty extends EntityModel implements IdentifiableEntity<String> {


    @Id
    @Column(insertable = true, nullable = false, unique = true, updatable = true)
    private String id;

    @ManyToOne
    private GAAccount gaAccount;

    @Column
    private String propertyName; // customerName..

    @Column
    private Boolean active;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "gaWebProperty")
    private Collection<GAWebPropertyProfile> gaWebPropertyProfiles;


    public GAWebProperty() {
    }

    public GAWebProperty(String id, GAAccount gaAccount, String propertyName, Boolean active) {
        this.id = id;
        this.gaAccount = gaAccount;
        this.propertyName = propertyName;
        this.active = active;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(final String id) {
        this.id = id;
    }

    public GAAccount getGaAccount() {
        return gaAccount;
    }

    public void setGaAccount(final GAAccount gaAccount) {
        this.gaAccount = gaAccount;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(final String propertyName) {
        this.propertyName = propertyName;
    }

    public Collection<GAWebPropertyProfile> getGaWebPropertyProfiles() {
        return gaWebPropertyProfiles;
    }

    public void setGaWebPropertyProfiles(final Collection<GAWebPropertyProfile> gaWebPropertyProfiles) {
        this.gaWebPropertyProfiles = gaWebPropertyProfiles;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(final Boolean active) {
        this.active = active;
    }
}
