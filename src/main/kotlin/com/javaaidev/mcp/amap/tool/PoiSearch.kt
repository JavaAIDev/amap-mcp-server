package com.javaaidev.mcp.amap.tool

import com.javaaidev.mcp.amap.api.ApiRequest
import io.github.smiley4.schemakenerator.core.annotations.Description
import kotlinx.serialization.Serializable

@Serializable
data class PoiTextSearchRequest(
    @Description(
        """
        需要被检索的地点文本信息。
        只支持一个关键字 ，文本总长度不可超过80字符
    """
    )
    val keywords: String,
    @Description(
        """
        搜索区划。
        增加指定区域内数据召回权重，如需严格限制召回数据在区域内，请搭配使用 city_limit 参数，可输入 citycode，adcode，cityname；cityname 仅支持城市级别和中文，如“北京市”。
    """
    )
    val region: String? = null,
    @Description(
        """
        指定城市数据召回限制。
        可选值：true/false
        为 true 时，仅召回 region 对应区域内数据。
    """
    )
    val cityLimit: Boolean? = false,
    @Description(
        """
        当前分页展示的数据条数。
        page_size 的取值1-25
    """
    )
    val pageSize: Int? = 10,
    @Description(
        """
        请求第几分页
    """
    )
    val pageNum: Int? = 1,
) : ApiRequest {
    override fun apiSubPaths() = listOf("v5", "place", "text")

    override fun params(): Map<String, Any?> {
        return mapOf(
            "keywords" to keywords,
            "region" to region,
            "city_limit" to (cityLimit ?: true),
            "page_size" to (pageSize ?: 10),
            "page_num" to (pageNum ?: 1),
        )
    }
}

@Serializable
data class PoiAroundSearchRequest(
    @Description(
        """
        需要被检索的地点文本信息。
        只支持一个关键字 ，文本总长度不可超过80字符
    """
    )
    val keywords: String,
    @Description(
        """
            中心点坐标。
            圆形区域检索中心点
        """
    )
    val location: GeoLocation? = null,
    @Description(
        """
        搜索半径。
        取值范围:0-50000，大于50000时按默认值，单位：米
    """
    )
    val radius: Int? = 5000,
    @Description(
        """
        排序规则。
        规定返回结果的排序规则。
        按距离排序：distance；综合排序：weight
    """
    )
    val sortrule: String? = "distance",
    @Description(
        """
        搜索区划。
        增加指定区域内数据召回权重，如需严格限制召回数据在区域内，请搭配使用 city_limit 参数，可输入行政区划名或对应 citycode 或 adcode
    """
    )
    val region: String? = null,
    @Description(
        """
        指定城市数据召回限制。
        可选值：true/false
        为 true 时，仅召回 region 对应区域内数据。
    """
    )
    val cityLimit: Boolean? = false,
    @Description(
        """
        当前分页展示的数据条数。
        page_size 的取值1-25
    """
    )
    val pageSize: Int? = 10,
    @Description(
        """
        请求第几分页
    """
    )
    val pageNum: Int? = 1,
) : ApiRequest {
    override fun apiSubPaths() = listOf("v5", "place", "around")

    override fun params(): Map<String, Any?> {
        return mapOf(
            "keywords" to keywords,
            "location" to location,
            "radius" to (radius ?: 5000),
            "sortrule" to (sortrule ?: "distance"),
            "region" to region,
            "city_limit" to (cityLimit ?: true),
            "page_size" to (pageSize ?: 10),
            "page_num" to (pageNum ?: 1),
        )
    }
}

@Serializable
data class PoiPolygonSearchRequest(
    @Description(
        """
        多边形区域。
        多个坐标对集合。多边形为矩形时，可传入左上右下两顶点坐标对；其他情况下首尾坐标对需相同。
    """
    )
    val polygon: List<GeoLocation>,
    @Description(
        """
        需要被检索的地点文本信息。
        只支持一个关键字 ，文本总长度不可超过80字符
    """
    )
    val keywords: String,
    @Description(
        """
        当前分页展示的数据条数。
        page_size 的取值1-25
    """
    )
    val pageSize: Int? = 10,
    @Description(
        """
        请求第几分页
    """
    )
    val pageNum: Int? = 1,
) : ApiRequest {
    override fun apiSubPaths() = listOf("v5", "place", "polygon")

    override fun params(): Map<String, Any?> {
        return mapOf(
            "polygon" to polygon.joinToString("|") { it.toParamValue() },
            "keywords" to keywords,
            "page_size" to (pageSize ?: 10),
            "page_num" to (pageNum ?: 1),
        )
    }
}

@Serializable
data class PoiDetailSearchRequest(
    @Description(
        """
        poi唯一标识。
        最多可以传入10个 id。
    """
    )
    val id: List<String>,
) : ApiRequest {
    override fun apiSubPaths() = listOf("v5", "place", "detail")

    override fun params(): Map<String, Any?> {
        return mapOf(
            "id" to id.joinToString("|"),
        )
    }
}