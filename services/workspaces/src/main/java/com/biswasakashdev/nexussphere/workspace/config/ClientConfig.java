package com.biswasakashdev.nexussphere.workspace.config;


import com.biswasakashdev.nexussphere.common.client.AccessManagerClient;
import com.biswasakashdev.nexussphere.common.client.UsersClient;
import com.biswasakashdev.nexussphere.common.client.impl.AccessManagerGRPCSecureClient;
import com.biswasakashdev.nexussphere.common.client.impl.UsersGRPCClient;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class ClientConfig {

    private final Environment environment;

    @Bean
    AccessManagerClient accessManagerClient(Environment environment) {
        String url = environment.getProperty("services.accessManager");
        if (Objects.isNull(url) || url.isBlank()) {
            throw new IllegalArgumentException("Invalid access-manager client url.");
        }

        return new AccessManagerGRPCSecureClient(
                ManagedChannelBuilder
                        .forTarget(url)
                        .usePlaintext()
                        .build()
        );
    }

    @Bean
    UsersClient usersClient(Environment environment) {
        String url = environment.getProperty("services.users");
        if (Objects.isNull(url) || url.isBlank()) {
            throw new IllegalArgumentException("Invalid users client url.");
        }
        return new UsersGRPCClient(
                ManagedChannelBuilder
                        .forTarget(url)
                        .usePlaintext()
                        .build()
        );
    }

}
