package com.aaron.vocabularyapi.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

// https://vladmihalcea.com/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/
// https://vladmihalcea.com/14-high-performance-java-persistence-tips/
@Data
@NoArgsConstructor
@Entity
@Table(name = "vocabulary")
public class Vocabulary implements Serializable
{
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    // name points to the column name of this table, referencedColumnName points to the column name of the foreign table
    @JoinColumn(name = "foreign_id", referencedColumnName = "id")
    private ForeignLanguage foreignLanguage;

    @JsonProperty("english_word")
    @Column(name = "english_word")
    private String englishWord;

    @JsonProperty("foreign_word")
    @Column(name = "foreign_word")
    private String foreignWord;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Embedded
    private AuditTime auditTime = new AuditTime();
}
