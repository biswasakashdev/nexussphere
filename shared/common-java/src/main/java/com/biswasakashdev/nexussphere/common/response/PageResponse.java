package com.biswasakashdev.nexussphere.common.response;

import com.biswasakashdev.nexussphere.common.dtos.Page;

import java.util.List;

public record PageResponse<T>(
        int page,
        int pageSize,
        int totalPages,
        long totalItems,
        List<T> content
) {

    public static <K> PageResponse<K> buildPageResponseFromPageDetails(
            List<K> content,
            Page.PageInfo pageInfo
    ) {
        return new PageResponse<>(
                pageInfo.page(),
                pageInfo.size(),
                pageInfo.totalPages(),
                pageInfo.totalElements(),
                content
        );
    }
}
