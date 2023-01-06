/*
 * Copyright 2017-2023 Open Networking Foundation (ONF) and the ONF Contributors
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

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.onosproject.rest.AbstractWebResource;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.opencord.sadis.BandwidthProfileInformation;
import org.opencord.sadis.BaseInformationService;
import org.opencord.sadis.SadisService;
import org.opencord.sadis.SubscriberAndDeviceInformation;
import org.onlab.util.ItemNotFoundException;

import javax.ws.rs.GET;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Subscriber And Device Information Service web resource.
 */
@Path("sadis")
public class SadisWebResource extends AbstractWebResource {
    private final ObjectNode root = mapper().createObjectNode();
    private final ArrayNode node = root.putArray("entry");
    private static final String SUBSCRIBER_NOT_FOUND = "Subscriber not found";
    private static final String BP_NOT_FOUND = "Bandwidth Profile not found";

    /**
     * Get subscriber object.
     *
     * @param id
     *            ID of the subscriber
     *
     * @return 200 OK
     */
    @GET
    @Path("/subscriber/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSubscriber(@PathParam("id") String id) {
        SadisService sadisService = get(SadisService.class);
        BaseInformationService<SubscriberAndDeviceInformation> subService =
                sadisService.getSubscriberInfoService();
        final SubscriberAndDeviceInformation entry = subService.get(id);
        if (entry == null) {
           throw new ItemNotFoundException(SUBSCRIBER_NOT_FOUND);
        }
        node.add(codec(SubscriberAndDeviceInformation.class).encode(entry, this));
        return ok(root).build();
    }

    /**
      * Get subscriber object from the cache.
      *
      * @param id
      *            ID of the subscriber
      *
      * @return 200 OK
      */
     @GET
     @Path("/cache/subscriber/{id}")
     @Produces(MediaType.APPLICATION_JSON)
     public Response getSubscriberCache(@PathParam("id") String id) {
         SadisService sadisService = get(SadisService.class);
         BaseInformationService<SubscriberAndDeviceInformation> subService =
                 sadisService.getSubscriberInfoService();
         final SubscriberAndDeviceInformation entry = subService.getfromCache(id);
         if (entry == null) {
            throw new ItemNotFoundException(SUBSCRIBER_NOT_FOUND);
         }
         node.add(codec(SubscriberAndDeviceInformation.class).encode(entry, this));
         return ok(root).build();
     }

    /**
     * Delete subscriber object.
     *
     * @param id
     *            ID of the subscriber
     * @return 204 NoContent
     */
    @DELETE
    @Path("/cache/subscriber/{id}")
    public Response deleteSubscriber(@PathParam("id") String id) {
        SadisService sadisService = get(SadisService.class);
        BaseInformationService<SubscriberAndDeviceInformation> subService =
                sadisService.getSubscriberInfoService();
        subService.invalidateId(id);
        return Response.noContent().build();
    }

    /**
      * Delete all the subscriber objects.
      *
      * @return 204 NoContent
      */
    @DELETE
    @Path("/cache/subscriber/")
    public Response deleteAllSubscribers() {
        SadisService sadisService = get(SadisService.class);
        BaseInformationService<SubscriberAndDeviceInformation> subService =
                sadisService.getSubscriberInfoService();
        subService.invalidateAll();
        return Response.noContent().build();
    }

    @GET
    @Path("/bandwidthprofile/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBandwidthProfile(@PathParam("id") String id) {
        SadisService sadisService = get(SadisService.class);
        BaseInformationService<BandwidthProfileInformation> bpService =
                sadisService.getBandwidthProfileService();
        final BandwidthProfileInformation entry = bpService.get(id);
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
        SadisService sadisService = get(SadisService.class);
        BaseInformationService<BandwidthProfileInformation> bpService =
                sadisService.getBandwidthProfileService();
        final BandwidthProfileInformation entry = bpService.getfromCache(id);
        if (entry == null) {
            throw new ItemNotFoundException(BP_NOT_FOUND);
        }
        node.add(codec(BandwidthProfileInformation.class).encode(entry, this));
        return ok(root).build();
    }

    @DELETE
    @Path("/cache/bandwidthprofile/{id}")
    public Response deleteBandwidthProfile(@PathParam("id") String id) {
        SadisService sadisService = get(SadisService.class);
        BaseInformationService<BandwidthProfileInformation> bpService =
                sadisService.getBandwidthProfileService();
        bpService.invalidateId(id);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/cache/bandwidthprofile/")
    public Response deleteAllBandwidthProfiles() {
        SadisService sadisService = get(SadisService.class);
        BaseInformationService<BandwidthProfileInformation> bpService =
                sadisService.getBandwidthProfileService();
        bpService.invalidateAll();
        return Response.noContent().build();
    }
}
