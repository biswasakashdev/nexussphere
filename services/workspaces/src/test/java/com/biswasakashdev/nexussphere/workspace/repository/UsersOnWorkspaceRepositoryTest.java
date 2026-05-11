package com.biswasakashdev.nexussphere.workspace.repository;

import com.biswasakashdev.nexussphere.common.dtos.Page;
import com.biswasakashdev.nexussphere.workspace.models.UsersOnWorkspace;
import com.biswasakashdev.nexussphere.workspace.models.Workspaces;
import com.biswasakashdev.nexussphere.workspace.repository.impl.PostgresUsersOnWorkspaceRepositoryImpl;
import com.biswasakashdev.nexussphere.workspace.repository.impl.PostgresWorkspaceRepositoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Import(value = {
        PostgresWorkspaceRepositoryImpl.class,
        PostgresUsersOnWorkspaceRepositoryImpl.class
})
class UsersOnWorkspaceRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private UsersOnWorkspaceRepository usersOnWorkspaceRepository;

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private DatabaseClient databaseClient;


    /**
     * Loads the data from the data.sql file
     */
    @BeforeEach
    void beforeEach() {

        try {
            ClassPathResource resource = new ClassPathResource("db/data/data.sql");
            String sql = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            Flux.fromArray(sql.split(";"))
                    .map(String::trim)
                    .filter(statement -> !statement.isEmpty())
                    .concatMap(statement ->
                            databaseClient.sql(statement)
                                    .fetch()
                                    .rowsUpdated()
                    ).blockLast();

        } catch (Exception e) {
            throw new RuntimeException("Failed to load SQL file", e);
        }

    }

    /**
     * Truncates all the tables in the database
     */
    @AfterEach
    void afterEach() {
        databaseClient.sql("TRUNCATE TABLE " +
                        "users_on_workspaces, " +
                        "users_on_groups, " +
                        "workspaces, " +
                        "groups " +
                        "RESTART IDENTITY CASCADE")
                .fetch()
                .rowsUpdated()
                .block();
    }

    /**
     * Tests the save operation of the UsersOnWorkspaceRepository
     */
    @Test
    void shouldSaveUsersOnWorkspace() {
        String userId = "a-long-user-id";

        Workspaces workspaces = Workspaces.builder()
                .name("Workspace 1")
                .description("Description 1")
                .ownedBy(userId)
                .createdOn(LocalDate.now())
                .build();

        workspaceRepository
                .save(workspaces)
                .flatMap((savedWorkspace) -> {
                    UsersOnWorkspace usersOnWorkspace =
                            UsersOnWorkspace
                                    .builder()
                                    .workspaceId(savedWorkspace.getId())
                                    .userId(userId)
                                    .joinedOn(LocalDate.now())
                                    .isActive(true)
                                    .build();
                    return usersOnWorkspaceRepository.save(usersOnWorkspace);
                })
                .as(StepVerifier::create)
                .expectNextCount(1)
                .expectComplete()
                .verify();
    }

    /**
     * Tests the findAllWorkspacesByUserId operation of the UsersOnWorkspaceRepository
     */
    @Test
    void shouldFindAllTheWorkspacesUserMemberOf() {
        Page.PageDetails pageDetails = new Page.PageDetails(
                1,
                10,
                new HashMap<>()
        );
        usersOnWorkspaceRepository
                .findAllWorkspacesByUserId("u1", pageDetails)
                .as(StepVerifier::create)
                .consumeNextWith((page) -> {
                    Page.PageInfo pageInfo = page.getPageInfo();
                    assertEquals(3, pageInfo.totalElements());
                })
                .expectComplete()
                .verify();

    }

    /**
     * Tests the findAllUsersByWorkspaceId operation of the UsersOnWorkspaceRepository
     */

    @Test
    void shouldFindAllTheUsersWithWorkspaceId() {
        Page.PageDetails pageDetails = new Page.PageDetails(
                1,
                10,
                new HashMap<>()
        );
        usersOnWorkspaceRepository
                .findAllUsersByWorkspaceId("w1", pageDetails)
                .as(StepVerifier::create)
                .consumeNextWith((page) -> {
                    Page.PageInfo pageInfo = page.getPageInfo();
                    assertEquals(3, pageInfo.totalElements());
                })
                .expectComplete()
                .verify();
    }


}