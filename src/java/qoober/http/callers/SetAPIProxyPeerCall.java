// Auto generated code, do not modify
package qoober.http.callers;

import qoober.http.APICall;

public class SetAPIProxyPeerCall extends APICall.Builder<SetAPIProxyPeerCall> {
    private SetAPIProxyPeerCall() {
        super(ApiSpec.setAPIProxyPeer);
    }

    public static SetAPIProxyPeerCall create() {
        return new SetAPIProxyPeerCall();
    }

    public SetAPIProxyPeerCall peer(String peer) {
        return param("peer", peer);
    }
}
