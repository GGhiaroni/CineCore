package com.CineCore.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Entity
@Table(name = "movie")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    private double rating;

    @CreationTimestamp
    // annotation do hibernate para eu não ter que me preocupar em setar a data de criação manualmente
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    // annotation do hibernate para eu não ter que me preocupar em setar a data de atualização manualmente
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany
    @JoinTable(name = "movie_category",
            joinColumns = @JoinColumn(name = "movie_id"), //FK da entidade DONA do relacionamento
            inverseJoinColumns = @JoinColumn(name = "category_id") //aqui, passo a FK da outra entidade que faz parte do join
    )
    private List<Category> categories;

    @ManyToMany
    @JoinTable(name = "movie_streaming",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "streaming_id")
    )
    private List<Streaming> streamings;

}
