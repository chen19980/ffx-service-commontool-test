package tw.com.firstbank.fcbcore.ffx.service.commontool;

import org.apache.commons.lang3.StringUtils;
import org.springframework.test.context.ActiveProfilesResolver;

public class ActiveProfileResolver implements ActiveProfilesResolver {
	private static final String PROFILES_ACTIVE = "spring.profiles.active";
	private static final String SYSTEX = "systex";
	private static final String TEST = "test";

	@Override
	public String[] resolve(final Class<?> aClass) {

		String activeProfile = System.getProperty(PROFILES_ACTIVE);

		if (StringUtils.contains(activeProfile, SYSTEX)) {
			activeProfile = SYSTEX + TEST;
		} else {
			activeProfile = TEST;
		}

		return new String[] {activeProfile};
	}
}
