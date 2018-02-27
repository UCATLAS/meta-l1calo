SUMMARY = "OPC UA Server for Zynq on gFEX"
DESCRIPTION = "OPA UA Server for Zynq on gFEX"
LICENSE = "LGPLv3"
LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=e94f6920e0f51ea34f43be88dc810edc"
SRC_URI = "git://git@gitlab.cern.ch:7999/atlas-dcs-opcua-servers/opc-ua-server-zynq.git;protocol=ssh"

S = "${WORKDIR}/${BPN}-${PV}"

inherit cmake

DEPENDS += "boost"

# Specify any options you want to pass to cmake using EXTRA_OECMAKE:
EXTRA_OECMAKE = ""
