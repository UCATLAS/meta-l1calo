# Check for the e2fsprogs program in homebrew
if ! brew list --formula | grep -q '^e2fsprogs$'; then
  echo "Error: 'e2fsprogs' is not installed via Homebrew."
  echo "Please install it using: brew install e2fsprogs"
else

# List the currently connected sd cards
echo -e "Look at the following sd cards and locate the one you wish to format:\n"
diskutil list

# Ask for input of which sd card to format
echo "Please enter the location of your sd card (e.g. '/dev/disk4'): "
read sd_card_name
echo "You inputted: $sd_card_name"

# Use fdisk command to format the sd card with 2 partitions, a small 1GB FAT32 boot partition
# and a Linux partition in the rest of the sd card
diskutil unmountdisk $sd_card_name
sudo fdisk "$sd_card_name" <<EOF
erase
edit 1
0C

2048
2097152
flag 1
edit 2
83



p
w
q  
EOF

# Add the appropriate filesystems to each partition
diskutil eraseVolume FAT32 "BOOT" bootable "$sd_card_name"s1
sudo /opt/homebrew/opt/e2fsprogs/sbin/mkfs.ext4 -L ROOT "$sd_card_name"s2

echo "Partitioning of $sd_card_name completed."

fi