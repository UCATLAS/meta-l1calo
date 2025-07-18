SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
cd "$SCRIPT_DIR"

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
sudo dd if=core-image-gfex-gfex-production-stf.rootfs.ext4 of="$sd_card_name"2 status=progress

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

sudo eject $sd_card_name