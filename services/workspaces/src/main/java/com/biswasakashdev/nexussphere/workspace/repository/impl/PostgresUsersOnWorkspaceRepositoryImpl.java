package com.biswasakashdev.nexussphere.workspace.repository.impl;

import com.biswasakashdev.nexussphere.common.dtos.Page;
import com.biswasakashdev.nexussphere.common.exceptions.DataSourceOperationFailedException;
import com.biswasakashdev.nexussphere.workspace.dtos.dao.UsersOnWorkspaceDTO;
import com.biswasakashdev.nexussphere.workspace.dtos.response.WorkspaceResponse;
import com.biswasakashdev.nexussphere.workspace.models.UsersOnWorkspace;
import com.biswasakashdev.nexussphere.workspace.repository.UsersOnWorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostgresUsersOnWorkspaceRepositoryImpl implements UsersOnWorkspaceRepository {

    private final DatabaseClient databaseClient;

    @Override
    public Mono<UsersOnWorkspace> save(UsersOnWorkspace usersOnWorkspace) {
        return databaseClient
                .sql("INSERT INTO users_on_workspaces(user_id, workspace_id, joined_on,last_active , is_active)" +
                        "VALUES (:user_id, :workspace_id, :joined_on, :last_active , :is_active)")
                .bind("user_id", usersOnWorkspace.getUserId())
                .bind("workspace_id", usersOnWorkspace.getWorkspaceId())
                .bind("joined_on", LocalDate.now())
                .bind("last_active", LocalDateTime.now())
                .bind("is_active", usersOnWorkspace.getIsActive())
                .fetch()
                .rowsUpdated()
                .flatMap((count) -> {
                    if (count == 0) {
                        String msg = String.format("Failed to add user %s to the workspace %s", usersOnWorkspace.getUserId(), usersOnWorkspace.getWorkspaceId());
                        return Mono.error(new DataSourceOperationFailedException(msg));
                    }
                    return Mono.just(usersOnWorkspace);
                });
    }


    @Override
    public Mono<Page<Map<String, UsersOnWorkspaceDTO>>> findAllUsersByWorkspaceId(
            String workspaceId,
            Page.PageDetails pageDetails
    ) {

        int requiredPage = pageDetails.page();
        int pageSize = pageDetails.size();

        Mono<Long> userCountMono = databaseClient
                .sql("SELECT COUNT(*) FROM users_on_workspaces WHERE workspace_id = :workspace_id")
                .bind("workspace_id", workspaceId)
                .map((row) -> Objects.requireNonNull(row.get(0, Long.class)))
                .one();


        return userCountMono.flatMap((count) -> {

            int page = Page.getRequiredPage(requiredPage, pageSize, count);

            int offSet = (page - 1) * pageSize;

            Mono<Map<String, UsersOnWorkspaceDTO>> userIdListMono = databaseClient
                    .sql("SELECT " +
                            "uw.user_id AS user_id, " +
                            "uw.last_active AS last_active, " +
                            "uw.is_active AS is_active " +
                            "FROM users_on_workspaces AS uw WHERE uw.workspace_id = :workspace_id " +
                            "LIMIT :page_size OFFSET :off_set")
                    .bind("workspace_id", workspaceId)
                    .bind("page_size", pageSize)
                    .bind("off_set", offSet)
                    .map((row, metadata) -> new UsersOnWorkspaceDTO(
                            row.get("user_id", String.class),
                            row.get("last_active", LocalDateTime.class),
                            row.get("is_active",Boolean.class)
                    ))
                    .all()
                    .collectMap(
                            UsersOnWorkspaceDTO::userId, // key mapper
                            member -> member         // value mapper
                    );

            return userIdListMono.map(userList -> {
                int pageCount = (int) Math.ceil((double) count / pageSize);
                return new Page<>(
                        page,
                        pageSize,
                        pageCount,
                        count,
                        userList
                );
            });
        });
    }

    @Override
    public Mono<Page<List<WorkspaceResponse>>> findAllWorkspacesByUserId(
            String userId,
            Page.PageDetails pageDetails
    ) {
        Mono<Long> workspaceCountMono = databaseClient
                .sql("SELECT COUNT(*) FROM users_on_workspaces WHERE user_id = :userId")
                .bind("userId", userId)
                .map((row) -> Objects.requireNonNull(row.get(0, Long.class)))
                .one();

        int requiredPage = pageDetails.page();
        int pageSize = pageDetails.size();

        return workspaceCountMono.flatMap((count) -> {

            int page = Page.getRequiredPage(requiredPage, pageSize, count);

            int offSet = (page - 1) * pageSize;

            Mono<List<WorkspaceResponse>> workspaceIdListMono = databaseClient
                    .sql("SELECT " +
                            "w.id AS workspace_id, " +
                            "w.workspace_name AS workspace_name, " +
                            "w.created_on AS created_on, " +
                            "w.description AS description, " +
                            "w.owner_id AS owner_id, " +
                            "uw.is_active AS is_active, " +
                            "uw.last_active AS last_active, " +
                            "(SELECT COUNT(*) FROM users_on_workspaces WHERE workspace_id = w.id) AS user_count, " +
                            "(SELECT COUNT(*) FROM groups WHERE workspace_id = w.id) AS owner_count " +
                            "FROM workspaces w JOIN users_on_workspaces uw ON uw.user_id = :user_id " +
                            "LIMIT :page_size OFFSET :off_set"
                    )
                    .bind("user_id", userId)
                    .bind("page_size", pageSize)
                    .bind("off_set", offSet)
                    .map((row) -> WorkspaceResponse.builder()
                            .id(row.get("workspace_id", String.class))
                            .name(row.get("workspace_name", String.class))
                            .description(row.get("description", String.class))
                            .ownedBy(row.get("owner_id", String.class))
                            .isActive(row.get("is_active", Boolean.class))
                            .lastActive(row.get("last_active", LocalDateTime.class))
                            .userCount(row.get("user_count", Long.class))
                            .groupCount(row.get("owner_count", Long.class))
                            .createdOn(row.get("created_on", LocalDate.class))
                            .build())
                    .all()
                    .collectList();

            return workspaceIdListMono.map(workspaceList -> {
                int pageCount = (int) Math.ceil((double) count / pageSize);
                return new Page<>(
                        page,
                        pageSize,
                        pageCount,
                        count,
                        workspaceList
                );
            });
        });
    }


}
