package org.dikhim.clickauto.jsengine.objects.generators;


import org.dikhim.clickauto.jsengine.objects.ClipboardObject;

public class ClipboardObjectCodeGenerator extends SimpleCodeGenerator implements ClipboardObject {

    public ClipboardObjectCodeGenerator(int lineSize) {
        super("clipboard", lineSize, ClipboardObject.class);
    }

    public ClipboardObjectCodeGenerator() {
        super("clipboard", ClipboardObject.class);
    }

    @Override
    public String get() {
        buildStringForCurrentMethod();
        return null;
    }

    @Override
    public void set(String str) {
        buildStringForCurrentMethod(str);
    }
}