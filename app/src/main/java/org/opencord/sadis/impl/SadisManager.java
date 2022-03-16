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

import com.google.common.collect.Lists;
import org.onosproject.codec.CodecService;
import org.onosproject.core.ApplicationId;
import org.onosproject.core.CoreService;
import org.onosproject.net.config.ConfigFactory;
import org.onosproject.net.config.NetworkConfigEvent;
import org.onosproject.net.config.NetworkConfigListener;
import org.onosproject.net.config.NetworkConfigRegistry;
import org.opencord.sadis.BandwidthProfileInformation;
import org.opencord.sadis.BaseInformationService;
import org.opencord.sadis.SadisService;
import org.opencord.sadis.SubscriberAndDeviceInformation;
import org.opencord.sadis.UniTagInformation;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

@Component(immediate = true)
public class SadisManager implements SadisService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String SADIS_APP = "org.opencord.sadis";

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected CoreService coreService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected NetworkConfigRegistry cfgService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    protected CodecService codecService;

    private final InternalConfigListener cfgListener = new InternalConfigListener();

    private SubscriberManager subscriberManager;
    private BandwidthProfileManager bandwidthProfileManager;

    private List<InformationAdapter> internalServices = Lists.newArrayList();

    @Activate
    protected void activate() {
        ApplicationId appId = this.coreService.registerApplication(SADIS_APP);
        cfgService.addListener(this.cfgListener);

        subscriberManager = new SubscriberManager(appId);
        bandwidthProfileManager = new BandwidthProfileManager(appId);

        internalServices.add(subscriberManager);
        internalServices.add(bandwidthProfileManager);

        registerAdapters();

        codecService.registerCodec(UniTagInformation.class, new UniTagInformationCodec());

        log.info("Started");
    }

    private void registerAdapters() {
        internalServices.forEach(service -> {
            registerConfigFactory(service.getConfigFactories());
            registerCodec(service);
            service.updateConfig(cfgService);
        });
    }

    @Deactivate
    protected void deactivate() {
        cfgService.removeListener(cfgListener);
        log.info("Stopped");
    }

    private void registerConfigFactory(Set<ConfigFactory> factories) {
        factories.forEach(cfgService::registerConfigFactory);
    }

    private void registerCodec(InformationAdapter service) {
        codecService.registerCodec(service.getInformationClass(), service.getCodec());
    }


    @Override
    public BaseInformationService<SubscriberAndDeviceInformation> getSubscriberInfoService() {
        return subscriberManager;
    }

    @Override
    public BaseInformationService<BandwidthProfileInformation> getBandwidthProfileService() {
        return bandwidthProfileManager;
    }

    /**
     * Listener for SADIS configuration events.
     */
    private class InternalConfigListener implements NetworkConfigListener {

        @Override
        public void event(final NetworkConfigEvent event) {

            if ((event.type() == NetworkConfigEvent.Type.CONFIG_ADDED
                    || event.type() == NetworkConfigEvent.Type.CONFIG_UPDATED)) {

                internalServices.forEach(adapter -> {
                    if (event.configClass().equals(adapter.getConfigClass())) {
                        adapter.updateConfig(cfgService);
                        log.info("Reconfigured");
                    }
                });
            }


        }
    }
}

