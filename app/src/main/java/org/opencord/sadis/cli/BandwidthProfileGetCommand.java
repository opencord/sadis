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
package org.opencord.sadis.cli;

import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.onosproject.cli.AbstractShellCommand;
import org.opencord.sadis.BandwidthProfileInformation;
import org.opencord.sadis.BaseInformationService;
import org.opencord.sadis.SadisService;

/**
 * Bandwidth profile information service CLI.
 */
@Service
@Command(scope = "onos", name = "bandwidthProfile", description = "Bandwidth profile information service CLI command")
public class BandwidthProfileGetCommand extends AbstractShellCommand {

    @Argument(index = 0, name = "ID", description = "bandwidthProfile ID", required = true, multiValued = false)
    String id;

    private SadisService sadisService = get(SadisService.class);
    private BaseInformationService<BandwidthProfileInformation> service = sadisService.getBandwidthProfileService();

    @Override
    protected void doExecute() {
        BandwidthProfileInformation info = service.get(id);
        if (info != null) {
            print(info.toString());
        } else {
            print("Bandwidth profile not found");
        }
    }
}
