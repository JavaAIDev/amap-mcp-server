package com.javaaidev.mcp.amap.tool

import com.javaaidev.mcp.amap.api.ApiRequest
import io.github.smiley4.schemakenerator.core.annotations.Description
import kotlinx.serialization.Serializable

@Serializable
data class DrivingRoutePlanRequest(
    @Description(
        """
        起点经纬度
    """
    )
    val origin: GeoLocation,
    @Description(
        """
        目的地
    """
    )
    val destination: GeoLocation,
    @Description(
        """
            终点的 poi 类别
        """
    )
    val destinationType: String? = null,
    @Description(
        """
        起点 POI ID。
        起点为 POI 时，建议填充此值，可提升路线规划准确性
    """
    )
    val originId: String? = null,
    @Description(
        """
        目的地 POI ID。
        目的地为 POI 时，建议填充此值，可提升路径规划准确性
    """
    )
    val destinationId: String? = null,
    @Description(
        """
        驾车算路策略。
        0：速度优先（只返回一条路线），此路线不一定距离最短
        1：费用优先（只返回一条路线），不走收费路段，且耗时最少的路线      
        2：常规最快（只返回一条路线）综合距离/耗时规划结果        
        32：默认，高德推荐，同高德地图APP默认        
        33：躲避拥堵        
        34：高速优先       
        35：不走高速        
        36：少收费       
        37：大路优先       
        38：速度最快   
        39：躲避拥堵＋高速优先
        40：躲避拥堵＋不走高速
        41：躲避拥堵＋少收费
        42：少收费＋不走高速
        43：躲避拥堵＋少收费＋不走高速
        44：躲避拥堵＋大路优先
        45：躲避拥堵＋速度最快
    """
    )
    val strategy: Int? = 32,
    @Description(
        """
        途经点。
        途径点坐标串，默认支持1个有序途径点。最大支持16个途经点。
    """
    )
    val waypoints: List<GeoLocation>? = null,
    @Description(
        """
        区域避让，默认支持1个避让区域，每个区域最多可有16个顶点。
        如果是四边形则有四个坐标点，如果是五边形则有五个坐标点；最大支持32个避让区域。
        每个避让区域不能超过81平方公里，否则避让区域会失效。
    """
    )
    val avoidPolygons: List<GeoLocation>? = null,
    @Description(
        """
        车牌号，如 京AHA322，支持6位传统车牌和7位新能源车牌，用于判断限行相关。
    """
    )
    val plate: String? = null,
    @Description(
        """
        车辆类型。
        0：普通燃油汽车
        1：纯电动汽车
        2：插电式混动汽车
    """
    )
    val carType: Int? = 0,
    @Description(
        """
        是否使用轮渡。
        0:使用渡轮
        1:不使用渡轮 
    """
    )
    val ferry: Int? = 0,
) : ApiRequest {
    override fun apiSubPaths() = listOf("v5", "direction", "driving")

    override fun params(): Map<String, Any?> {
        return mapOf(
            "origin" to origin,
            "destination" to destination,
            "destination_type" to destinationType,
            "origin_id" to originId,
            "destination_id" to destinationId,
            "strategy" to (strategy ?: 32),
            "waypoints" to (waypoints?.joinToString(";") { it.toParamValue() }),
            "avoidpolygons" to (avoidPolygons?.joinToString("|") { it.toParamValue() }),
            "plate" to plate,
            "cartype" to (carType ?: 0),
            "ferry" to (ferry ?: 0),
        )
    }
}

@Serializable
data class WalkingRoutePlanRequest(
    @Description(
        """
        起点信息
    """
    )
    val origin: GeoLocation,
    @Description(
        """
        目的地信息
    """
    )
    val destination: GeoLocation,
    @Description(
        """
            终点的 poi 类别
        """
    )
    val destinationType: String? = null,
    @Description(
        """
        起点 POI ID。
        起点为 POI 时，建议填充此值，可提升路线规划准确性
    """
    )
    val originId: String? = null,
    @Description(
        """
        目的地 POI ID。
        目的地为 POI 时，建议填充此值，可提升路径规划准确性
    """
    )
    val destinationId: String? = null,
    @Description(
        """
        返回路线条数。
        1：多备选路线中第一条路线
        2：多备选路线中前两条路线
        3：多备选路线中三条路线
        不传则默认返回一条路线方案
    """
    )
    val alternativeRoute: Int? = 1,
    @Description(
        """
        是否需要室内算路。
        0：不需要
        1：需要
    """
    )
    val isIndoor: Int? = 0,
) : ApiRequest {
    override fun apiSubPaths() = listOf("v5", "direction", "walking")

    override fun params(): Map<String, Any?> {
        return mapOf(
            "origin" to origin,
            "destination" to destination,
            "destination_type" to destinationType,
            "origin_id" to originId,
            "destination_id" to destinationId,
            "alternative_route" to (alternativeRoute ?: 1),
            "isindoor" to (isIndoor ?: 0),
        )
    }
}

