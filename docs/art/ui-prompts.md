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

### Экипаж (у каждого свой силуэт и цвет скафандра)

Первая попытка вышла с одинаковыми позами и мелкими различиями, все пятеро как близнецы.
Теперь отличие в позе и цвете, чтобы читалось даже крошкой на экране. Лица не видно ни у кого.

### `sprite_crew_commander.jpg` — реф [p10.jpg](../../art/intro/raw/p10.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. One Soviet cosmonaut commander floating in space: one arm raised high waving a small amber signal lamp, the other arm on the hip, legs straight and together (a tall, narrow silhouette). White spacesuit with broad red-and-gold stripes on the shoulders and a gold band around the helmet. The helmet visor is closed and mirrored gold: no face visible. Seen strictly from directly above (top-down game sprite), centred, filling about 70% of the square, the whole figure visible and not cropped. Flat solid pure green #00FF00 background, no gradient, no shadow on the background, no glow outside the figure. No text, no letters, no numbers, no frame, no white border.
```

### `sprite_crew_medic.jpg` — реф [p10.jpg](../../art/intro/raw/p10.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. One Soviet cosmonaut medic floating in space, curled up with knees drawn to the chest, hugging a large white medical case with a big red cross on it (a round, compact silhouette). White spacesuit with red cuffs and red boots. The helmet visor is closed and mirrored gold: no face visible. Seen strictly from directly above (top-down game sprite), centred, filling about 70% of the square, the whole figure visible and not cropped. Flat solid pure green #00FF00 background, no gradient, no shadow on the background, no glow outside the figure. No text, no letters, no numbers, no frame, no white border.
```

### `sprite_crew_scientist.jpg` — реф [p10.jpg](../../art/intro/raw/p10.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. One Soviet cosmonaut scientist floating in space, sitting cross-legged in zero gravity and holding a glowing pale-blue instrument tablet in both hands in front of the chest, a thin antenna on the backpack. White spacesuit with pale blue panels on the arms and legs. The helmet visor is closed and mirrored gold: no face visible. Seen strictly from directly above (top-down game sprite), centred, filling about 70% of the square, the whole figure visible and not cropped. Flat solid pure green #00FF00 background, no gradient, no shadow on the background, no glow outside the figure. No text, no letters, no numbers, no frame, no white border.
```

### `sprite_crew_mechanic.jpg` — реф [p10.jpg](../../art/intro/raw/p10.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. One Soviet cosmonaut mechanic tumbling sideways in space, one arm stretched far out gripping a huge wrench, a long tether cable snaking out from the belt (a wide, diagonal silhouette). Scuffed, dirty white spacesuit with a bright orange tool belt and orange knee pads. The helmet visor is closed and mirrored gold: no face visible. Seen strictly from directly above (top-down game sprite), centred, filling about 70% of the square, the whole figure visible and not cropped. Flat solid pure green #00FF00 background, no gradient, no shadow on the background, no glow outside the figure. No text, no letters, no numbers, no frame, no white border.
```

### `sprite_crew_military.jpg` — реф [p10.jpg](../../art/intro/raw/p10.jpg)

```
In the same style as the attached image: same graphic novel drawing, same ink outlines, same muted colours and lighting. Square 1:1 image. One Soviet military cosmonaut floating upright in space, standing straight at attention and saluting with one hand (a stiff, upright silhouette). Dark olive-green military spacesuit, not white, with heavier armoured shoulders and a red star on the chest. The helmet visor is closed and mirrored gold: no face visible. Seen strictly from directly above (top-down game sprite), centred, filling about 70% of the square, the whole figure visible and not cropped. Flat solid pure green #00FF00 background, no gradient, no shadow on the background, no glow outside the figure. No text, no letters, no numbers, no frame, no white border.
```

### `anim_buran_roll.gif` (или `anim_buran_roll_4x3.png`) — реф [ship_crew_static.png](../../art/sprites/ref/ship_crew_static.png)

Плавный крен: 12 кадров от ровного до 60°, в одну сторону. Обратную сторону игра делает
зеркалом, итого 24 положения, и кадр выбирается по крутизне поворота, без скачков.

```
Make ONE image: an animation sprite sheet of a spaceship banking, for a 2D top-down game.

LAYOUT (strict):
- A grid of 4 columns x 3 rows = 12 equal SQUARE cells. Thin straight white lines between the cells, nothing crosses a line.
- Read order: left to right, top row first. Frame 1 is top-left, frame 12 is bottom-right.
- In every cell the ship is centred at exactly the same spot, nose pointing to the RIGHT, its length the same in every frame (about 80% of the cell width), never touching the cell edges.
- Background in every cell: flat solid pure green #00FF00, no gradient, no shadow.

SUBJECT: the white Soviet Buran space shuttle from the attached image (top row, first ship), seen strictly from directly above: same drawing, same black heat-shield edges, same red stars, same engines, same cockpit windows. Exactly two wings and one tail in every frame - never add, remove or duplicate a wing.

MOTION: the ship rolls around its long nose-to-tail axis, smoothly, in 12 EVEN steps from 0 to 60 degrees, about 5 degrees more in every frame:
- frame 1: perfectly level, both wings equal and fully visible;
- frames 2-6: the upper wing (the one at the top of the cell) gradually tilts away: it gets a little shorter and a little darker each frame, the lower wing a little wider and brighter;
- frames 7-12: the upper wing keeps shortening into a thin dark sliver, the side of the fuselage becomes visible more and more, the lower wing looks wide; frame 12 is banked about 60 degrees, almost on edge.
The nose, length and position never move; only the roll changes, by small equal steps, like frames of one smooth animation.

STYLE: the same graphic-novel look as the attached image: ink outlines, muted painterly colours, soft film grain.

No text, no numbers, no labels, no frame around the whole image.
```

Если 12 кадров не держит, попроси 8 (сетка 4 × 2, шаг около 8°): тоже будет плавнее, чем сейчас.

