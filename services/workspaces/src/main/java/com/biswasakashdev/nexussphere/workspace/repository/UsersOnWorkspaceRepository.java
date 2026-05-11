package com.biswasakashdev.nexussphere.workspace.repository;

import com.biswasakashdev.nexussphere.common.dtos.Page;
import com.biswasakashdev.nexussphere.workspace.dtos.dao.UsersOnWorkspaceDTO;
import com.biswasakashdev.nexussphere.workspace.dtos.response.WorkspaceResponse;
import com.biswasakashdev.nexussphere.workspace.models.UsersOnWorkspace;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface UsersOnWorkspaceRepository {

    Mono<UsersOnWorkspace> save(UsersOnWorkspace usersOnWorkspace);

    Mono<Page<Map<String, UsersOnWorkspaceDTO>>> findAllUsersByWorkspaceId(String workspaceId, Page.PageDetails pageInfo);

    Mono<Page<List<WorkspaceResponse>>> findAllWorkspacesByUserId(String userId, Page.PageDetails pageInfo);

}
