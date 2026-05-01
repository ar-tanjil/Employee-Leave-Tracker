package com.employee_leave_tracker.backend.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.Collection;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResponse<T> extends CustomResponse {

    private final Collection<T> data;
    private final PageMetadata metaData;


    public PageResponse(Page<T> page) {
        String message = page.getContent().isEmpty() ? "No data found" : "Data found";

        super(message,  HttpStatus.OK.value(), HttpStatus.OK.name());

        PageMetadata mData = PageMetadata.builder()
                .currentPage(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .empty(page.isEmpty())
                .build();

        this.data = page.getContent();
        this.metaData = mData;

    }

    public PageResponse(Page<T> page, String message) {
        super(message,  HttpStatus.OK.value(), HttpStatus.OK.name());

        PageMetadata mData = PageMetadata.builder()
                .currentPage(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .empty(page.isEmpty())
                .build();

        this.data = page.getContent();
        this.metaData = mData;

    }


    public PageResponse(Page<T> page, String message, HttpStatus status) {
        super(message, status.value(), status.name());

        PageMetadata mData = PageMetadata.builder()
                .currentPage(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .empty(page.isEmpty())
                .build();

        this.data = page.getContent();
        this.metaData = mData;
    }

    public PageResponse(Collection<T> content, int currentPage, int pageSize,
                              long totalElements, String message) {
        super(message, HttpStatus.OK.value(), HttpStatus.OK.name());

        int totalPages = pageSize > 0 ? (int) Math.ceil((double) totalElements / pageSize) : 0;
        boolean first = currentPage == 0;
        boolean last = currentPage >= totalPages - 1;
        PageMetadata mData = PageMetadata.builder()
                .currentPage(currentPage)
                .pageSize(pageSize)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .first(first)
                .last(last)
                .empty(content.isEmpty())
                .build();

        this.data = content;
        this.metaData = mData;

    }

}
