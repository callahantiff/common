package edu.ucdenver.ccp.common.collections;

/*
 * #%L
 * Colorado Computational Pharmacology's common module
 * %%
 * Copyright (C) 2012 - 2014 Regents of the University of Colorado
 * %%
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the Regents of the University of Colorado nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil.SortOrder;
import edu.ucdenver.ccp.common.test.DefaultTestCase;

/**
 * This class contains tests for the CollectionsUtil class.
 * 
 * @author bill
 * 
 */
public class CollectionsUtilTest extends DefaultTestCase {

	/**
	 * Tests the normal operation of the createSet() method
	 */
	@Test
	public void testCreateSetWithValidInput() {
		Set<String> expectedSet = new HashSet<String>();
		expectedSet.add("1");
		expectedSet.add("2");
		expectedSet.add("3");

		assertEquals(String.format("Sets should be equal."), expectedSet, CollectionsUtil.createSet("1", "2", "3"));
		assertEquals(String.format("Sets should be equal."), expectedSet,
				CollectionsUtil.createSet("1", "2", "3", "1", "2", "3", "3"));
	}

	/**
	 * Tests the createSet() method using empty/null input
	 */
	@Test
	public void testCreateSetWithEmptyInput() {
		assertEquals(String.format("Should return empty set for no input."), new HashSet<String>(),
				CollectionsUtil.createSet());
		assertNull(String.format("Should return null for null input."), CollectionsUtil.createSet((Object[]) null));
	}

	/**
	 * Tests the normal operation of the createSet()_Iteratable method
	 * 
	 **/

	@Test
	public void testCreateSetIterableWithValidInput() {
		Set<Integer> actualSet = new HashSet<Integer>();
		actualSet.add(1);
		actualSet.add(2);
		actualSet.add(3);
		actualSet.add(4);
		Set<Integer> expectedSet = new HashSet<Integer>();
		expectedSet.add(1);
		expectedSet.add(2);
		expectedSet.add(3);
		expectedSet.add(4);

		assertEquals("Set should get added", expectedSet, CollectionsUtil.createSet(actualSet));

	}

	/**
	 * Tests the createSet()_Iteratable method using empty/null input
	 */

	@Test
	public void testCreateSetIteratableWithEmptyInput() {
		assertEquals(String.format("Should return empty set for no input."), new HashSet<String>(),
				CollectionsUtil.createSet());
		assertNull(String.format("Should return null for null input."), CollectionsUtil.createSet((Object[]) null));
	}

	/**
	 * Tests the normal operation of the createList(Iterator<T> iter) method
	 */
	@Test
	public void testCreateListIteratorWithValidInput() {
		List<String> expectedList = new ArrayList<String>();
		expectedList.add("2");
		expectedList.add("2");
		expectedList.add("3");

		List<String> actualList = new ArrayList<String>();
		actualList.add("2");
		actualList.add("2");
		actualList.add("3");

		Iterator<String> iter = actualList.iterator();
		List<String> observedList = CollectionsUtil.createList(iter);
		assertEquals(String.format("Lists should be equal."), expectedList, observedList);
	}

	/**
	 * Tests the normal operation of the createList() method
	 */
	@Test
	public void testCreateListWithValidInput() {
		List<String> expectedList = new ArrayList<String>();
		expectedList.add("2");
		expectedList.add("2");
		expectedList.add("3");

		assertEquals(String.format("Lists should be equal."), expectedList, CollectionsUtil.createList("2", "2", "3"));
	}

	/**
	 * Tests the createList() method using empty input
	 */
	@Test
	public void testCreateListWithEmptyInput() {
		assertEquals(String.format("Should return empty List for no input."), new ArrayList<String>(),
				CollectionsUtil.createList());
	}

	/**
	 * Tests the normal operation of the createSortedList() method
	 */
	@Test
	public void testCreateSortedList() {
		assertEquals(String.format("List should be sorted."), CollectionsUtil.createList(1, 2, 3, 4, 5),
				CollectionsUtil.createSortedList(CollectionsUtil.createList(5, 3, 2, 1, 4)));
	}

