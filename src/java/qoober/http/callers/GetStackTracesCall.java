// Auto generated code, do not modify
package qoober.http.callers;

import qoober.http.APICall;

public class GetStackTracesCall extends APICall.Builder<GetStackTracesCall> {
    private GetStackTracesCall() {
        super(ApiSpec.getStackTraces);
    }

    public static GetStackTracesCall create() {
        return new GetStackTracesCall();
    }

    public GetStackTracesCall depth(String depth) {
        return param("depth", depth);
    }
}
