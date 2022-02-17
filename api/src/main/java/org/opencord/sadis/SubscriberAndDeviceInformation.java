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

import java.util.List;
import java.util.Objects;

/**
 * Represents a unit of information about a subscriber or access device.
 */
public class SubscriberAndDeviceInformation extends BaseInformation {

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

    @JsonProperty(value = "nniDhcpTrapVid")
    VlanId nniDhcpTrapVid = VlanId.vlanId(VlanId.NO_VID);

    @JsonProperty(value = "uniTagList")
    List<UniTagInformation> uniTagList;

    public SubscriberAndDeviceInformation() {
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

    public final VlanId nniDhcpTrapVid() {
        return this.nniDhcpTrapVid;
    }

    public final void setNniDhcpTrapVid(final VlanId vid) {
        this.nniDhcpTrapVid = vid;
    }

    public final List<UniTagInformation> uniTagList() {
        return this.uniTagList;
    }

    public final void setUniTagList(final List<UniTagInformation> uniTagList) {
        this.uniTagList = uniTagList;
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
        result = prime * result + (this.hardwareIdentifier == null ? 0 : this.hardwareIdentifier.hashCode());
        result = prime * result + (this.id == null ? 0 : this.id.hashCode());
        result = prime * result + (this.nasPortId == null ? 0 : this.nasPortId.hashCode());
        result = prime * result + this.uplinkPort;
        result = prime * result + this.slot;
        result = prime * result + (this.ipAddress == null ? 0 : this.ipAddress.hashCode());
        result = prime * result + (this.nasId == null ? 0 : this.nasId.hashCode());
        result = prime + result + (this.circuitId == null ? 0 : this.circuitId.hashCode());
        result = prime + result + (this.remoteId == null ? 0 : this.remoteId.hashCode());
        result = prime + result + (this.uniTagList == null ? 0 : this.uniTagList.hashCode());
        result = prime + result + (this.nniDhcpTrapVid != null ? this.nniDhcpTrapVid.hashCode() : 0);
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
        if (this.uniTagList == null) {
            if (other.uniTagList != null) {
                return false;
            }
        } else if (!this.uniTagList.equals(other.uniTagList)) {
            return false;
        }
        if (!Objects.equals(this.nniDhcpTrapVid, other.nniDhcpTrapVid)) {
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
        buf.append("id:").append(this.id);
        buf.append(",nasPortId:").append(this.nasPortId);
        buf.append(",uplinkPort:").append(this.uplinkPort);
        buf.append(",slot:").append(this.slot);
        buf.append(",hardwareIdentifier:").append(this.hardwareIdentifier);
        buf.append(",ipaddress:").append(this.ipAddress);
        buf.append(",nasId:").append(this.nasId);
        buf.append(",circuitId:").append(this.circuitId);
        buf.append(",remoteId:").append(this.remoteId);
        buf.append(",nniDhcpTrapVid:").append(this.nniDhcpTrapVid);
        buf.append(",uniTagList:").append(this.uniTagList);
        buf.append(']');

        return buf.toString();
    }

}
