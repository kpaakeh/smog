# Project setup

- Target: Minecraft 26.3, Fabric API 0.161.0+26.3.
- Route: official Fabric Loom 1.19.3, Loader 0.20.1, Java 25, Gradle Wrapper 9.8.2.
- Base: official FabricMC/fabric-example-mod branch 26.3; wrapper downloaded from that branch.
- Origin: https://github.com/kpaakeh/tech-mod.git; existing MIT license retained.
- Scope: starter project only; no game installation or saves modified.
- Validation: Gradle build passed with installed JDK 25.0.4; common/client classes and JAR compiled successfully. In-game launch was outside template setup scope.
- Environment: system JAVA_HOME points to JDK 21; README explains selecting JDK 25. Fixed processResources closure to support Gradle configuration cache.
