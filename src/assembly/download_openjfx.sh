#!/bin/bash

# Variables
URL="https://download2.gluonhq.com/openjfx/24.0.2/openjfx-24.0.2_linux-aarch64_bin-sdk.zip"
SHA_URL="https://download2.gluonhq.com/openjfx/24.0.2/openjfx-24.0.2_linux-aarch64_bin-sdk.zip.sha256"
TARGET_DIR="$HOME/openjfx"
SUBDIR="extracted_files"
TEMP_DIR=$(mktemp -d)

# Function to calculate SHA256 sum
calculate_sha256() {
    sha256sum "$1" | awk '{print $1}'
}

# Create target directory if it doesn't exist
rm -rf "${TARGET_DIR:?}/$SUBDIR"
mkdir -p "$TARGET_DIR/$SUBDIR"

# Download the file if not already downloaded
if [ ! -f "$TARGET_DIR/$(basename "$URL")" ]; then
    echo "Downloading $URL..."
    wget -q --show-progress -O "$TARGET_DIR/$(basename "$URL")" "$URL"
else
    echo "File already exists. Skipping download."
fi

# Download the SHA256 file if not already downloaded
if [ ! -f "$TARGET_DIR/$(basename "$SHA_URL")" ]; then
    echo "Downloading SHA256 checksum file..."
    wget -q -O "$TARGET_DIR/$(basename "$SHA_URL")" "$SHA_URL"
fi

# Verify the SHA256 sum
echo "Verifying SHA256 sum..."
if [ "$(calculate_sha256 "$TARGET_DIR/$(basename "$URL")")" == "$(cut -d ' ' -f1 "$TARGET_DIR/$(basename "$SHA_URL")")" ]; then
    echo "SHA256 sum verified successfully."
else
    echo "SHA256 sum verification failed."
    rm -rf "$TEMP_DIR"
    exit 1
fi

# Extract the zip file
echo "Extracting the zip file..."
unzip -q "$TARGET_DIR/$(basename "$URL")" -d "$TARGET_DIR/$SUBDIR"

# Find the folder and rename it
folder=$(find "$TARGET_DIR/$SUBDIR" -maxdepth 1 -mindepth 1 -type d -exec echo {} \;)
if [ -n "$folder" ]; then
    mv "$folder" "$TARGET_DIR/$SUBDIR/openjfx"
fi

echo "Extraction completed. Files are located in: $TARGET_DIR/$SUBDIR"
