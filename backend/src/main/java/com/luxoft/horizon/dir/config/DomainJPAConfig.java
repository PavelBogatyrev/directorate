package com.luxoft.horizon.dir.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author rlapin
 */
@Component
@ConfigurationProperties(prefix="datasource_domain")
public class DomainJPAConfig {

    private String dbplatform;
    private boolean showSql;
    private String ddlAuto;
    private String jndiName;
    public String getDdlAuto() {
        return ddlAuto;
    }

    public void setDdlAuto(String ddlAuto) {
        this.ddlAuto = ddlAuto;
    }

    public boolean isShowSql() {
        return showSql;
    }

    public void setShowSql(boolean showSql) {
        this.showSql = showSql;
    }

    public String getDbplatform() {
        return dbplatform;
    }

    public void setDbplatform(String dbplatform) {
        this.dbplatform = dbplatform;
    }
    public String getJndiName() {
        return jndiName;
    }

    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }
}
