# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## [UNRELEASED]

### Changed

- Published MoreIotas to https://maven.hexxy.media.
- Converted Treasurer's Purification into an operator - the item argument can now take either a stack iota or a type iota.
- Made Cave Air and Void Air block types falsy to match normal Air.
- Updated zh_cn translations, by ChuijkYahus in [#55](https://github.com/FallingColors/MoreIotas/pull/55), [#58](https://github.com/FallingColors/MoreIotas/pull/58), and [#62](https://github.com/FallingColors/MoreIotas/pull/62).
- Moved certain lang keys for type iotas from Hexal into MoreIotas, since MoreIotas is what actually implements type iotas.
- Development: Removed `moreiotas.serialization-hooks` dependency on Fabric.

### Fixed

- Fixed item stack iotas using an untranslated lang key when referenced in a mishap message.
- Fixed the element-wise matrix operations not coercing numbers or vectors to matrices like the other matrix patterns do.
- Fixed Patternmaster's Purification throwing an exception when used on any non-static pattern, by beholderface in [#59](https://github.com/FallingColors/MoreIotas/pull/59).
- Fixed Listener's Reflection ignoring all messages from anyone with a Sifter's Gambit prefix set, by pythonmcpi in [#64](https://github.com/FallingColors/MoreIotas/pull/64).
- Fixed a crash when trying to invert a non-invertible matrix, by c-Caelum in [#66](https://github.com/FallingColors/MoreIotas/pull/66).

### Internal

- Deprecated all uses of the JBLAS matrix library and replaced them with EJML equivalents, by c-Caelum in [#66](https://github.com/FallingColors/MoreIotas/pull/66).

## `0.1.1` - 2025-09-23

### Changed

- Updated zh_cn translations, by ChuijkYahus in [#43](https://github.com/FallingColors/MoreIotas/pull/43).

### Fixed

- Fixed an issue where a required bundled dependency was missing from the Forge jar.

## `0.1.0` - 2025-09-22

### Added

- Added entity/iota/item type iotas (ported from Hexal).
- Added item stack iotas (ported from Hexbound).
- Added Power Distillation and Division Distillation for matrices.
- Added Hadamard multiplication/division/power patterns for matrices ([#8](https://github.com/FallingColors/MoreIotas/issues/8)).
- Added Restoration Purification II, which always converts a matrix to a list of lists of numbers ([#9](https://github.com/FallingColors/MoreIotas/issues/9)).

### Changed

- Updated to Minecraft 1.20.1! 
- Updated zh_cn translations, by ChuijkYahus in [#7](https://github.com/FallingColors/MoreIotas/pull/7) and [#14](https://github.com/FallingColors/MoreIotas/pull/14).
- Moved MoreIotas into the Falling Colors organization.

### Fixed

- Fixed a bug with string iotas accessing configs before they exist.
- Fixed the order of unmaking matrices into lists of vecs.
- Fixed matrix width/height documentation ([#12](https://github.com/FallingColors/MoreIotas/issues/12)).
- Fixed Patternmaster's Purification not recognising Introspection, Retrospection, Consideration, and Evanition ([#10](https://github.com/FallingColors/MoreIotas/issues/10)).
- Fixed Sifter's Gambit not working properly in multiplayer ([#11](https://github.com/FallingColors/MoreIotas/issues/11)).
- Fixed Write not working on lecterns ([#18](https://github.com/FallingColors/MoreIotas/issues/18)).

## Previous versions

See https://modrinth.com/mod/moreiotas/changelog for changelogs from previous versions.
