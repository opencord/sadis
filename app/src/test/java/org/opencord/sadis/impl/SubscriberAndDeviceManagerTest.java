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
import static org.junit.Assert.assertNull;

import java.util.List;

import com.google.common.collect.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.onlab.packet.Ip4Address;
import org.onlab.packet.MacAddress;

import org.onlab.packet.VlanId;
import org.opencord.sadis.UniServiceInformation;
import org.opencord.sadis.SubscriberAndDeviceInformation;
import org.opencord.sadis.BaseInformation;
import org.opencord.sadis.BaseInformationService;
import org.opencord.sadis.BaseConfig;

/**
 * Set of tests of the SADIS ONOS application component.
 */
public class SubscriberAndDeviceManagerTest extends BaseSadis {

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

    private static final String ID1 = "1";
    private static final String ID2 = "2";
    private static final String ID3 = "3";
    private static final String ID4 = "4";

    UniServiceInformation uniService = new UniServiceInformation.Builder()
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
            .setServiceName(HSA)
            .build();

    UniServiceInformation uniService2 = new UniServiceInformation.Builder()
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
            .build();

    List<UniServiceInformation> uniServiceList = Lists.newArrayList(uniService);
    List<UniServiceInformation> uniServiceList2 = Lists.newArrayList(uniService, uniService2);

    SubscriberAndDeviceInformationBuilder entry1 = SubscriberAndDeviceInformationBuilder.build(ID1, NAS_PORT_ID,
            PORT_1, SLOT_1, MAC1, NAS1, IP1, CIRCUIT1, REMOTE1, uniServiceList);
    SubscriberAndDeviceInformationBuilder entry2 = SubscriberAndDeviceInformationBuilder.build(ID2, NAS_PORT_ID,
            PORT_2, SLOT_2, MAC1, NAS2, IP2, CIRCUIT2, REMOTE2, uniServiceList2);
    SubscriberAndDeviceInformationBuilder entry3 = SubscriberAndDeviceInformationBuilder.build(ID3, NAS_PORT_ID,
            PORT_3, SLOT_3, MAC2, NAS3, IP3, CIRCUIT3, REMOTE3, uniServiceList);
    SubscriberAndDeviceInformationBuilder entry4 = SubscriberAndDeviceInformationBuilder.build(ID4, NAS_PORT_ID,
            PORT_4, SLOT_4, MAC3, NAS4, IP4, CIRCUIT4, REMOTE4, uniServiceList2);


    @Before
    public void setUp() throws Exception {
        config = new SubscriberAndDeviceInformationConfig();
        super.setUp("/LocalSubConfig.json", SubscriberAndDeviceInformationConfig.class);
    }

    @After
    public void tearDown() {
        super.tearDown();
    }

    @Test
    public void testConfiguration() {
        SubscriberAndDeviceInformationConfig config = sadis.cfgService.getConfig(null,
                SubscriberAndDeviceInformationConfig.class);
        checkConfigInfo(50, "PT1m", config);
        checkEntriesForSubscriberAndAccessDevice(config);
    }

    private void checkEntriesForSubscriberAndAccessDevice(BaseConfig config) {
        List<SubscriberAndDeviceInformation> entries = config.getEntries();
        assertEquals(2, entries.size());
        assertTrue(checkEquality(entry1, entries.get(0)));
        assertTrue(checkEquality(entry2, entries.get(1)));
    }

    @Test
    public void testLocalMode() throws Exception {

        BaseInformationService<SubscriberAndDeviceInformation> subscriberService = sadis.getSubscriberInfoService();

        checkGetForExisting(ID1, entry1, subscriberService);
        checkGetForExisting(ID2, entry2, subscriberService);

        invalidateId(ID1, subscriberService);
        checkFromBoth(ID1, entry1, subscriberService);

        invalidateAll(subscriberService);
        checkFromBoth(ID2, entry2, subscriberService);
    }


