<%@page contentType="text/html" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" buffer="32kb" %>
<!DOCTYPE HTML>
<%
    net.i2p.I2PAppContext ctx = net.i2p.I2PAppContext.getGlobalContext();
    String lang = "en";
    String pageTitlePrefix = "";
    if (ctx.getProperty("routerconsole.lang") != null) {
        lang = ctx.getProperty("routerconsole.lang");
    }
    if (ctx.getProperty("routerconsole.pageTitlePrefix") != null) {
        pageTitlePrefix = ctx.getProperty("routerconsole.pageTitlePrefix") + ' ';
    }
%>
<%@include file="../head.jsi" %>
<title><%=pageTitlePrefix%> <%=intl._t("Tunnel Filtering")%> - I2P+</title>
</head>
<body>
<%@include file="../sidebar.jsi" %>
<h1 class=hlp><%=intl._t("Tunnel Filtering")%></h1>
<div class=main id=help>
<div class=confignav>
<span class=tab><a href="/help/configuration" title="<%=intl._t("Configuration")%>"><%=intl._t("Configuration")%></a></span>
<span class=tab><a href="/help/advancedsettings" title="<%=intl._t("Advanced Settings")%>"><%=intl._t("Advanced Settings")%></a></span>
<span class=tab><a href="/help/ui" title="<%=intl._t("User Interface")%>"><%=intl._t("User Interface")%></a></span>
<span class=tab><a href="/help/reseed" title="<%=intl._t("Reseeding")%>"><%=intl._t("Reseeding")%></a></span>
<span class=tab2 id=tfilt><span><%=intl._t("Tunnel Filtering")%></span></span>
<span class=tab><a href="/help/faq" title="<%=intl._t("Frequently Asked Questions")%>"><%=intl._t("FAQ")%></a></span>
<span class=tab><a href="/help/newusers" title="<%=intl._t("New User Guide")%>"><%=intl._t("New User Guide")%></a></span>
<span class=tab><a href="/help/webhosting" title="<%=intl._t("Web Hosting")%>"><%=intl._t("Web Hosting")%></a></span>
<span class=tab><a href="/help/hostnameregistration" title="<%=intl._t("Hostname Registration")%>"><%=intl._t("Hostname Registration")%></a></span>
<span class=tab><a href="/help/troubleshoot" title="<%=intl._t("Troubleshoot")%>"><%=intl._t("Troubleshoot")%></a></span>
<span class=tab><a href="/help/glossary" title="<%=intl._t("Glossary")%>"><%=intl._t("Glossary")%></a></span>
<span class=tab><a href="/help/legal" title="<%=intl._t("Legal")%>"><%=intl._t("Legal")%></a></span>
<span class=tab><a href="/help/changelog"><%=intl._t("Change Log")%></a></span>
</div>

<div id=filterlist>

<h2><%=intl._t("Introduction to Tunnel Filtering")%></h2>

<p><%=intl._t("Server tunnels, configurable in the <a href=/i2ptunnelmgr>Tunnel Manager</a>, provide a number of ways to limit access to the hosted service. Destination whitelisting, blacklisting, connection throttling, and tunnel filtering are methods provisioned to mitigate denial of service attacks, grant exclusive access to known I2P destinations, and manage traffic to the server.")%></p>

<p><%=intl._t("A tunnel filter can provide protection against denial of service attacks, without otherwise interrupting the service for genuine users, either in conjunction with the tunnel throttler or as an alternative strategy. It can also be used to log access to a hosted service to determine if the server is under attack, or simply to log all visitors by I2P destination. Additionally, a filter can function simultaneously as a whitelist and blacklist making it more flexible than the Tunnel Manager's access list controls. Note that multiple server tunnels can share a single filter file if you wish to implement a global filter.")%></p>

<h3><%=intl._t("Overview")%></h3>

<p><%=intl._t("A filter is a text file that can contain one or more declarations. Blank lines and lines beginning with <i class=example>#</i> are ignored.")%></p>

<p><%=intl._t("A declaration will contain a directive (keyword) with parameters to define the scope of the declaration, and can represent one of the following:")%></p>

<ul>
<li><%=intl._t("A default threshold to apply to all remote destinations not listed in the containing file or any of the referenced files")%></li>
<li><%=intl._t("A threshold to apply to a specific remote destination")%></li>
<li><%=intl._t("A threshold to apply to remote destinations listed in a file")%></li>
<li><%=intl._t("A threshold that if breached will cause the offending remote destination to be recorded in a specified file")%></li>
</ul>

<p><%=intl._t("Note: The order of the declarations matters. The first threshold declaration for a given destination (whether explicit or listed in a referenced file) overrides any future thresholds for the same destination, whether explicit or listed in a file. Note: to apply a new filter file, the tunnel must be restarted after the location has been specified in the tunnel manager. Once configured, a definition file can be updated without requiring a restart of the target tunnel, although destinations that are blocked will persist until the tunnel is restarted.")%></p>

<h3><%=intl._t("Thresholds")%></h3>

<p><%=intl._t("A threshold is defined by the number of connection attempts a remote destination is permitted to perform over a specified number of seconds before a \"breach\" occurs.")%></p>

<p><%=intl._t("A threshold declaration can be expressed in one of the following ways:")%></p>

