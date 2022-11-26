package kr.megaptera.smash.validations;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

@GroupSequence(value = {
    Default.class, NotBlankGroup.class, PatternMatchGroup.class
})
public interface ValidationSequence {

}
