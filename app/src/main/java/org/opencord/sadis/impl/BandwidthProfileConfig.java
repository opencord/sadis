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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.opencord.sadis.BandwidthProfileInformation;
import org.opencord.sadis.BaseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Configuration options for the Bandwidth Profile Information Service.
 *
 * <pre>
 * "bandwidthprofile" : {
 *     "integration" : {
 *         "url" : "http://{hostname}{:port}",
 *         "cache" : {
 *             "enabled"  : true or false
 *             "maxsize"  : number of entries,
 *             "entryttl" : duration, i.e. 10s or 1m
 *         }
 *     },
 *     "entries" : [
 *         {
 *             "name"                        : string,
 *             "cir"                         : long,
 *             "cbs"                         : Long,
 *             "eir"                         : long,
 *             "ebs"                         : Long,
 *             "gir"                         : long,
 *         }, ...
 *     ]
 *
 *     OR
 *
 *     "entries" : [
 *          {
 *             "name"                        : string,
 *             "pir"                         : long,
 *             "pbs"                         : Long,
 *             "cir"                         : long,
 *             "cbs"                         : Long,
 *             "gir"                         : long,
 *          },
 *
 *     ]
 * }
 * </pre>
 */
public class BandwidthProfileConfig extends BaseConfig<BandwidthProfileInformation> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<BandwidthProfileInformation> getEntries() {
        List<BandwidthProfileInformation> result = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule());
        final JsonNode entries = this.object.path(ENTRIES);
        entries.forEach(entry -> {
            try {
                result.add(mapper.readValue(entry.toString(), BandwidthProfileInformation.class));
            } catch (IOException e) {
                log.warn("Unable to parse configuration entry, '{}', error: {}", entry, e.getMessage());
            }
        });

        return result;
    }
}