### Кадры крена шаттла (старый способ, по одному кадру)

Для микро-анимации: шаттл наклоняется на крыло по продольной оси. Нужны три кадра наклона
в одну сторону, в другую сторону я сделаю зеркалом. Приложи [sprite_buran.jpg](../../art/sprites/raw/sprite_buran.jpg)
и вставь промпт, меняя только число градусов. Если детали от кадра к кадру заметно гуляют,
оставим крен кодом, как сейчас.

### `sprite_buran_roll20.jpg` — реф [sprite_buran.jpg](../../art/sprites/raw/sprite_buran.jpg)

```
Edit the attached image. The same shuttle, exactly the same drawing, details, colours and size, still seen from directly above with the nose pointing to the right, but rolled 20 degrees around its long nose-to-tail axis: the upper wing tilts away from the viewer and looks shorter and slightly darker, the lower wing tilts towards the viewer, the side of the fuselage becomes a little visible. Keep the same position in the frame and the same flat solid pure green #00FF00 background. No text, no frame.
```

### `sprite_buran_roll40.jpg` — реф [sprite_buran.jpg](../../art/sprites/raw/sprite_buran.jpg)

```
Edit the attached image. The same shuttle, exactly the same drawing, details, colours and size, still seen from directly above with the nose pointing to the right, but rolled 40 degrees around its long nose-to-tail axis: the upper wing tilts away from the viewer and looks shorter and slightly darker, the lower wing tilts towards the viewer, the side of the fuselage becomes a little visible. Keep the same position in the frame and the same flat solid pure green #00FF00 background. No text, no frame.
```

### `sprite_buran_roll60.jpg` — реф [sprite_buran.jpg](../../art/sprites/raw/sprite_buran.jpg)

```
Edit the attached image. The same shuttle, exactly the same drawing, details, colours and size, still seen from directly above with the nose pointing to the right, but rolled 60 degrees around its long nose-to-tail axis: the upper wing tilts away from the viewer and looks shorter and slightly darker, the lower wing tilts towards the viewer, the side of the fuselage becomes a little visible. Keep the same position in the frame and the same flat solid pure green #00FF00 background. No text, no frame.
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

## 11. Анимации: раскадровки 8 кадров (аномалии и экипаж)

Чтобы всё в секторе шевелилось, как старые аномалии, нужен не один кадр, а **8 кадров одной
закольцованной анимации на одной картинке**: сетка 4 × 2, белые линии между клетками, зелёный
фон. Я режу сетку на кадры сам. Покадровая анимация на одной картинке работает лучше, чем кадры
по отдельности: генератор видит все кадры сразу и держит объект одинаковым.

**Как делать:**
1. Новый чат (ChatGPT или Nano Banana), один объект — одна картинка.
2. Приложи реф из заголовка: [anomalies_static.png](../../art/sprites/ref/anomalies_static.png)
   для аномалий, [ship_crew_static.png](../../art/sprites/ref/ship_crew_static.png) для экипажа.
   Можно дописать: «the subject should look like the one in cell N of the attached image».
3. Вставь промпт целиком.
4. Проверь:
   - ровно 8 клеток 4 × 2;
   - объект один и тот же во всех клетках;
   - движение маленькими шагами;
   - 8-й кадр плавно переходит в 1-й.
5. Сохрани в `art/sprites/raw/` под именем из заголовка.

Если 8 кадров не держатся, попроси 4 кадра в ряд (сетка 4 × 1): тоже подойдёт.

### Аномалии

Готово (GIF, в игре): все шесть аномалий и боссов, все пять космонавтов.

### `anim_corrosive.png` — реф [anomalies_static.png](../../art/sprites/ref/anomalies_static.png)

```
Make ONE image: an animation sprite sheet for a 2D top-down space game.

LAYOUT (strict):
- A grid of 4 columns x 2 rows = 8 equal SQUARE cells, wide 2:1 image.
- Thin straight white lines between the cells. Nothing crosses a line.
- Read order: left to right, top row first. Frame 1 is top-left, frame 8 is bottom-right.
- In every cell the subject is centred at the same spot and at the same size, filling about 70% of the cell, never touching the cell edges.
- Background in every cell: flat solid pure green #00FF00, no gradient, no shadow, no glow spilling onto the green.

ANIMATION (the most important part):
- The 8 frames are ONE continuous seamless LOOP: frame 8 flows straight back into frame 1.
- The subject is the SAME object in every frame: same design, same colours, same size, same line work. Only the motion described below changes, a little from each frame to the next (small steps, like a real animation, not 8 different pictures).
- Seen strictly from directly above.

STYLE: the same graphic-novel look as the attached reference image: ink outlines, muted painterly colours, soft film grain.

No text, no numbers, no labels, no frame around the whole image.

SUBJECT: Corrosive anomaly: a drifting cloud of acid dust with a dense swirling core, in neutral grey and white only (the game tints it by level).

MOTION over the 8 frames: The swirl turns slowly clockwise (about 45 degrees over the 8 frames, so the loop is seamless), puffs on the edge boil and drift outward and fade, new puffs appear; the core gently brightens on frames 3-4 and dims again.
```

### `anim_electric.png` — реф [anomalies_static.png](../../art/sprites/ref/anomalies_static.png)

```
Make ONE image: an animation sprite sheet for a 2D top-down space game.

LAYOUT (strict):
- A grid of 4 columns x 2 rows = 8 equal SQUARE cells, wide 2:1 image.
- Thin straight white lines between the cells. Nothing crosses a line.
- Read order: left to right, top row first. Frame 1 is top-left, frame 8 is bottom-right.
- In every cell the subject is centred at the same spot and at the same size, filling about 70% of the cell, never touching the cell edges.
- Background in every cell: flat solid pure green #00FF00, no gradient, no shadow, no glow spilling onto the green.

