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

import com.fasterxml.jackson.databind.JsonNode;
import org.onosproject.core.ApplicationId;
import org.onosproject.net.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;

public abstract class BaseConfig<T extends BaseInformation> extends Config<ApplicationId> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    protected static final String INTEGRATION = "integration";
    protected static final String CACHE = "cache";
    protected static final String CACHE_SIZE = "maxsize";
    protected static final String CACHE_TTL = "ttl";
    protected static final String URL = "url";
    protected static final String ENTRIES = "entries";
    protected static final String DEFAULT_CACHE_TTL = "PT0S";
    protected static final String ID_SUB_PATTERN = "%s";

    /**
     * Returns Integration URL.
     *
     * @return configured URL or null
     * @throws MalformedURLException specified URL not valid
     */
    public final URL getUrl() throws MalformedURLException {
        final JsonNode integration = this.object.path(INTEGRATION);
        if (integration.isMissingNode()) {
            return null;
        }

        final JsonNode url = integration.path(URL);
        if (url.isMissingNode()) {
            return null;
        }
        StringBuilder buf = new StringBuilder(ID_SUB_PATTERN);
        if (!url.asText().contains(buf)) {
            log.error("Error in url, missing {}", ID_SUB_PATTERN);
            return null;
        }
        return new URL(url.asText());
    }

    /**
     * Returns Cache Maximum Size.
     *
     * @return configured cache max size or -1
     */
    public final int getCacheMaxSize() {
        final JsonNode integration = this.object.path(INTEGRATION);
        if (integration.isMissingNode()) {
            return -1;
        }

        final JsonNode cache = integration.path(CACHE);
        if (cache.isMissingNode()) {
            return -1;
        }

        return cache.path(CACHE_SIZE).asInt(-1);
    }

    public final Duration getCacheTtl() {
        final JsonNode integration = this.object.path(INTEGRATION);
        if (integration.isMissingNode()) {
            return Duration.parse(DEFAULT_CACHE_TTL);
        }

        final JsonNode cache = integration.path(CACHE);
        if (cache.isMissingNode()) {
            return Duration.parse(DEFAULT_CACHE_TTL);
        }

        return Duration.parse(cache.path(CACHE_TTL).asText(DEFAULT_CACHE_TTL));
    }

    public abstract List<T> getEntries();

}
