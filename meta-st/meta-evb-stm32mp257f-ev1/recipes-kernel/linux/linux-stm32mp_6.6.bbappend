FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

LINUX_VERSION = "6.6"
LINUX_SUBVERSION = ".116"

SRC_URI += " \
    file://${LINUX_VERSION}/${LINUX_VERSION}${LINUX_SUBVERSION}/0001-arm64-dts-st-add-default-LED-configuration-for-OBMC.patch \
    file://${LINUX_VERSION}/${LINUX_VERSION}${LINUX_SUBVERSION}/0002-Force-USB-PD-mode-to-peripheral.patch \
    file://${LINUX_VERSION}/${LINUX_VERSION}${LINUX_SUBVERSION}/0003-Alias-added-to-set-mmc0-to-eMMC.patch \
    "
