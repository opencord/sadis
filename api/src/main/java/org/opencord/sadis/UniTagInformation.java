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
package org.opencord.sadis;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.onlab.packet.VlanId;

/**
 * Represents a unit of information about a service.
 */
public final class UniTagInformation {

    @JsonProperty(value = "uniTagMatch")
    private VlanId uniTagMatch = VlanId.vlanId(VlanId.NO_VID);

    @JsonProperty(value = "ponCTag")
    private VlanId ponCTag;

    @JsonProperty(value = "ponSTag")
    private VlanId ponSTag;

    @JsonProperty(value = "usPonCTagPriority")
    private int usPonCTagPriority = -1;

    @JsonProperty(value = "usPonSTagPriority")
    private int usPonSTagPriority = -1;

    @JsonProperty(value = "dsPonCTagPriority")
    private int dsPonCTagPriority = -1;

    @JsonProperty(value = "dsPonSTagPriority")
    private int dsPonSTagPriority = -1;

    @JsonProperty(value = "technologyProfileId")
    private int technologyProfileId = -1;

    @JsonProperty(value = "upstreamBandwidthProfile")
    private String upstreamBandwidthProfile;

    @JsonProperty(value = "downstreamBandwidthProfile")
    private String downstreamBandwidthProfile;

    @JsonProperty(value = "upstreamOltBandwidthProfile")
    private String upstreamOltBandwidthProfile;

    @JsonProperty(value = "downstreamOltBandwidthProfile")
    private String downstreamOltBandwidthProfile;

    @JsonProperty(value = "serviceName")
    private String serviceName;

    @JsonProperty(value = "enableMacLearning")
    private boolean enableMacLearning = false;

    @JsonProperty(value = "configuredMacAddress")
    private String configuredMacAddress;

    @JsonProperty(value = "isDhcpRequired")
    private boolean isDhcpRequired;

    @JsonProperty(value = "isIgmpRequired")
    private boolean isIgmpRequired;

    public UniTagInformation() {

    }
    //CHECKSTYLE:OFF
    private UniTagInformation(final VlanId uniTagMatch, final VlanId ponCTag, final VlanId ponSTag,
                              final int usPonCTagPriority, final int usPonSTagPriority,
                              final int dsPonCTagPriority, final int dsPonSTagPriority,
                              final int technologyProfileId,
                              final String upstreamBandwidthProfile,
                              final String downstreamBandwidthProfile,
                              final String upstreamOltBandwidthProfile,
                              final String downstreamOltBandwidthProfile,
                              final String serviceName, final boolean enableMacLearning,
                              final String configuredMacAddress, final boolean isDhcpRequired,
                              final boolean isIgmpRequired) {
        this.uniTagMatch = uniTagMatch;
        this.ponCTag = ponCTag;
        this.ponSTag = ponSTag;
        this.usPonCTagPriority = usPonCTagPriority;
        this.usPonSTagPriority = usPonSTagPriority;
        this.dsPonCTagPriority = dsPonCTagPriority;
        this.dsPonSTagPriority = dsPonSTagPriority;
        this.technologyProfileId = technologyProfileId;
        this.upstreamBandwidthProfile = upstreamBandwidthProfile;
        this.downstreamBandwidthProfile = downstreamBandwidthProfile;
        this.upstreamOltBandwidthProfile = upstreamOltBandwidthProfile;
        this.downstreamOltBandwidthProfile = downstreamOltBandwidthProfile;
        this.serviceName = serviceName;
        this.enableMacLearning = enableMacLearning;
        this.configuredMacAddress = configuredMacAddress;
        this.isDhcpRequired = isDhcpRequired;
        this.isIgmpRequired = isIgmpRequired;
    }
    //CHECKSTYLE:ON

    public final VlanId getUniTagMatch() {
        return uniTagMatch;
    }

    public final VlanId getPonCTag() {
        return ponCTag;
    }

    public final VlanId getPonSTag() {
        return ponSTag;
    }

    public final int getUsPonCTagPriority() {
        return usPonCTagPriority;
    }

    public final int getUsPonSTagPriority() {
        return usPonSTagPriority;
    }

    public final int getDsPonCTagPriority() {
        return dsPonCTagPriority;
    }

    public final int getDsPonSTagPriority() {
        return dsPonSTagPriority;
    }

    public final int getTechnologyProfileId() {
        return technologyProfileId;
    }

    public final String getUpstreamBandwidthProfile() {
        return upstreamBandwidthProfile;
    }

    public final String getDownstreamBandwidthProfile() {
        return downstreamBandwidthProfile;
    }

    public final String getUpstreamOltBandwidthProfile() {
        return upstreamOltBandwidthProfile;
    }

