import play.Application;
import play.GlobalSettings;
import play.Logger;
import bf.spring.SpringApplicationContext;

/**
 * @author rfanego
 */
public class Global extends GlobalSettings {
	
	@Override
	public void onStart(Application app) {
		super.onStart(app);
		Logger.info("Starting up the application...");
		SpringApplicationContext.initialize();
	}
	
	@Override
	public <C> C getControllerInstance(Class<C> clazz) {
		return SpringApplicationContext.getBean(clazz);
	}
}
