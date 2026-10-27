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

## Smog 0.3.0 — industry foundations and editable models

- Removed apatite. Six material families now expose 11 forms each: distinct shards, nuggets and plates. Removed crafting-table crushed/shard recipes; powered crusher and press have matching JEI categories.
- Added persistent single-block furnace/crusher/press, safe front-facing block/fluid actions, solid fuel generation, reusable GUI layouts/menu synchronization, LV/MV/HV connector networks, catenary wire geometry and selectable transformer output.
- Seven environmental multiblock generators share one controller and automatically select their pattern and formed model. Solar/lightning/thermal/flow/wind/steam/geothermal generation depends on world conditions. Heat and fluid consumption/balance are future work.
- Every technical block/component uses a separate editable JSON geometry model; formed generators have normalized whole-structure models. Ore/storage cubes remain cubes. Placeholder asset scripts preserve existing user PNGs and model JSONs by default; no paid services used.
- Fixed 26.3 SDL mouse mapping using InputConstants (left=1, middle=2, right=3). Fixed preview/formed-model depth writes by selecting an entity cutout render type instead of the default block-item sheet. User will verify mouse rotation manually.
- Validation: nine required server GameTests passed; JEI client GameTest passed, including wire snapshot arrival, real furnace output and GUI energy synchronization. Visually inspected JEI geometry overlap, catenary wire, machine GUI and assembled generator gallery.
- Editing paths and current foundation limits documented in docs/INDUSTRY.md. Models are static Java block JSON, editable with Blockbench. Original per-block collision remains after visual assembly; compatibility outside vanilla/Indigo is unverified.


## Smog 0.3.1 — preview depth, JEI names and coil stacking

- Corrected the preview coordinate system: native PictureInPictureRenderer applies a negative Z scale. The extra GUI Y inversion now also cancels that Z reflection, so displayed orbit angles and front/back depth agree. The earlier render-type change alone did not correct this. Verified roof panels occlude the frame correctly and a grass/gold diagnostic shows grass tops and solid side faces.
- Multiblock catalog entries now have display/display_ru names generated into multiblock.smog.* translations. JEI displays each structure name separately above its dimensions; the card keeps its original height for small windows.
- Completed/cancelled wire connections remove all four temporary selection keys and remove empty CUSTOM_DATA components. inventoryTick also cleans coordinate residue from 0.3.0 used coils, preserving unrelated custom data. Regression covers matching fresh-stack components and legacy cleanup.
- Client GameTest with JEI passed with solar and grass/gold depth screenshots. Final release validation recorded below.
- Final 0.3.1 validation: build and runClientGameTest -PwithJei succeeded; all 10 required server tests passed. Inspected the final solar JEI screenshot: named header, solid roof occlusion and both mouse-control lines fit within the card.


## Smog 0.3.2 — configured assembly, terrain hiding, PNG GUIs and Fabric energy

- Found two real causes of constituent meshes remaining visible: Fabric renderer API redirects SectionCompiler tessellation into AltModelBlockRenderer (Indigo), bypassing ModelBlockRenderer; LevelExtractor ignores dirty calls with equal BlockStates when requiresRender is false. Replaced the old tessellator mixin with render-shape/fluid suppression at SectionCompiler and explicitly dirty affected sections with neighbors after snapshot changes. Controller block entities remain available for the unified renderer.
- Assembly is explicit via ConfiguratorItem, retaining smog:wrench for saved-item compatibility. Right-click controller selects a complete pattern; incomplete patterns report failure. Ticks validate only the selected structure. Breaking a part dismantles it, repairing requires another click, unloaded chunks pause. Shift-right-click dismantles and rotates; regular machines retain click-to-rotate. MachineBlock skips its GUI fallback while the configurator is held.
- User clarified not to create a configurator model. Added optional item.model catalog override for custom Java Block/Item JSON; documented models/item/configurator.json and preserved existing item artwork.
- Replaced primitive GUI rectangle drawing with 17 editable PNGs for layouts, slots, button states, progress, fuel, energy and JEI preview background. Layout registration accepts an optional background Identifier. Text remains Minecraft font. Missing-only GUI asset script preserves manual artwork.
- Bundled teamreborn:energy:5.0.0 through Loom include and declared team_reborn_energy dependency. Public sided EnergyStorage wrappers use the existing persisted machine buffers, transaction snapshots and common per-tick limits across sides. Standard push transfers reach adjacent external devices and devices at remote wire terminals. Connectors accept external pushes with LV/MV/HV buffers 512/2048/8192. Native source and cable limits remain in effect. UI/JEI use FE, numerical buffers unchanged (1:1 Energy API units).
- Primary references: https://github.com/TechReborn/Energy/blob/master/README.md ; https://maven.fabricmc.net/teamreborn/energy/maven-metadata.xml . Confirmed 5.0.0 and its API signatures from official artifacts, compiled and ran on 26.3. This is Fabric API interoperability, not a claim of Forge/NeoForge loader support.
- Validation: build + runClientGameTest -PwithJei succeeded, all 12 required server tests passed. Tests cover actual configurator use/GUI priority, failed patterns, repair/reassembly, generator patterns, API rollback/commit, partial external storage, shared sided rate limit, wire output to external storage, and external push through a connector into a native machine.
- Client regression compiles an isolated section through actual Fabric/Indigo SectionCompiler: constituent terrain layers are empty after assembly while the controller BE remains; layers return after dismantling, and explicit reassembly hides them again. Visually inspected before/formed/broken solar screenshots and textured machine GUI with FE display. User PNGs and custom block models were not overwritten.

