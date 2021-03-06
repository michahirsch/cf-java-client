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

package org.cloudfoundry.reactor.client.v2.servicebindings;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cloudfoundry.client.v2.servicebindings.CreateServiceBindingRequest;
import org.cloudfoundry.client.v2.servicebindings.CreateServiceBindingResponse;
import org.cloudfoundry.client.v2.servicebindings.DeleteServiceBindingRequest;
import org.cloudfoundry.client.v2.servicebindings.DeleteServiceBindingResponse;
import org.cloudfoundry.client.v2.servicebindings.GetServiceBindingRequest;
import org.cloudfoundry.client.v2.servicebindings.GetServiceBindingResponse;
import org.cloudfoundry.client.v2.servicebindings.ListServiceBindingsRequest;
import org.cloudfoundry.client.v2.servicebindings.ListServiceBindingsResponse;
import org.cloudfoundry.client.v2.servicebindings.ServiceBindingsV2;
import org.cloudfoundry.reactor.client.v2.AbstractClientV2Operations;
import org.cloudfoundry.reactor.util.AuthorizationProvider;
import reactor.core.publisher.Mono;
import reactor.io.netty.http.HttpClient;

import static org.cloudfoundry.util.tuple.TupleUtils.function;

/**
 * The Reactor-based implementation of {@link ServiceBindingsV2}
 */
public final class ReactorServiceBindingsV2 extends AbstractClientV2Operations implements ServiceBindingsV2 {

    /**
     * Creates an instance
     *
     * @param authorizationProvider the {@link AuthorizationProvider} to use when communicating with the server
     * @param httpClient            the {@link HttpClient} to use when communicating with the server
     * @param objectMapper          the {@link ObjectMapper} to use when communicating with the server
     * @param root                  the root URI of the server.  Typically something like {@code https://uaa.run.pivotal.io}.
     */
    public ReactorServiceBindingsV2(AuthorizationProvider authorizationProvider, HttpClient httpClient, ObjectMapper objectMapper, Mono<String> root) {
        super(authorizationProvider, httpClient, objectMapper, root);
    }

    @Override
    public Mono<CreateServiceBindingResponse> create(CreateServiceBindingRequest request) {
        return post(request, CreateServiceBindingResponse.class, function((builder, validRequest) -> builder.pathSegment("v2", "service_bindings")));
    }

    @Override
    public Mono<DeleteServiceBindingResponse> delete(DeleteServiceBindingRequest request) {
        return delete(request, DeleteServiceBindingResponse.class, function((builder, validRequest) -> builder.pathSegment("v2", "service_bindings", validRequest.getServiceBindingId())));
    }

    @Override
    public Mono<GetServiceBindingResponse> get(GetServiceBindingRequest request) {
        return get(request, GetServiceBindingResponse.class, function((builder, validRequest) -> builder.pathSegment("v2", "service_bindings", validRequest.getServiceBindingId())));
    }

    @Override
    public Mono<ListServiceBindingsResponse> list(ListServiceBindingsRequest request) {
        return get(request, ListServiceBindingsResponse.class, function((builder, validRequets) -> builder.pathSegment("v2", "service_bindings")));
    }

}
