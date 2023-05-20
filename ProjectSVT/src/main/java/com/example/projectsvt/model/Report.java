package com.example.projectsvt.model;

import com.example.projectsvt.model.Enums.ReportReason;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "reportSequenceGenerator")
    @SequenceGenerator(name = "reportSequenceGenerator", sequenceName = "reportSequence", allocationSize = 1)
    private Long id;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT NOW()", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "reason", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportReason reason;

    @ManyToOne
    @JoinColumn(name = "fk_user_id", nullable = false)
    private User reportedBy;

    @ManyToOne
    @JoinColumn(name = "fk_comment_id")
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "fk_post_id")
    private Post post;

}
