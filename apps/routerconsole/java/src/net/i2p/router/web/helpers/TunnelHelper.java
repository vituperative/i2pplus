package net.i2p.router.web.helpers;

import java.io.IOException;
import java.io.StringWriter;

import net.i2p.router.web.HelperBase;

import net.i2p.router.RouterContext;

public class TunnelHelper extends HelperBase {
    public TunnelHelper() {}

    public String getTunnelSummary() {
        TunnelRenderer renderer = new TunnelRenderer(_context);
        try {
            if (_out != null) {
                renderer.renderStatusHTML(_out);
//                renderer.renderParticipating(_out);
                renderer.renderGuide(_out);
                return "";
            } else {
                StringWriter sw = new StringWriter(32*1024);
                renderer.renderStatusHTML(sw);
//                renderer.renderParticipating(sw);
                renderer.renderGuide(sw);
                return sw.toString();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return "";
        }
    }
}
