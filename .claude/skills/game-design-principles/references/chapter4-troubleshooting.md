# Chapter 4 — Universal Principles for Troubleshooting (pp. 168–209)

Source: *100 Principles of Game Design*, W. Despain (ed.), New Riders 2013. The owner supplied this
chapter in full. Notes are paraphrased; page numbers point to the book. Each entry ends with
**Buran-19** — how it applies here.

---

## Advance Organizers (p. 168)
An education technique: give a high-level hook before new material so it lands in context. In games
it takes these forms, in order of encounter:
- **Hype** (ads, trailers, press) — sets expectations before play.
- **Loading screen** — shows a character or art from the game, has a progress indicator, and *must
  match the game's art style*; it sets the tone.
- **Cutscene** — backstory and context, also used mid-game to signal a transition.
- **Menu** — a clear map-like menu prepares the player for the destination better than a bare list.
- Caveat: don't use an organizer where nothing new follows; long loads that deliver nothing frustrate.

**Buran-19:** boot screen, loading screen and menu share one style (done). Use a short "radio
intercept" or sector card before each sector as the organizer for what is new in it. A sector select
should look like a map, not a list. Keep loads short.

## Affordance Cues (p. 170)
Physical/visual qualities that tell how to interact (Donald Norman, *The Design of Everyday Things*).
Designers must check both intended and accidental cues. Applies to HUD/UI and to environments:
things to push must look pushable, a path to follow should be visibly a path. Good affordance cuts
tutorial time.

**Buran-19:** pickups (crates, crew, fuel) must read as "take me", threats as "avoid me" by shape,
colour and motion. Show A/B/Y glyphs on actions. The sector border reads as danger (red dashed glow).

## The Buster Principle (p. 172)
Be kind to players: when a player clearly fails a task repeatedly (trackable), make it slightly easier,
ideally imperceptibly. Named after a cockatoo that eased off when its owner got frustrated. Not "make
everything easy" — avoid leaving players frustrated and angry; they should leave with accomplishment.

**Buran-19:** track deaths per sector/cause. After repeated losses, quietly lower anomaly caps/speed,
lengthen the anomaly timer, raise fuel drops. Never announce it.

## Cognitive Biases (p. 174)
Players' filters turn "what happened" into "what I felt happened":
- **Confirmation bias** — notice what confirms beliefs.
- **Availability heuristic** — weight what comes to mind first; emotional events are more available.
- **Negativity bias** — bad events are remembered more (e.g. players believe random draws are rigged).
- **Recency bias** — recent events overweighted; random runs look like trends.
- **Anchoring** — first value becomes the reference.
- **Framing** — same information, different wording/colour/sound → different conclusions
  (Kahneman & Tversky "Outbreak").
- **Kuleshov effect** — people infer meaning from context; simple AI plus audio barks seems smart.

**Buran-19:** randomness (damaged boxes, spawns) will be felt as unfair after bad streaks — add
pity/streak protection. Frame losses gently ("signal lost", not "YOU DIED"). Give anomalies audio
"barks" (hum rising when hunting) so simple AI reads as intent.

## Dominant Strategy (p. 176)
A strategy so effective it removes variety. Can be accidental, emergent or deliberate. Signs: play
feels routine; everyone uses the same approach. Fix by balancing (Doubling and Halving) or adding
counters. A temporary or beatable dominant strategy can be good (mastery feeling); it can also create
hot spots that bring players into conflict. Skill level changes which strategy dominates.

**Buran-19:** candidates — turbo ramming (anything dies above 1500 speed), parking near the edge,
farming the same crate type, rocket spam. Watch run data; add counters rather than bans.

## Fitts' Law (p. 178)
Speed vs accuracy of pointing: bigger and closer targets are faster and more accurate; very thin
targets are hard. UI elements that are used often should be large and near where the player acts;
group related controls. For combat targeting, tune to real humans, not the fastest tester.

**Buran-19:** on TV, focus navigation replaces pointing, but the same idea holds — frequently used
entries first, short focus paths. On phones, touch targets ≥ thumb size near the thumbs.

## Fundamental Attribution Error (p. 180)
People excuse their own failures by situation but blame others' failures on character. Designers
blame testers ("the tutorial was in the pause menu!"). In playtests, assume the game is at fault.

**Buran-19:** when the owner says something "feels wrong", treat it as a design defect to find, not
taste to argue with.

## Golden Ratio (p. 182)
φ ≈ 1.618. Proportions near it feel pleasing; violating it on purpose can create unease. Fibonacci
approaches φ and can shape progression curves. Applies to boards, buildings and UI layout.

**Buran-19:** panel proportions (list vs detail ≈ 1 : 0.618), progression curves for step thresholds.

## Griefing (p. 184)
Players who enjoy ruining others' experience (~3% of players in MMOs), vs "Samaritans" who help.
Negative reputation can reward griefers; bans are the usual remedy.

**Buran-19:** single player, not relevant now; relevant if leaderboards appear (validate scores).

