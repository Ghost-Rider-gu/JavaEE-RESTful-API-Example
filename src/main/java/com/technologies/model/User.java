package com.technologies.model;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * User model.
 */
@Data
@Entity
@Table(name = "user")
@XmlRootElement(name = "User")
public class User implements Serializable {

    private static final Long serialVersionUid = 7244957405053753908L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(name = "name")
    private String userName;

    @Column(name = "phone")
    private String userPhone;

    @Column(name = "email")
    private String userEmail;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Account> userAccount = new HashSet<>();

}
