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

import org.onlab.packet.Ip4Address;
import org.onlab.packet.MacAddress;
import org.onlab.packet.VlanId;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a unit of information about a subscriber or access device.
 */
public class SubscriberAndDeviceInformation extends BaseInformation {


    @JsonProperty(value = "sTag")
    VlanId sTag;

    @JsonProperty(value = "cTag")
    VlanId cTag;

    @JsonProperty(value = "nasPortId")
    String nasPortId;

    @JsonProperty(value = "uplinkPort")
    int uplinkPort = -1;

    @JsonProperty(value = "slot")
    int slot = -1;

    @JsonProperty(value = "hardwareIdentifier")
    MacAddress hardwareIdentifier;

    @JsonProperty(value = "ipAddress")
    Ip4Address ipAddress;

    @JsonProperty(value = "nasId")
    String nasId;

    @JsonProperty(value = "circuitId")
    String circuitId;

    @JsonProperty(value = "remoteId")
    String remoteId;

    @JsonProperty(value = "technologyProfileId")
    int technologyProfileId = -1;

    @JsonProperty(value = "upstreamBandwidthProfile")
    String upstreamBandwidthProfile;

    @JsonProperty(value = "downstreamBandwidthProfile")
    String downstreamBandwidthProfile;

    protected SubscriberAndDeviceInformation() {
    }

    public final VlanId sTag() {
        return this.sTag;
    }

    public final void setSTag(final VlanId stag) {
        this.sTag = stag;
    }

    public final VlanId cTag() {
        return this.cTag;
    }

    public final void setCTag(final VlanId ctag) {
        this.cTag = ctag;
    }

    public final String nasPortId() {
        return this.nasPortId;
    }

    public final void setNasPortId(final String nasPortId) {
        this.nasPortId = nasPortId;
    }

    public final int uplinkPort() {
        return this.uplinkPort;
    }

    public final void setUplinkPort(final int uplinkPort) {
        this.uplinkPort = uplinkPort;
    }

    public final int slot() {
        return this.slot;
    }

    public final void setSlot(final int slot) {
        this.slot = slot;
    }

    public final MacAddress hardwareIdentifier() {
        return this.hardwareIdentifier;
    }

    public final void setHardwareIdentifier(final MacAddress hardwareIdentifier) {
        this.hardwareIdentifier = hardwareIdentifier;
    }

    public final Ip4Address ipAddress() {
        return this.ipAddress;
    }

    public final void setIPAddress(final Ip4Address ipAddress) {
        this.ipAddress = ipAddress;
    }

    public final String nasId() {
        return this.nasId;
    }

    public final void setNasId(final String nasId) {
        this.nasId = nasId;
    }

    public final String circuitId() {
        return this.circuitId;
    }

    public final void setCircuitId(final String circuitId) {
        this.circuitId = circuitId;
    }

    public final String remoteId() {
        return this.remoteId;
    }

    public final void setRemoteId(final String remoteId) {
        this.remoteId = remoteId;
    }

    public final int technologyProfileId() {
        return this.technologyProfileId;
    }

    public final void setTechnologyProfileId(final int technologyProfileId) {
        this.technologyProfileId = technologyProfileId;
    }

    public final String upstreamBandwidthProfile() {
        return this.upstreamBandwidthProfile;
    }

    public final void setUpstreamBandwidthProfile(final String upstreamBandwidthProfile) {
        this.upstreamBandwidthProfile = upstreamBandwidthProfile;
    }

    public final String downstreamBandwidthProfile() {
        return this.downstreamBandwidthProfile;
    }

