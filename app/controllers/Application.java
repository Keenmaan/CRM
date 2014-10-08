package controllers;

import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.index;
import views.html.login;
import views.html.register;

public class Application extends Controller {

    @Security.Authenticated(Secured.class)
    public static Result index() {
        return ok(index.render("index"));
    }

    public static class Login {
        public String name;
        public String password;
        public String validate() {
            if (User.authenticate(name, password) == null) {
                return "Invalid user or password";
            }
            return null;
        }
    }

    public static class RegisterData{
        public String name;
        public String password;
    }

    public static Result registerForm(){
        return ok(register.render(Form.form(RegisterData.class)));
    }

    public static Result register(){
        Form<RegisterData> registerDataForm = Form.form(RegisterData.class).bindFromRequest();
        if (registerDataForm.hasErrors()){
            return badRequest(register.render(registerDataForm));
        }
        else{
            User user=new User();
            user.setName(registerDataForm.get().name);
            user.setPassword(registerDataForm.get().password);
            //session().clear();
            //session("name",registerDataForm.get().name);
            //return redirect(routes.Application.index());
        }
        return ok();
    }


    public static Result logout() {
        session().clear();
        return redirect(routes.Application.login());
    }

    public static Result login() {
        return ok(login.render(Form.form(Login.class)));
    }

    public static Result authenticate(){
        Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            session().clear();
            session("name", loginForm.get().name);
            return redirect(
                    routes.Application.index()
            );
        }
    }
}