package homework.entity;

public enum EnumStatus {
    NEW("новое"), IN_PROGRESS("в работе"), READY("готово"), EMPTY("не задано");
    private String status;

    EnumStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
