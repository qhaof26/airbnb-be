package com.project.airbnb.dtos.response;

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