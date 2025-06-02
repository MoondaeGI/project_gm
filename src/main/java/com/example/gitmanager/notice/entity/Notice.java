package com.example.gitmanager.notice.entity;

import com.example.gitmanager.notice.dto.notice.NoticeUpdateDTO;
import com.example.gitmanager.util.entity.RecordTime;
import com.example.gitmanager.util.enums.Yn;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@SequenceGenerator(
        name = "notice_seq_gene",
        sequenceName = "NOTICE_ID_SEQ",
        allocationSize = 1
)
@Table(name = "NOTICE")
@Entity
public class Notice extends RecordTime {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "notice_seq_gene")
    private Long id;
    @Column(name = "TITLE", length = 300, nullable = false)
    private String title;
    @Column(name = "CONTENT", columnDefinition = "CLOB", nullable = false)
    private String content;
    @Column(name = "VIEW_COUNT", nullable = false)
    private Integer viewCount = 0;
    @Enumerated(EnumType.STRING)
    @Column(name = "OPEN_YN", nullable = false)
    private Yn openYn = Yn.Y;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NOTICE_CATEGORY_ID", nullable = false)
    private NoticeCategory noticeCategory;

    @OneToMany(mappedBy = "notice", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<NoticeReply> noticeReplyList;

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void update(NoticeUpdateDTO dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
    }

    public void toggleOpenYn() {
        this.openYn = this.openYn == Yn.Y ? Yn.N : Yn.Y;
    }
}
