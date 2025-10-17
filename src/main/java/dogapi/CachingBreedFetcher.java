package dogapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A BreedFetcher that caches results from another BreedFetcher to avoid redundant API calls.
 * It does not cache failed requests (invalid breeds).
 *
 * @author YourName
 * @version 1.0
 */
public class CachingBreedFetcher implements BreedFetcher {
    private final BreedFetcher delegate;
    private final Map<String, List<String>> cache = new HashMap<>();

    /** Number of actual delegate calls made (for testing verification). */
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
        List<String> result = delegate.getSubBreeds(breed);
        cache.put(key, result);
        return result;
    }

    public int getCallsMade() {
        return callsMade;
    }
}
