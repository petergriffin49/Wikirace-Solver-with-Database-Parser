import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class findFile {

    private static final String pathName = Paths.getFilePath();
    private static final String pathNameRedirects = Paths.getFilePathRedirects();

    public static String isFile(String pageName){

        Scanner myReader;
        String path = getFilePath(pageName,pathName);

        File f = new File(path);
        try {
            myReader = new Scanner(f);
            return pageName;
        }catch(FileNotFoundException e){
            pageName = asciiDecode(pageName);
            path = getFilePath(pageName,pathName);
            f = new File(path);
            try {
                myReader = new Scanner(f);
                return pageName;
            }catch(FileNotFoundException e2){

                path = getFilePath(pageName,pathNameRedirects);
                f = new File(path);
                if(f.exists() && !f.isDirectory()) {
                    try {
                        myReader = new Scanner(f);
                        pageName = myReader.nextLine();
                        pageName = asciiDecode(pageName);
                        path = getFilePath(pageName, pathName);
                        f = new File(path);
                        if (f.exists() && !f.isDirectory()) {
                            return pageName;
                        }
                    } catch (FileNotFoundException er) {
                        return "";
                    }
                }
            }
        }
        return "";
    }
    public static String asciiDecode(String str){
        String[] illegalChars = {"?","*",":","|",">","<","/","\\"};
        String[] illegalReps = {"&quest;","&ast;","&colon;","&verbar;","&gt;","&lt;","&sol;","&bsol;"};

        for(int i = 0; i < illegalChars.length; i ++){
            str = str.replace(illegalChars[i],illegalReps[i]);
        }

        return str;
    }
    public static String getFilePath(String pageName, String path){
        Character charSort = pageName.charAt(0);
        Character charSort2 = ' ';
        if (pageName.length() >= 2) {
            charSort2 = pageName.charAt(1);
        }
        String folder = Character.toString(Character.toUpperCase(charSort));
        String folder2 = Character.toString(Character.toLowerCase(charSort2));
        int charMax = 123;
        int charVal = Character.getNumericValue(charSort);
        int charVal2 = Character.getNumericValue(charSort2);
        if (!(Character.isLetterOrDigit(charSort)) || (charVal >= charMax) || (charVal < 0)){
            folder = "other";
        }
        if (!(Character.isLetterOrDigit(charSort2)) || (charVal2 >= charMax) || (charVal2 < 0)){
            folder2 = "other";
        }

        pageName = pageName.substring(0, 1).toUpperCase() + pageName.substring(1);
        return(path+folder+"\\"+folder2+"\\"+pageName+".txt");
    }

}
