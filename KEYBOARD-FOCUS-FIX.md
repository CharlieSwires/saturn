Close Saturn and extract this ZIP into the existing Saturn project folder,
allowing replacement. Paths start with src/ and bin/.
Refresh and clean/build in Eclipse if running from the IDE.

The frame-only KeyListener has been replaced by window-level key bindings.
Gameplay keys are disabled during name entry and the high-score table. Focus
is restored after accepting a name and on restart. Switching applications
resets controls to avoid stuck movement or firing.

Includes updated Rocket.java, compiled game classes and README.md.
Saved scores, images and sound/rendering source are not replaced.

Classpath build (JDK 8+):
  javac -encoding UTF-8 -d bin src/Rocket.java src/SoundEffects.java src/FrameRenderer.java
Run from the project folder:
  java -classpath bin Rocket

Compilation and automated key-action/restart checks passed, together with the
ranked-score regression checks. Actual Windows keyboard focus needs checking
on your laptop.
