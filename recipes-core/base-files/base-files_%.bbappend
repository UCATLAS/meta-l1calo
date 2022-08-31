#p Add log folder in rootfs
dirs755_append = " /logs \ 
	       	   /software
		   /software/misc "

# Add alias for emacs since I keep trying to use it
# Create link from python to python3 so that people can use both interchangeably
do_install_append () {
	#install -m 0755 -d ${D}/logs
	install -m 0755 -d ${D}/software
	echo "alias emacs='zile'" >> ${D}${sysconfdir}/profile
	echo "ln -sf /usr/bin/python3 /usr/bin/python" >> ${D}${sysconfdir}/profile
	
}