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

package org.cloudfoundry.reactor.client.v2.servicekeys;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cloudfoundry.client.v2.serviceinstances.ServiceInstances;
import org.cloudfoundry.client.v2.servicekeys.CreateServiceKeyRequest;
import org.cloudfoundry.client.v2.servicekeys.CreateServiceKeyResponse;
import org.cloudfoundry.client.v2.servicekeys.DeleteServiceKeyRequest;
import org.cloudfoundry.client.v2.servicekeys.GetServiceKeyRequest;
import org.cloudfoundry.client.v2.servicekeys.GetServiceKeyResponse;
import org.cloudfoundry.client.v2.servicekeys.ListServiceKeysRequest;
import org.cloudfoundry.client.v2.servicekeys.ListServiceKeysResponse;
import org.cloudfoundry.client.v2.servicekeys.ServiceKeys;
import org.cloudfoundry.reactor.client.v2.AbstractClientV2Operations;
import org.cloudfoundry.reactor.util.AuthorizationProvider;
import reactor.core.publisher.Mono;
import reactor.io.netty.http.HttpClient;

import static org.cloudfoundry.util.tuple.TupleUtils.function;

/**
 * The Reactor-based implementation of {@link ServiceInstances}
 */
public final class ReactorServiceKeys extends AbstractClientV2Operations implements ServiceKeys {

    /**
     * Creates an instance
     *
     * @param authorizationProvider the {@link AuthorizationProvider} to use when communicating with the server
     * @param httpClient            the {@link HttpClient} to use when communicating with the server
     * @param objectMapper          the {@link ObjectMapper} to use when communicating with the server
     * @param root                  the root URI of the server.  Typically something like {@code https://uaa.run.pivotal.io}.
     */
    public ReactorServiceKeys(AuthorizationProvider authorizationProvider, HttpClient httpClient, ObjectMapper objectMapper, Mono<String> root) {
        super(authorizationProvider, httpClient, objectMapper, root);
    }

    @Override
    public Mono<CreateServiceKeyResponse> create(CreateServiceKeyRequest request) {
        return post(request, CreateServiceKeyResponse.class, function((builder, validRequest) -> builder.pathSegment("v2", "service_keys")));
    }

    @Override
    public Mono<Void> delete(DeleteServiceKeyRequest request) {
        return delete(request, Void.class, function((builder, validRequest) -> builder.pathSegment("v2", "service_keys", validRequest.getServiceKeyId())));
    }

    @Override
    public Mono<GetServiceKeyResponse> get(GetServiceKeyRequest request) {
        return get(request, GetServiceKeyResponse.class, function((builder, validRequest) -> builder.pathSegment("v2", "service_keys", validRequest.getServiceKeyId())));
    }

    @Override
    public Mono<ListServiceKeysResponse> list(ListServiceKeysRequest request) {
        return get(request, ListServiceKeysResponse.class, function((builder, validRequest) -> builder.pathSegment("v2", "service_keys")));
    }

}
