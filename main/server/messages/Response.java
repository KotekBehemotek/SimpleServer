package main.server.messages;

import main.utility.Bytes;

public class Response extends Message {

    public Response() {
        getStatusLine().put(new ComparableArray(Bytes.CODE.getValue()), null);
        getStatusLine().put(new ComparableArray(Bytes.MESSAGE.getValue()), null);
    }

}
