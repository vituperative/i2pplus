package net.i2p.router.web.helpers;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import java.util.Properties;

import net.i2p.router.web.CSSHelper;
import net.i2p.router.web.ConsolePasswordManager;
import net.i2p.router.web.HelperBase;
import net.i2p.router.web.Messages;
import net.i2p.router.web.RouterConsoleRunner;

/**
 *  Helper for /configui
 */
public class ConfigUIHelper extends HelperBase {

    public String getSettings() {
        StringBuilder buf = new StringBuilder(512);
        buf.append("<div id=availablethemes>");
        String current = _context.getProperty(CSSHelper.PROP_THEME_NAME, CSSHelper.DEFAULT_THEME);
        boolean universalTheming = _context.getBooleanProperty(CSSHelper.PROP_UNIVERSAL_THEMING);
        boolean useSoraFont = _context.getBooleanProperty(CSSHelper.PROP_ENABLE_SORA_FONT);
        Set<String> themes = themeSet();
        for (String theme : themes) {
            buf.append("<label for=\"").append(theme).append("\"><div class=themechoice style=display:inline-block;text-align:center>")
               .append("<input type=radio class=optbox name=theme ");
            if (theme.equals(current))
                buf.append(CHECKED);
            buf.append("value=\"").append(theme).append("\" id=\"").append(theme).append("\">")
               .append("<img height=48 width=48 alt=\"\" src=\"/themes/console/").append(theme).append("/images/thumbnail.png\"><br>")
               .append("<div class=themelabel>").append(_t(theme)).append("</div></div></label>\n");
        }
        buf.append("</div><div id=themeoptions><label><input type=checkbox class=\"optbox slider\" name=\"universalTheming\" ");
        if (universalTheming)
            buf.append(CHECKED);
        buf.append("value=1>")
           .append(_t("Set theme universally across all apps"))
           .append("</label><br>\n");
        buf.append("<label><input type=checkbox class=\"optbox slider\" name=\"useSoraFont\" ");
        if (useSoraFont)
            buf.append(CHECKED);
        buf.append("value=1>")
           .append(_t("Use alternative display font in console and webapps"))
           .append("</label><br>\n");
        return buf.toString();
    }

    public String getForceMobileConsole() {
        StringBuilder buf = new StringBuilder(256);
        boolean forceMobileConsole = _context.getBooleanProperty(CSSHelper.PROP_FORCE_MOBILE_CONSOLE);
        boolean embedApps = _context.getBooleanProperty(CSSHelper.PROP_EMBED_APPS);
        buf.append("<label><input type=checkbox class=\"optbox slider\" name=forceMobileConsole ");
        if (forceMobileConsole)
            buf.append(CHECKED);
        buf.append("value=1>")
           .append(_t("Force the mobile console to be used"))
           .append("</label><br>\n");
        buf.append("<label title=\"")
           .append(_t("Enabling the Universal Theming option is recommended when embedding these applications"))
           .append("\"><input type=checkbox class=\"optbox slider\" name=embedApps ");
        if (embedApps)
            buf.append(CHECKED);
        buf.append("value=1>")
           .append(_t("Embed I2PSnark and I2PMail in the console"))
           .append("</label></div>\n");
        return buf.toString();
    }

    /** @return standard and user-installed themes, sorted (untranslated) */
    private Set<String> themeSet() {
         Set<String> rv = new TreeSet<String>();
         // add a failsafe even if we can't find any themes
         rv.add(CSSHelper.DEFAULT_THEME);
         File dir = new File(_context.getBaseDir(), "docs/themes/console");
         File[] files = dir.listFiles();
         if (files == null)
             return rv;
         for (int i = 0; i < files.length; i++) {
             if (!files[i].isDirectory())
                 continue;
             String name = files[i].getName();
             if (name.equals("images"))
                 continue;
             rv.add(name);
         }
         // user themes
         Set<String> props = _context.getPropertyNames();
         for (String prop : props) {
              if (prop.startsWith(CSSHelper.PROP_THEME_PFX) && prop.length() > CSSHelper.PROP_THEME_PFX.length())
                  rv.add(prop.substring(CSSHelper.PROP_THEME_PFX.length()));
         }
         return rv;
    }

