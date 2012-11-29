package com.dell.dw.persistence.domain;

import com.sourcen.core.persistence.domain.IdentifiableEntity;
import com.sourcen.core.persistence.domain.constructs.StatusAware;
import com.sourcen.core.persistence.domain.constructs.ThreadLockAware;
import com.sourcen.core.persistence.domain.impl.jpa.EntityModel;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy$
 * @version $Revision$, $Date::                     $
 */
@Entity
@Table(name = "ga_webproperty_profile")
public class GAWebPropertyProfile extends EntityModel implements IdentifiableEntity<Long>, StatusAware<Integer>, ThreadLockAware {

    @Id
    @Column(insertable = true, nullable = false, unique = true, updatable = true)
    private Long id;

    @ManyToOne
    private GAWebProperty gaWebProperty;

    @Column
    private String name;

    @Column
    private Date initializationDate;

    @Column
    private Date lastDownloadedDate;

    @Column
    private Integer status;

    @Column
    private String lockedThread;

    @Column
    private String timezone;

    public GAWebPropertyProfile() {
    }

    public GAWebPropertyProfile(Long id, GAWebProperty gaWebProperty, String name,
                                Date initializationDate, Date lastDownloadedDate,
                                int status, String timezone) {
        this.setId(id);
        this.gaWebProperty = gaWebProperty;
        this.name = name;
        this.initializationDate = initializationDate;
        this.lastDownloadedDate = lastDownloadedDate;
        this.status = status;
        this.timezone = timezone;
    }

    public GAWebProperty getGaWebProperty() {
        return gaWebProperty;
    }

    public void setGaWebProperty(final GAWebProperty gaWebProperty) {
        this.gaWebProperty = gaWebProperty;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Date getInitializationDate() {
        return initializationDate;
    }

    public void setInitializationDate(final Date initializationDate) {
        this.initializationDate = initializationDate;
    }

    public Date getLastDownloadedDate() {
        return lastDownloadedDate;
    }

    public void setLastDownloadedDate(final Date lastDownloadedDate) {
        this.lastDownloadedDate = lastDownloadedDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(final Integer status) {
        this.status = status;
    }

    public String getLockedThread() {
        return lockedThread;
    }

    public void setLockedThread(final String lockedThread) {
        this.lockedThread = lockedThread;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public static final class Status {
        public static final Integer IN_QUEUE = 0;
        public static final Integer PROCESSING = 1;
        public static final Integer DONE = 2;
        public static final Integer ERROR_READ = 3;
        public static final Integer ERROR_EXTRACTING = 4;
        public static final Integer ERROR_PARSING = 5;
        public static final Integer AUTHENCATION_FAILURE = 6;
    }
}
