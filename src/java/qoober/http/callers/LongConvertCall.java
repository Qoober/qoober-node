// Auto generated code, do not modify
package qoober.http.callers;

import qoober.http.APICall;

public class LongConvertCall extends APICall.Builder<LongConvertCall> {
    private LongConvertCall() {
        super(ApiSpec.longConvert);
    }

    public static LongConvertCall create() {
        return new LongConvertCall();
    }

    public LongConvertCall id(String id) {
        return param("id", id);
    }
}
