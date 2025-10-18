package dogapi;

import java.util.List;

/**
 * A minimal implementation of the BreedFetcher interface for local testing.
 * This mock avoids real API calls by returning fixed data for specific breeds.
 */
public class BreedFetcherForLocalTesting implements BreedFetcher {

    private int callCount = 0;

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        callCount++;

        if ("hound".equalsIgnoreCase(breed)) {
            return List.of("afghan", "basset");
        }

        throw new BreedNotFoundException(breed);
    }

    public int getCallCount() {
        return callCount;
    }
}
