SUMMARY = "OPC UA implementation"
HOMEPAGE = "https://open62541.org/"
SECTION = "devel/libs"
LICENSE = "MPL-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=815ca599c9df247a0c7f619bab123dad"
SRC_URI = "gitsm://github.com/${PN}/${PN}.git;protocol=https"
# for tarball, but out-of-tree builds don't work...
# SRC_URI = "https://github.com/${PN}/${PN}/archive/v${PV}.tar.gz"

S = "${WORKDIR}/git"

# NOTE: unable to map the following CMake package dependencies: Check Sphinx ClangTools LATEX MbedTLS Valgrind
DEPENDS = "openssl python-six-native"

inherit cmake pythonnative

# Specify any options you want to pass to cmake using EXTRA_OECMAKE:
EXTRA_OECMAKE = "\
  -DCMAKE_BUILD_TYPE=Release \
"

FILES_${PN}_append = "\
  ${libdir}/cmake \
  ${libdir}/cmake/open62541*.cmake \
"
