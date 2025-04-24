package com.project.therapies.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "therapy_id", nullable = false)
    private Therapy therapy;
}
