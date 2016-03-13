package com.mttnow.push.api.builders;

import com.mttnow.push.api.models.Application;
import com.mttnow.push.core.persistence.enums.ApplicationMode;

/**
 * Builder for application objects for testing.
 */
public class ApplicationBuilder {

    Application application;

    public ApplicationBuilder() {
        this.application = new Application();
    }

    public Application buildWithDefaultValues() {
        application.setId("1L");
        application.setName("appName");
        application.setMode(ApplicationMode.DEVELOPMENT);
        return application;
    }

    public Application build() {
        return application;
    }

    public ApplicationBuilder withId(String id) {
        application.setId(id);
        return this;
    }

    public ApplicationBuilder withName(String name) {
        application.setName(name);
        return this;
    }

    public ApplicationBuilder withMode(ApplicationMode mode) {
        application.setMode(mode);
        return this;
    }

}
