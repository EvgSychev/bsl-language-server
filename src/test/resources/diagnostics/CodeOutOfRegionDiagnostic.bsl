//////////////////////////////////////////////
// Название модуля
//////////////////////////////////////////////

Перем А;                // <- Ошибка
#Область Переменные
Перем Б;
Перем Дд;
#КонецОбласти
Перем Ии;               // <- Ошибка

#Область Методы
Функция Аа() Экспорт
    Возврат 7;
КонецФункции
#КонецОбласти

Процедура Бб()          // <- Ошибка
    #Область Методы2
    Сообщаить(42);
    #КонецОбласти
КонецПроцедуры

///////////////////////////////////////////
// инициализация
///////////////////////////////////////////

#Область Иниц
А = 78;
#КонецОбласти

Б = Аа() + А;           // <- Ошибка

#Область Иниц
Если Условие Тогда
    Ии = 79;
КонецЕсли;
#КонецОбласти

