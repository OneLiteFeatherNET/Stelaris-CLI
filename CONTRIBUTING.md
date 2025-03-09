# Contributing to Stelaris CLI

Thank you for your interest in contributing to Stelaris CLI! This document provides guidelines and steps to help you
contribute effectively.

## Code of Conduct

By participating in this project, you agree to maintain a respectful and inclusive environment for everyone.

## Prerequisites

Before you contribute to the project:

- Ensure your feature or bug fix isn't already in the project
- Check if there's an open issue for what you want to work on
- If not, consider opening an issue to discuss your proposed changes first

## Development Setup

1. Ensure you have the following installed:
    - JDK 21 or later
    - Gradle
    - Kotlin
    - Git

2. Clone the repository locally and set up the development environment:
   ```bash
   git clone https://github.com/yourusername/stelaris-cli.git
   cd stelaris-cli
   ./gradlew build
   ```

## Contribution Process

1. Fork the repository
2. Create a new branch
    - Use a descriptive name
    - Start with `feature/`, `fix/`, `docs/`, etc.
3. Make your changes
4. Commit your changes
    - Use descriptive commit messages
    - Follow conventional commit format if possible
5. Push your changes to your fork
6. Create a new pull request
    - Describe your changes thoroughly
    - Reference any related issues
    - Include screenshots if applicable

## Testing

- Add tests for new functionality
- Ensure all existing tests pass by running `./gradlew test`
- For data generation features, include sample outputs if possible

## Review Process

After submitting your pull request:

- Maintainers will review your changes
- You may need to address feedback and make additional commits
- Once approved, your PR will be merged

## License

By contributing to this project, your code/content will be licensed under the [Apache 2.0](LICENSE) license.

