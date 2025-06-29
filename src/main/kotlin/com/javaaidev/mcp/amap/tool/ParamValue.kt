package com.javaaidev.mcp.amap.tool

interface ParamValue {
    fun toParamValue(): String
}

object ParamValueUtils {
    fun getParamValue(value: Any?): String {
        return if (value is ParamValue) value.toParamValue() else value?.toString() ?: ""
    }

    fun build(values: List<Any?>): String {
        return values.joinToString(",") { getParamValue(it) }
    }
}