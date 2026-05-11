package com.biswasakashdev.nexussphere.workspace.models;


import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Workspaces {

    private String id;
    private String name;
    private String ownedBy;
    private String description;
    private LocalDate createdOn;

}

