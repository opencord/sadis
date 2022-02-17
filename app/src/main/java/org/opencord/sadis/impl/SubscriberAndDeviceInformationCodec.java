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
import com.google.common.collect.Lists;
import org.onlab.packet.Ip4Address;
import org.onlab.packet.MacAddress;
import org.onlab.packet.VlanId;
import org.onosproject.codec.CodecContext;
import org.onosproject.codec.JsonCodec;
import org.opencord.sadis.SubscriberAndDeviceInformation;
import org.opencord.sadis.UniTagInformation;

import java.util.ArrayList;
import java.util.List;

public class SubscriberAndDeviceInformationCodec extends JsonCodec<SubscriberAndDeviceInformation> {

    private static final String ID = "id";
    private static final String NAS_PORT_ID = "nasPortId";
    private static final String UPLINK_PORT = "uplinkPort";
    private static final String SLOT = "slot";
    private static final String HARDWARE_IDENTIFIER = "hardwareIdentifier";
    private static final String IP_ADDRESS = "ipAddress";
    private static final String NAS_ID = "nasId";
    private static final String CIRCUIT_ID = "circuitId";
    private static final String REMOTE_ID = "remoteId";
    private static final String UNI_TAG_LIST = "uniTagList";
    private static final String NNI_DHCP_TRAP_VID = "nniDhcpTrapVid";
    private static final String EMPTY_STRING = "";
    private static final int NO_VALUE = -1;

    @Override
    public ObjectNode encode(SubscriberAndDeviceInformation entry, CodecContext context) {

        List<ObjectNode> uniTagListNodes = Lists.newArrayList();
        List<UniTagInformation> uniTagList = entry.uniTagList();
        if (uniTagList != null) {
            for (UniTagInformation uniTagInformation : uniTagList) {
                uniTagListNodes.add(context.encode(uniTagInformation, UniTagInformation.class));
            }
        }
        return context.mapper().createObjectNode()
                .put(ID, entry.id())
                .put(NAS_PORT_ID, entry.nasPortId())
                .put(UPLINK_PORT, entry.uplinkPort())
                .put(SLOT, entry.slot())
                .put(HARDWARE_IDENTIFIER, (entry.hardwareIdentifier() == null) ? EMPTY_STRING :
                        entry.hardwareIdentifier().toString())
                .put(IP_ADDRESS, (entry.ipAddress() == null) ? EMPTY_STRING : entry.ipAddress().toString())
                .put(NAS_ID, entry.nasId())
                .put(CIRCUIT_ID, (entry.circuitId() == null) ? EMPTY_STRING : entry.circuitId())
                .put(REMOTE_ID, (entry.remoteId() == null) ? EMPTY_STRING : entry.remoteId())
                .put(NNI_DHCP_TRAP_VID, entry.nniDhcpTrapVid().toShort())
                .put(UNI_TAG_LIST, uniTagListNodes.toString());
    }

    @Override
    public SubscriberAndDeviceInformation decode(ObjectNode json, CodecContext context) {
        if (json == null || !json.isObject()) {
            return null;
        }
        if (json.get(ID) == null) {
            return null;
        }

        SubscriberAndDeviceInformation info = new SubscriberAndDeviceInformation();
        info.setId(json.get(ID).asText());
        info.setNasPortId(json.get(NAS_PORT_ID) == null ? EMPTY_STRING : json.get(NAS_PORT_ID).asText());
        info.setUplinkPort(json.get(UPLINK_PORT) == null ? NO_VALUE : json.get(UPLINK_PORT).asInt());
        info.setSlot(json.get(SLOT) == null ? NO_VALUE : json.get(SLOT).asInt());
        info.setNasId(json.get(NAS_ID) == null ? EMPTY_STRING : json.get(NAS_ID).asText());
        info.setCircuitId(json.get(CIRCUIT_ID) == null ? EMPTY_STRING : json.get(CIRCUIT_ID).asText());
        info.setRemoteId(json.get(REMOTE_ID) == null ? EMPTY_STRING : json.get(REMOTE_ID).asText());
        info.setNniDhcpTrapVid(json.get(NNI_DHCP_TRAP_VID) == null ? VlanId.vlanId(VlanId.NO_VID) :
        VlanId.vlanId(json.get(NNI_DHCP_TRAP_VID).shortValue()));

        if (json.get(HARDWARE_IDENTIFIER) != null) {
            info.setHardwareIdentifier(MacAddress.valueOf(json.get(HARDWARE_IDENTIFIER).asText()));
        }

        if (json.get(IP_ADDRESS) != null) {
            info.setIPAddress(Ip4Address.valueOf(json.get(IP_ADDRESS).asText()));
        }

        if (json.get(UNI_TAG_LIST) != null) {
            List<UniTagInformation> uniTagList = new ArrayList<>();
            json.get(UNI_TAG_LIST).forEach(entry -> {
                uniTagList.add(new SubscriberAndDeviceInformationConfig()
                        .getUniTagInformation(entry));

            });
            info.setUniTagList(uniTagList);
        }
        return info;
    }
}
