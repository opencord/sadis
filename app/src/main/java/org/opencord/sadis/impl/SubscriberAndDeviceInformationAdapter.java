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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.opencord.sadis.SubscriberAndDeviceInformation;
import org.opencord.sadis.SubscriberAndDeviceInformationService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class SubscriberAndDeviceInformationAdapter implements SubscriberAndDeviceInformationService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final int DEFAULT_MAXIMUM_CACHE_SIZE = 0;
    private static final long DEFAULT_TTL = 0;

    private String url;
    private Cache<String, SubscriberAndDeviceInformation> cache;
    private int maxiumCacheSize = DEFAULT_MAXIMUM_CACHE_SIZE;
    private long cacheEntryTtl = DEFAULT_TTL;

    private Map<String, SubscriberAndDeviceInformation> localCfgData = null;

    public SubscriberAndDeviceInformationAdapter() {
        cache = CacheBuilder.newBuilder().maximumSize(maxiumCacheSize)
                .expireAfterAccess(cacheEntryTtl, TimeUnit.SECONDS).build();
    }

    /**
     * Configures the Adapter for data source and cache parameters.
     *
     * @param cfg Configuration data.
     */
    public void configure(SadisConfig cfg) {
        String url = null;
        try {
            // if the url is not present then assume data is in netcfg
            if (cfg.getUrl() != null) {
                url = cfg.getUrl().toString();
            } else {
                localCfgData = Maps.newConcurrentMap();

                cfg.getEntries().forEach(entry -> {
                    localCfgData.put(entry.id(), entry);
                });
                log.info("url is null, data source is local netcfg data");
            }
        } catch (MalformedURLException mUrlEx) {
            log.error("Invalid URL specified: {}", mUrlEx);
        }

        int maximumCacheSeize = cfg.getCacheMaxSize();
        long cacheEntryTtl = cfg.getCacheTtl().getSeconds();

        // Rebuild cache if needed
        if ((url != null && url != this.url) || maximumCacheSeize != this.maxiumCacheSize ||
                cacheEntryTtl != this.cacheEntryTtl) {
            this.maxiumCacheSize = maximumCacheSeize;
            this.cacheEntryTtl = cacheEntryTtl;
            this.url = url;

            Cache<String, SubscriberAndDeviceInformation> newCache = CacheBuilder.newBuilder()
                    .maximumSize(maxiumCacheSize).expireAfterAccess(cacheEntryTtl, TimeUnit.SECONDS).build();
            Cache<String, SubscriberAndDeviceInformation> oldCache = cache;

            synchronized (this) {
                cache = newCache;
            }

            oldCache.invalidateAll();
            oldCache.cleanUp();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.opencord.sadis.SubscriberAndDeviceInformationService#clearCache()
     */
    @Override
    public void invalidateAll() {
        cache.invalidateAll();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.opencord.sadis.SubscriberAndDeviceInformationService#get(java.lang.
     * String)
     */
    @Override
    public SubscriberAndDeviceInformation get(String id) {
        Cache<String, SubscriberAndDeviceInformation> local;
        synchronized (this) {
            local = cache;
        }

        SubscriberAndDeviceInformation info = local.getIfPresent(id);
        if (info != null) {
            return info;
        }

        /*
         * Not in cache, if we have a URL configured we can attempt to get it
         * from there, else check for it in the locally configured data
         */
        if (this.url == null) {
            info = localCfgData.get(id);

            if (info != null) {
                local.put(id, info);
                return info;
            }
        } else {
            // Augment URL with query parameters
            StringBuilder buf = new StringBuilder(this.url);
            if (buf.charAt(buf.length() - 1) != '/') {
                buf.append('/');
            }

            buf.append(id);

            try (InputStream io = new URL(buf.toString()).openStream()) {
                ObjectMapper mapper = new ObjectMapper();
                info = mapper.readValue(io, SubscriberAndDeviceInformation.class);
                local.put(id, info);
                return info;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        log.error("Data not found for id {}", id);
        return null;
    }
}
