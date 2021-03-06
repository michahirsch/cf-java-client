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

package org.cloudfoundry.operations.routes;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

/**
 * A route and the applications which are bound to the route.
 */
@Data
public final class Route {

    /**
     * The applications bound to this route.
     *
     * @param applications the application names
     * @return the application names
     */
    private final List<String> applications;

    /**
     * The domain of this route
     *
     * @param domain the domain
     * @return the domain
     */
    private final String domain;

    /**
     * The host of this route
     *
     * @param host the host
     * @return the host
     */
    private final String host;

    /**
     * The id
     *
     * @param id the id
     * @return the id
     */
    private final String id;

    /**
     * The path of this route
     *
     * @param path the path
     * @return the path
     */
    private final String path;

    /**
     * The GUID of this route
     *
     * @param routId the GUID of this route
     * @return the GUID of this route
     */
    private final String routeId;

    /**
     * The name of the space of this route
     *
     * @param space the name of the space
     * @return the name of the space
     */
    private final String space;

    @Builder
    Route(String routeId,
          @Singular List<String> applications,
          String domain,
          String host,
          String id,
          String path,
          String space) {
        this.routeId = routeId;
        this.applications = applications;
        this.domain = domain;
        this.host = host;
        this.id = id;
        this.path = path;
        this.space = space;
    }

}
