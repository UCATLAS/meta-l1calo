# Useful Linux Commands

As someone who has not used Linux before, I thought it would be helpful to share the commands that were useful to know for this project:

### General File Management

| Command Name | Usage | Notes |
|:--|:--|:--|
| `cat` | `cat file1.txt file2.txt` | Short for **concatenate**. Outputs the combined contents of file1.txt and file2.txt to the terminal. Use the redirecting `>` to write this to a new file (i.e. `cat file1.txt file2.txt > output.txt`) or to append to the end of an existing file (i.e. `cat file1.txt file2.txt >> output.txt`). You can also just use `cat file1.txt` to read what's in a file on the command line.|
| `cp` | `cp file1.txt file1_copy.txt` | Creates a copy of a file. Use the `-r` for recursively copying a folder. |
| `mv` | `mv file1.txt folder/file1.txt` | Moves a file to a different location.|


### USB Serial Connections and Remote SSH Connections

| Command Name | Usage | Notes |
|:--|:--|:--|
| `minicom` | `minicom -D /dev/ttyUSB[X] -b 115200` | Connect to a device over a USB Serial connection with a baud rate of 115,200. Replace the `[X]` in the device name with the appropriate number. To exit the serial connection, hit Ctrl+A, then press X, and then Enter on the "Yes" option of the prompt. |
| `ssh` | `ssh user@remote` | Connects to a device on the network over SSH. When you are done with that session, use the `logout` command. |
| `scp` | `scp user@remote:~/path/to/file.txt ~/local/path/to/file.txt` | Copies a file from a remote connection to the local computer over the network using SSH. Can also be used for copying files the other way. Use the `-r` for recursively copying a folder and the `-O` option to switch to the legacy protocol if the file can't send. |

### Manipulating SD Cards, Partitions, and Filesystems

| Command Name | Usage | Notes |
|:--|:--|:--|
| `fdisk` | `sudo fdisk /dev/sd[X]` | Used for formatting partitions on a disk (in our case, an SD card). Use `sudo fdisk -l` to list the disks currently connected to your machine. |
| `mkfs` | `sudo mkfs.vfat -F 32 -n boot /dev/sd[X]1` `sudo mkfs.ext4 -L root /dev/sd[X]2`| Used for creating FAT and EXT4 filesystems. |
| `dd` | `sudo dd if=input-file of=/dev/sd[X][#] status=progress` | Raw data copying. Used for writing writing OS images to USB drives and SD Cards. |

### Bitbake Commands

| Command Name | Notes |
|:--|:--|:--|
| `bitbake <TARGET>` | Used to begin the build process for a given target. |
| `bitbake-layers show-layers` | Displays all of the yocto layers used in the project. |
| `bitbake -c menuconfig virtual/kernel` | Shows a graphical menu for kernel options.  |

### Previous article: [Structure of the Source Code](3-Source-Code-Structure.md)
### Home: [Documentation Overview](README.md)
### Next article: [Physical Hardware Setup with an ATCA shelf](5-Physical-Hardware-Setup.md)