# OpenBMC BSP layer for STMicroelectronics systems

This layer provides the necessary BSP support for **STM32MP2 SoCs**.
It also brings the necessary support for 2 evaluation kits:
* STM32MP257F-DK : called here *evb-stm32mp257f-dk*
* STM32MP257F-EV1 : called here *evb-stm32mp257f-ev1*

## Flash memories

The STM32MP257F-DK board can be used with eMMC flash memory.
The STM32MP257F-EV1 board can be used with either eMMC or SPI NOR flash memory.

On STM32MP257F-EV1 board, to switch from eMMC to SPI NOR flash, you need to comment the portion of configuration in both **evb-stm32mp257f-ev1.conf** located under *meta-st* and *meta-evb* folders.

eMMC uses WIC to describe the flashlayout while SPI NOR uses a static MTD flashlayout, using Squashfs as rootfs and JFFS2 as userfs as described here : https://github.com/openbmc/docs/blob/master/architecture/code-update/flash-layout.md
The memory mapping for this MTD flashlayout is described in the board configuration as well.

## Yocto build process

Example for STM32MP257F-EV1 board (from the OpenBMC root folder):
```
. setup evb-stm32mp257f-ev1
bitbake obmc-phosphor-image
```

## Image deployement on the board

The implementation of *meta-st* we do here is made to re-use programmer tools provided by STMicroelectronics. To get the necessary tools, you can follow these guidelines: https://wiki.st.com/stm32mpu/wiki/STM32MP25_Discovery_kits_-_Starter_Package#Installing_the_STM32CubeProgrammer_tool

Once the Yocto build is finished, you can move into *openbmc/build/<name_machine>/tmp/deploy/images/<name_machine>* folder.

Here you can create a folder called *flashlayout* :
```
mkdir flashlayout && cd flahlayout
```
Then you can now create the eMMC/NOR flashlayout you need in your context.
Find below 2 examples for *evb-stm32mp257f-ev1* board, one for eMMC and the other one for SPI NOR flash :

* Flashlayout_emmc.tsv
```
#Opt	Id	Name	Type	IP	Offset	Binary
-	0x01	fsbl-boot	Binary	none	0x0	arm-trusted-firmware/tf-a-stm32mp257f-ev1-optee-programmer-usb.stm32
-	0x02	fip-ddr	FIP	none	0x0	fip/fip-stm32mp257f-ev1-ddr-optee-programmer-usb.bin
-	0x03	fip-boot	FIP	none	0x0	fip/fip-stm32mp257f-ev1-optee-programmer-usb.bin
P	0x04	fsbla1	Binary	mmc1	boot1	arm-trusted-firmware/tf-a-stm32mp257f-ev1-optee-emmc.stm32
P	0x05	fsbla2	Binary	mmc1	boot2	arm-trusted-firmware/tf-a-stm32mp257f-ev1-optee-emmc.stm32
P	0x06	raw	RawImage	mmc1	0x00080200	obmc-phosphor-image-evb-stm32mp257f-ev1.wic
```

* Flashlayout_nor.tsv
```
#Opt	Id	Name	Type	IP	Offset	Binary
-	0x01	fsbl-boot	Binary	none	0x0	arm-trusted-firmware/tf-a-stm32mp257f-ev1-optee-programmer-usb.stm32
-	0x02	fip-ddr	FIP	none	0x0	fip/fip-stm32mp257f-ev1-ddr-optee-programmer-usb.bin
-	0x03	fip-boot	FIP	none	0x0	fip/fip-stm32mp257f-ev1-optee-programmer-usb.bin
P	0x06	raw	RawImage	nor1	0x00000000	obmc-phosphor-image-evb-stm32mp257f-ev1.static.mtd
```

Once the right TSV file created, move back in the deploy folder :
```
cd ..
```

You can now use STM32 CubeProgrammer to flash the board :
```
STM32_Programmer_CLI -c port=usb1 -w flashlayout/Flashlayout_emmc.tsv
```

More details at the following links :
* https://wiki.st.com/stm32mpu/wiki/STM32MP25_Discovery_kits_-_Starter_Package#Image_flashing
* https://wiki.st.com/stm32mpu/wiki/STM32CubeProgrammer_flashlayout