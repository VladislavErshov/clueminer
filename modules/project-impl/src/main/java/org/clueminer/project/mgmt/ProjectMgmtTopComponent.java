package org.clueminer.project.mgmt;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.awt.BorderLayout;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.clueminer.project.api.Project;
import org.clueminer.project.api.ProjectController;
import org.clueminer.project.api.Workspace;
import org.clueminer.project.api.WorkspaceListener;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//org.clueminer.project.mgmt//ProjectMgmt//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "ProjectMgmtTopComponent",
        iconBase = "org/clueminer/project/impl/mgmt_16.png",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "explorer", openAtStartup = true)
@ActionID(category = "Window", id = "org.clueminer.project.mgmt.ProjectMgmtTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_ProjectMgmtAction",
        preferredID = "ProjectMgmtTopComponent"
)
@Messages({
    "CTL_ProjectMgmtAction=ProjectMgmt",
    "CTL_ProjectMgmtTopComponent=ProjectMgmt Window",
    "HINT_ProjectMgmtTopComponent=This is a ProjectMgmt window"
})
public final class ProjectMgmtTopComponent extends TopComponent implements ExplorerManager.Provider, LookupListener {

    private final BeanTreeView treeView;
    private Lookup.Result<Project> result = null;
    private final ExplorerManager mgr = new ExplorerManager();
    private ProjectsNode root;
    private static final Logger logger = Logger.getLogger(ProjectMgmtTopComponent.class.getName());

    public ProjectMgmtTopComponent() {
        initComponents();
        setName(Bundle.CTL_ProjectMgmtTopComponent());
        setToolTipText(Bundle.HINT_ProjectMgmtTopComponent());

        associateLookup(ExplorerUtils.createLookup(mgr, getActionMap()));

        setLayout(new BorderLayout());
        this.treeView = new BeanTreeView();
        //treeView.setRootVisible(false);
        add(treeView, BorderLayout.CENTER);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        result = Utilities.actionsGlobalContext().lookupResult(Project.class);
        result.addLookupListener(this);
        resultChanged(new LookupEvent(result));

        final ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);

        Workspace workspace = pc.getCurrentWorkspace();
        if (workspace != null) {
            Project p = workspace.getLookup().lookup(Project.class);
            if (p != null) {
                logger.log(Level.WARNING, "got project! = {0}", p.getName());
            }
        }
        pc.addWorkspaceListener(new WorkspaceListener() {

            @Override
            public void initialize(Workspace workspace) {
                //add preview class to lookup

            }

            @Override
            public void select(Workspace workspace) {
                Collection<? extends Project> lp = workspace.getLookup().lookupAll(Project.class);
                updateProjects(pc.getProjects());
            }

            @Override
            public void unselect(Workspace workspace) {

            }

            @Override
            public void close(Workspace workspace) {

            }

            @Override
            public void disable() {

            }

            @Override
            public void projectActivated(Project project) {
                System.out.println("project activated: " + project.getName());
            }
        });

    }

    @Override
    public void componentClosed() {
        result.removeLookupListener(this);
        result = null;
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    private void updateProjects(Collection<? extends Project> allProjects) {
        if (allProjects.size() > 0) {
            root = new ProjectsNode(allProjects);
        }
        mgr.setRootContext(root);
    }

    @Override
    public void resultChanged(LookupEvent le) {
        logger.log(Level.INFO, "project lookup " + le.toString());
        Collection<? extends Project> allProjects = result.allInstances();
        if (allProjects.size() > 0) {
            if (root == null) {
                root = new ProjectsNode(allProjects);
            } else {
                Iterable<? extends Project> prj = Iterables.concat(root.getProjects(), allProjects);
                Collection<? extends Project> collection = Lists.newArrayList(prj);
                root = new ProjectsNode(collection);
            }
            mgr.setRootContext(root);
        }
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return mgr;
    }
}
