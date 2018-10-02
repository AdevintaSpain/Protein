package protein.common;


import com.bugsnag.Bugsnag;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SettingsManager implements Serializable {
    private static SettingsManager instance = null;
    private static String DEFAULT_FILE_NAME = "ProteinSettings.sav";
    private String savedFilePath;
    private List<Settings> settingsList;
    private Bugsnag bugSnag = new Bugsnag("a33c387e14e810eace96242d7382737d");

    private SettingsManager() {
        VirtualFile[] contentRoots = ProjectRootManager.getInstance(ProjectManager.getInstance().getOpenProjects()[0]).getContentRoots();
        this.savedFilePath = contentRoots[0].getPath() + "/" + DEFAULT_FILE_NAME;
        this.settingsList = new ArrayList<Settings>();
    }

    public static SettingsManager getInstance() {
        if (instance == null) {
            instance = new SettingsManager();
        }

        return instance;
    }

    public void add(Settings settings) {
        settingsList.add(settings);
    }

    public List<Settings> getSettingsList() {
        return settingsList;
    }

    public void setSettingsList(List<Settings> settingsList) {
        this.settingsList = settingsList;
    }

    public void save() {
        ObjectOutputStream objectOutputStream = null;
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(savedFilePath, true);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(settingsList);
        } catch (Exception e) {
            bugSnag.notify(e);
        } finally {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    bugSnag.notify(e);
                }
            }
        }
    }

    public void load() {
        ObjectInputStream objectinputstream = null;
        try {
            if (new File(savedFilePath).exists()) {
                FileInputStream streamIn = new FileInputStream(savedFilePath);
                objectinputstream = new ObjectInputStream(streamIn);
                settingsList = (List<Settings>) objectinputstream.readObject();
            }
        } catch (Exception e) {
            bugSnag.notify(e);
        } finally {
            if (objectinputstream != null) {
                try {
                    objectinputstream.close();
                } catch (IOException e) {
                    bugSnag.notify(e);
                }
            }
        }
    }
}
