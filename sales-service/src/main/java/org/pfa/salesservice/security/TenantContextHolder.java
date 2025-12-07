package org.pfa.salesservice.security;

import lombok.extern.slf4j.Slf4j;

/**
 * Stores the current tenant ID for the current thread.
 */
@Slf4j
public class TenantContextHolder {

    private static final ThreadLocal<Long> TENANT_ID = new ThreadLocal<>();

    public static void setTenantId(Long tenantId) {
        if (tenantId == null) {
            log.warn("Attempting to set null tenantId in TenantContext");
            return;
        }
        TENANT_ID.set(tenantId);
        log.debug("Tenant context set: tenantId={}", tenantId);
    }

    public static Long getTenantId() {
        return TENANT_ID.get();
    }

    public static Long getRequiredTenantId() {
        Long tenantId = TENANT_ID.get();
        if (tenantId == null) {
            throw new IllegalStateException("Tenant context is not set for this request");
        }
        return tenantId;
    }

    public static void clear() {
        TENANT_ID.remove();
    }
}
