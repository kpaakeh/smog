# Полный справочник JSON-рецептов 0.0.4

> Срез документации: 4 ноября 2026, версия **0.0.4**. Описывает существующее содержимое; наличие материала в каталоге не означает доступность его производства в Медной мастерской.


Содержимое фактически собранных ресурсов `data/smog/recipe`. Включает верстак, печные и камнерезные рецепты, генерируемый декор. Собственные процессы плавильни, промывки, керамики и инструментов дополнительно описаны в [каталоге](CONTENT_REFERENCE_0.0.4.md). Наличие JSON не отменяет проверок оборудования в коде.

Всего файлов: **820**.

## smog:air_intake

```json
{
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
```

## smog:air_nozzle

```json
{
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
```

## smog:aluminum_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:aluminum_ingot"
  },
  "result": {
    "id": "smog:aluminum_block"
  }
}
```

## smog:aluminum_bronze_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:aluminum_bronze_ingot"
  },
  "result": {
    "id": "smog:aluminum_bronze_block"
  }
}
```

## smog:aluminum_bronze_ingot_from_blasting

```json
{
  "type": "minecraft:blasting",
  "ingredient": "smog:aluminum_bronze_dust",
  "result": {
    "id": "smog:aluminum_bronze_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:aluminum_bronze_ingot_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:aluminum_bronze_block"
  ],
  "result": {
    "id": "smog:aluminum_bronze_ingot",
    "count": 9
  }
}
```

## smog:aluminum_bronze_ingot_from_nuggets

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:aluminum_bronze_nugget"
  },
  "result": {
    "id": "smog:aluminum_bronze_ingot"
  }
}
```

## smog:aluminum_bronze_ingot_from_smelting

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:aluminum_bronze_dust",
  "result": {
    "id": "smog:aluminum_bronze_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:aluminum_bronze_nugget

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:aluminum_bronze_ingot"
  ],
  "result": {
    "id": "smog:aluminum_bronze_nugget",
    "count": 9
  }
}
```

## smog:aluminum_ingot_from_blasting

```json
{
  "type": "minecraft:blasting",
  "ingredient": "smog:aluminum_dust",
  "result": {
    "id": "smog:aluminum_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:aluminum_ingot_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:aluminum_block"
  ],
  "result": {
    "id": "smog:aluminum_ingot",
    "count": 9
  }
}
```

## smog:aluminum_ingot_from_nuggets

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:aluminum_nugget"
  },
  "result": {
    "id": "smog:aluminum_ingot"
  }
}
```

## smog:aluminum_ingot_from_smelting

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:aluminum_dust",
  "result": {
    "id": "smog:aluminum_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:aluminum_nugget

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:aluminum_ingot"
  ],
  "result": {
    "id": "smog:aluminum_nugget",
    "count": 9
  }
}
```

## smog:axe_mold_firing

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:unfired_axe_mold",
  "result": {
    "id": "smog:axe_mold"
  },
  "experience": 0.1,
  "cookingtime": 200
}
```

## smog:brass_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:brass_ingot"
  },
  "result": {
    "id": "smog:brass_block"
  }
}
```

## smog:brass_ingot_from_blasting

```json
{
  "type": "minecraft:blasting",
  "ingredient": "smog:brass_dust",
  "result": {
    "id": "smog:brass_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:brass_ingot_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:brass_block"
  ],
  "result": {
    "id": "smog:brass_ingot",
    "count": 9
  }
}
```

## smog:brass_ingot_from_nuggets

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:brass_nugget"
  },
  "result": {
    "id": "smog:brass_ingot"
  }
}
```

## smog:brass_ingot_from_smelting

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:brass_dust",
  "result": {
    "id": "smog:brass_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:brass_nugget

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:brass_ingot"
  ],
  "result": {
    "id": "smog:brass_nugget",
    "count": 9
  }
}
```

## smog:bronze_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:bronze_ingot"
  },
  "result": {
    "id": "smog:bronze_block"
  }
}
```

## smog:bronze_clamp

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:bronze_fitting"
  ],
  "result": {
    "id": "smog:bronze_clamp",
    "count": 1
  }
}
```

## smog:bronze_drive

```json
{
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
```

## smog:bronze_ingot_from_blasting

```json
{
  "type": "minecraft:blasting",
  "ingredient": "smog:bronze_dust",
  "result": {
    "id": "smog:bronze_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:bronze_ingot_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:bronze_block"
  ],
  "result": {
    "id": "smog:bronze_ingot",
    "count": 9
  }
}
```

## smog:bronze_ingot_from_nuggets

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:bronze_nugget"
  },
  "result": {
    "id": "smog:bronze_ingot"
  }
}
```

## smog:bronze_ingot_from_smelting

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:bronze_dust",
  "result": {
    "id": "smog:bronze_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:bronze_nugget

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:bronze_ingot"
  ],
  "result": {
    "id": "smog:bronze_nugget",
    "count": 9
  }
}
```

## smog:carved_firebrick_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:carved_firebrick"
  },
  "result": {
    "id": "smog:carved_firebrick_slab",
    "count": 6
  }
}
```

## smog:carved_firebrick_slab_from_carved_firebrick_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:carved_firebrick",
  "result": {
    "id": "smog:carved_firebrick_slab",
    "count": 2
  }
}
```

## smog:carved_firebrick_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:carved_firebrick"
  },
  "result": {
    "id": "smog:carved_firebrick_stairs",
    "count": 4
  }
}
```

## smog:carved_firebrick_stairs_from_carved_firebrick_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:carved_firebrick",
  "result": {
    "id": "smog:carved_firebrick_stairs",
    "count": 1
  }
}
```

## smog:carved_firebrick_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:carved_firebrick"
  },
  "result": {
    "id": "smog:carved_firebrick_wall",
    "count": 6
  }
}
```

## smog:carved_firebrick_wall_from_carved_firebrick_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:carved_firebrick",
  "result": {
    "id": "smog:carved_firebrick_wall",
    "count": 1
  }
}
```

## smog:cast_iron_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:cast_iron_ingot"
  },
  "result": {
    "id": "smog:cast_iron_block"
  }
}
```

## smog:cast_iron_ingot_from_blasting

```json
{
  "type": "minecraft:blasting",
  "ingredient": "smog:cast_iron_dust",
  "result": {
    "id": "smog:cast_iron_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:cast_iron_ingot_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:cast_iron_block"
  ],
  "result": {
    "id": "smog:cast_iron_ingot",
    "count": 9
  }
}
```

## smog:cast_iron_ingot_from_nuggets

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:cast_iron_nugget"
  },
  "result": {
    "id": "smog:cast_iron_ingot"
  }
}
```

## smog:cast_iron_ingot_from_smelting

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:cast_iron_dust",
  "result": {
    "id": "smog:cast_iron_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:cast_iron_nugget

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:cast_iron_ingot"
  ],
  "result": {
    "id": "smog:cast_iron_nugget",
    "count": 9
  }
}
```

## smog:casting_basin

```json
{
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
```

## smog:casting_channel

```json
{
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
```

## smog:casting_faucet

```json
{
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
```

## smog:casting_faucet_from_ingots

```json
{
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
```

## smog:casting_spout

```json
{
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
```

## smog:casting_table

```json
{
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
```

## smog:ceramic_core

```json
{
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
```

## smog:ceramic_crucible_firing

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:unfired_crucible",
  "result": {
    "id": "smog:ceramic_crucible"
  },
  "experience": 0.1,
  "cookingtime": 200
}
```

## smog:ceramic_kiln

```json
{
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
```

## smog:chiseled_dolomite

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S",
    "S"
  ],
  "key": {
    "S": "smog:dolomite_slab"
  },
  "result": {
    "id": "smog:chiseled_dolomite",
    "count": 1
  }
}
```

## smog:chiseled_dolomite_from_dolomite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:dolomite",
  "result": {
    "id": "smog:chiseled_dolomite",
    "count": 1
  }
}
```

## smog:chiseled_dolomite_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:chiseled_dolomite"
  },
  "result": {
    "id": "smog:chiseled_dolomite_slab",
    "count": 6
  }
}
```

## smog:chiseled_dolomite_slab_from_chiseled_dolomite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:chiseled_dolomite",
  "result": {
    "id": "smog:chiseled_dolomite_slab",
    "count": 2
  }
}
```

## smog:chiseled_dolomite_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:chiseled_dolomite"
  },
  "result": {
    "id": "smog:chiseled_dolomite_stairs",
    "count": 4
  }
}
```

## smog:chiseled_dolomite_stairs_from_chiseled_dolomite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:chiseled_dolomite",
  "result": {
    "id": "smog:chiseled_dolomite_stairs",
    "count": 1
  }
}
```

## smog:chiseled_dolomite_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:chiseled_dolomite"
  },
  "result": {
    "id": "smog:chiseled_dolomite_wall",
    "count": 6
  }
}
```

## smog:chiseled_dolomite_wall_from_chiseled_dolomite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:chiseled_dolomite",
  "result": {
    "id": "smog:chiseled_dolomite_wall",
    "count": 1
  }
}
```

## smog:chiseled_fluorite

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S",
    "S"
  ],
  "key": {
    "S": "smog:fluorite_slab"
  },
  "result": {
    "id": "smog:chiseled_fluorite",
    "count": 1
  }
}
```

## smog:chiseled_fluorite_from_fluorite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:fluorite",
  "result": {
    "id": "smog:chiseled_fluorite",
    "count": 1
  }
}
```

## smog:chiseled_fluorite_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:chiseled_fluorite"
  },
  "result": {
    "id": "smog:chiseled_fluorite_slab",
    "count": 6
  }
}
```

## smog:chiseled_fluorite_slab_from_chiseled_fluorite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:chiseled_fluorite",
  "result": {
    "id": "smog:chiseled_fluorite_slab",
    "count": 2
  }
}
```

## smog:chiseled_fluorite_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:chiseled_fluorite"
  },
  "result": {
    "id": "smog:chiseled_fluorite_stairs",
    "count": 4
  }
}
```

## smog:chiseled_fluorite_stairs_from_chiseled_fluorite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:chiseled_fluorite",
  "result": {
    "id": "smog:chiseled_fluorite_stairs",
    "count": 1
  }
}
```

## smog:chiseled_fluorite_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:chiseled_fluorite"
  },
  "result": {
    "id": "smog:chiseled_fluorite_wall",
    "count": 6
  }
}
```

## smog:chiseled_fluorite_wall_from_chiseled_fluorite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:chiseled_fluorite",
  "result": {
    "id": "smog:chiseled_fluorite_wall",
    "count": 1
  }
}
```

## smog:chiseled_graphite

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S",
    "S"
  ],
  "key": {
    "S": "smog:graphite_slab"
  },
  "result": {
    "id": "smog:chiseled_graphite",
    "count": 1
  }
}
```

## smog:chiseled_graphite_from_graphite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:graphite",
  "result": {
    "id": "smog:chiseled_graphite",
    "count": 1
  }
}
```

## smog:chiseled_graphite_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:chiseled_graphite"
  },
  "result": {
    "id": "smog:chiseled_graphite_slab",
    "count": 6
  }
}
```

## smog:chiseled_graphite_slab_from_chiseled_graphite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:chiseled_graphite",
  "result": {
    "id": "smog:chiseled_graphite_slab",
    "count": 2
  }
}
```

## smog:chiseled_graphite_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:chiseled_graphite"
  },
  "result": {
    "id": "smog:chiseled_graphite_stairs",
    "count": 4
  }
}
```

## smog:chiseled_graphite_stairs_from_chiseled_graphite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:chiseled_graphite",
  "result": {
    "id": "smog:chiseled_graphite_stairs",
    "count": 1
  }
}
```

## smog:chiseled_graphite_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:chiseled_graphite"
  },
  "result": {
    "id": "smog:chiseled_graphite_wall",
    "count": 6
  }
}
```

## smog:chiseled_graphite_wall_from_chiseled_graphite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:chiseled_graphite",
  "result": {
    "id": "smog:chiseled_graphite_wall",
    "count": 1
  }
}
```

## smog:chiseled_kaolin

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S",
    "S"
  ],
  "key": {
    "S": "smog:kaolin_slab"
  },
  "result": {
    "id": "smog:chiseled_kaolin",
    "count": 1
  }
}
```

## smog:chiseled_kaolin_from_kaolin_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:kaolin",
  "result": {
    "id": "smog:chiseled_kaolin",
    "count": 1
  }
}
```

## smog:chiseled_kaolin_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:chiseled_kaolin"
  },
  "result": {
    "id": "smog:chiseled_kaolin_slab",
    "count": 6
  }
}
```

## smog:chiseled_kaolin_slab_from_chiseled_kaolin_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:chiseled_kaolin",
  "result": {
    "id": "smog:chiseled_kaolin_slab",
    "count": 2
  }
}
```

## smog:chiseled_kaolin_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:chiseled_kaolin"
  },
  "result": {
    "id": "smog:chiseled_kaolin_stairs",
    "count": 4
  }
}
```

## smog:chiseled_kaolin_stairs_from_chiseled_kaolin_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:chiseled_kaolin",
  "result": {
    "id": "smog:chiseled_kaolin_stairs",
    "count": 1
  }
}
```

## smog:chiseled_kaolin_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:chiseled_kaolin"
  },
  "result": {
    "id": "smog:chiseled_kaolin_wall",
    "count": 6
  }
}
```

## smog:chiseled_kaolin_wall_from_chiseled_kaolin_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:chiseled_kaolin",
  "result": {
    "id": "smog:chiseled_kaolin_wall",
    "count": 1
  }
}
```

## smog:chiseled_limestone

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S",
    "S"
  ],
  "key": {
    "S": "smog:limestone_slab"
  },
  "result": {
    "id": "smog:chiseled_limestone",
    "count": 1
  }
}
```

## smog:chiseled_limestone_from_limestone_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:limestone",
  "result": {
    "id": "smog:chiseled_limestone",
    "count": 1
  }
}
```

## smog:chiseled_limestone_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:chiseled_limestone"
  },
  "result": {
    "id": "smog:chiseled_limestone_slab",
    "count": 6
  }
}
```

## smog:chiseled_limestone_slab_from_chiseled_limestone_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:chiseled_limestone",
  "result": {
    "id": "smog:chiseled_limestone_slab",
    "count": 2
  }
}
```

## smog:chiseled_limestone_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:chiseled_limestone"
  },
  "result": {
    "id": "smog:chiseled_limestone_stairs",
    "count": 4
  }
}
```

## smog:chiseled_limestone_stairs_from_chiseled_limestone_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:chiseled_limestone",
  "result": {
    "id": "smog:chiseled_limestone_stairs",
    "count": 1
  }
}
```

## smog:chiseled_limestone_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:chiseled_limestone"
  },
  "result": {
    "id": "smog:chiseled_limestone_wall",
    "count": 6
  }
}
```

## smog:chiseled_limestone_wall_from_chiseled_limestone_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:chiseled_limestone",
  "result": {
    "id": "smog:chiseled_limestone_wall",
    "count": 1
  }
}
```

## smog:chiseled_phosphorite

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S",
    "S"
  ],
  "key": {
    "S": "smog:phosphorite_slab"
  },
  "result": {
    "id": "smog:chiseled_phosphorite",
    "count": 1
  }
}
```

## smog:chiseled_phosphorite_from_phosphorite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:phosphorite",
  "result": {
    "id": "smog:chiseled_phosphorite",
    "count": 1
  }
}
```

## smog:chiseled_phosphorite_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:chiseled_phosphorite"
  },
  "result": {
    "id": "smog:chiseled_phosphorite_slab",
    "count": 6
  }
}
```

## smog:chiseled_phosphorite_slab_from_chiseled_phosphorite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:chiseled_phosphorite",
  "result": {
    "id": "smog:chiseled_phosphorite_slab",
    "count": 2
  }
}
```

## smog:chiseled_phosphorite_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:chiseled_phosphorite"
  },
  "result": {
    "id": "smog:chiseled_phosphorite_stairs",
    "count": 4
  }
}
```

## smog:chiseled_phosphorite_stairs_from_chiseled_phosphorite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:chiseled_phosphorite",
  "result": {
    "id": "smog:chiseled_phosphorite_stairs",
    "count": 1
  }
}
```

## smog:chiseled_phosphorite_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:chiseled_phosphorite"
  },
  "result": {
    "id": "smog:chiseled_phosphorite_wall",
    "count": 6
  }
}
```

## smog:chiseled_phosphorite_wall_from_chiseled_phosphorite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:chiseled_phosphorite",
  "result": {
    "id": "smog:chiseled_phosphorite_wall",
    "count": 1
  }
}
```

## smog:chiseled_rock_salt

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S",
    "S"
  ],
  "key": {
    "S": "smog:rock_salt_slab"
  },
  "result": {
    "id": "smog:chiseled_rock_salt",
    "count": 1
  }
}
```

## smog:chiseled_rock_salt_from_rock_salt_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:rock_salt",
  "result": {
    "id": "smog:chiseled_rock_salt",
    "count": 1
  }
}
```

## smog:chiseled_rock_salt_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:chiseled_rock_salt"
  },
  "result": {
    "id": "smog:chiseled_rock_salt_slab",
    "count": 6
  }
}
```

## smog:chiseled_rock_salt_slab_from_chiseled_rock_salt_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:chiseled_rock_salt",
  "result": {
    "id": "smog:chiseled_rock_salt_slab",
    "count": 2
  }
}
```

## smog:chiseled_rock_salt_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:chiseled_rock_salt"
  },
  "result": {
    "id": "smog:chiseled_rock_salt_stairs",
    "count": 4
  }
}
```

## smog:chiseled_rock_salt_stairs_from_chiseled_rock_salt_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:chiseled_rock_salt",
  "result": {
    "id": "smog:chiseled_rock_salt_stairs",
    "count": 1
  }
}
```

## smog:chiseled_rock_salt_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:chiseled_rock_salt"
  },
  "result": {
    "id": "smog:chiseled_rock_salt_wall",
    "count": 6
  }
}
```

## smog:chiseled_rock_salt_wall_from_chiseled_rock_salt_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:chiseled_rock_salt",
  "result": {
    "id": "smog:chiseled_rock_salt_wall",
    "count": 1
  }
}
```

