package com.biswasakashdev.nexussphere.common.dtos;

import lombok.Getter;

import java.util.Map;

@Getter
public class Page<T> {

    private final T content;
    private final PageInfo pageInfo;

    public enum Direction {
        ASC,
        DESC
    }

    public record PageDetails(
            Integer page,
            Integer size,
            Map<String, Direction> sort
    ) {
    }

    public record PageInfo(
            Integer page,
            Integer size,
            Integer totalPages,
            Long totalElements
    ) {
    }


    // If requiredCount is greater than count, then return the last page.
    public static int getRequiredPage(int requiredPage, int pageSize, long totalCount) {

        long requiredCount = (long) requiredPage * pageSize;

        if (totalCount == 0) {
            return 1;
        } else if (totalCount < requiredCount) {
            return (int) Math.ceil((double) totalCount / pageSize);
        } else {
            return requiredPage;
        }
    }


    public Page(
            Integer page,
            Integer size,
            Integer totalPages,
            Long totalElements,
            T content
    ) {
        this.pageInfo = new PageInfo(
                page,
                size,
                totalPages,
                totalElements
        );
        this.content = content;
    }

}
