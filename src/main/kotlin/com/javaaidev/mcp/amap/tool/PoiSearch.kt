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

    override fun params(): Map<String, String> {
        return mapOf(
            "keywords" to keywords,
            "region" to (region ?: ""),
            "city_limit" to (cityLimit ?: true).toString(),
            "page_size" to (pageSize ?: 10).toString(),
            "page_num" to (pageNum ?: 1).toString(),
        )
    }
}