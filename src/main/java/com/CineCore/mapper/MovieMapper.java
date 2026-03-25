package com.CineCore.mapper;

import com.CineCore.entity.Category;
import com.CineCore.entity.Movie;
import com.CineCore.entity.Streaming;
import com.CineCore.request.MovieRequest;
import com.CineCore.response.CategoryResponse;
import com.CineCore.response.MovieResponse;
import com.CineCore.response.StreamingResponse;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class MovieMapper {
    public static Movie toMovie(MovieRequest movieRequest){
        List<Category> categoriesList = movieRequest.categories().stream()
                .map(categoryId -> Category.builder().id(categoryId).build())
                .toList();

        List<Streaming> streamingsList = movieRequest.streamings().stream()
                .map(streamingId -> Streaming.builder().id(streamingId).build())
                .toList();

        return Movie.builder()
                .title(movieRequest.title())
                .description(movieRequest.description())
                .releaseDate(movieRequest.releaseDate())
                .rating(movieRequest.rating())
                .categories(categoriesList)
                .streamings(streamingsList)
                .build();
    }

    public static MovieResponse toMovieResponse(Movie movie){

        List<CategoryResponse> categoriesResponseList = movie.getCategories()
                .stream()
                .map(CategoryMapper::toCategoryResponse)
                .toList();

        List<StreamingResponse> streamingResponseList = movie.getStreamings()
                .stream()
                .map(StreamingMapper::toStreamingResponse)
                .toList();

        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .description(movie.getDescription())
                .releaseDate(movie.getReleaseDate())
                .rating(movie.getRating())
                .categories(categoriesResponseList)
                .streamings(streamingResponseList)
                .build();
    }
}
