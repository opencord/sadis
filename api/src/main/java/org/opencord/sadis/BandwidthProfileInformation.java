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

import java.util.Objects;

/**
 * Represents bandwidth profile details such as PIR, CIR, AIR values.
 */
public class BandwidthProfileInformation extends BaseInformation {

    @JsonProperty(value = "cir")
    long committedInformationRate;

    @JsonProperty(value = "cbs")
    Long committedBurstSize;

    @JsonProperty(value = "eir")
    long exceededInformationRate;

    @JsonProperty(value = "ebs")
    Long exceededBurstSize;

    @JsonProperty(value = "air")
    long assuredInformationRate;

    //note that: the burst size of assured bandwidth will be always 0
    //the rate information must be in Kbps and burst must be in Kbits

    protected BandwidthProfileInformation() {
    }

    public final long committedInformationRate() {
        return this.committedInformationRate;
    }

    public final void setCommittedInformationRate(final long committedInformationRate) {
        this.committedInformationRate = committedInformationRate;
    }

    public final Long committedBurstSize() {
        return this.committedBurstSize;
    }

    public final void setCommittedBurstSize(final Long committedBurstSize) {
        this.committedBurstSize = committedBurstSize;
    }

    public final long exceededInformationRate() {
        return this.exceededInformationRate;
    }

    public final void setExceededInformationRate(final long exceededInformationRate) {
        this.exceededInformationRate = exceededInformationRate;
    }

    public final Long exceededBurstSize() {
        return this.exceededBurstSize;
    }

    public final void setExceededBurstSize(final Long exceededBurstSize) {
        this.exceededBurstSize = exceededBurstSize;
    }

    public final long assuredInformationRate() {
        return this.assuredInformationRate;
    }

    public final void setAssuredInformationRate(final long assuredInformationRate) {
        this.assuredInformationRate = assuredInformationRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BandwidthProfileInformation that = (BandwidthProfileInformation) o;
        return committedInformationRate == that.committedInformationRate &&
                exceededInformationRate == that.exceededInformationRate &&
                assuredInformationRate == that.assuredInformationRate &&
                Objects.equals(committedBurstSize, that.committedBurstSize) &&
                Objects.equals(exceededBurstSize, that.exceededBurstSize);
    }

    @Override
    public int hashCode() {

        return Objects.hash(committedInformationRate, committedBurstSize, exceededInformationRate, exceededBurstSize,
                assuredInformationRate);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BandwidthProfileInformation{");
        sb.append("id=").append(id);
        sb.append(", committedInformationRate=").append(committedInformationRate);
        sb.append(", committedBurstSize=").append(committedBurstSize);
        sb.append(", exceededInformationRate=").append(exceededInformationRate);
        sb.append(", exceededBurstSize=").append(exceededBurstSize);
        sb.append(", assuredInformationRate=").append(assuredInformationRate);
        sb.append('}');
        return sb.toString();
    }
}
