package com.javaaidev.mcp.amap

import io.modelcontextprotocol.kotlin.sdk.CallToolRequest
import io.modelcontextprotocol.kotlin.sdk.Implementation
import io.modelcontextprotocol.kotlin.sdk.TextContent
import io.modelcontextprotocol.kotlin.sdk.client.Client
import io.modelcontextprotocol.kotlin.sdk.client.StdioClientTransport
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.io.asSink
import kotlinx.io.asSource
import kotlinx.io.buffered
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.fail
import kotlin.time.Duration.Companion.minutes

class McpClientTest {
    @Test
    fun testRunClient() {
        runBlocking {
            try {
                withTimeout(1.minutes) {
                    val process =
                        ProcessBuilder("java", "-jar", "build/libs/amap-mcp-server-0.8.0-all.jar")
                            .start()

                    val transport = StdioClientTransport(
                        input = process.inputStream.asSource().buffered(),
                        output = process.outputStream.asSink().buffered()
                    )

                    val client = Client(
                        clientInfo = Implementation(name = "amap-client", version = "1.0.0"),
                    )

                    client.connect(transport)


                    val toolsList = client.listTools()?.tools?.map { it.name }
                    println("Available Tools = $toolsList")

                    val staticMapResult = client.callTool(
                        CallToolRequest(
                            name = "generateStaticMap",
                            arguments = JsonObject(
                                mapOf(
                                    "location" to JsonObject(
                                        mapOf(
                                            "lat" to JsonPrimitive("39.990464"),
                                            "lng" to JsonPrimitive("116.481485"),
                                        )
                                    )
                                )
                            )
                        )
                    )?.content?.map { if (it is TextContent) it.text else it.toString() }

                    println(
                        "Static map: ${
                            staticMapResult?.joinToString(
                                separator = "\n",
                                prefix = "\n",
                                postfix = "\n"
                            )
                        }"
                    )

                    client.close()
                }
            } catch (e: TimeoutCancellationException) {
                fail("timeout ${e.message}")
            }
        }
    }
}
