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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Lista todos os filmes cadastrados", description = "Rota lista todos os filmes cadastrados.")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "Filmes encontrados com sucesso."),
            @ApiResponse(responseCode = "404", description = "Filmes não encontrados."),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.")
    })
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

    @Operation(summary = "Lista o filme por ID", description = "Rota lista o filme pelo ID fornecido pelo usuário.")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "Filme encontrado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Filme não encontrado."),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable Long id) {
        Optional<Movie> movieFound = movieService.getMovieById(id);
        return movieFound
                .map(movie -> ResponseEntity.ok().body(MovieMapper.toMovieResponse(movie)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Cria um novo filme", description = "Rota cria um novo filme no banco de dados.")
    @ApiResponses(value={
            @ApiResponse(responseCode = "201", description = "Filme criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro na criação do filme."),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.")
    })
    @PostMapping
    public ResponseEntity<MovieResponse> saveMovie(@Valid @RequestBody MovieRequest movieRequest) {
        Movie movieSaved = movieService.saveMovie(MovieMapper.toMovie(movieRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(
                MovieMapper.toMovieResponse(movieSaved)
        );
    }

    @Operation(summary = "Altera o filme por ID.", description = "Rota altera o filme pelo ID fornecido pelo usuário.")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "Filme alterado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Filme não encontrado."),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MovieResponse> updateMovie(@PathVariable Long id, @Valid @RequestBody MovieRequest movieRequest) {
        return movieService.updateMovie(id, MovieMapper.toMovie(movieRequest))
                .map(movie -> ResponseEntity.ok(MovieMapper.toMovieResponse(movie)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Deleta o filme por ID.", description = "Rota deleta o filme pelo ID fornecido pelo usuário.")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "Filme deletado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Filme não encontrado."),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Long id){
        Optional<Movie> movieFound = movieService.getMovieById(id);
        if(movieFound.isPresent()){
            movieService.deleteMovie(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Lista os filmes por categoria.", description = "Rota lista os filmes pelo ID da categoria fornecido pelo usuário.")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "Filmes listados com sucesso para a categoria informada."),
            @ApiResponse(responseCode = "404", description = "Nenhum filme encontrado."),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.")
    })
    @GetMapping("/search")
    public ResponseEntity<List<MovieResponse>> findByCategory(@RequestParam Long categoryId){
        return ResponseEntity.ok(movieService.findByCategory(categoryId).stream().map(MovieMapper::toMovieResponse).toList());
    }


    @Operation(summary = "Lista os filmes por data de lançamento, dos mais recentes para os mais antigos.", description = "Rota lista os filmes pela data de lançamento. Número de filmes informado pelo usuário no parâmetro da requisição.")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "Filmes listados com sucesso, por data de lançamento, dos mais recentes aos mais antigos."),
            @ApiResponse(responseCode = "404", description = "Nenhum filme encontrado."),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.")
    })
    @GetMapping("/latest")
    public ResponseEntity<List<MovieResponse>> getLatestMovies(@RequestParam (defaultValue = "50") int limit){
        return ResponseEntity.ok(
                movieService.getLatestMovies(limit)
                        .stream()
                        .map(MovieMapper::toMovieResponse)
                        .toList()
        );
    }

    @Operation(summary = "Lista os filmes mais bem avaliados do catálogo.", description = "Rota lista os filmes mais bem avaliados (em ordem decrescente). Número de filmes informado pelo usuário no parâmetro da requisição.")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "Filmes listados com sucesso, por ordem de avaliação, do mais bem avaliado ao menor."),
            @ApiResponse(responseCode = "404", description = "Nenhum filme encontrado."),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.")
    })
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
