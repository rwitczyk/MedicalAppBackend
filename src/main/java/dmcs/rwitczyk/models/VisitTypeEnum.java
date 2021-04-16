package dmcs.rwitczyk.models;

import lombok.Getter;

@Getter
public enum VisitTypeEnum {

    STANDARD("standard"),
    ONLINE("online");

    private final String value;

    VisitTypeEnum(String value) {
        this.value = value;
    }

    public static VisitTypeEnum of(String value) {
        switch (value) {
            case "standard": {
                return VisitTypeEnum.STANDARD;
            }
            case "online": {
                return VisitTypeEnum.ONLINE;
            }
            default: {
                return null;
            }
        }
    }
}
