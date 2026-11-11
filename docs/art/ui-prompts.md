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

## Уже готово (в игре)

`menu.jpg`, `crew_commander / medic / scientist / mechanic / military.jpg`, `clear.jpg`,
`lost.jpg`, `sector01–04.jpg`, `codex_corrosive / electric / radiation / magnetic / rift.jpg`,
`layers/menu_shuttle.jpg` (фон меню без шаттла теперь `menu.jpg`). Слои меню: `tools/art/menu_layers.py`.
