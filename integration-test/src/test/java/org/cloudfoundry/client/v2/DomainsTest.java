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

package org.cloudfoundry.client.v2;

import org.cloudfoundry.AbstractIntegrationTest;
import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.client.v2.applications.CreateApplicationRequest;
import org.cloudfoundry.client.v2.applications.CreateApplicationResponse;
import org.cloudfoundry.client.v2.domains.CreateDomainRequest;
import org.cloudfoundry.client.v2.domains.CreateDomainResponse;
import org.cloudfoundry.client.v2.domains.DeleteDomainRequest;
import org.cloudfoundry.client.v2.domains.DeleteDomainResponse;
import org.cloudfoundry.client.v2.domains.DomainEntity;
import org.cloudfoundry.client.v2.domains.DomainResource;
import org.cloudfoundry.client.v2.domains.GetDomainRequest;
import org.cloudfoundry.client.v2.domains.GetDomainResponse;
import org.cloudfoundry.client.v2.domains.ListDomainSpacesRequest;
import org.cloudfoundry.client.v2.domains.ListDomainsRequest;
import org.cloudfoundry.client.v2.organizations.AssociateOrganizationUserRequest;
import org.cloudfoundry.client.v2.organizations.AssociateOrganizationUserResponse;
import org.cloudfoundry.client.v2.routes.AssociateRouteApplicationRequest;
import org.cloudfoundry.client.v2.routes.AssociateRouteApplicationResponse;
import org.cloudfoundry.client.v2.routes.CreateRouteRequest;
import org.cloudfoundry.client.v2.routes.CreateRouteResponse;
import org.cloudfoundry.client.v2.spaces.AssociateSpaceDeveloperRequest;
import org.cloudfoundry.client.v2.spaces.AssociateSpaceDeveloperResponse;
import org.cloudfoundry.client.v2.spaces.GetSpaceRequest;
import org.cloudfoundry.client.v2.spaces.GetSpaceResponse;
import org.cloudfoundry.client.v2.spaces.SpaceEntity;
import org.cloudfoundry.client.v2.spaces.SpaceResource;
import org.cloudfoundry.util.JobUtils;
import org.cloudfoundry.util.PaginationUtils;
import org.cloudfoundry.util.ResourceUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.tuple.Tuple2;

import java.util.function.Consumer;

import static org.cloudfoundry.util.OperationUtils.thenKeep;
import static org.cloudfoundry.util.tuple.TupleUtils.consumer;
import static org.cloudfoundry.util.tuple.TupleUtils.function;
import static org.junit.Assert.assertEquals;

public final class DomainsTest extends AbstractIntegrationTest {

    @Autowired
    private CloudFoundryClient cloudFoundryClient;

    @Autowired
    private Mono<String> organizationId;

    @Autowired
    private Mono<String> spaceId;

    @Autowired
    private Mono<String> userId;

    @Test
    public void create() {
        String domainName = getDomainName();

        this.organizationId
            .then(organizationId -> Mono
                .when(
                    createDomainEntity(this.cloudFoundryClient, organizationId, domainName),
                    Mono.just(organizationId)
                ))
            .subscribe(this.<Tuple2<DomainEntity, String>>testSubscriber()
                .assertThat(entityMatchesDomainNameAndOrganizationId(domainName)));
    }

    @Test
    public void delete() {
        String domainName = getDomainName();

        this.organizationId
            .then(organizationId -> createDomainId(this.cloudFoundryClient, domainName, organizationId))
            .as(thenKeep(domainId -> requestDeleteDomain(this.cloudFoundryClient, domainId)
                .then(job -> JobUtils.waitForCompletion(this.cloudFoundryClient, job))))
            .then(domainId -> requestGetDomain(this.cloudFoundryClient, domainId))
            .subscribe(testSubscriber()
                .assertErrorMatch(CloudFoundryException.class, "CF-DomainNotFound\\([0-9]+\\): The domain could not be found: .*"));
    }

