// Пути к файлам и каталогам
//////////////////////////////////////////

Массив = Новый Массив;
// windows
Массив.Добавить("\\test\test\TEST.xls"); // <-- ошибка
Массив.Добавить("\\server\share\folder\myfile.txt"); // <-- ошибка
Массив.Добавить("\\server\share\myfile.txt"); // <-- ошибка
Массив.Добавить("\\123.123.123.123\share\folder\myfile.txt"); // <-- ошибка
Массив.Добавить("c:\folder\myfile.txt"); // <-- ошибка
Массив.Добавить("c:\folder\myfileWithoutExtension"); // <-- ошибка
Массив.Добавить("c:\folder"); // <-- ошибка
Массив.Добавить("c:/folder"); // <-- ошибка
Массив.Добавить("c://folder"); // <-- ошибка
Массив.Добавить("//test/test/TEST.xls"); // <-- ошибка
Массив.Добавить("C:\Program Files (x86)\"); // <-- ошибка
Массив.Добавить("F:\DATA\Develop\Module.bsl"); // <-- ошибка
// unix
Массив.Добавить("/dev/test"); // <-- ошибка
Массив.Добавить("/dev/test/test.bsl"); // <-- ошибка
Массив.Добавить("/home/root/test.bsl"); // <-- ошибка
Массив.Добавить("/etc/php-fpm/php.ini"); // <-- ошибка
Массив.Добавить("/less/test");

// не сработает, там нет путей
ПутьОтносительный = "./catalog"; // не должно срабатывать
Путь = "/catalog"; // исключаем, т.к. может использовано для URL и будет FP
ПутьКФайлу = "d:тутНетПутиКФайлу";
ПутьКоВторомуФайлу = "тут/нетпутикфайлу";
Структура = Новый Структура("Путь", "Съешь ещё этих мягких французских булок, да выпей же чаю.");
// IP адреса
СетевойАдрес = "232.252.181.67"; // <-- ошибка
НеСетевойАдрес = "22.00";
НеСетевойАдресЕще = "10.10.2010";
МассивАдресов = Новый Массив();
МассивАдресов.Добавить("160.211.46.126"); // <-- ошибка
МассивАдресов.Добавить("127.0.0.1"); // <-- ошибка
МассивАдресов.Добавить("300.300.300.300");
МассивАдресов.Добавить("da52:04d9:f9fc:8c45:7180:332d:527f:0c08"); // <-- ошибка
МассивАдресов.Добавить("6885:e6fb:e56e:f46c:2da2:a736:f9dc:68b6"); // <-- ошибка
Структура = Новый Структура("СетевойАдрес", "6dd7:1388:7fa1:-:d0af:bb82:0eaf:2225");
Структура = Новый Структура("СетевойАдрес", "6dd7:1388:7fa1:783d:d0af:bb82:0eaf:2225"); // <-- ошибка
Обработчики = Новый ТаблицаЗначений;
Обработчики.Колонки.Добавить("Версия");
Обработчик = Обработчики.Добавить();
Обработчик.Версия = "1.1.1.2";
МассивВерсий = Новый Массив;
МассивВерсий.Добавить("3.1.10.155");
СписокIP = Новый Массив;
СписокIP.Добавить("10.4.10.1"); // <-- ошибка

// Снова проверки путей
ПутьWin = "~/temp/file.txt"; // <-- ошибка
ПутьДляСомневающихся = "C:\temp/обратный слеш\тоже/работает"; // <-- ошибка
НеПуть = "!/bin/sh";

// Тут невозможно отличить fp с доменным путем от обычного сетевого пути в windows.
ДоменныйПуть = "\\domain\user"; // <-- ошибка

ПутьОтКаталогаЮзера = "%UserProfile%/что/то/там"; // <-- ошибка