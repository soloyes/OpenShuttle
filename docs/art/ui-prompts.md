# Art prompts: sectors, codex (Nano Banana)

Все картинки должны быть в стиле интро. Поэтому в каждый запрос прикладывается **одна**
картинка из интро как образец стиля, и промпт начинается со слов «in the same style as the
attached image».

## Как делать (каждую картинку)

1. Открой **новый чат** в Gemini (Nano Banana). В старом чате он тянет стиль из прошлых картинок.
2. Приложи файл-образец из колонки «Реф» (Finder: папка проекта → `art/intro/raw/`).
   Прикладывай только его, без других картинок.
3. Вставь промпт из блока целиком, как есть.
4. Если на картинке появились буквы, подписи, схемы, рамка или планета, которой нет в промпте,
   нажми «перегенерировать». Подписи в игре добавляю я, в картинке их быть не должно.
5. Сохрани результат в `art/ui/raw/` под именем из заголовка (`sector01.jpg` и т. д.).
   Скажи мне, я вставлю в игру.

Образцы (рефы):

| Реф | Что на нём | Для чего |
|---|---|---|
| [p10.jpg](../../art/intro/raw/p10.jpg) | разбросанные космонавты в облаке | sector01 |
| [p09.jpg](../../art/intro/raw/p09.jpg) | взрыв станции, обломки | sector02 |
| [p08.jpg](../../art/intro/raw/p08.jpg) | Объект-19, светящаяся аномалия | sector03, codex (кроме rift) |
| [p11.jpg](../../art/intro/raw/p11.jpg) | разлом, чёрная дыра с диском | sector04, codex_rift |

---

## 1. Карточки секторов (широкие, 16:9)

### `sector01.jpg` — реф [p10.jpg](../../art/intro/raw/p10.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink lines, same
colours and lighting. Wide 16:9 image. Two tiny cosmonauts in white Soviet spacesuits drifting
far apart in empty dark space, a small warm amber beacon light blinking on each suit, total
silence, vast emptiness, deep navy and black. No Earth, no station, no explosion. No text, no
letters, no labels, no frame, no white border.
```

### `sector02.jpg` — реф [p09.jpg](../../art/intro/raw/p09.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink lines, same
colours and lighting. Wide 16:9 image. A slow field of dark rocky asteroids and twisted metal
debris lit from one side by a distant amber sun, a thin red laser beam burning into one rock,
glowing dust. No explosion, no station, no Earth. No text, no letters, no labels, no frame,
no white border.
```

### `sector03.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink lines, same
colours and lighting. Wide 16:9 image. Crackling pale-blue electric discharges drifting like
jellyfish in dark space, thin lightning arcs between them, one tiny cosmonaut far away, cold
nervous light, static haze. No station, no Earth. No text, no letters, no labels, no frame,
no white border.
```

### `sector04.jpg` — реф [p11.jpg](../../art/intro/raw/p11.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink lines, same
colours and lighting. Wide 16:9 image. The rift: a slowly spinning black hole with a disc of
sand-like debris, a few small round shelter domes glowing amber at a safe distance from it,
the tiny white Buran shuttle at the edge of the pull. No Earth. No text, no letters, no
labels, no frame, no white border.
```

---

## 2. Кодекс (квадрат, 1:1)

Один объект по центру на чёрном фоне. Не пиши в запросе «scientific illustration», иначе
выйдет плакат с подписями и врезками.

### `codex_electric.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink lines, same
colours and lighting. Square 1:1 image. One single pale-blue ball of crackling electric
discharge with thin lightning arcs, centred on plain black space. Only this one object.
No station, no shuttle, no Earth. No text, no letters, no labels, no diagrams, no insets,
no annotations, no frame, no white border.
```

### `codex_corrosive.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink lines, same
colours and lighting. Square 1:1 image. One single drifting cloud of brownish acid dust
eating into a small torn metal panel, centred on plain black space. Only this one object.
No station, no shuttle, no Earth. No text, no letters, no labels, no diagrams, no insets,
no annotations, no frame, no white border.
```

### `codex_radiation.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink lines, same
colours and lighting. Square 1:1 image. One single sickly green glowing cloud with a faint
shimmer, centred on plain black space. Only this one object. No station, no shuttle, no
Earth. No text, no letters, no labels, no diagrams, no insets, no annotations, no frame,
no white border.
```

### `codex_magnetic.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink lines, same
colours and lighting. Square 1:1 image. One single violet swirl of magnetic field lines
bending small metal debris around it, centred on plain black space. Only this one object.
No station, no shuttle, no Earth. No text, no letters, no labels, no diagrams, no insets,
no annotations, no frame, no white border.
```

### `codex_rift.jpg` — реф [p11.jpg](../../art/intro/raw/p11.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink lines, same
colours and lighting. Square 1:1 image. One single small black hole with an accretion disc
of sand and debris, light bending around it, centred on plain black space. Only this one
object. No station, no shuttle, no Earth. No text, no letters, no labels, no diagrams,
no insets, no annotations, no frame, no white border.
```

