# ShawnCut — UI/UX Design Specification

**Status:** Approved  
**Scope:** UI/UX specification only. This document does not authorize implementation changes by itself.  
**Product:** ShawnCut Android  
**Design reference:** CapCut Mobile for interaction patterns and timeline usability; preserve ShawnCut's own brand identity.

## 1. Design principles and scope

### Goals
- Make the app clear, compact, and comfortable to use on mobile.
- Use a consistent design system across Home, project management, Settings, and Export.
- Prioritize a practical, CapCut-Mobile-like editing workflow, especially timeline interactions.
- Preserve the existing editing engine, media-processing behavior, and supported capabilities. UI redesign must not imply unsupported engine features.
- Keep changes focused on user-visible UI/UX. Avoid unrelated refactors.

### Non-goals
- Do not replace or rewrite the editing/export engine as part of a UI redesign.
- Do not copy CapCut branding, logos, or proprietary visual identity.
- Do not add project-file sharing.
- Do not add Storage/Data settings in this phase.

### Required implementation approach
1. Inspect the existing implementation and engine contracts before changing behavior.
2. Reuse existing functionality where possible; identify unsupported or ambiguous behavior rather than inventing it.
3. Implement in small, reviewable steps and preserve existing features.
4. Validate the design on real device sizes and orientations, not only previews.
5. Do not treat a fixed delay as proof that an asynchronous save has completed.

## 2. Design tokens and theming

### Theme behavior
- Settings offers **System default**, **Light**, and **Dark**.
- Outside the Editor, Light mode uses an ivory/light-gray background.
- The Editor is **always dark**, regardless of the app-wide theme.
- The ShawnCut accent remains purple. Use the brighter Editor accent where needed for contrast.
- Components use moderately rounded corners.
- Apply theme changes consistently and persist the user's selection using the app's established settings mechanism.

### Approved color tokens

| Token | Light theme | Dark Editor |
|---|---|---|
| Background | `#F7F6FA` | `#121116` |
| Surface | `#FFFFFF` | `#1B1921` |
| Surface secondary / elevated | `#F0EEF5` | `#26232E` |
| Text primary | `#211C2B` | `#F5F2FA` |
| Text secondary | `#625C6D` | `#B8B1C5` |
| Border | `#DED9E7` | `#3B3547` |
| Accent | `#8B5CF6` | `#A78BFA` |
| Danger | `#B42318` | `#F87171` |

These are approved design tokens. Validate text, icon, and component contrast in implementation; adjust only where necessary to meet accessibility requirements, and document any token deviation.

### Typography
Use the compact typography direction approved for mobile and especially for the Editor. Inter is the proposed typeface; first verify that the expected font assets and weights are present and correctly packaged.

| Use | Size | Weight |
|---|---:|---|
| Screen title | 24sp | SemiBold |
| Section title | 18sp | SemiBold |
| Card title | 16sp | Medium |
| Body | 14sp | Regular |
| Secondary text | 12sp | Regular |
| Button | 14sp | Medium |
| Editor tool label | 11–12sp | Medium |

Keep hierarchy clear. Avoid making every label large or visually dominant.

### Spacing, shape, and controls
- Spacing scale: **4, 8, 12, 16, 20, 24, 32dp**.
- Corner-radius scale: **8, 12, 16, 20dp**.
- Common interactive touch targets: at least **48 × 48dp** where practical.
- Use consistent icon sizing, alignment, and label placement.
- Keep motion subtle, generally around **150–220ms**; honor system reduced-motion preferences where feasible.

## 3. Navigation and shared behavior

- Primary bottom navigation contains exactly three destinations: **Home**, **Edit**, and **Settings**.
- Hide the bottom navigation inside the Editor to maximize editing space.
- Tapping **Edit** returns to the currently open Editor session if one exists; otherwise, it opens the video picker.
- Maintain predictable back behavior between screens and panels.
- Confirm before leaving the Editor every time a leave action is initiated. Before leaving, ensure changes have actually been saved. Wait for the real save result; if saving fails, clearly explain the failure and let the user retry or choose how to proceed. Never silently discard edits.
- Autosave status must reflect the actual save lifecycle, not merely a scheduled or attempted save.

## 4. Home and project management

### Home layout
- Show recent projects near the top and make **Create project** prominent.
- Use compact project cards or a compact list so many projects remain discoverable without excessive scrolling.
- Sort by **last edited**, not last opened. Verify the existing project data model and add/update a last-edited timestamp only if required.
- A search icon opens a search field; do not keep the field permanently expanded.
- Each project exposes a three-dot overflow menu.

