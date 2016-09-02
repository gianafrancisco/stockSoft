; example2.nsi
;
; This script is based on example1.nsi, but it remember the directory, 
; has uninstall support and (optionally) installs start menu shortcuts.
;
; It will install example2.nsi into a directory that the user selects,

;--------------------------------

; The name of the installer
Name "Control de Stock"

; The file to write
OutFile "setup.exe"

; The default installation directory
;InstallDir $PROGRAMFILES\ggstock
InstallDir c:\ggstock

; Registry key to check for directory (so if you install again, it will 
; overwrite the old one automatically)
InstallDirRegKey HKLM "Software\ggstock" "Install_Dir"

; Request application privileges for Windows Vista
RequestExecutionLevel admin

;--------------------------------

; Pages

Page components
Page directory
Page instfiles

UninstPage uninstConfirm
UninstPage instfiles

;--------------------------------

; The stuff to install
Section "GG Intenieria - Control de Stock"

  SectionIn RO
  
  ; Set output path to the installation directory.
  SetOutPath $INSTDIR
  
  ; Put file there
  File "..\target\mpmStock-0.1.0.jar"
  File "run.bat"
  File "icon.ico"
  File /nonfatal /a /r "jre"
  File /nonfatal /a /r "..\src\main\webapp"
  WriteUninstaller "uninstall.exe"
  
SectionEnd

; Optional section (can be disabled by the user)
Section "Start Menu Shortcuts"

  CreateDirectory "$SMPROGRAMS\MPM"
  CreateShortCut "$SMPROGRAMS\MPM\Uninstall.lnk" "$INSTDIR\uninstall.exe" "" "$INSTDIR\uninstall.exe" 0
  CreateShortCut "$SMPROGRAMS\MPM\Stock.lnk" "$INSTDIR\acceso.bat" "" "$INSTDIR\icon.ico" 0
  
SectionEnd

Section "Desktop Shortcuts"

    CreateShortCut "$DESKTOP\Stock.lnk" "$INSTDIR\run.bat" "" "$INSTDIR\icon.ico" 0

SectionEnd

;--------------------------------

; Uninstaller

Section "Uninstall"
  
  ; Remove registry keys
  DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\ggstock"
  DeleteRegKey HKLM SOFTWARE\ggstock

  ; Remove files and uninstaller
  Delete $INSTDIR\run.bat
  Delete $INSTDIR\mpmStock-0.1.0.jar
  Delete $INSTDIR\uninstall.exe
  Delete $INSTDIR\icon.ico

  ; Remove shortcuts, if any
  Delete "$SMPROGRAMS\MPM\*.*"
  Delete "$DESKTOP\Stock.lnk"

  ; Remove directories used
  RMDir "$SMPROGRAMS\MPM"
  RMDir /r "$INSTDIR"

SectionEnd
