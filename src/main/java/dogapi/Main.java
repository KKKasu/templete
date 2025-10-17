package dogapi;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        BreedFetcher localFetcher = new BreedFetcherForLocalTesting();
        BreedFetcher cachingFetcher = new CachingBreedFetcher(localFetcher);

        String breed = "hound";
        int result = getNumberOfSubBreeds(breed, cachingFetcher);
        System.out.println(breed + " has " + result + " sub breeds");

        breed = "cat";
        result = getNumberOfSubBreeds(breed, cachingFetcher);
        System.out.println(breed + " has " + result + " sub breeds");

        if (localFetcher instanceof BreedFetcherForLocalTesting) {
            int count = ((BreedFetcherForLocalTesting) localFetcher).getCallCount();
            System.out.println("Underlying fetcher was called " + count + " time(s).");
        }
    }

    public static int getNumberOfSubBreeds(String breed, BreedFetcher breedFetcher) {
        try {
            List<String> subBreeds = breedFetcher.getSubBreeds(breed);
            return subBreeds.size();
        } catch (BreedFetcher.BreedNotFoundException e) {
            return 0;
        }
    }
}
