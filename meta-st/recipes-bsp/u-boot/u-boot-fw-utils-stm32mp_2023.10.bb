require u-boot-stm32mp-common_${PV}.inc
require u-boot-stm32mp.inc

SUMMARY = "U-Boot bootloader fw_printenv/setenv utilities"
DEPENDS += "mtd-utils bison-native"

PROVIDES = "u-boot-fw-utils"
RPROVIDES:${PN} = "u-boot-fw-utils"

SRC_URI += "file://fw_env_nor.config "
SRC_URI += "file://fw_env_mmc.config "
INSANE_SKIP:${PN} = "already-stripped"

ENV_CONFIG_FILE = "fw_env_nor.config"
ENV_CONFIG_FILE:df-phosphor-mmc = "fw_env_mmc.config"

COMPATIBLE_MACHINE = "(stm32mpcommon)"
PACKAGE_ARCH:stm32mpcommon = "${MACHINE_ARCH}"
INSANE_SKIP:${PN} = "already-stripped"
EXTRA_OEMAKE:class-target = 'CROSS_COMPILE=${TARGET_PREFIX} CC="${CC} ${CFLAGS} ${LDFLAGS}" HOSTCC="${BUILD_CC} ${BUILD_CFLAGS} ${BUILD_LDFLAGS}" V=1'

DEPENDS += "u-boot-stm32mp"
RDEPENDS:{PN} += "u-boot-stm32mp"

EXTRA_OEMAKE:class-cross = 'HOSTCC="${CC} ${CFLAGS} ${LDFLAGS}" V=1'

inherit uboot-config

do_compile:append() {
	for config in ${UBOOT_MACHINE}; do
		oe_runmake -C ${S} O=${B}/${config} envtools
	done
}

do_install() {
	for config in ${UBOOT_MACHINE}; do
		install -d ${D}${base_sbindir}
		install -m 755 ${B}/${config}/tools/env/fw_printenv ${D}${base_sbindir}/fw_printenv
		install -m 755 ${B}/${config}/tools/env/fw_printenv ${D}${base_sbindir}/fw_setenv

		install -d ${D}${sysconfdir}
		install -m 644 ${UNPACKDIR}/${ENV_CONFIG_FILE} ${D}${sysconfdir}/fw_env.config
	done
}

do_install:class-cross() {
	for config in ${UBOOT_MACHINE}; do
		install -d ${D}${bindir_cross}
		install -m 755 ${B}/${config}/tools/env/fw_printenv ${D}${bindir_cross}/fw_printenv
		install -m 755 ${B}/${config}/tools/env/fw_printenv ${D}${bindir_cross}/fw_setenv
	done
}

do_deploy() {
	#NA
}

BBCLASSEXTEND = "cross"

FILES:${PN} = "	${base_sbindir} \
				${base_sbindir}/fw_printenv \
				${base_sbindir}/fw_setenv \
		 "
