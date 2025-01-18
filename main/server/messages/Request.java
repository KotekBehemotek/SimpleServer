package main.server.messages;

import main.utility.Bytes;

public class Request extends Message {

    public Request() {
        getStatusLine().put(new ComparableArray(Bytes.TARGET.getValue()), null);
        getStatusLine().put(new ComparableArray(Bytes.METHOD.getValue()), null);
    }

}
