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

package org.cloudfoundry.reactor.client.v3.processes;

import org.cloudfoundry.client.v3.Link;
import org.cloudfoundry.client.v3.processes.AbstractProcessStatistics.PortMapping;
import org.cloudfoundry.client.v3.processes.GetProcessStatisticsRequest;
import org.cloudfoundry.client.v3.processes.GetProcessStatisticsResponse;
import org.cloudfoundry.client.v3.processes.GetProcessRequest;
import org.cloudfoundry.client.v3.processes.GetProcessResponse;
import org.cloudfoundry.client.v3.processes.HealthCheck;
import org.cloudfoundry.client.v3.processes.HealthCheck.Type;
import org.cloudfoundry.client.v3.processes.ListProcessesRequest;
import org.cloudfoundry.client.v3.processes.ListProcessesResponse;
import org.cloudfoundry.client.v3.processes.ProcessUsage;
import org.cloudfoundry.client.v3.processes.ScaleProcessRequest;
import org.cloudfoundry.client.v3.processes.ScaleProcessResponse;
import org.cloudfoundry.client.v3.processes.TerminateProcessInstanceRequest;
import org.cloudfoundry.client.v3.processes.UpdateProcessRequest;
import org.cloudfoundry.client.v3.processes.UpdateProcessResponse;
import org.cloudfoundry.reactor.InteractionContext;
import org.cloudfoundry.reactor.TestRequest;
import org.cloudfoundry.reactor.TestResponse;
import org.cloudfoundry.reactor.client.AbstractClientApiTest;
import reactor.core.publisher.Mono;

import static io.netty.handler.codec.http.HttpMethod.DELETE;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpMethod.PATCH;
import static io.netty.handler.codec.http.HttpMethod.PUT;
import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static org.cloudfoundry.client.v3.PaginatedResponse.Pagination;
import static org.cloudfoundry.client.v3.processes.ListProcessesResponse.Resource;

public final class ReactorProcessesTest {

    public static final class DeleteInstance extends AbstractClientApiTest<TerminateProcessInstanceRequest, Void> {

        private final ReactorProcesses processes = new ReactorProcesses(AUTHORIZATION_PROVIDER, HTTP_CLIENT, OBJECT_MAPPER, this.root);

        @Override
        protected InteractionContext getInteractionContext() {
            return InteractionContext.builder()
                .request(TestRequest.builder()
                    .method(DELETE).path("/v3/processes/test-process-id/instances/test-index")
                    .build())
                .response(TestResponse.builder()
                    .status(NO_CONTENT)
                    .build())
                .build();
        }

        @Override
        protected TerminateProcessInstanceRequest getInvalidRequest() {
            return TerminateProcessInstanceRequest.builder()
                .build();
        }

        @Override
        protected Void getResponse() {
            return null;
        }

        @Override
        protected TerminateProcessInstanceRequest getValidRequest() {
            return TerminateProcessInstanceRequest.builder()
                .processId("test-process-id")
                .index("test-index")
                .build();
        }

        @Override
        protected Mono<Void> invoke(TerminateProcessInstanceRequest request) {
            return this.processes.terminateInstance(request);
        }

    }

    public static final class Get extends AbstractClientApiTest<GetProcessRequest, GetProcessResponse> {

        private final ReactorProcesses processes = new ReactorProcesses(AUTHORIZATION_PROVIDER, HTTP_CLIENT, OBJECT_MAPPER, this.root);

        @Override
        protected InteractionContext getInteractionContext() {
            return InteractionContext.builder()
                .request(TestRequest.builder()
                    .method(GET).path("/v3/processes/test-process-id")
                    .build())
                .response(TestResponse.builder()
                    .status(OK)
                    .payload("fixtures/client/v3/processes/GET_{id}_response.json")
                    .build())
                .build();
        }

        @Override
        protected GetProcessRequest getInvalidRequest() {
            return GetProcessRequest.builder()
                .build();
        }

