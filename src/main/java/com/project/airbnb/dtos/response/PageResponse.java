package com.project.airbnb.dtos.response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> implements Serializable {
    private int page;
    private int size;
    private long totalPage;
    private long totalElement;
    T data;
}
