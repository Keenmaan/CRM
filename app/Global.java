import play.Application;
import play.GlobalSettings;
import play.Logger;
import controllers.Users;

public class Global extends GlobalSettings {

    public void onStart(Application app) {
        Logger.info("Application has started.");
        Logger.info("Looking for admin account...");
        if (!Users.findAdmin()){
            String name="admin";
            String password="admin";
            Users.createUser(
                    name,
                    password,
                    true);
            Logger.info("No admin user found." +name+"/"+password+" user created.");
        }
        else
            Logger.info("Admin found.");
    }

    public void onStop(Application app) {
        Logger.info("Application shutdown...");
    }

}