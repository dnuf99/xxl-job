package com.xxl.job.core.rpc.codec;

import com.xxl.job.core.enums.ProtocolEnum;

import java.io.Serializable;
import java.util.Arrays;

import static org.apache.zookeeper.ZooDefs.OpCode.error;

/**
 * response
 * @author xuxueli 2015-10-29 19:39:54
 */
public class ResponseProtocolBytes implements Serializable{
	
    private byte[] responseBytes;
    private ProtocolEnum protocol = ProtocolEnum.Hessian;

    public byte[] getResponseBytes() {
        return responseBytes;
    }

    public void setResponseBytes(byte[] responseBytes) {
        this.responseBytes = responseBytes;
    }

    public ProtocolEnum getProtocol() {
        return protocol;
    }

    public void setProtocol(ProtocolEnum protocol) {
        this.protocol = protocol;
    }

    @Override
    public String toString() {
        return "ResponseProtocolBytes{" +
                "responseBytes=" + Arrays.toString(responseBytes) +
                ", protocol=" + protocol +
                '}';
    }
}
