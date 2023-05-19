package com.example.projectsvt.Model;

import com.example.projectsvt.Model.Enums.ReactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reacts")
public class React {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "reactSequenceGenerator")
    @SequenceGenerator(name = "reactSequenceGenerator", sequenceName = "reactSequence", allocationSize = 1)
    private Long id;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT NOW()", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReactionType type;

    @ManyToOne
    @JoinColumn(name = "fk_comment_id")
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "fk_post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "fk_reacted_by_id", nullable = false)
    private User reactedBy;
}
