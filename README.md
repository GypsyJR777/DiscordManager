# DiscordManager
На данный момент документация **только на русском языке**. [Добавить к себе бота](https://discord.com/oauth2/authorize?client_id=1116872667811823698&scope=bot&permissions=8). 
Для бота требуются только права администратора.
 
## Про бота 
Идея данного бота зародилась у меня, когда послушал рассуждения друга (владельца нашего сервера в Дискорде), почему мы несколько раз меняли серваки. Итог у него был простой: слишком много накапливалось людей на сервере, при этом большинство из них больше одного раза ни с кем не играли.
Я предложил написать бота, который во первых сможет заменить нам нашего нынешнего "менеджера", а во вторых в который мы сможем добавить все необходимые нам функции. Таким образом первое, что было реализовано это считывание участников голосвых чатов, обновление времени присутствия на 
сервере и удаление пользователей, которые не общались больше 30 дней.

## Стек
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)  ![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)  ![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)  ![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white) ![JDA](https://img.shields.io/badge/Discord-%25235865F2.svg?style=for-the-badge&logo=discord&logoColor=white&label=JDA&labelColor=blue&color=blue)

## Инструкция по установке
Для работы бота требуется PostgreSQL, а также ряд переменных окружения:
- ${DISCORD_TOKEN} - токен дискорд-бота
- ${DB_URL} - адрес БД (например, jdbc:postgresql://manager-bd:5432/discordbot)
- ${DB_USERNAME} - имя пользователя БД
- ${DB_PASS} - пароль БД
- ${SEGMIND_TOKEN} - токен для Kandinsky 2.2 на Segming https://www.segmind.com/models/kandinsky2.2-txt2img/api  (опциональный параметр)
