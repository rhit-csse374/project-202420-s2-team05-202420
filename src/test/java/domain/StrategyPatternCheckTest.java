package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import datasource.Configuration;
import datasource.DirLoader;
import datasource.FilesLoader;
import domain.javadata.ClassData;
import domain.javadata.ClassReaderUtil;

public class StrategyPatternCheckTest {

    private static final String[] CLASS_DIR_PATHS = new String[] {"src/test/resources/StrategyPatternTest1",
            "src/test/resources/StrategyPatternTest2"};

    private static final Configuration CONFIG_EMPTY = new Configuration(Map.of());

    private ArrayList<Map<String, ClassData>> classes = new ArrayList<Map<String, ClassData>>();

    @BeforeEach
    public void setup() throws IOException {
        for (String CLASS_DIR_PATH : CLASS_DIR_PATHS) {
            FilesLoader loader = new DirLoader(CLASS_DIR_PATH);
            Set<byte[]> bytecodes = loader.loadFiles("class");
            HashMap<String, ClassData> item = new HashMap<String, ClassData>();
            for (byte[] bytecode : bytecodes) {
                ClassData classData = ClassReaderUtil.read(bytecode);
                item.put(classData.getFullName(), classData);
            }
            this.classes.add(item);

        }
    }

    @Test
    public void strategyPatternCheckTest1 () {
        Check check = new StrategyPatternCheck();
        Set<Message> result = check.run(this.classes.get(0), CONFIG_EMPTY);
        //System.out.println(result.toString());
        assertEquals("[[INFO] Strategy Pattern Detected!]",result.toString());

    }

    @Test
    public void strategyPatternCheckTest2 () {
        Check check = new StrategyPatternCheck();
        Set<Message> result = check.run(this.classes.get(1), CONFIG_EMPTY);
        //System.out.println(result.toString());
        assertEquals("[[INFO] No Strategy Pattern Detected!]",result.toString());

    }
}
