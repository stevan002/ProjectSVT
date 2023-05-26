package com.example.projectsvt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "group_requests")
public class GroupRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "groupRequestSeqGenerator")
    @SequenceGenerator(name = "groupRequestSeqGenerator", sequenceName = "groupRequestSequence", allocationSize = 1)
    private Long id;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT NOW()", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "approved", nullable = false)
    private boolean approved = false;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @ManyToOne
    @JoinColumn(name = "fk_user_id", nullable = false)
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "fk_group_id")
    private Group group;
}
