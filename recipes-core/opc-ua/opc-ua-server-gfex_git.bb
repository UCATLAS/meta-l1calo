SUMMARY = "OPC UA Server for Zynq on gFEX"
DESCRIPTION = "OPA UA Server for Zynq on gFEX"
LICENSE = "LGPLv3"
LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=e94f6920e0f51ea34f43be88dc810edc"
SRC_URI = "git://git@gitlab.cern.ch:7999/atlas-dcs-opcua-servers/OpcUaGFexServer.git;branch=yocto_compat;protocol=ssh"

S = "${WORKDIR}/git"
PV = "1.0+git${SRCPV}"
SRCREV ?= "1728431379711ba6d59931e1a31267ce20ec6ddf"

inherit cmake pythonnative

DEPENDS += "boost python-lxml-native xsd-native xerces-c python-enum34-native python-six-native"

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