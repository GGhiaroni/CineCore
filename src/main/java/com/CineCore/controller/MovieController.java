package com.CineCore.controller;

import com.CineCore.entity.Category;
import com.CineCore.entity.Movie;
import com.CineCore.mapper.CategoryMapper;
import com.CineCore.mapper.MovieMapper;
import com.CineCore.request.CategoryRequest;
import com.CineCore.request.MovieRequest;
import com.CineCore.response.CategoryResponse;
import com.CineCore.response.MovieResponse;
import com.CineCore.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cinecore/movie")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @GetMapping
    public ResponseEntity<?> getAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        if (movies != null) {
            List<MovieResponse> movieResponseList = movies.stream()
                    .map(MovieMapper::toMovieResponse)
                    .toList();
            return ResponseEntity.ok(movieResponseList);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma categoria foi encontrada.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable Long id) {
        Optional<Movie> movieFound = movieService.getMovieById(id);
        return movieFound
                .map(movie -> ResponseEntity.ok().body(MovieMapper.toMovieResponse(movie)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<MovieResponse> saveMovie(@RequestBody MovieRequest movieRequest) {
        Movie movieSaved = movieService.saveMovie(MovieMapper.toMovie(movieRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(
                MovieMapper.toMovieResponse(movieSaved)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieResponse> updateMovie(@PathVariable Long id, @RequestBody MovieRequest movieRequest) {
        return movieService.updateMovie(id, MovieMapper.toMovie(movieRequest))
                .map(movie -> ResponseEntity.ok(MovieMapper.toMovieResponse(movie)))
                .orElse(ResponseEntity.notFound().build());
    }

}
