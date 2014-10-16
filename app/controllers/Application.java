package controllers;

import models.User;
import play.Logger;
import play.data.Form;
import play.data.validation.ValidationError;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.index;
import views.html.login;
import views.html.logout;
import views.html.register;

import java.util.ArrayList;
import java.util.List;

public class Application extends Controller {

    @Security.Authenticated(Secured.class)
    public static Result index() {
        String username=session("name");
        User user = User.find.where().eq("name", session("name")).findUnique();
        if (user.getIsAdmin()) {
            return ok(index.render("You are logged in as " + username + "."));
        }
        else
            return ok(index.render("You are logged in as " + username + "."));
    }

    @Security.Authenticated(Secured.class)
    public static Result usersList(){
        return Users.userList();
    }

    public static Result registerForm(){
        return ok(register.render(Form.form(User.class)));
    }

    public static Result register(){
        Form<User> form = Form.form(User.class).bindFromRequest();
        List<ValidationError> errors = new ArrayList<>();
        if (form.hasErrors()){
            return badRequest(register.render(form));
        }
        //Safe validation:
        //Make sure there is no such username.
        if (Users.findName(form.get().name)){
            errors.add(new ValidationError(
                    "name",
                    "User already exists.",
                    new ArrayList<>()
            ));
            form.errors().put("name",errors);
        }
        //Check if proper password.
        if (form.hasErrors()){
            Logger.info("Errors:"+errors);
            return badRequest(register.render(form));
        }
        else
            Users.createUser(
                form.get().name,
                form.get().password,
                false);
        return redirect(routes.Application.loginForm());
    }

    public static Result logout() {
        session().clear();
        return ok(logout.render());
    }

    public static Result loginForm() {
        Form<User> form = Form.form(User.class);
        return ok(login.render(form));
    }

    public static Result login(){
        Form<User> form = Form.form(User.class).bindFromRequest();
        List<ValidationError> errors = new ArrayList<>();
        if (form.hasErrors()) {
            form.reject("You have entered incorrect data.");
            return badRequest(login.render(form));
        } else {
            String name=form.get().name;
            String password=form.get().password;
            if (User.authenticate(name, password) == null) {
                errors.add(new ValidationError(
                        "name",
                        "Wrong password or user name."));
                form.errors().put("global",errors
                );
                return badRequest(login.render(form));
            }
            session().clear();
            session("name", form.get().name);
            return redirect(
                    routes.Application.index()
            );
        }
    }


}