/*
 * Copyright 2017-present Open Networking Laboratory
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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;

public abstract class SubscriberAndDeviceInformationAdapter implements SubscriberAndDeviceInformationService {

    SubscriberDeviceData data = null;

    public SubscriberAndDeviceInformationAdapter() {
    }

    public void configure(String url, int maximumCacheSeize, long cacheEntryTtl) {
        if (data == null) {
            data = new RemoteCachedData();
        }

        if (data instanceof RemoteCachedData) {
            data.configure(url, maximumCacheSeize, cacheEntryTtl);
        }
    }

    public void configure(SadisConfig cfg) {
        if (data == null) {
            data = new LocallyConfiguredData();
        }

        if (data instanceof LocallyConfiguredData) {
            data.configure(cfg);
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
        if (data != null && data instanceof RemoteCachedData) {
            data.invalidateAll();
        }
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
        return data.get(id);
    }

    private interface SubscriberDeviceData {
        SubscriberAndDeviceInformation get(String id);

        void configure(String url, int maximumCacheSeize, long cacheEntryTtl);

        void configure(SadisConfig cfg);

        void invalidateAll();
    }

    private class RemoteCachedData implements SubscriberDeviceData {

        private static final int DEFAULT_MAXIMUM_CACHE_SIZE = 0;
        private static final long DEFAULT_TTL = 0;

        private String url;
        private Cache<String, SubscriberAndDeviceInformation> cache;
        private int maxiumCacheSize = DEFAULT_MAXIMUM_CACHE_SIZE;
        private long cacheEntryTtl = DEFAULT_TTL;


        public RemoteCachedData() {
            cache = CacheBuilder.newBuilder().maximumSize(maxiumCacheSize)
                    .expireAfterAccess(cacheEntryTtl, TimeUnit.SECONDS).build();
        }

        @Override
        public void configure(String url, int maximumCacheSeize, long cacheEntryTtl) {
            // Rebuild cache if needed
            if (url != this.url || maximumCacheSeize != this.maxiumCacheSize || cacheEntryTtl != this.cacheEntryTtl) {
                this.url = url;
                this.maxiumCacheSize = maximumCacheSeize;
                this.cacheEntryTtl = cacheEntryTtl;
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
         * from there.
         */
            if (this.url == null) {
                return null;
            }

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
            return null;
        }

        @Override
        public void invalidateAll() {
            cache.invalidateAll();
        }

        @Override
        public void configure(SadisConfig cfg) { }
    }

    private class LocallyConfiguredData implements SubscriberDeviceData {

        private Map<String, SubscriberAndDeviceInformation> data;

        public LocallyConfiguredData() {  }

        @Override
        public void configure(SadisConfig cfg) {

            data = Maps.newConcurrentMap();

            cfg.getEntries().forEach(entry -> {
                data.put(entry.id(), entry);
            });
        }

        @Override
        public SubscriberAndDeviceInformation get(String id) {
            return data.get(id);
        }

        @Override
        public void configure(String url, int maximumCacheSeize, long cacheEntryTtl) { }

        @Override
        public void invalidateAll() { }
    }
}
