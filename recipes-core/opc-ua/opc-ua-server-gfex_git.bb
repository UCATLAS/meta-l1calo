SUMMARY = "OPC UA Server for Zynq on gFEX"
DESCRIPTION = "OPA UA Server for Zynq on gFEX"
LICENSE = "LGPLv3"
LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=e94f6920e0f51ea34f43be88dc810edc"
SRC_URI = "gitsm://git@gitlab.cern.ch:7999/atlas-dcs-opcua-servers/OpcUaGFexServer.git;branch=yocto_compat;protocol=ssh \
           file://0001-Update-Poverty-submodule-to-boost-python3-and-python.patch \
           "

S = "${WORKDIR}/git"
PV = "1.0+git${SRCPV}"
SRCREV ?= "16528e2c97ae98c5725fd56bcec91e86f8a821e7"


inherit cmake python3native pythonnative

DEPENDS += "python3-native python3-dev boost boost-native python-native python-dev python-lxml-native xsd-native xerces-c python-enum34-native python-six-native python-jinja2-native python-markupsafe-native"

# install it correctly, manually
do_install() {
  # install configuration files to /etc/quasar/*
    install -d ${D}${sysconfdir}/quasar/
    install -m 0755 ${B}/Configuration/Configuration.xsd ${D}${sysconfdir}/quasar/
    install -m 0755 ${S}/bin/config.xml ${D}${sysconfdir}/quasar/


  # install binary to /usr/bin
    install -d ${D}${bindir}/
    install -m 0755 ${B}/bin/OpcUaServer ${D}${bindir}

  # install shared object file to /usr/bin
    install -m 0755 ${B}/bin/libOpcUaServerObject.so ${D}${bindir}
    chrpath -d ${D}${bindir}/libOpcUaServerObject.so
    
  # install Poverty shared object file
    install -m 0755 ${B}/Poverty/Poverty.so ${D}${bindir}
    chrpath -d ${D}${bindir}/Poverty.so
}