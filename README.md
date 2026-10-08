# Saturn (Rockets)

Saturn is a Java desktop arcade game inspired by Space Invaders. Move your gun,
shoot the alien formation, protect your remaining lives and aim for a high score.
A bonus craft offers extra points, and completing a level starts the next wave.

The game uses Java Swing and Java's built-in audio API. No third-party libraries
or downloaded sound files are required.

## Requirements

- An OpenJDK JDK, Java 8 or later, to compile the source.
- A compatible Java runtime to run the compiled game.
- A desktop environment; Windows 11 is supported by the intended setup.
- An audio output device for sound effects. The game continues silently if no
  supported audio device is available.

Download OpenJDK from the [OpenJDK download site](https://jdk.java.net/).
See the [OpenJDK project site](https://openjdk.org/) for project information.

Make sure `java` and `javac` are available on your PATH:

```bash
java -version
javac -version
```

## Get the code

Install [Git](https://git-scm.com/downloads), then clone the repository:

```bash
git clone https://github.com/CharlieSwires/saturn.git
cd saturn
```

If using the sound and rendering copy-over update, extract it into this project
folder, allowing replacement. ZIP paths start with `src/` and `bin/`.
Back up any local source changes before copying over.

## Build

Run from the project folder in Windows Command Prompt or Git Bash:

```bash
javac -encoding UTF-8 -d bin src/Rocket.java src/SoundEffects.java src/FrameRenderer.java
```

This command requires the sound and rendering update, which provides
`SoundEffects.java` and `FrameRenderer.java` alongside `Rocket.java`.

With JDK 9 or later, you can explicitly produce Java 8-compatible class files:

```bash
javac --release 8 -encoding UTF-8 -d bin src/Rocket.java src/SoundEffects.java src/FrameRenderer.java
```

In Eclipse, refresh the project after applying an update and build it. Run
`Rocket` as a Java Application with the project folder as its working directory.

## Run

Run from the project folder so the game can find its image files and scores:

```bash
java -classpath bin Rocket
```

The copy-over update includes compiled classes, so rebuilding is optional unless
you change the source.

## Controls

| Key | Action |
| --- | --- |
| A | Move left |
| D | Move right |
| Space | Fire |
| M | Toggle sound on/off; activates when the key is released |

Click the game window if it does not respond to the keyboard. When prompted for
a high-score name, enter it and click **select**.

## Sound effects

The sound update adds ten synthesized arcade effects:

- Player shot and enemy shot.
- Alien explosion and player damage.
- Shield impact and alien marching.
- Bonus craft and bonus craft hit.
- Level completion and game over.

`SoundEffects.java` generates PCM audio and plays it through
`javax.sound.sampled.SourceDataLine`. A separate daemon thread mixes overlapping
effects. Requests are bounded and do not wait for the audio device, so sound does
not block the game loop. If the audio device is unavailable, the game prints a
message and continues silently.

## Display and text

`FrameRenderer.java` combines the game graphics and smooth text into one frame
before drawing it to the window. The frame accounts for display scaling to keep
text clear on high-DPI displays and avoid separate background/text redraws that
can cause flicker. The sprites retain their retro appearance.

## Project files

| File or folder | Purpose |
| --- | --- |
| `src/Rocket.java` | Game, controls, collision handling and score display |
| `src/SoundEffects.java` | Synthesized sound effects and asynchronous audio mixer |
| `src/FrameRenderer.java` | Combined frame rendering with display scaling |
| `bin/` | Compiled Java classes |
| Root `.png` files | Alien, gun, bonus craft and shield images |
| `scores.bin` | Saved high-score data |
| `javadoc/` | Existing generated documentation; may predate the updates |

Keep the image files in the project folder. Preserve `scores.bin` if you want to
retain saved high scores. The sound and rendering copy-over ZIP does not replace
images or saved scores.

## Troubleshooting

- **`javac` is not recognised:** install an OpenJDK JDK and add its `bin` folder
  to PATH. Open a new terminal after changing PATH.
- **Main class or supporting class not found:** run from the project folder,
  check that the update was extracted into that folder, then rebuild all three
  source files using the command above.
- **Image file not found:** check the working directory and confirm that the
  original PNG files are present.
- **No sound:** press and release M, check Windows volume and the selected output
  device, and look for a sound-unavailable message in the console.
- **Old appearance after updating:** close any running instance. In Eclipse,
  refresh and clean/build the project, then relaunch it.

## Links

- [Saturn repository](https://github.com/CharlieSwires/saturn)
- [OpenJDK downloads](https://jdk.java.net/)
- [OpenJDK project](https://openjdk.org/)
- [Git downloads](https://git-scm.com/downloads)
