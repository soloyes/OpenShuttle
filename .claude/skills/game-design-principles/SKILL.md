---
name: game-design-principles
description: Design foundation of Buran-19, built on Vlad Markhulets' "100 советов и фишек геймдизайна" (DARQ). Use before designing or changing gameplay, sectors, enemies, HUD, menus, codex, difficulty, pacing, rewards, audio, story or onboarding, and whenever the owner says something "feels wrong".
---

# Game design principles for Buran-19

The owner chose one reference as the foundation of the game: **Vlad Markhulets (author of DARQ),
"100 советов и фишек геймдизайна"**, VK / Game Dynasty, 2021.
Full paraphrase with examples and per-tip application to Buran-19:
`references/100-tips.md`. Read the relevant section there before a design change. Do not attribute
to the article anything that is not in that file.

## Design pillars (derived from the tips)

1. **One emotion: loneliness and hope** (tips 52, 61). Quiet, dark palette, restrained text, radio silence.
   Every screen, sound and mechanic serves it.
2. **One core mechanic: inertial flight + tractor beam** (5, 98). Sectors reveal it in new ways instead of
   adding unrelated mechanics. One novelty on a familiar base (74).
3. **Clear goal, free method** (1, 2). Each sector states what to do, never how.
4. **Teach by doing, one thing at a time** (4, 28). Show → check → apply creatively, one new mechanic per sector.
5. **Fair, readable danger** (32, 33, 39, 53, 69, 70, 71). Enemies telegraph intent; size and colour show threat;
   randomness never feels rigged; first contact is forgiving.
6. **Rhythm with breathers** (6, 7, 15, 37, 38). Calm search → rising threat → climax → quiet radio intercept.
   Surprises and contrast scenes keep it fresh.
7. **Failure is partial and quick** (50, 54, 83, 87). Shield → hull → life → sector restart; short, varied defeats.
8. **Instant feedback, celebrated wins** (25, 27, 82, 96). Every action answers with sound, light or motion.
9. **Minimal, contextual UI** (16, 18, 20, 23, 75, 90, 91, 95). Consistent colour language, simple controls,
   HUD only when relevant, accessibility options.
10. **Story through the world** (12, 29, 36, 60). Debris, telemetry, radio and crew lines rather than text walls.
11. **Curiosity is rewarded** (57, 58, 68, 88, 89). Codex, hidden signals, sector map with dark cells, end rewards.
12. **Handmade-looking variety** (85, 83b, 99). Sectors assembled from designed blocks, not raw random noise.

## How to use

1. State the problem in one sentence.
2. Find the matching section in `references/100-tips.md`; list the tips the current design breaks and the
   **B19** notes there (statuses [есть] / [план] / [нужно] / [н/п]).
3. Propose the smallest change that satisfies the pillars; name the tips it follows in the commit/PR.
4. Verify by playing: headless browser screenshots, then real devices (Xbox Edge, phone portrait).
5. If a tester (the owner) is confused, the game is at fault, not the tester.
6. When a [нужно] item is done, flip it to [есть] in the reference file.
