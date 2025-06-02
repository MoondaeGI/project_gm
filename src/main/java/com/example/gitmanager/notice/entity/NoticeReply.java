package com.example.gitmanager.notice.entity;

import com.example.gitmanager.member.entity.Member;
import com.example.gitmanager.util.entity.RecordTime;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@SequenceGenerator(
        name = "notice_re_seq_gene",
        sequenceName = "NOTICE_REPLY_ID_SEQ",
        allocationSize = 1
)
@Table(name = "NOTICE_REPLY")
@Entity
public class NoticeReply extends RecordTime {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "notice_re_seq_gene")
    private Long id;
    @Builder.Default
    @Column(name = "DEPTH", nullable = false)
    private Integer depth = 0;
    @Column(name = "CONTENT", length = 300, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WRITER_ID", nullable = false)
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NOTICE_ID", nullable = false)
    private Notice notice;

    @OneToOne()
    @JoinColumn(name = "PARENT_ID")
    private NoticeReply parent;

    public void update(String content) {
        this.content = content;
    }
}
