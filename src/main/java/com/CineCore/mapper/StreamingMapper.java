package com.CineCore.mapper;

import com.CineCore.entity.Category;
import com.CineCore.entity.Streaming;
import com.CineCore.request.CategoryRequest;
import com.CineCore.request.StreamingRequest;
import com.CineCore.response.CategoryResponse;
import com.CineCore.response.StreamingResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StreamingMapper {
    public static Streaming toStreaming(StreamingRequest streamingRequest){
        return Streaming
                .builder()
                .name(streamingRequest.name())
                .build();
    }

    public static StreamingResponse toStreamingResponse(Streaming streaming){
        return StreamingResponse
                .builder()
                .id(streaming.getId())
                .name(streaming.getName())
                .build();
    }
}
