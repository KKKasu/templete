package dogapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A caching wrapper for any BreedFetcher.
 * - Caches successful results (breed -> list of sub-breeds).
 * - Does NOT cache failed lookups (BreedNotFoundException).
 * - Always converts breed to lowercase for cache keys.
 * - Records how many calls have been made to the delegate fetcher.
 */
public class CachingBreedFetcher implements BreedFetcher {
    private final BreedFetcher delegate;
    private final Map<String, List<String>> cache = new HashMap<>();
    private int callsMade = 0;

    public CachingBreedFetcher(BreedFetcher delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        String key = breed.toLowerCase();

        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        callsMade++;

        try {
            List<String> result = delegate.getSubBreeds(breed);
            cache.put(key, result);
            return result;
        } catch (BreedNotFoundException e) {
            throw e;
        }
    }

    }
}
