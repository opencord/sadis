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

import static org.onosproject.net.config.basics.SubjectFactories.APP_SUBJECT_FACTORY;

import java.util.Set;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.Service;
import org.onosproject.core.ApplicationId;
import org.onosproject.core.CoreService;
import org.onosproject.net.config.ConfigFactory;
import org.onosproject.net.config.NetworkConfigEvent;
import org.onosproject.net.config.NetworkConfigListener;
import org.onosproject.net.config.NetworkConfigRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;

/**
 * Subscriber And Device Information Service application component. Component
 * that manages the integration of ONOS into a deployment providing a bridge
 * between ONOS and deployment specific information about subscribers and access
 * devices.
 */
@Service
@Component(immediate = true)
public class SadisManager extends SubscriberAndDeviceInformationAdapter {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String SADIS_APP = "org.opencord.sadis";
    private ApplicationId appId;
    private final InternalConfigListener cfgListener = new InternalConfigListener();

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected CoreService coreService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected NetworkConfigRegistry cfgService;

    @SuppressWarnings("rawtypes")
    private final Set<ConfigFactory> factories = ImmutableSet
            .of(new ConfigFactory<ApplicationId, SadisConfig>(APP_SUBJECT_FACTORY, SadisConfig.class, "sadis") {
                @Override
                public SadisConfig createConfig() {
                    return new SadisConfig();
                }
            });

    /**
     * Initialize the SADIS ONOS application.
     */
    @Activate
    protected void activate() {

        this.appId = this.coreService.registerApplication(SADIS_APP);
        this.cfgService.addListener(this.cfgListener);
        this.factories.forEach(this.cfgService::registerConfigFactory);
        this.updateConfig();

        this.log.info("Started");
    }

    /**
     * Cleans up resources utilized by the SADIS ONOS application.
     */
    @Deactivate
    protected void deactivate() {
        this.log.info("Stopped");
    }

    /**
     * Validates the configuration and updates any operational settings that are
     * affected by configuration changes.
     */
    private void updateConfig() {
        final SadisConfig cfg = this.cfgService.getConfig(this.appId, SadisConfig.class);
        if (cfg == null) {
            this.log.warn("Subscriber And Device Information Service (SADIS) configuration not available");
            return;
        }
        this.log.info("Cache Enabled:  {}", cfg.getCacheEnabled());
        this.log.info("Cache Mac Size: {}", cfg.getCacheMaxSize());
        this.log.info("Cache TTL:      {}", cfg.getCacheTtl().getSeconds());
        this.log.info("Entries:        {}", cfg.getEntries());
    }

    /**
     * Listener for SADIS configuration events.
     */
    private class InternalConfigListener implements NetworkConfigListener {

        @Override
        public void event(final NetworkConfigEvent event) {

            if ((event.type() == NetworkConfigEvent.Type.CONFIG_ADDED
                    || event.type() == NetworkConfigEvent.Type.CONFIG_UPDATED)
                    && event.configClass().equals(SadisConfig.class)) {
                SadisManager.this.updateConfig();
                SadisManager.this.log.info("Reconfigured");
            }
        }
    }
}