    public final String getDownstreamOltBandwidthProfile() {
        return downstreamOltBandwidthProfile;
    }

    public final String getServiceName() {
        return serviceName;
    }

    public final boolean getEnableMacLearning() {
        return enableMacLearning;
    }

    public final String getConfiguredMacAddress() {
        return configuredMacAddress;
    }

    public final boolean getIsDhcpRequired() {
        return isDhcpRequired;
    }

    public final boolean getIsIgmpRequired() {
        return isIgmpRequired;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UniTagInformation that = (UniTagInformation) o;

        if (uniTagMatch != that.uniTagMatch) {
            return false;
        }
        if (technologyProfileId != that.technologyProfileId) {
            return false;
        }
        if (ponCTag != that.ponCTag) {
            return false;
        }
        if (ponSTag != that.ponSTag) {
            return false;
        }
        if (usPonCTagPriority != that.usPonCTagPriority) {
            return false;
        }
        if (usPonSTagPriority != that.usPonSTagPriority) {
            return false;
        }
        if (dsPonCTagPriority != that.dsPonCTagPriority) {
            return false;
        }
        if (dsPonSTagPriority != that.dsPonSTagPriority) {
            return false;
        }
        if (enableMacLearning != that.enableMacLearning) {
            return false;
        }
        if (configuredMacAddress != that.configuredMacAddress) {
            return false;
        }
        if (isDhcpRequired != that.isDhcpRequired) {
            return false;
        }
        if (isIgmpRequired != that.isIgmpRequired) {
            return false;
        }
        if (upstreamBandwidthProfile != null ? !upstreamBandwidthProfile.equals(that.upstreamBandwidthProfile) :
                that.upstreamBandwidthProfile != null) {
            return false;
        }
        if (downstreamBandwidthProfile != null ? !downstreamBandwidthProfile.equals(that.downstreamBandwidthProfile) :
                that.downstreamBandwidthProfile != null) {
            return false;
        }
        if (upstreamOltBandwidthProfile != null ?
                !upstreamOltBandwidthProfile.equals(that.upstreamOltBandwidthProfile) :
                that.upstreamOltBandwidthProfile != null) {
            return false;
        }
        if (downstreamOltBandwidthProfile != null ?
                !downstreamOltBandwidthProfile.equals(that.downstreamOltBandwidthProfile) :
                that.downstreamOltBandwidthProfile != null) {
            return false;
        }
        return serviceName != null ? serviceName.equals(that.serviceName) : that.serviceName == null;
    }

