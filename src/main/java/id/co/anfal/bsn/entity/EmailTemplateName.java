package id.co.anfal.bsn.entity;

import lombok.Getter;

@Getter
public enum EmailTemplateName {
    ACTIVATE_ACCOUNT("Activate_Account")
    // Add more templates here if needed
    ;

    private final String name;

    EmailTemplateName(String name) {
        this.name = name;
    }
}

