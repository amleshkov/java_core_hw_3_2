import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    private static final String BASEDIR = "/home/anonymous/Games/savegames/";
    private static final String ARCHIVE = BASEDIR + "savegames.zip";

    public static void saveGame(String path, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void zipFiles(String archivePath, ArrayList<String> filesList) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(archivePath))) {
            for (String file : filesList) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    ZipEntry zentry = new ZipEntry(file);
                    zout.putNextEntry(zentry);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zout.write(buffer);
                    zout.closeEntry();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (String iterFile : filesList) {
            File file = new File(iterFile);
            try {
                file.deleteOnExit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        GameProgress[] gameProgressList = {
                new GameProgress(50, 4, 2, 42.5),
                new GameProgress(60, 4, 3, 142.5),
                new GameProgress(70, 8, 10, 1042.5)
            };
        int i = 0;
        ArrayList<String> filesList = new ArrayList<>();
        for (GameProgress gp : gameProgressList) {
            String savePath = String.format("%ssave%d.dat", BASEDIR, i);
            saveGame(savePath, gp);
            filesList.add(savePath);
            i++;
        }
        zipFiles(ARCHIVE, filesList);
    }
}