package com.project.airbnb.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CloudinaryResponse {
    private String publicId;
    private String url;
}