	/**
	 * Tests the normal operation of the parseInts() method
	 */
	@Test
	public void testParseInts() {
		List<String> toParseList = CollectionsUtil.createList("5", "6", "789", "1", "-4", "0");
		List<Integer> expectedIntList = CollectionsUtil.createList(5, 6, 789, 1, -4, 0);
		assertEquals(String.format("The list of Strings should have been converted into a list of Integers."),
				expectedIntList, CollectionsUtil.parseInts(toParseList));
	}

	/**
	 * Tests the normal operation of the array2Set() method
	 */
	@Test
	public void testArray2Set() {
		Set<String> expectedSet = new HashSet<String>();
		expectedSet.add("1");
		expectedSet.add("2");
		expectedSet.add("3");

		assertEquals(String.format("Simple conversion from array to set."), expectedSet,
				CollectionsUtil.array2Set(new String[] { "1", "2", "3" }));
		assertEquals(String.format("Lossy conversion from array to set."), expectedSet,
				CollectionsUtil.array2Set(new String[] { "1", "2", "3", "3", "3", "2" }));
	}

	/**
	 * Tests the normal operation of the createZeroBasedSequence() method
	 */
	@Test
	public void testCreateZeroBasedSequence() {
		int[] expected = new int[] { 0, 1, 2, 3, 4, 5, 6, 7 };
		assertArrayEquals("Should have 8 sequential members.", expected, CollectionsUtil.createZeroBasedSequence(8));
	}

	/**
	 * Tests the normal operation of the createMap() method
	 */
	@Test
	public void testCreateMap() {
		Map<String, String> map = CollectionsUtil.createMap("key1", "value1", "key2", "value2", "key3", "value3");
		Map<String, String> expectedMap = new HashMap<String, String>();
		expectedMap.put("key1", "value1");
		expectedMap.put("key2", "value2");
		expectedMap.put("key3", "value3");
		assertEquals(String.format("Maps should be identical."), expectedMap, map);
	}

	/**
	 * Tests the normal operation of the createMap() method using different types for keys and
	 * values
	 */
	@Test
	public void testCreateMap_2keyValuePairs() {
		Map<Integer, String> map = CollectionsUtil.createMap(1, "value1", 2, "value2");
		Map<Integer, String> expectedMap = new HashMap<Integer, String>();
		expectedMap.put(1, "value1");
		expectedMap.put(2, "value2");
		assertEquals(String.format("Maps should be identical."), expectedMap, map);
	}

	/**
	 * Tests that createMap() with a duplicate keys results in an IllegalArgumentException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMap_2keyValuePairs_duplicateKeys() {
		CollectionsUtil.createMap(1, "value1", 1, "value2");
	}

	/**
	 * Tests the normal operation of the addToOne2ManyUniqueMap() method
	 */
	@Test
	public void testAddToOne2ManyMap() {
		Map<Integer, Set<String>> expectedMap = new HashMap<Integer, Set<String>>();
		expectedMap.put(1, CollectionsUtil.createSet("a", "b", "c"));
		expectedMap.put(2, CollectionsUtil.createSet("d"));

		Map<Integer, Set<String>> map = new HashMap<Integer, Set<String>>();
		CollectionsUtil.addToOne2ManyUniqueMap(1, "a", map);
		CollectionsUtil.addToOne2ManyUniqueMap(2, "d", map);
		CollectionsUtil.addToOne2ManyUniqueMap(1, "b", map);
		CollectionsUtil.addToOne2ManyUniqueMap(1, "c", map);

		assertEquals(String.format("Maps should be equal."), expectedMap, map);
	}

