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
import org.onlab.packet.VlanId;
import org.onosproject.codec.CodecContext;
import org.onosproject.codec.JsonCodec;
import org.opencord.sadis.UniTagInformation;

public class UniTagInformationCodec extends JsonCodec<UniTagInformation> {

    private static final String UNI_TAG_MATCH = "uniTagMatch";
    private static final String PON_CTAG = "ponCTag";
    private static final String PON_STAG = "ponSTag";
    private static final String US_PON_CTAG_PCP = "usPonCTagPriority";
    private static final String US_PON_STAG_PCP = "usPonSTagPriority";
    private static final String DS_PON_CTAG_PCP = "dsPonCTagPriority";
    private static final String DS_PON_STAG_PCP = "dsPonSTagPriority";
    private static final String TP_ID = "technologyProfileId";
    private static final String US_BP = "upstreamBandwidthProfile";
    private static final String DS_BP = "downstreamBandwidthProfile";
    private static final String SN = "serviceName";
    private static final String MAC_LEARN = "enableMacLearning";
    private static final String MAC = "configuredMacAddress";
    private static final String DHCP_REQ = "isDhcpRequired";
    private static final String IGMP_REQ = "isIgmpRequired";
    private static final int NO_PCP = -1;
    private static final int NO_TP = -1;
    private static final String EMPTY_BP = "";
    private static final String EMPTY_SN = "";
    private static final boolean DEFAULT_MAC_LEARN = false;
    private static final String EMPTY_MAC = "";
    private static final boolean DEFAULT_DHCP_REQ = false;
    private static final boolean DEFAULT_IGMP_REQ = false;

    @Override
    public ObjectNode encode(UniTagInformation entry, CodecContext context) {
        return context.mapper().createObjectNode()
                .put(UNI_TAG_MATCH, entry.getUniTagMatch().toShort())
                .put(PON_CTAG, entry.getPonCTag().toShort())
                .put(PON_STAG, entry.getPonSTag().toShort())
                .put(US_PON_CTAG_PCP, entry.getUsPonCTagPriority())
                .put(US_PON_STAG_PCP, entry.getUsPonSTagPriority())
                .put(DS_PON_CTAG_PCP, entry.getDsPonCTagPriority())
                .put(DS_PON_STAG_PCP, entry.getDsPonSTagPriority())
                .put(TP_ID, entry.getTechnologyProfileId())
                .put(US_BP, entry.getUpstreamBandwidthProfile())
                .put(DS_BP, entry.getDownstreamBandwidthProfile())
                .put(SN, entry.getServiceName())
                .put(MAC_LEARN, entry.getEnableMacLearning())
                .put(MAC, entry.getConfiguredMacAddress())
                .put(DHCP_REQ, entry.getIsDhcpRequired())
                .put(IGMP_REQ, entry.getIsIgmpRequired());
    }

    @Override
    public UniTagInformation decode(ObjectNode json, CodecContext context) {
        if (json == null || !json.isObject()) {
            return null;
        }

        UniTagInformation.Builder tagInfoBuilder = new UniTagInformation.Builder();
        tagInfoBuilder.setUniTagMatch(json.get(UNI_TAG_MATCH) == null ? VlanId.vlanId(VlanId.NO_VID) :
                VlanId.vlanId(json.get(UNI_TAG_MATCH).shortValue()))
                .setPonCTag(json.get(PON_CTAG) == null ? VlanId.vlanId(VlanId.NO_VID) :
                        VlanId.vlanId(json.get(PON_CTAG).shortValue()))
                .setPonCTag(json.get(PON_STAG) == null ? VlanId.vlanId(VlanId.NO_VID) :
                        VlanId.vlanId(json.get(PON_STAG).shortValue()))
                .setUsPonCTagPriority(json.get(US_PON_CTAG_PCP) == null ? NO_PCP :
                        json.get(US_PON_CTAG_PCP).asInt())
                .setUsPonSTagPriority(json.get(US_PON_STAG_PCP) == null ? NO_PCP :
                        json.get(US_PON_STAG_PCP).asInt())
                .setDsPonCTagPriority(json.get(DS_PON_CTAG_PCP) == null ? NO_PCP :
                        json.get(DS_PON_CTAG_PCP).asInt())
                .setDsPonSTagPriority(json.get(DS_PON_STAG_PCP) == null ? NO_PCP :
                        json.get(DS_PON_STAG_PCP).asInt())
                .setTechnologyProfileId(json.get(TP_ID) == null ? NO_TP :
                        json.get(TP_ID).asInt())
                .setUpstreamBandwidthProfile(json.get(US_BP) == null ? EMPTY_BP :
                        json.get(US_BP).asText())
                .setDownstreamBandwidthProfile(json.get(DS_BP) == null ? EMPTY_BP :
                        json.get(DS_BP).asText())
                .setServiceName(json.get(SN) == null ? EMPTY_SN :
                        json.get(SN).asText())
                .setEnableMacLearning(json.get(MAC_LEARN) == null ? DEFAULT_MAC_LEARN :
                        json.get(MAC_LEARN).asBoolean())
                .setConfiguredMacAddress(json.get(MAC) == null ? EMPTY_MAC :
                        json.get(MAC).asText())
                .setIsDhcpRequired(json.get(DHCP_REQ) == null ? DEFAULT_DHCP_REQ :
                        json.get(DHCP_REQ).asBoolean())
                .setIsIgmpRequired(json.get(IGMP_REQ) == null ? DEFAULT_IGMP_REQ :
                        json.get(IGMP_REQ).asBoolean());

        return tagInfoBuilder.build();
    }
}