- Additional request: LV/MV/HV connectors now use ConnectorBlock with six-way attachment property, leaving old horizontal facing intact for saved-world compatibility. Placement follows clicked support face; blockstate model rotations match vanilla end_rod rotations from the actual 26.3 client artifact. Legacy connectors default to UP.
- Cable records persist both endpoint directions (legacy fields default UP), orient their anchors to the actual tips, preserve canonical endpoint/face pairing, and reject duplicate endpoint connections regardless of face metadata. Packet snapshots use the same codec. Added all-18 real BlockItem placements and direction-codec/anchor regression; server suite now has 13 required tests.


## Smog 0.4.0 — material conductors and own wire metals

- Latest user request replaces player-facing voltage tiers with six conductor materials: copper, aluminum, silver, steel, gold and tin. Wire material determines transfer/reach/color/thickness; machine FE input no longer rejects a conductor based on its material. The old transformer becomes an energy distributor with a numeric FE/t output cap.
- Added full aluminum, silver and tin ore families, all 11 forms each, their worldgen, tags and recipes. The project now has nine ore families (99 family forms) plus a processed steel alloy family (ingot/dust/nugget/plate/storage block). Steel mixture from iron+coal smelts/blasts into steel; plate uses the powered press. Generic alloys catalog registration separates processed metals from ore families.
- Six matching coil and connector recipes use their actual metals. Old registry/artwork IDs remain for copper (*_lv), gold (*_mv), silver (*_hv), preserving placed devices and item stacks. New types use aluminum/steel/tin suffixes. A dual wire codec accepts legacy voltage records and writes material records; maximum spans for legacy-mapped types are not reduced.
- Material base balance: copper128/24, aluminum64/48, silver512/48, steel32/64, gold256/32, tin32/16 (FE per tick / blocks). Connector buffers are eight nominal ticks. These are game-design starter values, not a claim to simulate real resistivity or power losses.
- No generated GUI labels use LV/MV/HV; GUI displays FE/t and translated material names. Kept old texture filenames for manual-art compatibility. Asset scripts generated only missing PNGs and models.
- Validation: all 15 required server GameTests passed with nine actual worldgen placements, steel production/pressing, six matching coil crafting recipes, six material throughput paths into public Energy API targets, 36 real BlockItem mount placements, legacy wire-codec migration, transactional API tests and explicit multiblock assembly. Full JEI client test passed; visually inspected PNG machine GUI with numeric FE/t, six connector material/mount variants, correctly anchored wire, unified-only solar rendering and ore tab.
- Final release audit: smog-0.4.0.jar built successfully with all 15 required server tests passing; contains nested energy-5.0.0.jar, nine ore families, one steel alloy family, six connector/coil materials and 17 GUI PNGs. No apatite, gametest classes or Minecraft classes in the release; no LV/MV/HV player-facing translations.


## Smog 0.5.0 — resource-only scope, industry retained as tested systems

