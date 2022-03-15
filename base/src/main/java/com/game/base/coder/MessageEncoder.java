package com.game.base.coder;



import com.game.base.model.msg.Header;
import com.game.base.model.msg.Message;
import com.game.base.serialize.DataSerialize;
import com.game.base.serialize.DataSerializeFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
@ChannelHandler.Sharable
public class MessageEncoder extends MessageToByteEncoder<Message> {

    private DataSerialize dataSerialize = DataSerializeFactory.getInstance().getDefaultDataSerialize();

    public MessageEncoder() {
    }

    public MessageEncoder(DataSerialize dataSerialize) {
        this.dataSerialize = dataSerialize;
    }

    /**
     * @param ctx
     * @param msg
     * @param out
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        Header header = msg.getHeader();
        out.writeInt(header.getCrcCode());
        byte[] data = dataSerialize.serialize(msg.getBody());
        out.writeInt(data.length);
        out.writeInt(header.getSeqId());
        out.writeInt(header.getFromServerId());
        out.writeInt(header.getServiceId());
        out.writeInt(header.getServerId());
        out.writeByte(header.getType());
        out.writeBytes(data);
    }

}
