package kr.megaptera.smash.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberNameTest {
    @Test
    void equals() {
        assertThat(new MemberName("김인우")).isEqualTo(new MemberName("김인우"));
        assertThat(new MemberName("김인우")).isNotEqualTo(new MemberName("황인우"));
        assertThat(new MemberName("김인우")).isNotEqualTo(null);
        assertThat(new MemberName("김인우")).isNotEqualTo("김인우");
    }
}
