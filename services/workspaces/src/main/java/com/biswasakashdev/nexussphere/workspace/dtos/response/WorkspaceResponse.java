package com.biswasakashdev.nexussphere.workspace.dtos.response;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record WorkspaceResponse(
        String id,
        String name,
        String description,
        String ownedBy,
        Boolean isActive,
        LocalDateTime lastActive,
        LocalDate createdOn,
        Long userCount,
        Long groupCount
) {
}
