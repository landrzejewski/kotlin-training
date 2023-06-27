package pl.training

import kotlinx.coroutines.*
import kotlinx.coroutines.CoroutineStart.LAZY
import org.junit.jupiter.api.Test
import kotlin.concurrent.thread

class CoroutinesTest {

    @Test
    fun singleThreadRoutines() {
        println("Test started (${threadName()})")
        routine(1, 500)
        routine(2, 200)
        println("Test finished (${threadName()})")
    }

    @Test
    fun multiThreadRoutines() {
        println("Test started (${threadName()})")
        newThreadRoutine(1, 500)
        newThreadRoutine(2, 200)
        println("Test finished (${threadName()})")
        Thread.sleep(1_000)
    }

    @Test
    fun lotsOfThreadsProblem() {
        repeat(1_000_000) {
            thread {
                Thread.sleep(100_000)
                println("Done")
            }
        }
    }

    @Test
    fun coroutines() = runBlocking {
        println("Test started (${threadName()})")
        launch { coroutine(1, 500) }
        launch { coroutine(2, 200) }
        println("Test finished (${threadName()})")
    }

    @Test
    fun jobs() {
        println("Test started (${threadName()})")
        GlobalScope.launch {
            println("First task started (${threadName()})")
            Thread.sleep(500)
            println("First task finished (${threadName()})")
        }
        val secondJob = GlobalScope.launch(start = LAZY) {
            println("Second task started (${threadName()})")
            Thread.sleep(200)
            println("Second task finished (${threadName()})")
        }
        // secondJob.start()
        val thirdJob = GlobalScope.launch {
            println("Third task started (${threadName()})")
            secondJob.join()
            println("Third task finished (${threadName()})")
        }
        Thread.sleep(1_000)
        println("Test finished (${threadName()})")
    }

    @Test
    fun hierarchicalJobs() {
        val parentJob = GlobalScope.launch {
            delay(200)
            println("Parent job has finished (${threadName()})")
        }
        GlobalScope.launch(parentJob) {
            delay(1_000)
            println("Child job has finished (${threadName()})")
        }
        if (parentJob.children.iterator().hasNext()) {
            println("Has children ${parentJob.children.first()}")
        }
        Thread.sleep(500)
        println("Is parent job finished: ${parentJob.isCompleted}")
        Thread.sleep(10_000)
        println("Is parent job finished: ${parentJob.isCompleted}")
        println("Test has finished (${threadName()})")
    }

    @Test
    fun sharedState() {
        var isDataLoaded = false
        GlobalScope.launch {
            delay(1_000)
            isDataLoaded = true
            println("Data loading has finished (${threadName()})")
        }
        GlobalScope.launch {
            repeat(3) {
                println("Data loading status (${threadName()}): $isDataLoaded")
                delay(500)
            }
        }
        Thread.sleep(10_000)
        println("Test has finished (${threadName()})")
    }

    @Test
    fun coroutineWithResult() = runBlocking {
        val deferredResult = async(Dispatchers.IO) { getData() }
        val secondDeferredResult = getDataDeferred()
        println("Finished: ${deferredResult.await()}, ${secondDeferredResult.await()}")
    }

    @Test
    fun cancelingCoroutine() = runBlocking {
        println("Test started (${threadName()})")
        val job = launch {
            if (isActive) {
                println("First job starts (${threadName()})")
                try {
                    delay(10_000)
                } catch (exception: CancellationException) {
                    println("CancellationException")
                    throw exception
                }
                println("First job has finished (${threadName()})")
            }
        }
        delay(1_000)
        job.cancel()
        println("Test has finished (${threadName()})")
    }

    @Test
    fun cancelingHierarchicalJobs() = runBlocking {
        val parentJob = launch {
            delay(10_000)
            println("Parent job has finished (${threadName()})")
        }
        parentJob.invokeOnCompletion {
            if (it is CancellationException) {
                println("Parent was cancelled")
            }
        }
        val childJob = launch(parentJob) {
            delay(1_000)
            println("Child job has finished (${threadName()})")
        }
        childJob.invokeOnCompletion {
            if (it is CancellationException) {
                println("Child was cancelled")
            }
        }
        delay(500)
         childJob.cancel()
        //parentJob.cancel()
        delay(10_000)
        println("Main has finished (${threadName()})")
    }

    @Test
    fun exceptionPropagationInHierarchicalJobs() = runBlocking {
        // supervisorScope {
        val parentJob = launch {
            delay(10_000)
            println("Parent job has finished (${threadName()})")
        }
        parentJob.invokeOnCompletion {
            println("Parent isActive: $isActive ($it)")
        }
        val childJob = launch(parentJob) {
            delay(1_000)
            throw RuntimeException()
        }
        childJob.invokeOnCompletion {
            println("Child isActive: $isActive ($it)")
        }
        val secondChildJob = launch(parentJob) {
            delay(1_000)
        }
        secondChildJob.invokeOnCompletion {
            println("Second child isActive: $isActive ($it)")
        }
        delay(500)
        println("Main has finished (${threadName()})")
        //  }
    }

    private suspend fun getData(): String {
        delay(5_000)
        return "Data"
    }

    private suspend fun getDataDeferred(): Deferred<String> =  GlobalScope.async {
        delay(5_000)
        "Data"
    }

    fun routine(number: Int, milliseconds: Long) {
        println("Routine $number started (${threadName()})")
        Thread.sleep(milliseconds)
        println("Routine $number finished (${threadName()})")
    }

    suspend fun coroutine(number: Int, milliseconds: Long) {
        println("Routine $number started (${threadName()})")
        delay(milliseconds)
        println("Routine $number finished (${threadName()})")
    }

    fun newThreadRoutine(number: Int, delay: Long) = thread { routine(number,delay) }

    fun threadName() = Thread.currentThread().name

}
