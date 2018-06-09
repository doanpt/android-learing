package com.cnc.hcm.cnctrack.model;

import com.cnc.hcm.cnctrack.model.common.Data;
import com.cnc.hcm.cnctrack.model.common.Type;

//FIXME
//TODO add annotation
public final class ItemCancelTask {

    public final Type type;
    public final String content;
    public final Data data;

    public ItemCancelTask(Type type, String content, Data data) {
        this.type = type;
        this.content = content;
        this.data = data;
    }

}