- User narrowed the task to only the two latest requests: remove active legacy industry while preserving its systems, and implement the exact starter generation list (13 ores, 8 nonmetal deposits, 2 underground fluids). Earlier in-progress frame/era/inventory-model work was saved locally under .gradle/paused-eras-20261027 (manifest, binary diff and draft files), restored out of active source, and is not shipped.
- Active catalog machines/multiblocks/technical blocks are empty. Generator/controller/processor/world-action blocks, special components, connectors, coils and configurator registrations plus art/recipes moved to the independent gametest mod. Main sources retain FE, cable material/link/storage/rendering, machine/inventory/processing, GUI, multiblocks/JEI, fluid and zone APIs. Per-machine Energy API capability registration now occurs in Machines.register, including late test registrations. JEI processing categories guard absent device types.
- New generation: tin, zinc, lead, nickel, bauxite, manganese, chromium, titanium, tungsten, molybdenum, uranium, lithium, rare-earth ore. Bauxite/rare-earth use product:none (seven raw forms, no invented ingots or direct metal smelting). Eleven metallic families retain standard forms. Processed aluminum/silver/cobalt/bismuth remain separate, without independent ore generation.
- Added ModDeposits for limestone/dolomite/kaolin/rock salt/sulfur/fluorite/phosphorite/graphite: natural block, chunk, dust, biome injection and generated resources. Added actual underground source-fluid pockets for crude oil/mineralized brine using contained stone/deepslate replacement features. Oil excludes the water tag; brine keeps it; both are finite sources with buckets and animated textures. This is resource generation, not extraction machinery/reservoir engineering.
- Fixed Gradle DSL local-name collision (deposit Russian label shadowed the ru translation map). Isolated test content and data to keep full system coverage after removing production registrations. Catalog-owned creative tab contents prevent test fixtures leaking into material tabs. Removed the empty industry tab.
- Validation: clean build loaded the data and all 17 required server GameTests passed, including actual placement and biome injection of every one of the 23 resources. Client tests passed after adapting the gallery to mineral-only ore families; checked mineral/ore tabs, liquid rendering, JEI and the retained systems on test-only machines. Final blocks tab contains exactly eight natural blocks and three material buckets.
- Release audit: no active industry item/blockstate/recipe registrations, no test resources/classes, no paused frame/era content; 23 placed-feature JSONs, nested Energy 5.0.0 and retained API classes present. Texture scripts preserve existing art and do not regenerate dormant industry assets. User-facing scope documentation is README and docs/RESOURCES.md.


## Smog 0.6.0 — material geology and texture cleanup only

- User invoked the materials section of local docs/EPOCHS_AND_RESOURCES_CONCEPT.md and its point 7, then reiterated materials-only scope. Read the complete materials section; no mechanisms, age progression, processing chains, prospecting devices or nuclear machines were added. Uranium is mandatory because nuclear power is explicitly planned; recorded in resource_policy and docs/PROJECT_DECISIONS.md.
- Confirmed native minecraft:sulfur and variants against actual 26.3 Blocks/Items sources. Removed Smog sulfur deposit/chunk/dust and its three textures. Future sulfur recipe tags point to minecraft:sulfur. Current custom worldgen: 13 ores + 7 nonmetal deposits + 2 fluid reservoirs = 22 placed-feature entries.
- Implemented bounded ResourceDepositFeature using 26.3's real Feature/MapCodec/FEATURE_TYPE API: coherent bodies, flat/wavy strata, shallow lenses, sediment-hosted placers and sealed fluid reservoirs. Small accessible veins and rare specialist veins retain vanilla ore geometry with different rarity. Shared 96-block geological richness regions align titanium/rare-earth placer potential without a rare-biome lock. Every write stays local, replaces appropriate natural material, and uses no forced chunk loading.
- Surface resources use OCEAN_FLOOR_WG plus current 26.3 offset-placement providers. Profile-specific catalog fields avoid unused vein-size/radius/height settings. Frequencies/dimensions remain editable starter balance. Raw material IDs and old item stacks remain stable; potential byproducts/mineral names are metadata, never hidden per-block random components or rare extra drops.
- Artist cleanup: main textures contains exactly 195 active resource PNGs; no unused or missing PNGs according to the new read-only reachable-model/fluid audit. Inactive GUI textures moved to test resources, preserving system tests and avoiding clutter in the artist folder. GUI generation targets test resources while no machines are registered. docs/TEXTURES.md identifies precisely what to paint.
- Validation: build passed all 18 required server GameTests; client test passed. Tests check larger coherent deposits, sand-vs-rock host behavior, all resource placements, mandatory uranium, native sulfur and no duplicate Smog sulfur, plus all retained systems. No active machine or era content was restored.

## 2026-10-28 — Ore distribution balance

Reduced solid ore bodies from radii 10–12 to 2–4, with resource-specific placement rarity. Tin/zinc remain accessible; advanced ores and titanium/rare-earth placers are scarce. Reduced mineral masses while keeping limestone/dolomite larger than ores. Existing chunks and textures unchanged. Generation tests now bound deposit sizes and specifically prevent massive titanium placers.
