package com.CineCore.controller;

import com.CineCore.entity.Category;
import com.CineCore.entity.Streaming;
import com.CineCore.mapper.CategoryMapper;
import com.CineCore.mapper.StreamingMapper;
import com.CineCore.request.CategoryRequest;
import com.CineCore.request.StreamingRequest;
import com.CineCore.response.CategoryResponse;
import com.CineCore.response.StreamingResponse;
import com.CineCore.service.StreamingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cinecore/streaming")
@RequiredArgsConstructor
public class StreamingController {
    private final StreamingService streamingService;

    @GetMapping
    public ResponseEntity<?> getAllStreamings() {
        List<Streaming> streamings = streamingService.getAllStreamings();
        if(streamings != null) {
            List<StreamingResponse> streamingsResponseList = streamings.stream()
                    .map(StreamingMapper::toStreamingResponse)
                    .toList();
            return ResponseEntity.ok(streamingsResponseList);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum streaming foi encontrado.");
    }

    @PostMapping
    public ResponseEntity<StreamingResponse> saveStreaming(@RequestBody StreamingRequest streaming) {
        Streaming streamingMapped = StreamingMapper.toStreaming(streaming);
        Streaming streamingSaved = streamingService.saveStreaming(streamingMapped);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                StreamingMapper.toStreamingResponse(streamingSaved)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStreamingById(@PathVariable Long id) {
        Optional<Streaming>  streamingFound = streamingService.getStreamingById(id);
        if (streamingFound.isPresent()) {
            return ResponseEntity.ok(StreamingMapper.toStreamingResponse(streamingFound.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível "
                + "localizar um streaming de id " + id + ".");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStreaming(@PathVariable Long id, @RequestBody StreamingRequest streaming){
        return streamingService.updateStreaming(id, StreamingMapper.toStreaming(streaming))
                .map(streamingUpdated -> ResponseEntity.ok(StreamingMapper.toStreamingResponse(streamingUpdated)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStreamingById(@PathVariable Long id) {
        Optional<Streaming> streaming = streamingService.getStreamingById(id);
        if (streaming.isPresent()) {
            streamingService.deleteStreamingById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível " +
                "encontrar um streaming com o id " + id + ".");
    }

}
