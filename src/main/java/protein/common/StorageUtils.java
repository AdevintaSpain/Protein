package protein.common;

import com.bugsnag.Bugsnag;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.squareup.kotlinpoet.FileSpec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.ListModel;
import javax.swing.DefaultListModel;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class StorageUtils {

    @NotNull
    public static ListModel getFoldersList() {
        DefaultListModel listModel = new DefaultListModel();
        Project currentProject = (Project)DataManager.getInstance().getDataContext().getData(DataConstants.PROJECT);
        VirtualFile[] contentRoots = ProjectRootManager.getInstance(currentProject).getContentRoots();
        for (VirtualFile virtualFile : contentRoots) {
            if (virtualFile.isDirectory() && virtualFile.isWritable()) {
                listModel.addElement(virtualFile.getName());
            }
        }

        VirtualFile[] contentRootsFromAllModules = ProjectRootManager.getInstance(currentProject).getContentRootsFromAllModules()[0].getChildren();
        for (VirtualFile virtualFile : contentRootsFromAllModules) {
            if (virtualFile.isDirectory() && virtualFile.isWritable()) {
                listModel.addElement(virtualFile.getName());
            }
        }
        return listModel;
    }

    public static void generateFiles(@Nullable String moduleName, @Nullable String packageName, @NotNull com.squareup.kotlinpoet.TypeSpec classTypeSpec) {
        FileSpec kotlinFile = FileSpec.builder(packageName, classTypeSpec.getName()).addType(classTypeSpec).build();

        try {
            String projectPath;
            if (moduleName != null && !"".equals(moduleName)) {
                projectPath = FileEditorManager.getInstance(ProjectManager.getInstance().getOpenProjects()[0]).getProject().getBasePath() + "/" + moduleName + "/src/main/java/";
            } else {
                projectPath = FileEditorManager.getInstance(ProjectManager.getInstance().getOpenProjects()[0]).getProject().getBasePath() + "/src/main/java/";
            }
            Path path = FileSystems.getDefault().getPath(projectPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            kotlinFile.writeTo(path);
            kotlinFile.writeTo(System.out);
        } catch (IOException e) {
            e.printStackTrace();
            trackError(e);
        }
    }

    public static String generateString(@Nullable String packageName, @NotNull com.squareup.kotlinpoet.TypeSpec classTypeSpec) {
        return FileSpec.builder(packageName, classTypeSpec.getName()).addType(classTypeSpec).build().toString();
    }

    public static String toFirstUpperCase(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    public static String toFirstLowerCase(String text) {
        return text.substring(0, 1).toLowerCase() + text.substring(1);
    }

    private static void trackError(IOException e) {
        Bugsnag bugSnag = new Bugsnag("a33c387e14e810eace96242d7382737d");
        bugSnag.notify(e);
    }
}
