/*
 * Copyright 2017-2023 Open Networking Foundation (ONF) and the ONF Contributors
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

import org.opencord.sadis.BaseConfig;
import org.opencord.sadis.BaseInformation;
import org.opencord.sadis.BaseInformationService;
import org.opencord.sadis.SubscriberAndDeviceInformation;

/**
 * Set of tests of the SADIS ONOS application component.
 */
public class SubscriberAndDeviceManagerTest extends BaseSadis {

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
        assertEquals(4, entries.size());
        assertTrue(checkEquality(entry1, entries.get(0)));
        assertTrue(checkEquality(entry2, entries.get(1)));
        assertTrue(checkEquality(entry5, entries.get(2)));
        assertTrue(checkEquality(entry6, entries.get(3)));
    }

    @Test
    public void testLocalMode() throws Exception {

        BaseInformationService<SubscriberAndDeviceInformation> subscriberService = sadis.getSubscriberInfoService();

        checkGetForExisting(ID1, entry1, subscriberService);
        checkGetForExisting(ID2, entry2, subscriberService);
        checkGetForExisting(ID5, entry5, subscriberService);
        checkGetForExisting(ID6, entry6, subscriberService);

        invalidateId(ID1, subscriberService);
        checkFromBoth(ID1, entry1, subscriberService);

        invalidateAll(subscriberService);
        checkFromBoth(ID2, entry2, subscriberService);
        checkFromBoth(ID6, entry6, subscriberService);
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

        service.clearLocalData();

        checkGetForExisting(ID3, null, service);
        checkGetForNonExist(ID1, service);

        config.init(subject, "sadis-local-mode-test", node("/LocalSubConfig.json"), mapper, delegate);
        configListener.event(event);

        checkGetForExisting(ID1, null, service);
        checkGetForNonExist(ID3, service);
    }

    // test the hybrid mode (both local and remote data in the config)
    // ids 1 and 2 are local, others are remote
    @Test
    public void testHybridMode() throws Exception {
        BaseInformationService<SubscriberAndDeviceInformation> subscriberService = sadis.getSubscriberInfoService();
        config.init(subject, "sadis-hybrid-mode-test", node("/HybridSubConfig.json"), mapper, delegate);
        configListener.event(event);

        // check that I can fetch from remote
        checkGetForExisting(ID3, entry3, subscriberService);
        checkGetForExisting(ID4, entry4, subscriberService);

        // check that I can fetch from local
        checkGetForExisting(ID1, entry1, subscriberService);
        checkGetForExisting(ID2, entry2, subscriberService);
    }

    public boolean checkEquality(BaseInformation localEntry, BaseInformation entry) {
        SubscriberAndDeviceInformation sub = (SubscriberAndDeviceInformation) localEntry;
        SubscriberAndDeviceInformation other = (SubscriberAndDeviceInformation) entry;

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
        if (sub.uniTagList() == null) {
            if (other.uniTagList() != null) {
                return false;
            }
        } else if (!sub.uniTagList().equals(other.uniTagList())) {
            return false;
        }
        return true;
    }
}
