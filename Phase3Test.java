
package heap;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Random;

import org.junit.Test;
import org.junit.runners.MethodSorters;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Phase3Test {

    /*
    * Allows for customized error messages or hints to be thrown alongside the error from check()
    */
    public <V,P extends Comparable<P>> void check(String message, V[] b, P[] p, Heap<V,P> mh) {
      try {
        check(b, p, mh);
      }
      catch(AssertionError e) {
        System.out.println("\nHINT: " + message;)
        throw e;
      }
    }
    /** Use assertEquals to check that mh correctly represents a heap
     *  with values b and priorities p.
     *  This means that:<ul>
     *    <li> for each i in 0..size-1, (b[i], p[i]) is in mh.c
     *    <li> mh.size() = b.length = p.length
     *    <li> mh satisfies its invariants.
     *  </ul>
     *
     *  <p>Precondition: b.length = p.length.  */
    public <V,P extends Comparable<P>> void check(V[] b, P[] p, Heap<V,P> mh) {
        assert b.length == p.length;

        //System.out.println(mh.c);
        // invariant 1
        for (int i = 0; i < mh.size(); i++)
        	assertTrue("Heap invariant not satisfied: All levels except the last should be full, nodes in last level should be as far left as possible", mh.c.get(i) != null);

        // invariant 2
        for (int i = mh.size(); i < mh.c.size(); i++)
        	assertTrue("Heap invariant not satisfied: Nodes in last level should be as far left as possible", mh.c.get(i) == null);

        // invariant 3
        for (int i = 1; i < mh.size(); i++)
        	assertTrue(c, mh.c.get(i).priority.compareTo(mh.c.get((i-1)/2).priority) >= 0);

        // check equality with (b,p)
        assertEquals("Heap invariant not satisfied: The number of values in the heap should be equal to the size value of the heap", b.length, mh.size());

        // invariant 4:
        assertEquals("Heap/Map invariant not satisfied: The number of values in the heap should be equal to the size of the map", b.length, mh.map.getSize());


        // invariant 5:
        for (int i= 0; i < mh.c.size(); i++) {
        	// check that (b,p) is in the map
        	assertTrue("Map did not contain a key that should have existed", mh.map.containsKey(b[i]));
        	int n = mh.map.get(b[i]);
        	assertEquals("The index stored in the map for a given value did not represent the correct index in the heaps arraylist", mh.c.get(n).value, b[i]);
        	assertTrue("This test found the index of a value in the arraylist using the map, but the priority of this value was not correct", mh.c.get(n).priority.compareTo(p[i]) == 0);
        }

        //// check the entries of c match b and p
        //boolean seen[] = new boolean[b.length];
        //for (int i = 0; i < mh.size(); i++) {
            //V val = mh.c.get(i).value;
            //P pri = mh.c.get(i).priority;
            //for (int j = 0; j < b.length; j++) {
                //if (val.equals(b[j]) && pri.equals(p[j])) {
                    //// make sure we don't 't see an element twice
                    //assertFalse(seen[j]);
                    //seen[j] = true;
                //}
            //}
        //}

        //// make sure we saw every element
        //for (int i= 0; i < seen.length; i++) {
            //assertTrue(seen[i]);
        //}
    }

    /**Return a heap with the values of b added to it, in that order. The
     * priorities are the values. */
    public Heap<Integer,Integer> makeHeap(Integer[] b) {
        Heap<Integer,Integer> m= new Heap<>();
        for (Integer e : b) m.add(e, e);
        return m;
    }

    /**Return a heap with the values of b and corresponding priorities p
     * added to it, in that order.  */
    public Heap<Integer,Double> makeHeap(Integer[] b, double[] p) {
        Heap<Integer,Double> m= new Heap<>();
        for (int h= 0; h < b.length; h= h+1) {
            m.add(b[h], p[h]);
        }
        return m;
    }

    /**Return a heap with the values of b and corresponding priorities p
     * added to it, in that order.  */
    public Heap<String,Double> makeHeap(String[] b, double[] p) {
        Heap<String,Double> m= new Heap<String,Double>();
        for (int h= 0; h < b.length; h= h+1) {
            m.add(b[h], p[h]);
        }
        return m;
    }


    @Test
    /** Test whether add works when the priority of the value being added is
     * not smaller than priorities of other values in the heap. */
    public void test300Add() {
        Heap<Integer,Integer> mh= makeHeap(new Integer[] {5});
        check("\nFor phase 3 the hashmap will be used to create a more optimized heap. Store the index of the heap element as a value for the map, and use the heap-value as the key", new Integer[]{5}, new Integer[]{5}, mh);
        String dataStructureHint = "\nStore the index of the heap element into the map and use the heap value as the maps key. No bubbling up or down required yet";
        Heap<Integer,Integer> mh1= makeHeap(new Integer[] {5, 7});
        check(dataStructureHint, new Integer[]{5, 7}, new Integer[]{5,7}, mh1);

        Heap<Integer,Integer> mh2= makeHeap(new Integer[] {5, 7, 8});
        check(dataStructureHint, new Integer[]{5, 7, 8}, new Integer[]{5, 7, 8}, mh2);
    }

    @Test
    /**  Test whether swap works in isolation */
    public void test310Swap() {
        Heap<Integer,Integer> mh = new Heap<Integer,Integer>();
        mh.add(10, 5);
        mh.add(11, 5);
        mh.swap(0, 1);
        check("\nThis function swaps two values in the hashmap. Edit the heap swap function so that it also swaps the respective data stored in the map",new Integer[]{11, 10}, new Integer[]{5, 5}, mh);
    }


    @Test
    /** Test add and bubble up. */
    public void test315Add_BubbleUp() {
        Heap<Integer,Integer> mh= makeHeap(new Integer[]{3});
        check("\nThis test adds one entry to an empty heap", new Integer[]{3}, new Integer[]{3}, mh);

        Heap<Integer,Integer> mh1= makeHeap(new Integer[]{3, 6});
        check("\nThis test adds two entries with ascending priorities to your heap", new Integer[]{3, 6}, new Integer[]{3, 6}, mh1);
        mh1.add(8, 8);
        check("\nThis test adds three entries with ascending priorities to your heap", new Integer[]{3, 6, 8}, new Integer[]{3, 6, 8}, mh1);
        String hint = "\nUse the upgraded swap function to bubble up. Try and print the map and the heap to make sure the map and heap are consistent after bubbling up.";
        mh1.add(5, 5);
        check(hint, new Integer[]{3, 5, 8, 6}, new Integer[]{3, 5, 8, 6}, mh1);
        mh1.add(4, 4);
        check(hint, new Integer[]{3, 4, 8, 6, 5}, new Integer[]{3, 4, 8, 6, 5}, mh1);
        mh1.add(1, 1);
        check(hint, new Integer[]{1, 4, 3, 6, 5, 8}, new Integer[]{1, 4, 3, 6, 5, 8}, mh1);
    }

    @Test
    /** Test add and bubble up with duplicate priorities */
    public void test317Add_BubbleUpDuplicatePriorities() {
        String hint = "\nThis test is simply adding new heap values which all have duplicate priorities. BubbleUp should recognize this and refrain from swaps";
        Heap<Integer,Double> mh= new Heap<Integer,Double>();
        mh.add(4, 4.0);
        check(hint, new Integer[]{4}, new Double[]{4.0}, mh);
        mh.add(2, 4.0);
        check(hint, new Integer[]{4, 2}, new Double[]{4.0, 4.0}, mh);
        mh.add(1, 4.0);
        check(hint, new Integer[]{4, 2, 1}, new Double[]{4.0, 4.0, 4.0}, mh);
        mh.add(0, 4.0);
        check(hint, new Integer[]{4, 2, 1, 0}, new Double[]{4.0, 4.0, 4.0, 4.0}, mh);
    }

    @Test
    /** Test poll and bubbledown with no duplicate priorities. */
    public void test330Poll_BubbleDown_NoDups() {
        String pollReturnHint = "This test failed because .poll did not return the correct value";
        Heap<Integer,Integer> mh= makeHeap(new Integer[]{5});
        Integer res= mh.poll();
        assertEquals(pollReturnHint, new Integer(5), res);
        check(new Integer[]{}, new Integer[]{}, mh);

        Heap<Integer,Integer> mh1= makeHeap(new Integer[]{5, 6});
        Integer res1= mh1.poll();
        assertEquals(pollReturnHint, new Integer(5), res1);
        check(new Integer[]{6}, new Integer[]{6}, mh1);

        String bubbleDownTwoChildrenHint = "\nThe correct value was returned but a heap or map invariant failed. This test requires comparing the two children nodes to find the smaller child";
        // this requires comparing lchild and rchild and using lchild
        Heap<Integer,Integer> mh2= makeHeap(new Integer[] {4, 5, 6, 7, 8, 9});
        Integer res2= mh2.poll();
        assertEquals(pollReturnHint, new Integer(4), res2);
        check(bubbleDownTwoChildrenHint, new Integer[]{5, 7, 6, 9, 8}, new Integer[]{5, 7, 6, 9, 8}, mh2);

        // this requires comparing lchild and rchild and using rchild
        Heap<Integer,Integer> mh3= makeHeap(new Integer[] {4, 6, 5, 7, 8, 9});
        Integer res3= mh3.poll();
        assertEquals(pollReturnHint, new Integer(4), res3);
        check(bubbleDownTwoChildrenHint, new Integer[]{5, 6, 9, 7, 8}, new Integer[]{5, 6, 9, 7, 8}, mh3);

        // this requires bubbling down when only one child
        Heap<Integer,Integer> mh4= makeHeap(new Integer[] {4, 5, 6, 7, 8});
        Integer res4= mh4.poll();
        assertEquals(pollReturnHint, new Integer(4), res4);
        check("\nThe correct value was returned but the heap or map was not correct. This test requires bubbleDown on a node with only one child", new Integer[]{5,7, 6, 8}, new Integer[]{5, 7, 6, 8}, mh4);

        String message = "\nThe correct value was returned but the heap or map was not correct. Remember that bubbledown should swap the entry with it's smallest child until both children are greater or the node reaches the bottom";
        Heap<Integer,Integer> mh5= makeHeap(new Integer[] {2, 4, 3, 6, 7, 8, 9});
        Integer res5= mh5.poll();
        assertEquals(pollReturnHint, new Integer(2), res5);
        check(message, new Integer[]{3, 4, 8, 6, 7, 9}, new Integer[]{3, 4, 8, 6, 7, 9}, mh5);

        Heap<Integer,Integer> mh6= makeHeap(new Integer[] {2, 4, 3, 6, 7, 9, 8});
        Integer res6= mh6.poll();
        assertEquals(pollReturnHint, new Integer(2), res6);
        check(message, new Integer[]{3, 4, 8, 6, 7, 9}, new Integer[]{3, 4, 8, 6, 7, 9}, mh6);

        Heap<Integer,Integer> mh7= new Heap<Integer,Integer>();
        try {
            mh7.poll();  fail("Didn't throw an exception");
        } catch (NoSuchElementException e) {
            // This is supposed to happen
        } catch (Throwable e){
            fail("Threw something other than PCueException");
        }
    }

    @Test
    /** Test bubble-up and bubble-down with duplicate priorities. */
    public void test340testDuplicatePriorities() {
        // values should not bubble up or down past ones with duplicate priorities.
        // First two check bubble up
        String bubbleUpDupPriorities = "\nThis test uses add and bubbleup on a heap where all nodes have the same priority";
        Heap<Integer,Double> mh1= makeHeap(new Integer[] {6}, new double[] {4});
        mh1.add(5, 4.0);
        check(bubbleUpDupPriorities, new Integer[]{6, 5}, new Double[]{4.0, 4.0}, mh1);

        Heap<Integer,Double> mh2= makeHeap(new Integer[] {7, 6}, new double[] {4, 4});
        mh2.add(3, 4.0);
        check(bubbleUpDupPriorities, new Integer[]{7, 6, 3}, new Double[]{4.0, 4.0, 4.0}, mh2);

        // Check bubble down
        String bubbleDownDupPriorities = "\nThis test uses .poll and bubbleDown on a heap where all nodes have the same priority";
        Heap<Integer,Double> mh3= makeHeap(new Integer[] {5, 6, 7}, new double[] {4, 4, 4});
        mh3.poll();
        check(bubbleDownDupPriorities, new Integer[]{7, 6}, new Double[]{4.0, 4.0}, mh3);

        // Check bubble down
        Heap<Integer,Double> mh4= makeHeap(new Integer[] {5, 7, 6, 8}, new double[] {4, 4, 4, 4});
        mh4.poll();
        check(bubbleDownDupPriorities, new Integer[]{8, 7, 6}, new Double[]{4.0, 4.0, 4.0}, mh4);

    }

    @Test
    /** Test the contains method */
    public void test350contains() {
        Heap<Integer,Integer> mh1= makeHeap(new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9});
        check(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9}, new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9}, mh1);
        for (int i = 1; i < 10; i++) {
            assertTrue(".contains did not return true with an existing value. Use map.get in the contains() function to check if the heap contains an entry with a certain value", mh1.contains(i));
        }
        String containsReturnHint = "Contains() did not return false when called with a nonexisting value";
        assertFalse(containsReturnHint, mh1.contains(0));
        assertFalse(containsReturnHint, mh1.contains(11));
        assertFalse(containsReturnHint, mh1.contains(974));
    }


    @Test
    /** Test updatePriority. */
    public void test360ChangePriority() {
        // First three: bubble up tests
        String changePriNoBubbling = "\nThis check changes the priority of an entry, but doesn't require any swapping afterward since the heap invariant is still satisfied";
        Heap<Integer,Integer> mh1= makeHeap(new Integer[] {1, 2, 3, 5, 6, 7, 9});
        mh1.changePriority(5, 4);
        check(changePriNoBubbling, new Integer[]{1, 2, 3, 5, 6, 7, 9}, new Integer[]{1, 2, 3, 4, 6, 7, 9}, mh1);

        Heap<Integer,Integer> mh2= makeHeap(new Integer[] {1, 2, 3, 5, 6, 7, 9});
        mh2.changePriority(2, 1);
        check(changePriNoBubbling, new Integer[]{1, 2, 3, 5, 6, 7, 9}, new Integer[]{1, 1, 3, 5, 6, 7, 9}, mh2);

        Heap<Integer,Integer> mh3= makeHeap(new Integer[] {1, 2, 3, 5, 6, 7, 9});
        mh3.changePriority(5, 1);
        check("\nThis check changes the priority of an entry, and requires bubbling up for the heap priority invariant to be satisfied", new Integer[]{1, 5, 3, 2, 6, 7, 9}, new Integer[]{1, 1, 3, 2, 6, 7, 9}, mh3);

        String changePriBubbleDown = "\nThis check changes the priority of an entry, and requires bubbling down for the heap priority invariant to be satisfied";
       // second three: bubble down tests
        Heap<Integer,Integer> mh4= makeHeap(new Integer[] {1, 2, 3, 5, 6, 7, 9});
        mh4.changePriority(2, 5);
        check(changePriBubbleDown, new Integer[]{1, 2, 3, 5, 6, 7, 9}, new Integer[]{1, 5, 3, 5, 6, 7, 9}, mh4);

        Heap<Integer,Integer> mh5= makeHeap(new Integer[] {1, 2, 3, 5, 6, 7, 9});
        mh5.changePriority(2, 6);
        check(changePriBubbleDown, new Integer[]{1, 5, 3, 2, 6, 7, 9}, new Integer[]{1, 5, 3, 6, 6, 7, 9}, mh5);

        Heap<Integer,Integer> mh6= makeHeap(new Integer[] {1, 2, 3, 5, 6, 7, 9});
        mh6.changePriority(1, 7);
        check(changePriBubbleDown, new Integer[]{2, 5, 3, 1, 6, 7, 9}, new Integer[]{2, 5, 3, 7, 6, 7, 9}, mh6);

        Heap<Integer,Integer> mh7= new Heap<Integer,Integer>();
        mh7.add(5, 5);
        try {
            mh7.changePriority(6, 5);  fail("Didn't throw an exception");
        } catch (IllegalArgumentException e) {
            // This is supposed to happen
        } catch (Throwable e){
            fail("Threw something other than IllegalArgumentException");
        }
    }


    @Test
    /** Test a few calls with Strings */
    public void test370Strings() {
        Heap<String,Integer> mh= new Heap<String,Integer>();
        check("\nThis check is creating an empty heap with String as the data type for value (Heap<String,Integer>)", new String[]{}, new Integer[]{}, mh);
        mh.add("abc", 5);
        String heapWithStrings = "\nThis check is using strings as values in your heap instead of integers";
        check(heapWithStrings, new String[]{"abc"}, new Integer[]{5}, mh);
        mh.add("beep", 3);
        check(heapWithStrings, new String[]{"beep", "abc"}, new Integer[]{3, 5}, mh);
        mh.add("", 2);
        check(heapWithStrings, new String[]{"", "abc", "beep"}, new Integer[]{2, 5, 3}, mh);
        String p= mh.poll();
        assertEquals(".Poll did not return the correct value on a heap of strings.", "", p);
        check("\n.Poll returned the proper value, but incorrectly modified the heap of strings", new String[]{"beep", "abc"}, new Integer[]{3, 5}, mh);

        assertTrue("Contains() did not return true when called with a string value that exists in the heap", mh.contains("beep"));
        assertFalse("Contains() did not return false when called with a string value that doesn't exist in the heap", mh.contains("boop"));
    }

    @Test
    /** Test using values in 0..999 and random values for the priorities.
     *  There will be duplicate priorities. */
    public void test390BigTests() {
        // The values to put in Heap
        Integer[] b= new Integer[1000];
        for (int k= 0; k < b.length; k= k+1) {
            b[k]= k;
        }

        Random rand= new Random(52);

        // bp: priorities of the values
        Double[] bp= new Double[b.length];
        for (int k= 0; k < bp.length; k= k+1) {
            bp[k]= new Double((int)(rand.nextDouble()*bp.length));
        }


        // Build the Heap and map to be able to get priorities easily
        Heap<Integer,Double> mh= new Heap<Integer,Double>();
        HashMap<Integer, Double> hashMap= new HashMap<Integer, Double>();
        for (int k= 0; k < b.length; k= k+1) {
            mh.add(b[k], bp[k]);
            hashMap.put(b[k], bp[k]);
        }

        check("\nA heap or map invariant was not satisfied on a huge test. Values are Integers and Priorities are Doubles", b, bp, mh);

        // poll values one by one, check that priorities are in order, store
        // in dups the number of duplicate priorities, and save polled value
        // in array bpoll.
        double prevPriority= -1;
        int dups= 0; //Number of duplicate keys,
        int[] bpoll= new int[b.length];
        for (int k= 0; k < b.length; k= k+1) {
            bpoll[k]= mh.poll();
            Double p= hashMap.get(bpoll[k]);
            if (p == prevPriority) {
                dups= dups + 1;
            }
            assertEquals("This test failed because one of the parent nodes had a priority that greater than its child", true, prevPriority <= p);
            prevPriority= p;
        }

        // Sort bpoll and check that it contains 0..bpoll.length-1
        Arrays.sort(bpoll);
        for (int k= 0; k < b.length; k= k+1) {
            assertEquals("This test failed because the list of values collected from calling .poll on the heap was not equal to the actual list of values put in the heap", k, bpoll[k]);
        }
        // System.out.println("duplicate priorities: " + dups);
    }

}
