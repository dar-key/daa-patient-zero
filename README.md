# Assignment 4. Patient Zero

Student ID: 250922
Name: Sergey
Group: SE-2540

This file is the report. Fill it in as you go. How to start: `START_HERE.md`. The task: `docs/Assignment4.pdf`.

## Program output

The 9 lines your program prints on your data (`java -cp out Main`):

    Mutations: 83
    Shared core: 871
    Marker hits: 7
    First hit: 5856
    Border length: 4
    Shared words: 282
    Tests needed: 29
    Cheapest testing: 129
    Longest chain: 26

My outbreak report (a screenshot of `output/outbreak.html`):

![outbreak](output/outbreak.png)

## Questions

Answer in your own words, two or three sentences each.

Mission 1. What does the cell d[i][j] of your table mean, and how is it computed from other cells?

The cell `d[i][j]` represents the minimum number of single-letter edits (insertions, deletions, or substitutions) needed to turn the prefix `a[0..i-1]` into `b[0..j-1]`. It is computed by taking the minimum of three possibilities: deleting a character from `a` (`d[i-1][j] + 1`), inserting a character into `a` (`d[i][j-1] + 1`), or matching/replacing the character (`d[i-1][j-1] + cost`, where cost is 0 if `a[i-1] == b[j-1]` and 1 otherwise).

Mission 2. How is the longest common subsequence different from the longest common substring? My Shared core is bigger than the number of letters that are the same at the same place: why?

A substring must be a contiguous piece of text with no gaps, whereas a subsequence maintains relative character order but allows gaps in between. The Shared core is larger than direct positional matches because insertions and deletions shift the relative alignment of the sequences, allowing matching characters at different indices to pair up.

Mission 3. What does the prefix function tell you, and why does the pointer in the text never go back in KMP? What is the Border length of my marker and what does it mean?

The prefix function `pi[i]` stores the length of the longest proper prefix of `p[0..i]` that is also a suffix of `p[0..i]`. In KMP, the text pointer never goes back because the prefix function informs us exactly what portion of the already-matched prefix can be reused for the next possible match, shifting only the pattern index instead. The Border length is the final value `pi[m-1]`, which indicates the length of the longest proper prefix of the marker that is simultaneously its suffix.

Mission 4. Why must you compare the letters after the hashes are equal? What happens to the cost of Rabin-Karp if many windows have the same hash?

Because the hash function maps large letter windows into a finite range using modular arithmetic, different words can produce the identical hash (a hash collision), meaning letter comparison is required to confirm an exact match. If many windows share the same hash, the algorithm wastes time verifying false alarms, which degrades the running time toward the naive O(n \* k) worst case.

Mission 5. What are the two states of a person in your DP, and how do you get the answer for a person from its children? Why does a greedy "test the person with most links first" not always give the cheapest answer?

The two states for each person are the cheapest testing cost for their subtree when the person is tested (`take`) and when the person is not tested (`skip`). If a person is skipped, all their children must be tested to cover the connecting links; if a person is tested, each child independently chooses the cheaper option between being tested and skipped. A greedy choice fails because a person with many links might have an exorbitant test cost compared to testing all their cheap neighbors.

## AI use

AI tools (ChatGPT) were used as a learning assistant to clarify the tree dynamic programming state transitions, check the rolling hash modulo formulas, and structure the Git commit workflow. All code was written and verified independently.

## Timing (Mission 7)

Milliseconds, average of 3 runs, from `java -cp out Bench`.

| test          | size                     | time                                 |
| ------------- | ------------------------ | ------------------------------------ |
| naive search  | 20 000 / 40 000 / 80 000 | 18.2 ms / 36.4 ms / 73.1 ms          |
| KMP           | 20 000 / 40 000 / 80 000 | 0.4 ms / 0.7 ms / 1.3 ms             |
| edit distance | 500 / 1000 / 2000 / 4000 | 2.1 ms / 7.9 ms / 31.4 ms / 128.5 ms |

What do the numbers say about how the time grows? When you double the size, by how much does each time grow? Two or three sentences.

For KMP, doubling the text size doubles the running time (~2x growth), confirming its linear O(n) complexity. In contrast, for edit distance, doubling both string lengths quadruples the execution time (~4x growth). This matches the quadratic O(n \* m) complexity of filling the two-dimensional dynamic programming table.

## Complexity

n is the length of the text (or of a strain), m the length of the pattern, k the length of the window, v the number of people. Fill in every algorithm you wrote.

| algorithm                 | time                            | space     | assumptions                                                               |
| ------------------------- | ------------------------------- | --------- | ------------------------------------------------------------------------- |
| Edit distance             | O(n \* m)                       | O(n \* m) | Standard dynamic programming matrix for strings of lengths n and m        |
| LCS                       | O(n \* m)                       | O(n \* m) | Table of size (n+1) x (m+1) storing subproblem lengths                    |
| Prefix function and KMP   | O(n + m)                        | O(m)      | Preprocessing pattern takes O(m), text scan takes O(n)                    |
| Rabin-Karp (shared words) | O(n + m) avg, O(n\*k + m) worst | O(m)      | Rolling hash updates in O(1); worst case occurs with many hash collisions |
| Tree DP                   | O(v)                            | O(v)      | Tree with v vertices and v-1 edges; each node visited in BFS order        |

## Before you submit

- The four files in `data` and the screenshot `output/outbreak.png` are in the repository.
- `java tools/Check.java` shows all examples right.
- This README is filled in, the program output and the section AI use included.
- The work is pushed to main (at least one commit per mission) and the repository is public.
- The link is attached in Moodle.
