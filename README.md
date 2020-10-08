# Hotel administrator

# Java version: 14.0.1

# Apache tomcat version: 9.0.38

# Запуск

# Настройка работы с БД
Для успешной работы необходимо запустить run.bat, введя пароль требуемое количество раз. В app.properties настраиваются данные для базы данных (имя пользователя, пароль и тд.).

# Настройка логгирования
В app.properties можно включить/выключить логгирование sql в файл и указать уровень, а также в разделе #appender указать директорий в переменную 
util.appenderBuilder.folderName. В файле же web-module/.../resources/log4j2.xml атрибут fileName отвечает за расположение файла для стандартного логгирования.

# Настройка работы csv
Кроме того, в app.properties в разделе #csv переменная csv.export.directoryPath отвечает за директорий для файлов csv.

# Настройка развертывания
Для deploy/undeploy необходимо в pom.xml указать данные пользователя из файла tomcat-users.xml(каталог tomcat), а также имя сервера из файла
settings.xml(каталог apache-maven, раздел servers).