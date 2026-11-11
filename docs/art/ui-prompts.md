# Art prompts: menu, sectors, crew, codex

Same look as the intro. In Nano Banana attach **assets/intro/p08.jpg** (Object-19) to every
prompt and add: *"in the same style as the attached image"*. No text in images; the game adds it.
Save results into `art/ui/raw/` with the names below (any format).

Style anchor (already in every prompt):
> cinematic graphic novel panel, muted palette of deep navy and black with warm amber light
> accents, soft film grain, painterly, high contrast, large dark areas, minimal detail,
> 1980s Soviet space program, lonely quiet atmosphere, no text, no letters, no frame

## 1. Main menu backdrop (16:9) — `menu.png`

The menu list sits on the left third, so that part must be calm and dark.

> wide view of deep space, on the right side the white Soviet Buran shuttle drifting slowly
> toward a distant pale glowing anomaly, a broken ring-shaped station far away, faint dust,
> the left third of the image is empty dark space with a few stars, cinematic graphic novel
> panel, muted palette of deep navy and black with warm amber light accents, soft film grain,
> painterly, high contrast, large dark areas, minimal detail, 1980s Soviet space program,
> lonely quiet atmosphere, no text, no letters, no frame

## 2. Sector cards (16:9) — shown on the flight plan and the briefing

| File | Prompt (add the style anchor at the end) |
|---|---|
| `sector01.png` | two tiny cosmonauts in white Soviet spacesuits drifting far apart in empty dark space, one small warm beacon light blinking on each suit, total silence, vast emptiness |
| `sector02.png` | a slow field of dark rocky asteroids and metal debris lit from one side by a distant amber sun, a thin red laser line burning one rock, dust glowing |
| `sector03.png` | crackling pale-blue electric discharges drifting like jellyfish in dark space, a cosmonaut far away, static and haze, the light is cold and nervous |
| `sector04.png` | the rift: a slowly spinning black hole with a disc of sand-like debris, small round shelter domes glowing amber at a safe distance, the tiny shuttle at the edge of the pull |

## 3. Crew portraits (1:1) — Codex, rescue moment, results

Helmet and visor, face mostly hidden or in shadow, shoulder patch with a simple symbol (no letters).

| File | Prompt (add the style anchor at the end) |
|---|---|
| `crew_commander.png` | portrait of a Soviet cosmonaut commander in a white spacesuit and helmet, calm and tired, visor reflecting a distant Earth, a single amber light on the side |
| `crew_medic.png` | portrait of a Soviet cosmonaut medic in a white spacesuit, a red cross shape on the shoulder patch, visor half open, gentle face in shadow |
| `crew_scientist.png` | portrait of a Soviet cosmonaut scientist in a white spacesuit, a small instrument in hand, visor reflecting a pale glowing anomaly |
| `crew_mechanic.png` | portrait of a Soviet cosmonaut mechanic in a scuffed white spacesuit, a wrench clipped to the suit, frost on the visor |
| `crew_military.png` | portrait of a Soviet military cosmonaut in a darker spacesuit, stern posture, visor down, a red star shape on the patch |

## 4. Codex plates (1:1) — one object alone on black

The words "scientific illustration" make Nano Banana draw a labelled poster with insets and
captions (first tries are in `art/ui/raw/rejected/`). Ask for a single painted object instead,
and repeat the ban on text at the end.

| File | Prompt (add the style anchor at the end) |
|---|---|
| `codex_corrosive.png` | a drifting cloud of brownish acid dust in dark space, eating into a small metal panel, one single object centred on plain black, no labels, no diagrams, no insets, no annotations, no captions |
| `codex_electric.png` | a pale blue ball of crackling electric discharge in dark space, thin arcs, one single object centred on plain black, no labels, no diagrams, no insets, no annotations, no captions |
| `codex_radiation.png` | a sickly green glowing cloud with a faint shimmer, radiation in dark space, one single object centred on plain black, no labels, no diagrams, no insets, no annotations, no captions |
| `codex_magnetic.png` | a violet swirl of magnetic field lines bending small metal debris around it in dark space, one single object centred on plain black, no labels, no diagrams, no insets, no annotations, no captions |
| `codex_rift.png` | a small black hole with an accretion disc of sand and debris, light bending around it, one single object centred on plain black, no labels, no diagrams, no insets, no annotations, no captions |

## 5. Endings / clear (16:9)

| File | Prompt (add the style anchor at the end) |
|---|---|
| `clear.png` | inside a cramped dark shuttle cabin, two cosmonauts in white suits resting, one with a hand on the other's shoulder, a small amber lamp, Earth through a small window |
| `lost.png` | an empty white spacesuit helmet drifting slowly away into dark space, tiny reflection of the shuttle in the visor |
