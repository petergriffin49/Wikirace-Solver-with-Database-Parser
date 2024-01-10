import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;
import java.util.Stack;

public class LinkTree {

    private static final boolean showErrors = false;

    private static final String path = Paths.getFilePath();
    private static final String pathRedirects = Paths.getFilePathRedirects();

    Node head;  // source link
    Node endNode; // last node in the tree's order currently
    Node nodeCurrent;
    String goal;

    int files = 0;
    int files_failed = 0;

    //
    public static class Node {
        Node parent = null;    // parent of node (null if head)
        String link;    // link the node contains
        Node childRight = null;

        // adds head node
        public Node(String link) {
            this.link = link;
            this.parent = null;
        }

        // adds node
        public Node(String link, Node parent) {
            this.link = link;
            this.parent = parent;
        }

        public String getString(){
            return link;
        }

        public Node getParent(){
            return parent;
        }

    }

    // private methods to assist addChildren()
    private String asciiDecode(String str){
        String[] illegalChars = {"?","*",":","|",">","<","/","\\"};
        String[] illegalReps = {"&quest;","&ast;","&colon;","&verbar;","&gt;","&lt;","&sol;","&bsol;"};

        for(int i = 0; i < illegalChars.length; i ++){
            str = str.replace(illegalChars[i],illegalReps[i]);
        }

        return str;
    }
    private String getFilePath(String pageName, String filePath){
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
        return(filePath+folder+"\\"+folder2+"\\"+pageName+".txt");
    }

    public void addChild(Node myNode, String pagename){
        endNode.childRight = myNode;
        endNode = myNode;
    }

    // adds children as nodes to the tree
    public Node addChildren(String pageName, Node parent, boolean first) {

        Node myNode;
        String line;
        String fileName = getFilePath(pageName,path);
        File myObj = new File(fileName);
        files += 1;
        try { // try and find the article file
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                line = myReader.nextLine();
                try {
                    line = line.substring(0, 1).toUpperCase() + line.substring(1);
                }catch(StringIndexOutOfBoundsException ignored){}

                myNode = new Node(line, parent);
                addChild(myNode,line);

                if (first){
                    line = findFile.isFile(line);
                }

                if (Objects.equals(line, goal)){
                    return (myNode);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            pageName = asciiDecode(pageName); // retry, after changing illegal characters in file title

            fileName = getFilePath(pageName,path);
            myObj = new File(fileName);

            try {
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
                    line = myReader.nextLine();
                    myNode = new Node(line, parent);
                    addChild(myNode,line);
                    if (Objects.equals(line, goal)) {
                        return (myNode);
                    }
                }
                myReader.close();

            } catch (FileNotFoundException er) {
                fileName = getFilePath(pageName,pathRedirects); // retry, first find the redirect file name
                myObj = new File(fileName);

                try {
                    Scanner myReader = new Scanner(myObj);
                    while (myReader.hasNextLine()) {
                        pageName = myReader.nextLine();
                    }
                    myReader.close();

                    pageName = asciiDecode(pageName);
                    fileName = getFilePath(pageName,path); // then try to find the file it redirects to
                    myObj = new File(fileName);
                    try {
                        myReader = new Scanner(myObj);
                        // new fix (does not seem to work)
                        if (Objects.equals(pageName, goal)) {
                            return (parent);
                        }

                        while (myReader.hasNextLine()) {
                            line = myReader.nextLine();
                            myNode = new Node(line, parent);
                            addChild(myNode,line);
                            if (Objects.equals(line, goal)) {
                                return (myNode);
                            }
                        }
                        myReader.close();

                    } catch (FileNotFoundException err) { // failure to find redirect file
                        if (showErrors){System.out.println("-Error: file path '" + fileName + "' was not found-");}
                        files_failed++;
                    }

                } catch (FileNotFoundException err) { // failure to find file
                    if (showErrors){System.out.println("-Error: redirect file '" + fileName + "' was not found-");}
                    files_failed++;
                }
            }
        }

        return null;
    }
    
    // creates link-tree
    public LinkTree(String headStr,String goalStr) {
        this.head = new Node(headStr);
        nodeCurrent = this.head;
        endNode = this.head;
        goal = goalStr;
    }

    // gets next page link to search from tree
    public Node getNextLink() {
        Node nodeTemp = nodeCurrent;
        nodeCurrent = nodeCurrent.childRight;
        return nodeTemp;
    }

    // returns the final path as a string array
    public String[] getPathArray(Node foundNode){
        String path = "";
        Stack<String> stack = new Stack<String>();

        while (true){
            stack.push(foundNode.getString());

            if ((foundNode.getParent() == null)){
                break;
            }

            foundNode = foundNode.getParent();
        }

        String[] path_arr = new String[stack.size()];
        int i = 0;
        while(!stack.isEmpty()) {
            path_arr[i] = stack.pop();
            i += 1;
        }
        return path_arr;
    }

    // retrieves the file search rate of failure
    public Float getErrorRate() {
        return ((float) files_failed / (float) files);
    }
    
}

