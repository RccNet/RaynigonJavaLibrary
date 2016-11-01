package test.raynigon.lib.datatypes;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.raynigon.lib.datatypes.ArrayUtils;

public class ArrayUtilsTest{

    @Test
    public void testFindElementObject(){
        String[] array = new String[]{"123", "hallo", "welt", "dies", "das", "sonst", "was"};
        int pos = ArrayUtils.findElement(array, "hallo");
        assertEquals(1, pos);
    }
    
    @Test
    public void testRemoveElementObject(){
        String[] array = new String[]{"123", "hallo", "welt", "dies", "das", "sonst", "was"};
        String[] expResult = new String[]{"123", "hallo", "dies", "das", "sonst", "was"};
        String[] realResult = ArrayUtils.removeElement(array, "welt");
        assertArrayEquals(expResult, realResult);
    }
    
    @Test
    public void testRemoveElementInt(){
        int[] array = new int[]{1,2,3,4,5,6,7,8,9};
        int[] expResult = new int[]{1,2,3,4,5,6,8,9};
        int[] realResult = ArrayUtils.removeElement(array, 7);
        assertArrayEquals(expResult, realResult);
    }

    @Test
    public void testRemoveElementChar(){
        char[] array = new char[]{'A', 'c', 'G', 'H', '5', 't', 'Z'};
        char[] expResult = new char[]{'A', 'c', 'H', '5', 't', 'Z'};
        char[] realResult = ArrayUtils.removeElement(array, 'G');
        assertArrayEquals(expResult, realResult);
    }
    
    @Test
    public void testRemoveElementLong(){
        long[] array = new long[]{1,2,3,4,5,6,7,8,9};
        long[] expResult = new long[]{1,2,3,4,5,6,8,9};
        long[] realResult = ArrayUtils.removeElement(array, 7);
        assertArrayEquals(expResult, realResult);
    }
}
