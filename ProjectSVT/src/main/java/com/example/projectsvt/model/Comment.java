package com.example.projectsvt.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "commentSequenceGenerator")
    @SequenceGenerator(name = "commentSequenceGenerator", sequenceName = "commentSequence", allocationSize = 1)
    private Long id;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy. hh:mm")
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT NOW()", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Ukoliko je komentar prazan, nema smisla da postoji
    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "fk_user_id", nullable = false)
    private User writtenBy;

    @ManyToOne
    @JoinColumn(name = "fk_post_id")
    private Post belongsTo;

    @ManyToOne
    @JoinColumn(name = "fk_comment_id")
    private Comment comment;
}
