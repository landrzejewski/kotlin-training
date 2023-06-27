package pl.training

enum class Player {

    CIRCLE, CROSS;

    fun reverse() = if (this == CROSS) CIRCLE else CROSS

}
