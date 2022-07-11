package org.objectdetection.framework;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetectionDataTest {
    private DetectionData data;

    @Before
    public void set() {
        List<Map<String, Float>> list = new ArrayList<>();
        Map<String, Float> map1 = new HashMap<>();
        map1.put("Cat", 4.1F);
        map1.put("Dog", 70.9F);
        map1.put("Tiger", 25.0F);

        Map<String, Float> map2 = new HashMap<>();
        map2.put("Cat", 5.25F);
        map2.put("Dog", 4.7F);
        map2.put("Snake", 90.05F);

        Map<String, Float> map3 = new HashMap<>();
        map3.put("Rabbit", 0.296F);
        map3.put("Dog", 99.704F);
        list.add(map1);
        list.add(map2);
        list.add(map3);

        this.data = new DetectionData(list);

    }

    @Test
    public void testOverviewObjects() {
        Map<String, Integer> res = data.overviewObjects();

        assertEquals(res.keySet().size(), 5);
        assertTrue(res.containsKey("Cat"));
        assertTrue(res.containsKey("Dog"));
        assertTrue(res.containsKey("Tiger"));
        assertTrue(res.containsKey("Rabbit"));
        assertTrue(res.containsKey("Snake"));

        assertEquals((int) res.get("Cat"), 2);
        assertEquals((int) res.get("Dog"), 3);
        assertEquals((int) res.get("Tiger"), 1);
        assertEquals((int) res.get("Rabbit"), 1);
        assertEquals((int) res.get("Snake"), 1);
    }

    @Test
    public void testGetMostFrequentObject() {
        String res = data.getMostFrequentObject();

        assertTrue(res.equals("Dog"));
    }

    @Test
    public void testSearchObject() {
        List<Integer> indexes = data.searchObject("Dog");

        assertEquals(indexes.size(), 3);
        assertTrue(indexes.get(0) == 0);
        assertTrue(indexes.get(1) == 1);
        assertTrue(indexes.get(2) == 2);
    }

}