ANIMATION (the most important part):
- The 8 frames are ONE continuous seamless LOOP: frame 8 flows straight back into frame 1.
- The subject is the SAME object in every frame: same design, same colours, same size, same line work. Only the motion described below changes, a little from each frame to the next (small steps, like a real animation, not 8 different pictures).
- Seen strictly from directly above.

STYLE: the same graphic-novel look as the attached reference image: ink outlines, muted painterly colours, soft film grain.

No text, no numbers, no labels, no frame around the whole image.

SUBJECT: Electric anomaly: a ball of pale-blue electric discharge with thin crackling arcs.

MOTION over the 8 frames: The ball pulses (slightly larger on frames 2 and 6); the lightning arcs around it are DIFFERENT in every frame - they jump, fork and vanish like real lightning; one bright flash on frame 5.
```

### `anim_radiation.png` — реф [anomalies_static.png](../../art/sprites/ref/anomalies_static.png)

```
Make ONE image: an animation sprite sheet for a 2D top-down space game.

LAYOUT (strict):
- A grid of 4 columns x 2 rows = 8 equal SQUARE cells, wide 2:1 image.
- Thin straight white lines between the cells. Nothing crosses a line.
- Read order: left to right, top row first. Frame 1 is top-left, frame 8 is bottom-right.
- In every cell the subject is centred at the same spot and at the same size, filling about 70% of the cell, never touching the cell edges.
- Background in every cell: flat solid pure green #00FF00, no gradient, no shadow, no glow spilling onto the green.

ANIMATION (the most important part):
- The 8 frames are ONE continuous seamless LOOP: frame 8 flows straight back into frame 1.
- The subject is the SAME object in every frame: same design, same colours, same size, same line work. Only the motion described below changes, a little from each frame to the next (small steps, like a real animation, not 8 different pictures).
- Seen strictly from directly above.

STYLE: the same graphic-novel look as the attached reference image: ink outlines, muted painterly colours, soft film grain.

No text, no numbers, no labels, no frame around the whole image.

SUBJECT: Radiation anomaly: a sickly green glowing cloud with soft tendrils.

MOTION over the 8 frames: The cloud breathes: expands slowly over frames 1-4 and contracts over 5-8; the tendrils sway like seaweed; a faint green shimmer travels around the edge.
```

### `anim_magnetic.png` — реф [anomalies_static.png](../../art/sprites/ref/anomalies_static.png)

```
Make ONE image: an animation sprite sheet for a 2D top-down space game.

LAYOUT (strict):
- A grid of 4 columns x 2 rows = 8 equal SQUARE cells, wide 2:1 image.
- Thin straight white lines between the cells. Nothing crosses a line.
- Read order: left to right, top row first. Frame 1 is top-left, frame 8 is bottom-right.
- In every cell the subject is centred at the same spot and at the same size, filling about 70% of the cell, never touching the cell edges.
- Background in every cell: flat solid pure green #00FF00, no gradient, no shadow, no glow spilling onto the green.

ANIMATION (the most important part):
- The 8 frames are ONE continuous seamless LOOP: frame 8 flows straight back into frame 1.
- The subject is the SAME object in every frame: same design, same colours, same size, same line work. Only the motion described below changes, a little from each frame to the next (small steps, like a real animation, not 8 different pictures).
- Seen strictly from directly above.

STYLE: the same graphic-novel look as the attached reference image: ink outlines, muted painterly colours, soft film grain.

No text, no numbers, no labels, no frame around the whole image.

SUBJECT: Magnetic anomaly: a violet swirl of magnetic field lines with small metal shards caught in it.

MOTION over the 8 frames: The field lines rotate counter-clockwise (about 45 degrees over the 8 frames); the metal shards orbit with them and tumble; the bright violet core flickers.
```

### `anim_boss_mother.png` — реф [anomalies_static.png](../../art/sprites/ref/anomalies_static.png)

```
Make ONE image: an animation sprite sheet for a 2D top-down space game.

LAYOUT (strict):
- A grid of 4 columns x 2 rows = 8 equal SQUARE cells, wide 2:1 image.
- Thin straight white lines between the cells. Nothing crosses a line.
- Read order: left to right, top row first. Frame 1 is top-left, frame 8 is bottom-right.
- In every cell the subject is centred at the same spot and at the same size, filling about 70% of the cell, never touching the cell edges.
- Background in every cell: flat solid pure green #00FF00, no gradient, no shadow, no glow spilling onto the green.

ANIMATION (the most important part):
- The 8 frames are ONE continuous seamless LOOP: frame 8 flows straight back into frame 1.
- The subject is the SAME object in every frame: same design, same colours, same size, same line work. Only the motion described below changes, a little from each frame to the next (small steps, like a real animation, not 8 different pictures).
- Seen strictly from directly above.

STYLE: the same graphic-novel look as the attached reference image: ink outlines, muted painterly colours, soft film grain.

No text, no numbers, no labels, no frame around the whole image.

SUBJECT: The Mother: a huge heavy cloud of dark acid dust with a glowing amber core like an eye.

MOTION over the 8 frames: The whole mass churns slowly; the amber core pulses like a heartbeat (bright on frames 1 and 5); small clouds bud off the edge on frames 3 and 7 and drift away.
```

### `anim_boss_storm.png` — реф [anomalies_static.png](../../art/sprites/ref/anomalies_static.png)

```
Make ONE image: an animation sprite sheet for a 2D top-down space game.

LAYOUT (strict):
- A grid of 4 columns x 2 rows = 8 equal SQUARE cells, wide 2:1 image.
- Thin straight white lines between the cells. Nothing crosses a line.
- Read order: left to right, top row first. Frame 1 is top-left, frame 8 is bottom-right.
- In every cell the subject is centred at the same spot and at the same size, filling about 70% of the cell, never touching the cell edges.
- Background in every cell: flat solid pure green #00FF00, no gradient, no shadow, no glow spilling onto the green.

