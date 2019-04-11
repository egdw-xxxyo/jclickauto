package org.dikhim.clickauto.jsengine.objects;

import java.awt.*;

public interface CreateObject {
    Image image(int width, int height);    
    
    Image image(String zipBase64String);

    Image imageFile(String path);

    Point point(int x, int y);
}