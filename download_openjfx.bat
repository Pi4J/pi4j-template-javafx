@echo off

REM Variables
URL="https://download2.gluonhq.com/openjfx/23.0.1/openjfx-23.0.1_linux-aarch64_bin-sdk.zip"
SHA_URL="https://download2.gluonhq.com/openjfx/23.0.1/openjfx-23.0.1_linux-aarch64_bin-sdk.zip.sha256"

set TARGET_DIR="target\openjfx"
set SUBDIR="extracted_files"
set TEMP_DIR="%TEMP%\openjfx_temp"

REM Function to calculate SHA256 sum
:calculate_sha256
certutil -hashfile "%1" SHA256 | findstr /R "[0-9a-fA-F]*"

REM Create target directory
rmdir /s /q "%TARGET_DIR%\%SUBDIR%"
mkdir "%TARGET_DIR%\%SUBDIR%" 2>nul

REM Download the file if not already downloaded
if not exist "%TARGET_DIR%\%~n0" (
    echo Downloading %URL%...
    bitsadmin.exe /transfer myDownloadJob /download /priority normal %URL% "%TARGET_DIR%\%~n0"
) else (
    echo File already exists. Skipping download.
)

REM Download the SHA256 file if not already downloaded
if not exist "%TARGET_DIR%\%~n1" (
    echo Downloading SHA256 checksum file...
    bitsadmin.exe /transfer mySHA256Job /download /priority normal %SHA_URL% "%TARGET_DIR%\%~n1"
)

REM Verify the SHA256 sum
echo Verifying SHA256 sum...
for /f "tokens=*" %%a in ('type "%TARGET_DIR%\%~n1"') do set "sha256sum=%%a"
for /f "tokens=*" %%b in ('call :calculate_sha256 "%TARGET_DIR%\%~n0"') do (
    if "%%b"=="%sha256sum%" (
        echo SHA256 sum verified successfully.
    ) else (
        echo SHA256 sum verification failed.
        rmdir /s /q "%TEMP_DIR%"
        exit /b 1
    )
)

REM Extract the zip file
echo Extracting the zip file...
powershell -noprofile -command "& {Add-Type -A 'System.IO.Compression.FileSystem'; [System.IO.Compression.ZipFile]::ExtractToDirectory('%TARGET_DIR%\%~n0', '%TARGET_DIR%\%SUBDIR%')}"

rem Find the folder and rename it
for /f "delims=" %%D in ('dir /b /ad "%TARGET_DIR%\%SUBDIR%"') do (
    set "folder=%%~fD"
    goto :rename
)
:rename
if defined folder (
    ren "%folder%" new_name
    echo Folder renamed successfully.
) else (
    echo Folder not found.
)

echo Extraction completed. Files are located in: %TARGET_DIR%\%SUBDIR%
