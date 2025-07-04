package com.javaaidev.mcp.amap.tool

import com.javaaidev.mcp.amap.api.getApiKey
import io.github.smiley4.schemakenerator.core.annotations.Description
import io.ktor.http.*
import io.ktor.server.util.*
import kotlinx.serialization.Serializable

@Serializable
data class GeoLocation(
    @Description("维度，小数点后不得超过6位")
    val lat: Double,
    @Description("经度，小数点后不得超过6位")
    val lng: Double
) : ParamValue {
    override fun toParamValue(): String {
        return "$lng,$lat"
    }
}

@Serializable
data class MapSize(
    @Description("宽度")
    val width: Int,
    @Description("高度")
    val height: Int
) : ParamValue {
    override fun toParamValue(): String {
        return "$width*$height"
    }

    companion object {
        val DEFAULT = MapSize(400, 400)
        val MAXIMUM = MapSize(1024, 1024)
    }
}

@Serializable
enum class MapScaleMode(val value: String) : ParamValue {
    NORMAL("1"),
    HIGH_RES("2"),
    ;

    override fun toParamValue(): String {
        return value
    }

}

@Serializable
enum class TrafficMode(val value: String) : ParamValue {
    ON("1"),
    OFF("0"),
    ;

    override fun toParamValue(): String {
        return value
    }
}

@Serializable
enum class MarkerSize(val value: String) : ParamValue {
    SMALL("small"),
    MEDIUM("mid"),
    LARGE("large"),
    ;

    override fun toParamValue(): String {
        return value
    }
}

interface Style : ParamValue

@Serializable
data class MarkerStyle(
    @Description("可选值：small,mid,large")
    val size: MarkerSize,
    @Description("[0-9]、[A-Z]、[单个中文字]")
    val label: String? = null,
    @Description("选值范围：[0x000000, 0xffffff]")
    val color: String = "0xFC6054",
) : Style {
    override fun toParamValue(): String {
        return ParamValueUtils.build(
            listOf(
                size, color, label?.trim()?.substring(0, 1)
            )
        )
    }

}

interface Group<S : Style> : ParamValue {
    val style: S

    val locations: List<GeoLocation>

    override fun toParamValue(): String {
        return style.toParamValue() + ":" + locations.joinToString(";") { it.toParamValue() }
    }
}

@Serializable
data class MarkersGroup(
    override val locations: List<GeoLocation>,
    override val style: MarkerStyle = MarkerStyle(MarkerSize.SMALL),
) : Group<MarkerStyle>

@Serializable
data class Markers(val markersGroups: List<MarkersGroup> = listOf()) : ParamValue {
    override fun toParamValue(): String {
        return markersGroups.joinToString("|") { it.toParamValue() }
    }
}

@Serializable
data class PathStyle(
    @Description("线条粗细。可选值：[2,15]。")
    val weight: Int = 5,
    @Description("折线颜色。格式为0xRRGGBB。选值范围：[0x000000, 0xffffff]。")
    val color: String = "0x0000FF",
    @Description("透明度。可选值[0,1]，小数后最多2位，0表示完全透明，1表示完全不透明。")
    val transparency: Float = 1.0f,
    @Description("多边形的填充颜色，此值不为空时折线封闭成多边形。格式为0xRRGGBB。取值规则同color。")
    val fillcolor: String? = null,
    @Description("填充面透明度。可选值[0,1]，小数后最多2位，0表示完全透明，1表示完全不透明。")
    val fillTransparency: Float = 0.5f,
) : Style {
    override fun toParamValue(): String {
        return ParamValueUtils.build(listOf(weight, color, transparency, fillcolor, fillcolor))
    }
}

@Serializable
data class PathsGroup(
    override val locations: List<GeoLocation>,
    override val style: PathStyle = PathStyle(),
) : Group<PathStyle>

@Serializable
data class Paths(val pathGroups: List<PathsGroup> = listOf()) : ParamValue {
    override fun toParamValue(): String {
        return pathGroups.joinToString("|") { it.toParamValue() }
    }
}

@Serializable
enum class BoldMode(val value: String) : ParamValue {
    OFF("0"),
    ON("1"),
    ;

    override fun toParamValue(): String {
        return value
    }
}

@Serializable
enum class LabelFont(val value: String) : ParamValue {
    MS_YAHEI("0"),
    SONG_TI("1"),
    TIMES_NEW_ROMAN("2"),
    HELVETICA("3"),
    ;

    override fun toParamValue(): String {
        return value
    }
}

@Serializable
data class LabelStyle(
    @Description("标签内容，字符最大数目为15")
    val content: String? = null,
    @Description(
        """
        字体。
        0：微软雅黑；
        1：宋体；
        2：Times New Roman;
        3：Helvetica
    """
    )
    val font: LabelFont = LabelFont.MS_YAHEI,
    @Description("0：非粗体；1：粗体")
    val bold: BoldMode = BoldMode.OFF,
    @Description("字体大小，可选值[1,72]")
    val fontSize: Int = 10,
    @Description("字体颜色，格式为0xRRGGBB。取值范围：[0x000000, 0xffffff]")
    val fontColor: String = "0xFFFFFF",
    @Description("背景色，格式为0xRRGGBB。取值范围：[0x000000, 0xffffff]")
    val background: String = "0x5288d8",
) : Style {
    override fun toParamValue(): String {
        return ParamValueUtils.build(
            listOf(
                content?.trim()?.replace(" ", "")?.take(15),
                font,
                bold,
                fontSize.coerceAtMost(72).coerceAtLeast(1),
                fontColor,
                background
            )
        )
    }

}

@Serializable
data class LabelsGroup(
    override val locations: List<GeoLocation>,
    override val style: LabelStyle = LabelStyle(),
) : Group<LabelStyle>

@Serializable
data class Labels(val labelsGroups: List<LabelsGroup> = listOf()) : ParamValue {
    override fun toParamValue(): String {
        return labelsGroups.joinToString("|") { it.toParamValue() }
    }
}

@Serializable
data class StaticMapRequest(
    @Description("中心点坐标")
    val location: GeoLocation,
    @Description("地图缩放级别:[1,17]")
    val zoom: Int? = 10,
    @Description("地图大小")
    val size: MapSize? = MapSize.DEFAULT,
    @Description(
        """
        1:返回普通图；
        2:调用高清图，图片高度和宽度都增加一倍，zoom 也增加一倍（当zoom 为最大值时，zoom 不再改变）。
    """
    )
    val scale: MapScaleMode? = MapScaleMode.NORMAL,
    @Description("底图是否展现实时路况。 可选值： 0，不展现；1，展现。")
    val traffic: TrafficMode? = TrafficMode.OFF,
    @Description("标注")
    val markers: Markers? = null,
    @Description("标签")
    val labels: Labels? = null,
    @Description("折线")
    val paths: Paths? = null,
)

object StaticMap {
    fun generateUrl(request: StaticMapRequest): String {
        return url {
            protocol = URLProtocol.HTTPS
            host = "restapi.amap.com"
            appendPathSegments(listOf("v3", "staticmap"))
            parameters.append("key", getApiKey())
            mapOf(
                "location" to request.location,
                "zoom" to request.zoom,
                "size" to request.size,
                "scale" to request.scale,
                "traffic" to request.traffic,
                "markers" to request.markers,
                "labels" to request.labels,
                "paths" to request.paths,
            ).mapValues { ParamValueUtils.getParamValue(it.value) }
                .filterValues { it.isNotBlank() }
                .forEach { (key, value) ->
                    parameters.append(
                        key,
                        ParamValueUtils.getParamValue(value)
                    )
                }
        }
    }
}