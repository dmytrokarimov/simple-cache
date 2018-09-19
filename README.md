# Simple Cache

Uses for storing objects with long term of life.  
Returns elements from cache or creates new ones if they are not present.  
Can be present to store common images, font, etc.  

Example of use can be found here:

in this example was created cache for images with auto creating new elements for elements that do not present in cache.

```java
		Cache<ImageIcon> imageIconCache = CacheBuilder.create(ImageIcon.class)
			.addConstructor(String.class, Integer.class, Integer.class)
				.set((params) -> {
					Image image = new ImageIcon(ICONS_PATH + params[0]).getImage();
					Image newimg = image.getScaledInstance((Integer) params[1], (Integer) params[2],  java.awt.Image.SCALE_DEFAULT);
					return new ImageIcon(newimg);
				})
			.addConstructor(String.class)
				.set((params) -> new ImageIcon(ICONS_PATH + params[0]))
			.build();
```

method _addConstructor_ register specific params set and constructor for this set  
here is using of created cache:

```java
	imageIconCache.get(filename, width, height);
	imageIconCache.get(filename);
```

