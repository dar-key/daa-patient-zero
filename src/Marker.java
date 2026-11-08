import java.util.*;

/**
 * Missions 3 and 4. Find a word in a long text.
 */
public class Marker {

    /**
     * Mission 3a. The prefix function of p. pi[i] = the length of the longest
     * proper prefix of p[0..i] that is
     * also a suffix of p[0..i] ("proper" means shorter than p[0..i] itself).
     * Example: p = "ACGAC" gives [0, 0, 0, 1, 2]. The last number is the "Border
     * length" line.
     */
    public static int[] prefixFunction(String p) {
        int m = p.length();
        int[] pi = new int[m];
        if (m == 0)
            return pi;

        int k = 0; // length of the previous longest border
        for (int i = 1; i < m; i++) {
            while (k > 0 && p.charAt(i) != p.charAt(k)) {
                k = pi[k - 1]; // mismatch: fall back to previous border
            }
            if (p.charAt(i) == p.charAt(k)) {
                k++;
            }
            pi[i] = k;
        }

        return pi;
    }

    /**
     * Mission 3b. KMP. All start positions (counting from 0, in increasing order)
     * where p occurs in text.
     * Overlapping occurrences count: "ACG" occurs in "ACGACG" at 0 and 3, and "AA"
     * in "AAA" at 0 and 1.
     * Use the prefix function: the text pointer never goes back. The number of
     * positions is "Marker hits",
     * the first one is "First hit" (-1 when there is none).
     */
    public static int[] findAll(String text, String p) {
        int n = text.length();
        int m = p.length();
        if (m == 0 || n < m)
            return new int[0];

        int[] pi = prefixFunction(p);
        List<Integer> hits = new ArrayList<>();

        int k = 0; // matched characters in pattern
        for (int i = 0; i < n; i++) {
            while (k > 0 && text.charAt(i) != p.charAt(k)) {
                k = pi[k - 1]; // text pointer i stays, pattern shifts
            }
            if (text.charAt(i) == p.charAt(k)) {
                k++;
            }
            if (k == m) {
                hits.add(i - m + 1); // occurrence start index
                k = pi[m - 1]; // fall back to allow overlapping hits
            }
        }

        int[] res = new int[hits.size()];
        for (int i = 0; i < hits.size(); i++) {
            res[i] = hits.get(i);
        }
        return res;
    }

    /**
     * Mission 4. Rabin-Karp.
     */
    public static int sharedWords(String a, String b, int k) {
        // TODO Mission 4
        return -1;
    }
}
