package dogapi;

import java.util.List;

public interface BreedFetcher {

    List<String> getSubBreeds(String breed) throws BreedNotFoundException;

    class BreedNotFoundException extends Exception {
        public BreedNotFoundException(String breed) {
            super("Breed not found: " + breed);
        }
    }
}
