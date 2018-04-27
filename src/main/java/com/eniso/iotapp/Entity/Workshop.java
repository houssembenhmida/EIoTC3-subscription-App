package com.eniso.iotapp.Entity;

public enum Workshop {
    None("Workshop"),
    DeepLearning("Deep Learning"),
    AIGoogleAPIs("AI Google APIs"),
    BigData("Big Data"),
    Cloud("Cloud"),
    Blockchain("Blockchain");

    private final String fullName;

    Workshop(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

}
