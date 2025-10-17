package com.javaaidev.mcp.amap

import com.javaaidev.mcp.amap.api.ApiInvoker
import com.javaaidev.mcp.amap.api.fromToolParameters
import com.javaaidev.mcp.amap.api.getApiKey
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
import kotlin.system.exitProcess

object McpServer {
    fun start() {
        val server = Server(
            Implementation(
                name = "amap",
                version = "1.0.0"
            ),
            ServerOptions(
                capabilities = ServerCapabilities(
                    tools = ServerCapabilities.Tools(listChanged = true),
                    experimental = null,
                    logging = null,
                )
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
            name = "districtQuery",
            description = """
            行政区域查询是一类简单的 HTTP 接口，根据用户输入的搜索条件可以帮助用户快速的查找特定的行政区域信息。

            例如：中国>山东省>济南市>历下区>舜华路街道（国>省>市>区>街道）。
        """.trimIndent(),
            inputSchema = toToolInput<DistrictQueryRequest>()
        ) { request ->
            val params = fromToolParameters<DistrictQueryRequest>(request.arguments)
            val result = ApiInvoker.get(params)
            CallToolResult(content = listOf(TextContent(result)))
        }

        server.addTool(
            name = "poiSearchByText",
            description = """
            通过文本关键字搜索地点信息，文本可以是结构化地址，例如：北京市朝阳区望京阜荣街10号；也可以是 POI 名称，例如：首开广场；
        """.trimIndent(),
            inputSchema = toToolInput<PoiTextSearchRequest>()
        ) { request ->
            val params = fromToolParameters<PoiTextSearchRequest>(request.arguments)
            val result = ApiInvoker.get(params)
            CallToolResult(content = listOf(TextContent(result)))
        }

        server.addTool(
            name = "poiSearchAround",
            description = """
            设置圆心和半径，搜索圆形区域内的地点信息
        """.trimIndent(),
            inputSchema = toToolInput<PoiAroundSearchRequest>()
        ) { request ->
            val params = fromToolParameters<PoiAroundSearchRequest>(request.arguments)
            val result = ApiInvoker.get(params)
            CallToolResult(content = listOf(TextContent(result)))
        }

        server.addTool(
            name = "poiSearchByPolygon",
            description = """
            设置首尾连接的几何点组成多边形区域，搜索坐标对应多边形内的地点信息
        """.trimIndent(),
            inputSchema = toToolInput<PoiPolygonSearchRequest>()
        ) { request ->
            val params = fromToolParameters<PoiPolygonSearchRequest>(request.arguments)
            val result = ApiInvoker.get(params)
            CallToolResult(content = listOf(TextContent(result)))
        }

        server.addTool(
            name = "poiSearchById",
            description = """
            通过已知的地点 ID（POI ID）搜索对应地点信息
        """.trimIndent(),
            inputSchema = toToolInput<PoiDetailSearchRequest>()
        ) { request ->
            val params = fromToolParameters<PoiDetailSearchRequest>(request.arguments)
            val result = ApiInvoker.get(params)
            CallToolResult(content = listOf(TextContent(result)))
        }

        server.addTool(
            name = "inputTips",
            description = """
            根据用户输入的关键词查询返回建议列表
        """.trimIndent(),
            inputSchema = toToolInput<InputTipsRequest>()
        ) { request ->
            val params = fromToolParameters<InputTipsRequest>(request.arguments)
            val result = ApiInvoker.get(params)
            CallToolResult(content = listOf(TextContent(result)))
        }

        server.addTool(
            name = "coordinateCovert",
            description = """
            将用户输入的非高德坐标（GPS 坐标、mapbar 坐标、baidu 坐标）转换成高德坐标
        """.trimIndent(),
            inputSchema = toToolInput<CoordinateCovertRequest>()
        ) { request ->
            val params = fromToolParameters<CoordinateCovertRequest>(request.arguments)
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
            name = "drivingRoutePlan",
            description = """
            根据起终点坐标检索符合条件的驾车路线规划方案，支持一次请求返回多条路线结果、支持传入多个途经点、支持传入车牌规避限行、支持根据不同业务场景设置不同的算路策略等
        """.trimIndent(),
            inputSchema = toToolInput<DrivingRoutePlanRequest>()
        ) { request ->
            val params = fromToolParameters<DrivingRoutePlanRequest>(request.arguments)
            val result = ApiInvoker.get(params)
            CallToolResult(content = listOf(TextContent(result)))
        }

        server.addTool(
            name = "walkingRoutePlan",
            description = """
            根据起终点坐标检索符合条件的步行路线规划方案
        """.trimIndent(),
            inputSchema = toToolInput<WalkingRoutePlanRequest>()
        ) { request ->
            val params = fromToolParameters<WalkingRoutePlanRequest>(request.arguments)
            val result = ApiInvoker.get(params)
            CallToolResult(content = listOf(TextContent(result)))
        }

        server.addTool(
            name = "bicyclingRoutePlan",
            description = """
            根据起终点坐标检索符合条件的骑行路线规划方案
        """.trimIndent(),
            inputSchema = toToolInput<BicyclingRoutePlanRequest>()
        ) { request ->
            val params = fromToolParameters<BicyclingRoutePlanRequest>(request.arguments)
            val result = ApiInvoker.get(params)
            CallToolResult(content = listOf(TextContent(result)))
        }

        server.addTool(
            name = "electrobikeRoutePlan",
            description = """
            根据起终点坐标检索符合条件的电动车路线规划方案，与骑行略有不同的是会考虑限行等条件
        """.trimIndent(),
            inputSchema = toToolInput<ElectrobikeRoutePlanRequest>()
        ) { request ->
            val params = fromToolParameters<ElectrobikeRoutePlanRequest>(request.arguments)
            val result = ApiInvoker.get(params)
            CallToolResult(content = listOf(TextContent(result)))
        }

        server.addTool(
            name = "busRoutePlan",
            description = """
            根据起终点坐标检索符合条件的公共交通路线规划方案，支持结合业务场景设置不同的公交换乘策略
        """.trimIndent(),
            inputSchema = toToolInput<BusRoutePlanRequest>()
        ) { request ->
            val params = fromToolParameters<BusRoutePlanRequest>(request.arguments)
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
    try {
        getApiKey()
    } catch (e: Exception) {
        System.err.print(e.message)
        exitProcess(1)
    }

    McpServer.start()
}