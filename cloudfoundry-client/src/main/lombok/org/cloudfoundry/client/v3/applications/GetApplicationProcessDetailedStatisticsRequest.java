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

package org.cloudfoundry.client.v3.applications;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.cloudfoundry.Validatable;
import org.cloudfoundry.ValidationResult;
import org.cloudfoundry.client.v3.PaginatedRequest;

/**
 * The request payload for the Get Detailed Stats for an Application's Process operation
 */
@Data
@EqualsAndHashCode(callSuper = true)
public final class GetApplicationProcessDetailedStatisticsRequest extends PaginatedRequest implements Validatable {

    /**
     * The application id
     *
     * @param applicationId the application id
     * @return the application id
     */
    @Getter(onMethod = @__(@JsonIgnore))
    private final String applicationId;

    /**
     * The type of the process
     *
     * @param type the type of the process
     * @return the type of the process
     */
    @Getter(onMethod = @__(@JsonIgnore))
    private final String type;

    @Builder
    GetApplicationProcessDetailedStatisticsRequest(Integer page,
                                                   Integer perPage,
                                                   String applicationId,
                                                   String type) {
        super(page, perPage);
        this.applicationId = applicationId;
        this.type = type;
    }

    @Override
    public ValidationResult isValid() {
        ValidationResult.ValidationResultBuilder builder = ValidationResult.builder();

        if (this.applicationId == null) {
            builder.message("application id must be specified");
        }

        if (this.type == null) {
            builder.message("process type must be specified");
        }

        return builder.build();
    }

}