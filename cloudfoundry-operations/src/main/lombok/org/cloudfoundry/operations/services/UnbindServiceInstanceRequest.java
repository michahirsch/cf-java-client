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

package org.cloudfoundry.operations.services;

import lombok.Builder;
import lombok.Data;
import org.cloudfoundry.Validatable;
import org.cloudfoundry.ValidationResult;

/**
 * The request options for the unbind service instance operation
 */
@Data
public final class UnbindServiceInstanceRequest implements Validatable {

    /**
     * The name of the application to unbind
     *
     * @param applicationName the name of the application
     * @return the name of the application
     */
    private final String applicationName;

    /**
     * The name of the service instance to unbind
     *
     * @param serviceInstanceName the name of the service instance
     * @return the name of the service instance
     */
    private final String serviceInstanceName;

    @Builder
    UnbindServiceInstanceRequest(String applicationName, String serviceInstanceName) {
        this.applicationName = applicationName;
        this.serviceInstanceName = serviceInstanceName;
    }

    @Override
    public ValidationResult isValid() {
        ValidationResult.ValidationResultBuilder builder = ValidationResult.builder();

        if (this.applicationName == null) {
            builder.message("application name must be specified");
        }

        if (this.serviceInstanceName == null) {
            builder.message("service instance name must be specified");
        }

        return builder.build();
    }

}