ANIMATION (the most important part):
- The 8 frames are ONE continuous seamless LOOP: frame 8 flows straight back into frame 1.
- The subject is the SAME object in every frame: same design, same colours, same size, same line work. Only the motion described below changes, a little from each frame to the next (small steps, like a real animation, not 8 different pictures).
- Seen strictly from directly above.

STYLE: the same graphic-novel look as the attached reference image: ink outlines, muted painterly colours, soft film grain.

No text, no numbers, no labels, no frame around the whole image.

SUBJECT: The Storm Heart: a huge sphere of blue lightning around a bright core.

MOTION over the 8 frames: Thick lightning cage crawls and reshapes every frame; the core flares strongly on frame 4 and frame 8; the whole sphere trembles slightly.
```

### Экипаж

### `anim_crew_commander.png` — реф [ship_crew_static.png](../../art/sprites/ref/ship_crew_static.png)

```
Make ONE image: an animation sprite sheet for a 2D top-down space game.

LAYOUT (strict):
- A grid of 4 columns x 2 rows = 8 equal SQUARE cells, wide 2:1 image.
- Thin straight white lines between the cells. Nothing crosses a line.
- Read order: left to right, top row first. Frame 1 is top-left, frame 8 is bottom-right.
- In every cell the subject is centred at the same spot and at the same size, filling about 70% of the cell, never touching the cell edges.
- Background in every cell: flat solid pure green #00FF00, no gradient, no shadow, no glow spilling onto the green.

ANIMATION (the most important part):
- The 8 frames are ONE continuous seamless LOOP: frame 8 flows straight back into frame 1.
- The subject is the SAME object in every frame: same design, same colours, same size, same line work. Only the motion described below changes, a little from each frame to the next (small steps, like a real animation, not 8 different pictures).
- Seen strictly from directly above.

STYLE: the same graphic-novel look as the attached reference image: ink outlines, muted painterly colours, soft film grain.

No text, no numbers, no labels, no frame around the whole image.

SUBJECT: Soviet cosmonaut commander: white suit with red-and-gold shoulder stripes, gold helmet band, mirrored gold visor (no face), one arm raised holding an amber signal lamp, other hand on hip, legs together.

MOTION over the 8 frames: He floats in zero gravity: the raised arm slowly waves the lamp left and right, the lamp blinks (bright on frames 1-2 and 5-6), the legs drift a little apart and back, the body sways gently.
```

### `anim_crew_medic.png` — реф [ship_crew_static.png](../../art/sprites/ref/ship_crew_static.png)

```
Make ONE image: an animation sprite sheet for a 2D top-down space game.

LAYOUT (strict):
- A grid of 4 columns x 2 rows = 8 equal SQUARE cells, wide 2:1 image.
- Thin straight white lines between the cells. Nothing crosses a line.
- Read order: left to right, top row first. Frame 1 is top-left, frame 8 is bottom-right.
- In every cell the subject is centred at the same spot and at the same size, filling about 70% of the cell, never touching the cell edges.
- Background in every cell: flat solid pure green #00FF00, no gradient, no shadow, no glow spilling onto the green.

ANIMATION (the most important part):
- The 8 frames are ONE continuous seamless LOOP: frame 8 flows straight back into frame 1.
- The subject is the SAME object in every frame: same design, same colours, same size, same line work. Only the motion described below changes, a little from each frame to the next (small steps, like a real animation, not 8 different pictures).
- Seen strictly from directly above.

STYLE: the same graphic-novel look as the attached reference image: ink outlines, muted painterly colours, soft film grain.

No text, no numbers, no labels, no frame around the whole image.

SUBJECT: Soviet cosmonaut medic: white suit with red cuffs and red boots, mirrored gold visor (no face), curled up hugging a big white case with a red cross.

MOTION over the 8 frames: Curled up, slowly tumbling: the whole figure rotates about 45 degrees over the 8 frames, the knees open slightly and close again, a red light on the case blinks on frames 1 and 5.
```

### `anim_crew_scientist.png` — реф [ship_crew_static.png](../../art/sprites/ref/ship_crew_static.png)

```
Make ONE image: an animation sprite sheet for a 2D top-down space game.

LAYOUT (strict):
- A grid of 4 columns x 2 rows = 8 equal SQUARE cells, wide 2:1 image.
- Thin straight white lines between the cells. Nothing crosses a line.
- Read order: left to right, top row first. Frame 1 is top-left, frame 8 is bottom-right.
- In every cell the subject is centred at the same spot and at the same size, filling about 70% of the cell, never touching the cell edges.
- Background in every cell: flat solid pure green #00FF00, no gradient, no shadow, no glow spilling onto the green.

ANIMATION (the most important part):
- The 8 frames are ONE continuous seamless LOOP: frame 8 flows straight back into frame 1.
- The subject is the SAME object in every frame: same design, same colours, same size, same line work. Only the motion described below changes, a little from each frame to the next (small steps, like a real animation, not 8 different pictures).
- Seen strictly from directly above.

STYLE: the same graphic-novel look as the attached reference image: ink outlines, muted painterly colours, soft film grain.

No text, no numbers, no labels, no frame around the whole image.

SUBJECT: Soviet cosmonaut scientist: white suit with pale-blue panels, mirrored gold visor (no face), sitting cross-legged holding a glowing pale-blue tablet, a thin antenna on the backpack.

MOTION over the 8 frames: Sitting cross-legged in zero gravity: bobs gently up and down, the antenna sways, the tablet screen glows brighter and dimmer, the head tilts slightly as if reading.
```

### `anim_crew_mechanic.png` — реф [ship_crew_static.png](../../art/sprites/ref/ship_crew_static.png)

```
Make ONE image: an animation sprite sheet for a 2D top-down space game.

LAYOUT (strict):
- A grid of 4 columns x 2 rows = 8 equal SQUARE cells, wide 2:1 image.
- Thin straight white lines between the cells. Nothing crosses a line.
- Read order: left to right, top row first. Frame 1 is top-left, frame 8 is bottom-right.
- In every cell the subject is centred at the same spot and at the same size, filling about 70% of the cell, never touching the cell edges.
- Background in every cell: flat solid pure green #00FF00, no gradient, no shadow, no glow spilling onto the green.