### Empty state and create flow
- When there are no projects, show brief guidance and a clear action to open the video picker.
- If the user cancels the picker, do not create an empty project.
- Avoid implying a project has been created until the create flow succeeds.

### Project actions
- Menu actions: **Rename**, **Duplicate**, **Delete**.
- Offer **Share exported video** only when a valid exported output exists.
- Never offer sharing of the project file in this phase.
- Confirm destructive actions. The delete confirmation must include the project name.
- Keep success and failure feedback clear and accurate.

## 5. Editor UX

### Overall structure
- Redesign the Editor UI deeply while preserving the current editing engine and existing supported features.
- Keep the preview prominent above the timeline. Adapt the layout to screen size and orientation, balancing preview visibility with timeline usability.
- Use a dark Editor surface regardless of the app-wide theme.
- Keep the main editing controls compact and recognizable.

### Main tool groups
The main toolbar presents icons with text labels for these groups:
- Editing
- Audio
- Text
- Overlay
- Effects
- Filters
- Speed

Only expose actions that the current engine and selected media can actually support. Do not invent tool capabilities to fill the layout.

### Timeline interaction — high priority
Aim to match the usability and interaction principles of CapCut Mobile as closely as practical, without copying its branding or assuming unsupported capabilities.

Preserve and improve, where supported by the current engine:
- Clear tracks/lanes and clip boundaries.
- Video thumbnails and audio waveform/indicators where data is available.
- A visible playhead and time ruler.
- Clip selection and clear selected-state styling.
- Trim handles and direct manipulation for supported trim operations.
- Predictable drag, seek, zoom, and horizontal scroll gestures.
- Context-sensitive actions based on selected clip type and current state.
- Clear separation between selecting a clip and manipulating it.
- Undo/redo affordances only where the existing implementation supports them.

Do not implement a gesture that conflicts with an existing gesture without a deliberate, tested interaction rule. Validate touch targets, gesture arbitration, and timeline behavior on device.

### Tool panels and selection
- Selecting a tool group replaces the main tool row with that group's subtool panel.
- Provide an obvious way back to the main toolbar.
- Prioritize core editing operations on small screens.
- Select a clip first; then show a contextual action bar containing only actions relevant to that clip type and state.
- Keep panel navigation predictable and avoid trapping users in a subtool panel.

### Autosave and leaving the Editor
- Show truthful save states such as saving, saved, or save failed.
- The current debounce scheduling must not be treated as save completion.
- Expose the actual completion/failure of persistence so the UI can make safe navigation decisions.
- When leaving the Editor, wait for a real save completion or present a clear failure/retry path. Do not rely on a fixed sleep or silently lose changes.
- Verify session behavior across navigation and Activity recreation. Process-death restoration must not be claimed unless it is tested.

## 6. Export UX

### Export settings
- Use a dedicated Export settings screen.
- Group presets by intended use/goal so users can choose without understanding every technical parameter.
- Put advanced settings in a separate section, collapsed by default.
- Keep resolution, frame rate, aspect ratio, and quality choices limited to combinations actually supported by the app's engine and target-device encoder.

### Capability verification requirement
Before finalizing preset names or available combinations, inspect the export engine contract and test actual device encoder limits. Existing UI options alone are not evidence that every combination is supported. Do not promise a resolution, FPS, aspect ratio, or quality setting that cannot be reliably exported.

### Export result
After a successful export, show a result screen with:
- **View**
- **Share**
- **Return to editor**

Only enable actions that are valid for the actual export result. On failure, show a useful error and a clear retry/recovery path.

## 7. Settings and localization

### Settings scope
Keep Settings simple and grouped:
1. **Theme** — System default, Light, Dark.
2. **App Language** — language selection.
3. **About** — app version and copyright.

Do not add Storage/Data settings in this phase.

### Localization architecture
- Store each language in its own Android string-resource directory/file, for example:
  - `res/values/strings.xml` for the default language.
  - `res/values-vi/strings.xml` for Vietnamese.
  - `res/values-ja/strings.xml` for a future Japanese translation.
- New languages should be addable by adding the appropriate resource file(s) to the repository and releasing an app update, without redesigning the localization architecture.
- Apply language changes immediately using a suitable Android locale mechanism.
- Ensure every supported language has complete strings for the shipped UI; avoid mixed-language screens and hardcoded user-facing strings.
- Verify locale behavior across navigation, Activity recreation, and app relaunch.

## 8. Accessibility and motion