	/**
	 * Tests the normal operation of the combineMaps() method
	 */
	@Test
	public void testCombineMaps() {
		Map<String, String> inputMap1 = CollectionsUtil.createMap("1", "blue", "2", "red", "3", "green");
		Map<String, String> inputMap2 = CollectionsUtil.createMap("4", "purple", "5", "yellow", "1", "green");

		@SuppressWarnings("unchecked")
		Map<String, String> combinedMap = CollectionsUtil.combineMaps(inputMap1, inputMap2);

		@SuppressWarnings("unchecked")
		Map<String, String> expectedMap = CollectionsUtil.createMap(CollectionsUtil.createList(
				new SimpleEntry<String, String>("1", "green"), new SimpleEntry<String, String>("2", "red"),
				new SimpleEntry<String, String>("3", "green"), new SimpleEntry<String, String>("4", "purple"),
				new SimpleEntry<String, String>("5", "yellow")));
		assertEquals(String.format("Map not as expected."), expectedMap, combinedMap);
	}

	/**
	 * Tests the normal operation of the combineUniqueMaps() method
	 */
	@Test
	public void testCombineUniqueMaps() {
		Map<String, Set<String>> inputMap1 = new HashMap<String, Set<String>>();
		inputMap1.put("colors", CollectionsUtil.createSet("blue", "red", "yellow"));
		inputMap1.put("numbers", CollectionsUtil.createSet("one", "two", "three"));

		Map<String, Set<String>> inputMap2 = new HashMap<String, Set<String>>();
		inputMap2.put("colors", CollectionsUtil.createSet("green", "red", "purple"));

		Map<String, Set<String>> expectedMap = new HashMap<String, Set<String>>();
		expectedMap.put("colors", CollectionsUtil.createSet("blue", "red", "yellow", "green", "purple"));
		expectedMap.put("numbers", CollectionsUtil.createSet("one", "two", "three"));

		@SuppressWarnings("unchecked")
		Map<String, Set<String>> combinedMap = CollectionsUtil.combineUniqueMaps(inputMap1, inputMap2);
		assertEquals(String.format("Expected map not the same as combined."), expectedMap, combinedMap);
	}

	/**
	 * Tests the normal operation of the initMap() method
	 */
	@Test
	public void testInitHashMap() {
		String key = "1";
		Integer value = 1;
		Map<String, Integer> map = CollectionsUtil.initHashMap();
		map.put(key, value);
		assertEquals(value, map.get(key));
	}

	/**
	 * Tests the normal operation of the createDelimitedString() method
	 */
	@Test
	public void testCreateDelimitedString() {
		List<String> list = CollectionsUtil.createList("1", "2", "3");
		String delimitedStr = CollectionsUtil.createDelimitedString(list, "|");
		String expectedStr = "1|2|3";
		assertEquals(String.format("output or createDelimitedString not as expected."), expectedStr, delimitedStr);

		List<Integer> numberList = CollectionsUtil.createList(1, 2, 3);
		delimitedStr = CollectionsUtil.createDelimitedString(numberList, "|");
		assertEquals(String.format("output or createDelimitedString not as expected."), expectedStr, delimitedStr);
	}

	/**
	 * Testing createDelimitedString() method with Null and Empty space as collection input
	 */
	@Test
	public void testCreateDelimitedString_EmptySpace() {
		List<String> list = CollectionsUtil.createList();
		assertEquals(0, list.size());
		String delimitedStr = CollectionsUtil.createDelimitedString(list, "|");
		String expectedStr = "";
		assertEquals(String.format("output or createDelimitedString not as expected."), expectedStr, delimitedStr);
	}

	@Test
	public void testCreateDelimitedString_NullInput() {
		List<Integer> list3 = null;
		String delimitedStr = CollectionsUtil.createDelimitedString(list3, "|");
		String expectedlist = null;
		assertEquals(String.format("output or createDelimitedString not as expected."), expectedlist, delimitedStr);

	}

	/**
	 * Tests the normal operation of the toString() method
	 */
	@Test
	public void testToString() {
		Set<Integer> integerSet = CollectionsUtil.createSet(1, 2, 3, 4, 5);
		Set<String> strings = new HashSet<String>(CollectionsUtil.toString(integerSet));
		assertEquals(String.format("Set should now contain strings."),
				CollectionsUtil.createSet("1", "2", "3", "4", "5"), strings);
	}

