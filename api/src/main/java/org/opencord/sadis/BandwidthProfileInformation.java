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
package org.opencord.sadis;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Represents bandwidth profile details such as PIR, CIR, GIR values.
 */
public class BandwidthProfileInformation extends BaseInformation {

    @JsonProperty(value = "pir")
    long peakInformationRate;

    @JsonProperty(value = "pbs")
    Long peakBurstSize;

    @JsonProperty(value = "cir")
    long committedInformationRate;

    @JsonProperty(value = "cbs")
    Long committedBurstSize;

    @JsonProperty(value = "eir")
    long exceededInformationRate;

    @JsonProperty(value = "ebs")
    Long exceededBurstSize;

    // Deprecated in VOLTHA 2.8 (sadis 5.4.0)
    // Will be removed in 2.9, use GIR instead
    @JsonProperty(value = "air")
    long assuredInformationRate;

    @JsonProperty(value = "gir")
    long guaranteedInformationRate;

    //note that: the burst size of guaranteed bandwidth will be always 0
    //the rate information must be in Kbps and burst must be in Kbits

    public final long peakInformationRate() {
        return this.peakInformationRate;
    }

    public final void setPeakInformationRate(final long peakInformationRate) {
        this.peakInformationRate = peakInformationRate;
    }

    public final Long peakBurstSize() {
        return this.peakBurstSize;
    }

    public final void setPeakBurstSize(final Long peakBurstSize) {
        this.peakBurstSize = peakBurstSize;
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

    // Deprecated in VOLTHA 2.8 (sadis 5.4.0)
    // Will be removed in 2.9, use guaranteedInformationRate instead.
    public final long assuredInformationRate() {
        return this.assuredInformationRate;
    }

    // Deprecated in VOLTHA 2.8 (sadis 5.4.0)
    // Will be removed in 2.9, use guaranteedInformationRate instead.
    public final void setAssuredInformationRate(final long assuredInformationRate) {
        this.assuredInformationRate = assuredInformationRate;
    }

    public final long guaranteedInformationRate() {
        return this.guaranteedInformationRate;
    }

    public final void setGuaranteedInformationRate(final long guaranteedInformationRate) {
        this.guaranteedInformationRate = guaranteedInformationRate;
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
        return Objects.equals(id, that.id) &&
                peakInformationRate == that.peakInformationRate &&
                committedInformationRate == that.committedInformationRate &&
                exceededInformationRate == that.exceededInformationRate &&
                assuredInformationRate == that.assuredInformationRate &&
                guaranteedInformationRate == that.guaranteedInformationRate &&
                Objects.equals(peakBurstSize, that.peakBurstSize) &&
                Objects.equals(committedBurstSize, that.committedBurstSize) &&
                Objects.equals(exceededBurstSize, that.exceededBurstSize);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, peakInformationRate, peakBurstSize, committedInformationRate, committedBurstSize,
                exceededInformationRate, exceededBurstSize, assuredInformationRate, guaranteedInformationRate);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BandwidthProfileInformation{");
        sb.append("id=").append(id);
        sb.append(", committedInformationRate=").append(committedInformationRate);
        sb.append(", committedBurstSize=").append(committedBurstSize);
        if (peakInformationRate == 0 && peakBurstSize == null) {
            sb.append(", exceededInformationRate=").append(exceededInformationRate);
            sb.append(", exceededBurstSize=").append(exceededBurstSize);
        } else {
            sb.append(", peakInformationRate=").append(peakInformationRate);
            sb.append(", peakBurstSize=").append(peakBurstSize);
        }
        sb.append(", assuredInformationRate=").append(assuredInformationRate);
        sb.append(", guaranteedInformationRate=").append(guaranteedInformationRate);
        sb.append('}');
        return sb.toString();
    }
}
