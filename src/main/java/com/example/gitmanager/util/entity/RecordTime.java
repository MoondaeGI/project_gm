package com.example.gitmanager.util.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class RecordTime {
    @CreationTimestamp
    @Column(name = "REG_DATE", nullable = false, updatable = false)
    private LocalDateTime regDate;

    @UpdateTimestamp
    @Column(name = "MOD_DATE", nullable = false)
    private LocalDateTime modDate;
}
