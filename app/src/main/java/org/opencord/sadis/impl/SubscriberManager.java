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

import static org.onosproject.net.config.basics.SubjectFactories.APP_SUBJECT_FACTORY;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.cache.CacheBuilder;
import org.onlab.packet.Ip4Address;
import org.onlab.packet.VlanId;
import org.onosproject.codec.JsonCodec;
import org.onosproject.core.ApplicationId;
import org.onosproject.net.config.ConfigFactory;
import org.opencord.sadis.SubscriberAndDeviceInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;

/**
 * Subscriber And Device Information Service application component. Component
 * that manages the integration of ONOS into a deployment providing a bridge
 * between ONOS and deployment specific information about subscribers and access
 * devices.
 */

public class SubscriberManager extends InformationAdapter<SubscriberAndDeviceInformation,
        SubscriberAndDeviceInformationConfig> {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private ApplicationId appId;

    @SuppressWarnings("rawtypes")
    private final Set<ConfigFactory> factories = ImmutableSet
            .of(new ConfigFactory<ApplicationId, SubscriberAndDeviceInformationConfig>(APP_SUBJECT_FACTORY,
                    SubscriberAndDeviceInformationConfig.class, "sadis") {
                @Override
                public SubscriberAndDeviceInformationConfig createConfig() {
                    return new SubscriberAndDeviceInformationConfig();
                }
            });


    public SubscriberManager(ApplicationId appId) {
        this.appId = appId;
        this.registerModule();
        this.log.info("Started");
    }

    @Override
    public void registerModule() {
        cache = CacheBuilder.newBuilder().maximumSize(maxiumCacheSize)
                .expireAfterAccess(cacheEntryTtl, TimeUnit.SECONDS).build();
        log.info("Cache is {} and this {}", cache, this);
        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        SubscriberAndDeviceInformationConfig config = new SubscriberAndDeviceInformationConfig();
        SubscriberAndDeviceInformationConfig.VlanIdDeserializer vlanID = config.new VlanIdDeserializer();
        SubscriberAndDeviceInformationConfig.Ip4AddressDeserializer ip4Address = config.new Ip4AddressDeserializer();
        module.addDeserializer(VlanId.class, vlanID);
        module.addDeserializer(Ip4Address.class, ip4Address);
        mapper.registerModule(module);
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
    public JsonCodec<SubscriberAndDeviceInformation> getCodec() {
        return new SubscriberAndDeviceInformationCodec();
    }

    @Override
    public Class<SubscriberAndDeviceInformation> getInformationClass() {
        return SubscriberAndDeviceInformation.class;
    }

    @Override
    public Class<SubscriberAndDeviceInformationConfig> getConfigClass() {
        return SubscriberAndDeviceInformationConfig.class;
    }


}
