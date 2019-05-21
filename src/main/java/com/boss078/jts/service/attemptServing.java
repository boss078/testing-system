package com.boss078.jts.service;

import com.boss078.jts.entities.Attempt;
import com.boss078.jts.repositories.AttemptRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.tools.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class attemptServing {
    @Autowired
    private AttemptRepository attemptRepository;

    /*Returns error code: 0 - compilation and run successful, all tests passed;
    1 - compilation error; 2 - at least one test failed; 3 - class "Main" not found;
    4 - method "main" not found; 5 - class "Main" is not public; 6 - InvocationTargetException.*/
    public Attempt compileAndRun(Attempt attempt){
        BufferedWriter output = null;
        JavaFileObject compilationUnit =
                new attemptServing.StringJavaFileObject("Main", attempt.getCode());
        boolean buildResult = false;
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        attemptServing.SimpleJavaFileManager fileManager =
                new attemptServing.SimpleJavaFileManager(compiler.getStandardFileManager(null, null, null));
        StringWriter out = new StringWriter();
        PrintWriter outWriter = new PrintWriter(out);
        try {
            buildResult = compiler.getTask(outWriter, fileManager,
                    null, null, null, Arrays.asList(compilationUnit)).call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (buildResult) {
            attempt.addLog("Build successful.\n");

            attemptServing.CompiledClassLoader classLoader =
                    new attemptServing.CompiledClassLoader(fileManager.getGeneratedOutputFiles());
            boolean testsAreOk = true;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                Class<?> testClass = classLoader.loadClass("Main");
                attempt.addLog("Class loaded successfully.\n");
                Method main = testClass.getMethod("main", String[].class);
                attempt.addLog("Method loaded successfully.\n");

                for (int i = 0; i < attempt.getTask().getTests().size(); i++) {
                    String input = attempt.getTask().getTests().get(i).getIn();
                    PrintStream ps = new PrintStream(baos);
                    InputStream is = new ByteArrayInputStream(input.getBytes(Charset.forName("UTF-8")));

                    // IMPORTANT: Save the old System.out!
                    PrintStream oldOut = System.out;
                    // IMPORTANT: Save the old System.in!
                    InputStream oldIn = System.in;

                    System.setIn(is);
                    System.setOut(ps);

                    main.invoke(null, new Object[]{null});
                    System.out.flush();
                    System.setOut(oldOut);
                    System.setIn(oldIn);
                    if (!baos.toString().equals(attempt.getTask().getTests().get(i).getOut())) {
                        attempt.addLog("Test " + i + " failed.");
                        testsAreOk = false;
                    }
                    else {
                        attempt.addLog("Test " + i + " is successful.");
                    }
                }

            } catch(ClassNotFoundException e) {
                attempt.addLog("ClassNotFoundException: " + e.getStackTrace().toString() + "\n");
                attempt.setErrorCode(3);
                return attempt;
            }
            catch(NoSuchMethodException e) {
                attempt.addLog("NoSuchMethodException: " + e.getStackTrace().toString() + "\n");
                attempt.setErrorCode(4);
                return attempt;
            }
            catch(IllegalAccessException e) {
                attempt.addLog("IllegalAccessException: " + e.getStackTrace().toString() + "\n");
                attempt.setErrorCode(5);
                return attempt;
            }
            catch(InvocationTargetException e) {
                attempt.addLog("InvocationTargetException: " + e.getStackTrace().toString() + "\n");
                attempt.setErrorCode(6);
                return attempt;
            }

            if (testsAreOk) {
                attempt.addLog("All tests passed.\n");
                attempt.setErrorCode(0);
                return attempt;
            }
            else {
                attempt.addLog("At least one test failed. Check log above.\n");
                attempt.setErrorCode(2);
                return attempt;
            }
        }
        else {
            attempt.addLog("Build failed. Compiler message: " + out.toString() + "\n");
            attempt.setErrorCode(1);
            return attempt;
        }
    }
    private static class StringJavaFileObject extends SimpleJavaFileObject {
        private final String code;

        public StringJavaFileObject(String name, String code) {
            super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension),
                    Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            return code;
        }
    }

    private static class ClassJavaFileObject extends SimpleJavaFileObject {
        private final ByteArrayOutputStream outputStream;
        private final String className;

        protected ClassJavaFileObject(String className, Kind kind) {
            super(URI.create("mem:///" + className.replace('.', '/') + kind.extension), kind);
            this.className = className;
            outputStream = new ByteArrayOutputStream();
        }

        @Override
        public OutputStream openOutputStream() throws IOException {
            return outputStream;
        }

        public byte[] getBytes() {
            return outputStream.toByteArray();
        }

        public String getClassName() {
            return className;
        }
    }

    private static class SimpleJavaFileManager extends ForwardingJavaFileManager {
        private final List<attemptServing.ClassJavaFileObject> outputFiles;

        protected SimpleJavaFileManager(JavaFileManager fileManager) {
            super(fileManager);
            outputFiles = new ArrayList<attemptServing.ClassJavaFileObject>();
        }

        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
            attemptServing.ClassJavaFileObject file = new attemptServing.ClassJavaFileObject(className, kind);
            outputFiles.add(file);
            return file;
        }

        public List<attemptServing.ClassJavaFileObject> getGeneratedOutputFiles() {
            return outputFiles;
        }
    }

    private static class CompiledClassLoader extends ClassLoader {
        private final List<attemptServing.ClassJavaFileObject> files;

        private CompiledClassLoader(List<attemptServing.ClassJavaFileObject> files) {
            this.files = files;
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            Iterator<attemptServing.ClassJavaFileObject> itr = files.iterator();
            while (itr.hasNext()) {
                attemptServing.ClassJavaFileObject file = itr.next();
                if (file.getClassName().equals(name)) {
                    itr.remove();
                    byte[] bytes = file.getBytes();
                    return super.defineClass(name, bytes, 0, bytes.length);
                }
            }
            return super.findClass(name);
        }
    }
}
