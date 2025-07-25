mkdir output

if [ -d "build/tmp/deploy/images/gfex-prototype4" ]; then
  mkdir output/gfex-prototype4
  mkdir output/gfex-prototype4/BOOT
  mkdir output/gfex-prototype4/ROOT

  cp build/tmp/deploy/images/gfex-prototype4/boot.bin output/gfex-prototype4/BOOT/boot.bin
  cp build/tmp/deploy/images/gfex-prototype4/boot.scr output/gfex-prototype4/BOOT/boot.scr
  cp build/tmp/deploy/images/gfex-prototype4/Image output/gfex-prototype4/BOOT/Image
  cp build/tmp/deploy/images/gfex-prototype4/system.dtb output/gfex-prototype4/BOOT/system.dtb
  cp build/tmp/deploy/images/gfex-prototype4/u-boot.bin output/gfex-prototype4/BOOT/u-boot.bin

  cp build/tmp/deploy/images/gfex-prototype4/core-image-gfex-gfex-prototype4.rootfs.ext4 output/gfex-prototype4/ROOT/core-image-gfex-gfex-prototype4.rootfs.ext4
  cp build/tmp/deploy/images/gfex-prototype4/core-image-gfex-gfex-prototype4.rootfs.wic output/gfex-prototype4/core-image-gfex-gfex-prototype4.wic
fi

if [ -d "build/tmp/deploy/images/gfex-production-stf" ]; then
  mkdir output/gfex-production-stf
  mkdir output/gfex-production-stf/BOOT
  mkdir output/gfex-production-stf/ROOT

  cp build/tmp/deploy/images/gfex-production-stf/boot.bin output/gfex-production-stf/BOOT/boot.bin
  cp build/tmp/deploy/images/gfex-production-stf/boot.scr output/gfex-production-stf/BOOT/boot.scr
  cp build/tmp/deploy/images/gfex-production-stf/Image output/gfex-production-stf/BOOT/Image
  cp build/tmp/deploy/images/gfex-production-stf/system.dtb output/gfex-production-stf/BOOT/system.dtb
  cp build/tmp/deploy/images/gfex-production-stf/u-boot.bin output/gfex-production-stf/BOOT/u-boot.bin

  cp build/tmp/deploy/images/gfex-production-stf/core-image-gfex-gfex-production-stf.rootfs.ext4 output/gfex-production-stf/ROOT/core-image-gfex-gfex-production-stf.rootfs.ext4
  cp build/tmp/deploy/images/gfex-production-stf/core-image-gfex-gfex-production-stf.rootfs.wic output/gfex-production-stf/core-image-gfex-gfex-production-stf.wic
fi

if [ -d "build/tmp/deploy/images/gfex-production-p1" ]; then
  mkdir output/gfex-production-p1
  mkdir output/gfex-production-p1/BOOT
  mkdir output/gfex-production-p1/ROOT

  cp build/tmp/deploy/images/gfex-production-p1/boot.bin output/gfex-production-p1/BOOT/boot.bin
  cp build/tmp/deploy/images/gfex-production-p1/boot.scr output/gfex-production-p1/BOOT/boot.scr
  cp build/tmp/deploy/images/gfex-production-p1/Image output/gfex-production-p1/BOOT/Image
  cp build/tmp/deploy/images/gfex-production-p1/system.dtb output/gfex-production-p1/BOOT/system.dtb
  cp build/tmp/deploy/images/gfex-production-p1/u-boot.bin output/gfex-production-p1/BOOT/u-boot.bin

  cp build/tmp/deploy/images/gfex-production-p1/core-image-gfex-gfex-production-p1.rootfs.ext4 output/gfex-production-p1/ROOT/core-image-gfex-gfex-production-p1.rootfs.ext4
  cp build/tmp/deploy/images/gfex-production-p1/core-image-gfex-gfex-production-p1.rootfs.wic output/gfex-production-p1/core-image-gfex-gfex-production-p1.wic
fi

# zip -r output.zip output