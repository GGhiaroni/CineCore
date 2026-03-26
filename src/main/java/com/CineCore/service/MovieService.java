package com.CineCore.service;

import com.CineCore.entity.Category;
import com.CineCore.entity.Movie;
import com.CineCore.entity.Streaming;
import com.CineCore.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final CategoryService categoryService;
    private final StreamingService streamingService;

    private List<Category> findCategories(List<Category> categories){
        List<Category> categoriesFound = new ArrayList<>();
        for(Category category : categories){
            categoryService.getCategoryById(category.getId()).ifPresent(categoriesFound::add);
        }
        return categoriesFound;
    }

    private List<Streaming> findStreamings(List<Streaming> streamings){
        List<Streaming> streamingsFound = new ArrayList<>();
        for(Streaming streaming : streamings){
            streamingService.getStreamingById(streaming.getId()).ifPresent(streamingsFound::add);
        }
        return streamingsFound;
    }



    public List<Movie> getAllMovies(){
        return movieRepository.findAll();
    }

    public Movie saveMovie(Movie movie) {
        movie.setCategories(this.findCategories(movie.getCategories()));
        movie.setStreamings(this.findStreamings(movie.getStreamings()));
        return movieRepository.save(movie);

    }



}
