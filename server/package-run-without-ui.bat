call mvn -f ../pom.xml clean package

:: delete all dir
for /f %%i in ('dir /ad/b') do rd %%i /s /q

:: slow
:: ts.exe -show3 -time3 PlayerA.dll Tetries_HW.jar

:: fast
:: ts.exe -show3 PlayerA.dll Tetries_HW.jar

:: no ui
ts.exe PlayerA.dll Tetries_HW.jar
