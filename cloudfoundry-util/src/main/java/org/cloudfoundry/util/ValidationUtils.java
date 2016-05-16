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

package org.cloudfoundry.util;

import org.cloudfoundry.Validatable;
import org.cloudfoundry.ValidationResult;
import reactor.core.publisher.Mono;

public final class ValidationUtils {

    private ValidationUtils() {
    }

    public static <T> Mono<T> validate(T request) {
        if (request instanceof Validatable) {
            Validatable validatable = (Validatable) request;
            ValidationResult validationResult = validatable.isValid();

            if (validationResult.getStatus() == ValidationResult.Status.INVALID) {
                return Mono.error(new RequestValidationException(validationResult));
            }
        }

        return Mono.just(request);
    }

}
