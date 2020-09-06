// Auto generated code, do not modify
package qoober.http.callers;

import qoober.http.APICall;

public class AddPeerCall extends APICall.Builder<AddPeerCall> {
    private AddPeerCall() {
        super(ApiSpec.addPeer);
    }

    public static AddPeerCall create() {
        return new AddPeerCall();
    }

    public AddPeerCall peer(String peer) {
        return param("peer", peer);
    }
}
