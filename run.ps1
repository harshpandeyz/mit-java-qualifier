New-Item -ItemType Directory -Force out | Out-Null
javac -d out src\Main.java
java -cp out Main
