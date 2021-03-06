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

package org.cloudfoundry.client.v3.servicebindings;


import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;
import org.cloudfoundry.Validatable;
import org.cloudfoundry.ValidationResult;
import org.cloudfoundry.client.v3.FilterParameter;
import org.cloudfoundry.client.v3.PaginatedAndSortedRequest;

import java.util.List;

/**
 * The request payload for the List Service Bindings operation.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class ListServiceBindingsRequest extends PaginatedAndSortedRequest implements Validatable {

    /**
     * The application ids
     *
     * @param applicationIds the application ids
     * @return the application ids
     */
    @Getter(onMethod = @__(@FilterParameter("app_guids")))
    private final List<String> applicationIds;

    /**
     * The service instance ids
     *
     * @param serviceInstanceIds the service instance ids
     * @return the service instance ids
     */
    @Getter(onMethod = @__(@FilterParameter("service_instance_guids")))
    private final List<String> serviceInstanceIds;

    @Builder
    ListServiceBindingsRequest(Integer page, Integer perPage, String orderBy,
                               @Singular List<String> applicationIds,
                               @Singular List<String> serviceInstanceIds) {

        super(page, perPage, orderBy);
        this.applicationIds = applicationIds;
        this.serviceInstanceIds = serviceInstanceIds;
    }

    @Override
    public ValidationResult isValid() {
        return isPaginatedAndSortedRequestValid().build();
    }

}