ANIMATION (the most important part):
- The 8 frames are ONE continuous seamless LOOP: frame 8 flows straight back into frame 1.
- The subject is the SAME object in every frame: same design, same colours, same size, same line work. Only the motion described below changes, a little from each frame to the next (small steps, like a real animation, not 8 different pictures).
- Seen strictly from directly above.

STYLE: the same graphic-novel look as the attached reference image: ink outlines, muted painterly colours, soft film grain.

No text, no numbers, no labels, no frame around the whole image.

SUBJECT: Soviet cosmonaut mechanic: scuffed white suit with an orange tool belt and orange knee pads, mirrored gold visor (no face), tumbling sideways holding a big wrench in an outstretched arm, a long tether cable from the belt.

MOTION over the 8 frames: Slowly tumbling: the body rotates about 45 degrees over the 8 frames, the free arm and legs flail softly, the tether cable snakes and curls differently each frame.
```

### `anim_crew_military.png` — реф [ship_crew_static.png](../../art/sprites/ref/ship_crew_static.png)

```
Make ONE image: an animation sprite sheet for a 2D top-down space game.

LAYOUT (strict):
- A grid of 4 columns x 2 rows = 8 equal SQUARE cells, wide 2:1 image.
- Thin straight white lines between the cells. Nothing crosses a line.
- Read order: left to right, top row first. Frame 1 is top-left, frame 8 is bottom-right.
- In every cell the subject is centred at the same spot and at the same size, filling about 70% of the cell, never touching the cell edges.
- Background in every cell: flat solid pure green #00FF00, no gradient, no shadow, no glow spilling onto the green.

ANIMATION (the most important part):
- The 8 frames are ONE continuous seamless LOOP: frame 8 flows straight back into frame 1.
- The subject is the SAME object in every frame: same design, same colours, same size, same line work. Only the motion described below changes, a little from each frame to the next (small steps, like a real animation, not 8 different pictures).
- Seen strictly from directly above.

STYLE: the same graphic-novel look as the attached reference image: ink outlines, muted painterly colours, soft film grain.

No text, no numbers, no labels, no frame around the whole image.

SUBJECT: Soviet military cosmonaut: dark olive-green armoured suit with heavy shoulders and a red star on the chest, mirrored gold visor (no face), standing at attention, saluting.

MOTION over the 8 frames: Holds the salute, floating: the whole body drifts and sways slightly, the free arm swings a little, the knees bend and straighten a bit, a small red light on the helmet blinks on frames 1 and 5.
```

---

## 12. Анимации: остальное (камни, груз, убежище, маяк, ракета)

Как раздел 11, но **16 кадров**: одна картинка 4 × 4, или анимированный GIF с 16 кадрами на
зелёном фоне (как ты прислал в прошлый раз, это тоже подходит). Клади в `art/sprites/raw/` под
именем из заголовка: `.gif`, или `.png` с сеткой в имени (`anim_crate_4x4.png`), скрипт `tools/art/anim_frames.py` сам режет кадры.

### `anim_rock_metal_1` — реф [sprite_sheet_ship_crew.png](../../art/sprites/raw/sprite_sheet_ship_crew.png)

```
Make ONE image: an animation sprite sheet for a 2D top-down space game.

LAYOUT (strict):
- A grid of 4 columns x 4 rows = 16 equal SQUARE cells, a square image.
- Thin straight white lines between the cells. Nothing crosses a line.
- Read order: left to right, top row first. Frame 1 is top-left, frame 16 is bottom-right.
- In every cell the subject is centred at the same spot and at the same size, filling about 70% of the cell, never touching the cell edges.
- Background in every cell: flat solid pure green #00FF00, no gradient, no shadow, no glow spilling onto the green.

ANIMATION (the most important part):
- The 16 frames are ONE continuous seamless LOOP: frame 16 flows straight back into frame 1.
- The subject is the SAME object in every frame: same design, same colours, same size, same line work. Only the motion described below changes, a little from each frame to the next (small steps, like a real animation, not 8 different pictures).
- Seen strictly from directly above.

STYLE: the same graphic-novel look as the attached reference image: ink outlines, muted painterly colours, soft film grain.

No text, no numbers, no labels, no frame around the whole image.

SUBJECT: Corrosive anomaly: a drifting cloud of acid dust with a dense swirling core, in neutral grey and white only (the game tints it by level).

MOTION over the 16 frames: The swirl turns slowly clockwise (about 45 degrees over the 16 frames, so the loop is seamless), puffs on the edge boil and drift outward and fade, new puffs appear; the core gently brightens on frames 3-4 and dims again.

SUBJECT: A dark metallic asteroid made of twisted station wreckage and iron rock.

MOTION over the 16 frames: It tumbles slowly in place: rotates a full 360 degrees over the 16 frames so the loop is seamless, light catches the metal edges as it turns.
```

### `anim_rock_metal_2` — реф [sprite_sheet_ship_crew.png](../../art/sprites/raw/sprite_sheet_ship_crew.png)

```
Make ONE image: an animation sprite sheet for a 2D top-down space game.

LAYOUT (strict):
- A grid of 4 columns x 4 rows = 16 equal SQUARE cells, a square image.
- Thin straight white lines between the cells. Nothing crosses a line.
- Read order: left to right, top row first. Frame 1 is top-left, frame 16 is bottom-right.
- In every cell the subject is centred at the same spot and at the same size, filling about 70% of the cell, never touching the cell edges.
- Background in every cell: flat solid pure green #00FF00, no gradient, no shadow, no glow spilling onto the green.

ANIMATION (the most important part):
- The 16 frames are ONE continuous seamless LOOP: frame 16 flows straight back into frame 1.
- The subject is the SAME object in every frame: same design, same colours, same size, same line work. Only the motion described below changes, a little from each frame to the next (small steps, like a real animation, not 8 different pictures).
- Seen strictly from directly above.

