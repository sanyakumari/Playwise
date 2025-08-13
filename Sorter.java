package sorting;

import models.Song;
import java.util.List;

/**
 * Interface for sorting algorithms.
 */
public interface Sorter {
    /**
     * [cite_start]Sorts a list of songs based on a specified criterion. [cite: 50, 58]
     *
     * @param songs The list of songs to sort.
     * [cite_start]@param criteria The sorting criterion (e.g., "title", "duration_asc", "duration_desc"). [cite: 51, 52, 53]
     * Time Complexity and Space Complexity should be specified in implementing classes.
     */
    void sort(List<Song> songs, String criteria);
}