package org.objectdetection.framework;

import org.junit.Test;
import static org.junit.Assert.*;

public class ImageSourceTest {
    private ImageSource source;


    @Test
    public void testAddImageByURL() {
        source = new ImageSource();
        source.addImageByURL("https://static01.nyt.com/images/2021/09/14/science/07CAT-STRIPES/07CAT-STRIPES-superJumbo.jpg?quality=75&auto=webp");
        source.addImageByURL("https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/golden-retriever-royalty-free-image-506756303-1560962726.jpg?crop=1.00xw:0.756xh;0,0.0756xh&resize=980:*");
        source.addImageByURL("https://cdn.britannica.com/89/149189-050-68D7613E/Bengal-tiger.jpg");

        assertEquals(source.size(), 3);
        assertTrue(source.getType(0).equals("url"));
        assertTrue(source.getType(1).equals("url"));
        assertTrue(source.getType(2).equals("url"));

        assertTrue(source.getLocation(0).equals("https://static01.nyt.com/images/2021/09/14/science/07CAT-STRIPES/07CAT-STRIPES-superJumbo.jpg?quality=75&auto=webp"));
        assertTrue(source.getLocation(1).equals("https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/golden-retriever-royalty-free-image-506756303-1560962726.jpg?crop=1.00xw:0.756xh;0,0.0756xh&resize=980:*"));
        assertTrue(source.getLocation(2).equals("https://cdn.britannica.com/89/149189-050-68D7613E/Bengal-tiger.jpg"));
    }

    @Test
    public void testAddImageByPath() {
        source = new ImageSource();
        source.addImageByPath("../../../image/Project_structure.png");
        source.addImageByPath("../../../image/test1.png");
        source.addImageByPath("../../../image/test2.png");

        assertEquals(source.size(), 3);
        assertTrue(source.getType(0).equals("path"));
        assertTrue(source.getType(1).equals("path"));
        assertTrue(source.getType(2).equals("path"));

        assertTrue(source.getLocation(0).equals("../../../image/Project_structure.png"));
        assertTrue(source.getLocation(1).equals("../../../image/test1.png"));
        assertTrue(source.getLocation(2).equals("../../../image/test2.png"));
    }
}
