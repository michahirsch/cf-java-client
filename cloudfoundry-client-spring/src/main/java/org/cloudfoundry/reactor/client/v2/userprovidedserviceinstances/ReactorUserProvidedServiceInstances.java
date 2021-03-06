/*
 * Copyright 2013-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cloudfoundry.reactor.client.v2.userprovidedserviceinstances;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.CreateUserProvidedServiceInstanceRequest;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.CreateUserProvidedServiceInstanceResponse;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.DeleteUserProvidedServiceInstanceRequest;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.GetUserProvidedServiceInstanceRequest;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.GetUserProvidedServiceInstanceResponse;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.ListUserProvidedServiceInstanceServiceBindingsRequest;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.ListUserProvidedServiceInstanceServiceBindingsResponse;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.ListUserProvidedServiceInstancesRequest;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.ListUserProvidedServiceInstancesResponse;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.UpdateUserProvidedServiceInstanceRequest;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.UpdateUserProvidedServiceInstanceResponse;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.UserProvidedServiceInstances;
import org.cloudfoundry.reactor.client.v2.AbstractClientV2Operations;
import org.cloudfoundry.reactor.util.AuthorizationProvider;
import reactor.core.publisher.Mono;
import reactor.io.netty.http.HttpClient;

import static org.cloudfoundry.util.tuple.TupleUtils.function;

/**
 * The Reactor-based implementation of {@link UserProvidedServiceInstances}
 */
public final class ReactorUserProvidedServiceInstances extends AbstractClientV2Operations implements UserProvidedServiceInstances {

    /**
     * Creates an instance
     *
     * @param authorizationProvider the {@link AuthorizationProvider} to use when communicating with the server
     * @param httpClient            the {@link HttpClient} to use when communicating with the server
     * @param objectMapper          the {@link ObjectMapper} to use when communicating with the server
     * @param root                  the root URI of the server.  Typically something like {@code https://uaa.run.pivotal.io}.
     */
    public ReactorUserProvidedServiceInstances(AuthorizationProvider authorizationProvider, HttpClient httpClient, ObjectMapper objectMapper, Mono<String> root) {
        super(authorizationProvider, httpClient, objectMapper, root);
    }

    @Override
    public Mono<CreateUserProvidedServiceInstanceResponse> create(CreateUserProvidedServiceInstanceRequest request) {
        return post(request, CreateUserProvidedServiceInstanceResponse.class, function((builder, validRequest) -> builder.pathSegment("v2", "user_provided_service_instances")));
    }

    @Override
    public Mono<Void> delete(DeleteUserProvidedServiceInstanceRequest request) {
        return delete(request, Void.class, function((builder, validRequest) -> builder.pathSegment("v2", "user_provided_service_instances", validRequest.getUserProvidedServiceInstanceId())));
    }

    @Override
    public Mono<GetUserProvidedServiceInstanceResponse> get(GetUserProvidedServiceInstanceRequest request) {
        return get(request, GetUserProvidedServiceInstanceResponse.class,
            function((builder, validRequest) -> builder.pathSegment("v2", "user_provided_service_instances", validRequest.getUserProvidedServiceInstanceId())));
    }

    @Override
    public Mono<ListUserProvidedServiceInstancesResponse> list(ListUserProvidedServiceInstancesRequest request) {
        return get(request, ListUserProvidedServiceInstancesResponse.class, function((builder, validRequest) -> builder.pathSegment("v2", "user_provided_service_instances")));
    }

    @Override
    public Mono<ListUserProvidedServiceInstanceServiceBindingsResponse> listServiceBindings(ListUserProvidedServiceInstanceServiceBindingsRequest request) {
        return get(request, ListUserProvidedServiceInstanceServiceBindingsResponse.class,
            function((builder, validRequest) -> builder.pathSegment("v2", "user_provided_service_instances", validRequest.getUserProvidedServiceInstanceId(), "service_bindings")));
    }

    @Override
    public Mono<UpdateUserProvidedServiceInstanceResponse> update(UpdateUserProvidedServiceInstanceRequest request) {
        return put(request, UpdateUserProvidedServiceInstanceResponse.class,
            function((builder, validRequest) -> builder.pathSegment("v2", "user_provided_service_instances", validRequest.getUserProvidedServiceInstanceId())));
    }

}