## smog:chromium_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:chromium_ingot"
  },
  "result": {
    "id": "smog:chromium_block"
  }
}
```

## smog:chromium_ingot_from_blasting_chromium_dust

```json
{
  "type": "minecraft:blasting",
  "group": "chromium_ingot",
  "ingredient": "smog:chromium_dust",
  "result": {
    "id": "smog:chromium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:chromium_ingot_from_blasting_chromium_ore

```json
{
  "type": "minecraft:blasting",
  "group": "chromium_ingot",
  "ingredient": "smog:chromium_ore",
  "result": {
    "id": "smog:chromium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:chromium_ingot_from_blasting_chromium_shard

```json
{
  "type": "minecraft:blasting",
  "group": "chromium_ingot",
  "ingredient": "smog:chromium_shard",
  "result": {
    "id": "smog:chromium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:chromium_ingot_from_blasting_crushed_chromium_ore

```json
{
  "type": "minecraft:blasting",
  "group": "chromium_ingot",
  "ingredient": "smog:crushed_chromium_ore",
  "result": {
    "id": "smog:chromium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:chromium_ingot_from_blasting_deepslate_chromium_ore

```json
{
  "type": "minecraft:blasting",
  "group": "chromium_ingot",
  "ingredient": "smog:deepslate_chromium_ore",
  "result": {
    "id": "smog:chromium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:chromium_ingot_from_blasting_raw_chromium

```json
{
  "type": "minecraft:blasting",
  "group": "chromium_ingot",
  "ingredient": "smog:raw_chromium",
  "result": {
    "id": "smog:chromium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:chromium_ingot_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:chromium_block"
  ],
  "result": {
    "id": "smog:chromium_ingot",
    "count": 9
  }
}
```

## smog:chromium_ingot_from_small

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:chromium_nugget"
  },
  "result": {
    "id": "smog:chromium_ingot"
  }
}
```

## smog:chromium_ingot_from_smelting_chromium_dust

```json
{
  "type": "minecraft:smelting",
  "group": "chromium_ingot",
  "ingredient": "smog:chromium_dust",
  "result": {
    "id": "smog:chromium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:chromium_ingot_from_smelting_chromium_ore

```json
{
  "type": "minecraft:smelting",
  "group": "chromium_ingot",
  "ingredient": "smog:chromium_ore",
  "result": {
    "id": "smog:chromium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:chromium_ingot_from_smelting_chromium_shard

```json
{
  "type": "minecraft:smelting",
  "group": "chromium_ingot",
  "ingredient": "smog:chromium_shard",
  "result": {
    "id": "smog:chromium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:chromium_ingot_from_smelting_crushed_chromium_ore

```json
{
  "type": "minecraft:smelting",
  "group": "chromium_ingot",
  "ingredient": "smog:crushed_chromium_ore",
  "result": {
    "id": "smog:chromium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:chromium_ingot_from_smelting_deepslate_chromium_ore

```json
{
  "type": "minecraft:smelting",
  "group": "chromium_ingot",
  "ingredient": "smog:deepslate_chromium_ore",
  "result": {
    "id": "smog:chromium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:chromium_ingot_from_smelting_raw_chromium

```json
{
  "type": "minecraft:smelting",
  "group": "chromium_ingot",
  "ingredient": "smog:raw_chromium",
  "result": {
    "id": "smog:chromium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:chromium_nugget

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:chromium_ingot"
  ],
  "result": {
    "id": "smog:chromium_nugget",
    "count": 9
  }
}
```

## smog:closed_crucible_insert

```json
{
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
```

## smog:configurator

```json
{
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
```

## smog:constantan_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:constantan_ingot"
  },
  "result": {
    "id": "smog:constantan_block"
  }
}
```

## smog:constantan_ingot_from_blasting

```json
{
  "type": "minecraft:blasting",
  "ingredient": "smog:constantan_dust",
  "result": {
    "id": "smog:constantan_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:constantan_ingot_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:constantan_block"
  ],
  "result": {
    "id": "smog:constantan_ingot",
    "count": 9
  }
}
```

## smog:constantan_ingot_from_nuggets

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:constantan_nugget"
  },
  "result": {
    "id": "smog:constantan_ingot"
  }
}
```

## smog:constantan_ingot_from_smelting

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:constantan_dust",
  "result": {
    "id": "smog:constantan_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:constantan_nugget

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:constantan_ingot"
  ],
  "result": {
    "id": "smog:constantan_nugget",
    "count": 9
  }
}
```

## smog:coolant_bucket

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "minecraft:bucket",
    "minecraft:snowball",
    "minecraft:lapis_lazuli"
  ],
  "result": {
    "id": "smog:coolant_bucket",
    "count": 1
  }
}
```

## smog:copper_clamp

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:copper_fitting"
  ],
  "result": {
    "id": "smog:copper_clamp",
    "count": 1
  }
}
```

## smog:copper_concentrate_from_blasting

```json
{
  "type": "minecraft:blasting",
  "ingredient": "smog:copper_concentrate",
  "result": {
    "id": "minecraft:copper_ingot",
    "count": 2
  },
  "experience": 1.4,
  "cookingtime": 200
}
```

## smog:copper_concentrate_from_smelting

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:copper_concentrate",
  "result": {
    "id": "minecraft:copper_ingot",
    "count": 2
  },
  "experience": 1.4,
  "cookingtime": 400
}
```

## smog:copper_hearth

```json
{
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
```

## smog:copper_water_pipe

```json
{
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
```

## smog:cord_binding

```json
{
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
```

## smog:cracked_dolomite_bricks_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:cracked_dolomite_bricks"
  },
  "result": {
    "id": "smog:cracked_dolomite_bricks_slab",
    "count": 6
  }
}
```

## smog:cracked_dolomite_bricks_slab_from_cracked_dolomite_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:cracked_dolomite_bricks",
  "result": {
    "id": "smog:cracked_dolomite_bricks_slab",
    "count": 2
  }
}
```

## smog:cracked_dolomite_bricks_smelting

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:dolomite_bricks",
  "result": {
    "id": "smog:cracked_dolomite_bricks"
  },
  "experience": 0.1,
  "cookingtime": 200
}
```

## smog:cracked_dolomite_bricks_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:cracked_dolomite_bricks"
  },
  "result": {
    "id": "smog:cracked_dolomite_bricks_stairs",
    "count": 4
  }
}
```

## smog:cracked_dolomite_bricks_stairs_from_cracked_dolomite_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:cracked_dolomite_bricks",
  "result": {
    "id": "smog:cracked_dolomite_bricks_stairs",
    "count": 1
  }
}
```

## smog:cracked_dolomite_bricks_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:cracked_dolomite_bricks"
  },
  "result": {
    "id": "smog:cracked_dolomite_bricks_wall",
    "count": 6
  }
}
```

## smog:cracked_dolomite_bricks_wall_from_cracked_dolomite_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:cracked_dolomite_bricks",
  "result": {
    "id": "smog:cracked_dolomite_bricks_wall",
    "count": 1
  }
}
```

## smog:cracked_firebrick_bricks_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:cracked_firebrick_bricks"
  },
  "result": {
    "id": "smog:cracked_firebrick_bricks_slab",
    "count": 6
  }
}
```

## smog:cracked_firebrick_bricks_slab_from_cracked_firebrick_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:cracked_firebrick_bricks",
  "result": {
    "id": "smog:cracked_firebrick_bricks_slab",
    "count": 2
  }
}
```

## smog:cracked_firebrick_bricks_smelting

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:firebrick_casing",
  "result": {
    "id": "smog:cracked_firebrick_bricks"
  },
  "experience": 0.1,
  "cookingtime": 200
}
```

## smog:cracked_firebrick_bricks_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:cracked_firebrick_bricks"
  },
  "result": {
    "id": "smog:cracked_firebrick_bricks_stairs",
    "count": 4
  }
}
```

## smog:cracked_firebrick_bricks_stairs_from_cracked_firebrick_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:cracked_firebrick_bricks",
  "result": {
    "id": "smog:cracked_firebrick_bricks_stairs",
    "count": 1
  }
}
```

## smog:cracked_firebrick_bricks_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:cracked_firebrick_bricks"
  },
  "result": {
    "id": "smog:cracked_firebrick_bricks_wall",
    "count": 6
  }
}
```

## smog:cracked_firebrick_bricks_wall_from_cracked_firebrick_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:cracked_firebrick_bricks",
  "result": {
    "id": "smog:cracked_firebrick_bricks_wall",
    "count": 1
  }
}
```

## smog:cracked_fluorite_bricks_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:cracked_fluorite_bricks"
  },
  "result": {
    "id": "smog:cracked_fluorite_bricks_slab",
    "count": 6
  }
}
```

## smog:cracked_fluorite_bricks_slab_from_cracked_fluorite_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:cracked_fluorite_bricks",
  "result": {
    "id": "smog:cracked_fluorite_bricks_slab",
    "count": 2
  }
}
```

## smog:cracked_fluorite_bricks_smelting

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:fluorite_bricks",
  "result": {
    "id": "smog:cracked_fluorite_bricks"
  },
  "experience": 0.1,
  "cookingtime": 200
}
```

## smog:cracked_fluorite_bricks_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:cracked_fluorite_bricks"
  },
  "result": {
    "id": "smog:cracked_fluorite_bricks_stairs",
    "count": 4
  }
}
```

## smog:cracked_fluorite_bricks_stairs_from_cracked_fluorite_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:cracked_fluorite_bricks",
  "result": {
    "id": "smog:cracked_fluorite_bricks_stairs",
    "count": 1
  }
}
```

## smog:cracked_fluorite_bricks_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:cracked_fluorite_bricks"
  },
  "result": {
    "id": "smog:cracked_fluorite_bricks_wall",
    "count": 6
  }
}
```

## smog:cracked_fluorite_bricks_wall_from_cracked_fluorite_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:cracked_fluorite_bricks",
  "result": {
    "id": "smog:cracked_fluorite_bricks_wall",
    "count": 1
  }
}
```

## smog:cracked_graphite_bricks_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:cracked_graphite_bricks"
  },
  "result": {
    "id": "smog:cracked_graphite_bricks_slab",
    "count": 6
  }
}
```

## smog:cracked_graphite_bricks_slab_from_cracked_graphite_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:cracked_graphite_bricks",
  "result": {
    "id": "smog:cracked_graphite_bricks_slab",
    "count": 2
  }
}
```

## smog:cracked_graphite_bricks_smelting

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:graphite_bricks",
  "result": {
    "id": "smog:cracked_graphite_bricks"
  },
  "experience": 0.1,
  "cookingtime": 200
}
```

## smog:cracked_graphite_bricks_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:cracked_graphite_bricks"
  },
  "result": {
    "id": "smog:cracked_graphite_bricks_stairs",
    "count": 4
  }
}
```

## smog:cracked_graphite_bricks_stairs_from_cracked_graphite_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:cracked_graphite_bricks",
  "result": {
    "id": "smog:cracked_graphite_bricks_stairs",
    "count": 1
  }
}
```

## smog:cracked_graphite_bricks_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:cracked_graphite_bricks"
  },
  "result": {
    "id": "smog:cracked_graphite_bricks_wall",
    "count": 6
  }
}
```

## smog:cracked_graphite_bricks_wall_from_cracked_graphite_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:cracked_graphite_bricks",
  "result": {
    "id": "smog:cracked_graphite_bricks_wall",
    "count": 1
  }
}
```

## smog:cracked_kaolin_bricks_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:cracked_kaolin_bricks"
  },
  "result": {
    "id": "smog:cracked_kaolin_bricks_slab",
    "count": 6
  }
}
```

## smog:cracked_kaolin_bricks_slab_from_cracked_kaolin_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:cracked_kaolin_bricks",
  "result": {
    "id": "smog:cracked_kaolin_bricks_slab",
    "count": 2
  }
}
```

## smog:cracked_kaolin_bricks_smelting

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:kaolin_bricks",
  "result": {
    "id": "smog:cracked_kaolin_bricks"
  },
  "experience": 0.1,
  "cookingtime": 200
}
```

## smog:cracked_kaolin_bricks_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:cracked_kaolin_bricks"
  },
  "result": {
    "id": "smog:cracked_kaolin_bricks_stairs",
    "count": 4
  }
}
```

## smog:cracked_kaolin_bricks_stairs_from_cracked_kaolin_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:cracked_kaolin_bricks",
  "result": {
    "id": "smog:cracked_kaolin_bricks_stairs",
    "count": 1
  }
}
```

## smog:cracked_kaolin_bricks_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:cracked_kaolin_bricks"
  },
  "result": {
    "id": "smog:cracked_kaolin_bricks_wall",
    "count": 6
  }
}
```

## smog:cracked_kaolin_bricks_wall_from_cracked_kaolin_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:cracked_kaolin_bricks",
  "result": {
    "id": "smog:cracked_kaolin_bricks_wall",
    "count": 1
  }
}
```

## smog:cracked_limestone_bricks_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:cracked_limestone_bricks"
  },
  "result": {
    "id": "smog:cracked_limestone_bricks_slab",
    "count": 6
  }
}
```

## smog:cracked_limestone_bricks_slab_from_cracked_limestone_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:cracked_limestone_bricks",
  "result": {
    "id": "smog:cracked_limestone_bricks_slab",
    "count": 2
  }
}
```

## smog:cracked_limestone_bricks_smelting

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:limestone_bricks",
  "result": {
    "id": "smog:cracked_limestone_bricks"
  },
  "experience": 0.1,
  "cookingtime": 200
}
```

## smog:cracked_limestone_bricks_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:cracked_limestone_bricks"
  },
  "result": {
    "id": "smog:cracked_limestone_bricks_stairs",
    "count": 4
  }
}
```

## smog:cracked_limestone_bricks_stairs_from_cracked_limestone_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:cracked_limestone_bricks",
  "result": {
    "id": "smog:cracked_limestone_bricks_stairs",
    "count": 1
  }
}
```

## smog:cracked_limestone_bricks_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:cracked_limestone_bricks"
  },
  "result": {
    "id": "smog:cracked_limestone_bricks_wall",
    "count": 6
  }
}
```

## smog:cracked_limestone_bricks_wall_from_cracked_limestone_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:cracked_limestone_bricks",
  "result": {
    "id": "smog:cracked_limestone_bricks_wall",
    "count": 1
  }
}
```

## smog:cracked_phosphorite_bricks_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:cracked_phosphorite_bricks"
  },
  "result": {
    "id": "smog:cracked_phosphorite_bricks_slab",
    "count": 6
  }
}
```

## smog:cracked_phosphorite_bricks_slab_from_cracked_phosphorite_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:cracked_phosphorite_bricks",
  "result": {
    "id": "smog:cracked_phosphorite_bricks_slab",
    "count": 2
  }
}
```

## smog:cracked_phosphorite_bricks_smelting

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:phosphorite_bricks",
  "result": {
    "id": "smog:cracked_phosphorite_bricks"
  },
  "experience": 0.1,
  "cookingtime": 200
}
```

## smog:cracked_phosphorite_bricks_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:cracked_phosphorite_bricks"
  },
  "result": {
    "id": "smog:cracked_phosphorite_bricks_stairs",
    "count": 4
  }
}
```

## smog:cracked_phosphorite_bricks_stairs_from_cracked_phosphorite_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:cracked_phosphorite_bricks",
  "result": {
    "id": "smog:cracked_phosphorite_bricks_stairs",
    "count": 1
  }
}
```

## smog:cracked_phosphorite_bricks_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:cracked_phosphorite_bricks"
  },
  "result": {
    "id": "smog:cracked_phosphorite_bricks_wall",
    "count": 6
  }
}
```

## smog:cracked_phosphorite_bricks_wall_from_cracked_phosphorite_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:cracked_phosphorite_bricks",
  "result": {
    "id": "smog:cracked_phosphorite_bricks_wall",
    "count": 1
  }
}
```

## smog:cracked_rock_salt_bricks_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:cracked_rock_salt_bricks"
  },
  "result": {
    "id": "smog:cracked_rock_salt_bricks_slab",
    "count": 6
  }
}
```

## smog:cracked_rock_salt_bricks_slab_from_cracked_rock_salt_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:cracked_rock_salt_bricks",
  "result": {
    "id": "smog:cracked_rock_salt_bricks_slab",
    "count": 2
  }
}
```

## smog:cracked_rock_salt_bricks_smelting

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:rock_salt_bricks",
  "result": {
    "id": "smog:cracked_rock_salt_bricks"
  },
  "experience": 0.1,
  "cookingtime": 200
}
```

## smog:cracked_rock_salt_bricks_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:cracked_rock_salt_bricks"
  },
  "result": {
    "id": "smog:cracked_rock_salt_bricks_stairs",
    "count": 4
  }
}
```

## smog:cracked_rock_salt_bricks_stairs_from_cracked_rock_salt_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:cracked_rock_salt_bricks",
  "result": {
    "id": "smog:cracked_rock_salt_bricks_stairs",
    "count": 1
  }
}
```

## smog:cracked_rock_salt_bricks_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:cracked_rock_salt_bricks"
  },
  "result": {
    "id": "smog:cracked_rock_salt_bricks_wall",
    "count": 6
  }
}
```

## smog:cracked_rock_salt_bricks_wall_from_cracked_rock_salt_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:cracked_rock_salt_bricks",
  "result": {
    "id": "smog:cracked_rock_salt_bricks_wall",
    "count": 1
  }
}
```

## smog:cupronickel_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:cupronickel_ingot"
  },
  "result": {
    "id": "smog:cupronickel_block"
  }
}
```

## smog:cupronickel_ingot_from_blasting

```json
{
  "type": "minecraft:blasting",
  "ingredient": "smog:cupronickel_dust",
  "result": {
    "id": "smog:cupronickel_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:cupronickel_ingot_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:cupronickel_block"
  ],
  "result": {
    "id": "smog:cupronickel_ingot",
    "count": 9
  }
}
```

## smog:cupronickel_ingot_from_nuggets

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:cupronickel_nugget"
  },
  "result": {
    "id": "smog:cupronickel_ingot"
  }
}
```

## smog:cupronickel_ingot_from_smelting

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:cupronickel_dust",
  "result": {
    "id": "smog:cupronickel_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:cupronickel_nugget

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:cupronickel_ingot"
  ],
  "result": {
    "id": "smog:cupronickel_nugget",
    "count": 9
  }
}
```

## smog:dolomite_bricks

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SS",
    "SS"
  ],
  "key": {
    "S": "smog:polished_dolomite"
  },
  "result": {
    "id": "smog:dolomite_bricks",
    "count": 4
  }
}
```

## smog:dolomite_bricks_from_dolomite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:dolomite",
  "result": {
    "id": "smog:dolomite_bricks",
    "count": 1
  }
}
```

## smog:dolomite_bricks_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:dolomite_bricks"
  },
  "result": {
    "id": "smog:dolomite_bricks_slab",
    "count": 6
  }
}
```

