package com.javaaidev.mcp.amap.tool

import com.javaaidev.mcp.amap.api.ApiRequest
import io.github.smiley4.schemakenerator.core.annotations.Description
import kotlinx.serialization.Serializable

@Serializable
data class IpRequest(
    @Description("需要搜索的 IP 地址（仅支持国内）。若用户不填写 IP，则取客户 http 之中的请求来进行定位")
    val ip: String
) : ApiRequest {
    override fun apiSubPaths() = listOf("v3", "ip")

    override fun params(): Map<String, Any?> {
        return mapOf("ip" to ip)
    }
}