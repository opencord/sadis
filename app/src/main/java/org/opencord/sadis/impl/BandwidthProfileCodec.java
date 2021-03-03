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

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.onosproject.codec.CodecContext;
import org.onosproject.codec.JsonCodec;
import org.opencord.sadis.BandwidthProfileInformation;

public class BandwidthProfileCodec extends JsonCodec<BandwidthProfileInformation> {
    @Override
    public ObjectNode encode(BandwidthProfileInformation entry, CodecContext context) {

        ObjectNode node = context.mapper().createObjectNode()
                .put("id", entry.id())
                .put("cir", (entry.committedInformationRate()))
                .put("cbs", (entry.committedBurstSize() == null) ? "" : entry.committedBurstSize().toString())
                .put("air", entry.assuredInformationRate())
                .put("gir", entry.guaranteedInformationRate());

        if (entry.peakInformationRate() == 0 && entry.peakBurstSize() == null) {
            node.put("eir", entry.exceededInformationRate());
            node.put("ebs", (entry.exceededBurstSize() == null) ? "" : entry.exceededBurstSize().toString());
        } else {
            node.put("pir", entry.peakInformationRate());
            node.put("pbs", (entry.peakBurstSize() == null) ? "" : entry.peakBurstSize().toString());
        }

        return node;
    }

}