---

## 3. Шаттл для стартового экрана (отдельным спрайтом)

Фон без шаттла уже есть (`art/ui/raw/layers/menu_clean.jpg`). Нужен сам «Буран» один, без
станции: игра поставит его поверх фона и будет покачивать. В новом чате приложи
[art/ui/raw/layers/menu_shuttle_green.jpg](../../art/ui/raw/layers/menu_shuttle_green.jpg).

### `menu_shuttle.jpg` (реф: menu_shuttle_green.jpg)

```
Edit the attached image. Remove the ring space station completely, including every part of it
that is behind or touching the shuttle. Keep only the white space shuttle, exactly as it is
drawn, whole: nose, both wings, the tail fin, the engines. Everything except the shuttle must be
flat solid pure green #00FF00, no gradient, no shadow, no glow, no white border. No text.
```

Если от кольца что-то осталось или шаттл обрезан, перегенерируй. Сохрани в `art/ui/raw/layers/`.

---

## 4. Кодекс: груз (квадрат, 1:1)

Так же, как раздел 2: новый чат, реф [p08.jpg](../../art/intro/raw/p08.jpg), промпт целиком.

### `codex_crate.jpg`

```
In the same style as the attached image: same graphic novel drawing, same ink lines, same
colours and lighting. Square 1:1 image. One single small Soviet metal cargo crate drifting in
space, olive-grey painted steel, rivets, scuffed edges, a stencil-free blank lid, a little frost,
one amber light glint, centred on plain black space. Only this one object. No station, no
shuttle, no Earth. No text, no letters, no numbers, no labels, no diagrams, no insets, no frame,
no white border.
```

### `codex_storage.jpg`

```
In the same style as the attached image: same graphic novel drawing, same ink lines, same
colours and lighting. Square 1:1 image. One single large cylindrical Soviet storage module
drifting in space, ribbed metal shell, a round hatch on its side, small handles, soft amber
light from a seam, centred on plain black space. Only this one object. No station, no shuttle,
no Earth. No text, no letters, no numbers, no labels, no diagrams, no insets, no frame, no white
border.
```

### `codex_fuel.jpg`

```
In the same style as the attached image: same graphic novel drawing, same ink lines, same
colours and lighting. Square 1:1 image. One single spherical Soviet fuel tank drifting in
space, white insulated skin with dark seams, two valves and a short torn pipe, a faint vapour
trail, centred on plain black space. Only this one object. No station, no shuttle, no Earth.
No text, no letters, no numbers, no labels, no diagrams, no insets, no frame, no white border.
```

---

## 5. Финал первой главы (широкие, 16:9)

Сохраняй в `art/chapter1/raw/` как `p01.jpg` … `p04.jpg`. Пока их нет, в финале стоят старые картинки.

### `p01.jpg` — реф [p06.jpg](../../art/intro/raw/p06.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink lines, same
colours and lighting. Wide 16:9 image. Inside the cramped dark cabin of the shuttle, many cosmonauts in white Soviet spacesuits
sitting shoulder to shoulder, tired, some asleep, one smiling weakly, a small amber lamp,
breath fogging in the cold air, Earth not visible. No text, no letters, no labels, no frame,
no white border.
```

### `p02.jpg` — реф [p11.jpg](../../art/intro/raw/p11.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink lines, same
colours and lighting. Wide 16:9 image. The rift: a spinning black hole with a disc of sand-like debris, bigger and wilder than
before, the tiny white shuttle far away at the edge of the frame, looking small. No text, no letters, no labels, no frame,
no white border.
```

### `p03.jpg` — реф [p12.jpg](../../art/intro/raw/p12.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink lines, same
colours and lighting. Wide 16:9 image. Close-up of an old analog radio panel in the shuttle cockpit, a glowing amber frequency
dial, a needle trembling, a gloved hand on the knob, static and faint green light. No text, no letters, no labels, no frame,
no white border.
```

### `p04.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink lines, same
colours and lighting. Wide 16:9 image. Deep inside the rift, a faint cold light shaped almost like a human silhouette, far away,
watching, the edge of the black hole around it, eerie and quiet. No text, no letters, no labels, no frame,
no white border.
```

---

## 6. Карточки секторов второй главы (широкие, 16:9)

Сохраняй в `art/ui/raw/` как `sector07.jpg` … `sector12.jpg`.

### `sector07.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink lines, same
colours and lighting. Wide 16:9 image. A sickly green glowing radiation cloud clinging to the white hull of a shuttle like a leech,
green light spreading over the panels, dark space around. No text, no letters, no labels, no frame,
no white border.
```

### `sector08.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink lines, same
colours and lighting. Wide 16:9 image. Violet magnetic field lines latched onto a shuttle's fuel tanks, thin streams of fuel
vapour being pulled away into a violet swirl, dark space. No text, no letters, no labels, no frame,
no white border.
```

### `sector09.jpg` — реф [p10.jpg](../../art/intro/raw/p10.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink lines, same
colours and lighting. Wide 16:9 image. A pack of brownish acid clouds closing in on a small shuttle from all sides, frozen
mid-motion in a pale blue frost wave, ice crystals in space. No text, no letters, no labels, no frame,
no white border.
```

