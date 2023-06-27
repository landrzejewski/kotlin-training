package pl.training

data class MockHttpResponse(
    val path: String,
    val body: Any,
    val status: Int = 200,
    val description: String = "OK",
    val delayInMilliseconds: Long = 200,
    val isPersistent: Boolean = true
)