    /**
     *  Each language has the ISO code, the flag, the name, and the optional country name.
     *  Alphabetical by the ISO code please.
     *  See http://en.wikipedia.org/wiki/ISO_639-1 .
     *  Any language-specific flag added to the icon set must be
     *  added to the top-level build.xml for the updater.
     *  As of 0.9.12, ISO 639-2 three-letter codes are supported also.
     *  Note: To avoid truncation, ensure language name is no longer than 17 chars.
     */
    private static final String langs[][] = {
        /**
            Note: any additions, also add to:
            apps/i2psnark/java/src/org/klomp/snark/standalone/ConfigUIHelper.java
            apps/routerconsole/jsp/console.jsp
            apps/routerconsole/jsp/home.jsp
            .tx/config
            New lang_xx flags: Add to top-level build.xml
            Names must be 18 chars or less (including country if specified)
        **/
        { "ar", "lang_ar", "Arabic عربية", null },
        { "az", "az", "Azerbaijani", null },
        { "cs", "cz", "Čeština", null },
        { "zh", "cn", "Chinese 中文", null },
        { "da", "dk", "Dansk", null },
        { "de", "de", "Deutsch", null },
        { "et", "ee", "Eesti", null },
        { "en", "us", "English", null },
        { "es", "es", "Español", null },
        { "fi", "fi", "Finnish Suomi", null },
        { "fr", "fr", "Français", null },
        { "el", "gr", "Greek Ελληνικά", null },
        { "hi", "in", "Hindi", null },
        { "hu", "hu", "Hungarian Magyar", null },
        { "in", "id", "Indonesian", null },
        { "it", "it", "Italiano", null },
        { "ja", "jp", "Japanese 日本語", null },
        { "ko", "kr", "Korean 한국어", null },
        { "nl", "nl", "Nederlands", null },
        { "nb", "no", "Norsk (bokmål)", null },
        { "fa", "ir", "Persian فارسی", null },
        { "pl", "pl", "Polski", null },
        { "pt", "pt", "Português", null },
        { "ro", "ro", "Română", null },
        { "ru", "ru", "Russian Русский", null },
        { "sl", "sk", "Slovenčina", null },
        { "sv", "se", "Svenska", null },
        { "tr", "tr", "Türkçe", null },
        { "uk", "ua", "Ukraine Українська", null },
        { "vi", "vn", "Vietnam Tiếng Việt", null },
        { "xx", "a1", "Untagged strings", null },
        //{ "es_AR", "ar", "Español" ,"Argentina" },
        //{ "gl", "lang_gl", "Galego", null },
        //{ "iw", "il", "Hebrew עברית", null },
        //{ "ku", "ku", "Kurdî", null },
        //{ "mg", "mg", "Malagasy", null },
        //{ "pt_BR", "br", "Português", "Brazil" },
        //{ "tk", "tm", "Türkmen", null },
        //{ "zh_TW", "tw", "Chinese 中文", "Taiwan" },
    };

    public String getLangSettings() {
        String clang = Messages.getLanguage(_context);
        String current = clang;
        String country = Messages.getCountry(_context);
        if (country != null && country.length() > 0)
            current += '_' + country;
        // find best match
        boolean found = false;
        for (int i = 0; i < langs.length; i++) {
            if (langs[i][0].equals(current)) {
                found = true;
                break;
            }
        }
        if (!found) {
            if (country != null && country.length() > 0) {
                current = clang;
                for (int i = 0; i < langs.length; i++) {
                    if (langs[i][0].equals(current)) {
                        found = true;
                        break;
                    }
                }
            }
            if (!found)
                current = "en";
        }
        StringBuilder buf = new StringBuilder(512);
        for (int i = 0; i < langs.length; i++) {
            String lang = langs[i][0];
            if (lang.equals("xx") && !isAdvanced())
                continue;
            // we use "lang" so it is set automagically in CSSHelper
            buf.append("<label for=\"").append(lang).append("\"><div class=langselect>")
               .append("<input type=radio class=optbox name=lang ");
            if (lang.equals(current))
                buf.append(CHECKED);
            buf.append("value=\"").append(lang).append("\" id=\"").append(lang).append("\">")
               .append("<span class=langflag><img width=48 height=36 alt=\"\" src=\"/flags.jsp?c=")
               .append(langs[i][1]).append("\"></span>")
               .append("<div class=ui_lang>");
            int under = lang.indexOf('_');
            String slang = (under > 0) ? lang.substring(0, under) : lang;
            buf.append(langs[i][2]);
            String name = langs[i][3];
            if (name != null) {
                buf.append(" (")
                   .append(name)
                   .append(')');
            }
            buf.append("</div></div></label>\n");
        }
        return buf.toString();
    }

    /** @since 0.9.4 */
    public String getPasswordForm() {
        StringBuilder buf = new StringBuilder(512);
        ConsolePasswordManager mgr = new ConsolePasswordManager(_context);
        Map<String, String> userpw = mgr.getMD5(RouterConsoleRunner.PROP_CONSOLE_PW);
        Properties config = net.i2p.I2PAppContext.getGlobalContext().getProperties();
        // only show delete user button if user(s) configured
        if (!config.toString().contains("routerconsole.auth.i2prouter"))
            buf.append("<style>#consolepass .delete{display:none!important)</style>\n");
        buf.append("<table id=consolepass>\n");
        if (userpw.isEmpty()) {
            buf.append("<tr><td class=infohelp colspan=3>")
               .append(_t("Add a user and password to enable.")).append("&nbsp;")
               .append(_t("Note: If you forget your password, removing the configuration entry in your router.config file will clear it (restart required)."))
               .append("</td></tr>\n");
        } else {
            buf.append("<tr><td class=infohelp colspan=3>")
               .append(_t("Router console password is enabled."))
               .append("</td></tr>\n")
               .append("<tr><th title=\"").append(_t("Mark for deletion")).append("\">")
               .append(_t("Remove")).append("</th><th>").append(_t("Username"))
               .append("</th><th>&nbsp;</th></tr>\n");
            for (String name : userpw.keySet()) {
                buf.append("<tr><td><input type=checkbox class=optbox id=\"").append(name)
                   .append("\" name=\"delete_").append(name).append("\"></td>")
                   .append("<td colspan=2><label for=\"").append(name).append("\">")
                   .append(name).append("</label></td></tr>\n");
            }
        }
        buf.append("<tr><td id=pw_adduser colspan=3>")
           .append("<b>").append(_t("Username")).append(":</b> ").append("<input type=text name=name title=\"")
           .append(_t("Please supply a username")).append("\"><b>").append(_t("Password")).append(":</b> ")
           .append("<input type=password size=40 name=nofilter_pw title=\"")
           .append(_t("Please supply a password")).append("\">").append("</td></tr>\n</table>\n");
        return buf.toString();
    }
}
