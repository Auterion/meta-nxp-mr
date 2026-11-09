#!/bin/sh
#
# NavQ95 counterpart of AuterionOS scripts/run_flightstack_updater.sh:
# same directory layout as Skynode, the uploader code is taken from a
# manually installed checkout (UPLOADER_DIR) instead of a package.
#
# Configuration: /etc/default/flightstack-updater

set -eu

UPLOADER_DIR="${UPLOADER_DIR:-/home/user/px4/flightstack-uploader}"
CONFIG_DIR="${CONFIG_DIR:-/shared_container_dir/fmu}"
FIRMWARE_VERSION_FILE="${FIRMWARE_VERSION_FILE:-/persistent/current_fmu_version}"
AUTERION_UPLOADER_LOG_DIR="${AUTERION_UPLOADER_LOG_DIR:-/persistent}"
METADATA_OUTPUT_DIR="${METADATA_OUTPUT_DIR:-/persistent/metadata/fmu}"
EXTRA_ARGS="${EXTRA_ARGS:-}"

# Selects the i.MX95 (MCUboot staging slot) flashing path in update.py
export DEVICE="${DEVICE:-navq95}"

if [ ! -f "${UPLOADER_DIR}/flightstack-uploader.py" ]; then
    echo "flightstack-uploader not found in ${UPLOADER_DIR}, not starting." \
         "Copy it there and run: systemctl restart flightstack-updater"
    exit 0
fi

if ! command -v imx_uploader.py > /dev/null 2>&1; then
    echo "WARNING: imx_uploader.py not in PATH, flashing will fail." \
         "Install it: install -m 755 ${UPLOADER_DIR}/scripts/imx_uploader.py /usr/local/bin/"
fi

mkdir -p "${CONFIG_DIR}" "${AUTERION_UPLOADER_LOG_DIR}" "${METADATA_OUTPUT_DIR}"

# This keeps running and checks for new firmware/config updates in CONFIG_DIR
# shellcheck disable=SC2086
exec python3 "${UPLOADER_DIR}/flightstack-uploader.py" \
    --log-dir "${AUTERION_UPLOADER_LOG_DIR}" \
    --fw-ver-file "${FIRMWARE_VERSION_FILE}" \
    --metadata-output-dir "${METADATA_OUTPUT_DIR}" \
    ${EXTRA_ARGS} \
    "${CONFIG_DIR}" "$@"
