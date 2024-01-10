public class Wikirace{
    /* 
        using database in algorithm:
        try and find title page file,
        if not found then try replacing illegal chars like [que], 
        then search again
        if still not found continue
    */

    private static final boolean debugPrint = true;

    public static String[] run(String pageBegin, String pageEnd){
        long start = System.currentTimeMillis();

        String pageCurrent;
        boolean loop = true;
        String[] path = new String[0];

        LinkTree javaTree = new LinkTree(pageBegin,pageEnd);
        LinkTree.Node nodeCheck = javaTree.addChildren(pageBegin, javaTree.head, true);
        if (nodeCheck != null){
            loop = false;
            path = javaTree.getPathArray(nodeCheck);
        }
        LinkTree.Node node;
        LinkTree.Node nodeGoal;

        javaTree.getNextLink(); // move past root
        int i = 0;

        while (loop){ // begin search

            node = javaTree.getNextLink();
            pageCurrent = node.getString();

            nodeGoal = javaTree.addChildren(pageCurrent, node, false);

            if (nodeGoal != null){
                path = javaTree.getPathArray(nodeGoal);
                loop = false;
                break;
            }

            // prints progress when taking a long time
            i ++;
            if (i % 10000 == 0){
                System.out.println(i);
            }
        }

        // debug output
        long finish = System.currentTimeMillis();
        if (debugPrint) {
            System.out.println();
            System.out.println((finish - start) / 1000 + " seconds elapsed");
            System.out.println("Error rate: " + (javaTree.getErrorRate() * 100) + " %");
        }
        //
        return (path);
    }

}
