/*
 * Copyright 2017-present Open Networking Laboratory
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
package org.opencord.sadis;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.onlab.packet.Ip4Address;
import org.onlab.packet.VlanId;
import org.onosproject.core.ApplicationId;
import org.onosproject.net.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
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
 *             "enable"   : true or false,
 *             "maxsize"  : number of entries,
 *             "entryttl" : duration, i.e. 10s or 1m
 *         }
 *     },
 *     "entries" : [
 *         {
 *             "id"                 : "uniqueid",
 *             "ctag"               : int,
 *             "stag"               : int,
 *             "nasportid"          : string,
 *             "port"               : int,
 *             "slot"               : int,
 *             "hardwareidentifier" : string,
 *             "ipAddress"          : string,
 *             "nasId"              : string
 *         }, ...
 *     ]
 * }
 * </pre>
 */
public class SadisConfig extends Config<ApplicationId> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String SADIS_INTEGRATION = "integration";
    private static final String SADIS_CACHE = "cache";
    private static final String SADIS_CACHE_ENABLED = "enabled";
    private static final String SADIS_CACHE_SIZE = "maxsize";
    private static final String SADIS_CACHE_TTL = "ttl";
    private static final String SADIS_URL = "url";
    private static final String SADIS_ENTRIES = "entries";
    private static final String DEFAULT_CACHE_TTL = "PT0S";

    /**
     * Returns SADIS integration URL.
     *
     * @return configured URL or null
     * @throws MalformedURLException
     *             specified URL not valid
     */
    public URL getUrl() throws MalformedURLException {
        final JsonNode integration = this.object.path(SADIS_INTEGRATION);
        if (integration.isMissingNode()) {
            return null;
        }

        final JsonNode url = integration.path(SADIS_URL);
        if (url.isMissingNode()) {
            return null;
        }

        return new URL(url.asText());
    }

    /**
     * Returns configuration if cache should be used or not. Only used if
     * integration URL is set.
     *
     * @return if cache should be used
     */
    public Boolean getCacheEnabled() {
        final JsonNode integration = this.object.path(SADIS_INTEGRATION);
        if (integration.isMissingNode()) {
            return false;
        }

        final JsonNode cache = integration.path(SADIS_CACHE);
        if (cache.isMissingNode()) {
            return false;
        }

        return cache.path(SADIS_CACHE_ENABLED).asBoolean(false);
    }

    public int getCacheMaxSize() {
        final JsonNode integration = this.object.path(SADIS_INTEGRATION);
        if (integration.isMissingNode()) {
            return -1;
        }

        final JsonNode cache = integration.path(SADIS_CACHE);
        if (cache.isMissingNode()) {
            return -1;
        }

        return cache.path(SADIS_CACHE_SIZE).asInt(-1);
    }

    public Duration getCacheTtl() {
        final JsonNode integration = this.object.path(SADIS_INTEGRATION);
        if (integration.isMissingNode()) {
            return Duration.parse(DEFAULT_CACHE_TTL);
        }

        final JsonNode cache = integration.path(SADIS_CACHE);
        if (cache.isMissingNode()) {
            return Duration.parse(DEFAULT_CACHE_TTL);
        }

        return Duration.parse(cache.path(SADIS_CACHE_TTL).asText(DEFAULT_CACHE_TTL));
    }

    public List<SubscriberAndDeviceInformation> getEntries() {
        List<SubscriberAndDeviceInformation> result = new ArrayList<SubscriberAndDeviceInformation>();
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(VlanId.class, new VlanIdDeserializer());
        module.addDeserializer(Ip4Address.class, new Ip4AddressDeserializer());
        mapper.registerModule(module);
        final JsonNode entries = this.object.path(SADIS_ENTRIES);
        entries.forEach(entry -> {
            try {
                result.add(mapper.readValue(entry.toString(), SubscriberAndDeviceInformation.class));
            } catch (IOException e) {
                log.warn("Unable to parse configuration entry, '{}', error: {}", entry.toString(), e.getMessage());
            }
        });

        return result;
    }

    public class VlanIdDeserializer extends JsonDeserializer<VlanId> {
        @Override
        public VlanId deserialize(JsonParser jp, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            ObjectCodec oc = jp.getCodec();
            JsonNode node = oc.readTree(jp);
            return VlanId.vlanId((short) node.asInt());
        }
    }

    public class Ip4AddressDeserializer extends JsonDeserializer<Ip4Address> {
        @Override
        public Ip4Address deserialize(JsonParser jp, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            ObjectCodec oc = jp.getCodec();
            JsonNode node = oc.readTree(jp);
            return Ip4Address.valueOf((String) node.asText());
        }
    }
}
