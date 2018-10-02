package protein.common;

import java.io.Serializable;

import static protein.common.StorageUtils.toFirstLowerCase;

public class Settings implements Serializable {
    private static Settings instance = null;

    private String moduleName;
    private String componentName;
    private String domainName;
    private String swaggerUrl;
    private String serviceEndpoint;
    private String swaggerSchema;

    private Settings() {
        // Exists only to defeat instantiation.
    }

    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }

        return instance;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getSwaggerUrl() {
        return swaggerUrl;
    }

    public void setSwaggerUrl(String swaggerUrl) {
        this.swaggerUrl = swaggerUrl;
    }

    public String getServiceEndpoint() {
        return serviceEndpoint;
    }

    public void setServiceEndpoint(String serviceEndpoint) {
        this.serviceEndpoint = serviceEndpoint;
    }

    public String getPackageName() {
        return domainName + "." + toFirstLowerCase(this.componentName);
    }

    public String getSwaggerSchema() {
        return swaggerSchema;
    }

    public void setSwaggerSchema(String swaggerSchema) {
        this.swaggerSchema = swaggerSchema;
    }
}
