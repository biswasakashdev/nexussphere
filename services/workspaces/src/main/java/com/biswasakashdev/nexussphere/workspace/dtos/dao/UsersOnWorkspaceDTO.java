package com.biswasakashdev.nexussphere.workspace.dtos.dao;

import java.time.LocalDateTime;

public record UsersOnWorkspaceDTO(
        String userId,
        LocalDateTime lastActivated,
        Boolean isActive
){
}
