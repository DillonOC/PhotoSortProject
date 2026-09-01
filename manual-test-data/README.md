# PhotoSort Manual Acceptance Dataset

This dataset was generated specifically for the current PhotoSort project. The images are synthetic fixtures, so this folder is suitable for committing to your GitHub repository.

## Run the two sets separately

### `core-input/`
Designed to exercise normal behaviour without deliberate failures. Start with a fresh/empty output directory.

### `edge-input/`
Contains deliberately awkward/broken cases. Run it separately from the core set.

PhotoSort moves files, so keep a clean copy of this ZIP (or restore the dataset from Git) before rerunning tests.

## Core coverage

The core set contains:

- Five JPEGs with valid `EXIF DateTimeOriginal`, spread across different years/months.
- JPEG metadata with no `DateTimeOriginal`.
- A JPEG with no EXIF at all.
- PNG, BMP and TIFF files with no date.
- An exact byte-for-byte duplicate pair.
- A duplicate pair with identical decoded pixels but different metadata:
  - one dated;
  - one metadata-stripped.
- Three exact duplicates placed in different nested source folders.
- Deep/nested source directories.
- A filename containing spaces, `&` and parentheses.
- A `.txt` file which PhotoSort should leave untouched.

## Duplicate traversal order

`Files.walkFileTree()` does not guarantee sibling-file traversal order.

For duplicate groups whose members have equivalent date information, do not assert a particular canonical filename. Instead verify:

1. one file is in the normal `YYYY/MM` folder; and
2. all other copies are in the same `Duplicates - <canonical filename>/` folder.

For the `metadata-different` group, the final canonical file should be `dated-version.jpg` in `2024/07`, even if the metadata-stripped copy happens to be visited first. That exercises the visitor's no-date promotion logic.

## Edge coverage

### `corrupt/broken-photo.jpg`
Text bytes with a `.jpg` extension. On systems where `Files.probeContentType()` treats it as JPEG, `ContentHasher` should reject it and PhotoSort should record a failure rather than moving it.

### `same-name-collision/`
Two different images are both named `same-name.jpg` and both have a February 2024 date, so both plan to:

`output/2024/02/same-name.jpg`

With the current `FileMover`, one can move first and the second should fail because the destination already exists. This is intentionally included to expose the filename-collision design issue.

## Manifest

`manifest.csv` contains:

- expected `DateTimeOriginal`;
- duplicate-group membership;
- expected behaviour;
- binary SHA-256;
- decoded-pixel SHA-256.

Files in a metadata-different duplicate group deliberately have different binary hashes but the same decoded-pixel hash.
