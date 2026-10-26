# Branding Inventory (Pre-edit)

Audit baseline: `main` at `344a2f37b28ec606ace0fb2cb2183b4aafc1ec8e`.
The existing Android rename is present as working-tree changes: old `apps/filmcraft-android/` paths are deleted and `apps/shawncut-android/` is untracked. No staged changes were found. This inventory records the pre-edit branding review; no new source edits were made during the audit before it was saved.

## RENAME

| Location | Finding | Reason |
|---|---|---|
| `fastlane/metadata/android/en-US/title.txt:1` | Store title is `FilmCraft`. | Android product display name; change to `ShawnCut`. |
| `fastlane/metadata/android/en-US/full_description.txt:3,25,27` | Three references name the Android product `FilmCraft`; line 3 separately credits the FilmCraft engine. | Rename only the product-name references and retain the engine credit. |
| `docs/fdroid/io.github.artcraftandroid.filmcraft.yml:9` | `AutoName: FilmCraft`. | F-Droid display name; change to `ShawnCut` while preserving listing identity. |
| `NOTICE:1` | Heading says `FilmCraft for Android`. | Product heading only; update to `ShawnCut for Android`, leaving legal/attribution text below unchanged. |
| `NOTICE:31` | Third-party material section points to `apps/filmcraft-android`. | Stale internal source path; update it to the renamed Android tree without changing the listed licenses. |

## KEEP

| Location/group | Finding | Reason |
|---|---|---|
| `README.md:8`; Android `values/strings.xml`, `values-vi/strings.xml`, `SettingsScreens.kt`, `Theme.kt`, `app/build.gradle.kts` | FilmCraft engine, contributor, font and icon credits/provenance. | Attribution and upstream/license disclosures; retain verbatim. |
| `NOTICE:3-` and `LICENSE-MIT`; `ATTRIBUTION.md` and asset `.attribution` files | FilmCraft contributors, original source/project, copyright and asset authorship. | Legal, copyright, license or provenance material; do not rewrite as ShawnCut authorship. |
| `README.md` screenshot alt text; `docs/images/filmcraft-*`; historical Fastlane changelogs | Descriptions or names for existing FilmCraft screenshots/releases. | Historical content; retain unless screenshots/release history are replaced. |
| Shared `filmcraft-*` Cargo crates, Rust module/API identifiers, desktop/web paths and icons | Existing technical or separate-component identifiers. | Not Android product display names; no rename without dependency/compatibility evidence. |

## REVIEW_REQUIRED

| Location | Finding | Required decision |
|---|---|---|
| `docs/fdroid/io.github.artcraftandroid.filmcraft.yml:1-14,40-44` | Recipe/listing ID, repository owner, source/issue/changelog/repository URLs, and `Binaries` URL ending in `filmcraft-android-%v.apk`. | Preserve until the owner supplies official destination and confirms migration/compatibility. |
| `README.md:9`; `NOTICE:5-6`; `ATTRIBUTION.md` | Upstream maintainer/repository references and original authorship. | Keep current credits; any change requires owner confirmation and must not alter authorship. |

The repository-wide scan also finds legacy tokens in shared codec tests/APIs, asset filenames, desktop/web packaging, and generated/build material. Those are not treated as Android product branding and are intentionally outside this rename unless a concrete consumer requires a change.

## Post-edit follow-up

The full-repository residual scan found the stale `NOTICE:31` source path after the initial inventory was saved. It was classified as an internal path (not legal attribution) and updated; copyright, source credits, and license text remain unchanged.
