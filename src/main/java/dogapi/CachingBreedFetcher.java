package dogapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A BreedFetcher that caches successful results from another BreedFetcher.
 * Exceptions are not cached.
 */
public class CachingBreedFetcher implements BreedFetcher {
    private final BreedFetcher delegate;
    private final Map<String, List<String>> cache = new HashMap<>();

    public CachingBreedFetcher(BreedFetcher delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        String key = breed.toLowerCase();

        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        List<String> result = delegate.getSubBreeds(breed);
        cache.put(key, result);
        return result;
    }
}