### `sector10.jpg` — реф [p09.jpg](../../art/intro/raw/p09.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink lines, same
colours and lighting. Wide 16:9 image. A dense deep field of rocks and twisted station wreckage, a small shuttle with a glowing
amber shield in front of it smashing through a rock, sparks and fragments. No text, no letters, no labels, no frame,
no white border.
```

### `sector11.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink lines, same
colours and lighting. Wide 16:9 image. A storm of pale-blue electric discharges filling dark space like lightning between clouds,
a small domed shelter glowing amber in the middle, a shuttle racing toward it. No text, no letters, no labels, no frame,
no white border.
```

### `sector12.jpg` — реф [p11.jpg](../../art/intro/raw/p11.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink lines, same
colours and lighting. Wide 16:9 image. The rift collapsing: the black hole huge and violent, debris and anomalies of every colour
spiralling in, five tiny beacon lights around its edge, a small shuttle diving toward them. No text, no letters, no labels, no frame,
no white border.
```

---

## 7. Финал второй главы (16:9) — `art/chapter2/raw/p01…p03.jpg`

### `p01.jpg` — реф [p12.jpg](../../art/intro/raw/p12.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink lines, same
colours and lighting. Wide 16:9 image. The shuttle cockpit in darkness, the radio panel glowing, a cosmonaut in a white suit
leaning toward the speaker, listening, the reflection of a cold light on his visor. No text, no letters, no labels, no frame,
no white border.
```

### `p02.jpg` — реф [p06.jpg](../../art/intro/raw/p06.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink lines, same
colours and lighting. Wide 16:9 image. An old Soviet cosmonaut commander from 1991, seen as a faint ghostly figure inside a cold
light, worn spacesuit with an old mission patch, calm and patient, face half in shadow. No text, no letters, no labels, no frame,
no white border.
```

### `p03.jpg` — реф [p11.jpg](../../art/intro/raw/p11.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink lines, same
colours and lighting. Wide 16:9 image. The rift seen from close, and inside its dark centre a tiny warm amber glow like a window,
the shuttle small in front of it, hesitant. No text, no letters, no labels, no frame,
no white border.
```

---

## 8. Финал игры (16:9) — `art/chapter3/raw/p01…p04.jpg`

### `p01.jpg` — реф [p11.jpg](../../art/intro/raw/p11.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink lines, same
colours and lighting. Wide 16:9 image. The white shuttle diving straight into the centre of the rift, debris and light spiralling
around it, seen from behind, everything pulled forward. No text, no letters, no labels, no frame,
no white border.
```

### `p02.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink lines, same
colours and lighting. Wide 16:9 image. Pure white light filling almost the whole frame, the faint outline of the shuttle
dissolving into it, silence, soft grain. No text, no letters, no labels, no frame,
no white border.
```

### `p03.jpg` — реф [p03.jpg](../../art/intro/raw/p03.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink lines, same
colours and lighting. Wide 16:9 image. The whole blue Earth from orbit, bright and alive, sunrise on the horizon, the small white
shuttle in the foreground, hopeful. No text, no letters, no labels, no frame,
no white border.
```

### `p04.jpg` — реф [p01.jpg](../../art/intro/raw/p01.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink lines, same
colours and lighting. Wide 16:9 image. Baikonur at dawn, the launch pad and the steppe, a few people in coats looking up at the
sky, warm morning light, hope. No text, no letters, no labels, no frame,
no white border.
```

---

## 9. Карточки секторов третьей главы (16:9) — `art/ui/raw/sector13…sector19.jpg`

### `sector13.jpg` — реф [p10.jpg](../../art/intro/raw/p10.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink lines, same
colours and lighting. Wide 16:9 image. Old cosmonauts in worn white suits drifting among green radiation clouds and violet magnetic
swirls, faint old beacon lights on their suits. No text, no letters, no labels, no frame,
no white border.
```

### `sector14.jpg` — реф [p09.jpg](../../art/intro/raw/p09.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink lines, same
colours and lighting. Wide 16:9 image. A graveyard of station wreckage and rocks slowly drifting toward a black hole in the
distance, a shuttle burning a rock with a red laser. No text, no letters, no labels, no frame,
no white border.
```

### `sector15.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink lines, same
colours and lighting. Wide 16:9 image. Cargo crates floating in space guarded by blue electric discharges and brown acid clouds,
a shuttle slipping between them. No text, no letters, no labels, no frame,
no white border.
```

### `sector16.jpg` — реф [p11.jpg](../../art/intro/raw/p11.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink lines, same
colours and lighting. Wide 16:9 image. The rift pulling everything in like a tide, streams of debris and five small figures on its
edge, a shuttle racing along the ring. No text, no letters, no labels, no frame,
no white border.
```

