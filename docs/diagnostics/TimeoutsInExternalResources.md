# Таймауты при работе с внешними ресурсами (TimeoutsInExternalResources)

|   Тип    |    Поддерживаются<br>языки    |  Важность   |    Включена<br>по умолчанию    |    Время на<br>исправление (мин)    |                Теги                 |
|:--------:|:-----------------------------:|:-----------:|:------------------------------:|:-----------------------------------:|:-----------------------------------:|
| `Ошибка` |         `BSL`<br>`OS`         | `Критичный` |              `Да`              |                 `5`                 |    `unpredictable`<br>`standard`    |

## Параметры


|                   Имя                   |   Тип    |                      Описание                       |    Значение<br>по умолчанию    |
|:---------------------------------------:|:--------:|:---------------------------------------------------:|:------------------------------:|
| `analyzeInternetMailProfileZeroTimeout` | `Булево` | `Анализировать таймаут у "ИнтернетПочтовыйПрофиль"` |             `true`             |
<!-- Блоки выше заполняются автоматически, не трогать -->
## Описание диагностики

При работе с внешними ресурсами с помощью объектов WSОпределения, WSПрокси, HTTPСоединение, FTPСоединение следует задавать таймаут – предельное время ожидания выполнения операции. В противном случае, в результате бесконечного ожидания программа зависнет или часть функционала программы станет недоступна.  
Для ИнтернетПочтовыйПрофиль платформа устанавливает значение таймаута по умолчанию в 30 секунд, но, несмотря на это, стоит явно указывать значение таймаута при использовании.

Установка таймаута является защитой от целого ряда внешних факторов:

* нестабильного подключения к Интернету, когда регулярно происходит прерывание связи, и система не может получить цельный ответ сервера, к которому выполняется подключение;
* при включенных антивирусных программах или при неправильных настройках брандмауэра;
* неправильной настройки прокси-сервера;
* ненадежной работы веб-сервера из-за возросшей нагрузки или некорректной работы скриптов.

## Примеры

Неправильно:

```bsl
HTTPСоединение = Новый HTTPСоединение("zabbix.localhost", 80);
```

или

```bsl
FTPСоединение = Новый FTPСоединение(Сервер, Порт, Пользователь, Пароль, Прокси, ПассивныйРежим);
```

Правильно:

```bsl
HTTPСоединение = Новый HTTPСоединение("zabbix.localhost", 80,,,, 1);
```

или

```bsl
HTTPСоединение = Новый HTTPСоединение("zabbix.localhost", 80);
HTTPСоединение.Таймаут = 1;
```

## Источники

* [Таймауты при работе с внешними ресурсами](https://its.1c.ru/db/v8std#content:748:hdoc)
* [У ИнтернетПочтовыйПрофиль таймаут по умолчанию есть](https://its.1c.ru/db/metod8dev/content/2358/hdoc)

## Сниппеты

<!-- Блоки ниже заполняются автоматически, не трогать -->
### Экранирование кода

```bsl
// BSLLS:TimeoutsInExternalResources-off
// BSLLS:TimeoutsInExternalResources-on
```

### Параметр конфигурационного файла

```json
"TimeoutsInExternalResources": {
    "analyzeInternetMailProfileZeroTimeout": true
}
```
