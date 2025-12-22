package com.evento.model;

import jakarta.annotation.Nullable;
import lombok.NonNull;

public record ApiResponse<T>(
        @NonNull String code,
        @NonNull String message,
        @Nullable T response
) {
}