### `sector17.jpg` — реф [p12.jpg](../../art/intro/raw/p12.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink lines, same
colours and lighting. Wide 16:9 image. A dead dark radar screen in the cockpit, through the window only darkness and a few faint
beacon lights far away. No text, no letters, no labels, no frame,
no white border.
```

### `sector18.jpg` — реф [p09.jpg](../../art/intro/raw/p09.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink lines, same
colours and lighting. Wide 16:9 image. Every kind of anomaly at once around the rift: acid clouds, lightning, green and violet
glows, the shuttle in the middle of the chaos. No text, no letters, no labels, no frame,
no white border.
```

### `sector19.jpg` — реф [p11.jpg](../../art/intro/raw/p11.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink lines, same
colours and lighting. Wide 16:9 image. One cosmonaut floating alone at the very edge of the rift, a warm light behind him, the
shuttle approaching slowly. No text, no letters, no labels, no frame,
no white border.
```

---

## 10. Игровые спрайты (квадрат 1:1, на зелёном)

Чтобы всё, что летает в секторе, было в одном стиле с интро и меню. Сейчас в игре плоский
мультяшный шаттл рядом с почти фотографичными камнями.

Правила для этого раздела:
- **Фон** ровный ярко-зелёный (#00FF00). Я снимаю зелень сам, как сделал со шаттлом для меню.
  Если зелень заползла на объект или фон неровный, перегенерируй.
- **Ракурс** строго сверху: в игре камера смотрит сверху.
- **Кадров анимации не надо.** Одна картинка на объект. Вращение, пульсацию, мерцание игра
  делает сама.
- **Новый чат на каждую картинку,** реф указан в заголовке.
- **Куда класть:** в `art/sprites/raw/` под именем из заголовка.

### Корабль

### `sprite_buran.jpg` — реф [menu_shuttle.jpg](../../art/ui/raw/layers/menu_shuttle.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. The white Soviet Buran space shuttle seen from directly above, nose pointing to the right, wings spread, black heat-shield edges, a small red star on the left wing, three engine bells at the back. Seen strictly from directly above (top-down game sprite), centred, filling about 70% of the square, the whole object visible and not cropped. Flat solid pure green #00FF00 background, no gradient, no shadow on the background, no glow outside the object. No text, no letters, no numbers, no frame, no white border.
```

### Экипаж (у каждой профессии свой акцент на скафандре)

### `sprite_crew_commander.jpg` — реф [p10.jpg](../../art/intro/raw/p10.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. One Soviet cosmonaut in a white spacesuit floating in space, seen from directly above, arms and legs spread like a starfish, helmet towards the top, a gold stripe on the helmet and an amber light on the chest. Seen strictly from directly above (top-down game sprite), centred, filling about 70% of the square, the whole object visible and not cropped. Flat solid pure green #00FF00 background, no gradient, no shadow on the background, no glow outside the object. No text, no letters, no numbers, no frame, no white border.
```

### `sprite_crew_medic.jpg` — реф [p10.jpg](../../art/intro/raw/p10.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. One Soviet cosmonaut in a white spacesuit floating in space, seen from directly above, arms and legs spread like a starfish, helmet towards the top, a red cross shape on the shoulder and a white medical pack. Seen strictly from directly above (top-down game sprite), centred, filling about 70% of the square, the whole object visible and not cropped. Flat solid pure green #00FF00 background, no gradient, no shadow on the background, no glow outside the object. No text, no letters, no numbers, no frame, no white border.
```

### `sprite_crew_scientist.jpg` — реф [p10.jpg](../../art/intro/raw/p10.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. One Soviet cosmonaut in a white spacesuit floating in space, seen from directly above, arms and legs spread like a starfish, helmet towards the top, a small instrument case clipped to the belt and a pale blue visor. Seen strictly from directly above (top-down game sprite), centred, filling about 70% of the square, the whole object visible and not cropped. Flat solid pure green #00FF00 background, no gradient, no shadow on the background, no glow outside the object. No text, no letters, no numbers, no frame, no white border.
```

### `sprite_crew_mechanic.jpg` — реф [p10.jpg](../../art/intro/raw/p10.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. One Soviet cosmonaut in a white spacesuit floating in space, seen from directly above, arms and legs spread like a starfish, helmet towards the top, a wrench and a coil of cable clipped to the suit, scuffed knees. Seen strictly from directly above (top-down game sprite), centred, filling about 70% of the square, the whole object visible and not cropped. Flat solid pure green #00FF00 background, no gradient, no shadow on the background, no glow outside the object. No text, no letters, no numbers, no frame, no white border.
```