STYLE: the same graphic-novel look as the attached reference image: ink outlines, muted painterly colours, soft film grain.

No text, no numbers, no labels, no frame around the whole image.

SUBJECT: Corrosive anomaly: a drifting cloud of acid dust with a dense swirling core, in neutral grey and white only (the game tints it by level).

MOTION over the 16 frames: The swirl turns slowly clockwise (about 45 degrees over the 16 frames, so the loop is seamless), puffs on the edge boil and drift outward and fade, new puffs appear; the core gently brightens on frames 3-4 and dims again.

SUBJECT: A second, differently shaped dark metallic asteroid of wreckage and iron.

MOTION over the 16 frames: It tumbles slowly: a full 360-degree turn over the 16 frames, seamless loop.
```

### `anim_rock_mineral_1` — реф [sprite_sheet_ship_crew.png](../../art/sprites/raw/sprite_sheet_ship_crew.png)

```
Make ONE image: an animation sprite sheet for a 2D top-down space game.

LAYOUT (strict):
- A grid of 4 columns x 4 rows = 16 equal SQUARE cells, a square image.
- Thin straight white lines between the cells. Nothing crosses a line.
- Read order: left to right, top row first. Frame 1 is top-left, frame 16 is bottom-right.
- In every cell the subject is centred at the same spot and at the same size, filling about 70% of the cell, never touching the cell edges.
- Background in every cell: flat solid pure green #00FF00, no gradient, no shadow, no glow spilling onto the green.

ANIMATION (the most important part):
- The 16 frames are ONE continuous seamless LOOP: frame 16 flows straight back into frame 1.
- The subject is the SAME object in every frame: same design, same colours, same size, same line work. Only the motion described below changes, a little from each frame to the next (small steps, like a real animation, not 8 different pictures).
- Seen strictly from directly above.

STYLE: the same graphic-novel look as the attached reference image: ink outlines, muted painterly colours, soft film grain.

No text, no numbers, no labels, no frame around the whole image.

SUBJECT: Corrosive anomaly: a drifting cloud of acid dust with a dense swirling core, in neutral grey and white only (the game tints it by level).

MOTION over the 16 frames: The swirl turns slowly clockwise (about 45 degrees over the 16 frames, so the loop is seamless), puffs on the edge boil and drift outward and fade, new puffs appear; the core gently brightens on frames 3-4 and dims again.

SUBJECT: A grey-brown stony asteroid with pale mineral veins.

MOTION over the 16 frames: It tumbles slowly: a full 360-degree turn over the 16 frames, the mineral veins glint faintly as they face the light.
```

### `anim_rock_mineral_2` — реф [sprite_sheet_ship_crew.png](../../art/sprites/raw/sprite_sheet_ship_crew.png)

```
Make ONE image: an animation sprite sheet for a 2D top-down space game.

LAYOUT (strict):
- A grid of 4 columns x 4 rows = 16 equal SQUARE cells, a square image.
- Thin straight white lines between the cells. Nothing crosses a line.
- Read order: left to right, top row first. Frame 1 is top-left, frame 16 is bottom-right.
- In every cell the subject is centred at the same spot and at the same size, filling about 70% of the cell, never touching the cell edges.
- Background in every cell: flat solid pure green #00FF00, no gradient, no shadow, no glow spilling onto the green.

ANIMATION (the most important part):
- The 16 frames are ONE continuous seamless LOOP: frame 16 flows straight back into frame 1.
- The subject is the SAME object in every frame: same design, same colours, same size, same line work. Only the motion described below changes, a little from each frame to the next (small steps, like a real animation, not 8 different pictures).
- Seen strictly from directly above.

STYLE: the same graphic-novel look as the attached reference image: ink outlines, muted painterly colours, soft film grain.

No text, no numbers, no labels, no frame around the whole image.

SUBJECT: Corrosive anomaly: a drifting cloud of acid dust with a dense swirling core, in neutral grey and white only (the game tints it by level).

MOTION over the 16 frames: The swirl turns slowly clockwise (about 45 degrees over the 16 frames, so the loop is seamless), puffs on the edge boil and drift outward and fade, new puffs appear; the core gently brightens on frames 3-4 and dims again.

SUBJECT: A second, differently shaped grey-brown stony asteroid with pale mineral veins.

MOTION over the 16 frames: It tumbles slowly: a full 360-degree turn over the 16 frames, seamless loop.
```

### `anim_crate` — реф [codex_crate.jpg](../../art/ui/raw/codex_crate.jpg)

```
Make ONE image: an animation sprite sheet for a 2D top-down space game.

LAYOUT (strict):
- A grid of 4 columns x 4 rows = 16 equal SQUARE cells, a square image.
- Thin straight white lines between the cells. Nothing crosses a line.
- Read order: left to right, top row first. Frame 1 is top-left, frame 16 is bottom-right.
- In every cell the subject is centred at the same spot and at the same size, filling about 70% of the cell, never touching the cell edges.
- Background in every cell: flat solid pure green #00FF00, no gradient, no shadow, no glow spilling onto the green.

ANIMATION (the most important part):
- The 16 frames are ONE continuous seamless LOOP: frame 16 flows straight back into frame 1.
- The subject is the SAME object in every frame: same design, same colours, same size, same line work. Only the motion described below changes, a little from each frame to the next (small steps, like a real animation, not 8 different pictures).
- Seen strictly from directly above.

STYLE: the same graphic-novel look as the attached reference image: ink outlines, muted painterly colours, soft film grain.

No text, no numbers, no labels, no frame around the whole image.

SUBJECT: Corrosive anomaly: a drifting cloud of acid dust with a dense swirling core, in neutral grey and white only (the game tints it by level).

