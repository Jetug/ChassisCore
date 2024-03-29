package com.jetug.power_armor_mod.common.data.json;

import com.jetug.power_armor_mod.common.data.enums.*;

import javax.annotation.*;

public class FrameSettings extends ModelSettingsBase {
    public FrameAttachment[] attachments;

    @Nullable
    public FrameAttachment getAttachments(BodyPart part) {
        for (var att : attachments) {
            if(att.part == part) return att;
        }
        return null;
    }
}
