# Amap MCP Server （高德地图 MCP 服务器）

MCP Server for [Amap](https://lbs.amap.com/)

使用 Kotlin 开发的高德地图 MCP 服务器

## Tools available

- [IP定位](https://lbs.amap.com/api/webservice/guide/api/ipconfig)
- [地理/逆地理编码](https://lbs.amap.com/api/webservice/guide/api/georegeo)
- [静态地图](https://lbs.amap.com/api/webservice/guide/api/staticmaps)
- [天气查询](https://lbs.amap.com/api/webservice/guide/api-advanced/weatherinfo)
- [行政区域查询](https://lbs.amap.com/api/webservice/guide/api/district)
- [搜索POI 2.0](https://lbs.amap.com/api/webservice/guide/api-advanced/newpoisearch)
- [路径规划 2.0](https://lbs.amap.com/api/webservice/guide/api/newroute)
- [坐标转换](https://lbs.amap.com/api/webservice/guide/api/convert)
- [输入提示](https://lbs.amap.com/api/webservice/guide/api-advanced/inputtips)

## How to use

Go to [releases](https://github.com/JavaAIDev/amap-mcp-server/releases) page and download latest releases:

- JAR file, requires Java 11 to run
- Native executable files on Windows, macOS and Linux.

Or use the [container image](https://github.com/JavaAIDev/amap-mcp-server/pkgs/container/amap-mcp-server).

Amap API key is required. This key must be set as the environment variable `AMAP_API_KEY`.

Below is the config to start the server using `java`.

```json
{
  "mcpServers": {
    "amap": {
      "command": "java",
      "args": [
        "-jar",
        "amap-mcp-server.jar"
      ],
      "env": {
        "AMAP_API_KEY": "YOUR_AMAP_API_KEY"
      }
    }
  }
}
```

The config below starts the server using a container image.

```json
{
  "mcpServers": {
    "amap": {
      "command": "docker",
      "args": [
        "run",
        "-i",
        "-e",
        "AMAP_API_KEY=YOUR_AMAP_API_KEY",
        "ghcr.io/javaaidev/amap-mcp-server:0.8.0"
      ]
    }
  }
}
```