package com.project.airbnb.dto.request;

import com.project.airbnb.enums.ListingAvailabilityStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListingAvailabilityUpdateRequest {
    @NotNull
    private ListingAvailabilityStatus status;
}
