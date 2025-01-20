package com.project.airbnb.dtos.request;

import com.project.airbnb.enums.BookingStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingUpdateRequest {
    @NotBlank
    private String id;
    @NotNull
    private BookingStatus status;
}