	/**
	 * Tests the normal operation of the consolidateSets() method.
	 */
	@Test
	public void testConsolidateSets() {
		Collection<Set<String>> sets = new ArrayList<Set<String>>();
		sets.add(CollectionsUtil.createSet("1", "2"));
		sets.add(CollectionsUtil.createSet("2", "3"));

		assertEquals(String.format("Sets should be consolidated"), CollectionsUtil.createSet("1", "2", "3"),
				CollectionsUtil.consolidateSets(sets));

	}

	/**
	 * Tests the normal operation of the consolidateCollection() method.
	 * 
	 */

	@Test
	public void testConsolidateCollection() {
		Collection<Collection<String>> collection = new ArrayList<Collection<String>>();
		collection.add(CollectionsUtil.createList("1", "2"));
		collection.add(CollectionsUtil.createList("2", "3"));

		assertEquals(String.format("Collection should be consolidated"),
				CollectionsUtil.createList("1", "2", "2", "3"), CollectionsUtil.consolidate(collection));

	}

	/**
	 * Tests the operation of the consolidateCollection() method with Null Input.
	 */

	@Test(expected = NullPointerException.class)
	public void testConsolidateCollection_NullInput() {
		Collection<Collection<String>> collection1 = CollectionsUtil.consolidate(null);
		String expectedString = null;
		assertEquals(String.format("Collection should be consolidated"), expectedString, collection1);

	}

	/**
	 * Test for Simple utility to initialize a map with one key/value pair*
	 */

	@Test
	public void testCreateMap_WithOneKeyPairValue() {
		Map<Integer, String> map = CollectionsUtil.createMap(1, "value1");
		Map<Integer, String> expectedMap = new HashMap<Integer, String>();
		expectedMap.put(1, "value1");

		assertEquals(String.format("Maps should be identical."), expectedMap, map);
	}

	/**
	 * Testing Simple utility to initialize a map with Null key value.This is a negative test case*
	 */

	@Test
	public void testCreateMap_WithNullKeyValue() {
		Map<Integer, String> map = CollectionsUtil.createMap(null, "value1");
		Map<Integer, String> expectedMap = new HashMap<Integer, String>();
		expectedMap.put(null, "value1");

		assertEquals(String.format("Maps with Null Key value."), expectedMap, map);
	}

	/**
	 * Testing Simple utility to initialize a map with Empty key value.This is a negative test case*
	 */

	@Test
	public void testCreateMap_WithEmptyKeyValue() {
		Map<String, String> map = CollectionsUtil.createMap("", "value1");
		Map<String, String> expectedMap = new HashMap<String, String>();
		expectedMap.put("", "value1");

		assertEquals(String.format("Maps with Empty Key value."), expectedMap, map);
	}

	/**
	 * Testing Simple utility to initialize a map with Null value.This is a negative test case*
	 */

	@Test
	public void testCreateMap_WithNullPairValue() {
		Map<Integer, String> map = CollectionsUtil.createMap(1, null);
		Map<Integer, String> expectedMap = new HashMap<Integer, String>();
		expectedMap.put(1, null);

		assertEquals(String.format("Maps with Null value."), expectedMap, map);
	}

	/**
	 * Testing Simple utility to initialize a map with Empty value.This is a negative test case*
	 */

	@Test
	public void testCreateMap_WithEmptyValue() {
		Map<Integer, String> map = CollectionsUtil.createMap(1, "");
		Map<Integer, String> expectedMap = new HashMap<Integer, String>();
		expectedMap.put(1, "");

		assertEquals(String.format("Maps with Empty value."), expectedMap, map);
	}

	/**
	 * Tests the normal operation of the getSingleElement() method.
	 */

	@Test
	public void testgetSingleElement_WithValidInput() {

		String expectedString = "a";
		assertEquals("The size of the Collection list should be 1. Else it should throw an error", expectedString,
				CollectionsUtil.getSingleElement(CollectionsUtil.createList("a")));
	}

	/**
	 * Testing the getSingleElement() method with more than 1 collection element. This is a negative
	 * test case
	 */

