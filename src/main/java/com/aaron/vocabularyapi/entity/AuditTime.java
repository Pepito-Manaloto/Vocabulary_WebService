package com.aaron.vocabularyapi.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Embeddable
public class AuditTime
{
    @Column(name = "datein")
    private LocalDateTime dateIn;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @PrePersist
    public void prePersist()
    {
        dateIn = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate()
    {
        lastUpdated = LocalDateTime.now();
    }
}
