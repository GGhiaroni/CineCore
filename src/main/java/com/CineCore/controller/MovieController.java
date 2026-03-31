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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cinecore/movie")
@RequiredArgsConstructor
@Tag(name="Movie", description = "Recurso responsável pelo gerenciamento dos filmes.")
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
    public ResponseEntity<MovieResponse> saveMovie(@Valid @RequestBody MovieRequest movieRequest) {
        Movie movieSaved = movieService.saveMovie(MovieMapper.toMovie(movieRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(
                MovieMapper.toMovieResponse(movieSaved)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieResponse> updateMovie(@PathVariable Long id, @Valid @RequestBody MovieRequest movieRequest) {
        return movieService.updateMovie(id, MovieMapper.toMovie(movieRequest))
                .map(movie -> ResponseEntity.ok(MovieMapper.toMovieResponse(movie)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Long id){
        Optional<Movie> movieFound = movieService.getMovieById(id);
        if(movieFound.isPresent()){
            movieService.deleteMovie(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<MovieResponse>> findByCategory(@RequestParam Long categoryId){
        return ResponseEntity.ok(movieService.findByCategory(categoryId).stream().map(MovieMapper::toMovieResponse).toList());
    }

    @GetMapping("/latest")
    public ResponseEntity<List<MovieResponse>> getLatestMovies(@RequestParam (defaultValue = "50") int limit){
        return ResponseEntity.ok(
                movieService.getLatestMovies(limit)
                        .stream()
                        .map(MovieMapper::toMovieResponse)
                        .toList()
        );
    }

    @GetMapping("/top-rated")
        public ResponseEntity<List<MovieResponse>> getTopRatedMovies(@RequestParam(defaultValue = "5") int limit){
            return ResponseEntity.ok(
                    movieService.getTopRatedMovies(limit)
                            .stream()
                            .map(MovieMapper::toMovieResponse)
                            .toList()
            );
    }
}
