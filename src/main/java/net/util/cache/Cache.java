package net.util.cache;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Uses for storing objects with long term of life.
 * Returns elements from cache or creates new ones if they are not present
 * 
 * @author Dmytro Karimov
 */
public class Cache <T> {
	
	private final Map<Integer, T> resourceCache = new HashMap<>();
	
	private final Map<Integer, Function<Object[], T>> creators = new HashMap<>();
	
	protected Cache() {
	}
	
	protected void addCreator(Function<Object[], T> creator, Class<?>... params) {
		Function<Object[], T> previuosCreator = creators.put(Arrays.hashCode(params), creator);
		if (previuosCreator != null) {
			throw new IllegalStateException("Params should be uniq!");
		}
	}
	
	private Class<?>[] getKeyFromParams(Object... params) {
		return Arrays.asList(params).stream()
				.map(Object::getClass)
				.collect(Collectors.toList())
				.toArray(new Class<?>[params.length]);
	}
	
	protected T newInstant(Object... params) {
		int hash = Arrays.hashCode(getKeyFromParams(params));
		
		return Optional
				.of(creators.get(hash))
				.orElseThrow(() -> new IllegalStateException("Params " + Arrays.toString(params) + " is not registred to any constructor"))
				.apply(params);
	}
	
	/**
	 * @return value from cache or creating new one
	 */
	public T get(Object... params) {
		Integer key = Arrays.hashCode(params);
		if (!resourceCache.containsKey(key)) {
			synchronized (key) {
				if (!resourceCache.containsKey(key)) {
					resourceCache.put(key, newInstant(params));
				}
			}
		}
		
		return resourceCache.get(key);
	}
}
