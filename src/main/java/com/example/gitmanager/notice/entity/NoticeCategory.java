package com.example.gitmanager.notice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@SequenceGenerator(
        name = "notice_ct_seq_gene",
        sequenceName = "NOTICE_CATEGORY_ID_SEQ",
        allocationSize = 1
)
@Table(name = "NOTICE_CATEGORY")
@Entity
public class NoticeCategory {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "notice_ct_seq_gene")
    private Long id;
    @Column(name = "NAME", length = 100, nullable = false)
    private String name;

    @OneToMany(mappedBy = "noticeCategory", cascade = CascadeType.REMOVE, orphanRemoval = true)
    public List<Notice> noticeList;
}
