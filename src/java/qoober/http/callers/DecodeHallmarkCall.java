// Auto generated code, do not modify
package qoober.http.callers;

import qoober.http.APICall;

public class DecodeHallmarkCall extends APICall.Builder<DecodeHallmarkCall> {
    private DecodeHallmarkCall() {
        super(ApiSpec.decodeHallmark);
    }

    public static DecodeHallmarkCall create() {
        return new DecodeHallmarkCall();
    }

    public DecodeHallmarkCall hallmark(String hallmark) {
        return param("hallmark", hallmark);
    }
}
