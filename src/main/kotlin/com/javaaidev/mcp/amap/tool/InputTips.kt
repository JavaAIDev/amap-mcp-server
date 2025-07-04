package com.javaaidev.mcp.amap.tool

import com.javaaidev.mcp.amap.api.ApiRequest
import io.github.smiley4.schemakenerator.core.annotations.Description
import kotlinx.serialization.Serializable

@Serializable
data class InputTipsRequest(
    @Description(
        """
        查询关键词。
    """
    )
    val keywords: String,
    @Description(
        """
        POI 分类。
        服务可支持传入多个分类，多个类型间用“|”分隔
        可选值：POI 分类名称、分类代码
        此处强烈建议使用分类代码，否则可能会得到不符合预期的结果
    """
    )
    val type: String? = null,
    @Description(
        """
            坐标。
            建议使用 location 参数，可在此 location 附近优先返回搜索关键词信息
            在请求参数 city 不为空时生效
        """
    )
    val location: GeoLocation? = null,
    @Description(
        """
        搜索城市。
        可选值：citycode、adcode，不支持县级市。
        如：010/110000
        adcode 信息可参考 城市编码表 获取。
        填入此参数后，会尽量优先返回此城市数据，但是不一定仅局限此城市结果，若仅需要某个城市数据请调用 citylimit 参数。
        如：在深圳市搜天安门，返回北京天安门结果。
    """
    )
    val city: String? = null,
    @Description(
        """
        仅返回指定城市数据
    """
    )
    val cityLimit: Boolean? = false,
    @Description(
        """
        返回的数据类型。
        多种数据类型用“|”分隔，可选值：all-返回所有数据类型、poi-返回POI数据类型、bus-返回公交站点数据类型、busline-返回公交线路数据类型
    """
    )
    val dataType: String? = "all",
) : ApiRequest {
    override fun apiSubPaths() = listOf("v3", "assistant", "inputtips")

    override fun params(): Map<String, Any?> {
        return mapOf(
            "keywords" to keywords,
            "type" to type,
            "location" to location,
            "city" to city,
            "citylimit" to (cityLimit ?: false),
            "datatype" to (dataType ?: "all"),
        )
    }
}