MOTION over the 16 frames: The swirl turns slowly clockwise (about 45 degrees over the 16 frames, so the loop is seamless), puffs on the edge boil and drift outward and fade, new puffs appear; the core gently brightens on frames 3-4 and dims again.

SUBJECT: The olive-grey Soviet metal cargo crate, seen from directly above, the lid visible, a small amber status light on the lid.

MOTION over the 16 frames: It drifts and rotates slowly (about 45 degrees over the 16 frames), the amber light blinks: on in frames 1-3 and 9-11, off otherwise.
```

### `anim_crate_gold` — реф [codex_crate.jpg](../../art/ui/raw/codex_crate.jpg)

```
Make ONE image: an animation sprite sheet for a 2D top-down space game.

LAYOUT (strict):
- A grid of 4 columns x 4 rows = 16 equal SQUARE cells, a square image.
- Thin straight white lines between the cells. Nothing crosses a line.
- Read order: left to right, top row first. Frame 1 is top-left, frame 16 is bottom-right.
- In every cell the subject is centred at the same spot and at the same size, filling about 70% of the cell, never touching the cell edges.
- Background in every cell: flat solid pure green #00FF00, no gradient, no shadow, no glow spilling onto the green.

ANIMATION (the most important part):
- The 16 frames are ONE continuous seamless LOOP: frame 16 flows straight back into frame 1.
- The subject is the SAME object in every frame: same design, same colours, same size, same line work. Only the motion described below changes, a little from each frame to the next (small steps, like a real animation, not 8 different pictures).
- Seen strictly from directly above.

STYLE: the same graphic-novel look as the attached reference image: ink outlines, muted painterly colours, soft film grain.

No text, no numbers, no labels, no frame around the whole image.

SUBJECT: Corrosive anomaly: a drifting cloud of acid dust with a dense swirling core, in neutral grey and white only (the game tints it by level).

MOTION over the 16 frames: The swirl turns slowly clockwise (about 45 degrees over the 16 frames, so the loop is seamless), puffs on the edge boil and drift outward and fade, new puffs appear; the core gently brightens on frames 3-4 and dims again.

SUBJECT: The same cargo crate painted in worn gold and amber, seen from directly above.

MOTION over the 16 frames: It rotates slowly (about 45 degrees over the 16 frames) and a soft golden shine sweeps across the lid from left to right once per loop.
```

### `anim_storage` — реф [codex_storage.jpg](../../art/ui/raw/codex_storage.jpg)

```
Make ONE image: an animation sprite sheet for a 2D top-down space game.

LAYOUT (strict):
- A grid of 4 columns x 4 rows = 16 equal SQUARE cells, a square image.
- Thin straight white lines between the cells. Nothing crosses a line.
- Read order: left to right, top row first. Frame 1 is top-left, frame 16 is bottom-right.
- In every cell the subject is centred at the same spot and at the same size, filling about 70% of the cell, never touching the cell edges.
- Background in every cell: flat solid pure green #00FF00, no gradient, no shadow, no glow spilling onto the green.

ANIMATION (the most important part):
- The 16 frames are ONE continuous seamless LOOP: frame 16 flows straight back into frame 1.
- The subject is the SAME object in every frame: same design, same colours, same size, same line work. Only the motion described below changes, a little from each frame to the next (small steps, like a real animation, not 8 different pictures).
- Seen strictly from directly above.

STYLE: the same graphic-novel look as the attached reference image: ink outlines, muted painterly colours, soft film grain.

No text, no numbers, no labels, no frame around the whole image.

SUBJECT: Corrosive anomaly: a drifting cloud of acid dust with a dense swirling core, in neutral grey and white only (the game tints it by level).

MOTION over the 16 frames: The swirl turns slowly clockwise (about 45 degrees over the 16 frames, so the loop is seamless), puffs on the edge boil and drift outward and fade, new puffs appear; the core gently brightens on frames 3-4 and dims again.

SUBJECT: The cylindrical Soviet storage module, seen from directly above, the round hatch on top, amber light leaking from a seam.

MOTION over the 16 frames: It rotates slowly (about 45 degrees over the 16 frames), the amber seam light pulses softly, brightest on frame 8.
```

### `anim_fuel` — реф [codex_fuel.jpg](../../art/ui/raw/codex_fuel.jpg)

```
Make ONE image: an animation sprite sheet for a 2D top-down space game.

LAYOUT (strict):
- A grid of 4 columns x 4 rows = 16 equal SQUARE cells, a square image.
- Thin straight white lines between the cells. Nothing crosses a line.
- Read order: left to right, top row first. Frame 1 is top-left, frame 16 is bottom-right.
- In every cell the subject is centred at the same spot and at the same size, filling about 70% of the cell, never touching the cell edges.
- Background in every cell: flat solid pure green #00FF00, no gradient, no shadow, no glow spilling onto the green.

ANIMATION (the most important part):
- The 16 frames are ONE continuous seamless LOOP: frame 16 flows straight back into frame 1.
- The subject is the SAME object in every frame: same design, same colours, same size, same line work. Only the motion described below changes, a little from each frame to the next (small steps, like a real animation, not 8 different pictures).
- Seen strictly from directly above.

STYLE: the same graphic-novel look as the attached reference image: ink outlines, muted painterly colours, soft film grain.

No text, no numbers, no labels, no frame around the whole image.

SUBJECT: Corrosive anomaly: a drifting cloud of acid dust with a dense swirling core, in neutral grey and white only (the game tints it by level).

MOTION over the 16 frames: The swirl turns slowly clockwise (about 45 degrees over the 16 frames, so the loop is seamless), puffs on the edge boil and drift outward and fade, new puffs appear; the core gently brightens on frames 3-4 and dims again.

SUBJECT: The spherical Soviet fuel tank, white insulated skin with dark seams and two valves, seen from directly above.

