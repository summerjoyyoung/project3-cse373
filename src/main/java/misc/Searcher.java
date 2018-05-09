package misc;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;

public class Searcher {
    /**
     * This method takes the input list and returns the top k elements
     * in sorted order.
     *
     * So, the first element in the output list should be the "smallest"
     * element; the last element should be the "biggest".
     *
     * If the input list contains fewer then 'k' elements, return
     * a list containing all input.length elements in sorted order.
     *
     * This method must not modify the input list.
     *
     * @throws IllegalArgumentException  if k < 0
     */
    public static <T extends Comparable<T>> IList<T> topKSort(int k, IList<T> input) {
        // Implementation notes:
        //
        // - This static method is a _generic method_. A generic method is similar to
        //   the generic methods we covered in class, except that the generic parameter
        //   is used only within this method.
        //
        //   You can implement a generic method in basically the same way you implement
        //   generic classes: just use the 'T' generic type as if it were a regular type.
        //
        // - You should implement this method by using your ArrayHeap for the sake of
        //   efficiency.
        if (k < 0) {
            throw new IllegalArgumentException("Not a valid numbr of elements");
        }
                
        IPriorityQueue<T> maxHeap = new ArrayHeap<>();
        IList<T> result = new DoubleLinkedList<>();
        
        if (k >= input.size()) {
            result = input; 
            for (int i = 0; i < input.size(); i++) {
                T temp = input.get(i); 
                if (temp == null) {
                    throw new IllegalArgumentException("null null");
                }
                maxHeap.insert(temp);
            }
            for (int i = 0; i < input.size(); i++) {
                result.add(maxHeap.removeMin());
            }
        } else if (k > 0) {
            result = input; 
            for (int i = 0; i < input.size(); i++) {
                T temp = input.get(i); 
                if (temp == null) {
                    throw new IllegalArgumentException("null null");
                }
                maxHeap.insert(temp);
            }
            for (int i = 0; i < (input.size() - k); i++) {
                maxHeap.removeMin();
            }
            
            for (int i = 0; i < k; i++) {
                result.add(maxHeap.removeMin());
            }          
        }
        
        return result; 

        // Old Version 1 
//        if (k >= input.size()) {
//            for (int i = 0; i < input.size(); i++) {
//                T temp = input.get(i); 
//                if (temp == null ) {
//                    throw new IllegalArgumentException("null null");
//                }
//                maxHeap.insert(temp);
//            }
//        } else if (k > 0) {
//            for (int i = 0; i < k; i++) {
//                T temp = input.get(i); 
//                if (temp == null ) {
//                    throw new IllegalArgumentException("null null");
//                }
//                maxHeap.insert(temp);
//            }
//            for (int i = k; i < input.size(); i++) {
//                T temp = input.get(i); 
//                if (temp == null) {
//                    throw new IllegalArgumentException("null null");
//                }
//                if (temp.compareTo(maxHeap.peekMin()) > 0) {
//                    maxHeap.removeMin();
//                    maxHeap.insert(temp);
//                }               
//            }
//        }
        
        // Old version 2
//        if (k >= input.size()) {
//            for (int i = 0; i < input.size(); i++) {
//                T temp = input.get(i);
//                if (temp == null) {
//                    throw new IllegalArgumentException("null null");
//                }
//                result.insert(temp);
//            }
//        } else if (k > 0) {
//            for (int i = 0; i < Math.min(k, input.size()); i++) {
//                T temp = input.get(i);
//                if (temp == null) {
//                    throw new IllegalArgumentException("null null");
//                }
//                result.insert(temp);
//            }
//            if (!result.isEmpty() && Math.min(k, input.size()) == k) {
//                for (int i = k; i < input.size(); i++) {
//                    T temp = input.get(i);
//                    if (temp == null) {
//                        throw new IllegalArgumentException("null null");
//                    }
//                    if (temp.compareTo(result.peekMin()) > 0) {
//                        result.removeMin();
//                        result.insert(temp);
//                    }
//                }
//            }
//        }
        
        // IF YOU WANNA USE OLD VERSION THEN UNCOMMENT THOSE
//        while(!maxHeap.isEmpty()) {
//            result.add(maxHeap.removeMin());            
//        }
//        return result;
    }
}
