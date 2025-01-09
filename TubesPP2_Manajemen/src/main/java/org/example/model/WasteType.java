package org.example.model;

public class WasteType {
    private int typeId;
    private String typeName;
    private int categoryId;

    public WasteType(int typeId, String typeName, int categoryId) {
        this.typeId = typeId;
        this.typeName = typeName;
        this.categoryId = categoryId;
    }

    // Getter dan Setter
    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}