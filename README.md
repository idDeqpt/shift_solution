# Утилита фильтрации содержимого файлов

## Описание задачи
Задача утилиты записать разные типы данных в разные файлы. Целые числа в один
выходной файл, вещественные в другой, строки в третий. По умолчанию файлы с
результатами располагаются в текущей папке с именами integers.txt, floats.txt, strings.txt.

## Дополнительные опции
| Опция      | Описание                                                                           |
|------------|------------------------------------------------------------------------------------|
| -s         | Собрать краткую статистику по считанным значениям                                  |
| -f         | Собрать полную статистику по считанным значениям                                   |
| -a         | Задать режим добавления в существующие файлы (по умолчанию файлы перезаписываются) |
| -o [value] | Задать путь [value] для выходных файлов                                            |
| -p [value] | Задать префикс [value] для выходных файлов                                         |

Пример команды: ```-f -a -p sample- -o files in1.txt in2.txt```

## Описание классов
Все класы упакованы в пакет **util**.

| Название класса  | Описание                                                                                                             |
|------------------|----------------------------------------------------------------------------------------------------------------------|
| CombinedValue    | Содержит числовое и строковое значения для дальнейшей обработки статистики и записи проанализированной строки в файл |
| ValuesGroup      | Содержит ArrayList значений CombinedValue и RegEx строку, характеризующую хранящиеся значения                        |
| ValuesStatistics | Вычисляет полную/краткую статистику по принимаемым значениям CombinedValue                                           |
| OptionManager    | На вход принимает агрументы командной строки, затем конвертирует во внутренние переменные опций                      |
| Parser           | Парсит входящий файл, классифицируя содержащиеся в нём данные по шаблону RegEx                                       |
| Main             | Содержит точку входа                                                                                                 |

## Запуск приложения

Для запуска приложения требуется установленный JDK 24.
Для компиляции использовать последовательность команд из корневого каталога:
```
javac -d bin ./src/*
jar -cmf manifest.mf util.jar -C bin .
```
Далее используется команда запуска .jar файла с необходимыми аргументами, например:
```
java -jar util.jar -f -a -p sample- -o files in1.txt in2.txt
```
Результат выполнения:
```
Statistics of integer values:
        |-Count of values: 3
        |-Min value: 45.0
        |-Max value: 1.234568E18
        |-Average value: 4.1152264E17
        |-Sum of values: 1.234568E18
Statistics of float values:
        |-Count of values: 3
        |-Min value: -0.001
        |-Max value: 3.1415
        |-Average value: 1.0468334
        |-Sum of values: 3.1405
Statistics of string values:
        |-Count of values: 6
        |-Min value: 4.0
        |-Max value: 42.0
        |-Average value: 19.333334
        |-Sum of values: 116.0
```
Файл ./files/sample-integers.txt:
```
45
100500
1234567890123456789
```
Файл ./files/sample-floats.txt:
```
3.1415
-0.001
1.528535047E-25
```
Файл ./files/sample-strings.txt:
```
Lorem ipsum dolor sit amet
Пример
consectetur adipiscing
тестовое задание
Нормальная форма числа с плавающей запятой
Long
```

## Обработка ошибок
### Указание несуществующей опции:
```
C:\Projects\Java\Shift test>java -jar util.jar -s -a -e sample- in1.txt in2.txt bebe.txt
Unknown option: -e
```

### Окончание команды опцией, требующей какого-либо значения:
```
C:\Projects\Java\Shift test>java -jar util.jar -s -a -p
Value of -p option is missing.
For example: -p <prefix-value>
```

### Запуск команды без указания целевых файлов:
```
C:\Projects\Java\Shift test>java -jar util.jar -f -a -p sample-
Not entered source files
```

### Передача для обработки несуществующего файла:
```
C:\Projects\Java\Shift test>java -jar util.jar -f -a -p sample- -o files non-existent-file.txt
Error: file non-existent-file.txt not found
```
Стоит отметить, что если передать корректные файлы вместе с несуществующими, то команда оповестит о передаче несуществующих файлов, но при этом обработает корректные файлы:
```
C:\Projects\Java\Shift test>java -jar util.jar -f -a -p sample- -o files in1.txt in2.txt non-existent-file1.txt non-existent-file2.txt
Error: file non-existent-file1.txt not found
Error: file non-existent-file2.txt not found
Statistics of integer values:
        |-Count of values: 3
        |-Min value: 45.0
        |-Max value: 1.234568E18
        |-Average value: 4.1152264E17
        |-Sum of values: 1.234568E18
Statistics of float values:
        |-Count of values: 3
        |-Min value: -0.001
        |-Max value: 3.1415
        |-Average value: 1.0468334
        |-Sum of values: 3.1405
Statistics of string values:
        |-Count of values: 6
        |-Min value: 4.0
        |-Max value: 42.0
        |-Average value: 19.333334
        |-Sum of values: 116.0
```
