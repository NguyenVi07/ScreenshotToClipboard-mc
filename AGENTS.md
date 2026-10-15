# Repository Guidelines

## Project Structure & Modules

This is a Gradle multi-project mod for Minecraft 26.3, targeting Fabric and NeoForge. Shared code and resources live in `common/`; loader-specific entry points and metadata live in `fabric/` and `neoforge/`. Root Gradle files manage shared versions and project configuration. Release notes are in `CHANGELOG.md`, and CI/release automation is in `.github/workflows/build-release.yml`. There is currently no dedicated test source tree.

## Build and Development

Use the Gradle wrapper so builds use the repository’s configured Gradle version:

- `./gradlew :fabric:build :neoforge:build --no-daemon` builds both mod jars (Windows: `.\gradlew.bat :fabric:build :neoforge:build --no-daemon`).
- `./gradlew :fabric:runClient` or `./gradlew :neoforge:runClient` launches a development client for that loader.

GitHub Actions runs the build for pushes and pull requests, and attaches artifacts when a GitHub Release is published. Prefer CI for verification when you do not have the Minecraft development environment installed locally.

## Coding Style & Naming

Use Java for mod code and follow the existing package namespace, `link.infra.screenshotclipboard`. Match surrounding formatting: tabs for Java indentation, PascalCase class names, and camelCase methods and fields. Keep shared behavior in `common/`; put loader APIs and registration in the corresponding loader module. Keep dependency and Minecraft version changes centralized in the Gradle configuration. No formatter or linter is currently configured.

## Testing

No automated test framework or test suite is currently configured. For code changes, run both loader builds above, or confirm the corresponding GitHub Actions run succeeds. Manually verify affected behavior in a development client when practical; include the loader and Minecraft version in any reported reproduction steps.

## Commits and Pull Requests

Recent history uses short, imperative commit subjects (for example, “Fix neoforge build”); follow that style and keep each commit focused. PRs should explain the behavior or compatibility change, list the loaders affected, link a related issue when applicable, and report the build/CI result. Update `CHANGELOG.md` for release-facing changes. Screenshots are only useful when a change affects visible game output.

## Release and Configuration

Keep release version and Minecraft compatibility metadata consistent across Gradle properties, loader metadata, and the changelog. Do not commit credentials or local IDE/build output. Release artifacts are produced by the GitHub Actions workflow from a published GitHub Release.