    @Test
    public void deleteNotAsync() {
        String domainName = getDomainName();

        this.organizationId
            .then(organizationId -> createDomainId(this.cloudFoundryClient, domainName, organizationId))
            .as(thenKeep(domainId -> requestDeleteDomainAsyncFalse(this.cloudFoundryClient, domainId)))
            .then(domainId -> requestGetDomain(this.cloudFoundryClient, domainId))
            .subscribe(testSubscriber()
                .assertErrorMatch(CloudFoundryException.class, "CF-DomainNotFound\\([0-9]+\\): The domain could not be found: .*"));
    }

    @Test
    public void get() {
        String domainName = getDomainName();

        this.organizationId
            .then(organizationId -> Mono
                .when(
                    Mono.just(organizationId),
                    createDomainId(this.cloudFoundryClient, domainName, organizationId)
                ))
            .then(function((organizationId, domainId) -> Mono
                .when(
                    getDomainEntity(this.cloudFoundryClient, domainId),
                    Mono.just(organizationId)
                )))
            .subscribe(this.<Tuple2<DomainEntity, String>>testSubscriber()
                .assertThat(entityMatchesDomainNameAndOrganizationId(domainName)));
    }

    @Test
    public void list() {
        String domainName = getDomainName();

        this.organizationId
            .then(organizationId -> Mono
                .when(
                    Mono.just(organizationId),
                    createDomainId(this.cloudFoundryClient, domainName, organizationId)
                ))
            .then(function((organizationId, domainId) -> Mono
                .when(
                    requestListDomains(cloudFoundryClient)
                        .filter(resource -> domainId.equals(ResourceUtils.getId(resource)))
                        .single()
                        .map(ResourceUtils::getEntity),
                    Mono.just(organizationId)
                )))
            .subscribe(this.<Tuple2<DomainEntity, String>>testSubscriber()
                .assertThat(entityMatchesDomainNameAndOrganizationId(domainName)));
    }

    @Test
    public void listDomainSpaces() {
        String domainName = getDomainName();

        Mono
            .when(this.organizationId, this.spaceId)
            .then(function((organizationId, spaceId) -> Mono
                .when(
                    createDomainId(this.cloudFoundryClient, domainName, organizationId),
                    Mono.just(spaceId)
                )))
            .then(function((domainId, spaceId) -> Mono
                .when(
                    requestListDomainSpaces(cloudFoundryClient, domainId)
                        .filter(resource -> spaceId.equals(ResourceUtils.getId(resource)))
                        .single()
                        .map(ResourceUtils::getId),
                    Mono.just(spaceId)
                )))
            .subscribe(this.<Tuple2<String, String>>testSubscriber()
                .assertThat(this::assertTupleEquality));
    }

    @Test
    public void listDomainSpacesFilterByApplicationId() {
        String applicationName = getApplicationName();
        String domainName = getDomainName();

        this.organizationId
            .then(organizationId -> createDomainId(this.cloudFoundryClient, domainName, organizationId))
            .and(this.spaceId)
            .then(function((domainId, spaceId) -> Mono
                .when(
                    Mono.just(domainId),
                    Mono.just(spaceId),
                    getApplicationId(cloudFoundryClient, applicationName, spaceId),
                    getRouteId(cloudFoundryClient, domainId, spaceId)
                )))
            .as(thenKeep(function((domainId, spaceId, applicationId, routeId) -> requestAssociateRouteApplication(cloudFoundryClient, applicationId, routeId)
            )))
            .then(function((domainId, spaceId, applicationId, routeId) -> Mono
                .when(
                    requestListDomainSpacesByApplicationId(cloudFoundryClient, applicationId, domainId)
                        .filter(resource -> spaceId.equals(ResourceUtils.getId(resource)))
                        .single()
                        .map(ResourceUtils::getId),
                    Mono.just(spaceId)
                )))
            .subscribe(this.<Tuple2<String, String>>testSubscriber()
                .assertThat(this::assertTupleEquality));
    }

