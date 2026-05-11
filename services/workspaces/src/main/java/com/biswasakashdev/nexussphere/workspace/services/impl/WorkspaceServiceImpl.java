package com.biswasakashdev.nexussphere.workspace.services.impl;

import com.biswasakashdev.nexussphere.common.client.AccessManagerClient;
import com.biswasakashdev.nexussphere.common.client.UsersClient;
import com.biswasakashdev.nexussphere.common.dtos.Page;
import com.biswasakashdev.nexussphere.common.exceptions.DataSourceOperationFailedException;
import com.biswasakashdev.nexussphere.common.response.PageResponse;
import com.biswasakashdev.nexussphere.workspace.dtos.dao.UsersOnWorkspaceDTO;
import com.biswasakashdev.nexussphere.workspace.dtos.requests.NewWorkspaceRequest;
import com.biswasakashdev.nexussphere.workspace.dtos.response.UsersOnWorkspaceResponse;
import com.biswasakashdev.nexussphere.workspace.dtos.response.WorkspaceResponse;
import com.biswasakashdev.nexussphere.workspace.models.UsersOnWorkspace;
import com.biswasakashdev.nexussphere.workspace.models.Workspaces;
import com.biswasakashdev.nexussphere.workspace.repository.UsersOnWorkspaceRepository;
import com.biswasakashdev.nexussphere.workspace.repository.WorkspaceRepository;
import com.biswasakashdev.nexussphere.workspace.services.WorkspaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service
@Slf4j
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final UsersOnWorkspaceRepository usersOnWorkspaceRepository;
    private final AccessManagerClient accessManagerClient;
    private final UsersClient usersClient;

    @Override
    @Transactional
    public Mono<Workspaces> createWorkspace(
            String userId,
            NewWorkspaceRequest newWorkspace
    ) {
        Workspaces workspaces = Workspaces.builder()
                .name(newWorkspace.name())
                .description(newWorkspace.description())
                .ownedBy(userId)
                .createdOn(LocalDate.now())
                .build();

        return workspaceRepository
                .save(workspaces)
                .flatMap((savedWorkspace) -> {

                    UsersOnWorkspace usersOnWorkspace =
                            UsersOnWorkspace.builder()
                                    .userId(userId)
                                    .workspaceId(savedWorkspace.getId())
                                    .build();

                    return usersOnWorkspaceRepository.save(usersOnWorkspace)
                            .flatMap((savedUsersOnWorkspace) -> accessManagerClient
                                    .createPrimarySecurityGroup(
                                            savedUsersOnWorkspace.getUserId(),
                                            savedUsersOnWorkspace.getWorkspaceId()
                                    ))
                            .then(Mono.just(savedWorkspace));
                })
                .onErrorResume(DataSourceOperationFailedException.class, (err) ->
                        Mono.error(new RuntimeException("Failed to create workspaces."))
                );
    }

    @Override
    public Mono<Boolean> isWorkspaceNameExists(String workspaceId, String userId) {
        return null;
    }

    @Override
    public Mono<PageResponse<UsersOnWorkspaceResponse>> findAllUsersInWorkspace(
            String workspaceId,
            Page.PageDetails pageDetails
    ) {

        return usersOnWorkspaceRepository
                .findAllUsersByWorkspaceId(workspaceId, pageDetails)
                .flatMap(page-> {
                    Map<String, UsersOnWorkspaceDTO> content = page.getContent();

                    Set<String> userIds = content.keySet();

                    return usersClient
                            .getUsersDetails(new ArrayList<>(userIds))
                            .map(userDetailsProto -> {
//                                Build UsersOnWorkspaceDetails with UserDetailsProto and UsersOnWorkspaceDTO.
                                List<UsersOnWorkspaceResponse> usersOnWorkspaceResponseList = userDetailsProto
                                        .stream().map(userDet -> {
                                            UsersOnWorkspaceDTO usersOnWorkspaceDTO = content.get(userDet.getId());
                                            return new UsersOnWorkspaceResponse(
                                                    usersOnWorkspaceDTO.userId(),
                                                    userDet.getFirstName(),
                                                    userDet.getLastName(),
                                                    userDet.getUsername(),
                                                    usersOnWorkspaceDTO.lastActivated(),
                                                    usersOnWorkspaceDTO.isActive()
                                            );
                                        }).toList();

                                return PageResponse
                                        .buildPageResponseFromPageDetails(
                                                usersOnWorkspaceResponseList,
                                                page.getPageInfo()
                                        );
                            });
                });
    }

    @Override
    public Mono<PageResponse<WorkspaceResponse>> findAllWorkspace(
            String userId,
            Page.PageDetails pageDetails
    ) {
        return usersOnWorkspaceRepository
                .findAllWorkspacesByUserId(userId, pageDetails)
                .map(page -> {
                    List<WorkspaceResponse> content = page
                            .getContent();

                    return PageResponse.buildPageResponseFromPageDetails(
                            content,
                            page.getPageInfo()
                    );

                });

    }
}
