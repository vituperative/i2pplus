<%
/*
 * This file is part of SusiDNS project for I2P
 * Created on Sep 02, 2005
 * Copyright (C) 2005 <susi23@mail.i2p>
 * License: GPL2 or later
 */
%>
<%@page contentType="text/html" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" buffer="32kb" %>
<%@include file="headers.jsi"%>
<jsp:useBean id="version" class="i2p.susi.dns.VersionBean" scope="application"/>
<jsp:useBean id="cfg" class="i2p.susi.dns.ConfigBean" scope="session"/>
<jsp:useBean id="base" class="i2p.susi.dns.BaseBean" scope="session" />
<jsp:useBean id="intl" class="i2p.susi.dns.Messages" scope="application" />
<jsp:setProperty name="cfg" property="*" />
<%
    cfg.storeMethod(request.getMethod());
    boolean overrideCssActive = base.isOverrideCssActive();
    String theme = base.getTheme().replace("/themes/susidns/", "").replace("/", "");
    theme = "\"" + theme + "\"";
%>
<!DOCTYPE HTML>
<html>
<head>
<script src=/js/setupIframe.js></script>
<meta charset=utf-8>
<meta name=viewport content="width=device-width, initial-scale=1">
<title><%=intl._t("configuration")%> - susidns</title>
<link rel=stylesheet href="<%=base.getTheme()%>susidns.css?<%=net.i2p.CoreVersion.VERSION%>">
<%  if (base.useSoraFont()) { %><link href="<%=base.getTheme()%>../../fonts/Sora.css" rel=stylesheet><% } else { %>
<link href="<%=base.getTheme()%>../../fonts/OpenSans.css" rel=stylesheet><% } %>
<% if (overrideCssActive) { %><link rel=stylesheet href="<%=base.getTheme()%>override.css"><% } %>
<script nonce="<%=cspNonce%>">const theme = <%=theme%>;</script>
</head>
<body id=cfg style=display:none;pointer-events:none>
<div id=page>
<div id=navi>
<a class="abook router" href="addressbook?book=router&amp;filter=none"><%=intl._t("Router")%></a>&nbsp;
<a class="abook master" href="addressbook?book=master&amp;filter=none"><%=intl._t("Master")%></a>&nbsp;
<a class="abook private" href="addressbook?book=private&amp;filter=none"><%=intl._t("Private")%></a>&nbsp;
<a class="abook published" href="addressbook?book=published&amp;filter=none"><%=intl._t("Published")%></a>&nbsp;
<a id=subs href="subscriptions"><%=intl._t("Subscriptions")%></a>&nbsp;
<a id=configlink class=selected href="config"><%=intl._t("Configuration")%></a>&nbsp;
<a id=overview href="index"><%=intl._t("Help")%></a>
</div>
<hr>
<div class=headline id=configure>
<h3><%=intl._t("Configuration")%></h3>
<h4><%=intl._t("File location")%>: <span class=storage>${cfg.fileName}</span></h4>
</div>
<script src="/js/clickToClose.js?<%=net.i2p.CoreVersion.VERSION%>"></script>
<div id=messages class=canClose>${cfg.messages}</div>
<form method=POST action="config#navi">
<div id=config>
<input type=hidden name="serial" value="${cfg.serial}" >
<textarea name="config" rows=10 cols=80 spellcheck=false>${cfg.config}</textarea>
</div>
<div id=buttons>
<input class=reload type=submit name=action value="<%=intl._t("Reload")%>" >
<input class=accept type=submit name=action value="<%=intl._t("Save")%>" >
</div>
</form>
<div class=help id=helpconfig>
<h3><%=intl._t("Hints")%></h3>
<ol>
<li><%=intl._t("File and directory paths here are relative to the addressbook's working directory, which is normally ~/.i2p/addressbook/ (Linux) or %LOCALAPPDATA%\\I2P\\addressbook\\ (Windows).")%></li>
<li><%=intl._t("If you want to manually add lines to an addressbook, add them to the private or master addressbooks.")%>&nbsp;<wbr><%=intl._t("The router addressbook and the published addressbook are updated by the addressbook application.")%></li>
<li><%=intl._t("When you publish your addressbook, ALL destinations from the master and router addressbooks appear there.")%>&nbsp;<wbr><%=intl._t("Use the private addressbook for private destinations, these are not published.")%></li>
</ol>
<h3><%=intl._t("Options")%></h3>
<ul>
<li><b>subscriptions</b> - <%=intl._t("File containing the list of subscriptions URLs (no need to change)")%></li>
<li><b>update_delay</b> - <%=intl._t("Update interval in hours")%></li>
<li><b>published_addressbook</b> - <%=intl._t("Your public hosts.txt file (choose a path within your webserver document root)")%></li>
<li><b>router_addressbook</b> - <%=intl._t("Your hosts.txt (don't change)")%></li>
<li><b>master_addressbook</b> - <%=intl._t("Your personal addressbook, these hosts will be published")%></li>
<li><b>private_addressbook</b> - <%=intl._t("Your private addressbook, it is never published")%> </li>
<li><b>proxy_port</b> - <%=intl._t("Port for your eepProxy (no need to change)")%> </li>
<li><b>proxy_host</b> - <%=intl._t("Hostname for your eepProxy (no need to change)")%> </li>
<li><b>should_publish</b> - <%=intl._t("Whether to update the published addressbook")%> </li>
<li><b>etags</b> - <%=intl._t("File containing the etags header from the fetched subscription URLs (no need to change)")%></li>
<li><b>last_modified</b> - <%=intl._t("File containing the modification timestamp for each fetched subscription URL (no need to change)")%></li>
<li><b>log</b> - <%=intl._t("File to log activity to (change to /dev/null if you like)")%></li>
<li><b>theme</b> - <%=intl._t("Name of override theme to use (defaults to console-selected theme)")%></li>
</ul>
</div>
</div>
<span data-iframe-height></span>
<style>body{display:block!important;pointer-events:auto!important}</style>
<script src="/js/iframeResizer/iframeResizer.contentWindow.js?<%=net.i2p.CoreVersion.VERSION%>"></script>
<script src="/js/iframeResizer/updatedEvent.js?<%=net.i2p.CoreVersion.VERSION%>"></script>
</body>
</html>