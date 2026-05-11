package com.biswasakashdev.nexussphere.common.client;

import com.biswasakashdev.nexussphere.protogen.users.v1.Types;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UsersClient {

    Mono<List<Types.UserDetailesProto>> getUsersDetails(List<String> userIds);
}
