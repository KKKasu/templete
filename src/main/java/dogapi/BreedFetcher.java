package dogapi;

import java.util.List;

/**
 * Interface for fetching sub-breeds of a given dog breed.
 */
public interface BreedFetcher {

    List<String> getSubBreeds(String breed) throws BreedNotFoundException;

    class BreedNotFoundException extends Exception { // ✅ checked exception
        public BreedNotFoundException(String breed) {
            super("Breed not found: " + breed);
        }
    }
}
