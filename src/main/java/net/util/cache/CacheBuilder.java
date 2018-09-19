package net.util.cache;

import java.util.function.Function;

/**
 * Builds new {@link Cache} 
 * 
 * @author Dmytro Karimov
 */
public class CacheBuilder <T>{
	
	private final Cache<T> cache;
	
	private CacheBuilder() {
		cache = new Cache<>();
	}

	public static <T> CacheBuilder<T> create(Class<T> forClass) {
		return new CacheBuilder<>();
	}
	
	/**
	 * Adds builder for new elements to cache, uses for new elements while calling {@link Cache#get}
	 * @param add value builder for specific set of params, should be uniq among all constructors
	 */
	public NewConstructor addConstructor(Class<?>... params) {
		NewConstructor c = new NewConstructor();
		c.params = params;
		return c;
	}

	public Cache<T> build() {
		return cache;
	}
	
	public class NewConstructor {
		Class<?>[] params;
		
		/**
		 * Pass value of all params (for new element) in simple array. 
		 * Will be called together with {@link Cache#get} in case if value is not present in cache yet
		 */
		public CacheBuilder<T> set(Function<Object[], T> constructor) {
			cache.addCreator(constructor, params);
			return CacheBuilder.this;
		}
	}
}
