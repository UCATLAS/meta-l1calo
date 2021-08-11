# Add alias for emacs since I keep trying to use it
# Create link from python to python3 so that people can use both interchangeably
do_install_append () {
	echo "alias emacs='zile'" >> ${D}${sysconfdir}/profile
	echo "ln -sf /usr/bin/python3 /usr/bin/python" >> ${D}${sysconfdir}/profile
}