package com.biswasakashdev.nexussphere.workspace.controller;

import com.biswasakashdev.nexussphere.common.dtos.Page;
import com.biswasakashdev.nexussphere.common.response.PageResponse;
import com.biswasakashdev.nexussphere.workspace.dtos.requests.NewWorkspaceRequest;
import com.biswasakashdev.nexussphere.workspace.dtos.response.IsNameExists;
import com.biswasakashdev.nexussphere.workspace.dtos.response.UsersOnWorkspaceResponse;
import com.biswasakashdev.nexussphere.workspace.dtos.response.WorkspaceResponse;
import com.biswasakashdev.nexussphere.workspace.models.Workspaces;
import com.biswasakashdev.nexussphere.workspace.services.WorkspaceService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @Getter
    public enum SortParameter {
        CREATED_ON("Created On", "createdOn"),
        ;
        private final String value;
        private final String fieldName;

        SortParameter(String value, String fieldName) {
            this.value = value;
            this.fieldName = fieldName;
        }
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> createNewWorkspace(
            @RequestHeader("Authentication-Info") String userId,
            @RequestBody NewWorkspaceRequest newWorkspace) {
        Mono<Workspaces> workspacesMono = workspaceService.createWorkspace(userId, newWorkspace);
        return workspacesMono
                .then();
    }

    @GetMapping
    public Mono<PageResponse<WorkspaceResponse>> getAllWorkspace(
            @RequestHeader("Authentication-Info") String userId,
            @RequestParam(name = "page", required = false, defaultValue = "20") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "20") Integer pageSize,
            @RequestParam(name = "direction", required = false, defaultValue = "ASC") Page.Direction direction
    ) {
        Map<String, Page.Direction> sort= new HashMap<>();

        Page.PageDetails pageDetails= new Page.PageDetails(
                page,
                pageSize,
                sort
        );
        return workspaceService
                .findAllWorkspace(userId, pageDetails);
    }

    @GetMapping(value = "/exists")
    public Mono<IsNameExists> getWorkspace(
            @RequestHeader("Authentication-Info") String userId,
            @RequestParam(name = "workspaceName") String workspaceName
    ) {
        return workspaceService
                .isWorkspaceNameExists(userId, workspaceName)
                .map(IsNameExists::new);
    }


    @GetMapping(value = "/users")
    public Mono<PageResponse<UsersOnWorkspaceResponse>> getAllUserWithWorkspace(
            @RequestHeader("Authentication-Info") String userId,
            @RequestParam(name = "workspaceId") String workspaceId,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "20") Integer size,
            @RequestParam(name = "direction", required = false, defaultValue = "ASC") Page.Direction direction
    ) {

        Map<String, Page.Direction> sort= new HashMap<>();
        Page.PageDetails pageDetails= new Page.PageDetails(page, size, sort);
        return workspaceService
                .findAllUsersInWorkspace(workspaceId, pageDetails);
    }

}
