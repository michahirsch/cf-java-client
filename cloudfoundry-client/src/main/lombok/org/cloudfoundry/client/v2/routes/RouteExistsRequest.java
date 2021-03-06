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
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.cloudfoundry.QueryParameter;
import org.cloudfoundry.Validatable;
import org.cloudfoundry.ValidationResult;

/**
 * The request payload for the Check a Route exists operation
 */
@Data
public final class RouteExistsRequest implements Validatable {

    /**
     * The domain id
     *
     * @param domainId the domain id
     * @return the domain id
     */
    @Getter(onMethod = @__(@JsonIgnore))
    private final String domainId;

    /**
     * The host
     *
     * @param host the host
     * @return the host
     */
    @Getter(onMethod = @__(@JsonIgnore))
    private final String host;

    /**
     * The path
     *
     * @param path the path
     * @return the path
     */
    @Getter(onMethod = @__(@QueryParameter("path")))
    private final String path;

    @Builder
    RouteExistsRequest(String domainId, String host, String path) {
        this.domainId = domainId;
        this.host = host;
        this.path = path;
    }

    @Override
    public ValidationResult isValid() {
        ValidationResult.ValidationResultBuilder builder = ValidationResult.builder();

        if (this.domainId == null) {
            builder.message("domain id must be specified");
        }

        if (this.host == null) {
            builder.message("host must be specified");
        }

        return builder.build();
    }

}
