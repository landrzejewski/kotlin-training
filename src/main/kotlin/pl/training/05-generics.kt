package pl.training.examples

class Box<V>(val value: V)

interface Converter<S, T> {

    fun convert(source: S): T

}

class DoubleToInt : Converter<Double, Int> {

    override fun convert(source: Double): Int = source.toInt()

}

class IntToDouble : Converter<Int, Double> {

    override fun convert(source: Int): Double = source.toDouble()

}

class StringToBoolean : Converter<String, Boolean> {

    override fun convert(source: String): Boolean = source.toBoolean()

}

val converters = mapOf(
    "doubleToInt" to DoubleToInt(),
    "intToDouble" to IntToDouble(),
    "StringToBoolean" to StringToBoolean()
)

open class Vehicle
open class Car : Vehicle()
class RaceCar : Car()

class Garage</*out*/ T : Vehicle> {

    private val vehicles = mutableListOf<T>()

   // for T - invarinat - can take and return T
   // for out produces T, covariant - can return T but can't take T
   // for in consumes T, contravariant - can take T but can't return T

    fun getLast(): T = vehicles.last()

    fun add(vehicle: T) = vehicles.add(vehicle)

}

fun drive(garage: Garage<Car>) {
    val vehicle = garage.getLast()
}

fun main() {
    Box(2)
    Box("Test")

    val converter = converters["doubleToInt"] as? Converter<Double, Int>
    converter?.let {
        it.convert(2.0)
    }

    // for T - can pass exact type, can't pass subtype and super type
    // for out - can pass exact type, subtype, can't pass supertype
    // for in - can pass exact type, supertype, can't pass subtype

    /*drive(Garage<RaceCar>())
    drive(Garage<Car>())
    drive(Garage<Vehicle>())*/

}
