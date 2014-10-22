package bf.spring;

import static bf.spring.RuntimeEnvironment.currentRuntimeEnvironment;
import static lombok.AccessLevel.PRIVATE;
import lombok.NoArgsConstructor;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author rfanego
 */
@NoArgsConstructor(access = PRIVATE)
public class SpringApplicationContext {
	private static AnnotationConfigApplicationContext ctx;

	public static void initialize() {
		try {
			ctx = new AnnotationConfigApplicationContext();
			ctx.getEnvironment().setActiveProfiles(currentRuntimeEnvironment().name());
			ctx.register(CommonSpringConfiguration.class);
			ctx.register(SpringConfigurationForDevAndTest.class, SpringConfigurationForProd.class);
			ctx.refresh();
			if (ctx == null) {
				throw new IllegalStateException("application context could not be initialized properly");
			}
		}
		catch (Exception e) {
			throw new IllegalStateException("application context could not be initialized properly", e);
		}
	}

	public static <T> T getBean(Class<T> beanClass) {
		return ctx.getBean(beanClass);
	}

	public static <T> T getBeanNamed(String beanName, Class<T> beanClass) {
		return ctx.getBean(beanName, beanClass);
	}

	public static void close() {
		if (ctx != null) {
			ctx.close();
			ctx = null;
		}
	}
}
