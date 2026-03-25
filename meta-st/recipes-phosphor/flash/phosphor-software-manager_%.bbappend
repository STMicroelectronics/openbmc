FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://0001-Do-not-update-U-Boot-SPL.patch \
    file://0002-Use-after-free-temporary-hugly-workaround.patch \
    "