	@Test(expected = java.lang.IndexOutOfBoundsException.class)
	public void testgetSingleElement_WithGreaterthanOneCollection() {

		List<String> actualList = new ArrayList<String>();
		actualList.add("a");
		actualList.add("b");
		actualList.add("c");
		List<String> expectedList = new ArrayList<String>();
		expectedList.add("a");
		expectedList.add("b");
		expectedList.add("c");

		assertEquals("The size of the Collection list should be 1. Else it should throw an error", expectedList,
				CollectionsUtil.getSingleElement(CollectionsUtil.createList(actualList)));

	}

	/**
	 * Testing the getSingleElement() method with zero element in collection(Without any element in
	 * the collection). This is a negative test case
	 */

	@Test(expected = java.lang.IndexOutOfBoundsException.class)
	public void testgetSingleElement_WithoutInput() {
		CollectionsUtil.getSingleElement(CollectionsUtil.createList());
	}

	/**
	 * Tests the normal operation of the addToOne2ManyMap() method
	 */
	@Test
	public void testAddToOne2ManyMapCollection_ValidInput() {

		Map<Integer, Collection<String>> expectedCollection = new HashMap<Integer, Collection<String>>();
		expectedCollection.put(1, CollectionsUtil.createList("a", "b", "c"));
		expectedCollection.put(2, CollectionsUtil.createList("d", "e", "f"));

		Map<Integer, Collection<String>> map = new HashMap<Integer, Collection<String>>();
		CollectionsUtil.addToOne2ManyMap(1, "a", map);
		CollectionsUtil.addToOne2ManyMap(1, "b", map);
		CollectionsUtil.addToOne2ManyMap(1, "c", map);
		CollectionsUtil.addToOne2ManyMap(2, "d", map);
		CollectionsUtil.addToOne2ManyMap(2, "e", map);
		CollectionsUtil.addToOne2ManyMap(2, "f", map);

		assertEquals(String.format("Maps should get added."), expectedCollection, map);

	}

	/**
	 * Tests the addToOne2ManyMap() method when the method has same key value more than once. This
	 * is a negative test case.
	 */

	@Test
	public void testAddToOne2ManyMapCollection_InValidInput() {

		Map<Integer, Collection<String>> expectedCollection = new HashMap<Integer, Collection<String>>();
		expectedCollection.put(1, CollectionsUtil.createList("a", "b", "c"));
		expectedCollection.put(1, CollectionsUtil.createList("a", "b", "f"));
		Map<Integer, Collection<String>> map = new HashMap<Integer, Collection<String>>();
		CollectionsUtil.addToOne2ManyMap(1, "a", map);
		CollectionsUtil.addToOne2ManyMap(1, "b", map);
		CollectionsUtil.addToOne2ManyMap(1, "f", map);

		assertEquals(String.format("Maps should get added."), expectedCollection, map);

	}

	/**
	 * Tests the addToOne2ManyMap() method when the method has same values for different keys.
	 */

	@Test
	public void testAddToOne2ManyMapCollection_RepeatedInput() {

		Map<Integer, Collection<String>> expectedCollection = new HashMap<Integer, Collection<String>>();
		expectedCollection.put(1, CollectionsUtil.createList("a", "b", "c"));
		expectedCollection.put(2, CollectionsUtil.createList("a", "b", "f"));
		Map<Integer, Collection<String>> map = new HashMap<Integer, Collection<String>>();
		CollectionsUtil.addToOne2ManyMap(1, "a", map);
		CollectionsUtil.addToOne2ManyMap(1, "b", map);
		CollectionsUtil.addToOne2ManyMap(1, "c", map);
		CollectionsUtil.addToOne2ManyMap(2, "a", map);
		CollectionsUtil.addToOne2ManyMap(2, "b", map);
		CollectionsUtil.addToOne2ManyMap(2, "f", map);

		assertEquals(String.format("Maps should get added."), expectedCollection, map);

	}

