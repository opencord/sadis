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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.onosproject.codec.impl.CodecManager;
import org.onosproject.core.ApplicationId;
import org.onosproject.core.CoreServiceAdapter;
import org.onosproject.net.config.ConfigApplyDelegate;
import org.onosproject.net.config.NetworkConfigRegistryAdapter;
import org.onosproject.net.config.NetworkConfigListener;
import org.onosproject.net.config.Config;
import org.onosproject.net.config.NetworkConfigEvent;
import org.opencord.sadis.BaseConfig;
import org.opencord.sadis.BaseInformationService;
import org.opencord.sadis.BaseInformation;

import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public abstract class BaseSadis {

    protected SadisManager sadis;
    protected ConfigApplyDelegate delegate;
    protected ObjectMapper mapper;
    protected ApplicationId subject;
    protected BaseConfig config;
    protected NetworkConfigEvent event;
    protected static NetworkConfigListener configListener;

    public void setUp(String localConfig, Class configClass) throws Exception {
        sadis = new SadisManager();
        sadis.coreService = new MockCoreService();
        delegate = new MockConfigDelegate();
        mapper = new ObjectMapper();
        subject = sadis.coreService.registerApplication("org.opencord.sadis");

        config.init(subject, "sadis-local-mode-test", node(localConfig), mapper, delegate);
        sadis.cfgService = new MockNetworkConfigRegistry(subject, config);

        event = new NetworkConfigEvent(NetworkConfigEvent.Type.CONFIG_ADDED, subject, configClass);

        sadis.codecService = new CodecManager();
        sadis.activate();
    }

    public void tearDown() {
        this.sadis.deactivate();
    }

    protected JsonNode node(String jsonFile) throws Exception {
        final InputStream jsonStream = BaseSadis.class.getResourceAsStream(jsonFile);
        final JsonNode testConfig = mapper.readTree(jsonStream);
        return testConfig;
    }

    protected void checkConfigInfo(int cacheMaxSize, String cacheTtl, BaseConfig config) {
        assertEquals(cacheMaxSize, config.getCacheMaxSize());
        assertEquals(Duration.parse(cacheTtl), config.getCacheTtl());
    }

    protected void invalidateId(String id, BaseInformationService service) {
        service.invalidateId(id);
    }

    protected void invalidateAll(BaseInformationService service) {
        service.invalidateAll();
    }

    protected void checkFromBoth(String id, BaseInformation localEntry, BaseInformationService service) {
        BaseInformation entry = service.getfromCache(id);
        assertNull(entry);
        checkGetForExisting(id, localEntry, service);
    }

    abstract boolean checkEquality(BaseInformation localEntry, BaseInformation entry);

    protected void checkGetForExisting(String id, BaseInformation localEntry, BaseInformationService service) {
        BaseInformation entry = service.get(id);
        assertNotNull(entry);
        System.out.println(entry);
        if (localEntry != null) {
            assertTrue(checkEquality(localEntry, entry));
        }
    }


    /**
     * Mocks an ONOS configuration delegate to allow JSON based configuration to
     * be tested.
     */
    private static final class MockConfigDelegate implements ConfigApplyDelegate {
        @Override
        public void onApply(@SuppressWarnings("rawtypes") Config config) {
            config.apply();
        }
    }

    /**
     * Mocks an instance of {@link ApplicationId} so that the application
     * component under test can query and use its application ID.
     */
    private static final class MockApplicationId implements ApplicationId {

        private final short id;
        private final String name;

        public MockApplicationId(short id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public short id() {
            return id;
        }

        @Override
        public String name() {
            return name;
        }
    }

    /**
     * Mocks the core services of ONOS so that the application under test can
     * register and query application IDs.
     */
    private static final class MockCoreService extends CoreServiceAdapter {

        private List<ApplicationId> idList = new ArrayList<ApplicationId>();
        private Map<String, ApplicationId> idMap = new HashMap<String, ApplicationId>();

        /*
         * (non-Javadoc)
         *
         * @see
         * org.onosproject.core.CoreServiceAdapter#getAppId(java.lang.Short)
         */
        @Override
        public ApplicationId getAppId(Short id) {
            if (id >= idList.size()) {
                return null;
            }
            return idList.get(id);
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * org.onosproject.core.CoreServiceAdapter#getAppId(java.lang.String)
         */
        @Override
        public ApplicationId getAppId(String name) {
            return idMap.get(name);
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * org.onosproject.core.CoreServiceAdapter#registerApplication(java.lang
         * .String)
         */
        @Override
        public ApplicationId registerApplication(String name) {
            ApplicationId appId = idMap.get(name);
            if (appId == null) {
                appId = new MockApplicationId((short) idList.size(), name);
                idList.add(appId);
                idMap.put(name, appId);
            }
            return appId;
        }

    }


    /**
     * Mocks the ONOS network configuration registry so that the application
     * under test can access a JSON defined configuration.
     */
    static final class MockNetworkConfigRegistry<S> extends NetworkConfigRegistryAdapter {
        private final BaseConfig config;

        public MockNetworkConfigRegistry(final S subject, final BaseConfig config) {
            this.config = config;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <S, C extends Config<S>> C getConfig(final S subject, final Class<C> configClass) {
            return (C) config;
        }

        @Override
        public void addListener(NetworkConfigListener listener) {
            configListener = listener;
        }
    }
}
