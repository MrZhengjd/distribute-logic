package com.game.base.coder;



import com.game.base.model.msg.Body;
import com.game.base.model.msg.Header;
import com.game.base.model.msg.Message;
import com.game.base.model.vo.MessageVo;
import com.game.base.relation.Constants;
import com.game.base.serialize.DataSerialize;
import com.game.base.serialize.DataSerializeFactory;
import com.game.base.util.IncommingCount;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

//@ChannelHandler.Sharable
public class MessageDecoder extends ByteToMessageDecoder {
    private static Logger logger = LoggerFactory.getLogger(MessageDecoder.class);
//    private DataSerialize serializeUtil = DataSerializeFactory.getInstance().getDefaultDataSerialize();
    private final static int HEADER_LENGTH = 21;
    private final static int MAX_LENGTH = 65535;

    public MessageDecoder() {
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        try {
            if (in.readableBytes() < HEADER_LENGTH) {
                in.clear();
                return;
            }
            if (in.readableBytes() > MAX_LENGTH) {
                in.skipBytes(in.readableBytes());
                return;
            }
            int beginReader;
            while (true) {
                beginReader = in.readerIndex();
                in.markReaderIndex();
                if (in.readInt() == Constants.HEAD_START) {
                    break;
                }

                in.resetReaderIndex();
                in.readByte();

                if (in.readableBytes() < HEADER_LENGTH) {
                    return;
                }
            }
            int length = in.readInt();
//            if (in.readableBytes() < length +17){
//                in.resetReaderIndex();
//                return;
//            }
            int requestId = in.readInt();
            int fromServer = in.readInt();
            int serviceId = in.readInt();
            int serverId = in.readInt();
            byte type = in.readByte();

//            log.info("rest byte"+in.readableBytes());
            if (in.readableBytes() < length)
            {
                in.resetReaderIndex();
                return;
            }
            Header header = new Header(length,type,requestId);
            header.setServerId(serverId);
            header.setServiceId(serviceId);
            header.setFromServerId(fromServer);
            byte[] data = new byte[length];
            in.readBytes(data);
//            BaseMessage body = MessageUtil.getMessageByType(type);
//            log.info("receivei message "+body);
//            Body body = serializeUtil.deserialize(data, Body.class);
            MessageVo message = new MessageVo(header, data);
//            log.info("message"+message);
            out.add(message);
            IncommingCount.getAndIncrementDecode();
//            in.resetReaderIndex();
        } catch (Exception e) {
            logger.error("decode error " + e,e);
        }
    }


}