        @Override
        protected GetProcessResponse getResponse() {
            return GetProcessResponse.builder()
                .id("6a901b7c-9417-4dc1-8189-d3234aa0ab82")
                .type("web")
                .command("rackup")
                .instances(5)
                .memoryInMb(256)
                .diskInMb(1_024)
                .port(8080)
                .healthCheck(HealthCheck.builder()
                    .type(Type.PORT)
                    .data("timeout", null)
                    .build())
                .createdAt("2016-03-23T18:48:22Z")
                .updatedAt("2016-03-23T18:48:42Z")
                .link("self", Link.builder()
                    .href("/v3/processes/6a901b7c-9417-4dc1-8189-d3234aa0ab82")
                    .build())
                .link("scale", Link.builder()
                    .href("/v3/processes/6a901b7c-9417-4dc1-8189-d3234aa0ab82/scale")
                    .method("PUT")
                    .build())
                .link("app", Link.builder()
                    .href("/v3/apps/ccc25a0f-c8f4-4b39-9f1b-de9f328d0ee5")
                    .build())
                .link("space", Link.builder()
                    .href("/v2/spaces/2f35885d-0c9d-4423-83ad-fd05066f8576")
                    .build())
                .build();
        }

        @Override
        protected GetProcessRequest getValidRequest() {
            return GetProcessRequest.builder()
                .processId("test-process-id")
                .build();
        }

        @Override
        protected Mono<GetProcessResponse> invoke(GetProcessRequest request) {
            return this.processes.get(request);
        }

    }

    public static final class GetProcessStatistics extends AbstractClientApiTest<GetProcessStatisticsRequest, GetProcessStatisticsResponse> {

        private final ReactorProcesses processes = new ReactorProcesses(AUTHORIZATION_PROVIDER, HTTP_CLIENT, OBJECT_MAPPER, this.root);

        @Override
        protected InteractionContext getInteractionContext() {
            return InteractionContext.builder()
                .request(TestRequest.builder()
                    .method(GET).path("/v3/processes/test-id/stats")
                    .build())
                .response(TestResponse.builder()
                    .status(OK)
                    .payload("fixtures/client/v3/processes/GET_{id}_stats_response.json")
                    .build())
                .build();
        }

        @Override
        protected GetProcessStatisticsRequest getInvalidRequest() {
            return GetProcessStatisticsRequest.builder()
                .build();
        }

        @Override
        protected GetProcessStatisticsResponse getResponse() {
            return GetProcessStatisticsResponse.builder()
                .resource(GetProcessStatisticsResponse.Resource.builder()
                    .type("web")
                    .index(0)
                    .state("RUNNING")
                    .usage(ProcessUsage.builder()
                        .time("2016-03-23T23:17:30.476314154Z")
                        .cpu(0.00038711029163348665)
                        .memory(19177472L)
                        .disk(69705728L)
                        .build())
                    .host("10.244.16.10")
                    .instancePort(PortMapping.builder()
                        .external(64546)
                        .internal(8080)
                        .build())
                    .uptime(9042L)
                    .memoryQuota(268435456L)
                    .diskQuota(1073741824L)
                    .fdsQuota(16384)
                    .build())
                .build();
        }

        @Override
        protected GetProcessStatisticsRequest getValidRequest() throws Exception {
            return GetProcessStatisticsRequest.builder()
                .processId("test-id")
                .build();
        }

        @Override
        protected Mono<GetProcessStatisticsResponse> invoke(GetProcessStatisticsRequest request) {
            return this.processes.getStatistics(request);
        }

    }

    public static final class List extends AbstractClientApiTest<ListProcessesRequest, ListProcessesResponse> {

        private final ReactorProcesses processes = new ReactorProcesses(AUTHORIZATION_PROVIDER, HTTP_CLIENT, OBJECT_MAPPER, this.root);

        @Override
        protected InteractionContext getInteractionContext() {
            return InteractionContext.builder()
                .request(TestRequest.builder()
                    .method(GET).path("/v3/processes?page=1&per_page=2")
                    .build())
                .response(TestResponse.builder()
                    .status(OK)
                    .payload("fixtures/client/v3/processes/GET_response.json")
                    .build())
                .build();
        }

        @Override
        protected ListProcessesRequest getInvalidRequest() {
            return ListProcessesRequest.builder()
                .page(-1)
                .build();
        }

