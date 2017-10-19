package cz.cas.lib.arclib.index;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndexFieldConfig {
    private String fieldName;
    private FieldType fieldType;
    private String xpath;
    private boolean fullText;

    public IndexFieldConfig(String fieldName, String fieldType, String xpath, boolean fullText) {
        this.fieldName = fieldName;
        this.xpath = xpath;
        this.fullText = fullText;
        try {
            this.fieldType = FieldType.valueOf(fieldType.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            this.fieldType = FieldType.OTHER;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IndexFieldConfig that = (IndexFieldConfig) o;

        return getFieldName().equals(that.getFieldName());
    }

    @Override
    public int hashCode() {
        return getFieldName().hashCode();
    }
}
