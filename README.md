## Stelaris CLI

The `stelaris-cli` is a command-line tool that generates Dart files which contain data from the game Minecraft.
The data is extracted from the game using the [Microtus](https://github.com/OneLiteFeatherNET/Microtus) fork of Minestom
and transformed into Dart files using the code generation library [DartPoet](https://github.com/theEvilReaper/DartPoet).

> [!CAUTION]
> The project is currently in development and is not intended to be used by the public as a finished product.
> Missing data and bugs are to be expected.

### Versioning

For each call of the CLI, a new version tag is created for the generated files. To improve the identification which
version is related for which Minecraft version, the versioning schema follows the one of Minecraft with the addition of
a `-rv` tag. If a version for a specific Minecraft version is generated again, the revision tag will be incremented.

An example of a version tag would be `1.17.1-rv1`. This helps to identify the latest version for a specific Minecraft
version. If a new version is available, the revision tag will be incremented (e.g., `1.17.1-rv2`).

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
