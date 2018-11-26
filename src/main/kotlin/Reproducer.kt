import io.vertx.core.MultiMap
import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.client.HttpResponse
import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.coroutines.awaitResult
import io.vertx.kotlin.ext.web.client.WebClientOptions

suspend fun main(args: Array<String>) {
    val vertx = Vertx.vertx()

    val options = WebClientOptions(
        tryUseCompression = true,
        followRedirects = true,
        logActivity = false
    )

    val client = WebClient.create(vertx, options)

    val url = "http://skarg.net/vertx/redirect.php"
    val form = MultiMap.caseInsensitiveMultiMap().addAll(
        mapOf(
            "CSRF_TOKEN" to "4c1924751d8aafbaf93df5e8b5c368eb67c21634c7b147f1ea6783544a2c908cd1679c471bd9a65d6249942e8319fe80fa0cc4eea3ea9ff8bdb490e57890fc94",
            "login" to "",
            "password" to ""
        )
    )

    println("Sending request")

    val response = awaitResult<HttpResponse<Buffer>> {
        client.requestAbs(HttpMethod.POST, url).sendForm(form, it)
    }

    println("Response is: ${response.body()}")

    vertx.close()
}