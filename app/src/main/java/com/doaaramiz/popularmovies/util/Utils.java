package com.doaaramiz.popularmovies.util;

import java.util.Collection;

/**
 * Utils class containing helper utilities methods
 *
 * @author doaaramiz
 * @date 01.12.2015
 */
public class Utils {

	/**
	 * Null-safe check if the specified collection is empty.
	 * Null returns true.
	 * <p/>
	 * Source: org.apache.commons.collections.CollectionUtils
	 * Original Name: isEmpty
	 *
	 * @param collection the collection to check, may be null
	 *
	 * @return true if empty or null
	 *
	 * @since Commons Collections 3.2
	 */
	public static boolean collectionIsEmpty(Collection collection) {
		return collection == null || collection.isEmpty();
	}
}
