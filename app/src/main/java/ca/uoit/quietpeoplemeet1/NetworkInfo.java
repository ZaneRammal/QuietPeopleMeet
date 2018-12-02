package ca.uoit.quietpeoplemeet1;


import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * used to hold important global data
 */
public final class NetworkInfo {

    public static List<SoundNode> soundNodes = new ArrayList<>();
    public static final int SERVER_PORT_NUMBER = 50210;
    public static List<InetAddress> peerAddresses = new ArrayList<>();
    public static List<TextNode> textNodes = new ArrayList<>();
    public static boolean ServerSocketInUse;
    public static String serviceName = "NsdChat";

    private NetworkInfo() {
    }

}
