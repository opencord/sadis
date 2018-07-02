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

import org.junit.Before;
import org.junit.Test;
import org.opencord.sadis.BandwidthProfileInformation;
import org.opencord.sadis.BaseConfig;
import org.opencord.sadis.BaseInformation;
import org.opencord.sadis.BaseInformationService;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BandwidthProfileManagerTest extends BaseSadis {

    BandwidthProfileBuilder bp1 = BandwidthProfileBuilder.build("High Speed", 1000000000, 384000L,
            100000000, 384000L, 100000000);

    BandwidthProfileBuilder bp2 = BandwidthProfileBuilder.build("Home User Speed", 1000000000, 200000L,
            100000000, 200000L, 100000000);

    @Before
    public void setUp() throws Exception {
        config = new BandwidthProfileConfig();
        super.setUp("/LocalBpConfig.json", BandwidthProfileConfig.class);
    }

    @Test
    public void testConfiguration() {
        BandwidthProfileConfig bpConfig = sadis.cfgService.getConfig(null, BandwidthProfileConfig.class);
        checkConfigInfo(60, "PT1m", bpConfig);
        checkEntriesForBandwidthProfiles(bpConfig);
    }

    @Test
    public void testLocalMode() throws Exception {

        BaseInformationService<BandwidthProfileInformation> bpService = sadis.getBandwidthProfileService();
        checkGetForExisting("High Speed", bp1, bpService);
        checkGetForExisting("Home User Speed", bp2, bpService);

        invalidateId("High Speed", bpService);
        checkFromBoth("High Speed", bp1, bpService);

        invalidateAll(bpService);
        checkFromBoth("Home User Speed", bp2, bpService);
    }

    @Test
    public void testRemoteMode() throws Exception {
        BaseInformationService<BandwidthProfileInformation> service = sadis.getBandwidthProfileService();
        config.init(subject, "sadis-remote-mode-test", node("/RemoteConfig.json"), mapper, delegate);
        configListener.event(event);

        checkGetForExisting("HighSpeed", bp1, service);

        invalidateId("HighSpeed", service);
        checkFromBoth("HighSpeed", bp1, service);

        invalidateAll(service);
        checkFromBoth("HighSpeed", bp1, service);
    }

    private void checkEntriesForBandwidthProfiles(BaseConfig config) {
        List<BandwidthProfileInformation> entries = config.getEntries();
        assertEquals(2, entries.size());

        BandwidthProfileInformation bpi = BandwidthProfileBuilder.build("High Speed", 1000000000, 384000L, 100000000,
                384000L, 100000000);
        assertTrue(checkEquality(bpi, entries.get(0)));

        bpi = BandwidthProfileBuilder.build("Home User Speed", 1000000000, 200000L, 100000000,
                200000L, 100000000);
        assertTrue(checkEquality(bpi, entries.get(1)));
    }

    private static final class BandwidthProfileBuilder extends BandwidthProfileInformation {

        public static BandwidthProfileBuilder build(String id, long cir, Long cbs, long eir, Long ebs, long air) {
            BandwidthProfileBuilder info = new BandwidthProfileBuilder();
            info.setId(id);

            if (cbs != null) {
                info.setCommittedBurstSize(cbs);
            } else {
                info.setCommittedBurstSize(0L);
            }
            info.setCommittedInformationRate(cir);

            info.setExceededInformationRate(eir);
            if (ebs != null) {
                info.setExceededBurstSize(ebs);
            } else {
                info.setExceededBurstSize(0L);
            }

            info.setAssuredInformationRate(air);
            return info;
        }
    }

    @Override
    boolean checkEquality(BaseInformation localEntry, BaseInformation entry) {
        BandwidthProfileInformation bpi = (BandwidthProfileInformation) localEntry;
        BandwidthProfileInformation other = (BandwidthProfileInformation) entry;

        if (other == null) {
            return false;
        }

        if (bpi.id() == null) {
            if (other.id() != null) {
                return false;
            }
        } else if (!bpi.id().equals(other.id())) {
            return false;
        }

        if (bpi.committedInformationRate() != other.committedInformationRate()) {
            return false;
        }

        if (!bpi.committedBurstSize().equals(other.committedBurstSize())) {
            return false;
        }

        if (bpi.exceededInformationRate() != other.exceededInformationRate()) {
            return false;
        }

        if (!bpi.exceededBurstSize().equals(other.exceededBurstSize())) {
            return false;
        }

        if (bpi.assuredInformationRate() != other.assuredInformationRate()) {
            return false;
        }

        return true;
    }

}
