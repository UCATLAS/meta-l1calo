SUMMARY = "%{name}"
LICENSE = "LGPLv3"
LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=e94f6920e0f51ea34f43be88dc810edc"
SRC_URI = "git://github.com/kratsg/quasar_test_project.git;protocol=https"
PV = "1.0+git${SRCPV}"
SRCREV = "964d97026baed77560b39ced97a860748e37feaa"

S = "${WORKDIR}/git"

inherit cmake

DEPENDS += "boost open62541 libxml2-native openjdk-8-native"

EXTRA_OECMAKE = " \
  -DCMAKE_BUILD_TYPE=Release \
"
