package torricelli;

import torricelli.util.Proc;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Kohsuke Kawaguchi
 */
public class HgInvoker {
    private final List<String> commands = new ArrayList<String>();
    private final ProcessBuilder pb = new ProcessBuilder(commands);

    public HgInvoker(File workDir, Object... cmds) {
        commands.add("hg");
        arg(cmds);
        pb.directory(workDir);
    }

    public HgInvoker arg(Object a) {
        commands.add(a.toString());
        return this;
    }

    public HgInvoker arg(Object... args) {
        for (Object arg : args)
            arg(arg);
        return this;
    }

    public Proc launch(OutputStream out) throws IOException {
        pb.redirectErrorStream(true);
        out.write('$');
        for (String command : commands) {
            out.write(' ');
            out.write(command.getBytes());
        }
        return new Proc(pb.start(),out);
    }
}
