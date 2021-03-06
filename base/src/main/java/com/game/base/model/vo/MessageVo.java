package com.game.base.model.vo;


import com.game.base.model.msg.Header;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author zheng
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageVo implements Serializable {
    private Header header;
    private byte[] data;

}
