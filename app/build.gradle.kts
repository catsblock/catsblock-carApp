name: Compilar Android APK Automaticamente

on:
  push:
    branches: [ "main" ]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
    - name: Baixar o código do Repositório
      uses: actions/checkout@v4

    - name: Configurar o Java (JDK 17)
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'

    - name: Criar Gradle Wrapper e Compilar
      # Este comando abaixo resolve o erro de 'No such file or directory'
      # Ele cria o arquivo gradlew na hora, caso ele esteja faltando.
      run: |
        gradle wrapper --gradle-version 8.5
        chmod +x gradlew
        ./gradlew assembleDebug

    - name: Liberar o APK gerado para Download
      uses: actions/upload-artifact@v4
      with:
        name: CatsBlockCar-App
        path: app/build/outputs/apk/debug/app-debug.apk
