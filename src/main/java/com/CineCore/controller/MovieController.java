package com.CineCore.controller;

import com.CineCore.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cinecore/movie")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

}
