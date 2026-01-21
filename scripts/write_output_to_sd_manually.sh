# List the currently connected sd cards
echo -e "Look at the following sd cards and locate the one you wish to format:\n"
sudo fdisk -l /dev/sd* 2>/dev/null | grep -E '^(Disk /dev/sd|/|Device|$)' | awk '
  /^Disk \/dev\/sd[a-z]+[0-9]+:/ { next }  # skip partition Disk lines like /dev/sda1:
  NF { print; blank=0 }                    # print non-blank lines
  !NF && !blank { print; blank=1 }         # print only one blank line between blocks
'
read -p "Please enter the location of your sd card (e.g. '/dev/sdg'): " sd_card_name
#sd_card_name="/dev/sdg"
echo "You inputted: $sd_card_name"

cd output

# Copy the Linux filesystem ".ext4" image
if [ -d "gfex-prototype4" ]; then
  cd gfex-prototype4

  sudo dd if=ROOT/core-image-gfex-gfex-prototype4.rootfs.ext4 of="$sd_card_name"2 status=progress
fi
if [ -d "gfex-production-stf" ]; then
  cd gfex-production-stf

  sudo dd if=ROOT/core-image-gfex-gfex-production-stf.rootfs.ext4 of="$sd_card_name"2 status=progress
fi
if [ -d "gfex-production-p1" ]; then
  cd gfex-production-p1

  sudo dd if=ROOT/core-image-gfex-gfex-production-p1.rootfs.ext4 of="$sd_card_name"2 status=progress
fi

# Copy the boot partition files
if [ -d "BOOT" ]; then
  cd BOOT
  sudo mkdir -p /media/BOOT
  sudo mount "$sd_card_name"1 /media/BOOT
  sudo cp boot.bin /media/BOOT
  sudo cp system.dtb /media/BOOT
  sudo cp Image /media/BOOT
  sudo cp boot.scr /media/BOOT
  sudo cp u-boot.bin /media/BOOT
  sudo umount /media/BOOT

  cd ../..
fi

# Eject the sd card
sudo eject $sd_card_name