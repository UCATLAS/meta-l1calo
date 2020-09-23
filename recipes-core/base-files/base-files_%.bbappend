#Add alias for emacs since I keep trying to use it
do_install_append () {
	echo "alias emacs='zile'" >> ${D}${sysconfdir}/profile
}