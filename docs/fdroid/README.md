# F-Droid

The recipe in this directory is a draft, not a verified F-Droid release path. Its metadata ID and
`Binaries` URL do not currently match this checkout's application ID and workflow release asset
name, so reproducible-build and in-place-update compatibility are unverified.

`io.github.artcraftandroid.filmcraft.yml` is the recipe for
[fdroiddata](https://gitlab.com/fdroid/fdroiddata) (`metadata/io.github.artcraftandroid.filmcraft.yml`).
The store listing (title, descriptions, changelogs, icon, screenshots) is in
`fastlane/metadata/android/en-US/` at the repository root; F-Droid reads it from there.

## What makes the build reproducible

- **One Rust release** (`rust-toolchain.toml`), **one cargo-ndk** (4.1.2) and **one NDK**
  (29.0.14206865) everywhere: CI, F-Droid and your machine.
- **No build paths in the Rust library**: the Gradle task that runs cargo-ndk remaps the checkout
  and the Cargo home (`--remap-path-prefix`), and no crate built for Android keeps its source
  folder's path (`env!("CARGO_MANIFEST_DIR")`), so where it was built leaves no trace.
- **No AGP extras that differ**: no VCS info and no dependency metadata block in the APK
  (F-Droid's checker rejects the latter), and no baseline profile.
- **Nothing is downloaded during the build** but source. The speech models for captions and the
  natural voices are not in the APK at all: the app downloads them only when the user asks (each
  file pinned to a revision and checked against its SHA-256).
- **One APK, for 64-bit ARM.** The recipe requests `shawncut.unsigned=true` and applies version code
  (source version code × 10) + 2; the binary comparison has not been verified.

Releases are built by the release workflow on Linux, as F-Droid's are.

The Actions artifact is `shawncut-apk`, containing `shawncut-<version>.apk`. The GitHub Release
keeps the existing public asset name `shawn-cut-<version>.apk`; the recipe's `Binaries` URL still
expects `filmcraft-android-<version>.apk` and is intentionally unchanged pending owner approval.

## A release

1. Set `shawncut.version` and `shawncut.versionCode` in
   `apps/shawncut-android/android/gradle.properties` (the build checks they agree: the code is
   `((major·100 + minor)·100 + patch)·100 + (pre-release number, or 99)`, e.g. 0.0.3-beta → 301).
2. Add `fastlane/metadata/android/en-US/changelogs/<code>2.txt` (at most 500 characters).
3. Commit, tag `v<version>` and push the tag; the workflow refuses a tag that isn't the version in
   gradle.properties. Publish the draft release it makes.
4. F-Droid's update check finds the tag and adds the build itself (`AutoUpdateMode: Version`).

The release key's certificate SHA-256 (`AllowedAPKSigningKeys`):
`7356d58ae3446b3e6cc457d0b9901700376c787310c9dba14e9c11866588eeb5`.
