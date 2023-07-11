package Global;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Anonymous extends ClassLoader {

    private Class<?> loadedClass;

    private Object object;

    public Anonymous(File dotClassFile) throws Exception {
        byte[] b = Files.readAllBytes(dotClassFile.toPath());

        this.loadedClass = defineClass(null, b, 0, b.length);

        this.object = this.loadedClass.getDeclaredConstructor().newInstance();
    }

    public Object call(String methodName, Object... args) throws Exception {
        Class<?>[] types = new Class[args.length];

        for (int i = 0; i < args.length; i++) {
            types[i] = args[i].getClass();
        }

        return this.loadedClass
                .getDeclaredMethod(methodName, types)
                .invoke(this.object, args);
    }

    public Object getObject() {
        return this.object;
    }
}
