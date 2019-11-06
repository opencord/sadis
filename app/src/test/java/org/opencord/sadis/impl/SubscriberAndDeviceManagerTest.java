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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.onlab.packet.Ip4Address;
import org.onlab.packet.MacAddress;
import org.onlab.packet.VlanId;

import org.opencord.sadis.SubscriberAndDeviceInformation;
import org.opencord.sadis.BaseConfig;
import org.opencord.sadis.BaseInformationService;
import org.opencord.sadis.BaseInformation;

/**
 * Set of tests of the SADIS ONOS application component.
 */
public class SubscriberAndDeviceManagerTest extends BaseSadis {

    SubscriberAndDeviceInformationBuilder entry1 = SubscriberAndDeviceInformationBuilder.build("1", (short) 2,
            (short) 2, "1/1/2", (short) 125, (short) 3, "aa:bb:cc:dd:ee:ff", "XXX-NASID", "10.10.10.10",
            "circuit123", "remote123", 64, "1Gb", "1Gb");
    SubscriberAndDeviceInformationBuilder entry2 = SubscriberAndDeviceInformationBuilder.build("2", (short) 4,
            (short) 4, "1/1/2", (short) 129, (short) 4, "aa:bb:cc:dd:ee:ff", "YYY-NASID", "1.1.1.1",
            "circuit234", "remote234", 64, "10Gb", "10Gb");
    SubscriberAndDeviceInformationBuilder entry3 = SubscriberAndDeviceInformationBuilder.build("3", (short) 7,
            (short) 8, "1/1/2", (short) 130, (short) 7, "ff:aa:dd:cc:bb:ee", "MNO-NASID", "30.30.30.30",
            "circuit567", "remote567", 64, "10Gb", "10Gb");
    SubscriberAndDeviceInformationBuilder entry4 = SubscriberAndDeviceInformationBuilder.build("4", (short) 2,
            (short) 1, "1/1/2", (short) 132, (short) 1, "ff:cc:dd:aa:ee:bb", "PQR-NASID", "15.15.15.15",
            "circuit678", "remote678", 64, "5Gb", "5Gb");


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
        assertEquals(3, entries.size());

        SubscriberAndDeviceInformation sub = SubscriberAndDeviceInformationBuilder.build("1", (short) 2, (short) 2,
                "1/1/2", (short) 125,
                (short) 3, "aa:bb:cc:dd:ee:ff", "XXX-NASID", "10.10.10.10", "circuit123", "remote123",
                64, "1Gb", "1Gb");
        assertTrue(checkEquality(sub, entries.get(0)));


        sub = SubscriberAndDeviceInformationBuilder.build("2", (short) 4, (short) 4, "1/1/2", (short) 129,
                (short) 4, "aa:bb:cc:dd:ee:ff", "YYY-NASID", "1.1.1.1", "circuit234", "remote234",
                64, "10Gb", "10Gb");
        assertTrue(checkEquality(sub, entries.get(1)));

        sub = SubscriberAndDeviceInformationBuilder.build("cc:dd:ee:ff:aa:bb", (short) -1, (short) -1, null,
                (short) -1, (short) -1, "cc:dd:ee:ff:aa:bb", "CCC-NASID", "12.12.12.12", "circuit345", "remote345",
                64, "10Gb", "10Gb");
        assertTrue(checkEquality(sub, entries.get(2)));
    }

    @Test
    public void testLocalMode() throws Exception {

        BaseInformationService<SubscriberAndDeviceInformation> subscriberService = sadis.getSubscriberInfoService();

        checkGetForExisting("1", entry1, subscriberService);
        checkGetForExisting("2", entry2, subscriberService);

        invalidateId("1", subscriberService);
        checkFromBoth("1", entry1, subscriberService);

        invalidateAll(subscriberService);
        checkFromBoth("2", entry2, subscriberService);
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

        checkGetForExisting("3", entry3, subscriberService);
        checkGetForExisting("4", entry4, subscriberService);

        invalidateId("3", subscriberService);
        checkFromBoth("3", entry3, subscriberService);

        invalidateAll(subscriberService);
        checkFromBoth("4", entry4, subscriberService);
    }

    @Test
    public void testModeSwitch() throws Exception {
        BaseInformationService<SubscriberAndDeviceInformation> service = sadis.getSubscriberInfoService();
        config.init(subject, "sadis-remote-mode-test", node("/RemoteConfig.json"), mapper, delegate);
        configListener.event(event);

        checkGetForExisting("3", null, service);
        checkGetForNonExist("1", service);

        config.init(subject, "sadis-local-mode-test", node("/LocalSubConfig.json"), mapper, delegate);
        configListener.event(event);

        checkGetForExisting("1", null, service);
        checkGetForNonExist("3", service);
    }


    private static final class SubscriberAndDeviceInformationBuilder extends SubscriberAndDeviceInformation {

        public static SubscriberAndDeviceInformationBuilder build(String id, short cTag, short sTag, String nasPortId,
                                                                  short port, short slot, String mac, String nasId,
                                                                  String ipAddress, String circuitId, String remoteId,
                                                                  int technologyProfileId,
                                                                  String upstreamBandwidthProfile,
                                                                  String downstreamBandwidthProfile) {

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
                info.setUplinkPort(port);
            }
            if (slot != -1) {
                info.setSlot(slot);
            }
            info.setHardwareIdentifier(MacAddress.valueOf(mac));
            info.setIPAddress(Ip4Address.valueOf(ipAddress));
            info.setNasId(nasId);
            info.setCircuitId(circuitId);
            info.setRemoteId(remoteId);

            if (technologyProfileId != -1) {
                info.setTechnologyProfileId(technologyProfileId);
            }

            info.setUpstreamBandwidthProfile(upstreamBandwidthProfile);
            info.setDownstreamBandwidthProfile(downstreamBandwidthProfile);

            return info;
        }

    }

    @Override
    public boolean checkEquality(BaseInformation localEntry, BaseInformation entry) {
        SubscriberAndDeviceInformation sub = (SubscriberAndDeviceInformation) localEntry;
        SubscriberAndDeviceInformation other = (SubscriberAndDeviceInformation) localEntry;

        if (other == null) {
            return false;
        }
        if (sub.cTag() == null) {
            if (other.cTag() != null) {
                return false;
            }
        } else if (!sub.cTag().equals(other.cTag())) {
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
        if (sub.sTag() == null) {
            if (other.sTag() != null) {
                return false;
            }
        } else if (!sub.sTag().equals(other.sTag())) {
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
        if (sub.technologyProfileId() != other.technologyProfileId()) {
            return false;
        }
        if (sub.upstreamBandwidthProfile() == null) {
            if (other.upstreamBandwidthProfile() != null) {
                return false;
            }
        } else if (!sub.upstreamBandwidthProfile().equals(other.upstreamBandwidthProfile())) {
            return false;
        }
        if (sub.downstreamBandwidthProfile() == null) {
            if (other.downstreamBandwidthProfile() != null) {
                return false;
            }
        } else if (!sub.downstreamBandwidthProfile().equals(other.downstreamBandwidthProfile())) {
            return false;
        }
        return true;
    }


}