## smog:dolomite_bricks_slab_from_dolomite_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:dolomite_bricks",
  "result": {
    "id": "smog:dolomite_bricks_slab",
    "count": 2
  }
}
```

## smog:dolomite_bricks_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:dolomite_bricks"
  },
  "result": {
    "id": "smog:dolomite_bricks_stairs",
    "count": 4
  }
}
```

## smog:dolomite_bricks_stairs_from_dolomite_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:dolomite_bricks",
  "result": {
    "id": "smog:dolomite_bricks_stairs",
    "count": 1
  }
}
```

## smog:dolomite_bricks_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:dolomite_bricks"
  },
  "result": {
    "id": "smog:dolomite_bricks_wall",
    "count": 6
  }
}
```

## smog:dolomite_bricks_wall_from_dolomite_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:dolomite_bricks",
  "result": {
    "id": "smog:dolomite_bricks_wall",
    "count": 1
  }
}
```

## smog:dolomite_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:dolomite"
  },
  "result": {
    "id": "smog:dolomite_slab",
    "count": 6
  }
}
```

## smog:dolomite_slab_from_dolomite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:dolomite",
  "result": {
    "id": "smog:dolomite_slab",
    "count": 2
  }
}
```

## smog:dolomite_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:dolomite"
  },
  "result": {
    "id": "smog:dolomite_stairs",
    "count": 4
  }
}
```

## smog:dolomite_stairs_from_dolomite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:dolomite",
  "result": {
    "id": "smog:dolomite_stairs",
    "count": 1
  }
}
```

## smog:dolomite_tiles

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SS",
    "SS"
  ],
  "key": {
    "S": "smog:dolomite_bricks"
  },
  "result": {
    "id": "smog:dolomite_tiles",
    "count": 4
  }
}
```

## smog:dolomite_tiles_from_dolomite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:dolomite",
  "result": {
    "id": "smog:dolomite_tiles",
    "count": 1
  }
}
```

## smog:dolomite_tiles_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:dolomite_tiles"
  },
  "result": {
    "id": "smog:dolomite_tiles_slab",
    "count": 6
  }
}
```

## smog:dolomite_tiles_slab_from_dolomite_tiles_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:dolomite_tiles",
  "result": {
    "id": "smog:dolomite_tiles_slab",
    "count": 2
  }
}
```

## smog:dolomite_tiles_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:dolomite_tiles"
  },
  "result": {
    "id": "smog:dolomite_tiles_stairs",
    "count": 4
  }
}
```

## smog:dolomite_tiles_stairs_from_dolomite_tiles_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:dolomite_tiles",
  "result": {
    "id": "smog:dolomite_tiles_stairs",
    "count": 1
  }
}
```

## smog:dolomite_tiles_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:dolomite_tiles"
  },
  "result": {
    "id": "smog:dolomite_tiles_wall",
    "count": 6
  }
}
```

## smog:dolomite_tiles_wall_from_dolomite_tiles_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:dolomite_tiles",
  "result": {
    "id": "smog:dolomite_tiles_wall",
    "count": 1
  }
}
```

## smog:dolomite_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:dolomite"
  },
  "result": {
    "id": "smog:dolomite_wall",
    "count": 6
  }
}
```

## smog:dolomite_wall_from_dolomite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:dolomite",
  "result": {
    "id": "smog:dolomite_wall",
    "count": 1
  }
}
```

## smog:duralumin_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:duralumin_ingot"
  },
  "result": {
    "id": "smog:duralumin_block"
  }
}
```

## smog:duralumin_ingot_from_blasting

```json
{
  "type": "minecraft:blasting",
  "ingredient": "smog:duralumin_dust",
  "result": {
    "id": "smog:duralumin_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:duralumin_ingot_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:duralumin_block"
  ],
  "result": {
    "id": "smog:duralumin_ingot",
    "count": 9
  }
}
```

## smog:duralumin_ingot_from_nuggets

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:duralumin_nugget"
  },
  "result": {
    "id": "smog:duralumin_ingot"
  }
}
```

## smog:duralumin_ingot_from_smelting

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:duralumin_dust",
  "result": {
    "id": "smog:duralumin_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:duralumin_nugget

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:duralumin_ingot"
  ],
  "result": {
    "id": "smog:duralumin_nugget",
    "count": 9
  }
}
```

## smog:electrum_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:electrum_ingot"
  },
  "result": {
    "id": "smog:electrum_block"
  }
}
```

## smog:electrum_ingot_from_blasting

```json
{
  "type": "minecraft:blasting",
  "ingredient": "smog:electrum_dust",
  "result": {
    "id": "smog:electrum_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:electrum_ingot_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:electrum_block"
  ],
  "result": {
    "id": "smog:electrum_ingot",
    "count": 9
  }
}
```

## smog:electrum_ingot_from_nuggets

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:electrum_nugget"
  },
  "result": {
    "id": "smog:electrum_ingot"
  }
}
```

## smog:electrum_ingot_from_smelting

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:electrum_dust",
  "result": {
    "id": "smog:electrum_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:electrum_nugget

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:electrum_ingot"
  ],
  "result": {
    "id": "smog:electrum_nugget",
    "count": 9
  }
}
```

## smog:fiber_binding

```json
{
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
```

## smog:fiber_press_basket

```json
{
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
```

## smog:fiber_string

```json
{
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
```

## smog:fiber_wrap

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:plant_fiber"
  ],
  "result": {
    "id": "smog:fiber_wrap",
    "count": 2
  }
}
```

## smog:firebrick_casing

```json
{
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
```

## smog:firebrick_firing

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:unfired_firebrick",
  "result": {
    "id": "smog:firebrick"
  },
  "experience": 0.1,
  "cookingtime": 200
}
```

## smog:firebrick_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:firebrick_casing"
  },
  "result": {
    "id": "smog:firebrick_slab",
    "count": 6
  }
}
```

## smog:firebrick_slab_from_firebrick_casing_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:firebrick_casing",
  "result": {
    "id": "smog:firebrick_slab",
    "count": 2
  }
}
```

## smog:firebrick_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:firebrick_casing"
  },
  "result": {
    "id": "smog:firebrick_stairs",
    "count": 4
  }
}
```

## smog:firebrick_stairs_from_firebrick_casing_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:firebrick_casing",
  "result": {
    "id": "smog:firebrick_stairs",
    "count": 1
  }
}
```

## smog:firebrick_tiles_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:firebrick_tiles"
  },
  "result": {
    "id": "smog:firebrick_tiles_slab",
    "count": 6
  }
}
```

## smog:firebrick_tiles_slab_from_firebrick_tiles_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:firebrick_tiles",
  "result": {
    "id": "smog:firebrick_tiles_slab",
    "count": 2
  }
}
```

## smog:firebrick_tiles_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:firebrick_tiles"
  },
  "result": {
    "id": "smog:firebrick_tiles_stairs",
    "count": 4
  }
}
```

## smog:firebrick_tiles_stairs_from_firebrick_tiles_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:firebrick_tiles",
  "result": {
    "id": "smog:firebrick_tiles_stairs",
    "count": 1
  }
}
```

## smog:firebrick_tiles_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:firebrick_tiles"
  },
  "result": {
    "id": "smog:firebrick_tiles_wall",
    "count": 6
  }
}
```

## smog:firebrick_tiles_wall_from_firebrick_tiles_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:firebrick_tiles",
  "result": {
    "id": "smog:firebrick_tiles_wall",
    "count": 1
  }
}
```

## smog:firebrick_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:firebrick_casing"
  },
  "result": {
    "id": "smog:firebrick_wall",
    "count": 6
  }
}
```

## smog:firebrick_wall_from_firebrick_casing_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:firebrick_casing",
  "result": {
    "id": "smog:firebrick_wall",
    "count": 1
  }
}
```

## smog:fitting_mold_firing

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:unfired_fitting_mold",
  "result": {
    "id": "smog:fitting_mold"
  },
  "experience": 0.1,
  "cookingtime": 200
}
```

## smog:fluid_canister

```json
{
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
```

## smog:fluorite_bricks

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SS",
    "SS"
  ],
  "key": {
    "S": "smog:polished_fluorite"
  },
  "result": {
    "id": "smog:fluorite_bricks",
    "count": 4
  }
}
```

## smog:fluorite_bricks_from_fluorite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:fluorite",
  "result": {
    "id": "smog:fluorite_bricks",
    "count": 1
  }
}
```

## smog:fluorite_bricks_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:fluorite_bricks"
  },
  "result": {
    "id": "smog:fluorite_bricks_slab",
    "count": 6
  }
}
```

## smog:fluorite_bricks_slab_from_fluorite_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:fluorite_bricks",
  "result": {
    "id": "smog:fluorite_bricks_slab",
    "count": 2
  }
}
```

## smog:fluorite_bricks_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:fluorite_bricks"
  },
  "result": {
    "id": "smog:fluorite_bricks_stairs",
    "count": 4
  }
}
```

## smog:fluorite_bricks_stairs_from_fluorite_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:fluorite_bricks",
  "result": {
    "id": "smog:fluorite_bricks_stairs",
    "count": 1
  }
}
```

## smog:fluorite_bricks_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:fluorite_bricks"
  },
  "result": {
    "id": "smog:fluorite_bricks_wall",
    "count": 6
  }
}
```

## smog:fluorite_bricks_wall_from_fluorite_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:fluorite_bricks",
  "result": {
    "id": "smog:fluorite_bricks_wall",
    "count": 1
  }
}
```

## smog:fluorite_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:fluorite"
  },
  "result": {
    "id": "smog:fluorite_slab",
    "count": 6
  }
}
```

## smog:fluorite_slab_from_fluorite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:fluorite",
  "result": {
    "id": "smog:fluorite_slab",
    "count": 2
  }
}
```

## smog:fluorite_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:fluorite"
  },
  "result": {
    "id": "smog:fluorite_stairs",
    "count": 4
  }
}
```

## smog:fluorite_stairs_from_fluorite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:fluorite",
  "result": {
    "id": "smog:fluorite_stairs",
    "count": 1
  }
}
```

## smog:fluorite_tiles

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SS",
    "SS"
  ],
  "key": {
    "S": "smog:fluorite_bricks"
  },
  "result": {
    "id": "smog:fluorite_tiles",
    "count": 4
  }
}
```

## smog:fluorite_tiles_from_fluorite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:fluorite",
  "result": {
    "id": "smog:fluorite_tiles",
    "count": 1
  }
}
```

## smog:fluorite_tiles_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:fluorite_tiles"
  },
  "result": {
    "id": "smog:fluorite_tiles_slab",
    "count": 6
  }
}
```

## smog:fluorite_tiles_slab_from_fluorite_tiles_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:fluorite_tiles",
  "result": {
    "id": "smog:fluorite_tiles_slab",
    "count": 2
  }
}
```

## smog:fluorite_tiles_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:fluorite_tiles"
  },
  "result": {
    "id": "smog:fluorite_tiles_stairs",
    "count": 4
  }
}
```

## smog:fluorite_tiles_stairs_from_fluorite_tiles_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:fluorite_tiles",
  "result": {
    "id": "smog:fluorite_tiles_stairs",
    "count": 1
  }
}
```

## smog:fluorite_tiles_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:fluorite_tiles"
  },
  "result": {
    "id": "smog:fluorite_tiles_wall",
    "count": 6
  }
}
```

## smog:fluorite_tiles_wall_from_fluorite_tiles_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:fluorite_tiles",
  "result": {
    "id": "smog:fluorite_tiles_wall",
    "count": 1
  }
}
```

## smog:fluorite_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:fluorite"
  },
  "result": {
    "id": "smog:fluorite_wall",
    "count": 6
  }
}
```

## smog:fluorite_wall_from_fluorite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:fluorite",
  "result": {
    "id": "smog:fluorite_wall",
    "count": 1
  }
}
```

## smog:freight_rope

```json
{
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
```

## smog:gold_concentrate_from_blasting

```json
{
  "type": "minecraft:blasting",
  "ingredient": "smog:gold_concentrate",
  "result": {
    "id": "minecraft:gold_ingot",
    "count": 2
  },
  "experience": 1.4,
  "cookingtime": 200
}
```

## smog:gold_concentrate_from_smelting

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:gold_concentrate",
  "result": {
    "id": "minecraft:gold_ingot",
    "count": 2
  },
  "experience": 1.4,
  "cookingtime": 400
}
```

## smog:graphite_bricks

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SS",
    "SS"
  ],
  "key": {
    "S": "smog:polished_graphite"
  },
  "result": {
    "id": "smog:graphite_bricks",
    "count": 4
  }
}
```

## smog:graphite_bricks_from_graphite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:graphite",
  "result": {
    "id": "smog:graphite_bricks",
    "count": 1
  }
}
```

## smog:graphite_bricks_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:graphite_bricks"
  },
  "result": {
    "id": "smog:graphite_bricks_slab",
    "count": 6
  }
}
```

## smog:graphite_bricks_slab_from_graphite_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:graphite_bricks",
  "result": {
    "id": "smog:graphite_bricks_slab",
    "count": 2
  }
}
```

## smog:graphite_bricks_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:graphite_bricks"
  },
  "result": {
    "id": "smog:graphite_bricks_stairs",
    "count": 4
  }
}
```

## smog:graphite_bricks_stairs_from_graphite_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:graphite_bricks",
  "result": {
    "id": "smog:graphite_bricks_stairs",
    "count": 1
  }
}
```

## smog:graphite_bricks_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:graphite_bricks"
  },
  "result": {
    "id": "smog:graphite_bricks_wall",
    "count": 6
  }
}
```

## smog:graphite_bricks_wall_from_graphite_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:graphite_bricks",
  "result": {
    "id": "smog:graphite_bricks_wall",
    "count": 1
  }
}
```

## smog:graphite_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:graphite"
  },
  "result": {
    "id": "smog:graphite_slab",
    "count": 6
  }
}
```

## smog:graphite_slab_from_graphite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:graphite",
  "result": {
    "id": "smog:graphite_slab",
    "count": 2
  }
}
```

## smog:graphite_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:graphite"
  },
  "result": {
    "id": "smog:graphite_stairs",
    "count": 4
  }
}
```

## smog:graphite_stairs_from_graphite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:graphite",
  "result": {
    "id": "smog:graphite_stairs",
    "count": 1
  }
}
```

## smog:graphite_tiles

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SS",
    "SS"
  ],
  "key": {
    "S": "smog:graphite_bricks"
  },
  "result": {
    "id": "smog:graphite_tiles",
    "count": 4
  }
}
```

## smog:graphite_tiles_from_graphite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:graphite",
  "result": {
    "id": "smog:graphite_tiles",
    "count": 1
  }
}
```

## smog:graphite_tiles_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:graphite_tiles"
  },
  "result": {
    "id": "smog:graphite_tiles_slab",
    "count": 6
  }
}
```

## smog:graphite_tiles_slab_from_graphite_tiles_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:graphite_tiles",
  "result": {
    "id": "smog:graphite_tiles_slab",
    "count": 2
  }
}
```

## smog:graphite_tiles_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:graphite_tiles"
  },
  "result": {
    "id": "smog:graphite_tiles_stairs",
    "count": 4
  }
}
```

## smog:graphite_tiles_stairs_from_graphite_tiles_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:graphite_tiles",
  "result": {
    "id": "smog:graphite_tiles_stairs",
    "count": 1
  }
}
```

## smog:graphite_tiles_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:graphite_tiles"
  },
  "result": {
    "id": "smog:graphite_tiles_wall",
    "count": 6
  }
}
```

## smog:graphite_tiles_wall_from_graphite_tiles_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:graphite_tiles",
  "result": {
    "id": "smog:graphite_tiles_wall",
    "count": 1
  }
}
```

## smog:graphite_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:graphite"
  },
  "result": {
    "id": "smog:graphite_wall",
    "count": 6
  }
}
```

## smog:graphite_wall_from_graphite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:graphite",
  "result": {
    "id": "smog:graphite_wall",
    "count": 1
  }
}
```

## smog:hammer_mold_firing

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:unfired_hammer_mold",
  "result": {
    "id": "smog:hammer_mold"
  },
  "experience": 0.1,
  "cookingtime": 200
}
```

## smog:hand_bellows

```json
{
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
```

## smog:hand_water_pump

```json
{
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
```

## smog:handcart

```json
{
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
```

## smog:hoe_mold_firing

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:unfired_hoe_mold",
  "result": {
    "id": "smog:hoe_mold"
  },
  "experience": 0.1,
  "cookingtime": 200
}
```

## smog:ingot_mold_firing

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:unfired_ingot_mold",
  "result": {
    "id": "smog:ingot_mold"
  },
  "experience": 0.1,
  "cookingtime": 200
}
```

## smog:invar_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:invar_ingot"
  },
  "result": {
    "id": "smog:invar_block"
  }
}
```

## smog:invar_ingot_from_blasting

```json
{
  "type": "minecraft:blasting",
  "ingredient": "smog:invar_dust",
  "result": {
    "id": "smog:invar_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:invar_ingot_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:invar_block"
  ],
  "result": {
    "id": "smog:invar_ingot",
    "count": 9
  }
}
```

## smog:invar_ingot_from_nuggets

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:invar_nugget"
  },
  "result": {
    "id": "smog:invar_ingot"
  }
}
```

## smog:invar_ingot_from_smelting

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:invar_dust",
  "result": {
    "id": "smog:invar_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:invar_nugget

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:invar_ingot"
  ],
  "result": {
    "id": "smog:invar_nugget",
    "count": 9
  }
}
```

## smog:iron_concentrate_from_blasting

```json
{
  "type": "minecraft:blasting",
  "ingredient": "smog:iron_concentrate",
  "result": {
    "id": "minecraft:iron_ingot",
    "count": 2
  },
  "experience": 1.4,
  "cookingtime": 200
}
```

## smog:iron_concentrate_from_smelting

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:iron_concentrate",
  "result": {
    "id": "minecraft:iron_ingot",
    "count": 2
  },
  "experience": 1.4,
  "cookingtime": 400
}
```

## smog:kaolin_bricks

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SS",
    "SS"
  ],
  "key": {
    "S": "smog:polished_kaolin"
  },
  "result": {
    "id": "smog:kaolin_bricks",
    "count": 4
  }
}
```

## smog:kaolin_bricks_from_kaolin_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:kaolin",
  "result": {
    "id": "smog:kaolin_bricks",
    "count": 1
  }
}
```

