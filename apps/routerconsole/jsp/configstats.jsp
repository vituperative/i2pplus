<%@page contentType="text/html" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" buffer="32kb" %>
<!DOCTYPE HTML>
<%
    net.i2p.I2PAppContext ctx = net.i2p.I2PAppContext.getGlobalContext();
    String lang = "en";
    if (ctx.getProperty("routerconsole.lang") != null) {lang = ctx.getProperty("routerconsole.lang");}
%>
<%@include file="head.jsi" %>
<%=intl.title("config stats")%>
</head>
<body>
<script nonce=<%=cspNonce%>>progressx.show(theme);progressx.progress(0.1);</script>
<%@include file="sidebar.jsi" %>
<h1 class=conf><%=intl._t("Stats Collection &amp; Graphing")%></h1>
<div class=main id=config_stats>
<%@include file="confignav.jsi" %>

<jsp:useBean class="net.i2p.router.web.helpers.ConfigStatsHandler" id="formhandler" scope="request" />
<%@include file="formhandler.jsi" %>
<jsp:useBean class="net.i2p.router.web.helpers.ConfigStatsHelper" id="statshelper" scope="request" />
<jsp:setProperty name="statshelper" property="contextId" value="<%=i2pcontextId%>" />
<div class=configure>
<form id=statsForm name="statsForm" method=POST>
<input type=hidden name=action value="foo" >
<input type=hidden name="nonce" value="<%=pageNonce%>" >
<h3 class=tabletitle><%=intl._t("Configure I2P Stat Collection")%></h3>
<table id=statconfig class=configtable>
<tr><td class=infohelp id=enablefullstats colspan=2><%=intl._t("A limited selection of stats is enabled by default, required for monitoring router performance. Only stats that have an optional graph are listed here; for a full list of enabled stats, view the <a href=\"/stats\">stats page</a>.")%></td></tr>
<tr id=enablefull><td><label><input type=checkbox class="optbox slider" id=enableFull name="isFull" value=true <%
 if (statshelper.getIsFull()) { %>checked="checked" <% } %> > <b><%=intl._t("Enable full stats?").replace(" stats?", " stat collection")%></b>
 (<%=intl._t("change requires restart to take effect").replace("change requires restart to take effect", "restart required") %>)</label></td><td class=right><input type=submit name="shouldsave" class=accept value="<%=intl._t("Save changes")%>"></td></tr>
</table>
<h3 class=tabletitle id=graphchoice><%=intl._t("Select Stats for Graphing")%></h3>
<table id=configstats><tbody>
<%
    while (statshelper.hasMoreStats()) {
        while (statshelper.groupRequired()) {
%>
<tr class=statgroup><th class=left colspan=2 id=<%=statshelper.getCurrentGroupName().replace(" ", "_").replace("[", "").replace("]", "")%>><b><%=statshelper.getCurrentGroupName()%></b></th></tr>
<tr class=graphableStat><td colspan=2>
<%
        } // end iterating over required groups for the current stat
        if (statshelper.getCurrentCanBeGraphed() && !statshelper.getCurrentGraphName().contains("Ping")) {
%>
<input hidden type=checkbox class=optbox id="<%=statshelper.getCurrentStatName().replace(" ", "_").replace("[", "").replace("]", "")%>" name="graphList" value="<%=statshelper.getCurrentGraphName()%>"<% if (statshelper.getCurrentIsGraphed()){%> checked="checked"<%}%>>
<label for="<%=statshelper.getCurrentStatName().replace(" ", "_").replace("[", "").replace("]", "")%>" data-tooltip="<%=statshelper.getCurrentStatDescription()%>">
<%
    int dot = statshelper.getCurrentStatName().indexOf(".");
    String truncated = statshelper.getCurrentStatName().substring(dot + 1);
    truncated = truncated.replace("participating", "part").replace("Exploratory", "Expl").replace("Received", "RX")
                         .replace("con.", "").replace("garlic.decryptFail", "garlic.DecryptFail")
                         .replace(".data", ".Data").replace(".drop", ".Drop").replace(".delay", ".Delay")
                         .replace(".new", ".New").replace(".in", ".In").replace(".out", ".Out")
                         .replace("Received", "RX").replace("receive", "RX").replace("RXBps", "ReceiveBps")
                         .replace(".full", ".Full").replace(".size", ".Size").replace(".dups", ".Dups");
%>

<div class=stattograph><b><%=truncated%></b><br><span class=statdesc><%=statshelper.getCurrentStatDescription()%></span></div>
</label>
<%
        } // end iterating over all stats
    }
%>
</td></tr>
</tbody>
<tfoot><tr class=tablefooter><td colspan=2 class="optionsave right">
<input type=reset class=cancel value="<%=intl._t("Cancel")%>">
<input type=submit name="shouldsave" class=accept value="<%=intl._t("Save changes")%>">
</td></tr></tfoot>
</table>
</form>
</div>
</div>
<script src=/js/configstats.js type=module></script>
<script src=/js/toggleElements.js></script>
<script nonce=<%=cspNonce%> type=module>setupToggles(".statgroup", ".statgroup+graphableStat", "table-row", true, false);</script>
</body>
</html>