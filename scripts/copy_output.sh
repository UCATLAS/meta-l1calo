SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
cd "$SCRIPT_DIR"
## echo "Current working directory: $(pwd)"

mkdir output
mkdir output/BOOT

cp build/tmp/deploy/images/gfex-production-stf/boot.bin output/BOOT/boot.bin
cp build/tmp/deploy/images/gfex-production-stf/boot.scr output/BOOT/boot.scr
cp build/tmp/deploy/images/gfex-production-stf/Image output/BOOT/Image
cp build/tmp/deploy/images/gfex-production-stf/system.dtb output/BOOT/system.dtb
cp build/tmp/deploy/images/gfex-production-stf/u-boot.bin output/BOOT/u-boot.bin

cp build/tmp/deploy/images/gfex-production-stf/core-image-gfex-gfex-production-stf.rootfs.ext4 output/core-image-gfex-gfex-production-stf.rootfs.ext4

# zip -r output.zip output