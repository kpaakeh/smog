# Полный справочник содержимого 0.0.4

> Срез документации: 4 ноября 2026, версия **0.0.4**. Описывает существующее содержимое; наличие материала в каталоге не означает доступность его производства в Медной мастерской.


Источник: расширенный производственный каталог `build/resources/main/smog.content.json`, исходные данные `src/main/content/`. Генератор добавляет материалы, формы и детали к исходному каталогу. Декоративные рецепты вынесены в отдельный справочник. Это снимок данных, а не перечень всех доступных в первой главе цепочек. Статические `multiblocks` — схемы-примеры; фактические размеры плавильни задаёт `complex_multiblocks`.

SHA-256 снимка: `4acc5959c8724a1957ad8690f65b77ca0947733a061090db90d65e719fa361c4`.

## Как читать параметры

`input`/`output` — вход и выход; `count`/`output_count` — число предметов; `ticks` — игровые тики (20 в секунду при нормальной скорости); `heat`/`working_heat` — температура; `water`, `material_units` и другие жидкостные объёмы — внутренние единицы (81000 на ведро). `#` перед ID означает тег, а не отдельный предмет. Профиль материала действует только в указанной роли детали. `cast` различает литую заготовку и готовую деталь. Все поля сохранены, чтобы не терять условия и численные ограничения.

## Состав снимка

| Раздел | Записей верхнего уровня |
|---|---:|
| Теги (`tags`) | 19 |
| Базовые блоки (`blocks`) | 25 |
| Рудные семейства и генерация (`ores`) | 13 |
| Оборудование (`machines`) | 14 |
| Схемы-примеры структур (`multiblocks`) | 5 |
| Жидкости (`fluids`) | 40 |
| Предметы (`items`) | 880 |
| Семейства сплавов (`alloys`) | 18 |
| Дополнительные металлические семейства (`metals`) | 2 |
| Неметаллические залежи (`deposits`) | 7 |
| Подземные ресурсы (`underground_resources`) | 2 |
| Политика переработки (`resource_policy`) | 4 |
| Правила переменных структур (`complex_multiblocks`) | 2 |
| Переплавка концентратов (`concentrate_smelting`) | 3 |
| Промывка (`washing`) | 8 |
| Формование керамики (`pottery`) | 17 |
| Начальные и сборочные рецепты (`crafting`) | 61 |
| Обжиг (`firing`) | 17 |
| Дополнительные переводы (`translations`) | 2 |
| Сменные модули (`workshop_modules`) | 1 |
| Дополнительные литейные формы (`casting_forms`) | 0 |
| Условия обработки металлов (`casting_metals`) | 35 |
| Жидкое топливо (`smeltery_fuels`) | 2 |
| Рецепты сплавов (`alloy_recipes`) | 10 |
| Материалы инструментов (`tool_materials`) | 41 |
| Формы деталей (`tool_forms`) | 14 |
| Конструкции инструментов (`tool_templates`) | 10 |
| Каждая деталь инструмента (`tool_parts`) | 787 |

## Теги

### smog:sulfur_sources

```json
{
  "registry": "item",
  "id": "smog:sulfur_sources",
  "values": [
    "minecraft:sulfur"
  ]
}
```

### smog:sulfur_sources

```json
{
  "registry": "block",
  "id": "smog:sulfur_sources",
  "values": [
    "minecraft:sulfur"
  ]
}
```

### smog:hearth_floor

```json
{
  "registry": "block",
  "id": "smog:hearth_floor",
  "values": [
    "minecraft:stone",
    "minecraft:cobblestone",
    "minecraft:bricks",
    "smog:firebrick_casing",
    "smog:firebrick_tiles",
    "smog:carved_firebrick"
  ]
}
```

### smog:hearth_floor

```json
{
  "registry": "item",
  "id": "smog:hearth_floor",
  "values": [
    "minecraft:stone",
    "minecraft:cobblestone",
    "minecraft:bricks",
    "smog:firebrick_casing",
    "smog:firebrick_tiles",
    "smog:carved_firebrick"
  ]
}
```

### smog:copper_masonry

```json
{
  "registry": "block",
  "id": "smog:copper_masonry",
  "values": [
    "minecraft:bricks",
    "smog:firebrick_casing",
    "smog:firebrick_tiles",
    "smog:carved_firebrick",
    "smog:smeltery_glass"
  ]
}
```

### smog:copper_masonry

```json
{
  "registry": "item",
  "id": "smog:copper_masonry",
  "values": [
    "minecraft:bricks",
    "smog:firebrick_casing",
    "smog:firebrick_tiles",
    "smog:carved_firebrick"
  ]
}
```

### c:unfired_ceramics

```json
{
  "registry": "item",
  "id": "c:unfired_ceramics",
  "values": [
    "smog:unfired_crucible",
    "smog:unfired_large_crucible",
    "smog:unfired_firebrick",
    "smog:unfired_ingot_mold",
    "smog:unfired_plate_mold",
    "smog:unfired_fitting_mold",
    "smog:unfired_pipe_mold",
    "smog:unfired_pickaxe_mold",
    "smog:unfired_axe_mold",
    "smog:unfired_shovel_mold",
    "smog:unfired_hoe_mold",
    "smog:unfired_hammer_mold",
    "smog:unfired_saw_mold",
    "smog:unfired_sword_mold",
    "smog:unfired_katana_mold",
    "smog:unfired_machete_mold",
    "smog:unfired_sickle_mold"
  ]
}
```

### smog:oil_seeds

```json
{
  "registry": "item",
  "id": "smog:oil_seeds",
  "values": [
    "minecraft:wheat_seeds",
    "minecraft:beetroot_seeds",
    "minecraft:pumpkin_seeds",
    "minecraft:melon_seeds",
    "minecraft:torchflower_seeds"
  ]
}
```

### minecraft:pickaxes

```json
{
  "registry": "item",
  "id": "minecraft:pickaxes",
  "values": [
    "smog:modular_pickaxe",
    "smog:modular_hammer"
  ]
}
```

### minecraft:axes

```json
{
  "registry": "item",
  "id": "minecraft:axes",
  "values": [
    "smog:modular_axe"
  ]
}
```

### minecraft:shovels

```json
{
  "registry": "item",
  "id": "minecraft:shovels",
  "values": [
    "smog:modular_shovel"
  ]
}
```

### minecraft:hoes

```json
{
  "registry": "item",
  "id": "minecraft:hoes",
  "values": [
    "smog:modular_hoe"
  ]
}
```

### minecraft:enchantable/mining

```json
{
  "registry": "item",
  "id": "minecraft:enchantable/mining",
  "values": [
    "smog:modular_pickaxe",
    "smog:modular_axe",
    "smog:modular_shovel",
    "smog:modular_hoe",
    "smog:modular_hammer"
  ]
}
```

### minecraft:enchantable/mining_loot

```json
{
  "registry": "item",
  "id": "minecraft:enchantable/mining_loot",
  "values": [
    "smog:modular_pickaxe",
    "smog:modular_axe",
    "smog:modular_shovel",
    "smog:modular_hoe",
    "smog:modular_hammer"
  ]
}
```

### minecraft:enchantable/durability

```json
{
  "registry": "item",
  "id": "minecraft:enchantable/durability",
  "values": [
    "smog:modular_pickaxe",
    "smog:modular_axe",
    "smog:modular_shovel",
    "smog:modular_hoe",
    "smog:modular_hammer"
  ]
}
```

### smog:press_frames

```json
{
  "registry": "block",
  "id": "smog:press_frames",
  "values": [
    "smog:press_frame",
    "smog:bronze_drive"
  ]
}
```

### smog:press_baskets

```json
{
  "registry": "block",
  "id": "smog:press_baskets",
  "values": [
    "smog:press_basket",
    "smog:fiber_press_basket"
  ]
}
```

### smog:fiber_sources

```json
{
  "registry": "item",
  "id": "smog:fiber_sources",
  "values": [
    "minecraft:wheat",
    "minecraft:vine"
  ]
}
```

### smog:smeltery_heaters

```json
{
  "registry": "block",
  "id": "smog:smeltery_heaters",
  "values": [
    "smog:solid_firebox",
    "smog:smeltery_fuel_tank"
  ]
}
```


## Базовые блоки

### ceramic_crucible

```json
{
  "name": "ceramic_crucible",
  "display": "Ceramic Crucible",
  "display_ru": "Керамический тигель",
  "hardness": 2,
  "mining_level": "wood",
  "model": "smog:block/ceramic_crucible",
  "texture": "minecraft:block/bricks",
  "tab": "copper_age",
  "hollow_crucible": true
}
```

### large_ceramic_crucible

```json
{
  "name": "large_ceramic_crucible",
  "display": "Large Ceramic Crucible",
  "display_ru": "Увеличенный керамический тигель",
  "hardness": 2,
  "mining_level": "wood",
  "model": "smog:block/large_ceramic_crucible",
  "texture": "minecraft:block/bricks",
  "tab": "copper_age",
  "hollow_crucible": true
}
```

### firebrick_casing

```json
{
  "name": "firebrick_casing",
  "display": "Refractory Masonry",
  "display_ru": "Футерованная кладка",
  "hardness": 2,
  "mining_level": "wood",
  "model": "smog:block/firebrick_casing",
  "texture": "minecraft:block/bricks",
  "tab": "copper_age"
}
```

### casting_spout

```json
{
  "name": "casting_spout",
  "display": "Molten Metal Port",
  "display_ru": "Жидкостный порт",
  "hardness": 2,
  "mining_level": "wood",
  "model": "smog:block/casting_spout",
  "texture": "minecraft:block/bricks",
  "tab": "copper_age",
  "horizontal": true
}
```

### air_intake

```json
{
  "name": "air_intake",
  "display": "Air Port",
  "display_ru": "Воздушный порт",
  "hardness": 2,
  "mining_level": "stone",
  "model": "smog:block/air_intake",
  "texture": "minecraft:block/bricks",
  "tab": "copper_age",
  "horizontal": true
}
```

### kiln_vent

```json
{
  "name": "kiln_vent",
  "display": "Kiln Vent",
  "display_ru": "Печная вытяжка",
  "hardness": 2,
  "mining_level": "wood",
  "model": "smog:block/kiln_vent",
  "texture": "minecraft:block/bricks",
  "tab": "copper_age"
}
```

### casting_channel

```json
{
  "name": "casting_channel",
  "display": "Casting Channel",
  "display_ru": "Литейный желоб",
  "hardness": 2,
  "mining_level": "wood",
  "model": "smog:block/casting_channel",
  "texture": "minecraft:block/bricks",
  "tab": "copper_age",
  "horizontal": true
}
```

### solid_firebox

```json
{
  "name": "solid_firebox",
  "display": "Solid Fuel Firebox",
  "display_ru": "Твердотопливная топка",
  "hardness": 2,
  "mining_level": "wood",
  "model": "smog:block/solid_firebox",
  "texture": "minecraft:block/furnace_front",
  "tab": "copper_age",
  "horizontal": true
}
```

### casting_faucet

```json
{
  "name": "casting_faucet",
  "display": "Casting Faucet",
  "display_ru": "Литейный краник",
  "hardness": 2,
  "mining_level": "stone",
  "model": "smog:block/casting_faucet",
  "texture": "minecraft:block/copper_block",
  "horizontal": true,
  "valve": true,
  "tab": "copper_age"
}
```

### air_nozzle

```json
{
  "name": "air_nozzle",
  "display": "Air Nozzle",
  "display_ru": "Воздушная насадка",
  "hardness": 2,
  "mining_level": "stone",
  "model": "smog:block/air_nozzle",
  "texture": "minecraft:block/copper_block",
  "horizontal": true,
  "valve": true,
  "tab": "copper_age"
}
```

### washing_section

```json
{
  "name": "washing_section",
  "display": "Washing Channel",
  "display_ru": "Секция промывочного лотка",
  "hardness": 2,
  "mining_level": "wood",
  "horizontal": true,
  "model": "smog:block/washing_section",
  "texture": "minecraft:block/oak_planks",
  "tab": "copper_age"
}
```

### washing_sieve

```json
{
  "name": "washing_sieve",
  "display": "Washing Sieve",
  "display_ru": "Промывочное сито",
  "hardness": 2,
  "mining_level": "wood",
  "horizontal": true,
  "model": "smog:block/washing_sieve",
  "texture": "minecraft:block/oak_planks",
  "tab": "copper_age"
}
```

### workshop_support

```json
{
  "name": "workshop_support",
  "display": "Workshop Support",
  "display_ru": "Опора мастерской",
  "hardness": 2,
  "mining_level": "wood",
  "horizontal": true,
  "model": "smog:block/workshop_support",
  "texture": "minecraft:block/oak_planks",
  "tab": "copper_age"
}
```

### press_frame

```json
{
  "name": "press_frame",
  "display": "Press Frame",
  "display_ru": "Рама пресса",
  "hardness": 2,
  "mining_level": "wood",
  "horizontal": true,
  "model": "smog:block/press_frame",
  "texture": "minecraft:block/oak_planks",
  "tab": "copper_age"
}
```

### press_basket

```json
{
  "name": "press_basket",
  "display": "Press Basket",
  "display_ru": "Корзина пресса",
  "hardness": 2,
  "mining_level": "wood",
  "horizontal": true,
  "model": "smog:block/press_basket",
  "texture": "minecraft:block/oak_planks",
  "tab": "copper_age"
}
```

### firebrick_tiles

```json
{
  "name": "firebrick_tiles",
  "display": "Fireclay Tiles",
  "display_ru": "Огнеупорные изразцы",
  "hardness": 2,
  "mining_level": "wood",
  "model": "smog:block/firebrick_tiles",
  "texture": "smog:block/firebrick_tiles",
  "tab": "copper_age"
}
```

### carved_firebrick

```json
{
  "name": "carved_firebrick",
  "display": "Carved Fireclay",
  "display_ru": "Резная огнеупорная кладка",
  "hardness": 2,
  "mining_level": "wood",
  "model": "smog:block/carved_firebrick",
  "texture": "smog:block/carved_firebrick",
  "tab": "copper_age"
}
```

### copper_water_pipe

```json
{
  "name": "copper_water_pipe",
  "display": "Copper Water Pipe",
  "display_ru": "Медная водяная труба",
  "hardness": 1.5,
  "mining_level": "wood",
  "model": "smog:block/copper_water_pipe",
  "texture": "minecraft:block/copper_block",
  "tab": "copper_age",
  "pipe": true
}
```

### water_valve

```json
{
  "name": "water_valve",
  "display": "Water Shutoff Valve",
  "display_ru": "Водяной запорный кран",
  "hardness": 1.5,
  "mining_level": "wood",
  "model": "smog:block/water_valve",
  "texture": "minecraft:block/copper_block",
  "tab": "copper_age",
  "horizontal": true,
  "valve": true
}
```

### water_outlet

```json
{
  "name": "water_outlet",
  "display": "Downward Water Outlet",
  "display_ru": "Вывод воды вниз",
  "hardness": 1.5,
  "mining_level": "wood",
  "model": "smog:block/water_outlet",
  "texture": "minecraft:block/copper_block",
  "tab": "copper_age",
  "horizontal": true,
  "valve": true
}
```

### fiber_press_basket

```json
{
  "name": "fiber_press_basket",
  "display": "Fiber Press Basket",
  "display_ru": "Волокнистая корзина пресса",
  "hardness": 2,
  "mining_level": "wood",
  "horizontal": true,
  "model": "smog:block/fiber_press_basket",
  "texture": "minecraft:block/oak_planks",
  "tab": "copper_age"
}
```

### bronze_drive

```json
{
  "name": "bronze_drive",
  "display": "Bronze Water Drive",
  "display_ru": "Бронзовый водяной привод",
  "hardness": 2,
  "mining_level": "wood",
  "horizontal": true,
  "model": "smog:block/bronze_drive",
  "texture": "minecraft:block/oak_planks",
  "tab": "copper_age"
}
```

### closed_crucible_insert

```json
{
  "name": "closed_crucible_insert",
  "display": "Closed Crucible Insert",
  "display_ru": "Закрытая тигельная вставка",
  "hardness": 3,
  "mining_level": "wood",
  "tab": "copper_age",
  "model": "smog:block/closed_crucible_insert",
  "texture": "smog:block/firebrick_casing"
}
```

### smeltery_glass

```json
{
  "name": "smeltery_glass",
  "display": "Refractory Glass",
  "display_ru": "Огнеупорное стекло",
  "tab": "copper_age",
  "texture": "minecraft:block/glass",
  "model": "smog:block/smeltery_glass",
  "hardness": 2,
  "mining_level": "wood",
  "horizontal": true
}
```

### smeltery_item_port

```json
{
  "name": "smeltery_item_port",
  "display": "Smeltery Item Input Port",
  "display_ru": "Предметный порт плавильни",
  "tab": "copper_age",
  "hardness": 3,
  "mining_level": "wood",
  "texture": "smog:block/refractory_bricks",
  "model": "smog:block/smeltery_item_port",
  "horizontal": true
}
```


## Рудные семейства и генерация

### tin

```json
{
  "name": "tin",
  "material": "Tin",
  "material_ru": "олова",
  "display_ru": "Оловянная руда",
  "color": "9FBFC9",
  "product": "ingot",
  "vein_size": 8,
  "veins_per_chunk": 5,
  "min_y": -64,
  "max_y": 96,
  "biomes": "overworld",
  "generation_profile": "accessible_vein",
  "rarity": 1,
  "mineral": "cassiterite",
  "potential_byproducts": [],
  "height_distribution": "trapezoid",
  "mining_level": "stone",
  "display": "Tin Ore",
  "texture": "smog:block/tin_ore",
  "deepslate_texture": "smog:block/deepslate_tin_ore"
}
```

### zinc

```json
{
  "name": "zinc",
  "material": "Zinc",
  "material_ru": "цинка",
  "display_ru": "Цинковая руда",
  "color": "B4B99D",
  "product": "ingot",
  "vein_size": 7,
  "veins_per_chunk": 4,
  "min_y": -64,
  "max_y": 64,
  "biomes": "overworld",
  "generation_profile": "accessible_vein",
  "rarity": 1,
  "mineral": "sphalerite",
  "potential_byproducts": [
    "germanium"
  ],
  "height_distribution": "trapezoid",
  "mining_level": "stone",
  "display": "Zinc Ore",
  "texture": "smog:block/zinc_ore",
  "deepslate_texture": "smog:block/deepslate_zinc_ore"
}
```

### lead

```json
{
  "name": "lead",
  "material": "Lead",
  "material_ru": "свинца",
  "display_ru": "Свинцовая руда",
  "color": "77748F",
  "product": "ingot",
  "veins_per_chunk": 3,
  "min_y": -64,
  "max_y": 32,
  "biomes": "overworld",
  "generation_profile": "accessible_vein",
  "rarity": 1,
  "mineral": "galena",
  "potential_byproducts": [
    "silver"
  ],
  "vein_size": 8,
  "height_distribution": "trapezoid",
  "mining_level": "iron",
  "display": "Lead Ore",
  "texture": "smog:block/lead_ore",
  "deepslate_texture": "smog:block/deepslate_lead_ore"
}
```

### nickel

```json
{
  "name": "nickel",
  "material": "Nickel",
  "material_ru": "никеля",
  "display_ru": "Никелевая руда",
  "color": "A8AA83",
  "product": "ingot",
  "veins_per_chunk": 2,
  "min_y": -64,
  "max_y": 16,
  "biomes": "overworld",
  "generation_profile": "accessible_vein",
  "rarity": 1,
  "mineral": "nickel_sulfide",
  "potential_byproducts": [
    "platinum_group"
  ],
  "vein_size": 7,
  "height_distribution": "trapezoid",
  "mining_level": "iron",
  "display": "Nickel Ore",
  "texture": "smog:block/nickel_ore",
  "deepslate_texture": "smog:block/deepslate_nickel_ore"
}
```

### bauxite

```json
{
  "name": "bauxite",
  "material": "Bauxite",
  "material_ru": "боксита",
  "display_ru": "Бокситовая руда",
  "color": "B07854",
  "product": "none",
  "veins_per_chunk": 3,
  "biomes": "overworld",
  "generation_profile": "accessible_vein",
  "rarity": 1,
  "potential_byproducts": [
    "gallium"
  ],
  "vein_size": 9,
  "min_y": -32,
  "max_y": 128,
  "height_distribution": "trapezoid",
  "mining_level": "iron",
  "display": "Bauxite Ore",
  "texture": "smog:block/bauxite_ore",
  "deepslate_texture": "smog:block/deepslate_bauxite_ore"
}
```

### manganese

```json
{
  "name": "manganese",
  "material": "Manganese",
  "material_ru": "марганца",
  "display_ru": "Марганцевая руда",
  "color": "78808C",
  "product": "ingot",
  "veins_per_chunk": 3,
  "min_y": -64,
  "max_y": 48,
  "biomes": "overworld",
  "generation_profile": "accessible_vein",
  "rarity": 1,
  "mineral": "pyrolusite",
  "potential_byproducts": [],
  "vein_size": 8,
  "height_distribution": "trapezoid",
  "mining_level": "iron",
  "display": "Manganese Ore",
  "texture": "smog:block/manganese_ore",
  "deepslate_texture": "smog:block/deepslate_manganese_ore"
}
```

### chromium

```json
{
  "name": "chromium",
  "material": "Chromium",
  "material_ru": "хрома",
  "display_ru": "Хромовая руда",
  "color": "8DAAA7",
  "product": "ingot",
  "veins_per_chunk": 2,
  "min_y": -64,
  "max_y": 16,
  "biomes": "overworld",
  "generation_profile": "accessible_vein",
  "rarity": 1,
  "mineral": "chromite",
  "potential_byproducts": [],
  "vein_size": 7,
  "height_distribution": "trapezoid",
  "mining_level": "iron",
  "display": "Chromium Ore",
  "texture": "smog:block/chromium_ore",
  "deepslate_texture": "smog:block/deepslate_chromium_ore"
}
```

### titanium

```json
{
  "name": "titanium",
  "material": "Titanium",
  "material_ru": "титана",
  "display_ru": "Титановая руда",
  "color": "8F9FB3",
  "product": "ingot",
  "veins_per_chunk": 2,
  "biomes": "overworld",
  "generation_profile": "special_vein",
  "rarity": 2,
  "mineral": "ilmenite",
  "potential_byproducts": [
    "iron",
    "vanadium"
  ],
  "vein_size": 6,
  "min_y": -64,
  "max_y": 0,
  "height_distribution": "trapezoid",
  "mining_level": "diamond",
  "display": "Titanium Ore",
  "texture": "smog:block/titanium_ore",
  "deepslate_texture": "smog:block/deepslate_titanium_ore"
}
```

### tungsten

```json
{
  "name": "tungsten",
  "material": "Tungsten",
  "material_ru": "вольфрама",
  "display_ru": "Вольфрамовая руда",
  "color": "6D7886",
  "product": "ingot",
  "vein_size": 6,
  "veins_per_chunk": 1,
  "min_y": -64,
  "max_y": -8,
  "biomes": "overworld",
  "generation_profile": "special_vein",
  "rarity": 3,
  "mineral": "scheelite",
  "potential_byproducts": [],
  "height_distribution": "trapezoid",
  "mining_level": "netherite",
  "display": "Tungsten Ore",
  "texture": "smog:block/tungsten_ore",
  "deepslate_texture": "smog:block/deepslate_tungsten_ore"
}
```

### molybdenum

```json
{
  "name": "molybdenum",
  "material": "Molybdenum",
  "material_ru": "молибдена",
  "display_ru": "Молибденовая руда",
  "color": "8E9FAB",
  "product": "ingot",
  "vein_size": 6,
  "veins_per_chunk": 1,
  "min_y": -64,
  "max_y": 0,
  "biomes": "overworld",
  "generation_profile": "special_vein",
  "rarity": 3,
  "mineral": "molybdenite",
  "potential_byproducts": [
    "rhenium"
  ],
  "height_distribution": "trapezoid",
  "mining_level": "diamond",
  "display": "Molybdenum Ore",
  "texture": "smog:block/molybdenum_ore",
  "deepslate_texture": "smog:block/deepslate_molybdenum_ore"
}
```

### uranium

```json
{
  "name": "uranium",
  "material": "Uranium",
  "material_ru": "урана",
  "display_ru": "Урановая руда",
  "color": "7F964D",
  "product": "ingot",
  "vein_size": 5,
  "veins_per_chunk": 1,
  "min_y": -64,
  "max_y": -16,
  "biomes": "overworld",
  "generation_profile": "special_vein",
  "rarity": 4,
  "potential_byproducts": [],
  "height_distribution": "trapezoid",
  "mining_level": "netherite",
  "display": "Uranium Ore",
  "texture": "smog:block/uranium_ore",
  "deepslate_texture": "smog:block/deepslate_uranium_ore"
}
```

### lithium

```json
{
  "name": "lithium",
  "material": "Lithium",
  "material_ru": "лития",
  "display_ru": "Литиевая руда",
  "color": "C3BED4",
  "product": "ingot",
  "vein_size": 6,
  "veins_per_chunk": 1,
  "min_y": -64,
  "max_y": 32,
  "biomes": "overworld",
  "generation_profile": "special_vein",
  "rarity": 2,
  "mineral": "spodumene",
  "potential_byproducts": [],
  "height_distribution": "trapezoid",
  "mining_level": "iron",
  "display": "Lithium Ore",
  "texture": "smog:block/lithium_ore",
  "deepslate_texture": "smog:block/deepslate_lithium_ore"
}
```

### rare_earth

```json
{
  "name": "rare_earth",
  "material": "Rare Earth",
  "material_ru": "редкоземельных элементов",
  "display_ru": "Редкоземельная руда",
  "color": "96A481",
  "product": "none",
  "veins_per_chunk": 1,
  "biomes": "overworld",
  "generation_profile": "special_vein",
  "rarity": 4,
  "mineral": "monazite",
  "potential_byproducts": [
    "rare_earth_elements"
  ],
  "vein_size": 5,
  "min_y": -64,
  "max_y": 16,
  "height_distribution": "trapezoid",
  "mining_level": "netherite",
  "display": "Rare Earth Ore",
  "texture": "smog:block/rare_earth_ore",
  "deepslate_texture": "smog:block/deepslate_rare_earth_ore"
}
```


## Оборудование

### copper_hearth

```json
{
  "name": "copper_hearth",
  "display": "Smeltery",
  "display_ru": "Плавильня",
  "kind": "COPPER_WORKSHOP",
  "hardness": 2,
  "capacity": 1,
  "transfer_rate": 1,
  "cost": 0,
  "work_ticks": 80,
  "model": "smog:block/copper_hearth",
  "assembled_model": "smog:block/copper_hearth",
  "texture": "minecraft:block/bricks",
  "mining_level": "wood",
  "multiblock": "smog:copper_hearth"
}
```

### casting_table

```json
{
  "name": "casting_table",
  "display": "Casting Table",
  "display_ru": "Литейный стол",
  "kind": "COPPER_WORKSHOP",
  "hardness": 2,
  "capacity": 1,
  "transfer_rate": 1,
  "cost": 0,
  "work_ticks": 80,
  "model": "smog:block/casting_table",
  "assembled_model": "smog:block/casting_table",
  "texture": "minecraft:block/bricks",
  "mining_level": "wood"
}
```

### potter_table

```json
{
  "name": "potter_table",
  "display": "Potter Table",
  "display_ru": "Стол керамиста",
  "kind": "COPPER_WORKSHOP",
  "hardness": 2,
  "capacity": 1,
  "transfer_rate": 1,
  "cost": 0,
  "work_ticks": 80,
  "model": "smog:block/potter_table",
  "assembled_model": "smog:block/potter_table",
  "texture": "minecraft:block/bricks",
  "mining_level": "wood"
}
```

### hand_bellows

```json
{
  "name": "hand_bellows",
  "display": "Hand Bellows",
  "display_ru": "Ручные меха",
  "kind": "COPPER_WORKSHOP",
  "hardness": 2,
  "capacity": 1,
  "transfer_rate": 1,
  "cost": 0,
  "work_ticks": 80,
  "model": "smog:block/hand_bellows",
  "assembled_model": "smog:block/hand_bellows",
  "texture": "minecraft:block/bricks",
  "mining_level": "wood"
}
```

### ceramic_kiln

```json
{
  "name": "ceramic_kiln",
  "display": "Ceramic Kiln",
  "display_ru": "Печь обжига",
  "kind": "COPPER_WORKSHOP",
  "hardness": 2,
  "capacity": 1,
  "transfer_rate": 1,
  "cost": 0,
  "work_ticks": 80,
  "model": "smog:block/ceramic_kiln",
  "assembled_model": "smog:block/ceramic_kiln",
  "texture": "minecraft:block/bricks",
  "mining_level": "wood",
  "multiblock": "smog:ceramic_kiln"
}
```

### washing_trough

```json
{
  "name": "washing_trough",
  "display": "Ore Washing Trough",
  "display_ru": "Промывочный лоток",
  "kind": "COPPER_WORKSHOP",
  "hardness": 2,
  "capacity": 1,
  "transfer_rate": 1,
  "cost": 0,
  "work_ticks": 80,
  "model": "smog:block/washing_trough",
  "assembled_model": "smog:block/washing_trough",
  "texture": "minecraft:block/bricks",
  "mining_level": "wood",
  "multiblock": "smog:washing_trough"
}
```

### lever_press

```json
{
  "name": "lever_press",
  "display": "Lever Press",
  "display_ru": "Рычажный пресс",
  "kind": "COPPER_WORKSHOP",
  "hardness": 2,
  "capacity": 1,
  "transfer_rate": 1,
  "cost": 0,
  "work_ticks": 80,
  "model": "smog:block/lever_press",
  "assembled_model": "smog:block/lever_press",
  "texture": "minecraft:block/bricks",
  "mining_level": "wood",
  "multiblock": "smog:lever_press"
}
```

### water_tank

```json
{
  "name": "water_tank",
  "display": "Workshop Water Tank",
  "display_ru": "Резервуар мастерской",
  "kind": "COPPER_WORKSHOP",
  "hardness": 2,
  "capacity": 1,
  "transfer_rate": 1,
  "cost": 0,
  "work_ticks": 80,
  "model": "smog:block/water_tank",
  "assembled_model": "smog:block/water_tank",
  "texture": "minecraft:block/bricks",
  "mining_level": "wood"
}
```

### hand_water_pump

```json
{
  "name": "hand_water_pump",
  "display": "Hand Water Pump",
  "display_ru": "Ручная водяная помпа",
  "kind": "COPPER_WORKSHOP",
  "hardness": 2,
  "capacity": 1,
  "transfer_rate": 1,
  "cost": 0,
  "work_ticks": 80,
  "model": "smog:block/hand_water_pump",
  "assembled_model": "smog:block/hand_water_pump",
  "texture": "minecraft:block/bricks",
  "mining_level": "wood"
}
```

### tool_workbench

```json
{
  "name": "tool_workbench",
  "display": "Toolmaker Workbench",
  "display_ru": "Медная наковальня",
  "kind": "COPPER_WORKSHOP",
  "hardness": 2,
  "capacity": 1,
  "transfer_rate": 1,
  "cost": 0,
  "work_ticks": 80,
  "model": "smog:block/tool_workbench",
  "assembled_model": "smog:block/tool_workbench",
  "texture": "minecraft:block/bricks",
  "mining_level": "wood"
}
```

### loading_crane

```json
{
  "name": "loading_crane",
  "display": "Loading Gantry",
  "display_ru": "Погрузочный кран",
  "kind": "COPPER_WORKSHOP",
  "hardness": 2,
  "capacity": 1,
  "transfer_rate": 1,
  "cost": 0,
  "work_ticks": 160,
  "model": "smog:block/loading_crane",
  "assembled_model": "smog:block/loading_crane",
  "texture": "minecraft:block/bricks",
  "mining_level": "wood",
  "multiblock": "smog:loading_crane",
  "render_parts": [
    "post",
    "beam",
    "rail",
    "brace",
    "trolley",
    "drum",
    "hook",
    "cargo",
    "marker_a",
    "marker_b",
    "redstone_marker"
  ]
}
```

### rope_anchor

```json
{
  "name": "rope_anchor",
  "display": "Winch Ropeway Anchor",
  "display_ru": "Лебёдочная переправа: анкер",
  "kind": "COPPER_WORKSHOP",
  "hardness": 2,
  "capacity": 1,
  "transfer_rate": 1,
  "cost": 0,
  "work_ticks": 80,
  "model": "smog:block/rope_anchor",
  "assembled_model": "smog:block/rope_anchor",
  "render_parts": [
    "carriage"
  ],
  "texture": "minecraft:block/bricks",
  "mining_level": "wood"
}
```

### smeltery_fuel_tank

```json
{
  "name": "smeltery_fuel_tank",
  "display": "Smeltery Fuel Tank",
  "display_ru": "Топливный бак плавильни",
  "kind": "COPPER_WORKSHOP",
  "hardness": 2,
  "capacity": 1,
  "transfer_rate": 1,
  "cost": 0,
  "work_ticks": 80,
  "model": "smog:block/smeltery_fuel_tank",
  "assembled_model": "smog:block/smeltery_fuel_tank",
  "texture": "minecraft:block/bricks",
  "mining_level": "wood"
}
```

### casting_basin

```json
{
  "name": "casting_basin",
  "display": "Casting Basin",
  "display_ru": "Глубокая литейная ёмкость",
  "kind": "COPPER_WORKSHOP",
  "hardness": 2,
  "capacity": 1,
  "transfer_rate": 1,
  "cost": 0,
  "work_ticks": 80,
  "model": "smog:block/casting_basin",
  "assembled_model": "smog:block/casting_basin",
  "texture": "minecraft:block/bricks",
  "mining_level": "wood"
}
```


## Схемы-примеры структур

### copper_hearth

```json
{
  "name": "copper_hearth",
  "display": "Casting Hearth (small example)",
  "display_ru": "Литейный очаг (малый пример)",
  "parts": [
    {
      "x": -1,
      "y": -1,
      "z": 0,
      "block": "smog:firebrick_casing"
    },
    {
      "x": -1,
      "y": -1,
      "z": 1,
      "block": "smog:firebrick_casing"
    },
    {
      "x": -1,
      "y": -1,
      "z": 2,
      "block": "smog:firebrick_casing"
    },
    {
      "x": -1,
      "y": 0,
      "z": 0,
      "block": "smog:firebrick_casing"
    },
    {
      "x": -1,
      "y": 0,
      "z": 1,
      "block": "smog:casting_spout"
    },
    {
      "x": -1,
      "y": 0,
      "z": 2,
      "block": "smog:firebrick_casing"
    },
    {
      "x": -1,
      "y": 1,
      "z": 0,
      "block": "smog:firebrick_casing"
    },
    {
      "x": -1,
      "y": 1,
      "z": 1,
      "block": "smog:firebrick_casing"
    },
    {
      "x": -1,
      "y": 1,
      "z": 2,
      "block": "smog:firebrick_casing"
    },
    {
      "x": 0,
      "y": -1,
      "z": 0,
      "block": "smog:firebrick_casing"
    },
    {
      "x": 0,
      "y": -1,
      "z": 1,
      "block": "smog:solid_firebox"
    },
    {
      "x": 0,
      "y": -1,
      "z": 2,
      "block": "smog:firebrick_casing"
    },
    {
      "x": 0,
      "y": 0,
      "z": 1,
      "block": "minecraft:air"
    },
    {
      "x": 0,
      "y": 0,
      "z": 2,
      "block": "smog:firebrick_casing"
    },
    {
      "x": 0,
      "y": 1,
      "z": 0,
      "block": "smog:firebrick_casing"
    },
    {
      "x": 0,
      "y": 1,
      "z": 1,
      "block": "minecraft:air"
    },
    {
      "x": 0,
      "y": 1,
      "z": 2,
      "block": "smog:firebrick_casing"
    },
    {
      "x": 1,
      "y": -1,
      "z": 0,
      "block": "smog:firebrick_casing"
    },
    {
      "x": 1,
      "y": -1,
      "z": 1,
      "block": "smog:firebrick_casing"
    },
    {
      "x": 1,
      "y": -1,
      "z": 2,
      "block": "smog:firebrick_casing"
    },
    {
      "x": 1,
      "y": 0,
      "z": 0,
      "block": "smog:firebrick_casing"
    },
    {
      "x": 1,
      "y": 0,
      "z": 1,
      "block": "smog:air_intake"
    },
    {
      "x": 1,
      "y": 0,
      "z": 2,
      "block": "smog:firebrick_casing"
    },
    {
      "x": 1,
      "y": 1,
      "z": 0,
      "block": "smog:firebrick_casing"
    },
    {
      "x": 1,
      "y": 1,
      "z": 1,
      "block": "smog:firebrick_casing"
    },
    {
      "x": 1,
      "y": 1,
      "z": 2,
      "block": "smog:firebrick_casing"
    }
  ]
}
```

### ceramic_kiln

```json
{
  "name": "ceramic_kiln",
  "display": "Ceramic Kiln (small example)",
  "display_ru": "Печь обжига (малый пример)",
  "parts": [
    {
      "x": -1,
      "y": -1,
      "z": 0,
      "block": "smog:firebrick_casing"
    },
    {
      "x": -1,
      "y": -1,
      "z": 1,
      "block": "smog:firebrick_casing"
    },
    {
      "x": -1,
      "y": -1,
      "z": 2,
      "block": "smog:firebrick_casing"
    },
    {
      "x": -1,
      "y": 0,
      "z": 0,
      "block": "smog:firebrick_casing"
    },
    {
      "x": -1,
      "y": 0,
      "z": 1,
      "block": "smog:firebrick_casing"
    },
    {
      "x": -1,
      "y": 0,
      "z": 2,
      "block": "smog:firebrick_casing"
    },
    {
      "x": -1,
      "y": 1,
      "z": 0,
      "block": "smog:firebrick_casing"
    },
    {
      "x": -1,
      "y": 1,
      "z": 1,
      "block": "smog:firebrick_casing"
    },
    {
      "x": -1,
      "y": 1,
      "z": 2,
      "block": "smog:firebrick_casing"
    },
    {
      "x": 0,
      "y": -1,
      "z": 0,
      "block": "smog:firebrick_casing"
    },
    {
      "x": 0,
      "y": -1,
      "z": 1,
      "block": "smog:solid_firebox"
    },
    {
      "x": 0,
      "y": -1,
      "z": 2,
      "block": "smog:firebrick_casing"
    },
    {
      "x": 0,
      "y": 0,
      "z": 1,
      "block": "minecraft:air"
    },
    {
      "x": 0,
      "y": 0,
      "z": 2,
      "block": "smog:firebrick_casing"
    },
    {
      "x": 0,
      "y": 1,
      "z": 0,
      "block": "smog:firebrick_casing"
    },
    {
      "x": 0,
      "y": 1,
      "z": 1,
      "block": "smog:kiln_vent"
    },
    {
      "x": 0,
      "y": 1,
      "z": 2,
      "block": "smog:firebrick_casing"
    },
    {
      "x": 1,
      "y": -1,
      "z": 0,
      "block": "smog:firebrick_casing"
    },
    {
      "x": 1,
      "y": -1,
      "z": 1,
      "block": "smog:firebrick_casing"
    },
    {
      "x": 1,
      "y": -1,
      "z": 2,
      "block": "smog:firebrick_casing"
    },
    {
      "x": 1,
      "y": 0,
      "z": 0,
      "block": "smog:firebrick_casing"
    },
    {
      "x": 1,
      "y": 0,
      "z": 1,
      "block": "smog:firebrick_casing"
    },
    {
      "x": 1,
      "y": 0,
      "z": 2,
      "block": "smog:firebrick_casing"
    },
    {
      "x": 1,
      "y": 1,
      "z": 0,
      "block": "smog:firebrick_casing"
    },
    {
      "x": 1,
      "y": 1,
      "z": 1,
      "block": "smog:firebrick_casing"
    },
    {
      "x": 1,
      "y": 1,
      "z": 2,
      "block": "smog:firebrick_casing"
    }
  ]
}
```

### washing_trough

```json
{
  "name": "washing_trough",
  "display": "Washing Trough",
  "display_ru": "Промывочный лоток",
  "parts": [
    {
      "x": -1,
      "y": 0,
      "z": 0,
      "block": "smog:washing_section"
    },
    {
      "x": 1,
      "y": 0,
      "z": 0,
      "block": "smog:washing_section"
    },
    {
      "x": 0,
      "y": 0,
      "z": 1,
      "block": "smog:washing_sieve"
    },
    {
      "x": -1,
      "y": 0,
      "z": 1,
      "block": "smog:workshop_support"
    },
    {
      "x": 1,
      "y": 0,
      "z": 1,
      "block": "smog:workshop_support"
    }
  ]
}
```

### lever_press

```json
{
  "name": "lever_press",
  "display": "Lever Press",
  "display_ru": "Рычажный пресс",
  "parts": [
    {
      "x": -1,
      "y": 0,
      "z": 0,
      "block": "smog:workshop_support"
    },
    {
      "x": 1,
      "y": 0,
      "z": 0,
      "block": "smog:workshop_support"
    },
    {
      "x": -1,
      "y": 1,
      "z": 0,
      "block": "#smog:press_frames"
    },
    {
      "x": 0,
      "y": 1,
      "z": 0,
      "block": "#smog:press_frames"
    },
    {
      "x": 1,
      "y": 1,
      "z": 0,
      "block": "#smog:press_frames"
    },
    {
      "x": -1,
      "y": 0,
      "z": 1,
      "block": "smog:workshop_support"
    },
    {
      "x": 0,
      "y": 0,
      "z": 1,
      "block": "#smog:press_baskets"
    },
    {
      "x": 1,
      "y": 0,
      "z": 1,
      "block": "smog:workshop_support"
    },
    {
      "x": -1,
      "y": 1,
      "z": 1,
      "block": "#smog:press_frames"
    },
    {
      "x": 0,
      "y": 1,
      "z": 1,
      "block": "#smog:press_frames"
    },
    {
      "x": 1,
      "y": 1,
      "z": 1,
      "block": "#smog:press_frames"
    }
  ]
}
```

### loading_crane

```json
{
  "name": "loading_crane",
  "display": "Loading Gantry",
  "display_ru": "Погрузочный кран",
  "parts": [
    {
      "x": -2,
      "y": -4,
      "z": 0,
      "block": "smog:workshop_support"
    },
    {
      "x": -2,
      "y": -3,
      "z": 0,
      "block": "smog:workshop_support"
    },
    {
      "x": -2,
      "y": -2,
      "z": 0,
      "block": "smog:workshop_support"
    },
    {
      "x": -2,
      "y": -1,
      "z": 0,
      "block": "smog:workshop_support"
    },
    {
      "x": -2,
      "y": -4,
      "z": 4,
      "block": "smog:workshop_support"
    },
    {
      "x": -2,
      "y": -3,
      "z": 4,
      "block": "smog:workshop_support"
    },
    {
      "x": -2,
      "y": -2,
      "z": 4,
      "block": "smog:workshop_support"
    },
    {
      "x": -2,
      "y": -1,
      "z": 4,
      "block": "smog:workshop_support"
    },
    {
      "x": 2,
      "y": -4,
      "z": 0,
      "block": "smog:workshop_support"
    },
    {
      "x": 2,
      "y": -3,
      "z": 0,
      "block": "smog:workshop_support"
    },
    {
      "x": 2,
      "y": -2,
      "z": 0,
      "block": "smog:workshop_support"
    },
    {
      "x": 2,
      "y": -1,
      "z": 0,
      "block": "smog:workshop_support"
    },
    {
      "x": 2,
      "y": -4,
      "z": 4,
      "block": "smog:workshop_support"
    },
    {
      "x": 2,
      "y": -3,
      "z": 4,
      "block": "smog:workshop_support"
    },
    {
      "x": 2,
      "y": -2,
      "z": 4,
      "block": "smog:workshop_support"
    },
    {
      "x": 2,
      "y": -1,
      "z": 4,
      "block": "smog:workshop_support"
    },
    {
      "x": -2,
      "y": 0,
      "z": 0,
      "block": "smog:press_frame"
    },
    {
      "x": -1,
      "y": 0,
      "z": 0,
      "block": "smog:press_frame"
    },
    {
      "x": 1,
      "y": 0,
      "z": 0,
      "block": "smog:press_frame"
    },
    {
      "x": 2,
      "y": 0,
      "z": 0,
      "block": "smog:press_frame"
    },
    {
      "x": -2,
      "y": 0,
      "z": 4,
      "block": "smog:press_frame"
    },
    {
      "x": -1,
      "y": 0,
      "z": 4,
      "block": "smog:press_frame"
    },
    {
      "x": 0,
      "y": 0,
      "z": 4,
      "block": "smog:press_frame"
    },
    {
      "x": 1,
      "y": 0,
      "z": 4,
      "block": "smog:press_frame"
    },
    {
      "x": 2,
      "y": 0,
      "z": 4,
      "block": "smog:press_frame"
    },
    {
      "x": 0,
      "y": 0,
      "z": 1,
      "block": "smog:press_frame"
    },
    {
      "x": 0,
      "y": 0,
      "z": 2,
      "block": "smog:press_frame"
    },
    {
      "x": 0,
      "y": 0,
      "z": 3,
      "block": "smog:press_frame"
    }
  ]
}
```


## Жидкости

### coolant

```json
{
  "name": "coolant",
  "display": "Coolant",
  "display_ru": "Хладагент",
  "color": "44BBDD",
  "tick_delay": 10,
  "infinite": false,
  "tags": [
    "smog:coolants"
  ],
  "bucket_texture": "smog:item/coolant_bucket"
}
```

### crude_oil

```json
{
  "name": "crude_oil",
  "display": "Crude Oil",
  "display_ru": "Нефть",
  "color": "32271E",
  "tick_delay": 20,
  "infinite": false,
  "water_like": false,
  "bucket_texture": "smog:item/crude_oil_bucket",
  "tags": [
    "smog:underground_fluids"
  ]
}
```

### mineral_brine

```json
{
  "name": "mineral_brine",
  "display": "Mineralized Brine",
  "display_ru": "Минерализованный рассол",
  "color": "91B6AC",
  "tick_delay": 5,
  "infinite": false,
  "water_like": true,
  "bucket_texture": "smog:item/mineral_brine_bucket",
  "tags": [
    "smog:underground_fluids"
  ]
}
```

### molten_copper

```json
{
  "name": "molten_copper",
  "display": "Molten Copper",
  "display_ru": "Расплавленная медь",
  "color": "ED913F",
  "tick_delay": 30,
  "infinite": false,
  "water_like": false,
  "bucket_texture": "smog:item/molten_copper_bucket",
  "tags": [
    "c:molten_copper"
  ]
}
```

### molten_tin

```json
{
  "name": "molten_tin",
  "display": "Molten Tin",
  "display_ru": "Расплавленное олово",
  "color": "F0CD97",
  "tick_delay": 30,
  "infinite": false,
  "water_like": false,
  "bucket_texture": "smog:item/molten_tin_bucket",
  "tags": [
    "c:molten_tin"
  ]
}
```

### molten_bronze

```json
{
  "name": "molten_bronze",
  "display": "Molten Bronze",
  "display_ru": "Расплавленная бронза",
  "color": "DFA64B",
  "tick_delay": 30,
  "infinite": false,
  "water_like": false,
  "bucket_texture": "smog:item/molten_bronze_bucket",
  "tags": [
    "c:molten_bronze"
  ]
}
```

### plant_oil

```json
{
  "name": "plant_oil",
  "display": "Plant Oil",
  "display_ru": "Растительное масло",
  "color": "BB8D31",
  "tick_delay": 10,
  "infinite": false,
  "bucket_texture": "smog:item/plant_oil_bucket"
}
```

### molten_lead

```json
{
  "name": "molten_lead",
  "display": "Molten Lead",
  "display_ru": "Расплавленный свинец",
  "color": "857E9C",
  "tick_delay": 30,
  "infinite": false,
  "water_like": false,
  "bucket_texture": "smog:item/molten_lead_bucket",
  "tags": [
    "c:molten_lead"
  ]
}
```

### molten_zinc

```json
{
  "name": "molten_zinc",
  "display": "Molten Zinc",
  "display_ru": "Расплавленный цинк",
  "color": "B2C0BB",
  "tick_delay": 30,
  "infinite": false,
  "water_like": false,
  "bucket_texture": "smog:item/molten_zinc_bucket",
  "tags": [
    "c:molten_zinc"
  ]
}
```

### molten_aluminum

```json
{
  "name": "molten_aluminum",
  "display": "Molten Aluminum",
  "display_ru": "Расплавленный алюминий",
  "color": "D3DDE0",
  "tick_delay": 30,
  "infinite": false,
  "water_like": false,
  "bucket_texture": "smog:item/molten_aluminum_bucket",
  "tags": [
    "c:molten_aluminum"
  ]
}
```

### molten_silver

```json
{
  "name": "molten_silver",
  "display": "Molten Silver",
  "display_ru": "Расплавленное серебро",
  "color": "D5E4ED",
  "tick_delay": 30,
  "infinite": false,
  "water_like": false,
  "bucket_texture": "smog:item/molten_silver_bucket",
  "tags": [
    "c:molten_silver"
  ]
}
```

### molten_gold

```json
{
  "name": "molten_gold",
  "display": "Molten Gold",
  "display_ru": "Расплавленное золото",
  "color": "F8C54B",
  "tick_delay": 30,
  "infinite": false,
  "water_like": false,
  "bucket_texture": "smog:item/molten_gold_bucket",
  "tags": [
    "c:molten_gold"
  ]
}
```

### molten_brass

```json
{
  "name": "molten_brass",
  "display": "Molten Brass",
  "display_ru": "Расплавленная латунь",
  "color": "D6AF4C",
  "tick_delay": 30,
  "infinite": false,
  "water_like": false,
  "bucket_texture": "smog:item/molten_brass_bucket",
  "tags": [
    "c:molten_brass"
  ]
}
```

### molten_iron

```json
{
  "name": "molten_iron",
  "display": "Molten Iron",
  "display_ru": "Расплавленное железо",
  "color": "D8D8D8",
  "tick_delay": 30,
  "infinite": false,
  "water_like": false,
  "bucket_texture": "smog:item/molten_iron_bucket",
  "tags": [
    "c:molten_iron"
  ]
}
```

### molten_nickel

```json
{
  "name": "molten_nickel",
  "display": "Molten Nickel",
  "display_ru": "Расплавленный никель",
  "color": "A8AA83",
  "tick_delay": 30,
  "infinite": false,
  "water_like": false,
  "bucket_texture": "smog:item/molten_nickel_bucket",
  "tags": [
    "c:molten_nickel"
  ]
}
```

### molten_manganese

```json
{
  "name": "molten_manganese",
  "display": "Molten Manganese",
  "display_ru": "Расплавленный марганец",
  "color": "78808C",
  "tick_delay": 30,
  "infinite": false,
  "water_like": false,
  "bucket_texture": "smog:item/molten_manganese_bucket",
  "tags": [
    "c:molten_manganese"
  ]
}
```

### molten_chromium

```json
{
  "name": "molten_chromium",
  "display": "Molten Chromium",
  "display_ru": "Расплавленный хром",
  "color": "8DAAA7",
  "tick_delay": 30,
  "infinite": false,
  "water_like": false,
  "bucket_texture": "smog:item/molten_chromium_bucket",
  "tags": [
    "c:molten_chromium"
  ]
}
```

### molten_titanium

```json
{
  "name": "molten_titanium",
  "display": "Molten Titanium",
  "display_ru": "Расплавленный титан",
  "color": "8F9FB3",
  "tick_delay": 30,
  "infinite": false,
  "water_like": false,
  "bucket_texture": "smog:item/molten_titanium_bucket",
  "tags": [
    "c:molten_titanium"
  ]
}
```

### molten_tungsten

```json
{
  "name": "molten_tungsten",
  "display": "Molten Tungsten",
  "display_ru": "Расплавленный вольфрам",
  "color": "6D7886",
  "tick_delay": 30,
  "infinite": false,
  "water_like": false,
  "bucket_texture": "smog:item/molten_tungsten_bucket",
  "tags": [
    "c:molten_tungsten"
  ]
}
```

### molten_molybdenum

```json
{
  "name": "molten_molybdenum",
  "display": "Molten Molybdenum",
  "display_ru": "Расплавленный молибден",
  "color": "8E9FAB",
  "tick_delay": 30,
  "infinite": false,
  "water_like": false,
  "bucket_texture": "smog:item/molten_molybdenum_bucket",
  "tags": [
    "c:molten_molybdenum"
  ]
}
```

### molten_uranium

```json
{
  "name": "molten_uranium",
  "display": "Molten Uranium",
  "display_ru": "Расплавленный уран",
  "color": "7F964D",
  "tick_delay": 30,
  "infinite": false,
  "water_like": false,
  "bucket_texture": "smog:item/molten_uranium_bucket",
  "tags": [
    "c:molten_uranium"
  ]
}
```

### molten_lithium

```json
{
  "name": "molten_lithium",
  "display": "Molten Lithium",
  "display_ru": "Расплавленный литий",
  "color": "C3BED4",
  "tick_delay": 30,
  "infinite": false,
  "water_like": false,
  "bucket_texture": "smog:item/molten_lithium_bucket",
  "tags": [
    "c:molten_lithium"
  ]
}
```

### molten_steel

```json
{
  "name": "molten_steel",
  "display": "Molten Steel",
  "display_ru": "Расплавленная сталь",
  "color": "66717B",
  "tick_delay": 30,
  "infinite": false,
  "water_like": false,
  "bucket_texture": "smog:item/molten_steel_bucket",
  "tags": [
    "c:molten_steel"
  ]
}
```

### molten_cast_iron

```json
{
  "name": "molten_cast_iron",
  "display": "Molten Cast Iron",
  "display_ru": "Расплавленный чугун",
  "color": "665D59",
  "tick_delay": 30,
  "infinite": false,
  "water_like": false,
  "bucket_texture": "smog:item/molten_cast_iron_bucket",
  "tags": [
    "c:molten_cast_iron"
  ]
}
```

### molten_wrought_iron

```json
{
  "name": "molten_wrought_iron",
  "display": "Molten Wrought Iron",
  "display_ru": "Расплавленное кричное железо",
  "color": "A4A19A",
  "tick_delay": 30,
  "infinite": false,
  "water_like": false,
  "bucket_texture": "smog:item/molten_wrought_iron_bucket",
  "tags": [
    "c:molten_wrought_iron"
  ]
}
```

### molten_stainless_steel

```json
{
  "name": "molten_stainless_steel",
  "display": "Molten Stainless Steel",
  "display_ru": "Расплавленная нержавеющая сталь",
  "color": "B5BFC1",
  "tick_delay": 30,
  "infinite": false,
  "water_like": false,
  "bucket_texture": "smog:item/molten_stainless_steel_bucket",
  "tags": [
    "c:molten_stainless_steel"
  ]
}
```

### molten_tool_steel

```json
{
  "name": "molten_tool_steel",
  "display": "Molten Tool Steel",
  "display_ru": "Расплавленная инструментальная сталь",
  "color": "69747D",
  "tick_delay": 30,
  "infinite": false,
  "water_like": false,
  "bucket_texture": "smog:item/molten_tool_steel_bucket",
  "tags": [
    "c:molten_tool_steel"
  ]
}
```

### molten_duralumin

```json
{
  "name": "molten_duralumin",
  "display": "Molten Duralumin",
  "display_ru": "Расплавленный дюралюминий",
  "color": "B4C6CF",
  "tick_delay": 30,
  "infinite": false,
  "water_like": false,
  "bucket_texture": "smog:item/molten_duralumin_bucket",
  "tags": [
    "c:molten_duralumin"
  ]
}
```

### molten_cupronickel

```json
{
  "name": "molten_cupronickel",
  "display": "Molten Cupronickel",
  "display_ru": "Расплавленный мельхиор",
  "color": "B2B8AB",
  "tick_delay": 30,
  "infinite": false,
  "water_like": false,
  "bucket_texture": "smog:item/molten_cupronickel_bucket",
  "tags": [
    "c:molten_cupronickel"
  ]
}
```

### molten_constantan

```json
{
  "name": "molten_constantan",
  "display": "Molten Constantan",
  "display_ru": "Расплавленный константан",
  "color": "B18C77",
  "tick_delay": 30,
  "infinite": false,
  "water_like": false,
  "bucket_texture": "smog:item/molten_constantan_bucket",
  "tags": [
    "c:molten_constantan"
  ]
}
```

### molten_invar

```json
{
  "name": "molten_invar",
  "display": "Molten Invar",
  "display_ru": "Расплавленный инвар",
  "color": "9AABA4",
  "tick_delay": 30,
  "infinite": false,
  "water_like": false,
  "bucket_texture": "smog:item/molten_invar_bucket",
  "tags": [
    "c:molten_invar"
  ]
}
```

### molten_electrum

```json
{
  "name": "molten_electrum",
  "display": "Molten Electrum",
  "display_ru": "Расплавленный электрум",
  "color": "E6D58E",
  "tick_delay": 30,
  "infinite": false,
  "water_like": false,
  "bucket_texture": "smog:item/molten_electrum_bucket",
  "tags": [
    "c:molten_electrum"
  ]
}
```

### molten_nichrome

```json
{
  "name": "molten_nichrome",
  "display": "Molten Nichrome",
  "display_ru": "Расплавленный нихром",
  "color": "B9AD9F",
  "tick_delay": 30,
  "infinite": false,
  "water_like": false,
  "bucket_texture": "smog:item/molten_nichrome_bucket",
  "tags": [
    "c:molten_nichrome"
  ]
}
```

### molten_solder

```json
{
  "name": "molten_solder",
  "display": "Molten Tin-Lead Solder",
  "display_ru": "Расплавленный припой",
  "color": "B7BAC0",
  "tick_delay": 30,
  "infinite": false,
  "water_like": false,
  "bucket_texture": "smog:item/molten_solder_bucket",
  "tags": [
    "c:molten_solder"
  ]
}
```

### molten_tungsten_steel

```json
{
  "name": "molten_tungsten_steel",
  "display": "Molten Tungsten Steel",
  "display_ru": "Расплавленная вольфрамовая сталь",
  "color": "535E6C",
  "tick_delay": 30,
  "infinite": false,
  "water_like": false,
  "bucket_texture": "smog:item/molten_tungsten_steel_bucket",
  "tags": [
    "c:molten_tungsten_steel"
  ]
}
```

### molten_titanium_alloy

```json
{
  "name": "molten_titanium_alloy",
  "display": "Molten Titanium Alloy",
  "display_ru": "Расплавленный титановый сплав",
  "color": "A9B9CB",
  "tick_delay": 30,
  "infinite": false,
  "water_like": false,
  "bucket_texture": "smog:item/molten_titanium_alloy_bucket",
  "tags": [
    "c:molten_titanium_alloy"
  ]
}
```

### molten_nickel_superalloy

```json
{
  "name": "molten_nickel_superalloy",
  "display": "Molten Nickel Superalloy",
  "display_ru": "Расплавленный никелевый жаропрочный сплав",
  "color": "B4A385",
  "tick_delay": 30,
  "infinite": false,
  "water_like": false,
  "bucket_texture": "smog:item/molten_nickel_superalloy_bucket",
  "tags": [
    "c:molten_nickel_superalloy"
  ]
}
```

### molten_aluminum_bronze

```json
{
  "name": "molten_aluminum_bronze",
  "display": "Molten Aluminum Bronze",
  "display_ru": "Расплавленная алюминиевая бронза",
  "color": "BFA35E",
  "tick_delay": 30,
  "infinite": false,
  "water_like": false,
  "bucket_texture": "smog:item/molten_aluminum_bronze_bucket",
  "tags": [
    "c:molten_aluminum_bronze"
  ]
}
```

### molten_netherite

```json
{
  "name": "molten_netherite",
  "display": "Molten Netherite",
  "display_ru": "Расплавленный незерит",
  "color": "655C63",
  "tick_delay": 30,
  "infinite": false,
  "water_like": false,
  "bucket_texture": "smog:item/molten_netherite_bucket",
  "tags": [
    "c:molten_netherite"
  ]
}
```

### blazing_fuel

```json
{
  "name": "blazing_fuel",
  "display": "Blazing Fuel",
  "display_ru": "Пылающее топливо",
  "color": "F7CB55",
  "tick_delay": 30,
  "infinite": false,
  "bucket_texture": "smog:item/blazing_fuel_bucket",
  "tags": [
    "smog:smeltery_fuels"
  ]
}
```


## Предметы

### configurator

```json
{
  "name": "configurator",
  "display": "Configurator",
  "display_ru": "Настройщик",
  "tab": "copper_age",
  "model": "smog:item/configurator",
  "kind": "configurator"
}
```

### tempered_clay

```json
{
  "name": "tempered_clay",
  "display": "Tempered Clay",
  "display_ru": "Печная масса",
  "tab": "copper_age"
}
```

### unfired_crucible

```json
{
  "name": "unfired_crucible",
  "display": "Unfired Crucible",
  "display_ru": "Сырой тигель",
  "tab": "copper_age"
}
```

### unfired_large_crucible

```json
{
  "name": "unfired_large_crucible",
  "display": "Unfired Large Crucible",
  "display_ru": "Сырой увеличенный тигель",
  "tab": "copper_age"
}
```

### unfired_firebrick

```json
{
  "name": "unfired_firebrick",
  "display": "Unfired Firebrick",
  "display_ru": "Сырой огнеупорный кирпич",
  "tab": "copper_age"
}
```

### firebrick

```json
{
  "name": "firebrick",
  "display": "Firebrick",
  "display_ru": "Огнеупорный кирпич",
  "tab": "copper_age"
}
```

### copper_plate

```json
{
  "name": "copper_plate",
  "display": "Copper Plate",
  "display_ru": "Медная пластина",
  "tab": "copper_age"
}
```

### unfired_ingot_mold

```json
{
  "name": "unfired_ingot_mold",
  "display": "Unfired Ingot Mold",
  "display_ru": "Сырая слитковая форма",
  "tab": "copper_age"
}
```

### ingot_mold

```json
{
  "name": "ingot_mold",
  "display": "Ingot Mold",
  "display_ru": "Слитковая форма",
  "tab": "copper_age"
}
```

### unfired_plate_mold

```json
{
  "name": "unfired_plate_mold",
  "display": "Unfired Plate Mold",
  "display_ru": "Сырая плоская форма",
  "tab": "copper_age"
}
```

### plate_mold

```json
{
  "name": "plate_mold",
  "display": "Plate Mold",
  "display_ru": "Плоская форма",
  "tab": "copper_age"
}
```

### unfired_fitting_mold

```json
{
  "name": "unfired_fitting_mold",
  "display": "Unfired Fitting Mold",
  "display_ru": "Сырая форма мелких деталей",
  "tab": "copper_age"
}
```

### fitting_mold

```json
{
  "name": "fitting_mold",
  "display": "Fitting Mold",
  "display_ru": "Форма мелких деталей",
  "tab": "copper_age"
}
```

### unfired_pipe_mold

```json
{
  "name": "unfired_pipe_mold",
  "display": "Unfired Pipe Mold",
  "display_ru": "Сырая трубная форма",
  "tab": "copper_age"
}
```

### pipe_mold

```json
{
  "name": "pipe_mold",
  "display": "Pipe Mold",
  "display_ru": "Трубная форма",
  "tab": "copper_age"
}
```

### copper_fitting

```json
{
  "name": "copper_fitting",
  "display": "Copper Fitting",
  "display_ru": "Медный фитинг",
  "tab": "copper_age"
}
```

### copper_pipe_part

```json
{
  "name": "copper_pipe_part",
  "display": "Copper Pipe Blank",
  "display_ru": "Медный трубный элемент",
  "tab": "copper_age"
}
```

### bronze_fitting

```json
{
  "name": "bronze_fitting",
  "display": "Bronze Fitting",
  "display_ru": "Бронзовый фитинг",
  "tab": "copper_age"
}
```

### bronze_pipe_part

```json
{
  "name": "bronze_pipe_part",
  "display": "Bronze Pipe Blank",
  "display_ru": "Бронзовый трубный элемент",
  "tab": "copper_age"
}
```

### metal_residue

```json
{
  "name": "metal_residue",
  "display": "Recoverable Metal Residue",
  "display_ru": "Металлический остаток",
  "tab": "copper_age"
}
```

### copper_concentrate

```json
{
  "name": "copper_concentrate",
  "display": "Copper Concentrate",
  "display_ru": "Медный концентрат",
  "tab": "copper_age"
}
```

### tin_concentrate

```json
{
  "name": "tin_concentrate",
  "display": "Tin Concentrate",
  "display_ru": "Оловянный концентрат",
  "tab": "copper_age"
}
```

### lead_concentrate

```json
{
  "name": "lead_concentrate",
  "display": "Lead Concentrate",
  "display_ru": "Свинцовый концентрат",
  "tab": "copper_age"
}
```

### iron_concentrate

```json
{
  "name": "iron_concentrate",
  "display": "Iron Concentrate",
  "display_ru": "Железный концентрат",
  "tab": "copper_age"
}
```

### gold_concentrate

```json
{
  "name": "gold_concentrate",
  "display": "Gold Concentrate",
  "display_ru": "Золотой концентрат",
  "tab": "copper_age"
}
```

### seed_cake

```json
{
  "name": "seed_cake",
  "display": "Seed Cake",
  "display_ru": "Растительный жмых",
  "tab": "copper_age"
}
```

### plant_oil_bottle

```json
{
  "name": "plant_oil_bottle",
  "display": "Plant Oil Bottle",
  "display_ru": "Бутылочка растительного масла",
  "tab": "copper_age"
}
```

### lubricating_compound

```json
{
  "name": "lubricating_compound",
  "display": "Lubricating Compound",
  "display_ru": "Смазочная смесь",
  "tab": "copper_age"
}
```

### waxed_cord

```json
{
  "name": "waxed_cord",
  "display": "Waxed Cord",
  "display_ru": "Вощёный шнур",
  "tab": "copper_age"
}
```

### plant_fiber

```json
{
  "name": "plant_fiber",
  "display": "Plant Fiber",
  "display_ru": "Растительное волокно",
  "tab": "copper_age"
}
```

### handcart

```json
{
  "name": "handcart",
  "kind": "handcart",
  "display": "Workshop Handcart",
  "display_ru": "Грузовая тележка",
  "tab": "copper_age",
  "model": "smog:block/handcart"
}
```

### freight_rope

```json
{
  "name": "freight_rope",
  "kind": "rope",
  "display": "Freight Rope",
  "display_ru": "Грузовой канат",
  "tab": "copper_age"
}
```

### molding_sand

```json
{
  "name": "molding_sand",
  "display": "Molding Sand",
  "display_ru": "Формовочная смесь",
  "tab": "copper_age"
}
```

### ceramic_core

```json
{
  "name": "ceramic_core",
  "display": "Ceramic Casting Core",
  "display_ru": "Керамический литейный стержень",
  "tab": "copper_age"
}
```

### fluid_canister

```json
{
  "name": "fluid_canister",
  "kind": "fluid_canister",
  "display": "Workshop Canister",
  "display_ru": "Канистра мастерской",
  "tab": "copper_age"
}
```

### aluminum_pickaxe_head

```json
{
  "name": "aluminum_pickaxe_head",
  "kind": "tool_part",
  "display": "Aluminum: Pickaxe Head",
  "display_ru": "Алюминий: Головка кирки",
  "tab": "copper_age"
}
```

### aluminum_cast_pickaxe_head

```json
{
  "name": "aluminum_cast_pickaxe_head",
  "kind": "tool_part",
  "display": "Aluminum: Casting - Pickaxe Head",
  "display_ru": "Алюминий: Отливка — Головка кирки",
  "tab": "copper_age"
}
```

### aluminum_axe_head

```json
{
  "name": "aluminum_axe_head",
  "kind": "tool_part",
  "display": "Aluminum: Axe Head",
  "display_ru": "Алюминий: Головка топора",
  "tab": "copper_age"
}
```

### aluminum_cast_axe_head

```json
{
  "name": "aluminum_cast_axe_head",
  "kind": "tool_part",
  "display": "Aluminum: Casting - Axe Head",
  "display_ru": "Алюминий: Отливка — Головка топора",
  "tab": "copper_age"
}
```

### aluminum_shovel_head

```json
{
  "name": "aluminum_shovel_head",
  "kind": "tool_part",
  "display": "Aluminum: Shovel Head",
  "display_ru": "Алюминий: Головка лопаты",
  "tab": "copper_age"
}
```

### aluminum_cast_shovel_head

```json
{
  "name": "aluminum_cast_shovel_head",
  "kind": "tool_part",
  "display": "Aluminum: Casting - Shovel Head",
  "display_ru": "Алюминий: Отливка — Головка лопаты",
  "tab": "copper_age"
}
```

### aluminum_hoe_head

```json
{
  "name": "aluminum_hoe_head",
  "kind": "tool_part",
  "display": "Aluminum: Hoe Head",
  "display_ru": "Алюминий: Головка мотыги",
  "tab": "copper_age"
}
```

### aluminum_cast_hoe_head

```json
{
  "name": "aluminum_cast_hoe_head",
  "kind": "tool_part",
  "display": "Aluminum: Casting - Hoe Head",
  "display_ru": "Алюминий: Отливка — Головка мотыги",
  "tab": "copper_age"
}
```

### aluminum_hammer_head

```json
{
  "name": "aluminum_hammer_head",
  "kind": "tool_part",
  "display": "Aluminum: Hammer Head",
  "display_ru": "Алюминий: Головка молота",
  "tab": "copper_age"
}
```

### aluminum_cast_hammer_head

```json
{
  "name": "aluminum_cast_hammer_head",
  "kind": "tool_part",
  "display": "Aluminum: Casting - Hammer Head",
  "display_ru": "Алюминий: Отливка — Головка молота",
  "tab": "copper_age"
}
```

### aluminum_standard_handle

```json
{
  "name": "aluminum_standard_handle",
  "kind": "tool_part",
  "display": "Aluminum: Rod",
  "display_ru": "Алюминий: Стержень",
  "tab": "copper_age"
}
```

### aluminum_clamp

```json
{
  "name": "aluminum_clamp",
  "kind": "tool_part",
  "display": "Aluminum: Clamp",
  "display_ru": "Алюминий: Хомут",
  "tab": "copper_age"
}
```

### aluminum_saw_head

```json
{
  "name": "aluminum_saw_head",
  "kind": "tool_part",
  "display": "Aluminum: Saw Blade",
  "display_ru": "Алюминий: Полотно пилы",
  "tab": "copper_age"
}
```

### aluminum_cast_saw_head

```json
{
  "name": "aluminum_cast_saw_head",
  "kind": "tool_part",
  "display": "Aluminum: Casting - Saw Blade",
  "display_ru": "Алюминий: Отливка — Полотно пилы",
  "tab": "copper_age"
}
```

### aluminum_sword_head

```json
{
  "name": "aluminum_sword_head",
  "kind": "tool_part",
  "display": "Aluminum: Sword Blade",
  "display_ru": "Алюминий: Клинок меча",
  "tab": "copper_age"
}
```

### aluminum_cast_sword_head

```json
{
  "name": "aluminum_cast_sword_head",
  "kind": "tool_part",
  "display": "Aluminum: Casting - Sword Blade",
  "display_ru": "Алюминий: Отливка — Клинок меча",
  "tab": "copper_age"
}
```

### aluminum_katana_head

```json
{
  "name": "aluminum_katana_head",
  "kind": "tool_part",
  "display": "Aluminum: Katana Blade",
  "display_ru": "Алюминий: Клинок катаны",
  "tab": "copper_age"
}
```

### aluminum_cast_katana_head

```json
{
  "name": "aluminum_cast_katana_head",
  "kind": "tool_part",
  "display": "Aluminum: Casting - Katana Blade",
  "display_ru": "Алюминий: Отливка — Клинок катаны",
  "tab": "copper_age"
}
```

### aluminum_machete_head

```json
{
  "name": "aluminum_machete_head",
  "kind": "tool_part",
  "display": "Aluminum: Machete Blade",
  "display_ru": "Алюминий: Клинок мачете",
  "tab": "copper_age"
}
```

### aluminum_cast_machete_head

```json
{
  "name": "aluminum_cast_machete_head",
  "kind": "tool_part",
  "display": "Aluminum: Casting - Machete Blade",
  "display_ru": "Алюминий: Отливка — Клинок мачете",
  "tab": "copper_age"
}
```

### aluminum_sickle_head

```json
{
  "name": "aluminum_sickle_head",
  "kind": "tool_part",
  "display": "Aluminum: Sickle Blade",
  "display_ru": "Алюминий: Лезвие серпа",
  "tab": "copper_age"
}
```

### aluminum_cast_sickle_head

```json
{
  "name": "aluminum_cast_sickle_head",
  "kind": "tool_part",
  "display": "Aluminum: Casting - Sickle Blade",
  "display_ru": "Алюминий: Отливка — Лезвие серпа",
  "tab": "copper_age"
}
```

### aluminum_bronze_pickaxe_head

```json
{
  "name": "aluminum_bronze_pickaxe_head",
  "kind": "tool_part",
  "display": "Aluminum Bronze: Pickaxe Head",
  "display_ru": "Алюминиевая бронза: Головка кирки",
  "tab": "copper_age"
}
```

### aluminum_bronze_cast_pickaxe_head

```json
{
  "name": "aluminum_bronze_cast_pickaxe_head",
  "kind": "tool_part",
  "display": "Aluminum Bronze: Casting - Pickaxe Head",
  "display_ru": "Алюминиевая бронза: Отливка — Головка кирки",
  "tab": "copper_age"
}
```

### aluminum_bronze_axe_head

```json
{
  "name": "aluminum_bronze_axe_head",
  "kind": "tool_part",
  "display": "Aluminum Bronze: Axe Head",
  "display_ru": "Алюминиевая бронза: Головка топора",
  "tab": "copper_age"
}
```

### aluminum_bronze_cast_axe_head

```json
{
  "name": "aluminum_bronze_cast_axe_head",
  "kind": "tool_part",
  "display": "Aluminum Bronze: Casting - Axe Head",
  "display_ru": "Алюминиевая бронза: Отливка — Головка топора",
  "tab": "copper_age"
}
```

### aluminum_bronze_shovel_head

```json
{
  "name": "aluminum_bronze_shovel_head",
  "kind": "tool_part",
  "display": "Aluminum Bronze: Shovel Head",
  "display_ru": "Алюминиевая бронза: Головка лопаты",
  "tab": "copper_age"
}
```

### aluminum_bronze_cast_shovel_head

```json
{
  "name": "aluminum_bronze_cast_shovel_head",
  "kind": "tool_part",
  "display": "Aluminum Bronze: Casting - Shovel Head",
  "display_ru": "Алюминиевая бронза: Отливка — Головка лопаты",
  "tab": "copper_age"
}
```

### aluminum_bronze_hoe_head

```json
{
  "name": "aluminum_bronze_hoe_head",
  "kind": "tool_part",
  "display": "Aluminum Bronze: Hoe Head",
  "display_ru": "Алюминиевая бронза: Головка мотыги",
  "tab": "copper_age"
}
```

### aluminum_bronze_cast_hoe_head

```json
{
  "name": "aluminum_bronze_cast_hoe_head",
  "kind": "tool_part",
  "display": "Aluminum Bronze: Casting - Hoe Head",
  "display_ru": "Алюминиевая бронза: Отливка — Головка мотыги",
  "tab": "copper_age"
}
```

### aluminum_bronze_hammer_head

```json
{
  "name": "aluminum_bronze_hammer_head",
  "kind": "tool_part",
  "display": "Aluminum Bronze: Hammer Head",
  "display_ru": "Алюминиевая бронза: Головка молота",
  "tab": "copper_age"
}
```

### aluminum_bronze_cast_hammer_head

```json
{
  "name": "aluminum_bronze_cast_hammer_head",
  "kind": "tool_part",
  "display": "Aluminum Bronze: Casting - Hammer Head",
  "display_ru": "Алюминиевая бронза: Отливка — Головка молота",
  "tab": "copper_age"
}
```

### aluminum_bronze_standard_handle

```json
{
  "name": "aluminum_bronze_standard_handle",
  "kind": "tool_part",
  "display": "Aluminum Bronze: Rod",
  "display_ru": "Алюминиевая бронза: Стержень",
  "tab": "copper_age"
}
```

### aluminum_bronze_clamp

```json
{
  "name": "aluminum_bronze_clamp",
  "kind": "tool_part",
  "display": "Aluminum Bronze: Clamp",
  "display_ru": "Алюминиевая бронза: Хомут",
  "tab": "copper_age"
}
```

### aluminum_bronze_saw_head

```json
{
  "name": "aluminum_bronze_saw_head",
  "kind": "tool_part",
  "display": "Aluminum Bronze: Saw Blade",
  "display_ru": "Алюминиевая бронза: Полотно пилы",
  "tab": "copper_age"
}
```

### aluminum_bronze_cast_saw_head

```json
{
  "name": "aluminum_bronze_cast_saw_head",
  "kind": "tool_part",
  "display": "Aluminum Bronze: Casting - Saw Blade",
  "display_ru": "Алюминиевая бронза: Отливка — Полотно пилы",
  "tab": "copper_age"
}
```

### aluminum_bronze_sword_head

```json
{
  "name": "aluminum_bronze_sword_head",
  "kind": "tool_part",
  "display": "Aluminum Bronze: Sword Blade",
  "display_ru": "Алюминиевая бронза: Клинок меча",
  "tab": "copper_age"
}
```

### aluminum_bronze_cast_sword_head

```json
{
  "name": "aluminum_bronze_cast_sword_head",
  "kind": "tool_part",
  "display": "Aluminum Bronze: Casting - Sword Blade",
  "display_ru": "Алюминиевая бронза: Отливка — Клинок меча",
  "tab": "copper_age"
}
```

### aluminum_bronze_katana_head

```json
{
  "name": "aluminum_bronze_katana_head",
  "kind": "tool_part",
  "display": "Aluminum Bronze: Katana Blade",
  "display_ru": "Алюминиевая бронза: Клинок катаны",
  "tab": "copper_age"
}
```

### aluminum_bronze_cast_katana_head

```json
{
  "name": "aluminum_bronze_cast_katana_head",
  "kind": "tool_part",
  "display": "Aluminum Bronze: Casting - Katana Blade",
  "display_ru": "Алюминиевая бронза: Отливка — Клинок катаны",
  "tab": "copper_age"
}
```

### aluminum_bronze_machete_head

```json
{
  "name": "aluminum_bronze_machete_head",
  "kind": "tool_part",
  "display": "Aluminum Bronze: Machete Blade",
  "display_ru": "Алюминиевая бронза: Клинок мачете",
  "tab": "copper_age"
}
```

### aluminum_bronze_cast_machete_head

```json
{
  "name": "aluminum_bronze_cast_machete_head",
  "kind": "tool_part",
  "display": "Aluminum Bronze: Casting - Machete Blade",
  "display_ru": "Алюминиевая бронза: Отливка — Клинок мачете",
  "tab": "copper_age"
}
```

### aluminum_bronze_sickle_head

```json
{
  "name": "aluminum_bronze_sickle_head",
  "kind": "tool_part",
  "display": "Aluminum Bronze: Sickle Blade",
  "display_ru": "Алюминиевая бронза: Лезвие серпа",
  "tab": "copper_age"
}
```

### aluminum_bronze_cast_sickle_head

```json
{
  "name": "aluminum_bronze_cast_sickle_head",
  "kind": "tool_part",
  "display": "Aluminum Bronze: Casting - Sickle Blade",
  "display_ru": "Алюминиевая бронза: Отливка — Лезвие серпа",
  "tab": "copper_age"
}
```

### brass_pickaxe_head

```json
{
  "name": "brass_pickaxe_head",
  "kind": "tool_part",
  "display": "Brass: Pickaxe Head",
  "display_ru": "Латунь: Головка кирки",
  "tab": "copper_age"
}
```

### brass_cast_pickaxe_head

```json
{
  "name": "brass_cast_pickaxe_head",
  "kind": "tool_part",
  "display": "Brass: Casting - Pickaxe Head",
  "display_ru": "Латунь: Отливка — Головка кирки",
  "tab": "copper_age"
}
```

### brass_axe_head

```json
{
  "name": "brass_axe_head",
  "kind": "tool_part",
  "display": "Brass: Axe Head",
  "display_ru": "Латунь: Головка топора",
  "tab": "copper_age"
}
```

### brass_cast_axe_head

```json
{
  "name": "brass_cast_axe_head",
  "kind": "tool_part",
  "display": "Brass: Casting - Axe Head",
  "display_ru": "Латунь: Отливка — Головка топора",
  "tab": "copper_age"
}
```

### brass_shovel_head

```json
{
  "name": "brass_shovel_head",
  "kind": "tool_part",
  "display": "Brass: Shovel Head",
  "display_ru": "Латунь: Головка лопаты",
  "tab": "copper_age"
}
```

### brass_cast_shovel_head

```json
{
  "name": "brass_cast_shovel_head",
  "kind": "tool_part",
  "display": "Brass: Casting - Shovel Head",
  "display_ru": "Латунь: Отливка — Головка лопаты",
  "tab": "copper_age"
}
```

### brass_hoe_head

```json
{
  "name": "brass_hoe_head",
  "kind": "tool_part",
  "display": "Brass: Hoe Head",
  "display_ru": "Латунь: Головка мотыги",
  "tab": "copper_age"
}
```

### brass_cast_hoe_head

```json
{
  "name": "brass_cast_hoe_head",
  "kind": "tool_part",
  "display": "Brass: Casting - Hoe Head",
  "display_ru": "Латунь: Отливка — Головка мотыги",
  "tab": "copper_age"
}
```

### brass_hammer_head

```json
{
  "name": "brass_hammer_head",
  "kind": "tool_part",
  "display": "Brass: Hammer Head",
  "display_ru": "Латунь: Головка молота",
  "tab": "copper_age"
}
```

### brass_cast_hammer_head

```json
{
  "name": "brass_cast_hammer_head",
  "kind": "tool_part",
  "display": "Brass: Casting - Hammer Head",
  "display_ru": "Латунь: Отливка — Головка молота",
  "tab": "copper_age"
}
```

### brass_standard_handle

```json
{
  "name": "brass_standard_handle",
  "kind": "tool_part",
  "display": "Brass: Rod",
  "display_ru": "Латунь: Стержень",
  "tab": "copper_age"
}
```

### brass_clamp

```json
{
  "name": "brass_clamp",
  "kind": "tool_part",
  "display": "Brass: Clamp",
  "display_ru": "Латунь: Хомут",
  "tab": "copper_age"
}
```

### brass_saw_head

```json
{
  "name": "brass_saw_head",
  "kind": "tool_part",
  "display": "Brass: Saw Blade",
  "display_ru": "Латунь: Полотно пилы",
  "tab": "copper_age"
}
```

### brass_cast_saw_head

```json
{
  "name": "brass_cast_saw_head",
  "kind": "tool_part",
  "display": "Brass: Casting - Saw Blade",
  "display_ru": "Латунь: Отливка — Полотно пилы",
  "tab": "copper_age"
}
```

### brass_sword_head

```json
{
  "name": "brass_sword_head",
  "kind": "tool_part",
  "display": "Brass: Sword Blade",
  "display_ru": "Латунь: Клинок меча",
  "tab": "copper_age"
}
```

### brass_cast_sword_head

```json
{
  "name": "brass_cast_sword_head",
  "kind": "tool_part",
  "display": "Brass: Casting - Sword Blade",
  "display_ru": "Латунь: Отливка — Клинок меча",
  "tab": "copper_age"
}
```

### brass_katana_head

```json
{
  "name": "brass_katana_head",
  "kind": "tool_part",
  "display": "Brass: Katana Blade",
  "display_ru": "Латунь: Клинок катаны",
  "tab": "copper_age"
}
```

### brass_cast_katana_head

```json
{
  "name": "brass_cast_katana_head",
  "kind": "tool_part",
  "display": "Brass: Casting - Katana Blade",
  "display_ru": "Латунь: Отливка — Клинок катаны",
  "tab": "copper_age"
}
```

### brass_machete_head

```json
{
  "name": "brass_machete_head",
  "kind": "tool_part",
  "display": "Brass: Machete Blade",
  "display_ru": "Латунь: Клинок мачете",
  "tab": "copper_age"
}
```

### brass_cast_machete_head

```json
{
  "name": "brass_cast_machete_head",
  "kind": "tool_part",
  "display": "Brass: Casting - Machete Blade",
  "display_ru": "Латунь: Отливка — Клинок мачете",
  "tab": "copper_age"
}
```

### brass_sickle_head

```json
{
  "name": "brass_sickle_head",
  "kind": "tool_part",
  "display": "Brass: Sickle Blade",
  "display_ru": "Латунь: Лезвие серпа",
  "tab": "copper_age"
}
```

### brass_cast_sickle_head

```json
{
  "name": "brass_cast_sickle_head",
  "kind": "tool_part",
  "display": "Brass: Casting - Sickle Blade",
  "display_ru": "Латунь: Отливка — Лезвие серпа",
  "tab": "copper_age"
}
```

### bronze_pickaxe_head

```json
{
  "name": "bronze_pickaxe_head",
  "kind": "tool_part",
  "display": "Bronze: Pickaxe Head",
  "display_ru": "Бронза: Головка кирки",
  "tab": "copper_age"
}
```

### bronze_cast_pickaxe_head

```json
{
  "name": "bronze_cast_pickaxe_head",
  "kind": "tool_part",
  "display": "Bronze: Casting - Pickaxe Head",
  "display_ru": "Бронза: Отливка — Головка кирки",
  "tab": "copper_age"
}
```

### bronze_axe_head

```json
{
  "name": "bronze_axe_head",
  "kind": "tool_part",
  "display": "Bronze: Axe Head",
  "display_ru": "Бронза: Головка топора",
  "tab": "copper_age"
}
```

### bronze_cast_axe_head

```json
{
  "name": "bronze_cast_axe_head",
  "kind": "tool_part",
  "display": "Bronze: Casting - Axe Head",
  "display_ru": "Бронза: Отливка — Головка топора",
  "tab": "copper_age"
}
```

### bronze_shovel_head

```json
{
  "name": "bronze_shovel_head",
  "kind": "tool_part",
  "display": "Bronze: Shovel Head",
  "display_ru": "Бронза: Головка лопаты",
  "tab": "copper_age"
}
```

### bronze_cast_shovel_head

```json
{
  "name": "bronze_cast_shovel_head",
  "kind": "tool_part",
  "display": "Bronze: Casting - Shovel Head",
  "display_ru": "Бронза: Отливка — Головка лопаты",
  "tab": "copper_age"
}
```

### bronze_hoe_head

```json
{
  "name": "bronze_hoe_head",
  "kind": "tool_part",
  "display": "Bronze: Hoe Head",
  "display_ru": "Бронза: Головка мотыги",
  "tab": "copper_age"
}
```

### bronze_cast_hoe_head

```json
{
  "name": "bronze_cast_hoe_head",
  "kind": "tool_part",
  "display": "Bronze: Casting - Hoe Head",
  "display_ru": "Бронза: Отливка — Головка мотыги",
  "tab": "copper_age"
}
```

### bronze_hammer_head

```json
{
  "name": "bronze_hammer_head",
  "kind": "tool_part",
  "display": "Bronze: Hammer Head",
  "display_ru": "Бронза: Головка молота",
  "tab": "copper_age"
}
```

### bronze_cast_hammer_head

```json
{
  "name": "bronze_cast_hammer_head",
  "kind": "tool_part",
  "display": "Bronze: Casting - Hammer Head",
  "display_ru": "Бронза: Отливка — Головка молота",
  "tab": "copper_age"
}
```

### bronze_standard_handle

```json
{
  "name": "bronze_standard_handle",
  "kind": "tool_part",
  "display": "Bronze: Rod",
  "display_ru": "Бронза: Стержень",
  "tab": "copper_age"
}
```

### bronze_clamp

```json
{
  "name": "bronze_clamp",
  "kind": "tool_part",
  "display": "Bronze: Clamp",
  "display_ru": "Бронза: Хомут",
  "tab": "copper_age"
}
```

### bronze_saw_head

```json
{
  "name": "bronze_saw_head",
  "kind": "tool_part",
  "display": "Bronze: Saw Blade",
  "display_ru": "Бронза: Полотно пилы",
  "tab": "copper_age"
}
```

### bronze_cast_saw_head

```json
{
  "name": "bronze_cast_saw_head",
  "kind": "tool_part",
  "display": "Bronze: Casting - Saw Blade",
  "display_ru": "Бронза: Отливка — Полотно пилы",
  "tab": "copper_age"
}
```

### bronze_sword_head

```json
{
  "name": "bronze_sword_head",
  "kind": "tool_part",
  "display": "Bronze: Sword Blade",
  "display_ru": "Бронза: Клинок меча",
  "tab": "copper_age"
}
```

### bronze_cast_sword_head

```json
{
  "name": "bronze_cast_sword_head",
  "kind": "tool_part",
  "display": "Bronze: Casting - Sword Blade",
  "display_ru": "Бронза: Отливка — Клинок меча",
  "tab": "copper_age"
}
```

### bronze_katana_head

```json
{
  "name": "bronze_katana_head",
  "kind": "tool_part",
  "display": "Bronze: Katana Blade",
  "display_ru": "Бронза: Клинок катаны",
  "tab": "copper_age"
}
```

### bronze_cast_katana_head

```json
{
  "name": "bronze_cast_katana_head",
  "kind": "tool_part",
  "display": "Bronze: Casting - Katana Blade",
  "display_ru": "Бронза: Отливка — Клинок катаны",
  "tab": "copper_age"
}
```

### bronze_machete_head

```json
{
  "name": "bronze_machete_head",
  "kind": "tool_part",
  "display": "Bronze: Machete Blade",
  "display_ru": "Бронза: Клинок мачете",
  "tab": "copper_age"
}
```

### bronze_cast_machete_head

```json
{
  "name": "bronze_cast_machete_head",
  "kind": "tool_part",
  "display": "Bronze: Casting - Machete Blade",
  "display_ru": "Бронза: Отливка — Клинок мачете",
  "tab": "copper_age"
}
```

### bronze_sickle_head

```json
{
  "name": "bronze_sickle_head",
  "kind": "tool_part",
  "display": "Bronze: Sickle Blade",
  "display_ru": "Бронза: Лезвие серпа",
  "tab": "copper_age"
}
```

### bronze_cast_sickle_head

```json
{
  "name": "bronze_cast_sickle_head",
  "kind": "tool_part",
  "display": "Bronze: Casting - Sickle Blade",
  "display_ru": "Бронза: Отливка — Лезвие серпа",
  "tab": "copper_age"
}
```

### cast_iron_pickaxe_head

```json
{
  "name": "cast_iron_pickaxe_head",
  "kind": "tool_part",
  "display": "Cast Iron: Pickaxe Head",
  "display_ru": "Чугун: Головка кирки",
  "tab": "copper_age"
}
```

### cast_iron_cast_pickaxe_head

```json
{
  "name": "cast_iron_cast_pickaxe_head",
  "kind": "tool_part",
  "display": "Cast Iron: Casting - Pickaxe Head",
  "display_ru": "Чугун: Отливка — Головка кирки",
  "tab": "copper_age"
}
```

### cast_iron_axe_head

```json
{
  "name": "cast_iron_axe_head",
  "kind": "tool_part",
  "display": "Cast Iron: Axe Head",
  "display_ru": "Чугун: Головка топора",
  "tab": "copper_age"
}
```

### cast_iron_cast_axe_head

```json
{
  "name": "cast_iron_cast_axe_head",
  "kind": "tool_part",
  "display": "Cast Iron: Casting - Axe Head",
  "display_ru": "Чугун: Отливка — Головка топора",
  "tab": "copper_age"
}
```

### cast_iron_shovel_head

```json
{
  "name": "cast_iron_shovel_head",
  "kind": "tool_part",
  "display": "Cast Iron: Shovel Head",
  "display_ru": "Чугун: Головка лопаты",
  "tab": "copper_age"
}
```

### cast_iron_cast_shovel_head

```json
{
  "name": "cast_iron_cast_shovel_head",
  "kind": "tool_part",
  "display": "Cast Iron: Casting - Shovel Head",
  "display_ru": "Чугун: Отливка — Головка лопаты",
  "tab": "copper_age"
}
```

### cast_iron_hoe_head

```json
{
  "name": "cast_iron_hoe_head",
  "kind": "tool_part",
  "display": "Cast Iron: Hoe Head",
  "display_ru": "Чугун: Головка мотыги",
  "tab": "copper_age"
}
```

### cast_iron_cast_hoe_head

```json
{
  "name": "cast_iron_cast_hoe_head",
  "kind": "tool_part",
  "display": "Cast Iron: Casting - Hoe Head",
  "display_ru": "Чугун: Отливка — Головка мотыги",
  "tab": "copper_age"
}
```

### cast_iron_hammer_head

```json
{
  "name": "cast_iron_hammer_head",
  "kind": "tool_part",
  "display": "Cast Iron: Hammer Head",
  "display_ru": "Чугун: Головка молота",
  "tab": "copper_age"
}
```

### cast_iron_cast_hammer_head

```json
{
  "name": "cast_iron_cast_hammer_head",
  "kind": "tool_part",
  "display": "Cast Iron: Casting - Hammer Head",
  "display_ru": "Чугун: Отливка — Головка молота",
  "tab": "copper_age"
}
```

### cast_iron_standard_handle

```json
{
  "name": "cast_iron_standard_handle",
  "kind": "tool_part",
  "display": "Cast Iron: Rod",
  "display_ru": "Чугун: Стержень",
  "tab": "copper_age"
}
```

### cast_iron_clamp

```json
{
  "name": "cast_iron_clamp",
  "kind": "tool_part",
  "display": "Cast Iron: Clamp",
  "display_ru": "Чугун: Хомут",
  "tab": "copper_age"
}
```

### cast_iron_saw_head

```json
{
  "name": "cast_iron_saw_head",
  "kind": "tool_part",
  "display": "Cast Iron: Saw Blade",
  "display_ru": "Чугун: Полотно пилы",
  "tab": "copper_age"
}
```

### cast_iron_cast_saw_head

```json
{
  "name": "cast_iron_cast_saw_head",
  "kind": "tool_part",
  "display": "Cast Iron: Casting - Saw Blade",
  "display_ru": "Чугун: Отливка — Полотно пилы",
  "tab": "copper_age"
}
```

### cast_iron_sword_head

```json
{
  "name": "cast_iron_sword_head",
  "kind": "tool_part",
  "display": "Cast Iron: Sword Blade",
  "display_ru": "Чугун: Клинок меча",
  "tab": "copper_age"
}
```

### cast_iron_cast_sword_head

```json
{
  "name": "cast_iron_cast_sword_head",
  "kind": "tool_part",
  "display": "Cast Iron: Casting - Sword Blade",
  "display_ru": "Чугун: Отливка — Клинок меча",
  "tab": "copper_age"
}
```

### cast_iron_katana_head

```json
{
  "name": "cast_iron_katana_head",
  "kind": "tool_part",
  "display": "Cast Iron: Katana Blade",
  "display_ru": "Чугун: Клинок катаны",
  "tab": "copper_age"
}
```

### cast_iron_cast_katana_head

```json
{
  "name": "cast_iron_cast_katana_head",
  "kind": "tool_part",
  "display": "Cast Iron: Casting - Katana Blade",
  "display_ru": "Чугун: Отливка — Клинок катаны",
  "tab": "copper_age"
}
```

### cast_iron_machete_head

```json
{
  "name": "cast_iron_machete_head",
  "kind": "tool_part",
  "display": "Cast Iron: Machete Blade",
  "display_ru": "Чугун: Клинок мачете",
  "tab": "copper_age"
}
```

### cast_iron_cast_machete_head

```json
{
  "name": "cast_iron_cast_machete_head",
  "kind": "tool_part",
  "display": "Cast Iron: Casting - Machete Blade",
  "display_ru": "Чугун: Отливка — Клинок мачете",
  "tab": "copper_age"
}
```

### cast_iron_sickle_head

```json
{
  "name": "cast_iron_sickle_head",
  "kind": "tool_part",
  "display": "Cast Iron: Sickle Blade",
  "display_ru": "Чугун: Лезвие серпа",
  "tab": "copper_age"
}
```

### cast_iron_cast_sickle_head

```json
{
  "name": "cast_iron_cast_sickle_head",
  "kind": "tool_part",
  "display": "Cast Iron: Casting - Sickle Blade",
  "display_ru": "Чугун: Отливка — Лезвие серпа",
  "tab": "copper_age"
}
```

### chromium_pickaxe_head

```json
{
  "name": "chromium_pickaxe_head",
  "kind": "tool_part",
  "display": "Chromium: Pickaxe Head",
  "display_ru": "Хром: Головка кирки",
  "tab": "copper_age"
}
```

### chromium_cast_pickaxe_head

```json
{
  "name": "chromium_cast_pickaxe_head",
  "kind": "tool_part",
  "display": "Chromium: Casting - Pickaxe Head",
  "display_ru": "Хром: Отливка — Головка кирки",
  "tab": "copper_age"
}
```

### chromium_axe_head

```json
{
  "name": "chromium_axe_head",
  "kind": "tool_part",
  "display": "Chromium: Axe Head",
  "display_ru": "Хром: Головка топора",
  "tab": "copper_age"
}
```

### chromium_cast_axe_head

```json
{
  "name": "chromium_cast_axe_head",
  "kind": "tool_part",
  "display": "Chromium: Casting - Axe Head",
  "display_ru": "Хром: Отливка — Головка топора",
  "tab": "copper_age"
}
```

### chromium_shovel_head

```json
{
  "name": "chromium_shovel_head",
  "kind": "tool_part",
  "display": "Chromium: Shovel Head",
  "display_ru": "Хром: Головка лопаты",
  "tab": "copper_age"
}
```

### chromium_cast_shovel_head

```json
{
  "name": "chromium_cast_shovel_head",
  "kind": "tool_part",
  "display": "Chromium: Casting - Shovel Head",
  "display_ru": "Хром: Отливка — Головка лопаты",
  "tab": "copper_age"
}
```

### chromium_hoe_head

```json
{
  "name": "chromium_hoe_head",
  "kind": "tool_part",
  "display": "Chromium: Hoe Head",
  "display_ru": "Хром: Головка мотыги",
  "tab": "copper_age"
}
```

### chromium_cast_hoe_head

```json
{
  "name": "chromium_cast_hoe_head",
  "kind": "tool_part",
  "display": "Chromium: Casting - Hoe Head",
  "display_ru": "Хром: Отливка — Головка мотыги",
  "tab": "copper_age"
}
```

### chromium_hammer_head

```json
{
  "name": "chromium_hammer_head",
  "kind": "tool_part",
  "display": "Chromium: Hammer Head",
  "display_ru": "Хром: Головка молота",
  "tab": "copper_age"
}
```

### chromium_cast_hammer_head

```json
{
  "name": "chromium_cast_hammer_head",
  "kind": "tool_part",
  "display": "Chromium: Casting - Hammer Head",
  "display_ru": "Хром: Отливка — Головка молота",
  "tab": "copper_age"
}
```

### chromium_standard_handle

```json
{
  "name": "chromium_standard_handle",
  "kind": "tool_part",
  "display": "Chromium: Rod",
  "display_ru": "Хром: Стержень",
  "tab": "copper_age"
}
```

### chromium_clamp

```json
{
  "name": "chromium_clamp",
  "kind": "tool_part",
  "display": "Chromium: Clamp",
  "display_ru": "Хром: Хомут",
  "tab": "copper_age"
}
```

### chromium_saw_head

```json
{
  "name": "chromium_saw_head",
  "kind": "tool_part",
  "display": "Chromium: Saw Blade",
  "display_ru": "Хром: Полотно пилы",
  "tab": "copper_age"
}
```

### chromium_cast_saw_head

```json
{
  "name": "chromium_cast_saw_head",
  "kind": "tool_part",
  "display": "Chromium: Casting - Saw Blade",
  "display_ru": "Хром: Отливка — Полотно пилы",
  "tab": "copper_age"
}
```

### chromium_sword_head

```json
{
  "name": "chromium_sword_head",
  "kind": "tool_part",
  "display": "Chromium: Sword Blade",
  "display_ru": "Хром: Клинок меча",
  "tab": "copper_age"
}
```

### chromium_cast_sword_head

```json
{
  "name": "chromium_cast_sword_head",
  "kind": "tool_part",
  "display": "Chromium: Casting - Sword Blade",
  "display_ru": "Хром: Отливка — Клинок меча",
  "tab": "copper_age"
}
```

### chromium_katana_head

```json
{
  "name": "chromium_katana_head",
  "kind": "tool_part",
  "display": "Chromium: Katana Blade",
  "display_ru": "Хром: Клинок катаны",
  "tab": "copper_age"
}
```

### chromium_cast_katana_head

```json
{
  "name": "chromium_cast_katana_head",
  "kind": "tool_part",
  "display": "Chromium: Casting - Katana Blade",
  "display_ru": "Хром: Отливка — Клинок катаны",
  "tab": "copper_age"
}
```

### chromium_machete_head

```json
{
  "name": "chromium_machete_head",
  "kind": "tool_part",
  "display": "Chromium: Machete Blade",
  "display_ru": "Хром: Клинок мачете",
  "tab": "copper_age"
}
```

### chromium_cast_machete_head

```json
{
  "name": "chromium_cast_machete_head",
  "kind": "tool_part",
  "display": "Chromium: Casting - Machete Blade",
  "display_ru": "Хром: Отливка — Клинок мачете",
  "tab": "copper_age"
}
```

### chromium_sickle_head

```json
{
  "name": "chromium_sickle_head",
  "kind": "tool_part",
  "display": "Chromium: Sickle Blade",
  "display_ru": "Хром: Лезвие серпа",
  "tab": "copper_age"
}
```

### chromium_cast_sickle_head

```json
{
  "name": "chromium_cast_sickle_head",
  "kind": "tool_part",
  "display": "Chromium: Casting - Sickle Blade",
  "display_ru": "Хром: Отливка — Лезвие серпа",
  "tab": "copper_age"
}
```

### constantan_pickaxe_head

```json
{
  "name": "constantan_pickaxe_head",
  "kind": "tool_part",
  "display": "Constantan: Pickaxe Head",
  "display_ru": "Константан: Головка кирки",
  "tab": "copper_age"
}
```

### constantan_cast_pickaxe_head

```json
{
  "name": "constantan_cast_pickaxe_head",
  "kind": "tool_part",
  "display": "Constantan: Casting - Pickaxe Head",
  "display_ru": "Константан: Отливка — Головка кирки",
  "tab": "copper_age"
}
```

### constantan_axe_head

```json
{
  "name": "constantan_axe_head",
  "kind": "tool_part",
  "display": "Constantan: Axe Head",
  "display_ru": "Константан: Головка топора",
  "tab": "copper_age"
}
```

### constantan_cast_axe_head

```json
{
  "name": "constantan_cast_axe_head",
  "kind": "tool_part",
  "display": "Constantan: Casting - Axe Head",
  "display_ru": "Константан: Отливка — Головка топора",
  "tab": "copper_age"
}
```

### constantan_shovel_head

```json
{
  "name": "constantan_shovel_head",
  "kind": "tool_part",
  "display": "Constantan: Shovel Head",
  "display_ru": "Константан: Головка лопаты",
  "tab": "copper_age"
}
```

### constantan_cast_shovel_head

```json
{
  "name": "constantan_cast_shovel_head",
  "kind": "tool_part",
  "display": "Constantan: Casting - Shovel Head",
  "display_ru": "Константан: Отливка — Головка лопаты",
  "tab": "copper_age"
}
```

### constantan_hoe_head

```json
{
  "name": "constantan_hoe_head",
  "kind": "tool_part",
  "display": "Constantan: Hoe Head",
  "display_ru": "Константан: Головка мотыги",
  "tab": "copper_age"
}
```

### constantan_cast_hoe_head

```json
{
  "name": "constantan_cast_hoe_head",
  "kind": "tool_part",
  "display": "Constantan: Casting - Hoe Head",
  "display_ru": "Константан: Отливка — Головка мотыги",
  "tab": "copper_age"
}
```

### constantan_hammer_head

```json
{
  "name": "constantan_hammer_head",
  "kind": "tool_part",
  "display": "Constantan: Hammer Head",
  "display_ru": "Константан: Головка молота",
  "tab": "copper_age"
}
```

### constantan_cast_hammer_head

```json
{
  "name": "constantan_cast_hammer_head",
  "kind": "tool_part",
  "display": "Constantan: Casting - Hammer Head",
  "display_ru": "Константан: Отливка — Головка молота",
  "tab": "copper_age"
}
```

### constantan_standard_handle

```json
{
  "name": "constantan_standard_handle",
  "kind": "tool_part",
  "display": "Constantan: Rod",
  "display_ru": "Константан: Стержень",
  "tab": "copper_age"
}
```

### constantan_clamp

```json
{
  "name": "constantan_clamp",
  "kind": "tool_part",
  "display": "Constantan: Clamp",
  "display_ru": "Константан: Хомут",
  "tab": "copper_age"
}
```

### constantan_saw_head

```json
{
  "name": "constantan_saw_head",
  "kind": "tool_part",
  "display": "Constantan: Saw Blade",
  "display_ru": "Константан: Полотно пилы",
  "tab": "copper_age"
}
```

### constantan_cast_saw_head

```json
{
  "name": "constantan_cast_saw_head",
  "kind": "tool_part",
  "display": "Constantan: Casting - Saw Blade",
  "display_ru": "Константан: Отливка — Полотно пилы",
  "tab": "copper_age"
}
```

### constantan_sword_head

```json
{
  "name": "constantan_sword_head",
  "kind": "tool_part",
  "display": "Constantan: Sword Blade",
  "display_ru": "Константан: Клинок меча",
  "tab": "copper_age"
}
```

### constantan_cast_sword_head

```json
{
  "name": "constantan_cast_sword_head",
  "kind": "tool_part",
  "display": "Constantan: Casting - Sword Blade",
  "display_ru": "Константан: Отливка — Клинок меча",
  "tab": "copper_age"
}
```

### constantan_katana_head

```json
{
  "name": "constantan_katana_head",
  "kind": "tool_part",
  "display": "Constantan: Katana Blade",
  "display_ru": "Константан: Клинок катаны",
  "tab": "copper_age"
}
```

### constantan_cast_katana_head

```json
{
  "name": "constantan_cast_katana_head",
  "kind": "tool_part",
  "display": "Constantan: Casting - Katana Blade",
  "display_ru": "Константан: Отливка — Клинок катаны",
  "tab": "copper_age"
}
```

### constantan_machete_head

```json
{
  "name": "constantan_machete_head",
  "kind": "tool_part",
  "display": "Constantan: Machete Blade",
  "display_ru": "Константан: Клинок мачете",
  "tab": "copper_age"
}
```

### constantan_cast_machete_head

```json
{
  "name": "constantan_cast_machete_head",
  "kind": "tool_part",
  "display": "Constantan: Casting - Machete Blade",
  "display_ru": "Константан: Отливка — Клинок мачете",
  "tab": "copper_age"
}
```

### constantan_sickle_head

```json
{
  "name": "constantan_sickle_head",
  "kind": "tool_part",
  "display": "Constantan: Sickle Blade",
  "display_ru": "Константан: Лезвие серпа",
  "tab": "copper_age"
}
```

### constantan_cast_sickle_head

```json
{
  "name": "constantan_cast_sickle_head",
  "kind": "tool_part",
  "display": "Constantan: Casting - Sickle Blade",
  "display_ru": "Константан: Отливка — Лезвие серпа",
  "tab": "copper_age"
}
```

### copper_pickaxe_head

```json
{
  "name": "copper_pickaxe_head",
  "kind": "tool_part",
  "display": "Copper: Pickaxe Head",
  "display_ru": "Медь: Головка кирки",
  "tab": "copper_age"
}
```

### copper_cast_pickaxe_head

```json
{
  "name": "copper_cast_pickaxe_head",
  "kind": "tool_part",
  "display": "Copper: Casting - Pickaxe Head",
  "display_ru": "Медь: Отливка — Головка кирки",
  "tab": "copper_age"
}
```

### copper_axe_head

```json
{
  "name": "copper_axe_head",
  "kind": "tool_part",
  "display": "Copper: Axe Head",
  "display_ru": "Медь: Головка топора",
  "tab": "copper_age"
}
```

### copper_cast_axe_head

```json
{
  "name": "copper_cast_axe_head",
  "kind": "tool_part",
  "display": "Copper: Casting - Axe Head",
  "display_ru": "Медь: Отливка — Головка топора",
  "tab": "copper_age"
}
```

### copper_shovel_head

```json
{
  "name": "copper_shovel_head",
  "kind": "tool_part",
  "display": "Copper: Shovel Head",
  "display_ru": "Медь: Головка лопаты",
  "tab": "copper_age"
}
```

### copper_cast_shovel_head

```json
{
  "name": "copper_cast_shovel_head",
  "kind": "tool_part",
  "display": "Copper: Casting - Shovel Head",
  "display_ru": "Медь: Отливка — Головка лопаты",
  "tab": "copper_age"
}
```

### copper_hoe_head

```json
{
  "name": "copper_hoe_head",
  "kind": "tool_part",
  "display": "Copper: Hoe Head",
  "display_ru": "Медь: Головка мотыги",
  "tab": "copper_age"
}
```

### copper_cast_hoe_head

```json
{
  "name": "copper_cast_hoe_head",
  "kind": "tool_part",
  "display": "Copper: Casting - Hoe Head",
  "display_ru": "Медь: Отливка — Головка мотыги",
  "tab": "copper_age"
}
```

### copper_hammer_head

```json
{
  "name": "copper_hammer_head",
  "kind": "tool_part",
  "display": "Copper: Hammer Head",
  "display_ru": "Медь: Головка молота",
  "tab": "copper_age"
}
```

### copper_cast_hammer_head

```json
{
  "name": "copper_cast_hammer_head",
  "kind": "tool_part",
  "display": "Copper: Casting - Hammer Head",
  "display_ru": "Медь: Отливка — Головка молота",
  "tab": "copper_age"
}
```

### copper_standard_handle

```json
{
  "name": "copper_standard_handle",
  "kind": "tool_part",
  "display": "Copper: Rod",
  "display_ru": "Медь: Стержень",
  "tab": "copper_age"
}
```

### copper_clamp

```json
{
  "name": "copper_clamp",
  "kind": "tool_part",
  "display": "Copper: Clamp",
  "display_ru": "Медь: Хомут",
  "tab": "copper_age"
}
```

### copper_saw_head

```json
{
  "name": "copper_saw_head",
  "kind": "tool_part",
  "display": "Copper: Saw Blade",
  "display_ru": "Медь: Полотно пилы",
  "tab": "copper_age"
}
```

### copper_cast_saw_head

```json
{
  "name": "copper_cast_saw_head",
  "kind": "tool_part",
  "display": "Copper: Casting - Saw Blade",
  "display_ru": "Медь: Отливка — Полотно пилы",
  "tab": "copper_age"
}
```

### copper_sword_head

```json
{
  "name": "copper_sword_head",
  "kind": "tool_part",
  "display": "Copper: Sword Blade",
  "display_ru": "Медь: Клинок меча",
  "tab": "copper_age"
}
```

### copper_cast_sword_head

```json
{
  "name": "copper_cast_sword_head",
  "kind": "tool_part",
  "display": "Copper: Casting - Sword Blade",
  "display_ru": "Медь: Отливка — Клинок меча",
  "tab": "copper_age"
}
```

### copper_katana_head

```json
{
  "name": "copper_katana_head",
  "kind": "tool_part",
  "display": "Copper: Katana Blade",
  "display_ru": "Медь: Клинок катаны",
  "tab": "copper_age"
}
```

### copper_cast_katana_head

```json
{
  "name": "copper_cast_katana_head",
  "kind": "tool_part",
  "display": "Copper: Casting - Katana Blade",
  "display_ru": "Медь: Отливка — Клинок катаны",
  "tab": "copper_age"
}
```

### copper_machete_head

```json
{
  "name": "copper_machete_head",
  "kind": "tool_part",
  "display": "Copper: Machete Blade",
  "display_ru": "Медь: Клинок мачете",
  "tab": "copper_age"
}
```

### copper_cast_machete_head

```json
{
  "name": "copper_cast_machete_head",
  "kind": "tool_part",
  "display": "Copper: Casting - Machete Blade",
  "display_ru": "Медь: Отливка — Клинок мачете",
  "tab": "copper_age"
}
```

### copper_sickle_head

```json
{
  "name": "copper_sickle_head",
  "kind": "tool_part",
  "display": "Copper: Sickle Blade",
  "display_ru": "Медь: Лезвие серпа",
  "tab": "copper_age"
}
```

### copper_cast_sickle_head

```json
{
  "name": "copper_cast_sickle_head",
  "kind": "tool_part",
  "display": "Copper: Casting - Sickle Blade",
  "display_ru": "Медь: Отливка — Лезвие серпа",
  "tab": "copper_age"
}
```

### cord_binding

```json
{
  "name": "cord_binding",
  "kind": "tool_part",
  "display": "Cord: Binding",
  "display_ru": "Верёвка: Крепление",
  "tab": "copper_age"
}
```

### cupronickel_pickaxe_head

```json
{
  "name": "cupronickel_pickaxe_head",
  "kind": "tool_part",
  "display": "Cupronickel: Pickaxe Head",
  "display_ru": "Мельхиор: Головка кирки",
  "tab": "copper_age"
}
```

### cupronickel_cast_pickaxe_head

```json
{
  "name": "cupronickel_cast_pickaxe_head",
  "kind": "tool_part",
  "display": "Cupronickel: Casting - Pickaxe Head",
  "display_ru": "Мельхиор: Отливка — Головка кирки",
  "tab": "copper_age"
}
```

### cupronickel_axe_head

```json
{
  "name": "cupronickel_axe_head",
  "kind": "tool_part",
  "display": "Cupronickel: Axe Head",
  "display_ru": "Мельхиор: Головка топора",
  "tab": "copper_age"
}
```

### cupronickel_cast_axe_head

```json
{
  "name": "cupronickel_cast_axe_head",
  "kind": "tool_part",
  "display": "Cupronickel: Casting - Axe Head",
  "display_ru": "Мельхиор: Отливка — Головка топора",
  "tab": "copper_age"
}
```

### cupronickel_shovel_head

```json
{
  "name": "cupronickel_shovel_head",
  "kind": "tool_part",
  "display": "Cupronickel: Shovel Head",
  "display_ru": "Мельхиор: Головка лопаты",
  "tab": "copper_age"
}
```

### cupronickel_cast_shovel_head

```json
{
  "name": "cupronickel_cast_shovel_head",
  "kind": "tool_part",
  "display": "Cupronickel: Casting - Shovel Head",
  "display_ru": "Мельхиор: Отливка — Головка лопаты",
  "tab": "copper_age"
}
```

### cupronickel_hoe_head

```json
{
  "name": "cupronickel_hoe_head",
  "kind": "tool_part",
  "display": "Cupronickel: Hoe Head",
  "display_ru": "Мельхиор: Головка мотыги",
  "tab": "copper_age"
}
```

### cupronickel_cast_hoe_head

```json
{
  "name": "cupronickel_cast_hoe_head",
  "kind": "tool_part",
  "display": "Cupronickel: Casting - Hoe Head",
  "display_ru": "Мельхиор: Отливка — Головка мотыги",
  "tab": "copper_age"
}
```

### cupronickel_hammer_head

```json
{
  "name": "cupronickel_hammer_head",
  "kind": "tool_part",
  "display": "Cupronickel: Hammer Head",
  "display_ru": "Мельхиор: Головка молота",
  "tab": "copper_age"
}
```

### cupronickel_cast_hammer_head

```json
{
  "name": "cupronickel_cast_hammer_head",
  "kind": "tool_part",
  "display": "Cupronickel: Casting - Hammer Head",
  "display_ru": "Мельхиор: Отливка — Головка молота",
  "tab": "copper_age"
}
```

### cupronickel_standard_handle

```json
{
  "name": "cupronickel_standard_handle",
  "kind": "tool_part",
  "display": "Cupronickel: Rod",
  "display_ru": "Мельхиор: Стержень",
  "tab": "copper_age"
}
```

### cupronickel_clamp

```json
{
  "name": "cupronickel_clamp",
  "kind": "tool_part",
  "display": "Cupronickel: Clamp",
  "display_ru": "Мельхиор: Хомут",
  "tab": "copper_age"
}
```

### cupronickel_saw_head

```json
{
  "name": "cupronickel_saw_head",
  "kind": "tool_part",
  "display": "Cupronickel: Saw Blade",
  "display_ru": "Мельхиор: Полотно пилы",
  "tab": "copper_age"
}
```

### cupronickel_cast_saw_head

```json
{
  "name": "cupronickel_cast_saw_head",
  "kind": "tool_part",
  "display": "Cupronickel: Casting - Saw Blade",
  "display_ru": "Мельхиор: Отливка — Полотно пилы",
  "tab": "copper_age"
}
```

### cupronickel_sword_head

```json
{
  "name": "cupronickel_sword_head",
  "kind": "tool_part",
  "display": "Cupronickel: Sword Blade",
  "display_ru": "Мельхиор: Клинок меча",
  "tab": "copper_age"
}
```

### cupronickel_cast_sword_head

```json
{
  "name": "cupronickel_cast_sword_head",
  "kind": "tool_part",
  "display": "Cupronickel: Casting - Sword Blade",
  "display_ru": "Мельхиор: Отливка — Клинок меча",
  "tab": "copper_age"
}
```

### cupronickel_katana_head

```json
{
  "name": "cupronickel_katana_head",
  "kind": "tool_part",
  "display": "Cupronickel: Katana Blade",
  "display_ru": "Мельхиор: Клинок катаны",
  "tab": "copper_age"
}
```

### cupronickel_cast_katana_head

```json
{
  "name": "cupronickel_cast_katana_head",
  "kind": "tool_part",
  "display": "Cupronickel: Casting - Katana Blade",
  "display_ru": "Мельхиор: Отливка — Клинок катаны",
  "tab": "copper_age"
}
```

### cupronickel_machete_head

```json
{
  "name": "cupronickel_machete_head",
  "kind": "tool_part",
  "display": "Cupronickel: Machete Blade",
  "display_ru": "Мельхиор: Клинок мачете",
  "tab": "copper_age"
}
```

### cupronickel_cast_machete_head

```json
{
  "name": "cupronickel_cast_machete_head",
  "kind": "tool_part",
  "display": "Cupronickel: Casting - Machete Blade",
  "display_ru": "Мельхиор: Отливка — Клинок мачете",
  "tab": "copper_age"
}
```

### cupronickel_sickle_head

```json
{
  "name": "cupronickel_sickle_head",
  "kind": "tool_part",
  "display": "Cupronickel: Sickle Blade",
  "display_ru": "Мельхиор: Лезвие серпа",
  "tab": "copper_age"
}
```

### cupronickel_cast_sickle_head

```json
{
  "name": "cupronickel_cast_sickle_head",
  "kind": "tool_part",
  "display": "Cupronickel: Casting - Sickle Blade",
  "display_ru": "Мельхиор: Отливка — Лезвие серпа",
  "tab": "copper_age"
}
```

### duralumin_pickaxe_head

```json
{
  "name": "duralumin_pickaxe_head",
  "kind": "tool_part",
  "display": "Duralumin: Pickaxe Head",
  "display_ru": "Дюралюминий: Головка кирки",
  "tab": "copper_age"
}
```

### duralumin_cast_pickaxe_head

```json
{
  "name": "duralumin_cast_pickaxe_head",
  "kind": "tool_part",
  "display": "Duralumin: Casting - Pickaxe Head",
  "display_ru": "Дюралюминий: Отливка — Головка кирки",
  "tab": "copper_age"
}
```

### duralumin_axe_head

```json
{
  "name": "duralumin_axe_head",
  "kind": "tool_part",
  "display": "Duralumin: Axe Head",
  "display_ru": "Дюралюминий: Головка топора",
  "tab": "copper_age"
}
```

### duralumin_cast_axe_head

```json
{
  "name": "duralumin_cast_axe_head",
  "kind": "tool_part",
  "display": "Duralumin: Casting - Axe Head",
  "display_ru": "Дюралюминий: Отливка — Головка топора",
  "tab": "copper_age"
}
```

### duralumin_shovel_head

```json
{
  "name": "duralumin_shovel_head",
  "kind": "tool_part",
  "display": "Duralumin: Shovel Head",
  "display_ru": "Дюралюминий: Головка лопаты",
  "tab": "copper_age"
}
```

### duralumin_cast_shovel_head

```json
{
  "name": "duralumin_cast_shovel_head",
  "kind": "tool_part",
  "display": "Duralumin: Casting - Shovel Head",
  "display_ru": "Дюралюминий: Отливка — Головка лопаты",
  "tab": "copper_age"
}
```

### duralumin_hoe_head

```json
{
  "name": "duralumin_hoe_head",
  "kind": "tool_part",
  "display": "Duralumin: Hoe Head",
  "display_ru": "Дюралюминий: Головка мотыги",
  "tab": "copper_age"
}
```

### duralumin_cast_hoe_head

```json
{
  "name": "duralumin_cast_hoe_head",
  "kind": "tool_part",
  "display": "Duralumin: Casting - Hoe Head",
  "display_ru": "Дюралюминий: Отливка — Головка мотыги",
  "tab": "copper_age"
}
```

### duralumin_hammer_head

```json
{
  "name": "duralumin_hammer_head",
  "kind": "tool_part",
  "display": "Duralumin: Hammer Head",
  "display_ru": "Дюралюминий: Головка молота",
  "tab": "copper_age"
}
```

### duralumin_cast_hammer_head

```json
{
  "name": "duralumin_cast_hammer_head",
  "kind": "tool_part",
  "display": "Duralumin: Casting - Hammer Head",
  "display_ru": "Дюралюминий: Отливка — Головка молота",
  "tab": "copper_age"
}
```

### duralumin_standard_handle

```json
{
  "name": "duralumin_standard_handle",
  "kind": "tool_part",
  "display": "Duralumin: Rod",
  "display_ru": "Дюралюминий: Стержень",
  "tab": "copper_age"
}
```

### duralumin_clamp

```json
{
  "name": "duralumin_clamp",
  "kind": "tool_part",
  "display": "Duralumin: Clamp",
  "display_ru": "Дюралюминий: Хомут",
  "tab": "copper_age"
}
```

### duralumin_saw_head

```json
{
  "name": "duralumin_saw_head",
  "kind": "tool_part",
  "display": "Duralumin: Saw Blade",
  "display_ru": "Дюралюминий: Полотно пилы",
  "tab": "copper_age"
}
```

### duralumin_cast_saw_head

```json
{
  "name": "duralumin_cast_saw_head",
  "kind": "tool_part",
  "display": "Duralumin: Casting - Saw Blade",
  "display_ru": "Дюралюминий: Отливка — Полотно пилы",
  "tab": "copper_age"
}
```

### duralumin_sword_head

```json
{
  "name": "duralumin_sword_head",
  "kind": "tool_part",
  "display": "Duralumin: Sword Blade",
  "display_ru": "Дюралюминий: Клинок меча",
  "tab": "copper_age"
}
```

### duralumin_cast_sword_head

```json
{
  "name": "duralumin_cast_sword_head",
  "kind": "tool_part",
  "display": "Duralumin: Casting - Sword Blade",
  "display_ru": "Дюралюминий: Отливка — Клинок меча",
  "tab": "copper_age"
}
```

### duralumin_katana_head

```json
{
  "name": "duralumin_katana_head",
  "kind": "tool_part",
  "display": "Duralumin: Katana Blade",
  "display_ru": "Дюралюминий: Клинок катаны",
  "tab": "copper_age"
}
```

### duralumin_cast_katana_head

```json
{
  "name": "duralumin_cast_katana_head",
  "kind": "tool_part",
  "display": "Duralumin: Casting - Katana Blade",
  "display_ru": "Дюралюминий: Отливка — Клинок катаны",
  "tab": "copper_age"
}
```

### duralumin_machete_head

```json
{
  "name": "duralumin_machete_head",
  "kind": "tool_part",
  "display": "Duralumin: Machete Blade",
  "display_ru": "Дюралюминий: Клинок мачете",
  "tab": "copper_age"
}
```

### duralumin_cast_machete_head

```json
{
  "name": "duralumin_cast_machete_head",
  "kind": "tool_part",
  "display": "Duralumin: Casting - Machete Blade",
  "display_ru": "Дюралюминий: Отливка — Клинок мачете",
  "tab": "copper_age"
}
```

### duralumin_sickle_head

```json
{
  "name": "duralumin_sickle_head",
  "kind": "tool_part",
  "display": "Duralumin: Sickle Blade",
  "display_ru": "Дюралюминий: Лезвие серпа",
  "tab": "copper_age"
}
```

### duralumin_cast_sickle_head

```json
{
  "name": "duralumin_cast_sickle_head",
  "kind": "tool_part",
  "display": "Duralumin: Casting - Sickle Blade",
  "display_ru": "Дюралюминий: Отливка — Лезвие серпа",
  "tab": "copper_age"
}
```

### electrum_pickaxe_head

```json
{
  "name": "electrum_pickaxe_head",
  "kind": "tool_part",
  "display": "Electrum: Pickaxe Head",
  "display_ru": "Электрум: Головка кирки",
  "tab": "copper_age"
}
```

### electrum_cast_pickaxe_head

```json
{
  "name": "electrum_cast_pickaxe_head",
  "kind": "tool_part",
  "display": "Electrum: Casting - Pickaxe Head",
  "display_ru": "Электрум: Отливка — Головка кирки",
  "tab": "copper_age"
}
```

### electrum_axe_head

```json
{
  "name": "electrum_axe_head",
  "kind": "tool_part",
  "display": "Electrum: Axe Head",
  "display_ru": "Электрум: Головка топора",
  "tab": "copper_age"
}
```

### electrum_cast_axe_head

```json
{
  "name": "electrum_cast_axe_head",
  "kind": "tool_part",
  "display": "Electrum: Casting - Axe Head",
  "display_ru": "Электрум: Отливка — Головка топора",
  "tab": "copper_age"
}
```

### electrum_shovel_head

```json
{
  "name": "electrum_shovel_head",
  "kind": "tool_part",
  "display": "Electrum: Shovel Head",
  "display_ru": "Электрум: Головка лопаты",
  "tab": "copper_age"
}
```

### electrum_cast_shovel_head

```json
{
  "name": "electrum_cast_shovel_head",
  "kind": "tool_part",
  "display": "Electrum: Casting - Shovel Head",
  "display_ru": "Электрум: Отливка — Головка лопаты",
  "tab": "copper_age"
}
```

### electrum_hoe_head

```json
{
  "name": "electrum_hoe_head",
  "kind": "tool_part",
  "display": "Electrum: Hoe Head",
  "display_ru": "Электрум: Головка мотыги",
  "tab": "copper_age"
}
```

### electrum_cast_hoe_head

```json
{
  "name": "electrum_cast_hoe_head",
  "kind": "tool_part",
  "display": "Electrum: Casting - Hoe Head",
  "display_ru": "Электрум: Отливка — Головка мотыги",
  "tab": "copper_age"
}
```

### electrum_hammer_head

```json
{
  "name": "electrum_hammer_head",
  "kind": "tool_part",
  "display": "Electrum: Hammer Head",
  "display_ru": "Электрум: Головка молота",
  "tab": "copper_age"
}
```

### electrum_cast_hammer_head

```json
{
  "name": "electrum_cast_hammer_head",
  "kind": "tool_part",
  "display": "Electrum: Casting - Hammer Head",
  "display_ru": "Электрум: Отливка — Головка молота",
  "tab": "copper_age"
}
```

### electrum_standard_handle

```json
{
  "name": "electrum_standard_handle",
  "kind": "tool_part",
  "display": "Electrum: Rod",
  "display_ru": "Электрум: Стержень",
  "tab": "copper_age"
}
```

### electrum_clamp

```json
{
  "name": "electrum_clamp",
  "kind": "tool_part",
  "display": "Electrum: Clamp",
  "display_ru": "Электрум: Хомут",
  "tab": "copper_age"
}
```

### electrum_saw_head

```json
{
  "name": "electrum_saw_head",
  "kind": "tool_part",
  "display": "Electrum: Saw Blade",
  "display_ru": "Электрум: Полотно пилы",
  "tab": "copper_age"
}
```

### electrum_cast_saw_head

```json
{
  "name": "electrum_cast_saw_head",
  "kind": "tool_part",
  "display": "Electrum: Casting - Saw Blade",
  "display_ru": "Электрум: Отливка — Полотно пилы",
  "tab": "copper_age"
}
```

### electrum_sword_head

```json
{
  "name": "electrum_sword_head",
  "kind": "tool_part",
  "display": "Electrum: Sword Blade",
  "display_ru": "Электрум: Клинок меча",
  "tab": "copper_age"
}
```

### electrum_cast_sword_head

```json
{
  "name": "electrum_cast_sword_head",
  "kind": "tool_part",
  "display": "Electrum: Casting - Sword Blade",
  "display_ru": "Электрум: Отливка — Клинок меча",
  "tab": "copper_age"
}
```

### electrum_katana_head

```json
{
  "name": "electrum_katana_head",
  "kind": "tool_part",
  "display": "Electrum: Katana Blade",
  "display_ru": "Электрум: Клинок катаны",
  "tab": "copper_age"
}
```

### electrum_cast_katana_head

```json
{
  "name": "electrum_cast_katana_head",
  "kind": "tool_part",
  "display": "Electrum: Casting - Katana Blade",
  "display_ru": "Электрум: Отливка — Клинок катаны",
  "tab": "copper_age"
}
```

### electrum_machete_head

```json
{
  "name": "electrum_machete_head",
  "kind": "tool_part",
  "display": "Electrum: Machete Blade",
  "display_ru": "Электрум: Клинок мачете",
  "tab": "copper_age"
}
```

### electrum_cast_machete_head

```json
{
  "name": "electrum_cast_machete_head",
  "kind": "tool_part",
  "display": "Electrum: Casting - Machete Blade",
  "display_ru": "Электрум: Отливка — Клинок мачете",
  "tab": "copper_age"
}
```

### electrum_sickle_head

```json
{
  "name": "electrum_sickle_head",
  "kind": "tool_part",
  "display": "Electrum: Sickle Blade",
  "display_ru": "Электрум: Лезвие серпа",
  "tab": "copper_age"
}
```

### electrum_cast_sickle_head

```json
{
  "name": "electrum_cast_sickle_head",
  "kind": "tool_part",
  "display": "Electrum: Casting - Sickle Blade",
  "display_ru": "Электрум: Отливка — Лезвие серпа",
  "tab": "copper_age"
}
```

### fiber_wrap

```json
{
  "name": "fiber_wrap",
  "kind": "tool_part",
  "display": "Plant Fiber: Grip Wrap",
  "display_ru": "Растительное волокно: Обмотка",
  "tab": "copper_age"
}
```

### gold_pickaxe_head

```json
{
  "name": "gold_pickaxe_head",
  "kind": "tool_part",
  "display": "Gold: Pickaxe Head",
  "display_ru": "Золото: Головка кирки",
  "tab": "copper_age"
}
```

### gold_cast_pickaxe_head

```json
{
  "name": "gold_cast_pickaxe_head",
  "kind": "tool_part",
  "display": "Gold: Casting - Pickaxe Head",
  "display_ru": "Золото: Отливка — Головка кирки",
  "tab": "copper_age"
}
```

### gold_axe_head

```json
{
  "name": "gold_axe_head",
  "kind": "tool_part",
  "display": "Gold: Axe Head",
  "display_ru": "Золото: Головка топора",
  "tab": "copper_age"
}
```

### gold_cast_axe_head

```json
{
  "name": "gold_cast_axe_head",
  "kind": "tool_part",
  "display": "Gold: Casting - Axe Head",
  "display_ru": "Золото: Отливка — Головка топора",
  "tab": "copper_age"
}
```

### gold_shovel_head

```json
{
  "name": "gold_shovel_head",
  "kind": "tool_part",
  "display": "Gold: Shovel Head",
  "display_ru": "Золото: Головка лопаты",
  "tab": "copper_age"
}
```

### gold_cast_shovel_head

```json
{
  "name": "gold_cast_shovel_head",
  "kind": "tool_part",
  "display": "Gold: Casting - Shovel Head",
  "display_ru": "Золото: Отливка — Головка лопаты",
  "tab": "copper_age"
}
```

### gold_hoe_head

```json
{
  "name": "gold_hoe_head",
  "kind": "tool_part",
  "display": "Gold: Hoe Head",
  "display_ru": "Золото: Головка мотыги",
  "tab": "copper_age"
}
```

### gold_cast_hoe_head

```json
{
  "name": "gold_cast_hoe_head",
  "kind": "tool_part",
  "display": "Gold: Casting - Hoe Head",
  "display_ru": "Золото: Отливка — Головка мотыги",
  "tab": "copper_age"
}
```

### gold_hammer_head

```json
{
  "name": "gold_hammer_head",
  "kind": "tool_part",
  "display": "Gold: Hammer Head",
  "display_ru": "Золото: Головка молота",
  "tab": "copper_age"
}
```

### gold_cast_hammer_head

```json
{
  "name": "gold_cast_hammer_head",
  "kind": "tool_part",
  "display": "Gold: Casting - Hammer Head",
  "display_ru": "Золото: Отливка — Головка молота",
  "tab": "copper_age"
}
```

### gold_standard_handle

```json
{
  "name": "gold_standard_handle",
  "kind": "tool_part",
  "display": "Gold: Rod",
  "display_ru": "Золото: Стержень",
  "tab": "copper_age"
}
```

### gold_clamp

```json
{
  "name": "gold_clamp",
  "kind": "tool_part",
  "display": "Gold: Clamp",
  "display_ru": "Золото: Хомут",
  "tab": "copper_age"
}
```

### gold_saw_head

```json
{
  "name": "gold_saw_head",
  "kind": "tool_part",
  "display": "Gold: Saw Blade",
  "display_ru": "Золото: Полотно пилы",
  "tab": "copper_age"
}
```

### gold_cast_saw_head

```json
{
  "name": "gold_cast_saw_head",
  "kind": "tool_part",
  "display": "Gold: Casting - Saw Blade",
  "display_ru": "Золото: Отливка — Полотно пилы",
  "tab": "copper_age"
}
```

### gold_sword_head

```json
{
  "name": "gold_sword_head",
  "kind": "tool_part",
  "display": "Gold: Sword Blade",
  "display_ru": "Золото: Клинок меча",
  "tab": "copper_age"
}
```

### gold_cast_sword_head

```json
{
  "name": "gold_cast_sword_head",
  "kind": "tool_part",
  "display": "Gold: Casting - Sword Blade",
  "display_ru": "Золото: Отливка — Клинок меча",
  "tab": "copper_age"
}
```

### gold_katana_head

```json
{
  "name": "gold_katana_head",
  "kind": "tool_part",
  "display": "Gold: Katana Blade",
  "display_ru": "Золото: Клинок катаны",
  "tab": "copper_age"
}
```

### gold_cast_katana_head

```json
{
  "name": "gold_cast_katana_head",
  "kind": "tool_part",
  "display": "Gold: Casting - Katana Blade",
  "display_ru": "Золото: Отливка — Клинок катаны",
  "tab": "copper_age"
}
```

### gold_machete_head

```json
{
  "name": "gold_machete_head",
  "kind": "tool_part",
  "display": "Gold: Machete Blade",
  "display_ru": "Золото: Клинок мачете",
  "tab": "copper_age"
}
```

### gold_cast_machete_head

```json
{
  "name": "gold_cast_machete_head",
  "kind": "tool_part",
  "display": "Gold: Casting - Machete Blade",
  "display_ru": "Золото: Отливка — Клинок мачете",
  "tab": "copper_age"
}
```

### gold_sickle_head

```json
{
  "name": "gold_sickle_head",
  "kind": "tool_part",
  "display": "Gold: Sickle Blade",
  "display_ru": "Золото: Лезвие серпа",
  "tab": "copper_age"
}
```

### gold_cast_sickle_head

```json
{
  "name": "gold_cast_sickle_head",
  "kind": "tool_part",
  "display": "Gold: Casting - Sickle Blade",
  "display_ru": "Золото: Отливка — Лезвие серпа",
  "tab": "copper_age"
}
```

### invar_pickaxe_head

```json
{
  "name": "invar_pickaxe_head",
  "kind": "tool_part",
  "display": "Invar: Pickaxe Head",
  "display_ru": "Инвар: Головка кирки",
  "tab": "copper_age"
}
```

### invar_cast_pickaxe_head

```json
{
  "name": "invar_cast_pickaxe_head",
  "kind": "tool_part",
  "display": "Invar: Casting - Pickaxe Head",
  "display_ru": "Инвар: Отливка — Головка кирки",
  "tab": "copper_age"
}
```

### invar_axe_head

```json
{
  "name": "invar_axe_head",
  "kind": "tool_part",
  "display": "Invar: Axe Head",
  "display_ru": "Инвар: Головка топора",
  "tab": "copper_age"
}
```

### invar_cast_axe_head

```json
{
  "name": "invar_cast_axe_head",
  "kind": "tool_part",
  "display": "Invar: Casting - Axe Head",
  "display_ru": "Инвар: Отливка — Головка топора",
  "tab": "copper_age"
}
```

### invar_shovel_head

```json
{
  "name": "invar_shovel_head",
  "kind": "tool_part",
  "display": "Invar: Shovel Head",
  "display_ru": "Инвар: Головка лопаты",
  "tab": "copper_age"
}
```

### invar_cast_shovel_head

```json
{
  "name": "invar_cast_shovel_head",
  "kind": "tool_part",
  "display": "Invar: Casting - Shovel Head",
  "display_ru": "Инвар: Отливка — Головка лопаты",
  "tab": "copper_age"
}
```

### invar_hoe_head

```json
{
  "name": "invar_hoe_head",
  "kind": "tool_part",
  "display": "Invar: Hoe Head",
  "display_ru": "Инвар: Головка мотыги",
  "tab": "copper_age"
}
```

### invar_cast_hoe_head

```json
{
  "name": "invar_cast_hoe_head",
  "kind": "tool_part",
  "display": "Invar: Casting - Hoe Head",
  "display_ru": "Инвар: Отливка — Головка мотыги",
  "tab": "copper_age"
}
```

### invar_hammer_head

```json
{
  "name": "invar_hammer_head",
  "kind": "tool_part",
  "display": "Invar: Hammer Head",
  "display_ru": "Инвар: Головка молота",
  "tab": "copper_age"
}
```

### invar_cast_hammer_head

```json
{
  "name": "invar_cast_hammer_head",
  "kind": "tool_part",
  "display": "Invar: Casting - Hammer Head",
  "display_ru": "Инвар: Отливка — Головка молота",
  "tab": "copper_age"
}
```

### invar_standard_handle

```json
{
  "name": "invar_standard_handle",
  "kind": "tool_part",
  "display": "Invar: Rod",
  "display_ru": "Инвар: Стержень",
  "tab": "copper_age"
}
```

### invar_clamp

```json
{
  "name": "invar_clamp",
  "kind": "tool_part",
  "display": "Invar: Clamp",
  "display_ru": "Инвар: Хомут",
  "tab": "copper_age"
}
```

### invar_saw_head

```json
{
  "name": "invar_saw_head",
  "kind": "tool_part",
  "display": "Invar: Saw Blade",
  "display_ru": "Инвар: Полотно пилы",
  "tab": "copper_age"
}
```

### invar_cast_saw_head

```json
{
  "name": "invar_cast_saw_head",
  "kind": "tool_part",
  "display": "Invar: Casting - Saw Blade",
  "display_ru": "Инвар: Отливка — Полотно пилы",
  "tab": "copper_age"
}
```

### invar_sword_head

```json
{
  "name": "invar_sword_head",
  "kind": "tool_part",
  "display": "Invar: Sword Blade",
  "display_ru": "Инвар: Клинок меча",
  "tab": "copper_age"
}
```

### invar_cast_sword_head

```json
{
  "name": "invar_cast_sword_head",
  "kind": "tool_part",
  "display": "Invar: Casting - Sword Blade",
  "display_ru": "Инвар: Отливка — Клинок меча",
  "tab": "copper_age"
}
```

### invar_katana_head

```json
{
  "name": "invar_katana_head",
  "kind": "tool_part",
  "display": "Invar: Katana Blade",
  "display_ru": "Инвар: Клинок катаны",
  "tab": "copper_age"
}
```

### invar_cast_katana_head

```json
{
  "name": "invar_cast_katana_head",
  "kind": "tool_part",
  "display": "Invar: Casting - Katana Blade",
  "display_ru": "Инвар: Отливка — Клинок катаны",
  "tab": "copper_age"
}
```

### invar_machete_head

```json
{
  "name": "invar_machete_head",
  "kind": "tool_part",
  "display": "Invar: Machete Blade",
  "display_ru": "Инвар: Клинок мачете",
  "tab": "copper_age"
}
```

### invar_cast_machete_head

```json
{
  "name": "invar_cast_machete_head",
  "kind": "tool_part",
  "display": "Invar: Casting - Machete Blade",
  "display_ru": "Инвар: Отливка — Клинок мачете",
  "tab": "copper_age"
}
```

### invar_sickle_head

```json
{
  "name": "invar_sickle_head",
  "kind": "tool_part",
  "display": "Invar: Sickle Blade",
  "display_ru": "Инвар: Лезвие серпа",
  "tab": "copper_age"
}
```

### invar_cast_sickle_head

```json
{
  "name": "invar_cast_sickle_head",
  "kind": "tool_part",
  "display": "Invar: Casting - Sickle Blade",
  "display_ru": "Инвар: Отливка — Лезвие серпа",
  "tab": "copper_age"
}
```

### iron_pickaxe_head

```json
{
  "name": "iron_pickaxe_head",
  "kind": "tool_part",
  "display": "Iron: Pickaxe Head",
  "display_ru": "Железо: Головка кирки",
  "tab": "copper_age"
}
```

### iron_axe_head

```json
{
  "name": "iron_axe_head",
  "kind": "tool_part",
  "display": "Iron: Axe Head",
  "display_ru": "Железо: Головка топора",
  "tab": "copper_age"
}
```

### iron_shovel_head

```json
{
  "name": "iron_shovel_head",
  "kind": "tool_part",
  "display": "Iron: Shovel Head",
  "display_ru": "Железо: Головка лопаты",
  "tab": "copper_age"
}
```

### iron_hoe_head

```json
{
  "name": "iron_hoe_head",
  "kind": "tool_part",
  "display": "Iron: Hoe Head",
  "display_ru": "Железо: Головка мотыги",
  "tab": "copper_age"
}
```

### iron_hammer_head

```json
{
  "name": "iron_hammer_head",
  "kind": "tool_part",
  "display": "Iron: Hammer Head",
  "display_ru": "Железо: Головка молота",
  "tab": "copper_age"
}
```

### iron_standard_handle

```json
{
  "name": "iron_standard_handle",
  "kind": "tool_part",
  "display": "Iron: Rod",
  "display_ru": "Железо: Стержень",
  "tab": "copper_age"
}
```

### iron_clamp

```json
{
  "name": "iron_clamp",
  "kind": "tool_part",
  "display": "Iron: Clamp",
  "display_ru": "Железо: Хомут",
  "tab": "copper_age"
}
```

### iron_saw_head

```json
{
  "name": "iron_saw_head",
  "kind": "tool_part",
  "display": "Iron: Saw Blade",
  "display_ru": "Железо: Полотно пилы",
  "tab": "copper_age"
}
```

### iron_sword_head

```json
{
  "name": "iron_sword_head",
  "kind": "tool_part",
  "display": "Iron: Sword Blade",
  "display_ru": "Железо: Клинок меча",
  "tab": "copper_age"
}
```

### iron_katana_head

```json
{
  "name": "iron_katana_head",
  "kind": "tool_part",
  "display": "Iron: Katana Blade",
  "display_ru": "Железо: Клинок катаны",
  "tab": "copper_age"
}
```

### iron_machete_head

```json
{
  "name": "iron_machete_head",
  "kind": "tool_part",
  "display": "Iron: Machete Blade",
  "display_ru": "Железо: Клинок мачете",
  "tab": "copper_age"
}
```

### iron_sickle_head

```json
{
  "name": "iron_sickle_head",
  "kind": "tool_part",
  "display": "Iron: Sickle Blade",
  "display_ru": "Железо: Лезвие серпа",
  "tab": "copper_age"
}
```

### lead_pickaxe_head

```json
{
  "name": "lead_pickaxe_head",
  "kind": "tool_part",
  "display": "Lead: Pickaxe Head",
  "display_ru": "Свинец: Головка кирки",
  "tab": "copper_age"
}
```

### lead_cast_pickaxe_head

```json
{
  "name": "lead_cast_pickaxe_head",
  "kind": "tool_part",
  "display": "Lead: Casting - Pickaxe Head",
  "display_ru": "Свинец: Отливка — Головка кирки",
  "tab": "copper_age"
}
```

### lead_axe_head

```json
{
  "name": "lead_axe_head",
  "kind": "tool_part",
  "display": "Lead: Axe Head",
  "display_ru": "Свинец: Головка топора",
  "tab": "copper_age"
}
```

### lead_cast_axe_head

```json
{
  "name": "lead_cast_axe_head",
  "kind": "tool_part",
  "display": "Lead: Casting - Axe Head",
  "display_ru": "Свинец: Отливка — Головка топора",
  "tab": "copper_age"
}
```

### lead_shovel_head

```json
{
  "name": "lead_shovel_head",
  "kind": "tool_part",
  "display": "Lead: Shovel Head",
  "display_ru": "Свинец: Головка лопаты",
  "tab": "copper_age"
}
```

### lead_cast_shovel_head

```json
{
  "name": "lead_cast_shovel_head",
  "kind": "tool_part",
  "display": "Lead: Casting - Shovel Head",
  "display_ru": "Свинец: Отливка — Головка лопаты",
  "tab": "copper_age"
}
```

### lead_hoe_head

```json
{
  "name": "lead_hoe_head",
  "kind": "tool_part",
  "display": "Lead: Hoe Head",
  "display_ru": "Свинец: Головка мотыги",
  "tab": "copper_age"
}
```

### lead_cast_hoe_head

```json
{
  "name": "lead_cast_hoe_head",
  "kind": "tool_part",
  "display": "Lead: Casting - Hoe Head",
  "display_ru": "Свинец: Отливка — Головка мотыги",
  "tab": "copper_age"
}
```

### lead_hammer_head

```json
{
  "name": "lead_hammer_head",
  "kind": "tool_part",
  "display": "Lead: Hammer Head",
  "display_ru": "Свинец: Головка молота",
  "tab": "copper_age"
}
```

### lead_cast_hammer_head

```json
{
  "name": "lead_cast_hammer_head",
  "kind": "tool_part",
  "display": "Lead: Casting - Hammer Head",
  "display_ru": "Свинец: Отливка — Головка молота",
  "tab": "copper_age"
}
```

### lead_standard_handle

```json
{
  "name": "lead_standard_handle",
  "kind": "tool_part",
  "display": "Lead: Rod",
  "display_ru": "Свинец: Стержень",
  "tab": "copper_age"
}
```

### lead_clamp

```json
{
  "name": "lead_clamp",
  "kind": "tool_part",
  "display": "Lead: Clamp",
  "display_ru": "Свинец: Хомут",
  "tab": "copper_age"
}
```

### lead_saw_head

```json
{
  "name": "lead_saw_head",
  "kind": "tool_part",
  "display": "Lead: Saw Blade",
  "display_ru": "Свинец: Полотно пилы",
  "tab": "copper_age"
}
```

### lead_cast_saw_head

```json
{
  "name": "lead_cast_saw_head",
  "kind": "tool_part",
  "display": "Lead: Casting - Saw Blade",
  "display_ru": "Свинец: Отливка — Полотно пилы",
  "tab": "copper_age"
}
```

### lead_sword_head

```json
{
  "name": "lead_sword_head",
  "kind": "tool_part",
  "display": "Lead: Sword Blade",
  "display_ru": "Свинец: Клинок меча",
  "tab": "copper_age"
}
```

### lead_cast_sword_head

```json
{
  "name": "lead_cast_sword_head",
  "kind": "tool_part",
  "display": "Lead: Casting - Sword Blade",
  "display_ru": "Свинец: Отливка — Клинок меча",
  "tab": "copper_age"
}
```

### lead_katana_head

```json
{
  "name": "lead_katana_head",
  "kind": "tool_part",
  "display": "Lead: Katana Blade",
  "display_ru": "Свинец: Клинок катаны",
  "tab": "copper_age"
}
```

### lead_cast_katana_head

```json
{
  "name": "lead_cast_katana_head",
  "kind": "tool_part",
  "display": "Lead: Casting - Katana Blade",
  "display_ru": "Свинец: Отливка — Клинок катаны",
  "tab": "copper_age"
}
```

### lead_machete_head

```json
{
  "name": "lead_machete_head",
  "kind": "tool_part",
  "display": "Lead: Machete Blade",
  "display_ru": "Свинец: Клинок мачете",
  "tab": "copper_age"
}
```

### lead_cast_machete_head

```json
{
  "name": "lead_cast_machete_head",
  "kind": "tool_part",
  "display": "Lead: Casting - Machete Blade",
  "display_ru": "Свинец: Отливка — Клинок мачете",
  "tab": "copper_age"
}
```

### lead_sickle_head

```json
{
  "name": "lead_sickle_head",
  "kind": "tool_part",
  "display": "Lead: Sickle Blade",
  "display_ru": "Свинец: Лезвие серпа",
  "tab": "copper_age"
}
```

### lead_cast_sickle_head

```json
{
  "name": "lead_cast_sickle_head",
  "kind": "tool_part",
  "display": "Lead: Casting - Sickle Blade",
  "display_ru": "Свинец: Отливка — Лезвие серпа",
  "tab": "copper_age"
}
```

### leather_binding

```json
{
  "name": "leather_binding",
  "kind": "tool_part",
  "display": "Leather: Binding",
  "display_ru": "Кожа: Крепление",
  "tab": "copper_age"
}
```

### leather_wrap

```json
{
  "name": "leather_wrap",
  "kind": "tool_part",
  "display": "Leather: Grip Wrap",
  "display_ru": "Кожа: Обмотка",
  "tab": "copper_age"
}
```

### lithium_pickaxe_head

```json
{
  "name": "lithium_pickaxe_head",
  "kind": "tool_part",
  "display": "Lithium: Pickaxe Head",
  "display_ru": "Литий: Головка кирки",
  "tab": "copper_age"
}
```

### lithium_cast_pickaxe_head

```json
{
  "name": "lithium_cast_pickaxe_head",
  "kind": "tool_part",
  "display": "Lithium: Casting - Pickaxe Head",
  "display_ru": "Литий: Отливка — Головка кирки",
  "tab": "copper_age"
}
```

### lithium_axe_head

```json
{
  "name": "lithium_axe_head",
  "kind": "tool_part",
  "display": "Lithium: Axe Head",
  "display_ru": "Литий: Головка топора",
  "tab": "copper_age"
}
```

### lithium_cast_axe_head

```json
{
  "name": "lithium_cast_axe_head",
  "kind": "tool_part",
  "display": "Lithium: Casting - Axe Head",
  "display_ru": "Литий: Отливка — Головка топора",
  "tab": "copper_age"
}
```

### lithium_shovel_head

```json
{
  "name": "lithium_shovel_head",
  "kind": "tool_part",
  "display": "Lithium: Shovel Head",
  "display_ru": "Литий: Головка лопаты",
  "tab": "copper_age"
}
```

### lithium_cast_shovel_head

```json
{
  "name": "lithium_cast_shovel_head",
  "kind": "tool_part",
  "display": "Lithium: Casting - Shovel Head",
  "display_ru": "Литий: Отливка — Головка лопаты",
  "tab": "copper_age"
}
```

### lithium_hoe_head

```json
{
  "name": "lithium_hoe_head",
  "kind": "tool_part",
  "display": "Lithium: Hoe Head",
  "display_ru": "Литий: Головка мотыги",
  "tab": "copper_age"
}
```

### lithium_cast_hoe_head

```json
{
  "name": "lithium_cast_hoe_head",
  "kind": "tool_part",
  "display": "Lithium: Casting - Hoe Head",
  "display_ru": "Литий: Отливка — Головка мотыги",
  "tab": "copper_age"
}
```

### lithium_hammer_head

```json
{
  "name": "lithium_hammer_head",
  "kind": "tool_part",
  "display": "Lithium: Hammer Head",
  "display_ru": "Литий: Головка молота",
  "tab": "copper_age"
}
```

### lithium_cast_hammer_head

```json
{
  "name": "lithium_cast_hammer_head",
  "kind": "tool_part",
  "display": "Lithium: Casting - Hammer Head",
  "display_ru": "Литий: Отливка — Головка молота",
  "tab": "copper_age"
}
```

### lithium_standard_handle

```json
{
  "name": "lithium_standard_handle",
  "kind": "tool_part",
  "display": "Lithium: Rod",
  "display_ru": "Литий: Стержень",
  "tab": "copper_age"
}
```

### lithium_clamp

```json
{
  "name": "lithium_clamp",
  "kind": "tool_part",
  "display": "Lithium: Clamp",
  "display_ru": "Литий: Хомут",
  "tab": "copper_age"
}
```

### lithium_saw_head

```json
{
  "name": "lithium_saw_head",
  "kind": "tool_part",
  "display": "Lithium: Saw Blade",
  "display_ru": "Литий: Полотно пилы",
  "tab": "copper_age"
}
```

### lithium_cast_saw_head

```json
{
  "name": "lithium_cast_saw_head",
  "kind": "tool_part",
  "display": "Lithium: Casting - Saw Blade",
  "display_ru": "Литий: Отливка — Полотно пилы",
  "tab": "copper_age"
}
```

### lithium_sword_head

```json
{
  "name": "lithium_sword_head",
  "kind": "tool_part",
  "display": "Lithium: Sword Blade",
  "display_ru": "Литий: Клинок меча",
  "tab": "copper_age"
}
```

### lithium_cast_sword_head

```json
{
  "name": "lithium_cast_sword_head",
  "kind": "tool_part",
  "display": "Lithium: Casting - Sword Blade",
  "display_ru": "Литий: Отливка — Клинок меча",
  "tab": "copper_age"
}
```

### lithium_katana_head

```json
{
  "name": "lithium_katana_head",
  "kind": "tool_part",
  "display": "Lithium: Katana Blade",
  "display_ru": "Литий: Клинок катаны",
  "tab": "copper_age"
}
```

### lithium_cast_katana_head

```json
{
  "name": "lithium_cast_katana_head",
  "kind": "tool_part",
  "display": "Lithium: Casting - Katana Blade",
  "display_ru": "Литий: Отливка — Клинок катаны",
  "tab": "copper_age"
}
```

### lithium_machete_head

```json
{
  "name": "lithium_machete_head",
  "kind": "tool_part",
  "display": "Lithium: Machete Blade",
  "display_ru": "Литий: Клинок мачете",
  "tab": "copper_age"
}
```

### lithium_cast_machete_head

```json
{
  "name": "lithium_cast_machete_head",
  "kind": "tool_part",
  "display": "Lithium: Casting - Machete Blade",
  "display_ru": "Литий: Отливка — Клинок мачете",
  "tab": "copper_age"
}
```

### lithium_sickle_head

```json
{
  "name": "lithium_sickle_head",
  "kind": "tool_part",
  "display": "Lithium: Sickle Blade",
  "display_ru": "Литий: Лезвие серпа",
  "tab": "copper_age"
}
```

### lithium_cast_sickle_head

```json
{
  "name": "lithium_cast_sickle_head",
  "kind": "tool_part",
  "display": "Lithium: Casting - Sickle Blade",
  "display_ru": "Литий: Отливка — Лезвие серпа",
  "tab": "copper_age"
}
```

### manganese_pickaxe_head

```json
{
  "name": "manganese_pickaxe_head",
  "kind": "tool_part",
  "display": "Manganese: Pickaxe Head",
  "display_ru": "Марганец: Головка кирки",
  "tab": "copper_age"
}
```

### manganese_cast_pickaxe_head

```json
{
  "name": "manganese_cast_pickaxe_head",
  "kind": "tool_part",
  "display": "Manganese: Casting - Pickaxe Head",
  "display_ru": "Марганец: Отливка — Головка кирки",
  "tab": "copper_age"
}
```

### manganese_axe_head

```json
{
  "name": "manganese_axe_head",
  "kind": "tool_part",
  "display": "Manganese: Axe Head",
  "display_ru": "Марганец: Головка топора",
  "tab": "copper_age"
}
```

### manganese_cast_axe_head

```json
{
  "name": "manganese_cast_axe_head",
  "kind": "tool_part",
  "display": "Manganese: Casting - Axe Head",
  "display_ru": "Марганец: Отливка — Головка топора",
  "tab": "copper_age"
}
```

### manganese_shovel_head

```json
{
  "name": "manganese_shovel_head",
  "kind": "tool_part",
  "display": "Manganese: Shovel Head",
  "display_ru": "Марганец: Головка лопаты",
  "tab": "copper_age"
}
```

### manganese_cast_shovel_head

```json
{
  "name": "manganese_cast_shovel_head",
  "kind": "tool_part",
  "display": "Manganese: Casting - Shovel Head",
  "display_ru": "Марганец: Отливка — Головка лопаты",
  "tab": "copper_age"
}
```

### manganese_hoe_head

```json
{
  "name": "manganese_hoe_head",
  "kind": "tool_part",
  "display": "Manganese: Hoe Head",
  "display_ru": "Марганец: Головка мотыги",
  "tab": "copper_age"
}
```

### manganese_cast_hoe_head

```json
{
  "name": "manganese_cast_hoe_head",
  "kind": "tool_part",
  "display": "Manganese: Casting - Hoe Head",
  "display_ru": "Марганец: Отливка — Головка мотыги",
  "tab": "copper_age"
}
```

### manganese_hammer_head

```json
{
  "name": "manganese_hammer_head",
  "kind": "tool_part",
  "display": "Manganese: Hammer Head",
  "display_ru": "Марганец: Головка молота",
  "tab": "copper_age"
}
```

### manganese_cast_hammer_head

```json
{
  "name": "manganese_cast_hammer_head",
  "kind": "tool_part",
  "display": "Manganese: Casting - Hammer Head",
  "display_ru": "Марганец: Отливка — Головка молота",
  "tab": "copper_age"
}
```

### manganese_standard_handle

```json
{
  "name": "manganese_standard_handle",
  "kind": "tool_part",
  "display": "Manganese: Rod",
  "display_ru": "Марганец: Стержень",
  "tab": "copper_age"
}
```

### manganese_clamp

```json
{
  "name": "manganese_clamp",
  "kind": "tool_part",
  "display": "Manganese: Clamp",
  "display_ru": "Марганец: Хомут",
  "tab": "copper_age"
}
```

### manganese_saw_head

```json
{
  "name": "manganese_saw_head",
  "kind": "tool_part",
  "display": "Manganese: Saw Blade",
  "display_ru": "Марганец: Полотно пилы",
  "tab": "copper_age"
}
```

### manganese_cast_saw_head

```json
{
  "name": "manganese_cast_saw_head",
  "kind": "tool_part",
  "display": "Manganese: Casting - Saw Blade",
  "display_ru": "Марганец: Отливка — Полотно пилы",
  "tab": "copper_age"
}
```

### manganese_sword_head

```json
{
  "name": "manganese_sword_head",
  "kind": "tool_part",
  "display": "Manganese: Sword Blade",
  "display_ru": "Марганец: Клинок меча",
  "tab": "copper_age"
}
```

### manganese_cast_sword_head

```json
{
  "name": "manganese_cast_sword_head",
  "kind": "tool_part",
  "display": "Manganese: Casting - Sword Blade",
  "display_ru": "Марганец: Отливка — Клинок меча",
  "tab": "copper_age"
}
```

### manganese_katana_head

```json
{
  "name": "manganese_katana_head",
  "kind": "tool_part",
  "display": "Manganese: Katana Blade",
  "display_ru": "Марганец: Клинок катаны",
  "tab": "copper_age"
}
```

### manganese_cast_katana_head

```json
{
  "name": "manganese_cast_katana_head",
  "kind": "tool_part",
  "display": "Manganese: Casting - Katana Blade",
  "display_ru": "Марганец: Отливка — Клинок катаны",
  "tab": "copper_age"
}
```

### manganese_machete_head

```json
{
  "name": "manganese_machete_head",
  "kind": "tool_part",
  "display": "Manganese: Machete Blade",
  "display_ru": "Марганец: Клинок мачете",
  "tab": "copper_age"
}
```

### manganese_cast_machete_head

```json
{
  "name": "manganese_cast_machete_head",
  "kind": "tool_part",
  "display": "Manganese: Casting - Machete Blade",
  "display_ru": "Марганец: Отливка — Клинок мачете",
  "tab": "copper_age"
}
```

### manganese_sickle_head

```json
{
  "name": "manganese_sickle_head",
  "kind": "tool_part",
  "display": "Manganese: Sickle Blade",
  "display_ru": "Марганец: Лезвие серпа",
  "tab": "copper_age"
}
```

### manganese_cast_sickle_head

```json
{
  "name": "manganese_cast_sickle_head",
  "kind": "tool_part",
  "display": "Manganese: Casting - Sickle Blade",
  "display_ru": "Марганец: Отливка — Лезвие серпа",
  "tab": "copper_age"
}
```

### molybdenum_pickaxe_head

```json
{
  "name": "molybdenum_pickaxe_head",
  "kind": "tool_part",
  "display": "Molybdenum: Pickaxe Head",
  "display_ru": "Молибден: Головка кирки",
  "tab": "copper_age"
}
```

### molybdenum_cast_pickaxe_head

```json
{
  "name": "molybdenum_cast_pickaxe_head",
  "kind": "tool_part",
  "display": "Molybdenum: Casting - Pickaxe Head",
  "display_ru": "Молибден: Отливка — Головка кирки",
  "tab": "copper_age"
}
```

### molybdenum_axe_head

```json
{
  "name": "molybdenum_axe_head",
  "kind": "tool_part",
  "display": "Molybdenum: Axe Head",
  "display_ru": "Молибден: Головка топора",
  "tab": "copper_age"
}
```

### molybdenum_cast_axe_head

```json
{
  "name": "molybdenum_cast_axe_head",
  "kind": "tool_part",
  "display": "Molybdenum: Casting - Axe Head",
  "display_ru": "Молибден: Отливка — Головка топора",
  "tab": "copper_age"
}
```

### molybdenum_shovel_head

```json
{
  "name": "molybdenum_shovel_head",
  "kind": "tool_part",
  "display": "Molybdenum: Shovel Head",
  "display_ru": "Молибден: Головка лопаты",
  "tab": "copper_age"
}
```

### molybdenum_cast_shovel_head

```json
{
  "name": "molybdenum_cast_shovel_head",
  "kind": "tool_part",
  "display": "Molybdenum: Casting - Shovel Head",
  "display_ru": "Молибден: Отливка — Головка лопаты",
  "tab": "copper_age"
}
```

### molybdenum_hoe_head

```json
{
  "name": "molybdenum_hoe_head",
  "kind": "tool_part",
  "display": "Molybdenum: Hoe Head",
  "display_ru": "Молибден: Головка мотыги",
  "tab": "copper_age"
}
```

### molybdenum_cast_hoe_head

```json
{
  "name": "molybdenum_cast_hoe_head",
  "kind": "tool_part",
  "display": "Molybdenum: Casting - Hoe Head",
  "display_ru": "Молибден: Отливка — Головка мотыги",
  "tab": "copper_age"
}
```

### molybdenum_hammer_head

```json
{
  "name": "molybdenum_hammer_head",
  "kind": "tool_part",
  "display": "Molybdenum: Hammer Head",
  "display_ru": "Молибден: Головка молота",
  "tab": "copper_age"
}
```

### molybdenum_cast_hammer_head

```json
{
  "name": "molybdenum_cast_hammer_head",
  "kind": "tool_part",
  "display": "Molybdenum: Casting - Hammer Head",
  "display_ru": "Молибден: Отливка — Головка молота",
  "tab": "copper_age"
}
```

### molybdenum_standard_handle

```json
{
  "name": "molybdenum_standard_handle",
  "kind": "tool_part",
  "display": "Molybdenum: Rod",
  "display_ru": "Молибден: Стержень",
  "tab": "copper_age"
}
```

### molybdenum_clamp

```json
{
  "name": "molybdenum_clamp",
  "kind": "tool_part",
  "display": "Molybdenum: Clamp",
  "display_ru": "Молибден: Хомут",
  "tab": "copper_age"
}
```

### molybdenum_saw_head

```json
{
  "name": "molybdenum_saw_head",
  "kind": "tool_part",
  "display": "Molybdenum: Saw Blade",
  "display_ru": "Молибден: Полотно пилы",
  "tab": "copper_age"
}
```

### molybdenum_cast_saw_head

```json
{
  "name": "molybdenum_cast_saw_head",
  "kind": "tool_part",
  "display": "Molybdenum: Casting - Saw Blade",
  "display_ru": "Молибден: Отливка — Полотно пилы",
  "tab": "copper_age"
}
```

### molybdenum_sword_head

```json
{
  "name": "molybdenum_sword_head",
  "kind": "tool_part",
  "display": "Molybdenum: Sword Blade",
  "display_ru": "Молибден: Клинок меча",
  "tab": "copper_age"
}
```

### molybdenum_cast_sword_head

```json
{
  "name": "molybdenum_cast_sword_head",
  "kind": "tool_part",
  "display": "Molybdenum: Casting - Sword Blade",
  "display_ru": "Молибден: Отливка — Клинок меча",
  "tab": "copper_age"
}
```

### molybdenum_katana_head

```json
{
  "name": "molybdenum_katana_head",
  "kind": "tool_part",
  "display": "Molybdenum: Katana Blade",
  "display_ru": "Молибден: Клинок катаны",
  "tab": "copper_age"
}
```

### molybdenum_cast_katana_head

```json
{
  "name": "molybdenum_cast_katana_head",
  "kind": "tool_part",
  "display": "Molybdenum: Casting - Katana Blade",
  "display_ru": "Молибден: Отливка — Клинок катаны",
  "tab": "copper_age"
}
```

### molybdenum_machete_head

```json
{
  "name": "molybdenum_machete_head",
  "kind": "tool_part",
  "display": "Molybdenum: Machete Blade",
  "display_ru": "Молибден: Клинок мачете",
  "tab": "copper_age"
}
```

### molybdenum_cast_machete_head

```json
{
  "name": "molybdenum_cast_machete_head",
  "kind": "tool_part",
  "display": "Molybdenum: Casting - Machete Blade",
  "display_ru": "Молибден: Отливка — Клинок мачете",
  "tab": "copper_age"
}
```

### molybdenum_sickle_head

```json
{
  "name": "molybdenum_sickle_head",
  "kind": "tool_part",
  "display": "Molybdenum: Sickle Blade",
  "display_ru": "Молибден: Лезвие серпа",
  "tab": "copper_age"
}
```

### molybdenum_cast_sickle_head

```json
{
  "name": "molybdenum_cast_sickle_head",
  "kind": "tool_part",
  "display": "Molybdenum: Casting - Sickle Blade",
  "display_ru": "Молибден: Отливка — Лезвие серпа",
  "tab": "copper_age"
}
```

### netherite_pickaxe_head

```json
{
  "name": "netherite_pickaxe_head",
  "kind": "tool_part",
  "display": "Netherite: Pickaxe Head",
  "display_ru": "Незерит: Головка кирки",
  "tab": "copper_age"
}
```

### netherite_cast_pickaxe_head

```json
{
  "name": "netherite_cast_pickaxe_head",
  "kind": "tool_part",
  "display": "Netherite: Casting - Pickaxe Head",
  "display_ru": "Незерит: Отливка — Головка кирки",
  "tab": "copper_age"
}
```

### netherite_axe_head

```json
{
  "name": "netherite_axe_head",
  "kind": "tool_part",
  "display": "Netherite: Axe Head",
  "display_ru": "Незерит: Головка топора",
  "tab": "copper_age"
}
```

### netherite_cast_axe_head

```json
{
  "name": "netherite_cast_axe_head",
  "kind": "tool_part",
  "display": "Netherite: Casting - Axe Head",
  "display_ru": "Незерит: Отливка — Головка топора",
  "tab": "copper_age"
}
```

### netherite_shovel_head

```json
{
  "name": "netherite_shovel_head",
  "kind": "tool_part",
  "display": "Netherite: Shovel Head",
  "display_ru": "Незерит: Головка лопаты",
  "tab": "copper_age"
}
```

### netherite_cast_shovel_head

```json
{
  "name": "netherite_cast_shovel_head",
  "kind": "tool_part",
  "display": "Netherite: Casting - Shovel Head",
  "display_ru": "Незерит: Отливка — Головка лопаты",
  "tab": "copper_age"
}
```

### netherite_hoe_head

```json
{
  "name": "netherite_hoe_head",
  "kind": "tool_part",
  "display": "Netherite: Hoe Head",
  "display_ru": "Незерит: Головка мотыги",
  "tab": "copper_age"
}
```

### netherite_cast_hoe_head

```json
{
  "name": "netherite_cast_hoe_head",
  "kind": "tool_part",
  "display": "Netherite: Casting - Hoe Head",
  "display_ru": "Незерит: Отливка — Головка мотыги",
  "tab": "copper_age"
}
```

### netherite_hammer_head

```json
{
  "name": "netherite_hammer_head",
  "kind": "tool_part",
  "display": "Netherite: Hammer Head",
  "display_ru": "Незерит: Головка молота",
  "tab": "copper_age"
}
```

### netherite_cast_hammer_head

```json
{
  "name": "netherite_cast_hammer_head",
  "kind": "tool_part",
  "display": "Netherite: Casting - Hammer Head",
  "display_ru": "Незерит: Отливка — Головка молота",
  "tab": "copper_age"
}
```

### netherite_standard_handle

```json
{
  "name": "netherite_standard_handle",
  "kind": "tool_part",
  "display": "Netherite: Rod",
  "display_ru": "Незерит: Стержень",
  "tab": "copper_age"
}
```

### netherite_clamp

```json
{
  "name": "netherite_clamp",
  "kind": "tool_part",
  "display": "Netherite: Clamp",
  "display_ru": "Незерит: Хомут",
  "tab": "copper_age"
}
```

### netherite_saw_head

```json
{
  "name": "netherite_saw_head",
  "kind": "tool_part",
  "display": "Netherite: Saw Blade",
  "display_ru": "Незерит: Полотно пилы",
  "tab": "copper_age"
}
```

### netherite_cast_saw_head

```json
{
  "name": "netherite_cast_saw_head",
  "kind": "tool_part",
  "display": "Netherite: Casting - Saw Blade",
  "display_ru": "Незерит: Отливка — Полотно пилы",
  "tab": "copper_age"
}
```

### netherite_sword_head

```json
{
  "name": "netherite_sword_head",
  "kind": "tool_part",
  "display": "Netherite: Sword Blade",
  "display_ru": "Незерит: Клинок меча",
  "tab": "copper_age"
}
```

### netherite_cast_sword_head

```json
{
  "name": "netherite_cast_sword_head",
  "kind": "tool_part",
  "display": "Netherite: Casting - Sword Blade",
  "display_ru": "Незерит: Отливка — Клинок меча",
  "tab": "copper_age"
}
```

### netherite_katana_head

```json
{
  "name": "netherite_katana_head",
  "kind": "tool_part",
  "display": "Netherite: Katana Blade",
  "display_ru": "Незерит: Клинок катаны",
  "tab": "copper_age"
}
```

### netherite_cast_katana_head

```json
{
  "name": "netherite_cast_katana_head",
  "kind": "tool_part",
  "display": "Netherite: Casting - Katana Blade",
  "display_ru": "Незерит: Отливка — Клинок катаны",
  "tab": "copper_age"
}
```

### netherite_machete_head

```json
{
  "name": "netherite_machete_head",
  "kind": "tool_part",
  "display": "Netherite: Machete Blade",
  "display_ru": "Незерит: Клинок мачете",
  "tab": "copper_age"
}
```

### netherite_cast_machete_head

```json
{
  "name": "netherite_cast_machete_head",
  "kind": "tool_part",
  "display": "Netherite: Casting - Machete Blade",
  "display_ru": "Незерит: Отливка — Клинок мачете",
  "tab": "copper_age"
}
```

### netherite_sickle_head

```json
{
  "name": "netherite_sickle_head",
  "kind": "tool_part",
  "display": "Netherite: Sickle Blade",
  "display_ru": "Незерит: Лезвие серпа",
  "tab": "copper_age"
}
```

### netherite_cast_sickle_head

```json
{
  "name": "netherite_cast_sickle_head",
  "kind": "tool_part",
  "display": "Netherite: Casting - Sickle Blade",
  "display_ru": "Незерит: Отливка — Лезвие серпа",
  "tab": "copper_age"
}
```

### nichrome_pickaxe_head

```json
{
  "name": "nichrome_pickaxe_head",
  "kind": "tool_part",
  "display": "Nichrome: Pickaxe Head",
  "display_ru": "Нихром: Головка кирки",
  "tab": "copper_age"
}
```

### nichrome_cast_pickaxe_head

```json
{
  "name": "nichrome_cast_pickaxe_head",
  "kind": "tool_part",
  "display": "Nichrome: Casting - Pickaxe Head",
  "display_ru": "Нихром: Отливка — Головка кирки",
  "tab": "copper_age"
}
```

### nichrome_axe_head

```json
{
  "name": "nichrome_axe_head",
  "kind": "tool_part",
  "display": "Nichrome: Axe Head",
  "display_ru": "Нихром: Головка топора",
  "tab": "copper_age"
}
```

### nichrome_cast_axe_head

```json
{
  "name": "nichrome_cast_axe_head",
  "kind": "tool_part",
  "display": "Nichrome: Casting - Axe Head",
  "display_ru": "Нихром: Отливка — Головка топора",
  "tab": "copper_age"
}
```

### nichrome_shovel_head

```json
{
  "name": "nichrome_shovel_head",
  "kind": "tool_part",
  "display": "Nichrome: Shovel Head",
  "display_ru": "Нихром: Головка лопаты",
  "tab": "copper_age"
}
```

### nichrome_cast_shovel_head

```json
{
  "name": "nichrome_cast_shovel_head",
  "kind": "tool_part",
  "display": "Nichrome: Casting - Shovel Head",
  "display_ru": "Нихром: Отливка — Головка лопаты",
  "tab": "copper_age"
}
```

### nichrome_hoe_head

```json
{
  "name": "nichrome_hoe_head",
  "kind": "tool_part",
  "display": "Nichrome: Hoe Head",
  "display_ru": "Нихром: Головка мотыги",
  "tab": "copper_age"
}
```

### nichrome_cast_hoe_head

```json
{
  "name": "nichrome_cast_hoe_head",
  "kind": "tool_part",
  "display": "Nichrome: Casting - Hoe Head",
  "display_ru": "Нихром: Отливка — Головка мотыги",
  "tab": "copper_age"
}
```

### nichrome_hammer_head

```json
{
  "name": "nichrome_hammer_head",
  "kind": "tool_part",
  "display": "Nichrome: Hammer Head",
  "display_ru": "Нихром: Головка молота",
  "tab": "copper_age"
}
```

### nichrome_cast_hammer_head

```json
{
  "name": "nichrome_cast_hammer_head",
  "kind": "tool_part",
  "display": "Nichrome: Casting - Hammer Head",
  "display_ru": "Нихром: Отливка — Головка молота",
  "tab": "copper_age"
}
```

### nichrome_standard_handle

```json
{
  "name": "nichrome_standard_handle",
  "kind": "tool_part",
  "display": "Nichrome: Rod",
  "display_ru": "Нихром: Стержень",
  "tab": "copper_age"
}
```

### nichrome_clamp

```json
{
  "name": "nichrome_clamp",
  "kind": "tool_part",
  "display": "Nichrome: Clamp",
  "display_ru": "Нихром: Хомут",
  "tab": "copper_age"
}
```

### nichrome_saw_head

```json
{
  "name": "nichrome_saw_head",
  "kind": "tool_part",
  "display": "Nichrome: Saw Blade",
  "display_ru": "Нихром: Полотно пилы",
  "tab": "copper_age"
}
```

### nichrome_cast_saw_head

```json
{
  "name": "nichrome_cast_saw_head",
  "kind": "tool_part",
  "display": "Nichrome: Casting - Saw Blade",
  "display_ru": "Нихром: Отливка — Полотно пилы",
  "tab": "copper_age"
}
```

### nichrome_sword_head

```json
{
  "name": "nichrome_sword_head",
  "kind": "tool_part",
  "display": "Nichrome: Sword Blade",
  "display_ru": "Нихром: Клинок меча",
  "tab": "copper_age"
}
```

### nichrome_cast_sword_head

```json
{
  "name": "nichrome_cast_sword_head",
  "kind": "tool_part",
  "display": "Nichrome: Casting - Sword Blade",
  "display_ru": "Нихром: Отливка — Клинок меча",
  "tab": "copper_age"
}
```

### nichrome_katana_head

```json
{
  "name": "nichrome_katana_head",
  "kind": "tool_part",
  "display": "Nichrome: Katana Blade",
  "display_ru": "Нихром: Клинок катаны",
  "tab": "copper_age"
}
```

### nichrome_cast_katana_head

```json
{
  "name": "nichrome_cast_katana_head",
  "kind": "tool_part",
  "display": "Nichrome: Casting - Katana Blade",
  "display_ru": "Нихром: Отливка — Клинок катаны",
  "tab": "copper_age"
}
```

### nichrome_machete_head

```json
{
  "name": "nichrome_machete_head",
  "kind": "tool_part",
  "display": "Nichrome: Machete Blade",
  "display_ru": "Нихром: Клинок мачете",
  "tab": "copper_age"
}
```

### nichrome_cast_machete_head

```json
{
  "name": "nichrome_cast_machete_head",
  "kind": "tool_part",
  "display": "Nichrome: Casting - Machete Blade",
  "display_ru": "Нихром: Отливка — Клинок мачете",
  "tab": "copper_age"
}
```

### nichrome_sickle_head

```json
{
  "name": "nichrome_sickle_head",
  "kind": "tool_part",
  "display": "Nichrome: Sickle Blade",
  "display_ru": "Нихром: Лезвие серпа",
  "tab": "copper_age"
}
```

### nichrome_cast_sickle_head

```json
{
  "name": "nichrome_cast_sickle_head",
  "kind": "tool_part",
  "display": "Nichrome: Casting - Sickle Blade",
  "display_ru": "Нихром: Отливка — Лезвие серпа",
  "tab": "copper_age"
}
```

### nickel_pickaxe_head

```json
{
  "name": "nickel_pickaxe_head",
  "kind": "tool_part",
  "display": "Nickel: Pickaxe Head",
  "display_ru": "Никель: Головка кирки",
  "tab": "copper_age"
}
```

### nickel_cast_pickaxe_head

```json
{
  "name": "nickel_cast_pickaxe_head",
  "kind": "tool_part",
  "display": "Nickel: Casting - Pickaxe Head",
  "display_ru": "Никель: Отливка — Головка кирки",
  "tab": "copper_age"
}
```

### nickel_axe_head

```json
{
  "name": "nickel_axe_head",
  "kind": "tool_part",
  "display": "Nickel: Axe Head",
  "display_ru": "Никель: Головка топора",
  "tab": "copper_age"
}
```

### nickel_cast_axe_head

```json
{
  "name": "nickel_cast_axe_head",
  "kind": "tool_part",
  "display": "Nickel: Casting - Axe Head",
  "display_ru": "Никель: Отливка — Головка топора",
  "tab": "copper_age"
}
```

### nickel_shovel_head

```json
{
  "name": "nickel_shovel_head",
  "kind": "tool_part",
  "display": "Nickel: Shovel Head",
  "display_ru": "Никель: Головка лопаты",
  "tab": "copper_age"
}
```

### nickel_cast_shovel_head

```json
{
  "name": "nickel_cast_shovel_head",
  "kind": "tool_part",
  "display": "Nickel: Casting - Shovel Head",
  "display_ru": "Никель: Отливка — Головка лопаты",
  "tab": "copper_age"
}
```

### nickel_hoe_head

```json
{
  "name": "nickel_hoe_head",
  "kind": "tool_part",
  "display": "Nickel: Hoe Head",
  "display_ru": "Никель: Головка мотыги",
  "tab": "copper_age"
}
```

### nickel_cast_hoe_head

```json
{
  "name": "nickel_cast_hoe_head",
  "kind": "tool_part",
  "display": "Nickel: Casting - Hoe Head",
  "display_ru": "Никель: Отливка — Головка мотыги",
  "tab": "copper_age"
}
```

### nickel_hammer_head

```json
{
  "name": "nickel_hammer_head",
  "kind": "tool_part",
  "display": "Nickel: Hammer Head",
  "display_ru": "Никель: Головка молота",
  "tab": "copper_age"
}
```

### nickel_cast_hammer_head

```json
{
  "name": "nickel_cast_hammer_head",
  "kind": "tool_part",
  "display": "Nickel: Casting - Hammer Head",
  "display_ru": "Никель: Отливка — Головка молота",
  "tab": "copper_age"
}
```

### nickel_standard_handle

```json
{
  "name": "nickel_standard_handle",
  "kind": "tool_part",
  "display": "Nickel: Rod",
  "display_ru": "Никель: Стержень",
  "tab": "copper_age"
}
```

### nickel_clamp

```json
{
  "name": "nickel_clamp",
  "kind": "tool_part",
  "display": "Nickel: Clamp",
  "display_ru": "Никель: Хомут",
  "tab": "copper_age"
}
```

### nickel_saw_head

```json
{
  "name": "nickel_saw_head",
  "kind": "tool_part",
  "display": "Nickel: Saw Blade",
  "display_ru": "Никель: Полотно пилы",
  "tab": "copper_age"
}
```

### nickel_cast_saw_head

```json
{
  "name": "nickel_cast_saw_head",
  "kind": "tool_part",
  "display": "Nickel: Casting - Saw Blade",
  "display_ru": "Никель: Отливка — Полотно пилы",
  "tab": "copper_age"
}
```

### nickel_sword_head

```json
{
  "name": "nickel_sword_head",
  "kind": "tool_part",
  "display": "Nickel: Sword Blade",
  "display_ru": "Никель: Клинок меча",
  "tab": "copper_age"
}
```

### nickel_cast_sword_head

```json
{
  "name": "nickel_cast_sword_head",
  "kind": "tool_part",
  "display": "Nickel: Casting - Sword Blade",
  "display_ru": "Никель: Отливка — Клинок меча",
  "tab": "copper_age"
}
```

### nickel_katana_head

```json
{
  "name": "nickel_katana_head",
  "kind": "tool_part",
  "display": "Nickel: Katana Blade",
  "display_ru": "Никель: Клинок катаны",
  "tab": "copper_age"
}
```

### nickel_cast_katana_head

```json
{
  "name": "nickel_cast_katana_head",
  "kind": "tool_part",
  "display": "Nickel: Casting - Katana Blade",
  "display_ru": "Никель: Отливка — Клинок катаны",
  "tab": "copper_age"
}
```

### nickel_machete_head

```json
{
  "name": "nickel_machete_head",
  "kind": "tool_part",
  "display": "Nickel: Machete Blade",
  "display_ru": "Никель: Клинок мачете",
  "tab": "copper_age"
}
```

### nickel_cast_machete_head

```json
{
  "name": "nickel_cast_machete_head",
  "kind": "tool_part",
  "display": "Nickel: Casting - Machete Blade",
  "display_ru": "Никель: Отливка — Клинок мачете",
  "tab": "copper_age"
}
```

### nickel_sickle_head

```json
{
  "name": "nickel_sickle_head",
  "kind": "tool_part",
  "display": "Nickel: Sickle Blade",
  "display_ru": "Никель: Лезвие серпа",
  "tab": "copper_age"
}
```

### nickel_cast_sickle_head

```json
{
  "name": "nickel_cast_sickle_head",
  "kind": "tool_part",
  "display": "Nickel: Casting - Sickle Blade",
  "display_ru": "Никель: Отливка — Лезвие серпа",
  "tab": "copper_age"
}
```

### nickel_superalloy_pickaxe_head

```json
{
  "name": "nickel_superalloy_pickaxe_head",
  "kind": "tool_part",
  "display": "Nickel Superalloy: Pickaxe Head",
  "display_ru": "Никелевый жаропрочный сплав: Головка кирки",
  "tab": "copper_age"
}
```

### nickel_superalloy_cast_pickaxe_head

```json
{
  "name": "nickel_superalloy_cast_pickaxe_head",
  "kind": "tool_part",
  "display": "Nickel Superalloy: Casting - Pickaxe Head",
  "display_ru": "Никелевый жаропрочный сплав: Отливка — Головка кирки",
  "tab": "copper_age"
}
```

### nickel_superalloy_axe_head

```json
{
  "name": "nickel_superalloy_axe_head",
  "kind": "tool_part",
  "display": "Nickel Superalloy: Axe Head",
  "display_ru": "Никелевый жаропрочный сплав: Головка топора",
  "tab": "copper_age"
}
```

### nickel_superalloy_cast_axe_head

```json
{
  "name": "nickel_superalloy_cast_axe_head",
  "kind": "tool_part",
  "display": "Nickel Superalloy: Casting - Axe Head",
  "display_ru": "Никелевый жаропрочный сплав: Отливка — Головка топора",
  "tab": "copper_age"
}
```

### nickel_superalloy_shovel_head

```json
{
  "name": "nickel_superalloy_shovel_head",
  "kind": "tool_part",
  "display": "Nickel Superalloy: Shovel Head",
  "display_ru": "Никелевый жаропрочный сплав: Головка лопаты",
  "tab": "copper_age"
}
```

### nickel_superalloy_cast_shovel_head

```json
{
  "name": "nickel_superalloy_cast_shovel_head",
  "kind": "tool_part",
  "display": "Nickel Superalloy: Casting - Shovel Head",
  "display_ru": "Никелевый жаропрочный сплав: Отливка — Головка лопаты",
  "tab": "copper_age"
}
```

### nickel_superalloy_hoe_head

```json
{
  "name": "nickel_superalloy_hoe_head",
  "kind": "tool_part",
  "display": "Nickel Superalloy: Hoe Head",
  "display_ru": "Никелевый жаропрочный сплав: Головка мотыги",
  "tab": "copper_age"
}
```

### nickel_superalloy_cast_hoe_head

```json
{
  "name": "nickel_superalloy_cast_hoe_head",
  "kind": "tool_part",
  "display": "Nickel Superalloy: Casting - Hoe Head",
  "display_ru": "Никелевый жаропрочный сплав: Отливка — Головка мотыги",
  "tab": "copper_age"
}
```

### nickel_superalloy_hammer_head

```json
{
  "name": "nickel_superalloy_hammer_head",
  "kind": "tool_part",
  "display": "Nickel Superalloy: Hammer Head",
  "display_ru": "Никелевый жаропрочный сплав: Головка молота",
  "tab": "copper_age"
}
```

### nickel_superalloy_cast_hammer_head

```json
{
  "name": "nickel_superalloy_cast_hammer_head",
  "kind": "tool_part",
  "display": "Nickel Superalloy: Casting - Hammer Head",
  "display_ru": "Никелевый жаропрочный сплав: Отливка — Головка молота",
  "tab": "copper_age"
}
```

### nickel_superalloy_standard_handle

```json
{
  "name": "nickel_superalloy_standard_handle",
  "kind": "tool_part",
  "display": "Nickel Superalloy: Rod",
  "display_ru": "Никелевый жаропрочный сплав: Стержень",
  "tab": "copper_age"
}
```

### nickel_superalloy_clamp

```json
{
  "name": "nickel_superalloy_clamp",
  "kind": "tool_part",
  "display": "Nickel Superalloy: Clamp",
  "display_ru": "Никелевый жаропрочный сплав: Хомут",
  "tab": "copper_age"
}
```

### nickel_superalloy_saw_head

```json
{
  "name": "nickel_superalloy_saw_head",
  "kind": "tool_part",
  "display": "Nickel Superalloy: Saw Blade",
  "display_ru": "Никелевый жаропрочный сплав: Полотно пилы",
  "tab": "copper_age"
}
```

### nickel_superalloy_cast_saw_head

```json
{
  "name": "nickel_superalloy_cast_saw_head",
  "kind": "tool_part",
  "display": "Nickel Superalloy: Casting - Saw Blade",
  "display_ru": "Никелевый жаропрочный сплав: Отливка — Полотно пилы",
  "tab": "copper_age"
}
```

### nickel_superalloy_sword_head

```json
{
  "name": "nickel_superalloy_sword_head",
  "kind": "tool_part",
  "display": "Nickel Superalloy: Sword Blade",
  "display_ru": "Никелевый жаропрочный сплав: Клинок меча",
  "tab": "copper_age"
}
```

### nickel_superalloy_cast_sword_head

```json
{
  "name": "nickel_superalloy_cast_sword_head",
  "kind": "tool_part",
  "display": "Nickel Superalloy: Casting - Sword Blade",
  "display_ru": "Никелевый жаропрочный сплав: Отливка — Клинок меча",
  "tab": "copper_age"
}
```

### nickel_superalloy_katana_head

```json
{
  "name": "nickel_superalloy_katana_head",
  "kind": "tool_part",
  "display": "Nickel Superalloy: Katana Blade",
  "display_ru": "Никелевый жаропрочный сплав: Клинок катаны",
  "tab": "copper_age"
}
```

### nickel_superalloy_cast_katana_head

```json
{
  "name": "nickel_superalloy_cast_katana_head",
  "kind": "tool_part",
  "display": "Nickel Superalloy: Casting - Katana Blade",
  "display_ru": "Никелевый жаропрочный сплав: Отливка — Клинок катаны",
  "tab": "copper_age"
}
```

### nickel_superalloy_machete_head

```json
{
  "name": "nickel_superalloy_machete_head",
  "kind": "tool_part",
  "display": "Nickel Superalloy: Machete Blade",
  "display_ru": "Никелевый жаропрочный сплав: Клинок мачете",
  "tab": "copper_age"
}
```

### nickel_superalloy_cast_machete_head

```json
{
  "name": "nickel_superalloy_cast_machete_head",
  "kind": "tool_part",
  "display": "Nickel Superalloy: Casting - Machete Blade",
  "display_ru": "Никелевый жаропрочный сплав: Отливка — Клинок мачете",
  "tab": "copper_age"
}
```

### nickel_superalloy_sickle_head

```json
{
  "name": "nickel_superalloy_sickle_head",
  "kind": "tool_part",
  "display": "Nickel Superalloy: Sickle Blade",
  "display_ru": "Никелевый жаропрочный сплав: Лезвие серпа",
  "tab": "copper_age"
}
```

### nickel_superalloy_cast_sickle_head

```json
{
  "name": "nickel_superalloy_cast_sickle_head",
  "kind": "tool_part",
  "display": "Nickel Superalloy: Casting - Sickle Blade",
  "display_ru": "Никелевый жаропрочный сплав: Отливка — Лезвие серпа",
  "tab": "copper_age"
}
```

### silver_pickaxe_head

```json
{
  "name": "silver_pickaxe_head",
  "kind": "tool_part",
  "display": "Silver: Pickaxe Head",
  "display_ru": "Серебро: Головка кирки",
  "tab": "copper_age"
}
```

### silver_cast_pickaxe_head

```json
{
  "name": "silver_cast_pickaxe_head",
  "kind": "tool_part",
  "display": "Silver: Casting - Pickaxe Head",
  "display_ru": "Серебро: Отливка — Головка кирки",
  "tab": "copper_age"
}
```

### silver_axe_head

```json
{
  "name": "silver_axe_head",
  "kind": "tool_part",
  "display": "Silver: Axe Head",
  "display_ru": "Серебро: Головка топора",
  "tab": "copper_age"
}
```

### silver_cast_axe_head

```json
{
  "name": "silver_cast_axe_head",
  "kind": "tool_part",
  "display": "Silver: Casting - Axe Head",
  "display_ru": "Серебро: Отливка — Головка топора",
  "tab": "copper_age"
}
```

### silver_shovel_head

```json
{
  "name": "silver_shovel_head",
  "kind": "tool_part",
  "display": "Silver: Shovel Head",
  "display_ru": "Серебро: Головка лопаты",
  "tab": "copper_age"
}
```

### silver_cast_shovel_head

```json
{
  "name": "silver_cast_shovel_head",
  "kind": "tool_part",
  "display": "Silver: Casting - Shovel Head",
  "display_ru": "Серебро: Отливка — Головка лопаты",
  "tab": "copper_age"
}
```

### silver_hoe_head

```json
{
  "name": "silver_hoe_head",
  "kind": "tool_part",
  "display": "Silver: Hoe Head",
  "display_ru": "Серебро: Головка мотыги",
  "tab": "copper_age"
}
```

### silver_cast_hoe_head

```json
{
  "name": "silver_cast_hoe_head",
  "kind": "tool_part",
  "display": "Silver: Casting - Hoe Head",
  "display_ru": "Серебро: Отливка — Головка мотыги",
  "tab": "copper_age"
}
```

### silver_hammer_head

```json
{
  "name": "silver_hammer_head",
  "kind": "tool_part",
  "display": "Silver: Hammer Head",
  "display_ru": "Серебро: Головка молота",
  "tab": "copper_age"
}
```

### silver_cast_hammer_head

```json
{
  "name": "silver_cast_hammer_head",
  "kind": "tool_part",
  "display": "Silver: Casting - Hammer Head",
  "display_ru": "Серебро: Отливка — Головка молота",
  "tab": "copper_age"
}
```

### silver_standard_handle

```json
{
  "name": "silver_standard_handle",
  "kind": "tool_part",
  "display": "Silver: Rod",
  "display_ru": "Серебро: Стержень",
  "tab": "copper_age"
}
```

### silver_clamp

```json
{
  "name": "silver_clamp",
  "kind": "tool_part",
  "display": "Silver: Clamp",
  "display_ru": "Серебро: Хомут",
  "tab": "copper_age"
}
```

### silver_saw_head

```json
{
  "name": "silver_saw_head",
  "kind": "tool_part",
  "display": "Silver: Saw Blade",
  "display_ru": "Серебро: Полотно пилы",
  "tab": "copper_age"
}
```

### silver_cast_saw_head

```json
{
  "name": "silver_cast_saw_head",
  "kind": "tool_part",
  "display": "Silver: Casting - Saw Blade",
  "display_ru": "Серебро: Отливка — Полотно пилы",
  "tab": "copper_age"
}
```

### silver_sword_head

```json
{
  "name": "silver_sword_head",
  "kind": "tool_part",
  "display": "Silver: Sword Blade",
  "display_ru": "Серебро: Клинок меча",
  "tab": "copper_age"
}
```

### silver_cast_sword_head

```json
{
  "name": "silver_cast_sword_head",
  "kind": "tool_part",
  "display": "Silver: Casting - Sword Blade",
  "display_ru": "Серебро: Отливка — Клинок меча",
  "tab": "copper_age"
}
```

### silver_katana_head

```json
{
  "name": "silver_katana_head",
  "kind": "tool_part",
  "display": "Silver: Katana Blade",
  "display_ru": "Серебро: Клинок катаны",
  "tab": "copper_age"
}
```

### silver_cast_katana_head

```json
{
  "name": "silver_cast_katana_head",
  "kind": "tool_part",
  "display": "Silver: Casting - Katana Blade",
  "display_ru": "Серебро: Отливка — Клинок катаны",
  "tab": "copper_age"
}
```

### silver_machete_head

```json
{
  "name": "silver_machete_head",
  "kind": "tool_part",
  "display": "Silver: Machete Blade",
  "display_ru": "Серебро: Клинок мачете",
  "tab": "copper_age"
}
```

### silver_cast_machete_head

```json
{
  "name": "silver_cast_machete_head",
  "kind": "tool_part",
  "display": "Silver: Casting - Machete Blade",
  "display_ru": "Серебро: Отливка — Клинок мачете",
  "tab": "copper_age"
}
```

### silver_sickle_head

```json
{
  "name": "silver_sickle_head",
  "kind": "tool_part",
  "display": "Silver: Sickle Blade",
  "display_ru": "Серебро: Лезвие серпа",
  "tab": "copper_age"
}
```

### silver_cast_sickle_head

```json
{
  "name": "silver_cast_sickle_head",
  "kind": "tool_part",
  "display": "Silver: Casting - Sickle Blade",
  "display_ru": "Серебро: Отливка — Лезвие серпа",
  "tab": "copper_age"
}
```

### solder_pickaxe_head

```json
{
  "name": "solder_pickaxe_head",
  "kind": "tool_part",
  "display": "Tin-Lead Solder: Pickaxe Head",
  "display_ru": "Припой: Головка кирки",
  "tab": "copper_age"
}
```

### solder_cast_pickaxe_head

```json
{
  "name": "solder_cast_pickaxe_head",
  "kind": "tool_part",
  "display": "Tin-Lead Solder: Casting - Pickaxe Head",
  "display_ru": "Припой: Отливка — Головка кирки",
  "tab": "copper_age"
}
```

### solder_axe_head

```json
{
  "name": "solder_axe_head",
  "kind": "tool_part",
  "display": "Tin-Lead Solder: Axe Head",
  "display_ru": "Припой: Головка топора",
  "tab": "copper_age"
}
```

### solder_cast_axe_head

```json
{
  "name": "solder_cast_axe_head",
  "kind": "tool_part",
  "display": "Tin-Lead Solder: Casting - Axe Head",
  "display_ru": "Припой: Отливка — Головка топора",
  "tab": "copper_age"
}
```

### solder_shovel_head

```json
{
  "name": "solder_shovel_head",
  "kind": "tool_part",
  "display": "Tin-Lead Solder: Shovel Head",
  "display_ru": "Припой: Головка лопаты",
  "tab": "copper_age"
}
```

### solder_cast_shovel_head

```json
{
  "name": "solder_cast_shovel_head",
  "kind": "tool_part",
  "display": "Tin-Lead Solder: Casting - Shovel Head",
  "display_ru": "Припой: Отливка — Головка лопаты",
  "tab": "copper_age"
}
```

### solder_hoe_head

```json
{
  "name": "solder_hoe_head",
  "kind": "tool_part",
  "display": "Tin-Lead Solder: Hoe Head",
  "display_ru": "Припой: Головка мотыги",
  "tab": "copper_age"
}
```

### solder_cast_hoe_head

```json
{
  "name": "solder_cast_hoe_head",
  "kind": "tool_part",
  "display": "Tin-Lead Solder: Casting - Hoe Head",
  "display_ru": "Припой: Отливка — Головка мотыги",
  "tab": "copper_age"
}
```

### solder_hammer_head

```json
{
  "name": "solder_hammer_head",
  "kind": "tool_part",
  "display": "Tin-Lead Solder: Hammer Head",
  "display_ru": "Припой: Головка молота",
  "tab": "copper_age"
}
```

### solder_cast_hammer_head

```json
{
  "name": "solder_cast_hammer_head",
  "kind": "tool_part",
  "display": "Tin-Lead Solder: Casting - Hammer Head",
  "display_ru": "Припой: Отливка — Головка молота",
  "tab": "copper_age"
}
```

### solder_standard_handle

```json
{
  "name": "solder_standard_handle",
  "kind": "tool_part",
  "display": "Tin-Lead Solder: Rod",
  "display_ru": "Припой: Стержень",
  "tab": "copper_age"
}
```

### solder_clamp

```json
{
  "name": "solder_clamp",
  "kind": "tool_part",
  "display": "Tin-Lead Solder: Clamp",
  "display_ru": "Припой: Хомут",
  "tab": "copper_age"
}
```

### solder_saw_head

```json
{
  "name": "solder_saw_head",
  "kind": "tool_part",
  "display": "Tin-Lead Solder: Saw Blade",
  "display_ru": "Припой: Полотно пилы",
  "tab": "copper_age"
}
```

### solder_cast_saw_head

```json
{
  "name": "solder_cast_saw_head",
  "kind": "tool_part",
  "display": "Tin-Lead Solder: Casting - Saw Blade",
  "display_ru": "Припой: Отливка — Полотно пилы",
  "tab": "copper_age"
}
```

### solder_sword_head

```json
{
  "name": "solder_sword_head",
  "kind": "tool_part",
  "display": "Tin-Lead Solder: Sword Blade",
  "display_ru": "Припой: Клинок меча",
  "tab": "copper_age"
}
```

### solder_cast_sword_head

```json
{
  "name": "solder_cast_sword_head",
  "kind": "tool_part",
  "display": "Tin-Lead Solder: Casting - Sword Blade",
  "display_ru": "Припой: Отливка — Клинок меча",
  "tab": "copper_age"
}
```

### solder_katana_head

```json
{
  "name": "solder_katana_head",
  "kind": "tool_part",
  "display": "Tin-Lead Solder: Katana Blade",
  "display_ru": "Припой: Клинок катаны",
  "tab": "copper_age"
}
```

### solder_cast_katana_head

```json
{
  "name": "solder_cast_katana_head",
  "kind": "tool_part",
  "display": "Tin-Lead Solder: Casting - Katana Blade",
  "display_ru": "Припой: Отливка — Клинок катаны",
  "tab": "copper_age"
}
```

### solder_machete_head

```json
{
  "name": "solder_machete_head",
  "kind": "tool_part",
  "display": "Tin-Lead Solder: Machete Blade",
  "display_ru": "Припой: Клинок мачете",
  "tab": "copper_age"
}
```

### solder_cast_machete_head

```json
{
  "name": "solder_cast_machete_head",
  "kind": "tool_part",
  "display": "Tin-Lead Solder: Casting - Machete Blade",
  "display_ru": "Припой: Отливка — Клинок мачете",
  "tab": "copper_age"
}
```

### solder_sickle_head

```json
{
  "name": "solder_sickle_head",
  "kind": "tool_part",
  "display": "Tin-Lead Solder: Sickle Blade",
  "display_ru": "Припой: Лезвие серпа",
  "tab": "copper_age"
}
```

### solder_cast_sickle_head

```json
{
  "name": "solder_cast_sickle_head",
  "kind": "tool_part",
  "display": "Tin-Lead Solder: Casting - Sickle Blade",
  "display_ru": "Припой: Отливка — Лезвие серпа",
  "tab": "copper_age"
}
```

### stainless_steel_pickaxe_head

```json
{
  "name": "stainless_steel_pickaxe_head",
  "kind": "tool_part",
  "display": "Stainless Steel: Pickaxe Head",
  "display_ru": "Нержавеющая сталь: Головка кирки",
  "tab": "copper_age"
}
```

### stainless_steel_cast_pickaxe_head

```json
{
  "name": "stainless_steel_cast_pickaxe_head",
  "kind": "tool_part",
  "display": "Stainless Steel: Casting - Pickaxe Head",
  "display_ru": "Нержавеющая сталь: Отливка — Головка кирки",
  "tab": "copper_age"
}
```

### stainless_steel_axe_head

```json
{
  "name": "stainless_steel_axe_head",
  "kind": "tool_part",
  "display": "Stainless Steel: Axe Head",
  "display_ru": "Нержавеющая сталь: Головка топора",
  "tab": "copper_age"
}
```

### stainless_steel_cast_axe_head

```json
{
  "name": "stainless_steel_cast_axe_head",
  "kind": "tool_part",
  "display": "Stainless Steel: Casting - Axe Head",
  "display_ru": "Нержавеющая сталь: Отливка — Головка топора",
  "tab": "copper_age"
}
```

### stainless_steel_shovel_head

```json
{
  "name": "stainless_steel_shovel_head",
  "kind": "tool_part",
  "display": "Stainless Steel: Shovel Head",
  "display_ru": "Нержавеющая сталь: Головка лопаты",
  "tab": "copper_age"
}
```

### stainless_steel_cast_shovel_head

```json
{
  "name": "stainless_steel_cast_shovel_head",
  "kind": "tool_part",
  "display": "Stainless Steel: Casting - Shovel Head",
  "display_ru": "Нержавеющая сталь: Отливка — Головка лопаты",
  "tab": "copper_age"
}
```

### stainless_steel_hoe_head

```json
{
  "name": "stainless_steel_hoe_head",
  "kind": "tool_part",
  "display": "Stainless Steel: Hoe Head",
  "display_ru": "Нержавеющая сталь: Головка мотыги",
  "tab": "copper_age"
}
```

### stainless_steel_cast_hoe_head

```json
{
  "name": "stainless_steel_cast_hoe_head",
  "kind": "tool_part",
  "display": "Stainless Steel: Casting - Hoe Head",
  "display_ru": "Нержавеющая сталь: Отливка — Головка мотыги",
  "tab": "copper_age"
}
```

### stainless_steel_hammer_head

```json
{
  "name": "stainless_steel_hammer_head",
  "kind": "tool_part",
  "display": "Stainless Steel: Hammer Head",
  "display_ru": "Нержавеющая сталь: Головка молота",
  "tab": "copper_age"
}
```

### stainless_steel_cast_hammer_head

```json
{
  "name": "stainless_steel_cast_hammer_head",
  "kind": "tool_part",
  "display": "Stainless Steel: Casting - Hammer Head",
  "display_ru": "Нержавеющая сталь: Отливка — Головка молота",
  "tab": "copper_age"
}
```

### stainless_steel_standard_handle

```json
{
  "name": "stainless_steel_standard_handle",
  "kind": "tool_part",
  "display": "Stainless Steel: Rod",
  "display_ru": "Нержавеющая сталь: Стержень",
  "tab": "copper_age"
}
```

### stainless_steel_clamp

```json
{
  "name": "stainless_steel_clamp",
  "kind": "tool_part",
  "display": "Stainless Steel: Clamp",
  "display_ru": "Нержавеющая сталь: Хомут",
  "tab": "copper_age"
}
```

### stainless_steel_saw_head

```json
{
  "name": "stainless_steel_saw_head",
  "kind": "tool_part",
  "display": "Stainless Steel: Saw Blade",
  "display_ru": "Нержавеющая сталь: Полотно пилы",
  "tab": "copper_age"
}
```

### stainless_steel_cast_saw_head

```json
{
  "name": "stainless_steel_cast_saw_head",
  "kind": "tool_part",
  "display": "Stainless Steel: Casting - Saw Blade",
  "display_ru": "Нержавеющая сталь: Отливка — Полотно пилы",
  "tab": "copper_age"
}
```

### stainless_steel_sword_head

```json
{
  "name": "stainless_steel_sword_head",
  "kind": "tool_part",
  "display": "Stainless Steel: Sword Blade",
  "display_ru": "Нержавеющая сталь: Клинок меча",
  "tab": "copper_age"
}
```

### stainless_steel_cast_sword_head

```json
{
  "name": "stainless_steel_cast_sword_head",
  "kind": "tool_part",
  "display": "Stainless Steel: Casting - Sword Blade",
  "display_ru": "Нержавеющая сталь: Отливка — Клинок меча",
  "tab": "copper_age"
}
```

### stainless_steel_katana_head

```json
{
  "name": "stainless_steel_katana_head",
  "kind": "tool_part",
  "display": "Stainless Steel: Katana Blade",
  "display_ru": "Нержавеющая сталь: Клинок катаны",
  "tab": "copper_age"
}
```

### stainless_steel_cast_katana_head

```json
{
  "name": "stainless_steel_cast_katana_head",
  "kind": "tool_part",
  "display": "Stainless Steel: Casting - Katana Blade",
  "display_ru": "Нержавеющая сталь: Отливка — Клинок катаны",
  "tab": "copper_age"
}
```

### stainless_steel_machete_head

```json
{
  "name": "stainless_steel_machete_head",
  "kind": "tool_part",
  "display": "Stainless Steel: Machete Blade",
  "display_ru": "Нержавеющая сталь: Клинок мачете",
  "tab": "copper_age"
}
```

### stainless_steel_cast_machete_head

```json
{
  "name": "stainless_steel_cast_machete_head",
  "kind": "tool_part",
  "display": "Stainless Steel: Casting - Machete Blade",
  "display_ru": "Нержавеющая сталь: Отливка — Клинок мачете",
  "tab": "copper_age"
}
```

### stainless_steel_sickle_head

```json
{
  "name": "stainless_steel_sickle_head",
  "kind": "tool_part",
  "display": "Stainless Steel: Sickle Blade",
  "display_ru": "Нержавеющая сталь: Лезвие серпа",
  "tab": "copper_age"
}
```

### stainless_steel_cast_sickle_head

```json
{
  "name": "stainless_steel_cast_sickle_head",
  "kind": "tool_part",
  "display": "Stainless Steel: Casting - Sickle Blade",
  "display_ru": "Нержавеющая сталь: Отливка — Лезвие серпа",
  "tab": "copper_age"
}
```

### steel_pickaxe_head

```json
{
  "name": "steel_pickaxe_head",
  "kind": "tool_part",
  "display": "Steel: Pickaxe Head",
  "display_ru": "Сталь: Головка кирки",
  "tab": "copper_age"
}
```

### steel_cast_pickaxe_head

```json
{
  "name": "steel_cast_pickaxe_head",
  "kind": "tool_part",
  "display": "Steel: Casting - Pickaxe Head",
  "display_ru": "Сталь: Отливка — Головка кирки",
  "tab": "copper_age"
}
```

### steel_axe_head

```json
{
  "name": "steel_axe_head",
  "kind": "tool_part",
  "display": "Steel: Axe Head",
  "display_ru": "Сталь: Головка топора",
  "tab": "copper_age"
}
```

### steel_cast_axe_head

```json
{
  "name": "steel_cast_axe_head",
  "kind": "tool_part",
  "display": "Steel: Casting - Axe Head",
  "display_ru": "Сталь: Отливка — Головка топора",
  "tab": "copper_age"
}
```

### steel_shovel_head

```json
{
  "name": "steel_shovel_head",
  "kind": "tool_part",
  "display": "Steel: Shovel Head",
  "display_ru": "Сталь: Головка лопаты",
  "tab": "copper_age"
}
```

### steel_cast_shovel_head

```json
{
  "name": "steel_cast_shovel_head",
  "kind": "tool_part",
  "display": "Steel: Casting - Shovel Head",
  "display_ru": "Сталь: Отливка — Головка лопаты",
  "tab": "copper_age"
}
```

### steel_hoe_head

```json
{
  "name": "steel_hoe_head",
  "kind": "tool_part",
  "display": "Steel: Hoe Head",
  "display_ru": "Сталь: Головка мотыги",
  "tab": "copper_age"
}
```

### steel_cast_hoe_head

```json
{
  "name": "steel_cast_hoe_head",
  "kind": "tool_part",
  "display": "Steel: Casting - Hoe Head",
  "display_ru": "Сталь: Отливка — Головка мотыги",
  "tab": "copper_age"
}
```

### steel_hammer_head

```json
{
  "name": "steel_hammer_head",
  "kind": "tool_part",
  "display": "Steel: Hammer Head",
  "display_ru": "Сталь: Головка молота",
  "tab": "copper_age"
}
```

### steel_cast_hammer_head

```json
{
  "name": "steel_cast_hammer_head",
  "kind": "tool_part",
  "display": "Steel: Casting - Hammer Head",
  "display_ru": "Сталь: Отливка — Головка молота",
  "tab": "copper_age"
}
```

### steel_standard_handle

```json
{
  "name": "steel_standard_handle",
  "kind": "tool_part",
  "display": "Steel: Rod",
  "display_ru": "Сталь: Стержень",
  "tab": "copper_age"
}
```

### steel_clamp

```json
{
  "name": "steel_clamp",
  "kind": "tool_part",
  "display": "Steel: Clamp",
  "display_ru": "Сталь: Хомут",
  "tab": "copper_age"
}
```

### steel_saw_head

```json
{
  "name": "steel_saw_head",
  "kind": "tool_part",
  "display": "Steel: Saw Blade",
  "display_ru": "Сталь: Полотно пилы",
  "tab": "copper_age"
}
```

### steel_cast_saw_head

```json
{
  "name": "steel_cast_saw_head",
  "kind": "tool_part",
  "display": "Steel: Casting - Saw Blade",
  "display_ru": "Сталь: Отливка — Полотно пилы",
  "tab": "copper_age"
}
```

### steel_sword_head

```json
{
  "name": "steel_sword_head",
  "kind": "tool_part",
  "display": "Steel: Sword Blade",
  "display_ru": "Сталь: Клинок меча",
  "tab": "copper_age"
}
```

### steel_cast_sword_head

```json
{
  "name": "steel_cast_sword_head",
  "kind": "tool_part",
  "display": "Steel: Casting - Sword Blade",
  "display_ru": "Сталь: Отливка — Клинок меча",
  "tab": "copper_age"
}
```

### steel_katana_head

```json
{
  "name": "steel_katana_head",
  "kind": "tool_part",
  "display": "Steel: Katana Blade",
  "display_ru": "Сталь: Клинок катаны",
  "tab": "copper_age"
}
```

### steel_cast_katana_head

```json
{
  "name": "steel_cast_katana_head",
  "kind": "tool_part",
  "display": "Steel: Casting - Katana Blade",
  "display_ru": "Сталь: Отливка — Клинок катаны",
  "tab": "copper_age"
}
```

### steel_machete_head

```json
{
  "name": "steel_machete_head",
  "kind": "tool_part",
  "display": "Steel: Machete Blade",
  "display_ru": "Сталь: Клинок мачете",
  "tab": "copper_age"
}
```

### steel_cast_machete_head

```json
{
  "name": "steel_cast_machete_head",
  "kind": "tool_part",
  "display": "Steel: Casting - Machete Blade",
  "display_ru": "Сталь: Отливка — Клинок мачете",
  "tab": "copper_age"
}
```

### steel_sickle_head

```json
{
  "name": "steel_sickle_head",
  "kind": "tool_part",
  "display": "Steel: Sickle Blade",
  "display_ru": "Сталь: Лезвие серпа",
  "tab": "copper_age"
}
```

### steel_cast_sickle_head

```json
{
  "name": "steel_cast_sickle_head",
  "kind": "tool_part",
  "display": "Steel: Casting - Sickle Blade",
  "display_ru": "Сталь: Отливка — Лезвие серпа",
  "tab": "copper_age"
}
```

### stone_pickaxe_head

```json
{
  "name": "stone_pickaxe_head",
  "kind": "tool_part",
  "display": "Stone: Pickaxe Head",
  "display_ru": "Камень: Головка кирки",
  "tab": "copper_age"
}
```

### stone_axe_head

```json
{
  "name": "stone_axe_head",
  "kind": "tool_part",
  "display": "Stone: Axe Head",
  "display_ru": "Камень: Головка топора",
  "tab": "copper_age"
}
```

### stone_shovel_head

```json
{
  "name": "stone_shovel_head",
  "kind": "tool_part",
  "display": "Stone: Shovel Head",
  "display_ru": "Камень: Головка лопаты",
  "tab": "copper_age"
}
```

### stone_hoe_head

```json
{
  "name": "stone_hoe_head",
  "kind": "tool_part",
  "display": "Stone: Hoe Head",
  "display_ru": "Камень: Головка мотыги",
  "tab": "copper_age"
}
```

### stone_hammer_head

```json
{
  "name": "stone_hammer_head",
  "kind": "tool_part",
  "display": "Stone: Hammer Head",
  "display_ru": "Камень: Головка молота",
  "tab": "copper_age"
}
```

### stone_standard_handle

```json
{
  "name": "stone_standard_handle",
  "kind": "tool_part",
  "display": "Stone: Rod",
  "display_ru": "Камень: Стержень",
  "tab": "copper_age"
}
```

### stone_saw_head

```json
{
  "name": "stone_saw_head",
  "kind": "tool_part",
  "display": "Stone: Saw Blade",
  "display_ru": "Камень: Полотно пилы",
  "tab": "copper_age"
}
```

### stone_sword_head

```json
{
  "name": "stone_sword_head",
  "kind": "tool_part",
  "display": "Stone: Sword Blade",
  "display_ru": "Камень: Клинок меча",
  "tab": "copper_age"
}
```

### stone_katana_head

```json
{
  "name": "stone_katana_head",
  "kind": "tool_part",
  "display": "Stone: Katana Blade",
  "display_ru": "Камень: Клинок катаны",
  "tab": "copper_age"
}
```

### stone_machete_head

```json
{
  "name": "stone_machete_head",
  "kind": "tool_part",
  "display": "Stone: Machete Blade",
  "display_ru": "Камень: Клинок мачете",
  "tab": "copper_age"
}
```

### stone_sickle_head

```json
{
  "name": "stone_sickle_head",
  "kind": "tool_part",
  "display": "Stone: Sickle Blade",
  "display_ru": "Камень: Лезвие серпа",
  "tab": "copper_age"
}
```

### tin_pickaxe_head

```json
{
  "name": "tin_pickaxe_head",
  "kind": "tool_part",
  "display": "Tin: Pickaxe Head",
  "display_ru": "Олово: Головка кирки",
  "tab": "copper_age"
}
```

### tin_cast_pickaxe_head

```json
{
  "name": "tin_cast_pickaxe_head",
  "kind": "tool_part",
  "display": "Tin: Casting - Pickaxe Head",
  "display_ru": "Олово: Отливка — Головка кирки",
  "tab": "copper_age"
}
```

### tin_axe_head

```json
{
  "name": "tin_axe_head",
  "kind": "tool_part",
  "display": "Tin: Axe Head",
  "display_ru": "Олово: Головка топора",
  "tab": "copper_age"
}
```

### tin_cast_axe_head

```json
{
  "name": "tin_cast_axe_head",
  "kind": "tool_part",
  "display": "Tin: Casting - Axe Head",
  "display_ru": "Олово: Отливка — Головка топора",
  "tab": "copper_age"
}
```

### tin_shovel_head

```json
{
  "name": "tin_shovel_head",
  "kind": "tool_part",
  "display": "Tin: Shovel Head",
  "display_ru": "Олово: Головка лопаты",
  "tab": "copper_age"
}
```

### tin_cast_shovel_head

```json
{
  "name": "tin_cast_shovel_head",
  "kind": "tool_part",
  "display": "Tin: Casting - Shovel Head",
  "display_ru": "Олово: Отливка — Головка лопаты",
  "tab": "copper_age"
}
```

### tin_hoe_head

```json
{
  "name": "tin_hoe_head",
  "kind": "tool_part",
  "display": "Tin: Hoe Head",
  "display_ru": "Олово: Головка мотыги",
  "tab": "copper_age"
}
```

### tin_cast_hoe_head

```json
{
  "name": "tin_cast_hoe_head",
  "kind": "tool_part",
  "display": "Tin: Casting - Hoe Head",
  "display_ru": "Олово: Отливка — Головка мотыги",
  "tab": "copper_age"
}
```

### tin_hammer_head

```json
{
  "name": "tin_hammer_head",
  "kind": "tool_part",
  "display": "Tin: Hammer Head",
  "display_ru": "Олово: Головка молота",
  "tab": "copper_age"
}
```

### tin_cast_hammer_head

```json
{
  "name": "tin_cast_hammer_head",
  "kind": "tool_part",
  "display": "Tin: Casting - Hammer Head",
  "display_ru": "Олово: Отливка — Головка молота",
  "tab": "copper_age"
}
```

### tin_standard_handle

```json
{
  "name": "tin_standard_handle",
  "kind": "tool_part",
  "display": "Tin: Rod",
  "display_ru": "Олово: Стержень",
  "tab": "copper_age"
}
```

### tin_clamp

```json
{
  "name": "tin_clamp",
  "kind": "tool_part",
  "display": "Tin: Clamp",
  "display_ru": "Олово: Хомут",
  "tab": "copper_age"
}
```

### tin_saw_head

```json
{
  "name": "tin_saw_head",
  "kind": "tool_part",
  "display": "Tin: Saw Blade",
  "display_ru": "Олово: Полотно пилы",
  "tab": "copper_age"
}
```

### tin_cast_saw_head

```json
{
  "name": "tin_cast_saw_head",
  "kind": "tool_part",
  "display": "Tin: Casting - Saw Blade",
  "display_ru": "Олово: Отливка — Полотно пилы",
  "tab": "copper_age"
}
```

### tin_sword_head

```json
{
  "name": "tin_sword_head",
  "kind": "tool_part",
  "display": "Tin: Sword Blade",
  "display_ru": "Олово: Клинок меча",
  "tab": "copper_age"
}
```

### tin_cast_sword_head

```json
{
  "name": "tin_cast_sword_head",
  "kind": "tool_part",
  "display": "Tin: Casting - Sword Blade",
  "display_ru": "Олово: Отливка — Клинок меча",
  "tab": "copper_age"
}
```

### tin_katana_head

```json
{
  "name": "tin_katana_head",
  "kind": "tool_part",
  "display": "Tin: Katana Blade",
  "display_ru": "Олово: Клинок катаны",
  "tab": "copper_age"
}
```

### tin_cast_katana_head

```json
{
  "name": "tin_cast_katana_head",
  "kind": "tool_part",
  "display": "Tin: Casting - Katana Blade",
  "display_ru": "Олово: Отливка — Клинок катаны",
  "tab": "copper_age"
}
```

### tin_machete_head

```json
{
  "name": "tin_machete_head",
  "kind": "tool_part",
  "display": "Tin: Machete Blade",
  "display_ru": "Олово: Клинок мачете",
  "tab": "copper_age"
}
```

### tin_cast_machete_head

```json
{
  "name": "tin_cast_machete_head",
  "kind": "tool_part",
  "display": "Tin: Casting - Machete Blade",
  "display_ru": "Олово: Отливка — Клинок мачете",
  "tab": "copper_age"
}
```

### tin_sickle_head

```json
{
  "name": "tin_sickle_head",
  "kind": "tool_part",
  "display": "Tin: Sickle Blade",
  "display_ru": "Олово: Лезвие серпа",
  "tab": "copper_age"
}
```

### tin_cast_sickle_head

```json
{
  "name": "tin_cast_sickle_head",
  "kind": "tool_part",
  "display": "Tin: Casting - Sickle Blade",
  "display_ru": "Олово: Отливка — Лезвие серпа",
  "tab": "copper_age"
}
```

### titanium_pickaxe_head

```json
{
  "name": "titanium_pickaxe_head",
  "kind": "tool_part",
  "display": "Titanium: Pickaxe Head",
  "display_ru": "Титан: Головка кирки",
  "tab": "copper_age"
}
```

### titanium_cast_pickaxe_head

```json
{
  "name": "titanium_cast_pickaxe_head",
  "kind": "tool_part",
  "display": "Titanium: Casting - Pickaxe Head",
  "display_ru": "Титан: Отливка — Головка кирки",
  "tab": "copper_age"
}
```

### titanium_axe_head

```json
{
  "name": "titanium_axe_head",
  "kind": "tool_part",
  "display": "Titanium: Axe Head",
  "display_ru": "Титан: Головка топора",
  "tab": "copper_age"
}
```

### titanium_cast_axe_head

```json
{
  "name": "titanium_cast_axe_head",
  "kind": "tool_part",
  "display": "Titanium: Casting - Axe Head",
  "display_ru": "Титан: Отливка — Головка топора",
  "tab": "copper_age"
}
```

### titanium_shovel_head

```json
{
  "name": "titanium_shovel_head",
  "kind": "tool_part",
  "display": "Titanium: Shovel Head",
  "display_ru": "Титан: Головка лопаты",
  "tab": "copper_age"
}
```

### titanium_cast_shovel_head

```json
{
  "name": "titanium_cast_shovel_head",
  "kind": "tool_part",
  "display": "Titanium: Casting - Shovel Head",
  "display_ru": "Титан: Отливка — Головка лопаты",
  "tab": "copper_age"
}
```

### titanium_hoe_head

```json
{
  "name": "titanium_hoe_head",
  "kind": "tool_part",
  "display": "Titanium: Hoe Head",
  "display_ru": "Титан: Головка мотыги",
  "tab": "copper_age"
}
```

### titanium_cast_hoe_head

```json
{
  "name": "titanium_cast_hoe_head",
  "kind": "tool_part",
  "display": "Titanium: Casting - Hoe Head",
  "display_ru": "Титан: Отливка — Головка мотыги",
  "tab": "copper_age"
}
```

### titanium_hammer_head

```json
{
  "name": "titanium_hammer_head",
  "kind": "tool_part",
  "display": "Titanium: Hammer Head",
  "display_ru": "Титан: Головка молота",
  "tab": "copper_age"
}
```

### titanium_cast_hammer_head

```json
{
  "name": "titanium_cast_hammer_head",
  "kind": "tool_part",
  "display": "Titanium: Casting - Hammer Head",
  "display_ru": "Титан: Отливка — Головка молота",
  "tab": "copper_age"
}
```

### titanium_standard_handle

```json
{
  "name": "titanium_standard_handle",
  "kind": "tool_part",
  "display": "Titanium: Rod",
  "display_ru": "Титан: Стержень",
  "tab": "copper_age"
}
```

### titanium_clamp

```json
{
  "name": "titanium_clamp",
  "kind": "tool_part",
  "display": "Titanium: Clamp",
  "display_ru": "Титан: Хомут",
  "tab": "copper_age"
}
```

### titanium_saw_head

```json
{
  "name": "titanium_saw_head",
  "kind": "tool_part",
  "display": "Titanium: Saw Blade",
  "display_ru": "Титан: Полотно пилы",
  "tab": "copper_age"
}
```

### titanium_cast_saw_head

```json
{
  "name": "titanium_cast_saw_head",
  "kind": "tool_part",
  "display": "Titanium: Casting - Saw Blade",
  "display_ru": "Титан: Отливка — Полотно пилы",
  "tab": "copper_age"
}
```

### titanium_sword_head

```json
{
  "name": "titanium_sword_head",
  "kind": "tool_part",
  "display": "Titanium: Sword Blade",
  "display_ru": "Титан: Клинок меча",
  "tab": "copper_age"
}
```

### titanium_cast_sword_head

```json
{
  "name": "titanium_cast_sword_head",
  "kind": "tool_part",
  "display": "Titanium: Casting - Sword Blade",
  "display_ru": "Титан: Отливка — Клинок меча",
  "tab": "copper_age"
}
```

### titanium_katana_head

```json
{
  "name": "titanium_katana_head",
  "kind": "tool_part",
  "display": "Titanium: Katana Blade",
  "display_ru": "Титан: Клинок катаны",
  "tab": "copper_age"
}
```

### titanium_cast_katana_head

```json
{
  "name": "titanium_cast_katana_head",
  "kind": "tool_part",
  "display": "Titanium: Casting - Katana Blade",
  "display_ru": "Титан: Отливка — Клинок катаны",
  "tab": "copper_age"
}
```

### titanium_machete_head

```json
{
  "name": "titanium_machete_head",
  "kind": "tool_part",
  "display": "Titanium: Machete Blade",
  "display_ru": "Титан: Клинок мачете",
  "tab": "copper_age"
}
```

### titanium_cast_machete_head

```json
{
  "name": "titanium_cast_machete_head",
  "kind": "tool_part",
  "display": "Titanium: Casting - Machete Blade",
  "display_ru": "Титан: Отливка — Клинок мачете",
  "tab": "copper_age"
}
```

### titanium_sickle_head

```json
{
  "name": "titanium_sickle_head",
  "kind": "tool_part",
  "display": "Titanium: Sickle Blade",
  "display_ru": "Титан: Лезвие серпа",
  "tab": "copper_age"
}
```

### titanium_cast_sickle_head

```json
{
  "name": "titanium_cast_sickle_head",
  "kind": "tool_part",
  "display": "Titanium: Casting - Sickle Blade",
  "display_ru": "Титан: Отливка — Лезвие серпа",
  "tab": "copper_age"
}
```

### titanium_alloy_pickaxe_head

```json
{
  "name": "titanium_alloy_pickaxe_head",
  "kind": "tool_part",
  "display": "Titanium Alloy: Pickaxe Head",
  "display_ru": "Титановый сплав: Головка кирки",
  "tab": "copper_age"
}
```

### titanium_alloy_cast_pickaxe_head

```json
{
  "name": "titanium_alloy_cast_pickaxe_head",
  "kind": "tool_part",
  "display": "Titanium Alloy: Casting - Pickaxe Head",
  "display_ru": "Титановый сплав: Отливка — Головка кирки",
  "tab": "copper_age"
}
```

### titanium_alloy_axe_head

```json
{
  "name": "titanium_alloy_axe_head",
  "kind": "tool_part",
  "display": "Titanium Alloy: Axe Head",
  "display_ru": "Титановый сплав: Головка топора",
  "tab": "copper_age"
}
```

### titanium_alloy_cast_axe_head

```json
{
  "name": "titanium_alloy_cast_axe_head",
  "kind": "tool_part",
  "display": "Titanium Alloy: Casting - Axe Head",
  "display_ru": "Титановый сплав: Отливка — Головка топора",
  "tab": "copper_age"
}
```

### titanium_alloy_shovel_head

```json
{
  "name": "titanium_alloy_shovel_head",
  "kind": "tool_part",
  "display": "Titanium Alloy: Shovel Head",
  "display_ru": "Титановый сплав: Головка лопаты",
  "tab": "copper_age"
}
```

### titanium_alloy_cast_shovel_head

```json
{
  "name": "titanium_alloy_cast_shovel_head",
  "kind": "tool_part",
  "display": "Titanium Alloy: Casting - Shovel Head",
  "display_ru": "Титановый сплав: Отливка — Головка лопаты",
  "tab": "copper_age"
}
```

### titanium_alloy_hoe_head

```json
{
  "name": "titanium_alloy_hoe_head",
  "kind": "tool_part",
  "display": "Titanium Alloy: Hoe Head",
  "display_ru": "Титановый сплав: Головка мотыги",
  "tab": "copper_age"
}
```

### titanium_alloy_cast_hoe_head

```json
{
  "name": "titanium_alloy_cast_hoe_head",
  "kind": "tool_part",
  "display": "Titanium Alloy: Casting - Hoe Head",
  "display_ru": "Титановый сплав: Отливка — Головка мотыги",
  "tab": "copper_age"
}
```

### titanium_alloy_hammer_head

```json
{
  "name": "titanium_alloy_hammer_head",
  "kind": "tool_part",
  "display": "Titanium Alloy: Hammer Head",
  "display_ru": "Титановый сплав: Головка молота",
  "tab": "copper_age"
}
```

### titanium_alloy_cast_hammer_head

```json
{
  "name": "titanium_alloy_cast_hammer_head",
  "kind": "tool_part",
  "display": "Titanium Alloy: Casting - Hammer Head",
  "display_ru": "Титановый сплав: Отливка — Головка молота",
  "tab": "copper_age"
}
```

### titanium_alloy_standard_handle

```json
{
  "name": "titanium_alloy_standard_handle",
  "kind": "tool_part",
  "display": "Titanium Alloy: Rod",
  "display_ru": "Титановый сплав: Стержень",
  "tab": "copper_age"
}
```

### titanium_alloy_clamp

```json
{
  "name": "titanium_alloy_clamp",
  "kind": "tool_part",
  "display": "Titanium Alloy: Clamp",
  "display_ru": "Титановый сплав: Хомут",
  "tab": "copper_age"
}
```

### titanium_alloy_saw_head

```json
{
  "name": "titanium_alloy_saw_head",
  "kind": "tool_part",
  "display": "Titanium Alloy: Saw Blade",
  "display_ru": "Титановый сплав: Полотно пилы",
  "tab": "copper_age"
}
```

### titanium_alloy_cast_saw_head

```json
{
  "name": "titanium_alloy_cast_saw_head",
  "kind": "tool_part",
  "display": "Titanium Alloy: Casting - Saw Blade",
  "display_ru": "Титановый сплав: Отливка — Полотно пилы",
  "tab": "copper_age"
}
```

### titanium_alloy_sword_head

```json
{
  "name": "titanium_alloy_sword_head",
  "kind": "tool_part",
  "display": "Titanium Alloy: Sword Blade",
  "display_ru": "Титановый сплав: Клинок меча",
  "tab": "copper_age"
}
```

### titanium_alloy_cast_sword_head

```json
{
  "name": "titanium_alloy_cast_sword_head",
  "kind": "tool_part",
  "display": "Titanium Alloy: Casting - Sword Blade",
  "display_ru": "Титановый сплав: Отливка — Клинок меча",
  "tab": "copper_age"
}
```

### titanium_alloy_katana_head

```json
{
  "name": "titanium_alloy_katana_head",
  "kind": "tool_part",
  "display": "Titanium Alloy: Katana Blade",
  "display_ru": "Титановый сплав: Клинок катаны",
  "tab": "copper_age"
}
```

### titanium_alloy_cast_katana_head

```json
{
  "name": "titanium_alloy_cast_katana_head",
  "kind": "tool_part",
  "display": "Titanium Alloy: Casting - Katana Blade",
  "display_ru": "Титановый сплав: Отливка — Клинок катаны",
  "tab": "copper_age"
}
```

### titanium_alloy_machete_head

```json
{
  "name": "titanium_alloy_machete_head",
  "kind": "tool_part",
  "display": "Titanium Alloy: Machete Blade",
  "display_ru": "Титановый сплав: Клинок мачете",
  "tab": "copper_age"
}
```

### titanium_alloy_cast_machete_head

```json
{
  "name": "titanium_alloy_cast_machete_head",
  "kind": "tool_part",
  "display": "Titanium Alloy: Casting - Machete Blade",
  "display_ru": "Титановый сплав: Отливка — Клинок мачете",
  "tab": "copper_age"
}
```

### titanium_alloy_sickle_head

```json
{
  "name": "titanium_alloy_sickle_head",
  "kind": "tool_part",
  "display": "Titanium Alloy: Sickle Blade",
  "display_ru": "Титановый сплав: Лезвие серпа",
  "tab": "copper_age"
}
```

### titanium_alloy_cast_sickle_head

```json
{
  "name": "titanium_alloy_cast_sickle_head",
  "kind": "tool_part",
  "display": "Titanium Alloy: Casting - Sickle Blade",
  "display_ru": "Титановый сплав: Отливка — Лезвие серпа",
  "tab": "copper_age"
}
```

### tool_steel_pickaxe_head

```json
{
  "name": "tool_steel_pickaxe_head",
  "kind": "tool_part",
  "display": "Tool Steel: Pickaxe Head",
  "display_ru": "Инструментальная сталь: Головка кирки",
  "tab": "copper_age"
}
```

### tool_steel_cast_pickaxe_head

```json
{
  "name": "tool_steel_cast_pickaxe_head",
  "kind": "tool_part",
  "display": "Tool Steel: Casting - Pickaxe Head",
  "display_ru": "Инструментальная сталь: Отливка — Головка кирки",
  "tab": "copper_age"
}
```

### tool_steel_axe_head

```json
{
  "name": "tool_steel_axe_head",
  "kind": "tool_part",
  "display": "Tool Steel: Axe Head",
  "display_ru": "Инструментальная сталь: Головка топора",
  "tab": "copper_age"
}
```

### tool_steel_cast_axe_head

```json
{
  "name": "tool_steel_cast_axe_head",
  "kind": "tool_part",
  "display": "Tool Steel: Casting - Axe Head",
  "display_ru": "Инструментальная сталь: Отливка — Головка топора",
  "tab": "copper_age"
}
```

### tool_steel_shovel_head

```json
{
  "name": "tool_steel_shovel_head",
  "kind": "tool_part",
  "display": "Tool Steel: Shovel Head",
  "display_ru": "Инструментальная сталь: Головка лопаты",
  "tab": "copper_age"
}
```

### tool_steel_cast_shovel_head

```json
{
  "name": "tool_steel_cast_shovel_head",
  "kind": "tool_part",
  "display": "Tool Steel: Casting - Shovel Head",
  "display_ru": "Инструментальная сталь: Отливка — Головка лопаты",
  "tab": "copper_age"
}
```

### tool_steel_hoe_head

```json
{
  "name": "tool_steel_hoe_head",
  "kind": "tool_part",
  "display": "Tool Steel: Hoe Head",
  "display_ru": "Инструментальная сталь: Головка мотыги",
  "tab": "copper_age"
}
```

### tool_steel_cast_hoe_head

```json
{
  "name": "tool_steel_cast_hoe_head",
  "kind": "tool_part",
  "display": "Tool Steel: Casting - Hoe Head",
  "display_ru": "Инструментальная сталь: Отливка — Головка мотыги",
  "tab": "copper_age"
}
```

### tool_steel_hammer_head

```json
{
  "name": "tool_steel_hammer_head",
  "kind": "tool_part",
  "display": "Tool Steel: Hammer Head",
  "display_ru": "Инструментальная сталь: Головка молота",
  "tab": "copper_age"
}
```

### tool_steel_cast_hammer_head

```json
{
  "name": "tool_steel_cast_hammer_head",
  "kind": "tool_part",
  "display": "Tool Steel: Casting - Hammer Head",
  "display_ru": "Инструментальная сталь: Отливка — Головка молота",
  "tab": "copper_age"
}
```

### tool_steel_standard_handle

```json
{
  "name": "tool_steel_standard_handle",
  "kind": "tool_part",
  "display": "Tool Steel: Rod",
  "display_ru": "Инструментальная сталь: Стержень",
  "tab": "copper_age"
}
```

### tool_steel_clamp

```json
{
  "name": "tool_steel_clamp",
  "kind": "tool_part",
  "display": "Tool Steel: Clamp",
  "display_ru": "Инструментальная сталь: Хомут",
  "tab": "copper_age"
}
```

### tool_steel_saw_head

```json
{
  "name": "tool_steel_saw_head",
  "kind": "tool_part",
  "display": "Tool Steel: Saw Blade",
  "display_ru": "Инструментальная сталь: Полотно пилы",
  "tab": "copper_age"
}
```

### tool_steel_cast_saw_head

```json
{
  "name": "tool_steel_cast_saw_head",
  "kind": "tool_part",
  "display": "Tool Steel: Casting - Saw Blade",
  "display_ru": "Инструментальная сталь: Отливка — Полотно пилы",
  "tab": "copper_age"
}
```

### tool_steel_sword_head

```json
{
  "name": "tool_steel_sword_head",
  "kind": "tool_part",
  "display": "Tool Steel: Sword Blade",
  "display_ru": "Инструментальная сталь: Клинок меча",
  "tab": "copper_age"
}
```

### tool_steel_cast_sword_head

```json
{
  "name": "tool_steel_cast_sword_head",
  "kind": "tool_part",
  "display": "Tool Steel: Casting - Sword Blade",
  "display_ru": "Инструментальная сталь: Отливка — Клинок меча",
  "tab": "copper_age"
}
```

### tool_steel_katana_head

```json
{
  "name": "tool_steel_katana_head",
  "kind": "tool_part",
  "display": "Tool Steel: Katana Blade",
  "display_ru": "Инструментальная сталь: Клинок катаны",
  "tab": "copper_age"
}
```

### tool_steel_cast_katana_head

```json
{
  "name": "tool_steel_cast_katana_head",
  "kind": "tool_part",
  "display": "Tool Steel: Casting - Katana Blade",
  "display_ru": "Инструментальная сталь: Отливка — Клинок катаны",
  "tab": "copper_age"
}
```

### tool_steel_machete_head

```json
{
  "name": "tool_steel_machete_head",
  "kind": "tool_part",
  "display": "Tool Steel: Machete Blade",
  "display_ru": "Инструментальная сталь: Клинок мачете",
  "tab": "copper_age"
}
```

### tool_steel_cast_machete_head

```json
{
  "name": "tool_steel_cast_machete_head",
  "kind": "tool_part",
  "display": "Tool Steel: Casting - Machete Blade",
  "display_ru": "Инструментальная сталь: Отливка — Клинок мачете",
  "tab": "copper_age"
}
```

### tool_steel_sickle_head

```json
{
  "name": "tool_steel_sickle_head",
  "kind": "tool_part",
  "display": "Tool Steel: Sickle Blade",
  "display_ru": "Инструментальная сталь: Лезвие серпа",
  "tab": "copper_age"
}
```

### tool_steel_cast_sickle_head

```json
{
  "name": "tool_steel_cast_sickle_head",
  "kind": "tool_part",
  "display": "Tool Steel: Casting - Sickle Blade",
  "display_ru": "Инструментальная сталь: Отливка — Лезвие серпа",
  "tab": "copper_age"
}
```

### tungsten_pickaxe_head

```json
{
  "name": "tungsten_pickaxe_head",
  "kind": "tool_part",
  "display": "Tungsten: Pickaxe Head",
  "display_ru": "Вольфрам: Головка кирки",
  "tab": "copper_age"
}
```

### tungsten_cast_pickaxe_head

```json
{
  "name": "tungsten_cast_pickaxe_head",
  "kind": "tool_part",
  "display": "Tungsten: Casting - Pickaxe Head",
  "display_ru": "Вольфрам: Отливка — Головка кирки",
  "tab": "copper_age"
}
```

### tungsten_axe_head

```json
{
  "name": "tungsten_axe_head",
  "kind": "tool_part",
  "display": "Tungsten: Axe Head",
  "display_ru": "Вольфрам: Головка топора",
  "tab": "copper_age"
}
```

### tungsten_cast_axe_head

```json
{
  "name": "tungsten_cast_axe_head",
  "kind": "tool_part",
  "display": "Tungsten: Casting - Axe Head",
  "display_ru": "Вольфрам: Отливка — Головка топора",
  "tab": "copper_age"
}
```

### tungsten_shovel_head

```json
{
  "name": "tungsten_shovel_head",
  "kind": "tool_part",
  "display": "Tungsten: Shovel Head",
  "display_ru": "Вольфрам: Головка лопаты",
  "tab": "copper_age"
}
```

### tungsten_cast_shovel_head

```json
{
  "name": "tungsten_cast_shovel_head",
  "kind": "tool_part",
  "display": "Tungsten: Casting - Shovel Head",
  "display_ru": "Вольфрам: Отливка — Головка лопаты",
  "tab": "copper_age"
}
```

### tungsten_hoe_head

```json
{
  "name": "tungsten_hoe_head",
  "kind": "tool_part",
  "display": "Tungsten: Hoe Head",
  "display_ru": "Вольфрам: Головка мотыги",
  "tab": "copper_age"
}
```

### tungsten_cast_hoe_head

```json
{
  "name": "tungsten_cast_hoe_head",
  "kind": "tool_part",
  "display": "Tungsten: Casting - Hoe Head",
  "display_ru": "Вольфрам: Отливка — Головка мотыги",
  "tab": "copper_age"
}
```

### tungsten_hammer_head

```json
{
  "name": "tungsten_hammer_head",
  "kind": "tool_part",
  "display": "Tungsten: Hammer Head",
  "display_ru": "Вольфрам: Головка молота",
  "tab": "copper_age"
}
```

### tungsten_cast_hammer_head

```json
{
  "name": "tungsten_cast_hammer_head",
  "kind": "tool_part",
  "display": "Tungsten: Casting - Hammer Head",
  "display_ru": "Вольфрам: Отливка — Головка молота",
  "tab": "copper_age"
}
```

### tungsten_standard_handle

```json
{
  "name": "tungsten_standard_handle",
  "kind": "tool_part",
  "display": "Tungsten: Rod",
  "display_ru": "Вольфрам: Стержень",
  "tab": "copper_age"
}
```

### tungsten_clamp

```json
{
  "name": "tungsten_clamp",
  "kind": "tool_part",
  "display": "Tungsten: Clamp",
  "display_ru": "Вольфрам: Хомут",
  "tab": "copper_age"
}
```

### tungsten_saw_head

```json
{
  "name": "tungsten_saw_head",
  "kind": "tool_part",
  "display": "Tungsten: Saw Blade",
  "display_ru": "Вольфрам: Полотно пилы",
  "tab": "copper_age"
}
```

### tungsten_cast_saw_head

```json
{
  "name": "tungsten_cast_saw_head",
  "kind": "tool_part",
  "display": "Tungsten: Casting - Saw Blade",
  "display_ru": "Вольфрам: Отливка — Полотно пилы",
  "tab": "copper_age"
}
```

### tungsten_sword_head

```json
{
  "name": "tungsten_sword_head",
  "kind": "tool_part",
  "display": "Tungsten: Sword Blade",
  "display_ru": "Вольфрам: Клинок меча",
  "tab": "copper_age"
}
```

### tungsten_cast_sword_head

```json
{
  "name": "tungsten_cast_sword_head",
  "kind": "tool_part",
  "display": "Tungsten: Casting - Sword Blade",
  "display_ru": "Вольфрам: Отливка — Клинок меча",
  "tab": "copper_age"
}
```

### tungsten_katana_head

```json
{
  "name": "tungsten_katana_head",
  "kind": "tool_part",
  "display": "Tungsten: Katana Blade",
  "display_ru": "Вольфрам: Клинок катаны",
  "tab": "copper_age"
}
```

### tungsten_cast_katana_head

```json
{
  "name": "tungsten_cast_katana_head",
  "kind": "tool_part",
  "display": "Tungsten: Casting - Katana Blade",
  "display_ru": "Вольфрам: Отливка — Клинок катаны",
  "tab": "copper_age"
}
```

### tungsten_machete_head

```json
{
  "name": "tungsten_machete_head",
  "kind": "tool_part",
  "display": "Tungsten: Machete Blade",
  "display_ru": "Вольфрам: Клинок мачете",
  "tab": "copper_age"
}
```

### tungsten_cast_machete_head

```json
{
  "name": "tungsten_cast_machete_head",
  "kind": "tool_part",
  "display": "Tungsten: Casting - Machete Blade",
  "display_ru": "Вольфрам: Отливка — Клинок мачете",
  "tab": "copper_age"
}
```

### tungsten_sickle_head

```json
{
  "name": "tungsten_sickle_head",
  "kind": "tool_part",
  "display": "Tungsten: Sickle Blade",
  "display_ru": "Вольфрам: Лезвие серпа",
  "tab": "copper_age"
}
```

### tungsten_cast_sickle_head

```json
{
  "name": "tungsten_cast_sickle_head",
  "kind": "tool_part",
  "display": "Tungsten: Casting - Sickle Blade",
  "display_ru": "Вольфрам: Отливка — Лезвие серпа",
  "tab": "copper_age"
}
```

### tungsten_steel_pickaxe_head

```json
{
  "name": "tungsten_steel_pickaxe_head",
  "kind": "tool_part",
  "display": "Tungsten Steel: Pickaxe Head",
  "display_ru": "Вольфрамовая сталь: Головка кирки",
  "tab": "copper_age"
}
```

### tungsten_steel_cast_pickaxe_head

```json
{
  "name": "tungsten_steel_cast_pickaxe_head",
  "kind": "tool_part",
  "display": "Tungsten Steel: Casting - Pickaxe Head",
  "display_ru": "Вольфрамовая сталь: Отливка — Головка кирки",
  "tab": "copper_age"
}
```

### tungsten_steel_axe_head

```json
{
  "name": "tungsten_steel_axe_head",
  "kind": "tool_part",
  "display": "Tungsten Steel: Axe Head",
  "display_ru": "Вольфрамовая сталь: Головка топора",
  "tab": "copper_age"
}
```

### tungsten_steel_cast_axe_head

```json
{
  "name": "tungsten_steel_cast_axe_head",
  "kind": "tool_part",
  "display": "Tungsten Steel: Casting - Axe Head",
  "display_ru": "Вольфрамовая сталь: Отливка — Головка топора",
  "tab": "copper_age"
}
```

### tungsten_steel_shovel_head

```json
{
  "name": "tungsten_steel_shovel_head",
  "kind": "tool_part",
  "display": "Tungsten Steel: Shovel Head",
  "display_ru": "Вольфрамовая сталь: Головка лопаты",
  "tab": "copper_age"
}
```

### tungsten_steel_cast_shovel_head

```json
{
  "name": "tungsten_steel_cast_shovel_head",
  "kind": "tool_part",
  "display": "Tungsten Steel: Casting - Shovel Head",
  "display_ru": "Вольфрамовая сталь: Отливка — Головка лопаты",
  "tab": "copper_age"
}
```

### tungsten_steel_hoe_head

```json
{
  "name": "tungsten_steel_hoe_head",
  "kind": "tool_part",
  "display": "Tungsten Steel: Hoe Head",
  "display_ru": "Вольфрамовая сталь: Головка мотыги",
  "tab": "copper_age"
}
```

### tungsten_steel_cast_hoe_head

```json
{
  "name": "tungsten_steel_cast_hoe_head",
  "kind": "tool_part",
  "display": "Tungsten Steel: Casting - Hoe Head",
  "display_ru": "Вольфрамовая сталь: Отливка — Головка мотыги",
  "tab": "copper_age"
}
```

### tungsten_steel_hammer_head

```json
{
  "name": "tungsten_steel_hammer_head",
  "kind": "tool_part",
  "display": "Tungsten Steel: Hammer Head",
  "display_ru": "Вольфрамовая сталь: Головка молота",
  "tab": "copper_age"
}
```

### tungsten_steel_cast_hammer_head

```json
{
  "name": "tungsten_steel_cast_hammer_head",
  "kind": "tool_part",
  "display": "Tungsten Steel: Casting - Hammer Head",
  "display_ru": "Вольфрамовая сталь: Отливка — Головка молота",
  "tab": "copper_age"
}
```

### tungsten_steel_standard_handle

```json
{
  "name": "tungsten_steel_standard_handle",
  "kind": "tool_part",
  "display": "Tungsten Steel: Rod",
  "display_ru": "Вольфрамовая сталь: Стержень",
  "tab": "copper_age"
}
```

### tungsten_steel_clamp

```json
{
  "name": "tungsten_steel_clamp",
  "kind": "tool_part",
  "display": "Tungsten Steel: Clamp",
  "display_ru": "Вольфрамовая сталь: Хомут",
  "tab": "copper_age"
}
```

### tungsten_steel_saw_head

```json
{
  "name": "tungsten_steel_saw_head",
  "kind": "tool_part",
  "display": "Tungsten Steel: Saw Blade",
  "display_ru": "Вольфрамовая сталь: Полотно пилы",
  "tab": "copper_age"
}
```

### tungsten_steel_cast_saw_head

```json
{
  "name": "tungsten_steel_cast_saw_head",
  "kind": "tool_part",
  "display": "Tungsten Steel: Casting - Saw Blade",
  "display_ru": "Вольфрамовая сталь: Отливка — Полотно пилы",
  "tab": "copper_age"
}
```

### tungsten_steel_sword_head

```json
{
  "name": "tungsten_steel_sword_head",
  "kind": "tool_part",
  "display": "Tungsten Steel: Sword Blade",
  "display_ru": "Вольфрамовая сталь: Клинок меча",
  "tab": "copper_age"
}
```

### tungsten_steel_cast_sword_head

```json
{
  "name": "tungsten_steel_cast_sword_head",
  "kind": "tool_part",
  "display": "Tungsten Steel: Casting - Sword Blade",
  "display_ru": "Вольфрамовая сталь: Отливка — Клинок меча",
  "tab": "copper_age"
}
```

### tungsten_steel_katana_head

```json
{
  "name": "tungsten_steel_katana_head",
  "kind": "tool_part",
  "display": "Tungsten Steel: Katana Blade",
  "display_ru": "Вольфрамовая сталь: Клинок катаны",
  "tab": "copper_age"
}
```

### tungsten_steel_cast_katana_head

```json
{
  "name": "tungsten_steel_cast_katana_head",
  "kind": "tool_part",
  "display": "Tungsten Steel: Casting - Katana Blade",
  "display_ru": "Вольфрамовая сталь: Отливка — Клинок катаны",
  "tab": "copper_age"
}
```

### tungsten_steel_machete_head

```json
{
  "name": "tungsten_steel_machete_head",
  "kind": "tool_part",
  "display": "Tungsten Steel: Machete Blade",
  "display_ru": "Вольфрамовая сталь: Клинок мачете",
  "tab": "copper_age"
}
```

### tungsten_steel_cast_machete_head

```json
{
  "name": "tungsten_steel_cast_machete_head",
  "kind": "tool_part",
  "display": "Tungsten Steel: Casting - Machete Blade",
  "display_ru": "Вольфрамовая сталь: Отливка — Клинок мачете",
  "tab": "copper_age"
}
```

### tungsten_steel_sickle_head

```json
{
  "name": "tungsten_steel_sickle_head",
  "kind": "tool_part",
  "display": "Tungsten Steel: Sickle Blade",
  "display_ru": "Вольфрамовая сталь: Лезвие серпа",
  "tab": "copper_age"
}
```

### tungsten_steel_cast_sickle_head

```json
{
  "name": "tungsten_steel_cast_sickle_head",
  "kind": "tool_part",
  "display": "Tungsten Steel: Casting - Sickle Blade",
  "display_ru": "Вольфрамовая сталь: Отливка — Лезвие серпа",
  "tab": "copper_age"
}
```

### uranium_pickaxe_head

```json
{
  "name": "uranium_pickaxe_head",
  "kind": "tool_part",
  "display": "Uranium: Pickaxe Head",
  "display_ru": "Уран: Головка кирки",
  "tab": "copper_age"
}
```

### uranium_cast_pickaxe_head

```json
{
  "name": "uranium_cast_pickaxe_head",
  "kind": "tool_part",
  "display": "Uranium: Casting - Pickaxe Head",
  "display_ru": "Уран: Отливка — Головка кирки",
  "tab": "copper_age"
}
```

### uranium_axe_head

```json
{
  "name": "uranium_axe_head",
  "kind": "tool_part",
  "display": "Uranium: Axe Head",
  "display_ru": "Уран: Головка топора",
  "tab": "copper_age"
}
```

### uranium_cast_axe_head

```json
{
  "name": "uranium_cast_axe_head",
  "kind": "tool_part",
  "display": "Uranium: Casting - Axe Head",
  "display_ru": "Уран: Отливка — Головка топора",
  "tab": "copper_age"
}
```

### uranium_shovel_head

```json
{
  "name": "uranium_shovel_head",
  "kind": "tool_part",
  "display": "Uranium: Shovel Head",
  "display_ru": "Уран: Головка лопаты",
  "tab": "copper_age"
}
```

### uranium_cast_shovel_head

```json
{
  "name": "uranium_cast_shovel_head",
  "kind": "tool_part",
  "display": "Uranium: Casting - Shovel Head",
  "display_ru": "Уран: Отливка — Головка лопаты",
  "tab": "copper_age"
}
```

### uranium_hoe_head

```json
{
  "name": "uranium_hoe_head",
  "kind": "tool_part",
  "display": "Uranium: Hoe Head",
  "display_ru": "Уран: Головка мотыги",
  "tab": "copper_age"
}
```

### uranium_cast_hoe_head

```json
{
  "name": "uranium_cast_hoe_head",
  "kind": "tool_part",
  "display": "Uranium: Casting - Hoe Head",
  "display_ru": "Уран: Отливка — Головка мотыги",
  "tab": "copper_age"
}
```

### uranium_hammer_head

```json
{
  "name": "uranium_hammer_head",
  "kind": "tool_part",
  "display": "Uranium: Hammer Head",
  "display_ru": "Уран: Головка молота",
  "tab": "copper_age"
}
```

### uranium_cast_hammer_head

```json
{
  "name": "uranium_cast_hammer_head",
  "kind": "tool_part",
  "display": "Uranium: Casting - Hammer Head",
  "display_ru": "Уран: Отливка — Головка молота",
  "tab": "copper_age"
}
```

### uranium_standard_handle

```json
{
  "name": "uranium_standard_handle",
  "kind": "tool_part",
  "display": "Uranium: Rod",
  "display_ru": "Уран: Стержень",
  "tab": "copper_age"
}
```

### uranium_clamp

```json
{
  "name": "uranium_clamp",
  "kind": "tool_part",
  "display": "Uranium: Clamp",
  "display_ru": "Уран: Хомут",
  "tab": "copper_age"
}
```

### uranium_saw_head

```json
{
  "name": "uranium_saw_head",
  "kind": "tool_part",
  "display": "Uranium: Saw Blade",
  "display_ru": "Уран: Полотно пилы",
  "tab": "copper_age"
}
```

### uranium_cast_saw_head

```json
{
  "name": "uranium_cast_saw_head",
  "kind": "tool_part",
  "display": "Uranium: Casting - Saw Blade",
  "display_ru": "Уран: Отливка — Полотно пилы",
  "tab": "copper_age"
}
```

### uranium_sword_head

```json
{
  "name": "uranium_sword_head",
  "kind": "tool_part",
  "display": "Uranium: Sword Blade",
  "display_ru": "Уран: Клинок меча",
  "tab": "copper_age"
}
```

### uranium_cast_sword_head

```json
{
  "name": "uranium_cast_sword_head",
  "kind": "tool_part",
  "display": "Uranium: Casting - Sword Blade",
  "display_ru": "Уран: Отливка — Клинок меча",
  "tab": "copper_age"
}
```

### uranium_katana_head

```json
{
  "name": "uranium_katana_head",
  "kind": "tool_part",
  "display": "Uranium: Katana Blade",
  "display_ru": "Уран: Клинок катаны",
  "tab": "copper_age"
}
```

### uranium_cast_katana_head

```json
{
  "name": "uranium_cast_katana_head",
  "kind": "tool_part",
  "display": "Uranium: Casting - Katana Blade",
  "display_ru": "Уран: Отливка — Клинок катаны",
  "tab": "copper_age"
}
```

### uranium_machete_head

```json
{
  "name": "uranium_machete_head",
  "kind": "tool_part",
  "display": "Uranium: Machete Blade",
  "display_ru": "Уран: Клинок мачете",
  "tab": "copper_age"
}
```

### uranium_cast_machete_head

```json
{
  "name": "uranium_cast_machete_head",
  "kind": "tool_part",
  "display": "Uranium: Casting - Machete Blade",
  "display_ru": "Уран: Отливка — Клинок мачете",
  "tab": "copper_age"
}
```

### uranium_sickle_head

```json
{
  "name": "uranium_sickle_head",
  "kind": "tool_part",
  "display": "Uranium: Sickle Blade",
  "display_ru": "Уран: Лезвие серпа",
  "tab": "copper_age"
}
```

### uranium_cast_sickle_head

```json
{
  "name": "uranium_cast_sickle_head",
  "kind": "tool_part",
  "display": "Uranium: Casting - Sickle Blade",
  "display_ru": "Уран: Отливка — Лезвие серпа",
  "tab": "copper_age"
}
```

### wood_pickaxe_head

```json
{
  "name": "wood_pickaxe_head",
  "kind": "tool_part",
  "display": "Wood: Pickaxe Head",
  "display_ru": "Древесина: Головка кирки",
  "tab": "copper_age"
}
```

### wood_axe_head

```json
{
  "name": "wood_axe_head",
  "kind": "tool_part",
  "display": "Wood: Axe Head",
  "display_ru": "Древесина: Головка топора",
  "tab": "copper_age"
}
```

### wood_shovel_head

```json
{
  "name": "wood_shovel_head",
  "kind": "tool_part",
  "display": "Wood: Shovel Head",
  "display_ru": "Древесина: Головка лопаты",
  "tab": "copper_age"
}
```

### wood_hoe_head

```json
{
  "name": "wood_hoe_head",
  "kind": "tool_part",
  "display": "Wood: Hoe Head",
  "display_ru": "Древесина: Головка мотыги",
  "tab": "copper_age"
}
```

### wood_hammer_head

```json
{
  "name": "wood_hammer_head",
  "kind": "tool_part",
  "display": "Wood: Hammer Head",
  "display_ru": "Древесина: Головка молота",
  "tab": "copper_age"
}
```

### wooden_handle

```json
{
  "name": "wooden_handle",
  "kind": "tool_part",
  "display": "Wood: Rod",
  "display_ru": "Древесина: Стержень",
  "tab": "copper_age"
}
```

### wood_saw_head

```json
{
  "name": "wood_saw_head",
  "kind": "tool_part",
  "display": "Wood: Saw Blade",
  "display_ru": "Древесина: Полотно пилы",
  "tab": "copper_age"
}
```

### wood_sword_head

```json
{
  "name": "wood_sword_head",
  "kind": "tool_part",
  "display": "Wood: Sword Blade",
  "display_ru": "Древесина: Клинок меча",
  "tab": "copper_age"
}
```

### wood_katana_head

```json
{
  "name": "wood_katana_head",
  "kind": "tool_part",
  "display": "Wood: Katana Blade",
  "display_ru": "Древесина: Клинок катаны",
  "tab": "copper_age"
}
```

### wood_machete_head

```json
{
  "name": "wood_machete_head",
  "kind": "tool_part",
  "display": "Wood: Machete Blade",
  "display_ru": "Древесина: Клинок мачете",
  "tab": "copper_age"
}
```

### wood_sickle_head

```json
{
  "name": "wood_sickle_head",
  "kind": "tool_part",
  "display": "Wood: Sickle Blade",
  "display_ru": "Древесина: Лезвие серпа",
  "tab": "copper_age"
}
```

### wool_wrap

```json
{
  "name": "wool_wrap",
  "kind": "tool_part",
  "display": "Wool: Grip Wrap",
  "display_ru": "Шерсть: Обмотка",
  "tab": "copper_age"
}
```

### wrought_iron_pickaxe_head

```json
{
  "name": "wrought_iron_pickaxe_head",
  "kind": "tool_part",
  "display": "Wrought Iron: Pickaxe Head",
  "display_ru": "Кричное железо: Головка кирки",
  "tab": "copper_age"
}
```

### wrought_iron_cast_pickaxe_head

```json
{
  "name": "wrought_iron_cast_pickaxe_head",
  "kind": "tool_part",
  "display": "Wrought Iron: Casting - Pickaxe Head",
  "display_ru": "Кричное железо: Отливка — Головка кирки",
  "tab": "copper_age"
}
```

### wrought_iron_axe_head

```json
{
  "name": "wrought_iron_axe_head",
  "kind": "tool_part",
  "display": "Wrought Iron: Axe Head",
  "display_ru": "Кричное железо: Головка топора",
  "tab": "copper_age"
}
```

### wrought_iron_cast_axe_head

```json
{
  "name": "wrought_iron_cast_axe_head",
  "kind": "tool_part",
  "display": "Wrought Iron: Casting - Axe Head",
  "display_ru": "Кричное железо: Отливка — Головка топора",
  "tab": "copper_age"
}
```

### wrought_iron_shovel_head

```json
{
  "name": "wrought_iron_shovel_head",
  "kind": "tool_part",
  "display": "Wrought Iron: Shovel Head",
  "display_ru": "Кричное железо: Головка лопаты",
  "tab": "copper_age"
}
```

### wrought_iron_cast_shovel_head

```json
{
  "name": "wrought_iron_cast_shovel_head",
  "kind": "tool_part",
  "display": "Wrought Iron: Casting - Shovel Head",
  "display_ru": "Кричное железо: Отливка — Головка лопаты",
  "tab": "copper_age"
}
```

### wrought_iron_hoe_head

```json
{
  "name": "wrought_iron_hoe_head",
  "kind": "tool_part",
  "display": "Wrought Iron: Hoe Head",
  "display_ru": "Кричное железо: Головка мотыги",
  "tab": "copper_age"
}
```

### wrought_iron_cast_hoe_head

```json
{
  "name": "wrought_iron_cast_hoe_head",
  "kind": "tool_part",
  "display": "Wrought Iron: Casting - Hoe Head",
  "display_ru": "Кричное железо: Отливка — Головка мотыги",
  "tab": "copper_age"
}
```

### wrought_iron_hammer_head

```json
{
  "name": "wrought_iron_hammer_head",
  "kind": "tool_part",
  "display": "Wrought Iron: Hammer Head",
  "display_ru": "Кричное железо: Головка молота",
  "tab": "copper_age"
}
```

### wrought_iron_cast_hammer_head

```json
{
  "name": "wrought_iron_cast_hammer_head",
  "kind": "tool_part",
  "display": "Wrought Iron: Casting - Hammer Head",
  "display_ru": "Кричное железо: Отливка — Головка молота",
  "tab": "copper_age"
}
```

### wrought_iron_standard_handle

```json
{
  "name": "wrought_iron_standard_handle",
  "kind": "tool_part",
  "display": "Wrought Iron: Rod",
  "display_ru": "Кричное железо: Стержень",
  "tab": "copper_age"
}
```

### wrought_iron_clamp

```json
{
  "name": "wrought_iron_clamp",
  "kind": "tool_part",
  "display": "Wrought Iron: Clamp",
  "display_ru": "Кричное железо: Хомут",
  "tab": "copper_age"
}
```

### wrought_iron_saw_head

```json
{
  "name": "wrought_iron_saw_head",
  "kind": "tool_part",
  "display": "Wrought Iron: Saw Blade",
  "display_ru": "Кричное железо: Полотно пилы",
  "tab": "copper_age"
}
```

### wrought_iron_cast_saw_head

```json
{
  "name": "wrought_iron_cast_saw_head",
  "kind": "tool_part",
  "display": "Wrought Iron: Casting - Saw Blade",
  "display_ru": "Кричное железо: Отливка — Полотно пилы",
  "tab": "copper_age"
}
```

### wrought_iron_sword_head

```json
{
  "name": "wrought_iron_sword_head",
  "kind": "tool_part",
  "display": "Wrought Iron: Sword Blade",
  "display_ru": "Кричное железо: Клинок меча",
  "tab": "copper_age"
}
```

### wrought_iron_cast_sword_head

```json
{
  "name": "wrought_iron_cast_sword_head",
  "kind": "tool_part",
  "display": "Wrought Iron: Casting - Sword Blade",
  "display_ru": "Кричное железо: Отливка — Клинок меча",
  "tab": "copper_age"
}
```

### wrought_iron_katana_head

```json
{
  "name": "wrought_iron_katana_head",
  "kind": "tool_part",
  "display": "Wrought Iron: Katana Blade",
  "display_ru": "Кричное железо: Клинок катаны",
  "tab": "copper_age"
}
```

### wrought_iron_cast_katana_head

```json
{
  "name": "wrought_iron_cast_katana_head",
  "kind": "tool_part",
  "display": "Wrought Iron: Casting - Katana Blade",
  "display_ru": "Кричное железо: Отливка — Клинок катаны",
  "tab": "copper_age"
}
```

### wrought_iron_machete_head

```json
{
  "name": "wrought_iron_machete_head",
  "kind": "tool_part",
  "display": "Wrought Iron: Machete Blade",
  "display_ru": "Кричное железо: Клинок мачете",
  "tab": "copper_age"
}
```

### wrought_iron_cast_machete_head

```json
{
  "name": "wrought_iron_cast_machete_head",
  "kind": "tool_part",
  "display": "Wrought Iron: Casting - Machete Blade",
  "display_ru": "Кричное железо: Отливка — Клинок мачете",
  "tab": "copper_age"
}
```

### wrought_iron_sickle_head

```json
{
  "name": "wrought_iron_sickle_head",
  "kind": "tool_part",
  "display": "Wrought Iron: Sickle Blade",
  "display_ru": "Кричное железо: Лезвие серпа",
  "tab": "copper_age"
}
```

### wrought_iron_cast_sickle_head

```json
{
  "name": "wrought_iron_cast_sickle_head",
  "kind": "tool_part",
  "display": "Wrought Iron: Casting - Sickle Blade",
  "display_ru": "Кричное железо: Отливка — Лезвие серпа",
  "tab": "copper_age"
}
```

### zinc_pickaxe_head

```json
{
  "name": "zinc_pickaxe_head",
  "kind": "tool_part",
  "display": "Zinc: Pickaxe Head",
  "display_ru": "Цинк: Головка кирки",
  "tab": "copper_age"
}
```

### zinc_cast_pickaxe_head

```json
{
  "name": "zinc_cast_pickaxe_head",
  "kind": "tool_part",
  "display": "Zinc: Casting - Pickaxe Head",
  "display_ru": "Цинк: Отливка — Головка кирки",
  "tab": "copper_age"
}
```

### zinc_axe_head

```json
{
  "name": "zinc_axe_head",
  "kind": "tool_part",
  "display": "Zinc: Axe Head",
  "display_ru": "Цинк: Головка топора",
  "tab": "copper_age"
}
```

### zinc_cast_axe_head

```json
{
  "name": "zinc_cast_axe_head",
  "kind": "tool_part",
  "display": "Zinc: Casting - Axe Head",
  "display_ru": "Цинк: Отливка — Головка топора",
  "tab": "copper_age"
}
```

### zinc_shovel_head

```json
{
  "name": "zinc_shovel_head",
  "kind": "tool_part",
  "display": "Zinc: Shovel Head",
  "display_ru": "Цинк: Головка лопаты",
  "tab": "copper_age"
}
```

### zinc_cast_shovel_head

```json
{
  "name": "zinc_cast_shovel_head",
  "kind": "tool_part",
  "display": "Zinc: Casting - Shovel Head",
  "display_ru": "Цинк: Отливка — Головка лопаты",
  "tab": "copper_age"
}
```

### zinc_hoe_head

```json
{
  "name": "zinc_hoe_head",
  "kind": "tool_part",
  "display": "Zinc: Hoe Head",
  "display_ru": "Цинк: Головка мотыги",
  "tab": "copper_age"
}
```

### zinc_cast_hoe_head

```json
{
  "name": "zinc_cast_hoe_head",
  "kind": "tool_part",
  "display": "Zinc: Casting - Hoe Head",
  "display_ru": "Цинк: Отливка — Головка мотыги",
  "tab": "copper_age"
}
```

### zinc_hammer_head

```json
{
  "name": "zinc_hammer_head",
  "kind": "tool_part",
  "display": "Zinc: Hammer Head",
  "display_ru": "Цинк: Головка молота",
  "tab": "copper_age"
}
```

### zinc_cast_hammer_head

```json
{
  "name": "zinc_cast_hammer_head",
  "kind": "tool_part",
  "display": "Zinc: Casting - Hammer Head",
  "display_ru": "Цинк: Отливка — Головка молота",
  "tab": "copper_age"
}
```

### zinc_standard_handle

```json
{
  "name": "zinc_standard_handle",
  "kind": "tool_part",
  "display": "Zinc: Rod",
  "display_ru": "Цинк: Стержень",
  "tab": "copper_age"
}
```

### zinc_clamp

```json
{
  "name": "zinc_clamp",
  "kind": "tool_part",
  "display": "Zinc: Clamp",
  "display_ru": "Цинк: Хомут",
  "tab": "copper_age"
}
```

### zinc_saw_head

```json
{
  "name": "zinc_saw_head",
  "kind": "tool_part",
  "display": "Zinc: Saw Blade",
  "display_ru": "Цинк: Полотно пилы",
  "tab": "copper_age"
}
```

### zinc_cast_saw_head

```json
{
  "name": "zinc_cast_saw_head",
  "kind": "tool_part",
  "display": "Zinc: Casting - Saw Blade",
  "display_ru": "Цинк: Отливка — Полотно пилы",
  "tab": "copper_age"
}
```

### zinc_sword_head

```json
{
  "name": "zinc_sword_head",
  "kind": "tool_part",
  "display": "Zinc: Sword Blade",
  "display_ru": "Цинк: Клинок меча",
  "tab": "copper_age"
}
```

### zinc_cast_sword_head

```json
{
  "name": "zinc_cast_sword_head",
  "kind": "tool_part",
  "display": "Zinc: Casting - Sword Blade",
  "display_ru": "Цинк: Отливка — Клинок меча",
  "tab": "copper_age"
}
```

### zinc_katana_head

```json
{
  "name": "zinc_katana_head",
  "kind": "tool_part",
  "display": "Zinc: Katana Blade",
  "display_ru": "Цинк: Клинок катаны",
  "tab": "copper_age"
}
```

### zinc_cast_katana_head

```json
{
  "name": "zinc_cast_katana_head",
  "kind": "tool_part",
  "display": "Zinc: Casting - Katana Blade",
  "display_ru": "Цинк: Отливка — Клинок катаны",
  "tab": "copper_age"
}
```

### zinc_machete_head

```json
{
  "name": "zinc_machete_head",
  "kind": "tool_part",
  "display": "Zinc: Machete Blade",
  "display_ru": "Цинк: Клинок мачете",
  "tab": "copper_age"
}
```

### zinc_cast_machete_head

```json
{
  "name": "zinc_cast_machete_head",
  "kind": "tool_part",
  "display": "Zinc: Casting - Machete Blade",
  "display_ru": "Цинк: Отливка — Клинок мачете",
  "tab": "copper_age"
}
```

### zinc_sickle_head

```json
{
  "name": "zinc_sickle_head",
  "kind": "tool_part",
  "display": "Zinc: Sickle Blade",
  "display_ru": "Цинк: Лезвие серпа",
  "tab": "copper_age"
}
```

### zinc_cast_sickle_head

```json
{
  "name": "zinc_cast_sickle_head",
  "kind": "tool_part",
  "display": "Zinc: Casting - Sickle Blade",
  "display_ru": "Цинк: Отливка — Лезвие серпа",
  "tab": "copper_age"
}
```

### pickaxe_mold

```json
{
  "name": "pickaxe_mold",
  "kind": "tool_mold",
  "form": "pickaxe_head",
  "raw": false,
  "display": "Mold: Pickaxe Head",
  "display_ru": "Форма: Головка кирки",
  "tab": "copper_age"
}
```

### unfired_pickaxe_mold

```json
{
  "name": "unfired_pickaxe_mold",
  "kind": "tool_mold",
  "form": "pickaxe_head",
  "raw": true,
  "display": "Unfired mold: Pickaxe Head",
  "display_ru": "Сырая форма: Головка кирки",
  "tab": "copper_age"
}
```

### axe_mold

```json
{
  "name": "axe_mold",
  "kind": "tool_mold",
  "form": "axe_head",
  "raw": false,
  "display": "Mold: Axe Head",
  "display_ru": "Форма: Головка топора",
  "tab": "copper_age"
}
```

### unfired_axe_mold

```json
{
  "name": "unfired_axe_mold",
  "kind": "tool_mold",
  "form": "axe_head",
  "raw": true,
  "display": "Unfired mold: Axe Head",
  "display_ru": "Сырая форма: Головка топора",
  "tab": "copper_age"
}
```

### shovel_mold

```json
{
  "name": "shovel_mold",
  "kind": "tool_mold",
  "form": "shovel_head",
  "raw": false,
  "display": "Mold: Shovel Head",
  "display_ru": "Форма: Головка лопаты",
  "tab": "copper_age"
}
```

### unfired_shovel_mold

```json
{
  "name": "unfired_shovel_mold",
  "kind": "tool_mold",
  "form": "shovel_head",
  "raw": true,
  "display": "Unfired mold: Shovel Head",
  "display_ru": "Сырая форма: Головка лопаты",
  "tab": "copper_age"
}
```

### hoe_mold

```json
{
  "name": "hoe_mold",
  "kind": "tool_mold",
  "form": "hoe_head",
  "raw": false,
  "display": "Mold: Hoe Head",
  "display_ru": "Форма: Головка мотыги",
  "tab": "copper_age"
}
```

### unfired_hoe_mold

```json
{
  "name": "unfired_hoe_mold",
  "kind": "tool_mold",
  "form": "hoe_head",
  "raw": true,
  "display": "Unfired mold: Hoe Head",
  "display_ru": "Сырая форма: Головка мотыги",
  "tab": "copper_age"
}
```

### hammer_mold

```json
{
  "name": "hammer_mold",
  "kind": "tool_mold",
  "form": "hammer_head",
  "raw": false,
  "display": "Mold: Hammer Head",
  "display_ru": "Форма: Головка молота",
  "tab": "copper_age"
}
```

### unfired_hammer_mold

```json
{
  "name": "unfired_hammer_mold",
  "kind": "tool_mold",
  "form": "hammer_head",
  "raw": true,
  "display": "Unfired mold: Hammer Head",
  "display_ru": "Сырая форма: Головка молота",
  "tab": "copper_age"
}
```

### saw_mold

```json
{
  "name": "saw_mold",
  "kind": "tool_mold",
  "form": "saw_head",
  "raw": false,
  "display": "Mold: Saw Blade",
  "display_ru": "Форма: Полотно пилы",
  "tab": "copper_age"
}
```

### unfired_saw_mold

```json
{
  "name": "unfired_saw_mold",
  "kind": "tool_mold",
  "form": "saw_head",
  "raw": true,
  "display": "Unfired mold: Saw Blade",
  "display_ru": "Сырая форма: Полотно пилы",
  "tab": "copper_age"
}
```

### sword_mold

```json
{
  "name": "sword_mold",
  "kind": "tool_mold",
  "form": "sword_head",
  "raw": false,
  "display": "Mold: Sword Blade",
  "display_ru": "Форма: Клинок меча",
  "tab": "copper_age"
}
```

### unfired_sword_mold

```json
{
  "name": "unfired_sword_mold",
  "kind": "tool_mold",
  "form": "sword_head",
  "raw": true,
  "display": "Unfired mold: Sword Blade",
  "display_ru": "Сырая форма: Клинок меча",
  "tab": "copper_age"
}
```

### katana_mold

```json
{
  "name": "katana_mold",
  "kind": "tool_mold",
  "form": "katana_head",
  "raw": false,
  "display": "Mold: Katana Blade",
  "display_ru": "Форма: Клинок катаны",
  "tab": "copper_age"
}
```

### unfired_katana_mold

```json
{
  "name": "unfired_katana_mold",
  "kind": "tool_mold",
  "form": "katana_head",
  "raw": true,
  "display": "Unfired mold: Katana Blade",
  "display_ru": "Сырая форма: Клинок катаны",
  "tab": "copper_age"
}
```

### machete_mold

```json
{
  "name": "machete_mold",
  "kind": "tool_mold",
  "form": "machete_head",
  "raw": false,
  "display": "Mold: Machete Blade",
  "display_ru": "Форма: Клинок мачете",
  "tab": "copper_age"
}
```

### unfired_machete_mold

```json
{
  "name": "unfired_machete_mold",
  "kind": "tool_mold",
  "form": "machete_head",
  "raw": true,
  "display": "Unfired mold: Machete Blade",
  "display_ru": "Сырая форма: Клинок мачете",
  "tab": "copper_age"
}
```

### sickle_mold

```json
{
  "name": "sickle_mold",
  "kind": "tool_mold",
  "form": "sickle_head",
  "raw": false,
  "display": "Mold: Sickle Blade",
  "display_ru": "Форма: Лезвие серпа",
  "tab": "copper_age"
}
```

### unfired_sickle_mold

```json
{
  "name": "unfired_sickle_mold",
  "kind": "tool_mold",
  "form": "sickle_head",
  "raw": true,
  "display": "Unfired mold: Sickle Blade",
  "display_ru": "Сырая форма: Лезвие серпа",
  "tab": "copper_age"
}
```

### sand_ingot_mold

```json
{
  "name": "sand_ingot_mold",
  "kind": "mold_variant",
  "base_mold": "ingot_mold",
  "style": "sand",
  "display": "Sand mold: Ingot",
  "display_ru": "Песчаная форма: слитка",
  "tab": "copper_age"
}
```

### metal_ingot_mold

```json
{
  "name": "metal_ingot_mold",
  "kind": "mold_variant",
  "base_mold": "ingot_mold",
  "style": "metal",
  "display": "Metal cast: Ingot",
  "display_ru": "Металлическая форма: слитка",
  "tab": "copper_age"
}
```

### sand_plate_mold

```json
{
  "name": "sand_plate_mold",
  "kind": "mold_variant",
  "base_mold": "plate_mold",
  "style": "sand",
  "display": "Sand mold: Plate",
  "display_ru": "Песчаная форма: пластины",
  "tab": "copper_age"
}
```

### metal_plate_mold

```json
{
  "name": "metal_plate_mold",
  "kind": "mold_variant",
  "base_mold": "plate_mold",
  "style": "metal",
  "display": "Metal cast: Plate",
  "display_ru": "Металлическая форма: пластины",
  "tab": "copper_age"
}
```

### sand_fitting_mold

```json
{
  "name": "sand_fitting_mold",
  "kind": "mold_variant",
  "base_mold": "fitting_mold",
  "style": "sand",
  "display": "Sand mold: Fitting",
  "display_ru": "Песчаная форма: фитинга",
  "tab": "copper_age"
}
```

### metal_fitting_mold

```json
{
  "name": "metal_fitting_mold",
  "kind": "mold_variant",
  "base_mold": "fitting_mold",
  "style": "metal",
  "display": "Metal cast: Fitting",
  "display_ru": "Металлическая форма: фитинга",
  "tab": "copper_age"
}
```

### sand_pipe_mold

```json
{
  "name": "sand_pipe_mold",
  "kind": "mold_variant",
  "base_mold": "pipe_mold",
  "style": "sand",
  "display": "Sand mold: Pipe",
  "display_ru": "Песчаная форма: трубы",
  "tab": "copper_age"
}
```

### metal_pipe_mold

```json
{
  "name": "metal_pipe_mold",
  "kind": "mold_variant",
  "base_mold": "pipe_mold",
  "style": "metal",
  "display": "Metal cast: Pipe",
  "display_ru": "Металлическая форма: трубы",
  "tab": "copper_age"
}
```

### sand_pickaxe_mold

```json
{
  "name": "sand_pickaxe_mold",
  "kind": "mold_variant",
  "base_mold": "pickaxe_mold",
  "style": "sand",
  "display": "Sand mold: Pickaxe Head",
  "display_ru": "Песчаная форма: Головка кирки",
  "tab": "copper_age"
}
```

### metal_pickaxe_mold

```json
{
  "name": "metal_pickaxe_mold",
  "kind": "mold_variant",
  "base_mold": "pickaxe_mold",
  "style": "metal",
  "display": "Metal cast: Pickaxe Head",
  "display_ru": "Металлическая форма: Головка кирки",
  "tab": "copper_age"
}
```

### sand_axe_mold

```json
{
  "name": "sand_axe_mold",
  "kind": "mold_variant",
  "base_mold": "axe_mold",
  "style": "sand",
  "display": "Sand mold: Axe Head",
  "display_ru": "Песчаная форма: Головка топора",
  "tab": "copper_age"
}
```

### metal_axe_mold

```json
{
  "name": "metal_axe_mold",
  "kind": "mold_variant",
  "base_mold": "axe_mold",
  "style": "metal",
  "display": "Metal cast: Axe Head",
  "display_ru": "Металлическая форма: Головка топора",
  "tab": "copper_age"
}
```

### sand_shovel_mold

```json
{
  "name": "sand_shovel_mold",
  "kind": "mold_variant",
  "base_mold": "shovel_mold",
  "style": "sand",
  "display": "Sand mold: Shovel Head",
  "display_ru": "Песчаная форма: Головка лопаты",
  "tab": "copper_age"
}
```

### metal_shovel_mold

```json
{
  "name": "metal_shovel_mold",
  "kind": "mold_variant",
  "base_mold": "shovel_mold",
  "style": "metal",
  "display": "Metal cast: Shovel Head",
  "display_ru": "Металлическая форма: Головка лопаты",
  "tab": "copper_age"
}
```

### sand_hoe_mold

```json
{
  "name": "sand_hoe_mold",
  "kind": "mold_variant",
  "base_mold": "hoe_mold",
  "style": "sand",
  "display": "Sand mold: Hoe Head",
  "display_ru": "Песчаная форма: Головка мотыги",
  "tab": "copper_age"
}
```

### metal_hoe_mold

```json
{
  "name": "metal_hoe_mold",
  "kind": "mold_variant",
  "base_mold": "hoe_mold",
  "style": "metal",
  "display": "Metal cast: Hoe Head",
  "display_ru": "Металлическая форма: Головка мотыги",
  "tab": "copper_age"
}
```

### sand_hammer_mold

```json
{
  "name": "sand_hammer_mold",
  "kind": "mold_variant",
  "base_mold": "hammer_mold",
  "style": "sand",
  "display": "Sand mold: Hammer Head",
  "display_ru": "Песчаная форма: Головка молота",
  "tab": "copper_age"
}
```

### metal_hammer_mold

```json
{
  "name": "metal_hammer_mold",
  "kind": "mold_variant",
  "base_mold": "hammer_mold",
  "style": "metal",
  "display": "Metal cast: Hammer Head",
  "display_ru": "Металлическая форма: Головка молота",
  "tab": "copper_age"
}
```

### sand_saw_mold

```json
{
  "name": "sand_saw_mold",
  "kind": "mold_variant",
  "base_mold": "saw_mold",
  "style": "sand",
  "display": "Sand mold: Saw Blade",
  "display_ru": "Песчаная форма: Полотно пилы",
  "tab": "copper_age"
}
```

### metal_saw_mold

```json
{
  "name": "metal_saw_mold",
  "kind": "mold_variant",
  "base_mold": "saw_mold",
  "style": "metal",
  "display": "Metal cast: Saw Blade",
  "display_ru": "Металлическая форма: Полотно пилы",
  "tab": "copper_age"
}
```

### sand_sword_mold

```json
{
  "name": "sand_sword_mold",
  "kind": "mold_variant",
  "base_mold": "sword_mold",
  "style": "sand",
  "display": "Sand mold: Sword Blade",
  "display_ru": "Песчаная форма: Клинок меча",
  "tab": "copper_age"
}
```

### metal_sword_mold

```json
{
  "name": "metal_sword_mold",
  "kind": "mold_variant",
  "base_mold": "sword_mold",
  "style": "metal",
  "display": "Metal cast: Sword Blade",
  "display_ru": "Металлическая форма: Клинок меча",
  "tab": "copper_age"
}
```

### sand_katana_mold

```json
{
  "name": "sand_katana_mold",
  "kind": "mold_variant",
  "base_mold": "katana_mold",
  "style": "sand",
  "display": "Sand mold: Katana Blade",
  "display_ru": "Песчаная форма: Клинок катаны",
  "tab": "copper_age"
}
```

### metal_katana_mold

```json
{
  "name": "metal_katana_mold",
  "kind": "mold_variant",
  "base_mold": "katana_mold",
  "style": "metal",
  "display": "Metal cast: Katana Blade",
  "display_ru": "Металлическая форма: Клинок катаны",
  "tab": "copper_age"
}
```

### sand_machete_mold

```json
{
  "name": "sand_machete_mold",
  "kind": "mold_variant",
  "base_mold": "machete_mold",
  "style": "sand",
  "display": "Sand mold: Machete Blade",
  "display_ru": "Песчаная форма: Клинок мачете",
  "tab": "copper_age"
}
```

### metal_machete_mold

```json
{
  "name": "metal_machete_mold",
  "kind": "mold_variant",
  "base_mold": "machete_mold",
  "style": "metal",
  "display": "Metal cast: Machete Blade",
  "display_ru": "Металлическая форма: Клинок мачете",
  "tab": "copper_age"
}
```

### sand_sickle_mold

```json
{
  "name": "sand_sickle_mold",
  "kind": "mold_variant",
  "base_mold": "sickle_mold",
  "style": "sand",
  "display": "Sand mold: Sickle Blade",
  "display_ru": "Песчаная форма: Лезвие серпа",
  "tab": "copper_age"
}
```

### metal_sickle_mold

```json
{
  "name": "metal_sickle_mold",
  "kind": "mold_variant",
  "base_mold": "sickle_mold",
  "style": "metal",
  "display": "Metal cast: Sickle Blade",
  "display_ru": "Металлическая форма: Лезвие серпа",
  "tab": "copper_age"
}
```

### modular_pickaxe

```json
{
  "name": "modular_pickaxe",
  "kind": "modular_tool",
  "template": "pickaxe",
  "display": "Assembled Pickaxe",
  "display_ru": "Составной инструмент: Кирка",
  "tags": [
    "minecraft:enchantable/durability",
    "minecraft:enchantable/mining",
    "minecraft:enchantable/mining_loot",
    "minecraft:pickaxes"
  ],
  "tab": "copper_age"
}
```

### modular_axe

```json
{
  "name": "modular_axe",
  "kind": "modular_tool",
  "template": "axe",
  "display": "Assembled Axe",
  "display_ru": "Составной инструмент: Топор",
  "tags": [
    "minecraft:enchantable/durability",
    "minecraft:enchantable/mining",
    "minecraft:enchantable/mining_loot",
    "minecraft:axes"
  ],
  "tab": "copper_age"
}
```

### modular_saw

```json
{
  "name": "modular_saw",
  "kind": "modular_tool",
  "template": "saw",
  "display": "Assembled Saw",
  "display_ru": "Составной инструмент: Пила",
  "tags": [
    "minecraft:enchantable/durability",
    "minecraft:enchantable/mining",
    "minecraft:enchantable/mining_loot",
    "minecraft:axes"
  ],
  "tab": "copper_age"
}
```

### modular_shovel

```json
{
  "name": "modular_shovel",
  "kind": "modular_tool",
  "template": "shovel",
  "display": "Assembled Shovel",
  "display_ru": "Составной инструмент: Лопата",
  "tags": [
    "minecraft:enchantable/durability",
    "minecraft:enchantable/mining",
    "minecraft:enchantable/mining_loot",
    "minecraft:shovels"
  ],
  "tab": "copper_age"
}
```

### modular_hammer

```json
{
  "name": "modular_hammer",
  "kind": "modular_tool",
  "template": "hammer",
  "display": "Assembled Hammer",
  "display_ru": "Составной инструмент: Молот",
  "tags": [
    "minecraft:enchantable/durability",
    "minecraft:enchantable/mining",
    "minecraft:enchantable/mining_loot",
    "minecraft:pickaxes"
  ],
  "tab": "copper_age"
}
```

### modular_hoe

```json
{
  "name": "modular_hoe",
  "kind": "modular_tool",
  "template": "hoe",
  "display": "Assembled Hoe",
  "display_ru": "Составной инструмент: Мотыга",
  "tags": [
    "minecraft:enchantable/durability",
    "minecraft:enchantable/mining",
    "minecraft:enchantable/mining_loot",
    "minecraft:hoes"
  ],
  "tab": "copper_age"
}
```

### modular_sword

```json
{
  "name": "modular_sword",
  "kind": "modular_tool",
  "template": "sword",
  "display": "Assembled Sword",
  "display_ru": "Составной инструмент: Меч",
  "tags": [
    "minecraft:enchantable/durability",
    "minecraft:swords"
  ],
  "tab": "copper_age"
}
```

### modular_katana

```json
{
  "name": "modular_katana",
  "kind": "modular_tool",
  "template": "katana",
  "display": "Assembled Katana",
  "display_ru": "Составной инструмент: Катана",
  "tags": [
    "minecraft:enchantable/durability",
    "minecraft:swords"
  ],
  "tab": "copper_age"
}
```

### modular_machete

```json
{
  "name": "modular_machete",
  "kind": "modular_tool",
  "template": "machete",
  "display": "Assembled Machete",
  "display_ru": "Составной инструмент: Мачете",
  "tags": [
    "minecraft:enchantable/durability",
    "minecraft:swords",
    "minecraft:enchantable/mining",
    "minecraft:enchantable/mining_loot",
    "minecraft:axes"
  ],
  "tab": "copper_age"
}
```

### modular_sickle

```json
{
  "name": "modular_sickle",
  "kind": "modular_tool",
  "template": "sickle",
  "display": "Assembled Sickle",
  "display_ru": "Составной инструмент: Серп",
  "tags": [
    "minecraft:enchantable/durability",
    "minecraft:enchantable/mining",
    "minecraft:enchantable/mining_loot",
    "minecraft:hoes"
  ],
  "tab": "copper_age"
}
```


## Семейства сплавов

### steel

```json
{
  "name": "steel",
  "material": "Steel",
  "material_ru": "стали",
  "color": "66717B",
  "ingredients": [
    "minecraft:iron_ingot",
    "minecraft:coal"
  ],
  "mining_level": "iron",
  "composition": {
    "iron": 99,
    "carbon": 1
  },
  "process": "future_controlled_metallurgy"
}
```

### bronze

```json
{
  "name": "bronze",
  "material": "Bronze",
  "material_ru": "бронзы",
  "color": "B8874D",
  "mining_level": "stone"
}
```

### brass

```json
{
  "name": "brass",
  "material": "Brass",
  "material_ru": "латуни",
  "color": "CDA947",
  "mining_level": "stone"
}
```

### cast_iron

```json
{
  "name": "cast_iron",
  "material": "Cast Iron",
  "material_ru": "чугуна",
  "color": "665D59",
  "mining_level": "iron",
  "planned": true,
  "composition": {
    "iron": 96,
    "carbon": 4
  },
  "process": "future_controlled_metallurgy"
}
```

### wrought_iron

```json
{
  "name": "wrought_iron",
  "material": "Wrought Iron",
  "material_ru": "кричного железа",
  "color": "A4A19A",
  "mining_level": "iron",
  "planned": true,
  "composition": {
    "iron": 100
  },
  "process": "future_controlled_metallurgy"
}
```

### stainless_steel

```json
{
  "name": "stainless_steel",
  "material": "Stainless Steel",
  "material_ru": "нержавеющей стали",
  "color": "B5BFC1",
  "mining_level": "iron",
  "planned": true
}
```

### tool_steel

```json
{
  "name": "tool_steel",
  "material": "Tool Steel",
  "material_ru": "инструментальной стали",
  "color": "69747D",
  "mining_level": "iron",
  "planned": true,
  "composition": {
    "iron": 98,
    "carbon": 1,
    "tungsten": 1
  },
  "process": "future_controlled_metallurgy"
}
```

### duralumin

```json
{
  "name": "duralumin",
  "material": "Duralumin",
  "material_ru": "дюралюминия",
  "color": "B4C6CF",
  "mining_level": "iron",
  "planned": true,
  "composition": {
    "aluminum": 94,
    "copper": 4,
    "magnesium": 1,
    "manganese": 1
  },
  "process": "future_controlled_metallurgy"
}
```

### cupronickel

```json
{
  "name": "cupronickel",
  "material": "Cupronickel",
  "material_ru": "мельхиора",
  "color": "B2B8AB",
  "mining_level": "iron",
  "planned": true
}
```

### constantan

```json
{
  "name": "constantan",
  "material": "Constantan",
  "material_ru": "константана",
  "color": "B18C77",
  "mining_level": "iron",
  "planned": true
}
```

### invar

```json
{
  "name": "invar",
  "material": "Invar",
  "material_ru": "инвара",
  "color": "9AABA4",
  "mining_level": "iron",
  "planned": true
}
```

### electrum

```json
{
  "name": "electrum",
  "material": "Electrum",
  "material_ru": "электрума",
  "color": "E6D58E",
  "mining_level": "iron",
  "planned": true
}
```

### nichrome

```json
{
  "name": "nichrome",
  "material": "Nichrome",
  "material_ru": "нихрома",
  "color": "B9AD9F",
  "mining_level": "iron",
  "planned": true
}
```

### solder

```json
{
  "name": "solder",
  "material": "Tin-Lead Solder",
  "material_ru": "оловянно-свинцового припоя",
  "color": "B7BAC0",
  "mining_level": "iron",
  "planned": true
}
```

### tungsten_steel

```json
{
  "name": "tungsten_steel",
  "material": "Tungsten Steel",
  "material_ru": "вольфрамовой стали",
  "color": "535E6C",
  "mining_level": "iron",
  "planned": true,
  "composition": {
    "iron": 89,
    "tungsten": 10,
    "carbon": 1
  },
  "process": "future_controlled_metallurgy"
}
```

### titanium_alloy

```json
{
  "name": "titanium_alloy",
  "material": "Titanium Alloy",
  "material_ru": "титанового сплава",
  "color": "A9B9CB",
  "mining_level": "iron",
  "planned": true,
  "composition": {
    "titanium": 90,
    "aluminum": 6,
    "molybdenum": 4
  },
  "process": "future_controlled_metallurgy"
}
```

### nickel_superalloy

```json
{
  "name": "nickel_superalloy",
  "material": "Nickel Superalloy",
  "material_ru": "никелевого жаропрочного сплава",
  "color": "B4A385",
  "mining_level": "iron",
  "planned": true,
  "composition": {
    "nickel": 60,
    "chromium": 20,
    "iron": 15,
    "molybdenum": 5
  },
  "process": "future_controlled_metallurgy"
}
```

### aluminum_bronze

```json
{
  "name": "aluminum_bronze",
  "material": "Aluminum Bronze",
  "material_ru": "алюминиевой бронзы",
  "color": "BFA35E",
  "mining_level": "iron",
  "planned": true
}
```


## Дополнительные металлические семейства

### aluminum

```json
{
  "name": "aluminum",
  "material": "Aluminum",
  "material_ru": "алюминия",
  "color": "C5CDD3",
  "mining_level": "iron"
}
```

### silver

```json
{
  "name": "silver",
  "material": "Silver",
  "material_ru": "серебра",
  "color": "D4DDE8",
  "mining_level": "iron"
}
```


## Неметаллические залежи

### limestone

```json
{
  "name": "limestone",
  "material": "Limestone",
  "material_ru": "известняка",
  "display_ru": "Известняк",
  "color": "B6B5A2",
  "veins_per_chunk": 1,
  "min_y": -16,
  "max_y": 112,
  "hardness": 2,
  "biomes": "overworld",
  "generation_profile": "stratum",
  "rarity": 6,
  "radius": 6,
  "thickness": 2,
  "mining_level": "wood"
}
```

### dolomite

```json
{
  "name": "dolomite",
  "material": "Dolomite",
  "material_ru": "доломита",
  "display_ru": "Доломит",
  "color": "B4A795",
  "veins_per_chunk": 1,
  "min_y": -32,
  "max_y": 96,
  "hardness": 2,
  "biomes": "overworld",
  "generation_profile": "stratum",
  "rarity": 8,
  "radius": 5,
  "thickness": 2,
  "mining_level": "wood"
}
```

### kaolin

```json
{
  "name": "kaolin",
  "material": "Kaolin",
  "material_ru": "каолина",
  "display_ru": "Каолин",
  "color": "D6D3C4",
  "veins_per_chunk": 1,
  "hardness": 2,
  "biomes": "overworld",
  "generation_profile": "shallow",
  "rarity": 12,
  "radius": 4,
  "thickness": 2,
  "surface_depth": 5,
  "mining_level": "wood"
}
```

### rock_salt

```json
{
  "name": "rock_salt",
  "material": "Rock Salt",
  "material_ru": "каменной соли",
  "display_ru": "Каменная соль",
  "color": "D5C7C6",
  "veins_per_chunk": 1,
  "min_y": -32,
  "max_y": 64,
  "hardness": 2,
  "biomes": "overworld",
  "generation_profile": "stratum",
  "rarity": 12,
  "radius": 4,
  "thickness": 2,
  "mining_level": "wood"
}
```

### fluorite

```json
{
  "name": "fluorite",
  "material": "Fluorite",
  "material_ru": "флюорита",
  "display_ru": "Флюорит",
  "color": "A48EBE",
  "veins_per_chunk": 1,
  "min_y": -48,
  "max_y": 32,
  "hardness": 2,
  "biomes": "overworld",
  "generation_profile": "mineral_body",
  "rarity": 16,
  "radius": 3,
  "thickness": 2,
  "mining_level": "stone"
}
```

### phosphorite

```json
{
  "name": "phosphorite",
  "material": "Phosphorite",
  "material_ru": "фосфорита",
  "display_ru": "Фосфорит",
  "color": "8E9579",
  "veins_per_chunk": 1,
  "min_y": -16,
  "max_y": 64,
  "hardness": 2,
  "biomes": "overworld",
  "generation_profile": "mineral_body",
  "rarity": 12,
  "radius": 3,
  "thickness": 2,
  "mining_level": "wood"
}
```

### graphite

```json
{
  "name": "graphite",
  "material": "Graphite",
  "material_ru": "графита",
  "display_ru": "Графит",
  "color": "4C545C",
  "veins_per_chunk": 1,
  "min_y": -64,
  "max_y": 16,
  "hardness": 2,
  "biomes": "overworld",
  "generation_profile": "mineral_body",
  "rarity": 14,
  "radius": 3,
  "thickness": 2,
  "mining_level": "stone"
}
```


## Подземные ресурсы

### crude_oil_reservoir

```json
{
  "name": "crude_oil_reservoir",
  "fluid": "crude_oil",
  "veins_per_chunk": 1,
  "min_y": -56,
  "max_y": -8,
  "generation_profile": "reservoir",
  "rarity": 28,
  "radius": 12,
  "thickness": 4
}
```

### mineral_brine_reservoir

```json
{
  "name": "mineral_brine_reservoir",
  "fluid": "mineral_brine",
  "veins_per_chunk": 1,
  "min_y": -48,
  "max_y": 16,
  "generation_profile": "reservoir",
  "rarity": 28,
  "radius": 12,
  "thickness": 4
}
```


## Политика переработки

### nuclear_power_planned

```json
true
```

### uranium_required

```json
true
```

### sulfur_source

```json
"minecraft:sulfur"
```

### stable_raw_materials

```json
true
```


## Правила переменных структур

### smog:copper_hearth

```json
{
  "min_width": 3,
  "max_width": 11,
  "min_depth": 3,
  "max_depth": 11,
  "floor": "#smog:hearth_floor",
  "casing": "#smog:copper_masonry",
  "id": "smog:copper_hearth",
  "controller": "smog:copper_hearth",
  "closed": false,
  "min_height": 3,
  "max_height": 11,
  "roles": [
    {
      "name": "fuel",
      "block": "#smog:smeltery_heaters",
      "zones": [
        "floor",
        "wall"
      ],
      "min": 1,
      "max": 4
    },
    {
      "name": "spout",
      "block": "smog:casting_spout",
      "zones": [
        "wall"
      ],
      "min": 0,
      "max": 2147483647
    },
    {
      "name": "air",
      "block": "smog:air_intake",
      "zones": [
        "wall"
      ],
      "min": 0,
      "max": 2147483647
    },
    {
      "name": "lining",
      "block": "smog:firebrick_casing",
      "zones": [
        "floor",
        "wall"
      ],
      "min": 0,
      "max": 1331
    },
    {
      "name": "closed_insert",
      "block": "smog:closed_crucible_insert",
      "zones": [
        "wall"
      ],
      "min": 0,
      "max": 1
    },
    {
      "name": "items",
      "block": "smog:smeltery_item_port",
      "zones": [
        "wall",
        "floor"
      ],
      "min": 0,
      "max": 2147483647
    }
  ]
}
```

### smog:ceramic_kiln

```json
{
  "min_width": 3,
  "max_width": 5,
  "min_depth": 3,
  "max_depth": 5,
  "floor": "#smog:hearth_floor",
  "casing": "#smog:copper_masonry",
  "id": "smog:ceramic_kiln",
  "controller": "smog:ceramic_kiln",
  "closed": true,
  "min_height": 3,
  "max_height": 5,
  "roles": [
    {
      "name": "fuel",
      "block": "smog:solid_firebox",
      "zones": [
        "floor",
        "wall"
      ],
      "min": 1,
      "max": 1
    },
    {
      "name": "vent",
      "block": "smog:kiln_vent",
      "zones": [
        "roof"
      ],
      "min": 1,
      "max": 2
    },
    {
      "name": "lining",
      "block": "smog:firebrick_casing",
      "zones": [
        "floor",
        "wall",
        "roof"
      ],
      "min": 0,
      "max": 150
    }
  ]
}
```


## Переплавка концентратов

### minecraft:copper_ingot

```json
{
  "input": "smog:copper_concentrate",
  "output": "minecraft:copper_ingot",
  "count": 2,
  "experience": 1.4,
  "smelting_ticks": 400,
  "blasting_ticks": 200
}
```

### minecraft:iron_ingot

```json
{
  "input": "smog:iron_concentrate",
  "output": "minecraft:iron_ingot",
  "count": 2,
  "experience": 1.4,
  "smelting_ticks": 400,
  "blasting_ticks": 200
}
```

### minecraft:gold_ingot

```json
{
  "input": "smog:gold_concentrate",
  "output": "minecraft:gold_ingot",
  "count": 2,
  "experience": 1.4,
  "smelting_ticks": 400,
  "blasting_ticks": 200
}
```


## Промывка

### smog:copper_concentrate

```json
{
  "input": "minecraft:raw_copper",
  "count": 8,
  "output": "smog:copper_concentrate",
  "output_count": 5,
  "water": 81000,
  "ticks": 100,
  "metal": "copper",
  "metal_units": 18000
}
```

### smog:tin_concentrate

```json
{
  "input": "smog:raw_tin",
  "count": 8,
  "output": "smog:tin_concentrate",
  "output_count": 5,
  "water": 81000,
  "ticks": 100,
  "metal": "tin",
  "metal_units": 18000
}
```

### smog:lead_concentrate

```json
{
  "input": "smog:raw_lead",
  "count": 8,
  "output": "smog:lead_concentrate",
  "output_count": 5,
  "water": 81000,
  "ticks": 100,
  "metal": "lead",
  "metal_units": 18000
}
```

### smog:iron_concentrate

```json
{
  "input": "minecraft:raw_iron",
  "count": 8,
  "output": "smog:iron_concentrate",
  "output_count": 5,
  "water": 81000,
  "ticks": 100,
  "metal": "iron",
  "metal_units": 18000
}
```

### smog:gold_concentrate

```json
{
  "input": "minecraft:raw_gold",
  "count": 8,
  "output": "smog:gold_concentrate",
  "output_count": 5,
  "water": 81000,
  "ticks": 100,
  "metal": "gold",
  "metal_units": 18000
}
```

### minecraft:flint

```json
{
  "input": "minecraft:gravel",
  "count": 4,
  "output": "minecraft:flint",
  "output_count": 1,
  "water": 20250,
  "ticks": 80
}
```

### minecraft:sand

```json
{
  "input": "minecraft:red_sand",
  "count": 4,
  "output": "minecraft:sand",
  "output_count": 4,
  "water": 20250,
  "ticks": 60
}
```

### minecraft:clay_ball

```json
{
  "input": "minecraft:mud",
  "count": 4,
  "output": "minecraft:clay_ball",
  "output_count": 8,
  "water": 40500,
  "ticks": 80
}
```


## Формование керамики

### smog:unfired_ingot_mold

```json
{
  "input": "minecraft:clay_ball",
  "count": 2,
  "output": "smog:unfired_ingot_mold"
}
```

### smog:unfired_plate_mold

```json
{
  "input": "minecraft:clay_ball",
  "count": 2,
  "output": "smog:unfired_plate_mold"
}
```

### smog:unfired_fitting_mold

```json
{
  "input": "minecraft:clay_ball",
  "count": 2,
  "output": "smog:unfired_fitting_mold"
}
```

### smog:unfired_pipe_mold

```json
{
  "input": "minecraft:clay_ball",
  "count": 3,
  "output": "smog:unfired_pipe_mold"
}
```

### smog:unfired_crucible

```json
{
  "input": "minecraft:clay_ball",
  "count": 8,
  "output": "smog:unfired_crucible"
}
```

### smog:unfired_large_crucible

```json
{
  "input": "smog:tempered_clay",
  "count": 12,
  "output": "smog:unfired_large_crucible"
}
```

### smog:unfired_firebrick

```json
{
  "input": "smog:tempered_clay",
  "count": 2,
  "output": "smog:unfired_firebrick"
}
```

### smog:unfired_pickaxe_mold

```json
{
  "input": "minecraft:clay_ball",
  "count": 3,
  "output": "smog:unfired_pickaxe_mold"
}
```

### smog:unfired_axe_mold

```json
{
  "input": "minecraft:clay_ball",
  "count": 3,
  "output": "smog:unfired_axe_mold"
}
```

### smog:unfired_shovel_mold

```json
{
  "input": "minecraft:clay_ball",
  "count": 3,
  "output": "smog:unfired_shovel_mold"
}
```

### smog:unfired_hoe_mold

```json
{
  "input": "minecraft:clay_ball",
  "count": 3,
  "output": "smog:unfired_hoe_mold"
}
```

### smog:unfired_hammer_mold

```json
{
  "input": "minecraft:clay_ball",
  "count": 3,
  "output": "smog:unfired_hammer_mold"
}
```

### smog:unfired_saw_mold

```json
{
  "input": "minecraft:clay_ball",
  "count": 3,
  "output": "smog:unfired_saw_mold"
}
```

### smog:unfired_sword_mold

```json
{
  "input": "minecraft:clay_ball",
  "count": 3,
  "output": "smog:unfired_sword_mold"
}
```

### smog:unfired_katana_mold

```json
{
  "input": "minecraft:clay_ball",
  "count": 3,
  "output": "smog:unfired_katana_mold"
}
```

### smog:unfired_machete_mold

```json
{
  "input": "minecraft:clay_ball",
  "count": 3,
  "output": "smog:unfired_machete_mold"
}
```

### smog:unfired_sickle_mold

```json
{
  "input": "minecraft:clay_ball",
  "count": 3,
  "output": "smog:unfired_sickle_mold"
}
```


## Начальные и сборочные рецепты

### potter_table

```json
{
  "name": "potter_table",
  "unlock": "minecraft:clay_ball",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "C C",
      "PPP",
      "S S"
    ],
    "key": {
      "C": "minecraft:clay_ball",
      "P": "#minecraft:planks",
      "S": "minecraft:stick"
    },
    "result": {
      "id": "smog:potter_table",
      "count": 1
    }
  }
}
```

### casting_table

```json
{
  "name": "casting_table",
  "unlock": "minecraft:brick",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "BBB",
      "S S",
      "S S"
    ],
    "key": {
      "B": "minecraft:brick",
      "S": "#minecraft:stone_tool_materials"
    },
    "result": {
      "id": "smog:casting_table",
      "count": 1
    }
  }
}
```

### copper_hearth

```json
{
  "name": "copper_hearth",
  "unlock": "minecraft:brick",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "BBB",
      "BFB",
      "CCC"
    ],
    "key": {
      "B": "minecraft:brick",
      "F": "minecraft:furnace",
      "C": "#minecraft:stone_tool_materials"
    },
    "result": {
      "id": "smog:copper_hearth",
      "count": 1
    }
  }
}
```

### ceramic_kiln

```json
{
  "name": "ceramic_kiln",
  "unlock": "minecraft:brick",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "BBB",
      "BFB",
      "BBB"
    ],
    "key": {
      "B": "minecraft:brick",
      "F": "minecraft:furnace"
    },
    "result": {
      "id": "smog:ceramic_kiln",
      "count": 1
    }
  }
}
```

### hand_bellows

```json
{
  "name": "hand_bellows",
  "unlock": "#minecraft:planks",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "PPP",
      "LLL",
      "PPP"
    ],
    "key": {
      "P": "#minecraft:planks",
      "L": "minecraft:leather"
    },
    "result": {
      "id": "smog:hand_bellows",
      "count": 1
    }
  }
}
```

### configurator

```json
{
  "name": "configurator",
  "unlock": "minecraft:stick",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      " S",
      "S "
    ],
    "key": {
      "S": "minecraft:stick"
    },
    "result": {
      "id": "smog:configurator",
      "count": 1
    }
  }
}
```

### unfired_crucible

```json
{
  "name": "unfired_crucible",
  "unlock": "minecraft:clay_ball",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "C C",
      "C C",
      "CCC"
    ],
    "key": {
      "C": "minecraft:clay_ball"
    },
    "result": {
      "id": "smog:unfired_crucible",
      "count": 1
    }
  }
}
```

### unfired_ingot_mold

```json
{
  "name": "unfired_ingot_mold",
  "unlock": "minecraft:clay_ball",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "CC"
    ],
    "key": {
      "C": "minecraft:clay_ball"
    },
    "result": {
      "id": "smog:unfired_ingot_mold",
      "count": 1
    }
  }
}
```

### unfired_plate_mold

```json
{
  "name": "unfired_plate_mold",
  "unlock": "minecraft:clay_ball",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "C C"
    ],
    "key": {
      "C": "minecraft:clay_ball"
    },
    "result": {
      "id": "smog:unfired_plate_mold",
      "count": 1
    }
  }
}
```

### unfired_fitting_mold

```json
{
  "name": "unfired_fitting_mold",
  "unlock": "minecraft:clay_ball",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "C",
      "C"
    ],
    "key": {
      "C": "minecraft:clay_ball"
    },
    "result": {
      "id": "smog:unfired_fitting_mold",
      "count": 1
    }
  }
}
```

### unfired_pipe_mold

```json
{
  "name": "unfired_pipe_mold",
  "unlock": "minecraft:clay_ball",
  "data": {
    "type": "minecraft:crafting_shapeless",
    "ingredients": [
      "minecraft:clay_ball",
      "minecraft:clay_ball",
      "minecraft:clay_ball"
    ],
    "result": {
      "id": "smog:unfired_pipe_mold",
      "count": 1
    }
  }
}
```

### tempered_clay

```json
{
  "name": "tempered_clay",
  "unlock": "minecraft:clay_ball",
  "data": {
    "type": "minecraft:crafting_shapeless",
    "ingredients": [
      "minecraft:clay_ball",
      "minecraft:clay_ball",
      "minecraft:clay_ball",
      "minecraft:clay_ball",
      "#minecraft:sand"
    ],
    "result": {
      "id": "smog:tempered_clay",
      "count": 4
    }
  }
}
```

### casting_spout

```json
{
  "name": "casting_spout",
  "unlock": "minecraft:brick",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "B B",
      "BBB"
    ],
    "key": {
      "B": "minecraft:brick"
    },
    "result": {
      "id": "smog:casting_spout",
      "count": 1
    }
  }
}
```

### casting_channel

```json
{
  "name": "casting_channel",
  "unlock": "minecraft:brick",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "BBB",
      "B B"
    ],
    "key": {
      "B": "minecraft:brick"
    },
    "result": {
      "id": "smog:casting_channel",
      "count": 2
    }
  }
}
```

### kiln_vent

```json
{
  "name": "kiln_vent",
  "unlock": "minecraft:brick",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      " B ",
      "BBB",
      " B "
    ],
    "key": {
      "B": "minecraft:brick"
    },
    "result": {
      "id": "smog:kiln_vent",
      "count": 1
    }
  }
}
```

### air_intake

```json
{
  "name": "air_intake",
  "unlock": "minecraft:brick",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "B B",
      " B ",
      "B B"
    ],
    "key": {
      "B": "minecraft:brick"
    },
    "result": {
      "id": "smog:air_intake",
      "count": 1
    }
  }
}
```

### firebrick_casing

```json
{
  "name": "firebrick_casing",
  "unlock": "smog:firebrick",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "BB",
      "BB"
    ],
    "key": {
      "B": "smog:firebrick"
    },
    "result": {
      "id": "smog:firebrick_casing",
      "count": 1
    }
  }
}
```

### solid_firebox

```json
{
  "name": "solid_firebox",
  "unlock": "minecraft:furnace",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "BFB"
    ],
    "key": {
      "B": "minecraft:brick",
      "F": "minecraft:furnace"
    },
    "result": {
      "id": "smog:solid_firebox"
    }
  }
}
```

### casting_faucet_from_ingots

```json
{
  "name": "casting_faucet_from_ingots",
  "unlock": "minecraft:copper_ingot",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      " B ",
      "CBC",
      " B "
    ],
    "key": {
      "B": "minecraft:brick",
      "C": "minecraft:copper_ingot"
    },
    "result": {
      "id": "smog:casting_faucet"
    }
  }
}
```

### casting_faucet

```json
{
  "name": "casting_faucet",
  "unlock": "smog:copper_fitting",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      " F ",
      "PFP"
    ],
    "key": {
      "F": "smog:copper_fitting",
      "P": "smog:copper_pipe_part"
    },
    "result": {
      "id": "smog:casting_faucet"
    }
  }
}
```

### air_nozzle

```json
{
  "name": "air_nozzle",
  "unlock": "smog:copper_fitting",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "PFP",
      " F "
    ],
    "key": {
      "F": "smog:copper_fitting",
      "P": "smog:copper_pipe_part"
    },
    "result": {
      "id": "smog:air_nozzle"
    }
  }
}
```

### washing_trough

```json
{
  "name": "washing_trough",
  "unlock": "#minecraft:planks",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "P P",
      "CBC",
      "PPP"
    ],
    "key": {
      "P": "#minecraft:planks",
      "C": "smog:copper_fitting",
      "B": "minecraft:bucket"
    },
    "result": {
      "id": "smog:washing_trough",
      "count": 1
    }
  }
}
```

### washing_section

```json
{
  "name": "washing_section",
  "unlock": "#minecraft:planks",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      " P ",
      "PPP",
      " P "
    ],
    "key": {
      "P": "#minecraft:planks"
    },
    "result": {
      "id": "smog:washing_section",
      "count": 2
    }
  }
}
```

### washing_sieve

```json
{
  "name": "washing_sieve",
  "unlock": "minecraft:string",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "SSS",
      "PPP"
    ],
    "key": {
      "S": "minecraft:string",
      "P": "#minecraft:planks"
    },
    "result": {
      "id": "smog:washing_sieve",
      "count": 1
    }
  }
}
```

### workshop_support

```json
{
  "name": "workshop_support",
  "unlock": "#minecraft:planks",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "P P",
      "SSS",
      "P P"
    ],
    "key": {
      "P": "#minecraft:planks",
      "S": "minecraft:stick"
    },
    "result": {
      "id": "smog:workshop_support",
      "count": 4
    }
  }
}
```

### lever_press

```json
{
  "name": "lever_press",
  "unlock": "#minecraft:planks",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "PSP",
      "CFC",
      "PPP"
    ],
    "key": {
      "P": "#minecraft:planks",
      "S": "minecraft:stick",
      "C": "smog:copper_fitting",
      "F": "#minecraft:wooden_fences"
    },
    "result": {
      "id": "smog:lever_press",
      "count": 1
    }
  }
}
```

### press_frame

```json
{
  "name": "press_frame",
  "unlock": "#minecraft:planks",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "PPP",
      "C C"
    ],
    "key": {
      "P": "#minecraft:planks",
      "C": "smog:copper_fitting"
    },
    "result": {
      "id": "smog:press_frame",
      "count": 4
    }
  }
}
```

### press_basket

```json
{
  "name": "press_basket",
  "unlock": "minecraft:stick",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "S S",
      "S S",
      "PPP"
    ],
    "key": {
      "S": "minecraft:stick",
      "P": "#minecraft:planks"
    },
    "result": {
      "id": "smog:press_basket",
      "count": 1
    }
  }
}
```

### lubricating_compound

```json
{
  "name": "lubricating_compound",
  "unlock": "smog:plant_oil_bottle",
  "data": {
    "type": "minecraft:crafting_shapeless",
    "ingredients": [
      "smog:plant_oil_bottle",
      "smog:graphite_dust"
    ],
    "result": {
      "id": "smog:lubricating_compound",
      "count": 2
    }
  }
}
```

### waxed_cord

```json
{
  "name": "waxed_cord",
  "unlock": "smog:plant_oil_bottle",
  "data": {
    "type": "minecraft:crafting_shapeless",
    "ingredients": [
      "smog:plant_oil_bottle",
      "minecraft:honeycomb",
      "minecraft:string"
    ],
    "result": {
      "id": "smog:waxed_cord",
      "count": 4
    }
  }
}
```

### water_tank

```json
{
  "name": "water_tank",
  "unlock": "minecraft:copper_ingot",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "PPP",
      "C C",
      "PPP"
    ],
    "key": {
      "P": "#minecraft:planks",
      "C": "smog:copper_plate"
    },
    "result": {
      "id": "smog:water_tank",
      "count": 1
    }
  }
}
```

### hand_water_pump

```json
{
  "name": "hand_water_pump",
  "unlock": "minecraft:copper_ingot",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      " S ",
      "CPC",
      " C "
    ],
    "key": {
      "S": "minecraft:stick",
      "C": "smog:copper_fitting",
      "P": "smog:copper_pipe_part"
    },
    "result": {
      "id": "smog:hand_water_pump",
      "count": 1
    }
  }
}
```

### press_basket_waxed

```json
{
  "name": "press_basket_waxed",
  "unlock": "smog:waxed_cord",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "S S",
      "S S",
      "PPP"
    ],
    "key": {
      "S": "smog:waxed_cord",
      "P": "#minecraft:planks"
    },
    "result": {
      "id": "smog:press_basket",
      "count": 2
    }
  }
}
```

### tool_workbench

```json
{
  "name": "tool_workbench",
  "unlock": "#minecraft:planks",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "PPP",
      "CSC",
      "P P"
    ],
    "key": {
      "P": "#minecraft:planks",
      "C": "smog:copper_fitting",
      "S": "minecraft:crafting_table"
    },
    "result": {
      "id": "smog:tool_workbench",
      "count": 1
    }
  }
}
```

### wooden_handle

```json
{
  "name": "wooden_handle",
  "unlock": "minecraft:stick",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "S",
      "S"
    ],
    "key": {
      "S": "minecraft:stick"
    },
    "result": {
      "id": "smog:wooden_handle",
      "count": 2
    }
  }
}
```

### cord_binding

```json
{
  "name": "cord_binding",
  "unlock": "minecraft:string",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "S",
      "S"
    ],
    "key": {
      "S": "minecraft:string"
    },
    "result": {
      "id": "smog:cord_binding",
      "count": 2
    }
  }
}
```

### leather_binding

```json
{
  "name": "leather_binding",
  "unlock": "minecraft:leather",
  "data": {
    "type": "minecraft:crafting_shapeless",
    "ingredients": [
      "minecraft:leather"
    ],
    "result": {
      "id": "smog:leather_binding",
      "count": 2
    }
  }
}
```

### copper_clamp

```json
{
  "name": "copper_clamp",
  "unlock": "smog:copper_fitting",
  "data": {
    "type": "minecraft:crafting_shapeless",
    "ingredients": [
      "smog:copper_fitting"
    ],
    "result": {
      "id": "smog:copper_clamp",
      "count": 1
    }
  }
}
```

### bronze_clamp

```json
{
  "name": "bronze_clamp",
  "unlock": "smog:bronze_fitting",
  "data": {
    "type": "minecraft:crafting_shapeless",
    "ingredients": [
      "smog:bronze_fitting"
    ],
    "result": {
      "id": "smog:bronze_clamp",
      "count": 1
    }
  }
}
```

### fiber_wrap

```json
{
  "name": "fiber_wrap",
  "unlock": "smog:plant_fiber",
  "data": {
    "type": "minecraft:crafting_shapeless",
    "ingredients": [
      "smog:plant_fiber"
    ],
    "result": {
      "id": "smog:fiber_wrap",
      "count": 2
    }
  }
}
```

### leather_wrap

```json
{
  "name": "leather_wrap",
  "unlock": "minecraft:leather",
  "data": {
    "type": "minecraft:crafting_shapeless",
    "ingredients": [
      "minecraft:leather",
      "minecraft:string"
    ],
    "result": {
      "id": "smog:leather_wrap",
      "count": 2
    }
  }
}
```

### wool_wrap

```json
{
  "name": "wool_wrap",
  "unlock": "#minecraft:wool",
  "data": {
    "type": "minecraft:crafting_shapeless",
    "ingredients": [
      "#minecraft:wool",
      "minecraft:string"
    ],
    "result": {
      "id": "smog:wool_wrap",
      "count": 3
    }
  }
}
```

### copper_water_pipe

```json
{
  "name": "copper_water_pipe",
  "unlock": "smog:copper_pipe_part",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "PPP"
    ],
    "key": {
      "P": "smog:copper_pipe_part"
    },
    "result": {
      "id": "smog:copper_water_pipe",
      "count": 6
    }
  }
}
```

### water_valve

```json
{
  "name": "water_valve",
  "unlock": "smog:copper_fitting",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      " F ",
      "PCP"
    ],
    "key": {
      "F": "smog:copper_fitting",
      "P": "smog:copper_pipe_part",
      "C": "smog:copper_plate"
    },
    "result": {
      "id": "smog:water_valve",
      "count": 1
    }
  }
}
```

### water_outlet

```json
{
  "name": "water_outlet",
  "unlock": "smog:copper_pipe_part",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "PF",
      " P"
    ],
    "key": {
      "P": "smog:copper_pipe_part",
      "F": "smog:copper_fitting"
    },
    "result": {
      "id": "smog:water_outlet",
      "count": 1
    }
  }
}
```

### fiber_press_basket

```json
{
  "name": "fiber_press_basket",
  "unlock": "minecraft:string",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "SSS",
      "SBS",
      "SSS"
    ],
    "key": {
      "S": "minecraft:string",
      "B": "smog:press_basket"
    },
    "result": {
      "id": "smog:fiber_press_basket",
      "count": 1
    }
  }
}
```

### bronze_drive

```json
{
  "name": "bronze_drive",
  "unlock": "smog:bronze_ingot",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "BFB",
      "PSP",
      "BFB"
    ],
    "key": {
      "B": "smog:bronze_ingot",
      "F": "smog:bronze_fitting",
      "P": "smog:bronze_pipe_part",
      "S": "minecraft:stick"
    },
    "result": {
      "id": "smog:bronze_drive",
      "count": 1
    }
  }
}
```

### fiber_string

```json
{
  "name": "fiber_string",
  "unlock": "smog:plant_fiber",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "FF"
    ],
    "key": {
      "F": "smog:plant_fiber"
    },
    "result": {
      "id": "minecraft:string",
      "count": 1
    }
  }
}
```

### fiber_binding

```json
{
  "name": "fiber_binding",
  "unlock": "smog:plant_fiber",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "F",
      "F"
    ],
    "key": {
      "F": "smog:plant_fiber"
    },
    "result": {
      "id": "smog:cord_binding",
      "count": 2
    }
  }
}
```

### handcart

```json
{
  "name": "handcart",
  "unlock": "smog:copper_fitting",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "PCP",
      "FWF",
      "P P"
    ],
    "key": {
      "P": "#minecraft:planks",
      "C": "minecraft:chest",
      "F": "smog:copper_fitting",
      "W": "smog:copper_plate"
    },
    "result": {
      "id": "smog:handcart",
      "count": 1
    }
  }
}
```

### loading_crane

```json
{
  "name": "loading_crane",
  "unlock": "smog:copper_fitting",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "PPP",
      "FCF",
      " S "
    ],
    "key": {
      "P": "#minecraft:planks",
      "F": "smog:copper_fitting",
      "C": "smog:copper_plate",
      "S": "minecraft:string"
    },
    "result": {
      "id": "smog:loading_crane",
      "count": 1
    }
  }
}
```

### closed_crucible_insert

```json
{
  "name": "closed_crucible_insert",
  "unlock": "smog:firebrick",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "BBB",
      "BCB",
      "BBB"
    ],
    "key": {
      "B": "smog:firebrick",
      "C": "minecraft:clay_ball"
    },
    "result": {
      "id": "smog:closed_crucible_insert",
      "count": 1
    }
  }
}
```

### freight_rope

```json
{
  "name": "freight_rope",
  "unlock": "minecraft:string",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "SSS",
      "SSS"
    ],
    "key": {
      "S": "minecraft:string"
    },
    "result": {
      "id": "smog:freight_rope",
      "count": 1
    }
  }
}
```

### rope_anchor

```json
{
  "name": "rope_anchor",
  "unlock": "minecraft:string",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "PPP",
      "FCF",
      "PSP"
    ],
    "key": {
      "P": "#minecraft:planks",
      "F": "smog:copper_fitting",
      "C": "smog:copper_plate",
      "S": "minecraft:string"
    },
    "result": {
      "id": "smog:rope_anchor",
      "count": 1
    }
  }
}
```

### molding_sand

```json
{
  "name": "molding_sand",
  "unlock": "#minecraft:sand",
  "data": {
    "type": "minecraft:crafting_shapeless",
    "ingredients": [
      "#minecraft:sand",
      "minecraft:clay_ball"
    ],
    "result": {
      "id": "smog:molding_sand",
      "count": 4
    }
  }
}
```

### ceramic_core

```json
{
  "name": "ceramic_core",
  "unlock": "minecraft:brick",
  "data": {
    "type": "minecraft:crafting_shapeless",
    "ingredients": [
      "minecraft:brick",
      "minecraft:clay_ball"
    ],
    "result": {
      "id": "smog:ceramic_core",
      "count": 4
    }
  }
}
```

### smeltery_fuel_tank

```json
{
  "name": "smeltery_fuel_tank",
  "unlock": "smog:firebrick",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "BGB",
      "G G",
      "BBB"
    ],
    "key": {
      "B": "smog:firebrick",
      "G": "minecraft:glass"
    },
    "result": {
      "id": "smog:smeltery_fuel_tank",
      "count": 1
    }
  }
}
```

### casting_basin

```json
{
  "name": "casting_basin",
  "unlock": "smog:firebrick",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "B B",
      "B B",
      "BBB"
    ],
    "key": {
      "B": "smog:firebrick"
    },
    "result": {
      "id": "smog:casting_basin",
      "count": 1
    }
  }
}
```

### smeltery_glass

```json
{
  "name": "smeltery_glass",
  "unlock": "smog:firebrick",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "BBB",
      "BGB",
      "BBB"
    ],
    "key": {
      "B": "smog:firebrick",
      "G": "minecraft:glass"
    },
    "result": {
      "id": "smog:smeltery_glass",
      "count": 4
    }
  }
}
```

### fluid_canister

```json
{
  "name": "fluid_canister",
  "unlock": "smog:copper_plate",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      " F ",
      "P P",
      "PPP"
    ],
    "key": {
      "F": "smog:copper_fitting",
      "P": "smog:copper_plate"
    },
    "result": {
      "id": "smog:fluid_canister",
      "count": 1
    }
  }
}
```

### smeltery_item_port

```json
{
  "name": "smeltery_item_port",
  "unlock": "smog:firebrick",
  "data": {
    "type": "minecraft:crafting_shaped",
    "pattern": [
      "BBB",
      "BCB",
      "BBB"
    ],
    "key": {
      "B": "smog:firebrick",
      "C": "minecraft:chest"
    },
    "result": {
      "id": "smog:smeltery_item_port",
      "count": 1
    }
  }
}
```


## Обжиг

### smog:ingot_mold

```json
{
  "input": "smog:unfired_ingot_mold",
  "output": "smog:ingot_mold"
}
```

### smog:plate_mold

```json
{
  "input": "smog:unfired_plate_mold",
  "output": "smog:plate_mold"
}
```

### smog:fitting_mold

```json
{
  "input": "smog:unfired_fitting_mold",
  "output": "smog:fitting_mold"
}
```

### smog:pipe_mold

```json
{
  "input": "smog:unfired_pipe_mold",
  "output": "smog:pipe_mold"
}
```

### smog:ceramic_crucible

```json
{
  "input": "smog:unfired_crucible",
  "output": "smog:ceramic_crucible"
}
```

### smog:large_ceramic_crucible

```json
{
  "input": "smog:unfired_large_crucible",
  "output": "smog:large_ceramic_crucible"
}
```

### smog:firebrick

```json
{
  "input": "smog:unfired_firebrick",
  "output": "smog:firebrick"
}
```

### smog:pickaxe_mold

```json
{
  "input": "smog:unfired_pickaxe_mold",
  "output": "smog:pickaxe_mold"
}
```

### smog:axe_mold

```json
{
  "input": "smog:unfired_axe_mold",
  "output": "smog:axe_mold"
}
```

### smog:shovel_mold

```json
{
  "input": "smog:unfired_shovel_mold",
  "output": "smog:shovel_mold"
}
```

### smog:hoe_mold

```json
{
  "input": "smog:unfired_hoe_mold",
  "output": "smog:hoe_mold"
}
```

### smog:hammer_mold

```json
{
  "input": "smog:unfired_hammer_mold",
  "output": "smog:hammer_mold"
}
```

### smog:saw_mold

```json
{
  "input": "smog:unfired_saw_mold",
  "output": "smog:saw_mold"
}
```

### smog:sword_mold

```json
{
  "input": "smog:unfired_sword_mold",
  "output": "smog:sword_mold"
}
```

### smog:katana_mold

```json
{
  "input": "smog:unfired_katana_mold",
  "output": "smog:katana_mold"
}
```

### smog:machete_mold

```json
{
  "input": "smog:unfired_machete_mold",
  "output": "smog:machete_mold"
}
```

### smog:sickle_mold

```json
{
  "input": "smog:unfired_sickle_mold",
  "output": "smog:sickle_mold"
}
```


## Дополнительные переводы

### en_us

```json
{
  "jei.smog.example_label": "Representative recipe",
  "jei.smog.example_materials": "Example materials. Parts can use other compatible materials; the anvil shows the actual result.",
  "jei.smog.copper.finishing": "Hammer-finish a casting. The same operation works for every head material.",
  "jei.smog.copper.conversion": "Recover a head from a vanilla tool; its wear, name and enchantments are retained.",
  "tool.smog.tip.durability": "Durability",
  "tool.smog.tip.material": "Material",
  "tool.smog.tip.traits": "Traits",
  "tool.smog.tip.properties": "Properties",
  "tool.smog.tip.damage": "Attack damage",
  "tool.smog.tip.attack_speed": "Attacks / second",
  "tool.smog.tip.mining_speed": "Mining speed",
  "tool.smog.tip.head_damage": "Head damage",
  "tool.smog.tip.speed_factor": "Speed factor",
  "tool.smog.tip.life_factor": "Durability factor",
  "tool.smog.tip.tier": "Harvest tier",
  "tool.smog.tip.repair": "Repair material",
  "tool.smog.tip.trait_details": "Material effects",
  "tool.smog.tip.construction": "Construction",
  "tool.smog.tip.part.head": "Head",
  "tool.smog.tip.part.handle": "Rod",
  "tool.smog.tip.part.binding": "Binding",
  "tool.smog.tip.part.wrap": "Grip wrap",
  "tool.smog.tip.part_type": "Part",
  "tool.smog.tip.content": "Metal content",
  "tool.smog.tip.keys": "Ctrl · properties    Alt · construction",
  "tool.smog.tip.trait.work_hardening": "Work hardening",
  "tool.smog.tip.trait.staggering": "Staggering",
  "tool.smog.tip.trait.marking": "Marking",
  "tool.smog.tip.trait.grip": "Secure grip",
  "tool.smog.tip.use.pickaxe": "Mining stone and ores",
  "tool.smog.tip.use.axe": "Chopping wood",
  "tool.smog.tip.use.saw": "Tree felling · sneak for one block",
  "tool.smog.tip.use.shovel": "Digging soil",
  "tool.smog.tip.use.hammer": "Mining 3×3×1 · workshop hammer",
  "tool.smog.tip.use.hoe": "Tilling soil",
  "tool.smog.tip.use.sword": "Balanced melee blade",
  "tool.smog.tip.use.katana": "Fast blade · lower damage",
  "tool.smog.tip.use.machete": "Heavy blade · higher damage",
  "tool.smog.tip.use.sickle": "Right-click · harvest and replant 3×1×3",
  "gui.smog.pottery.input": "Material",
  "gui.smog.pottery.choose": "Shape",
  "gui.smog.pottery.output": "Result",
  "gui.smog.pottery.cost": "%s × %s",
  "gui.smog.pottery.take": "Load material, select a shape, then take the result to craft.",
  "gui.smog.amount.blocks.one": "%s block",
  "gui.smog.amount.blocks.ending_one": "%s blocks",
  "gui.smog.amount.blocks.few": "%s blocks",
  "gui.smog.amount.blocks.many": "%s blocks",
  "gui.smog.amount.ingots.one": "%s ingot",
  "gui.smog.amount.ingots.ending_one": "%s ingots",
  "gui.smog.amount.ingots.few": "%s ingots",
  "gui.smog.amount.ingots.many": "%s ingots",
  "gui.smog.amount.nuggets.one": "%s nugget",
  "gui.smog.amount.nuggets.ending_one": "%s nuggets",
  "gui.smog.amount.nuggets.few": "%s nuggets",
  "gui.smog.amount.nuggets.many": "%s nuggets",
  "gui.smog.amount.nuggets.fraction": "%s nuggets",
  "itemGroup.smog.blocks": "Smog: Blocks",
  "itemGroup.smog.mechanisms": "Smog: Mechanisms",
  "itemGroup.smog.controllers": "Smog: Controllers",
  "itemGroup.smog.tool_parts": "Smog: Tool Parts",
  "itemGroup.smog.tools": "Smog: Tools",
  "tool.smog.trait.work_hardening": "Continuous mining: up to +%1$s%% speed; expires after %2$s s.",
  "tool.smog.trait.staggering": "Staggering: hits slow mobs for %s seconds, once every %s seconds.",
  "tool.smog.trait.marking": "Marking: a hit outlines the mob for %s seconds, once every %s seconds.",
  "tool.smog.trait.grip": "Every %s uses, one use costs no durability.",
  "tool.smog.preview_queue": "Working: %s remaining",
  "tool.smog.preview_head_tier": "%s — harvest tier: %s",
  "tool.smog.preview_purpose": "Use: %s",
  "tool.smog.preview_use_pickaxe": "Mine stone and ores",
  "tool.smog.preview_use_axe": "Chop wood; strip logs",
  "tool.smog.preview_use_shovel": "Dig soil, sand and gravel; make paths",
  "tool.smog.preview_use_hoe": "Harvest vegetation; till soil",
  "tool.smog.preview_use_hammer": "Mines a 3×3×1 area; workshop assembly and finishing",
  "tool.smog.preview_baseline": "Heads → assembled tool (multiple heads: average durability)",
  "tool.smog.preview_life": "Full durability: %s → %s (%s)",
  "tool.smog.preview_speed": "Peak mining speed: %s → %s (%s)",
  "tool.smog.preview_speed_family": "%s: %s → %s (%s)",
  "tool.smog.preview_conditions": "Base values; enchantments and situational traits apply during use.",
  "gui.smog.casting.core_slot": "Optional ceramic mandrel: pipe mold → standard rod; the mandrel is consumed.",
  "gui.smog.casting.mold_slot": "Casting mold or sample for making a metal cast",
  "message.smog.assembly_fixed": "At %s, %s, %s: required part — %s.",
  "tag.item.smog.copper_masonry": "Foundry Masonry",
  "tag.item.smog.fiber_sources": "Plant Fiber Sources",
  "tag.item.smog.hearth_floor": "Foundry Floor Blocks",
  "tag.item.smog.nonmetal_deposits": "Nonmetal Deposits",
  "tag.item.smog.nonmetal_materials": "Nonmetal Materials",
  "tag.item.smog.oil_seeds": "Oil Seeds",
  "tag.item.smog.sulfur_sources": "Sulfur Sources",
  "tag.item.c.unfired_ceramics": "Unfired Ceramics",
  "itemGroup.smog.copper_age": "Smog: Copper Workshop",
  "gui.smog.copper.previous": "Previous",
  "gui.smog.copper.next": "Next",
  "gui.smog.copper.craft": "Make batch",
  "gui.smog.copper.alloy": "3Cu + Sn",
  "gui.smog.copper.heat": "Heat: %s / %s",
  "gui.smog.copper.metal": "Metal: %s / %s ingots",
  "gui.smog.copper.selection": "Mode: %s",
  "gui.smog.copper.status_0": "Waiting for an operation",
  "gui.smog.copper.status_1": "Assemble with configurator",
  "gui.smog.copper.status_2": "Add recipe input",
  "gui.smog.copper.status_3": "Add fuel",
  "gui.smog.copper.status_4": "Warming up",
  "gui.smog.copper.status_5": "Storage full",
  "gui.smog.copper.status_6": "Insert suitable mold",
  "gui.smog.copper.status_7": "Casting / cooling",
  "gui.smog.copper.status_8": "Not enough molten metal",
  "conductor.smog.bronze": "Bronze",
  "jei.smog.copper.title": "Copper workshop",
  "jei.smog.copper.pottery": "Choose shape; make a batch.",
  "jei.smog.copper.casting": "Melt in hearth; pour into reusable mold.",
  "jei.smog.copper.alloy": "Keep Cu and Sn molten together: automatic 3:1 alloy.",
  "message.smog.molten_status": "Cu %s | Sn %s | Bronze %s ingots | Heat %s",
  "message.smog.mold_selected": "Selected: %s",
  "jei.smog.guide.hearth": "Hollow 3 to 11 blocks on every axis. Solid floor, valid walls, empty chamber. Functional inserts cannot occupy edges or corners. Configure with ordinary use; sneak-use only rotates. Use the controller empty-handed for the tank GUI and up to 81 independent input jobs, nine per page. Click a fluid layer to select it for every faucet. Stored liquids do not solidify when fuel ends. Alloys form automatically only with sufficient fuel heat. Hoppers and items dropped inside the chamber remain supported. Resizing preserves all items and fluid.",
  "jei.smog.guide.kiln": "Closed shell 3–5 wide/deep, 3–5 high. Empty chamber, one floor firebox and one or two roof vents. Configure to assemble. Load fuel and unfired ceramics by hand; empty-hand click collects output. Batch size 8–64 depends on chamber.",
  "jei.smog.guide.casting": "Mount a mold on the table, select the fluid in the controller and open the faucet. Actual fluid portions accumulate in the mold, then cool separately. A partial cast retains its metal and cannot accept a different metal. Closing the faucet, stopping fuel or reloading the world preserves it. Mold pickup is locked until finished. Breaking the vessel returns exact molten contents in canisters. Take finished castings by hand or hopper. Use a basin for nine-ingot storage blocks.",
  "jei.smog.guide.pottery": "Sneak empty-hand click selects a visible product. Click with clay or tempered clay to make up to eight. Empty-hand click collects output. Fire raw ceramics in a furnace or ceramic kiln. Sneak-click with an item opens the optional detailed GUI.",
  "jei.smog.guide.bellows": "Fit an air nozzle outside a formed hearth air port, open it and place hand bellows beside it. Right-click bellows to assist up to eight ingots. Rotate only with sneak-right-click configurator.",
  "jei.smog.guide.crucible": "Separate ceramic vessel. Do not install it inside the casting hearth: its chamber must be completely empty. Hearth capacity now comes from chamber dimensions, not crucibles.",
  "jei.smog.guide.firebox": "Exactly one Smog firebox in a floor or wall face. It shares the controller inventory rather than acting as a separate vanilla furnace. Click the assembled structure with fuel or feed the controller side with a hopper.",
  "jei.smog.guide.spout": "Wall fluid port does not pour by itself. Attach a casting faucet outside and open with right-click. It pours downward onto a table or channel below. Pipes and compatible containers may still connect directly to the port. Rotate only with sneak-right-click configurator.",
  "jei.smog.guide.lining": "Fire refractory bricks from tempered clay; four bricks make refractory masonry. Replace ordinary masonry or floor blocks to reduce heat loss. More lining gives better retention. All models use 16 pixels per block.",
  "jei.smog.guide.molds": "Ceramic starter molds remain usable. Imprint a sand mold on the toolmaker bench: molding sand in slot1, a finished head/tool/component in slot2, then hammer. The sample is retained; sand molds are single use. Pour two gold ingots around a sample on the casting table for a reusable metal cast. The sample remains, and remelting the cast returns the same two gold ingots. Forms depend on shape, not material.",
  "jei.smog.guide.configurator": "Right-click controller to assemble. Sneak-right-click dismantles and rotates it. Formed parts route to their controller. Sneak-right-click loose spouts, channels and intakes with the configurator to rotate them. Feedback appears above the hotbar.",
  "jei.smog.guide.residue": "Preserves an exact fractional fluid amount recovered when a tank is broken. Remelt it to recover all metal without rounding or duplication.",
  "jei.smog.guide.parts": "Cast copper and bronze parts. A plate contains one ingot, fitting a quarter, pipe blank a half. Remelting returns exactly the same amount. Parts remain available for upcoming copper-age devices.",
  "jei.smog.guide.bronze": "Three parts molten copper plus one part molten tin automatically become four parts molten bronze in a hot hearth. No mixing button. Excess metal stays in the tank. Reuse the same molds.",
  "jei.smog.guide.molten": "The smeltery stores molten metals separately. Available metals and alloys depend on temperature and lining. The selected metal flows through a liquid port, faucet and casting channels. One ingot is one ninth of a bucket. Compatible canisters, tanks and other mods can access the ports. Copper utility pipes carry water and oil. Shared metal tags determine accepted materials.",
  "jei.smog.structure.copper_hearth": "Hollow body, 3 to 11 blocks on each axis. 1 to 4 fireboxes/fuel tanks. Ports optional; inserts cannot occupy edges/corners. Use a configurator to assemble.",
  "jei.smog.structure.ceramic_kiln": "Closed shell, empty chamber. Floor firebox, roof vent. Use configurator. Controller information page lists full rules.",
  "jei.smog.copper.firing": "Batch firing: chamber size allows 8–64 items.",
  "jei.smog.copper.melting": "Heat the hearth; molten metal drains through a spout.",
  "message.smog.bellows": "Bellows operated: assists up to 8 ingots.",
  "message.smog.bellows_disconnected": "Requires an open air nozzle and a formed hearth air port.",
  "message.smog.valve_open": "Valve open",
  "message.smog.valve_closed": "Valve closed",
  "jei.smog.size_range": "Size: from %s to %s blocks",
  "message.smog.controller_missing": "Place a hearth or kiln controller in the structure wall",
  "message.smog.manual_workshop": "Fluid: %s mB | Queued batches: %s. Load ingredients, then use with an empty hand.",
  "message.smog.manual_queued": "Queued batches: %s. Repeated clicks do not speed up work.",
  "jei.smog.copper.washing": "Wash primary ores or sort gravel, red sand and mud. Water and output amounts are shown in the recipe.",
  "jei.smog.copper.pressing": "8 seeds → 250 mB oil + seed cake. Start empty-handed; extract with bottles or pipes.",
  "jei.smog.guide.washing": "Assemble the 3 × 1 × 2 trough with the configurator. Supply water by bucket or pipe. Wash raw copper, tin, lead, iron or gold in batches of eight for five concentrate; each melts to two ingots. Also separate flint from gravel, wash red sand into sand, or extract clay from mud. Start a finite queue empty-handed or with the GUI button. Finished metal and concentrate cannot be washed again.",
  "jei.smog.guide.pressing": "Build the 3 × 2 × 2 layout. Load seeds, then use empty-handed to queue batches. Extract oil with bottles, buckets or pipes. Seed cake is compostable. This press does not make metal plates.",
  "message.smog.assembly_edge": "Component on an edge or corner at %s, %s, %s; use masonry here.",
  "message.smog.assembly_block": "Check block at %s, %s, %s: complete the shell and keep the chamber empty.",
  "message.smog.assembly_role": "%s: found %s, requires %s to %s.",
  "message.smog.assembly_overlap": "A block already belongs to another structure.",
  "message.smog.assembly_check": "Check the JEI layout, controller position and required parts.",
  "role.smog.fuel": "Firebox",
  "role.smog.spout": "Fluid port",
  "role.smog.air": "Air port",
  "role.smog.vent": "Vent",
  "role.smog.lining": "Lining",
  "message.smog.pump_ready": "Pump primed: up to 4 buckets per action.",
  "message.smog.pump_missing_water": "Place the pump above a water source.",
  "message.smog.water_supply": "Water: %s / 8000 mB. Supplies adjacent workshops.",
  "message.smog.lubricated": "Press lubricated: the next 8 batches work faster.",
  "jei.smog.guide.water": "Copper reservoirs hold 8 buckets of water or plant oil without mixing. Prime a hand pump above source water for a finite 4-bucket job. Copper pipes connect pumps, reservoirs and consumers; presses export oil and reservoirs equalize their levels. Each source supplies up to 200 mB/s. Valves pass fluid along their axis; outlets accept from behind and deliver downwards. Empty-hand use toggles the manual valve, while redstone closes it. Routes are limited to 16 blocks, 256 nodes and loaded chunks. Copper plumbing does not carry lava or molten metals.",
  "advancements.smog.copper.foundry.title": "Heart of the workshop",
  "advancements.smog.copper.foundry.description": "Make a smeltery controller. Build its hollow shell using the JEI layout.",
  "advancements.smog.copper.kiln.title": "Firing in batches",
  "advancements.smog.copper.kiln.description": "Prepare a kiln controller for batch ceramic firing.",
  "advancements.smog.copper.bench.title": "Piece by piece",
  "advancements.smog.copper.bench.description": "Prepare a copper anvil.",
  "advancements.smog.copper.tools.title": "A tool of your own",
  "advancements.smog.copper.tools.description": "Obtain a modular pickaxe with a head, handle and binding of your choice.",
  "advancements.smog.copper.hollow.title": "A rod for every tool",
  "advancements.smog.copper.hollow.description": "Obtain a copper rod for a modular tool.",
  "advancements.smog.copper.water.title": "Water on site",
  "advancements.smog.copper.water.description": "Make a hand pump for finite water orders.",
  "advancements.smog.copper.fiber.title": "Without hunting",
  "advancements.smog.copper.fiber.description": "Obtain plant fiber using the press fiber basket.",
  "advancements.smog.copper.cart.title": "Off to the building site",
  "advancements.smog.copper.cart.description": "Make a freight handcart with 54 slots.",
  "advancements.smog.copper.crane.title": "A loading yard",
  "advancements.smog.copper.crane.description": "Prepare a crane controller to move batches between two bays.",
  "advancements.smog.copper.ropeway.title": "Across the gap",
  "advancements.smog.copper.ropeway.description": "Make a ropeway anchor. Connect two anchors with freight rope.",
  "advancements.smog.copper.root.title": "Copper Workshop",
  "advancements.smog.copper.root.description": "Start with clay: molds, casting, tools and workshop supplies.",
  "advancements.smog.copper.pottery.title": "A reusable mold",
  "advancements.smog.copper.pottery.description": "Fire a reusable ingot mold.",
  "advancements.smog.copper.casting.title": "Workshop fittings",
  "advancements.smog.copper.casting.description": "Obtain copper fittings for workshop equipment.",
  "advancements.smog.copper.washing.title": "Prepare the ore",
  "advancements.smog.copper.washing.description": "Wash 8 raw copper into 5 concentrate, yielding 10 ingots.",
  "advancements.smog.copper.pressing.title": "Workshop oil",
  "advancements.smog.copper.pressing.description": "Obtain plant oil. The press also produces useful seed cake.",
  "advancements.smog.copper.bronze.title": "The first alloy",
  "advancements.smog.copper.bronze.description": "Alloy copper and tin at 3:1. Put the bronze to work in your workshop.",
  "tool.smog.unconfigured": "Assemble this tool from parts at the workbench.",
  "tool.smog.broken": "Head worn out. Assembly retained for repair.",
  "tool.smog.durability": "Durability: %s / %s",
  "tool.smog.part_head": "Head: %s",
  "tool.smog.part_handle": "Handle: %s",
  "tool.smog.part_binding": "Binding: %s",
  "tool.smog.part_wrap": "Wrap: %s",
  "tool.smog.working": "Working. Parts are temporarily secured.",
  "tool.smog.mount_hammer": "Sneak + right-click an empty workbench mount to service this hammer.",
  "tool.smog.assembly_help": "Add a head, handle and binding. Metal parts require a working assembled hammer; nonmetal parts can be assembled empty-handed or with the Craft button.",
  "tool.smog.preview": "Result: %s | Durability: %s. Nonmetal: empty hand; metal: assembled hammer.",
  "jei.smog.copper.tools": "Head, handle and binding; optional wrap. Only assemblies containing metal require a working assembled hammer.",
  "jei.smog.guide.tools": "Cast and hammer-finish a head on an empty workbench. Add a compatible handle and binding, optionally a wrap. One hammer action starts assembly. Sneak with hammer to disassemble a mounted tool, retaining head wear and enchantments. New heads carry only their own enchantments. A matching ingot repairs a quarter of durability. Sneak-use an item for the optional menu.",
  "tool.smog.working_short": "Assembly in progress",
  "tool.smog.ready_short": "Nonmetal: empty hand; metal: hammer",
  "tool.smog.missing_parts": "Add compatible parts",
  "tool.smog.tier": "Head harvest level: %s",
  "tool.smog.speed": "Working speed: %s",
  "jei.smog.copper.fiber_pressing": "8 fibrous plants → 4 fiber. Requires the fiber basket.",
  "jei.smog.guide.drive": "Replace an upper press-frame block with a bronze drive. Flowing water must touch the front impeller; a still source does not power it. The press automatically queues loaded batches while flow, ingredients and output space are available; manual use remains possible without flow. The same drive next to bellows operates them periodically. Sneak-configure to rotate. This is mechanical power, no FE is generated.",
  "tool.smog.profile_head": "Head life: %s | Mining speed: %s",
  "tool.smog.profile_support": "Speed ×%s | Life ×%s",
  "tool.smog.finish_cast": "Finish the casting with a working hammer.",
  "tool.smog.selected_schema": "Template: %s. Sneak-use an empty bench to cycle.",
  "jei.smog.copper.shaping": "Direct shaping costs more metal than casting. Select the template and use a working hammer.",
  "message.smog.cart_busy": "Another player is pulling this cart",
  "message.smog.cart_released": "Cart parked",
  "message.smog.cart_pulling": "Pulling cart · Sneak + use to park",
  "message.smog.crane_forward": "Loading batch: front bay → rear bay · Sneak + use to reverse",
  "message.smog.crane_reverse": "Loading batch: rear bay → front bay · Sneak + use to reverse",
  "conductor.smog.lead": "Lead",
  "conductor.smog.zinc": "Zinc",
  "conductor.smog.aluminum": "Aluminum",
  "conductor.smog.silver": "Silver",
  "conductor.smog.gold": "Gold",
  "conductor.smog.brass": "Brass",
  "role.smog.closed_insert": "Closed crucible insert",
  "gui.smog.copper.status_9": "Requires a compatible insert or temperature range",
  "message.smog.rope_first": "First anchor selected · Use on the other anchor",
  "message.smog.rope_linked": "Linked: items to load, empty hand to send, stick to ride",
  "message.smog.rope_invalid": "Clear span up to 24 blocks required; anchors must be free",
  "message.smog.rope_unlinked": "Ropeway disconnected; travelling cargo remains in its anchor",
  "message.smog.rope_queued": "One cargo batch queued",
  "jei.smog.guide.handcart": "54 ordinary slots. Use to open cargo; sneak + use to pull or park. Prepare a road. Disconnects beyond 6 blocks. Cargo persists and drops on destruction.",
  "jei.smog.guide.crane": "5 × 5 × 5 gantry, 3 × 4 passage. Bays A and B are 4 blocks below the controller, 1 and 3 blocks inside. Use empty-handed to transfer up to 64 items; sneak-use reverses. Cargo remains on the hoist if blocked. A bronze drive speeds up and repeats loading. A rising redstone signal at the controller starts one batch; the input has a red marker on the front beam.",
  "jei.smog.guide.ropeway": "Connect two anchors with freight rope across a clear, loaded span up to 24 blocks. Insert items and use empty-handed to send one stack. Take delivered items with use; sneak empty-handed retrieves input. Hoppers work. Sneak with rope disconnects without losing cargo.",
  "jei.smog.guide.closed_insert": "Install in a flat hearth wall and configure the structure. Allows hot zinc processing and automatic brass alloying at 3 copper to 1 zinc. Open zinc remelting needs low heat. Ready aluminum needs refractory lining; bauxite cannot be reduced by this process.",
  "jei.smog.copper.brass_alloy": "Closed insert · Cu:Zn = 3:1",
  "tool.smog.shaping_cost": "Input: %s · Yield: %s",
  "conductor.smog.copper": "Copper",
  "conductor.smog.tin": "Tin",
  "conductor.smog.iron": "Iron",
  "conductor.smog.nickel": "Nickel",
  "conductor.smog.manganese": "Manganese",
  "conductor.smog.chromium": "Chromium",
  "conductor.smog.titanium": "Titanium",
  "conductor.smog.tungsten": "Tungsten",
  "conductor.smog.molybdenum": "Molybdenum",
  "conductor.smog.uranium": "Uranium",
  "conductor.smog.lithium": "Lithium",
  "conductor.smog.steel": "Steel",
  "conductor.smog.cast_iron": "Cast Iron",
  "conductor.smog.wrought_iron": "Wrought Iron",
  "conductor.smog.stainless_steel": "Stainless Steel",
  "conductor.smog.tool_steel": "Tool Steel",
  "conductor.smog.duralumin": "Duralumin",
  "conductor.smog.cupronickel": "Cupronickel",
  "conductor.smog.constantan": "Constantan",
  "conductor.smog.invar": "Invar",
  "conductor.smog.electrum": "Electrum",
  "conductor.smog.nichrome": "Nichrome",
  "conductor.smog.solder": "Tin-Lead Solder",
  "conductor.smog.tungsten_steel": "Tungsten Steel",
  "conductor.smog.titanium_alloy": "Titanium Alloy",
  "conductor.smog.nickel_superalloy": "Nickel Superalloy",
  "conductor.smog.aluminum_bronze": "Aluminum Bronze",
  "conductor.smog.netherite": "Netherite",
  "gui.smog.smeltery.inputs": "Slots: %s",
  "gui.smog.smeltery.quantity": "Volume: %s ingots",
  "gui.smog.smeltery.select": "Left click: select bottom layer · Right click with container: fill",
  "gui.smog.amount.blocks": "%s blocks",
  "gui.smog.amount.ingots": "%s ingots",
  "gui.smog.amount.nuggets": "%s nuggets",
  "gui.smog.smeltery.fuel": "Stored: %s",
  "gui.smog.smeltery.temperature": "Heat: %s · Fuel limit: %s",
  "gui.smog.smeltery.fuel_cursor": "Carried container: left click to empty, right click to fill",
  "gui.smog.smeltery.container_modes": "Container mode: auto / empty / fill",
  "gui.smog.smeltery.container_mode_0": "Auto: empty container first, then try filling it",
  "gui.smog.smeltery.container_mode_1": "Empty containers into the shared tank",
  "gui.smog.smeltery.container_mode_2": "Fill containers with the selected molten metal",
  "gui.smog.smeltery.solid_fuel": "Solid fuel",
  "message.smog.fuel_tank": "Fuel tank: %s / %s",
  "message.smog.fuel_powder": "Blaze powder required for all stored lava: %s",
  "message.smog.fuel_prepared": "Lava converted into blazing fuel without changing its volume",
  "message.smog.casting_locked": "Pouring: %s / %s · Mold locked until ready",
  "gui.smog.copper.status_10": "Casting is cooling",
  "jei.smog.copper.imprint": "Sand mold · Sample retained",
  "jei.smog.copper.mold_making": "Gold cast · Sample retained",
  "jei.smog.copper.required_heat": "Requires heat %s",
  "jei.smog.guide.fuel_tank": "Physical eight-bucket fuel tank in a flat wall or floor. Use lava buckets, canisters or Fabric pipes. Lava heat1100; blazing fuel1600, limited by the shell to1100 or1350 with sufficient refractory lining. Use blaze powder on stored lava: two powder per started bucket of the entire tank. Volume is preserved and no extra bucket is produced. Larger workshops consume more fuel. Breaking returns residual fuel in a canister.",
  "jei.smog.guide.basin": "Cast one metal storage block from nine ingots of fluid, without a mold. Fill under a faucet or via compatible Fabric fluid pipes. Filling and cooling are distinct world-visible stages.",
  "jei.smog.guide.canister": "Eight-bucket single-fluid canister with standard FluidStorage.ITEM support. Transfer through physical ports and tanks. Breaking occupied vessels returns exact residual amounts.",
  "gui.smog.copper.status_11": "Needs a larger structure; input may be retrieved",
  "gui.smog.smeltery.capacity": "Tank: %s / %s ingots",
  "role.smog.items": "Item input",
  "entity.smog.handcart": "Cargo Handcart",
  "gui.smog.smeltery.heat": "Heat: %s / %s °C; fuel limit: %s °C",
  "jei.smog.smeltery_fuels": "Smeltery Fuels",
  "jei.smog.fuel_heat": "Limit: %s °C · speed ×%s",
  "jei.smog.fuel_preparation": "Prepare inside the fuel tank",
  "message.smog.crane_started": "Loading started: %s → %s",
  "message.smog.crane_busy": "Crane is working; wait for the batch",
  "message.smog.crane_source": "No container or parked handcart at %s",
  "message.smog.crane_empty": "No cargo available at %s",
  "message.smog.crane_destination": "No room for cargo at %s",
  "message.smog.crane_blocked": "Hoist path obstructed: %s",
  "message.smog.crane_unloaded": "Loading bay is not loaded",
  "message.smog.crane_direction": "Direction: %s → %s · Use to load",
  "message.smog.crane_wait": "Cargo held safely; clear the destination",
  "message.smog.crane_complete": "Transferred: %s items",
  "tool.smog.tier.wooden": "Wood",
  "tool.smog.tier.wood": "Wood",
  "tool.smog.tier.stone": "Stone",
  "tool.smog.tier.copper": "Copper",
  "tool.smog.tier.iron": "Iron",
  "tool.smog.tier.diamond": "Diamond",
  "tool.smog.tier.netherite": "Netherite",
  "tool.smog.tier.gold": "Gold",
  "tool.smog.tier.golden": "Gold",
  "tool.smog.extra_parts": "Remove unused parts",
  "tool.smog.need_role": "Required part: %s",
  "tool.smog.rebuild_compare": "Durability before: %s; after: %s",
  "tool.smog.role.head": "head",
  "tool.smog.role.handle": "handle",
  "tool.smog.role.binding": "binding",
  "tool.smog.role.wrap": "wrap",
  "tool.smog.role.clamp": "clamp",
  "tool.smog.role.pickaxe_head": "pickaxe head",
  "tool.smog.role.axe_head": "axe head",
  "tool.smog.role.shovel_head": "shovel head",
  "tool.smog.head_wear": "Remaining head life: %s / %s",
  "gui.smog.process.heat": "Required heat: %s",
  "gui.smog.process.open_max": "Open chamber limit: %s",
  "gui.smog.copper.status_12": "Required heat exceeds the body limit",
  "gui.smog.copper.status_13": "Refractory lining required",
  "gui.smog.copper.status_14": "Closed working chamber required",
  "gui.smog.copper.status_15": "Controlled working atmosphere required",
  "gui.smog.copper.status_16": "Overheated: cool the open chamber",
  "gui.smog.casting.stage_0": "Install a mold",
  "gui.smog.casting.stage_1": "Waiting for molten metal",
  "gui.smog.casting.stage_2": "Pouring",
  "gui.smog.casting.stage_3": "Cooling",
  "gui.smog.casting.stage_4": "Casting ready",
  "gui.smog.casting.stage_5": "Clear the output",
  "gui.smog.casting.volume": "Poured: %s / %s",
  "message.smog.casting_cooling": "Cooling: %s s remaining · Mold locked",
  "gui.smog.casting.cooling": "Cooling: %s s",
  "gui.smog.manual.batches": "Batches: %s",
  "item.smog.freight_rope.help": "Ropeway: use rope on two anchors, up to 24 blocks",
  "item.smog.freight_rope.unlink": "Sneak-use an anchor to disconnect the line",
  "message.smog.ride_link": "First connect two anchors with freight rope",
  "message.smog.ride_busy": "Ropeway busy with cargo or another rider",
  "message.smog.ride_clear": "Riding needs three clear blocks below the line and landing space at both anchors",
  "message.smog.ride_started": "Look along the rope to choose direction. Sneak to exit",
  "message.smog.ride_blocked": "Ropeway paused: clear the path and load both ends",
  "message.smog.ride_landing": "Landing blocked; restore a platform. Sneak: emergency exit",
  "message.smog.rope_help": "Use with items to load, empty-handed to send, or with a stick to ride",
  "gui.smog.copper.status_17": "Ropeway occupied by a rider",
  "entity.smog.ropeway_ride": "Ropeway Passenger Hoist",
  "item.smog.rope_anchor.help_1": "Connect two anchors with freight rope: up to 24 blocks",
  "item.smog.rope_anchor.help_2": "Use with items to load; empty-handed to send",
  "item.smog.rope_anchor.help_3": "Use a stick on any rope section to ride where you look",
  "item.smog.rope_anchor.help_4": "Sneak to exit; leave clearance and safe landing platforms",
  "item.smog.loading_crane.help_1": "Use: load a batch; sneak-use: reverse direction",
  "item.smog.loading_crane.help_2": "Red controller marker: one rising redstone pulse per batch",
  "tool.smog.preview_use_saw": "Fells marked natural trees",
  "tool.smog.preview_use_sword": "Balanced melee blade",
  "tool.smog.preview_use_katana": "Faster attacks, lower damage",
  "tool.smog.preview_use_machete": "Slower attacks, higher damage",
  "tool.smog.preview_use_sickle": "Right-click: harvest and replant mature crops in 3×1×3",
  "itemGroup.smog.materials": "Smog: Materials",
  "message.smog.fluid_supply": "%s: %s / 8000 mB. Transfers through copper pipes.",
  "gui.smog.fluid.empty": "Empty reservoir",
  "gui.smog.fluid.rate": "Flow: up to 200 mB/s",
  "gui.smog.pump.prime": "Pump water",
  "gui.smog.pump.remaining": "Remaining job: %s",
  "message.smog.valve_powered": "Manual valve open; redstone blocks the flow",
  "advancements.smog.copper.bronze_parts.title": "More than ingots",
  "advancements.smog.copper.bronze_parts.description": "Make a bronze fitting and pipe section for your first drive.",
  "advancements.smog.copper.drive.title": "Passing on motion",
  "advancements.smog.copper.drive.description": "Build a bronze water drive. Let flowing water do the work.",
  "advancements.smog.copper.commissioning.title": "Commissioning",
  "advancements.smog.copper.commissioning.description": "Fit the drive in a press frame or beside bellows, supply flowing water and use the drive empty-handed.",
  "advancements.smog.copper.mechanized.title": "The workshop comes alive",
  "advancements.smog.copper.mechanized.description": "Complete two automatic press batches or two melts using mechanical bellows. The Copper Workshop is complete: bronze mechanization lies ahead.",
  "message.smog.finale.attach": "Fit the drive into an assembled press or place it beside bellows.",
  "message.smog.finale.flow": "The drive needs moving water at its working face. Still water cannot turn it.",
  "message.smog.finale.bellows": "Connect the bellows through an open air nozzle to an assembled smeltery air port.",
  "message.smog.finale.press": "Commissioning started: load materials and clear the output. Complete two automatic batches.",
  "message.smog.finale.smelt": "Commissioning started: fuel the smeltery and complete two melts with automatic bellows.",
  "message.smog.finale.progress": "Automatic operations: %s / %s. Use to check; sneak-use to cancel commissioning.",
  "message.smog.finale.complete": "Copper Workshop complete. The water now works alongside you.",
  "message.smog.finale.cancelled": "Commissioning cancelled. Equipment continues normal operation.",
  "message.smog.finale.already": "You have completed the Copper Workshop. The drive works without another commissioning.",
  "message.smog.finale.busy": "Another player is commissioning this device.",
  "jei.smog.guide.finale": "Finish the Copper Workshop by putting a drive to work. Alloy copper and tin at 3:1, make bronze fittings and pipe sections, and build the drive. Replace a press frame block or place it beside bellows. Supply moving water at the working face. Complete two press batches or two real melts with connected bellows. Use the drive empty-handed to start commissioning and check progress. Manual work and idle bellows do not count. Losing water pauses work without consuming inputs or clearing progress. Optional branches and advancements never lock recipes.",
  "item.smog.bronze_drive.help_1": "Fit into a press frame or place beside bellows.",
  "item.smog.bronze_drive.help_2": "Moving water turns the drive. Use empty-handed to commission.",
  "gui.smog.manual.state_0": "Assemble the workshop",
  "gui.smog.manual.detail_0": "Assemble the workshop with the configurator.",
  "gui.smog.manual.state_1": "Equipment disabled",
  "gui.smog.manual.detail_1": "Enable the equipment.",
  "gui.smog.manual.state_2": "Drive stopped",
  "gui.smog.manual.detail_2": "Check the drive and moving water. Queue and inputs are preserved.",
  "gui.smog.manual.state_3": "Add materials",
  "gui.smog.manual.detail_3": "Put suitable materials in the input slot.",
  "gui.smog.manual.state_4": "Unsupported input",
  "gui.smog.manual.detail_4": "This material does not match the selected process.",
  "gui.smog.manual.state_5": "Not enough input",
  "gui.smog.manual.detail_5": "Required per batch: %s; loaded: %s.",
  "gui.smog.manual.state_6": "Not enough water",
  "gui.smog.manual.detail_6": "Water required: %s; stored: %s.",
  "gui.smog.manual.state_7": "No room for oil",
  "gui.smog.manual.detail_7": "Oil space required: %s; available: %s.",
  "gui.smog.manual.state_8": "Output blocked",
  "gui.smog.manual.detail_8": "Clear the output slot or collect excess items.",
  "gui.smog.manual.state_9": "Waiting to start",
  "gui.smog.manual.detail_9": "Start with the button or an empty-hand use.",
  "gui.smog.manual.state_10": "Processing batch",
  "gui.smog.manual.detail_10": "Processing a finite manual order.",
  "gui.smog.manual.state_11": "Mechanically driven",
  "gui.smog.manual.detail_11": "The drive is doing the work automatically.",
  "gui.smog.manual.state_12": "Starting next batch",
  "gui.smog.manual.detail_12": "The next batch will start automatically.",
  "gui.smog.manual.state_13": "Collect the output",
  "gui.smog.manual.detail_13": "Output contains %2$s items.",
  "gui.smog.manual.start": "Start",
  "gui.smog.casting.filled": "Filled: %s",
  "gui.smog.casting.required": "Required: %s",
  "gui.smog.utility.water_0": "Equipment disabled",
  "gui.smog.utility.water_1": "Source water required below",
  "gui.smog.utility.water_2": "Prime the pump",
  "gui.smog.utility.water_3": "Pumping water",
  "gui.smog.utility.water_4": "Tank full",
  "gui.smog.utility.water_5": "Reservoir empty",
  "gui.smog.utility.water_6": "Fluid available for supply",
  "gui.smog.utility.rope_0": "Ropeway disabled",
  "gui.smog.utility.rope_1": "Link the anchors",
  "gui.smog.utility.rope_2": "Route chunk not loaded",
  "gui.smog.utility.rope_3": "Anchor link broken",
  "gui.smog.utility.rope_4": "Cargo route blocked",
  "gui.smog.utility.rope_5": "Passenger on the line",
  "gui.smog.utility.rope_6": "Cargo in transit",
  "gui.smog.utility.rope_7": "Receiver output blocked",
  "gui.smog.utility.rope_8": "Collect delivered cargo",
  "gui.smog.utility.rope_9": "Add cargo",
  "gui.smog.utility.rope_10": "Waiting for dispatch",
  "gui.smog.utility.rope_11": "Dispatch queued",
  "gui.smog.utility.crane_0": "Assemble the crane",
  "gui.smog.utility.crane_1": "Crane disabled",
  "gui.smog.utility.crane_2": "Waiting for an order",
  "gui.smog.utility.crane_3": "Order queued",
  "gui.smog.utility.crane_4": "Moving cargo",
  "gui.smog.utility.crane_5": "Cargo source missing",
  "gui.smog.utility.crane_6": "Source empty",
  "gui.smog.utility.crane_7": "Cargo receiver missing",
  "gui.smog.utility.crane_8": "Crane route blocked",
  "gui.smog.utility.crane_9": "Route chunk not loaded",
  "gui.smog.utility.crane_10": "Receiver rejects cargo",
  "gui.smog.utility.cargo": "Suspended cargo: %s items",
  "gui.smog.utility.crane_hint": "Use the controller to order a transfer",
  "gui.smog.utility.rope_hint": "Use the anchor empty-handed to dispatch",
  "tool.smog.tip.trait.cultivation": "Cultivation",
  "tool.smog.trait.cultivation": "Tilling hydrates the soil.",
  "tool.smog.tip.trait.impact": "Impact",
  "tool.smog.trait.impact": "Pushes creatures away; cooldown: %2$s s.",
  "tool.smog.tip.trait.magnetic": "Magnetic",
  "tool.smog.trait.magnetic": "Attracts nearby free drops for 2 s after mining.",
  "tool.smog.tip.trait.quenching": "Quenching",
  "tool.smog.trait.quenching": "Extinguishes creatures; cooldown: %2$s s.",
  "tool.smog.tip.trait.sapping": "Sapping",
  "tool.smog.trait.sapping": "Weakness: %1$s s; cooldown: %2$s s.",
  "tool.smog.tip.trait.breathing": "Breathing",
  "tool.smog.trait.breathing": "Suitable underwater mining restores 4 air.",
  "tool.smog.tip.trait.insulation": "Insulation",
  "tool.smog.trait.insulation": "Reduces freezing while held."
}
```

### ru_ru

```json
{
  "jei.smog.example_label": "Пример изготовления",
  "jei.smog.example_materials": "Материалы показаны для примера. Детали могут быть из других совместимых материалов; точный результат виден на наковальне.",
  "jei.smog.copper.finishing": "Доводка отливки молотом. Та же операция применяется к головкам из других материалов.",
  "jei.smog.copper.conversion": "Получение головки из ванильного инструмента с сохранением износа, имени и чар.",
  "tool.smog.tip.durability": "Прочность",
  "tool.smog.tip.material": "Материал",
  "tool.smog.tip.traits": "Свойства",
  "tool.smog.tip.properties": "Характеристики",
  "tool.smog.tip.damage": "Урон",
  "tool.smog.tip.attack_speed": "Атак в секунду",
  "tool.smog.tip.mining_speed": "Скорость добычи",
  "tool.smog.tip.head_damage": "Урон головки",
  "tool.smog.tip.speed_factor": "Множитель скорости",
  "tool.smog.tip.life_factor": "Множитель прочности",
  "tool.smog.tip.tier": "Уровень добычи",
  "tool.smog.tip.repair": "Материал ремонта",
  "tool.smog.tip.trait_details": "Эффекты материалов",
  "tool.smog.tip.construction": "Состав",
  "tool.smog.tip.part.head": "Головка",
  "tool.smog.tip.part.handle": "Стержень",
  "tool.smog.tip.part.binding": "Крепление",
  "tool.smog.tip.part.wrap": "Обмотка",
  "tool.smog.tip.part_type": "Часть",
  "tool.smog.tip.content": "Содержание металла",
  "tool.smog.tip.keys": "Ctrl · характеристики    Alt · состав",
  "tool.smog.tip.trait.work_hardening": "Наклёп",
  "tool.smog.tip.trait.staggering": "Ошеломление",
  "tool.smog.tip.trait.marking": "Метка",
  "tool.smog.tip.trait.grip": "Цепкий хват",
  "tool.smog.tip.use.pickaxe": "Добыча камня и руд",
  "tool.smog.tip.use.axe": "Рубка древесины",
  "tool.smog.tip.use.saw": "Валка дерева · Shift для одного блока",
  "tool.smog.tip.use.shovel": "Земляные работы",
  "tool.smog.tip.use.hammer": "Добыча 3×3×1 · работа в мастерской",
  "tool.smog.tip.use.hoe": "Вспашка земли",
  "tool.smog.tip.use.sword": "Сбалансированный клинок",
  "tool.smog.tip.use.katana": "Быстрый клинок · меньше урон",
  "tool.smog.tip.use.machete": "Тяжёлый клинок · больше урон",
  "tool.smog.tip.use.sickle": "ПКМ · сбор и посадка 3×1×3",
  "gui.smog.pottery.input": "Материал",
  "gui.smog.pottery.choose": "Форма",
  "gui.smog.pottery.output": "Итог",
  "gui.smog.pottery.cost": "%s × %s",
  "gui.smog.pottery.take": "Положите материал, выберите форму и заберите результат. Материал расходуется только при получении изделия.",
  "gui.smog.amount.blocks.one": "%s блок",
  "gui.smog.amount.blocks.ending_one": "%s блок",
  "gui.smog.amount.blocks.few": "%s блока",
  "gui.smog.amount.blocks.many": "%s блоков",
  "gui.smog.amount.ingots.one": "%s слиток",
  "gui.smog.amount.ingots.ending_one": "%s слиток",
  "gui.smog.amount.ingots.few": "%s слитка",
  "gui.smog.amount.ingots.many": "%s слитков",
  "gui.smog.amount.nuggets.one": "%s самородок",
  "gui.smog.amount.nuggets.ending_one": "%s самородок",
  "gui.smog.amount.nuggets.few": "%s самородка",
  "gui.smog.amount.nuggets.many": "%s самородков",
  "gui.smog.amount.nuggets.fraction": "%s самородка",
  "itemGroup.smog.blocks": "Smog: Блоки",
  "itemGroup.smog.mechanisms": "Smog: Механизмы",
  "itemGroup.smog.controllers": "Smog: Контроллеры",
  "itemGroup.smog.tool_parts": "Smog: Части инструментов",
  "itemGroup.smog.tools": "Smog: Инструменты",
  "tool.smog.trait.work_hardening": "Непрерывная добыча: до +%1$s%% скорости; спад через %2$s с.",
  "tool.smog.trait.staggering": "Сбивание темпа: удар замедляет моба на %s с, не чаще раза в %s с.",
  "tool.smog.trait.marking": "Метка: удар подсвечивает моба на %s с, не чаще раза в %s с.",
  "tool.smog.trait.grip": "Каждое %s-е действие не тратит прочность.",
  "tool.smog.preview_queue": "В работе: осталось %s",
  "tool.smog.preview_head_tier": "%s — уровень добычи: %s",
  "tool.smog.preview_purpose": "Назначение: %s",
  "tool.smog.preview_use_pickaxe": "Добыча камня и руд",
  "tool.smog.preview_use_axe": "Рубка дерева; снятие коры",
  "tool.smog.preview_use_shovel": "Копание земли, песка и гравия; создание тропинок",
  "tool.smog.preview_use_hoe": "Сбор растений; вспашка земли",
  "tool.smog.preview_use_hammer": "Добыча 3×3×1; сборка и доводка в мастерской",
  "tool.smog.preview_baseline": "Головки → инструмент (несколько головок: средний ресурс)",
  "tool.smog.preview_life": "Полный ресурс: %s → %s (%s)",
  "tool.smog.preview_speed": "Максимальная скорость добычи: %s → %s (%s)",
  "tool.smog.preview_speed_family": "%s: %s → %s (%s)",
  "tool.smog.preview_conditions": "Базовые значения; чары и ситуативные свойства действуют при работе.",
  "gui.smog.casting.core_slot": "Необязательная керамическая оснастка: трубная форма → обычный стержень; оснастка расходуется.",
  "gui.smog.casting.mold_slot": "Литейная форма или образец для металлической оснастки",
  "message.smog.assembly_fixed": "На %s, %s, %s нужна деталь: %s.",
  "tag.item.smog.copper_masonry": "Литейная кладка",
  "tag.item.smog.fiber_sources": "Растительное сырьё для волокна",
  "tag.item.smog.hearth_floor": "Блоки дна плавильни",
  "tag.item.smog.nonmetal_deposits": "Залежи неметаллов",
  "tag.item.smog.nonmetal_materials": "Неметаллические материалы",
  "tag.item.smog.oil_seeds": "Масличные семена",
  "tag.item.smog.sulfur_sources": "Источники серы",
  "tag.item.c.unfired_ceramics": "Необожжённая керамика",
  "itemGroup.smog.copper_age": "Smog: Медная мастерская",
  "gui.smog.copper.previous": "Назад",
  "gui.smog.copper.next": "Далее",
  "gui.smog.copper.craft": "Партия",
  "gui.smog.copper.alloy": "3Cu + Sn",
  "gui.smog.copper.heat": "Нагрев: %s / %s",
  "gui.smog.copper.metal": "Металл: %s / %s слитков",
  "gui.smog.copper.selection": "Выбор: %s",
  "gui.smog.copper.status_0": "Ожидание операции",
  "gui.smog.copper.status_1": "Соберите настройщиком",
  "gui.smog.copper.status_2": "Нужно сырьё",
  "gui.smog.copper.status_3": "Нужно топливо",
  "gui.smog.copper.status_4": "Идёт нагрев",
  "gui.smog.copper.status_5": "Нет места",
  "gui.smog.copper.status_6": "Нужна подходящая форма",
  "gui.smog.copper.status_7": "Литьё / охлаждение",
  "gui.smog.copper.status_8": "Не хватает расплава",
  "conductor.smog.bronze": "Бронза",
  "jei.smog.copper.title": "Медная мастерская",
  "jei.smog.copper.pottery": "Выберите изделие и сделайте партию.",
  "jei.smog.copper.casting": "Расплав в очаге, многоразовая форма.",
  "jei.smog.copper.alloy": "Cu и Sn вместе в расплаве: автосплав 3:1.",
  "message.smog.molten_status": "Cu %s | Sn %s | Бронза %s слитков | Нагрев %s",
  "message.smog.mold_selected": "Выбрано: %s",
  "jei.smog.guide.hearth": "Полая плавильня от 3 до 11 блоков по каждой стороне: сплошное дно, стены, пустая камера. Контроллер и порты — в плоскостях, не на рёбрах или углах. Топка или топливный бак заменяет часть стены/дна. Собери настройщиком обычным ПКМ; Shift + ПКМ только поворачивает. ПКМ пустой рукой по контроллеру открывает GUI. Число параллельных слотов зависит от объёма камеры, максимум 81; стрелки листают их по девять. Отдельные полосы показывают прогресс. Слои в центре — настоящий общий резервуар, слева выбранный расплав отмечен светлой чертой: ЛКМ выбирает его для всех кранов. Количество, рецепт и нагрев видны по наведению. Уже накопленный расплав не затвердевает при остановке топлива. Сплавление автоматически работает при подходящем топливе и нагреве. Металл можно забрасывать в камеру и подавать воронками. При перестройке содержимое сохраняется; лишние слоты доступны для возврата, новые задания в них не стартуют.",
  "jei.smog.guide.kiln": "Закрытая оболочка от 3 до 5 × от 3 до 5, высота от 3 до 5. Внутренняя камера пустая. Контроллер на передней стене над полом, одна топка Smog в полу и одна-две вытяжки в крыше. Стены кирпичные или огнеупорные. Соберите настройщиком. Загрузите сырую керамику и топливо рукой; ПКМ пустой рукой забирает результат. Камера определяет партию от 8 до 64 изделий. Обычный обжиг в ванильной печи также доступен.",
  "jei.smog.guide.casting": "Кран устанавливается на жидкостный порт и открывается ПКМ. Выбери расплав в интерфейсе плавильни: стол не подменяет выбор другим доступным металлом. Поставь форму ПКМ на стол, подведи кран сверху напрямую или через желоба. Металл постепенно переходит в собственный резервуар формы; после полной заливки отливка отдельно остывает. До завершения форма закреплена. Закрытие крана, смена выбранного металла или остановка печи не теряют частичную заливку. Нельзя доливать другой металл. При разрушении заливка возвращается в канистре. Готовую отливку забери пустой рукой или воронкой. Для полного блока используй глубокую литейную ёмкость: 9 слитков без формы.",
  "jei.smog.guide.pottery": "Shift+ПКМ пустой рукой выбирает изделие: оно видно на столе, название над хотбаром. ПКМ глиной или печной массой загружает материал и делает партию до восьми штук. ПКМ пустой рукой забирает результат. Сырые формы и тигли обжигаются в обычной печи или печи обжига. Печная масса: четыре глиняных шарика и песок. Shift+ПКМ с предметом открывает дополнительный интерфейс.",
  "jei.smog.guide.bellows": "Воздушный порт ставится в стенке. Снаружи прикрепите к нему воздушную насадку, откройте её ПКМ и поставьте рядом меха. ПКМ по мехам даёт нагнетание для партии до восьми слитков. Поворот — только Shift+ПКМ настройщиком.",
  "jei.smog.guide.crucible": "Отдельное керамическое изделие. В литейный очаг больше не устанавливается: камера очага должна быть полностью пустой. Вместимость очага определяется размерами свободного пространства, а не тиглями.",
  "jei.smog.guide.firebox": "Функциональная топка Smog в полу или стене комплекса, одна на установку. Это не ванильная печь с отдельным инвентарём. ПКМ топливом по собранной установке загружает общее топливо. Воронка сбоку контроллера также подаёт топливо.",
  "jei.smog.guide.spout": "Жидкостный порт — блок стенки, он сам не разливает металл. Прикрепите снаружи литейный краник и откройте его ПКМ. Расплав из краника льётся вниз: под ним нужен стол или желоб. Насадка не является частью стенки. Порт сохраняет подключение внешних труб и контейнеров. Поворот — только Shift+ПКМ настройщиком.",
  "jei.smog.guide.lining": "Огнеупорные кирпичи делаются из печной массы и обжигаются, четыре кирпича дают блок кладки. Кладка заменяет обычные кирпичи и части пола. Чем больше футеровки, тем медленнее остывает установка. Единая кирпичная текстура сохраняет 16 пикселей на блок.",
  "jei.smog.guide.molds": "Керамические формы сохраняются как стартовый вариант. Песчаную форму отпечатай на медной наковальне: формовочная смесь в первый слот, подходящая готовая головка, инструмент или простая металлическая деталь во второй; удар молотом. Образец остаётся, расходуется только смесь. Песчаная форма одноразовая. Для многоразовой металлической формы положи образец на литейный стол и залей 2 слитка золота; после охлаждения получишь оснастку, образец сохраняется. Повторная переплавка оснастки возвращает те же 2 слитка золота. Формы зависят от геометрии детали, а не её материала.",
  "jei.smog.guide.configurator": "ПКМ по контроллеру собирает комплекс по его правилам. Shift+ПКМ разбирает и поворачивает контроллер. По части собранной установки работает тот же контроллер. До сборки Shift+ПКМ по сливу, вводу или желобу поворачивает отдельную деталь. Сообщения отображаются над хотбаром.",
  "jei.smog.guide.residue": "Сохраняет точное количество металла, оставшееся после работы труб или разрушения бака. Переплавьте остаток обратно в очаге: материал не округляется и не теряется.",
  "jei.smog.guide.parts": "Медные и бронзовые детали получаются литьём. Пластина содержит один слиток, фитинг — четверть, трубный элемент — половину. Переплавка возвращает ровно исходное количество металла. Эти детали сохраняются для следующих устройств медной эпохи.",
  "jei.smog.guide.bronze": "В нагретом очаге 3 части расплавленной меди и 1 часть олова автоматически дают 4 части расплавленной бронзы. Кнопка смешивания не нужна. Излишки исходного металла остаются в баке. Для отливки используются те же многоразовые формы.",
  "jei.smog.guide.molten": "Плавильня хранит расплавы раздельно. Доступные металлы и сплавы зависят от температуры и огнеупорности корпуса. Выбранный расплав подаётся через жидкостный порт, литейный кран и желоба. Один слиток равен девятой части ведра. Совместимые канистры, ёмкости и жидкостные системы других модов работают через порты. Медные хозяйственные трубы предназначены для воды и масла. Принимаемые материалы определяются общими тегами металлов.",
  "jei.smog.structure.copper_hearth": "Полый корпус от 3 до 11 по каждой оси. От 1 до 4 топок/топливных баков. Порты необязательны; вставки вне рёбер и углов. Сборка — ПКМ настройщиком.",
  "jei.smog.structure.ceramic_kiln": "Закрытая оболочка, пустая камера. Топка в полу, вытяжка в крыше. ПКМ настройщиком. Подробности — страница информации контроллера.",
  "jei.smog.copper.firing": "Обжиг партии: от 8 до 64 изделий по камере.",
  "jei.smog.copper.melting": "Нагрейте очаг; расплав сливается через носик.",
  "message.smog.bellows": "Меха запущены: помощь для партии до 8 слитков.",
  "message.smog.bellows_disconnected": "Нужны открытая воздушная насадка и порт собранного очага.",
  "message.smog.valve_open": "Кран открыт",
  "message.smog.valve_closed": "Кран закрыт",
  "jei.smog.size_range": "Размеры: от %s до %s блоков",
  "message.smog.controller_missing": "Нужен контроллер литейного очага или печи обжига в стене конструкции",
  "message.smog.manual_workshop": "Жидкость: %s мВ | Заказано партий: %s. Загрузите сырьё и нажмите пустой рукой.",
  "message.smog.manual_queued": "Заказано партий: %s. Повторный клик не ускоряет работу.",
  "jei.smog.copper.washing": "Обогащение первичной руды и промывка гравия, красного песка, грязи. Количество воды и результат указаны в рецепте.",
  "jei.smog.copper.pressing": "8 семян → 250 мВ масла + жмых. Запуск пустой рукой; масло забирается бутылкой или трубой.",
  "jei.smog.guide.washing": "Соберите лоток 3 × 1 × 2 настройщиком и подайте воду ведром или трубой. Восемь единиц необработанной меди, олова, свинца, железа или золота дают пять концентратов по два слитка. Из гравия извлекается кремень, красный песок отмывается до обычного, из грязи выделяется глина. Запустите конечную очередь пустой рукой или кнопкой в интерфейсе. Готовые металлы и концентраты повторно не промываются.",
  "jei.smog.guide.pressing": "Соберите пресс 3 × 2 × 2 по схеме. Загрузите семена и нажмите пустой рукой. Один заказ обрабатывает загруженные партии без повторных ударов. Масло забирается бутылкой, ведром или трубой; жмых идёт в компост. Металлические пластины здесь не производятся.",
  "message.smog.assembly_edge": "Компонент на ребре или в углу: %s, %s, %s. Здесь нужна кладка.",
  "message.smog.assembly_block": "Проверьте блок на %s, %s, %s: оболочка должна быть целой, камера — пустой.",
  "message.smog.assembly_role": "Узел «%s»: установлено %s, требуется от %s до %s.",
  "message.smog.assembly_overlap": "Блок уже принадлежит другой конструкции.",
  "message.smog.assembly_check": "Проверьте схему JEI, положение контроллера и обязательные узлы.",
  "role.smog.fuel": "Топка",
  "role.smog.spout": "Жидкостный порт",
  "role.smog.air": "Воздушный порт",
  "role.smog.vent": "Вытяжка",
  "role.smog.lining": "Футеровка",
  "message.smog.pump_ready": "Помпа запущена: до 4 вёдер за одно действие.",
  "message.smog.pump_missing_water": "Поместите помпу над источником воды.",
  "message.smog.water_supply": "Вода: %s / 8000 мВ. Передача соседним установкам.",
  "message.smog.lubricated": "Пресс смазан: ускорение следующих 8 партий.",
  "jei.smog.guide.water": "Медный резервуар вмещает 8 вёдер воды или растительного масла без смешивания. Ручной насос над источником воды после запуска перекачивает до 4 вёдер. Соедините насос, резервуар и потребителей медными трубами. Пресс отдаёт масло в подключённую линию; соседние резервуары выравнивают уровень. Скорость подачи — до 200 мВ/с на установку. Запорный кран пропускает жидкость вдоль своей оси, разливочный вывод принимает её сзади и подаёт вниз. ПКМ пустой рукой открывает кран; сигнал редстоуна перекрывает проход. Shift с настройщиком поворачивает соединения. Длина маршрута — до 16 блоков, сеть — до 256 узлов в загруженных чанках. Расплавы и лаву медная линия не принимает.",
  "advancements.smog.copper.foundry.title": "Сердце мастерской",
  "advancements.smog.copper.foundry.description": "Изготовьте контроллер полой плавильни. Соберите оболочку по схеме JEI.",
  "advancements.smog.copper.kiln.title": "Обжиг партиями",
  "advancements.smog.copper.kiln.description": "Подготовьте контроллер печи для серийного обжига керамики.",
  "advancements.smog.copper.bench.title": "Всё по частям",
  "advancements.smog.copper.bench.description": "Подготовьте медную наковальню.",
  "advancements.smog.copper.tools.title": "Свой инструмент",
  "advancements.smog.copper.tools.description": "Получите составную кирку: головка, рукоять и крепление выбираются отдельно.",
  "advancements.smog.copper.hollow.title": "Основа инструмента",
  "advancements.smog.copper.hollow.description": "Получите медный стержень для составного инструмента.",
  "advancements.smog.copper.water.title": "Вода на месте",
  "advancements.smog.copper.water.description": "Изготовьте ручную помпу для конечных заказов воды.",
  "advancements.smog.copper.fiber.title": "Без охоты",
  "advancements.smog.copper.fiber.description": "Получите растительное волокно с волокнистой корзиной пресса.",
  "advancements.smog.copper.cart.title": "На стройку",
  "advancements.smog.copper.cart.description": "Изготовьте грузовую тележку на 54 слота.",
  "advancements.smog.copper.crane.title": "Перегрузочная площадка",
  "advancements.smog.copper.crane.description": "Подготовьте контроллер крана для переноса партий между двумя позициями.",
  "advancements.smog.copper.ropeway.title": "Между берегами",
  "advancements.smog.copper.ropeway.description": "Изготовьте анкер переправы. Два анкера соединяются грузовым канатом.",
  "advancements.smog.copper.root.title": "Медная мастерская",
  "advancements.smog.copper.root.description": "Начните с глины: формы, литьё, инструменты и хозяйство.",
  "advancements.smog.copper.pottery.title": "Повторяемая форма",
  "advancements.smog.copper.pottery.description": "Обожгите многоразовую слитковую форму.",
  "advancements.smog.copper.casting.title": "Своя оснастка",
  "advancements.smog.copper.casting.description": "Получите медный фитинг для оборудования мастерской.",
  "advancements.smog.copper.washing.title": "Подготовить руду",
  "advancements.smog.copper.washing.description": "Промойте 8 единиц необработанной меди: 5 концентрата дадут 10 слитков.",
  "advancements.smog.copper.pressing.title": "Масло для мастерской",
  "advancements.smog.copper.pressing.description": "Получите растительное масло. Пресс также даёт полезный жмых.",
  "advancements.smog.copper.bronze.title": "Первый сплав",
  "advancements.smog.copper.bronze.description": "Соедините медь и олово в отношении 3:1. Следующий шаг — применить бронзу в мастерской.",
  "tool.smog.unconfigured": "Сначала соберите инструмент из частей на верстаке.",
  "tool.smog.broken": "Головка изношена. Сборка сохранена для ремонта.",
  "tool.smog.durability": "Ресурс: %s / %s",
  "tool.smog.part_head": "Головка: %s",
  "tool.smog.part_handle": "Рукоять: %s",
  "tool.smog.part_binding": "Крепление: %s",
  "tool.smog.part_wrap": "Обмотка: %s",
  "tool.smog.working": "Работа идёт. Части временно закреплены.",
  "tool.smog.mount_hammer": "Shift + ПКМ по столу со свободным креплением — установить этот молот для ремонта.",
  "tool.smog.assembly_help": "Положите головку, рукоять и крепление. Если есть металл, нужен рабочий составной молот; без металла — пустая рука или кнопка изготовления.",
  "tool.smog.preview": "Результат: %s | Прочность: %s. Без металла — пустая рука; с металлом — составной молот.",
  "jei.smog.copper.tools": "Головка, рукоять, крепление и необязательная обмотка. Рабочий составной молот нужен только при наличии металлических деталей.",
  "jei.smog.guide.tools": "Отлейте головку, доведите рабочим молотом на пустом столе. Затем внесите рукоять и крепление, обмотка необязательна. Запуск одним ПКМ молотом. Shift+молот разбирает помещённый инструмент, сохраняя износ головки и её чары. Новая головка получает только собственные чары. Слиток соответствующего металла чинит четверть ресурса. Shift+ПКМ предметом открывает необязательный интерфейс.",
  "tool.smog.working_short": "Идёт сборка",
  "tool.smog.ready_short": "Без металла — пустая рука; с металлом — молот",
  "tool.smog.missing_parts": "Добавьте детали",
  "tool.smog.tier": "Уровень головки: %s",
  "tool.smog.speed": "Скорость работы: %s",
  "jei.smog.copper.fiber_pressing": "8 растительного сырья → 4 волокна. Требуется волокнистая корзина.",
  "jei.smog.guide.drive": "Замените любой верхний блок рамы пресса бронзовым приводом. Перед лопастями должна течь вода: неподвижный источник сам по себе привод не вращает. Пресс сам ставит полные партии в очередь, пока есть поток, сырьё и место. Без потока остаётся ручной запуск. Тот же привод рядом с мехами периодически запускает их. Поворот — Shift+ПКМ настройщиком. Это механическая передача, FE не вырабатывается.",
  "tool.smog.profile_head": "Ресурс головки: %s | Скорость: %s",
  "tool.smog.profile_support": "Скорость ×%s | Ресурс ×%s",
  "tool.smog.finish_cast": "Доведите отливку рабочим молотом.",
  "tool.smog.selected_schema": "Схема: %s. Shift+ПКМ по пустому столу — следующая.",
  "jei.smog.copper.shaping": "Прямая обработка слитков дороже литья. Выберите схему и ударьте рабочим молотом.",
  "message.smog.cart_busy": "Тележку уже тянет другой игрок",
  "message.smog.cart_released": "Тележка оставлена на месте",
  "message.smog.cart_pulling": "Тянем тележку · Shift + ПКМ — отпустить",
  "message.smog.crane_forward": "Партия: передняя площадка → задняя · Shift + ПКМ — сменить направление",
  "message.smog.crane_reverse": "Партия: задняя площадка → передняя · Shift + ПКМ — сменить направление",
  "conductor.smog.lead": "Свинец",
  "conductor.smog.zinc": "Цинк",
  "conductor.smog.aluminum": "Алюминий",
  "conductor.smog.silver": "Серебро",
  "conductor.smog.gold": "Золото",
  "conductor.smog.brass": "Латунь",
  "role.smog.closed_insert": "Закрытая тигельная вставка",
  "gui.smog.copper.status_9": "Нужна совместимая вставка или допустимый нагрев",
  "message.smog.rope_first": "Первый анкер выбран · Нажми по второму анкеру",
  "message.smog.rope_linked": "Соединено: предметы — загрузить, пустая рука — отправить, палка — ехать",
  "message.smog.rope_invalid": "Нужен свободный пролёт до 24 блоков и два незанятых анкера",
  "message.smog.rope_unlinked": "Переправа отключена; груз сохранён в своём анкере",
  "message.smog.rope_queued": "Заказана перевозка одной партии",
  "jei.smog.guide.handcart": "54 слота для обычных предметов. ПКМ открывает груз, Shift + ПКМ берёт тележку за рукоять или отпускает. Тяни её по подготовленной дороге; при удалении игрока дальше 6 блоков тележка остаётся на месте. Содержимое сохраняется и выпадает при разрушении.",
  "jei.smog.guide.crane": "Кран 5 × 5 × 5: проход 3 × 4. Грузовые точки на 4 блока ниже контроллера, на 1 и 3 блока вглубь; отмечены A и B. ПКМ пустой рукой запускает партию до 64 предметов; Shift + ПКМ меняет направление. Подвес показывает весь цикл. Груз при остановке сохраняется. Бронзовый привод ускоряет и повторяет погрузку. Импульс редстоуна на контроллере запускает одну партию; вход отмечен красной меткой на передней балке.",
  "jei.smog.guide.ropeway": "Два анкера с лебёдками и грузовой канат образуют переправу до 24 блоков. Канатом нажми по первому и второму анкеру; пролёт должен быть свободен и загружен. Загрузи обычные предметы и нажми пустой рукой: каретка перевезёт до стака. ПКМ забирает доставленное, Shift + пустая рука возвращает загрузку. Воронки доступны. Shift + канат отсоединяет линию без потери груза.",
  "jei.smog.guide.closed_insert": "Поставь вставку в плоскую стенку очага и собери структуру настройщиком. Она позволяет работать с цинком при высоком нагреве и получать латунь из трёх частей меди и одной цинка. В открытой конфигурации цинк переплавляется только в низком диапазоне нагрева. Готовый алюминий требует огнеупорной футеровки; боксит этим способом не восстанавливается.",
  "jei.smog.copper.brass_alloy": "Закрытая вставка · Cu:Zn = 3:1",
  "tool.smog.shaping_cost": "Материал: %s · Выход: %s",
  "conductor.smog.copper": "Медь",
  "conductor.smog.tin": "Олово",
  "conductor.smog.iron": "Железо",
  "conductor.smog.nickel": "Никель",
  "conductor.smog.manganese": "Марганец",
  "conductor.smog.chromium": "Хром",
  "conductor.smog.titanium": "Титан",
  "conductor.smog.tungsten": "Вольфрам",
  "conductor.smog.molybdenum": "Молибден",
  "conductor.smog.uranium": "Уран",
  "conductor.smog.lithium": "Литий",
  "conductor.smog.steel": "Сталь",
  "conductor.smog.cast_iron": "Чугун",
  "conductor.smog.wrought_iron": "Кричное железо",
  "conductor.smog.stainless_steel": "Нержавеющая сталь",
  "conductor.smog.tool_steel": "Инструментальная сталь",
  "conductor.smog.duralumin": "Дюралюминий",
  "conductor.smog.cupronickel": "Мельхиор",
  "conductor.smog.constantan": "Константан",
  "conductor.smog.invar": "Инвар",
  "conductor.smog.electrum": "Электрум",
  "conductor.smog.nichrome": "Нихром",
  "conductor.smog.solder": "Припой",
  "conductor.smog.tungsten_steel": "Вольфрамовая сталь",
  "conductor.smog.titanium_alloy": "Титановый сплав",
  "conductor.smog.nickel_superalloy": "Никелевый жаропрочный сплав",
  "conductor.smog.aluminum_bronze": "Алюминиевая бронза",
  "conductor.smog.netherite": "Незерит",
  "gui.smog.smeltery.inputs": "Слоты: %s",
  "gui.smog.smeltery.quantity": "Объём: %s слитка",
  "gui.smog.smeltery.select": "ЛКМ: выбрать нижний слой · ПКМ с ёмкостью: наполнить",
  "gui.smog.amount.blocks": "%s блоков",
  "gui.smog.amount.ingots": "%s слитков",
  "gui.smog.amount.nuggets": "%s самородков",
  "gui.smog.smeltery.fuel": "Запас: %s",
  "gui.smog.smeltery.temperature": "Нагрев: %s · Предел топлива: %s",
  "gui.smog.smeltery.fuel_cursor": "Ёмкость на курсоре: ЛКМ — залить, ПКМ — забрать",
  "gui.smog.smeltery.container_modes": "Режим ёмкостей: авто / опорожнение / наполнение",
  "gui.smog.smeltery.container_mode_0": "Авто: сначала опорожнить ёмкость, затем попробовать наполнить",
  "gui.smog.smeltery.container_mode_1": "Опорожнять ёмкости в общий резервуар",
  "gui.smog.smeltery.container_mode_2": "Наполнять ёмкости выбранным расплавом",
  "gui.smog.smeltery.solid_fuel": "Твёрдое топливо",
  "message.smog.fuel_tank": "Топливо в баке: %s / %s",
  "message.smog.fuel_powder": "Для всего запаса лавы нужно огненного порошка: %s",
  "message.smog.fuel_prepared": "Лава превращена в пылающее топливо без изменения объёма",
  "message.smog.casting_locked": "Заливка: %s / %s · Форма закреплена до готовности",
  "gui.smog.copper.status_10": "Отливка остывает",
  "jei.smog.copper.imprint": "Песчаная форма · Образец сохраняется",
  "jei.smog.copper.mold_making": "Золотая оснастка · Образец сохраняется",
  "jei.smog.copper.required_heat": "Нагрев не ниже %s",
  "jei.smog.guide.fuel_tank": "Физический бак вмещает 8 вёдер топлива и является функциональной вставкой в стене или дне плавильни. Заполняй лавой через ведро, канистру или Fabric-совместимые трубы. Топливо хранится здесь, отдельно от расплавов. Лава даёт нагрев до 1100; пылающее топливо — до 1600, но обычный корпус ограничен 1100, с достаточной огнеупорной футеровкой — 1350. Чтобы усилить лаву, нажми огненным порошком по баку: 2 порошка на каждое начатое ведро всего запаса. Топливо не создаёт второй бак или ведро. Большая камера и параллельная работа расходуют больше топлива. При разрушении остаток возвращается в канистре.",
  "jei.smog.guide.basin": "Глубокая ёмкость отливает металлический блок из 9 слитков расплава. Форма не нужна. Поставь под краном, выбери расплав в плавильне и открой кран. Заливка и охлаждение видны в мире. Можно подать металл трубой другого мода; принимается только совместимый металл текущей отливки.",
  "jei.smog.guide.canister": "Канистра на 8 вёдер одной жидкости; поддерживает стандартное FluidStorage.ITEM. Переливание через порты и баки в мире. При разрушении заполненных ёмкостей остатки возвращаются без округления.",
  "gui.smog.copper.status_11": "Нужно больше рабочих мест; предмет можно забрать",
  "gui.smog.smeltery.capacity": "Резервуар: %s / %s слитков",
  "role.smog.items": "Подача предметов",
  "entity.smog.handcart": "Грузовая тележка",
  "gui.smog.smeltery.heat": "Нагрев: %s / %s °C; предел топлива: %s °C",
  "jei.smog.smeltery_fuels": "Топливо плавильни",
  "jei.smog.fuel_heat": "Предел: %s °C · скорость ×%s",
  "jei.smog.fuel_preparation": "Подготовка в топливном баке",
  "message.smog.crane_started": "Погрузка начата: %s → %s",
  "message.smog.crane_busy": "Кран работает; дождись завершения партии",
  "message.smog.crane_source": "Нет сундука или припаркованной тележки в точке %s",
  "message.smog.crane_empty": "В точке %s нет подходящего груза",
  "message.smog.crane_destination": "Нет места для груза в точке %s",
  "message.smog.crane_blocked": "Путь подвеса перекрыт: %s",
  "message.smog.crane_unloaded": "Грузовая площадка не загружена",
  "message.smog.crane_direction": "Направление: %s → %s · ПКМ — погрузка",
  "message.smog.crane_wait": "Груз сохранён на подвесе; освободи место выгрузки",
  "message.smog.crane_complete": "Перегружено: %s предметов",
  "tool.smog.tier.wooden": "Дерево",
  "tool.smog.tier.wood": "Дерево",
  "tool.smog.tier.stone": "Камень",
  "tool.smog.tier.copper": "Медь",
  "tool.smog.tier.iron": "Железо",
  "tool.smog.tier.diamond": "Алмаз",
  "tool.smog.tier.netherite": "Незерит",
  "tool.smog.tier.gold": "Золото",
  "tool.smog.tier.golden": "Золото",
  "tool.smog.extra_parts": "Уберите лишние детали",
  "tool.smog.need_role": "Нужна деталь: %s",
  "tool.smog.rebuild_compare": "Ресурс до замены: %s; после: %s",
  "tool.smog.role.head": "головка",
  "tool.smog.role.handle": "рукоять",
  "tool.smog.role.binding": "крепление",
  "tool.smog.role.wrap": "обмотка",
  "tool.smog.role.clamp": "хомут",
  "tool.smog.role.pickaxe_head": "головка кирки",
  "tool.smog.role.axe_head": "головка топора",
  "tool.smog.role.shovel_head": "головка лопаты",
  "tool.smog.head_wear": "Осталось ресурса головки: %s / %s",
  "gui.smog.process.heat": "Рабочий нагрев: %s",
  "gui.smog.process.open_max": "В открытой камере: не выше %s",
  "gui.smog.copper.status_12": "Нагрев выше предела корпуса",
  "gui.smog.copper.status_13": "Нужна огнеупорная футеровка",
  "gui.smog.copper.status_14": "Нужна закрытая рабочая камера",
  "gui.smog.copper.status_15": "Нужна контролируемая рабочая среда",
  "gui.smog.copper.status_16": "Перегрев: остудите открытую камеру",
  "gui.smog.casting.stage_0": "Установите форму",
  "gui.smog.casting.stage_1": "Ожидание расплава",
  "gui.smog.casting.stage_2": "Заливка",
  "gui.smog.casting.stage_3": "Охлаждение",
  "gui.smog.casting.stage_4": "Отливка готова",
  "gui.smog.casting.stage_5": "Освободите выход",
  "gui.smog.casting.volume": "Залито: %s / %s",
  "message.smog.casting_cooling": "Охлаждение: ещё %s с · Форма закреплена",
  "gui.smog.casting.cooling": "Охлаждение: %s с",
  "gui.smog.manual.batches": "Партий: %s",
  "item.smog.freight_rope.help": "Переправа: ПКМ канатом по двум анкерам, до 24 блоков",
  "item.smog.freight_rope.unlink": "Shift + ПКМ по анкеру: отсоединить линию",
  "message.smog.ride_link": "Сначала соедини два анкера грузовым канатом",
  "message.smog.ride_busy": "Переправа занята грузом или другим игроком",
  "message.smog.ride_clear": "Для поездки нужны свободные 3 блока под канатом и площадки высадки у обоих анкеров",
  "message.smog.ride_started": "Смотри вдоль каната, чтобы ехать. Shift — выйти",
  "message.smog.ride_blocked": "Переправа остановлена: освободи путь и загрузи оба конца",
  "message.smog.ride_landing": "Нет площадки высадки. Восстанови её; Shift — аварийный выход",
  "message.smog.rope_help": "ПКМ предметами — загрузить; пустой рукой — отправить; палкой — поехать",
  "gui.smog.copper.status_17": "Переправа занята игроком",
  "entity.smog.ropeway_ride": "Подвес лебёдочной переправы",
  "item.smog.rope_anchor.help_1": "Свяжи два анкера грузовым канатом: до 24 блоков",
  "item.smog.rope_anchor.help_2": "ПКМ предметами: загрузить; пустой рукой: отправить",
  "item.smog.rope_anchor.help_3": "ПКМ палкой по канату: ехать в сторону взгляда",
  "item.smog.rope_anchor.help_4": "Shift: выйти; оставь место под канатом и площадки высадки",
  "item.smog.loading_crane.help_1": "ПКМ: партия; Shift + ПКМ: смена направления",
  "item.smog.loading_crane.help_2": "Красная метка на контроллере: 1 импульс редстоуна — 1 партия",
  "tool.smog.preview_use_saw": "Валит деревья с метками естественного происхождения",
  "tool.smog.preview_use_sword": "Сбалансированный клинок",
  "tool.smog.preview_use_katana": "Быстрее атаки, меньше урон",
  "tool.smog.preview_use_machete": "Медленнее атаки, больше урон",
  "tool.smog.preview_use_sickle": "ПКМ: сбор и повторная посадка зрелых культур в области 3×1×3",
  "itemGroup.smog.materials": "Smog: Материалы",
  "message.smog.fluid_supply": "%s: %s / 8000 mB. Передача по медным трубам.",
  "gui.smog.fluid.empty": "Пустой резервуар",
  "gui.smog.fluid.rate": "Подача: до 200 мВ/с",
  "gui.smog.pump.prime": "Качать воду",
  "gui.smog.pump.remaining": "Осталось перекачать: %s",
  "message.smog.valve_powered": "Ручной кран открыт, поток перекрыт редстоуном",
  "advancements.smog.copper.bronze_parts.title": "Не только слитки",
  "advancements.smog.copper.bronze_parts.description": "Изготовьте бронзовый фитинг и трубную деталь для первого привода.",
  "advancements.smog.copper.drive.title": "Передача движения",
  "advancements.smog.copper.drive.description": "Соберите бронзовый водяной привод. Поток воды станет работать вместо ваших рук.",
  "advancements.smog.copper.commissioning.title": "Пусконаладка",
  "advancements.smog.copper.commissioning.description": "Установите привод в раму пресса или рядом с мехами, подведите текущую воду и нажмите на привод пустой рукой.",
  "advancements.smog.copper.mechanized.title": "Мастерская ожила",
  "advancements.smog.copper.mechanized.description": "Завершите две автоматические партии пресса или две плавки с механическими мехами. Медная мастерская освоена — впереди бронзовая механизация.",
  "message.smog.finale.attach": "Встройте привод в собранный пресс или поставьте рядом с мехами.",
  "message.smog.finale.flow": "Нужен движущийся поток перед рабочей стороной привода. Стоячая вода его не вращает.",
  "message.smog.finale.bellows": "Подключите меха открытой воздушной насадкой к воздушному порту собранной плавильни.",
  "message.smog.finale.press": "Наладка начата: загрузите сырьё и освободите выход. Нужны две автоматические партии.",
  "message.smog.finale.smelt": "Наладка начата: заправьте плавильню и выполните две плавки с автоматическими мехами.",
  "message.smog.finale.progress": "Автоматические операции: %s / %s. ПКМ — состояние, Shift + ПКМ — отменить наладку.",
  "message.smog.finale.complete": "Медная мастерская завершена. Теперь вода работает вместе с вами.",
  "message.smog.finale.cancelled": "Наладка отменена. Оборудование продолжает обычную работу.",
  "message.smog.finale.already": "Медная мастерская уже освоена. Привод работает без повторной наладки.",
  "message.smog.finale.busy": "Наладку этого устройства уже ведёт другой игрок.",
  "jei.smog.guide.finale": "Финал Медной мастерской — работающий привод. Сделайте бронзу из трёх частей меди и одной олова, изготовьте фитинги и трубные детали и соберите привод. Замените им блок рамы пресса либо поставьте рядом с мехами. Перед рабочей стороной организуйте текущую, например падающую, воду. На прессе нужны две партии сырья, а на мехах — две настоящие плавки в подключённой плавильне. ПКМ пустой рукой по приводу начинает наладку и показывает её состояние. Ручные операции, пустые меха и простое наличие привода не засчитываются. Остановка воды приостанавливает работу без потери сырья и прогресса. Дополнительные ветки, достижения и исследования не запирают рецепты.",
  "item.smog.bronze_drive.help_1": "Встройте в раму пресса или поставьте рядом с мехами.",
  "item.smog.bronze_drive.help_2": "Текущая вода вращает привод. ПКМ пустой рукой — наладка.",
  "gui.smog.manual.state_0": "Соберите установку",
  "gui.smog.manual.detail_0": "Соберите установку настройщиком.",
  "gui.smog.manual.state_1": "Оборудование выключено",
  "gui.smog.manual.detail_1": "Включите оборудование.",
  "gui.smog.manual.state_2": "Привод остановлен",
  "gui.smog.manual.detail_2": "Проверьте привод и поток воды. Очередь и сырьё сохранены.",
  "gui.smog.manual.state_3": "Загрузите сырьё",
  "gui.smog.manual.detail_3": "Положите подходящее сырьё во входной слот.",
  "gui.smog.manual.state_4": "Неподходящее сырьё",
  "gui.smog.manual.detail_4": "Это сырьё не подходит для текущей операции.",
  "gui.smog.manual.state_5": "Не хватает сырья",
  "gui.smog.manual.detail_5": "Нужно для партии: %s; загружено: %s.",
  "gui.smog.manual.state_6": "Недостаточно воды",
  "gui.smog.manual.detail_6": "Нужно воды: %s; в лотке: %s.",
  "gui.smog.manual.state_7": "Нет места для масла",
  "gui.smog.manual.detail_7": "Нужно места для масла: %s; свободно: %s.",
  "gui.smog.manual.state_8": "Выход занят",
  "gui.smog.manual.detail_8": "Освободите выходной слот или заберите лишние предметы.",
  "gui.smog.manual.state_9": "Ожидает запуска",
  "gui.smog.manual.detail_9": "Запустите работу кнопкой или ПКМ пустой рукой.",
  "gui.smog.manual.state_10": "Обработка партии",
  "gui.smog.manual.detail_10": "Выполняется конечный ручной заказ.",
  "gui.smog.manual.state_11": "Механическая обработка",
  "gui.smog.manual.detail_11": "Привод выполняет работу самостоятельно.",
  "gui.smog.manual.state_12": "Автозапуск следующей партии",
  "gui.smog.manual.detail_12": "Следующая партия начнётся автоматически.",
  "gui.smog.manual.state_13": "Заберите готовые предметы",
  "gui.smog.manual.detail_13": "В выходном слоте: %2$s шт.",
  "gui.smog.manual.start": "Запустить",
  "gui.smog.casting.filled": "Залито: %s",
  "gui.smog.casting.required": "Требуется: %s",
  "gui.smog.utility.water_0": "Оборудование выключено",
  "gui.smog.utility.water_1": "Нужен источник воды снизу",
  "gui.smog.utility.water_2": "Запустите перекачку",
  "gui.smog.utility.water_3": "Выполняется перекачка",
  "gui.smog.utility.water_4": "Бак заполнен",
  "gui.smog.utility.water_5": "Резервуар пуст",
  "gui.smog.utility.water_6": "Запас готов к подаче",
  "gui.smog.utility.rope_0": "Переправа выключена",
  "gui.smog.utility.rope_1": "Соедините анкеры",
  "gui.smog.utility.rope_2": "Участок пути не загружен",
  "gui.smog.utility.rope_3": "Связь анкеров нарушена",
  "gui.smog.utility.rope_4": "Путь груза перекрыт",
  "gui.smog.utility.rope_5": "На линии пассажир",
  "gui.smog.utility.rope_6": "Груз в пути",
  "gui.smog.utility.rope_7": "Выход приёмника занят",
  "gui.smog.utility.rope_8": "Заберите доставленный груз",
  "gui.smog.utility.rope_9": "Загрузите груз",
  "gui.smog.utility.rope_10": "Ожидает отправки",
  "gui.smog.utility.rope_11": "Отправка в очереди",
  "gui.smog.utility.crane_0": "Соберите кран",
  "gui.smog.utility.crane_1": "Кран выключен",
  "gui.smog.utility.crane_2": "Ожидает заказа",
  "gui.smog.utility.crane_3": "Заказ принят",
  "gui.smog.utility.crane_4": "Перенос груза",
  "gui.smog.utility.crane_5": "Нет источника груза",
  "gui.smog.utility.crane_6": "Источник пуст",
  "gui.smog.utility.crane_7": "Нет приёмника груза",
  "gui.smog.utility.crane_8": "Путь крана перекрыт",
  "gui.smog.utility.crane_9": "Часть пути не загружена",
  "gui.smog.utility.crane_10": "Приёмник не принимает груз",
  "gui.smog.utility.cargo": "На подвесе: %s шт.",
  "gui.smog.utility.crane_hint": "ПКМ по контроллеру — заказать перенос",
  "gui.smog.utility.rope_hint": "ПКМ пустой рукой по анкеру — отправить",
  "tool.smog.tip.trait.cultivation": "Земледелие",
  "tool.smog.trait.cultivation": "Вспашка увлажняет почву.",
  "tool.smog.tip.trait.impact": "Ударная волна",
  "tool.smog.trait.impact": "Отталкивает существ; перерыв: %2$s с.",
  "tool.smog.tip.trait.magnetic": "Магнетизм",
  "tool.smog.trait.magnetic": "Притягивает свободные предметы рядом на 2 с после добычи.",
  "tool.smog.tip.trait.quenching": "Гашение",
  "tool.smog.trait.quenching": "Тушит существ; перерыв: %2$s с.",
  "tool.smog.tip.trait.sapping": "Изнурение",
  "tool.smog.trait.sapping": "Слабость: %1$s с; перерыв: %2$s с.",
  "tool.smog.tip.trait.breathing": "Передышка",
  "tool.smog.trait.breathing": "Подходящая добыча под водой возвращает 4 ед. воздуха.",
  "tool.smog.tip.trait.insulation": "Теплоизоляция",
  "tool.smog.trait.insulation": "Уменьшает замерзание в руке."
}
```


## Сменные модули

### 1

```json
{
  "controller": "smog:lever_press",
  "roles": [
    {
      "name": "basket",
      "positions": [
        [
          0,
          0,
          1
        ]
      ],
      "modes": {
        "smog:press_basket": "oil",
        "smog:fiber_press_basket": "fiber"
      }
    },
    {
      "name": "drive",
      "positions": [
        [
          -1,
          1,
          0
        ],
        [
          0,
          1,
          0
        ],
        [
          1,
          1,
          0
        ],
        [
          -1,
          1,
          1
        ],
        [
          0,
          1,
          1
        ],
        [
          1,
          1,
          1
        ]
      ],
      "modes": {
        "smog:press_frame": "manual",
        "smog:bronze_drive": "hydraulic"
      }
    }
  ]
}
```


## Дополнительные литейные формы


## Условия обработки металлов

### copper

```json
{
  "id": "copper",
  "working_heat": 600,
  "lining": false,
  "closed": false,
  "controlled_atmosphere": false,
  "open_max_heat": 10000
}
```

### tin

```json
{
  "id": "tin",
  "working_heat": 260,
  "lining": false,
  "closed": false,
  "controlled_atmosphere": false,
  "open_max_heat": 10000
}
```

### bronze

```json
{
  "id": "bronze",
  "working_heat": 580,
  "lining": false,
  "closed": false,
  "controlled_atmosphere": false,
  "open_max_heat": 10000
}
```

### lead

```json
{
  "id": "lead",
  "working_heat": 350,
  "lining": false,
  "closed": false,
  "controlled_atmosphere": false,
  "open_max_heat": 10000
}
```

### zinc

```json
{
  "id": "zinc",
  "working_heat": 460,
  "lining": false,
  "closed": false,
  "controlled_atmosphere": false,
  "open_max_heat": 560
}
```

### aluminum

```json
{
  "id": "aluminum",
  "working_heat": 520,
  "lining": true,
  "closed": false,
  "controlled_atmosphere": false,
  "open_max_heat": 10000
}
```

### silver

```json
{
  "id": "silver",
  "working_heat": 580,
  "lining": false,
  "closed": false,
  "controlled_atmosphere": false,
  "open_max_heat": 10000
}
```

### gold

```json
{
  "id": "gold",
  "working_heat": 600,
  "lining": false,
  "closed": false,
  "controlled_atmosphere": false,
  "open_max_heat": 10000
}
```

### brass

```json
{
  "id": "brass",
  "working_heat": 580,
  "lining": false,
  "closed": false,
  "controlled_atmosphere": false,
  "open_max_heat": 10000
}
```

### iron

```json
{
  "id": "iron",
  "working_heat": 1500,
  "lining": false,
  "closed": false,
  "controlled_atmosphere": false,
  "open_max_heat": 10000
}
```

### nickel

```json
{
  "id": "nickel",
  "working_heat": 1450,
  "lining": false,
  "closed": false,
  "controlled_atmosphere": false,
  "open_max_heat": 10000
}
```

### manganese

```json
{
  "id": "manganese",
  "working_heat": 1250,
  "lining": false,
  "closed": false,
  "controlled_atmosphere": false,
  "open_max_heat": 10000
}
```

### chromium

```json
{
  "id": "chromium",
  "working_heat": 1900,
  "lining": false,
  "closed": false,
  "controlled_atmosphere": false,
  "open_max_heat": 10000
}
```

### titanium

```json
{
  "id": "titanium",
  "working_heat": 1700,
  "lining": false,
  "closed": false,
  "controlled_atmosphere": false,
  "open_max_heat": 10000
}
```

### tungsten

```json
{
  "id": "tungsten",
  "working_heat": 3400,
  "lining": false,
  "closed": false,
  "controlled_atmosphere": false,
  "open_max_heat": 10000
}
```

### molybdenum

```json
{
  "id": "molybdenum",
  "working_heat": 2650,
  "lining": false,
  "closed": false,
  "controlled_atmosphere": false,
  "open_max_heat": 10000
}
```

### uranium

```json
{
  "id": "uranium",
  "working_heat": 1140,
  "lining": false,
  "closed": true,
  "controlled_atmosphere": true,
  "open_max_heat": 10000
}
```

### lithium

```json
{
  "id": "lithium",
  "working_heat": 220,
  "lining": false,
  "closed": true,
  "controlled_atmosphere": true,
  "open_max_heat": 10000
}
```

### steel

```json
{
  "id": "steel",
  "working_heat": 1450,
  "lining": false,
  "closed": false,
  "controlled_atmosphere": false,
  "open_max_heat": 10000
}
```

### cast_iron

```json
{
  "id": "cast_iron",
  "working_heat": 1200,
  "lining": false,
  "closed": false,
  "controlled_atmosphere": false,
  "open_max_heat": 10000
}
```

### wrought_iron

```json
{
  "id": "wrought_iron",
  "working_heat": 1500,
  "lining": false,
  "closed": false,
  "controlled_atmosphere": false,
  "open_max_heat": 10000
}
```

### stainless_steel

```json
{
  "id": "stainless_steel",
  "working_heat": 1450,
  "lining": false,
  "closed": false,
  "controlled_atmosphere": false,
  "open_max_heat": 10000
}
```

### tool_steel

```json
{
  "id": "tool_steel",
  "working_heat": 1450,
  "lining": false,
  "closed": false,
  "controlled_atmosphere": false,
  "open_max_heat": 10000
}
```

### duralumin

```json
{
  "id": "duralumin",
  "working_heat": 550,
  "lining": true,
  "closed": false,
  "controlled_atmosphere": false,
  "open_max_heat": 10000
}
```

### cupronickel

```json
{
  "id": "cupronickel",
  "working_heat": 1200,
  "lining": false,
  "closed": false,
  "controlled_atmosphere": false,
  "open_max_heat": 10000
}
```

### constantan

```json
{
  "id": "constantan",
  "working_heat": 1250,
  "lining": false,
  "closed": false,
  "controlled_atmosphere": false,
  "open_max_heat": 10000
}
```

### invar

```json
{
  "id": "invar",
  "working_heat": 1450,
  "lining": false,
  "closed": false,
  "controlled_atmosphere": false,
  "open_max_heat": 10000
}
```

### electrum

```json
{
  "id": "electrum",
  "working_heat": 580,
  "lining": false,
  "closed": false,
  "controlled_atmosphere": false,
  "open_max_heat": 10000
}
```

### nichrome

```json
{
  "id": "nichrome",
  "working_heat": 1400,
  "lining": false,
  "closed": false,
  "controlled_atmosphere": false,
  "open_max_heat": 10000
}
```

### solder

```json
{
  "id": "solder",
  "working_heat": 270,
  "lining": false,
  "closed": false,
  "controlled_atmosphere": false,
  "open_max_heat": 10000
}
```

### tungsten_steel

```json
{
  "id": "tungsten_steel",
  "working_heat": 1700,
  "lining": false,
  "closed": false,
  "controlled_atmosphere": false,
  "open_max_heat": 10000
}
```

### titanium_alloy

```json
{
  "id": "titanium_alloy",
  "working_heat": 1700,
  "lining": false,
  "closed": false,
  "controlled_atmosphere": false,
  "open_max_heat": 10000
}
```

### nickel_superalloy

```json
{
  "id": "nickel_superalloy",
  "working_heat": 1400,
  "lining": false,
  "closed": false,
  "controlled_atmosphere": false,
  "open_max_heat": 10000
}
```

### aluminum_bronze

```json
{
  "id": "aluminum_bronze",
  "working_heat": 600,
  "lining": true,
  "closed": false,
  "controlled_atmosphere": false,
  "open_max_heat": 10000
}
```

### netherite

```json
{
  "id": "netherite",
  "working_heat": 2200,
  "lining": false,
  "closed": false,
  "controlled_atmosphere": false,
  "open_max_heat": 10000
}
```


## Жидкое топливо

### 1

```json
{
  "fluid": "minecraft:lava",
  "heat": 1100,
  "speed": 1,
  "cost": 9
}
```

### 2

```json
{
  "fluid": "smog:blazing_fuel",
  "heat": 1600,
  "speed": 2,
  "cost": 12
}
```


## Рецепты сплавов

### bronze

```json
{
  "output": "bronze",
  "ingredients": {
    "copper": 3,
    "tin": 1
  },
  "heat": 600
}
```

### brass

```json
{
  "output": "brass",
  "ingredients": {
    "copper": 3,
    "zinc": 1
  },
  "heat": 600,
  "closed": true
}
```

### electrum

```json
{
  "output": "electrum",
  "ingredients": {
    "gold": 1,
    "silver": 1
  },
  "heat": 600
}
```

### solder

```json
{
  "output": "solder",
  "ingredients": {
    "tin": 3,
    "lead": 2
  },
  "heat": 350
}
```

### aluminum_bronze

```json
{
  "output": "aluminum_bronze",
  "ingredients": {
    "copper": 9,
    "aluminum": 1
  },
  "heat": 650
}
```

### cupronickel

```json
{
  "output": "cupronickel",
  "ingredients": {
    "copper": 3,
    "nickel": 1
  },
  "heat": 1200
}
```

### constantan

```json
{
  "output": "constantan",
  "ingredients": {
    "copper": 1,
    "nickel": 1
  },
  "heat": 1250
}
```

### invar

```json
{
  "output": "invar",
  "ingredients": {
    "iron": 2,
    "nickel": 1
  },
  "heat": 1450
}
```

### nichrome

```json
{
  "output": "nichrome",
  "ingredients": {
    "nickel": 4,
    "chromium": 1
  },
  "heat": 1450
}
```

### stainless_steel

```json
{
  "output": "stainless_steel",
  "ingredients": {
    "iron": 6,
    "chromium": 2,
    "nickel": 1
  },
  "heat": 1500
}
```


## Материалы инструментов

### aluminum

```json
{
  "id": "aluminum",
  "name": {
    "en_us": "Aluminum",
    "ru_ru": "Алюминий"
  },
  "color": "C5CDD3",
  "profiles": {
    "head": {
      "durability": 180,
      "mining_speed": 6,
      "attack_damage": 2,
      "harvest_tier": "iron",
      "casting": true,
      "shaping": true,
      "shaping_extra_units": 9000
    },
    "handle": {
      "forms": [
        "standard_handle"
      ],
      "shaping": true,
      "speed_multiplier": 1.08,
      "durability_multiplier": 0.85,
      "handling_multiplier": 1.07,
      "material_units": 4500
    },
    "binding": {
      "forms": [
        "clamp"
      ],
      "durability_multiplier": 1.08,
      "shaping": true
    }
  },
  "repair_item": "smog:aluminum_ingot",
  "molten": "aluminum"
}
```

### aluminum_bronze

```json
{
  "id": "aluminum_bronze",
  "name": {
    "en_us": "Aluminum Bronze",
    "ru_ru": "Алюминиевая бронза"
  },
  "color": "BFA35E",
  "profiles": {
    "head": {
      "durability": 450,
      "mining_speed": 6.5,
      "attack_damage": 3,
      "harvest_tier": "iron",
      "casting": true,
      "shaping": true,
      "shaping_extra_units": 9000
    },
    "handle": {
      "forms": [
        "standard_handle"
      ],
      "shaping": true,
      "speed_multiplier": 1,
      "durability_multiplier": 1.08,
      "handling_multiplier": 0.98,
      "material_units": 4500
    },
    "binding": {
      "forms": [
        "clamp"
      ],
      "durability_multiplier": 1.08,
      "shaping": true
    }
  },
  "repair_item": "smog:aluminum_bronze_ingot",
  "molten": "aluminum_bronze"
}
```

### brass

```json
{
  "id": "brass",
  "name": {
    "en_us": "Brass",
    "ru_ru": "Латунь"
  },
  "color": "CDA947",
  "profiles": {
    "head": {
      "durability": 220,
      "mining_speed": 5.5,
      "attack_damage": 2,
      "harvest_tier": "iron",
      "casting": true,
      "shaping": true,
      "shaping_extra_units": 9000
    },
    "handle": {
      "forms": [
        "standard_handle"
      ],
      "shaping": true,
      "speed_multiplier": 1,
      "durability_multiplier": 1.08,
      "handling_multiplier": 0.98,
      "material_units": 4500
    },
    "binding": {
      "forms": [
        "clamp"
      ],
      "durability_multiplier": 1.08,
      "shaping": true
    }
  },
  "repair_item": "smog:brass_ingot",
  "molten": "brass"
}
```

### bronze

```json
{
  "id": "bronze",
  "name": {
    "en_us": "Bronze",
    "ru_ru": "Бронза"
  },
  "color": "E9B34C",
  "profiles": {
    "head": {
      "traits": [
        {
          "id": "staggering",
          "duration_ticks": 20,
          "cooldown_ticks": 60
        }
      ],
      "durability": 320,
      "mining_speed": 6,
      "attack_damage": 3,
      "harvest_tier": "iron",
      "casting": true,
      "shaping": true,
      "shaping_extra_units": 9000
    },
    "binding": {
      "forms": [
        "clamp"
      ],
      "durability_multiplier": 1.1
    },
    "handle": {
      "forms": [
        "standard_handle"
      ],
      "shaping": true,
      "speed_multiplier": 0.98,
      "durability_multiplier": 1.18,
      "handling_multiplier": 0.94,
      "material_units": 4500
    }
  },
  "repair_item": "smog:bronze_ingot",
  "molten": "bronze"
}
```

### cast_iron

```json
{
  "id": "cast_iron",
  "name": {
    "en_us": "Cast Iron",
    "ru_ru": "Чугун"
  },
  "color": "665D59",
  "profiles": {
    "head": {
      "durability": 300,
      "mining_speed": 4,
      "attack_damage": 3,
      "harvest_tier": "iron",
      "casting": true,
      "shaping": false,
      "shaping_extra_units": 9000
    },
    "handle": {
      "forms": [
        "standard_handle"
      ],
      "shaping": true,
      "speed_multiplier": 0.9,
      "durability_multiplier": 1.05,
      "handling_multiplier": 0.88,
      "material_units": 4500
    },
    "binding": {
      "forms": [
        "clamp"
      ],
      "durability_multiplier": 1.08,
      "shaping": false
    }
  },
  "repair_item": "smog:cast_iron_ingot",
  "molten": "cast_iron"
}
```

### chromium

```json
{
  "id": "chromium",
  "name": {
    "en_us": "Chromium",
    "ru_ru": "Хром"
  },
  "color": "8DAAA7",
  "profiles": {
    "head": {
      "durability": 800,
      "mining_speed": 7,
      "attack_damage": 4,
      "harvest_tier": "diamond",
      "casting": true,
      "shaping": false,
      "shaping_extra_units": 9000
    },
    "handle": {
      "forms": [
        "standard_handle"
      ],
      "shaping": true,
      "speed_multiplier": 1,
      "durability_multiplier": 1.08,
      "handling_multiplier": 0.98,
      "material_units": 4500
    },
    "binding": {
      "forms": [
        "clamp"
      ],
      "durability_multiplier": 1.08,
      "shaping": false
    }
  },
  "repair_item": "smog:chromium_ingot",
  "molten": "chromium"
}
```

### constantan

```json
{
  "id": "constantan",
  "name": {
    "en_us": "Constantan",
    "ru_ru": "Константан"
  },
  "color": "B18C77",
  "profiles": {
    "head": {
      "durability": 300,
      "mining_speed": 5,
      "attack_damage": 2,
      "harvest_tier": "iron",
      "casting": true,
      "shaping": false,
      "shaping_extra_units": 9000
    },
    "handle": {
      "forms": [
        "standard_handle"
      ],
      "shaping": true,
      "speed_multiplier": 1,
      "durability_multiplier": 1.08,
      "handling_multiplier": 0.98,
      "material_units": 4500
    },
    "binding": {
      "forms": [
        "clamp"
      ],
      "durability_multiplier": 1.08,
      "shaping": false
    }
  },
  "repair_item": "smog:constantan_ingot",
  "molten": "constantan"
}
```

### copper

```json
{
  "id": "copper",
  "name": {
    "en_us": "Copper",
    "ru_ru": "Медь"
  },
  "color": "FF966C",
  "profiles": {
    "head": {
      "traits": [
        {
          "id": "work_hardening",
          "max_stacks": 4,
          "speed_per_stack": 0.04,
          "timeout_ticks": 100
        }
      ],
      "durability": 200,
      "mining_speed": 5,
      "attack_damage": 3,
      "harvest_tier": "copper",
      "casting": true,
      "shaping": true,
      "shaping_extra_units": 9000
    },
    "handle": {
      "forms": [
        "standard_handle"
      ],
      "shaping": true,
      "speed_multiplier": 1,
      "durability_multiplier": 1.08,
      "handling_multiplier": 0.96,
      "material_units": 4500
    },
    "binding": {
      "forms": [
        "clamp"
      ],
      "durability_multiplier": 1.08
    }
  },
  "repair_item": "minecraft:copper_ingot",
  "native_prefix": "copper",
  "molten": "copper"
}
```

### cord

```json
{
  "id": "cord",
  "name": {
    "en_us": "Cord",
    "ru_ru": "Верёвка"
  },
  "color": "CAB891",
  "profiles": {
    "binding": {
      "forms": [
        "binding"
      ],
      "durability_multiplier": 1,
      "traits": [
        {
          "id": "grip",
          "every": 6
        }
      ]
    }
  },
  "repair_ingredient": "minecraft:string"
}
```

### cupronickel

```json
{
  "id": "cupronickel",
  "name": {
    "en_us": "Cupronickel",
    "ru_ru": "Мельхиор"
  },
  "color": "B2B8AB",
  "profiles": {
    "head": {
      "durability": 350,
      "mining_speed": 6,
      "attack_damage": 2,
      "harvest_tier": "iron",
      "casting": true,
      "shaping": false,
      "shaping_extra_units": 9000
    },
    "handle": {
      "forms": [
        "standard_handle"
      ],
      "shaping": true,
      "speed_multiplier": 1,
      "durability_multiplier": 1.08,
      "handling_multiplier": 0.98,
      "material_units": 4500
    },
    "binding": {
      "forms": [
        "clamp"
      ],
      "durability_multiplier": 1.08,
      "shaping": false
    }
  },
  "repair_item": "smog:cupronickel_ingot",
  "molten": "cupronickel"
}
```

### duralumin

```json
{
  "id": "duralumin",
  "name": {
    "en_us": "Duralumin",
    "ru_ru": "Дюралюминий"
  },
  "color": "B4C6CF",
  "profiles": {
    "head": {
      "durability": 350,
      "mining_speed": 7,
      "attack_damage": 2,
      "harvest_tier": "iron",
      "casting": true,
      "shaping": true,
      "shaping_extra_units": 9000
    },
    "handle": {
      "forms": [
        "standard_handle"
      ],
      "shaping": true,
      "speed_multiplier": 1.08,
      "durability_multiplier": 1.02,
      "handling_multiplier": 1.08,
      "material_units": 4500
    },
    "binding": {
      "forms": [
        "clamp"
      ],
      "durability_multiplier": 1.08,
      "shaping": true
    }
  },
  "repair_item": "smog:duralumin_ingot",
  "molten": "duralumin"
}
```

### electrum

```json
{
  "id": "electrum",
  "name": {
    "en_us": "Electrum",
    "ru_ru": "Электрум"
  },
  "color": "E6D58E",
  "profiles": {
    "head": {
      "durability": 110,
      "mining_speed": 10,
      "attack_damage": 1,
      "harvest_tier": "iron",
      "casting": true,
      "shaping": true,
      "shaping_extra_units": 9000
    },
    "handle": {
      "forms": [
        "standard_handle"
      ],
      "shaping": true,
      "speed_multiplier": 1,
      "durability_multiplier": 1.08,
      "handling_multiplier": 0.98,
      "material_units": 4500
    },
    "binding": {
      "forms": [
        "clamp"
      ],
      "durability_multiplier": 1.08,
      "shaping": true
    }
  },
  "repair_item": "smog:electrum_ingot",
  "molten": "electrum"
}
```

### fiber

```json
{
  "id": "fiber",
  "name": {
    "en_us": "Plant Fiber",
    "ru_ru": "Растительное волокно"
  },
  "color": "C0B78B",
  "profiles": {
    "wrap": {
      "forms": [
        "wrap"
      ],
      "speed_multiplier": 1.03,
      "durability_multiplier": 1.02,
      "traits": [
        {
          "id": "breathing"
        }
      ]
    }
  },
  "repair_ingredient": "smog:plant_fiber"
}
```

### gold

```json
{
  "id": "gold",
  "name": {
    "en_us": "Gold",
    "ru_ru": "Золото"
  },
  "color": "F8D34E",
  "profiles": {
    "head": {
      "traits": [
        {
          "id": "marking",
          "duration_ticks": 60,
          "cooldown_ticks": 60
        }
      ],
      "durability": 32,
      "mining_speed": 12,
      "attack_damage": 0,
      "harvest_tier": "gold",
      "casting": true,
      "shaping": true,
      "shaping_extra_units": 9000
    },
    "handle": {
      "forms": [
        "standard_handle"
      ],
      "shaping": true,
      "speed_multiplier": 1.06,
      "durability_multiplier": 0.75,
      "handling_multiplier": 1.06,
      "material_units": 4500
    },
    "binding": {
      "forms": [
        "clamp"
      ],
      "durability_multiplier": 0.9,
      "shaping": true
    }
  },
  "repair_item": "minecraft:gold_ingot",
  "molten": "gold",
  "native_prefix": "golden"
}
```

### invar

```json
{
  "id": "invar",
  "name": {
    "en_us": "Invar",
    "ru_ru": "Инвар"
  },
  "color": "9AABA4",
  "profiles": {
    "head": {
      "durability": 500,
      "mining_speed": 6,
      "attack_damage": 3,
      "harvest_tier": "iron",
      "casting": true,
      "shaping": false,
      "shaping_extra_units": 9000
    },
    "handle": {
      "forms": [
        "standard_handle"
      ],
      "shaping": true,
      "speed_multiplier": 1,
      "durability_multiplier": 1.08,
      "handling_multiplier": 0.98,
      "material_units": 4500
    },
    "binding": {
      "forms": [
        "clamp"
      ],
      "durability_multiplier": 1.08,
      "shaping": false
    }
  },
  "repair_item": "smog:invar_ingot",
  "molten": "invar"
}
```

### iron

```json
{
  "id": "iron",
  "name": {
    "en_us": "Iron",
    "ru_ru": "Железо"
  },
  "color": "D8D8D8",
  "profiles": {
    "head": {
      "durability": 250,
      "mining_speed": 6,
      "attack_damage": 3,
      "harvest_tier": "iron",
      "casting": false,
      "shaping": true,
      "shaping_extra_units": 9000,
      "traits": [
        {
          "id": "magnetic"
        }
      ]
    },
    "handle": {
      "forms": [
        "standard_handle"
      ],
      "shaping": true,
      "speed_multiplier": 0.98,
      "durability_multiplier": 1.15,
      "handling_multiplier": 0.93,
      "material_units": 4500
    },
    "binding": {
      "forms": [
        "clamp"
      ],
      "durability_multiplier": 1.08,
      "shaping": true
    }
  },
  "repair_item": "minecraft:iron_ingot",
  "native_prefix": "iron",
  "molten": "iron"
}
```

### lead

```json
{
  "id": "lead",
  "name": {
    "en_us": "Lead",
    "ru_ru": "Свинец"
  },
  "color": "77748F",
  "profiles": {
    "head": {
      "durability": 70,
      "mining_speed": 2,
      "attack_damage": 1,
      "harvest_tier": "stone",
      "casting": true,
      "shaping": true,
      "shaping_extra_units": 9000,
      "traits": [
        {
          "id": "sapping",
          "duration_ticks": 30,
          "cooldown_ticks": 100
        }
      ]
    },
    "handle": {
      "forms": [
        "standard_handle"
      ],
      "shaping": true,
      "speed_multiplier": 0.82,
      "durability_multiplier": 0.85,
      "handling_multiplier": 0.8,
      "material_units": 4500
    },
    "binding": {
      "forms": [
        "clamp"
      ],
      "durability_multiplier": 0.9,
      "shaping": true
    }
  },
  "repair_item": "smog:lead_ingot",
  "molten": "lead"
}
```

### leather

```json
{
  "id": "leather",
  "name": {
    "en_us": "Leather",
    "ru_ru": "Кожа"
  },
  "color": "8E5937",
  "profiles": {
    "binding": {
      "forms": [
        "binding"
      ],
      "durability_multiplier": 1.03,
      "material_units": 4500
    },
    "wrap": {
      "traits": [
        {
          "id": "grip",
          "every": 4
        }
      ],
      "forms": [
        "wrap"
      ],
      "speed_multiplier": 1.01,
      "durability_multiplier": 1.04
    }
  },
  "repair_ingredient": "minecraft:leather"
}
```

### lithium

```json
{
  "id": "lithium",
  "name": {
    "en_us": "Lithium",
    "ru_ru": "Литий"
  },
  "color": "C3BED4",
  "profiles": {
    "head": {
      "durability": 35,
      "mining_speed": 2,
      "attack_damage": 0,
      "harvest_tier": "stone",
      "casting": true,
      "shaping": false,
      "shaping_extra_units": 9000
    },
    "handle": {
      "forms": [
        "standard_handle"
      ],
      "shaping": true,
      "speed_multiplier": 1.06,
      "durability_multiplier": 0.65,
      "handling_multiplier": 1.08,
      "material_units": 4500
    },
    "binding": {
      "forms": [
        "clamp"
      ],
      "durability_multiplier": 0.9,
      "shaping": false
    }
  },
  "repair_item": "smog:lithium_ingot",
  "molten": "lithium"
}
```

### manganese

```json
{
  "id": "manganese",
  "name": {
    "en_us": "Manganese",
    "ru_ru": "Марганец"
  },
  "color": "78808C",
  "profiles": {
    "head": {
      "durability": 260,
      "mining_speed": 5,
      "attack_damage": 2,
      "harvest_tier": "iron",
      "casting": true,
      "shaping": false,
      "shaping_extra_units": 9000
    },
    "handle": {
      "forms": [
        "standard_handle"
      ],
      "shaping": true,
      "speed_multiplier": 1,
      "durability_multiplier": 1.08,
      "handling_multiplier": 0.98,
      "material_units": 4500
    },
    "binding": {
      "forms": [
        "clamp"
      ],
      "durability_multiplier": 1.08,
      "shaping": false
    }
  },
  "repair_item": "smog:manganese_ingot",
  "molten": "manganese"
}
```

### molybdenum

```json
{
  "id": "molybdenum",
  "name": {
    "en_us": "Molybdenum",
    "ru_ru": "Молибден"
  },
  "color": "8E9FAB",
  "profiles": {
    "head": {
      "durability": 1000,
      "mining_speed": 7,
      "attack_damage": 4,
      "harvest_tier": "diamond",
      "casting": true,
      "shaping": false,
      "shaping_extra_units": 9000
    },
    "handle": {
      "forms": [
        "standard_handle"
      ],
      "shaping": true,
      "speed_multiplier": 1,
      "durability_multiplier": 1.08,
      "handling_multiplier": 0.98,
      "material_units": 4500
    },
    "binding": {
      "forms": [
        "clamp"
      ],
      "durability_multiplier": 1.08,
      "shaping": false
    }
  },
  "repair_item": "smog:molybdenum_ingot",
  "molten": "molybdenum"
}
```

### netherite

```json
{
  "id": "netherite",
  "name": {
    "en_us": "Netherite",
    "ru_ru": "Незерит"
  },
  "color": "655C63",
  "profiles": {
    "head": {
      "durability": 2031,
      "mining_speed": 9,
      "attack_damage": 4,
      "harvest_tier": "netherite",
      "casting": true,
      "shaping": false,
      "shaping_extra_units": 9000
    },
    "handle": {
      "forms": [
        "standard_handle"
      ],
      "shaping": true,
      "speed_multiplier": 1,
      "durability_multiplier": 1.08,
      "handling_multiplier": 0.98,
      "material_units": 4500
    },
    "binding": {
      "forms": [
        "clamp"
      ],
      "durability_multiplier": 1.08,
      "shaping": false
    }
  },
  "repair_item": "minecraft:netherite_ingot",
  "molten": "netherite",
  "native_prefix": "netherite"
}
```

### nichrome

```json
{
  "id": "nichrome",
  "name": {
    "en_us": "Nichrome",
    "ru_ru": "Нихром"
  },
  "color": "B9AD9F",
  "profiles": {
    "head": {
      "durability": 600,
      "mining_speed": 6,
      "attack_damage": 3,
      "harvest_tier": "iron",
      "casting": true,
      "shaping": false,
      "shaping_extra_units": 9000
    },
    "handle": {
      "forms": [
        "standard_handle"
      ],
      "shaping": true,
      "speed_multiplier": 1,
      "durability_multiplier": 1.08,
      "handling_multiplier": 0.98,
      "material_units": 4500
    },
    "binding": {
      "forms": [
        "clamp"
      ],
      "durability_multiplier": 1.08,
      "shaping": false
    }
  },
  "repair_item": "smog:nichrome_ingot",
  "molten": "nichrome"
}
```

### nickel

```json
{
  "id": "nickel",
  "name": {
    "en_us": "Nickel",
    "ru_ru": "Никель"
  },
  "color": "A8AA83",
  "profiles": {
    "head": {
      "durability": 450,
      "mining_speed": 6,
      "attack_damage": 3,
      "harvest_tier": "iron",
      "casting": true,
      "shaping": false,
      "shaping_extra_units": 9000
    },
    "handle": {
      "forms": [
        "standard_handle"
      ],
      "shaping": true,
      "speed_multiplier": 1,
      "durability_multiplier": 1.08,
      "handling_multiplier": 0.98,
      "material_units": 4500
    },
    "binding": {
      "forms": [
        "clamp"
      ],
      "durability_multiplier": 1.08,
      "shaping": false
    }
  },
  "repair_item": "smog:nickel_ingot",
  "molten": "nickel"
}
```

### nickel_superalloy

```json
{
  "id": "nickel_superalloy",
  "name": {
    "en_us": "Nickel Superalloy",
    "ru_ru": "Никелевый жаропрочный сплав"
  },
  "color": "B4A385",
  "profiles": {
    "head": {
      "durability": 1200,
      "mining_speed": 7.5,
      "attack_damage": 4,
      "harvest_tier": "diamond",
      "casting": true,
      "shaping": false,
      "shaping_extra_units": 9000
    },
    "handle": {
      "forms": [
        "standard_handle"
      ],
      "shaping": true,
      "speed_multiplier": 1,
      "durability_multiplier": 1.08,
      "handling_multiplier": 0.98,
      "material_units": 4500
    },
    "binding": {
      "forms": [
        "clamp"
      ],
      "durability_multiplier": 1.08,
      "shaping": false
    }
  },
  "repair_item": "smog:nickel_superalloy_ingot",
  "molten": "nickel_superalloy"
}
```

### silver

```json
{
  "id": "silver",
  "name": {
    "en_us": "Silver",
    "ru_ru": "Серебро"
  },
  "color": "D4DDE8",
  "profiles": {
    "head": {
      "durability": 130,
      "mining_speed": 7,
      "attack_damage": 2,
      "harvest_tier": "iron",
      "casting": true,
      "shaping": true,
      "shaping_extra_units": 9000
    },
    "handle": {
      "forms": [
        "standard_handle"
      ],
      "shaping": true,
      "speed_multiplier": 1.04,
      "durability_multiplier": 0.92,
      "handling_multiplier": 1.04,
      "material_units": 4500
    },
    "binding": {
      "forms": [
        "clamp"
      ],
      "durability_multiplier": 1.08,
      "shaping": true
    }
  },
  "repair_item": "smog:silver_ingot",
  "molten": "silver"
}
```

### solder

```json
{
  "id": "solder",
  "name": {
    "en_us": "Tin-Lead Solder",
    "ru_ru": "Припой"
  },
  "color": "B7BAC0",
  "profiles": {
    "head": {
      "durability": 45,
      "mining_speed": 2,
      "attack_damage": 0,
      "harvest_tier": "stone",
      "casting": true,
      "shaping": true,
      "shaping_extra_units": 9000
    },
    "handle": {
      "forms": [
        "standard_handle"
      ],
      "shaping": true,
      "speed_multiplier": 0.9,
      "durability_multiplier": 0.7,
      "handling_multiplier": 0.95,
      "material_units": 4500
    },
    "binding": {
      "forms": [
        "clamp"
      ],
      "durability_multiplier": 0.9,
      "shaping": true
    }
  },
  "repair_item": "smog:solder_ingot",
  "molten": "solder"
}
```

### stainless_steel

```json
{
  "id": "stainless_steel",
  "name": {
    "en_us": "Stainless Steel",
    "ru_ru": "Нержавеющая сталь"
  },
  "color": "B5BFC1",
  "profiles": {
    "head": {
      "durability": 800,
      "mining_speed": 7,
      "attack_damage": 3,
      "harvest_tier": "diamond",
      "casting": true,
      "shaping": false,
      "shaping_extra_units": 9000
    },
    "handle": {
      "forms": [
        "standard_handle"
      ],
      "shaping": true,
      "speed_multiplier": 1,
      "durability_multiplier": 1.08,
      "handling_multiplier": 0.98,
      "material_units": 4500
    },
    "binding": {
      "forms": [
        "clamp"
      ],
      "durability_multiplier": 1.08,
      "shaping": false
    }
  },
  "repair_item": "smog:stainless_steel_ingot",
  "molten": "stainless_steel"
}
```

### steel

```json
{
  "id": "steel",
  "name": {
    "en_us": "Steel",
    "ru_ru": "Сталь"
  },
  "color": "66717B",
  "profiles": {
    "head": {
      "durability": 650,
      "mining_speed": 7,
      "attack_damage": 3,
      "harvest_tier": "iron",
      "casting": true,
      "shaping": false,
      "shaping_extra_units": 9000
    },
    "handle": {
      "forms": [
        "standard_handle"
      ],
      "shaping": true,
      "speed_multiplier": 0.98,
      "durability_multiplier": 1.2,
      "handling_multiplier": 0.96,
      "material_units": 4500
    },
    "binding": {
      "forms": [
        "clamp"
      ],
      "durability_multiplier": 1.08,
      "shaping": false
    }
  },
  "repair_item": "smog:steel_ingot",
  "molten": "steel"
}
```

### stone

```json
{
  "id": "stone",
  "name": {
    "en_us": "Stone",
    "ru_ru": "Камень"
  },
  "color": "969696",
  "profiles": {
    "head": {
      "durability": 131,
      "mining_speed": 4,
      "attack_damage": 2,
      "harvest_tier": "stone",
      "casting": false,
      "shaping": true,
      "traits": [
        {
          "id": "impact",
          "duration_ticks": 1,
          "cooldown_ticks": 40
        }
      ]
    },
    "handle": {
      "forms": [
        "standard_handle"
      ],
      "shaping": true,
      "speed_multiplier": 0.9,
      "durability_multiplier": 1.03,
      "handling_multiplier": 0.9,
      "material_units": 4500
    }
  },
  "repair_ingredient": "#minecraft:stone_tool_materials",
  "native_prefix": "stone"
}
```

### tin

```json
{
  "id": "tin",
  "name": {
    "en_us": "Tin",
    "ru_ru": "Олово"
  },
  "color": "9FBFC9",
  "profiles": {
    "head": {
      "durability": 90,
      "mining_speed": 3,
      "attack_damage": 1,
      "harvest_tier": "stone",
      "casting": true,
      "shaping": true,
      "shaping_extra_units": 9000,
      "traits": [
        {
          "id": "quenching",
          "duration_ticks": 1,
          "cooldown_ticks": 40
        }
      ]
    },
    "handle": {
      "forms": [
        "standard_handle"
      ],
      "shaping": true,
      "speed_multiplier": 0.95,
      "durability_multiplier": 0.9,
      "handling_multiplier": 0.97,
      "material_units": 4500
    },
    "binding": {
      "forms": [
        "clamp"
      ],
      "durability_multiplier": 1.08,
      "shaping": true
    }
  },
  "repair_item": "smog:tin_ingot",
  "molten": "tin"
}
```

### titanium

```json
{
  "id": "titanium",
  "name": {
    "en_us": "Titanium",
    "ru_ru": "Титан"
  },
  "color": "8F9FB3",
  "profiles": {
    "head": {
      "durability": 1300,
      "mining_speed": 8,
      "attack_damage": 4,
      "harvest_tier": "diamond",
      "casting": true,
      "shaping": false,
      "shaping_extra_units": 9000
    },
    "handle": {
      "forms": [
        "standard_handle"
      ],
      "shaping": true,
      "speed_multiplier": 1.04,
      "durability_multiplier": 1.12,
      "handling_multiplier": 1.02,
      "material_units": 4500
    },
    "binding": {
      "forms": [
        "clamp"
      ],
      "durability_multiplier": 1.08,
      "shaping": false
    }
  },
  "repair_item": "smog:titanium_ingot",
  "molten": "titanium"
}
```

### titanium_alloy

```json
{
  "id": "titanium_alloy",
  "name": {
    "en_us": "Titanium Alloy",
    "ru_ru": "Титановый сплав"
  },
  "color": "A9B9CB",
  "profiles": {
    "head": {
      "durability": 1700,
      "mining_speed": 8.5,
      "attack_damage": 4,
      "harvest_tier": "diamond",
      "casting": true,
      "shaping": false,
      "shaping_extra_units": 9000
    },
    "handle": {
      "forms": [
        "standard_handle"
      ],
      "shaping": true,
      "speed_multiplier": 1.06,
      "durability_multiplier": 1.18,
      "handling_multiplier": 1.04,
      "material_units": 4500
    },
    "binding": {
      "forms": [
        "clamp"
      ],
      "durability_multiplier": 1.08,
      "shaping": false
    }
  },
  "repair_item": "smog:titanium_alloy_ingot",
  "molten": "titanium_alloy"
}
```

### tool_steel

```json
{
  "id": "tool_steel",
  "name": {
    "en_us": "Tool Steel",
    "ru_ru": "Инструментальная сталь"
  },
  "color": "69747D",
  "profiles": {
    "head": {
      "durability": 1000,
      "mining_speed": 8,
      "attack_damage": 4,
      "harvest_tier": "diamond",
      "casting": true,
      "shaping": false,
      "shaping_extra_units": 9000
    },
    "handle": {
      "forms": [
        "standard_handle"
      ],
      "shaping": true,
      "speed_multiplier": 1,
      "durability_multiplier": 1.08,
      "handling_multiplier": 0.98,
      "material_units": 4500
    },
    "binding": {
      "forms": [
        "clamp"
      ],
      "durability_multiplier": 1.08,
      "shaping": false
    }
  },
  "repair_item": "smog:tool_steel_ingot",
  "molten": "tool_steel"
}
```

### tungsten

```json
{
  "id": "tungsten",
  "name": {
    "en_us": "Tungsten",
    "ru_ru": "Вольфрам"
  },
  "color": "6D7886",
  "profiles": {
    "head": {
      "durability": 1800,
      "mining_speed": 7,
      "attack_damage": 5,
      "harvest_tier": "diamond",
      "casting": true,
      "shaping": false,
      "shaping_extra_units": 9000
    },
    "handle": {
      "forms": [
        "standard_handle"
      ],
      "shaping": true,
      "speed_multiplier": 0.85,
      "durability_multiplier": 1.2,
      "handling_multiplier": 0.83,
      "material_units": 4500
    },
    "binding": {
      "forms": [
        "clamp"
      ],
      "durability_multiplier": 1.08,
      "shaping": false
    }
  },
  "repair_item": "smog:tungsten_ingot",
  "molten": "tungsten"
}
```

### tungsten_steel

```json
{
  "id": "tungsten_steel",
  "name": {
    "en_us": "Tungsten Steel",
    "ru_ru": "Вольфрамовая сталь"
  },
  "color": "535E6C",
  "profiles": {
    "head": {
      "durability": 1900,
      "mining_speed": 8,
      "attack_damage": 4,
      "harvest_tier": "diamond",
      "casting": true,
      "shaping": false,
      "shaping_extra_units": 9000
    },
    "handle": {
      "forms": [
        "standard_handle"
      ],
      "shaping": true,
      "speed_multiplier": 0.9,
      "durability_multiplier": 1.22,
      "handling_multiplier": 0.88,
      "material_units": 4500
    },
    "binding": {
      "forms": [
        "clamp"
      ],
      "durability_multiplier": 1.08,
      "shaping": false
    }
  },
  "repair_item": "smog:tungsten_steel_ingot",
  "molten": "tungsten_steel"
}
```

### uranium

```json
{
  "id": "uranium",
  "name": {
    "en_us": "Uranium",
    "ru_ru": "Уран"
  },
  "color": "7F964D",
  "profiles": {
    "head": {
      "durability": 220,
      "mining_speed": 4,
      "attack_damage": 2,
      "harvest_tier": "iron",
      "casting": true,
      "shaping": false,
      "shaping_extra_units": 9000
    },
    "handle": {
      "forms": [
        "standard_handle"
      ],
      "shaping": true,
      "speed_multiplier": 0.86,
      "durability_multiplier": 1.03,
      "handling_multiplier": 0.85,
      "material_units": 4500
    },
    "binding": {
      "forms": [
        "clamp"
      ],
      "durability_multiplier": 1.08,
      "shaping": false
    }
  },
  "repair_item": "smog:uranium_ingot",
  "molten": "uranium"
}
```

### wood

```json
{
  "id": "wood",
  "name": {
    "en_us": "Wood",
    "ru_ru": "Древесина"
  },
  "color": "B8884A",
  "profiles": {
    "handle": {
      "forms": [
        "standard_handle"
      ],
      "shaping": true,
      "speed_multiplier": 1,
      "durability_multiplier": 1,
      "handling_multiplier": 1,
      "material_units": 4500
    },
    "head": {
      "durability": 59,
      "mining_speed": 2,
      "attack_damage": 1,
      "harvest_tier": "wooden",
      "casting": false,
      "shaping": true,
      "traits": [
        {
          "id": "cultivation"
        }
      ]
    }
  },
  "repair_ingredient": "#minecraft:planks",
  "native_prefix": "wooden"
}
```

### wool

```json
{
  "id": "wool",
  "name": {
    "en_us": "Wool",
    "ru_ru": "Шерсть"
  },
  "color": "E6E6E8",
  "profiles": {
    "wrap": {
      "forms": [
        "wrap"
      ],
      "speed_multiplier": 1.02,
      "durability_multiplier": 1,
      "traits": [
        {
          "id": "insulation"
        }
      ]
    }
  },
  "repair_ingredient": "#minecraft:wool"
}
```

### wrought_iron

```json
{
  "id": "wrought_iron",
  "name": {
    "en_us": "Wrought Iron",
    "ru_ru": "Кричное железо"
  },
  "color": "A4A19A",
  "profiles": {
    "head": {
      "durability": 250,
      "mining_speed": 6,
      "attack_damage": 2,
      "harvest_tier": "iron",
      "casting": true,
      "shaping": false,
      "shaping_extra_units": 9000
    },
    "handle": {
      "forms": [
        "standard_handle"
      ],
      "shaping": true,
      "speed_multiplier": 1,
      "durability_multiplier": 1.08,
      "handling_multiplier": 0.98,
      "material_units": 4500
    },
    "binding": {
      "forms": [
        "clamp"
      ],
      "durability_multiplier": 1.08,
      "shaping": false
    }
  },
  "repair_item": "smog:wrought_iron_ingot",
  "molten": "wrought_iron"
}
```

### zinc

```json
{
  "id": "zinc",
  "name": {
    "en_us": "Zinc",
    "ru_ru": "Цинк"
  },
  "color": "B4B99D",
  "profiles": {
    "head": {
      "durability": 100,
      "mining_speed": 3.5,
      "attack_damage": 1,
      "harvest_tier": "stone",
      "casting": true,
      "shaping": true,
      "shaping_extra_units": 9000
    },
    "handle": {
      "forms": [
        "standard_handle"
      ],
      "shaping": true,
      "speed_multiplier": 0.95,
      "durability_multiplier": 0.93,
      "handling_multiplier": 0.97,
      "material_units": 4500
    },
    "binding": {
      "forms": [
        "clamp"
      ],
      "durability_multiplier": 1.08,
      "shaping": true
    }
  },
  "repair_item": "smog:zinc_ingot",
  "molten": "zinc"
}
```


## Формы деталей

### pickaxe_head

```json
{
  "id": "pickaxe_head",
  "category": "head",
  "name": {
    "en_us": "Pickaxe Head",
    "ru_ru": "Головка кирки"
  },
  "mining_tag": "minecraft:mineable/pickaxe",
  "source_texture": "minecraft:item/iron_pickaxe",
  "native_suffix": "pickaxe",
  "head_region": [
    4,
    0,
    16,
    10
  ],
  "material_units": 27000
}
```

### axe_head

```json
{
  "id": "axe_head",
  "category": "head",
  "name": {
    "en_us": "Axe Head",
    "ru_ru": "Головка топора"
  },
  "mining_tag": "minecraft:mineable/axe",
  "source_texture": "minecraft:item/iron_axe",
  "native_suffix": "axe",
  "head_region": [
    7,
    0,
    16,
    10
  ],
  "material_units": 27000
}
```

### shovel_head

```json
{
  "id": "shovel_head",
  "category": "head",
  "name": {
    "en_us": "Shovel Head",
    "ru_ru": "Головка лопаты"
  },
  "mining_tag": "minecraft:mineable/shovel",
  "source_texture": "minecraft:item/iron_shovel",
  "native_suffix": "shovel",
  "head_region": [
    8,
    0,
    16,
    9
  ],
  "material_units": 9000
}
```

### hoe_head

```json
{
  "id": "hoe_head",
  "category": "head",
  "name": {
    "en_us": "Hoe Head",
    "ru_ru": "Головка мотыги"
  },
  "mining_tag": "minecraft:mineable/hoe",
  "source_texture": "minecraft:item/iron_hoe",
  "native_suffix": "hoe",
  "head_region": [
    5,
    0,
    16,
    9
  ],
  "material_units": 18000
}
```

### hammer_head

```json
{
  "id": "hammer_head",
  "category": "head",
  "name": {
    "en_us": "Hammer Head",
    "ru_ru": "Головка молота"
  },
  "mining_tag": "minecraft:mineable/pickaxe",
  "prototype_shape": "hammer",
  "material_units": 27000
}
```

### standard_handle

```json
{
  "id": "standard_handle",
  "category": "handle",
  "variant": "standard",
  "name": {
    "en_us": "Rod",
    "ru_ru": "Стержень"
  },
  "aliases": {
    "wood": "wooden_handle"
  },
  "prototype_shape": "wooden_handle",
  "material_units": 4500
}
```

### binding

```json
{
  "id": "binding",
  "category": "binding",
  "name": {
    "en_us": "Binding",
    "ru_ru": "Крепление"
  },
  "aliases": {
    "cord": "cord_binding",
    "leather": "leather_binding"
  },
  "prototype_shape": "cord_binding",
  "material_units": 9000
}
```

### wrap

```json
{
  "id": "wrap",
  "category": "wrap",
  "name": {
    "en_us": "Grip Wrap",
    "ru_ru": "Обмотка"
  },
  "aliases": {
    "fiber": "fiber_wrap",
    "leather": "leather_wrap",
    "wool": "wool_wrap"
  },
  "prototype_shape": "fiber_wrap",
  "material_units": 4500
}
```

### clamp

```json
{
  "id": "clamp",
  "category": "binding",
  "name": {
    "en_us": "Clamp",
    "ru_ru": "Хомут"
  },
  "aliases": {
    "copper": "copper_clamp",
    "bronze": "bronze_clamp"
  },
  "prototype_shape": "copper_clamp",
  "material_units": 2250
}
```

### saw_head

```json
{
  "id": "saw_head",
  "category": "head",
  "name": {
    "en_us": "Saw Blade",
    "ru_ru": "Полотно пилы"
  },
  "prototype_shape": "saw",
  "material_units": 27000,
  "mining_tag": "minecraft:mineable/axe"
}
```

### sword_head

```json
{
  "id": "sword_head",
  "category": "head",
  "name": {
    "en_us": "Sword Blade",
    "ru_ru": "Клинок меча"
  },
  "prototype_shape": "sword",
  "material_units": 18000,
  "source_texture": "minecraft:item/iron_sword",
  "native_suffix": "sword",
  "head_region": [
    4,
    0,
    16,
    12
  ]
}
```

### katana_head

```json
{
  "id": "katana_head",
  "category": "head",
  "name": {
    "en_us": "Katana Blade",
    "ru_ru": "Клинок катаны"
  },
  "prototype_shape": "katana",
  "material_units": 18000
}
```

### machete_head

```json
{
  "id": "machete_head",
  "category": "head",
  "name": {
    "en_us": "Machete Blade",
    "ru_ru": "Клинок мачете"
  },
  "prototype_shape": "machete",
  "material_units": 27000,
  "mining_tag": "minecraft:mineable/axe"
}
```

### sickle_head

```json
{
  "id": "sickle_head",
  "category": "head",
  "name": {
    "en_us": "Sickle Blade",
    "ru_ru": "Лезвие серпа"
  },
  "prototype_shape": "sickle",
  "material_units": 18000,
  "mining_tag": "minecraft:mineable/hoe"
}
```


## Конструкции инструментов

### pickaxe

```json
{
  "id": "pickaxe",
  "behavior": "pickaxe",
  "name": {
    "en_us": "Pickaxe",
    "ru_ru": "Кирка"
  },
  "parts": [
    {
      "id": "head",
      "category": "head",
      "forms": [
        "pickaxe_head"
      ],
      "primary": true
    },
    {
      "id": "handle",
      "category": "handle",
      "forms": [
        "standard_handle"
      ]
    },
    {
      "id": "binding",
      "category": "binding"
    },
    {
      "id": "wrap",
      "category": "wrap",
      "optional": true
    }
  ],
  "max_support_speed_multiplier": 1.15,
  "max_support_durability_multiplier": 1.3,
  "attack_damage_bonus": 0,
  "attack_speed": 1.2,
  "head_damage_multiplier": 1,
  "mining_speed_multiplier": 1,
  "durability_multiplier": 1
}
```

### axe

```json
{
  "id": "axe",
  "behavior": "axe",
  "name": {
    "en_us": "Axe",
    "ru_ru": "Топор"
  },
  "parts": [
    {
      "id": "head",
      "category": "head",
      "forms": [
        "axe_head"
      ],
      "primary": true
    },
    {
      "id": "handle",
      "category": "handle",
      "forms": [
        "standard_handle"
      ]
    },
    {
      "id": "binding",
      "category": "binding"
    },
    {
      "id": "wrap",
      "category": "wrap",
      "optional": true
    }
  ],
  "max_support_speed_multiplier": 1.15,
  "max_support_durability_multiplier": 1.3,
  "attack_damage_bonus": 5,
  "attack_speed": 0.9,
  "head_damage_multiplier": 1,
  "mining_speed_multiplier": 1,
  "durability_multiplier": 1
}
```

### saw

```json
{
  "id": "saw",
  "behavior": "saw",
  "name": {
    "en_us": "Saw",
    "ru_ru": "Пила"
  },
  "parts": [
    {
      "id": "head",
      "category": "head",
      "forms": [
        "saw_head"
      ],
      "primary": true
    },
    {
      "id": "handle",
      "category": "handle",
      "forms": [
        "standard_handle"
      ]
    },
    {
      "id": "binding",
      "category": "binding"
    },
    {
      "id": "wrap",
      "category": "wrap",
      "optional": true
    }
  ],
  "max_support_speed_multiplier": 1.15,
  "max_support_durability_multiplier": 1.3,
  "attack_damage_bonus": 1,
  "attack_speed": 1,
  "head_damage_multiplier": 1,
  "mining_speed_multiplier": 0.7,
  "durability_multiplier": 1.6
}
```

### shovel

```json
{
  "id": "shovel",
  "behavior": "shovel",
  "name": {
    "en_us": "Shovel",
    "ru_ru": "Лопата"
  },
  "parts": [
    {
      "id": "head",
      "category": "head",
      "forms": [
        "shovel_head"
      ],
      "primary": true
    },
    {
      "id": "handle",
      "category": "handle",
      "forms": [
        "standard_handle"
      ]
    },
    {
      "id": "binding",
      "category": "binding"
    },
    {
      "id": "wrap",
      "category": "wrap",
      "optional": true
    }
  ],
  "max_support_speed_multiplier": 1.15,
  "max_support_durability_multiplier": 1.3,
  "attack_damage_bonus": 0.5,
  "attack_speed": 1,
  "head_damage_multiplier": 1,
  "mining_speed_multiplier": 1,
  "durability_multiplier": 1
}
```

### hammer

```json
{
  "id": "hammer",
  "behavior": "hammer",
  "name": {
    "en_us": "Hammer",
    "ru_ru": "Молот"
  },
  "parts": [
    {
      "id": "head",
      "category": "head",
      "forms": [
        "hammer_head"
      ],
      "primary": true
    },
    {
      "id": "handle",
      "category": "handle",
      "forms": [
        "standard_handle"
      ]
    },
    {
      "id": "binding",
      "category": "binding"
    },
    {
      "id": "wrap",
      "category": "wrap",
      "optional": true
    }
  ],
  "max_support_speed_multiplier": 1.15,
  "max_support_durability_multiplier": 1.3,
  "attack_damage_bonus": 3,
  "attack_speed": 0.8,
  "head_damage_multiplier": 1,
  "mining_speed_multiplier": 0.55,
  "durability_multiplier": 1.6
}
```

### hoe

```json
{
  "id": "hoe",
  "behavior": "hoe",
  "name": {
    "en_us": "Hoe",
    "ru_ru": "Мотыга"
  },
  "parts": [
    {
      "id": "head",
      "category": "head",
      "forms": [
        "hoe_head"
      ],
      "primary": true
    },
    {
      "id": "handle",
      "category": "handle",
      "forms": [
        "standard_handle"
      ]
    },
    {
      "id": "binding",
      "category": "binding"
    },
    {
      "id": "wrap",
      "category": "wrap",
      "optional": true
    }
  ],
  "max_support_speed_multiplier": 1.15,
  "max_support_durability_multiplier": 1.3,
  "attack_damage_bonus": 0,
  "attack_speed": 2.5,
  "head_damage_multiplier": 0,
  "mining_speed_multiplier": 1,
  "durability_multiplier": 1
}
```

### sword

```json
{
  "id": "sword",
  "behavior": "sword",
  "name": {
    "en_us": "Sword",
    "ru_ru": "Меч"
  },
  "parts": [
    {
      "id": "head",
      "category": "head",
      "forms": [
        "sword_head"
      ],
      "primary": true
    },
    {
      "id": "handle",
      "category": "handle",
      "forms": [
        "standard_handle"
      ]
    },
    {
      "id": "binding",
      "category": "binding"
    },
    {
      "id": "wrap",
      "category": "wrap",
      "optional": true
    }
  ],
  "max_support_speed_multiplier": 1.15,
  "max_support_durability_multiplier": 1.3,
  "attack_damage_bonus": 2,
  "attack_speed": 1.6,
  "head_damage_multiplier": 1,
  "mining_speed_multiplier": 0.35,
  "durability_multiplier": 1
}
```

### katana

```json
{
  "id": "katana",
  "behavior": "katana",
  "name": {
    "en_us": "Katana",
    "ru_ru": "Катана"
  },
  "parts": [
    {
      "id": "head",
      "category": "head",
      "forms": [
        "katana_head"
      ],
      "primary": true
    },
    {
      "id": "handle",
      "category": "handle",
      "forms": [
        "standard_handle"
      ]
    },
    {
      "id": "binding",
      "category": "binding"
    },
    {
      "id": "wrap",
      "category": "wrap",
      "optional": true
    }
  ],
  "max_support_speed_multiplier": 1.15,
  "max_support_durability_multiplier": 1.3,
  "attack_damage_bonus": 1,
  "attack_speed": 2,
  "head_damage_multiplier": 1,
  "mining_speed_multiplier": 0.35,
  "durability_multiplier": 0.85
}
```

### machete

```json
{
  "id": "machete",
  "behavior": "machete",
  "name": {
    "en_us": "Machete",
    "ru_ru": "Мачете"
  },
  "parts": [
    {
      "id": "head",
      "category": "head",
      "forms": [
        "machete_head"
      ],
      "primary": true
    },
    {
      "id": "handle",
      "category": "handle",
      "forms": [
        "standard_handle"
      ]
    },
    {
      "id": "binding",
      "category": "binding"
    },
    {
      "id": "wrap",
      "category": "wrap",
      "optional": true
    }
  ],
  "max_support_speed_multiplier": 1.15,
  "max_support_durability_multiplier": 1.3,
  "attack_damage_bonus": 3,
  "attack_speed": 1.1,
  "head_damage_multiplier": 1,
  "mining_speed_multiplier": 0.6,
  "durability_multiplier": 1.15
}
```

### sickle

```json
{
  "id": "sickle",
  "behavior": "sickle",
  "name": {
    "en_us": "Sickle",
    "ru_ru": "Серп"
  },
  "parts": [
    {
      "id": "head",
      "category": "head",
      "forms": [
        "sickle_head"
      ],
      "primary": true
    },
    {
      "id": "handle",
      "category": "handle",
      "forms": [
        "standard_handle"
      ]
    },
    {
      "id": "binding",
      "category": "binding"
    },
    {
      "id": "wrap",
      "category": "wrap",
      "optional": true
    }
  ],
  "max_support_speed_multiplier": 1.15,
  "max_support_durability_multiplier": 1.3,
  "attack_damage_bonus": 0,
  "attack_speed": 1.6,
  "head_damage_multiplier": 0,
  "mining_speed_multiplier": 1,
  "durability_multiplier": 1.25
}
```


## Каждая деталь инструмента

### smog:aluminum_pickaxe_head

```json
{
  "material": "aluminum",
  "form": "pickaxe_head",
  "cast": false
}
```

### smog:aluminum_cast_pickaxe_head

```json
{
  "material": "aluminum",
  "form": "pickaxe_head",
  "cast": true
}
```

### smog:aluminum_axe_head

```json
{
  "material": "aluminum",
  "form": "axe_head",
  "cast": false
}
```

### smog:aluminum_cast_axe_head

```json
{
  "material": "aluminum",
  "form": "axe_head",
  "cast": true
}
```

### smog:aluminum_shovel_head

```json
{
  "material": "aluminum",
  "form": "shovel_head",
  "cast": false
}
```

### smog:aluminum_cast_shovel_head

```json
{
  "material": "aluminum",
  "form": "shovel_head",
  "cast": true
}
```

### smog:aluminum_hoe_head

```json
{
  "material": "aluminum",
  "form": "hoe_head",
  "cast": false
}
```

### smog:aluminum_cast_hoe_head

```json
{
  "material": "aluminum",
  "form": "hoe_head",
  "cast": true
}
```

### smog:aluminum_hammer_head

```json
{
  "material": "aluminum",
  "form": "hammer_head",
  "cast": false
}
```

### smog:aluminum_cast_hammer_head

```json
{
  "material": "aluminum",
  "form": "hammer_head",
  "cast": true
}
```

### smog:aluminum_standard_handle

```json
{
  "material": "aluminum",
  "form": "standard_handle",
  "cast": false
}
```

### smog:aluminum_clamp

```json
{
  "material": "aluminum",
  "form": "clamp",
  "cast": false
}
```

### smog:aluminum_saw_head

```json
{
  "material": "aluminum",
  "form": "saw_head",
  "cast": false
}
```

### smog:aluminum_cast_saw_head

```json
{
  "material": "aluminum",
  "form": "saw_head",
  "cast": true
}
```

### smog:aluminum_sword_head

```json
{
  "material": "aluminum",
  "form": "sword_head",
  "cast": false
}
```

### smog:aluminum_cast_sword_head

```json
{
  "material": "aluminum",
  "form": "sword_head",
  "cast": true
}
```

### smog:aluminum_katana_head

```json
{
  "material": "aluminum",
  "form": "katana_head",
  "cast": false
}
```

### smog:aluminum_cast_katana_head

```json
{
  "material": "aluminum",
  "form": "katana_head",
  "cast": true
}
```

### smog:aluminum_machete_head

```json
{
  "material": "aluminum",
  "form": "machete_head",
  "cast": false
}
```

### smog:aluminum_cast_machete_head

```json
{
  "material": "aluminum",
  "form": "machete_head",
  "cast": true
}
```

### smog:aluminum_sickle_head

```json
{
  "material": "aluminum",
  "form": "sickle_head",
  "cast": false
}
```

### smog:aluminum_cast_sickle_head

```json
{
  "material": "aluminum",
  "form": "sickle_head",
  "cast": true
}
```

### smog:aluminum_bronze_pickaxe_head

```json
{
  "material": "aluminum_bronze",
  "form": "pickaxe_head",
  "cast": false
}
```

### smog:aluminum_bronze_cast_pickaxe_head

```json
{
  "material": "aluminum_bronze",
  "form": "pickaxe_head",
  "cast": true
}
```

### smog:aluminum_bronze_axe_head

```json
{
  "material": "aluminum_bronze",
  "form": "axe_head",
  "cast": false
}
```

### smog:aluminum_bronze_cast_axe_head

```json
{
  "material": "aluminum_bronze",
  "form": "axe_head",
  "cast": true
}
```

### smog:aluminum_bronze_shovel_head

```json
{
  "material": "aluminum_bronze",
  "form": "shovel_head",
  "cast": false
}
```

### smog:aluminum_bronze_cast_shovel_head

```json
{
  "material": "aluminum_bronze",
  "form": "shovel_head",
  "cast": true
}
```

### smog:aluminum_bronze_hoe_head

```json
{
  "material": "aluminum_bronze",
  "form": "hoe_head",
  "cast": false
}
```

### smog:aluminum_bronze_cast_hoe_head

```json
{
  "material": "aluminum_bronze",
  "form": "hoe_head",
  "cast": true
}
```

### smog:aluminum_bronze_hammer_head

```json
{
  "material": "aluminum_bronze",
  "form": "hammer_head",
  "cast": false
}
```

### smog:aluminum_bronze_cast_hammer_head

```json
{
  "material": "aluminum_bronze",
  "form": "hammer_head",
  "cast": true
}
```

### smog:aluminum_bronze_standard_handle

```json
{
  "material": "aluminum_bronze",
  "form": "standard_handle",
  "cast": false
}
```

### smog:aluminum_bronze_clamp

```json
{
  "material": "aluminum_bronze",
  "form": "clamp",
  "cast": false
}
```

### smog:aluminum_bronze_saw_head

```json
{
  "material": "aluminum_bronze",
  "form": "saw_head",
  "cast": false
}
```

### smog:aluminum_bronze_cast_saw_head

```json
{
  "material": "aluminum_bronze",
  "form": "saw_head",
  "cast": true
}
```

### smog:aluminum_bronze_sword_head

```json
{
  "material": "aluminum_bronze",
  "form": "sword_head",
  "cast": false
}
```

### smog:aluminum_bronze_cast_sword_head

```json
{
  "material": "aluminum_bronze",
  "form": "sword_head",
  "cast": true
}
```

### smog:aluminum_bronze_katana_head

```json
{
  "material": "aluminum_bronze",
  "form": "katana_head",
  "cast": false
}
```

### smog:aluminum_bronze_cast_katana_head

```json
{
  "material": "aluminum_bronze",
  "form": "katana_head",
  "cast": true
}
```

### smog:aluminum_bronze_machete_head

```json
{
  "material": "aluminum_bronze",
  "form": "machete_head",
  "cast": false
}
```

### smog:aluminum_bronze_cast_machete_head

```json
{
  "material": "aluminum_bronze",
  "form": "machete_head",
  "cast": true
}
```

### smog:aluminum_bronze_sickle_head

```json
{
  "material": "aluminum_bronze",
  "form": "sickle_head",
  "cast": false
}
```

### smog:aluminum_bronze_cast_sickle_head

```json
{
  "material": "aluminum_bronze",
  "form": "sickle_head",
  "cast": true
}
```

### smog:brass_pickaxe_head

```json
{
  "material": "brass",
  "form": "pickaxe_head",
  "cast": false
}
```

### smog:brass_cast_pickaxe_head

```json
{
  "material": "brass",
  "form": "pickaxe_head",
  "cast": true
}
```

### smog:brass_axe_head

```json
{
  "material": "brass",
  "form": "axe_head",
  "cast": false
}
```

### smog:brass_cast_axe_head

```json
{
  "material": "brass",
  "form": "axe_head",
  "cast": true
}
```

### smog:brass_shovel_head

```json
{
  "material": "brass",
  "form": "shovel_head",
  "cast": false
}
```

### smog:brass_cast_shovel_head

```json
{
  "material": "brass",
  "form": "shovel_head",
  "cast": true
}
```

### smog:brass_hoe_head

```json
{
  "material": "brass",
  "form": "hoe_head",
  "cast": false
}
```

### smog:brass_cast_hoe_head

```json
{
  "material": "brass",
  "form": "hoe_head",
  "cast": true
}
```

### smog:brass_hammer_head

```json
{
  "material": "brass",
  "form": "hammer_head",
  "cast": false
}
```

### smog:brass_cast_hammer_head

```json
{
  "material": "brass",
  "form": "hammer_head",
  "cast": true
}
```

### smog:brass_standard_handle

```json
{
  "material": "brass",
  "form": "standard_handle",
  "cast": false
}
```

### smog:brass_clamp

```json
{
  "material": "brass",
  "form": "clamp",
  "cast": false
}
```

### smog:brass_saw_head

```json
{
  "material": "brass",
  "form": "saw_head",
  "cast": false
}
```

### smog:brass_cast_saw_head

```json
{
  "material": "brass",
  "form": "saw_head",
  "cast": true
}
```

### smog:brass_sword_head

```json
{
  "material": "brass",
  "form": "sword_head",
  "cast": false
}
```

### smog:brass_cast_sword_head

```json
{
  "material": "brass",
  "form": "sword_head",
  "cast": true
}
```

### smog:brass_katana_head

```json
{
  "material": "brass",
  "form": "katana_head",
  "cast": false
}
```

### smog:brass_cast_katana_head

```json
{
  "material": "brass",
  "form": "katana_head",
  "cast": true
}
```

### smog:brass_machete_head

```json
{
  "material": "brass",
  "form": "machete_head",
  "cast": false
}
```

### smog:brass_cast_machete_head

```json
{
  "material": "brass",
  "form": "machete_head",
  "cast": true
}
```

### smog:brass_sickle_head

```json
{
  "material": "brass",
  "form": "sickle_head",
  "cast": false
}
```

### smog:brass_cast_sickle_head

```json
{
  "material": "brass",
  "form": "sickle_head",
  "cast": true
}
```

### smog:bronze_pickaxe_head

```json
{
  "material": "bronze",
  "form": "pickaxe_head",
  "cast": false
}
```

### smog:bronze_cast_pickaxe_head

```json
{
  "material": "bronze",
  "form": "pickaxe_head",
  "cast": true
}
```

### smog:bronze_axe_head

```json
{
  "material": "bronze",
  "form": "axe_head",
  "cast": false
}
```

### smog:bronze_cast_axe_head

```json
{
  "material": "bronze",
  "form": "axe_head",
  "cast": true
}
```

### smog:bronze_shovel_head

```json
{
  "material": "bronze",
  "form": "shovel_head",
  "cast": false
}
```

### smog:bronze_cast_shovel_head

```json
{
  "material": "bronze",
  "form": "shovel_head",
  "cast": true
}
```

### smog:bronze_hoe_head

```json
{
  "material": "bronze",
  "form": "hoe_head",
  "cast": false
}
```

### smog:bronze_cast_hoe_head

```json
{
  "material": "bronze",
  "form": "hoe_head",
  "cast": true
}
```

### smog:bronze_hammer_head

```json
{
  "material": "bronze",
  "form": "hammer_head",
  "cast": false
}
```

### smog:bronze_cast_hammer_head

```json
{
  "material": "bronze",
  "form": "hammer_head",
  "cast": true
}
```

### smog:bronze_standard_handle

```json
{
  "material": "bronze",
  "form": "standard_handle",
  "cast": false
}
```

### smog:bronze_clamp

```json
{
  "material": "bronze",
  "form": "clamp",
  "cast": false
}
```

### smog:bronze_saw_head

```json
{
  "material": "bronze",
  "form": "saw_head",
  "cast": false
}
```

### smog:bronze_cast_saw_head

```json
{
  "material": "bronze",
  "form": "saw_head",
  "cast": true
}
```

### smog:bronze_sword_head

```json
{
  "material": "bronze",
  "form": "sword_head",
  "cast": false
}
```

### smog:bronze_cast_sword_head

```json
{
  "material": "bronze",
  "form": "sword_head",
  "cast": true
}
```

### smog:bronze_katana_head

```json
{
  "material": "bronze",
  "form": "katana_head",
  "cast": false
}
```

### smog:bronze_cast_katana_head

```json
{
  "material": "bronze",
  "form": "katana_head",
  "cast": true
}
```

### smog:bronze_machete_head

```json
{
  "material": "bronze",
  "form": "machete_head",
  "cast": false
}
```

### smog:bronze_cast_machete_head

```json
{
  "material": "bronze",
  "form": "machete_head",
  "cast": true
}
```

### smog:bronze_sickle_head

```json
{
  "material": "bronze",
  "form": "sickle_head",
  "cast": false
}
```

### smog:bronze_cast_sickle_head

```json
{
  "material": "bronze",
  "form": "sickle_head",
  "cast": true
}
```

### smog:cast_iron_pickaxe_head

```json
{
  "material": "cast_iron",
  "form": "pickaxe_head",
  "cast": false
}
```

### smog:cast_iron_cast_pickaxe_head

```json
{
  "material": "cast_iron",
  "form": "pickaxe_head",
  "cast": true
}
```

### smog:cast_iron_axe_head

```json
{
  "material": "cast_iron",
  "form": "axe_head",
  "cast": false
}
```

### smog:cast_iron_cast_axe_head

```json
{
  "material": "cast_iron",
  "form": "axe_head",
  "cast": true
}
```

### smog:cast_iron_shovel_head

```json
{
  "material": "cast_iron",
  "form": "shovel_head",
  "cast": false
}
```

### smog:cast_iron_cast_shovel_head

```json
{
  "material": "cast_iron",
  "form": "shovel_head",
  "cast": true
}
```

### smog:cast_iron_hoe_head

```json
{
  "material": "cast_iron",
  "form": "hoe_head",
  "cast": false
}
```

### smog:cast_iron_cast_hoe_head

```json
{
  "material": "cast_iron",
  "form": "hoe_head",
  "cast": true
}
```

### smog:cast_iron_hammer_head

```json
{
  "material": "cast_iron",
  "form": "hammer_head",
  "cast": false
}
```

### smog:cast_iron_cast_hammer_head

```json
{
  "material": "cast_iron",
  "form": "hammer_head",
  "cast": true
}
```

### smog:cast_iron_standard_handle

```json
{
  "material": "cast_iron",
  "form": "standard_handle",
  "cast": false
}
```

### smog:cast_iron_clamp

```json
{
  "material": "cast_iron",
  "form": "clamp",
  "cast": false
}
```

### smog:cast_iron_saw_head

```json
{
  "material": "cast_iron",
  "form": "saw_head",
  "cast": false
}
```

### smog:cast_iron_cast_saw_head

```json
{
  "material": "cast_iron",
  "form": "saw_head",
  "cast": true
}
```

### smog:cast_iron_sword_head

```json
{
  "material": "cast_iron",
  "form": "sword_head",
  "cast": false
}
```

### smog:cast_iron_cast_sword_head

```json
{
  "material": "cast_iron",
  "form": "sword_head",
  "cast": true
}
```

### smog:cast_iron_katana_head

```json
{
  "material": "cast_iron",
  "form": "katana_head",
  "cast": false
}
```

### smog:cast_iron_cast_katana_head

```json
{
  "material": "cast_iron",
  "form": "katana_head",
  "cast": true
}
```

### smog:cast_iron_machete_head

```json
{
  "material": "cast_iron",
  "form": "machete_head",
  "cast": false
}
```

### smog:cast_iron_cast_machete_head

```json
{
  "material": "cast_iron",
  "form": "machete_head",
  "cast": true
}
```

### smog:cast_iron_sickle_head

```json
{
  "material": "cast_iron",
  "form": "sickle_head",
  "cast": false
}
```

### smog:cast_iron_cast_sickle_head

```json
{
  "material": "cast_iron",
  "form": "sickle_head",
  "cast": true
}
```

### smog:chromium_pickaxe_head

```json
{
  "material": "chromium",
  "form": "pickaxe_head",
  "cast": false
}
```

### smog:chromium_cast_pickaxe_head

```json
{
  "material": "chromium",
  "form": "pickaxe_head",
  "cast": true
}
```

### smog:chromium_axe_head

```json
{
  "material": "chromium",
  "form": "axe_head",
  "cast": false
}
```

### smog:chromium_cast_axe_head

```json
{
  "material": "chromium",
  "form": "axe_head",
  "cast": true
}
```

### smog:chromium_shovel_head

```json
{
  "material": "chromium",
  "form": "shovel_head",
  "cast": false
}
```

### smog:chromium_cast_shovel_head

```json
{
  "material": "chromium",
  "form": "shovel_head",
  "cast": true
}
```

### smog:chromium_hoe_head

```json
{
  "material": "chromium",
  "form": "hoe_head",
  "cast": false
}
```

### smog:chromium_cast_hoe_head

```json
{
  "material": "chromium",
  "form": "hoe_head",
  "cast": true
}
```

### smog:chromium_hammer_head

```json
{
  "material": "chromium",
  "form": "hammer_head",
  "cast": false
}
```

### smog:chromium_cast_hammer_head

```json
{
  "material": "chromium",
  "form": "hammer_head",
  "cast": true
}
```

### smog:chromium_standard_handle

```json
{
  "material": "chromium",
  "form": "standard_handle",
  "cast": false
}
```

### smog:chromium_clamp

```json
{
  "material": "chromium",
  "form": "clamp",
  "cast": false
}
```

### smog:chromium_saw_head

```json
{
  "material": "chromium",
  "form": "saw_head",
  "cast": false
}
```

### smog:chromium_cast_saw_head

```json
{
  "material": "chromium",
  "form": "saw_head",
  "cast": true
}
```

### smog:chromium_sword_head

```json
{
  "material": "chromium",
  "form": "sword_head",
  "cast": false
}
```

### smog:chromium_cast_sword_head

```json
{
  "material": "chromium",
  "form": "sword_head",
  "cast": true
}
```

### smog:chromium_katana_head

```json
{
  "material": "chromium",
  "form": "katana_head",
  "cast": false
}
```

### smog:chromium_cast_katana_head

```json
{
  "material": "chromium",
  "form": "katana_head",
  "cast": true
}
```

### smog:chromium_machete_head

```json
{
  "material": "chromium",
  "form": "machete_head",
  "cast": false
}
```

### smog:chromium_cast_machete_head

```json
{
  "material": "chromium",
  "form": "machete_head",
  "cast": true
}
```

### smog:chromium_sickle_head

```json
{
  "material": "chromium",
  "form": "sickle_head",
  "cast": false
}
```

### smog:chromium_cast_sickle_head

```json
{
  "material": "chromium",
  "form": "sickle_head",
  "cast": true
}
```

### smog:constantan_pickaxe_head

```json
{
  "material": "constantan",
  "form": "pickaxe_head",
  "cast": false
}
```

### smog:constantan_cast_pickaxe_head

```json
{
  "material": "constantan",
  "form": "pickaxe_head",
  "cast": true
}
```

### smog:constantan_axe_head

```json
{
  "material": "constantan",
  "form": "axe_head",
  "cast": false
}
```

### smog:constantan_cast_axe_head

```json
{
  "material": "constantan",
  "form": "axe_head",
  "cast": true
}
```

### smog:constantan_shovel_head

```json
{
  "material": "constantan",
  "form": "shovel_head",
  "cast": false
}
```

### smog:constantan_cast_shovel_head

```json
{
  "material": "constantan",
  "form": "shovel_head",
  "cast": true
}
```

### smog:constantan_hoe_head

```json
{
  "material": "constantan",
  "form": "hoe_head",
  "cast": false
}
```

### smog:constantan_cast_hoe_head

```json
{
  "material": "constantan",
  "form": "hoe_head",
  "cast": true
}
```

### smog:constantan_hammer_head

```json
{
  "material": "constantan",
  "form": "hammer_head",
  "cast": false
}
```

### smog:constantan_cast_hammer_head

```json
{
  "material": "constantan",
  "form": "hammer_head",
  "cast": true
}
```

### smog:constantan_standard_handle

```json
{
  "material": "constantan",
  "form": "standard_handle",
  "cast": false
}
```

### smog:constantan_clamp

```json
{
  "material": "constantan",
  "form": "clamp",
  "cast": false
}
```

### smog:constantan_saw_head

```json
{
  "material": "constantan",
  "form": "saw_head",
  "cast": false
}
```

### smog:constantan_cast_saw_head

```json
{
  "material": "constantan",
  "form": "saw_head",
  "cast": true
}
```

### smog:constantan_sword_head

```json
{
  "material": "constantan",
  "form": "sword_head",
  "cast": false
}
```

### smog:constantan_cast_sword_head

```json
{
  "material": "constantan",
  "form": "sword_head",
  "cast": true
}
```

### smog:constantan_katana_head

```json
{
  "material": "constantan",
  "form": "katana_head",
  "cast": false
}
```

### smog:constantan_cast_katana_head

```json
{
  "material": "constantan",
  "form": "katana_head",
  "cast": true
}
```

### smog:constantan_machete_head

```json
{
  "material": "constantan",
  "form": "machete_head",
  "cast": false
}
```

### smog:constantan_cast_machete_head

```json
{
  "material": "constantan",
  "form": "machete_head",
  "cast": true
}
```

### smog:constantan_sickle_head

```json
{
  "material": "constantan",
  "form": "sickle_head",
  "cast": false
}
```

### smog:constantan_cast_sickle_head

```json
{
  "material": "constantan",
  "form": "sickle_head",
  "cast": true
}
```

### smog:copper_pickaxe_head

```json
{
  "material": "copper",
  "form": "pickaxe_head",
  "cast": false
}
```

### smog:copper_cast_pickaxe_head

```json
{
  "material": "copper",
  "form": "pickaxe_head",
  "cast": true
}
```

### smog:copper_axe_head

```json
{
  "material": "copper",
  "form": "axe_head",
  "cast": false
}
```

### smog:copper_cast_axe_head

```json
{
  "material": "copper",
  "form": "axe_head",
  "cast": true
}
```

### smog:copper_shovel_head

```json
{
  "material": "copper",
  "form": "shovel_head",
  "cast": false
}
```

### smog:copper_cast_shovel_head

```json
{
  "material": "copper",
  "form": "shovel_head",
  "cast": true
}
```

### smog:copper_hoe_head

```json
{
  "material": "copper",
  "form": "hoe_head",
  "cast": false
}
```

### smog:copper_cast_hoe_head

```json
{
  "material": "copper",
  "form": "hoe_head",
  "cast": true
}
```

### smog:copper_hammer_head

```json
{
  "material": "copper",
  "form": "hammer_head",
  "cast": false
}
```

### smog:copper_cast_hammer_head

```json
{
  "material": "copper",
  "form": "hammer_head",
  "cast": true
}
```

### smog:copper_standard_handle

```json
{
  "material": "copper",
  "form": "standard_handle",
  "cast": false
}
```

### smog:copper_clamp

```json
{
  "material": "copper",
  "form": "clamp",
  "cast": false
}
```

### smog:copper_saw_head

```json
{
  "material": "copper",
  "form": "saw_head",
  "cast": false
}
```

### smog:copper_cast_saw_head

```json
{
  "material": "copper",
  "form": "saw_head",
  "cast": true
}
```

### smog:copper_sword_head

```json
{
  "material": "copper",
  "form": "sword_head",
  "cast": false
}
```

### smog:copper_cast_sword_head

```json
{
  "material": "copper",
  "form": "sword_head",
  "cast": true
}
```

### smog:copper_katana_head

```json
{
  "material": "copper",
  "form": "katana_head",
  "cast": false
}
```

### smog:copper_cast_katana_head

```json
{
  "material": "copper",
  "form": "katana_head",
  "cast": true
}
```

### smog:copper_machete_head

```json
{
  "material": "copper",
  "form": "machete_head",
  "cast": false
}
```

### smog:copper_cast_machete_head

```json
{
  "material": "copper",
  "form": "machete_head",
  "cast": true
}
```

### smog:copper_sickle_head

```json
{
  "material": "copper",
  "form": "sickle_head",
  "cast": false
}
```

### smog:copper_cast_sickle_head

```json
{
  "material": "copper",
  "form": "sickle_head",
  "cast": true
}
```

### smog:cord_binding

```json
{
  "material": "cord",
  "form": "binding",
  "cast": false
}
```

### smog:cupronickel_pickaxe_head

```json
{
  "material": "cupronickel",
  "form": "pickaxe_head",
  "cast": false
}
```

### smog:cupronickel_cast_pickaxe_head

```json
{
  "material": "cupronickel",
  "form": "pickaxe_head",
  "cast": true
}
```

### smog:cupronickel_axe_head

```json
{
  "material": "cupronickel",
  "form": "axe_head",
  "cast": false
}
```

### smog:cupronickel_cast_axe_head

```json
{
  "material": "cupronickel",
  "form": "axe_head",
  "cast": true
}
```

### smog:cupronickel_shovel_head

```json
{
  "material": "cupronickel",
  "form": "shovel_head",
  "cast": false
}
```

### smog:cupronickel_cast_shovel_head

```json
{
  "material": "cupronickel",
  "form": "shovel_head",
  "cast": true
}
```

### smog:cupronickel_hoe_head

```json
{
  "material": "cupronickel",
  "form": "hoe_head",
  "cast": false
}
```

### smog:cupronickel_cast_hoe_head

```json
{
  "material": "cupronickel",
  "form": "hoe_head",
  "cast": true
}
```

### smog:cupronickel_hammer_head

```json
{
  "material": "cupronickel",
  "form": "hammer_head",
  "cast": false
}
```

### smog:cupronickel_cast_hammer_head

```json
{
  "material": "cupronickel",
  "form": "hammer_head",
  "cast": true
}
```

### smog:cupronickel_standard_handle

```json
{
  "material": "cupronickel",
  "form": "standard_handle",
  "cast": false
}
```

### smog:cupronickel_clamp

```json
{
  "material": "cupronickel",
  "form": "clamp",
  "cast": false
}
```

### smog:cupronickel_saw_head

```json
{
  "material": "cupronickel",
  "form": "saw_head",
  "cast": false
}
```

### smog:cupronickel_cast_saw_head

```json
{
  "material": "cupronickel",
  "form": "saw_head",
  "cast": true
}
```

### smog:cupronickel_sword_head

```json
{
  "material": "cupronickel",
  "form": "sword_head",
  "cast": false
}
```

### smog:cupronickel_cast_sword_head

```json
{
  "material": "cupronickel",
  "form": "sword_head",
  "cast": true
}
```

### smog:cupronickel_katana_head

```json
{
  "material": "cupronickel",
  "form": "katana_head",
  "cast": false
}
```

### smog:cupronickel_cast_katana_head

```json
{
  "material": "cupronickel",
  "form": "katana_head",
  "cast": true
}
```

### smog:cupronickel_machete_head

```json
{
  "material": "cupronickel",
  "form": "machete_head",
  "cast": false
}
```

### smog:cupronickel_cast_machete_head

```json
{
  "material": "cupronickel",
  "form": "machete_head",
  "cast": true
}
```

### smog:cupronickel_sickle_head

```json
{
  "material": "cupronickel",
  "form": "sickle_head",
  "cast": false
}
```

### smog:cupronickel_cast_sickle_head

```json
{
  "material": "cupronickel",
  "form": "sickle_head",
  "cast": true
}
```

### smog:duralumin_pickaxe_head

```json
{
  "material": "duralumin",
  "form": "pickaxe_head",
  "cast": false
}
```

### smog:duralumin_cast_pickaxe_head

```json
{
  "material": "duralumin",
  "form": "pickaxe_head",
  "cast": true
}
```

### smog:duralumin_axe_head

```json
{
  "material": "duralumin",
  "form": "axe_head",
  "cast": false
}
```

### smog:duralumin_cast_axe_head

```json
{
  "material": "duralumin",
  "form": "axe_head",
  "cast": true
}
```

### smog:duralumin_shovel_head

```json
{
  "material": "duralumin",
  "form": "shovel_head",
  "cast": false
}
```

### smog:duralumin_cast_shovel_head

```json
{
  "material": "duralumin",
  "form": "shovel_head",
  "cast": true
}
```

### smog:duralumin_hoe_head

```json
{
  "material": "duralumin",
  "form": "hoe_head",
  "cast": false
}
```

### smog:duralumin_cast_hoe_head

```json
{
  "material": "duralumin",
  "form": "hoe_head",
  "cast": true
}
```

### smog:duralumin_hammer_head

```json
{
  "material": "duralumin",
  "form": "hammer_head",
  "cast": false
}
```

### smog:duralumin_cast_hammer_head

```json
{
  "material": "duralumin",
  "form": "hammer_head",
  "cast": true
}
```

### smog:duralumin_standard_handle

```json
{
  "material": "duralumin",
  "form": "standard_handle",
  "cast": false
}
```

### smog:duralumin_clamp

```json
{
  "material": "duralumin",
  "form": "clamp",
  "cast": false
}
```

### smog:duralumin_saw_head

```json
{
  "material": "duralumin",
  "form": "saw_head",
  "cast": false
}
```

### smog:duralumin_cast_saw_head

```json
{
  "material": "duralumin",
  "form": "saw_head",
  "cast": true
}
```

### smog:duralumin_sword_head

```json
{
  "material": "duralumin",
  "form": "sword_head",
  "cast": false
}
```

### smog:duralumin_cast_sword_head

```json
{
  "material": "duralumin",
  "form": "sword_head",
  "cast": true
}
```

### smog:duralumin_katana_head

```json
{
  "material": "duralumin",
  "form": "katana_head",
  "cast": false
}
```

### smog:duralumin_cast_katana_head

```json
{
  "material": "duralumin",
  "form": "katana_head",
  "cast": true
}
```

### smog:duralumin_machete_head

```json
{
  "material": "duralumin",
  "form": "machete_head",
  "cast": false
}
```

### smog:duralumin_cast_machete_head

```json
{
  "material": "duralumin",
  "form": "machete_head",
  "cast": true
}
```

### smog:duralumin_sickle_head

```json
{
  "material": "duralumin",
  "form": "sickle_head",
  "cast": false
}
```

### smog:duralumin_cast_sickle_head

```json
{
  "material": "duralumin",
  "form": "sickle_head",
  "cast": true
}
```

### smog:electrum_pickaxe_head

```json
{
  "material": "electrum",
  "form": "pickaxe_head",
  "cast": false
}
```

### smog:electrum_cast_pickaxe_head

```json
{
  "material": "electrum",
  "form": "pickaxe_head",
  "cast": true
}
```

### smog:electrum_axe_head

```json
{
  "material": "electrum",
  "form": "axe_head",
  "cast": false
}
```

### smog:electrum_cast_axe_head

```json
{
  "material": "electrum",
  "form": "axe_head",
  "cast": true
}
```

### smog:electrum_shovel_head

```json
{
  "material": "electrum",
  "form": "shovel_head",
  "cast": false
}
```

### smog:electrum_cast_shovel_head

```json
{
  "material": "electrum",
  "form": "shovel_head",
  "cast": true
}
```

### smog:electrum_hoe_head

```json
{
  "material": "electrum",
  "form": "hoe_head",
  "cast": false
}
```

### smog:electrum_cast_hoe_head

```json
{
  "material": "electrum",
  "form": "hoe_head",
  "cast": true
}
```

### smog:electrum_hammer_head

```json
{
  "material": "electrum",
  "form": "hammer_head",
  "cast": false
}
```

### smog:electrum_cast_hammer_head

```json
{
  "material": "electrum",
  "form": "hammer_head",
  "cast": true
}
```

### smog:electrum_standard_handle

```json
{
  "material": "electrum",
  "form": "standard_handle",
  "cast": false
}
```

### smog:electrum_clamp

```json
{
  "material": "electrum",
  "form": "clamp",
  "cast": false
}
```

### smog:electrum_saw_head

```json
{
  "material": "electrum",
  "form": "saw_head",
  "cast": false
}
```

### smog:electrum_cast_saw_head

```json
{
  "material": "electrum",
  "form": "saw_head",
  "cast": true
}
```

### smog:electrum_sword_head

```json
{
  "material": "electrum",
  "form": "sword_head",
  "cast": false
}
```

### smog:electrum_cast_sword_head

```json
{
  "material": "electrum",
  "form": "sword_head",
  "cast": true
}
```

### smog:electrum_katana_head

```json
{
  "material": "electrum",
  "form": "katana_head",
  "cast": false
}
```

### smog:electrum_cast_katana_head

```json
{
  "material": "electrum",
  "form": "katana_head",
  "cast": true
}
```

### smog:electrum_machete_head

```json
{
  "material": "electrum",
  "form": "machete_head",
  "cast": false
}
```

### smog:electrum_cast_machete_head

```json
{
  "material": "electrum",
  "form": "machete_head",
  "cast": true
}
```

### smog:electrum_sickle_head

```json
{
  "material": "electrum",
  "form": "sickle_head",
  "cast": false
}
```

### smog:electrum_cast_sickle_head

```json
{
  "material": "electrum",
  "form": "sickle_head",
  "cast": true
}
```

### smog:fiber_wrap

```json
{
  "material": "fiber",
  "form": "wrap",
  "cast": false
}
```

### smog:gold_pickaxe_head

```json
{
  "material": "gold",
  "form": "pickaxe_head",
  "cast": false
}
```

### smog:gold_cast_pickaxe_head

```json
{
  "material": "gold",
  "form": "pickaxe_head",
  "cast": true
}
```

### smog:gold_axe_head

```json
{
  "material": "gold",
  "form": "axe_head",
  "cast": false
}
```

### smog:gold_cast_axe_head

```json
{
  "material": "gold",
  "form": "axe_head",
  "cast": true
}
```

### smog:gold_shovel_head

```json
{
  "material": "gold",
  "form": "shovel_head",
  "cast": false
}
```

### smog:gold_cast_shovel_head

```json
{
  "material": "gold",
  "form": "shovel_head",
  "cast": true
}
```

### smog:gold_hoe_head

```json
{
  "material": "gold",
  "form": "hoe_head",
  "cast": false
}
```

### smog:gold_cast_hoe_head

```json
{
  "material": "gold",
  "form": "hoe_head",
  "cast": true
}
```

### smog:gold_hammer_head

```json
{
  "material": "gold",
  "form": "hammer_head",
  "cast": false
}
```

### smog:gold_cast_hammer_head

```json
{
  "material": "gold",
  "form": "hammer_head",
  "cast": true
}
```

### smog:gold_standard_handle

```json
{
  "material": "gold",
  "form": "standard_handle",
  "cast": false
}
```

### smog:gold_clamp

```json
{
  "material": "gold",
  "form": "clamp",
  "cast": false
}
```

### smog:gold_saw_head

```json
{
  "material": "gold",
  "form": "saw_head",
  "cast": false
}
```

### smog:gold_cast_saw_head

```json
{
  "material": "gold",
  "form": "saw_head",
  "cast": true
}
```

### smog:gold_sword_head

```json
{
  "material": "gold",
  "form": "sword_head",
  "cast": false
}
```

### smog:gold_cast_sword_head

```json
{
  "material": "gold",
  "form": "sword_head",
  "cast": true
}
```

### smog:gold_katana_head

```json
{
  "material": "gold",
  "form": "katana_head",
  "cast": false
}
```

### smog:gold_cast_katana_head

```json
{
  "material": "gold",
  "form": "katana_head",
  "cast": true
}
```

### smog:gold_machete_head

```json
{
  "material": "gold",
  "form": "machete_head",
  "cast": false
}
```

### smog:gold_cast_machete_head

```json
{
  "material": "gold",
  "form": "machete_head",
  "cast": true
}
```

### smog:gold_sickle_head

```json
{
  "material": "gold",
  "form": "sickle_head",
  "cast": false
}
```

### smog:gold_cast_sickle_head

```json
{
  "material": "gold",
  "form": "sickle_head",
  "cast": true
}
```

### smog:invar_pickaxe_head

```json
{
  "material": "invar",
  "form": "pickaxe_head",
  "cast": false
}
```

### smog:invar_cast_pickaxe_head

```json
{
  "material": "invar",
  "form": "pickaxe_head",
  "cast": true
}
```

### smog:invar_axe_head

```json
{
  "material": "invar",
  "form": "axe_head",
  "cast": false
}
```

### smog:invar_cast_axe_head

```json
{
  "material": "invar",
  "form": "axe_head",
  "cast": true
}
```

### smog:invar_shovel_head

```json
{
  "material": "invar",
  "form": "shovel_head",
  "cast": false
}
```

### smog:invar_cast_shovel_head

```json
{
  "material": "invar",
  "form": "shovel_head",
  "cast": true
}
```

### smog:invar_hoe_head

```json
{
  "material": "invar",
  "form": "hoe_head",
  "cast": false
}
```

### smog:invar_cast_hoe_head

```json
{
  "material": "invar",
  "form": "hoe_head",
  "cast": true
}
```

### smog:invar_hammer_head

```json
{
  "material": "invar",
  "form": "hammer_head",
  "cast": false
}
```

### smog:invar_cast_hammer_head

```json
{
  "material": "invar",
  "form": "hammer_head",
  "cast": true
}
```

### smog:invar_standard_handle

```json
{
  "material": "invar",
  "form": "standard_handle",
  "cast": false
}
```

### smog:invar_clamp

```json
{
  "material": "invar",
  "form": "clamp",
  "cast": false
}
```

### smog:invar_saw_head

```json
{
  "material": "invar",
  "form": "saw_head",
  "cast": false
}
```

### smog:invar_cast_saw_head

```json
{
  "material": "invar",
  "form": "saw_head",
  "cast": true
}
```

### smog:invar_sword_head

```json
{
  "material": "invar",
  "form": "sword_head",
  "cast": false
}
```

### smog:invar_cast_sword_head

```json
{
  "material": "invar",
  "form": "sword_head",
  "cast": true
}
```

### smog:invar_katana_head

```json
{
  "material": "invar",
  "form": "katana_head",
  "cast": false
}
```

### smog:invar_cast_katana_head

```json
{
  "material": "invar",
  "form": "katana_head",
  "cast": true
}
```

### smog:invar_machete_head

```json
{
  "material": "invar",
  "form": "machete_head",
  "cast": false
}
```

### smog:invar_cast_machete_head

```json
{
  "material": "invar",
  "form": "machete_head",
  "cast": true
}
```

### smog:invar_sickle_head

```json
{
  "material": "invar",
  "form": "sickle_head",
  "cast": false
}
```

### smog:invar_cast_sickle_head

```json
{
  "material": "invar",
  "form": "sickle_head",
  "cast": true
}
```

### smog:iron_pickaxe_head

```json
{
  "material": "iron",
  "form": "pickaxe_head",
  "cast": false
}
```

### smog:iron_axe_head

```json
{
  "material": "iron",
  "form": "axe_head",
  "cast": false
}
```

### smog:iron_shovel_head

```json
{
  "material": "iron",
  "form": "shovel_head",
  "cast": false
}
```

### smog:iron_hoe_head

```json
{
  "material": "iron",
  "form": "hoe_head",
  "cast": false
}
```

### smog:iron_hammer_head

```json
{
  "material": "iron",
  "form": "hammer_head",
  "cast": false
}
```

### smog:iron_standard_handle

```json
{
  "material": "iron",
  "form": "standard_handle",
  "cast": false
}
```

### smog:iron_clamp

```json
{
  "material": "iron",
  "form": "clamp",
  "cast": false
}
```

### smog:iron_saw_head

```json
{
  "material": "iron",
  "form": "saw_head",
  "cast": false
}
```

### smog:iron_sword_head

```json
{
  "material": "iron",
  "form": "sword_head",
  "cast": false
}
```

### smog:iron_katana_head

```json
{
  "material": "iron",
  "form": "katana_head",
  "cast": false
}
```

### smog:iron_machete_head

```json
{
  "material": "iron",
  "form": "machete_head",
  "cast": false
}
```

### smog:iron_sickle_head

```json
{
  "material": "iron",
  "form": "sickle_head",
  "cast": false
}
```

### smog:lead_pickaxe_head

```json
{
  "material": "lead",
  "form": "pickaxe_head",
  "cast": false
}
```

### smog:lead_cast_pickaxe_head

```json
{
  "material": "lead",
  "form": "pickaxe_head",
  "cast": true
}
```

### smog:lead_axe_head

```json
{
  "material": "lead",
  "form": "axe_head",
  "cast": false
}
```

### smog:lead_cast_axe_head

```json
{
  "material": "lead",
  "form": "axe_head",
  "cast": true
}
```

### smog:lead_shovel_head

```json
{
  "material": "lead",
  "form": "shovel_head",
  "cast": false
}
```

### smog:lead_cast_shovel_head

```json
{
  "material": "lead",
  "form": "shovel_head",
  "cast": true
}
```

### smog:lead_hoe_head

```json
{
  "material": "lead",
  "form": "hoe_head",
  "cast": false
}
```

### smog:lead_cast_hoe_head

```json
{
  "material": "lead",
  "form": "hoe_head",
  "cast": true
}
```

### smog:lead_hammer_head

```json
{
  "material": "lead",
  "form": "hammer_head",
  "cast": false
}
```

### smog:lead_cast_hammer_head

```json
{
  "material": "lead",
  "form": "hammer_head",
  "cast": true
}
```

### smog:lead_standard_handle

```json
{
  "material": "lead",
  "form": "standard_handle",
  "cast": false
}
```

### smog:lead_clamp

```json
{
  "material": "lead",
  "form": "clamp",
  "cast": false
}
```

### smog:lead_saw_head

```json
{
  "material": "lead",
  "form": "saw_head",
  "cast": false
}
```

### smog:lead_cast_saw_head

```json
{
  "material": "lead",
  "form": "saw_head",
  "cast": true
}
```

### smog:lead_sword_head

```json
{
  "material": "lead",
  "form": "sword_head",
  "cast": false
}
```

### smog:lead_cast_sword_head

```json
{
  "material": "lead",
  "form": "sword_head",
  "cast": true
}
```

### smog:lead_katana_head

```json
{
  "material": "lead",
  "form": "katana_head",
  "cast": false
}
```

### smog:lead_cast_katana_head

```json
{
  "material": "lead",
  "form": "katana_head",
  "cast": true
}
```

### smog:lead_machete_head

```json
{
  "material": "lead",
  "form": "machete_head",
  "cast": false
}
```

### smog:lead_cast_machete_head

```json
{
  "material": "lead",
  "form": "machete_head",
  "cast": true
}
```

### smog:lead_sickle_head

```json
{
  "material": "lead",
  "form": "sickle_head",
  "cast": false
}
```

### smog:lead_cast_sickle_head

```json
{
  "material": "lead",
  "form": "sickle_head",
  "cast": true
}
```

### smog:leather_binding

```json
{
  "material": "leather",
  "form": "binding",
  "cast": false
}
```

### smog:leather_wrap

```json
{
  "material": "leather",
  "form": "wrap",
  "cast": false
}
```

### smog:lithium_pickaxe_head

```json
{
  "material": "lithium",
  "form": "pickaxe_head",
  "cast": false
}
```

### smog:lithium_cast_pickaxe_head

```json
{
  "material": "lithium",
  "form": "pickaxe_head",
  "cast": true
}
```

### smog:lithium_axe_head

```json
{
  "material": "lithium",
  "form": "axe_head",
  "cast": false
}
```

### smog:lithium_cast_axe_head

```json
{
  "material": "lithium",
  "form": "axe_head",
  "cast": true
}
```

### smog:lithium_shovel_head

```json
{
  "material": "lithium",
  "form": "shovel_head",
  "cast": false
}
```

### smog:lithium_cast_shovel_head

```json
{
  "material": "lithium",
  "form": "shovel_head",
  "cast": true
}
```

### smog:lithium_hoe_head

```json
{
  "material": "lithium",
  "form": "hoe_head",
  "cast": false
}
```

### smog:lithium_cast_hoe_head

```json
{
  "material": "lithium",
  "form": "hoe_head",
  "cast": true
}
```

### smog:lithium_hammer_head

```json
{
  "material": "lithium",
  "form": "hammer_head",
  "cast": false
}
```

### smog:lithium_cast_hammer_head

```json
{
  "material": "lithium",
  "form": "hammer_head",
  "cast": true
}
```

### smog:lithium_standard_handle

```json
{
  "material": "lithium",
  "form": "standard_handle",
  "cast": false
}
```

### smog:lithium_clamp

```json
{
  "material": "lithium",
  "form": "clamp",
  "cast": false
}
```

### smog:lithium_saw_head

```json
{
  "material": "lithium",
  "form": "saw_head",
  "cast": false
}
```

### smog:lithium_cast_saw_head

```json
{
  "material": "lithium",
  "form": "saw_head",
  "cast": true
}
```

### smog:lithium_sword_head

```json
{
  "material": "lithium",
  "form": "sword_head",
  "cast": false
}
```

### smog:lithium_cast_sword_head

```json
{
  "material": "lithium",
  "form": "sword_head",
  "cast": true
}
```

### smog:lithium_katana_head

```json
{
  "material": "lithium",
  "form": "katana_head",
  "cast": false
}
```

### smog:lithium_cast_katana_head

```json
{
  "material": "lithium",
  "form": "katana_head",
  "cast": true
}
```

### smog:lithium_machete_head

```json
{
  "material": "lithium",
  "form": "machete_head",
  "cast": false
}
```

### smog:lithium_cast_machete_head

```json
{
  "material": "lithium",
  "form": "machete_head",
  "cast": true
}
```

### smog:lithium_sickle_head

```json
{
  "material": "lithium",
  "form": "sickle_head",
  "cast": false
}
```

### smog:lithium_cast_sickle_head

```json
{
  "material": "lithium",
  "form": "sickle_head",
  "cast": true
}
```

### smog:manganese_pickaxe_head

```json
{
  "material": "manganese",
  "form": "pickaxe_head",
  "cast": false
}
```

### smog:manganese_cast_pickaxe_head

```json
{
  "material": "manganese",
  "form": "pickaxe_head",
  "cast": true
}
```

### smog:manganese_axe_head

```json
{
  "material": "manganese",
  "form": "axe_head",
  "cast": false
}
```

### smog:manganese_cast_axe_head

```json
{
  "material": "manganese",
  "form": "axe_head",
  "cast": true
}
```

### smog:manganese_shovel_head

```json
{
  "material": "manganese",
  "form": "shovel_head",
  "cast": false
}
```

### smog:manganese_cast_shovel_head

```json
{
  "material": "manganese",
  "form": "shovel_head",
  "cast": true
}
```

### smog:manganese_hoe_head

```json
{
  "material": "manganese",
  "form": "hoe_head",
  "cast": false
}
```

### smog:manganese_cast_hoe_head

```json
{
  "material": "manganese",
  "form": "hoe_head",
  "cast": true
}
```

### smog:manganese_hammer_head

```json
{
  "material": "manganese",
  "form": "hammer_head",
  "cast": false
}
```

### smog:manganese_cast_hammer_head

```json
{
  "material": "manganese",
  "form": "hammer_head",
  "cast": true
}
```

### smog:manganese_standard_handle

```json
{
  "material": "manganese",
  "form": "standard_handle",
  "cast": false
}
```

### smog:manganese_clamp

```json
{
  "material": "manganese",
  "form": "clamp",
  "cast": false
}
```

### smog:manganese_saw_head

```json
{
  "material": "manganese",
  "form": "saw_head",
  "cast": false
}
```

### smog:manganese_cast_saw_head

```json
{
  "material": "manganese",
  "form": "saw_head",
  "cast": true
}
```

### smog:manganese_sword_head

```json
{
  "material": "manganese",
  "form": "sword_head",
  "cast": false
}
```

### smog:manganese_cast_sword_head

```json
{
  "material": "manganese",
  "form": "sword_head",
  "cast": true
}
```

### smog:manganese_katana_head

```json
{
  "material": "manganese",
  "form": "katana_head",
  "cast": false
}
```

### smog:manganese_cast_katana_head

```json
{
  "material": "manganese",
  "form": "katana_head",
  "cast": true
}
```

### smog:manganese_machete_head

```json
{
  "material": "manganese",
  "form": "machete_head",
  "cast": false
}
```

### smog:manganese_cast_machete_head

```json
{
  "material": "manganese",
  "form": "machete_head",
  "cast": true
}
```

### smog:manganese_sickle_head

```json
{
  "material": "manganese",
  "form": "sickle_head",
  "cast": false
}
```

### smog:manganese_cast_sickle_head

```json
{
  "material": "manganese",
  "form": "sickle_head",
  "cast": true
}
```

### smog:molybdenum_pickaxe_head

```json
{
  "material": "molybdenum",
  "form": "pickaxe_head",
  "cast": false
}
```

### smog:molybdenum_cast_pickaxe_head

```json
{
  "material": "molybdenum",
  "form": "pickaxe_head",
  "cast": true
}
```

### smog:molybdenum_axe_head

```json
{
  "material": "molybdenum",
  "form": "axe_head",
  "cast": false
}
```

### smog:molybdenum_cast_axe_head

```json
{
  "material": "molybdenum",
  "form": "axe_head",
  "cast": true
}
```

### smog:molybdenum_shovel_head

```json
{
  "material": "molybdenum",
  "form": "shovel_head",
  "cast": false
}
```

### smog:molybdenum_cast_shovel_head

```json
{
  "material": "molybdenum",
  "form": "shovel_head",
  "cast": true
}
```

### smog:molybdenum_hoe_head

```json
{
  "material": "molybdenum",
  "form": "hoe_head",
  "cast": false
}
```

### smog:molybdenum_cast_hoe_head

```json
{
  "material": "molybdenum",
  "form": "hoe_head",
  "cast": true
}
```

### smog:molybdenum_hammer_head

```json
{
  "material": "molybdenum",
  "form": "hammer_head",
  "cast": false
}
```

### smog:molybdenum_cast_hammer_head

```json
{
  "material": "molybdenum",
  "form": "hammer_head",
  "cast": true
}
```

### smog:molybdenum_standard_handle

```json
{
  "material": "molybdenum",
  "form": "standard_handle",
  "cast": false
}
```

### smog:molybdenum_clamp

```json
{
  "material": "molybdenum",
  "form": "clamp",
  "cast": false
}
```

### smog:molybdenum_saw_head

```json
{
  "material": "molybdenum",
  "form": "saw_head",
  "cast": false
}
```

### smog:molybdenum_cast_saw_head

```json
{
  "material": "molybdenum",
  "form": "saw_head",
  "cast": true
}
```

### smog:molybdenum_sword_head

```json
{
  "material": "molybdenum",
  "form": "sword_head",
  "cast": false
}
```

### smog:molybdenum_cast_sword_head

```json
{
  "material": "molybdenum",
  "form": "sword_head",
  "cast": true
}
```

### smog:molybdenum_katana_head

```json
{
  "material": "molybdenum",
  "form": "katana_head",
  "cast": false
}
```

### smog:molybdenum_cast_katana_head

```json
{
  "material": "molybdenum",
  "form": "katana_head",
  "cast": true
}
```

### smog:molybdenum_machete_head

```json
{
  "material": "molybdenum",
  "form": "machete_head",
  "cast": false
}
```

### smog:molybdenum_cast_machete_head

```json
{
  "material": "molybdenum",
  "form": "machete_head",
  "cast": true
}
```

### smog:molybdenum_sickle_head

```json
{
  "material": "molybdenum",
  "form": "sickle_head",
  "cast": false
}
```

### smog:molybdenum_cast_sickle_head

```json
{
  "material": "molybdenum",
  "form": "sickle_head",
  "cast": true
}
```

### smog:netherite_pickaxe_head

```json
{
  "material": "netherite",
  "form": "pickaxe_head",
  "cast": false
}
```

### smog:netherite_cast_pickaxe_head

```json
{
  "material": "netherite",
  "form": "pickaxe_head",
  "cast": true
}
```

### smog:netherite_axe_head

```json
{
  "material": "netherite",
  "form": "axe_head",
  "cast": false
}
```

### smog:netherite_cast_axe_head

```json
{
  "material": "netherite",
  "form": "axe_head",
  "cast": true
}
```

### smog:netherite_shovel_head

```json
{
  "material": "netherite",
  "form": "shovel_head",
  "cast": false
}
```

### smog:netherite_cast_shovel_head

```json
{
  "material": "netherite",
  "form": "shovel_head",
  "cast": true
}
```

### smog:netherite_hoe_head

```json
{
  "material": "netherite",
  "form": "hoe_head",
  "cast": false
}
```

### smog:netherite_cast_hoe_head

```json
{
  "material": "netherite",
  "form": "hoe_head",
  "cast": true
}
```

### smog:netherite_hammer_head

```json
{
  "material": "netherite",
  "form": "hammer_head",
  "cast": false
}
```

### smog:netherite_cast_hammer_head

```json
{
  "material": "netherite",
  "form": "hammer_head",
  "cast": true
}
```

### smog:netherite_standard_handle

```json
{
  "material": "netherite",
  "form": "standard_handle",
  "cast": false
}
```

### smog:netherite_clamp

```json
{
  "material": "netherite",
  "form": "clamp",
  "cast": false
}
```

### smog:netherite_saw_head

```json
{
  "material": "netherite",
  "form": "saw_head",
  "cast": false
}
```

### smog:netherite_cast_saw_head

```json
{
  "material": "netherite",
  "form": "saw_head",
  "cast": true
}
```

### smog:netherite_sword_head

```json
{
  "material": "netherite",
  "form": "sword_head",
  "cast": false
}
```

### smog:netherite_cast_sword_head

```json
{
  "material": "netherite",
  "form": "sword_head",
  "cast": true
}
```

### smog:netherite_katana_head

```json
{
  "material": "netherite",
  "form": "katana_head",
  "cast": false
}
```

### smog:netherite_cast_katana_head

```json
{
  "material": "netherite",
  "form": "katana_head",
  "cast": true
}
```

### smog:netherite_machete_head

```json
{
  "material": "netherite",
  "form": "machete_head",
  "cast": false
}
```

### smog:netherite_cast_machete_head

```json
{
  "material": "netherite",
  "form": "machete_head",
  "cast": true
}
```

### smog:netherite_sickle_head

```json
{
  "material": "netherite",
  "form": "sickle_head",
  "cast": false
}
```

### smog:netherite_cast_sickle_head

```json
{
  "material": "netherite",
  "form": "sickle_head",
  "cast": true
}
```

### smog:nichrome_pickaxe_head

```json
{
  "material": "nichrome",
  "form": "pickaxe_head",
  "cast": false
}
```

### smog:nichrome_cast_pickaxe_head

```json
{
  "material": "nichrome",
  "form": "pickaxe_head",
  "cast": true
}
```

### smog:nichrome_axe_head

```json
{
  "material": "nichrome",
  "form": "axe_head",
  "cast": false
}
```

### smog:nichrome_cast_axe_head

```json
{
  "material": "nichrome",
  "form": "axe_head",
  "cast": true
}
```

### smog:nichrome_shovel_head

```json
{
  "material": "nichrome",
  "form": "shovel_head",
  "cast": false
}
```

### smog:nichrome_cast_shovel_head

```json
{
  "material": "nichrome",
  "form": "shovel_head",
  "cast": true
}
```

### smog:nichrome_hoe_head

```json
{
  "material": "nichrome",
  "form": "hoe_head",
  "cast": false
}
```

### smog:nichrome_cast_hoe_head

```json
{
  "material": "nichrome",
  "form": "hoe_head",
  "cast": true
}
```

### smog:nichrome_hammer_head

```json
{
  "material": "nichrome",
  "form": "hammer_head",
  "cast": false
}
```

### smog:nichrome_cast_hammer_head

```json
{
  "material": "nichrome",
  "form": "hammer_head",
  "cast": true
}
```

### smog:nichrome_standard_handle

```json
{
  "material": "nichrome",
  "form": "standard_handle",
  "cast": false
}
```

### smog:nichrome_clamp

```json
{
  "material": "nichrome",
  "form": "clamp",
  "cast": false
}
```

### smog:nichrome_saw_head

```json
{
  "material": "nichrome",
  "form": "saw_head",
  "cast": false
}
```

### smog:nichrome_cast_saw_head

```json
{
  "material": "nichrome",
  "form": "saw_head",
  "cast": true
}
```

### smog:nichrome_sword_head

```json
{
  "material": "nichrome",
  "form": "sword_head",
  "cast": false
}
```

### smog:nichrome_cast_sword_head

```json
{
  "material": "nichrome",
  "form": "sword_head",
  "cast": true
}
```

### smog:nichrome_katana_head

```json
{
  "material": "nichrome",
  "form": "katana_head",
  "cast": false
}
```

### smog:nichrome_cast_katana_head

```json
{
  "material": "nichrome",
  "form": "katana_head",
  "cast": true
}
```

### smog:nichrome_machete_head

```json
{
  "material": "nichrome",
  "form": "machete_head",
  "cast": false
}
```

### smog:nichrome_cast_machete_head

```json
{
  "material": "nichrome",
  "form": "machete_head",
  "cast": true
}
```

### smog:nichrome_sickle_head

```json
{
  "material": "nichrome",
  "form": "sickle_head",
  "cast": false
}
```

### smog:nichrome_cast_sickle_head

```json
{
  "material": "nichrome",
  "form": "sickle_head",
  "cast": true
}
```

### smog:nickel_pickaxe_head

```json
{
  "material": "nickel",
  "form": "pickaxe_head",
  "cast": false
}
```

### smog:nickel_cast_pickaxe_head

```json
{
  "material": "nickel",
  "form": "pickaxe_head",
  "cast": true
}
```

### smog:nickel_axe_head

```json
{
  "material": "nickel",
  "form": "axe_head",
  "cast": false
}
```

### smog:nickel_cast_axe_head

```json
{
  "material": "nickel",
  "form": "axe_head",
  "cast": true
}
```

### smog:nickel_shovel_head

```json
{
  "material": "nickel",
  "form": "shovel_head",
  "cast": false
}
```

### smog:nickel_cast_shovel_head

```json
{
  "material": "nickel",
  "form": "shovel_head",
  "cast": true
}
```

### smog:nickel_hoe_head

```json
{
  "material": "nickel",
  "form": "hoe_head",
  "cast": false
}
```

### smog:nickel_cast_hoe_head

```json
{
  "material": "nickel",
  "form": "hoe_head",
  "cast": true
}
```

### smog:nickel_hammer_head

```json
{
  "material": "nickel",
  "form": "hammer_head",
  "cast": false
}
```

### smog:nickel_cast_hammer_head

```json
{
  "material": "nickel",
  "form": "hammer_head",
  "cast": true
}
```

### smog:nickel_standard_handle

```json
{
  "material": "nickel",
  "form": "standard_handle",
  "cast": false
}
```

### smog:nickel_clamp

```json
{
  "material": "nickel",
  "form": "clamp",
  "cast": false
}
```

### smog:nickel_saw_head

```json
{
  "material": "nickel",
  "form": "saw_head",
  "cast": false
}
```

### smog:nickel_cast_saw_head

```json
{
  "material": "nickel",
  "form": "saw_head",
  "cast": true
}
```

### smog:nickel_sword_head

```json
{
  "material": "nickel",
  "form": "sword_head",
  "cast": false
}
```

### smog:nickel_cast_sword_head

```json
{
  "material": "nickel",
  "form": "sword_head",
  "cast": true
}
```

### smog:nickel_katana_head

```json
{
  "material": "nickel",
  "form": "katana_head",
  "cast": false
}
```

### smog:nickel_cast_katana_head

```json
{
  "material": "nickel",
  "form": "katana_head",
  "cast": true
}
```

### smog:nickel_machete_head

```json
{
  "material": "nickel",
  "form": "machete_head",
  "cast": false
}
```

### smog:nickel_cast_machete_head

```json
{
  "material": "nickel",
  "form": "machete_head",
  "cast": true
}
```

### smog:nickel_sickle_head

```json
{
  "material": "nickel",
  "form": "sickle_head",
  "cast": false
}
```

### smog:nickel_cast_sickle_head

```json
{
  "material": "nickel",
  "form": "sickle_head",
  "cast": true
}
```

### smog:nickel_superalloy_pickaxe_head

```json
{
  "material": "nickel_superalloy",
  "form": "pickaxe_head",
  "cast": false
}
```

### smog:nickel_superalloy_cast_pickaxe_head

```json
{
  "material": "nickel_superalloy",
  "form": "pickaxe_head",
  "cast": true
}
```

### smog:nickel_superalloy_axe_head

```json
{
  "material": "nickel_superalloy",
  "form": "axe_head",
  "cast": false
}
```

### smog:nickel_superalloy_cast_axe_head

```json
{
  "material": "nickel_superalloy",
  "form": "axe_head",
  "cast": true
}
```

### smog:nickel_superalloy_shovel_head

```json
{
  "material": "nickel_superalloy",
  "form": "shovel_head",
  "cast": false
}
```

### smog:nickel_superalloy_cast_shovel_head

```json
{
  "material": "nickel_superalloy",
  "form": "shovel_head",
  "cast": true
}
```

### smog:nickel_superalloy_hoe_head

```json
{
  "material": "nickel_superalloy",
  "form": "hoe_head",
  "cast": false
}
```

### smog:nickel_superalloy_cast_hoe_head

```json
{
  "material": "nickel_superalloy",
  "form": "hoe_head",
  "cast": true
}
```

### smog:nickel_superalloy_hammer_head

```json
{
  "material": "nickel_superalloy",
  "form": "hammer_head",
  "cast": false
}
```

### smog:nickel_superalloy_cast_hammer_head

```json
{
  "material": "nickel_superalloy",
  "form": "hammer_head",
  "cast": true
}
```

### smog:nickel_superalloy_standard_handle

```json
{
  "material": "nickel_superalloy",
  "form": "standard_handle",
  "cast": false
}
```

### smog:nickel_superalloy_clamp

```json
{
  "material": "nickel_superalloy",
  "form": "clamp",
  "cast": false
}
```

### smog:nickel_superalloy_saw_head

```json
{
  "material": "nickel_superalloy",
  "form": "saw_head",
  "cast": false
}
```

### smog:nickel_superalloy_cast_saw_head

```json
{
  "material": "nickel_superalloy",
  "form": "saw_head",
  "cast": true
}
```

### smog:nickel_superalloy_sword_head

```json
{
  "material": "nickel_superalloy",
  "form": "sword_head",
  "cast": false
}
```

### smog:nickel_superalloy_cast_sword_head

```json
{
  "material": "nickel_superalloy",
  "form": "sword_head",
  "cast": true
}
```

### smog:nickel_superalloy_katana_head

```json
{
  "material": "nickel_superalloy",
  "form": "katana_head",
  "cast": false
}
```

### smog:nickel_superalloy_cast_katana_head

```json
{
  "material": "nickel_superalloy",
  "form": "katana_head",
  "cast": true
}
```

### smog:nickel_superalloy_machete_head

```json
{
  "material": "nickel_superalloy",
  "form": "machete_head",
  "cast": false
}
```

### smog:nickel_superalloy_cast_machete_head

```json
{
  "material": "nickel_superalloy",
  "form": "machete_head",
  "cast": true
}
```

### smog:nickel_superalloy_sickle_head

```json
{
  "material": "nickel_superalloy",
  "form": "sickle_head",
  "cast": false
}
```

### smog:nickel_superalloy_cast_sickle_head

```json
{
  "material": "nickel_superalloy",
  "form": "sickle_head",
  "cast": true
}
```

### smog:silver_pickaxe_head

```json
{
  "material": "silver",
  "form": "pickaxe_head",
  "cast": false
}
```

### smog:silver_cast_pickaxe_head

```json
{
  "material": "silver",
  "form": "pickaxe_head",
  "cast": true
}
```

### smog:silver_axe_head

```json
{
  "material": "silver",
  "form": "axe_head",
  "cast": false
}
```

### smog:silver_cast_axe_head

```json
{
  "material": "silver",
  "form": "axe_head",
  "cast": true
}
```

### smog:silver_shovel_head

```json
{
  "material": "silver",
  "form": "shovel_head",
  "cast": false
}
```

### smog:silver_cast_shovel_head

```json
{
  "material": "silver",
  "form": "shovel_head",
  "cast": true
}
```

### smog:silver_hoe_head

```json
{
  "material": "silver",
  "form": "hoe_head",
  "cast": false
}
```

### smog:silver_cast_hoe_head

```json
{
  "material": "silver",
  "form": "hoe_head",
  "cast": true
}
```

### smog:silver_hammer_head

```json
{
  "material": "silver",
  "form": "hammer_head",
  "cast": false
}
```

### smog:silver_cast_hammer_head

```json
{
  "material": "silver",
  "form": "hammer_head",
  "cast": true
}
```

### smog:silver_standard_handle

```json
{
  "material": "silver",
  "form": "standard_handle",
  "cast": false
}
```

### smog:silver_clamp

```json
{
  "material": "silver",
  "form": "clamp",
  "cast": false
}
```

### smog:silver_saw_head

```json
{
  "material": "silver",
  "form": "saw_head",
  "cast": false
}
```

### smog:silver_cast_saw_head

```json
{
  "material": "silver",
  "form": "saw_head",
  "cast": true
}
```

### smog:silver_sword_head

```json
{
  "material": "silver",
  "form": "sword_head",
  "cast": false
}
```

### smog:silver_cast_sword_head

```json
{
  "material": "silver",
  "form": "sword_head",
  "cast": true
}
```

### smog:silver_katana_head

```json
{
  "material": "silver",
  "form": "katana_head",
  "cast": false
}
```

### smog:silver_cast_katana_head

```json
{
  "material": "silver",
  "form": "katana_head",
  "cast": true
}
```

### smog:silver_machete_head

```json
{
  "material": "silver",
  "form": "machete_head",
  "cast": false
}
```

### smog:silver_cast_machete_head

```json
{
  "material": "silver",
  "form": "machete_head",
  "cast": true
}
```

### smog:silver_sickle_head

```json
{
  "material": "silver",
  "form": "sickle_head",
  "cast": false
}
```

### smog:silver_cast_sickle_head

```json
{
  "material": "silver",
  "form": "sickle_head",
  "cast": true
}
```

### smog:solder_pickaxe_head

```json
{
  "material": "solder",
  "form": "pickaxe_head",
  "cast": false
}
```

### smog:solder_cast_pickaxe_head

```json
{
  "material": "solder",
  "form": "pickaxe_head",
  "cast": true
}
```

### smog:solder_axe_head

```json
{
  "material": "solder",
  "form": "axe_head",
  "cast": false
}
```

### smog:solder_cast_axe_head

```json
{
  "material": "solder",
  "form": "axe_head",
  "cast": true
}
```

### smog:solder_shovel_head

```json
{
  "material": "solder",
  "form": "shovel_head",
  "cast": false
}
```

### smog:solder_cast_shovel_head

```json
{
  "material": "solder",
  "form": "shovel_head",
  "cast": true
}
```

### smog:solder_hoe_head

```json
{
  "material": "solder",
  "form": "hoe_head",
  "cast": false
}
```

### smog:solder_cast_hoe_head

```json
{
  "material": "solder",
  "form": "hoe_head",
  "cast": true
}
```

### smog:solder_hammer_head

```json
{
  "material": "solder",
  "form": "hammer_head",
  "cast": false
}
```

### smog:solder_cast_hammer_head

```json
{
  "material": "solder",
  "form": "hammer_head",
  "cast": true
}
```

### smog:solder_standard_handle

```json
{
  "material": "solder",
  "form": "standard_handle",
  "cast": false
}
```

### smog:solder_clamp

```json
{
  "material": "solder",
  "form": "clamp",
  "cast": false
}
```

### smog:solder_saw_head

```json
{
  "material": "solder",
  "form": "saw_head",
  "cast": false
}
```

### smog:solder_cast_saw_head

```json
{
  "material": "solder",
  "form": "saw_head",
  "cast": true
}
```

### smog:solder_sword_head

```json
{
  "material": "solder",
  "form": "sword_head",
  "cast": false
}
```

### smog:solder_cast_sword_head

```json
{
  "material": "solder",
  "form": "sword_head",
  "cast": true
}
```

### smog:solder_katana_head

```json
{
  "material": "solder",
  "form": "katana_head",
  "cast": false
}
```

### smog:solder_cast_katana_head

```json
{
  "material": "solder",
  "form": "katana_head",
  "cast": true
}
```

### smog:solder_machete_head

```json
{
  "material": "solder",
  "form": "machete_head",
  "cast": false
}
```

### smog:solder_cast_machete_head

```json
{
  "material": "solder",
  "form": "machete_head",
  "cast": true
}
```

### smog:solder_sickle_head

```json
{
  "material": "solder",
  "form": "sickle_head",
  "cast": false
}
```

### smog:solder_cast_sickle_head

```json
{
  "material": "solder",
  "form": "sickle_head",
  "cast": true
}
```

### smog:stainless_steel_pickaxe_head

```json
{
  "material": "stainless_steel",
  "form": "pickaxe_head",
  "cast": false
}
```

### smog:stainless_steel_cast_pickaxe_head

```json
{
  "material": "stainless_steel",
  "form": "pickaxe_head",
  "cast": true
}
```

### smog:stainless_steel_axe_head

```json
{
  "material": "stainless_steel",
  "form": "axe_head",
  "cast": false
}
```

### smog:stainless_steel_cast_axe_head

```json
{
  "material": "stainless_steel",
  "form": "axe_head",
  "cast": true
}
```

### smog:stainless_steel_shovel_head

```json
{
  "material": "stainless_steel",
  "form": "shovel_head",
  "cast": false
}
```

### smog:stainless_steel_cast_shovel_head

```json
{
  "material": "stainless_steel",
  "form": "shovel_head",
  "cast": true
}
```

### smog:stainless_steel_hoe_head

```json
{
  "material": "stainless_steel",
  "form": "hoe_head",
  "cast": false
}
```

### smog:stainless_steel_cast_hoe_head

```json
{
  "material": "stainless_steel",
  "form": "hoe_head",
  "cast": true
}
```

### smog:stainless_steel_hammer_head

```json
{
  "material": "stainless_steel",
  "form": "hammer_head",
  "cast": false
}
```

### smog:stainless_steel_cast_hammer_head

```json
{
  "material": "stainless_steel",
  "form": "hammer_head",
  "cast": true
}
```

### smog:stainless_steel_standard_handle

```json
{
  "material": "stainless_steel",
  "form": "standard_handle",
  "cast": false
}
```

### smog:stainless_steel_clamp

```json
{
  "material": "stainless_steel",
  "form": "clamp",
  "cast": false
}
```

### smog:stainless_steel_saw_head

```json
{
  "material": "stainless_steel",
  "form": "saw_head",
  "cast": false
}
```

### smog:stainless_steel_cast_saw_head

```json
{
  "material": "stainless_steel",
  "form": "saw_head",
  "cast": true
}
```

### smog:stainless_steel_sword_head

```json
{
  "material": "stainless_steel",
  "form": "sword_head",
  "cast": false
}
```

### smog:stainless_steel_cast_sword_head

```json
{
  "material": "stainless_steel",
  "form": "sword_head",
  "cast": true
}
```

### smog:stainless_steel_katana_head

```json
{
  "material": "stainless_steel",
  "form": "katana_head",
  "cast": false
}
```

### smog:stainless_steel_cast_katana_head

```json
{
  "material": "stainless_steel",
  "form": "katana_head",
  "cast": true
}
```

### smog:stainless_steel_machete_head

```json
{
  "material": "stainless_steel",
  "form": "machete_head",
  "cast": false
}
```

### smog:stainless_steel_cast_machete_head

```json
{
  "material": "stainless_steel",
  "form": "machete_head",
  "cast": true
}
```

### smog:stainless_steel_sickle_head

```json
{
  "material": "stainless_steel",
  "form": "sickle_head",
  "cast": false
}
```

### smog:stainless_steel_cast_sickle_head

```json
{
  "material": "stainless_steel",
  "form": "sickle_head",
  "cast": true
}
```

### smog:steel_pickaxe_head

```json
{
  "material": "steel",
  "form": "pickaxe_head",
  "cast": false
}
```

### smog:steel_cast_pickaxe_head

```json
{
  "material": "steel",
  "form": "pickaxe_head",
  "cast": true
}
```

### smog:steel_axe_head

```json
{
  "material": "steel",
  "form": "axe_head",
  "cast": false
}
```

### smog:steel_cast_axe_head

```json
{
  "material": "steel",
  "form": "axe_head",
  "cast": true
}
```

### smog:steel_shovel_head

```json
{
  "material": "steel",
  "form": "shovel_head",
  "cast": false
}
```

### smog:steel_cast_shovel_head

```json
{
  "material": "steel",
  "form": "shovel_head",
  "cast": true
}
```

### smog:steel_hoe_head

```json
{
  "material": "steel",
  "form": "hoe_head",
  "cast": false
}
```

### smog:steel_cast_hoe_head

```json
{
  "material": "steel",
  "form": "hoe_head",
  "cast": true
}
```

### smog:steel_hammer_head

```json
{
  "material": "steel",
  "form": "hammer_head",
  "cast": false
}
```

### smog:steel_cast_hammer_head

```json
{
  "material": "steel",
  "form": "hammer_head",
  "cast": true
}
```

### smog:steel_standard_handle

```json
{
  "material": "steel",
  "form": "standard_handle",
  "cast": false
}
```

### smog:steel_clamp

```json
{
  "material": "steel",
  "form": "clamp",
  "cast": false
}
```

### smog:steel_saw_head

```json
{
  "material": "steel",
  "form": "saw_head",
  "cast": false
}
```

### smog:steel_cast_saw_head

```json
{
  "material": "steel",
  "form": "saw_head",
  "cast": true
}
```

### smog:steel_sword_head

```json
{
  "material": "steel",
  "form": "sword_head",
  "cast": false
}
```

### smog:steel_cast_sword_head

```json
{
  "material": "steel",
  "form": "sword_head",
  "cast": true
}
```

### smog:steel_katana_head

```json
{
  "material": "steel",
  "form": "katana_head",
  "cast": false
}
```

### smog:steel_cast_katana_head

```json
{
  "material": "steel",
  "form": "katana_head",
  "cast": true
}
```

### smog:steel_machete_head

```json
{
  "material": "steel",
  "form": "machete_head",
  "cast": false
}
```

### smog:steel_cast_machete_head

```json
{
  "material": "steel",
  "form": "machete_head",
  "cast": true
}
```

### smog:steel_sickle_head

```json
{
  "material": "steel",
  "form": "sickle_head",
  "cast": false
}
```

### smog:steel_cast_sickle_head

```json
{
  "material": "steel",
  "form": "sickle_head",
  "cast": true
}
```

### smog:stone_pickaxe_head

```json
{
  "material": "stone",
  "form": "pickaxe_head",
  "cast": false
}
```

### smog:stone_axe_head

```json
{
  "material": "stone",
  "form": "axe_head",
  "cast": false
}
```

### smog:stone_shovel_head

```json
{
  "material": "stone",
  "form": "shovel_head",
  "cast": false
}
```

### smog:stone_hoe_head

```json
{
  "material": "stone",
  "form": "hoe_head",
  "cast": false
}
```

### smog:stone_hammer_head

```json
{
  "material": "stone",
  "form": "hammer_head",
  "cast": false
}
```

### smog:stone_standard_handle

```json
{
  "material": "stone",
  "form": "standard_handle",
  "cast": false
}
```

### smog:stone_saw_head

```json
{
  "material": "stone",
  "form": "saw_head",
  "cast": false
}
```

### smog:stone_sword_head

```json
{
  "material": "stone",
  "form": "sword_head",
  "cast": false
}
```

### smog:stone_katana_head

```json
{
  "material": "stone",
  "form": "katana_head",
  "cast": false
}
```

### smog:stone_machete_head

```json
{
  "material": "stone",
  "form": "machete_head",
  "cast": false
}
```

### smog:stone_sickle_head

```json
{
  "material": "stone",
  "form": "sickle_head",
  "cast": false
}
```

### smog:tin_pickaxe_head

```json
{
  "material": "tin",
  "form": "pickaxe_head",
  "cast": false
}
```

### smog:tin_cast_pickaxe_head

```json
{
  "material": "tin",
  "form": "pickaxe_head",
  "cast": true
}
```

### smog:tin_axe_head

```json
{
  "material": "tin",
  "form": "axe_head",
  "cast": false
}
```

### smog:tin_cast_axe_head

```json
{
  "material": "tin",
  "form": "axe_head",
  "cast": true
}
```

### smog:tin_shovel_head

```json
{
  "material": "tin",
  "form": "shovel_head",
  "cast": false
}
```

### smog:tin_cast_shovel_head

```json
{
  "material": "tin",
  "form": "shovel_head",
  "cast": true
}
```

### smog:tin_hoe_head

```json
{
  "material": "tin",
  "form": "hoe_head",
  "cast": false
}
```

### smog:tin_cast_hoe_head

```json
{
  "material": "tin",
  "form": "hoe_head",
  "cast": true
}
```

### smog:tin_hammer_head

```json
{
  "material": "tin",
  "form": "hammer_head",
  "cast": false
}
```

### smog:tin_cast_hammer_head

```json
{
  "material": "tin",
  "form": "hammer_head",
  "cast": true
}
```

### smog:tin_standard_handle

```json
{
  "material": "tin",
  "form": "standard_handle",
  "cast": false
}
```

### smog:tin_clamp

```json
{
  "material": "tin",
  "form": "clamp",
  "cast": false
}
```

### smog:tin_saw_head

```json
{
  "material": "tin",
  "form": "saw_head",
  "cast": false
}
```

### smog:tin_cast_saw_head

```json
{
  "material": "tin",
  "form": "saw_head",
  "cast": true
}
```

### smog:tin_sword_head

```json
{
  "material": "tin",
  "form": "sword_head",
  "cast": false
}
```

### smog:tin_cast_sword_head

```json
{
  "material": "tin",
  "form": "sword_head",
  "cast": true
}
```

### smog:tin_katana_head

```json
{
  "material": "tin",
  "form": "katana_head",
  "cast": false
}
```

### smog:tin_cast_katana_head

```json
{
  "material": "tin",
  "form": "katana_head",
  "cast": true
}
```

### smog:tin_machete_head

```json
{
  "material": "tin",
  "form": "machete_head",
  "cast": false
}
```

### smog:tin_cast_machete_head

```json
{
  "material": "tin",
  "form": "machete_head",
  "cast": true
}
```

### smog:tin_sickle_head

```json
{
  "material": "tin",
  "form": "sickle_head",
  "cast": false
}
```

### smog:tin_cast_sickle_head

```json
{
  "material": "tin",
  "form": "sickle_head",
  "cast": true
}
```

### smog:titanium_pickaxe_head

```json
{
  "material": "titanium",
  "form": "pickaxe_head",
  "cast": false
}
```

### smog:titanium_cast_pickaxe_head

```json
{
  "material": "titanium",
  "form": "pickaxe_head",
  "cast": true
}
```

### smog:titanium_axe_head

```json
{
  "material": "titanium",
  "form": "axe_head",
  "cast": false
}
```

### smog:titanium_cast_axe_head

```json
{
  "material": "titanium",
  "form": "axe_head",
  "cast": true
}
```

### smog:titanium_shovel_head

```json
{
  "material": "titanium",
  "form": "shovel_head",
  "cast": false
}
```

### smog:titanium_cast_shovel_head

```json
{
  "material": "titanium",
  "form": "shovel_head",
  "cast": true
}
```

### smog:titanium_hoe_head

```json
{
  "material": "titanium",
  "form": "hoe_head",
  "cast": false
}
```

### smog:titanium_cast_hoe_head

```json
{
  "material": "titanium",
  "form": "hoe_head",
  "cast": true
}
```

### smog:titanium_hammer_head

```json
{
  "material": "titanium",
  "form": "hammer_head",
  "cast": false
}
```

### smog:titanium_cast_hammer_head

```json
{
  "material": "titanium",
  "form": "hammer_head",
  "cast": true
}
```

### smog:titanium_standard_handle

```json
{
  "material": "titanium",
  "form": "standard_handle",
  "cast": false
}
```

### smog:titanium_clamp

```json
{
  "material": "titanium",
  "form": "clamp",
  "cast": false
}
```

### smog:titanium_saw_head

```json
{
  "material": "titanium",
  "form": "saw_head",
  "cast": false
}
```

### smog:titanium_cast_saw_head

```json
{
  "material": "titanium",
  "form": "saw_head",
  "cast": true
}
```

### smog:titanium_sword_head

```json
{
  "material": "titanium",
  "form": "sword_head",
  "cast": false
}
```

### smog:titanium_cast_sword_head

```json
{
  "material": "titanium",
  "form": "sword_head",
  "cast": true
}
```

### smog:titanium_katana_head

```json
{
  "material": "titanium",
  "form": "katana_head",
  "cast": false
}
```

### smog:titanium_cast_katana_head

```json
{
  "material": "titanium",
  "form": "katana_head",
  "cast": true
}
```

### smog:titanium_machete_head

```json
{
  "material": "titanium",
  "form": "machete_head",
  "cast": false
}
```

### smog:titanium_cast_machete_head

```json
{
  "material": "titanium",
  "form": "machete_head",
  "cast": true
}
```

### smog:titanium_sickle_head

```json
{
  "material": "titanium",
  "form": "sickle_head",
  "cast": false
}
```

### smog:titanium_cast_sickle_head

```json
{
  "material": "titanium",
  "form": "sickle_head",
  "cast": true
}
```

### smog:titanium_alloy_pickaxe_head

```json
{
  "material": "titanium_alloy",
  "form": "pickaxe_head",
  "cast": false
}
```

### smog:titanium_alloy_cast_pickaxe_head

```json
{
  "material": "titanium_alloy",
  "form": "pickaxe_head",
  "cast": true
}
```

### smog:titanium_alloy_axe_head

```json
{
  "material": "titanium_alloy",
  "form": "axe_head",
  "cast": false
}
```

### smog:titanium_alloy_cast_axe_head

```json
{
  "material": "titanium_alloy",
  "form": "axe_head",
  "cast": true
}
```

### smog:titanium_alloy_shovel_head

```json
{
  "material": "titanium_alloy",
  "form": "shovel_head",
  "cast": false
}
```

### smog:titanium_alloy_cast_shovel_head

```json
{
  "material": "titanium_alloy",
  "form": "shovel_head",
  "cast": true
}
```

### smog:titanium_alloy_hoe_head

```json
{
  "material": "titanium_alloy",
  "form": "hoe_head",
  "cast": false
}
```

### smog:titanium_alloy_cast_hoe_head

```json
{
  "material": "titanium_alloy",
  "form": "hoe_head",
  "cast": true
}
```

### smog:titanium_alloy_hammer_head

```json
{
  "material": "titanium_alloy",
  "form": "hammer_head",
  "cast": false
}
```

### smog:titanium_alloy_cast_hammer_head

```json
{
  "material": "titanium_alloy",
  "form": "hammer_head",
  "cast": true
}
```

### smog:titanium_alloy_standard_handle

```json
{
  "material": "titanium_alloy",
  "form": "standard_handle",
  "cast": false
}
```

### smog:titanium_alloy_clamp

```json
{
  "material": "titanium_alloy",
  "form": "clamp",
  "cast": false
}
```

### smog:titanium_alloy_saw_head

```json
{
  "material": "titanium_alloy",
  "form": "saw_head",
  "cast": false
}
```

### smog:titanium_alloy_cast_saw_head

```json
{
  "material": "titanium_alloy",
  "form": "saw_head",
  "cast": true
}
```

### smog:titanium_alloy_sword_head

```json
{
  "material": "titanium_alloy",
  "form": "sword_head",
  "cast": false
}
```

### smog:titanium_alloy_cast_sword_head

```json
{
  "material": "titanium_alloy",
  "form": "sword_head",
  "cast": true
}
```

### smog:titanium_alloy_katana_head

```json
{
  "material": "titanium_alloy",
  "form": "katana_head",
  "cast": false
}
```

### smog:titanium_alloy_cast_katana_head

```json
{
  "material": "titanium_alloy",
  "form": "katana_head",
  "cast": true
}
```

### smog:titanium_alloy_machete_head

```json
{
  "material": "titanium_alloy",
  "form": "machete_head",
  "cast": false
}
```

### smog:titanium_alloy_cast_machete_head

```json
{
  "material": "titanium_alloy",
  "form": "machete_head",
  "cast": true
}
```

### smog:titanium_alloy_sickle_head

```json
{
  "material": "titanium_alloy",
  "form": "sickle_head",
  "cast": false
}
```

### smog:titanium_alloy_cast_sickle_head

```json
{
  "material": "titanium_alloy",
  "form": "sickle_head",
  "cast": true
}
```

### smog:tool_steel_pickaxe_head

```json
{
  "material": "tool_steel",
  "form": "pickaxe_head",
  "cast": false
}
```

### smog:tool_steel_cast_pickaxe_head

```json
{
  "material": "tool_steel",
  "form": "pickaxe_head",
  "cast": true
}
```

### smog:tool_steel_axe_head

```json
{
  "material": "tool_steel",
  "form": "axe_head",
  "cast": false
}
```

### smog:tool_steel_cast_axe_head

```json
{
  "material": "tool_steel",
  "form": "axe_head",
  "cast": true
}
```

### smog:tool_steel_shovel_head

```json
{
  "material": "tool_steel",
  "form": "shovel_head",
  "cast": false
}
```

### smog:tool_steel_cast_shovel_head

```json
{
  "material": "tool_steel",
  "form": "shovel_head",
  "cast": true
}
```

### smog:tool_steel_hoe_head

```json
{
  "material": "tool_steel",
  "form": "hoe_head",
  "cast": false
}
```

### smog:tool_steel_cast_hoe_head

```json
{
  "material": "tool_steel",
  "form": "hoe_head",
  "cast": true
}
```

### smog:tool_steel_hammer_head

```json
{
  "material": "tool_steel",
  "form": "hammer_head",
  "cast": false
}
```

### smog:tool_steel_cast_hammer_head

```json
{
  "material": "tool_steel",
  "form": "hammer_head",
  "cast": true
}
```

### smog:tool_steel_standard_handle

```json
{
  "material": "tool_steel",
  "form": "standard_handle",
  "cast": false
}
```

### smog:tool_steel_clamp

```json
{
  "material": "tool_steel",
  "form": "clamp",
  "cast": false
}
```

### smog:tool_steel_saw_head

```json
{
  "material": "tool_steel",
  "form": "saw_head",
  "cast": false
}
```

### smog:tool_steel_cast_saw_head

```json
{
  "material": "tool_steel",
  "form": "saw_head",
  "cast": true
}
```

### smog:tool_steel_sword_head

```json
{
  "material": "tool_steel",
  "form": "sword_head",
  "cast": false
}
```

### smog:tool_steel_cast_sword_head

```json
{
  "material": "tool_steel",
  "form": "sword_head",
  "cast": true
}
```

### smog:tool_steel_katana_head

```json
{
  "material": "tool_steel",
  "form": "katana_head",
  "cast": false
}
```

### smog:tool_steel_cast_katana_head

```json
{
  "material": "tool_steel",
  "form": "katana_head",
  "cast": true
}
```

### smog:tool_steel_machete_head

```json
{
  "material": "tool_steel",
  "form": "machete_head",
  "cast": false
}
```

### smog:tool_steel_cast_machete_head

```json
{
  "material": "tool_steel",
  "form": "machete_head",
  "cast": true
}
```

### smog:tool_steel_sickle_head

```json
{
  "material": "tool_steel",
  "form": "sickle_head",
  "cast": false
}
```

### smog:tool_steel_cast_sickle_head

```json
{
  "material": "tool_steel",
  "form": "sickle_head",
  "cast": true
}
```

### smog:tungsten_pickaxe_head

```json
{
  "material": "tungsten",
  "form": "pickaxe_head",
  "cast": false
}
```

### smog:tungsten_cast_pickaxe_head

```json
{
  "material": "tungsten",
  "form": "pickaxe_head",
  "cast": true
}
```

### smog:tungsten_axe_head

```json
{
  "material": "tungsten",
  "form": "axe_head",
  "cast": false
}
```

### smog:tungsten_cast_axe_head

```json
{
  "material": "tungsten",
  "form": "axe_head",
  "cast": true
}
```

### smog:tungsten_shovel_head

```json
{
  "material": "tungsten",
  "form": "shovel_head",
  "cast": false
}
```

### smog:tungsten_cast_shovel_head

```json
{
  "material": "tungsten",
  "form": "shovel_head",
  "cast": true
}
```

### smog:tungsten_hoe_head

```json
{
  "material": "tungsten",
  "form": "hoe_head",
  "cast": false
}
```

### smog:tungsten_cast_hoe_head

```json
{
  "material": "tungsten",
  "form": "hoe_head",
  "cast": true
}
```

### smog:tungsten_hammer_head

```json
{
  "material": "tungsten",
  "form": "hammer_head",
  "cast": false
}
```

### smog:tungsten_cast_hammer_head

```json
{
  "material": "tungsten",
  "form": "hammer_head",
  "cast": true
}
```

### smog:tungsten_standard_handle

```json
{
  "material": "tungsten",
  "form": "standard_handle",
  "cast": false
}
```

### smog:tungsten_clamp

```json
{
  "material": "tungsten",
  "form": "clamp",
  "cast": false
}
```

### smog:tungsten_saw_head

```json
{
  "material": "tungsten",
  "form": "saw_head",
  "cast": false
}
```

### smog:tungsten_cast_saw_head

```json
{
  "material": "tungsten",
  "form": "saw_head",
  "cast": true
}
```

### smog:tungsten_sword_head

```json
{
  "material": "tungsten",
  "form": "sword_head",
  "cast": false
}
```

### smog:tungsten_cast_sword_head

```json
{
  "material": "tungsten",
  "form": "sword_head",
  "cast": true
}
```

### smog:tungsten_katana_head

```json
{
  "material": "tungsten",
  "form": "katana_head",
  "cast": false
}
```

### smog:tungsten_cast_katana_head

```json
{
  "material": "tungsten",
  "form": "katana_head",
  "cast": true
}
```

### smog:tungsten_machete_head

```json
{
  "material": "tungsten",
  "form": "machete_head",
  "cast": false
}
```

### smog:tungsten_cast_machete_head

```json
{
  "material": "tungsten",
  "form": "machete_head",
  "cast": true
}
```

### smog:tungsten_sickle_head

```json
{
  "material": "tungsten",
  "form": "sickle_head",
  "cast": false
}
```

### smog:tungsten_cast_sickle_head

```json
{
  "material": "tungsten",
  "form": "sickle_head",
  "cast": true
}
```

### smog:tungsten_steel_pickaxe_head

```json
{
  "material": "tungsten_steel",
  "form": "pickaxe_head",
  "cast": false
}
```

### smog:tungsten_steel_cast_pickaxe_head

```json
{
  "material": "tungsten_steel",
  "form": "pickaxe_head",
  "cast": true
}
```

### smog:tungsten_steel_axe_head

```json
{
  "material": "tungsten_steel",
  "form": "axe_head",
  "cast": false
}
```

### smog:tungsten_steel_cast_axe_head

```json
{
  "material": "tungsten_steel",
  "form": "axe_head",
  "cast": true
}
```

### smog:tungsten_steel_shovel_head

```json
{
  "material": "tungsten_steel",
  "form": "shovel_head",
  "cast": false
}
```

### smog:tungsten_steel_cast_shovel_head

```json
{
  "material": "tungsten_steel",
  "form": "shovel_head",
  "cast": true
}
```

### smog:tungsten_steel_hoe_head

```json
{
  "material": "tungsten_steel",
  "form": "hoe_head",
  "cast": false
}
```

### smog:tungsten_steel_cast_hoe_head

```json
{
  "material": "tungsten_steel",
  "form": "hoe_head",
  "cast": true
}
```

### smog:tungsten_steel_hammer_head

```json
{
  "material": "tungsten_steel",
  "form": "hammer_head",
  "cast": false
}
```

### smog:tungsten_steel_cast_hammer_head

```json
{
  "material": "tungsten_steel",
  "form": "hammer_head",
  "cast": true
}
```

### smog:tungsten_steel_standard_handle

```json
{
  "material": "tungsten_steel",
  "form": "standard_handle",
  "cast": false
}
```

### smog:tungsten_steel_clamp

```json
{
  "material": "tungsten_steel",
  "form": "clamp",
  "cast": false
}
```

### smog:tungsten_steel_saw_head

```json
{
  "material": "tungsten_steel",
  "form": "saw_head",
  "cast": false
}
```

### smog:tungsten_steel_cast_saw_head

```json
{
  "material": "tungsten_steel",
  "form": "saw_head",
  "cast": true
}
```

### smog:tungsten_steel_sword_head

```json
{
  "material": "tungsten_steel",
  "form": "sword_head",
  "cast": false
}
```

### smog:tungsten_steel_cast_sword_head

```json
{
  "material": "tungsten_steel",
  "form": "sword_head",
  "cast": true
}
```

### smog:tungsten_steel_katana_head

```json
{
  "material": "tungsten_steel",
  "form": "katana_head",
  "cast": false
}
```

### smog:tungsten_steel_cast_katana_head

```json
{
  "material": "tungsten_steel",
  "form": "katana_head",
  "cast": true
}
```

### smog:tungsten_steel_machete_head

```json
{
  "material": "tungsten_steel",
  "form": "machete_head",
  "cast": false
}
```

### smog:tungsten_steel_cast_machete_head

```json
{
  "material": "tungsten_steel",
  "form": "machete_head",
  "cast": true
}
```

### smog:tungsten_steel_sickle_head

```json
{
  "material": "tungsten_steel",
  "form": "sickle_head",
  "cast": false
}
```

### smog:tungsten_steel_cast_sickle_head

```json
{
  "material": "tungsten_steel",
  "form": "sickle_head",
  "cast": true
}
```

### smog:uranium_pickaxe_head

```json
{
  "material": "uranium",
  "form": "pickaxe_head",
  "cast": false
}
```

### smog:uranium_cast_pickaxe_head

```json
{
  "material": "uranium",
  "form": "pickaxe_head",
  "cast": true
}
```

### smog:uranium_axe_head

```json
{
  "material": "uranium",
  "form": "axe_head",
  "cast": false
}
```

### smog:uranium_cast_axe_head

```json
{
  "material": "uranium",
  "form": "axe_head",
  "cast": true
}
```

### smog:uranium_shovel_head

```json
{
  "material": "uranium",
  "form": "shovel_head",
  "cast": false
}
```

### smog:uranium_cast_shovel_head

```json
{
  "material": "uranium",
  "form": "shovel_head",
  "cast": true
}
```

### smog:uranium_hoe_head

```json
{
  "material": "uranium",
  "form": "hoe_head",
  "cast": false
}
```

### smog:uranium_cast_hoe_head

```json
{
  "material": "uranium",
  "form": "hoe_head",
  "cast": true
}
```

### smog:uranium_hammer_head

```json
{
  "material": "uranium",
  "form": "hammer_head",
  "cast": false
}
```

### smog:uranium_cast_hammer_head

```json
{
  "material": "uranium",
  "form": "hammer_head",
  "cast": true
}
```

### smog:uranium_standard_handle

```json
{
  "material": "uranium",
  "form": "standard_handle",
  "cast": false
}
```

### smog:uranium_clamp

```json
{
  "material": "uranium",
  "form": "clamp",
  "cast": false
}
```

### smog:uranium_saw_head

```json
{
  "material": "uranium",
  "form": "saw_head",
  "cast": false
}
```

### smog:uranium_cast_saw_head

```json
{
  "material": "uranium",
  "form": "saw_head",
  "cast": true
}
```

### smog:uranium_sword_head

```json
{
  "material": "uranium",
  "form": "sword_head",
  "cast": false
}
```

### smog:uranium_cast_sword_head

```json
{
  "material": "uranium",
  "form": "sword_head",
  "cast": true
}
```

### smog:uranium_katana_head

```json
{
  "material": "uranium",
  "form": "katana_head",
  "cast": false
}
```

### smog:uranium_cast_katana_head

```json
{
  "material": "uranium",
  "form": "katana_head",
  "cast": true
}
```

### smog:uranium_machete_head

```json
{
  "material": "uranium",
  "form": "machete_head",
  "cast": false
}
```

### smog:uranium_cast_machete_head

```json
{
  "material": "uranium",
  "form": "machete_head",
  "cast": true
}
```

### smog:uranium_sickle_head

```json
{
  "material": "uranium",
  "form": "sickle_head",
  "cast": false
}
```

### smog:uranium_cast_sickle_head

```json
{
  "material": "uranium",
  "form": "sickle_head",
  "cast": true
}
```

### smog:wood_pickaxe_head

```json
{
  "material": "wood",
  "form": "pickaxe_head",
  "cast": false
}
```

### smog:wood_axe_head

```json
{
  "material": "wood",
  "form": "axe_head",
  "cast": false
}
```

### smog:wood_shovel_head

```json
{
  "material": "wood",
  "form": "shovel_head",
  "cast": false
}
```

### smog:wood_hoe_head

```json
{
  "material": "wood",
  "form": "hoe_head",
  "cast": false
}
```

### smog:wood_hammer_head

```json
{
  "material": "wood",
  "form": "hammer_head",
  "cast": false
}
```

### smog:wooden_handle

```json
{
  "material": "wood",
  "form": "standard_handle",
  "cast": false
}
```

### smog:wood_saw_head

```json
{
  "material": "wood",
  "form": "saw_head",
  "cast": false
}
```

### smog:wood_sword_head

```json
{
  "material": "wood",
  "form": "sword_head",
  "cast": false
}
```

### smog:wood_katana_head

```json
{
  "material": "wood",
  "form": "katana_head",
  "cast": false
}
```

### smog:wood_machete_head

```json
{
  "material": "wood",
  "form": "machete_head",
  "cast": false
}
```

### smog:wood_sickle_head

```json
{
  "material": "wood",
  "form": "sickle_head",
  "cast": false
}
```

### smog:wool_wrap

```json
{
  "material": "wool",
  "form": "wrap",
  "cast": false
}
```

### smog:wrought_iron_pickaxe_head

```json
{
  "material": "wrought_iron",
  "form": "pickaxe_head",
  "cast": false
}
```

### smog:wrought_iron_cast_pickaxe_head

```json
{
  "material": "wrought_iron",
  "form": "pickaxe_head",
  "cast": true
}
```

### smog:wrought_iron_axe_head

```json
{
  "material": "wrought_iron",
  "form": "axe_head",
  "cast": false
}
```

### smog:wrought_iron_cast_axe_head

```json
{
  "material": "wrought_iron",
  "form": "axe_head",
  "cast": true
}
```

### smog:wrought_iron_shovel_head

```json
{
  "material": "wrought_iron",
  "form": "shovel_head",
  "cast": false
}
```

### smog:wrought_iron_cast_shovel_head

```json
{
  "material": "wrought_iron",
  "form": "shovel_head",
  "cast": true
}
```

### smog:wrought_iron_hoe_head

```json
{
  "material": "wrought_iron",
  "form": "hoe_head",
  "cast": false
}
```

### smog:wrought_iron_cast_hoe_head

```json
{
  "material": "wrought_iron",
  "form": "hoe_head",
  "cast": true
}
```

### smog:wrought_iron_hammer_head

```json
{
  "material": "wrought_iron",
  "form": "hammer_head",
  "cast": false
}
```

### smog:wrought_iron_cast_hammer_head

```json
{
  "material": "wrought_iron",
  "form": "hammer_head",
  "cast": true
}
```

### smog:wrought_iron_standard_handle

```json
{
  "material": "wrought_iron",
  "form": "standard_handle",
  "cast": false
}
```

### smog:wrought_iron_clamp

```json
{
  "material": "wrought_iron",
  "form": "clamp",
  "cast": false
}
```

### smog:wrought_iron_saw_head

```json
{
  "material": "wrought_iron",
  "form": "saw_head",
  "cast": false
}
```

### smog:wrought_iron_cast_saw_head

```json
{
  "material": "wrought_iron",
  "form": "saw_head",
  "cast": true
}
```

### smog:wrought_iron_sword_head

```json
{
  "material": "wrought_iron",
  "form": "sword_head",
  "cast": false
}
```

### smog:wrought_iron_cast_sword_head

```json
{
  "material": "wrought_iron",
  "form": "sword_head",
  "cast": true
}
```

### smog:wrought_iron_katana_head

```json
{
  "material": "wrought_iron",
  "form": "katana_head",
  "cast": false
}
```

### smog:wrought_iron_cast_katana_head

```json
{
  "material": "wrought_iron",
  "form": "katana_head",
  "cast": true
}
```

### smog:wrought_iron_machete_head

```json
{
  "material": "wrought_iron",
  "form": "machete_head",
  "cast": false
}
```

### smog:wrought_iron_cast_machete_head

```json
{
  "material": "wrought_iron",
  "form": "machete_head",
  "cast": true
}
```

### smog:wrought_iron_sickle_head

```json
{
  "material": "wrought_iron",
  "form": "sickle_head",
  "cast": false
}
```

### smog:wrought_iron_cast_sickle_head

```json
{
  "material": "wrought_iron",
  "form": "sickle_head",
  "cast": true
}
```

### smog:zinc_pickaxe_head

```json
{
  "material": "zinc",
  "form": "pickaxe_head",
  "cast": false
}
```

### smog:zinc_cast_pickaxe_head

```json
{
  "material": "zinc",
  "form": "pickaxe_head",
  "cast": true
}
```

### smog:zinc_axe_head

```json
{
  "material": "zinc",
  "form": "axe_head",
  "cast": false
}
```

### smog:zinc_cast_axe_head

```json
{
  "material": "zinc",
  "form": "axe_head",
  "cast": true
}
```

### smog:zinc_shovel_head

```json
{
  "material": "zinc",
  "form": "shovel_head",
  "cast": false
}
```

### smog:zinc_cast_shovel_head

```json
{
  "material": "zinc",
  "form": "shovel_head",
  "cast": true
}
```

### smog:zinc_hoe_head

```json
{
  "material": "zinc",
  "form": "hoe_head",
  "cast": false
}
```

### smog:zinc_cast_hoe_head

```json
{
  "material": "zinc",
  "form": "hoe_head",
  "cast": true
}
```

### smog:zinc_hammer_head

```json
{
  "material": "zinc",
  "form": "hammer_head",
  "cast": false
}
```

### smog:zinc_cast_hammer_head

```json
{
  "material": "zinc",
  "form": "hammer_head",
  "cast": true
}
```

### smog:zinc_standard_handle

```json
{
  "material": "zinc",
  "form": "standard_handle",
  "cast": false
}
```

### smog:zinc_clamp

```json
{
  "material": "zinc",
  "form": "clamp",
  "cast": false
}
```

### smog:zinc_saw_head

```json
{
  "material": "zinc",
  "form": "saw_head",
  "cast": false
}
```

### smog:zinc_cast_saw_head

```json
{
  "material": "zinc",
  "form": "saw_head",
  "cast": true
}
```

### smog:zinc_sword_head

```json
{
  "material": "zinc",
  "form": "sword_head",
  "cast": false
}
```

### smog:zinc_cast_sword_head

```json
{
  "material": "zinc",
  "form": "sword_head",
  "cast": true
}
```

### smog:zinc_katana_head

```json
{
  "material": "zinc",
  "form": "katana_head",
  "cast": false
}
```

### smog:zinc_cast_katana_head

```json
{
  "material": "zinc",
  "form": "katana_head",
  "cast": true
}
```

### smog:zinc_machete_head

```json
{
  "material": "zinc",
  "form": "machete_head",
  "cast": false
}
```

### smog:zinc_cast_machete_head

```json
{
  "material": "zinc",
  "form": "machete_head",
  "cast": true
}
```

### smog:zinc_sickle_head

```json
{
  "material": "zinc",
  "form": "sickle_head",
  "cast": false
}
```

### smog:zinc_cast_sickle_head

```json
{
  "material": "zinc",
  "form": "sickle_head",
  "cast": true
}
```
