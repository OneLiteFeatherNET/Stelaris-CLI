## Stelaris CLI

The `stelaris-cli` is a command-line tool that generates Dart files which contain data from the game Minecraft.
The data is extracted from the game using the [Microtus](https://github.com/OneLiteFeatherNET/Microtus) fork of Minestom
and transformed into Dart files using the code generation library [DartPoet](https://github.com/theEvilReaper/DartPoet).

> [!CAUTION]
> The project is currently in development and is not intended to be used by the public as a finished product.
> Missing data and bugs are to be expected.

### Versioning

The generated files are versioned after the Minecraft version they describe, with a `-rev` suffix that is incremented
whenever the same Minecraft version is generated again (e.g. `26.2-rev1`, then `26.2-rev2`). This makes it possible to
tell which generated package belongs to which Minecraft version, and which of them is the most recent one.

#### Generating a version

Pushing a tag of the form `v<minecraft-version>-rev<n>` (e.g. `v26.2-rev3`) generates the Dart project and pushes it to
the data repository under that version.

Which Minecraft version the CLI can actually generate for is not a free choice: it is decided by the Minestom version on
its classpath, and Minestom arrives without a version of its own through the `mycelium-bom`, so a bump of that BOM can
change it silently. The second half of a Minestom version string is the Minecraft version:

```
2026.08.16-26.2
           ^^^^ Minecraft version
```

`./gradlew resolveMinestomVersion` prints the current one. The tag is checked against it before anything is pushed, so a
tag naming a Minecraft version the build does not generate for fails instead of silently mislabelling the data.

### Usage

The CLI comes with a few arguments which can be used to customize the process of generating the Dart files.
Below is a list of all available arguments:

- `-update <major, minor, patch, rev>` - Updates the version tag of the generated files
- `-help` - Displays the help message
- `-experimental` - Enables experimental features

#### Examples

### Contributing

The tool does not generate all data from the game, so contributions are welcome. It should be noted that the data should
come from the game and not be added manually. For more information on how to contribute to the project,
see [CONTRIBUTING.md](CONTRIBUTING.md).
