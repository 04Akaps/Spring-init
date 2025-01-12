package com.example.demo.domain.requestBinding.model.reqeust;

import com.example.demo.domain.requestBinding.binding.Hex.StartWithHex;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "post biding test request")
public record PostBinding (
    @Schema(description = "key")
    @StartWithHex
    @NotBlank @NotNull String key
){}