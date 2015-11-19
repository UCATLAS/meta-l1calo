require core-image-base.bb

DESCRIPTION = "The base image for all L1Calo embedded OS work providing python at a minimum."

IMAGE_FEATURES += "python-lang python-core python-twisted"

