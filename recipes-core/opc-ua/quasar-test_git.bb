SUMMARY = "%{name}"
LICENSE = "LGPLv3"
LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=e94f6920e0f51ea34f43be88dc810edc"
SRC_URI = "git://github.com/kratsg/quasar_test_project.git;protocol=https"
PV = "1.0+git${SRCPV}"
SRCREV = "7aab509b4613feadd473c59ccf22dbde667678c4"

S = "${WORKDIR}/git"

inherit cmake pythonnative

DEPENDS += "boost python python-lxml-native xsd xerces-c"

EXTRA_OECMAKE = " \
  -DCMAKE_BUILD_TYPE=Release \
  -DCMAKE_INSTALL_PREFIX=/opt/QuasarServer \
"
