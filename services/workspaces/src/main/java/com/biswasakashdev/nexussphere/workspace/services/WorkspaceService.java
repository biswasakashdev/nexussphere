package com.biswasakashdev.nexussphere.workspace.services;

import com.biswasakashdev.nexussphere.common.dtos.Page;
import com.biswasakashdev.nexussphere.common.response.PageResponse;
import com.biswasakashdev.nexussphere.workspace.dtos.requests.NewWorkspaceRequest;
import com.biswasakashdev.nexussphere.workspace.dtos.response.UsersOnWorkspaceResponse;
import com.biswasakashdev.nexussphere.workspace.dtos.response.WorkspaceResponse;
import com.biswasakashdev.nexussphere.workspace.models.Workspaces;
import reactor.core.publisher.Mono;

public interface WorkspaceService {

    Mono<Workspaces> createWorkspace(String userId, NewWorkspaceRequest newWorkspace);

    Mono<Boolean> isWorkspaceNameExists(String userId, String workspaceName);

    Mono<PageResponse<UsersOnWorkspaceResponse>> findAllUsersInWorkspace(String workspaceId, Page.PageDetails pageDetails);

    Mono<PageResponse<WorkspaceResponse>> findAllWorkspace(String userId, Page.PageDetails pageDetails);
}
