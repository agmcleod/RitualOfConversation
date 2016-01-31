package com.agmcleod.ritual_of_conversation.helpers;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by aaronmcleod on 2016-01-30.
 */
public class TextureRegionDrawer {
    public static void drawRegionForBatch(Batch batch, TextureAtlas.AtlasRegion region, float x, float y, float width, float height) {
        float w = width;
        float h = height;
        if (region.rotate) {
            w = height;
            h = width;
            y += height;
        }
        batch.draw(region, x, y, 0, 0, w, h, 1, 1, region.rotate ? -90 : 0);
    }

}
