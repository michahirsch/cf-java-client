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

package org.cloudfoundry.client.v3.processes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Singular;

import java.util.Map;

@Data
public final class HealthCheck {

    /**
     * The data
     *
     * @param datas the data
     * @return the data
     */
    @Getter(onMethod = @__(@JsonProperty("data")))
    private final Map<String, Object> datas;

    /**
     * The type
     *
     * @param type the type
     * @return the type
     */
    @Getter(onMethod = @__(@JsonProperty("type")))
    private final Type type;

    @Builder
    HealthCheck(@JsonProperty("data") @Singular Map<String, Object> datas,
                @JsonProperty("type") Type type) {

        this.datas = datas;
        this.type = type;
    }

    /**
     * The type of a {@link HealthCheck}
     */
    public enum Type {

        /**
         * A port health check
         */
        PORT,

        /**
         * A process health check
         */
        PROCESS;

        @JsonValue
        @Override
        public String toString() {
            return name().toLowerCase();
        }

        @JsonCreator
        static Type fromString(String s) {
            if (PORT.toString().equals(s)) {
                return PORT;
            } else if (PROCESS.toString().equals(s)) {
                return PROCESS;
            } else {
                throw new IllegalArgumentException(String.format("Type %s is not a valid type", s));
            }
        }

    }

}
