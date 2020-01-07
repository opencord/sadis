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
package org.opencord.sadis.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.onlab.packet.Ip4Address;
import org.onlab.packet.VlanId;
import org.opencord.sadis.BaseConfig;
import org.opencord.sadis.SubscriberAndDeviceInformation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Configuration options for the Subscriber And Device Information Service.
 *
 * <pre>
 * "sadis" : {
 *     "integration" : {
 *         "url" : "http://{hostname}{:port}",
 *         "cache" : {
 *             "maxsize"  : number of entries,
 *             "entryttl" : duration, i.e. 10s or 1m
 *         }
 *     },
 *     "entries" : [
 *         {
 *             "id"                         : "uniqueid",
 *             "ctag"                       : int,
 *             "stag"                       : int,
 *             "nasportid"                  : string,
 *             "port"                       : int,
 *             "slot"                       : int,
 *             "hardwareidentifier"         : string,
 *             "ipAddress"                  : string,
 *             "nasId"                      : string,
 *             "circuitId"                  : string,
 *             "removeId"                   : string,
 *             "technologyProfileId"        : int,
 *             "upstreamBandwidthProfile"   : string,
 *             "downstreamBandwidthProfile" : string
 *         }, ...
 *     ]
 * }
 * </pre>
 */
public class SubscriberAndDeviceInformationConfig extends BaseConfig<SubscriberAndDeviceInformation> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public List<SubscriberAndDeviceInformation> getEntries() {
        List<SubscriberAndDeviceInformation> result = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(VlanId.class, new VlanIdDeserializer());
        module.addDeserializer(Ip4Address.class, new Ip4AddressDeserializer());
        mapper.registerModule(module);
        final JsonNode entries = this.object.path(ENTRIES);
        entries.forEach(entry -> {
            try {
                result.add(mapper.readValue(entry.toString(), SubscriberAndDeviceInformation.class));
            } catch (IOException e) {
                log.warn("Unable to parse configuration entry, '{}', error: {}", entry, e.getMessage());
            }
        });

        return result;
    }

    public class VlanIdDeserializer extends JsonDeserializer<VlanId> {
        @Override
        public VlanId deserialize(JsonParser jp, DeserializationContext ctxt)
                throws IOException {
            ObjectCodec oc = jp.getCodec();
            JsonNode node = oc.readTree(jp);
            return VlanId.vlanId((short) node.asInt());
        }
    }

    public class Ip4AddressDeserializer extends JsonDeserializer<Ip4Address> {
        @Override
        public Ip4Address deserialize(JsonParser jp, DeserializationContext ctxt)
                throws IOException {
            ObjectCodec oc = jp.getCodec();
            JsonNode node = oc.readTree(jp);
            return Ip4Address.valueOf(node.asText());
        }
    }
}