        @Override
        protected ListProcessesResponse getResponse() {
            return ListProcessesResponse.builder()
                .pagination(Pagination.builder()
                    .totalResults(3)
                    .first(Link.builder()
                        .href("/v3/processes?page=1&per_page=2")
                        .build())
                    .last(Link.builder()
                        .href("/v3/processes?page=2&per_page=2")
                        .build())
                    .next(Link.builder()
                        .href("/v3/processes?page=2&per_page=2")
                        .build())
                    .build())
                .resource(Resource.builder()
                    .id("6a901b7c-9417-4dc1-8189-d3234aa0ab82")
                    .type("web")
                    .command("rackup")
                    .instances(5)
                    .memoryInMb(256)
                    .diskInMb(1_024)
                    .port(8080)
                    .healthCheck(HealthCheck.builder()
                        .type(Type.PORT)
                        .data("timeout", null)
                        .build())
                    .createdAt("2016-03-23T18:48:22Z")
                    .updatedAt("2016-03-23T18:48:42Z")
                    .link("self", Link.builder()
                        .href("/v3/processes/6a901b7c-9417-4dc1-8189-d3234aa0ab82")
                        .build())
                    .link("scale", Link.builder()
                        .href("/v3/processes/6a901b7c-9417-4dc1-8189-d3234aa0ab82/scale")
                        .method("PUT")
                        .build())
                    .link("app", Link.builder()
                        .href("/v3/apps/ccc25a0f-c8f4-4b39-9f1b-de9f328d0ee5")
                        .build())
                    .link("space", Link.builder()
                        .href("/v2/spaces/2f35885d-0c9d-4423-83ad-fd05066f8576")
                        .build())
                    .build())
                .resource(Resource.builder()
                    .id("3fccacd9-4b02-4b96-8d02-8e865865e9eb")
                    .type("worker")
                    .command("bundle exec rake worker")
                    .instances(1)
                    .memoryInMb(256)
                    .diskInMb(1_024)
                    .healthCheck(HealthCheck.builder()
                        .type(Type.PROCESS)
                        .data("timeout", null)
                        .build())
                    .createdAt("2016-03-23T18:48:22Z")
                    .updatedAt("2016-03-23T18:48:42Z")
                    .link("self", Link.builder()
                        .href("/v3/processes/3fccacd9-4b02-4b96-8d02-8e865865e9eb")
                        .build())
                    .link("scale", Link.builder()
                        .href("/v3/processes/6a901b7c-9417-4dc1-8189-d3234aa0ab82/scale")
                        .method("PUT")
                        .build())
                    .link("app", Link.builder()
                        .href("/v3/apps/ccc25a0f-c8f4-4b39-9f1b-de9f328d0ee5")
                        .build())
                    .link("space", Link.builder()
                        .href("/v2/spaces/2f35885d-0c9d-4423-83ad-fd05066f8576")
                        .build())
                    .build())
                .build();
        }

        @Override
        protected ListProcessesRequest getValidRequest() {
            return ListProcessesRequest.builder()
                .page(1)
                .perPage(2)
                .build();
        }

        @Override
        protected Mono<ListProcessesResponse> invoke(ListProcessesRequest request) {
            return this.processes.list(request);
        }

    }

    public static final class Scale extends AbstractClientApiTest<ScaleProcessRequest, ScaleProcessResponse> {

        private final ReactorProcesses processes = new ReactorProcesses(AUTHORIZATION_PROVIDER, HTTP_CLIENT, OBJECT_MAPPER, this.root);

        @Override
        protected InteractionContext getInteractionContext() {
            return InteractionContext.builder()
                .request(TestRequest.builder()
                    .method(PUT).path("/v3/processes/test-process-id/scale")
                    .payload("fixtures/client/v3/processes/PUT_{id}_scale_request.json")
                    .build())
                .response(TestResponse.builder()
                    .status(OK)
                    .payload("fixtures/client/v3/processes/PUT_{id}_scale_response.json")
                    .build())
                .build();
        }

        @Override
        protected ScaleProcessRequest getInvalidRequest() {
            return ScaleProcessRequest.builder()
                .build();
        }