<ul>
<li><%=intl._t("Numeric definition of the number of connection attempts in the period specified (in seconds) e.g. <i class=example>15/5</i>, <i class=example>60/60</i> etc.")%><br>
<%=intl._t("Note that if the number of connections is 1 (e.g. <i class=example>1/60</i>), the first connection attempt will result in a breach.")%></li>
<li><%=intl._t("The keyword <i class=example>allow</i>. This threshold is never breached, i.e. an infinite number of connection attempts is permitted.")%></li>
<li><%=intl._t("The keyword <i class=example>deny</i>. This threshold is always breached, i.e. no connection attempts will be allowed.")%></li>
</ul>

<h4><%=intl._t("Default Threshold")%></h4>

<p><%=intl._t("The <i class=example>default</i> threshold applies to any remote destination not explicitly listed in the definition or in any of the referenced files. To set a default threshold use the keyword <i class=example>default</i>.")%></p>

<p><%=intl._t("The following threshold definition <i class=example>180/60</i> specifies that the same remote destination is allowed to make 179 connection attempts during a 60 second period. If it makes one more attempt within the same period, the threshold will be breached and further connection attempts will be rejected until the end of the period:")%></p>

<code>180/60 default</code>

<p><%=intl._t("To allow all unspecified client destinations:")%></p>

<code>allow default</code>

<p><%=intl._t("Or to deny all unspecified destinations:")%></p>

<code>deny default</code>

<p><%=intl._t("Note that there can only be a single reference to the <i class=example>default</i> keyword in a filter file. If unstated, all connections are permitted unless explicitly forbidden. Errors in the filter file will prevent a stopped tunnel from starting, so be sure to double check the syntax before deployment.")%></p>

<h4><%=intl._t("Explicit Thresholds")%></h4>

<p><%=intl._t("Using the <i class=example>explicit</i> directive, a threshold can be applied to a remote destination listed in the definition itself, with the option to specify custom thresholds for multiple destinations. Remote destinations can also be excluded from the default threshold using the <i class=example>allow</i> keyword, or permanently blocked using the <i class=example>deny</i> keyword:")%></p>

<code>
180/240 default<br>
15/5 explicit szdb4cdtbahqkapustcrc37xwxc65ejithdiqdxffs73wxbswdc2.b32.i2p<br>
allow explicit zyakly6qqzjnad2kqqrpnupex7ryl6cyfrj52abm3kqrougtxids.b32.i2p<br>
deny explicit cqfnbbvxz5xjgo3saix4ygtqlyuujraz4ptclq5o7tdqzg3jffkm.b32.i2p
</code>

<h4><%=intl._t("Bulk Thresholds")%></h4>

<p><%=intl._t("For convenience you can maintain a list of destinations in a file and define a threshold for all of them in bulk:")%></p>

<code>
90/60 file /path/throttled_destinations.txt<br>
deny file /path/forbidden_destinations.txt<br>
allow file /path/unlimited_destinations.txt
</code>

<h3><%=intl._t("Recorders")%></h3>

<p><%=intl._t("Recorders keep track of connection attempts made by a remote destination, and if that breaches the defined threshold, the destination gets logged in the specified file. Multiple recorders can be specified:")%></p>

<code>
30/5 record /path/aggressive.txt<br>
60/5 record /path/very_aggressive.txt
</code>

<p><%=intl._t("You can use a recorder to log the destinations of aggressive clients to a specified file, and then use that same file to throttle them. The following snippet defines a filter that initially allows all connection requests, but if any single destination exceeds 180 requests in 60 seconds, it gets throttled down to 30 requests every 60 seconds:")%></p>

<code>
# <%=intl._t("by default there are no limits")%><br>
allow default<br>
# <%=intl._t("but log overly aggressive destinations")%><br>
180/60 record /path/throttled.txt<br>
# <%=intl._t("and any that end up in that file will get throttled in the future")%><br>
30/60 file /path/throttled.txt
</code>

<p><%=intl._t("To log all unique remote destinations connecting to a server, and also log aggressive connection attempts:")%></p>

<code>
# <%=intl._t("log all unique destinations")%><br>
1/60 record /path/visitors.txt<br>
# <%=intl._t("log visitors making 90 or more connections a minute")%><br>
90/60 record /path/aggressive.txt
</code>

<p><%=intl._t("You can use a recorder in one tunnel that writes to a file that another tunnel, or multiple tunnels, can use for throttling. These files can also be edited by hand.")%></p>

<p><%=intl._t("The following filter definition applies some throttling by default, no throttling for destinations in the file <i class=example>friends.txt</i>, forbids any connections from destinations in the file <i class=example>enemies.txt</i>, and logs any aggressive behavior in a file called <i class=example>suspicious.txt</i>:")%></p>

<code>
30/5 default<br>
allow file /path/friends.txt<br>
deny file /path/enemies.txt<br>
60/60 record /path/suspicious.txt
</code>

<p><%=intl._t("Note that the full path to the output files must be accessible before deploying the filter. If the target directory does not exist, the destinations will not be recorded and an error will appear in the router logs. The files will be created as required. The examples provided above are not recommendations; your configuration should be tuned according to the amount of content (number and size of files) your site is serving, the speed of the server, and the frequency with which the site is updated. Configuring recorders and monitoring the resulting files will help you fine tune the settings.")%></p>

</div>

</div>
</body>
</html>