/*
 * Copyright 2017-present Open Networking Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opencord.sadis.rest;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.onlab.util.ItemNotFoundException;
import org.onosproject.rest.AbstractWebResource;
import org.opencord.sadis.BandwidthProfileInformation;
import org.opencord.sadis.BaseInformationService;
import org.opencord.sadis.SadisService;

import javax.ws.rs.PathParam;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Bandwidth Profile web resource.
 */
@Path("bpApp")
public class BandwidthProfileWebResource extends AbstractWebResource {
    private final ObjectNode root = mapper().createObjectNode();
    private final ArrayNode node = root.putArray("entry");
    private static final String BP_NOT_FOUND = "Bandwidth Profile not found";
    private final SadisService sadisService = get(SadisService.class);
    private final BaseInformationService<BandwidthProfileInformation> service = sadisService
            .getBandwidthProfileService();

    @GET
    @Path("/bandwidthprofile/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBandwidthProfile(@PathParam("id") String id) {
        final BandwidthProfileInformation entry = service.get(id);
        if (entry == null) {
            throw new ItemNotFoundException(BP_NOT_FOUND);
        }
        node.add(codec(BandwidthProfileInformation.class).encode(entry, this));
        return ok(root).build();
    }

    @GET
    @Path("/cache/bandwidthprofile/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBandwidthProfileCache(@PathParam("id") String id) {
        final BandwidthProfileInformation entry = service.getfromCache(id);
        if (entry == null) {
            throw new ItemNotFoundException(BP_NOT_FOUND);
        }
        node.add(codec(BandwidthProfileInformation.class).encode(entry, this));
        return ok(root).build();
    }

    @DELETE
    @Path("/cache/bandwidthprofile/{id}")
    public Response deleteBandwidthProfile(@PathParam("id") String id) {
        service.invalidateAll();
        return Response.noContent().build();
    }

    @DELETE
    @Path("/cache/bandwidthprofile/")
    public Response deleteAllSubscribers() {
        service.invalidateAll();
        return Response.noContent().build();
    }
}