        @Override
        protected ScaleProcessResponse getResponse() {
            return ScaleProcessResponse.builder()
                .id("6a901b7c-9417-4dc1-8189-d3234aa0ab82")
                .type("web")
                .command("rackup")
                .instances(5)
                .memoryInMb(256)
                .diskInMb(1_024)
                .port(8080)
                .healthCheck(HealthCheck.builder()
                    .type(Type.PORT)
                    .data("timeout", null)
                    .build())
                .createdAt("2016-03-23T18:48:22Z")
                .updatedAt("2016-03-23T18:48:42Z")
                .link("self", Link.builder()
                    .href("/v3/processes/6a901b7c-9417-4dc1-8189-d3234aa0ab82")
                    .build())
                .link("scale", Link.builder()
                    .href("/v3/processes/6a901b7c-9417-4dc1-8189-d3234aa0ab82/scale")
                    .method("PUT")
                    .build())
                .link("app", Link.builder()
                    .href("/v3/apps/ccc25a0f-c8f4-4b39-9f1b-de9f328d0ee5")
                    .build())
                .link("space", Link.builder()
                    .href("/v2/spaces/2f35885d-0c9d-4423-83ad-fd05066f8576")
                    .build())
                .build();
        }

        @Override
        protected ScaleProcessRequest getValidRequest() {
            return ScaleProcessRequest.builder()
                .processId("test-process-id")
                .instances(5)
                .memoryInMb(256)
                .diskInMb(1_024)
                .build();
        }

        @Override
        protected Mono<ScaleProcessResponse> invoke(ScaleProcessRequest request) {
            return this.processes.scale(request);
        }
    }

    public static final class Update extends AbstractClientApiTest<UpdateProcessRequest, UpdateProcessResponse> {

        private final ReactorProcesses processes = new ReactorProcesses(AUTHORIZATION_PROVIDER, HTTP_CLIENT, OBJECT_MAPPER, this.root);

        @Override
        protected InteractionContext getInteractionContext() {
            return InteractionContext.builder()
                .request(TestRequest.builder()
                    .method(PATCH).path("/v3/processes/test-process-id")
                    .payload("fixtures/client/v3/processes/PATCH_{id}_request.json")
                    .build())
                .response(TestResponse.builder()
                    .status(OK)
                    .payload("fixtures/client/v3/processes/PATCH_{id}_response.json")
                    .build())
                .build();
        }

        @Override
        protected UpdateProcessRequest getInvalidRequest() {
            return UpdateProcessRequest.builder()
                .build();
        }

        @Override
        protected UpdateProcessResponse getResponse() {
            return UpdateProcessResponse.builder()
                .id("6a901b7c-9417-4dc1-8189-d3234aa0ab82")
                .type("web")
                .command("rackup")
                .instances(5)
                .memoryInMb(256)
                .diskInMb(1_024)
                .port(8080)
                .healthCheck(HealthCheck.builder()
                    .type(Type.PORT)
                    .data("timeout", null)
                    .build())
                .createdAt("2016-03-23T18:48:22Z")
                .updatedAt("2016-03-23T18:48:42Z")
                .link("self", Link.builder()
                    .href("/v3/processes/6a901b7c-9417-4dc1-8189-d3234aa0ab82")
                    .build())
                .link("scale", Link.builder()
                    .href("/v3/processes/6a901b7c-9417-4dc1-8189-d3234aa0ab82/scale")
                    .method("PUT")
                    .build())
                .link("app", Link.builder()
                    .href("/v3/apps/ccc25a0f-c8f4-4b39-9f1b-de9f328d0ee5")
                    .build())
                .link("space", Link.builder()
                    .href("/v2/spaces/2f35885d-0c9d-4423-83ad-fd05066f8576")
                    .build())
                .build();
        }

        @Override
        protected UpdateProcessRequest getValidRequest() {
            return UpdateProcessRequest.builder()
                .processId("test-process-id")
                .command("rackup")
                .build();
        }

        @Override
        protected Mono<UpdateProcessResponse> invoke(UpdateProcessRequest request) {
            return this.processes.update(request);
        }
    }

}
