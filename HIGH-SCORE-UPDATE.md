Close Saturn, then extract this ZIP into the existing Saturn project folder,
allowing replacement. ZIP paths begin with src/ and bin/.
Includes updated source, Java 8-compatible compiled classes and README.md.
Your scores.bin, game images and project configuration are not replaced.

Run from the project folder:
    java -classpath bin Rocket

In Eclipse, refresh the project and clean/build before relaunching.

Scores now qualify for the top ten rather than requiring a new overall record.
Entries are inserted in ranked order; only the best ten are retained.
Ties appear below existing equal scores. At a full table's cutoff, an equal
score does not replace an older entry. Select confirms the name immediately;
the existing timeout accepts it automatically. A game is saved only once.
The existing saved-score format and XOR sequence are unchanged.

Verified: compilation targeting Java 8, middle and top insertion, eviction,
ties, cutoff, empty table, reading supplied scores, saving and reloading,
and preventing duplicate submissions. Interactive Windows gameplay was not
available in the test environment.
