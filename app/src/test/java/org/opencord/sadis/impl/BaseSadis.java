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
import com.google.common.collect.Lists;
import org.onlab.packet.Ip4Address;
import org.onlab.packet.MacAddress;
import org.onlab.packet.VlanId;
import org.onosproject.codec.impl.CodecManager;
import org.onosproject.core.ApplicationId;
import org.onosproject.core.CoreServiceAdapter;
import org.onosproject.net.config.ConfigApplyDelegate;
import org.onosproject.net.config.NetworkConfigRegistryAdapter;
import org.onosproject.net.config.NetworkConfigListener;
import org.onosproject.net.config.Config;
import org.onosproject.net.config.NetworkConfigEvent;
import org.opencord.sadis.BaseConfig;
import org.opencord.sadis.BaseInformation;
import org.opencord.sadis.BaseInformationService;
import org.opencord.sadis.SubscriberAndDeviceInformation;
import org.opencord.sadis.UniTagInformation;

import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public abstract class BaseSadis {

    protected SadisManager sadis;
    protected ConfigApplyDelegate delegate;
    protected ObjectMapper mapper;
    protected ApplicationId subject;
    protected BaseConfig config;
    protected NetworkConfigEvent event;
    protected static NetworkConfigListener configListener;

    private static final short UNI_TAG_MATCH_1 = 100;
    private static final short UNI_TAG_MATCH_2 = 200;
    private static final short C_TAG_1 = 2;
    private static final short S_TAG_1 = 2;
    private static final short S_TAG_2 = 3;
    private static final int C_TAG_PRIORITY = 0;
    private static final int S_TAG_PRIORITY = 1;
    private static final int TECH_PROF_ID_1 = 64;
    private static final int TECH_PROF_ID_2 = 65;

    private static final String HSA = "HSA";
    private static final String HSIA = "HSIA";
    private static final String HSA_OLT = "HSA_OLT";
    private static final String IPTV = "IPTV";
    private static final String NAS_PORT_ID = "1/1/2";

    private static final short PORT_1 = 125;
    private static final short PORT_2 = 129;
    private static final short PORT_3 = 130;
    private static final short PORT_4 = 132;

    private static final short SLOT_1 = 3;
    private static final short SLOT_2 = 4;
    private static final short SLOT_3 = 7;
    private static final short SLOT_4 = 1;

    private static final String MAC1 = "aa:bb:cc:dd:ee:ff";
    private static final String MAC2 = "ff:aa:dd:cc:bb:ee";
    private static final String MAC3 = "ff:cc:dd:aa:ee:bb";

    private static final String NAS1 = "XXX-NASID";
    private static final String NAS2 = "YYY-NASID";
    private static final String NAS3 = "MNO-NASID";
    private static final String NAS4 = "PQR-NASID";

    private static final String CIRCUIT1 = "circuit123";
    private static final String CIRCUIT2 = "circuit234";
    private static final String CIRCUIT3 = "circuit567";
    private static final String CIRCUIT4 = "circuit678";

    private static final String REMOTE1 = "remote123";
    private static final String REMOTE2 = "remote234";
    private static final String REMOTE3 = "remote567";
    private static final String REMOTE4 = "remote678";

    private static final String IP1 = "10.10.10.10";
    private static final String IP2 = "1.1.1.1";
    private static final String IP3 = "30.30.30.30";
    private static final String IP4 = "15.15.15.15";

    protected static final String ID1 = "1";
    protected static final String ID2 = "2";
    protected static final String ID3 = "3";
    protected static final String ID4 = "4";
    protected static final String ID5 = "5";
    protected static final String ID6 = "6";
    protected static final String EMPTY = "";

    UniTagInformation ttService1 = new UniTagInformation.Builder()
            .setUniTagMatch(VlanId.vlanId(UNI_TAG_MATCH_1))
            .setPonCTag(VlanId.vlanId(C_TAG_1))
            .setPonSTag(VlanId.vlanId(S_TAG_1))
            .setUsPonCTagPriority(C_TAG_PRIORITY)
            .setUsPonSTagPriority(S_TAG_PRIORITY)
            .setDsPonCTagPriority(C_TAG_PRIORITY)
            .setDsPonSTagPriority(S_TAG_PRIORITY)
            .setTechnologyProfileId(TECH_PROF_ID_1)
            .setUpstreamBandwidthProfile(HSA)
            .setDownstreamBandwidthProfile(HSA)
            .setServiceName(HSIA)
            .setConfiguredMacAddress(EMPTY)
            .setUpstreamOltBandwidthProfile(HSA)
            .setDownstreamOltBandwidthProfile(HSA)
            .build();

    UniTagInformation ttService2 = new UniTagInformation.Builder()
            .setUniTagMatch(VlanId.vlanId(UNI_TAG_MATCH_2))
            .setPonCTag(VlanId.vlanId(C_TAG_1))
            .setPonSTag(VlanId.vlanId(S_TAG_2))
            .setUsPonCTagPriority(C_TAG_PRIORITY)
            .setUsPonSTagPriority(S_TAG_PRIORITY)
            .setDsPonCTagPriority(C_TAG_PRIORITY)
            .setDsPonSTagPriority((S_TAG_PRIORITY))
            .setTechnologyProfileId(TECH_PROF_ID_2)
            .setUpstreamBandwidthProfile(IPTV)
            .setDownstreamBandwidthProfile(IPTV)
            .setServiceName(IPTV)
            .setIsIgmpRequired(true)
            .setIsDhcpRequired(true)
            .setEnableMacLearning(true)
            .setConfiguredMacAddress(MAC2)
            .setUpstreamOltBandwidthProfile(IPTV)
            .setDownstreamOltBandwidthProfile(IPTV)
            .build();

    UniTagInformation ttService3 = new UniTagInformation.Builder()
            .setUniTagMatch(VlanId.vlanId(UNI_TAG_MATCH_1))
            .setPonCTag(VlanId.vlanId(C_TAG_1))
            .setPonSTag(VlanId.vlanId(S_TAG_1))
            .setUsPonCTagPriority(C_TAG_PRIORITY)
            .setUsPonSTagPriority(S_TAG_PRIORITY)
            .setDsPonCTagPriority(C_TAG_PRIORITY)
            .setDsPonSTagPriority(S_TAG_PRIORITY)
            .setTechnologyProfileId(TECH_PROF_ID_1)
            .setUpstreamBandwidthProfile(HSA)
            .setDownstreamBandwidthProfile(HSA)
            .setUpstreamOltBandwidthProfile(HSA_OLT)
            .setDownstreamOltBandwidthProfile(HSA_OLT)
            .setServiceName(HSIA)
            .setConfiguredMacAddress(EMPTY)
            .build();

    UniTagInformation attService1 = new UniTagInformation.Builder()
            .setServiceName(EMPTY)
            .setPonCTag(VlanId.vlanId(C_TAG_1))
            .setPonSTag(VlanId.vlanId(S_TAG_2))
            .setTechnologyProfileId(TECH_PROF_ID_1)
            .setUpstreamBandwidthProfile(HSA)
            .setDownstreamBandwidthProfile(HSA)
            .setUniTagMatch(VlanId.vlanId(VlanId.NO_VID))
            .setConfiguredMacAddress(EMPTY)
            .setUpstreamOltBandwidthProfile(HSA)
            .setDownstreamOltBandwidthProfile(HSA)
            .build();

    List<UniTagInformation> uniTagListForTT = Lists.newArrayList(ttService1);
    List<UniTagInformation> uniTagList2ForTT = Lists.newArrayList(ttService1, ttService2);
    List<UniTagInformation> uniTagList3ForTT = Lists.newArrayList(ttService3);
    List<UniTagInformation> uniTagList3Att = Lists.newArrayList(attService1);

    SubscriberAndDeviceInformationBuilder entry1 = SubscriberAndDeviceInformationBuilder.build(ID1, NAS_PORT_ID,
            PORT_1, SLOT_1, MAC1, NAS1, IP1, CIRCUIT1, REMOTE1, uniTagListForTT);
    SubscriberAndDeviceInformationBuilder entry2 = SubscriberAndDeviceInformationBuilder.build(ID2, NAS_PORT_ID,
            PORT_2, SLOT_2, MAC1, NAS2, IP2, CIRCUIT2, REMOTE2, uniTagList2ForTT);
    SubscriberAndDeviceInformationBuilder entry3 = SubscriberAndDeviceInformationBuilder.build(ID3, NAS_PORT_ID,
            PORT_3, SLOT_3, MAC2, NAS3, IP3, CIRCUIT3, REMOTE3, uniTagListForTT);
    SubscriberAndDeviceInformationBuilder entry4 = SubscriberAndDeviceInformationBuilder.build(ID4, NAS_PORT_ID,
            PORT_4, SLOT_4, MAC3, NAS4, IP4, CIRCUIT4, REMOTE4, uniTagList2ForTT);
    SubscriberAndDeviceInformationBuilder entry5 = SubscriberAndDeviceInformationBuilder.build(ID5, NAS_PORT_ID,
            PORT_3, SLOT_3, MAC2, NAS3, IP3, CIRCUIT3, REMOTE3, uniTagList3Att);
    SubscriberAndDeviceInformationBuilder entry6 = SubscriberAndDeviceInformationBuilder.build(ID6, NAS_PORT_ID,
            PORT_3, SLOT_3, MAC2, NAS3, IP3, CIRCUIT3, REMOTE3, uniTagList3ForTT);

    public void setUp(String localConfig, Class configClass) throws Exception {
        sadis = new SadisManager();
        sadis.coreService = new MockCoreService();
        delegate = new MockConfigDelegate();
        mapper = new ObjectMapper();
        subject = sadis.coreService.registerApplication("org.opencord.sadis");

        config.init(subject, "sadis-local-mode-test", node(localConfig), mapper, delegate);
        sadis.cfgService = new MockNetworkConfigRegistry(subject, config);

        event = new NetworkConfigEvent(NetworkConfigEvent.Type.CONFIG_ADDED, subject, configClass);

        sadis.codecService = new CodecManager();
        sadis.activate();
    }

    public void tearDown() {
        this.sadis.deactivate();
    }

    protected JsonNode node(String jsonFile) throws Exception {
        final InputStream jsonStream = BaseSadis.class.getResourceAsStream(jsonFile);
        final JsonNode testConfig = mapper.readTree(jsonStream);
        return testConfig;
    }

    protected void checkConfigInfo(int cacheMaxSize, String cacheTtl, BaseConfig config) {
        assertEquals(cacheMaxSize, config.getCacheMaxSize());
        assertEquals(Duration.parse(cacheTtl), config.getCacheTtl());
    }

    protected void invalidateId(String id, BaseInformationService service) {
        service.invalidateId(id);
    }

    protected void invalidateAll(BaseInformationService service) {
        service.invalidateAll();
    }

    protected void checkFromBoth(String id, BaseInformation localEntry, BaseInformationService service) {
        BaseInformation entry = service.getfromCache(id);
        assertNull(entry);
        checkGetForExisting(id, localEntry, service);
    }

    abstract boolean checkEquality(BaseInformation localEntry, BaseInformation entry);

    protected void checkGetForExisting(String id, BaseInformation localEntry, BaseInformationService service) {
        BaseInformation entry = service.get(id);
        assertNotNull(entry);
        System.out.println(entry);
        if (localEntry != null) {
            assertTrue(checkEquality(localEntry, entry));
        }
    }

    private static final class SubscriberAndDeviceInformationBuilder extends SubscriberAndDeviceInformation {

        public static SubscriberAndDeviceInformationBuilder build(String id, String nasPortId,
                                                                  short port, short slot, String mac,
                                                                  String nasId, String ipAddress, String circuitId,
                                                                  String remoteId,
                                                                  List<UniTagInformation> uniTagList) {

            SubscriberAndDeviceInformationBuilder info = new SubscriberAndDeviceInformationBuilder();
            info.setId(id);
            if (slot != -1) {
                info.setSlot(slot);
            }
            info.setNasPortId(nasPortId);
            info.setUplinkPort(port);
            info.setHardwareIdentifier(MacAddress.valueOf(mac));
            info.setIPAddress(Ip4Address.valueOf(ipAddress));
            info.setNasId(nasId);
            info.setCircuitId(circuitId);
            info.setRemoteId(remoteId);

            info.setUniTagList(uniTagList);

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
    static final class MockNetworkConfigRegistry<S> extends NetworkConfigRegistryAdapter {
        private final BaseConfig config;

        public MockNetworkConfigRegistry(final S subject, final BaseConfig config) {
            this.config = config;
        }

        @SuppressWarnings({"unchecked", "TypeParameterUnusedInFormals"})
        @Override
        public <S, C extends Config<S>> C getConfig(final S subject, final Class<C> configClass) {
            return (C) config;
        }

        @Override
        public void addListener(NetworkConfigListener listener) {
            configListener = listener;
        }
    }
}
