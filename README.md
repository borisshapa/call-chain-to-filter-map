CallChainToFilterMap
----
* Язык описания операций над целочисленными массивами задан следующей грамматикой:
```
<ws> ::= "" | '0020' | '000A' | '000D' | '0009'
<ws-seq> ::= <ws> | <ws> <ws-seq>
<digit> ::= “0” | “1" | “2” | “3" | “4” | “5" | “6” | “7" | “8” | “9"
<number> ::= <digit> | <digit> <number>
<operation> ::= “+” | “-” | “*” | “>” | “<” | “=” | “&” | “|”
<constant-expression> ::= “-” <number> | <number>
<binary-expression> ::= “(” <expression> <operation> <expression> “)”
<weak-expression> ::= “element” | <constant-expression> | <binary-expression>
<expression> ::= <ws-seq> <weak-expression> <ws-seq>
<map-call> ::= “map{” <expression> “}”
<filter-call> ::= “filter{” <expression> “}”
<weak-call> ::= <map-call> | <filter-call>
<call> ::= <ws-seq> <weak-call> <ws-seq>
<call-chain> ::= <call> | <call> “%>%” <call-chain>
```
* Арифметические операции имеют стандартную семантику. 
Операция “&” это логическое “и”, операция “|” &mdash; логическое “или“. 
Бинарные выражения с операторами “&”, “|” , “=”, “>”, “<” имеют булевый тип, 
а с операторами “+”, “-”, “*” &mdash; арифметический. 
Операнды арифметических операций и операций сравнения имеют целочисленный тип, 
а операнды логических &mdash; булевый. Вызов функции map заменяет каждый элемент массива 
на результат вычисления переданного арифметического выражения, в котором вместо element подставляется 
значение текущего элемента. Вызов функции filter оставляет в массиве только элементы, для которых переданное выражение истинно.
Последовательность вызовов применяется к массиву по очереди, слева направо.

* Программа преобразовывает выражения, описываемые 
правилом <call-chain> в выражения вида 
`<filter-call> “%>%” <map-call>`, эквивалентные исходным. 
Она принимать на стандартный поток ввода одну строку &mdash; 
выражение описываемое правилом `<call-chain>` и 
выводит строку с преобразованным выражением. 
В случае наличия синтаксической ошибки или несовпадения типов, программа бросает соответствующее
 исключение (строка информации об ошибке начинается с "SYNTAX ERROR" или "TYPE ERROR"
 в зависимости от ошибки).
 
* Программа производит упрощение переданных выражений.  
Так как в языке описания операций над элементами массива нет операции деления,
то все аргументы функций `filter` и `map` являются оперцаиями над многочленами
с целочисленными коэффицентами. При этом аргументы функции `filter` задают множество
вещественных чисел. С помощью нахождения вещественных корней многочленов и промежутков на вещественной
прямой, удовлетворяющих предикату, переданному в `filter`, а также округления границ
этих промежутков в нужную сторону (так как язык описывает операции над **целочисленными** массивами
и грамматика не допускает дробных чисел), предикат `filter` в возвращаемой строке преобразуется в запись, описывающую 
непересекающиеся промежутки на вещественной прямой с целочисленными границами,
не содержащую арифметические операции. При этом, если этот предикат описывает все вещественные числа,
то он заменяется на `(0=0)`, если описывает пустое множество &mdash; на `(1=0)`. Аргумент операции `map` в исходной строке записан в виде многочлена.

* **Примеры**
    * ```
      In:
        filter{(element>10)}%>%filter{(element<20)}
      Out:
        filter{((element>10)&(element<20))}%>%map{element}
      ```
    
    * ```
      In:
        map{(element+10)}%>%filter{(element>10)}%>%map{(element*element)}
      Out:
        filter{(element>0)}%>%map{(((element*element)+(20*element))+100)}
      ```
      
    * ```
      In:
        filter{(element>0)}%>%filter{(element<0)}%>%map{(element*element)}
      Out:
        filter{(1=0)}%>%map{element}
      ```