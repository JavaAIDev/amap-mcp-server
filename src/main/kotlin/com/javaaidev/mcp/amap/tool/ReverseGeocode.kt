package com.javaaidev.mcp.amap.tool

import com.javaaidev.mcp.amap.api.ApiRequest
import io.github.smiley4.schemakenerator.core.annotations.Description
import kotlinx.serialization.Serializable

@Serializable
data class ReverseGeocodeRequest(
    @Description("经纬度坐标")
    val location: GeoLocation,
    @Description("搜索半径。取值范围：0~3000，默认值：1000。单位：米")
    val radius: String? = null,
    @Description(
        """
        返回结果控制
        extensions 参数默认取值是 base，也就是返回基本地址信息；
        extensions 参数取值为 all 时会返回基本地址信息、附近 POI 内容、道路信息以及道路交叉口信息。
    """
    )
    val extensions: String? = null,
    @Description(
        """
        道路等级
        以下内容需要 extensions 参数为 all 时才生效。
        可选值：0，1  当 roadlevel=0时，显示所有道路 ； 当 roadlevel=1时，过滤非主干道路，仅输出主干道路数据 
    """
    )
    val roadlevel: String? = null,
) : ApiRequest {
    override fun apiSubPaths() = listOf("geocode", "geo")

    override fun params(): Map<String, Any?> {
        return mapOf("address" to location, "radius" to radius)
    }
}