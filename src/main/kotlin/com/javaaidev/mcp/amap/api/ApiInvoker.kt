package com.javaaidev.mcp.amap.api

import com.javaaidev.mcp.amap.tool.ParamValueUtils
import io.github.smiley4.schemakenerator.core.CoreSteps.initial
import io.github.smiley4.schemakenerator.jsonschema.JsonSchemaSteps.compileInlining
import io.github.smiley4.schemakenerator.jsonschema.JsonSchemaSteps.generateJsonSchema
import io.github.smiley4.schemakenerator.jsonschema.JsonSchemaSteps.handleCoreAnnotations
import io.github.smiley4.schemakenerator.serialization.SerializationSteps.analyzeTypeUsingKotlinxSerialization
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.modelcontextprotocol.kotlin.sdk.Tool
import kotlinx.serialization.json.*

interface ApiRequest {
    fun apiSubPaths(): List<String>
    fun params(): Map<String, Any?>
}

val json = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
}

inline fun <reified T> toToolInput(): Tool.Input {
    val jsonString = initial<T>()
        .analyzeTypeUsingKotlinxSerialization()
        .generateJsonSchema()
        .handleCoreAnnotations()
        .compileInlining()
        .json.prettyPrint()
    val json = json.parseToJsonElement(jsonString).jsonObject
    return Tool.Input(
        json["properties"] as JsonObject,
        (json["required"] as? JsonArray)?.toList()?.map { it.jsonPrimitive.content })
}

inline fun <reified T> fromToolParameters(parameters: JsonObject): T {
    return json.decodeFromJsonElement<T>(parameters)
}

fun getApiKey() = System.getenv("AMAP_API_KEY") ?: throw RuntimeException("API key is required")

suspend fun HttpClient.amapApiGet(request: ApiRequest): String {
    try {
        return get {
            url {
                protocol = URLProtocol.HTTPS
                host = "restapi.amap.com"
                appendPathSegments(request.apiSubPaths())
                parameters.append("key", getApiKey())
                parameters.append("output", "JSON")
                request.params()
                    .mapValues { ParamValueUtils.getParamValue(it.value) }
                    .filterValues { it.isNotBlank() }
                    .forEach { (key, value) ->
                        parameters.append(key, value)
                    }
            }
            expectSuccess = false
        }.bodyAsText()
    } catch (e: ResponseException) {
        return e.response.bodyAsText()
    } catch (e: Exception) {
        return "Internal error: ${e.message}"
    }
}

object ApiInvoker {
    val httpClient = HttpClient {
        defaultRequest {
            headers {
                append(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/139.0.0.0 Safari/537.36"
                )
            }
            contentType(ContentType.Application.Json)
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }

    suspend fun get(request: ApiRequest): String {
        return httpClient.amapApiGet(request)
    }
}