    @Test
    public void listDomainSpacesFilterByDeveloperId() {
        String domainName = getDomainName();

        Mono
            .when(this.spaceId, this.organizationId, this.userId)
            .then(function((spaceId, organizationId, userId) -> Mono
                .when(
                    createDomainId(this.cloudFoundryClient, domainName, organizationId),
                    requestAssociateOrganizationUser(cloudFoundryClient, organizationId, userId),
                    Mono.just(spaceId),
                    Mono.just(userId)
                )))
            .as(thenKeep(function((domainId, response, spaceId, userId) -> requestAssociateSpaceDeveloper(cloudFoundryClient, spaceId, userId))))
            .then(function((domainId, response, spaceId, userId) -> requestListSpaceDevelopers(cloudFoundryClient, domainId, userId)
                .filter(resource -> spaceId.equals(ResourceUtils.getId(resource)))
                .single()
                .map(ResourceUtils::getId)
                .and(Mono.just(spaceId))))
            .subscribe(this.<Tuple2<String, String>>testSubscriber()
                .assertThat(this::assertTupleEquality));

    }

    @Test
    public void listDomainSpacesFilterByName() {
        String domainName = getDomainName();

        Mono
            .when(this.organizationId, this.spaceId)
            .then(function((organizationId, spaceId) -> Mono
                .when(
                    Mono.just(spaceId),
                    getSpaceName(this.cloudFoundryClient, spaceId),
                    createDomainId(this.cloudFoundryClient, domainName, organizationId)
                )))
            .then(function((spaceId, spaceName, domainId) -> Mono
                .when(
                    requestListDomainSpacesBySpaceName(cloudFoundryClient, domainId, spaceName)
                        .filter(resource -> spaceId.equals(ResourceUtils.getId(resource)))
                        .single()
                        .map(ResourceUtils::getId),
                    Mono.just(spaceId)
                )))
            .subscribe(this.<Tuple2<String, String>>testSubscriber()
                .assertThat(this::assertTupleEquality));
    }

    @Test
    public void listDomainSpacesFilterByOrganizationId() {
        String domainName = getDomainName();

        Mono
            .when(this.organizationId, this.spaceId)
            .then(function((organizationId, spaceId) -> Mono
                .when(
                    createDomainId(this.cloudFoundryClient, domainName, organizationId),
                    Mono.just(organizationId),
                    Mono.just(spaceId)
                )))
            .then(function((domainId, organizationId, spaceId) -> Mono
                .when(
                    requestListDomainSpacesByOrganizationId(cloudFoundryClient, domainId, organizationId)
                        .filter(resource -> spaceId.equals(ResourceUtils.getId(resource)))
                        .single()
                        .map(ResourceUtils::getId),
                    Mono.just(spaceId)
                )))
            .subscribe(this.<Tuple2<String, String>>testSubscriber()
                .assertThat(this::assertTupleEquality));
    }

    @Test
    public void listFilterByName() {
        String domainName = getDomainName();

        this.organizationId
            .then(organizationId -> Mono
                .when(
                    Mono.just(organizationId),
                    createDomainId(this.cloudFoundryClient, domainName, organizationId)
                ))
            .then(function((organizationId, domainId) -> Mono
                .when(
                    requestListDomains(cloudFoundryClient, domainName)
                        .filter(resource -> domainId.equals(ResourceUtils.getId(resource)))
                        .single()
                        .map(ResourceUtils::getEntity),
                    Mono.just(organizationId)
                )))
            .subscribe(this.<Tuple2<DomainEntity, String>>testSubscriber()
                .assertThat(entityMatchesDomainNameAndOrganizationId(domainName)));
    }

    @Test
    public void listFilterByOwningOrganizationId() {
        String domainName = getDomainName();

        this.organizationId
            .then(organizationId -> Mono
                .when(
                    Mono.just(organizationId),
                    createDomainId(this.cloudFoundryClient, domainName, organizationId)
                ))
            .then(function((organizationId, domainId) -> Mono
                .when(
                    requestListDomainsByOwningOrganization(cloudFoundryClient, organizationId)
                        .filter(resource -> domainId.equals(ResourceUtils.getId(resource)))
                        .single()
                        .map(ResourceUtils::getEntity),
                    Mono.just(organizationId)
                )))
            .subscribe(this.<Tuple2<DomainEntity, String>>testSubscriber()
                .assertThat(entityMatchesDomainNameAndOrganizationId(domainName)));
    }

