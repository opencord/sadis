/*
 * Copyright 2021-present Open Networking Foundation
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

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Bandwidth profiles are grouped under this class in order to reduce the parameter size in UniTagInformation class
 * and to make them more manageable.
 * These profiles are assigned to devices(ONU and OLT) for both upstream and downstream flows.
 */
public class BandwidthProfiles {

    @JsonProperty(value = "upstreamBandwidthProfile")
    private String upstreamBandwidthProfile;

    @JsonProperty(value = "downstreamBandwidthProfile")
    private String downstreamBandwidthProfile;

    @JsonProperty(value = "upstreamOltBandwidthProfile")
    private String upstreamOltBandwidthProfile;

    @JsonProperty(value = "downstreamOltBandwidthProfile")
    private String downstreamOltBandwidthProfile;

    public BandwidthProfiles() {
    }


    /**
     * @param upstreamBandwidthProfile      upstream bandwidth profile of ONU device
     * @param downstreamBandwidthProfile    downstream bandwidth profile of ONU device
     * @param upstreamOltBandwidthProfile   upstream bandwidth profile of OLT device
     * @param downstreamOltBandwidthProfile downstream bandwidth profile of OLT device
     */
    public BandwidthProfiles(String upstreamBandwidthProfile, String downstreamBandwidthProfile,
                             String upstreamOltBandwidthProfile, String downstreamOltBandwidthProfile) {
        this.upstreamBandwidthProfile = upstreamBandwidthProfile;
        this.downstreamBandwidthProfile = downstreamBandwidthProfile;
        this.upstreamOltBandwidthProfile = upstreamOltBandwidthProfile;
        this.downstreamOltBandwidthProfile = downstreamOltBandwidthProfile;
    }

    public String getUpstreamBandwidthProfile() {
        return upstreamBandwidthProfile;
    }

    public void setUpstreamBandwidthProfile(String upstreamBandwidthProfile) {
        this.upstreamBandwidthProfile = upstreamBandwidthProfile;
    }

    public String getDownstreamBandwidthProfile() {
        return downstreamBandwidthProfile;
    }

    public void setDownstreamBandwidthProfile(String downstreamBandwidthProfile) {
        this.downstreamBandwidthProfile = downstreamBandwidthProfile;
    }

    public String getUpstreamOltBandwidthProfile() {
        return upstreamOltBandwidthProfile;
    }

    public void setUpstreamOltBandwidthProfile(String upstreamOltBandwidthProfile) {
        this.upstreamOltBandwidthProfile = upstreamOltBandwidthProfile;
    }

    public String getDownstreamOltBandwidthProfile() {
        return downstreamOltBandwidthProfile;
    }

    public void setDownstreamOltBandwidthProfile(String downstreamOltBandwidthProfile) {
        this.downstreamOltBandwidthProfile = downstreamOltBandwidthProfile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BandwidthProfiles that = (BandwidthProfiles) o;
        return Objects.equals(upstreamBandwidthProfile, that.upstreamBandwidthProfile) &&
                Objects.equals(downstreamBandwidthProfile, that.downstreamBandwidthProfile) &&
                Objects.equals(upstreamOltBandwidthProfile, that.upstreamOltBandwidthProfile) &&
                Objects.equals(downstreamOltBandwidthProfile, that.downstreamOltBandwidthProfile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upstreamBandwidthProfile, downstreamBandwidthProfile, upstreamOltBandwidthProfile,
                downstreamOltBandwidthProfile);
    }

    @Override
    public String toString() {
        return "BandwidthProfiles{" +
                "upstreamBandwidthProfile='" + upstreamBandwidthProfile + '\'' +
                ", downstreamBandwidthProfile='" + downstreamBandwidthProfile + '\'' +
                ", upstreamOltBandwidthProfile='" + upstreamOltBandwidthProfile + '\'' +
                ", downstreamOltBandwidthProfile='" + downstreamOltBandwidthProfile + '\'' +
                '}';
    }
}