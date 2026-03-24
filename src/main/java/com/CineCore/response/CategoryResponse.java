package com.CineCore.response;

import lombok.Builder;

@Builder
public record CategoryResponse(Long id, String name) {
}
