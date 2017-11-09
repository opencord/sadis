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
import org.opencord.sadis.SubscriberAndDeviceInformation;

public  class SubscriberAndDeviceInformationCodec extends JsonCodec<SubscriberAndDeviceInformation> {
      @Override
      public ObjectNode encode(SubscriberAndDeviceInformation entry, CodecContext context) {
          final ObjectNode result = context.mapper().createObjectNode()
                                    .put("id", entry.id())
                                    .put("cTag", (entry.cTag() == null) ? "" : entry.cTag().toString())
                                    .put("sTag", (entry.sTag() == null) ? "" : entry.sTag().toString())
                                    .put("nasPortId", entry.nasPortId())
                                    .put("uplinkPort", entry.uplinkPort())
                                    .put("slot", entry.slot())
                                    .put("hardwareIdentifier", (entry.hardwareIdentifier() == null) ? "" :
                                          entry.hardwareIdentifier().toString())
                                    .put("ipAddress", (entry.ipAddress() == null) ? "" : entry.ipAddress().toString())
                                    .put("nasId", entry.nasId())
                                    .put("circuiltId", (entry.circuitId() == null) ? "" : entry.circuitId())
                                    .put("remoteId", (entry.remoteId() == null) ? "" : entry.remoteId());

          return result;
      }
}
