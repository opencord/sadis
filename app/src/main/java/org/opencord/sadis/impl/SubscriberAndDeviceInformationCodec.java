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

    private static final String ID = "id";
    private static final String NAS_PORT_ID = "nasPortId";
    private static final String UPLINK_PORT = "uplinkPort";
    private static final String SLOT = "slot";
    private static final String HARDWARE_IDENTIFIER = "hardwareIdentifier";
    private static final String IP_ADDRESS = "ipAddress";
    private static final String NAS_ID = "nasId";
    private static final String CIRCUIT_ID = "circuitId";
    private static final String REMOTE_ID = "remoteId";
    private static final String UNI_SERVICE_LIST = "uniServiceList";
    private static final String EMPTY_STRING = "";
    private static final int NO_VALUE = -1;

      @Override
      public ObjectNode encode(SubscriberAndDeviceInformation entry, CodecContext context) {
          return context.mapper().createObjectNode()
                                    .put(ID, entry.id())
                                    .put(NAS_PORT_ID, entry.nasPortId())
                                    .put(UPLINK_PORT, entry.uplinkPort())
                                    .put(SLOT, entry.slot())
                                    .put(HARDWARE_IDENTIFIER, (entry.hardwareIdentifier() == null) ? EMPTY_STRING :
                                          entry.hardwareIdentifier().toString())
                                    .put(IP_ADDRESS, (entry.ipAddress() == null) ? EMPTY_STRING :
                                            entry.ipAddress().toString())
                                    .put(NAS_ID, entry.nasId())
                                    .put(CIRCUIT_ID, (entry.circuitId() == null) ? EMPTY_STRING : entry.circuitId())
                                    .put(REMOTE_ID, (entry.remoteId() == null) ? EMPTY_STRING : entry.remoteId())
                                    .put(UNI_SERVICE_LIST, (entry.uniServiceList() == null) ? EMPTY_STRING :
                                            entry.uniServiceList().toString());

      }
}
