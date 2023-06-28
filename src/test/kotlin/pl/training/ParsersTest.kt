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

})

