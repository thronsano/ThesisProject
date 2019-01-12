package com.diamondLounge.entity.models.tables;

import static com.diamondLounge.entity.models.tables.HeadersPosition.HORIZONTAL_AND_VERTICAL;

public abstract class TableModel {
    String[][] table;
    int width;
    int height;
    HeadersPosition headersPosition = HORIZONTAL_AND_VERTICAL;
    boolean ignoreMissingValues = false;

    public String[][] getTable() {
        return table;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public HeadersPosition getHeadersPosition() {
        return headersPosition;
    }

    public boolean ignoreMissingValues() {
        return ignoreMissingValues;
    }
}