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
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BandwidthProfileManagerTest extends BaseSadis {

    BandwidthProfileBuilder bp1 = BandwidthProfileBuilder.build("High Speed", 0, null, 1000000000,
            384000L, 100000000, 384000L, 100000000);

    BandwidthProfileBuilder bp2 = BandwidthProfileBuilder.build("Home User Speed", 0, null, 1000000000,
            200000L, 100000000, 200000L, 100000000);

    BandwidthProfileBuilder bp3 = BandwidthProfileBuilder.build("TCONT_TYPE1_100Mbps_Fixed", 100000, 10000L,
            0, 0L, 0, null, 100000);

    BandwidthProfileBuilder bp4 = BandwidthProfileBuilder.build("TCONT_TYPE2_50Mbps_Assured", 50000, 10000L,
            50000, 10000L, 0, null, 0);

    BandwidthProfileBuilder bp5 = BandwidthProfileBuilder.build("TCONT_TYPE3_50Mbps_Assured_100Mbps_Peak",
            100000, 10000L, 50000, 10000L, 0, null, 0);

    BandwidthProfileBuilder bp6 = BandwidthProfileBuilder.build("TCONT_TYPE4_200Mbps_Peak", 200000, 10000L,
            0, 0L, 0, null, 0);

    BandwidthProfileBuilder bp7 = BandwidthProfileBuilder.build(
            "TCONT_TYPE5_100Mbps_Peak_50Mbps_Assured_10Mbps_Fixed", 100000, 10000L, 50000, 10000L,
            0, null, 10000);

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
        checkGetForExisting("TCONT_TYPE1_100Mbps_Fixed", bp3, bpService);
        checkGetForExisting("TCONT_TYPE2_50Mbps_Assured", bp4, bpService);
        checkGetForExisting("TCONT_TYPE3_50Mbps_Assured_100Mbps_Peak", bp5, bpService);
        checkGetForExisting("TCONT_TYPE4_200Mbps_Peak", bp6, bpService);
        checkGetForExisting("TCONT_TYPE5_100Mbps_Peak_50Mbps_Assured_10Mbps_Fixed", bp7, bpService);

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
        assertEquals(7, entries.size());

        BandwidthProfileInformation bpi = BandwidthProfileBuilder.build("High Speed", 0, null,
                1000000000, 384000L, 100000000, 384000L, 100000000);
        assertTrue(checkEquality(bpi, entries.get(0)));

        bpi = BandwidthProfileBuilder.build("Home User Speed", 0, null, 1000000000, 200000L,
                100000000, 200000L, 100000000);
        assertTrue(checkEquality(bpi, entries.get(1)));

        bpi = BandwidthProfileBuilder.build("TCONT_TYPE1_100Mbps_Fixed", 100000, 10000L, 0, 0L,
                0, null, 100000);
        assertTrue(checkEquality(bpi, entries.get(2)));
    }

    private static final class BandwidthProfileBuilder extends BandwidthProfileInformation {

        public static BandwidthProfileBuilder build(String id, long pir, Long pbs,
                                                    long cir, Long cbs, long eir, Long ebs, long gir) {
            BandwidthProfileBuilder info = new BandwidthProfileBuilder();
            info.setId(id);

            if (pir != 0) {
                info.setPeakInformationRate(pir);
                info.setPeakBurstSize(pbs);
            }

            if (cbs != null) {
                info.setCommittedBurstSize(cbs);
            } else {
                info.setCommittedBurstSize(0L);
            }
            info.setCommittedInformationRate(cir);

            if (eir != 0) {
                info.setExceededInformationRate(eir);
                info.setExceededBurstSize(ebs);
            }

            info.setGuaranteedInformationRate(gir);
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

        if (bpi.peakInformationRate() != other.peakInformationRate()) {
            return false;
        }

        if (!Objects.equals(bpi.peakBurstSize(), other.peakBurstSize())) {
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

        if (!Objects.equals(bpi.exceededBurstSize(), other.exceededBurstSize())) {
            return false;
        }

        if (bpi.guaranteedInformationRate() != other.guaranteedInformationRate()) {
            return false;
        }

        return true;
    }

}
