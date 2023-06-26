package pl.training

// Konwencje kodowania https://kotlinlang.org/docs/coding-conventions.html

// Rozpoczęcie programu odbywa się przez wykonanie specjalnej funkcji main, parametry wejściowe są opcjonalne

/*fun main() {
    println("Hello World")
}*/

/*fun main(args: Array<String>) {

    println("Hello World, parameters: ${args.contentToString()}")
}*/


// Zmienne
// Zmienne tylko do odczytu deklarowane są z użyciem słowa kluczowego val. Zmienne, których stan może się zmienić deklarowane są z wykorzystaniem var

fun main() {
    val a: Int = 1
    //  a = 5 // exception, a  jest niezmienne

    var b = 20
    b = 30

    /*

    Typy danych
    W języku Kotlin wszystko jest obiektem (brak typów prymitywnych jak w Javie, chociaż są one wykorzystywane "pod spodem" z wyłączeniem typów generycznych oraz nullable)

    Type	Size (bits)    Min value	Max value
    Byte	8	           -128	        127
    Short	16	           -32768	    32767
    Int	    32	           -231	        231-1    // default
    Long	64	           -263	        263-1

    Tak jak wiele innych języków, Kotlin oferuje zestaw typów całkowitych bez znaku. Odznaczają się one większym
    zakresem liczb dodatnich (kosztem braku wartości ujemnych) i oferują większość operacji dostępnych w ramach typów standardowych.

    Type	Size (bits)	   Min value	Max value
    UByte	8	           0	        255
    UShort	16	           0	        65535
    UInt	32	           0	        232-1
    ULong	64	           0	        264-1


    Type	Size (bits)	   Significant bits	     Exponent bits 	    Decimal digits
    Float	32	           24	                 8	                6-7
    Double	64	           53	                 11	                15-16             // default

    Typ zmiennej całkowitej może być podany wprost lub automatycznie wywnioskowany (zależnie od przypisanej wartości)
    Typ zmiennej zmiennoprzecinkowej może być podany wprost lub jest zakładany domyślnie jako Double w momencie zdefiniowania części ułamkowej

    */

    val size: Byte = 100 // Byte
    val smallValue = 1 // Int
    val bigValue = 5000000000 // Long
    val creditCardNumber = 1234_5678_9012_3456L // Long
    val socialSecurityNumber = 999_99_9999L // Long
    val hexBytes = 0xFF_EC_DE_5E // Long
    val bytes = 0b11010010_01101001_10010100_10010010 // Long
    val b1: UByte = 1u  // UByte
    val s: UShort = 1u // UShort
    val l: ULong = 1u  // ULong
    val a1 = 42u // UInt
    val a2 = 0xFFFF_FFFF_FFFFu // ULong
    val a3 = 1uL // ULong
    val result = 4.5 // Double
    val totalValue: Float = 1.4F // Float

    /*
     W języku kotlin nie występuje niejawna konwersja typów, jednak często jest ona niepotrzebna,
     ponieważ typ jest wnioskowany z kontekstu. Jawna konwersja typów odbywa się przy użyciu dedykowanych metod np. toByte(), toShort() czy toChar()
    */

    val value = 3 // Int
    val sumOfNumbers = result + value
    val sum = 1L + 2 // Long + Int => Long

    /*
      Kotlin oferuje standardowy zestaw operatorów arytmetycznych tzn. +, -, *, /, %. Dzielenie liczb
      całkowitych zawsze zwraca liczbę całkowitą (część ułamkowa jest odrzucana). Typ zwracanego wyniku promowany jest do
       największego z używanych typów. Istnieje możliwość nadpisania standardowych operatorów.
    */

    val x1 = 5L / 3 // 1 Long
    val x2 = 5 / 2.toFloat() // 2.5 Float
    val x3 = 5.toDouble() / 2.toFloat() // 2.5 Double
    val x4 = 5.0 / 2.toFloat() // 2.5 Double

    /*
      Boolean reprezentuje typ, który może przechowywać jedną z dwóch dozwolonych wartości tj. true lub false. W połączeniu z
      operatorami logicznymi ||, &&, ! (lub, i, negacja) umożliwia tworzenie i rozwiązywanie wyrażeń logicznych.
      Wyrażenia logiczne są rozwijane leniwie
    */

    // Typ Char reprezentuje pojedynczy znak

    val letter: Char = 'a'
    val newLine = '\n'
    val char = '\uFF00'

    /*
      W języku Kotlin tekst reprezentowany jest przez typ String, w rzeczywistości będący sekwencją znaków.
      Każda modyfikacja zmiennej typu String powoduje utworzenie nowego obiektu. Literały mogą zawierać specjalne wyrażenia,
      których wynik (po rozwinięciu) jest włączany do wynikowego ciągu znaków.
    */

    val text = "Hello Kotlin"
    val firstLetter = text[0]
    val name = "Jan"
    val helloMessage = "Hello $name \n"
    println("$helloMessage length is ${helloMessage.length}")
    val otherText = """
       |Programming Kotlin
       |is fun!
       |$helloMessage\n Programmer earns a lot of ${'$'}
       """.trimMargin()

    /*
      Tablice reprezentowane są przez typ Array. Każda tablica posiada długość, a dostęp do jej elementów odbywa się z
      wykorzystaniem operatora [] oraz indeksu zmieniającego się od 0 do n - 1 (gdzie n to rozmiar tablicy).
      Istnieją także typy reprezentujące tablice typów prymitywnych takie jak IntArray czy ByteArray
     */

    val numbers = arrayOf(1, 2, 3, 4, 5)
    print(numbers.size) // 5
    val secondValue = numbers[1] // 2
    Array(5) { it + 1 }.forEach { println(it) }

    /*
      W celu sprawdzenia przynależności do danego typu używamy operatora is. Jeśli typ zostanie potwierdzony, nie ma
      potrzeby jawnego rzutowania (smart casts). Ze względu na zacieranie typów, w przypadku typów generycznych np.
      Listy nie jest możliwe sprawdzenie dokładnego typu obiektu (wyjątkiem może być sytuacja, kiedy typ wynika z
      kontekstu np. argumenty wejściowe funkcji sprawdzone w czasie kompilacji).
     */

    val response: Any = "Some text"
    if (response is String) {
        print(response.length)
    }

    if (text !is String) return
    print(text.length)

    if (text !is String || text.length == 0) return

    if (text is String && text.length > 0) {
        print(text.length)
    }

    when (response) {
        is String -> print(text.length + 1)
        is Int -> print(text)
    }

    // Jawne rzutowanie można przeprowadzić za pomocą operatora as. Jeśli rzutowanie nie będzie możliwe, operacja zakończy się wyjątkiem.

    val chatMessage: Any = "Some text"
    val valueText = text as String
    val message: String? = null
    message as? String // String?(null)
    // message as String // exception

    /*
      Pakiety i importowanie
      Plik źródłowy może rozpoczynać się od deklaracji przynależności do pakietu, tworzącego przestrzeni nazw dla elementów w nim zawartych.
      Jeśli pakiet nie zostanie zdefiniowany elementy przynależą do default package.
      Nazwa pakietu nie musi odpowiadać fizycznej strukturze katalogów na dysku (chociaż jest to zalecane).
      Każdy plik może zawierać zestaw importów, umieszczanych po definicji pakietu.
      Domyślnie importowane są m.in. kotlin.*, kotlin.annotation.*, kotlin.collections.*, java.lang.
      Istnieje możliwość importowania pojedynczych elementów, jak i całej zawartości pakietu (z wyłączeniem elementów prywatnych).
      W przypadku kolizji nazewniczej istnieje możliwość zastosowania aliasu.

      package org.example

      import pl.training.Message
      import pl.training.domain.Message as Event
      import pl.training.services.*
     */

}
