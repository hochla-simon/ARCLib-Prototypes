package cas.lib.arclib;

import cas.lib.arclib.droid.DroidFormatIdentifier;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static cz.inqool.uas.helper.ThrowableAssertion.assertThrown;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.exists;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DroidFormatIdentifierTest {

    private static final String SIP_ID = "KPW01169310";
    private static final String WORKSPACE = "workspace";
    private static final Path SIP_SOURCES_FOLDER = Paths.get("../");

    @Autowired
    private DroidFormatIdentifier formatIdentifier;

    @BeforeClass
    public static void initialize() throws IOException {
        copySipToWorkspace(SIP_SOURCES_FOLDER, SIP_ID);
    }

    @AfterClass
    public static void tearDown() throws IOException {
        deleteWorkspace();
    }

    @Before
    public void setUp() {
        formatIdentifier.setWorkspace(WORKSPACE);
    }

    /**
     * Test that all the files in the SIP package are scanned (also the files located in all the subfolders)
     */
    @Test
    public void testThatAllFilesScanned() throws InterruptedException, IOException {
        Map<String, List<String>> result = formatIdentifier.analyze(SIP_ID);

        assertThat(result.size(), is(39));
    }

    /**
     * Test the given files have been identified with the right formats
     */
    @Test
    public void analyzeRightExtensionsIdentifiedTest() throws IOException, InterruptedException {
        Map<String, List<String>> result = formatIdentifier.analyze(SIP_ID);

        Path pathToSip = Paths.get(WORKSPACE).resolve(SIP_ID);

        String filePath1 = pathToSip.resolve("METS_KPW01169310.xml").toAbsolutePath().toString();
        assertThat(result.get(filePath1), contains("text/xml"));

        String filePath2 = pathToSip.resolve("desktop.ini").toAbsolutePath().toString();
        assertThat(result.get(filePath2), contains("Text Configuration file"));

        String filePath3 = pathToSip.resolve("KPW01169310.md5").toAbsolutePath().toString();
        assertThat(result.get(filePath3), contains("MD5 File"));

        String filePath4 = pathToSip.resolve("TXT/TXT_KPW01169310_0002.TXT").toAbsolutePath().toString();
        assertThat(result.get(filePath4), contains("Plain Text File"));
    }

    /**
     * Test the the right exception is thrown when an ID of a nonexistent SIP is provided
     */
    @Test
    public void analyzeNonExistentPackageTest() {
        assertThrown(() -> formatIdentifier.analyze("nonExistentPackage")).isInstanceOf(IOException.class);
    }

    /**
     * Test the the right exception is thrown when an ID of a nonexistent workspace path is provided
     */
    @Test
    public void analyzeNonExistentWorkspaceTest() {
        formatIdentifier.setWorkspace("nonExistentWorkspace");

        assertThrown(() -> formatIdentifier.analyze("KPW01169310")).isInstanceOf(IOException.class);
    }

    private static void copySipToWorkspace(Path path, String sipId) throws IOException {
        if (!exists(Paths.get(WORKSPACE))) {
            createDirectories(Paths.get(WORKSPACE));
        }

        FileSystemUtils.copyRecursively(new File(path.resolve(sipId).toAbsolutePath().toString()),
                new File(Paths.get(WORKSPACE).resolve(sipId).toAbsolutePath().toString()));
    }

    private static void deleteWorkspace() {
        if (exists(Paths.get(WORKSPACE))) {
            FileSystemUtils.deleteRecursively(new File(Paths.get(WORKSPACE).toAbsolutePath().toString()));
        }
    }
}
