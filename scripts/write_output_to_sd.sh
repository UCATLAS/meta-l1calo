# List the currently connected sd cards
echo -e "Look at the following sd cards and locate the one you wish to format:\n"
sudo fdisk -l /dev/sd* 2>/dev/null | grep -E '^(Disk /dev/sd|/|Device|$)' | awk '
  /^Disk \/dev\/sd[a-z]+[0-9]+:/ { next }  # skip partition Disk lines like /dev/sda1:
  NF { print; blank=0 }                    # print non-blank lines
  !NF && !blank { print; blank=1 }         # print only one blank line between blocks
'

# Ask for input of which sd card to write to
read -p "Please enter the location of your sd card (e.g. '/dev/sdg'): " sd_card_name
#sd_card_name="/dev/sdg"
echo "You inputted: $sd_card_name"

# Write appropriate ".wic" sd image onto the sd card
cd output

if [ -d "gfex-prototype4" ]; then
  cd gfex-prototype4

  sudo dd if=core-image-gfex-gfex-prototype4.wic of="$sd_card_name" status=progress
  cd ..
fi
if [ -d "gfex-production-stf" ]; then
  cd gfex-production-stf

  sudo dd if=core-image-gfex-gfex-production-stf.wic of="$sd_card_name" status=progress
  cd ..
fi
if [ -d "gfex-production-p1" ]; then
  cd gfex-production-p1

  sudo dd if=core-image-gfex-gfex-production-p1.wic of="$sd_card_name" status=progress
  cd ..
fi

cd ..

# Eject the sd card
sudo eject $sd_card_name