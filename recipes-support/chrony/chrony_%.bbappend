FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://chrony.conf"

do_install_append() {

    install -m 644 ${WORKDIR}/chrony.conf ${D}${sysconfdir}
    
}

