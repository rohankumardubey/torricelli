package torricelli;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import groovy.util.XmlParser;
import groovy.util.Node;

import javax.xml.parsers.ParserConfigurationException;
import javax.servlet.ServletException;

/**
 * Mercurial repository.
 *
 * @author Kohsuke Kawaguchi
 */
public class Repository {
    public final String name;

    /**
     * Root directory of the repository.
     */
    public final File home;

    /**
     * Currently running, or last completed task.
     */
    protected volatile TaskThread task;

    public Repository(File home) throws IOException {
        this.name = home.getName();
        this.home = home;
    }

    public TaskThread getTask() {
        return task;
    }

    public void startTask(TaskThread t) {
        t.start();
        this.task = t;
    }

    /**
     * Delegate the processing to "hg serve".
     *
     * This is mostly for helping development by looking at the backend raw output.
     */
    public void doDynamic(StaplerRequest req, StaplerResponse rsp) throws IOException {
        getRunner().proxy(req, rsp);
    }

    public void doIndex(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException {
        TaskThread t = task;
        if(t!=null && t.isProminent()) {
            req.getView(this,"executingProminentTask").forward(req,rsp);
        } else {
            // the default action is to forward to "hg serve"
            getRunner().proxy(req, rsp);
        }
    }

    /**
     * Sends the request to the backend "hg serve" and parses the result into
     * the format suitable for processing by Groovy.
     */
    public Node parse(String relative) throws SAXException, ParserConfigurationException, IOException {
        return getRunner().parse('/'+name+relative);
    }

    protected final HgServeRunner getRunner() {
        return Torricelli.INSTANCE.getRunner();
    }
}
