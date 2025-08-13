package sorting;

import models.Song;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * [cite_start]Implements the Merge Sort algorithm for sorting a list of Songs. [cite: 56]
 * [cite_start]Allows sorting based on song title (alphabetical), duration (ascending/descending). [cite: 51, 52]
 * Time and Space complexities are annotated.
 */
public class MergeSort implements Sorter {

    /**
     * [cite_start]Sorts a list of songs using the Merge Sort algorithm. [cite: 56]
     * Time Complexity: O(N log N)
     * Space Complexity: O(N) due to temporary lists created during merge.
     * @param songs The list of songs to sort.
     * @param criteria The sorting criteria: "title", "duration_asc", "duration_desc".
     */
    @Override
    public void sort(List<Song> songs, String criteria) {
        if (songs == null || songs.size() <= 1) {
            return;
        }
        mergeSort(songs, 0, songs.size() - 1, criteria);
    }

    private void mergeSort(List<Song> songs, int left, int right, String criteria) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(songs, left, mid, criteria);
            mergeSort(songs, mid + 1, right, criteria);
            merge(songs, left, mid, right, criteria);
        }
    }

    private void merge(List<Song> songs, int left, int mid, int right, String criteria) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        List<Song> L = new ArrayList<>(n1);
        List<Song> R = new ArrayList<>(n2);

        for (int i = 0; i < n1; ++i) {
            L.add(songs.get(left + i));
        }
        for (int j = 0; j < n2; ++j) {
            R.add(songs.get(mid + 1 + j));
        }

        int i = 0, j = 0;
        int k = left;

        Comparator<Song> comparator = getComparator(criteria);

        while (i < n1 && j < n2) {
            if (comparator.compare(L.get(i), R.get(j)) <= 0) {
                songs.set(k, L.get(i));
                i++;
            } else {
                songs.set(k, R.get(j));
                j++;
            }
            k++;
        }

        while (i < n1) {
            songs.set(k, L.get(i));
            i++;
            k++;
        }

        while (j < n2) {
            songs.set(k, R.get(j));
            j++;
            k++;
        }
    }

    /**
     * Returns a Comparator based on the given criteria.
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     * @param criteria The sorting criterion.
     * @return A Comparator for Song objects.
     */
    private Comparator<Song> getComparator(String criteria) {
        switch (criteria) {
            case "title":
                return Comparator.comparing(Song::getTitle, String.CASE_INSENSITIVE_ORDER);
            case "duration_asc":
                return Comparator.comparingLong(Song::getDurationMs);
            case "duration_desc":
                return Comparator.comparingLong(Song::getDurationMs).reversed();
            // Add more criteria if needed, e.g., "recently_added" would require an "addedTimestamp" in Song.java
            default:
                throw new IllegalArgumentException("Unsupported sorting criteria: " + criteria);
        }
    }
}