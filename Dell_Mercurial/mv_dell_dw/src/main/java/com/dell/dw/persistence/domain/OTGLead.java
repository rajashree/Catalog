package com.dell.dw.persistence.domain;

import com.sourcen.core.persistence.domain.IdentifiableEntity;
import com.sourcen.core.persistence.domain.impl.jpa.EntityModel;
import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 6/26/12
 * Time: 11:41 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "otg_leads")
public class OTGLead extends EntityModel implements IdentifiableEntity<Long> {

    @Id
    @Column(insertable = true, nullable = false, unique = true, updatable = true)
    private Long id;

    @Column
    private String name;

    @Column
    private String companyName;

    @Column
    private String companySize;

    @Column
    private String email;

    @Column
    private String phone;

    @Column
    private String zip;

    @Column
    private String interest;

    @Column
    private String contactMe;

    @Column
    private String idea;

    @Column
    private String emailMe;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanySize() {
        return companySize;
    }

    public void setCompanySize(String companySize) {
        this.companySize = companySize;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getContactMe() {
        return contactMe;
    }

    public void setContactMe(String contactMe) {
        this.contactMe = contactMe;
    }

    public String getIdea() {
        return idea;
    }

    public void setIdea(String idea) {
        this.idea = idea;
    }

    public String getEmailMe() {
        return emailMe;
    }

    public void setEmailMe(String emailMe) {
        this.emailMe = emailMe;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
