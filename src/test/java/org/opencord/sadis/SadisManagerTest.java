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

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.onlab.packet.MacAddress;
import org.onlab.packet.VlanId;
import org.onosproject.core.ApplicationId;
import org.onosproject.core.CoreServiceAdapter;
import org.onosproject.net.config.Config;
import org.onosproject.net.config.ConfigApplyDelegate;
import org.onosproject.net.config.NetworkConfigRegistryAdapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Set of tests of the SADIS ONOS application component.
 */
public class SadisManagerTest {

    private SadisManager sadis;

    @Before
    public void setUp() throws Exception {
        this.sadis = new SadisManager();
        this.sadis.coreService = new MockCoreService();

        final InputStream jsonStream = SadisManagerTest.class.getResourceAsStream("/config.json");

        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode testConfig = mapper.readTree(jsonStream);
        final ConfigApplyDelegate delegate = new MockConfigDelegate();

        final SadisConfig config = new SadisConfig();
        final ApplicationId subject = this.sadis.coreService.registerApplication("org.opencord.sadis");

        config.init(subject, "sadis-test", testConfig, mapper, delegate);

        this.sadis.cfgService = new MockNetworkConfigRegistry(config);
        this.sadis.activate();
    }

    @After
    public void tearDown() {
        this.sadis.deactivate();
    }

    @Test
    public void testConfiguration() {
        SadisConfig config = sadis.cfgService.getConfig(null, SadisConfig.class);
        assertEquals(true, config.getCacheEnabled());
        assertEquals(50, config.getCacheMaxSize());
        assertEquals(Duration.parse("PT1m"), config.getCacheTtl());
        List<SubscriberAndDeviceInformation> entries = config.getEntries();
        assertEquals(2, entries.size());
        assertEquals(SubscriberAndDeviceInformationBuilder.build("1", (short) 2, (short) 2, "1/1/2", (short) 125,
                (short) 3, "aa:bb:cc:dd:ee:ff"), entries.get(0));
        assertEquals(SubscriberAndDeviceInformationBuilder.build("2", (short) 4, (short) 4, "1/1/2", (short) 129,
                (short) 4, "aa:bb:cc:dd:ee:ff"), entries.get(1));

    }

    // Mocks live here

    private static final class SubscriberAndDeviceInformationBuilder extends SubscriberAndDeviceInformation {

        public static SubscriberAndDeviceInformation build(String id, short cTag, short sTag, String nasPortId,
                short port, short slot, String mac) {
            SubscriberAndDeviceInformation info = new SubscriberAndDeviceInformation();
            info.setId(id);
            info.setCTag(VlanId.vlanId(cTag));
            info.setSTag(VlanId.vlanId(sTag));
            info.setNasPortId(nasPortId);
            info.setPort(port);
            info.setSlot(slot);
            info.setHardwareIdentifier(MacAddress.valueOf(mac));
            return info;
        }
    }

    /**
     * Mocks an ONOS configuration delegate to allow JSON based configuration to
     * be tested.
     */
    private static final class MockConfigDelegate implements ConfigApplyDelegate {
        @Override
        public void onApply(@SuppressWarnings("rawtypes") Config config) {
            config.apply();
        }
    }

    /**
     * Mocks an instance of {@link ApplicationId} so that the application
     * component under test can query and use its application ID.
     */
    private static final class MockApplicationId implements ApplicationId {

        private final short id;
        private final String name;

        public MockApplicationId(short id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public short id() {
            return id;
        }

        @Override
        public String name() {
            return name;
        }
    }

    /**
     * Mocks the core services of ONOS so that the application under test can
     * register and query application IDs.
     */
    private static final class MockCoreService extends CoreServiceAdapter {

        private List<ApplicationId> idList = new ArrayList<ApplicationId>();
        private Map<String, ApplicationId> idMap = new HashMap<String, ApplicationId>();

        /*
         * (non-Javadoc)
         *
         * @see
         * org.onosproject.core.CoreServiceAdapter#getAppId(java.lang.Short)
         */
        @Override
        public ApplicationId getAppId(Short id) {
            if (id >= idList.size()) {
                return null;
            }
            return idList.get(id);
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * org.onosproject.core.CoreServiceAdapter#getAppId(java.lang.String)
         */
        @Override
        public ApplicationId getAppId(String name) {
            return idMap.get(name);
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * org.onosproject.core.CoreServiceAdapter#registerApplication(java.lang
         * .String)
         */
        @Override
        public ApplicationId registerApplication(String name) {
            ApplicationId appId = idMap.get(name);
            if (appId == null) {
                appId = new MockApplicationId((short) idList.size(), name);
                idList.add(appId);
                idMap.put(name, appId);
            }
            return appId;
        }

    }

    /**
     * Mocks the ONOS network configuration registry so that the application
     * under test can access a JSON defined configuration.
     */
    static final class MockNetworkConfigRegistry extends NetworkConfigRegistryAdapter {
        private final SadisConfig config;

        public MockNetworkConfigRegistry(final SadisConfig config) {
            this.config = config;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <S, C extends Config<S>> C getConfig(final S subject, final Class<C> configClass) {
            return (C) this.config;
        }
    }
}
