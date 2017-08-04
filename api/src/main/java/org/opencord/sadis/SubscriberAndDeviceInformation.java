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
public class SubscriberAndDeviceInformation {

    @JsonProperty(value = "id")
    String id;

    @JsonProperty(value = "sTag")
    VlanId sTag;

    @JsonProperty(value = "cTag")
    VlanId cTag;

    @JsonProperty(value = "nasPortId")
    String nasPortId;

    @JsonProperty(value = "port")
    int port = -1;

    @JsonProperty(value = "slot")
    int slot = -1;

    @JsonProperty(value = "hardwareIdentifier")
    MacAddress hardwareIdentifier;

    @JsonProperty(value = "ipAddress")
    Ip4Address ipAddress;

    @JsonProperty(value = "nasId")
    String nasId;

    protected SubscriberAndDeviceInformation() {
    }

    public final String id() {
        return this.id;
    }

    public final void setId(final String id) {
        this.id = id;
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

    public final int port() {
        return this.port;
    }

    public final void setPort(final int port) {
        this.port = port;
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
        result = prime * result + this.port;
        result = prime * result + (this.sTag == null ? 0 : this.sTag.hashCode());
        result = prime * result + this.slot;
        result = prime * result + (this.ipAddress == null ? 0 : this.ipAddress.hashCode());
        result = prime * result + (this.nasId == null ? 0 : this.nasId.hashCode());
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
        if (this.port != other.port) {
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
        buf.append(",port:");
        buf.append(this.port);
        buf.append(",slot:");
        buf.append(this.slot);
        buf.append(",hardwareIdentifier:");
        buf.append(this.hardwareIdentifier);
        buf.append(",ipaddress:");
        buf.append(this.ipAddress);
        buf.append(",nasId:");
        buf.append(this.nasId);
        buf.append(']');

        return buf.toString();
    }

}
