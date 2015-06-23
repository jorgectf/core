package com.dotcms.rest.api.v1.sites.rules;

import com.dotcms.repackage.com.fasterxml.jackson.annotation.JsonCreator;
import com.dotcms.repackage.com.fasterxml.jackson.annotation.JsonProperty;
import com.dotcms.repackage.com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.dotcms.rest.exception.BadRequestException;

import static com.dotcms.rest.validation.Preconditions.checkNotNull;

@JsonDeserialize(builder = RestRuleActionParameter.Builder.class)
class RestRuleActionParameter {

    public final String id;
    public final String key;
    public final String value;
    public final int priority;

    private RestRuleActionParameter(Builder builder) {
        id = builder.id;
        key = builder.key;
        value = builder.value;
        priority = builder.priority;
    }

    public static final class Builder {
        @JsonProperty private String id;
        @JsonProperty private final String key;
        @JsonProperty private final String value;
        @JsonProperty private int priority = 0;

        @JsonCreator
        public Builder(@JsonProperty("key") String key, @JsonProperty("value") String value) {
            this.key = key;
            this.value = value;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }

        public void validate(){
            checkNotNull(key, BadRequestException.class, "conditionValue.key is required.");
            checkNotNull(value, BadRequestException.class, "conditionValue.value is required.");
        }

        public RestRuleActionParameter build() {
            this.validate();
            return new RestRuleActionParameter(this);
        }
    }
}
