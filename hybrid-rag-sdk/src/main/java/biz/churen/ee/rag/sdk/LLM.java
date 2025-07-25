package biz.churen.ee.rag.sdk;

@SuppressWarnings("LombokGetterMayBeUsed")
public enum LLM {
    QWEN_PLUS("qwen-plus"),
    QWEN_TURBO_LATEST("qwen-turbo-latest"),
    ;

    private final String model;

    LLM(String model) {
        this.model = model;
    }

    public String getModel() {
        return model;
    }
}
