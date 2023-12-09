import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

val currentISODate: String
    get() = isoInstantFormat.format(System.currentTimeMillis())

val systemUserName: String
    get() = System.getProperty("user.name")

val systemOS: String
    get() = System.getProperty("os.name").lowercase()

val systemIP: String
    get() = URL("http://ipinfo.io/ip").readText()

val String.escapedVersion
    get() = this.replace(".", "").replace("_", "").replace("-", "")