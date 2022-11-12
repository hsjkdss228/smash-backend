package kr.megaptera.smash.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostHitsTest {
    @Test
    void equals() {
        assertThat(new PostHits(100L)).isEqualTo(new PostHits(100L));
        assertThat(new PostHits(100L)).isNotEqualTo(new PostHits(2000L));
        assertThat(new PostHits(100L)).isNotEqualTo(null);
        assertThat(new PostHits(100L)).isNotEqualTo(100L);
    }
}
