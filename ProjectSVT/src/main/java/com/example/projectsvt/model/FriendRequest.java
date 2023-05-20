package com.example.projectsvt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "friend_requests")
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "friendRequestSequenceGenerator")
    @SequenceGenerator(name = "friendRequestSequenceGenerator", sequenceName = "friendRequestSequence", allocationSize = 1)
    private Long id;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT NOW()", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "approved")
    private boolean approved = false;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @ManyToOne
    @JoinColumn(name = "fk_sent_by_id", nullable = false)
    private User sentBy;

    @ManyToOne
    @JoinColumn(name = "fk_sent_to_id", nullable = false)
    private User sentTo;
}
