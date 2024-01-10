public class Paths {

    // change 'filePath' to wherever the wikipedia database is stored on your computer
    private static String filePath =
            "C:\\Users\\capta\\Downloads\\Wikirace Database\\";




    private static String filePathRedirects = filePath + "redirects\\";
    public static String getFilePath(){
        return filePath;
    }
    public static String getFilePathRedirects(){
        return filePathRedirects;
    }
}
