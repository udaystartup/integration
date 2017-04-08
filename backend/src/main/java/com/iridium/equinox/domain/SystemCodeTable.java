package com.iridium.equinox.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A SystemCodeTable.
 */
@Entity
@Table(name = "system_code_table")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "systemcodetable")
public class SystemCodeTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2)
    @Column(name = "code_table_name", nullable = false)
    private String codeTableName;

    @NotNull
    @Size(min = 2)
    @Column(name = "code_table_desc", nullable = false)
    private String codeTableDesc;

    @NotNull
    @Size(min = 1)
    @Column(name = "code_table_entry", nullable = false)
    private String codeTableEntry;

    @NotNull
    @Size(min = 1)
    @Column(name = "code_table_entry_desc", nullable = false)
    private String codeTableEntryDesc;

    @NotNull
    @Column(name = "active_indicator", nullable = false)
    private Boolean activeIndicator;

    @NotNull
    @Column(name = "tenant_id", nullable = false)
    private String tenantID;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeTableName() {
        return codeTableName;
    }

    public SystemCodeTable codeTableName(String codeTableName) {
        this.codeTableName = codeTableName;
        return this;
    }

    public void setCodeTableName(String codeTableName) {
        this.codeTableName = codeTableName;
    }

    public String getCodeTableDesc() {
        return codeTableDesc;
    }

    public SystemCodeTable codeTableDesc(String codeTableDesc) {
        this.codeTableDesc = codeTableDesc;
        return this;
    }

    public void setCodeTableDesc(String codeTableDesc) {
        this.codeTableDesc = codeTableDesc;
    }

    public String getCodeTableEntry() {
        return codeTableEntry;
    }

    public SystemCodeTable codeTableEntry(String codeTableEntry) {
        this.codeTableEntry = codeTableEntry;
        return this;
    }

    public void setCodeTableEntry(String codeTableEntry) {
        this.codeTableEntry = codeTableEntry;
    }

    public String getCodeTableEntryDesc() {
        return codeTableEntryDesc;
    }

    public SystemCodeTable codeTableEntryDesc(String codeTableEntryDesc) {
        this.codeTableEntryDesc = codeTableEntryDesc;
        return this;
    }

    public void setCodeTableEntryDesc(String codeTableEntryDesc) {
        this.codeTableEntryDesc = codeTableEntryDesc;
    }

    public Boolean isActiveIndicator() {
        return activeIndicator;
    }

    public SystemCodeTable activeIndicator(Boolean activeIndicator) {
        this.activeIndicator = activeIndicator;
        return this;
    }

    public void setActiveIndicator(Boolean activeIndicator) {
        this.activeIndicator = activeIndicator;
    }

    public String getTenantID() {
        return tenantID;
    }

    public SystemCodeTable tenantID(String tenantID) {
        this.tenantID = tenantID;
        return this;
    }

    public void setTenantID(String tenantID) {
        this.tenantID = tenantID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SystemCodeTable systemCodeTable = (SystemCodeTable) o;
        if (systemCodeTable.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, systemCodeTable.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SystemCodeTable{" +
            "id=" + id +
            ", codeTableName='" + codeTableName + "'" +
            ", codeTableDesc='" + codeTableDesc + "'" +
            ", codeTableEntry='" + codeTableEntry + "'" +
            ", codeTableEntryDesc='" + codeTableEntryDesc + "'" +
            ", activeIndicator='" + activeIndicator + "'" +
            ", tenantID='" + tenantID + "'" +
            '}';
    }
}
