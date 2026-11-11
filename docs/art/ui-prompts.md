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
выйдет плакат с подписями и врезками (неудачные попытки лежат в `art/ui/raw/rejected/`).

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

## 3. Слои стартового экрана (чтобы «Буран» покачивался)

Нужно две картинки, обе делаются **правкой** готового фона меню. В новом чате приложи
[art/ui/raw/menu.jpg](../../art/ui/raw/menu.jpg) (не образец из интро, а сам фон меню).
Совмещу и вырежу фон сам. Главное, чтобы кадр не сдвинулся и не поменял размер.

### `menu_clean.jpg` — фон без шаттла (реф: menu.jpg)

```
Edit the attached image. Remove the white space shuttle completely and fill the place where it
was with the same dark starry space and faint dust, matching the surroundings. Where the shuttle
covered the end of the ring station, continue the ring naturally. Keep everything else exactly
as it is: same framing, same size, same position of the station and the glowing sphere, same
colours, same stars. Do not add anything. No text, no frame.
```

### `menu_shuttle.jpg` — только шаттл на зелёном (реф: menu.jpg)

```
Edit the attached image. Keep only the white space shuttle, exactly as it is drawn: same size,
same position in the frame, same angle, same outlines and details. Replace everything else
(space, stars, the ring station, the glowing sphere, the dust) with a flat solid pure green
background #00FF00, no gradient, no shadow, no glow. The shuttle must not touch or overlap
anything green-coloured. No text, no frame.
```

Если в `menu_shuttle` на шаттле появилась зелень по краям или фон не ровно зелёный,
перегенерируй. Сохрани обе в `art/ui/raw/`.

---

## Уже готово (в игре)

`menu.jpg`, `crew_commander / medic / scientist / mechanic / military.jpg`, `clear.jpg`,
`lost.jpg`.
