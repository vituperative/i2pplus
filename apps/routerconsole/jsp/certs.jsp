<%@page contentType="text/html" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" buffer="32kb" %>
<!DOCTYPE HTML>
<%
    net.i2p.I2PAppContext ctx = net.i2p.I2PAppContext.getGlobalContext();
    String lang = "en";
    if (ctx.getProperty("routerconsole.lang") != null) {lang = ctx.getProperty("routerconsole.lang");}
%>
<%@include file="head.jsi" %>
<%=intl.title("Certificates")%>
</head>
<body>
<script nonce=<%=cspNonce%>>progressx.show(theme);progressx.progress(0.1);</script>
<%@include file="sidebar.jsi" %><h1 class="conf adv"><%=intl._t("Certificates")%></h1>
<div class=main id=certs>
<jsp:useBean class="net.i2p.router.web.helpers.CertHelper" id="certhelper" scope="request" />
<jsp:setProperty name="certhelper" property="contextId" value="<%=i2pcontextId%>" />
<% certhelper.storeWriter(out); %>
<jsp:getProperty name="certhelper" property="summary" />
<span id=end></span>
</div>
</body>
</html>