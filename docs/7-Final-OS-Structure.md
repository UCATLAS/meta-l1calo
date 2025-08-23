# Structure of the Final OS

Once the OS is done booting and you've logged in, you might want to see if the changes you made to the OS have worked. The following is a guide through the customizations to the standard Linux filesystem:  
  

## Custom Program Folders 
  
The following folders contain custom programs defined in the `meta-l1calo` layer:

### Folder 1: `/usr/lib/gfex-programs`

```  
gfex-programs  
└── init-<gfex-init script>.[sh/py]  
└── ...  
```

This holds the `[gfex-init]` programs that run when the OS boots. Their corresponding `.service` files which tell `systemd` to run them at boot are located in `/lib/systemd/system`.

### Folder 2: `/software`

```  
software  
└── gfex-register-access  
│   └── ...  
└── opc-ua-gfex-milkyway-server  
|   └── ...  
└── gfex-management-scripts  
|   └── ...  
└── misc  
|   └── i2c_poll
|   └── ipmc_auto_shutdown
|   └── log-manager.py
```

This holds the repositories from CERN's GitLab, as well as some files used by the `[gfex-init]` programs.

----------

### Previous article: [Useful ATCA Commands](6-Useful-ATCA-Commands.md)
### Home:  [Documentation Overview](README.md)
### Next article:  [Useful OS Commands](8-Useful-OS-Commands.md)