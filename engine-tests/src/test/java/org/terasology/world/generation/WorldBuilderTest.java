/*
 * Copyright 2014 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.world.generation;

import org.junit.Test;
import org.terasology.math.Region3i;
import org.terasology.math.Vector3i;
import org.terasology.world.generation.facets.base.BaseFacet3D;

import static org.junit.Assert.*;

public class WorldBuilderTest {
    @Test
    public void borderCalculation() {
        WorldBuilder worldBuilder = new WorldBuilder(12L);
        worldBuilder.addProvider(new Facet1Provider());
        worldBuilder.addProvider(new Facet2Provider());

        World world = worldBuilder.build();
        Region3i regionToGenerate = Region3i.createFromCenterExtents(new Vector3i(), 1);
        Region regionData = world.getWorldData(regionToGenerate);

        Facet1 facet1 = regionData.getFacet(Facet1.class);
        assertEquals(regionToGenerate, facet1.getWorldRegion());

        Facet2 facet2 = regionData.getFacet(Facet2.class);
        assertEquals(Region3i.createFromMinAndSize(new Vector3i(-3, -1, -3), new Vector3i(7, 3, 7)), facet2.getWorldRegion());
    }

    @Test
    public void cumulativeBorderCalculation() {
        WorldBuilder worldBuilder = new WorldBuilder(12L);
        worldBuilder.addProvider(new Facet1Provider());
        worldBuilder.addProvider(new Facet2Provider());
        worldBuilder.addProvider(new Facet3Provider());

        World world = worldBuilder.build();
        Region3i regionToGenerate = Region3i.createFromCenterExtents(new Vector3i(), 1);
        Region regionData = world.getWorldData(regionToGenerate);

        Facet3 facet3 = regionData.getFacet(Facet3.class);
        assertEquals(regionToGenerate, facet3.getWorldRegion());

        Facet1 facet1 = regionData.getFacet(Facet1.class);
        assertEquals(Region3i.createFromMinAndSize(new Vector3i(-2, -1, -2), new Vector3i(5, 3, 5)), facet1.getWorldRegion());

        Facet2 facet2 = regionData.getFacet(Facet2.class);
        assertEquals(Region3i.createFromMinAndSize(new Vector3i(-4, -1, -4), new Vector3i(9, 3, 9)), facet2.getWorldRegion());
    }

    @Test
    public void multiplePathsBorderCalculation() {
        WorldBuilder worldBuilder = new WorldBuilder(12L);
        worldBuilder.addProvider(new Facet1Provider());
        worldBuilder.addProvider(new Facet2Provider());
        worldBuilder.addProvider(new Facet4Provider());

        World world = worldBuilder.build();
        Region3i regionToGenerate = Region3i.createFromCenterExtents(new Vector3i(), 1);
        Region regionData = world.getWorldData(regionToGenerate);

        Facet1 facet1 = regionData.getFacet(Facet1.class);
        assertEquals(regionToGenerate, facet1.getWorldRegion());

        Facet4 facet4 = regionData.getFacet(Facet4.class);
        assertEquals(regionToGenerate, facet4.getWorldRegion());

        Facet2 facet2 = regionData.getFacet(Facet2.class);
        assertEquals(Region3i.createFromMinAndSize(new Vector3i(-4, -1, -4), new Vector3i(9, 3, 9)), facet2.getWorldRegion());
    }

    public static class Facet1 extends BaseFacet3D {
        public Facet1(Region3i targetRegion, Border3D border) {
            super(targetRegion, border);
        }
    }

    public static class Facet2 extends BaseFacet3D {
        public Facet2(Region3i targetRegion, Border3D border) {
            super(targetRegion, border);
        }
    }

    public static class Facet3 extends BaseFacet3D {
        public Facet3(Region3i targetRegion, Border3D border) {
            super(targetRegion, border);
        }
    }

    public static class Facet4 extends BaseFacet3D {
        public Facet4(Region3i targetRegion, Border3D border) {
            super(targetRegion, border);
        }
    }

    @Produces(Facet1.class)
    @Requires(@Facet(value = Facet2.class, border = @FacetBorder(sides = 2)))
    public static class Facet1Provider implements FacetProvider {
        @Override
        public void setSeed(long seed) {

        }

        @Override
        public void process(GeneratingRegion region) {
            Facet1 facet = new Facet1(region.getRegion(), region.getBorderForFacet(Facet1.class));
            region.setRegionFacet(Facet1.class, facet);
        }
    }

    @Produces(Facet2.class)
    public static class Facet2Provider implements FacetProvider {
        @Override
        public void setSeed(long seed) {

        }

        @Override
        public void process(GeneratingRegion region) {
            Facet2 facet = new Facet2(region.getRegion(), region.getBorderForFacet(Facet2.class));
            region.setRegionFacet(Facet2.class, facet);
        }
    }

    @Produces(Facet3.class)
    @Requires(@Facet(value = Facet1.class, border = @FacetBorder(sides = 1)))
    public static class Facet3Provider implements FacetProvider {
        @Override
        public void setSeed(long seed) {

        }

        @Override
        public void process(GeneratingRegion region) {
            Facet3 facet = new Facet3(region.getRegion(), region.getBorderForFacet(Facet3.class));
            region.setRegionFacet(Facet3.class, facet);
        }
    }

    @Produces(Facet4.class)
    @Requires(@Facet(value = Facet2.class, border = @FacetBorder(sides = 3)))
    public static class Facet4Provider implements FacetProvider {
        @Override
        public void setSeed(long seed) {

        }

        @Override
        public void process(GeneratingRegion region) {
            Facet4 facet = new Facet4(region.getRegion(), region.getBorderForFacet(Facet4.class));
            region.setRegionFacet(Facet4.class, facet);
        }
    }
}
