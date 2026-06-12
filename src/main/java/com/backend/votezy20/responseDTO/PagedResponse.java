package com.backend.votezy20.responseDTO;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagedResponse<T> implements Serializable{

    private List<T> content;

    private int pageNumber;
    private int pageSize;

    private long totalElements;
    private int totalPages;
}