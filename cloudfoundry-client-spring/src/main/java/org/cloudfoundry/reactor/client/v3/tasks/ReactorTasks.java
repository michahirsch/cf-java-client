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

package org.cloudfoundry.reactor.client.v3.tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cloudfoundry.client.v3.tasks.CancelTaskRequest;
import org.cloudfoundry.client.v3.tasks.CancelTaskResponse;
import org.cloudfoundry.client.v3.tasks.CreateTaskRequest;
import org.cloudfoundry.client.v3.tasks.CreateTaskResponse;
import org.cloudfoundry.client.v3.tasks.GetTaskRequest;
import org.cloudfoundry.client.v3.tasks.GetTaskResponse;
import org.cloudfoundry.client.v3.tasks.ListTasksRequest;
import org.cloudfoundry.client.v3.tasks.ListTasksResponse;
import org.cloudfoundry.client.v3.tasks.Tasks;
import org.cloudfoundry.reactor.client.v3.AbstractClientV3Operations;
import org.cloudfoundry.reactor.util.AuthorizationProvider;
import reactor.core.publisher.Mono;
import reactor.io.netty.http.HttpClient;

import static org.cloudfoundry.util.tuple.TupleUtils.function;

/**
 * The Reactor-based implementation of {@link Tasks}
 */
public final class ReactorTasks extends AbstractClientV3Operations implements Tasks {

    /**
     * Creates an instance
     *
     * @param authorizationProvider the {@link AuthorizationProvider} to use when communicating with the server
     * @param httpClient            the {@link HttpClient} to use when communicating with the server
     * @param objectMapper          the {@link ObjectMapper} to use when communicating with the server
     * @param root                  the root URI of the server.  Typically something like {@code https://uaa.run.pivotal.io}.
     */
    public ReactorTasks(AuthorizationProvider authorizationProvider, HttpClient httpClient, ObjectMapper objectMapper, Mono<String> root) {
        super(authorizationProvider, httpClient, objectMapper, root);
    }

    @Override
    public Mono<CancelTaskResponse> cancel(CancelTaskRequest request) {
        return put(request, CancelTaskResponse.class, function((builder, validRequest) -> builder.pathSegment("v3", "tasks", validRequest.getTaskId(), "cancel")));
    }

    @Override
    public Mono<CreateTaskResponse> create(CreateTaskRequest request) {
        return post(request, CreateTaskResponse.class, function((builder, validRequest) -> builder.pathSegment("v3", "apps", validRequest.getApplicationId(), "tasks")));
    }

    @Override
    public Mono<GetTaskResponse> get(GetTaskRequest request) {
        return get(request, GetTaskResponse.class, function((builder, validRequest) -> builder.pathSegment("v3", "tasks", validRequest.getTaskId())));
    }

    @Override
    public Mono<ListTasksResponse> list(ListTasksRequest request) {
        return get(request, ListTasksResponse.class, function((builder, validRequest) -> builder.pathSegment("v3", "tasks")));
    }

}