@Serializable
data class BicyclingRoutePlanRequest(
    @Description(
        """
        起点经纬度
    """
    )
    val origin: GeoLocation,
    @Description(
        """
        目的地
    """
    )
    val destination: GeoLocation,
    @Description(
        """
        返回路线条数。
        1：多备选路线中第一条路线
        2：多备选路线中前两条路线
        3：多备选路线中三条路线
        不传则默认返回一条路线方案
    """
    )
    val alternativeRoute: Int? = 1,
) : ApiRequest {
    override fun apiSubPaths() = listOf("v5", "direction", "bicycling")

    override fun params(): Map<String, Any?> {
        return mapOf(
            "origin" to origin,
            "destination" to destination,
            "alternative_route" to (alternativeRoute ?: 1),
        )
    }
}

@Serializable
data class ElectrobikeRoutePlanRequest(
    @Description(
        """
        起点经纬度
    """
    )
    val origin: GeoLocation,
    @Description(
        """
        目的地
    """
    )
    val destination: GeoLocation,
    @Description(
        """
        返回路线条数。
        1：多备选路线中第一条路线
        2：多备选路线中前两条路线
        3：多备选路线中三条路线
        不传则默认返回一条路线方案
    """
    )
    val alternativeRoute: Int? = 1,
) : ApiRequest {
    override fun apiSubPaths() = listOf("v5", "direction", "electrobike")

    override fun params(): Map<String, Any?> {
        return mapOf(
            "origin" to origin,
            "destination" to destination,
            "alternative_route" to (alternativeRoute ?: 1),
        )
    }
}

@Serializable
data class BusRoutePlanRequest(
    @Description(
        """
        起点经纬度
    """
    )
    val origin: GeoLocation,
    @Description(
        """
        目的地经纬度
    """
    )
    val destination: GeoLocation,
    @Description(
        """
        起点 POI ID。
        1、起点 POI ID 与起点经纬度均填写时，服务使用起点 POI ID；
        2、该字段必须和目的地 POI ID 成组使用。
    """
    )
    val originPoi: String? = null,
    @Description(
        """
        目的地 POI ID。
        1、目的地 POI ID 与目的地经纬度均填写时，服务使用目的地 POI ID；
        2、该字段必须和起点 POI ID 成组使用。
    """
    )
    val destinationPoi: String? = null,
    @Description(
        """
        起点所在行政区域编码。
        仅支持 adcode。
    """
    )
    val ad1: String? = null,
    @Description(
        """
        终点所在行政区域编码。
        仅支持 adcode。
    """
    )
    val ad2: String? = null,
    @Description(
        """
        起点所在城市。
        仅支持 citycode，相同时代表同城，不同时代表跨城。
    """
    )
    val city1: String? = null,
    @Description(
        """  	
        目的地所在城市。
        仅支持 citycode，相同时代表同城，不同时代表跨城。
    """
    )
    val city2: String? = null,
    @Description(
        """
        公共交通换乘策略。
        0：推荐模式，综合权重，同高德APP默认        
        1：最经济模式，票价最低        
        2：最少换乘模式，换乘次数少        
        3：最少步行模式，尽可能减少步行距离        
        4：最舒适模式，尽可能乘坐空调车        
        5：不乘地铁模式，不乘坐地铁路线       
        6：地铁图模式，起终点都是地铁站（地铁图模式下 originpoi 及 destinationpoi 为必填项）
        7：地铁优先模式，步行距离不超过4KM
        8：时间短模式，方案花费总时间最少
    """
    )
    val strategy: Int? = 0,
    @Description(
        """
        返回方案条数。
        可传入1-10的阿拉伯数字，代表返回的不同条数。 
    """
    )
    val alternativeRoute: Int? = 5,
    @Description(
        """
        地铁出入口数量。
        0：只返回一个地铁出入口
        1：返回全部地铁出入口
    """
    )
    val multiExport: Int? = 0,
    @Description(
        """
        考虑夜班车。
        可选值：
        0：不考虑夜班车
        1：考虑夜班车
    """
    )
    val nightFlag: Int? = 0,
    @Description(
        """
        请求日期。例如:2013-10-28
    """
    )
    val date: String? = null,
    @Description(
        """
        请求时间。例如:9-54
    """
    )
    val time: String? = null,
) : ApiRequest {
    override fun apiSubPaths() = listOf("v5", "direction", "transit", "integrated")

    override fun params(): Map<String, Any?> {
        return mapOf(
            "origin" to origin,
            "destination" to destination,
            "originpoi" to originPoi,
            "destinationpoi" to destinationPoi,
            "ad1" to ad1,
            "ad2" to ad2,
            "city1" to city1,
            "city2" to city2,
            "strategy" to (strategy ?: 0),
            "AlternativeRoute" to (alternativeRoute ?: 5),
            "multiexport" to (multiExport ?: 0),
            "nightflag" to (nightFlag ?: 0),
            "date" to date,
            "time" to time,
        )
    }
}