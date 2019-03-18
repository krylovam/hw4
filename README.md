# hw4 Домашняя работа к лекции №5 "Базы данных"
Данная программа заполняет таблицы формата Excel и PDF данными о пользователях, а также добавляет их в базу данных mySQL. 
Данные при наличии сети скачиваются с помощью Api с сайта ramdomuser.me. При отсутствии сети данные заполняются из базы данных,если в ней
недостаточно пользователей, то данные генерируются рандомно из файлов, приложенных к программе

Подготовительные мероприятия:
В mysql создать базу данных с 2 таблицами по шаблонам:
address ( id int auto_increment not null, postcode varchar(256), country varchar(256), region varchar(256), city varchar(256), street varchar(256), house int, flat int, primary key (id) )

persons ( id int auto_increment not null, surname varchar(256), name varchar(256), middlename varchar(256), birthday date, gender varchar(1), inn varchar(12), address_id int not null, foreign key (address_id) references address(id), primary key (id) )

Инструкция по запуску:
1. Скачать репозиторий любым удобным способом
2. Открыть проект в Intellij IDEA
3. Добавить нужные библиотеки в проект(Project Structure -> Modules -> Dependencies - > "+" -> выбираем все jar файлы из папки resources
4. Изменяем значения URL, USER, PASS в соответствии со своими
5. Запускаем программу
