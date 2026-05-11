package com.biswasakashdev.nexussphere.workspace.models;


import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersOnWorkspace {

    private String userId;
    private String workspaceId;
    private LocalDate joinedOn;
    private Boolean isActive;

}
