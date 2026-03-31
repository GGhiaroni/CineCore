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
@RequestMapping("/cinecore/streaming")
@RequiredArgsConstructor
@Tag(name="Streaming", description = "Recurso responsável pelo gerenciamento dos streamings.")
public class StreamingController {
    private final StreamingService streamingService;

    @Operation(summary = "Lista todos os streamings cadastrados", description = "Rota lista todos os streamings cadastrados.")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "Streamings encontrados com sucesso."),
            @ApiResponse(responseCode = "404", description = "Streamings não encontrados."),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.")
    })
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

    @Operation(summary = "Cria um novo streaming", description = "Rota cria um novo streaming no banco de dados.")
    @ApiResponses(value={
            @ApiResponse(responseCode = "201", description = "Streaming criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro na criação do streaming."),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.")
    })
    @PostMapping
    public ResponseEntity<StreamingResponse> saveStreaming(@Valid @RequestBody StreamingRequest streaming) {
        Streaming streamingMapped = StreamingMapper.toStreaming(streaming);
        Streaming streamingSaved = streamingService.saveStreaming(streamingMapped);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                StreamingMapper.toStreamingResponse(streamingSaved)
        );
    }

    @Operation(summary = "Lista o streaming por ID", description = "Rota lista o streaming pelo ID fornecido pelo usuário.")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "Streaming encontrado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Streaming não encontrado."),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getStreamingById(@PathVariable Long id) {
        Optional<Streaming>  streamingFound = streamingService.getStreamingById(id);
        if (streamingFound.isPresent()) {
            return ResponseEntity.ok(StreamingMapper.toStreamingResponse(streamingFound.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível "
                + "localizar um streaming de id " + id + ".");
    }

    @Operation(summary = "Altera o streaming por ID.", description = "Rota altera o streaming pelo ID fornecido pelo usuário.")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "Streaming alterado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Streaming não encontrado."),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStreaming(@PathVariable Long id, @Valid @RequestBody StreamingRequest streaming){
        return streamingService.updateStreaming(id, StreamingMapper.toStreaming(streaming))
                .map(streamingUpdated -> ResponseEntity.ok(StreamingMapper.toStreamingResponse(streamingUpdated)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Deleta o streaming por ID.", description = "Rota deleta o streaming pelo ID fornecido pelo usuário.")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "Streaming deletado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Streaming não encontrado."),
            @ApiResponse(responseCode = "403", description = "Usuário não autenticado.")
    })
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
