!define TITLE "malykh-geo"
!define EXE "geo-enter.exe"
!define LIB "$EXEDIR\lib"

!define CLASS "com.malykh.geo.enterpoint.Main"
!define JAVA "javaw"

Name ${TITLE}
Caption ${TITLE}

OutFile ${EXE}

Icon ep.ico

RequestExecutionLevel user

XPStyle on
SilentInstall silent
AutoCloseWindow true
ShowInstDetails nevershow

Section ""
  Call GetJRE
  Pop $R0

  StrCpy $R1 ""
  Call GetParameters
  Pop $R1

  Call GetClassPath
  Pop $R2
;  MessageBox MB_OK "$R2"  

  StrCpy $R0 '$R0 -Xmx512m -classpath "$R2" ${CLASS} "$EXEDIR" $R1'
;  MessageBox MB_OK "$R0"
;  SetOutPath $EXEDIR
  Exec "$R0"
  Quit

SectionEnd

Function GetParameters
  Push $R0
  Push $R1
  Push $R2
  StrCpy $R0 $CMDLINE 1
  StrCpy $R1 '"'
  StrCpy $R2 1
  StrCmp $R0 '"' loop
  StrCpy $R1 ' '
loop:
    StrCpy $R0 $CMDLINE 1 $R2
    StrCmp $R0 $R1 loop2
    StrCmp $R0 "" loop2
    IntOp $R2 $R2 + 1
    Goto loop
loop2:
    IntOp $R2 $R2 + 1
    StrCpy $R0 $CMDLINE 1 $R2
    StrCmp $R0 " " loop2
  StrCpy $R0 $CMDLINE "" $R2
  Pop $R2
  Pop $R1
  Exch $R0
FunctionEnd

Function GetJRE
;
;  Find JRE (exe-file)
;  1 - in .\jre directory (JRE Installed with application)
;  2 - in JAVA_HOME environment variable
;  3 - in the registry
  Push $R0
  Push $R1

  StrCpy $R0 "$EXEDIR\jre\bin\${JAVA}.exe"
  IfFileExists $R0 JreFound

  ClearErrors
  ReadRegStr $R1 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
  ReadRegStr $R0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$R1" "JavaHome"
  StrCpy $R0 "$R0\bin\${JAVA}.exe"
  IfFileExists $R0 JreFound

  ClearErrors
  ReadEnvStr $R0 "JAVA_HOME"
  StrCpy $R0 "$R0\bin\${JAVA}.exe"
  IfFileExists $R0 JreFound

  ClearErrors
  StrCpy $R0 "$PROGRAMFILES64\Java\jre6\bin\${JAVA}.exe"
  IfFileExists $R0 JreFound

  Sleep 800
  MessageBox MB_ICONEXCLAMATION|MB_YESNO \
               'Could not find a Java Runtime Environment installed on your computer. \
               $\nWithout it you cannot run "${TITLE}". \
               $\n$\nWould you like to visit the Java website to download it?' \
               IDNO +2
  ExecShell open "http://java.sun.com/getjava"
  Quit
        
JreFound:
  Pop $R1
  Exch $R0
FunctionEnd

Function GetClassPath
;
; Find jar files
  Push $R0
  Push $R1
  Push $R2

  StrCpy $R0 ""  
  ClearErrors
  FindFirst $R1 $R2 "${LIB}\*.jar"
  IfErrors NoJars JarFound
NoJars:
  MessageBox MB_OK "No jars!"
  Quit
JarFound:
  StrCpy $R0 "$R0${LIB}\$R2;"
  ClearErrors
  FindNext $R1 $R2
  IfErrors JarsReady JarFound
JarsReady:
  FindClose $R1

  Pop $R2
  Pop $R1
  Exch $R0  
FunctionEnd