	/**
	 * Tests the addToOne2ManyMap() method with Null key value. This is a negative test condition.
	 */
	@Test(expected = NullPointerException.class)
	public void testAddToOne2ManyMapCollection_NullInput() {

		Map<Integer, Collection<String>> map = null;
		CollectionsUtil.addToOne2ManyMap(null, null, map);
		String expectedCollection = null;
		assertEquals(String.format("Maps should get added."), expectedCollection, map);
	}

	/**
	 * Tests the filterMap() method with valid test input
	 */
	@Test
	public void testfilterMap_ValidInput() {
		Map<String, String> actualMap = CollectionsUtil.createMap("1", "blue", "2", "red", "3", "green");

		Map<String, String> expectedMap = CollectionsUtil.createMap("1", "blue", "2", "red", "3", "green");

		Set<String> actualSet = new HashSet<String>();
		actualSet.add("1");
		actualSet.add("2");
		actualSet.add("3");

		assertEquals(String.format("Expected Key should be in the map."), expectedMap,
				CollectionsUtil.filterMap(actualMap, actualSet));

	}

	/**
	 * Tests the filterMap() method with Invalid test input. This is a negative test case
	 */

	@Test()
	public void testfilterMap_keyNotPresentInMapIsIgnored() {
		Map<String, String> actualMap = CollectionsUtil.createMap("1", "blue", "7", "red", "3", "green");

		Map<String, String> expectedMap = CollectionsUtil.createMap("1", "blue", "3", "green");

		Set<String> actualSet = new HashSet<String>();
		actualSet.add("1");
		actualSet.add("2");
		actualSet.add("3");

		assertEquals(String.format("Expected Key should be in the map."), expectedMap,
				CollectionsUtil.filterMap(actualMap, actualSet));

	}

	@Test
	public void testSortMapByValues_Ascending() {
		Map<String, Integer> inputMap = new HashMap<String, Integer>();
		inputMap.put("B", 10);
		inputMap.put("C", 10);
		inputMap.put("A", 0);
		inputMap.put("D", 15);
		inputMap.put("E", 20);
		inputMap.put("Z", -5);

		Map<String, Integer> sortedMap = CollectionsUtil.sortMapByValues(inputMap, SortOrder.ASCENDING);

		System.out.println(sortedMap.toString());

		int index = 0;
		for (Entry<String, Integer> entry : sortedMap.entrySet()) {
			switch (index++) {
			case 0:
				assertEquals("Z", entry.getKey());
				assertEquals(Integer.valueOf(-5), entry.getValue());
				break;
			case 1:
				assertEquals("A", entry.getKey());
				assertEquals(Integer.valueOf(0), entry.getValue());
				break;

			case 2:
				assertTrue(entry.getKey().equals("B") || entry.getKey().equals("C"));
				assertEquals(Integer.valueOf(10), entry.getValue());
				break;

			case 3:
				assertTrue(entry.getKey().equals("B") || entry.getKey().equals("C"));
				assertEquals(Integer.valueOf(10), entry.getValue());
				break;

			case 4:
				assertEquals("D", entry.getKey());
				assertEquals(Integer.valueOf(15), entry.getValue());
				break;

			case 5:
				assertEquals("E", entry.getKey());
				assertEquals(Integer.valueOf(20), entry.getValue());
				break;

			default:
				fail("Should not be any more values in the map");
			}
		}
	}

