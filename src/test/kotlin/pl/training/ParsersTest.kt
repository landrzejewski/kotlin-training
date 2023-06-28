package pl.training

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import pl.training.Result.Failure
import pl.training.Result.Success

class ParsersTest : StringSpec({

    "should parse text to key value pair" {
        keyValuePair("firstName:Jan") shouldBe mapOf("firstName" to "Jan")
    }

    "should parse text to many key value pairs" {
        keyValuePair("firstName:Jan\nlastName:Kowalski") shouldBe mapOf(
            "firstName" to "Jan",
            "lastName" to "Kowalski"
        )
    }

    "should parse prefix" {
        val prefixParser = prefix("* ")
        prefixParser("* jan") shouldBe Success("* ", "jan")
        prefixParser("jan") shouldBe Failure("Expected: \"* \" prefix", "jan")
    }

    "should parse integer" {
        int("1234jan") shouldBe Success(1234, "jan")
        int("jan") shouldBe Failure("Expected: integer value", "jan")
    }

    "should parse whitespace" {
        whitespace("   jan") shouldBe Success("   ", "jan")
        whitespace("jan") shouldBe Failure("Expected: one or more whitespaces", "jan")
    }

    "should parse sequence" {
        //val sequenceParser = sequence(prefix("-"), ::int)
        val sequenceParser = prefix("-") then ::int
        sequenceParser("-123jan") shouldBe Success(Pair("-", 123), "jan")
        sequenceParser("123jan") shouldBe Failure("Expected: \"-\" prefix", "123jan")
    }

    "should parse one of" {
        //val onOfParser = oneOf(prefix("a"), prefix("b"))
        val onOfParser = prefix("a") or (prefix("b"))
        onOfParser("ab") shouldBe Success("a", "b")
        onOfParser("bc") shouldBe Success("b", "c")
        onOfParser("cd") shouldBe Failure("Expected: \"a\" prefix, Expected: \"b\" prefix", "cd")
    }

    "should map result" {
        int("11").map { it % 2 == 0 } shouldBe Success(false, "")
    }

    "should skip left parser" {
        val parser = ::int skipLeft (prefix("a"))
        parser("1a") shouldBe Success("a", "")
    }

    "should skip right parser" {
        val parser = prefix("a") skipRight (::int)
        parser("a1") shouldBe Success("a", "")
    }

    "should parse many" {
        val manyPrefixes = prefix("a").many()
        manyPrefixes("aaa") shouldBe Success(listOf("a", "a", "a"), "")
    }

    "should parse many with separator" {
        val manyWithSeparatorParser = ::int.separatedBy(prefix(","))
        manyWithSeparatorParser("1,2") shouldBe Success(listOf(1, 2), "")
        manyWithSeparatorParser("1,a") shouldBe Failure("Expected: integer value", "a")
        manyWithSeparatorParser("a") shouldBe Success(emptyList(), "a")
    }

    "should parse expression with array of ints" {
        val text = "let  ab = [1, 2, 3,  4]"

        val manySpaces = ::whitespace.many()
        val let = prefix("let") then ::whitespace.many()
        val variableName = prefixWhile { it.isLetter() } skipRight manySpaces
        val assigment = prefix("=") skipRight manySpaces
        val numbers = ::int separatedBy sequence(prefix(","), manySpaces)
        val array = prefix("[") skipLeft numbers skipRight prefix("]")
        val parser = let skipLeft variableName skipRight assigment then array

        when (val result = parser(text)) {
            is Success -> println(result.match)
            is Failure -> println(result.expected)
        }
    }

})

