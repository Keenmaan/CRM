package controllers;

import models.User;
import play.Logger;
import play.data.Form;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import views.html.profile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static play.mvc.Controller.flash;
import static play.mvc.Controller.request;
import static play.mvc.Results.ok;
import static play.mvc.Results.redirect;

/**
 * Created by keen on 10/15/14.
 */
public class Profile {

    @Security.Authenticated(Secured.class)
    public static Result profile(){

        return ok(profile.render(Form.form(User.class)));
    }

    public static Result getImage(){
        //Image image =
        return ok();
    }

    public static Result upload() {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart picture = body.getFile("picture");
        if (picture != null) {
            String fileName = picture.getFilename();
            File file = picture.getFile();
            Path currentRelativePath = Paths.get("");
            String relativePath = currentRelativePath.toAbsolutePath().toString();
            Path source=Paths.get(file.getAbsolutePath());
            Path target=Paths.get(relativePath,"data/images/");
            Path targetVerify=Paths.get(target.toString(),fileName);
            target = imageNameVerify(targetVerify,0);
            try {
                Files.copy(source,
                        target, StandardCopyOption.REPLACE_EXISTING);
                Files.deleteIfExists(source);
                Logger.info("Picture "+fileName+" has been moved from "+source+" to "+target);
            }
            catch (IOException e){
                Logger.info("Problems with IO.");
                e.printStackTrace();
            }
            User user = Users.getCurrentUser();
            if (user!=null)
                user.image=target.toString();
            Logger.info("user.image="+user.image);
            return ok(profile.render(Form.form(User.class)));
        } else {
            flash("error", "Missing file");
            return redirect(routes.Application.index());
        }
    }

    /*  imageNameVerify(Path,int)
    Returns a Path destination including the file name.
    Checks if there is a file with such a name, if there is, adds +i to the end of the name.
    Example: path="image.jpg" returns path/image.jpg
    if image.jpg exists at destination, then it will return path/image1.jpg etc.
    */
    public static Path imageNameVerify(Path path,int i){
        if (i==0){
            if(Files.exists(Paths.get(path.toString()))){
                i++;
                return imageNameVerify(path,i);
            }
            else
                return Paths.get(path.toString());
        }
        else
        {
            String extension=path.toString();
            path=Paths.get(removeExtension(path.toString()));
            if (extension.equals(path.toString())) {
                extension="";
            }
            else{
                extension=getExtension(extension);
            }
            if(Files.exists(Paths.get(String.join("",path.toString(),String.valueOf(i),extension)))){
                i++;
                path=Paths.get(path.toString(),extension);
                return imageNameVerify(path,i);
            }
            else{
                return Paths.get(String.join("",path.toString(),String.valueOf(i),extension));
            }
        }
    }

    public static String removeExtension(String s) {

        String separator = System.getProperty("file.separator");
        String filename;

        // Remove the path upto the filename.
        int lastSeparatorIndex = s.lastIndexOf(separator);
        if (lastSeparatorIndex == -1) {
            filename = s;
        } else {
            filename = s.substring(lastSeparatorIndex + 1);
        }

        // Remove the extension.
        int extensionIndex = filename.lastIndexOf(".");
        if (extensionIndex == -1)
            return s;

        return s.substring(0, lastSeparatorIndex+1)
                + filename.substring(0, extensionIndex);
    }

    public static String getExtension(String s){
        return s.substring(s.lastIndexOf("."));
    }
}