## smog:kaolin_bricks_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:kaolin_bricks"
  },
  "result": {
    "id": "smog:kaolin_bricks_slab",
    "count": 6
  }
}
```

## smog:kaolin_bricks_slab_from_kaolin_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:kaolin_bricks",
  "result": {
    "id": "smog:kaolin_bricks_slab",
    "count": 2
  }
}
```

## smog:kaolin_bricks_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:kaolin_bricks"
  },
  "result": {
    "id": "smog:kaolin_bricks_stairs",
    "count": 4
  }
}
```

## smog:kaolin_bricks_stairs_from_kaolin_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:kaolin_bricks",
  "result": {
    "id": "smog:kaolin_bricks_stairs",
    "count": 1
  }
}
```

## smog:kaolin_bricks_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:kaolin_bricks"
  },
  "result": {
    "id": "smog:kaolin_bricks_wall",
    "count": 6
  }
}
```

## smog:kaolin_bricks_wall_from_kaolin_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:kaolin_bricks",
  "result": {
    "id": "smog:kaolin_bricks_wall",
    "count": 1
  }
}
```

## smog:kaolin_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:kaolin"
  },
  "result": {
    "id": "smog:kaolin_slab",
    "count": 6
  }
}
```

## smog:kaolin_slab_from_kaolin_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:kaolin",
  "result": {
    "id": "smog:kaolin_slab",
    "count": 2
  }
}
```

## smog:kaolin_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:kaolin"
  },
  "result": {
    "id": "smog:kaolin_stairs",
    "count": 4
  }
}
```

## smog:kaolin_stairs_from_kaolin_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:kaolin",
  "result": {
    "id": "smog:kaolin_stairs",
    "count": 1
  }
}
```

## smog:kaolin_tiles

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SS",
    "SS"
  ],
  "key": {
    "S": "smog:kaolin_bricks"
  },
  "result": {
    "id": "smog:kaolin_tiles",
    "count": 4
  }
}
```

## smog:kaolin_tiles_from_kaolin_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:kaolin",
  "result": {
    "id": "smog:kaolin_tiles",
    "count": 1
  }
}
```

## smog:kaolin_tiles_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:kaolin_tiles"
  },
  "result": {
    "id": "smog:kaolin_tiles_slab",
    "count": 6
  }
}
```

## smog:kaolin_tiles_slab_from_kaolin_tiles_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:kaolin_tiles",
  "result": {
    "id": "smog:kaolin_tiles_slab",
    "count": 2
  }
}
```

## smog:kaolin_tiles_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:kaolin_tiles"
  },
  "result": {
    "id": "smog:kaolin_tiles_stairs",
    "count": 4
  }
}
```

## smog:kaolin_tiles_stairs_from_kaolin_tiles_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:kaolin_tiles",
  "result": {
    "id": "smog:kaolin_tiles_stairs",
    "count": 1
  }
}
```

## smog:kaolin_tiles_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:kaolin_tiles"
  },
  "result": {
    "id": "smog:kaolin_tiles_wall",
    "count": 6
  }
}
```

## smog:kaolin_tiles_wall_from_kaolin_tiles_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:kaolin_tiles",
  "result": {
    "id": "smog:kaolin_tiles_wall",
    "count": 1
  }
}
```

## smog:kaolin_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:kaolin"
  },
  "result": {
    "id": "smog:kaolin_wall",
    "count": 6
  }
}
```

## smog:kaolin_wall_from_kaolin_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:kaolin",
  "result": {
    "id": "smog:kaolin_wall",
    "count": 1
  }
}
```

## smog:katana_mold_firing

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:unfired_katana_mold",
  "result": {
    "id": "smog:katana_mold"
  },
  "experience": 0.1,
  "cookingtime": 200
}
```

## smog:kiln_vent

```json
{
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
```

## smog:large_ceramic_crucible_firing

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:unfired_large_crucible",
  "result": {
    "id": "smog:large_ceramic_crucible"
  },
  "experience": 0.1,
  "cookingtime": 200
}
```

## smog:lead_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:lead_ingot"
  },
  "result": {
    "id": "smog:lead_block"
  }
}
```

## smog:lead_ingot_from_blasting_crushed_lead_ore

```json
{
  "type": "minecraft:blasting",
  "group": "lead_ingot",
  "ingredient": "smog:crushed_lead_ore",
  "result": {
    "id": "smog:lead_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:lead_ingot_from_blasting_deepslate_lead_ore

```json
{
  "type": "minecraft:blasting",
  "group": "lead_ingot",
  "ingredient": "smog:deepslate_lead_ore",
  "result": {
    "id": "smog:lead_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:lead_ingot_from_blasting_lead_dust

```json
{
  "type": "minecraft:blasting",
  "group": "lead_ingot",
  "ingredient": "smog:lead_dust",
  "result": {
    "id": "smog:lead_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:lead_ingot_from_blasting_lead_ore

```json
{
  "type": "minecraft:blasting",
  "group": "lead_ingot",
  "ingredient": "smog:lead_ore",
  "result": {
    "id": "smog:lead_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:lead_ingot_from_blasting_lead_shard

```json
{
  "type": "minecraft:blasting",
  "group": "lead_ingot",
  "ingredient": "smog:lead_shard",
  "result": {
    "id": "smog:lead_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:lead_ingot_from_blasting_raw_lead

```json
{
  "type": "minecraft:blasting",
  "group": "lead_ingot",
  "ingredient": "smog:raw_lead",
  "result": {
    "id": "smog:lead_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:lead_ingot_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:lead_block"
  ],
  "result": {
    "id": "smog:lead_ingot",
    "count": 9
  }
}
```

## smog:lead_ingot_from_small

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:lead_nugget"
  },
  "result": {
    "id": "smog:lead_ingot"
  }
}
```

## smog:lead_ingot_from_smelting_crushed_lead_ore

```json
{
  "type": "minecraft:smelting",
  "group": "lead_ingot",
  "ingredient": "smog:crushed_lead_ore",
  "result": {
    "id": "smog:lead_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:lead_ingot_from_smelting_deepslate_lead_ore

```json
{
  "type": "minecraft:smelting",
  "group": "lead_ingot",
  "ingredient": "smog:deepslate_lead_ore",
  "result": {
    "id": "smog:lead_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:lead_ingot_from_smelting_lead_dust

```json
{
  "type": "minecraft:smelting",
  "group": "lead_ingot",
  "ingredient": "smog:lead_dust",
  "result": {
    "id": "smog:lead_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:lead_ingot_from_smelting_lead_ore

```json
{
  "type": "minecraft:smelting",
  "group": "lead_ingot",
  "ingredient": "smog:lead_ore",
  "result": {
    "id": "smog:lead_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:lead_ingot_from_smelting_lead_shard

```json
{
  "type": "minecraft:smelting",
  "group": "lead_ingot",
  "ingredient": "smog:lead_shard",
  "result": {
    "id": "smog:lead_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:lead_ingot_from_smelting_raw_lead

```json
{
  "type": "minecraft:smelting",
  "group": "lead_ingot",
  "ingredient": "smog:raw_lead",
  "result": {
    "id": "smog:lead_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:lead_nugget

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:lead_ingot"
  ],
  "result": {
    "id": "smog:lead_nugget",
    "count": 9
  }
}
```

## smog:leather_binding

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "minecraft:leather"
  ],
  "result": {
    "id": "smog:leather_binding",
    "count": 2
  }
}
```

## smog:leather_wrap

```json
{
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
```

## smog:lever_press

```json
{
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
```

## smog:limestone_bricks

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SS",
    "SS"
  ],
  "key": {
    "S": "smog:polished_limestone"
  },
  "result": {
    "id": "smog:limestone_bricks",
    "count": 4
  }
}
```

## smog:limestone_bricks_from_limestone_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:limestone",
  "result": {
    "id": "smog:limestone_bricks",
    "count": 1
  }
}
```

## smog:limestone_bricks_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:limestone_bricks"
  },
  "result": {
    "id": "smog:limestone_bricks_slab",
    "count": 6
  }
}
```

## smog:limestone_bricks_slab_from_limestone_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:limestone_bricks",
  "result": {
    "id": "smog:limestone_bricks_slab",
    "count": 2
  }
}
```

## smog:limestone_bricks_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:limestone_bricks"
  },
  "result": {
    "id": "smog:limestone_bricks_stairs",
    "count": 4
  }
}
```

## smog:limestone_bricks_stairs_from_limestone_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:limestone_bricks",
  "result": {
    "id": "smog:limestone_bricks_stairs",
    "count": 1
  }
}
```

## smog:limestone_bricks_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:limestone_bricks"
  },
  "result": {
    "id": "smog:limestone_bricks_wall",
    "count": 6
  }
}
```

## smog:limestone_bricks_wall_from_limestone_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:limestone_bricks",
  "result": {
    "id": "smog:limestone_bricks_wall",
    "count": 1
  }
}
```

## smog:limestone_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:limestone"
  },
  "result": {
    "id": "smog:limestone_slab",
    "count": 6
  }
}
```

## smog:limestone_slab_from_limestone_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:limestone",
  "result": {
    "id": "smog:limestone_slab",
    "count": 2
  }
}
```

## smog:limestone_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:limestone"
  },
  "result": {
    "id": "smog:limestone_stairs",
    "count": 4
  }
}
```

## smog:limestone_stairs_from_limestone_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:limestone",
  "result": {
    "id": "smog:limestone_stairs",
    "count": 1
  }
}
```

## smog:limestone_tiles

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SS",
    "SS"
  ],
  "key": {
    "S": "smog:limestone_bricks"
  },
  "result": {
    "id": "smog:limestone_tiles",
    "count": 4
  }
}
```

## smog:limestone_tiles_from_limestone_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:limestone",
  "result": {
    "id": "smog:limestone_tiles",
    "count": 1
  }
}
```

## smog:limestone_tiles_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:limestone_tiles"
  },
  "result": {
    "id": "smog:limestone_tiles_slab",
    "count": 6
  }
}
```

## smog:limestone_tiles_slab_from_limestone_tiles_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:limestone_tiles",
  "result": {
    "id": "smog:limestone_tiles_slab",
    "count": 2
  }
}
```

## smog:limestone_tiles_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:limestone_tiles"
  },
  "result": {
    "id": "smog:limestone_tiles_stairs",
    "count": 4
  }
}
```

## smog:limestone_tiles_stairs_from_limestone_tiles_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:limestone_tiles",
  "result": {
    "id": "smog:limestone_tiles_stairs",
    "count": 1
  }
}
```

## smog:limestone_tiles_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:limestone_tiles"
  },
  "result": {
    "id": "smog:limestone_tiles_wall",
    "count": 6
  }
}
```

## smog:limestone_tiles_wall_from_limestone_tiles_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:limestone_tiles",
  "result": {
    "id": "smog:limestone_tiles_wall",
    "count": 1
  }
}
```

## smog:limestone_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:limestone"
  },
  "result": {
    "id": "smog:limestone_wall",
    "count": 6
  }
}
```

## smog:limestone_wall_from_limestone_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:limestone",
  "result": {
    "id": "smog:limestone_wall",
    "count": 1
  }
}
```

## smog:lithium_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:lithium_ingot"
  },
  "result": {
    "id": "smog:lithium_block"
  }
}
```

## smog:lithium_ingot_from_blasting_crushed_lithium_ore

