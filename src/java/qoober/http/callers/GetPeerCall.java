// Auto generated code, do not modify
package qoober.http.callers;

import qoober.http.APICall;

public class GetPeerCall extends APICall.Builder<GetPeerCall> {
    private GetPeerCall() {
        super(ApiSpec.getPeer);
    }

    public static GetPeerCall create() {
        return new GetPeerCall();
    }

    public GetPeerCall peer(String peer) {
        return param("peer", peer);
    }
}