### `sprite_crew_military.jpg` — реф [p10.jpg](../../art/intro/raw/p10.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. One Soviet cosmonaut in a white spacesuit floating in space, seen from directly above, arms and legs spread like a starfish, helmet towards the top, a darker grey-green suit and a red star on the helmet. Seen strictly from directly above (top-down game sprite), centred, filling about 70% of the square, the whole object visible and not cropped. Flat solid pure green #00FF00 background, no gradient, no shadow on the background, no glow outside the object. No text, no letters, no numbers, no frame, no white border.
```

### Аномалии

### `sprite_corrosive.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. One cloud of drifting acid dust with a dense swirling core and ragged edges, in pale neutral grey and white only (the game colours it by level). Seen strictly from directly above (top-down game sprite), centred, filling about 70% of the square, the whole object visible and not cropped. Flat solid pure green #00FF00 background, no gradient, no shadow on the background, no glow outside the object. No text, no letters, no numbers, no frame, no white border.
```

### `sprite_electric.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. One ball of pale-blue electric discharge with thin crackling arcs around it. Seen strictly from directly above (top-down game sprite), centred, filling about 70% of the square, the whole object visible and not cropped. Flat solid pure green #00FF00 background, no gradient, no shadow on the background, no glow outside the object. No text, no letters, no numbers, no frame, no white border.
```

### `sprite_radiation.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. One sickly green glowing cloud with a faint shimmer and soft tendrils. Seen strictly from directly above (top-down game sprite), centred, filling about 70% of the square, the whole object visible and not cropped. Flat solid pure green #00FF00 background, no gradient, no shadow on the background, no glow outside the object. No text, no letters, no numbers, no frame, no white border.
```

### `sprite_magnetic.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. One violet swirl of magnetic field lines with a few small metal shards caught in it. Seen strictly from directly above (top-down game sprite), centred, filling about 70% of the square, the whole object visible and not cropped. Flat solid pure green #00FF00 background, no gradient, no shadow on the background, no glow outside the object. No text, no letters, no numbers, no frame, no white border.
```

### `sprite_boss_mother.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. One huge, heavy cloud of acid dust, darker and denser than an ordinary one, with a glowing amber core like an eye and small clouds budding off its edges, menacing. Seen strictly from directly above (top-down game sprite), centred, filling about 70% of the square, the whole object visible and not cropped. Flat solid pure green #00FF00 background, no gradient, no shadow on the background, no glow outside the object. No text, no letters, no numbers, no frame, no white border.
```

### `sprite_boss_storm.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. One huge heart of pale-blue lightning: a bright core inside a cage of thick electric arcs, menacing. Seen strictly from directly above (top-down game sprite), centred, filling about 70% of the square, the whole object visible and not cropped. Flat solid pure green #00FF00 background, no gradient, no shadow on the background, no glow outside the object. No text, no letters, no numbers, no frame, no white border.
```

### Камни и обломки

### `sprite_rock_metal_1.jpg` — реф [p09.jpg](../../art/intro/raw/p09.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. One dark metallic asteroid made of twisted station wreckage and iron rock, variant 1: a different shape from the others. Seen strictly from directly above (top-down game sprite), centred, filling about 70% of the square, the whole object visible and not cropped. Flat solid pure green #00FF00 background, no gradient, no shadow on the background, no glow outside the object. No text, no letters, no numbers, no frame, no white border.
```

### `sprite_rock_metal_2.jpg` — реф [p09.jpg](../../art/intro/raw/p09.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. One dark metallic asteroid made of twisted station wreckage and iron rock, variant 2: a different shape from the others. Seen strictly from directly above (top-down game sprite), centred, filling about 70% of the square, the whole object visible and not cropped. Flat solid pure green #00FF00 background, no gradient, no shadow on the background, no glow outside the object. No text, no letters, no numbers, no frame, no white border.
```

### `sprite_rock_metal_3.jpg` — реф [p09.jpg](../../art/intro/raw/p09.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. One dark metallic asteroid made of twisted station wreckage and iron rock, variant 3: a different shape from the others. Seen strictly from directly above (top-down game sprite), centred, filling about 70% of the square, the whole object visible and not cropped. Flat solid pure green #00FF00 background, no gradient, no shadow on the background, no glow outside the object. No text, no letters, no numbers, no frame, no white border.
```

### `sprite_rock_mineral_1.jpg` — реф [p09.jpg](../../art/intro/raw/p09.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. One grey-brown stony asteroid with pale mineral veins, variant 1: a different shape from the others. Seen strictly from directly above (top-down game sprite), centred, filling about 70% of the square, the whole object visible and not cropped. Flat solid pure green #00FF00 background, no gradient, no shadow on the background, no glow outside the object. No text, no letters, no numbers, no frame, no white border.
```

### `sprite_rock_mineral_2.jpg` — реф [p09.jpg](../../art/intro/raw/p09.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. One grey-brown stony asteroid with pale mineral veins, variant 2: a different shape from the others. Seen strictly from directly above (top-down game sprite), centred, filling about 70% of the square, the whole object visible and not cropped. Flat solid pure green #00FF00 background, no gradient, no shadow on the background, no glow outside the object. No text, no letters, no numbers, no frame, no white border.
```