```json
{
  "type": "minecraft:blasting",
  "group": "lithium_ingot",
  "ingredient": "smog:crushed_lithium_ore",
  "result": {
    "id": "smog:lithium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:lithium_ingot_from_blasting_deepslate_lithium_ore

```json
{
  "type": "minecraft:blasting",
  "group": "lithium_ingot",
  "ingredient": "smog:deepslate_lithium_ore",
  "result": {
    "id": "smog:lithium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:lithium_ingot_from_blasting_lithium_dust

```json
{
  "type": "minecraft:blasting",
  "group": "lithium_ingot",
  "ingredient": "smog:lithium_dust",
  "result": {
    "id": "smog:lithium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:lithium_ingot_from_blasting_lithium_ore

```json
{
  "type": "minecraft:blasting",
  "group": "lithium_ingot",
  "ingredient": "smog:lithium_ore",
  "result": {
    "id": "smog:lithium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:lithium_ingot_from_blasting_lithium_shard

```json
{
  "type": "minecraft:blasting",
  "group": "lithium_ingot",
  "ingredient": "smog:lithium_shard",
  "result": {
    "id": "smog:lithium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:lithium_ingot_from_blasting_raw_lithium

```json
{
  "type": "minecraft:blasting",
  "group": "lithium_ingot",
  "ingredient": "smog:raw_lithium",
  "result": {
    "id": "smog:lithium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:lithium_ingot_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:lithium_block"
  ],
  "result": {
    "id": "smog:lithium_ingot",
    "count": 9
  }
}
```

## smog:lithium_ingot_from_small

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:lithium_nugget"
  },
  "result": {
    "id": "smog:lithium_ingot"
  }
}
```

## smog:lithium_ingot_from_smelting_crushed_lithium_ore

```json
{
  "type": "minecraft:smelting",
  "group": "lithium_ingot",
  "ingredient": "smog:crushed_lithium_ore",
  "result": {
    "id": "smog:lithium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:lithium_ingot_from_smelting_deepslate_lithium_ore

```json
{
  "type": "minecraft:smelting",
  "group": "lithium_ingot",
  "ingredient": "smog:deepslate_lithium_ore",
  "result": {
    "id": "smog:lithium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:lithium_ingot_from_smelting_lithium_dust

```json
{
  "type": "minecraft:smelting",
  "group": "lithium_ingot",
  "ingredient": "smog:lithium_dust",
  "result": {
    "id": "smog:lithium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:lithium_ingot_from_smelting_lithium_ore

```json
{
  "type": "minecraft:smelting",
  "group": "lithium_ingot",
  "ingredient": "smog:lithium_ore",
  "result": {
    "id": "smog:lithium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:lithium_ingot_from_smelting_lithium_shard

```json
{
  "type": "minecraft:smelting",
  "group": "lithium_ingot",
  "ingredient": "smog:lithium_shard",
  "result": {
    "id": "smog:lithium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:lithium_ingot_from_smelting_raw_lithium

```json
{
  "type": "minecraft:smelting",
  "group": "lithium_ingot",
  "ingredient": "smog:raw_lithium",
  "result": {
    "id": "smog:lithium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:lithium_nugget

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:lithium_ingot"
  ],
  "result": {
    "id": "smog:lithium_nugget",
    "count": 9
  }
}
```

## smog:loading_crane

```json
{
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
```

## smog:lubricating_compound

```json
{
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
```

## smog:machete_mold_firing

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:unfired_machete_mold",
  "result": {
    "id": "smog:machete_mold"
  },
  "experience": 0.1,
  "cookingtime": 200
}
```

## smog:manganese_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:manganese_ingot"
  },
  "result": {
    "id": "smog:manganese_block"
  }
}
```

## smog:manganese_ingot_from_blasting_crushed_manganese_ore

```json
{
  "type": "minecraft:blasting",
  "group": "manganese_ingot",
  "ingredient": "smog:crushed_manganese_ore",
  "result": {
    "id": "smog:manganese_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:manganese_ingot_from_blasting_deepslate_manganese_ore

```json
{
  "type": "minecraft:blasting",
  "group": "manganese_ingot",
  "ingredient": "smog:deepslate_manganese_ore",
  "result": {
    "id": "smog:manganese_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:manganese_ingot_from_blasting_manganese_dust

```json
{
  "type": "minecraft:blasting",
  "group": "manganese_ingot",
  "ingredient": "smog:manganese_dust",
  "result": {
    "id": "smog:manganese_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:manganese_ingot_from_blasting_manganese_ore

```json
{
  "type": "minecraft:blasting",
  "group": "manganese_ingot",
  "ingredient": "smog:manganese_ore",
  "result": {
    "id": "smog:manganese_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:manganese_ingot_from_blasting_manganese_shard

```json
{
  "type": "minecraft:blasting",
  "group": "manganese_ingot",
  "ingredient": "smog:manganese_shard",
  "result": {
    "id": "smog:manganese_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:manganese_ingot_from_blasting_raw_manganese

```json
{
  "type": "minecraft:blasting",
  "group": "manganese_ingot",
  "ingredient": "smog:raw_manganese",
  "result": {
    "id": "smog:manganese_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:manganese_ingot_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:manganese_block"
  ],
  "result": {
    "id": "smog:manganese_ingot",
    "count": 9
  }
}
```

## smog:manganese_ingot_from_small

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:manganese_nugget"
  },
  "result": {
    "id": "smog:manganese_ingot"
  }
}
```

## smog:manganese_ingot_from_smelting_crushed_manganese_ore

```json
{
  "type": "minecraft:smelting",
  "group": "manganese_ingot",
  "ingredient": "smog:crushed_manganese_ore",
  "result": {
    "id": "smog:manganese_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:manganese_ingot_from_smelting_deepslate_manganese_ore

```json
{
  "type": "minecraft:smelting",
  "group": "manganese_ingot",
  "ingredient": "smog:deepslate_manganese_ore",
  "result": {
    "id": "smog:manganese_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:manganese_ingot_from_smelting_manganese_dust

```json
{
  "type": "minecraft:smelting",
  "group": "manganese_ingot",
  "ingredient": "smog:manganese_dust",
  "result": {
    "id": "smog:manganese_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:manganese_ingot_from_smelting_manganese_ore

```json
{
  "type": "minecraft:smelting",
  "group": "manganese_ingot",
  "ingredient": "smog:manganese_ore",
  "result": {
    "id": "smog:manganese_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:manganese_ingot_from_smelting_manganese_shard

```json
{
  "type": "minecraft:smelting",
  "group": "manganese_ingot",
  "ingredient": "smog:manganese_shard",
  "result": {
    "id": "smog:manganese_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:manganese_ingot_from_smelting_raw_manganese

```json
{
  "type": "minecraft:smelting",
  "group": "manganese_ingot",
  "ingredient": "smog:raw_manganese",
  "result": {
    "id": "smog:manganese_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:manganese_nugget

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:manganese_ingot"
  ],
  "result": {
    "id": "smog:manganese_nugget",
    "count": 9
  }
}
```

## smog:molding_sand

```json
{
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
```

## smog:molybdenum_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:molybdenum_ingot"
  },
  "result": {
    "id": "smog:molybdenum_block"
  }
}
```

## smog:molybdenum_ingot_from_blasting_crushed_molybdenum_ore

```json
{
  "type": "minecraft:blasting",
  "group": "molybdenum_ingot",
  "ingredient": "smog:crushed_molybdenum_ore",
  "result": {
    "id": "smog:molybdenum_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:molybdenum_ingot_from_blasting_deepslate_molybdenum_ore

```json
{
  "type": "minecraft:blasting",
  "group": "molybdenum_ingot",
  "ingredient": "smog:deepslate_molybdenum_ore",
  "result": {
    "id": "smog:molybdenum_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:molybdenum_ingot_from_blasting_molybdenum_dust

```json
{
  "type": "minecraft:blasting",
  "group": "molybdenum_ingot",
  "ingredient": "smog:molybdenum_dust",
  "result": {
    "id": "smog:molybdenum_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:molybdenum_ingot_from_blasting_molybdenum_ore

```json
{
  "type": "minecraft:blasting",
  "group": "molybdenum_ingot",
  "ingredient": "smog:molybdenum_ore",
  "result": {
    "id": "smog:molybdenum_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:molybdenum_ingot_from_blasting_molybdenum_shard

```json
{
  "type": "minecraft:blasting",
  "group": "molybdenum_ingot",
  "ingredient": "smog:molybdenum_shard",
  "result": {
    "id": "smog:molybdenum_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:molybdenum_ingot_from_blasting_raw_molybdenum

```json
{
  "type": "minecraft:blasting",
  "group": "molybdenum_ingot",
  "ingredient": "smog:raw_molybdenum",
  "result": {
    "id": "smog:molybdenum_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:molybdenum_ingot_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:molybdenum_block"
  ],
  "result": {
    "id": "smog:molybdenum_ingot",
    "count": 9
  }
}
```

## smog:molybdenum_ingot_from_small

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:molybdenum_nugget"
  },
  "result": {
    "id": "smog:molybdenum_ingot"
  }
}
```

## smog:molybdenum_ingot_from_smelting_crushed_molybdenum_ore

```json
{
  "type": "minecraft:smelting",
  "group": "molybdenum_ingot",
  "ingredient": "smog:crushed_molybdenum_ore",
  "result": {
    "id": "smog:molybdenum_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:molybdenum_ingot_from_smelting_deepslate_molybdenum_ore

```json
{
  "type": "minecraft:smelting",
  "group": "molybdenum_ingot",
  "ingredient": "smog:deepslate_molybdenum_ore",
  "result": {
    "id": "smog:molybdenum_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:molybdenum_ingot_from_smelting_molybdenum_dust

```json
{
  "type": "minecraft:smelting",
  "group": "molybdenum_ingot",
  "ingredient": "smog:molybdenum_dust",
  "result": {
    "id": "smog:molybdenum_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:molybdenum_ingot_from_smelting_molybdenum_ore

```json
{
  "type": "minecraft:smelting",
  "group": "molybdenum_ingot",
  "ingredient": "smog:molybdenum_ore",
  "result": {
    "id": "smog:molybdenum_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:molybdenum_ingot_from_smelting_molybdenum_shard

```json
{
  "type": "minecraft:smelting",
  "group": "molybdenum_ingot",
  "ingredient": "smog:molybdenum_shard",
  "result": {
    "id": "smog:molybdenum_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:molybdenum_ingot_from_smelting_raw_molybdenum

```json
{
  "type": "minecraft:smelting",
  "group": "molybdenum_ingot",
  "ingredient": "smog:raw_molybdenum",
  "result": {
    "id": "smog:molybdenum_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:molybdenum_nugget

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:molybdenum_ingot"
  ],
  "result": {
    "id": "smog:molybdenum_nugget",
    "count": 9
  }
}
```

## smog:mossy_dolomite_bricks_from_moss_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:dolomite_bricks",
    "minecraft:moss_block"
  ],
  "result": {
    "id": "smog:mossy_dolomite_bricks"
  }
}
```

## smog:mossy_dolomite_bricks_from_vine

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:dolomite_bricks",
    "minecraft:vine"
  ],
  "result": {
    "id": "smog:mossy_dolomite_bricks"
  }
}
```

## smog:mossy_dolomite_bricks_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:mossy_dolomite_bricks"
  },
  "result": {
    "id": "smog:mossy_dolomite_bricks_slab",
    "count": 6
  }
}
```

## smog:mossy_dolomite_bricks_slab_from_mossy_dolomite_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:mossy_dolomite_bricks",
  "result": {
    "id": "smog:mossy_dolomite_bricks_slab",
    "count": 2
  }
}
```

## smog:mossy_dolomite_bricks_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:mossy_dolomite_bricks"
  },
  "result": {
    "id": "smog:mossy_dolomite_bricks_stairs",
    "count": 4
  }
}
```

## smog:mossy_dolomite_bricks_stairs_from_mossy_dolomite_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:mossy_dolomite_bricks",
  "result": {
    "id": "smog:mossy_dolomite_bricks_stairs",
    "count": 1
  }
}
```

## smog:mossy_dolomite_bricks_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:mossy_dolomite_bricks"
  },
  "result": {
    "id": "smog:mossy_dolomite_bricks_wall",
    "count": 6
  }
}
```

## smog:mossy_dolomite_bricks_wall_from_mossy_dolomite_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:mossy_dolomite_bricks",
  "result": {
    "id": "smog:mossy_dolomite_bricks_wall",
    "count": 1
  }
}
```

## smog:mossy_firebrick_bricks_from_moss_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:firebrick_casing",
    "minecraft:moss_block"
  ],
  "result": {
    "id": "smog:mossy_firebrick_bricks"
  }
}
```

## smog:mossy_firebrick_bricks_from_vine

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:firebrick_casing",
    "minecraft:vine"
  ],
  "result": {
    "id": "smog:mossy_firebrick_bricks"
  }
}
```

## smog:mossy_firebrick_bricks_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:mossy_firebrick_bricks"
  },
  "result": {
    "id": "smog:mossy_firebrick_bricks_slab",
    "count": 6
  }
}
```

## smog:mossy_firebrick_bricks_slab_from_mossy_firebrick_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:mossy_firebrick_bricks",
  "result": {
    "id": "smog:mossy_firebrick_bricks_slab",
    "count": 2
  }
}
```

## smog:mossy_firebrick_bricks_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:mossy_firebrick_bricks"
  },
  "result": {
    "id": "smog:mossy_firebrick_bricks_stairs",
    "count": 4
  }
}
```

## smog:mossy_firebrick_bricks_stairs_from_mossy_firebrick_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:mossy_firebrick_bricks",
  "result": {
    "id": "smog:mossy_firebrick_bricks_stairs",
    "count": 1
  }
}
```

## smog:mossy_firebrick_bricks_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:mossy_firebrick_bricks"
  },
  "result": {
    "id": "smog:mossy_firebrick_bricks_wall",
    "count": 6
  }
}
```

## smog:mossy_firebrick_bricks_wall_from_mossy_firebrick_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:mossy_firebrick_bricks",
  "result": {
    "id": "smog:mossy_firebrick_bricks_wall",
    "count": 1
  }
}
```

## smog:mossy_fluorite_bricks_from_moss_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:fluorite_bricks",
    "minecraft:moss_block"
  ],
  "result": {
    "id": "smog:mossy_fluorite_bricks"
  }
}
```

## smog:mossy_fluorite_bricks_from_vine

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:fluorite_bricks",
    "minecraft:vine"
  ],
  "result": {
    "id": "smog:mossy_fluorite_bricks"
  }
}
```

## smog:mossy_fluorite_bricks_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:mossy_fluorite_bricks"
  },
  "result": {
    "id": "smog:mossy_fluorite_bricks_slab",
    "count": 6
  }
}
```

## smog:mossy_fluorite_bricks_slab_from_mossy_fluorite_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:mossy_fluorite_bricks",
  "result": {
    "id": "smog:mossy_fluorite_bricks_slab",
    "count": 2
  }
}
```

## smog:mossy_fluorite_bricks_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:mossy_fluorite_bricks"
  },
  "result": {
    "id": "smog:mossy_fluorite_bricks_stairs",
    "count": 4
  }
}
```

## smog:mossy_fluorite_bricks_stairs_from_mossy_fluorite_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:mossy_fluorite_bricks",
  "result": {
    "id": "smog:mossy_fluorite_bricks_stairs",
    "count": 1
  }
}
```

## smog:mossy_fluorite_bricks_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:mossy_fluorite_bricks"
  },
  "result": {
    "id": "smog:mossy_fluorite_bricks_wall",
    "count": 6
  }
}
```

## smog:mossy_fluorite_bricks_wall_from_mossy_fluorite_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:mossy_fluorite_bricks",
  "result": {
    "id": "smog:mossy_fluorite_bricks_wall",
    "count": 1
  }
}
```

## smog:mossy_graphite_bricks_from_moss_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:graphite_bricks",
    "minecraft:moss_block"
  ],
  "result": {
    "id": "smog:mossy_graphite_bricks"
  }
}
```

## smog:mossy_graphite_bricks_from_vine

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:graphite_bricks",
    "minecraft:vine"
  ],
  "result": {
    "id": "smog:mossy_graphite_bricks"
  }
}
```

## smog:mossy_graphite_bricks_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:mossy_graphite_bricks"
  },
  "result": {
    "id": "smog:mossy_graphite_bricks_slab",
    "count": 6
  }
}
```

## smog:mossy_graphite_bricks_slab_from_mossy_graphite_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:mossy_graphite_bricks",
  "result": {
    "id": "smog:mossy_graphite_bricks_slab",
    "count": 2
  }
}
```

## smog:mossy_graphite_bricks_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:mossy_graphite_bricks"
  },
  "result": {
    "id": "smog:mossy_graphite_bricks_stairs",
    "count": 4
  }
}
```

## smog:mossy_graphite_bricks_stairs_from_mossy_graphite_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:mossy_graphite_bricks",
  "result": {
    "id": "smog:mossy_graphite_bricks_stairs",
    "count": 1
  }
}
```

## smog:mossy_graphite_bricks_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:mossy_graphite_bricks"
  },
  "result": {
    "id": "smog:mossy_graphite_bricks_wall",
    "count": 6
  }
}
```

## smog:mossy_graphite_bricks_wall_from_mossy_graphite_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:mossy_graphite_bricks",
  "result": {
    "id": "smog:mossy_graphite_bricks_wall",
    "count": 1
  }
}
```

## smog:mossy_kaolin_bricks_from_moss_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:kaolin_bricks",
    "minecraft:moss_block"
  ],
  "result": {
    "id": "smog:mossy_kaolin_bricks"
  }
}
```

## smog:mossy_kaolin_bricks_from_vine

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:kaolin_bricks",
    "minecraft:vine"
  ],
  "result": {
    "id": "smog:mossy_kaolin_bricks"
  }
}
```

## smog:mossy_kaolin_bricks_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:mossy_kaolin_bricks"
  },
  "result": {
    "id": "smog:mossy_kaolin_bricks_slab",
    "count": 6
  }
}
```

## smog:mossy_kaolin_bricks_slab_from_mossy_kaolin_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:mossy_kaolin_bricks",
  "result": {
    "id": "smog:mossy_kaolin_bricks_slab",
    "count": 2
  }
}
```

## smog:mossy_kaolin_bricks_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:mossy_kaolin_bricks"
  },
  "result": {
    "id": "smog:mossy_kaolin_bricks_stairs",
    "count": 4
  }
}
```

## smog:mossy_kaolin_bricks_stairs_from_mossy_kaolin_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:mossy_kaolin_bricks",
  "result": {
    "id": "smog:mossy_kaolin_bricks_stairs",
    "count": 1
  }
}
```

## smog:mossy_kaolin_bricks_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:mossy_kaolin_bricks"
  },
  "result": {
    "id": "smog:mossy_kaolin_bricks_wall",
    "count": 6
  }
}
```

## smog:mossy_kaolin_bricks_wall_from_mossy_kaolin_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:mossy_kaolin_bricks",
  "result": {
    "id": "smog:mossy_kaolin_bricks_wall",
    "count": 1
  }
}
```

## smog:mossy_limestone_bricks_from_moss_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:limestone_bricks",
    "minecraft:moss_block"
  ],
  "result": {
    "id": "smog:mossy_limestone_bricks"
  }
}
```

## smog:mossy_limestone_bricks_from_vine

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:limestone_bricks",
    "minecraft:vine"
  ],
  "result": {
    "id": "smog:mossy_limestone_bricks"
  }
}
```

## smog:mossy_limestone_bricks_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:mossy_limestone_bricks"
  },
  "result": {
    "id": "smog:mossy_limestone_bricks_slab",
    "count": 6
  }
}
```

## smog:mossy_limestone_bricks_slab_from_mossy_limestone_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:mossy_limestone_bricks",
  "result": {
    "id": "smog:mossy_limestone_bricks_slab",
    "count": 2
  }
}
```

## smog:mossy_limestone_bricks_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:mossy_limestone_bricks"
  },
  "result": {
    "id": "smog:mossy_limestone_bricks_stairs",
    "count": 4
  }
}
```

## smog:mossy_limestone_bricks_stairs_from_mossy_limestone_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:mossy_limestone_bricks",
  "result": {
    "id": "smog:mossy_limestone_bricks_stairs",
    "count": 1
  }
}
```

## smog:mossy_limestone_bricks_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:mossy_limestone_bricks"
  },
  "result": {
    "id": "smog:mossy_limestone_bricks_wall",
    "count": 6
  }
}
```

## smog:mossy_limestone_bricks_wall_from_mossy_limestone_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:mossy_limestone_bricks",
  "result": {
    "id": "smog:mossy_limestone_bricks_wall",
    "count": 1
  }
}
```

## smog:mossy_phosphorite_bricks_from_moss_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:phosphorite_bricks",
    "minecraft:moss_block"
  ],
  "result": {
    "id": "smog:mossy_phosphorite_bricks"
  }
}
```

## smog:mossy_phosphorite_bricks_from_vine

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:phosphorite_bricks",
    "minecraft:vine"
  ],
  "result": {
    "id": "smog:mossy_phosphorite_bricks"
  }
}
```

## smog:mossy_phosphorite_bricks_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:mossy_phosphorite_bricks"
  },
  "result": {
    "id": "smog:mossy_phosphorite_bricks_slab",
    "count": 6
  }
}
```

## smog:mossy_phosphorite_bricks_slab_from_mossy_phosphorite_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:mossy_phosphorite_bricks",
  "result": {
    "id": "smog:mossy_phosphorite_bricks_slab",
    "count": 2
  }
}
```

## smog:mossy_phosphorite_bricks_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:mossy_phosphorite_bricks"
  },
  "result": {
    "id": "smog:mossy_phosphorite_bricks_stairs",
    "count": 4
  }
}
```

## smog:mossy_phosphorite_bricks_stairs_from_mossy_phosphorite_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:mossy_phosphorite_bricks",
  "result": {
    "id": "smog:mossy_phosphorite_bricks_stairs",
    "count": 1
  }
}
```

## smog:mossy_phosphorite_bricks_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:mossy_phosphorite_bricks"
  },
  "result": {
    "id": "smog:mossy_phosphorite_bricks_wall",
    "count": 6
  }
}
```

## smog:mossy_phosphorite_bricks_wall_from_mossy_phosphorite_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:mossy_phosphorite_bricks",
  "result": {
    "id": "smog:mossy_phosphorite_bricks_wall",
    "count": 1
  }
}
```

## smog:mossy_rock_salt_bricks_from_moss_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:rock_salt_bricks",
    "minecraft:moss_block"
  ],
  "result": {
    "id": "smog:mossy_rock_salt_bricks"
  }
}
```

## smog:mossy_rock_salt_bricks_from_vine

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:rock_salt_bricks",
    "minecraft:vine"
  ],
  "result": {
    "id": "smog:mossy_rock_salt_bricks"
  }
}
```

## smog:mossy_rock_salt_bricks_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:mossy_rock_salt_bricks"
  },
  "result": {
    "id": "smog:mossy_rock_salt_bricks_slab",
    "count": 6
  }
}
```

## smog:mossy_rock_salt_bricks_slab_from_mossy_rock_salt_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:mossy_rock_salt_bricks",
  "result": {
    "id": "smog:mossy_rock_salt_bricks_slab",
    "count": 2
  }
}
```

## smog:mossy_rock_salt_bricks_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:mossy_rock_salt_bricks"
  },
  "result": {
    "id": "smog:mossy_rock_salt_bricks_stairs",
    "count": 4
  }
}
```

## smog:mossy_rock_salt_bricks_stairs_from_mossy_rock_salt_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:mossy_rock_salt_bricks",
  "result": {
    "id": "smog:mossy_rock_salt_bricks_stairs",
    "count": 1
  }
}
```

## smog:mossy_rock_salt_bricks_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:mossy_rock_salt_bricks"
  },
  "result": {
    "id": "smog:mossy_rock_salt_bricks_wall",
    "count": 6
  }
}
```

## smog:mossy_rock_salt_bricks_wall_from_mossy_rock_salt_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:mossy_rock_salt_bricks",
  "result": {
    "id": "smog:mossy_rock_salt_bricks_wall",
    "count": 1
  }
}
```

## smog:nichrome_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:nichrome_ingot"
  },
  "result": {
    "id": "smog:nichrome_block"
  }
}
```

## smog:nichrome_ingot_from_blasting

```json
{
  "type": "minecraft:blasting",
  "ingredient": "smog:nichrome_dust",
  "result": {
    "id": "smog:nichrome_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:nichrome_ingot_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:nichrome_block"
  ],
  "result": {
    "id": "smog:nichrome_ingot",
    "count": 9
  }
}
```

## smog:nichrome_ingot_from_nuggets

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:nichrome_nugget"
  },
  "result": {
    "id": "smog:nichrome_ingot"
  }
}
```

## smog:nichrome_ingot_from_smelting

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:nichrome_dust",
  "result": {
    "id": "smog:nichrome_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:nichrome_nugget

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:nichrome_ingot"
  ],
  "result": {
    "id": "smog:nichrome_nugget",
    "count": 9
  }
}
```

## smog:nickel_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:nickel_ingot"
  },
  "result": {
    "id": "smog:nickel_block"
  }
}
```

## smog:nickel_ingot_from_blasting_crushed_nickel_ore

