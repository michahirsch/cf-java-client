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

import org.cloudfoundry.ValidationResult;
import org.junit.Test;

import static org.cloudfoundry.ValidationResult.Status.INVALID;
import static org.cloudfoundry.ValidationResult.Status.VALID;
import static org.junit.Assert.assertEquals;

public final class GetServiceKeyRequestTest {

    @Test
    public void isNotValidNoInstanceName() {
        ValidationResult result = GetServiceKeyRequest.builder()
            .serviceKeyName("test-service-key-name")
            .build()
            .isValid();

        assertEquals(INVALID, result.getStatus());
        assertEquals("service instance name must be specified", result.getMessages().get(0));
    }

    @Test
    public void isNotValidNoKeyName() {
        ValidationResult result = GetServiceKeyRequest.builder()
            .serviceInstanceName("test-service-instance-name")
            .build()
            .isValid();

        assertEquals(INVALID, result.getStatus());
        assertEquals("service key name must be specified", result.getMessages().get(0));
    }

    @Test
    public void isValid() {
        ValidationResult result = GetServiceKeyRequest.builder()
            .serviceKeyName("test-service-key-name")
            .serviceInstanceName("test-service-instance-name")
            .build()
            .isValid();

        assertEquals(VALID, result.getStatus());
    }

}
