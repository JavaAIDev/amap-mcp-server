package com.javaaidev.mcp.amap.tool

import com.javaaidev.mcp.amap.api.ApiRequest
import io.github.smiley4.schemakenerator.core.annotations.Description
import kotlinx.serialization.Serializable

@Serializable
data class CoordinateCovertRequest(
    @Description(
        """
        坐标点。最多支持40对坐标
    """
    )
    val locations: List<GeoLocation>,
    @Description(
        """
        原坐标系。
        可选值：gps; mapbar; baidu; autonavi(不进行转换)
    """
    )
    val coordsys: String? = "autonavi",
) : ApiRequest {
    override fun apiSubPaths() = listOf("v3", "assistant", "coordinate", "convert")

    override fun params(): Map<String, Any?> {
        return mapOf(
            "locations" to locations.joinToString("|") { it.toParamValue() },
            "coordsys" to coordsys,
        )
    }
}