```json
{
  "type": "minecraft:blasting",
  "group": "nickel_ingot",
  "ingredient": "smog:crushed_nickel_ore",
  "result": {
    "id": "smog:nickel_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:nickel_ingot_from_blasting_deepslate_nickel_ore

```json
{
  "type": "minecraft:blasting",
  "group": "nickel_ingot",
  "ingredient": "smog:deepslate_nickel_ore",
  "result": {
    "id": "smog:nickel_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:nickel_ingot_from_blasting_nickel_dust

```json
{
  "type": "minecraft:blasting",
  "group": "nickel_ingot",
  "ingredient": "smog:nickel_dust",
  "result": {
    "id": "smog:nickel_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:nickel_ingot_from_blasting_nickel_ore

```json
{
  "type": "minecraft:blasting",
  "group": "nickel_ingot",
  "ingredient": "smog:nickel_ore",
  "result": {
    "id": "smog:nickel_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:nickel_ingot_from_blasting_nickel_shard

```json
{
  "type": "minecraft:blasting",
  "group": "nickel_ingot",
  "ingredient": "smog:nickel_shard",
  "result": {
    "id": "smog:nickel_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:nickel_ingot_from_blasting_raw_nickel

```json
{
  "type": "minecraft:blasting",
  "group": "nickel_ingot",
  "ingredient": "smog:raw_nickel",
  "result": {
    "id": "smog:nickel_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:nickel_ingot_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:nickel_block"
  ],
  "result": {
    "id": "smog:nickel_ingot",
    "count": 9
  }
}
```

## smog:nickel_ingot_from_small

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:nickel_nugget"
  },
  "result": {
    "id": "smog:nickel_ingot"
  }
}
```

## smog:nickel_ingot_from_smelting_crushed_nickel_ore

```json
{
  "type": "minecraft:smelting",
  "group": "nickel_ingot",
  "ingredient": "smog:crushed_nickel_ore",
  "result": {
    "id": "smog:nickel_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:nickel_ingot_from_smelting_deepslate_nickel_ore

```json
{
  "type": "minecraft:smelting",
  "group": "nickel_ingot",
  "ingredient": "smog:deepslate_nickel_ore",
  "result": {
    "id": "smog:nickel_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:nickel_ingot_from_smelting_nickel_dust

```json
{
  "type": "minecraft:smelting",
  "group": "nickel_ingot",
  "ingredient": "smog:nickel_dust",
  "result": {
    "id": "smog:nickel_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:nickel_ingot_from_smelting_nickel_ore

```json
{
  "type": "minecraft:smelting",
  "group": "nickel_ingot",
  "ingredient": "smog:nickel_ore",
  "result": {
    "id": "smog:nickel_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:nickel_ingot_from_smelting_nickel_shard

```json
{
  "type": "minecraft:smelting",
  "group": "nickel_ingot",
  "ingredient": "smog:nickel_shard",
  "result": {
    "id": "smog:nickel_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:nickel_ingot_from_smelting_raw_nickel

```json
{
  "type": "minecraft:smelting",
  "group": "nickel_ingot",
  "ingredient": "smog:raw_nickel",
  "result": {
    "id": "smog:nickel_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:nickel_nugget

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:nickel_ingot"
  ],
  "result": {
    "id": "smog:nickel_nugget",
    "count": 9
  }
}
```

## smog:nickel_superalloy_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:nickel_superalloy_ingot"
  },
  "result": {
    "id": "smog:nickel_superalloy_block"
  }
}
```

## smog:nickel_superalloy_ingot_from_blasting

```json
{
  "type": "minecraft:blasting",
  "ingredient": "smog:nickel_superalloy_dust",
  "result": {
    "id": "smog:nickel_superalloy_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:nickel_superalloy_ingot_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:nickel_superalloy_block"
  ],
  "result": {
    "id": "smog:nickel_superalloy_ingot",
    "count": 9
  }
}
```

## smog:nickel_superalloy_ingot_from_nuggets

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:nickel_superalloy_nugget"
  },
  "result": {
    "id": "smog:nickel_superalloy_ingot"
  }
}
```

## smog:nickel_superalloy_ingot_from_smelting

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:nickel_superalloy_dust",
  "result": {
    "id": "smog:nickel_superalloy_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:nickel_superalloy_nugget

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:nickel_superalloy_ingot"
  ],
  "result": {
    "id": "smog:nickel_superalloy_nugget",
    "count": 9
  }
}
```

## smog:phosphorite_bricks

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SS",
    "SS"
  ],
  "key": {
    "S": "smog:polished_phosphorite"
  },
  "result": {
    "id": "smog:phosphorite_bricks",
    "count": 4
  }
}
```

## smog:phosphorite_bricks_from_phosphorite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:phosphorite",
  "result": {
    "id": "smog:phosphorite_bricks",
    "count": 1
  }
}
```

## smog:phosphorite_bricks_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:phosphorite_bricks"
  },
  "result": {
    "id": "smog:phosphorite_bricks_slab",
    "count": 6
  }
}
```

## smog:phosphorite_bricks_slab_from_phosphorite_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:phosphorite_bricks",
  "result": {
    "id": "smog:phosphorite_bricks_slab",
    "count": 2
  }
}
```

## smog:phosphorite_bricks_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:phosphorite_bricks"
  },
  "result": {
    "id": "smog:phosphorite_bricks_stairs",
    "count": 4
  }
}
```

## smog:phosphorite_bricks_stairs_from_phosphorite_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:phosphorite_bricks",
  "result": {
    "id": "smog:phosphorite_bricks_stairs",
    "count": 1
  }
}
```

## smog:phosphorite_bricks_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:phosphorite_bricks"
  },
  "result": {
    "id": "smog:phosphorite_bricks_wall",
    "count": 6
  }
}
```

## smog:phosphorite_bricks_wall_from_phosphorite_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:phosphorite_bricks",
  "result": {
    "id": "smog:phosphorite_bricks_wall",
    "count": 1
  }
}
```

## smog:phosphorite_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:phosphorite"
  },
  "result": {
    "id": "smog:phosphorite_slab",
    "count": 6
  }
}
```

## smog:phosphorite_slab_from_phosphorite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:phosphorite",
  "result": {
    "id": "smog:phosphorite_slab",
    "count": 2
  }
}
```

## smog:phosphorite_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:phosphorite"
  },
  "result": {
    "id": "smog:phosphorite_stairs",
    "count": 4
  }
}
```

## smog:phosphorite_stairs_from_phosphorite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:phosphorite",
  "result": {
    "id": "smog:phosphorite_stairs",
    "count": 1
  }
}
```

## smog:phosphorite_tiles

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SS",
    "SS"
  ],
  "key": {
    "S": "smog:phosphorite_bricks"
  },
  "result": {
    "id": "smog:phosphorite_tiles",
    "count": 4
  }
}
```

## smog:phosphorite_tiles_from_phosphorite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:phosphorite",
  "result": {
    "id": "smog:phosphorite_tiles",
    "count": 1
  }
}
```

## smog:phosphorite_tiles_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:phosphorite_tiles"
  },
  "result": {
    "id": "smog:phosphorite_tiles_slab",
    "count": 6
  }
}
```

## smog:phosphorite_tiles_slab_from_phosphorite_tiles_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:phosphorite_tiles",
  "result": {
    "id": "smog:phosphorite_tiles_slab",
    "count": 2
  }
}
```

## smog:phosphorite_tiles_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:phosphorite_tiles"
  },
  "result": {
    "id": "smog:phosphorite_tiles_stairs",
    "count": 4
  }
}
```

## smog:phosphorite_tiles_stairs_from_phosphorite_tiles_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:phosphorite_tiles",
  "result": {
    "id": "smog:phosphorite_tiles_stairs",
    "count": 1
  }
}
```

## smog:phosphorite_tiles_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:phosphorite_tiles"
  },
  "result": {
    "id": "smog:phosphorite_tiles_wall",
    "count": 6
  }
}
```

## smog:phosphorite_tiles_wall_from_phosphorite_tiles_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:phosphorite_tiles",
  "result": {
    "id": "smog:phosphorite_tiles_wall",
    "count": 1
  }
}
```

## smog:phosphorite_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:phosphorite"
  },
  "result": {
    "id": "smog:phosphorite_wall",
    "count": 6
  }
}
```

## smog:phosphorite_wall_from_phosphorite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:phosphorite",
  "result": {
    "id": "smog:phosphorite_wall",
    "count": 1
  }
}
```

## smog:pickaxe_mold_firing

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:unfired_pickaxe_mold",
  "result": {
    "id": "smog:pickaxe_mold"
  },
  "experience": 0.1,
  "cookingtime": 200
}
```

## smog:pipe_mold_firing

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:unfired_pipe_mold",
  "result": {
    "id": "smog:pipe_mold"
  },
  "experience": 0.1,
  "cookingtime": 200
}
```

## smog:plate_mold_firing

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:unfired_plate_mold",
  "result": {
    "id": "smog:plate_mold"
  },
  "experience": 0.1,
  "cookingtime": 200
}
```

## smog:polished_dolomite

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SS",
    "SS"
  ],
  "key": {
    "S": "smog:dolomite"
  },
  "result": {
    "id": "smog:polished_dolomite",
    "count": 4
  }
}
```

## smog:polished_dolomite_from_dolomite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:dolomite",
  "result": {
    "id": "smog:polished_dolomite",
    "count": 1
  }
}
```

## smog:polished_dolomite_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:polished_dolomite"
  },
  "result": {
    "id": "smog:polished_dolomite_slab",
    "count": 6
  }
}
```

## smog:polished_dolomite_slab_from_polished_dolomite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:polished_dolomite",
  "result": {
    "id": "smog:polished_dolomite_slab",
    "count": 2
  }
}
```

## smog:polished_dolomite_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:polished_dolomite"
  },
  "result": {
    "id": "smog:polished_dolomite_stairs",
    "count": 4
  }
}
```

## smog:polished_dolomite_stairs_from_polished_dolomite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:polished_dolomite",
  "result": {
    "id": "smog:polished_dolomite_stairs",
    "count": 1
  }
}
```

## smog:polished_dolomite_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:polished_dolomite"
  },
  "result": {
    "id": "smog:polished_dolomite_wall",
    "count": 6
  }
}
```

## smog:polished_dolomite_wall_from_polished_dolomite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:polished_dolomite",
  "result": {
    "id": "smog:polished_dolomite_wall",
    "count": 1
  }
}
```

## smog:polished_firebrick

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SS",
    "SS"
  ],
  "key": {
    "S": "smog:firebrick_casing"
  },
  "result": {
    "id": "smog:polished_firebrick",
    "count": 4
  }
}
```

## smog:polished_firebrick_from_firebrick_casing_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:firebrick_casing",
  "result": {
    "id": "smog:polished_firebrick",
    "count": 1
  }
}
```

## smog:polished_firebrick_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:polished_firebrick"
  },
  "result": {
    "id": "smog:polished_firebrick_slab",
    "count": 6
  }
}
```

## smog:polished_firebrick_slab_from_polished_firebrick_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:polished_firebrick",
  "result": {
    "id": "smog:polished_firebrick_slab",
    "count": 2
  }
}
```

## smog:polished_firebrick_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:polished_firebrick"
  },
  "result": {
    "id": "smog:polished_firebrick_stairs",
    "count": 4
  }
}
```

## smog:polished_firebrick_stairs_from_polished_firebrick_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:polished_firebrick",
  "result": {
    "id": "smog:polished_firebrick_stairs",
    "count": 1
  }
}
```

## smog:polished_firebrick_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:polished_firebrick"
  },
  "result": {
    "id": "smog:polished_firebrick_wall",
    "count": 6
  }
}
```

## smog:polished_firebrick_wall_from_polished_firebrick_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:polished_firebrick",
  "result": {
    "id": "smog:polished_firebrick_wall",
    "count": 1
  }
}
```

## smog:polished_fluorite

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SS",
    "SS"
  ],
  "key": {
    "S": "smog:fluorite"
  },
  "result": {
    "id": "smog:polished_fluorite",
    "count": 4
  }
}
```

## smog:polished_fluorite_from_fluorite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:fluorite",
  "result": {
    "id": "smog:polished_fluorite",
    "count": 1
  }
}
```

## smog:polished_fluorite_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:polished_fluorite"
  },
  "result": {
    "id": "smog:polished_fluorite_slab",
    "count": 6
  }
}
```

## smog:polished_fluorite_slab_from_polished_fluorite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:polished_fluorite",
  "result": {
    "id": "smog:polished_fluorite_slab",
    "count": 2
  }
}
```

## smog:polished_fluorite_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:polished_fluorite"
  },
  "result": {
    "id": "smog:polished_fluorite_stairs",
    "count": 4
  }
}
```

## smog:polished_fluorite_stairs_from_polished_fluorite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:polished_fluorite",
  "result": {
    "id": "smog:polished_fluorite_stairs",
    "count": 1
  }
}
```

## smog:polished_fluorite_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:polished_fluorite"
  },
  "result": {
    "id": "smog:polished_fluorite_wall",
    "count": 6
  }
}
```

## smog:polished_fluorite_wall_from_polished_fluorite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:polished_fluorite",
  "result": {
    "id": "smog:polished_fluorite_wall",
    "count": 1
  }
}
```

## smog:polished_graphite

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SS",
    "SS"
  ],
  "key": {
    "S": "smog:graphite"
  },
  "result": {
    "id": "smog:polished_graphite",
    "count": 4
  }
}
```

## smog:polished_graphite_from_graphite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:graphite",
  "result": {
    "id": "smog:polished_graphite",
    "count": 1
  }
}
```

## smog:polished_graphite_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:polished_graphite"
  },
  "result": {
    "id": "smog:polished_graphite_slab",
    "count": 6
  }
}
```

## smog:polished_graphite_slab_from_polished_graphite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:polished_graphite",
  "result": {
    "id": "smog:polished_graphite_slab",
    "count": 2
  }
}
```

## smog:polished_graphite_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:polished_graphite"
  },
  "result": {
    "id": "smog:polished_graphite_stairs",
    "count": 4
  }
}
```

## smog:polished_graphite_stairs_from_polished_graphite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:polished_graphite",
  "result": {
    "id": "smog:polished_graphite_stairs",
    "count": 1
  }
}
```

## smog:polished_graphite_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:polished_graphite"
  },
  "result": {
    "id": "smog:polished_graphite_wall",
    "count": 6
  }
}
```

## smog:polished_graphite_wall_from_polished_graphite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:polished_graphite",
  "result": {
    "id": "smog:polished_graphite_wall",
    "count": 1
  }
}
```

## smog:polished_kaolin

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SS",
    "SS"
  ],
  "key": {
    "S": "smog:kaolin"
  },
  "result": {
    "id": "smog:polished_kaolin",
    "count": 4
  }
}
```

## smog:polished_kaolin_from_kaolin_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:kaolin",
  "result": {
    "id": "smog:polished_kaolin",
    "count": 1
  }
}
```

## smog:polished_kaolin_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:polished_kaolin"
  },
  "result": {
    "id": "smog:polished_kaolin_slab",
    "count": 6
  }
}
```

## smog:polished_kaolin_slab_from_polished_kaolin_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:polished_kaolin",
  "result": {
    "id": "smog:polished_kaolin_slab",
    "count": 2
  }
}
```

## smog:polished_kaolin_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:polished_kaolin"
  },
  "result": {
    "id": "smog:polished_kaolin_stairs",
    "count": 4
  }
}
```

## smog:polished_kaolin_stairs_from_polished_kaolin_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:polished_kaolin",
  "result": {
    "id": "smog:polished_kaolin_stairs",
    "count": 1
  }
}
```

## smog:polished_kaolin_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:polished_kaolin"
  },
  "result": {
    "id": "smog:polished_kaolin_wall",
    "count": 6
  }
}
```

## smog:polished_kaolin_wall_from_polished_kaolin_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:polished_kaolin",
  "result": {
    "id": "smog:polished_kaolin_wall",
    "count": 1
  }
}
```

## smog:polished_limestone

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SS",
    "SS"
  ],
  "key": {
    "S": "smog:limestone"
  },
  "result": {
    "id": "smog:polished_limestone",
    "count": 4
  }
}
```

## smog:polished_limestone_from_limestone_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:limestone",
  "result": {
    "id": "smog:polished_limestone",
    "count": 1
  }
}
```

## smog:polished_limestone_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:polished_limestone"
  },
  "result": {
    "id": "smog:polished_limestone_slab",
    "count": 6
  }
}
```

## smog:polished_limestone_slab_from_polished_limestone_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:polished_limestone",
  "result": {
    "id": "smog:polished_limestone_slab",
    "count": 2
  }
}
```

## smog:polished_limestone_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:polished_limestone"
  },
  "result": {
    "id": "smog:polished_limestone_stairs",
    "count": 4
  }
}
```

## smog:polished_limestone_stairs_from_polished_limestone_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:polished_limestone",
  "result": {
    "id": "smog:polished_limestone_stairs",
    "count": 1
  }
}
```

## smog:polished_limestone_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:polished_limestone"
  },
  "result": {
    "id": "smog:polished_limestone_wall",
    "count": 6
  }
}
```

## smog:polished_limestone_wall_from_polished_limestone_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:polished_limestone",
  "result": {
    "id": "smog:polished_limestone_wall",
    "count": 1
  }
}
```

## smog:polished_phosphorite

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SS",
    "SS"
  ],
  "key": {
    "S": "smog:phosphorite"
  },
  "result": {
    "id": "smog:polished_phosphorite",
    "count": 4
  }
}
```

## smog:polished_phosphorite_from_phosphorite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:phosphorite",
  "result": {
    "id": "smog:polished_phosphorite",
    "count": 1
  }
}
```

## smog:polished_phosphorite_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:polished_phosphorite"
  },
  "result": {
    "id": "smog:polished_phosphorite_slab",
    "count": 6
  }
}
```

## smog:polished_phosphorite_slab_from_polished_phosphorite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:polished_phosphorite",
  "result": {
    "id": "smog:polished_phosphorite_slab",
    "count": 2
  }
}
```

## smog:polished_phosphorite_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:polished_phosphorite"
  },
  "result": {
    "id": "smog:polished_phosphorite_stairs",
    "count": 4
  }
}
```

## smog:polished_phosphorite_stairs_from_polished_phosphorite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:polished_phosphorite",
  "result": {
    "id": "smog:polished_phosphorite_stairs",
    "count": 1
  }
}
```

## smog:polished_phosphorite_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:polished_phosphorite"
  },
  "result": {
    "id": "smog:polished_phosphorite_wall",
    "count": 6
  }
}
```

