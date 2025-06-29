package com.javaaidev.mcp.amap

import com.javaaidev.mcp.amap.api.ApiInvoker
import com.javaaidev.mcp.amap.api.fromToolParameters
import com.javaaidev.mcp.amap.api.toToolInput
import com.javaaidev.mcp.amap.tool.*
import io.ktor.utils.io.streams.*
import io.modelcontextprotocol.kotlin.sdk.CallToolResult
import io.modelcontextprotocol.kotlin.sdk.Implementation
import io.modelcontextprotocol.kotlin.sdk.ServerCapabilities
import io.modelcontextprotocol.kotlin.sdk.TextContent
import io.modelcontextprotocol.kotlin.sdk.server.Server
import io.modelcontextprotocol.kotlin.sdk.server.ServerOptions
import io.modelcontextprotocol.kotlin.sdk.server.StdioServerTransport
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlinx.io.asSink
import kotlinx.io.buffered

object McpServer {
    fun start() {
        val server = Server(
            Implementation(
                name = "amap",
                version = "1.0.0"
            ),
            ServerOptions(
                capabilities = ServerCapabilities(tools = ServerCapabilities.Tools(listChanged = true))
            )
        )

        server.addTool(
            name = "ip",
            description = """
            将 IP 信息转换为地理位置信息
        """.trimIndent(),
            inputSchema = toToolInput<IpRequest>()
        ) { request ->
            val params = fromToolParameters<IpRequest>(request.arguments)
            val result = ApiInvoker.get(params)
            CallToolResult(content = listOf(TextContent(result)))
        }

        server.addTool(
            name = "getWeather",
            description = """
            根据用户输入的 adcode，查询目标区域当前/未来的天气情况
        """.trimIndent(),
            inputSchema = toToolInput<GetWeatherRequest>()
        ) { request ->
            val params = fromToolParameters<GetWeatherRequest>(request.arguments)
            val result = ApiInvoker.get(params)
            CallToolResult(content = listOf(TextContent(result)))
        }

        server.addTool(
            name = "geocode",
            description = """
            将详细的结构化地址转换为高德经纬度坐标。且支持对地标性名胜景区、建筑物名称解析为高德经纬度坐标。 
            结构化地址举例：北京市朝阳区阜通东大街6号转换后经纬度：116.480881,39.989410。
            地标性建筑举例：天安门转换后经纬度：116.397499,39.908722。
        """.trimIndent(),
            inputSchema = toToolInput<GeocodeRequest>()
        ) { request ->
            val params = fromToolParameters<GeocodeRequest>(request.arguments)
            val result = ApiInvoker.get(params)
            CallToolResult(content = listOf(TextContent(result)))
        }

        server.addTool(
            name = "reverseGeocode",
            description = """
            将经纬度转换为详细结构化的地址，且返回附近周边的 POI、AOI 信息。
             例如：116.480881,39.989410 转换地址描述后：北京市朝阳区阜通东大街6号。
        """.trimIndent(),
            inputSchema = toToolInput<ReverseGeocodeRequest>()
        ) { request ->
            val params = fromToolParameters<ReverseGeocodeRequest>(request.arguments)
            val result = ApiInvoker.get(params)
            CallToolResult(content = listOf(TextContent(result)))
        }

        server.addTool(
            name = "generateStaticMap",
            description = """
            静态地图服务通过返回一张地图图片响应 HTTP 请求，使用户能够将高德地图以图片形式嵌入自己的网页中。用户可以指定请求的地图位置、图片大小、以及在地图上添加覆盖物，如标签、标注、折线、多边形。
        """.trimIndent(),
            inputSchema = toToolInput<StaticMapRequest>()
        ) { request ->
            val params = fromToolParameters<StaticMapRequest>(request.arguments)
            val result = StaticMap.generateUrl(params)
            CallToolResult(content = listOf(TextContent(result)))
        }

        val transport = StdioServerTransport(
            System.`in`.asInput(),
            System.out.asSink().buffered()
        )

        runBlocking {
            server.connect(transport)
            val done = Job()
            server.onClose {
                done.complete()
            }
            done.join()
        }
    }
}

fun main() {
    McpServer.start()
}