### `sprite_rock_mineral_3.jpg` — реф [p09.jpg](../../art/intro/raw/p09.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. One grey-brown stony asteroid with pale mineral veins, variant 3: a different shape from the others. Seen strictly from directly above (top-down game sprite), centred, filling about 70% of the square, the whole object visible and not cropped. Flat solid pure green #00FF00 background, no gradient, no shadow on the background, no glow outside the object. No text, no letters, no numbers, no frame, no white border.
```

### `sprite_fragments_metal.jpg` — реф [p09.jpg](../../art/intro/raw/p09.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. Six small separate metal fragments and scraps of plating, spread apart with clear green space between each piece, none touching. Seen strictly from directly above (top-down game sprite), centred, filling about 70% of the square, the whole object visible and not cropped. Flat solid pure green #00FF00 background, no gradient, no shadow on the background, no glow outside the object. No text, no letters, no numbers, no frame, no white border.
```

### `sprite_fragments_mineral.jpg` — реф [p09.jpg](../../art/intro/raw/p09.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. Six small separate stone chips with pale mineral veins, spread apart with clear green space between each piece, none touching. Seen strictly from directly above (top-down game sprite), centred, filling about 70% of the square, the whole object visible and not cropped. Flat solid pure green #00FF00 background, no gradient, no shadow on the background, no glow outside the object. No text, no letters, no numbers, no frame, no white border.
```

### Груз, ракета, убежище, маяк

### `sprite_crate.jpg` — реф [codex_crate.jpg](../../art/ui/raw/codex_crate.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. The olive-grey Soviet metal cargo crate from the reference, seen from directly above, the lid visible. Seen strictly from directly above (top-down game sprite), centred, filling about 70% of the square, the whole object visible and not cropped. Flat solid pure green #00FF00 background, no gradient, no shadow on the background, no glow outside the object. No text, no letters, no numbers, no frame, no white border.
```

### `sprite_crate_gold.jpg` — реф [codex_crate.jpg](../../art/ui/raw/codex_crate.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. The same cargo crate but painted in worn gold and amber, seen from directly above, the lid visible. Seen strictly from directly above (top-down game sprite), centred, filling about 70% of the square, the whole object visible and not cropped. Flat solid pure green #00FF00 background, no gradient, no shadow on the background, no glow outside the object. No text, no letters, no numbers, no frame, no white border.
```

### `sprite_storage.jpg` — реф [codex_storage.jpg](../../art/ui/raw/codex_storage.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. The cylindrical Soviet storage module from the reference, seen from directly above, the round hatch on top. Seen strictly from directly above (top-down game sprite), centred, filling about 70% of the square, the whole object visible and not cropped. Flat solid pure green #00FF00 background, no gradient, no shadow on the background, no glow outside the object. No text, no letters, no numbers, no frame, no white border.
```

### `sprite_storage_gold.jpg` — реф [codex_storage.jpg](../../art/ui/raw/codex_storage.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. The same storage module but in worn gold and amber, seen from directly above, the round hatch on top. Seen strictly from directly above (top-down game sprite), centred, filling about 70% of the square, the whole object visible and not cropped. Flat solid pure green #00FF00 background, no gradient, no shadow on the background, no glow outside the object. No text, no letters, no numbers, no frame, no white border.
```

### `sprite_fuel.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. One spherical Soviet fuel tank, white insulated skin with dark seams and two valves, seen from directly above. Seen strictly from directly above (top-down game sprite), centred, filling about 70% of the square, the whole object visible and not cropped. Flat solid pure green #00FF00 background, no gradient, no shadow on the background, no glow outside the object. No text, no letters, no numbers, no frame, no white border.
```

### `sprite_fuel_gold.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. The same spherical fuel tank but in worn gold and amber, seen from directly above. Seen strictly from directly above (top-down game sprite), centred, filling about 70% of the square, the whole object visible and not cropped. Flat solid pure green #00FF00 background, no gradient, no shadow on the background, no glow outside the object. No text, no letters, no numbers, no frame, no white border.
```

### `sprite_rocket.jpg` — реф [menu_shuttle.jpg](../../art/ui/raw/layers/menu_shuttle.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. One small Soviet missile seen from directly above, nose pointing to the right, white body with a red tip and four small fins. Seen strictly from directly above (top-down game sprite), centred, filling about 70% of the square, the whole object visible and not cropped. Flat solid pure green #00FF00 background, no gradient, no shadow on the background, no glow outside the object. No text, no letters, no numbers, no frame, no white border.
```

