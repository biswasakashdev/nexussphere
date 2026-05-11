package com.biswasakashdev.nexussphere.workspace.dtos.response;

import java.time.LocalDateTime;

public record UsersOnWorkspaceResponse(
        String userId,
        String firstName,
        String lastName,
        String username,
        LocalDateTime lastAccessed,
        Boolean isActivate
) {
}
