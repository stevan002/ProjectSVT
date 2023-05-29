package com.example.projectsvt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "postSequenceGenerator")
    @SequenceGenerator(name = "postSequenceGenerator", sequenceName = "postSequence", allocationSize = 1)
    private Long id;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT NOW()", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    // Nema smisla da postoji ukoliko je content prazan
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "image_path")
    private String image;

    @ManyToOne
    @JoinColumn(name = "fk_user_id", nullable = false)
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "fk_group_id")
    private Group containedBy;
}