### `sprite_shelter.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. One small round shelter dome from a space station, seen from directly above, dark metal ring with a softly glowing amber dome in the middle. Seen strictly from directly above (top-down game sprite), centred, filling about 70% of the square, the whole object visible and not cropped. Flat solid pure green #00FF00 background, no gradient, no shadow on the background, no glow outside the object. No text, no letters, no numbers, no frame, no white border.
```

### `sprite_beacon.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. One small extraction beacon, seen from directly above: a metal tripod frame with a bright warm amber lamp in the centre. Seen strictly from directly above (top-down game sprite), centred, filling about 70% of the square, the whole object visible and not cropped. Flat solid pure green #00FF00 background, no gradient, no shadow on the background, no glow outside the object. No text, no letters, no numbers, no frame, no white border.
```

### Иконки предметов и меток (значки в одном стиле)

### `icon_boost.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. The pictogram: a stylised rocket engine flame with two speed lines. A round dark enamel badge in the style of a Soviet space-program pin: a thin amber rim, a dark navy field, one simple bold pictogram in warm white and amber in the middle. Seen straight on, centred, filling about 80% of the square. Flat solid pure green #00FF00 background outside the badge. No text, no letters, no numbers, no frame, no white border.
```

### `icon_repair.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. The pictogram: a wrench crossed with a hull plate. A round dark enamel badge in the style of a Soviet space-program pin: a thin amber rim, a dark navy field, one simple bold pictogram in warm white and amber in the middle. Seen straight on, centred, filling about 80% of the square. Flat solid pure green #00FF00 background outside the badge. No text, no letters, no numbers, no frame, no white border.
```

### `icon_shield.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. The pictogram: a hexagonal shield over a small crate. A round dark enamel badge in the style of a Soviet space-program pin: a thin amber rim, a dark navy field, one simple bold pictogram in warm white and amber in the middle. Seen straight on, centred, filling about 80% of the square. Flat solid pure green #00FF00 background outside the badge. No text, no letters, no numbers, no frame, no white border.
```

### `icon_splash.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. The pictogram: a burst of droplets breaking outwards. A round dark enamel badge in the style of a Soviet space-program pin: a thin amber rim, a dark navy field, one simple bold pictogram in warm white and amber in the middle. Seen straight on, centred, filling about 80% of the square. Flat solid pure green #00FF00 background outside the badge. No text, no letters, no numbers, no frame, no white border.
```

### `icon_scan.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. The pictogram: a radar sweep arc over a closed crate. A round dark enamel badge in the style of a Soviet space-program pin: a thin amber rim, a dark navy field, one simple bold pictogram in warm white and amber in the middle. Seen straight on, centred, filling about 80% of the square. Flat solid pure green #00FF00 background outside the badge. No text, no letters, no numbers, no frame, no white border.
```

### `icon_rockets.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. The pictogram: two small rockets side by side pointing up. A round dark enamel badge in the style of a Soviet space-program pin: a thin amber rim, a dark navy field, one simple bold pictogram in warm white and amber in the middle. Seen straight on, centred, filling about 80% of the square. Flat solid pure green #00FF00 background outside the badge. No text, no letters, no numbers, no frame, no white border.
```

### `icon_freeze.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. The pictogram: a six-pointed snowflake. A round dark enamel badge in the style of a Soviet space-program pin: a thin amber rim, a dark navy field, one simple bold pictogram in warm white and amber in the middle. Seen straight on, centred, filling about 80% of the square. Flat solid pure green #00FF00 background outside the badge. No text, no letters, no numbers, no frame, no white border.
```

### `icon_degrade.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. The pictogram: a downward arrow breaking a cloud in half. A round dark enamel badge in the style of a Soviet space-program pin: a thin amber rim, a dark navy field, one simple bold pictogram in warm white and amber in the middle. Seen straight on, centred, filling about 80% of the square. Flat solid pure green #00FF00 background outside the badge. No text, no letters, no numbers, no frame, no white border.
```

### `icon_laser.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. The pictogram: a thin straight beam from a lens. A round dark enamel badge in the style of a Soviet space-program pin: a thin amber rim, a dark navy field, one simple bold pictogram in warm white and amber in the middle. Seen straight on, centred, filling about 80% of the square. Flat solid pure green #00FF00 background outside the badge. No text, no letters, no numbers, no frame, no white border.
```

### `icon_radar.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. The pictogram: a round radar screen with a sweep line. A round dark enamel badge in the style of a Soviet space-program pin: a thin amber rim, a dark navy field, one simple bold pictogram in warm white and amber in the middle. Seen straight on, centred, filling about 80% of the square. Flat solid pure green #00FF00 background outside the badge. No text, no letters, no numbers, no frame, no white border.
```

### `icon_trash.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. The pictogram: a crate with a crack and falling rocks. A round dark enamel badge in the style of a Soviet space-program pin: a thin amber rim, a dark navy field, one simple bold pictogram in warm white and amber in the middle. Seen straight on, centred, filling about 80% of the square. Flat solid pure green #00FF00 background outside the badge. No text, no letters, no numbers, no frame, no white border.
```

### `icon_fuel.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. The pictogram: a fuel drop inside a canister outline. A round dark enamel badge in the style of a Soviet space-program pin: a thin amber rim, a dark navy field, one simple bold pictogram in warm white and amber in the middle. Seen straight on, centred, filling about 80% of the square. Flat solid pure green #00FF00 background outside the badge. No text, no letters, no numbers, no frame, no white border.
```

### `mark_sealed.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. The pictogram: a closed padlock. A round dark enamel badge in the style of a Soviet space-program pin: a thin amber rim, a dark navy field, one simple bold pictogram in warm white and amber in the middle. Seen straight on, centred, filling about 80% of the square. Flat solid pure green #00FF00 background outside the badge. No text, no letters, no numbers, no frame, no white border.
```

### `mark_burnt.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. The pictogram: a scorched crate with a small flame. A round dark enamel badge in the style of a Soviet space-program pin: a thin amber rim, a dark navy field, one simple bold pictogram in warm white and amber in the middle. Seen straight on, centred, filling about 80% of the square. Flat solid pure green #00FF00 background outside the badge. No text, no letters, no numbers, no frame, no white border.
```

### `badge_commander.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. The pictogram: a five-pointed star with a laurel branch. A round dark enamel badge in the style of a Soviet space-program pin: a thin amber rim, a dark navy field, one simple bold pictogram in warm white and amber in the middle. Seen straight on, centred, filling about 80% of the square. Flat solid pure green #00FF00 background outside the badge. No text, no letters, no numbers, no frame, no white border.
```

### `badge_medic.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. The pictogram: a red cross. A round dark enamel badge in the style of a Soviet space-program pin: a thin amber rim, a dark navy field, one simple bold pictogram in warm white and amber in the middle. Seen straight on, centred, filling about 80% of the square. Flat solid pure green #00FF00 background outside the badge. No text, no letters, no numbers, no frame, no white border.
```

### `badge_scientist.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. The pictogram: an atom with three orbits. A round dark enamel badge in the style of a Soviet space-program pin: a thin amber rim, a dark navy field, one simple bold pictogram in warm white and amber in the middle. Seen straight on, centred, filling about 80% of the square. Flat solid pure green #00FF00 background outside the badge. No text, no letters, no numbers, no frame, no white border.
```

### `badge_mechanic.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. The pictogram: a gear and a wrench. A round dark enamel badge in the style of a Soviet space-program pin: a thin amber rim, a dark navy field, one simple bold pictogram in warm white and amber in the middle. Seen straight on, centred, filling about 80% of the square. Flat solid pure green #00FF00 background outside the badge. No text, no letters, no numbers, no frame, no white border.
```

### `badge_military.jpg` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. The pictogram: a red star with crossed rifles. A round dark enamel badge in the style of a Soviet space-program pin: a thin amber rim, a dark navy field, one simple bold pictogram in warm white and amber in the middle. Seen straight on, centred, filling about 80% of the square. Flat solid pure green #00FF00 background outside the badge. No text, no letters, no numbers, no frame, no white border.
```