MOTION over the 16 frames: It turns slowly (about 45 degrees over the 16 frames), a thin wisp of vapour leaks from one valve and curls differently each frame.
```

### `anim_shelter` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
Make ONE image: an animation sprite sheet for a 2D top-down space game.

LAYOUT (strict):
- A grid of 4 columns x 4 rows = 16 equal SQUARE cells, a square image.
- Thin straight white lines between the cells. Nothing crosses a line.
- Read order: left to right, top row first. Frame 1 is top-left, frame 16 is bottom-right.
- In every cell the subject is centred at the same spot and at the same size, filling about 70% of the cell, never touching the cell edges.
- Background in every cell: flat solid pure green #00FF00, no gradient, no shadow, no glow spilling onto the green.

ANIMATION (the most important part):
- The 16 frames are ONE continuous seamless LOOP: frame 16 flows straight back into frame 1.
- The subject is the SAME object in every frame: same design, same colours, same size, same line work. Only the motion described below changes, a little from each frame to the next (small steps, like a real animation, not 8 different pictures).
- Seen strictly from directly above.

STYLE: the same graphic-novel look as the attached reference image: ink outlines, muted painterly colours, soft film grain.

No text, no numbers, no labels, no frame around the whole image.

SUBJECT: Corrosive anomaly: a drifting cloud of acid dust with a dense swirling core, in neutral grey and white only (the game tints it by level).

MOTION over the 16 frames: The swirl turns slowly clockwise (about 45 degrees over the 16 frames, so the loop is seamless), puffs on the edge boil and drift outward and fade, new puffs appear; the core gently brightens on frames 3-4 and dims again.

SUBJECT: A small round shelter dome from a space station, seen from directly above: a dark metal ring with a softly glowing amber dome in the middle.

MOTION over the 16 frames: The amber dome breathes: glows brighter over frames 1-8 and dimmer over 9-16; small lights on the ring chase around it clockwise.
```

### `anim_beacon` — реф [p08.jpg](../../art/intro/raw/p08.jpg)

```
Make ONE image: an animation sprite sheet for a 2D top-down space game.

LAYOUT (strict):
- A grid of 4 columns x 4 rows = 16 equal SQUARE cells, a square image.
- Thin straight white lines between the cells. Nothing crosses a line.
- Read order: left to right, top row first. Frame 1 is top-left, frame 16 is bottom-right.
- In every cell the subject is centred at the same spot and at the same size, filling about 70% of the cell, never touching the cell edges.
- Background in every cell: flat solid pure green #00FF00, no gradient, no shadow, no glow spilling onto the green.

ANIMATION (the most important part):
- The 16 frames are ONE continuous seamless LOOP: frame 16 flows straight back into frame 1.
- The subject is the SAME object in every frame: same design, same colours, same size, same line work. Only the motion described below changes, a little from each frame to the next (small steps, like a real animation, not 8 different pictures).
- Seen strictly from directly above.

STYLE: the same graphic-novel look as the attached reference image: ink outlines, muted painterly colours, soft film grain.

No text, no numbers, no labels, no frame around the whole image.

SUBJECT: Corrosive anomaly: a drifting cloud of acid dust with a dense swirling core, in neutral grey and white only (the game tints it by level).

MOTION over the 16 frames: The swirl turns slowly clockwise (about 45 degrees over the 16 frames, so the loop is seamless), puffs on the edge boil and drift outward and fade, new puffs appear; the core gently brightens on frames 3-4 and dims again.

SUBJECT: A small extraction beacon seen from directly above: a metal tripod frame with a bright warm amber lamp in the centre.

MOTION over the 16 frames: The lamp pulses like a lighthouse: a bright flash with a soft ring on frame 1, fading through frame 8, dim on 9-14, rising on 15-16.
```

### `anim_rocket` — реф [sprite_sheet_ship_crew.png](../../art/sprites/raw/sprite_sheet_ship_crew.png)

```
Make ONE image: an animation sprite sheet for a 2D top-down space game.

LAYOUT (strict):
- A grid of 4 columns x 4 rows = 16 equal SQUARE cells, a square image.
- Thin straight white lines between the cells. Nothing crosses a line.
- Read order: left to right, top row first. Frame 1 is top-left, frame 16 is bottom-right.
- In every cell the subject is centred at the same spot and at the same size, filling about 70% of the cell, never touching the cell edges.
- Background in every cell: flat solid pure green #00FF00, no gradient, no shadow, no glow spilling onto the green.

ANIMATION (the most important part):
- The 16 frames are ONE continuous seamless LOOP: frame 16 flows straight back into frame 1.
- The subject is the SAME object in every frame: same design, same colours, same size, same line work. Only the motion described below changes, a little from each frame to the next (small steps, like a real animation, not 8 different pictures).
- Seen strictly from directly above.

STYLE: the same graphic-novel look as the attached reference image: ink outlines, muted painterly colours, soft film grain.

No text, no numbers, no labels, no frame around the whole image.

SUBJECT: Corrosive anomaly: a drifting cloud of acid dust with a dense swirling core, in neutral grey and white only (the game tints it by level).

MOTION over the 16 frames: The swirl turns slowly clockwise (about 45 degrees over the 16 frames, so the loop is seamless), puffs on the edge boil and drift outward and fade, new puffs appear; the core gently brightens on frames 3-4 and dims again.

SUBJECT: A small Soviet missile seen from directly above, nose pointing to the right, white body with a red tip and four small fins, an engine flame at the back.

MOTION over the 16 frames: The body stays still; only the engine flame flickers: longer and shorter, changing shape every frame.
```

---

## Уже готово (в игре)

`sector07`, `sector08`, `sector10`, `sector11`, `sector12`, `codex_fuel`, `sprite_buran`;
`menu.jpg`, `crew_commander / medic / scientist / mechanic / military.jpg`, `clear.jpg`,
`lost.jpg`, `sector01–04.jpg`, `codex_corrosive / electric / radiation / magnetic / rift.jpg`,
`layers/menu_shuttle.jpg` (фон меню без шаттла теперь `menu.jpg`). Слои меню: `tools/art/menu_layers.py`.
