import com.marklogic.xcc.*;
import com.marklogic.xcc.exceptions.RequestException;
import com.marklogic.xcc.exceptions.XccConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Created by ableasdale on 02/03/17.
 */
public class FindGameContent {

    private static final String STEAM_BASE_DIR = "/home/ableasdale/.local/share/Steam/steamapps/common/";
    private static final Logger LOG = LoggerFactory.getLogger("FindGameContent");
    private static ContentSource cs;
    private static ContentCreateOptions options = null;

    public static void main(String[] args) {


        try {
            cs = ContentSourceFactory.newContentSource(new URI("xcc://q:q@localhost:8000/Documents"));
            LOG.info("ML Connection established: "+ cs.newSession().getCurrentServerPointInTime());
        } catch (XccConfigException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (RequestException e) {
            e.printStackTrace();
        }


        LOG.info("Started");

        try {
            Files.list(Paths.get(STEAM_BASE_DIR))
                    .filter(p -> p.getFileName().toString().contains("Civilization"))
                    .forEach(p -> doDir(p));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void doDir(Path p) {

        LOG.info(p.toString().substring(STEAM_BASE_DIR.length()));

        try (Stream<Path> filePathStream=Files.walk(p)) {
            filePathStream.forEach(filePath -> {
                if (Files.isRegularFile(filePath) && filePath.toString().endsWith("xml")) {
                    Session s = cs.newSession();
                    try {
                        s.insertContent(ContentFactory.newContent(filePath.toString().substring(STEAM_BASE_DIR.length()-1), filePath.toFile(), options));
                    } catch (RequestException e) {
                        e.printStackTrace();
                    }
                    s.close();
                    //LOG.info("       "+filePath.toString().substring(STEAM_BASE_DIR.length()-1));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