    private void checkGetForNonExist(String id, BaseInformationService service) {
        BaseInformation entry = service.get(id);
        assertNull(entry);
    }

    @Test
    public void testRemoteMode() throws Exception {
        BaseInformationService<SubscriberAndDeviceInformation> subscriberService = sadis.getSubscriberInfoService();
        config.init(subject, "sadis-remote-mode-test", node("/RemoteConfig.json"), mapper, delegate);
        configListener.event(event);

        checkGetForExisting(ID3, entry3, subscriberService);
        checkGetForExisting(ID4, entry4, subscriberService);

        invalidateId(ID3, subscriberService);
        checkFromBoth(ID3, entry3, subscriberService);

        invalidateAll(subscriberService);
        checkFromBoth(ID4, entry4, subscriberService);
    }

    @Test
    public void testModeSwitch() throws Exception {
        BaseInformationService<SubscriberAndDeviceInformation> service = sadis.getSubscriberInfoService();
        config.init(subject, "sadis-remote-mode-test", node("/RemoteConfig.json"), mapper, delegate);
        configListener.event(event);

        checkGetForExisting(ID3, null, service);
        checkGetForNonExist(ID1, service);

        config.init(subject, "sadis-local-mode-test", node("/LocalSubConfig.json"), mapper, delegate);
        configListener.event(event);

        checkGetForExisting(ID1, null, service);
        checkGetForNonExist(ID3, service);
    }

    private static final class SubscriberAndDeviceInformationBuilder extends SubscriberAndDeviceInformation {

        public static SubscriberAndDeviceInformationBuilder build(String id, String nasPortId,
                                                                  short port, short slot, String mac,
                                                                  String nasId, String ipAddress, String circuitId,
                                                                  String remoteId,
                                                                  List<UniServiceInformation> uniServiceList) {

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

            info.setUniServiceList(uniServiceList);

            return info;
        }

    }

    public boolean checkEquality(BaseInformation localEntry, BaseInformation entry) {
        SubscriberAndDeviceInformation sub = (SubscriberAndDeviceInformation) localEntry;
        SubscriberAndDeviceInformation other = (SubscriberAndDeviceInformation) localEntry;

        if (other == null) {
            return false;
        }
        if (sub.hardwareIdentifier() == null) {
            if (other.hardwareIdentifier() != null) {
                return false;
            }
        } else if (!sub.hardwareIdentifier().equals(other.hardwareIdentifier())) {
            return false;
        }
        if (sub.id() == null) {
            if (other.id() != null) {
                return false;
            }
        } else if (!sub.id().equals(other.id())) {
            return false;
        }
        if (sub.nasPortId() == null) {
            if (other.nasPortId() != null) {
                return false;
            }
        } else if (!sub.nasPortId().equals(other.nasPortId())) {
            return false;
        }
        if (sub.nasId() == null) {
            if (other.nasId() != null) {
                return false;
            }
        } else if (!sub.nasId().equals(other.nasId())) {
            return false;
        }
        if (sub.ipAddress() == null) {
            if (other.ipAddress() != null) {
                return false;
            }
        } else if (!sub.ipAddress().equals(other.ipAddress())) {
            return false;
        }
        if (sub.uplinkPort() != other.uplinkPort()) {
            return false;
        }
        if (sub.slot() != other.slot()) {
            return false;
        }
        if (sub.circuitId() == null) {
            if (other.circuitId() != null) {
                return false;
            }
        } else if (!sub.circuitId().equals(other.circuitId())) {
            return false;
        }
        if (sub.remoteId() == null) {
            if (other.remoteId() != null) {
                return false;
            }
        } else if (!sub.remoteId().equals(other.remoteId())) {
            return false;
        }
        if (sub.uniServiceList() == null) {
            if (other.uniServiceList() != null) {
                return false;
            }
        } else if (!sub.uniServiceList().equals(other.uniServiceList())) {
            return false;
        }
        return true;
    }
}
