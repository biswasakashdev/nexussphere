package com.biswasakashdev.nexussphere.common.client.impl;

import com.biswasakashdev.nexussphere.common.client.UsersClient;
import com.biswasakashdev.nexussphere.protogen.users.v1.GetUsersRequest;
import com.biswasakashdev.nexussphere.protogen.users.v1.GetUsersResponse;
import com.biswasakashdev.nexussphere.protogen.users.v1.ReactorUserGRPCServiceGrpc;
import com.biswasakashdev.nexussphere.protogen.users.v1.Types;
import io.grpc.Channel;
import reactor.core.publisher.Mono;

import java.util.List;

public class UsersGRPCClient implements UsersClient {

    private final ReactorUserGRPCServiceGrpc.ReactorUserGRPCServiceStub stub;

    public UsersGRPCClient(Channel channel) {
        this.stub = ReactorUserGRPCServiceGrpc.newReactorStub(channel);
    }

    @Override
    public Mono<List<Types.UserDetailesProto>> getUsersDetails(List<String> userIds) {
        GetUsersRequest req = GetUsersRequest.newBuilder()
                .addAllUserId(userIds)
                .build();
        return Mono
                .just(req)
                .transform(stub::getUsers)
                .map(GetUsersResponse::getUsersList);
    }
}
