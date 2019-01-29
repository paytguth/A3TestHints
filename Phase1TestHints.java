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
public class Phase1Test{



    /*
    * Allows for customized error messages or hints to be thrown alongside the exception from check()
    */
    public <V,P extends Comparable<P>> void check(String message, V[] b, P[] p, Heap<V,P> mh) throws AssertionError {
        try {
          check(b, p, mh);
        }
        catch (AssertionError e) {
          throw new AssertionError(message);
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
        	assertTrue(mh.c.get(i) != null);

        // invariant 2
        for (int i = mh.size(); i < mh.c.size(); i++)
        	assertTrue(mh.c.get(i) == null);

        // invariant 3
        for (int i = 1; i < mh.size(); i++)
        	assertTrue(mh.c.get(i).priority.compareTo(mh.c.get((i-1)/2).priority) >= 0);

        // check equality with (b,p)
        assertEquals(b.length, mh.size());
        //assertEquals(b.length, mh.map.size());

        // check the entries of c match b and p
        boolean seen[] = new boolean[b.length];
        for (int i = 0; i < mh.size(); i++) {
            V val = mh.c.get(i).value;
            P pri = mh.c.get(i).priority;
            for (int j = 0; j < b.length; j++) {
                if (val.equals(b[j]) && pri.equals(p[j])) {
                    // make sure we don't 't see an element twice
                    assertFalse(seen[j]);
                    seen[j] = true;
                }
            }
        }

        // make sure we saw every element
        for (int i= 0; i < seen.length; i++) {
            assertTrue(seen[i]);
        }
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
    public void test00Add() {
        Heap<Integer,Integer> mh= makeHeap(new Integer[] {5});
        check("\nThe min-heap used in this assignment is stored using an AList<Entry<value,priority>>. What function do you use to add something to the end of an Arraylist?", new Integer[]{5}, new Integer[]{5}, mh);
        Heap<Integer,Integer> mh1= makeHeap(new Integer[] {5, 7});
        check("\nThis check is adding entries whose priorities are in ascending order. You don't need to worry about the swapping just yet", new Integer[]{5, 7}, new Integer[]{5,7}, mh1);
        Heap<Integer,Integer> mh2= makeHeap(new Integer[] {5, 7, 8});
        check("\nCheck and see if you are properly using the generiv values from your add() function", new Integer[]{5, 7, 8}, new Integer[]{5, 7, 8}, mh2);
    }

    @Test
    /**  Test whether swap works in isolation */
    public void test10Swap() {
        Heap<Integer,Integer> mh = new Heap<Integer,Integer>();
        mh.add(10, 5);
        mh.add(11, 5);
        mh.swap(0, 1);
        assertEquals("\nWhat functions lets you get values from your arraylist at a given index?\nWhat function lets you change values in your arraylist at a given index?\n", new Integer(11), mh.c.get(0).value);
        assertEquals("\nOne of the Entries swapped correctly but the other didn't.\nTake another look and make sure you're not getting your indices and values mixed up\n", new Integer(10), mh.c.get(1).value);
        check("\nThe values were swapped correctly but the heap invariants were false.\nCheck and see if you swapped the priorities correctly", new Integer[]{10, 11}, new Integer[]{5, 5}, mh);
    }


    @Test
    /** Test add and bubble up. */
    public void test15Add_BubbleUp() {
        Heap<Integer,Integer> mh= makeHeap(new Integer[]{3});
        check(new Integer[]{3}, new Integer[]{3}, mh);

        Heap<Integer,Integer> mh1= makeHeap(new Integer[]{3, 6});
        check("\nCheck your base case.\nbubbleUp shouldn't swap anything if the new Entry's priority is already greater than it's parents", new Integer[]{3, 6}, new Integer[]{3, 6}, mh1);
        mh1.add(8, 8);
        check("\nCheck your base case.\nbubbleUp shouldn't swap anything if the new Entry's priority is already greater than it's parents", new Integer[]{3, 6, 8}, new Integer[]{3, 6, 8}, mh1);
        mh1.add(5, 5);
        check("\nIf an added node has a smaller priority than it's parent, it should be recursively swapped upward until it meets a parent with a smaller priority", new Integer[]{3, 5, 8, 6}, new Integer[]{3, 5, 8, 6}, mh1);
        mh1.add(4, 4);
        check(new Integer[]{3, 4, 8, 6, 5}, new Integer[]{3, 4, 8, 6, 5}, mh1);
        mh1.add(1, 1);
        check(new Integer[]{1, 4, 3, 6, 5, 8}, new Integer[]{1, 4, 3, 6, 5, 8}, mh1);
    }

    @Test
    /** Test add and bubble up with duplicate priorities */
    public void test17Add_BubbleUpDuplicatePriorities() {
        Heap<Integer,Double> mh= new Heap<Integer,Double>();
        mh.add(4, 4.0);
        check(new Integer[]{4}, new Double[]{4.0}, mh);
        mh.add(2, 4.0);
        check(new Integer[]{4, 2}, new Double[]{4.0, 4.0}, mh);
        mh.add(1, 4.0);
        check(new Integer[]{4, 2, 1}, new Double[]{4.0, 4.0, 4.0}, mh);
        mh.add(0, 4.0);
        check(new Integer[]{4, 2, 1, 0}, new Double[]{4.0, 4.0, 4.0, 4.0}, mh);
    }

    @Test
    /** Test peek. */
    public void test20Peek() {
        Heap<Integer,Integer> mh= makeHeap(new Integer[]{1, 3});
        assertEquals(new Integer(1), mh.peek());
        check(new Integer[]{1, 3}, new Integer[]{1, 3}, mh);

        Heap<Integer,Double> mh1= new Heap<Integer,Double>();
        try {
            mh1.peek();  fail("Didn't throw an exception");
        } catch (NoSuchElementException e) {
            // This is supposed to happen
        } catch (Throwable e){
            fail("Threw something other than PCueException");
        }
    }

    @Test
    /** Test poll and bubbledown with no duplicate priorities. */
    public void test30Poll_BubbleDown_NoDups() {
        Heap<Integer,Integer> mh= makeHeap(new Integer[]{5});
        Integer res= mh.poll();
        assertEquals(new Integer(5), res);
        check(new Integer[]{}, new Integer[]{}, mh);

        Heap<Integer,Integer> mh1= makeHeap(new Integer[]{5, 6});
        Integer res1= mh1.poll();
        assertEquals(new Integer(5), res1);
        check(new Integer[]{6}, new Integer[]{6}, mh1);

        // this requires comparing lchild and rchild and using lchild
        Heap<Integer,Integer> mh2= makeHeap(new Integer[] {4, 5, 6, 7, 8, 9});
        Integer res2= mh2.poll();
        assertEquals(new Integer(4), res2);
        check(new Integer[]{5, 7, 6, 9, 8}, new Integer[]{5, 7, 6, 9, 8}, mh2);

        // this requires comparing lchild and rchild and using rchild
        Heap<Integer,Integer> mh3= makeHeap(new Integer[] {4, 6, 5, 7, 8, 9});
        Integer res3= mh3.poll();
        assertEquals(new Integer(4), res3);
        check(new Integer[]{5, 6, 9, 7, 8}, new Integer[]{5, 6, 9, 7, 8}, mh3);

        // this requires bubbling down when only one child
        Heap<Integer,Integer> mh4= makeHeap(new Integer[] {4, 5, 6, 7, 8});
        Integer res4= mh4.poll();
        assertEquals(new Integer(4), res4);
        check(new Integer[]{5,7, 6, 8}, new Integer[]{5, 7, 6, 8}, mh4);

        Heap<Integer,Integer> mh5= makeHeap(new Integer[] {2, 4, 3, 6, 7, 8, 9});
        Integer res5= mh5.poll();
        assertEquals(new Integer(2), res5);
        check(new Integer[]{3, 4, 8, 6, 7, 9}, new Integer[]{3, 4, 8, 6, 7, 9}, mh5);

        Heap<Integer,Integer> mh6= makeHeap(new Integer[] {2, 4, 3, 6, 7, 9, 8});
        Integer res6= mh6.poll();
        assertEquals(new Integer(2), res6);
        check(new Integer[]{3, 4, 8, 6, 7, 9}, new Integer[]{3, 4, 8, 6, 7, 9}, mh6);

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
    public void test40testDuplicatePriorities() {
        // values should not bubble up or down past ones with duplicate priorities.
        // First two check bubble up
        Heap<Integer,Double> mh1= makeHeap(new Integer[] {6}, new double[] {4});
        mh1.add(5, 4.0);
        check(new Integer[]{6, 5}, new Double[]{4.0, 4.0}, mh1);

        Heap<Integer,Double> mh2= makeHeap(new Integer[] {7, 6}, new double[] {4, 4});
        mh2.add(3, 4.0);
        check(new Integer[]{7, 6, 3}, new Double[]{4.0, 4.0, 4.0}, mh2);

        // Check bubble up
        Heap<Integer,Double> mh3= makeHeap(new Integer[] {5, 6, 7}, new double[] {4, 4, 4});
        mh3.poll();
        check(new Integer[]{7, 6}, new Double[]{4.0, 4.0}, mh3);

     // Check bubble up
        Heap<Integer,Double> mh4= makeHeap(new Integer[] {5, 7, 6, 8}, new double[] {4, 4, 4, 4});
        mh4.poll();
        check(new Integer[]{8, 7, 6}, new Double[]{4.0, 4.0, 4.0}, mh4);

    }




    @Test
    /** Test a few calls with Strings */
    public void test70Strings() {
        Heap<String,Integer> mh= new Heap<String,Integer>();
        check(new String[]{}, new Integer[]{}, mh);
        mh.add("abc", 5);
        check(new String[]{"abc"}, new Integer[]{5}, mh);
        mh.add("beep", 3);
        check(new String[]{"beep", "abc"}, new Integer[]{3, 5}, mh);
        mh.add("", 2);
        check(new String[]{"", "abc", "beep"}, new Integer[]{2, 5, 3}, mh);
        String p= mh.poll();
        assertEquals("", p);
        check(new String[]{"beep", "abc"}, new Integer[]{3, 5}, mh);
    }

    @Test
    /** Test using values in 0..999 and random values for the priorities.
     *  There will be duplicate priorities. */
    public void test90BigTests() {
        // The values to put in Heap
        int[] b= new int[1000];
        for (int k= 0; k < b.length; k= k+1) {
            b[k]= k;
        }

        Random rand= new Random(52);

        // bp: priorities of the values
        double[] bp= new double[b.length];
        for (int k= 0; k < bp.length; k= k+1) {
            bp[k]= (int)(rand.nextDouble()*bp.length);
        }

        // Build the Heap and map to be able to get priorities easily
        Heap<Integer,Double> mh= new Heap<Integer,Double>();
        HashMap<Integer, Double> hashMap= new HashMap<Integer, Double>();
        for (int k= 0; k < b.length; k= k+1) {
            mh.add(b[k], bp[k]);
            hashMap.put(b[k], bp[k]);
        }

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
            assertEquals(true, prevPriority <= p);
            prevPriority= p;
        }

        // Sort bpoll and check that it contains 0..bpoll.length-1
        Arrays.sort(bpoll);
        for (int k= 0; k < b.length; k= k+1) {
            assertEquals(k, bpoll[k]);
        }
        // System.out.println("duplicate priorities: " + dups);
    }

}
