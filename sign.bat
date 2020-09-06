if exist jdk (
    set javaDir=jdk\bin\
)

%javaDir%java.exe -Xmx1024m -cp "classes;lib/*;conf" -Dqoober.runtime.mode=desktop qoober.tools.SignTransactionJSON %*