### Фоны глав (широкие 16:9)

### `bg_chapter1.jpg` — реф [p13.jpg](../../art/intro/raw/p13.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Wide 16:9 image. Empty deep space as a game background, seen as if from above, cold and silent: deep navy, sparse stars, a faint cold blue nebula. Very dark overall so objects on top read clearly, low contrast, soft grain, no planets, no ships, no objects, no bright spots. No text, no letters, no frame, no white border.
```

### `bg_chapter2.jpg` — реф [p13.jpg](../../art/intro/raw/p13.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Wide 16:9 image. Empty deep space as a game background, seen as if from above, uneasy: deep navy with a violet and sickly green nebula, distant lightning glints. Very dark overall so objects on top read clearly, low contrast, soft grain, no planets, no ships, no objects, no bright spots. No text, no letters, no frame, no white border.
```

### `bg_chapter3.jpg` — реф [p13.jpg](../../art/intro/raw/p13.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Wide 16:9 image. Empty deep space as a game background, seen as if from above, the end of the road: dark navy turning warm, an amber and red nebula, the glow of a rift far away. Very dark overall so objects on top read clearly, low contrast, soft grain, no planets, no ships, no objects, no bright spots. No text, no letters, no frame, no white border.
```

---

## Уже готово (в игре)

`menu.jpg`, `crew_commander / medic / scientist / mechanic / military.jpg`, `clear.jpg`,
`lost.jpg`, `sector01–04.jpg`, `codex_corrosive / electric / radiation / magnetic / rift.jpg`,
`layers/menu_shuttle.jpg` (фон меню без шаттла теперь `menu.jpg`). Слои меню: `tools/art/menu_layers.py`.
