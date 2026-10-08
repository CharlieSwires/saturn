Saturn J2SE sound update
=======================
Based on repository commit da9c331ee4efbf55822d605e10832084888b9a51.

Extract this ZIP into your existing saturn folder, allowing replacement.
The ZIP paths start with src/ and bin/; do not extract into src itself.
Back up any local changes to Rocket.java before copying over.

Run from the saturn folder as before:

    java -classpath bin Rocket

Controls: A/D move, Space fires, M toggles sound (on key release).

Effects: player and enemy shots, alien explosion, player damage, shield
impact, alien march, bonus craft, bonus hit, level completion and game over.
All effects are generated in Java with javax.sound.sampled; no extra library,
WAV files, internet connection or audio downloads are needed. Effects overlap
through a single PCM mixer on a daemon audio thread. Excess sound requests
are dropped rather than delaying gameplay. An unavailable audio device causes
a single console message and the game continues silently.

To rebuild with a JDK (Java 8 or later):

    javac -encoding UTF-8 -d bin src/Rocket.java src/SoundEffects.java

With a modern JDK, add --release 8 to produce Java 8-compatible class files.
Existing images and scores.bin are not included or overwritten.
Two small collision guards also prevent firing from an empty alien list on
level completion and using a discarded bullet when it hits at the top edge.

Text clarity fix: HUD, game-over and high-score text now uses native window
Graphics2D with font smoothing, avoiding enlargement of bitmap text on HiDPI
Windows displays. The retro game sprites are unchanged.
