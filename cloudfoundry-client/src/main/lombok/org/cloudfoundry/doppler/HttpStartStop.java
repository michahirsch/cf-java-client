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

package org.cloudfoundry.doppler;

import lombok.Builder;
import lombok.Data;
import org.cloudfoundry.Validatable;
import org.cloudfoundry.ValidationResult;

import java.util.Optional;
import java.util.UUID;

@Data
public final class HttpStartStop implements Event, Validatable {

    /**
     * The application id
     *
     * @return applicationId the application id
     * @return the application id
     */
    private final UUID applicationId;

    /**
     * The length of the response in bytes
     *
     * @param contentLength the length of the response in bytes
     * @return the length of the response in bytes
     */
    private final Long contentLength;

    /**
     * The ID of the application instance
     *
     * @param instanceId the ID of the application instance
     * @return the ID of the application instance
     */
    private final String instanceId;

    /**
     * The index of the application instance
     *
     * @param instanceId the index of the application instance
     * @return the index of the application instance
     */
    private final Integer instanceIndex;

    /**
     * The method of the request
     *
     * @param method the method of the request
     * @return the method of the request
     */
    private final Method method;

    /**
     * The role of the emitting process in the request cycle
     *
     * @param peerType the role of the emitting process in the request cycle
     * @return the role of the emitting process in the request cycle
     */
    private final PeerType peerType;

    /**
     * The remote address of the request. (For a server, this should be the origin of the request.)
     *
     * @param remoteAddress the remote address of the request
     * @return the remote address of the request
     */
    private final String remoteAddress;

    /**
     * The ID for tracking lifecycle of request. Should match requestId of a {@link HttpStop} event
     *
     * @param requestId the ID for tracking lifecycle of request
     * @return the ID for tracking lifecycle of request
     */
    private final UUID requestId;

    /**
     * The UNIX timestamp (in nanoseconds) when the request was sent (by a client) or received (by a server)
     *
     * @param timestamp the UNIX timestamp
     * @return the UNIX timestamp
     */
    private final Long startTimestamp;

    /**
     * The status code returned with the response to the request
     *
     * @param statusCode the status code returned with the response to the request
     * @return the status code returned with the response to the request
     */
    private final Integer statusCode;

    /**
     * The UNIX timestamp (in nanoseconds) when the request was received
     *
     * @param timestamp the UNIX timestamp
     * @return the UNIX timestamp
     */
    private final Long stopTimestamp;

    /**
     * The uri of the request
     *
     * @param uri the uri of the request
     * @return the uri of the request
     */
    private final String uri;

    /**
     * The contents of the UserAgent header on the request
     *
     * @param userAgent the contents of the UserAgent header on the request
     * @return the contents of the UserAgent header on the request
     */
    private final String userAgent;

    @Builder
    HttpStartStop(org.cloudfoundry.dropsonde.events.HttpStartStop dropsonde, UUID applicationId, Long contentLength, String instanceId, Integer instanceIndex, Method method, PeerType peerType,
                  String remoteAddress, UUID requestId, Long startTimestamp, Integer statusCode, Long stopTimestamp, String uri, String userAgent) {

        Optional<org.cloudfoundry.dropsonde.events.HttpStartStop> o = Optional.ofNullable(dropsonde);

        this.applicationId = o.map(d -> d.applicationId).map(DropsondeUtils::uuid).orElse(applicationId);
        this.contentLength = o.map(d -> d.contentLength).orElse(contentLength);
        this.instanceId = o.map(d -> d.instanceId).orElse(instanceId);
        this.instanceIndex = o.map(d -> d.instanceIndex).orElse(instanceIndex);
        this.method = o.map(d -> d.method).map(Method::dropsonde).orElse(method);
        this.peerType = o.map(d -> d.peerType).map(PeerType::dropsonde).orElse(peerType);
        this.remoteAddress = o.map(d -> d.remoteAddress).orElse(remoteAddress);
        this.requestId = o.map(d -> d.requestId).map(DropsondeUtils::uuid).orElse(requestId);
        this.startTimestamp = o.map(d -> d.startTimestamp).orElse(startTimestamp);
        this.statusCode = o.map(d -> d.statusCode).orElse(statusCode);
        this.stopTimestamp = o.map(d -> d.stopTimestamp).orElse(stopTimestamp);
        this.uri = o.map(d -> d.uri).orElse(uri);
        this.userAgent = o.map(d -> d.userAgent).orElse(userAgent);
    }

    @Override
    public ValidationResult isValid() {
        ValidationResult.ValidationResultBuilder builder = ValidationResult.builder();

        if (this.contentLength == null) {
            builder.message("content length must be specified");
        }

        if (this.method == null) {
            builder.message("method must be specified");
        }

        if (this.peerType == null) {
            builder.message("peer type must be specified");
        }

        if (this.remoteAddress == null) {
            builder.message("remote address must be specified");
        }

        if (this.requestId == null) {
            builder.message("request id must be specified");
        }

        if (this.startTimestamp == null) {
            builder.message("start timestamp must be specified");
        }

        if (this.statusCode == null) {
            builder.message("status code must be specified");
        }

        if (this.stopTimestamp == null) {
            builder.message("start timestamp must be specified");
        }

        if (this.uri == null) {
            builder.message("uri must be specified");
        }

        if (this.userAgent == null) {
            builder.message("user agent must be specified");
        }

        return builder.build();
    }

}
