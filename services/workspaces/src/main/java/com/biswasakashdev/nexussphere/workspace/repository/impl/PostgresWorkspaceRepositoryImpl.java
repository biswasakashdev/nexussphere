package com.biswasakashdev.nexussphere.workspace.repository.impl;

import com.biswasakashdev.nexussphere.common.exceptions.DataSourceOperationFailedException;
import com.biswasakashdev.nexussphere.workspace.models.Workspaces;
import com.biswasakashdev.nexussphere.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PostgresWorkspaceRepositoryImpl implements WorkspaceRepository {

    private final DatabaseClient databaseClient;

    @Override
    public Mono<Workspaces> save(Workspaces workspaces) {

        String id = UUID.randomUUID().toString();

        return databaseClient
                .sql("INSERT INTO workspaces(id, workspace_name, description, owner_id, created_on) " +
                        "VALUES (:id, :name, :description, :owner_id, :created_on)")
                .bind("id", id)
                .bind("name", workspaces.getName())
                .bind("description", workspaces.getDescription())
                .bind("owner_id", workspaces.getOwnedBy())
                .bind("created_on", workspaces.getCreatedOn())
                .fetch()
                .rowsUpdated()
                .flatMap(rows -> {
                    if (rows == 0) {
                        String msg = String.format("Failed to save workspace with name: %s.", workspaces.getName());
                        return Mono.error(new DataSourceOperationFailedException(msg));
                    }
                    workspaces.setId(id); // set generated id
                    return Mono.just(workspaces);
                });
    }

    @Override
    public Mono<Workspaces> findById(String id) {
        return databaseClient.sql("SELECT * FROM workspaces WHERE id = :id")
                .bind("id", id)
                .map(properties -> {
                    LocalDate createdOn = (LocalDate) properties.get("created_on");
                    return Workspaces
                            .builder()
                            .id((String) properties.get("id"))
                            .name((String) properties.get("workspace_name"))
                            .description((String) properties.get("description"))
                            .createdOn(createdOn)
                            .build();
                })
                .one();
    }

    @Override
    public Mono<Boolean> deleteById(String id) {
        return databaseClient
                .sql("DELETE FROM workspaces WHERE id= :id")
                .bind("id",id)
                .fetch()
                .rowsUpdated()
                .map(rows-> rows >0);
    }
}
