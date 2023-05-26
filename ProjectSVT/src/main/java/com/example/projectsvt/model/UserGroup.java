package com.example.projectsvt.model;

import javax.persistence.*;
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
@Table(name = "user_groups")
public class UserGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "userGroupSequenceGenerator")
    @SequenceGenerator(name = "userGroupSequenceGenerator", sequenceName = "userGroupSequence", allocationSize = 1)
    private Long id;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT NOW()", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @ManyToOne
    @JoinColumn(name = "fk_user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "fk_group_id", nullable = false)
    private Group group;


    public UserGroup(User user, Group group) {
        this.user = user;
        this.group = group;
    }
}
