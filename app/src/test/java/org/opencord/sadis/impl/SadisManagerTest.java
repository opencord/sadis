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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.onlab.packet.Ip4Address;
import org.onlab.packet.MacAddress;
import org.onlab.packet.VlanId;
import org.onosproject.core.ApplicationId;
import org.onosproject.core.CoreServiceAdapter;
import org.onosproject.net.config.Config;
import org.onosproject.net.config.ConfigApplyDelegate;
import org.onosproject.net.config.NetworkConfigRegistryAdapter;
import org.onosproject.codec.impl.CodecManager;

import org.opencord.sadis.SubscriberAndDeviceInformation;

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
        this.sadis.codecService = new CodecManager();
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
        assertEquals(3, entries.size());
        assertTrue(SubscriberAndDeviceInformationBuilder.build("1", (short) 2, (short) 2, "1/1/2", (short) 125,
                (short) 3, "aa:bb:cc:dd:ee:ff", "XXX-NASID", "10.10.10.10").checkEquals(entries.get(0)));
        assertTrue(SubscriberAndDeviceInformationBuilder.build("2", (short) 4, (short) 4, "1/1/2", (short) 129,
                (short) 4, "aa:bb:cc:dd:ee:ff", "YYY-NASID", "1.1.1.1").checkEquals(entries.get(1)));
        assertTrue(SubscriberAndDeviceInformationBuilder.build("cc:dd:ee:ff:aa:bb", (short) -1, (short) -1, null,
                (short) -1,
                (short) -1, "cc:dd:ee:ff:aa:bb", "CCC-NASID", "12.12.12.12").checkEquals(entries.get(2)));

    }

    // Mocks live here

    private static final class SubscriberAndDeviceInformationBuilder extends SubscriberAndDeviceInformation {

        public static SubscriberAndDeviceInformationBuilder build(String id, short cTag, short sTag, String nasPortId,
                short port, short slot, String mac, String nasId, String ipAddress) {
            //SubscriberAndDeviceInformation info = new SubscriberAndDeviceInformation();
            SubscriberAndDeviceInformationBuilder info = new SubscriberAndDeviceInformationBuilder();
            info.setId(id);
            if (cTag != -1) {
                info.setCTag(VlanId.vlanId(cTag));
            }
            if (sTag != -1) {
                info.setSTag(VlanId.vlanId(sTag));
            }
            info.setNasPortId(nasPortId);
            if (port != -1) {
                info.setPort(port);
            }
            if (slot != -1) {
                info.setSlot(slot);
            }
            info.setHardwareIdentifier(MacAddress.valueOf(mac));
            info.setIPAddress(Ip4Address.valueOf(ipAddress));
            info.setNasId(nasId);
            return info;
        }

        public boolean checkEquals(SubscriberAndDeviceInformation other) {
            if (other == null) {
                return false;
            }
            if (this.cTag() == null) {
                if (other.cTag() != null) {
                    return false;
                }
            } else if (!this.cTag().equals(other.cTag())) {
                return false;
            }
            if (this.hardwareIdentifier() == null) {
                if (other.hardwareIdentifier() != null) {
                    return false;
                }
            } else if (!this.hardwareIdentifier().equals(other.hardwareIdentifier())) {
                return false;
            }
            if (this.id() == null) {
                if (other.id() != null) {
                    return false;
                }
            } else if (!this.id().equals(other.id())) {
                return false;
            }
            if (this.nasPortId() == null) {
                if (other.nasPortId() != null) {
                    return false;
                }
            } else if (!this.nasPortId().equals(other.nasPortId())) {
                return false;
            }
            if (this.nasId() == null) {
                if (other.nasId() != null) {
                    return false;
                }
            } else if (!this.nasId().equals(other.nasId())) {
                return false;
            }
            if (this.ipAddress() == null) {
                if (other.ipAddress() != null) {
                    return false;
                }
            } else if (!this.ipAddress().equals(other.ipAddress())) {
                return false;
            }
            if (this.port() != other.port()) {
                return false;
            }
            if (this.sTag() == null) {
                if (other.sTag() != null) {
                    return false;
                }
            } else if (!this.sTag().equals(other.sTag())) {
                return false;
            }
            if (this.slot() != other.slot()) {
                return false;
            }
            return true;
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