	@Test
	public void testSortMapByValues_Descending() {
		Map<String, Integer> inputMap = new HashMap<String, Integer>();
		inputMap.put("B", 10);
		inputMap.put("C", 10);
		inputMap.put("A", 0);
		inputMap.put("D", 15);
		inputMap.put("E", 20);
		inputMap.put("Z", -5);

		Map<String, Integer> sortedMap = CollectionsUtil.sortMapByValues(inputMap, SortOrder.DESCENDING);

		System.out.println(sortedMap.toString());

		int index = 0;
		for (Entry<String, Integer> entry : sortedMap.entrySet()) {
			switch (index++) {
			case 0:
				assertEquals("E", entry.getKey());
				assertEquals(Integer.valueOf(20), entry.getValue());
				break;
			case 1:
				assertEquals("D", entry.getKey());
				assertEquals(Integer.valueOf(15), entry.getValue());
				break;

			case 2:
				assertTrue(entry.getKey().equals("B") || entry.getKey().equals("C"));
				assertEquals(Integer.valueOf(10), entry.getValue());
				break;

			case 3:
				assertTrue(entry.getKey().equals("B") || entry.getKey().equals("C"));
				assertEquals(Integer.valueOf(10), entry.getValue());
				break;

			case 4:
				assertEquals("A", entry.getKey());
				assertEquals(Integer.valueOf(0), entry.getValue());
				break;

			case 5:
				assertEquals("Z", entry.getKey());
				assertEquals(Integer.valueOf(-5), entry.getValue());
				break;

			default:
				fail("Should not be any more values in the map");
			}
		}

	}

	@Test
	public void testSortMapByKeys_Ascending() {
		Map<String, Integer> inputMap = new HashMap<String, Integer>();
		inputMap.put("B", 10);
		inputMap.put("C", 10);
		inputMap.put("A", 0);
		inputMap.put("D", 15);
		inputMap.put("E", 20);
		inputMap.put("Z", -5);

		Map<String, Integer> sortedMap = CollectionsUtil.sortMapByKeys(inputMap, SortOrder.ASCENDING);

		System.out.println(sortedMap.toString());

		int index = 0;
		for (Entry<String, Integer> entry : sortedMap.entrySet()) {
			switch (index++) {
			case 0:
				assertEquals("A", entry.getKey());
				assertEquals(Integer.valueOf(0), entry.getValue());
				break;
			case 1:
				assertTrue(entry.getKey().equals("B"));
				assertEquals(Integer.valueOf(10), entry.getValue());
				break;

			case 2:
				assertTrue(entry.getKey().equals("C"));
				assertEquals(Integer.valueOf(10), entry.getValue());
				break;

			case 3:
				assertEquals("D", entry.getKey());
				assertEquals(Integer.valueOf(15), entry.getValue());
				break;

			case 4:
				assertEquals("E", entry.getKey());
				assertEquals(Integer.valueOf(20), entry.getValue());
				break;

			case 5:
				assertEquals("Z", entry.getKey());
				assertEquals(Integer.valueOf(-5), entry.getValue());
				break;

			default:
				fail("Should not be any more values in the map");
			}
		}
	}

	private static class MyClass {
		private final String param1;

		public MyClass(String param1) {
			this.param1 = param1;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((param1 == null) ? 0 : param1.hashCode());
			return result;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			MyClass other = (MyClass) obj;
			if (param1 == null) {
				if (other.param1 != null)
					return false;
			} else if (!param1.equals(other.param1))
				return false;
			return true;
		}

	}

	@Test
	public void testFromDelimitedString() {
		String delimitedString = "A;B;C";
		Set<MyClass> expected = CollectionsUtil.createSet(new MyClass("A"), new MyClass("B"), new MyClass("C"));
		Set<MyClass> observed = new HashSet<MyClass>(CollectionsUtil.fromDelimitedString(delimitedString, ";",
				MyClass.class));
		assertEquals(expected, observed);
	}

	@Test
	public void testFromDelimitedStringToInteger() {
		String delimitedString = "1;2;3";
		Set<Integer> expected = CollectionsUtil.createSet(1, 2, 3);
		Set<Integer> observed = new HashSet<Integer>(CollectionsUtil.fromDelimitedString(delimitedString, ";",
				Integer.class));
		assertEquals(expected, observed);
	}

	@Test
	public void testSetFromDelimitedString() {
		String delimitedString = "A;B;C";
		Set<MyClass> expected = CollectionsUtil.createSet(new MyClass("A"), new MyClass("B"), new MyClass("C"));
		Set<MyClass> observed = CollectionsUtil.setFromDelimitedString(delimitedString, ";", MyClass.class);
		assertEquals(expected, observed);
	}
}
