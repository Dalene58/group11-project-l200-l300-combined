import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import structures.DynamicArray;
import structures.LinkedList;
import structures.Stack;

public class DataStructureTests {
    @Test
    void dynamicArrayStoresValues() {
        DynamicArray<Integer> array = new DynamicArray<>();
        array.add(1);
        array.add(2);
        assertEquals(2, array.size());
        assertEquals(2, array.get(1));
    }

    @Test
    void linkedListStoresValues() {
        LinkedList<String> list = new LinkedList<>();
        list.add("a");
        list.add("b");
        assertEquals(2, list.size());
        assertEquals("b", list.get(1));
    }

    @Test
    void stackPushPopWorks() {
        Stack<Integer> stack = new Stack<>();
        stack.push(7);
        assertFalse(stack.isEmpty());
        assertEquals(7, stack.pop());
        assertTrue(stack.isEmpty());
    }
}
