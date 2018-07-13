SUMMARY = "%{name}"
LICENSE = "LGPLv3"
LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=e94f6920e0f51ea34f43be88dc810edc"
SRC_URI = "git://github.com/kratsg/quasar_test_project.git;protocol=https"
PV = "1.0+git${SRCPV}"
SRCREV = "405e81271ef98fb3a441e78877dcaea47812742c"

S = "${WORKDIR}/git"

inherit cmake pythonnative

DEPENDS += "boost python-lxml-native xsd-native xerces-c"

# install it correctly, manually
do_install() {
  # install configuration files to /etc/quasar/*
  install -d ${D}${sysconfdir}/quasar/
  install -m 0755 ${B}/Configuration/Configuration.xsd ${D}${sysconfdir}/quasar/
  install -m 0755 ${S}/bin/config.xml ${D}${sysconfdir}/quasar/
  # install binary to /usr/bin
  install -d ${D}${bindir}/
  install -m 0755 ${B}/bin/OpcUaServer ${D}${bindir}/
}