    private static Mono<DomainEntity> createDomainEntity(CloudFoundryClient cloudFoundryClient, String organizationId, String domainName) {
        return requestCreateDomain(cloudFoundryClient, organizationId, domainName)
            .map(ResourceUtils::getEntity);
    }

    private static Mono<String> createDomainId(CloudFoundryClient cloudFoundryClient, String domainName, String organizationId) {
        return requestCreateDomain(cloudFoundryClient, organizationId, domainName)
            .map(ResourceUtils::getId);
    }

    private static Consumer<Tuple2<DomainEntity, String>> entityMatchesDomainNameAndOrganizationId(String domainName) {
        return consumer((entity, organizationId) -> {
            assertEquals(domainName, entity.getName());
            assertEquals(organizationId, entity.getOwningOrganizationId());
        });
    }

    private static Mono<String> getApplicationId(CloudFoundryClient cloudFoundryClient, String applicationName, String spaceId) {
        return requestCreateApplication(cloudFoundryClient, spaceId, applicationName)
            .map(ResourceUtils::getId);
    }

    private static Mono<DomainEntity> getDomainEntity(CloudFoundryClient cloudFoundryClient, String domainId) {
        return requestGetDomain(cloudFoundryClient, domainId)
            .map(ResourceUtils::getEntity);
    }

    private static Mono<String> getRouteId(CloudFoundryClient cloudFoundryClient, String domainId, String spaceId) {
        return requestCreateRoute(cloudFoundryClient, domainId, spaceId)
            .map(ResourceUtils::getId);
    }

    private static Mono<String> getSpaceName(CloudFoundryClient cloudFoundryClient, String spaceId) {
        return requestSpace(cloudFoundryClient, spaceId)
            .map(ResourceUtils::getEntity)
            .map(SpaceEntity::getName);
    }

    private static Mono<AssociateOrganizationUserResponse> requestAssociateOrganizationUser(CloudFoundryClient cloudFoundryClient, String organizationId, String userId) {
        return cloudFoundryClient.organizations()
            .associateUser(AssociateOrganizationUserRequest.builder()
                .organizationId(organizationId)
                .userId(userId)
                .build());
    }

    private static Mono<AssociateRouteApplicationResponse> requestAssociateRouteApplication(CloudFoundryClient cloudFoundryClient, String applicationId, String routeId) {
        return cloudFoundryClient.routes()
            .associateApplication(AssociateRouteApplicationRequest.builder()
                .routeId(routeId)
                .applicationId(applicationId)
                .build());
    }

    private static Mono<AssociateSpaceDeveloperResponse> requestAssociateSpaceDeveloper(CloudFoundryClient cloudFoundryClient, String spaceId, String developerId) {
        return cloudFoundryClient.spaces()
            .associateDeveloper(AssociateSpaceDeveloperRequest.builder()
                .spaceId(spaceId)
                .developerId(developerId)
                .build());
    }

    private static Mono<CreateApplicationResponse> requestCreateApplication(CloudFoundryClient cloudFoundryClient, String spaceId, String applicationName) {
        return cloudFoundryClient.applicationsV2()
            .create(CreateApplicationRequest.builder()
                .name(applicationName)
                .spaceId(spaceId)
                .build());
    }

    private static Mono<CreateDomainResponse> requestCreateDomain(CloudFoundryClient cloudFoundryClient, String organizationId, String domainName) {
        return cloudFoundryClient.domains()
            .create(CreateDomainRequest.builder()
                .name(domainName)
                .owningOrganizationId(organizationId)
                .wildcard(true)
                .build());
    }

    private static Mono<CreateRouteResponse> requestCreateRoute(CloudFoundryClient cloudFoundryClient, String domainId, String spaceId) {
        return cloudFoundryClient.routes()
            .create(CreateRouteRequest.builder()
                .domainId(domainId)
                .spaceId(spaceId)
                .build());
    }

