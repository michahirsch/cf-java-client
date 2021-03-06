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

package org.cloudfoundry.reactor.client.v2.featureflags;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cloudfoundry.client.v2.featureflags.FeatureFlags;
import org.cloudfoundry.client.v2.featureflags.GetFeatureFlagRequest;
import org.cloudfoundry.client.v2.featureflags.GetFeatureFlagResponse;
import org.cloudfoundry.client.v2.featureflags.ListFeatureFlagsRequest;
import org.cloudfoundry.client.v2.featureflags.ListFeatureFlagsResponse;
import org.cloudfoundry.client.v2.featureflags.SetFeatureFlagRequest;
import org.cloudfoundry.client.v2.featureflags.SetFeatureFlagResponse;
import org.cloudfoundry.reactor.client.v2.AbstractClientV2Operations;
import org.cloudfoundry.reactor.util.AuthorizationProvider;
import reactor.core.publisher.Mono;
import reactor.io.netty.http.HttpClient;

import static org.cloudfoundry.util.tuple.TupleUtils.function;

/**
 * The Reactor-based implementation of {@link FeatureFlags}
 */
public final class ReactorFeatureFlags extends AbstractClientV2Operations implements FeatureFlags {

    /**
     * Creates an instance
     *
     * @param authorizationProvider the {@link AuthorizationProvider} to use when communicating with the server
     * @param httpClient            the {@link HttpClient} to use when communicating with the server
     * @param objectMapper          the {@link ObjectMapper} to use when communicating with the server
     * @param root                  the root URI of the server.  Typically something like {@code https://uaa.run.pivotal.io}.
     */
    public ReactorFeatureFlags(AuthorizationProvider authorizationProvider, HttpClient httpClient, ObjectMapper objectMapper, Mono<String> root) {
        super(authorizationProvider, httpClient, objectMapper, root);
    }

    @Override
    public Mono<GetFeatureFlagResponse> get(GetFeatureFlagRequest request) {
        return get(request, GetFeatureFlagResponse.class, function((builder, validRequest) -> builder.pathSegment("v2", "config", "feature_flags", validRequest.getName())));
    }

    @Override
    public Mono<ListFeatureFlagsResponse> list(ListFeatureFlagsRequest request) {
        return get(request, ListFeatureFlagsResponse.class, function((builder, validRequest) -> builder.pathSegment("v2", "config", "feature_flags")));
    }

    @Override
    public Mono<SetFeatureFlagResponse> set(SetFeatureFlagRequest request) {
        return put(request, SetFeatureFlagResponse.class, function((builder, validRequest) -> builder.pathSegment("v2", "config", "feature_flags", validRequest.getName())));
    }

}
