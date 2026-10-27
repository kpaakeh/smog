# Project setup

- Target: Minecraft 26.3, Fabric API 0.161.0+26.3.
- Route: official Fabric Loom 1.19.3, Loader 0.20.1, Java 25, Gradle Wrapper 9.8.2.
- Base: official FabricMC/fabric-example-mod branch 26.3; wrapper downloaded from that branch.
- Origin: https://github.com/kpaakeh/smog.git; existing MIT license retained.
- Scope: starter project only; no game installation or saves modified.
- Validation: Gradle build passed with installed JDK 25.0.4; common/client classes and JAR compiled successfully. In-game launch was outside template setup scope.
- Environment: system JAVA_HOME points to JDK 21; README explains selecting JDK 25. Fixed processResources closure to support Gradle configuration cache.

## Foundations — 2026-10-27

- User preference: implement working foundations first; extend and polish afterwards.
- Implemented seven foundations: persistent dynamic zones, ore registration/worldgen, ordinary blocks, machine controllers, rotated multiblocks/assembled models, typed tags, water-like fluid families.
- Catalog: src/main/content/smog.content.json. Gradle task generateContent creates bundled assets/data; runtime ContentCatalog registers matching objects. Includes small demonstration entries with vanilla texture references.
- Zones: dimension SavedData, ellipsoid/box, velocity/growth, expiry, intensity, custom tick/enter/exit callbacks. Server-owned particles/sounds/status effects and 26.3 native ServerPlayer post effects. Persistent attachment tracks zone-owned shader IDs; overlapping zones reconcile the requested set.
- Multiblocks: relative positions and block/tag/air predicates, four rotations, no forced chunk loading. Every-tick validation prevents continued processing after dismantling. ASSEMBLED switches controller blockstate/model; other parts stay visible. Processing callback/progress is the machine foundation; inventories/energy/recipes are future work.
- JEI: optional 31.17.0.94, Fabric jei_mod_plugin entrypoint, public IRecipeType API, layered 7x7 tiled diagrams. Development flag -PwithJei adds JEI and MezzConfig 0.6.9.
- Fluids: source/flow/block/bucket, water-like physics, configurable tint/delay/source conversion; new FluidRenderingRegistry + FluidModel.Unbaked API.
- 26.3 pitfalls confirmed against local official artifacts: Identifier replaces ResourceLocation; facing property is EnumProperty<Direction>; worldgen feature replaces configured_feature with inline ore fields; loot conditions use inline {type: ...} or predicate IDs; Minecraft client screen is managed through Gui rather than Minecraft.setScreen.
- Validation: `build runClientGameTest -PwithJei` completed successfully on JDK 25.0.4. All 4 required server GameTests passed (three project tests plus Fabric's test); client test verified assembly/reversal sync, custom fluid state/rendering, JEI diagram opening, native shader application/removal. Earlier server build without JEI also passed.
- Screenshots inspected: build/run/clientGameTest/screenshots/{0000_foundation-blocks-and-fluid,0001_foundation-fluid,0002_foundation-jei-multiblock,0003_foundation-zone-shader}.png. They show the assembled controller, tinted coolant, JEI structure diagram, and green zone postprocessing.
- Release JAR checked: 121 entries, no test classes, decompiled code or Minecraft files. Test worlds, sources and downloaded dependencies remain ignored in build/.gradle caches.
- Known environment log noise: Windows OSHI performance counters, development Realms token, test-client anisotropic setting, vanilla end_of_frame post-effect lookup. Tests still completed successfully; no system settings changed.

### Research evidence

- Queries: `site.modrinth.com mod jei 26.3 Just Enough Items`; `site.docs.fabricmc.net 26.3 networking particles world generation blocks`.
- Primary JEI project: https://modrinth.com/mod/jei and https://github.com/mezz/JustEnoughItems . Modrinth API confirmed Fabric 26.3 version 31.17.0.94 and MezzConfig dependency; downloaded official JEI artifact was inspected with javap to confirm actual signatures and plugin discovery.
- Fabric test setup: https://docs.fabricmc.net/develop/automatic-testing . Used Loom configureTests, isolated gametest source set, Fabric server and client game tests.
- Knowledge-base index checked: https://github.com/rehan-remade/universal-modder/blob/main/knowledge/INDEX.md . Version-specific behavior was verified against the actual 26.3 artifacts instead of older examples.
- Source of truth for the 26.3 changes: Minecraft 26.3 JARs downloaded by Loom and their private generated sources, Fabric API 0.161.0+26.3 artifacts. Registry/loot formats were also validated by a real server world load, not just compilation.


## Smog 0.2.0 — materials and presentation

- User requested local, original art instead of paid Fal; 73 PNGs are created by tools/generate_textures.py using Pillow and fixed seeds. No external image generation or paid model calls were made.
- Added seven complete material families: apatite (gem), beryllium, vanadium, bismuth, cobalt, titanium, uranium. Each has stone/deepslate ore, raw ore, raw storage block, processed product/storage block, dust, crushed ore and nugget/shard: 63 material items.
- Catalog drives runtime registration and 130 recipes: reversible 9:1 packing, raw -> crushed -> dust, smelting/blasting, basic machine/casing/controller and coolant bucket recipes. Default values allow a small catalog entry; the texture generator creates every family asset.
- Seven worldgen features are injected into overworld biomes. Actual feature placement in a test stone volume passed for all seven, as did biome injection, packing/unpacking, dust smelting and all three creative-tab content checks.
- Native 26.3 PictureInPictureRenderer renders a shared depth-tested 3D scene for JEI. The preview supports drag rotation, wheel zoom, RMB blueprint/formed toggle and MMB reset. User will review mouse rotation manually; it is no longer driven in the final test script.
- Assembled controller renders one normalized model scaled to the recipe's full bounds and rotated around the anchor. Thread-safe snapshots hide original terrain meshes and other part BlockEntity renderers. Original blocks, collisions and inventories remain untouched; dismantling restores normal rendering without item duplication. Default Minecraft/Indigo rendering verified; other rendering backends are not claimed supported.
- Public Fabric creative inventory API is used in tests to select the correct tab page. setSelectedTab returning false can mean the requested tab is already selected; final assertions use getSelectedTab instead.
- User renamed the mod to Smog: mod ID/assets/data namespace smog, Java package io.github.kpaakeh.smog, Smog/SmogClient entrypoints, smogzone command, smog-0.2.0.jar. Text searches found no old branding in project sources or documentation.
- GitHub repository renamed through the official API with existing Git credentials: stable repository ID 1435728468 is now kpaakeh/smog; origin updated to https://github.com/kpaakeh/smog.git. No credentials were stored in project files or printed.
- Reference supplied by user: https://ru.wikipedia.org/wiki/Категория:Руды . Used as naming context; ore distribution, colors and processing are starter game design, not a metallurgy simulation.
- Local 26.3 sources and Fabric/JEI artifacts confirmed renderer extraction/submission, GuiRenderState PIP submission, client block-entity lifecycle, creative inventory pagination, and current recipe/loot formats.
- Final Smog validation: clean compilation/assembly passed, all 5 required server GameTests passed, then runClientGameTest -PwithJei passed after correcting the test's interpretation of setSelectedTab. All three Smog tabs were visually inspected with their actual contents, as were ore gallery, united model, JEI preview, fluid and dismantled rendering.
- Final release audit: smog-0.2.0.jar contains 130 recipes, 73 PNGs and 7 placed ore features, with no old namespace, test classes or Minecraft/decompiled files. Working tree and generated output remain in the existing workspace folder; GitHub project and all mod identifiers are Smog.
