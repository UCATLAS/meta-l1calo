SUMMARY = "humanreadable is a Python library to convert human-readable values to other units."
HOMEPAGE = "https://github.com/thombashi/humanreadable"
AUTHOR = "Tsuyoshi Hombashi"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=aeb2b32cd58a544e28c22b87ec55268b"

PYPI_PACKAGE = "humanreadable"

inherit pypi setuptools3

SRC_URI = "https://files.pythonhosted.org/packages/f6/64/b79577649ff6bb9d6677c8c13632a5b03b8bd80100df6d67307db9fe0cf8/humanreadable-0.3.0.tar.gz"

SRC_URI[md5sum] = "b4ad5d181745183445bd47703f7ed493"
SRC_URI[sha256sum] = "13a0de021098a0cb474b46e53cfedb9cbbfdb3c9a33e20a9dda48d8ea987109d"

#RDEPENDS_${PN} = "python3-pandas"

