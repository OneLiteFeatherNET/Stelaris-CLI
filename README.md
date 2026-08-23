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

Note that this is unrelated to the version of the CLI itself, which is a normal SemVer version managed by
release-please.

#### Where the Minecraft version comes from

The CLI generates whatever the Minestom version on its classpath exposes, and Minestom arrives without a version of its
own through the `mycelium-bom`. A bump of that BOM can therefore change the Minecraft version silently. The file
`minestom.version` mirrors the version the dependency graph actually resolves to - the second half of a Minestom
version string is the Minecraft version:

```
2026.08.16-26.2
           ^^^^ Minecraft version
```

The `Sync Minestom Version` workflow keeps that mirror up to date and, whenever it moves, generates and pushes the Dart
package for the new version. `./gradlew resolveMinestomVersion` prints the same value locally. To re-push a version by
hand, run the `Generate Dart Project` workflow - it always takes the Minecraft version from `minestom.version` and
refuses to run if that file has gone stale.

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
