Close Saturn and extract this ZIP into your existing Saturn project folder,
allowing replacement. Paths start with src/ and bin/. Restart the game.

Includes sound effects and M mute control from the supplied Saturn.zip.
Text and game graphics now form a single completed frame before it is shown.
The frame matches display scaling, preserving smooth text at high DPI.
Repaint updates also avoid a separate background clear.

Rebuild with Java 8 or later:
    javac -encoding UTF-8 -d bin src/Rocket.java src/SoundEffects.java src/FrameRenderer.java
Run from the project folder:
    java -classpath bin Rocket

Verified: Java 8-compatible compilation and 120 offscreen frames at 100%,
125%, 150%, 200% scaling, matching native-resolution text with no stale pixels.
Actual Windows display flicker needs checking on your laptop.
Images, saved scores and Eclipse configuration are not overwritten.
