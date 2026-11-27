package com.example.notify.it;

import com.example.notify.dispatch.LeaseReaper;
import com.example.notify.provider.SimulatedProvider;
import com.example.notify.support.AbstractIntegrationTest;
import com.example.notify.support.ApiClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LeaseHeartbeatIT extends AbstractIntegrationTest {

    @Autowired
    LeaseReaper reaper;
    @Autowired @Qualifier("simulatedEmail")
    SimulatedProvider email;

    @Test
    void sendsSlowerThanLeaseIsNotReapedOrSentTwice() throws Exception {
        ApiClient.TenantHandle t = api.newTenant();
        int before = email.sendCount();
        UUID id = api.send(t, "EMAIL", "slow+slow13000@example.com");
        Thread.sleep(11_000);
        reaper.reapOnce();
        assertThat(api.awaitStatus(t, id, "SENT").toString()).doesNotContain("LEASE_EXPIRED");
        assertThat(email.sendCount() - before).as("provider calls").isEqualTo(1);
    }
}
