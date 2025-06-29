package com.javaaidev.mcp.amap.tool

import com.javaaidev.mcp.amap.api.ApiRequest
import io.github.smiley4.schemakenerator.core.annotations.Description
import kotlinx.serialization.Serializable

@Serializable
data class GetWeatherRequest(
    @Description("输入城市的 adcode")
    val city: String,
    @Description(
        """
        可选值：base/all
        base:返回实况天气
        all:返回预报天气
    """
    )
    val extensions: String? = null,
) : ApiRequest {
    override fun apiSubPaths() = listOf("weather", "weatherInfo")

    override fun params(): Map<String, String> {
        return mapOf("city" to city, "extensions" to (extensions ?: ""))
    }
}