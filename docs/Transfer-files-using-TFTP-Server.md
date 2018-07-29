See the instructions here: http://www.wiki.xilinx.com/x1.0%20Zynq%20UltraScale+%20MPSoC%20boot%20in%20Non%20Secure%20Boot-1.1%20ZCU102%20Hardware%20platform-1.1.5%20Booting%20Linux%20From%20U-Boot%20Using%20TFTP and using the Windows application here http://tftpd32.jounin.net/ .

To set up TFTPd32/d64 correctly, follow instructions here: http://techzain.com/how-to-setup-tftp-server-tftpd64-tfptd32-windows/

Currently can't get the ethernet working on the v3 prototype because of this problem? https://forums.xilinx.com/t5/Embedded-Processor-System-Design/ZCU102-design-with-multiple-Ethernet-ports-U-Boot-error-mdio/td-p/745094 


# References

- [1] https://www.emcraft.com/som/k70/loading-linux-images-via-ethernet-and-tftp