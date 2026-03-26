package com.CineCore.service;

import com.CineCore.entity.Category;
import com.CineCore.entity.Streaming;
import com.CineCore.repository.StreamingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StreamingService {
    private final StreamingRepository streamingRepository;

    public List<Streaming> getAllStreamings(){
        return streamingRepository.findAll();
    }

    public Streaming saveStreaming(Streaming streaming){
        return streamingRepository.save(streaming);
    }

    public Optional<Streaming> getStreamingById(Long id){
        return streamingRepository.findById(id);
    }

    public Optional<Streaming> updateStreaming(Long id, Streaming streaming){
        Optional<Streaming> streamingFound = streamingRepository.findById(id);
        if(streamingFound.isPresent()){
            Streaming streamingUpdated = streamingFound.get();
            streamingUpdated.setName(streaming.getName());

            streamingRepository.save(streamingUpdated);
            return Optional.of(streamingUpdated);
        }
        return Optional.empty();
    }

    public void deleteStreamingById(Long id){
        streamingRepository.deleteById(id);
    }
}