- Maintain practical baseline accessibility across Home, Editor, Settings, dialogs, and Export.
- Use touch targets of at least 48 × 48dp for common interactive controls where practical.
- Validate foreground/background contrast, including the brighter accent used in the dark Editor.
- Provide meaningful screen-reader labels for icon-only controls.
- Preserve logical focus order and visible focus states where applicable.
- Ensure larger system font settings do not hide critical actions or make core flows unusable.
- Reduce or remove nonessential motion when the system requests reduced motion, where feasible.
- Use animation to clarify state changes, not as decoration that slows editing.

## 9. Technical constraints and items to verify

These are implementation checks, not permission to change unrelated code.

1. **Last-edited timestamp:** inspect the project model and persistence flow. Current observations indicate the Home sort may use recently opened time; verify and add a distinct last-edited value only if needed.
2. **Save lifecycle:** current observations indicate save is debounced (approximately 400ms) and errors may be caught without a clear UI result. Confirm actual implementation and expose a reliable success/failure state.
3. **Editor session lifecycle:** verify returning to an existing session, navigation, Activity recreation, and process death separately. Do not claim restoration guarantees without evidence.
4. **Export support:** verify the engine's supported output parameters and real device encoder combinations before offering presets.
5. **Theme and locale persistence:** verify the selected theme and language survive the intended lifecycle events.
6. **Font resources:** verify the referenced Inter resources and weights are present and packaged correctly before making any build-health claim.
7. **Token consistency:** audit relevant UI components for hardcoded colors, spacing, text sizes, and corner radii; migrate only the UI code in scope and avoid unrelated cleanup.
8. **Timeline regression:** test selection, trim, drag, seek, zoom, scroll, multi-lane behavior, and undo/redo where applicable. Check performance on a real device.

## 10. Acceptance criteria

### A. Design system and themes
- Approved tokens are applied consistently to the redesigned surfaces.
- System, Light, and Dark selections work; the Editor remains dark in all three cases.
- Contrast and touch-target checks pass for common actions.

### B. Navigation
- Bottom navigation shows only Home, Edit, and Settings outside the Editor.
- Bottom navigation is hidden in the Editor.
- Edit returns to an existing Editor session when available, otherwise opens the video picker.
- Back/navigation behavior is consistent and does not silently discard changes.

### C. Home and projects
- Recent projects are visible near the top and Create project is prominent.
- Project presentation is compact and scales to many projects.
- Search is opened from the search icon.
- Projects sort by last edited.
- Cancelling the video picker does not create an empty project.
- Rename, Duplicate, Delete, and conditional exported-video sharing behave correctly.
- Delete confirmation includes the project name.

### D. Editor and timeline
- Preview and timeline adapt to screen size/orientation.
- Main tool groups have icons and labels; subtool panels can return to the main toolbar.
- Contextual actions match the selected clip type/state.
- Supported timeline gestures work without unintended conflicts.
- Save status reflects real persistence results.
- Leaving the Editor never silently loses unsaved work.

### E. Export
- Presets are grouped by goal; advanced settings are collapsed by default.
- Only verified engine/device-supported combinations are offered.
- Successful export shows View, Share, and Return to editor.
- Failure paths are understandable and recoverable.

### F. Localization
- Theme and language changes apply immediately and persist as intended.
- Strings are resource-based and complete for supported languages.
- Adding a language resource file and shipping an app update is sufficient for the planned language-expansion workflow.

### G. Accessibility and motion
- Common controls meet the 48 × 48dp target where practical.
- Icon-only controls have screen-reader labels.
- Contrast, focus, larger font, and motion behavior are checked.

### H. Regression and implementation discipline
- Existing editing/export capabilities remain intact.
- Core timeline interactions are tested on a real device.
- No unsupported capability is presented as available.
- Changes stay within the approved UI/UX scope; no unrelated refactoring.
- Build and relevant tests pass before the UI/UX redesign is considered complete.

## 11. Implementation order

1. Verify the technical constraints listed in Section 9 and record any engine limitation before designing controls around it.
2. Implement the shared design tokens, theme behavior, and navigation shell.
3. Implement Home and project-management UX.
4. Implement Editor layout and timeline interaction improvements while preserving engine behavior.
5. Implement the Export flow using verified supported combinations.
6. Complete Settings, theme, and localization behavior.
7. Run accessibility checks, regression tests, and real-device validation against Section 10.

**Approval note:** This specification is approved as the source of truth for the UI/UX phase. Any material deviation from these decisions should be raised for review before implementation.