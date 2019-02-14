
package heap;

import java.util.Set;
import java.util.Random;
import java.util.Map;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.Assert.*;
import org.junit.FixMethodOrder;

import org.junit.Test;
import org.junit.runners.MethodSorters;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Phase2Test {

    /* make a hash table containing the giving key-value mappings */
    private static <K,V> HashTable<K,V> make(
            K[] keySet, V[] valSet, int initCap) {
        assert keySet.length == valSet.length;
        HashTable<K,V> hm = new HashTable<K,V>(initCap);
        for (int i = 0; i < keySet.length; i++) {
            hm.put(keySet[i], valSet[i]);
        }

        return hm;
    }

    private static <K,V> HashTable<K,V> make(K[] keySet, V[] valSet) {
        return make(keySet, valSet, 17);
    }

    /*
    * Allows for customized messages or hints to be thrown alongside the error from check()
    */
    private static <K,V> void check(String message, K[] keySet, V[] valSet, HashTable<K,V> hm) {
      try {
        check(keySet, valSet, hm);
      }
      catch(AssertionError e) {
        System.out.println("\nHINT:" + message);
        throw e;
      }
    }
    /* check that the hash table contains the appropriate mappings */
    private static <K,V> void check(K[] keySet, V[] valSet, HashTable<K,V> hm) {
        assert keySet.length == valSet.length;

        assertEquals("HashTable invariant not satisfied: Size value of map and number of keys were not equal", keySet.length, hm.getSize());
        for (int i = 0; i < keySet.length; i++) {
            assertEquals("Value was not able to be retrieved from map", valSet[i], hm.get(keySet[i]));
        }
    }


    //@Test
    //[>* Test zero-param constructor <]
    //public void test200Constructor() {
        //HashTable<Integer,Integer> hm = new HashTable<Integer,Integer>();
        //assertEquals(0, hm.getSize());
        //assertEquals(17, hm.getCapacity());
    //}

    //@Test
    //[>* Test constructor that takes a capacity <]
    //public void test201Constructor() {
        //HashTable<Integer,Integer> hm = new HashTable<Integer,Integer>(31);
        //assertEquals(0, hm.getSize());
        //assertEquals(31, hm.getCapacity());
    //}hm.dump();


    @Test
    /** Test put and get with no and no mod*/
    public void test210PutGet() {
        HashTable<Integer,Integer> hm = new HashTable<Integer,Integer>();
        for (int i = 0; i < 8; i++) {;
            assertEquals("Your .put function did not return null when there were no duplicate keys in map", null, hm.put(i,i));
            assertEquals("Your .put function needs to increase the size of the map by 1 when a new pair is added.", i+1, hm.getSize());
            assertEquals("You increased the capacity of the buckets array when it didn't need it. This test doesn't require any rehashing/load factor business so don't worry about that yet.", 17, hm.getCapacity());
        }
        for (int i = 0; i < 8; i++) {
            assertEquals("Your .get() function didn't return the correct value. In this test, every value should be the first Pair in your array of linked lists which will be indexed by key", new Integer(i), hm.get(i));
        }
        for (int i = 8; i < 17; i++) {
            assertEquals("Your .get function needs to return null when it is called with a key that doesn't exist in the map.", null, hm.get(i));
        }
    }

    @Test
    /** Test put and get with no collisions but mod needed */
    public void test211PutGet() {
        Integer[] keys = new Integer[]{0,1,2,3,22,23,24,25};
        Integer[] vals = new Integer[]{0,1,2,3,22,23,24,25};
        HashTable<Integer,Integer> hm = make(keys, vals);
        check("\nFor this test you need to hash keys to know which index to store it in. For example if the key is 22 and the capacity is 17, it would be stored in index 5 because 22 % 17 is 5", keys, vals, hm);
    }

    @Test
    /** Test that put overwrites and returns a pre-existing value */
    public void test212Put() {
        Integer[] keys = new Integer[]{0,1,2,3,4,5,6,7,8,9};
        Integer[] vals = new Integer[]{0,1,2,3,4,5,6,7,8,9};
        HashTable<Integer,Integer> hm = make(keys, vals);

        for (int i = 0; i < 10; i++) {
            assertEquals("Your .put function needs to return the previous value of a given key when .put is called with a key that was already in the map. Don't forget to overrite the pre-xisting Pair's old value with the new one.", new Integer(i), hm.put(i,i+1));
            assertEquals("The size of your map wasn't correct. .put should increase the size when a new key is added but it shouldn't change the size when a pre-existing key is overwritten", 10, hm.getSize());
        }


    }

    @Test
    /** Test put/get with collisions */
    public void test230PutGet() {
        Integer[] keys = new Integer[]{0,1,2,3,17,18,19};
        Integer[] vals = new Integer[]{0,1,2,3,17,18,19};
        HashTable<Integer,Integer> hm = make(keys, vals);
        check("\nThis test is testing your put and get functions with collisions.\nRemember that if a hash value collides with another, the new Pair should be appended to the end of the linked list.\nTake a look at the Pair class and notice its similarites to a linked list", keys, vals, hm);
    }

    @Test
    /** Test that put overwrites and returns a pre-existing value, with
     * collisions */
    public void test231Put() {
        Integer[] keys = new Integer[]{0,1,2,3,17,18,19};
        Integer[] vals = new Integer[]{0,1,2,3,17,18,19};
        HashTable<Integer,Integer> hm = make(keys, vals);

        for (int i = 0; i < keys.length; i++) {
          assertEquals("Your put function did not properly return the previous value stored with that key", vals[i], hm.put(keys[i],keys[i]+1));
          assertEquals("the size of your map wasn't correct. .put should not change the size if it is replacing a duplicate key.", 7, hm.getSize());
        }


        keys = new Integer[]{0,1,2,3,17,18,19};
        vals = new Integer[]{1,2,3,4,18,19,20};
        check("\nThe mappings of pairs wasn't correct. Try printing out the map using .dump() and debug .put or .get accordingly", keys, vals, hm);
    }

    @Test
    /** Test containsKey (no collisions) */
    public void test240ContainsKey() {
        Integer[] keys = new Integer[]{0,1,2,3,4,5,26,27,28,29};
        Integer[] vals = new Integer[]{0,1,2,3,4,5,26,27,28,29};
        HashTable<Integer,Integer> hm = make(keys, vals);

        int i = 0;
        int ki = 0;
        while (i < 35) {
            if (ki < keys.length && keys[ki] == i) {
                assertEquals("Your containsKey() didn't return true when called with a key that exists in the map.\nRemember that .get already looks for a returns a non-null value when called with a key in the map", true, hm.containsKey(i));
                ki++;
            } else {
                assertEquals("Your containsKey() didn't return false when called with a key that doesn't exist in the map.\nRemember .get should return null when a key is not in the map", false, hm.containsKey(i));
            }
            i++;
        }
    }

    @Test
    /** Test containsKey (with collisions) */
    public void test241ContainsKey() {
        Integer[] keys = new Integer[]{0,1,2,17,18,19};
        Integer[] vals = new Integer[]{0,1,2,17,18,19};
        HashTable<Integer,Integer> hm = make(keys, vals);

        int i = 0;
        int ki = 0;
        while (i < 34) {
            if (ki < keys.length && keys[ki] == i) {
                assertEquals("Your containsKey returned false when called with a valid key in a map with collisions.", true, hm.containsKey(i));
                ki++;
            } else {
                assertEquals("Your containsKey returned true when called with a nonexistant key in a map with collisions.", false, hm.containsKey(i));
            }
            i++;
        }
    }

    @Test
    /** Test that remove works (no collisions) */
    public void test250Remove() {
        Integer[] keys = new Integer[]{0,1,2,3,4,5,6,7,8,9};
        Integer[] vals = new Integer[]{0,1,2,3,4,5,6,7,8,9};
        HashTable<Integer,Integer> hm = make(keys, vals);

        for (int i = 0; i < 10; i++) {
            assertEquals("Your remove function didn't properly return the value of the removed key. This test doesn't have any collisons", vals[i], hm.remove(keys[i]));
            assertEquals("The size of your hashmap wasn't correct after removal. Remove should always decrease the size if the key existed in the map", 10-i-1, hm.getSize());
        }

    }


    @Test
    /** Test that remove works (with collisions) */
    public void test251Remove() {
        Integer[] keys = new Integer[]{0,1,2,17,18,19};
        Integer[] vals = new Integer[]{0,1,2,17,18,19};
        HashTable<Integer,Integer> hm = make(keys, vals);
        assertEquals("When removing the first pair in an index, the next pair should be the head of the list. This test failed because it did not return the correct value of the removed Pair.", vals[0], hm.remove(keys[0]));
        assertEquals(".remove didn't properly modify the size", 5, hm.getSize());
        assertEquals("When removing the last pair in a list, the pair before it should point to null. This test failed because it did not return the correct value of the removed Pair.", vals[5], hm.remove(keys[5]));
        assertEquals(".remove didn't properly modify the size", 4, hm.getSize());
        assertEquals("When removing the last pair in a list, the pair before it should point to null. This test failed because it did not return the correct value of the removed Pair.", vals[4], hm.remove(keys[4]));
        assertEquals(".remove didn't properly modify the size", 3, hm.getSize());
        assertEquals("When removing the only Pair at an index, this index should contain nothing. This test failed because it did not return the correct value of the removed Pair.", vals[2], hm.remove(keys[2]));
        assertEquals(".remove didn't properly modify the size", 2, hm.getSize());
        assertEquals("When removing the only Pair at an index, this index should contain nothing. This test failed because it did not return the correct value of the removed Pair.", vals[1], hm.remove(keys[1]));
        assertEquals(".remove didn't properly modify the size", 1, hm.getSize());
        assertEquals("When removing the only Pair at an index, this index should contain nothing. This test failed because it did not return the correct value of the removed Pair.", vals[3], hm.remove(keys[3]));
        assertEquals(".remove didn't properly modify the size", 0, hm.getSize());

    }



    @Test
    /** Test that table grows when load factor exceeds 0.8 */
    public void test280Grow() {

        Integer[] keys = new Integer[]{0,1,2,17,18,19,20,21,5,6,7,8,9,11};
        Integer[] vals = new Integer[]{0,1,2,17,18,19,20,21,5,6,7,8,9,11};
        HashTable<Integer,Integer> hm = new HashTable<Integer,Integer>();

        for (int i = 0; i < 13; i++) {
            hm.put(keys[i], vals[i]);
            assertEquals("The load factor does not exceed 0.8, your hashtable's capacity changed when it shouldn't have. Make sure you're checking the load factor correctly", 17, hm.getCapacity());
        }

        hm.put(keys[13], vals[13]);
        assertEquals("The hashtable's load factor just exceeded 0.8, and should have doubled capacity from the default 17 to 34. Make sure you're checking the load factor correctly", 34, hm.getCapacity());
    }

    @Test
    /** Test that rehashing works after growth */
    public void test281Rehash() {
        Integer[] keys = new Integer[]{0,1,2,17,18,19,20,21,5,6,7,8,9,11,12,63,64};
        Integer[] vals = new Integer[]{0,1,2,17,18,19,20,21,5,6,7,8,9,11,12,63,64};
        HashTable<Integer,Integer> hm = new HashTable<Integer,Integer>();

        for (int i = 0; i < keys.length; i++) {
            hm.put(keys[i], vals[i]);
        }
        assertEquals("The hashtable's load factor just exceeded 0.8, and should have doubled capacity from the default 17 to 34. Make sure you're checking the load factor correctly", 34, hm.getCapacity());
        check("\n Hashtable didn't rehash correctly. Create a new hashtable with double the size and put all the old hashtable values into the new one with .put", keys, vals, hm);
    }


    @Test
    /** Test a small example with strings */
    public void test290Strings() {
        String[] keys = new String[]{"iztf", "uiu", "eqm", "rzh", "vjw", "ris", "tut", "wbb", "sjb", "lii", "urv", "fhm"};
        Integer[] vals = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12};

        HashTable<String,Integer> hm = make(keys, vals);
        check("This test is using string values as keys. Look up the .hashCode() method and use it in your hash formula", keys, vals, hm);

    }

    @Test
    /** Test a big example with strings, including put (new and replace), get,
     * and remove */
    public void test291Strings() {
        HashMap<String,String> truth = new HashMap<String,String>();
        HashTable<String,String> hm = new HashTable<String,String>();

        try {
            Scanner sc = new Scanner(new File("P2TestInput.txt"));
            while (sc.hasNext()) {
                String k = sc.next();
                String v = sc.next();
                hm.put(k, v);
                truth.put(k, v);
            }
        } catch (FileNotFoundException e) {
            assertTrue("File P2TestInput.txt from skeleton repo not found.", false);
        }

        Set<String> trueKeySet = truth.keySet();
        Random rand = new Random(0);
        Iterator<Map.Entry<String,String>> iter = truth.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String,String> entry = iter.next();
            String k = entry.getKey();
            double roll = rand.nextDouble();
            if (roll > 0.9) {
                // replace k's mapped value with a new random one
                String newVal = "";
                for (int i = 0; i < 4; i++) {
                    newVal = newVal + (char) (97 + rand.nextInt(26));
                }
                hm.put(k, newVal);
                truth.put(k, newVal);
            } else if (roll < 0.1) {
                // remove k from the mapping
                hm.remove(k);
                iter.remove();
            }
        }
        // check that the mappings are equivalent:
        assertEquals(truth.size(), hm.getSize());
        for (String k : truth.keySet()) {
            assertTrue(hm.containsKey(k));
            assertEquals(truth.get(k), hm.get(k));
        }
        assertEquals(hm.getCapacity(), 2176);
    }

    //@Test
    //public void example() {
        //HashTable<Integer,Integer> hm = new HashTable<Integer,Integer>(4);
        //hm.put(0,0);
        //hm.put(4,1);
        //hm.put(19,1);
        //hm.put(19,4);
        //hm.dump();
    //}


}
