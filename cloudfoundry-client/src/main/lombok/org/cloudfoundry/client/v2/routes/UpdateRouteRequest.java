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

package org.cloudfoundry.client.v2.routes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.cloudfoundry.Validatable;
import org.cloudfoundry.ValidationResult;

/**
 * The request payload for the Update a Route operation
 */
@Data
public final class UpdateRouteRequest implements Validatable {

    /**
     * The domain id
     *
     * @param domainId the domain id
     * @return the domain id
     */
    @Getter(onMethod = @__(@JsonProperty("domain_guid")))
    private final String domainId;

    /**
     * The host
     *
     * @param host the host
     * @return the host
     */
    @Getter(onMethod = @__(@JsonProperty("host")))
    private final String host;

    /**
     * The path
     *
     * @param path the path
     * @return the path
     */
    @Getter(onMethod = @__(@JsonProperty("path")))
    private final String path;

    /**
     * The port
     *
     * @param port the port
     * @return the port
     */
    @Getter(onMethod = @__(@JsonProperty("port")))
    private final Integer port;

    /**
     * The route id
     *
     * @param routeId the route id
     * @return the route id
     */
    @Getter(onMethod = @__(@JsonIgnore))
    private final String routeId;

    /**
     * The space id
     *
     * @param spaceId the space id
     * @return the space id
     */
    @Getter(onMethod = @__(@JsonProperty("space_guid")))
    private final String spaceId;

    @Builder
    UpdateRouteRequest(String domainId, String host, String path, Integer port, String routeId, String spaceId) {
        this.domainId = domainId;
        this.host = host;
        this.path = path;
        this.port = port;
        this.routeId = routeId;
        this.spaceId = spaceId;
    }

    @Override
    public ValidationResult isValid() {
        ValidationResult.ValidationResultBuilder builder = ValidationResult.builder();

        if (this.routeId == null) {
            builder.message("route id must be specified");
        }

        return builder.build();
    }

}
