# Knowledge service

# Java version: 14.0.1

# Apache tomcat version: 9.0.38

# Общее описание
Проект представляет собой собой Restful WEB проект на тему "Сервис по обмен знаниями", предоставляющий API для доступа к своему функционалу. Предполагается следующие возможности:
1. Регистрация и логин пользователей (администраторы и обычные пользователи).
2. Редактирование профиля пользователя.
3. Просмотр списка доступных тем, разделов, занятий и курсов, а также их поиск и фильтрация.
4. Возможность добавления, удаления и редактирования тем, разделов, занятий и курсов.
5. Возможность создать подписки на курсы, а также занятия.
6. Возможность написания отзывовов о занятиях, курсах.
7. Возможность добавления и редактирования информации о преподавателях, а также назначения им взнагражадения, которое может быть как фиксированным, так и плавающим.

Для тестирования проекта можно использовать логин:iliuha1907 и пароль:password для входа в систему. При отправке запроса через swagger ui в раздел заголовка можно поместить слово Bearer. В базе данных добавленные пароли зашифрованы с помощью сервиса: https://bcrypt-generator.com.

# Настройка работы с БД
За базу данных отвечает папка database в проекте. Внутри находится папка scripts, которая содержит скрипты для создания схемы базы данных, а также для ее инициализации. Кроме того, папка database содержит модель схемы базы данных  в виде изображения и расширения mwb.
Для создания схемы и заполнения ее данными, нужно запустить скрипт setupDatabase.bat, а после ввести пароль некоторое количество раз по запросу.
# Настройка логгирования
В модуле контроллер в папке resources log4j2.xml отвечает за настройку логгирования. Чтобы указать папку для логов, нужно изменить свойство dir в этом файле. Чтобы изменить уровень логгирования, нужно изменить свойство  level логгера.  В частности, уровень off выключает логгирование.
# Настройка развертывания
Для deploy достаточно запустить скрипт buildAndDeploy.bat, а просто для сборки - build.bat. Для undeploy нужно запустить скрипт undeploy.bat. 
Для успешного deploy/undeploy необходимо в главном pom.xml указать данные пользователя из файла tomcat-users.xml(каталог tomcat), а также имя сервера из файла settings.xml(каталог apache-maven, раздел servers).
После развертывания список доступных адресов можно получить по адресу /swagger-ui.html в формате ui и по адресу /v2/api-docs в формате JSON.
# Настройка app.properties
Раздел #teacher содержит свойство teacher.reward, которое отвечает за награду учителя за 1 проведенное занятие.
Раздел #date содержит свойство date.dateFormat, которое  определяет формат даты, который будет выдан контроллером.
Раздел #database содержит данные для подключения и работы с базой данных. Это драйвер, имя пользователя, пароль, адрес подключения, а также имя пакета с классами-моделями, в которое будут проецироваться данные из базы.
Раздел #authentication содержит свойства описывающие ключ, с помощью которого будут шифроваться данные, а также время, в течение которого токен будет действителен в миллисекундах.
