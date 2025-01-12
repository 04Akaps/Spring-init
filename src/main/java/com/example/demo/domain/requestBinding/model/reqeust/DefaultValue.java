package com.example.demo.domain.requestBinding.model.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;

public record DefaultValue(
        @Schema(name = "default is DESC")
        String key
) {
        public DefaultValue {
                if (key == null || key.trim().isEmpty()) {
                        key = "DESC";
                }
        }
}
