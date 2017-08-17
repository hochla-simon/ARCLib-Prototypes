package cas.lib.arclib.store;

import cas.lib.arclib.domain.QValidationProfile;
import cas.lib.arclib.domain.ValidationProfile;
import cz.inqool.uas.store.DatedStore;
import org.springframework.stereotype.Repository;

@Repository
public class ValidationProfileStore extends DatedStore<ValidationProfile, QValidationProfile> {
    public ValidationProfileStore() {
        super(ValidationProfile.class, QValidationProfile.class);
    }
}
