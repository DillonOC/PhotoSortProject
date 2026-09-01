# Core acceptance checklist

After sorting `core-input/` into a fresh output folder:

- `2022/09/September walk.jpg` exists.
- `2023/01/January frost.jpg` exists.
- `2024/04/April garden.jpg` exists.
- `2025/12/December lights.jpg` exists.
- `2026/08/August picnic.jpg` exists.
- `2023/10/Family & friends (1).jpg` exists.
- `metadata-but-no-DateTimeOriginal.jpg` is under `No_date/`.
- `no-exif-at-all.jpg` is under `No_date/`.
- `plain-image.png` is under `No_date/`.
- `plain-image.bmp` is under `No_date/`.
- `plain-image.tiff` is under `No_date/`.
- `notes.txt` remains in the input tree.
- The exact-byte duplicate pair becomes one canonical file plus one duplicate.
- The metadata-different pair ends with `dated-version.jpg` as the canonical file in `2024/07/`.
- The metadata-stripped copy is under `2024/07/Duplicates - dated-version.jpg/`.
- The triple group becomes one canonical file plus two files in one duplicate folder.
- Successfully sorted photos no longer exist at their original input paths.

Run `edge-input/` separately; failures there are intentional.