    @Override
    public int hashCode() {
        int result = uniTagMatch != null ? uniTagMatch.hashCode() : 0;
        result = 31 * result + (ponCTag != null ? ponCTag.hashCode() : 0);
        result = 31 * result + (ponSTag != null ? ponSTag.hashCode() : 0);
        result = 31 * result + usPonCTagPriority;
        result = 31 * result + usPonSTagPriority;
        result = 31 * result + dsPonCTagPriority;
        result = 31 * result + dsPonSTagPriority;
        result = 31 * result + technologyProfileId;
        result = 31 * result + (upstreamBandwidthProfile != null ? upstreamBandwidthProfile.hashCode() : 0);
        result = 31 * result + (downstreamBandwidthProfile != null ? downstreamBandwidthProfile.hashCode() : 0);
        result = 31 * result + (upstreamOltBandwidthProfile != null ? upstreamOltBandwidthProfile.hashCode() : 0);
        result = 31 * result + (downstreamOltBandwidthProfile != null ? downstreamOltBandwidthProfile.hashCode() : 0);
        result = 31 * result + (serviceName != null ? serviceName.hashCode() : 0);
        result = 31 * result + (enableMacLearning ? 1 : 0);
        result = 31 * result + (configuredMacAddress != null ? configuredMacAddress.hashCode() : 0);
        result = 31 * result + (isDhcpRequired ? 1 : 0);
        result = 31 * result + (isIgmpRequired ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UniTagInformation{");
        sb.append("uniTagMatch=").append(uniTagMatch);
        sb.append(", ponCTag=").append(ponCTag);
        sb.append(", ponSTag=").append(ponSTag);
        sb.append(", usPonCTagPriority=").append(usPonCTagPriority);
        sb.append(", usPonSTagPriority=").append(usPonSTagPriority);
        sb.append(", dsPonCTagPriority=").append(dsPonCTagPriority);
        sb.append(", dsPonSTagPriority=").append(dsPonSTagPriority);
        sb.append(", technologyProfileId=").append(technologyProfileId);
        sb.append(", enableMacLearning=").append(enableMacLearning);
        sb.append(", upstreamBandwidthProfile='").append(upstreamBandwidthProfile).append('\'');
        sb.append(", downstreamBandwidthProfile='").append(downstreamBandwidthProfile).append('\'');
        sb.append(", upstreamOltBandwidthProfile='").append(upstreamOltBandwidthProfile).append('\'');
        sb.append(", downstreamOltBandwidthProfile='").append(downstreamOltBandwidthProfile).append('\'');
        sb.append(", serviceName='").append(serviceName).append('\'');
        sb.append(", configuredMacAddress='").append(configuredMacAddress).append('\'');
        sb.append(", isDhcpRequired=").append(isDhcpRequired);
        sb.append(", isIgmpRequired=").append(isIgmpRequired);
        sb.append('}');
        return sb.toString();
    }

    public static final class Builder {

        private VlanId uniTagMatch;
        private VlanId ponCTag;
        private VlanId ponSTag;
        private int usPonCTagPriority = -1;
        private int usPonSTagPriority = -1;
        private int dsPonCTagPriority = -1;
        private int dsPonSTagPriority = -1;
        private int technologyProfileId;
        private String upstreamBandwidthProfile;
        private String downstreamBandwidthProfile;
        private String upstreamOltBandwidthProfile;
        private String downstreamOltBandwidthProfile;
        private String serviceName;
        private boolean enableMacLearning;
        private String configuredMacAddress;
        private boolean isDhcpRequired;
        private boolean isIgmpRequired;

        public final Builder setUniTagMatch(final VlanId uniTagMatch) {
            this.uniTagMatch = uniTagMatch;
            return this;
        }

        public final Builder setPonCTag(final VlanId ponCTag) {
            this.ponCTag = ponCTag;
            return this;
        }

        public final Builder setPonSTag(final VlanId ponSTag) {
            this.ponSTag = ponSTag;
            return this;
        }

        public final Builder setUsPonCTagPriority(final int usPonCTagPriority) {
            this.usPonCTagPriority = usPonCTagPriority;
            return this;
        }

        public final Builder setUsPonSTagPriority(final int usPonSTagPriority) {
            this.usPonSTagPriority = usPonSTagPriority;
            return this;
        }

        public final Builder setDsPonCTagPriority(final int dsPonCTagPriority) {
            this.dsPonCTagPriority = dsPonCTagPriority;
            return this;
        }

        public final Builder setDsPonSTagPriority(final int dsPonSTagPriority) {
            this.dsPonSTagPriority = dsPonSTagPriority;
            return this;
        }

        public final Builder setTechnologyProfileId(final int technologyProfileId) {
            this.technologyProfileId = technologyProfileId;
            return this;
        }

        public final Builder setUpstreamBandwidthProfile(final String upstreamBandwidthProfile) {
            this.upstreamBandwidthProfile = upstreamBandwidthProfile;
            return this;
        }

        public final Builder setDownstreamBandwidthProfile(final String downstreamBandwidthProfile) {
            this.downstreamBandwidthProfile = downstreamBandwidthProfile;
            return this;
        }

        public final Builder setUpstreamOltBandwidthProfile(final String upstreamOltBandwidthProfile) {
            this.upstreamOltBandwidthProfile = upstreamOltBandwidthProfile;
            return this;
        }

        public final Builder setDownstreamOltBandwidthProfile(final String downstreamOltBandwidthProfile) {
            this.downstreamOltBandwidthProfile = downstreamOltBandwidthProfile;
            return this;
        }

        public final Builder setServiceName(final String serviceName) {
            this.serviceName = serviceName;
            return this;
        }

        public final Builder setEnableMacLearning(final boolean enableMacLearning) {
            this.enableMacLearning = enableMacLearning;
            return this;
        }

        public final Builder setConfiguredMacAddress(final String configuredMacAddress) {
            this.configuredMacAddress = configuredMacAddress;
            return this;
        }

        public final Builder setIsDhcpRequired(final boolean isDhcpRequired) {
            this.isDhcpRequired = isDhcpRequired;
            return this;
        }

        public final Builder setIsIgmpRequired(final boolean isIgmpRequired) {
            this.isIgmpRequired = isIgmpRequired;
            return this;
        }

        public final UniTagInformation build() {
            return new UniTagInformation(this.uniTagMatch, this.ponCTag,
                    this.ponSTag, this.usPonCTagPriority, this.usPonSTagPriority,
                    this.dsPonCTagPriority, this.dsPonSTagPriority, this.technologyProfileId,
                    this.upstreamBandwidthProfile, this.downstreamBandwidthProfile,
                    this.upstreamOltBandwidthProfile, this.downstreamOltBandwidthProfile,
                    this.serviceName, this.enableMacLearning,
                    this.configuredMacAddress, this.isDhcpRequired, this.isIgmpRequired);
        }
    }
}
