package pl.training

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.lang.RuntimeException

class UserApiTest {

    private val usersApi = createMockApi(UsersApi::class.java,
        MockHttpResponse("users", listOf(1, 2)),
        MockHttpResponse("users/1", UserDto(1L, "Jan Kowalski", "jan@training.pl")),
        MockHttpResponse("users/2", UserDto(2L, "Marek Nowak", "marek@training.pl"))
    )
    private val brokenUsersApi = createMockApi(UsersApi::class.java,
        MockHttpResponse("users", listOf(1, 2), delayInMilliseconds = 5_000)
    )

    @Test
    fun singleRequest() {
        GlobalScope.launch {
            println("Ids: ${usersApi.getUsersIds()}")
        }
        Thread.sleep(1_000)
    }

    @Test
    fun manySerialRequests() {
        GlobalScope.launch {
            val ids = usersApi.getUsersIds()
            println("Ids: $ids}")
            ids.forEach { println(usersApi.getUserById(it)) }
        }
        Thread.sleep(1_000)
    }

    @Test
    fun manyConcurrentRequests() {
        GlobalScope.launch(Dispatchers.IO) {
            val ids = usersApi.getUsersIds()
            println("Ids: $ids}")
            ids.forEach {
                GlobalScope.launch { println(usersApi.getUserById(it)) }
            }
        }
        Thread.sleep(1_000)
    }

    @Test
    fun manyConcurrentRequestsWithFinalSync() {
        GlobalScope.launch(Dispatchers.IO) {
            val ids = usersApi.getUsersIds()
            println("Ids: $ids}")
            val results = ids.map { async { usersApi.getUserById(it) } }.toTypedArray()
            awaitAll(*results).forEach {  println(it) }
        }
        Thread.sleep(1_000)
    }

    @Test
    fun timeoutTest() {
        GlobalScope.launch {
            try {
                val ids = withTimeout(1_000) {
                    brokenUsersApi.getUsersIds()
                }
                println("Ids: $ids")
            } catch (exception: CancellationException) {
                println("Timeout")
            }
        }
        Thread.sleep(5_000)
    }

    @Test
    fun retryTest() = runBlocking {
        try {
            retry(3) { throw RuntimeException() }
        } catch (exception: Throwable) {
            println("Failed")
        }
    }

    suspend fun <T> retry(times: Int, maxDelayInMilliseconds: Long = 1_000, factor: Double = 2.0, task: suspend () -> T): T {
        var delayValue = 1L
        repeat(times) {
            try {
                return task()
            } catch (exception: Throwable) {
                println("Retry due to exception $exception")
            }
            delay(delayValue)
            delayValue = (delayValue * factor).toLong().coerceAtMost(maxDelayInMilliseconds)
        }
        return task()
    }

}