## smog:polished_phosphorite_wall_from_polished_phosphorite_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:polished_phosphorite",
  "result": {
    "id": "smog:polished_phosphorite_wall",
    "count": 1
  }
}
```

## smog:polished_rock_salt

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SS",
    "SS"
  ],
  "key": {
    "S": "smog:rock_salt"
  },
  "result": {
    "id": "smog:polished_rock_salt",
    "count": 4
  }
}
```

## smog:polished_rock_salt_from_rock_salt_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:rock_salt",
  "result": {
    "id": "smog:polished_rock_salt",
    "count": 1
  }
}
```

## smog:polished_rock_salt_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:polished_rock_salt"
  },
  "result": {
    "id": "smog:polished_rock_salt_slab",
    "count": 6
  }
}
```

## smog:polished_rock_salt_slab_from_polished_rock_salt_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:polished_rock_salt",
  "result": {
    "id": "smog:polished_rock_salt_slab",
    "count": 2
  }
}
```

## smog:polished_rock_salt_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:polished_rock_salt"
  },
  "result": {
    "id": "smog:polished_rock_salt_stairs",
    "count": 4
  }
}
```

## smog:polished_rock_salt_stairs_from_polished_rock_salt_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:polished_rock_salt",
  "result": {
    "id": "smog:polished_rock_salt_stairs",
    "count": 1
  }
}
```

## smog:polished_rock_salt_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:polished_rock_salt"
  },
  "result": {
    "id": "smog:polished_rock_salt_wall",
    "count": 6
  }
}
```

## smog:polished_rock_salt_wall_from_polished_rock_salt_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:polished_rock_salt",
  "result": {
    "id": "smog:polished_rock_salt_wall",
    "count": 1
  }
}
```

## smog:potter_table

```json
{
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
```

## smog:press_basket

```json
{
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
```

## smog:press_basket_waxed

```json
{
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
```

## smog:press_frame

```json
{
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
```

## smog:raw_bauxite_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:raw_bauxite"
  },
  "result": {
    "id": "smog:raw_bauxite_block"
  }
}
```

## smog:raw_bauxite_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:raw_bauxite_block"
  ],
  "result": {
    "id": "smog:raw_bauxite",
    "count": 9
  }
}
```

## smog:raw_chromium_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:raw_chromium"
  },
  "result": {
    "id": "smog:raw_chromium_block"
  }
}
```

## smog:raw_chromium_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:raw_chromium_block"
  ],
  "result": {
    "id": "smog:raw_chromium",
    "count": 9
  }
}
```

## smog:raw_lead_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:raw_lead"
  },
  "result": {
    "id": "smog:raw_lead_block"
  }
}
```

## smog:raw_lead_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:raw_lead_block"
  ],
  "result": {
    "id": "smog:raw_lead",
    "count": 9
  }
}
```

## smog:raw_lithium_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:raw_lithium"
  },
  "result": {
    "id": "smog:raw_lithium_block"
  }
}
```

## smog:raw_lithium_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:raw_lithium_block"
  ],
  "result": {
    "id": "smog:raw_lithium",
    "count": 9
  }
}
```

## smog:raw_manganese_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:raw_manganese"
  },
  "result": {
    "id": "smog:raw_manganese_block"
  }
}
```

## smog:raw_manganese_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:raw_manganese_block"
  ],
  "result": {
    "id": "smog:raw_manganese",
    "count": 9
  }
}
```

## smog:raw_molybdenum_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:raw_molybdenum"
  },
  "result": {
    "id": "smog:raw_molybdenum_block"
  }
}
```

## smog:raw_molybdenum_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:raw_molybdenum_block"
  ],
  "result": {
    "id": "smog:raw_molybdenum",
    "count": 9
  }
}
```

## smog:raw_nickel_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:raw_nickel"
  },
  "result": {
    "id": "smog:raw_nickel_block"
  }
}
```

## smog:raw_nickel_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:raw_nickel_block"
  ],
  "result": {
    "id": "smog:raw_nickel",
    "count": 9
  }
}
```

## smog:raw_rare_earth_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:raw_rare_earth"
  },
  "result": {
    "id": "smog:raw_rare_earth_block"
  }
}
```

## smog:raw_rare_earth_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:raw_rare_earth_block"
  ],
  "result": {
    "id": "smog:raw_rare_earth",
    "count": 9
  }
}
```

## smog:raw_tin_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:raw_tin"
  },
  "result": {
    "id": "smog:raw_tin_block"
  }
}
```

## smog:raw_tin_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:raw_tin_block"
  ],
  "result": {
    "id": "smog:raw_tin",
    "count": 9
  }
}
```

## smog:raw_titanium_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:raw_titanium"
  },
  "result": {
    "id": "smog:raw_titanium_block"
  }
}
```

## smog:raw_titanium_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:raw_titanium_block"
  ],
  "result": {
    "id": "smog:raw_titanium",
    "count": 9
  }
}
```

## smog:raw_tungsten_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:raw_tungsten"
  },
  "result": {
    "id": "smog:raw_tungsten_block"
  }
}
```

## smog:raw_tungsten_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:raw_tungsten_block"
  ],
  "result": {
    "id": "smog:raw_tungsten",
    "count": 9
  }
}
```

## smog:raw_uranium_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:raw_uranium"
  },
  "result": {
    "id": "smog:raw_uranium_block"
  }
}
```

## smog:raw_uranium_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:raw_uranium_block"
  ],
  "result": {
    "id": "smog:raw_uranium",
    "count": 9
  }
}
```

## smog:raw_zinc_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:raw_zinc"
  },
  "result": {
    "id": "smog:raw_zinc_block"
  }
}
```

## smog:raw_zinc_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:raw_zinc_block"
  ],
  "result": {
    "id": "smog:raw_zinc",
    "count": 9
  }
}
```

## smog:rock_salt_bricks

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SS",
    "SS"
  ],
  "key": {
    "S": "smog:polished_rock_salt"
  },
  "result": {
    "id": "smog:rock_salt_bricks",
    "count": 4
  }
}
```

## smog:rock_salt_bricks_from_rock_salt_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:rock_salt",
  "result": {
    "id": "smog:rock_salt_bricks",
    "count": 1
  }
}
```

## smog:rock_salt_bricks_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:rock_salt_bricks"
  },
  "result": {
    "id": "smog:rock_salt_bricks_slab",
    "count": 6
  }
}
```

## smog:rock_salt_bricks_slab_from_rock_salt_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:rock_salt_bricks",
  "result": {
    "id": "smog:rock_salt_bricks_slab",
    "count": 2
  }
}
```

## smog:rock_salt_bricks_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:rock_salt_bricks"
  },
  "result": {
    "id": "smog:rock_salt_bricks_stairs",
    "count": 4
  }
}
```

## smog:rock_salt_bricks_stairs_from_rock_salt_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:rock_salt_bricks",
  "result": {
    "id": "smog:rock_salt_bricks_stairs",
    "count": 1
  }
}
```

## smog:rock_salt_bricks_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:rock_salt_bricks"
  },
  "result": {
    "id": "smog:rock_salt_bricks_wall",
    "count": 6
  }
}
```

## smog:rock_salt_bricks_wall_from_rock_salt_bricks_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:rock_salt_bricks",
  "result": {
    "id": "smog:rock_salt_bricks_wall",
    "count": 1
  }
}
```

## smog:rock_salt_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:rock_salt"
  },
  "result": {
    "id": "smog:rock_salt_slab",
    "count": 6
  }
}
```

## smog:rock_salt_slab_from_rock_salt_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:rock_salt",
  "result": {
    "id": "smog:rock_salt_slab",
    "count": 2
  }
}
```

## smog:rock_salt_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:rock_salt"
  },
  "result": {
    "id": "smog:rock_salt_stairs",
    "count": 4
  }
}
```

## smog:rock_salt_stairs_from_rock_salt_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:rock_salt",
  "result": {
    "id": "smog:rock_salt_stairs",
    "count": 1
  }
}
```

## smog:rock_salt_tiles

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SS",
    "SS"
  ],
  "key": {
    "S": "smog:rock_salt_bricks"
  },
  "result": {
    "id": "smog:rock_salt_tiles",
    "count": 4
  }
}
```

## smog:rock_salt_tiles_from_rock_salt_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:rock_salt",
  "result": {
    "id": "smog:rock_salt_tiles",
    "count": 1
  }
}
```

## smog:rock_salt_tiles_slab

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS"
  ],
  "key": {
    "S": "smog:rock_salt_tiles"
  },
  "result": {
    "id": "smog:rock_salt_tiles_slab",
    "count": 6
  }
}
```

## smog:rock_salt_tiles_slab_from_rock_salt_tiles_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:rock_salt_tiles",
  "result": {
    "id": "smog:rock_salt_tiles_slab",
    "count": 2
  }
}
```

## smog:rock_salt_tiles_stairs

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S  ",
    "SS ",
    "SSS"
  ],
  "key": {
    "S": "smog:rock_salt_tiles"
  },
  "result": {
    "id": "smog:rock_salt_tiles_stairs",
    "count": 4
  }
}
```

## smog:rock_salt_tiles_stairs_from_rock_salt_tiles_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:rock_salt_tiles",
  "result": {
    "id": "smog:rock_salt_tiles_stairs",
    "count": 1
  }
}
```

## smog:rock_salt_tiles_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:rock_salt_tiles"
  },
  "result": {
    "id": "smog:rock_salt_tiles_wall",
    "count": 6
  }
}
```

## smog:rock_salt_tiles_wall_from_rock_salt_tiles_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:rock_salt_tiles",
  "result": {
    "id": "smog:rock_salt_tiles_wall",
    "count": 1
  }
}
```

## smog:rock_salt_wall

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "SSS",
    "SSS"
  ],
  "key": {
    "S": "smog:rock_salt"
  },
  "result": {
    "id": "smog:rock_salt_wall",
    "count": 6
  }
}
```

## smog:rock_salt_wall_from_rock_salt_stonecutting

```json
{
  "type": "minecraft:stonecutting",
  "ingredient": "smog:rock_salt",
  "result": {
    "id": "smog:rock_salt_wall",
    "count": 1
  }
}
```

## smog:rope_anchor

```json
{
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
```

## smog:saw_mold_firing

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:unfired_saw_mold",
  "result": {
    "id": "smog:saw_mold"
  },
  "experience": 0.1,
  "cookingtime": 200
}
```

## smog:shovel_mold_firing

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:unfired_shovel_mold",
  "result": {
    "id": "smog:shovel_mold"
  },
  "experience": 0.1,
  "cookingtime": 200
}
```

## smog:sickle_mold_firing

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:unfired_sickle_mold",
  "result": {
    "id": "smog:sickle_mold"
  },
  "experience": 0.1,
  "cookingtime": 200
}
```

## smog:silver_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:silver_ingot"
  },
  "result": {
    "id": "smog:silver_block"
  }
}
```

## smog:silver_ingot_from_blasting

```json
{
  "type": "minecraft:blasting",
  "ingredient": "smog:silver_dust",
  "result": {
    "id": "smog:silver_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:silver_ingot_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:silver_block"
  ],
  "result": {
    "id": "smog:silver_ingot",
    "count": 9
  }
}
```

## smog:silver_ingot_from_nuggets

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:silver_nugget"
  },
  "result": {
    "id": "smog:silver_ingot"
  }
}
```

## smog:silver_ingot_from_smelting

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:silver_dust",
  "result": {
    "id": "smog:silver_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:silver_nugget

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:silver_ingot"
  ],
  "result": {
    "id": "smog:silver_nugget",
    "count": 9
  }
}
```

## smog:smeltery_fuel_tank

```json
{
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
```

## smog:smeltery_glass

```json
{
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
```

## smog:smeltery_item_port

```json
{
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
```

## smog:solder_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:solder_ingot"
  },
  "result": {
    "id": "smog:solder_block"
  }
}
```

## smog:solder_ingot_from_blasting

```json
{
  "type": "minecraft:blasting",
  "ingredient": "smog:solder_dust",
  "result": {
    "id": "smog:solder_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:solder_ingot_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:solder_block"
  ],
  "result": {
    "id": "smog:solder_ingot",
    "count": 9
  }
}
```

## smog:solder_ingot_from_nuggets

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:solder_nugget"
  },
  "result": {
    "id": "smog:solder_ingot"
  }
}
```

## smog:solder_ingot_from_smelting

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:solder_dust",
  "result": {
    "id": "smog:solder_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:solder_nugget

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:solder_ingot"
  ],
  "result": {
    "id": "smog:solder_nugget",
    "count": 9
  }
}
```

## smog:solid_firebox

```json
{
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
```

## smog:stainless_steel_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:stainless_steel_ingot"
  },
  "result": {
    "id": "smog:stainless_steel_block"
  }
}
```

## smog:stainless_steel_ingot_from_blasting

```json
{
  "type": "minecraft:blasting",
  "ingredient": "smog:stainless_steel_dust",
  "result": {
    "id": "smog:stainless_steel_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:stainless_steel_ingot_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:stainless_steel_block"
  ],
  "result": {
    "id": "smog:stainless_steel_ingot",
    "count": 9
  }
}
```

## smog:stainless_steel_ingot_from_nuggets

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:stainless_steel_nugget"
  },
  "result": {
    "id": "smog:stainless_steel_ingot"
  }
}
```

## smog:stainless_steel_ingot_from_smelting

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:stainless_steel_dust",
  "result": {
    "id": "smog:stainless_steel_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:stainless_steel_nugget

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:stainless_steel_ingot"
  ],
  "result": {
    "id": "smog:stainless_steel_nugget",
    "count": 9
  }
}
```

## smog:steel_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:steel_ingot"
  },
  "result": {
    "id": "smog:steel_block"
  }
}
```

## smog:steel_dust

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "minecraft:iron_ingot",
    "minecraft:coal"
  ],
  "result": {
    "id": "smog:steel_dust",
    "count": 1
  }
}
```

## smog:steel_ingot_from_blasting

```json
{
  "type": "minecraft:blasting",
  "ingredient": "smog:steel_dust",
  "result": {
    "id": "smog:steel_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:steel_ingot_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:steel_block"
  ],
  "result": {
    "id": "smog:steel_ingot",
    "count": 9
  }
}
```

## smog:steel_ingot_from_nuggets

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:steel_nugget"
  },
  "result": {
    "id": "smog:steel_ingot"
  }
}
```

## smog:steel_ingot_from_smelting

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:steel_dust",
  "result": {
    "id": "smog:steel_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:steel_nugget

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:steel_ingot"
  ],
  "result": {
    "id": "smog:steel_nugget",
    "count": 9
  }
}
```

