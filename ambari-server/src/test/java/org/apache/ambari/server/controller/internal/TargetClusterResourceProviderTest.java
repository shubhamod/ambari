/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ambari.server.controller.internal;


import org.apache.ambari.server.controller.ivory.Cluster;
import org.apache.ambari.server.controller.ivory.IvoryService;
import org.apache.ambari.server.controller.spi.Predicate;
import org.apache.ambari.server.controller.spi.Request;
import org.apache.ambari.server.controller.spi.Resource;
import org.apache.ambari.server.controller.utilities.PredicateBuilder;
import org.apache.ambari.server.controller.utilities.PropertyHelper;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

/**
 * Tests for TargetClusterResourceProvider.
 */
public class TargetClusterResourceProviderTest {
  @Test
  public void testCreateResources() throws Exception {
    IvoryService service = createMock(IvoryService.class);

    Set<Map<String, Object>> propertySet = new HashSet<Map<String, Object>>();

    Map<String, Object> properties = new HashMap<String, Object>();

    properties.put(TargetClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID, "Cluster1");
    properties.put(TargetClusterResourceProvider.CLUSTER_COLO_PROPERTY_ID, "Colo");
    properties.put(TargetClusterResourceProvider.CLUSTER_INTERFACES_PROPERTY_ID, Collections.singleton("Interface1"));
    properties.put(TargetClusterResourceProvider.CLUSTER_LOCATIONS_PROPERTY_ID, Collections.singleton("Location1"));
    properties.put(TargetClusterResourceProvider.CLUSTER_PROPERTIES_PROPERTY_ID, Collections.singletonMap("P1", "V1"));

    // set expectations
    service.submitCluster(TargetClusterResourceProvider.getCluster("Cluster1", properties));

    // replay
    replay(service);

    propertySet.add(properties);

    Request request = PropertyHelper.getCreateRequest(propertySet, Collections.<String,String>emptyMap());

    TargetClusterResourceProvider provider = new TargetClusterResourceProvider(service,
        PropertyHelper.getPropertyIds(Resource.Type.DRTargetCluster),
        PropertyHelper.getKeyPropertyIds(Resource.Type.DRTargetCluster));

    provider.createResources(request);

    // verify
    verify(service);
  }

  @Test
  public void testGetResources() throws Exception {
    IvoryService service = createMock(IvoryService.class);

    Set<Map<String, Object>> propertySet = new HashSet<Map<String, Object>>();

    Map<String, Object> properties = new HashMap<String, Object>();

    List<String> targetClusterNames = new LinkedList<String>();
    targetClusterNames.add("Cluster1");
    targetClusterNames.add("Cluster2");
    targetClusterNames.add("Cluster3");

    Cluster targetCluster1 = new Cluster("Cluster1", "Colo", Collections.singleton("Interface1"),
        Collections.singleton("Location1"), Collections.singletonMap("P1", "V1"));
    Cluster targetCluster2 = new Cluster("Cluster2", "Colo", Collections.singleton("Interface1"),
        Collections.singleton("Location1"), Collections.singletonMap("P1", "V1"));
    Cluster targetCluster3 = new Cluster("Cluster3", "Colo", Collections.singleton("Interface1"),
        Collections.singleton("Location1"), Collections.singletonMap("P1", "V1"));

    // set expectations
    expect(service.getClusterNames()).andReturn(targetClusterNames);

    expect(service.getCluster("Cluster1")).andReturn(targetCluster1);
    expect(service.getCluster("Cluster2")).andReturn(targetCluster2);
    expect(service.getCluster("Cluster3")).andReturn(targetCluster3);

    // replay
    replay(service);

    propertySet.add(properties);

    Request request = PropertyHelper.getCreateRequest(propertySet, Collections.<String,String>emptyMap());

    TargetClusterResourceProvider provider = new TargetClusterResourceProvider(service,
        PropertyHelper.getPropertyIds(Resource.Type.DRTargetCluster),
        PropertyHelper.getKeyPropertyIds(Resource.Type.DRTargetCluster));

    Set<Resource> resources = provider.getResources(request, null);

    Assert.assertEquals(3, resources.size());

    // verify
    verify(service);
  }

  @Test
  public void testUpdateResources() throws Exception {
    IvoryService service = createMock(IvoryService.class);

    Set<Map<String, Object>> propertySet = new HashSet<Map<String, Object>>();

    Map<String, Object> properties = new HashMap<String, Object>();

    properties.put(TargetClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID, "Cluster1");
    properties.put(TargetClusterResourceProvider.CLUSTER_COLO_PROPERTY_ID, "Colo");
    properties.put(TargetClusterResourceProvider.CLUSTER_INTERFACES_PROPERTY_ID, Collections.singleton("Interface1"));
    properties.put(TargetClusterResourceProvider.CLUSTER_LOCATIONS_PROPERTY_ID, Collections.singleton("Location1"));
    properties.put(TargetClusterResourceProvider.CLUSTER_PROPERTIES_PROPERTY_ID, Collections.singletonMap("P1", "V1"));

    List<String> targetClusterNames = new LinkedList<String>();
    targetClusterNames.add("Cluster1");

    Cluster targetCluster1 = new Cluster("Cluster1", "Colo", Collections.singleton("Interface1"),
        Collections.singleton("Location1"), Collections.singletonMap("P1", "V1"));

    // set expectations
    expect(service.getClusterNames()).andReturn(targetClusterNames);

    expect(service.getCluster("Cluster1")).andReturn(targetCluster1);

    service.updateCluster(targetCluster1);

    // replay
    replay(service);

    propertySet.add(properties);

    Request request = PropertyHelper.getCreateRequest(propertySet, Collections.<String,String>emptyMap());

    TargetClusterResourceProvider provider = new TargetClusterResourceProvider(service,
        PropertyHelper.getPropertyIds(Resource.Type.DRTargetCluster),
        PropertyHelper.getKeyPropertyIds(Resource.Type.DRTargetCluster));

    provider.updateResources(request, null);

    // verify
    verify(service);
  }

  @Test
  public void testDeleteResources() throws Exception {
    IvoryService service = createMock(IvoryService.class);

    List<String> targetClusterNames = new LinkedList<String>();
    targetClusterNames.add("Cluster1");

    Cluster targetCluster1 = new Cluster("Cluster1", "Colo", Collections.singleton("Interface1"),
        Collections.singleton("Location1"), Collections.singletonMap("P1", "V1"));

    // set expectations
    expect(service.getClusterNames()).andReturn(targetClusterNames);

    expect(service.getCluster("Cluster1")).andReturn(targetCluster1);

    service.deleteCluster("Cluster1");

    // replay
    replay(service);


    TargetClusterResourceProvider provider = new TargetClusterResourceProvider(service,
        PropertyHelper.getPropertyIds(Resource.Type.DRTargetCluster),
        PropertyHelper.getKeyPropertyIds(Resource.Type.DRTargetCluster));

    Predicate predicate = new PredicateBuilder().property(TargetClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID).equals("Cluster1").toPredicate();

    provider.deleteResources(predicate);

    // verify
    verify(service);
  }

  @Test
  public void testGetKeyPropertyIds() throws Exception {
    IvoryService service = createMock(IvoryService.class);

    Map<Resource.Type, String> keyPropertyIds = PropertyHelper.getKeyPropertyIds(Resource.Type.DRTargetCluster);

    TargetClusterResourceProvider provider = new TargetClusterResourceProvider(service,
        PropertyHelper.getPropertyIds(Resource.Type.DRTargetCluster),
        keyPropertyIds);

    Assert.assertEquals(keyPropertyIds, provider.getKeyPropertyIds());
  }
}
