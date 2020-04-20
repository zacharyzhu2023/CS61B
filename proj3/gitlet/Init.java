package gitlet;
import java.io.File;
import java.io.Serializable;

public class Init implements Serializable{
    private File gitletDirectory;
    private File stagingArea;
    private File objectArea;
    static Stage stage;
    public Init() {
        if (!hasGitletRepository()) {
            gitletDirectory = new File(".gitlet");
            gitletDirectory.mkdir();
            stagingArea = Utils.join(gitletDirectory, "stage");
            stagingArea.mkdir();
            objectArea = Utils.join(gitletDirectory, "objects");
            objectArea.mkdir();
            // FIXME?? Do more directories need to be added?

            Commit initial = new Commit();
            Branch master = new Branch();
            Branch.currentBranch = master;
            stage = new Stage();
            // FIXME
        } else {
            System.out.println(" A Gitlet version-control system already exists in the current directory.");
        }
    }
    private boolean hasGitletRepository() {
        File testDirectory = new File(".gitlet");
        if (testDirectory.exists()) {
            return true;
        }
        return false;
    }
}
