# Tech Mod

Шаблон Fabric-мода для Minecraft **26.3** с Fabric API **0.161.0+26.3**.

Добавлены базовые системы зон, блоков, руд, механизмов, мультиблоков, тегов и жидкостей.
Примеры регистрации и команды: [docs/FOUNDATIONS.md](docs/FOUNDATIONS.md).
Контент задаётся в `src/main/content/tech-mod.content.json`; ресурсы генерируются при сборке.

## Требования

- JDK 25 (также выберите её как Gradle JVM в IDE).
- Подключение к интернету для первой сборки.

## Сборка и запуск

Откройте папку как Gradle-проект в IntelliJ IDEA либо используйте PowerShell:

```powershell
.\gradlew.bat build
.\gradlew.bat runClient
.\gradlew.bat runServer
```

Если `JAVA_HOME` указывает на старую Java, выберите установленную JDK 25 перед сборкой:

```powershell
$env:JAVA_HOME = 'C:\Program Files\Java\jdk-25.0.4'
.\gradlew.bat build
```

При другой установке замените путь на свой каталог JDK 25.

На Linux/macOS: `sh ./gradlew build` или `sh ./gradlew runClient`.
Готовый мод: `build/libs/tech-mod-0.1.0.jar`.
Для тестового сервера после первого запуска прочитайте EULA и примите её в `run/eula.txt`, если согласны.

## Структура

- `src/main/java/io/github/kpaakeh/techmod/TechMod.java` — общий код и регистрация содержимого.
- `src/client/java/io/github/kpaakeh/techmod/TechModClient.java` — клиентский код.
- `src/main/resources/fabric.mod.json` — метаданные и точки входа.
- Ресурсы добавляйте в `src/main/resources/assets/tech_mod/`, данные — в `src/main/resources/data/tech_mod/`.
- `gradle.properties` — версии Minecraft, Fabric, мода и Maven group.

Mod ID: `tech_mod`. Java package: `io.github.kpaakeh.techmod`.
Minecraft 26.3 использует необфусцированные имена; отдельная зависимость Yarn не нужна.

## Git

Origin: https://github.com/kpaakeh/tech-mod.git, ветка `main`.
Чтобы сохранить и отправить шаблон:

```powershell
git add .
git commit -m "Initialize Fabric mod template for Minecraft 26.3"
git push origin main
```
