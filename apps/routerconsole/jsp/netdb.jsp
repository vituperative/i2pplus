<%@page contentType="text/html" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" buffer="256kb" %>
<!DOCTYPE HTML>
<%
    net.i2p.I2PAppContext ctx = net.i2p.I2PAppContext.getGlobalContext();
    String lang = "en";
    if (ctx.getProperty("routerconsole.lang") != null) {lang = ctx.getProperty("routerconsole.lang");}
%>
<%@include file="head.jsi" %>
<%=intl.title("network database")%>
<link href=/themes/console/tablesort.css rel=stylesheet>
</head>
<body>
<script nonce=<%=cspNonce%>>progressx.show(theme);progressx.progress(0.1);</script>
<%@include file="sidebar.jsi" %>
<jsp:useBean class="net.i2p.router.web.helpers.NetDbHelper" id="formhandler" scope="request"/>
<jsp:setProperty name="formhandler" property="full" value="<%=request.getParameter(\"f\")%>"/>
<jsp:setProperty name="formhandler" property="router" value="<%=request.getParameter(\"r\")%>"/>
<jsp:setProperty name="formhandler" property="lease" value="<%=request.getParameter(\"l\")%>"/>
<jsp:setProperty name="formhandler" property="version" value="<%=request.getParameter(\"v\")%>"/>
<%  if (request.getParameter("cc") != null && !request.getParameter("cc").equals("")) { %>
<jsp:setProperty name="formhandler" property="country" value="<%=request.getParameter(\"cc\")%>"/>
<%  } else { %>
<jsp:setProperty name="formhandler" property="country" value="<%=request.getParameter(\"c\")%>"/>
<%  } %>
<jsp:setProperty name="formhandler" property="family" value="<%=request.getParameter(\"fam\")%>"/>
<jsp:setProperty name="formhandler" property="caps" value="<%=request.getParameter(\"caps\")%>"/>
<jsp:setProperty name="formhandler" property="ip" value="<%=request.getParameter(\"ip\")%>"/>
<jsp:setProperty name="formhandler" property="sybil" value="<%=request.getParameter(\"sybil\")%>"/>
<jsp:setProperty name="formhandler" property="sybil2" value="<%=request.getParameter(\"sybil2\")%>"/>
<jsp:setProperty name="formhandler" property="port" value="<%=request.getParameter(\"port\")%>"/>
<jsp:setProperty name="formhandler" property="type" value="<%=request.getParameter(\"type\")%>"/>
<jsp:setProperty name="formhandler" property="ipv6" value="<%=request.getParameter(\"ipv6\")%>"/>
<jsp:setProperty name="formhandler" property="cost" value="<%=request.getParameter(\"cost\")%>"/>
<jsp:setProperty name="formhandler" property="mtu" value="<%=request.getParameter(\"mtu\")%>"/>
<jsp:setProperty name="formhandler" property="ssucaps" value="<%=request.getParameter(\"ssucaps\")%>"/>
<jsp:setProperty name="formhandler" property="transport" value="<%=request.getParameter(\"tr\")%>"/>
<jsp:setProperty name="formhandler" property="limit" value="<%=request.getParameter(\"ps\")%>"/>
<jsp:setProperty name="formhandler" property="page" value="<%=request.getParameter(\"pg\")%>"/>
<jsp:setProperty name="formhandler" property="mode" value="<%=request.getParameter(\"m\")%>"/>
<jsp:setProperty name="formhandler" property="date" value="<%=request.getParameter(\"date\")%>"/>
<jsp:setProperty name="formhandler" property="leaseset" value="<%=request.getParameter(\"ls\")%>"/>
<%
    String c = request.getParameter("c");
    String f = request.getParameter("f");
    String l = request.getParameter("l");
    String ls = request.getParameter("ls");
    String r = request.getParameter("r");
    boolean delayLoad = false;
    if (f == null && l == null && ls == null && r == null) {
%>
<h1 class=netwrk><%=intl._t("Network Database")%></h1>
<%
    } else if (f != null) {
        if (f.equals("1") || f.equals("2")) {
            delayLoad = true;
%>
<h1 class=netwrk><%=intl._t("Network Database")%> &ndash; <%=intl._t("All Routers")%></h1>
<%
        } else if (f.equals("3")) {
            delayLoad = true;
%>
<h1 class=netwrk><%=intl._t("Network Database")%> &ndash; <%=intl._t("Sybil Analysis")%></h1>
<%
        } else if (f.equals("4")) {
%>
<h1 class=netwrk><%=intl._t("Network Database")%> &ndash; <%=intl._t("Advanced Lookup")%></h1>
<script src=/js/netdbLookup.js></script>
<%
        }
    } else if (f == null) {
        if (r != null && r.equals(".")) {
%>
<h1 class=netwrk><%=intl._t("Network Database")%> &ndash; <%=intl._t("Local Router")%></h1>
<%
        } else if (r != null) {
%>
<h1 class=netwrk><%=intl._t("Network Database")%> &ndash; <%=intl._t("Router Lookup")%></h1>
<%
        } else if (r == null && ls != null) {
%>
<h1 class=netwrk><%=intl._t("Network Database")%> &ndash; <%=intl._t("LeaseSet Lookup")%></h1>
<%
        } else if (r == null && ls == null && l != null) {
            delayLoad = true;
%>
<h1 class=netwrk><%=intl._t("Network Database")%> &ndash; <%=intl._t("LeaseSets")%></h1>
<%
        } else if (r == null && ls == null && l == null && c != null) {
%>
<h1 class=netwrk><%=intl._t("Network Database")%> &ndash; <%=intl._t("Routers")%></h1>
<%
        } else {
%>
<h1 class=netwrk><%=intl._t("Network Database")%></h1>
<%
        }
    }
%>
<div class=main id=netdb>
<%  if (delayLoad) { %>
<div id=netdbwrap style=height:5px;opacity:0>
<%  } %>
<%  if (r == null && ls != null || l != null) { %>
<div class=leasesets_container>
<%  }
    formhandler.storeWriter(out);
    if (allowIFrame) {formhandler.allowGraphical();}
%>
<%@include file="formhandler.jsi" %>
<jsp:getProperty name="formhandler" property="floodfillNetDbSummary"/>
<%  if (r == null && ls != null || l != null) { %>
</div>
<%  } %>
</div>
<%  if (delayLoad) { %>
</div>
<style>#netdbwrap{height:unset!important;opacity:1!important}#netdb::before{display:none}</style>
<noscript><style>body:not(.ready) .lazy{display:table!important}</style></noscript>
<%  } %>
<script src=/js/lazyload.js></script>
<script src=/js/lsCompact.js type=module></script>
<script src=/js/tablesort/tablesort.js></script>
<script src=/js/tablesort/tablesort.number.js></script>
<script src=/js/netdb.js></script>
</body>
</html>