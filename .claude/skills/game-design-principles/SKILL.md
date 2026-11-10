---
name: game-design-principles
description: Game-design review checklist for Buran-19, based on "100 Principles of Game Design" (Despain et al., 2013). Use before designing or changing gameplay, UI/HUD, menus, loading screens, difficulty, pacing, rewards, music or onboarding, and when the owner says something "feels wrong" without saying why.
---

# Game design principles for Buran-19

Reference: Wendy Despain (ed.), *100 Principles of Game Design*, New Riders 2013.
The owner chose it as the design reference. Notes below are our own paraphrase, tied to this game.
The book is a toolbox, not a rulebook: pick the principles that fit the problem.

## Sources (read these, don't improvise)
- `references/chapter4-troubleshooting.md` — all 21 principles of chapter 4 (pp. 168–209), from the full text the owner supplied, each mapped to Buran-19.
- `references/chapters1-3-index-only.md` — chapters 1–3: **only titles and index terms are available** (the supplied PDF is a 71-page excerpt). Never invent their content; say the text is missing and ask the owner for those chapters if a decision depends on them.
- Notes in the sections below that cite chapter 1–3 principles (Flow, Interest Curve, Hick's law, Doubling and Halving, Variable Rewards, Punishment, etc.) are general design knowledge, not quotes from the book.

## How to use
1. Name the problem in one sentence (Define the Problem).
2. Walk the relevant checklist below and note which principles it violates.
3. Propose the smallest change that fixes it; verify by playing (screenshots + a playtest run).
4. Don't blame the player for confusion (Fundamental Attribution Error): if a tester missed it, the game didn't show it.

## UI, HUD, menus
- **Krug: don't make me think.** Every screen has one obvious next action. Same function = same look and place everywhere. Cut text in half, then half again.
- **Affordance cues.** Things that can be used must look usable; decoration must not look like a button. Gamepad glyphs (A/B/Y) next to actions.
- **Fitts' law.** Frequently used targets big and close; on TV the D-pad focus path must be short and predictable.
- **Hick's law.** Few choices per screen; group the rest behind one entry.
- **Working memory (~4 chunks).** Teach controls one at a time during play, not as a wall of text up front.
- **Golden ratio** for panel proportions when in doubt.
- **Advance organizers.** Loading/boot screens set tone and promise what comes next, in the game's own art style; never a long load that delivers nothing new.

## Pacing and difficulty
- **Pacing / Jo-Ha-Kyu.** Calm start, rising break, rapid climax, then a breather — inside a run, inside a sector, even inside one attack (telegraph, build, release).
- **Interest curve / Ten minutes of sustained attention.** Something new or a peak at least every few minutes; minute 10 must differ from minute 2.
- **Flow / Time dilation.** If time "drags" in a playtest, the challenge/skill balance is off.
- **Buster principle.** Quietly ease off after repeated failures; never stall the player at one wall.
- **Punishment / Errors without punishment / Loss aversion.** Losing hurts more than winning pleases; make penalties readable and proportional, avoid unseen instant deaths.
- **Dominant strategy.** Check that one tactic (e.g. turbo ramming, hiding in a corner) doesn't trivialize the run; counter it with balance, not bans.
- **Doubling and halving** when tuning numbers: move by x2 / x0.5 until it feels close, then fine-tune.

## Rewards and feel
- **Sense of accomplishment.** Reward effort, not clicks; give fiero moments (boss, rescue, survival).
- **Variable rewards / Skinner box** — use sparingly and fairly (crates, damaged boxes), never as a slot machine.
- **Feedback loops.** Watch runaway positive loops (rich get richer) and add catch-up where needed.
- **Music and dopamine.** Motifs that return with variation, build-up before peaks, short themes tied to events (rescue, step up).
- **Instant vs. delayed gratification.** Offer both: immediate juice on every action, long goals across sectors.

## World, story, perception
- **Hype and cognitive biases.** Framing changes how mechanics feel: the "rift", "crew", "signal lost" framing makes the same mechanics feel meaningful. Promise only what the game delivers.
- **Environmental storytelling.** Tell the 1991 story through objects, debris, radio and telemetry, not long texts.
- **Spatial awareness.** Prospect/refuge, readable edges, lighting draws the eye to what matters.
- **Theme.** Every feature serves "the last Soviet crew": quiet, lonely, hopeful.

## Process
- Playtest after every change (headless browser harness in scratchpad, then real devices: Xbox Edge, phone portrait).
- Iterate small; prototype risky ideas before polishing.
