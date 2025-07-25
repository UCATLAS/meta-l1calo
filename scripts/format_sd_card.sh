echo -e "Look at the following sd cards and locate the one you wish to format:\n"
sudo fdisk -l /dev/sd* 2>/dev/null | grep -E '^(Disk /dev/sd|/|Device|$)' | awk '
  /^Disk \/dev\/sd[a-z]+[0-9]+:/ { next }  # skip partition Disk lines like /dev/sda1:
  NF { print; blank=0 }                    # print non-blank lines
  !NF && !blank { print; blank=1 }         # print only one blank line between blocks
'
read -p "Please enter the location of your sd card (e.g. '/dev/sdg'): " sd_card_name
#sd_card_name="/dev/sdg"
echo "You inputted: $sd_card_name"

#sudo dd if=/dev/zero of=$sd_card_name bs=1M count=2000 status=progress

sudo fdisk "$sd_card_name" <<EOF
d
 
d
 
n
p
 
 
+1G
a
n
p
 
 
 
t
1
c
t
2
83
p
w   
EOF

sudo mkfs.vfat -F 32 -n BOOT "$sd_card_name"1
sudo mkfs.ext4 -L ROOT "$sd_card_name"2

echo "Partitioning of $sd_card_name completed."