    private static Mono<DeleteDomainResponse> requestDeleteDomain(CloudFoundryClient cloudFoundryClient, String domainId) {
        return cloudFoundryClient.domains()
            .delete(DeleteDomainRequest.builder()
                .async(true)
                .domainId(domainId)
                .build());
    }

    private static Mono<DeleteDomainResponse> requestDeleteDomainAsyncFalse(CloudFoundryClient cloudFoundryClient, String domainId) {
        return cloudFoundryClient.domains()
            .delete(DeleteDomainRequest.builder()
                .async(false)
                .domainId(domainId)
                .build());
    }

    private static Mono<GetDomainResponse> requestGetDomain(CloudFoundryClient cloudFoundryClient, String domainId) {
        return cloudFoundryClient.domains()
            .get(GetDomainRequest.builder()
                .domainId(domainId)
                .build());
    }

    private static Flux<SpaceResource> requestListDomainSpaces(CloudFoundryClient cloudFoundryClient, String domainId) {
        return PaginationUtils
            .requestResources(page -> cloudFoundryClient.domains()
                .listSpaces(ListDomainSpacesRequest.builder()
                    .domainId(domainId)
                    .page(page)
                    .build()));
    }

    private static Flux<SpaceResource> requestListDomainSpacesByApplicationId(CloudFoundryClient cloudFoundryClient, String applicationId, String domainId) {
        return PaginationUtils
            .requestResources(page -> cloudFoundryClient.domains()
                .listSpaces(ListDomainSpacesRequest.builder()
                    .page(page)
                    .applicationId(applicationId)
                    .domainId(domainId)
                    .build())
            );
    }

    private static Flux<SpaceResource> requestListDomainSpacesByOrganizationId(CloudFoundryClient cloudFoundryClient, String domainId, String organizationId) {
        return PaginationUtils
            .requestResources(page -> cloudFoundryClient.domains()
                .listSpaces(ListDomainSpacesRequest.builder()
                    .domainId(domainId)
                    .organizationId(organizationId)
                    .page(page)
                    .build()));
    }

    private static Flux<SpaceResource> requestListDomainSpacesBySpaceName(CloudFoundryClient cloudFoundryClient, String domainId, String spaceName) {
        return PaginationUtils
            .requestResources(page -> cloudFoundryClient.domains()
                .listSpaces(ListDomainSpacesRequest.builder()
                    .domainId(domainId)
                    .name(spaceName)
                    .page(page)
                    .build()));
    }

    private static Flux<DomainResource> requestListDomains(CloudFoundryClient cloudFoundryClient, String domainName) {
        return PaginationUtils
            .requestResources(page -> cloudFoundryClient.domains()
                .list(ListDomainsRequest.builder()
                    .name(domainName)
                    .page(page)
                    .build()));
    }

    private static Flux<DomainResource> requestListDomains(CloudFoundryClient cloudFoundryClient) {
        return PaginationUtils
            .requestResources(page -> cloudFoundryClient.domains()
                .list(ListDomainsRequest.builder()
                    .page(page)
                    .build()));
    }

    private static Flux<DomainResource> requestListDomainsByOwningOrganization(CloudFoundryClient cloudFoundryClient, String organizationId) {
        return PaginationUtils
            .requestResources(page -> cloudFoundryClient.domains()
                .list(ListDomainsRequest.builder()
                    .owningOrganizationId(organizationId)
                    .page(page)
                    .build()));
    }

    private static Flux<SpaceResource> requestListSpaceDevelopers(CloudFoundryClient cloudFoundryClient, String domainId, String userId) {
        return PaginationUtils
            .requestResources(page -> cloudFoundryClient.domains()
                .listSpaces(ListDomainSpacesRequest.builder()
                    .page(page)
                    .developerId(userId)
                    .domainId(domainId)
                    .build())
            );
    }

    private static Mono<GetSpaceResponse> requestSpace(CloudFoundryClient cloudFoundryClient, String spaceId) {
        return cloudFoundryClient.spaces()
            .get(GetSpaceRequest.builder()
                .spaceId(spaceId)
                .build());
    }

}

