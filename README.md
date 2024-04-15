## Stelaris CLI

The frontend of `Stelaris` has the ability to select data based on the version of the game Minecraft. This tool extracts
game-related data using the [Microtus](https://github.com/OneLiteFeatherNET/Microtus) fork of Minestom and
transforms it into various files for Dart using the
[DartPoet](https://github.com/theEvilReaper/DartPoet) library.

### Versioning

Each time the CLI is executed, it creates a new version tag for the generated files. It is crucial to use distinct
version tags to prevent misuse in projects reliant on this tool. The versioning schema follows that of Minecraft, with
the addition of a `-rv` tag.

An example of a version tag would be `1.17.1-rv1`. If a new version is generated for an existing version, the revision
tag
will be incremented. This aids in identifying the latest version for a specific Minecraft release.

### Usage

The CLI can only be used with specific arguments otherwise it will not work. The following arguments are required:

- -update <major, minor, patch, rev> updates the version tag of the generated files
- -help displays the help message
- -experimental enables experimental features
