# Intro: Midjourney prompts

The intro is a living comic: panels appear page by page, each with slow motion, sound and a
caption. The images are backgrounds only; captions are added by the game (no text in images).

## How to generate

1. Run **P01** first. Pick the best of the four, upscale it, copy its image URL.
2. Add `--sref <that URL>` to every other prompt, so all panels share one look.
3. Save the chosen images as `assets/intro/raw/p01.png` ... `p13.png` (any size, 16:9).
4. If a panel comes out with letters or logos, reroll: text is added in the game.

Common tail (already included below): `--ar 16:9 --style raw --v 7`

## Style anchor

> cinematic graphic novel panel, muted palette of deep navy and black with warm amber light
> accents, soft film grain, painterly, high contrast, large dark areas, minimal detail,
> 1980s Soviet space program, lonely quiet atmosphere, no text, no letters

## Panels

| # | Caption in game | Prompt |
|---|---|---|
| P01 | 15.11.1988. BAIKONUR | Soviet Buran space shuttle mounted on the Energia rocket on the launch pad at night, Baikonur steppe, floodlights, steam drifting, tiny service tower lights, wide shot from far away, cinematic graphic novel panel, muted palette of deep navy and black with warm amber light accents, soft film grain, painterly, high contrast, large dark areas, minimal detail, 1980s Soviet space program, lonely quiet atmosphere, no text, no letters --ar 16:9 --style raw --v 7 |
| P02 | ONE FLIGHT. NO CREW. | Energia rocket with the Buran shuttle lifting off at night, enormous orange flame lighting low clouds, the steppe glowing, seen from a distance, cinematic graphic novel panel, muted palette of deep navy and black with warm amber light accents, soft film grain, painterly, high contrast, no text, no letters --ar 16:9 --style raw --v 7 |
| P03 | — | white Soviet Buran shuttle with black heat-shield underside in low Earth orbit, thin blue line of the atmosphere, sunrise edge on the horizon, deep black space, small and silent, cinematic graphic novel panel, muted navy and black, warm amber sunrise accent, soft film grain, painterly, no text, no letters --ar 16:9 --style raw --v 7 |
| P04 | THEN THE PROGRAM WAS CLOSED. | the Buran shuttle standing in a dark abandoned hangar, dust in a single beam of light from a high window, tarps, cold concrete, silence, cinematic graphic novel panel, muted navy and black with one warm amber light beam, soft film grain, painterly, high contrast, no text, no letters --ar 16:9 --style raw --v 7 |
| P05 | 1991 | a lone old modular space station in orbit above the night side of Earth, scattered city lights below slowly going dark, seen from far away, tiny against the planet, cinematic graphic novel panel, deep navy and black, faint amber city lights, soft film grain, painterly, melancholic, no text, no letters --ar 16:9 --style raw --v 7 |
| P06 | A COSMONAUT STAYED IN ORBIT. THE COUNTRY BELOW WAS GONE. | silhouette of a cosmonaut seen from behind, floating at a small round porthole inside a cramped space station, looking down at the dark Earth, face not visible, cabin lit by one dim amber lamp, cinematic graphic novel panel, deep navy and black, soft film grain, painterly, quiet and lonely, no text, no letters --ar 16:9 --style raw --v 7 |
| P07 | BUT THE PROGRAM WENT ON. IN SECRET. | the Buran shuttle on a remote snowy launch pad at night, only a few lights, small figures of engineers in heavy coats, snow falling, secret launch, cinematic graphic novel panel, deep navy and black with warm amber work lights, soft film grain, painterly, high contrast, no text, no letters --ar 16:9 --style raw --v 7 |
| P08 | FLIGHT 19. OBJECT-19. | a strange glowing anomaly in high orbit, a soft sphere of pale light and slow swirling dust, a ring-shaped research station built around it, the small Buran shuttle approaching from the distance, cinematic graphic novel panel, deep navy and black, cold pale glow with amber station lights, soft film grain, painterly, eerie and calm, no text, no letters --ar 16:9 --style raw --v 7 |
| P09 | THE EXPERIMENT FAILED. | a blinding white flash at the centre of a ring-shaped space station, the station tearing apart into pieces, debris flying outward, frozen moment, cinematic graphic novel panel, deep navy and black with harsh white and amber light, soft film grain, painterly, high contrast, no text, no letters --ar 16:9 --style raw --v 7 |
| P10 | THE CREW WAS SCATTERED. | several small cosmonauts in white Soviet spacesuits drifting apart among station debris and glowing clouds of strange dust, tiny figures in vast dark space, faces not visible, cinematic graphic novel panel, deep navy and black with amber and pale green glows, soft film grain, painterly, lonely, no text, no letters --ar 16:9 --style raw --v 7 |
| P11 | AND AT THE CENTRE, THE RIFT. | a slowly spinning rift in space like a small black hole, a disc of sand-like debris and dust spiralling into darkness, faint light bending around it, station fragments falling in, cinematic graphic novel panel, deep navy and black with warm amber rim light, soft film grain, painterly, ominous, no text, no letters --ar 16:9 --style raw --v 7 |
| P12 | BAIKONUR, THIS IS BURAN-19... BAIKONUR? | dark cockpit of a Soviet shuttle, analog instrument panel with toggle switches and round gauges, a single red warning lamp, frost on the window, an empty headset hanging, stars outside, cinematic graphic novel panel, deep navy and black with one red and one amber light, soft film grain, painterly, silent, no text, no letters --ar 16:9 --style raw --v 7 |
| P13 | BRING THEM HOME. BUT WHERE IS HOME? | the tiny white Buran shuttle alone in vast dark space, the distant Earth small and dark behind it, scattered faint glowing anomalies far away, huge negative space, cinematic graphic novel panel, deep navy and black with a faint warm amber glint on the shuttle, soft film grain, painterly, lonely and hopeful, no text, no letters --ar 16:9 --style raw --v 7 |

## Notes

- P06 refers to a real person (a cosmonaut who stayed in orbit in 1991); the panel shows only a
  silhouette from behind and the caption names no one.
- Pages: 1 = P01-P04 (1988), 2 = P05-P07 (1991), 3 = P08-P11 (Object-19), 4 = P12-P13 (silence).
