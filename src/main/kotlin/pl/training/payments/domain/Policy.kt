package pl.training.payments.domain

fun interface Policy<R> {

    fun execute(): R

}