## Hype (p. 186)
Expectations from marketing, genre, studio or in-game framing make players perceive what they expect
(placebo); negative framing makes things feel worse (nocebo). Can cheaply substitute for hard
features (higher difficulty "feels smarter"). Risk: if expectations are broken, the effect flips.

**Buran-19:** the "last Soviet crew" framing, codex lore and item descriptions make mechanics feel
meaningful — but every promise in text must be true in play.

## Instant vs. Delayed Gratification (p. 188)
Instant-gratification players: fast, aggressive, trial-and-error, frustrated by walls. Delayed: plan,
explore, play longer. Overpowered items too early feel unearned; too late, too complex. Use visual,
sound, story cues to set the pace; meta goals (badges, unlocks) serve delayed players.

**Buran-19:** every action gives instant juice (sound, flash, numbers); across runs offer delayed goals
(sectors, crew roster, codex completion, achievements).

## Krug's First Law of Usability (p. 190)
"Don't make me think." Users scan and take the first good-enough option. Keys: **simplicity** (bare
essentials on HUD and core loop) and **consistency** (same function looks the same and sits in the
same place; things that look alike are assumed alike). Rule 2: many clicks are fine if each is
mindless. Rule 3: remove half the words, then half again. Doesn't mean removing challenge — make goals
clear.

**Buran-19:** consistent colours: shield cyan, hull red-orange, fuel yellow, laser violet, accent
orange = focus/danger. Short labels. Every screen shows its back/confirm buttons.

## Music and Dopamine (p. 192)
Music triggers dopamine, especially with anticipation and payoff; receptors adapt to repetition.
Use motifs with variation, build-ups before peaks, and short stingers tied to rewards (Zelda chest).

**Buran-19:** layered music that builds with danger; short stingers for rescue, step-up, sector clear;
avoid one 86 s loop for a whole session.

## Pacing (p. 194)
Ebb and flow of highs and lows; sanctuaries between thrills. Western building blocks: tempo, tension,
threat/suspense, movement impetus. **Jo-Ha-Kyu** (Zeami, Noh): Jo — calm start building slowly;
Ha — break into intense action/twist; Kyu — rapid climax that releases tension; then calm again.
Works at every scale: level → boss; a single attack (wind-up, telegraph, release).

**Buran-19:** a sector = Jo (calm search) → Ha (anomalies wake, storm) → Kyu (climax/escape) → calm
radio intercept. Each anomaly attack telegraphs before it hits.

## Problem-Solving Approaches (p. 196)
Designers solve problems and design problems for players; anticipate how players solve them; nudge
without being obvious. Appendix lists methods: make lists/tables/flowcharts, draw it, find patterns,
work backward, solve a similar or opposite problem, simplify, find the weakest link, explain to a
non-expert, step back, measure in numbers.

## Satisficing vs. Optimizing (p. 198)
People pick the first satisfactory option rather than computing the optimum (Herbert Simon). Don't
force optimizing where satisficing works. Some players love optimizing — know the audience.

**Buran-19:** item use must be obvious (A uses the current item); depth for optimizers lives in crew
buffs and combos, never required to progress.

## Sense of Accomplishment (p. 200)
Comes from completing something challenging; fiero at big obstacles. Balance easy and hard tasks
(Flow). ARCS model (John Keller): Attention, Relevance, Confidence (positive feedback on progress),
Satisfaction (reward).

**Buran-19:** each sector ends with a clear "done" beat and a reward screen; boss-like set pieces for
fiero; crew rescued counter as visible progress.

## Spatial Awareness (p. 202)
Space shapes emotion. **Prospect-refuge** (open views feel safe; so do hiding places); **cathedral
effect** (high ceilings, clearer thinking); **agoraphobic** spaces (vast, nowhere to hide — anxiety);
**claustrophobic** spaces (narrow, trapped with a threat). Defensible spaces balance both. **Lighting**
directs attention; warm glow in darkness draws people.

**Buran-19:** open sectors feel agoraphobic by design — add refuges (station wrecks, shielded
zones). Use light (warm beacon, glowing crew) to pull the eye to goals.

## Time Dilation (p. 204)
Perceived time flies in flow (Tetris) and drags when bored. If playtesters feel time dragging, the fun
isn't there.

**Buran-19:** watch for the run "dragging" after minute 2 (current problem: no change until step 19).

## Working Memory (p. 206)
About four chunks at once; lost unless used quickly and repeatedly. Don't teach many mechanics at
once; introduce them over time so they move to long-term memory. Steep learning curves overload it.

**Buran-19:** first sector teaches flying + pickup only; each next sector introduces one mechanic
(laser, rockets, EMP, damaged boxes…), with a single-line hint when it first appears.

## Zero-Sum Game (p. 208)
Winnings exactly balance losses (poker, rock-paper-scissors); every zero-sum game has a minimax
solution; mixed strategies raise the minimum payoff. Non-zero-sum games allow all to win or lose.

**Buran-19:** single-player, mostly not applicable; the anomaly score-drain mechanic is a zero-sum
feel to avoid overusing.
