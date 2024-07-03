package com.teleo.accounts.domain;

import static com.teleo.accounts.domain.UserExtraTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.teleo.accounts.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserExtraTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserExtra.class);
        UserExtra userExtra1 = getUserExtraSample1();
        UserExtra userExtra2 = new UserExtra();
        assertThat(userExtra1).isNotEqualTo(userExtra2);

        userExtra2.setId(userExtra1.getId());
        assertThat(userExtra1).isEqualTo(userExtra2);

        userExtra2 = getUserExtraSample2();
        assertThat(userExtra1).isNotEqualTo(userExtra2);
    }
}
