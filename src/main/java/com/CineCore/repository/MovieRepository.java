package com.CineCore.repository;

import com.CineCore.entity.Category;
import com.CineCore.entity.Movie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByCategoriesId(Long categoryId);

    List<Movie> findAllByOrderByReleaseDateDesc(Pageable pageable);

    List<Movie> findAllByOrderByRatingDesc(Pageable pageable);
}
