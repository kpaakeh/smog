# Project setup

- Target: Minecraft 26.3, Fabric API 0.161.0+26.3.
- Route: official Fabric Loom 1.19.3, Loader 0.20.1, Java 25, Gradle Wrapper 9.8.2.
- Base: official FabricMC/fabric-example-mod branch 26.3; wrapper downloaded from that branch.
- Origin: https://github.com/kpaakeh/tech-mod.git; existing MIT license retained.
- Scope: starter project only; no game installation or saves modified.
- Validation: Gradle build passed with installed JDK 25.0.4; common/client classes and JAR compiled successfully. In-game launch was outside template setup scope.
- Environment: system JAVA_HOME points to JDK 21; README explains selecting JDK 25. Fixed processResources closure to support Gradle configuration cache.

## Foundations — 2026-10-27

- User preference: implement working foundations first; extend and polish afterwards.
- Implemented seven foundations: persistent dynamic zones, ore registration/worldgen, ordinary blocks, machine controllers, rotated multiblocks/assembled models, typed tags, water-like fluid families.
- Catalog: src/main/content/tech-mod.content.json. Gradle task generateContent creates bundled assets/data; runtime ContentCatalog registers matching objects. Includes small demonstration entries with vanilla texture references.
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
