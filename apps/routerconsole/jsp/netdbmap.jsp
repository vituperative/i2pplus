<%@page contentType="text/html"%>
<%@page trimDirectiveWhitespaces="true"%>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<%
    net.i2p.I2PAppContext ctx = net.i2p.I2PAppContext.getGlobalContext();
    String lang = "en";
    if (ctx.getProperty("routerconsole.lang") != null)
        lang = ctx.getProperty("routerconsole.lang");
%>
<html lang="<%=lang%>">
<head>
<%@include file="css.jsi" %>
<%@include file="summaryajax.jsi" %>
<%=intl.title("network database router map")%>
<link href=/themes/geomap/geomap.css rel=stylesheet>
</head>
<body>
<%@include file="summary.jsi" %>
<h1 class=netwrk><%=intl._t("Network Database Router Map")%></h1>
<div class=main id=netdbRouterMap>
<div id=netdbmap>
<%@ include file="/viewnetdbmap.jsp" %>
<div id=netdbmapLegend>
<span id=legend_500><span class=legend_color></span>500</span>
<span id=legend_400><span class=legend_color></span>400</span>
<span id=legend_300><span class=legend_color></span>300</span>
<span id=legend_200><span class=legend_color></span>200</span>
<span id=legend_100><span class=legend_color></span>100</span>
<span id=legend_50><span class=legend_color></span>50</span>
<span id=legend_10><span class=legend_color></span>10</span>
<span id=legend_1><span class=legend_color></span>1</span>
</div>
</div>
</div>
</body>
</html>
