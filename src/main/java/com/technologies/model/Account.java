package com.technologies.model;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Account model.
 */
@Data
@Entity
@Table(name = "account")
@XmlRootElement(name = "Account")
public class Account implements Serializable {

    private static final Long serialVersionUid = 23487982311093L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long accountId;

    @Column(name = "number")
    private String accountNumber;

    @Column(name = "balance")
    private BigDecimal accountBalance;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

}