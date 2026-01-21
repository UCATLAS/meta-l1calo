# Useful ATCA Shelf Commands

The ATCA board runs a version of Linux. Here are the relevant ATCA shelf commands:

| Command Name | Notes |
|:--|:--|
| `clia board`  | Lists all of the boards connected to the shelf.|
| `clia activate board [X]`  | Turns on power for board `[X]`. Note that you should not use the `reboot` command in the SoC's OS or else these commands will stop working and you will need to unplug and replug the ATCA shelf. |
| `clia deactivate board [X]`  | Turns off power for board `[X]`. |

### Previous article: [Physical Hardware Setup with an ACTA Shelf](5-Physical-Hardware-Setup.md)
### Home: [Documentation Overview](README.md)
### Next article: [Structure of the Final OS](7-Final-OS-Structure.md)