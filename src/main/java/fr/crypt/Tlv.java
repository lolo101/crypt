package fr.crypt;

class Tlv {
    private final String type;
    private final String value;

    Tlv(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("%s%02d%s", type, value.length(), value);
    }
    
}
