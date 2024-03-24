package com.vamae.payload.request;

import com.vamae.entity.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.List;

@Builder
public record CreateNewsRequest(
    @NotBlank(message = "Header is required")
    String header,
    @NotBlank(message = "Content is required")
    String content,
    String imageUrl,
    List<Tag> tags
) {
}
