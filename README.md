<p align="center">
  <img src="docs/banner-pixel-v3.gif" alt="Buran-19" width="100%"/>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/libGDX-1.9.10-E74A45?style=flat-square" alt="libGDX"/>
  <img src="https://img.shields.io/badge/Java-8-ED8B00?style=flat-square&logo=openjdk&logoColor=white" alt="Java 8"/>
  <img src="https://img.shields.io/badge/platforms-Android%20%C2%B7%20iOS%20%C2%B7%20Desktop-6a5cff?style=flat-square" alt="Platforms"/>
  <img src="https://img.shields.io/badge/commits-595-39e6c8?style=flat-square" alt="Commits"/>
</p>

**Buran-19** is an indie 2D space arcade. You fly the Buran shuttle through open space, rescue stranded astronauts
and try to stay alive as long as the fuel lasts.

![Gameplay](tools/google/feature.jpg)

## Gameplay

- **Rescue astronauts.** Fly close and the tractor beam pulls them aboard. Medics, mechanics, scientists and military crew each bring their own bonus.
- **Collect supplies.** Crates, fuel boxes and storages refill fuel, shields and weapons.
- **Survive.** Metal and mineral asteroids, homing rockets, black holes and four kinds of anomalies (corrosive, electrical, magnetic, radiation) are all out to get you.
- **Fight back.** Lasers, rockets, shields, freeze, scan and radar, with killing sprees for chain kills.
- **Profiles and results.** Several player profiles, difficulty levels, detailed score history.

## Screenshots

<p align="center">
  <img src="tools/google/0.jpg" width="24%"/>
  <img src="tools/google/1.jpg" width="24%"/>
  <img src="tools/google/2.jpg" width="24%"/>
  <img src="tools/google/3.jpg" width="24%"/>
</p>
<p align="center">
  <img src="tools/google/4.jpg" width="24%"/>
  <img src="tools/google/5.jpg" width="24%"/>
  <img src="tools/google/6.jpg" width="24%"/>
  <img src="tools/google/7.jpg" width="24%"/>
</p>

## Build and run

The project is a standard libGDX Gradle setup from 2018–2021: Gradle 4.6 wrapper and Android Gradle plugin 3.1, so use **JDK 8**.

```bash
git clone https://github.com/soloyes/buran-19.git
cd buran-19
./gradlew desktop:run           # desktop version
./gradlew android:installDebug  # Android device or emulator (needs Android SDK)
./gradlew ios:launchIPhoneSimulator  # iOS via RoboVM (macOS + Xcode)
```

## Project layout

| Module | Contents |
|---|---|
| `core` | All game logic: shuttle, bots (asteroids, rockets, anomalies, black hole, consumables), effects, UI, screens |
| `android` | Android launcher; shared assets live in `android/assets` (sprites, sounds, music, shaders) |
| `desktop` | LWJGL desktop launcher |
| `ios` | RoboVM iOS launcher |
| `tools` | Texture packer and skin composer projects, source images, store graphics |

## Authors

Made by [Vitaly Solovev](https://github.com/soloyes), with contributions from FlameXander, Pavel Guryanov and htfdream.

History moved from [Bitbucket](https://bitbucket.org/soloyes/buran-19) with all 595 commits and branches.