    public final void setDownstreamBandwidthProfile(final String downstreamBandwidthProfile) {
        this.downstreamBandwidthProfile = downstreamBandwidthProfile;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.cTag == null ? 0 : this.cTag.hashCode());
        result = prime * result + (this.hardwareIdentifier == null ? 0 : this.hardwareIdentifier.hashCode());
        result = prime * result + (this.id == null ? 0 : this.id.hashCode());
        result = prime * result + (this.nasPortId == null ? 0 : this.nasPortId.hashCode());
        result = prime * result + this.uplinkPort;
        result = prime * result + (this.sTag == null ? 0 : this.sTag.hashCode());
        result = prime * result + this.slot;
        result = prime * result + (this.ipAddress == null ? 0 : this.ipAddress.hashCode());
        result = prime * result + (this.nasId == null ? 0 : this.nasId.hashCode());
        result = prime + result + (this.circuitId == null ? 0 : this.circuitId.hashCode());
        result = prime + result + (this.remoteId == null ? 0 : this.remoteId.hashCode());
        result = prime + result + this.technologyProfileId;
        result = prime + result +
                (this.upstreamBandwidthProfile == null ? 0 : this.upstreamBandwidthProfile.hashCode());
        result = prime + result +
                (this.downstreamBandwidthProfile == null ? 0 : this.downstreamBandwidthProfile.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final SubscriberAndDeviceInformation other = (SubscriberAndDeviceInformation) obj;
        if (this.cTag == null) {
            if (other.cTag != null) {
                return false;
            }
        } else if (!this.cTag.equals(other.cTag)) {
            return false;
        }
        if (this.hardwareIdentifier == null) {
            if (other.hardwareIdentifier != null) {
                return false;
            }
        } else if (!this.hardwareIdentifier.equals(other.hardwareIdentifier)) {
            return false;
        }
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        if (this.nasPortId == null) {
            if (other.nasPortId != null) {
                return false;
            }
        } else if (!this.nasPortId.equals(other.nasPortId)) {
            return false;
        }
        if (this.nasId == null) {
            if (other.nasId != null) {
                return false;
            }
        } else if (!this.nasId.equals(other.nasId)) {
            return false;
        }
        if (this.ipAddress == null) {
            if (other.ipAddress != null) {
                return false;
            }
        } else if (!this.ipAddress.equals(other.ipAddress())) {
            return false;
        }
        if (this.uplinkPort != other.uplinkPort) {
            return false;
        }
        if (this.sTag == null) {
            if (other.sTag != null) {
                return false;
            }
        } else if (!this.sTag.equals(other.sTag)) {
            return false;
        }
        if (this.slot != other.slot) {
            return false;
        }
        if (this.circuitId == null) {
            if (other.circuitId != null) {
                return false;
            }
        } else if (!this.circuitId.equals(other.circuitId)) {
            return false;
        }
        if (this.remoteId == null) {
            if (other.remoteId != null) {
                return false;
            }
        } else if (!this.remoteId.equals(other.remoteId)) {
            return false;
        }
        if (this.technologyProfileId != other.technologyProfileId) {
            return false;
        }
        if (this.upstreamBandwidthProfile == null) {
            if (other.upstreamBandwidthProfile != null) {
                return false;
            }
        } else if (!this.upstreamBandwidthProfile.equals(other.upstreamBandwidthProfile)) {
            return false;
        }
        if (this.downstreamBandwidthProfile == null) {
            if (other.downstreamBandwidthProfile != null) {
                return false;
            }
        } else if (!this.downstreamBandwidthProfile.equals(other.downstreamBandwidthProfile)) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append('[');
        buf.append("id:");
        buf.append(this.id);
        buf.append(",cTag:");
        buf.append(this.cTag);
        buf.append(",sTag:");
        buf.append(this.sTag);
        buf.append(",nasPortId:");
        buf.append(this.nasPortId);
        buf.append(",uplinkPort:");
        buf.append(this.uplinkPort);
        buf.append(",slot:");
        buf.append(this.slot);
        buf.append(",hardwareIdentifier:");
        buf.append(this.hardwareIdentifier);
        buf.append(",ipaddress:");
        buf.append(this.ipAddress);
        buf.append(",nasId:");
        buf.append(this.nasId);
        buf.append(",circuitId:");
        buf.append(this.circuitId);
        buf.append(",remoteId:");
        buf.append(this.remoteId);
        buf.append(",technologyProfileId:");
        buf.append(this.technologyProfileId);
        buf.append(",upstreamBandwidthProfile:");
        buf.append(this.upstreamBandwidthProfile);
        buf.append(",downstreamBandwidthProfile:");
        buf.append(this.downstreamBandwidthProfile);
        buf.append(']');

        return buf.toString();
    }

}