## smog:sword_mold_firing

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:unfired_sword_mold",
  "result": {
    "id": "smog:sword_mold"
  },
  "experience": 0.1,
  "cookingtime": 200
}
```

## smog:tempered_clay

```json
{
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
```

## smog:tin_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:tin_ingot"
  },
  "result": {
    "id": "smog:tin_block"
  }
}
```

## smog:tin_ingot_from_blasting_crushed_tin_ore

```json
{
  "type": "minecraft:blasting",
  "group": "tin_ingot",
  "ingredient": "smog:crushed_tin_ore",
  "result": {
    "id": "smog:tin_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:tin_ingot_from_blasting_deepslate_tin_ore

```json
{
  "type": "minecraft:blasting",
  "group": "tin_ingot",
  "ingredient": "smog:deepslate_tin_ore",
  "result": {
    "id": "smog:tin_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:tin_ingot_from_blasting_raw_tin

```json
{
  "type": "minecraft:blasting",
  "group": "tin_ingot",
  "ingredient": "smog:raw_tin",
  "result": {
    "id": "smog:tin_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:tin_ingot_from_blasting_tin_dust

```json
{
  "type": "minecraft:blasting",
  "group": "tin_ingot",
  "ingredient": "smog:tin_dust",
  "result": {
    "id": "smog:tin_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:tin_ingot_from_blasting_tin_ore

```json
{
  "type": "minecraft:blasting",
  "group": "tin_ingot",
  "ingredient": "smog:tin_ore",
  "result": {
    "id": "smog:tin_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:tin_ingot_from_blasting_tin_shard

```json
{
  "type": "minecraft:blasting",
  "group": "tin_ingot",
  "ingredient": "smog:tin_shard",
  "result": {
    "id": "smog:tin_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:tin_ingot_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:tin_block"
  ],
  "result": {
    "id": "smog:tin_ingot",
    "count": 9
  }
}
```

## smog:tin_ingot_from_small

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:tin_nugget"
  },
  "result": {
    "id": "smog:tin_ingot"
  }
}
```

## smog:tin_ingot_from_smelting_crushed_tin_ore

```json
{
  "type": "minecraft:smelting",
  "group": "tin_ingot",
  "ingredient": "smog:crushed_tin_ore",
  "result": {
    "id": "smog:tin_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:tin_ingot_from_smelting_deepslate_tin_ore

```json
{
  "type": "minecraft:smelting",
  "group": "tin_ingot",
  "ingredient": "smog:deepslate_tin_ore",
  "result": {
    "id": "smog:tin_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:tin_ingot_from_smelting_raw_tin

```json
{
  "type": "minecraft:smelting",
  "group": "tin_ingot",
  "ingredient": "smog:raw_tin",
  "result": {
    "id": "smog:tin_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:tin_ingot_from_smelting_tin_dust

```json
{
  "type": "minecraft:smelting",
  "group": "tin_ingot",
  "ingredient": "smog:tin_dust",
  "result": {
    "id": "smog:tin_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:tin_ingot_from_smelting_tin_ore

```json
{
  "type": "minecraft:smelting",
  "group": "tin_ingot",
  "ingredient": "smog:tin_ore",
  "result": {
    "id": "smog:tin_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:tin_ingot_from_smelting_tin_shard

```json
{
  "type": "minecraft:smelting",
  "group": "tin_ingot",
  "ingredient": "smog:tin_shard",
  "result": {
    "id": "smog:tin_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:tin_nugget

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:tin_ingot"
  ],
  "result": {
    "id": "smog:tin_nugget",
    "count": 9
  }
}
```

## smog:titanium_alloy_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:titanium_alloy_ingot"
  },
  "result": {
    "id": "smog:titanium_alloy_block"
  }
}
```

## smog:titanium_alloy_ingot_from_blasting

```json
{
  "type": "minecraft:blasting",
  "ingredient": "smog:titanium_alloy_dust",
  "result": {
    "id": "smog:titanium_alloy_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:titanium_alloy_ingot_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:titanium_alloy_block"
  ],
  "result": {
    "id": "smog:titanium_alloy_ingot",
    "count": 9
  }
}
```

## smog:titanium_alloy_ingot_from_nuggets

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:titanium_alloy_nugget"
  },
  "result": {
    "id": "smog:titanium_alloy_ingot"
  }
}
```

## smog:titanium_alloy_ingot_from_smelting

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:titanium_alloy_dust",
  "result": {
    "id": "smog:titanium_alloy_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:titanium_alloy_nugget

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:titanium_alloy_ingot"
  ],
  "result": {
    "id": "smog:titanium_alloy_nugget",
    "count": 9
  }
}
```

## smog:titanium_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:titanium_ingot"
  },
  "result": {
    "id": "smog:titanium_block"
  }
}
```

## smog:titanium_ingot_from_blasting_crushed_titanium_ore

```json
{
  "type": "minecraft:blasting",
  "group": "titanium_ingot",
  "ingredient": "smog:crushed_titanium_ore",
  "result": {
    "id": "smog:titanium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:titanium_ingot_from_blasting_deepslate_titanium_ore

```json
{
  "type": "minecraft:blasting",
  "group": "titanium_ingot",
  "ingredient": "smog:deepslate_titanium_ore",
  "result": {
    "id": "smog:titanium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:titanium_ingot_from_blasting_raw_titanium

```json
{
  "type": "minecraft:blasting",
  "group": "titanium_ingot",
  "ingredient": "smog:raw_titanium",
  "result": {
    "id": "smog:titanium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:titanium_ingot_from_blasting_titanium_dust

```json
{
  "type": "minecraft:blasting",
  "group": "titanium_ingot",
  "ingredient": "smog:titanium_dust",
  "result": {
    "id": "smog:titanium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:titanium_ingot_from_blasting_titanium_ore

```json
{
  "type": "minecraft:blasting",
  "group": "titanium_ingot",
  "ingredient": "smog:titanium_ore",
  "result": {
    "id": "smog:titanium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:titanium_ingot_from_blasting_titanium_shard

```json
{
  "type": "minecraft:blasting",
  "group": "titanium_ingot",
  "ingredient": "smog:titanium_shard",
  "result": {
    "id": "smog:titanium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:titanium_ingot_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:titanium_block"
  ],
  "result": {
    "id": "smog:titanium_ingot",
    "count": 9
  }
}
```

## smog:titanium_ingot_from_small

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:titanium_nugget"
  },
  "result": {
    "id": "smog:titanium_ingot"
  }
}
```

## smog:titanium_ingot_from_smelting_crushed_titanium_ore

```json
{
  "type": "minecraft:smelting",
  "group": "titanium_ingot",
  "ingredient": "smog:crushed_titanium_ore",
  "result": {
    "id": "smog:titanium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:titanium_ingot_from_smelting_deepslate_titanium_ore

```json
{
  "type": "minecraft:smelting",
  "group": "titanium_ingot",
  "ingredient": "smog:deepslate_titanium_ore",
  "result": {
    "id": "smog:titanium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:titanium_ingot_from_smelting_raw_titanium

```json
{
  "type": "minecraft:smelting",
  "group": "titanium_ingot",
  "ingredient": "smog:raw_titanium",
  "result": {
    "id": "smog:titanium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:titanium_ingot_from_smelting_titanium_dust

```json
{
  "type": "minecraft:smelting",
  "group": "titanium_ingot",
  "ingredient": "smog:titanium_dust",
  "result": {
    "id": "smog:titanium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:titanium_ingot_from_smelting_titanium_ore

```json
{
  "type": "minecraft:smelting",
  "group": "titanium_ingot",
  "ingredient": "smog:titanium_ore",
  "result": {
    "id": "smog:titanium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:titanium_ingot_from_smelting_titanium_shard

```json
{
  "type": "minecraft:smelting",
  "group": "titanium_ingot",
  "ingredient": "smog:titanium_shard",
  "result": {
    "id": "smog:titanium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:titanium_nugget

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:titanium_ingot"
  ],
  "result": {
    "id": "smog:titanium_nugget",
    "count": 9
  }
}
```

## smog:tool_steel_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:tool_steel_ingot"
  },
  "result": {
    "id": "smog:tool_steel_block"
  }
}
```

## smog:tool_steel_ingot_from_blasting

```json
{
  "type": "minecraft:blasting",
  "ingredient": "smog:tool_steel_dust",
  "result": {
    "id": "smog:tool_steel_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:tool_steel_ingot_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:tool_steel_block"
  ],
  "result": {
    "id": "smog:tool_steel_ingot",
    "count": 9
  }
}
```

## smog:tool_steel_ingot_from_nuggets

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:tool_steel_nugget"
  },
  "result": {
    "id": "smog:tool_steel_ingot"
  }
}
```

## smog:tool_steel_ingot_from_smelting

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:tool_steel_dust",
  "result": {
    "id": "smog:tool_steel_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:tool_steel_nugget

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:tool_steel_ingot"
  ],
  "result": {
    "id": "smog:tool_steel_nugget",
    "count": 9
  }
}
```

## smog:tool_workbench

```json
{
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
```

## smog:tungsten_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:tungsten_ingot"
  },
  "result": {
    "id": "smog:tungsten_block"
  }
}
```

## smog:tungsten_ingot_from_blasting_crushed_tungsten_ore

```json
{
  "type": "minecraft:blasting",
  "group": "tungsten_ingot",
  "ingredient": "smog:crushed_tungsten_ore",
  "result": {
    "id": "smog:tungsten_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:tungsten_ingot_from_blasting_deepslate_tungsten_ore

```json
{
  "type": "minecraft:blasting",
  "group": "tungsten_ingot",
  "ingredient": "smog:deepslate_tungsten_ore",
  "result": {
    "id": "smog:tungsten_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:tungsten_ingot_from_blasting_raw_tungsten

```json
{
  "type": "minecraft:blasting",
  "group": "tungsten_ingot",
  "ingredient": "smog:raw_tungsten",
  "result": {
    "id": "smog:tungsten_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:tungsten_ingot_from_blasting_tungsten_dust

```json
{
  "type": "minecraft:blasting",
  "group": "tungsten_ingot",
  "ingredient": "smog:tungsten_dust",
  "result": {
    "id": "smog:tungsten_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:tungsten_ingot_from_blasting_tungsten_ore

```json
{
  "type": "minecraft:blasting",
  "group": "tungsten_ingot",
  "ingredient": "smog:tungsten_ore",
  "result": {
    "id": "smog:tungsten_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:tungsten_ingot_from_blasting_tungsten_shard

```json
{
  "type": "minecraft:blasting",
  "group": "tungsten_ingot",
  "ingredient": "smog:tungsten_shard",
  "result": {
    "id": "smog:tungsten_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:tungsten_ingot_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:tungsten_block"
  ],
  "result": {
    "id": "smog:tungsten_ingot",
    "count": 9
  }
}
```

## smog:tungsten_ingot_from_small

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:tungsten_nugget"
  },
  "result": {
    "id": "smog:tungsten_ingot"
  }
}
```

## smog:tungsten_ingot_from_smelting_crushed_tungsten_ore

```json
{
  "type": "minecraft:smelting",
  "group": "tungsten_ingot",
  "ingredient": "smog:crushed_tungsten_ore",
  "result": {
    "id": "smog:tungsten_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:tungsten_ingot_from_smelting_deepslate_tungsten_ore

```json
{
  "type": "minecraft:smelting",
  "group": "tungsten_ingot",
  "ingredient": "smog:deepslate_tungsten_ore",
  "result": {
    "id": "smog:tungsten_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:tungsten_ingot_from_smelting_raw_tungsten

```json
{
  "type": "minecraft:smelting",
  "group": "tungsten_ingot",
  "ingredient": "smog:raw_tungsten",
  "result": {
    "id": "smog:tungsten_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:tungsten_ingot_from_smelting_tungsten_dust

```json
{
  "type": "minecraft:smelting",
  "group": "tungsten_ingot",
  "ingredient": "smog:tungsten_dust",
  "result": {
    "id": "smog:tungsten_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:tungsten_ingot_from_smelting_tungsten_ore

```json
{
  "type": "minecraft:smelting",
  "group": "tungsten_ingot",
  "ingredient": "smog:tungsten_ore",
  "result": {
    "id": "smog:tungsten_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:tungsten_ingot_from_smelting_tungsten_shard

```json
{
  "type": "minecraft:smelting",
  "group": "tungsten_ingot",
  "ingredient": "smog:tungsten_shard",
  "result": {
    "id": "smog:tungsten_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:tungsten_nugget

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:tungsten_ingot"
  ],
  "result": {
    "id": "smog:tungsten_nugget",
    "count": 9
  }
}
```

## smog:tungsten_steel_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:tungsten_steel_ingot"
  },
  "result": {
    "id": "smog:tungsten_steel_block"
  }
}
```

## smog:tungsten_steel_ingot_from_blasting

```json
{
  "type": "minecraft:blasting",
  "ingredient": "smog:tungsten_steel_dust",
  "result": {
    "id": "smog:tungsten_steel_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:tungsten_steel_ingot_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:tungsten_steel_block"
  ],
  "result": {
    "id": "smog:tungsten_steel_ingot",
    "count": 9
  }
}
```

## smog:tungsten_steel_ingot_from_nuggets

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:tungsten_steel_nugget"
  },
  "result": {
    "id": "smog:tungsten_steel_ingot"
  }
}
```

## smog:tungsten_steel_ingot_from_smelting

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:tungsten_steel_dust",
  "result": {
    "id": "smog:tungsten_steel_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:tungsten_steel_nugget

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:tungsten_steel_ingot"
  ],
  "result": {
    "id": "smog:tungsten_steel_nugget",
    "count": 9
  }
}
```

## smog:unfired_crucible

```json
{
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
```

## smog:unfired_fitting_mold

```json
{
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
```

## smog:unfired_ingot_mold

```json
{
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
```

## smog:unfired_pipe_mold

```json
{
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
```

## smog:unfired_plate_mold

```json
{
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
```

## smog:uranium_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:uranium_ingot"
  },
  "result": {
    "id": "smog:uranium_block"
  }
}
```

## smog:uranium_ingot_from_blasting_crushed_uranium_ore

```json
{
  "type": "minecraft:blasting",
  "group": "uranium_ingot",
  "ingredient": "smog:crushed_uranium_ore",
  "result": {
    "id": "smog:uranium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:uranium_ingot_from_blasting_deepslate_uranium_ore

```json
{
  "type": "minecraft:blasting",
  "group": "uranium_ingot",
  "ingredient": "smog:deepslate_uranium_ore",
  "result": {
    "id": "smog:uranium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:uranium_ingot_from_blasting_raw_uranium

```json
{
  "type": "minecraft:blasting",
  "group": "uranium_ingot",
  "ingredient": "smog:raw_uranium",
  "result": {
    "id": "smog:uranium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:uranium_ingot_from_blasting_uranium_dust

```json
{
  "type": "minecraft:blasting",
  "group": "uranium_ingot",
  "ingredient": "smog:uranium_dust",
  "result": {
    "id": "smog:uranium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:uranium_ingot_from_blasting_uranium_ore

```json
{
  "type": "minecraft:blasting",
  "group": "uranium_ingot",
  "ingredient": "smog:uranium_ore",
  "result": {
    "id": "smog:uranium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:uranium_ingot_from_blasting_uranium_shard

```json
{
  "type": "minecraft:blasting",
  "group": "uranium_ingot",
  "ingredient": "smog:uranium_shard",
  "result": {
    "id": "smog:uranium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:uranium_ingot_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:uranium_block"
  ],
  "result": {
    "id": "smog:uranium_ingot",
    "count": 9
  }
}
```

## smog:uranium_ingot_from_small

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:uranium_nugget"
  },
  "result": {
    "id": "smog:uranium_ingot"
  }
}
```

## smog:uranium_ingot_from_smelting_crushed_uranium_ore

```json
{
  "type": "minecraft:smelting",
  "group": "uranium_ingot",
  "ingredient": "smog:crushed_uranium_ore",
  "result": {
    "id": "smog:uranium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:uranium_ingot_from_smelting_deepslate_uranium_ore

```json
{
  "type": "minecraft:smelting",
  "group": "uranium_ingot",
  "ingredient": "smog:deepslate_uranium_ore",
  "result": {
    "id": "smog:uranium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:uranium_ingot_from_smelting_raw_uranium

```json
{
  "type": "minecraft:smelting",
  "group": "uranium_ingot",
  "ingredient": "smog:raw_uranium",
  "result": {
    "id": "smog:uranium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:uranium_ingot_from_smelting_uranium_dust

```json
{
  "type": "minecraft:smelting",
  "group": "uranium_ingot",
  "ingredient": "smog:uranium_dust",
  "result": {
    "id": "smog:uranium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:uranium_ingot_from_smelting_uranium_ore

```json
{
  "type": "minecraft:smelting",
  "group": "uranium_ingot",
  "ingredient": "smog:uranium_ore",
  "result": {
    "id": "smog:uranium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:uranium_ingot_from_smelting_uranium_shard

```json
{
  "type": "minecraft:smelting",
  "group": "uranium_ingot",
  "ingredient": "smog:uranium_shard",
  "result": {
    "id": "smog:uranium_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:uranium_nugget

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:uranium_ingot"
  ],
  "result": {
    "id": "smog:uranium_nugget",
    "count": 9
  }
}
```

## smog:washing_section

```json
{
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
```

## smog:washing_sieve

```json
{
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
```

## smog:washing_trough

```json
{
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
```

## smog:water_outlet

```json
{
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
```

## smog:water_tank

```json
{
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
```

## smog:water_valve

```json
{
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
```

## smog:waxed_cord

```json
{
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
```

## smog:wooden_handle

```json
{
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
```

## smog:wool_wrap

```json
{
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
```

## smog:workshop_support

```json
{
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
```

## smog:wrought_iron_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:wrought_iron_ingot"
  },
  "result": {
    "id": "smog:wrought_iron_block"
  }
}
```

## smog:wrought_iron_ingot_from_blasting

```json
{
  "type": "minecraft:blasting",
  "ingredient": "smog:wrought_iron_dust",
  "result": {
    "id": "smog:wrought_iron_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:wrought_iron_ingot_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:wrought_iron_block"
  ],
  "result": {
    "id": "smog:wrought_iron_ingot",
    "count": 9
  }
}
```

## smog:wrought_iron_ingot_from_nuggets

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:wrought_iron_nugget"
  },
  "result": {
    "id": "smog:wrought_iron_ingot"
  }
}
```

## smog:wrought_iron_ingot_from_smelting

```json
{
  "type": "minecraft:smelting",
  "ingredient": "smog:wrought_iron_dust",
  "result": {
    "id": "smog:wrought_iron_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:wrought_iron_nugget

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:wrought_iron_ingot"
  ],
  "result": {
    "id": "smog:wrought_iron_nugget",
    "count": 9
  }
}
```

## smog:zinc_block

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:zinc_ingot"
  },
  "result": {
    "id": "smog:zinc_block"
  }
}
```

## smog:zinc_ingot_from_blasting_crushed_zinc_ore

```json
{
  "type": "minecraft:blasting",
  "group": "zinc_ingot",
  "ingredient": "smog:crushed_zinc_ore",
  "result": {
    "id": "smog:zinc_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:zinc_ingot_from_blasting_deepslate_zinc_ore

```json
{
  "type": "minecraft:blasting",
  "group": "zinc_ingot",
  "ingredient": "smog:deepslate_zinc_ore",
  "result": {
    "id": "smog:zinc_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:zinc_ingot_from_blasting_raw_zinc

```json
{
  "type": "minecraft:blasting",
  "group": "zinc_ingot",
  "ingredient": "smog:raw_zinc",
  "result": {
    "id": "smog:zinc_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:zinc_ingot_from_blasting_zinc_dust

```json
{
  "type": "minecraft:blasting",
  "group": "zinc_ingot",
  "ingredient": "smog:zinc_dust",
  "result": {
    "id": "smog:zinc_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:zinc_ingot_from_blasting_zinc_ore

```json
{
  "type": "minecraft:blasting",
  "group": "zinc_ingot",
  "ingredient": "smog:zinc_ore",
  "result": {
    "id": "smog:zinc_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:zinc_ingot_from_blasting_zinc_shard

```json
{
  "type": "minecraft:blasting",
  "group": "zinc_ingot",
  "ingredient": "smog:zinc_shard",
  "result": {
    "id": "smog:zinc_ingot"
  },
  "experience": 0.7,
  "cookingtime": 100
}
```

## smog:zinc_ingot_from_block

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:zinc_block"
  ],
  "result": {
    "id": "smog:zinc_ingot",
    "count": 9
  }
}
```

## smog:zinc_ingot_from_small

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "pattern": [
    "###",
    "###",
    "###"
  ],
  "key": {
    "#": "smog:zinc_nugget"
  },
  "result": {
    "id": "smog:zinc_ingot"
  }
}
```

## smog:zinc_ingot_from_smelting_crushed_zinc_ore

```json
{
  "type": "minecraft:smelting",
  "group": "zinc_ingot",
  "ingredient": "smog:crushed_zinc_ore",
  "result": {
    "id": "smog:zinc_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:zinc_ingot_from_smelting_deepslate_zinc_ore

```json
{
  "type": "minecraft:smelting",
  "group": "zinc_ingot",
  "ingredient": "smog:deepslate_zinc_ore",
  "result": {
    "id": "smog:zinc_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:zinc_ingot_from_smelting_raw_zinc

```json
{
  "type": "minecraft:smelting",
  "group": "zinc_ingot",
  "ingredient": "smog:raw_zinc",
  "result": {
    "id": "smog:zinc_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:zinc_ingot_from_smelting_zinc_dust

```json
{
  "type": "minecraft:smelting",
  "group": "zinc_ingot",
  "ingredient": "smog:zinc_dust",
  "result": {
    "id": "smog:zinc_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:zinc_ingot_from_smelting_zinc_ore

```json
{
  "type": "minecraft:smelting",
  "group": "zinc_ingot",
  "ingredient": "smog:zinc_ore",
  "result": {
    "id": "smog:zinc_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:zinc_ingot_from_smelting_zinc_shard

```json
{
  "type": "minecraft:smelting",
  "group": "zinc_ingot",
  "ingredient": "smog:zinc_shard",
  "result": {
    "id": "smog:zinc_ingot"
  },
  "experience": 0.7,
  "cookingtime": 200
}
```

## smog:zinc_nugget

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "smog:zinc_ingot"
  ],
  "result": {
    "id": "smog:zinc_nugget",
    "count": 9
  }
}
```
