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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableSet;
import org.onosproject.codec.JsonCodec;
import org.onosproject.core.ApplicationId;
import org.onosproject.net.config.ConfigFactory;
import org.opencord.sadis.BandwidthProfileInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.onosproject.net.config.basics.SubjectFactories.APP_SUBJECT_FACTORY;

public class BandwidthProfileManager extends InformationAdapter<BandwidthProfileInformation, BandwidthProfileConfig> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private ApplicationId appId;

    @SuppressWarnings("rawtypes")
    private final Set<ConfigFactory> factories = ImmutableSet
            .of(new ConfigFactory<ApplicationId, BandwidthProfileConfig>(APP_SUBJECT_FACTORY,
                    BandwidthProfileConfig.class, "bandwidthprofile") {
                @Override
                public BandwidthProfileConfig createConfig() {
                    return new BandwidthProfileConfig();
                }
            });

    public BandwidthProfileManager(ApplicationId appId) {
        this.appId = appId;
        this.registerModule();
        this.log.info("Started");
    }


    @Override
    public void registerModule() {
        cache = CacheBuilder.newBuilder().maximumSize(maxiumCacheSize)
                .expireAfterAccess(cacheEntryTtl, TimeUnit.SECONDS).build();
        mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule());
    }

    @Override
    public ApplicationId getAppId() {
        return this.appId;
    }

    @Override
    public Set<ConfigFactory> getConfigFactories() {
        return factories;
    }

    @Override
    public JsonCodec<BandwidthProfileInformation> getCodec() {
        return new BandwidthProfileCodec();
    }

    @Override
    public Class<BandwidthProfileInformation> getInformationClass() {
        return BandwidthProfileInformation.class;
    }

    @Override
    public Class<BandwidthProfileConfig> getConfigClass() {
        return BandwidthProfileConfig.class;
    }

}
