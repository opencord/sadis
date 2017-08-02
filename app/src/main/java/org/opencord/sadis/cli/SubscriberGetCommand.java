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

import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.onosproject.cli.AbstractShellCommand;
import org.opencord.sadis.SubscriberAndDeviceInformationService;
import org.opencord.sadis.SubscriberAndDeviceInformation;

/**
 * Subscriber And Device Information Service CLI.
 */
@Command(scope = "onos", name = "sadis", description = "Subscriber And Device Information Service CLI command")
public class SubscriberGetCommand extends AbstractShellCommand {

    @Argument(index = 0, name = "ID", description = "subscriber ID", required = true, multiValued = false)
    String id;

    private SubscriberAndDeviceInformationService sadisService = get(SubscriberAndDeviceInformationService.class);

    @Override
    protected void execute() {
        SubscriberAndDeviceInformation info = sadisService.get(id);
        if (info != null) {
           print(info.toString());
        } else {
           print("Subscriber not found");
        }
    }
}
