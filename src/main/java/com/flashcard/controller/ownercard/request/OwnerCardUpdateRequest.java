package com.flashcard.controller.ownercard.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OwnerCardUpdateRequest {

    @NotNull
    private Long id;

    @NotBlank
    @Size(max = 512)
    private String frontFace;

    @NotBlank
    @Size(max = 512)
    private String backFace;
}
